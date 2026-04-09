package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ NotificationsActivity (ЭКРАН УВЕДОМЛЕНИЙ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * NotificationsActivity является экраном уведомлений в приложении Fitness App.
 * Сюда пользователь попадает при нажатии на иконку колокольчика на главном экране.
 * Этот экран отображает все важные сообщения, напоминания и новости приложения:
 * советы по безопасности, напоминания о тренировках, информация о новых функциях.
 *
 * Экран позволяет пользователю просматривать уведомления, удалять их по одному
 * или удалить все сразу. Уведомления представлены в виде карточек (CardView)
 * с заголовком, текстом сообщения и датой получения.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель (Toolbar):
 * - Кнопка "Назад" (backButton) — возврат на предыдущий экран
 * - Кнопка меню (btnMenu) — открывает выпадающее меню с дополнительными действиями
 * - Заголовок "Уведомления" (отображается через Toolbar)
 *
 * Выпадающее меню (dropdownMenu):
 * - Пункт "Удалить все уведомления" (menuDeleteAll) — удаляет все уведомления
 *
 * Основная область (notificationsContainer):
 * - Карточки уведомлений (CardView), каждая содержит:
 *   - Заголовок (жирный шрифт, чёрный цвет)
 *   - Текст сообщения (серый цвет, с увеличенным межстрочным интервалом)
 *   - Дата получения (мелкий шрифт, серый цвет)
 *
 * Состояние "Пусто":
 * - Если уведомлений нет, отображается текст "Уведомлений нет" по центру экрана
 *
 *
 * ЧАСТЬ 3. ТЕСТОВЫЕ УВЕДОМЛЕНИЯ (ДЕТАЛЬНО)
 * -----------------------------------------
 *
 * Для демонстрации работы экрана при первом запуске создаются три тестовых
 * уведомления через метод initializeNotifications():
 *
 * УВЕДОМЛЕНИЕ №1 (Безопасность):
 * - Заголовок: "Тренеры никогда не спрашивают пароли"
 * - Текст: "Никому не сообщайте данные для входа в аккаунт — ими интересуются
 *          только мошенники"
 * - Дата: "14 ноября, 13:15"
 *
 * УВЕДОМЛЕНИЕ №2 (Напоминания):
 * - Заголовок: "Настройте напоминания о тренировках"
 * - Текст: "Экономьте время и не пропускайте тренировки — настроив напоминания,
 *          вы будете получать уведомления перед началом занятий. Чтобы настроить,
 *          перейдите в раздел Настройки → Уведомления."
 * - Дата: "14 ноября, 10:30"
 *
 * УВЕДОМЛЕНИЕ №3 (Новый контент):
 * - Заголовок: "Новая тренировка доступна"
 * - Текст: "Добавлена новая кардио-тренировка для начинающих. Попробуйте её
 *          в разделе Тренировки → Кардио."
 * - Дата: "13 ноября, 15:45"
 *
 *
 * ЧАСТЬ 4. УПРАВЛЕНИЕ УВЕДОМЛЕНИЯМИ (ДЕТАЛЬНО)
 * ---------------------------------------------
 *
 * 4.1. Отображение уведомлений (displayNotifications):
 *
 * Метод очищает контейнер notificationsContainer и проходит по списку
 * notificationItems. Для каждого видимого уведомления (isVisible() == true)
 * вызывается addNotificationCard(), который создаёт карточку и добавляет
 * её в контейнер.
 *
 * 4.2. Создание карточки уведомления (addNotificationCard):
 *
 * Каждое уведомление оформляется как CardView (материальный дизайн):
 * - Белый фон
 * - Тень (elevation = 2dp)
 * - Скруглённые углы (radius = 8dp)
 * - Отступы между карточками (16dp)
 *
 * Внутри карточки вертикальный LinearLayout с содержимым:
 * - Заголовок: жирный шрифт, размер 16sp, чёрный цвет
 * - Текст: размер 14sp, серый цвет, межстрочный интервал 4dp
 * - Дата: размер 12sp, серый цвет
 *
 * 4.3. Удаление отдельных уведомлений (долгое нажатие):
 *
 * При долгом нажатии (onLongClickListener) на карточку уведомления открывается
 * диалоговое окно с подтверждением:
 * - Заголовок: "Удалить уведомление?"
 * - Сообщение: "Уведомление будет удалено."
 * - Кнопка "Удалить" — помечает уведомление как невидимое и обновляет список
 * - Кнопка "Отмена" — закрывает диалог без удаления
 *
 * 4.4. Удаление всех уведомлений (выпадающее меню):
 *
 * При нажатии на пункт "Удалить все уведомления" открывается диалог:
 * - Заголовок: "Удалить все уведомления?"
 * - Сообщение: "Все уведомления будут удалены. Это действие нельзя отменить."
 * - Кнопка "Удалить" — все уведомления помечаются как невидимые
 * - Кнопка "Отмена" — закрывает диалог
 *
 *
 * ЧАСТЬ 5. ВЫПАДАЮЩЕЕ МЕНЮ (ДЕТАЛЬНО)
 * ------------------------------------
 *
 * 5.1. Открытие/закрытие меню:
 *
 * Меню управляется флагом isMenuVisible:
 * - При нажатии на btnMenu вызывается toggleMenu()
 * - Если меню скрыто — показывается (showMenu)
 * - Если меню видно — скрывается (hideMenu)
 *
 * 5.2. Закрытие меню при клике вне его:
 *
 * В setupOutsideClickListener() устанавливается слушатель на корневую View
 * (android.R.id.content). При клике в любом месте экрана, если меню открыто,
 * оно закрывается. При этом клик по самому меню не вызывает его закрытия.
 *
 * 5.3. Закрытие меню кнопкой "Назад":
 *
 * В методе onBackPressed() проверяется: если меню открыто — оно закрывается,
 * иначе выполняется стандартный выход с анимацией.
 *
 *
 * ЧАСТЬ 6. НАВИГАЦИЯ И АНИМАЦИИ
 * ------------------------------
 *
 * 6.1. Возврат на главный экран:
 *
 * Кнопка "Назад" (backButton) вызывает onBackPressed(), который закрывает
 * текущую активность с анимацией slide_in_left и slide_out_right.
 *
 * 6.2. Анимации переходов:
 *
 * При нажатии системной кнопки "Назад" используется:
 * - slide_in_left — новая активность въезжает слева
 * - slide_out_right — текущая активность уезжает вправо
 *
 * Это создаёт эффект "возврата" к предыдущему экрану.
 *
 *
 * ЧАСТЬ 7. ПУСТОЕ СОСТОЯНИЕ (EMPTY STATE)
 * ----------------------------------------
 *
 * Если после удаления всех уведомлений контейнер notificationsContainer
 * не содержит дочерних View, вызывается метод showEmptyState():
 *
 * - Создаётся TextView с текстом "Уведомлений нет"
 * - Размер шрифта: 18sp
 * - Цвет: серый (darker_gray)
 * - Выравнивание: по центру
 * - Отступ сверху: 100dp для визуального центрирования
 *
 *
 * ЧАСТЬ 8. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * --------------------------------------------
 *
 * onCreate() — инициализация активности: поиск View, загрузка уведомлений,
 *              настройка Toolbar, меню и слушателей
 *
 * initializeNotifications() — создание трёх тестовых уведомлений и вызов
 *                             displayNotifications() для их отображения
 *
 * displayNotifications() — очистка контейнера и отображение всех видимых
 *                          уведомлений
 *
 * addNotificationCard() — создание карточки уведомления с заголовком,
 *                         текстом, датой и слушателем долгого нажатия
 *
 * showEmptyState() — отображение сообщения "Уведомлений нет" при пустом списке
 *
 * setupToolbar() — настройка панели инструментов и кнопки "Назад"
 *
 * setupMenuListeners() — настройка кнопки меню и пункта "Удалить все"
 *
 * setupOutsideClickListener() — закрытие меню при клике вне его
 *
 * toggleMenu() — переключение состояния меню (открыть/закрыть)
 *
 * showMenu() — отображение выпадающего меню
 *
 * hideMenu() — скрытие выпадающего меню
 *
 * deleteAllNotifications() — диалог подтверждения удаления всех уведомлений
 *
 * showDeleteSingleDialog() — диалог подтверждения удаления одного уведомления
 *
 * dpToPx() — конвертация dp в пиксели (для корректного отображения на разных
 *            экранах)
 *
 * onBackPressed() — обработка кнопки "Назад" (закрытие меню или активности)
 *
 *
 * ЧАСТЬ 9. МОДЕЛЬ ДАННЫХ (NotificationItem)
 * ------------------------------------------
 *
 * Каждое уведомление представлено классом NotificationItem с полями:
 * - title (String) — заголовок уведомления
 * - message (String) — текст уведомления
 * - date (String) — дата и время получения уведомления
 * - isVisible (boolean) — флаг, отображать ли уведомление (по умолчанию true)
 *
 * При удалении уведомления флаг isVisible устанавливается в false, но объект
 * остаётся в списке. Это позволяет в будущем реализовать "Корзину" или
 * восстановление удалённых уведомлений.
 *
 *
 * ЧАСТЬ 10. ВЕРСТКА И СТИЛИ (ДЕТАЛЬНО)
 * -------------------------------------
 *
 * Карточки уведомлений создаются программно (динамически), а не через XML.
 * Это сделано для гибкости и удобства добавления новых полей в будущем.
 *
 * Параметры карточки:
 * - CardElevation: 2dp (лёгкая тень)
 * - Radius: 8dp (скруглённые углы)
 * - BackgroundColor: белый
 *
 * Отступы:
 * - Между карточками: 16dp
 * - Внутренние отступы карточки: 16dp со всех сторон
 * - Отступ между заголовком и текстом: 8dp
 * - Отступ между текстом и датой: 12dp
 *
 * Типографика:
 * - Заголовок: bold, 16sp, чёрный
 * - Текст: normal, 14sp, darker_gray
 * - Дата: normal, 12sp, darker_gray
 * - Межстрочный интервал текста: 4dp (для лучшей читаемости)
 *
 *
 * ЧАСТЬ 11. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Статические тестовые данные:
 *    - Сейчас уведомления создаются в коде, а не загружаются извне
 *    - Нужно подключить загрузку уведомлений из Firebase или API
 *
 * 2. Нет отметки о прочтении:
 *    - Нет визуального отличия между прочитанными и непрочитанными
 *    - Нужно добавить статус "прочитано" и счётчик непрочитанных
 *
 * 3. Нет уведомлений в реальном времени:
 *    - Уведомления не приходят в реальном времени через FCM
 *    - Нужно подключить Firebase Cloud Messaging
 *
 * 4. Нет восстановления удалённых:
 *    - Удалённые уведомления исчезают навсегда
 *    - Нужно добавить "Корзину" или отмену удаления (Snackbar с "Отменить")
 *
 * 5. Нет группировки по датам:
 *    - Все уведомления идут подряд без разделителей
 *    - Нужно добавить заголовки с датами ("Сегодня", "Вчера", "Ранее")
 *
 * 6. Нет пагинации:
 *    - При большом количестве уведомлений список может тормозить
 *    - Нужно добавить постраничную загрузку
 *
 * 7. Нет обновления в реальном времени:
 *    - При добавлении нового уведомления экран не обновляется автоматически
 *    - Нужно реализовать механизм обновления (например, через EventBus)
 *
 *
 * ЧАСТЬ 12. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - CardView (для оформления уведомлений в виде карточек)
 * - Material Design компоненты (Toolbar, LinearLayout и др.)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 13. ИТОГИ
 * ----------------
 *
 * NotificationsActivity — это полноценный экран для отображения и управления
 * уведомлениями в приложении Fitness App. В нём реализованы: отображение
 * уведомлений в виде карточек, удаление отдельных уведомлений (долгим нажатием),
 * удаление всех уведомлений (через выпадающее меню), пустое состояние,
 * выпадающее меню с дополнительными действиями, анимации переходов и
 * корректная обработка кнопки "Назад" (с закрытием меню при необходимости).
 *
 * =============================================================================
 */



import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private LinearLayout dropdownMenu;
    private boolean isMenuVisible = false;
    private LinearLayout notificationsContainer;
    private List<NotificationItem> notificationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        btnMenu = findViewById(R.id.btnMenu);
        dropdownMenu = findViewById(R.id.dropdownMenu);
        notificationsContainer = findViewById(R.id.notificationsContainer);

        // Инициализируем уведомления
        initializeNotifications();

        setupToolbar();
        setupMenuListeners();
        setupOutsideClickListener();
    }

    private void initializeNotifications() {
        // Добавляем тестовые уведомления
        notificationItems.add(new NotificationItem(
                "Тренеры никогда не спрашивают пароли",
                "Никому не сообщайте данные для входа в аккаунт — ими интересуются только мошенники",
                "14 ноября, 13:15"
        ));

        notificationItems.add(new NotificationItem(
                "Настройте напоминания о тренировках",
                "Экономьте время и не пропускайте тренировки — настроив напоминания, вы будете получать уведомления перед началом занятий. Чтобы настроить, перейдите в раздел Настройки → Уведомления.",
                "14 ноября, 10:30"
        ));

        notificationItems.add(new NotificationItem(
                "Новая тренировка доступна",
                "Добавлена новая кардио-тренировка для начинающих. Попробуйте её в разделе Тренировки → Кардио.",
                "13 ноября, 15:45"
        ));

        // Отображаем уведомления
        displayNotifications();
    }

    private void displayNotifications() {
        notificationsContainer.removeAllViews();

        for (int i = 0; i < notificationItems.size(); i++) {
            NotificationItem item = notificationItems.get(i);
            if (item.isVisible()) {
                addNotificationCard(item, i);
            }
        }

        // Если все уведомления удалены, показываем сообщение
        if (notificationsContainer.getChildCount() == 0) {
            showEmptyState();
        }
    }

    private void addNotificationCard(NotificationItem item, int index) {
        CardView cardView = new CardView(this);
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(16));
        cardView.setLayoutParams(cardParams);
        cardView.setCardBackgroundColor(getColor(android.R.color.white));
        cardView.setCardElevation(dpToPx(2));
        cardView.setRadius(dpToPx(8));

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        // Заголовок
        TextView titleText = new TextView(this);
        titleText.setText(item.getTitle());
        titleText.setTextSize(16);
        titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
        titleText.setTextColor(getColor(android.R.color.black));
        titleText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) titleText.getLayoutParams()).bottomMargin = dpToPx(8);

        // Сообщение
        TextView messageText = new TextView(this);
        messageText.setText(item.getMessage());
        messageText.setTextSize(14);
        messageText.setTextColor(getColor(android.R.color.darker_gray));
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) messageText.getLayoutParams()).bottomMargin = dpToPx(12);
        messageText.setLineSpacing(dpToPx(4), 1);

        // Дата
        TextView dateText = new TextView(this);
        dateText.setText(item.getDate());
        dateText.setTextSize(12);
        dateText.setTextColor(getColor(android.R.color.darker_gray));
        dateText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        cardContent.addView(titleText);
        cardContent.addView(messageText);
        cardContent.addView(dateText);
        cardView.addView(cardContent);

        // Добавляем возможность удалять отдельные уведомления по долгому нажатию
        cardView.setOnLongClickListener(v -> {
            showDeleteSingleDialog(index);
            return true;
        });

        notificationsContainer.addView(cardView);
    }

    private void showEmptyState() {
        TextView emptyText = new TextView(this);
        emptyText.setText("Уведомлений нет");
        emptyText.setTextSize(18);
        emptyText.setTextColor(getColor(android.R.color.darker_gray));
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) emptyText.getLayoutParams()).topMargin = dpToPx(100);

        notificationsContainer.addView(emptyText);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupMenuListeners() {
        btnMenu.setOnClickListener(v -> toggleMenu());

        TextView menuDeleteAll = findViewById(R.id.menuDeleteAll);
        menuDeleteAll.setOnClickListener(v -> {
            deleteAllNotifications();
            hideMenu();
        });
    }

    private void setupOutsideClickListener() {
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(v -> {
            if (isMenuVisible) {
                hideMenu();
            }
        });

        dropdownMenu.setOnClickListener(v -> {
            // Ничего не делаем, чтобы меню не закрывалось при клике на него
        });
    }

    private void toggleMenu() {
        if (isMenuVisible) {
            hideMenu();
        } else {
            showMenu();
        }
    }

    private void showMenu() {
        dropdownMenu.setVisibility(View.VISIBLE);
        isMenuVisible = true;
    }

    private void hideMenu() {
        dropdownMenu.setVisibility(View.GONE);
        isMenuVisible = false;
    }

    private void deleteAllNotifications() {
        if (notificationItems.isEmpty()) {
            Toast.makeText(this, "Уведомлений нет", Toast.LENGTH_SHORT).show();
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Удалить все уведомления?")
                .setMessage("Все уведомления будут удалены. Это действие нельзя отменить.")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    // Помечаем все уведомления как невидимые
                    for (NotificationItem item : notificationItems) {
                        item.setVisible(false);
                    }

                    // Обновляем отображение
                    displayNotifications();

                    Toast.makeText(this, "Все уведомления удалены", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showDeleteSingleDialog(int index) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Удалить уведомление?")
                .setMessage("Уведомление будет удалено.")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    notificationItems.get(index).setVisible(false);
                    displayNotifications();
                    Toast.makeText(this, "Уведомление удалено", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (isMenuVisible) {
            hideMenu();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}