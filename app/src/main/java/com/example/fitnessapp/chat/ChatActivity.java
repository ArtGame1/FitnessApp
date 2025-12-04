package com.example.fitnessapp.chat;

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