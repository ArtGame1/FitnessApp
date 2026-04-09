package com.example.fitnessapp.chat;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ ChatActivity (ЭКРАН ЧАТА)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * ChatActivity является экраном чата в приложении Fitness App.
 * Сюда пользователь попадает при нажатии на кнопку "Чат" на главном экране.
 *
 * Этот экран позволяет пользователям:
 * - Обмениваться сообщениями в реальном времени через Firebase
 * - Отправлять текстовые сообщения с поддержкой эмодзи
 * - Выбирать эмодзи из панели смайликов (8xN сетка)
 * - Просматривать историю сообщений всех пользователей
 * - Видеть имя отправителя, текст сообщения и время отправки
 *
 * Чат работает без авторизации — все пользователи отправляют сообщения
 * от имени "Пользователь". Это демо-версия для тестирования функционала.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель:
 * - Заголовок "Чат" (через Toolbar)
 * - Статус-бар синего цвета
 *
 * Основная область:
 * - ListView (listOfMessages) — отображает все сообщения в виде списка
 * - Каждое сообщение содержит: имя пользователя, текст, время отправки
 *
 * Нижняя панель (ввод сообщения):
 * - EmojiEditText (textField) — поле ввода текста с поддержкой эмодзи
 * - Кнопка эмодзи (emojiBtn) — открывает/закрывает панель выбора смайликов
 * - Кнопка отправки (submitBtn) — отправляет сообщение в чат
 *
 * Выпадающая панель эмодзи (emojiContainer):
 * - RecyclerView (emojiRecyclerView) — сетка эмодзи (8 колонок)
 * - При клике на эмодзи он вставляется в поле ввода
 *
 *
 * ЧАСТЬ 3. FIREBASE СТРУКТУРА ДАННЫХ (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * 3.1. База данных:
 *
 * Используется Firebase Realtime Database.
 * Сообщения хранятся по пути "messages" с автоматическими ключами.
 *
 * 3.2. Структура сообщения (класс Message):
 *
 * - userName (String) — имя отправителя (всегда "Пользователь")
 * - textMessage (String) — текст сообщения
 * - messageTime (long) — timestamp времени отправки
 * - timestamp (long) — дополнительное поле для сортировки
 *
 * 3.3. Пример данных в Firebase:
 *
 * {
 *   "messages": {
 *     "-Nx7Y8z9A1b2C3d4E5f6": {
 *       "userName": "Пользователь",
 *       "textMessage": "Всем привет!",
 *       "messageTime": 1734567890123,
 *       "timestamp": 1734567890123
 *     },
 *     "-Nx7Z0a1B2c3D4e5F6g7": {
 *       "userName": "Пользователь",
 *       "textMessage": "Как дела? 😊",
 *       "messageTime": 1734567890456,
 *       "timestamp": 1734567890456
 *     }
 *   }
 * }
 *
 *
 * ЧАСТЬ 4. ОТОБРАЖЕНИЕ СООБЩЕНИЙ (ДЕТАЛЬНО)
 * ------------------------------------------
 *
 * 4.1. FirebaseListAdapter:
 *
 * Используется библиотека firebase-ui-database для удобной привязки
 * данных из Firebase к ListView.
 *
 * 4.2. Настройка адаптера (displayAllMessages):
 *
 * - Создаётся запрос к базе данных: DatabaseReference("messages")
 * - Сортировка по полю "timestamp" для хронологического порядка
 * - Устанавливается макет для каждого элемента: R.layout.list_item
 * - Привязывается к жизненному циклу активности (setLifecycleOwner)
 *
 * 4.3. Заполнение элементов (populateView):
 *
 * Для каждого сообщения:
 * - messageUser.setText(model.getUserName()) — имя отправителя
 * - messageText.setText(model.getTextMessage()) — текст сообщения
 * - messageTime.setText(formatTime(model.getMessageTime())) — время
 *
 * 4.4. Автоматическое обновление:
 *
 * adapter.startListening() в onStart() — начинает прослушивание
 * adapter.stopListening() в onStop() — останавливает прослушивание
 *
 * При добавлении нового сообщения в Firebase, ListView обновляется
 * автоматически в реальном времени.
 *
 *
 * ЧАСТЬ 5. ОТПРАВКА СООБЩЕНИЙ (ДЕТАЛЬНО)
 * ----------------------------------------
 *
 * 5.1. Метод sendMessage():
 *
 * 1. Получает текст из emojiEditText и удаляет пробелы по краям
 * 2. Проверяет, что текст не пустой
 * 3. Создаёт объект Message с именем "Пользователь" и текстом
 * 4. Сохраняет сообщение в Firebase по пути "messages" с автоматическим ключом
 * 5. Очищает поле ввода
 * 6. Скрывает панель эмодзи (если открыта)
 * 7. Прокручивает список к последнему сообщению
 *
 * 5.2. Автоматический ключ:
 *
 * FirebaseDatabase.getInstance().getReference("messages").push()
 * - push() генерирует уникальный ключ (например, "-Nx7Y8z9A1b2C3d4E5f6")
 * - Это гарантирует уникальность каждого сообщения и сохранение порядка
 *
 *
 * ЧАСТЬ 6. ПАНЕЛЬ ЭМОДЗИ (ДЕТАЛЬНО)
 * ----------------------------------
 *
 * 6.1. Структура:
 *
 * - emojiContainer (FrameLayout) — контейнер, скрыт по умолчанию
 * - emojiRecyclerView — отображает список эмодзи в сетке 8 колонок
 * - EmojiAdapter — адаптер для отображения эмодзи
 * - EmojiAdapter.EmojiClickListener — интерфейс для обработки кликов
 *
 * 6.2. Открытие/закрытие:
 *
 * toggleEmojiPicker() — переключает видимость
 * showEmojiPicker() — показывает панель, меняет иконку на клавиатуру
 * hideEmojiPicker() — скрывает панель, возвращает иконку смайлика
 *
 * 6.3. Выбор эмодзи (onEmojiClick):
 *
 * При клике на эмодзи:
 * - Определяется позиция курсора в поле ввода
 * - Если есть выделенный текст, он заменяется на эмодзи
 * - Если нет выделения, эмодзи добавляется в конец
 *
 * 6.4. Автоматическое скрытие:
 *
 * - При вводе текста (TextWatcher) панель автоматически скрывается
 * - При нажатии на список сообщений панель скрывается
 * - При отправке сообщения панель скрывается
 * - При нажатии кнопки "Назад" сначала скрывается панель
 *
 *
 * ЧАСТЬ 7. EMOJIEDITEXT (ДЕТАЛЬНО)
 * ---------------------------------
 *
 * Используется androidx.emoji2.widget.EmojiEditText — специальное поле ввода,
 * которое корректно отображает эмодзи на всех версиях Android.
 *
 * Преимущества:
 * - Автоматическая поддержка современных эмодзи
 * - Корректное отображение на Android 4.4+
 * - Обработка ввода с клавиатуры
 *
 *
 * ЧАСТЬ 8. ФОРМАТИРОВАНИЕ ВРЕМЕНИ
 * --------------------------------
 *
 * Время сообщения форматируется с помощью:
 * android.text.format.DateFormat.format("HH:mm", model.getMessageTime())
 *
 * Пример вывода: "14:30", "09:15", "23:45"
 *
 *
 * ЧАСТЬ 9. ОФОРМЛЕНИЕ И СТИЛИ
 * ----------------------------
 *
 * 9.1. Статус-бар:
 *
 * Для Android 5.0 (Lollipop) и выше устанавливается синий цвет статус-бара.
 * Цвет берётся из ресурсов: R.color.menuColor.
 *
 * 9.2. Иконки:
 *
 * - ic_keyboard — иконка клавиатуры (когда панель эмодзи открыта)
 * - smile — иконка смайлика (когда панель эмодзи закрыта)
 *
 *
 * ЧАСТЬ 10. ЖИЗНЕННЫЙ ЦИКЛ АКТИВНОСТИ (ДЕТАЛЬНО)
 * ------------------------------------------------
 *
 * 10.1. onCreate():
 *
 * - Установка макета и цвета статус-бара
 * - Инициализация всех UI-элементов
 * - Настройка панели эмодзи
 * - Настройка адаптера для отображения сообщений
 * - Установка обработчиков для кнопок и TextWatcher
 *
 * 10.2. onStart():
 *
 * - Запускает прослушивание Firebase (adapter.startListening())
 * - Сообщения начинают обновляться в реальном времени
 *
 * 10.3. onStop():
 *
 * - Останавливает прослушивание Firebase (adapter.stopListening())
 * - Экономит трафик и ресурсы устройства
 *
 * 10.4. onBackPressed():
 *
 * - Если панель эмодзи открыта — скрывает её
 * - Иначе — закрывает активность
 *
 *
 * ЧАСТЬ 11. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * ---------------------------------------------
 *
 * onCreate() — инициализация активности: установка макета, цвета статус-бара,
 *              инициализация UI, настройка панели эмодзи, загрузка сообщений
 *
 * setupEmojiPicker() — настройка RecyclerView для отображения сетки эмодзи
 *
 * toggleEmojiPicker() — переключение видимости панели эмодзи
 *
 * showEmojiPicker() — отображение панели эмодзи
 *
 * hideEmojiPicker() — скрытие панели эмодзи
 *
 * onEmojiClick() — вставка выбранного эмодзи в поле ввода
 *
 * sendMessage() — отправка сообщения в Firebase
 *
 * displayAllMessages() — настройка FirebaseListAdapter для отображения сообщений
 *
 * onStart() — начало прослушивания Firebase
 *
 * onStop() — остановка прослушивания Firebase
 *
 * onBackPressed() — обработка кнопки "Назад" (скрытие панели или выход)
 *
 *
 * ЧАСТЬ 12. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - Firebase Realtime Database (хранение сообщений)
 * - Firebase UI Database (адаптер для ListView)
 * - AndroidX Emoji2 (EmojiEditText для поддержки эмодзи)
 * - AndroidX RecyclerView (сетка эмодзи)
 *
 * Вспомогательные классы:
 * - Message — модель сообщения
 * - EmojiAdapter — адаптер для списка эмодзи
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 13. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Нет авторизации:
 *    - Все сообщения отправляются от имени "Пользователь"
 *    - Нужно добавить Firebase Authentication и отображать реальные имена
 *
 * 2. Нет личных сообщений:
 *    - Все сообщения видны всем пользователям
 *    - Нужно добавить личные чаты между пользователями
 *
 * 3. Нет уведомлений:
 *    - При новых сообщениях нет push-уведомлений
 *    - Нужно добавить Firebase Cloud Messaging
 *
 * 4. Нет возможности редактировать/удалять сообщения:
 *    - Сообщения нельзя изменить или удалить после отправки
 *    - Нужно добавить функционал модерации
 *
 * 5. Нет загрузки изображений:
 *    - Можно отправлять только текст и эмодзи
 *    - Нужно добавить отправку фото и видео
 *
 * 6. Нет индикатора набора текста:
 *    - Не видно, печатает ли кто-то сообщение
 *    - Нужно добавить "Пользователь печатает..."
 *
 * 7. Нет смайликов последней версии:
 *    - Список эмодзи ограничен (то, что в EmojiAdapter)
 *    - Нужно добавить все современные эмодзи
 *
 * 8. Нет поиска по сообщениям:
 *    - Нельзя найти нужное сообщение в истории
 *    - Нужно добавить строку поиска
 *
 *
 * ЧАСТЬ 14. ПРИМЕР ИСПОЛЬЗОВАНИЯ
 * -------------------------------
 *
 * Пользователь заходит в чат:
 * 1. Видит историю всех предыдущих сообщений
 * 2. Нажимает на поле ввода, пишет "Привет всем!"
 * 3. Нажимает кнопку эмодзи, выбирает смайлик 😊
 * 4. Нажимает кнопку отправки
 * 5. Сообщение "Привет всем! 😊" появляется в списке
 * 6. Все другие пользователи видят это сообщение в реальном времени
 *
 *
 * ЧАСТЬ 15. ИТОГИ
 * ----------------
 *
 * ChatActivity — это полноценный чат с поддержкой эмодзи и обменом сообщениями
 * в реальном времени через Firebase Realtime Database.
 *
 * Реализованы следующие функции:
 * - Отправка и получение сообщений в реальном времени
 * - Панель выбора эмодзи с сеткой 8 колонок
 * - Вставка эмодзи в поле ввода с поддержкой выделения текста
 * - Отображение имени отправителя, текста и времени сообщения
 * - Автоматическая прокрутка к последнему сообщению
 * - Скрытие панели эмодзи при вводе текста, отправке, нажатии на список
 * - Корректная обработка кнопки "Назад"
 * - Изменение цвета статус-бара
 *
 * Чат работает в реальном времени: новые сообщения появляются у всех
 * пользователей мгновенно без необходимости обновлять экран.
 *
 * =============================================================================
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.emoji2.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

//Основной класс активности чата, реализующий интерфейс для обработки кликов по эмодзи
public class ChatActivity extends AppCompatActivity implements EmojiAdapter.EmojiClickListener {

    //Переменные для UI элементов и адаптеров
    private ListView listOfMessages; //Список для отображения сообщений
    private RelativeLayout activity_chat; //Основной контейнер активности
    private FirebaseListAdapter<Message> adapter; //Адаптер для сообщений из Firebase
    private EmojiEditText emojiEditText; //Поле ввода текста с поддержкой эмодзи
    private ImageView emojiBtn, submitBtn; //Кнопки для эмодзи и отправки
    private FrameLayout emojiContainer; //Контейнер для выбора эмодзи
    private RecyclerView emojiRecyclerView; //RecyclerView для отображения эмодзи
    private boolean isEmojiPickerVisible = false; //Флаг видимости панели эмодзи
    private EmojiAdapter emojiAdapter; //Адаптер для списка эмодзи

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat); //Установка макета активности

        //Изменение цвета статус-бара на синий для Android 5.0+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        //Инициализация UI элементов
        activity_chat = findViewById(R.id.activity_chat);
        submitBtn = findViewById(R.id.submitBtn);
        emojiBtn = findViewById(R.id.emojiBtn);
        emojiEditText = findViewById(R.id.textField);
        listOfMessages = findViewById(R.id.listOfMessages);
        emojiContainer = findViewById(R.id.emojiContainer);
        emojiRecyclerView = findViewById(R.id.emojiRecyclerView);

        setupEmojiPicker(); //Настройка панели выбора эмодзи

        //Приветственное сообщение без проверки авторизации
        Toast.makeText(this, "Добро пожаловать в чат!", Toast.LENGTH_SHORT).show();

        displayAllMessages(); // Загрузка и отображение всех сообщений

        //Обработчик нажатия кнопки отправки сообщения
        submitBtn.setOnClickListener(v -> sendMessage());

        //Обработчик нажатия кнопки эмодзи
        emojiBtn.setOnClickListener(v -> toggleEmojiPicker());

        //Скрытие панели эмодзи при вводе текста
        emojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Автоматическое скрытие панели эмодзи при вводе текста
                if (isEmojiPickerVisible) {
                    hideEmojiPicker();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Скрытие панели эмодзи при нажатии на список сообщений
        listOfMessages.setOnTouchListener((v, event) -> {
            if (isEmojiPickerVisible) {
                hideEmojiPicker();
                return true;
            }
            return false;
        });
    }

    //Настройка RecyclerView для отображения эмодзи
    private void setupEmojiPicker() {
        emojiAdapter = new EmojiAdapter(this, this); //Создание адаптера с передачей контекста и слушателя
        emojiRecyclerView.setLayoutManager(new GridLayoutManager(this, 8)); //Установка сетки 8 колонок
        emojiRecyclerView.setAdapter(emojiAdapter); //Назначение адаптера

        emojiContainer.setVisibility(View.GONE); //Скрытие контейнера по умолчанию
    }

    //Переключение видимости панели эмодзи
    private void toggleEmojiPicker() {
        if (isEmojiPickerVisible) {
            hideEmojiPicker();
        } else {
            showEmojiPicker();
        }
    }

    //Показать панель эмодзи
    private void showEmojiPicker() {
        isEmojiPickerVisible = true;
        emojiContainer.setVisibility(View.VISIBLE);
        emojiBtn.setImageResource(R.drawable.ic_keyboard); //Смена иконки на клавиатуру
        emojiContainer.bringToFront(); //Поднятие контейнера на передний план

        emojiRecyclerView.scrollToPosition(0); //Прокрутка к началу списка эмодзи
    }

    //Скрыть панель эмодзи
    private void hideEmojiPicker() {
        isEmojiPickerVisible = false;
        emojiContainer.setVisibility(View.GONE);
        emojiBtn.setImageResource(R.drawable.smile); //Возврат иконки смайлика

        emojiEditText.requestFocus(); //Установка фокуса на поле ввода
    }

    //Обработка клика по эмодзи (реализация интерфейса)
    @Override
    public void onEmojiClick(String emoji) {
        //Вставка выбранного эмодзи в поле ввода
        int start = emojiEditText.getSelectionStart();
        int end = emojiEditText.getSelectionEnd();

        if (start < 0) {
            //Если нет выделения, добавляем в конец
            emojiEditText.append(emoji);
        } else {
            //Замена выделенного текста
            emojiEditText.getText().replace(
                    Math.min(start, end),
                    Math.max(start, end),
                    emoji,
                    0,
                    emoji.length()
            );
        }

        //Прокрутка к последнему эмодзи в списке (опционально)
        emojiRecyclerView.scrollToPosition(emojiAdapter.getItemCount() - 1);
    }

    //Отправка сообщения
    private void sendMessage() {
        String messageText = emojiEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            //Создание объекта сообщения с фиксированным именем "Пользователь"
            Message message = new Message("Пользователь", messageText);
            //Сохранение в Firebase с автоматическим ключом
            FirebaseDatabase.getInstance().getReference("messages")
                    .push()
                    .setValue(message);

            emojiEditText.setText(""); //Очистка поля ввода

            //Скрытие панели эмодзи после отправки
            if (isEmojiPickerVisible) {
                hideEmojiPicker();
            }

            //Автоматическая прокрутка к последнему сообщению
            listOfMessages.post(() -> listOfMessages.smoothScrollToPosition(adapter.getCount() - 1));
        }
    }

    //Загрузка и отображение всех сообщений из Firebase
    private void displayAllMessages() {
        //Создание запроса к базе данных с сортировкой по времени
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("messages");
        Query query = databaseRef.orderByChild("timestamp");

        //Настройка опций для FirebaseListAdapter
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.list_item) //Макет для каждого элемента
                .setLifecycleOwner(this) //Привязка к жизненному циклу активности
                .build();

        //Создание адаптера для ListView
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                //Привязка данных сообщения к элементам макета
                androidx.appcompat.widget.AppCompatTextView messageText = v.findViewById(R.id.message_text);
                androidx.appcompat.widget.AppCompatTextView messageUser = v.findViewById(R.id.message_user);
                androidx.appcompat.widget.AppCompatTextView messageTime = v.findViewById(R.id.message_time);

                messageUser.setText(model.getUserName());
                messageText.setText(model.getTextMessage());

                //Форматирование времени сообщения
                if (model.getMessageTime() != null) {
                    messageTime.setText(android.text.format.DateFormat.format("HH:mm",
                            model.getMessageTime()));
                }
            }
        };

        listOfMessages.setAdapter(adapter); //Установка адаптера в ListView
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening(); //Начало прослушивания изменений в Firebase
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening(); //Остановка прослушивания Firebase
        }
    }

    @Override
    public void onBackPressed() {
        //Обработка кнопки "Назад": сначала скрыть панель эмодзи, если открыта
        if (isEmojiPickerVisible) {
            hideEmojiPicker();
        } else {
            super.onBackPressed();
        }
    }
}