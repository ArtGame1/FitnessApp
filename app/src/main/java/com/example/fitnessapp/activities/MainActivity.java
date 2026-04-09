package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ MainActivity (ГЛАВНЫЙ ЭКРАН ПРИЛОЖЕНИЯ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * MainActivity является главным экраном приложения Fitness App.
 * Сюда пользователь попадает после успешной авторизации. Этот экран выполняет
 * роль "домашней страницы", откуда можно перейти в любой раздел приложения:
 * тренировки, профиль, настройки, статистику, а также воспользоваться
 * дополнительными функциями: поиск, уведомления, смена темы и другие.
 *
 * Экран объединяет в себе несколько навигационных элементов:
 * - Нижняя панель навигации (BottomNavigationView) для быстрых переходов
 * - Боковое выдвижное меню (DrawerLayout + NavigationView) для дополнительных функций
 * - Верхняя панель инструментов (Toolbar) с кнопками поиска, уведомлений и меню
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель (Toolbar):
 * - Кнопка-иконка (nav_button) для открытия бокового меню
 * - Кнопка уведомлений (notifications_btn) — переход в раздел уведомлений
 * - Кнопка поиска (search_btn) — открывает диалог поиска тренировок
 *
 * Центральная область:
 * - Кнопка "Тренировки" (workoutsButton) — переход в WorkoutActivity
 * - Кнопка "Профиль" (profileButton) — переход в ProfileActivity
 * - Кнопка "Настройки" (settingsButton) — переход в UserManagementActivity
 *
 * Нижняя панель навигации (BottomNavigationView):
 * - Домой (nav_home) — остаётся на MainActivity
 * - Тренировки (nav_workouts) — переход в WorkoutActivity
 * - Профиль (nav_profile) — переход в LoginActivity
 * - Настройки (nav_settings) — переход в SettingsActivity
 * - О приложении (nav_about) — показывает диалоговое окно с информацией
 *
 * Боковое меню (NavigationView):
 * - Мой профиль (nav_my_profile) — переход в AccountActivity
 * - Тренировки (nav_workouts) — переход в WorkoutActivity
 * - Статистика (nav_statistics) — переход в StatsActivity
 * - Настройки (nav_settings) — переход в SettingsActivity
 * - Ночной режим (nav_night_mode) — переключение темы
 * - О приложении (nav_about) — диалоговое окно
 * - Кнопка "Добавить аккаунт" (add_account_btn) — в заголовке меню
 *
 *
 * ЧАСТЬ 3. НАВИГАЦИЯ И ПЕРЕХОДЫ (ДЕТАЛЬНО)
 * ------------------------------------------
 *
 * 3.1. Нижняя панель навигации (BottomNavigationView):
 *
 * При нажатии на любой пункт нижнего меню создаётся Intent для перехода
 * на соответствующую активность. Исключение — пункт "О приложении",
 * который показывает диалоговое окно без перехода.
 *
 * Особенность: используется overridePendingTransition(0,0), чтобы отключить
 * стандартную анимацию перехода между экранами. Это делает переключение
 * мгновенным и более отзывчивым.
 *
 * 3.2. Боковое меню (NavigationDrawer):
 *
 * Открывается при нажатии на кнопку-гамбургер (nav_button) или свайпом
 * от левого края экрана. При выборе пункта меню выполняется соответствующее
 * действие, после чего меню автоматически закрывается.
 *
 * Обработка кнопки "Назад": если боковое меню открыто, при нажатии "Назад"
 * оно закрывается, а не закрывается сама активность.
 *
 * 3.3. Кнопки в центральной области:
 *
 * - Тренировки → WorkoutActivity
 * - Профиль → ProfileActivity
 * - Настройки → UserManagementActivity
 *
 *
 * ЧАСТЬ 4. СИСТЕМА УВЕДОМЛЕНИЙ (ДЕТАЛЬНО)
 * -----------------------------------------
 *
 * 4.1. Канал уведомлений:
 *
 * Для Android 8.0 (Oreo) и выше создаётся канал "fitness_app_channel"
 * с важностью IMPORTANCE_DEFAULT. Это обязательное требование для
 * корректного отображения уведомлений на новых версиях Android.
 *
 * 4.2. Разрешения (Android 13+):
 *
 * На Android 13 (Tiramisu) и выше требуется явное разрешение
 * POST_NOTIFICATIONS. При первом запуске приложение запрашивает
 * это разрешение у пользователя.
 *
 * 4.3. Ежедневные напоминания:
 *
 * Приложение планирует ежедневные напоминания через NotificationHelper.
 * Если в настройках пользователя уведомления включены, каждый день
 * в определённое время приходит напоминание о тренировке.
 *
 * 4.4. Кнопка уведомлений:
 *
 * При нажатии на иконку колокольчика (notifications_btn) открывается
 * NotificationsActivity, где пользователь может посмотреть все свои
 * уведомления.
 *
 *
 * ЧАСТЬ 5. ПОИСК ТРЕНИРОВОК (ДЕТАЛЬНО)
 * -------------------------------------
 *
 * 5.1. Открытие диалога поиска:
 *
 * При нажатии на иконку лупы (search_btn) появляется диалоговое окно
 * с полем ввода. Клавиатура автоматически открывается, фокус ставится
 * на поле ввода.
 *
 * 5.2. Ввод запроса:
 *
 * Пользователь вводит название тренировки. Поддерживается нажатие
 * кнопки "Найти" в диалоге или клавиши "Enter" на клавиатуре.
 *
 * 5.3. Проверка существования тренировки:
 *
 * Метод isWorkoutExists() проверяет, есть ли тренировка с таким названием
 * в списке доступных. Сейчас используется статический список из 10 тренировок:
 * - Скручивание согнутой ноги
 * - Велосипедные скручивания
 * - Ягодичный мостик
 * - Планка
 * - Скручивания с хлопком
 * - Скручивания со скрещенными руками
 * - Упражнение мертвый жук
 * - Бег в упоре лежа
 * - Упражнение для пресса
 * - Скручивание согнутых ног
 *
 * 5.4. Результаты поиска:
 *
 * Если тренировка найдена — открывается WorkoutActivity с передачей
 * search_query в Intent.
 *
 * Если тренировка не найдена — показывается Toast с сообщением
 * "Тренировка не найдена", а затем диалог с предложением похожих
 * тренировок (через метод showSimilarWorkouts).
 *
 *
 * ЧАСТЬ 6. НОЧНОЙ РЕЖИМ (ТЕМА) (ДЕТАЛЬНО)
 * ----------------------------------------
 *
 * 6.1. Сохранение настроек:
 *
 * Состояние ночного режима сохраняется в SharedPreferences с ключом
 * KEY_NIGHT_MODE. При первом запуске режим выключен (светлая тема).
 *
 * 6.2. Применение темы:
 *
 * В методе setupTheme() перед установкой макета загружается сохранённое
 * состояние и применяется соответствующая тема:
 * - R.style.AppTheme (светлая)
 * - R.style.AppTheme_Night (тёмная)
 *
 * 6.3. Переключение режима:
 *
 * При выборе пункта "Ночной режим" в боковом меню:
 * - Переключается флаг isNightMode
 * - Сохраняется новое значение в SharedPreferences
 * - Вызывается recreate() для перезапуска активности и применения темы
 * - Показывается Toast с сообщением о смене режима
 * - Обновляется иконка и текст пункта меню (солнце/луна)
 *
 * 6.4. Динамические цвета:
 *
 * Метод applyDynamicColors() применяет цвета к элементам, которые
 * не меняются автоматически через тему (например, фон Toolbar).
 *
 *
 * ЧАСТЬ 7. УПРАВЛЕНИЕ АККАУНТАМИ (ДЕТАЛЬНО)
 * ------------------------------------------
 *
 * 7.1. Кнопка "Добавить аккаунт":
 *
 * Находится в заголовке бокового меню. При нажатии открывается диалог
 * showAddAccountDialog() с четырьмя вариантами:
 *
 * 1. Создать новый аккаунт → переход на RegisterActivity
 * 2. Войти в существующий аккаунт → переход на LoginActivity
 * 3. Войти как администратор → проверка доступа к админ-панели
 * 4. Отмена → закрытие диалога
 *
 * 7.2. Административный доступ:
 *
 * Через AdminManager проверяется, зарегистрирован ли администратор.
 * Если нет — открывается AdminRegisterDialog для регистрации.
 * Если да — открывается AdminLoginDialog для входа.
 * После успешного входа открывается AdminPanel.
 *
 *
 * ЧАСТЬ 8. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * -----------------------------
 *
 * При запуске MainActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 * Это создаёт иммерсивный опыт и даёт больше пространства для контента.
 *
 *
 * ЧАСТЬ 9. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * --------------------------------------------
 *
 * onCreate() — инициализация активности, загрузка темы, настройка UI,
 *              инициализация AdminManager, проверка разрешений
 *
 * setupTheme() — загрузка сохранённой темы из SharedPreferences
 *
 * initViews() — привязка всех View-компонентов по ID из XML
 *
 * setupNavigationDrawer() — настройка бокового меню и обработка кликов
 *
 * setupClickListeners() — установка обработчиков для кнопок
 *
 * setupNotifications() — создание канала уведомлений
 *
 * createNotificationChannel() — создание канала для Android 8.0+
 *
 * checkNotificationPermission() — запрос разрешения на уведомления для Android 13+
 *
 * showFitnessNotification() — переход в NotificationsActivity
 *
 * showFitnessSearch() — открытие диалога поиска тренировок
 *
 * checkAndPerformSearch() — проверка поискового запроса и выполнение поиска
 *
 * isWorkoutExists() — проверка существования тренировки в списке
 *
 * showSimilarWorkouts() — показ похожих тренировок при отсутствии точного совпадения
 *
 * findSimilarWorkouts() — поиск похожих тренировок по частичному совпадению
 *
 * onBackPressed() — обработка кнопки "Назад" (закрытие меню или активности)
 *
 * openMyProfile() — переход в AccountActivity
 *
 * openWorkouts() — переход в WorkoutActivity
 *
 * openStatistics() — переход в StatsActivity
 *
 * openSettings() — переход в SettingsActivity
 *
 * toggleNightMode() — переключение ночного режима
 *
 * updateNightModeIcon() — обновление иконки в меню при смене темы
 *
 * applyDynamicColors() — применение цветов к динамическим элементам
 *
 * showAddAccountDialog() — диалог выбора типа аккаунта
 *
 * checkAdminAccess() — проверка доступа к админ-панели
 *
 * navigateToAdminPanel() — переход в AdminPanel
 *
 * onResume() — обновление цветов и иконки при возвращении на экран
 *
 *
 * ЧАСТЬ 10. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Поиск тренировок:
 *    - Сейчас используется статический список из 10 тренировок
 *    - Нужно подключить поиск по реальной базе данных или API
 *
 * 2. Уведомления:
 *    - Есть канал и разрешения, но нет полноценной логики отправки
 *    - Нужно добавить планировщик уведомлений о предстоящих тренировках
 *
 * 3. Профиль:
 *    - Кнопка "Профиль" ведёт в ProfileActivity, которая не реализована
 *    - Нужно создать экран профиля пользователя
 *
 * 4. Статистика:
 *    - Пункт меню "Статистика" ведёт в StatsActivity (есть заглушка)
 *    - Нужно добавить реальные графики и показатели
 *
 * 5. Достижения и друзья:
 *    - Пункты закомментированы, ждут реализации
 *
 * 6. Админ-панель:
 *    - Реализована базовая проверка доступа
 *    - Нужно добавить полноценное управление пользователями
 *
 *
 * ЧАСТЬ 11. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - Google Material Design (BottomNavigationView, NavigationView)
 * - AndroidX Core (для проверки разрешений)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 * Рекомендуемая версия: API 33 (Android 13) и выше
 *
 *
 * ЧАСТЬ 12. ИТОГИ
 * ----------------
 *
 * MainActivity является центральным узлом навигации в приложении Fitness App.
 * В нём реализованы: нижняя панель навигации, боковое меню, поиск тренировок,
 * переключение ночного режима, управление аккаунтами, система уведомлений,
 * полноэкранный режим и обработка системных кнопок. Это полностью рабочий
 * главный экран, готовый к использованию и дальнейшему расширению.
 *
 *
 * КОНЕЦ ДОКУМЕНТАЦИИ
 * =============================================================================
 */

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.fitnessapp.R;
import com.example.fitnessapp.admin.AdminLoginDialog;
import com.example.fitnessapp.admin.AdminManager;
import com.example.fitnessapp.admin.AdminPanel;
import com.example.fitnessapp.admin.AdminRegisterDialog;
import com.example.fitnessapp.utils.NotificationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Приватное поле для хранения объекта SharedPreferences для работы с настройками приложения
    private SharedPreferences sharedPreferences;

    //Константа с именем файла настроек
    private static final String PREF_NAME = "app_preferences";

    //Ключ для сохранения/загрузки режима ночной темы
    private static final String KEY_NIGHT_MODE = "night_mode";

    //Флаг, указывающий включен ли ночной режим (по умолчанию выключен)
    private boolean isNightMode = false;

    //Кнопки интерфейса с поддержкой совместимости (для старых версий Android)
    private AppCompatButton workoutsButton, profileButton, settingsButton;

    //Нижняя панель навигации (обычно содержит 3-5 пунктов меню)
    private BottomNavigationView bottomNavigationView;

    //Контейнер для бокового (выдвижного) меню
    private DrawerLayout drawerLayout;

    //Боковая навигационная панель
    private NavigationView navigationView;

    //Панель инструментов (замена ActionBar)
    private Toolbar toolbar;

    //Кнопка-иконка для открытия бокового меню
    private ImageButton navButton;

    //Кнопка-иконка для уведомлений
    private ImageButton notificationsBtn;

    //Кнопка-иконка для поиска (лупа)
    private ImageButton searchBtn;

    private Button workoutBtn; //Объявляем переменную для кнопки "Тренировки"
    private Button profileBtn; //Объявляем переменную для кнопки "Профиль"
    private Button settingsBtn; //Объявляем переменную для кнопки "Настройки"
    private Button addAccountBtn; //Объявляем переменную для кнопки "Добавить аккаунт"
    private AdminManager adminManager;

    //Менеджер уведомлений
    private NotificationManager notificationManager;
    private static final String CHANNEL_ID = "fitness_app_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //Устанавливаем макет (XML файл) активности из ресурсов


        NotificationHelper.createNotificationChannel(this); //Создаем канал уведомлений

        SharedPreferences prefs = getSharedPreferences("FitnessAppSettings", MODE_PRIVATE);
        boolean notificationsEnabled = prefs.getBoolean("notifications", true);
        if (notificationsEnabled) {
            NotificationHelper.scheduleDailyReminder(this);
        }

        //Загружаем настройки темы перед setContentView
        setupTheme();

        initViews(); //Инициализируем View-компоненты (кнопки, поля и т.д.)
        setupNavigationDrawer(); //Настраиваем боковое выдвижное меню
        setupClickListeners(); //Устанавливаем обработчики кликов для кнопок
        setupNotifications(); // Настраиваем систему уведомлений

        adminManager = new AdminManager(this); //Создаем менеджер для административных функций

        // Проверяем разрешения для уведомлений (для Android 13+)
        checkNotificationPermission();

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        workoutBtn = findViewById(R.id.workoutsButton); //Ищет кнопку по её ID в макете и присваивает её переменной workoutBtn.
        profileBtn = findViewById(R.id.profileButton); //Ищет кнопку по её ID в макете и присваивает её переменной profileBtn.
        settingsBtn = findViewById(R.id.settingsButton); //Ищет кнопку по её ID в макете и присваивает её переменной settingsBtn.

        workoutBtn.setOnClickListener(v -> { //Устанавливает слушатель нажатия для кнопки "Тренировки"
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class); //Создает новый Intent для перехода на WorkoutActivity.
            startActivity(intent); //Запускает активность WorkoutActivity.
            //finish(); //Завершает текущую активность
        });

        profileBtn.setOnClickListener(v -> { //Устанавливает слушатель нажатия для кнопки "Профиль".
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class); //Создает новый Intent для перехода на ProfileActivity.
            startActivity(intent); //Запускает активность ProfileActivity.
            //finish(); //Завершает текущую активность
        });

        settingsBtn.setOnClickListener(v -> { //Устанавливает слушатель нажатия для кнопки "Настройки".
            Intent intent = new Intent(MainActivity.this, UserManagementActivity.class); //Создает новый Intent для перехода на SettingsActivity.
            startActivity(intent); //Запускает активность SettingsActivity.
            //finish(); //Завершает текущую активность
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView); //Находим нижнюю панель
        //навигации по ID из макета
        //Отключаем обработку оконных inset'ов (отступов системных элементов). Это предотвращает
        //автоматические отступы для системных панелей
        bottomNavigationView.setOnApplyWindowInsetsListener(null);
        bottomNavigationView.setSelectedItemId(R.id.nav_home); //Устанавливаем пункт "Главная"
        //как выбранный по умолчанию. Показывает пользователю, что он находится на главном экране

        /*Устанавливаем обработчик выбора пунктов в нижней панели навигации*/
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent = null;

            //Проверяем какой пункт меню был выбран
            if (item.getItemId() == R.id.nav_home)
            {
                //Создаем Intent для перехода на главный экран
                intent = new Intent(this, MainActivity.class);
            }
            else if (item.getItemId() == R.id.nav_workouts)
            {
                //Intent для перехода к тренировкам
                intent = new Intent(this, WorkoutActivity.class);
            }
            else if (item.getItemId() == R.id.nav_settings)
            {
                //Intent для перехода к настройкам приложения
                intent = new Intent(this, SettingsActivity.class);
            }
            else if (item.getItemId() == R.id.nav_about)
            {
                //Показываем диалоговое окно "О приложении"
                new AlertDialog.Builder(this)
                        .setTitle("О приложении")
                        .setMessage("Fitness App Pro\n\n" +
                                "Ваш персональный фитнес-помощник!\n\n" +
                                "Версия: 1.0.0\n" +
                                "Приложение поможет вам:\n" +
                                "• Следить за тренировками\n" +
                                "• Отслеживать прогресс\n" +
                                "• Достигать спортивных целей")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.mipmap.ic_fitness) //Иконка приложения
                        .show();
                return true; //Возвращаем true, но не запускаем новую активность
            }
            else if (item.getItemId() == R.id.nav_profile)
            {
                //Intent для перехода к профилю (через экран логина)
                intent = new Intent(this, LoginActivity.class);
            }
            /*else if (item.getItemId() == R.id.nav_admin)
            {
                //код для админ-панели
                checkAdminAccess();
                return true;
                intent = new Intent(this, AdminPanel.class);
            }*/

            //Если Intent создан, запускаем активность
            if (intent != null) {
                startActivity(intent);
                //Отключаем стандартную анимацию перехода между активностями
                overridePendingTransition(0, 0);
            }
            return true; //Возвращаем true чтобы показать что выбор обработан
        });
        //checkAdminAccess(); //Проверка для администратора

        //Применяем сохраненную тему при запуске приложения
        SharedPreferences pref = getSharedPreferences("FitnessAppSettings", MODE_PRIVATE);
        boolean isDarkMode = pref.getBoolean("dark_mode", false);

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    /**
     * Настройка темы приложения
     */
    private void setupTheme() {
        //Получаем доступ к SharedPreferences с указанием имени файла и режима (приватный)
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        //Загружаем состояние ночной темы, если значение не найдено - используем false по умолчанию
        isNightMode = sharedPreferences.getBoolean(KEY_NIGHT_MODE, false);

        //Устанавливаем соответствующую тему в зависимости от сохраненных настроек
        if (isNightMode) {
            setTheme(R.style.AppTheme_Night); //Устанавливаем ночную тему
        } else {
            setTheme(R.style.AppTheme); //Устанавливаем дневную тему
        }
    }

    private void initViews() {
        //Находим и инициализируем все View-компоненты по их ID из макета

        drawerLayout = findViewById(R.id.drawer_layout); //Контейнер бокового меню
        navigationView = findViewById(R.id.nav_view); //Само боковое навигационное меню
        toolbar = findViewById(R.id.my_toolbar); //Панель инструментов
        navButton = findViewById(R.id.nav_button); //Кнопка для открытия бокового меню
        notificationsBtn = findViewById(R.id.notifications_btn); //Кнопка для уведомлений
        searchBtn = findViewById(R.id.search_btn); //Кнопка поиска

        //Инициализируем менеджер уведомлений
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //Получаем заголовок NavigationView
        View headerView = navigationView.getHeaderView(0);

        //Находим кнопку "Добавить аккаунт" в заголовке
        if (headerView != null)
        {
            addAccountBtn = headerView.findViewById(R.id.add_account_btn);

            //Для отладки можно проверить, нашлась ли кнопка
            if (addAccountBtn == null)
            {
                Log.e("MainActivity", "Кнопка add_account_btn не найдена!");
            } else {
                Log.d("MainActivity", "Кнопка add_account_btn успешно инициализирована!");
            }
        }

        //Устанавливаем кастомный Toolbar в качестве ActionBar для активности
        //Это позволяет использовать стандартные функции ActionBar с кастомным видом
        setSupportActionBar(toolbar);
    }

    /**
     * Настройка системы уведомлений
     */
    private void setupNotifications() {
        // Создаем канал уведомлений (для Android 8.0+)
        createNotificationChannel();
    }

    /**
     * Создание канала уведомлений (обязательно для Android 8.0+)
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Fitness App Уведомления";
            String description = "Канал для уведомлений фитнес приложения";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * Проверка разрешений для уведомлений (для Android 13+)
     */
    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        100);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешение на уведомления получено", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешение на уведомления отклонено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupNavigationDrawer() {
        //Обработка клика по кнопке меню
        navButton.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        //Обработка выбора пунктов меню
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Логика обработки пунктов меню
            if (id == R.id.nav_my_profile) {
                openMyProfile(); //Открыть мой профиль
            } else if (id == R.id.nav_workouts) {
                openWorkouts(); //Открыть раздел тренировок
            } else if (id == R.id.nav_statistics) {
                openStatistics(); //Открыть статистику
            } else if (id == R.id.nav_settings) {
                openSettings(); //Открыть настройки
            } else if (id == R.id.nav_night_mode) {
                toggleNightMode(); //Переключить ночной режим
            } else if (id == R.id.nav_about) {
                //Показываем диалоговое окно "О приложении"
                new AlertDialog.Builder(this)
                        .setTitle("О приложении")
                        .setMessage("Fitness App Pro\n\n" +
                                "Ваш персональный фитнес-помощник!\n\n" +
                                "Версия: 1.0.0\n" +
                                "Приложение поможет вам:\n" +
                                "• Следить за тренировками\n" +
                                "• Отслеживать прогресс\n" +
                                "• Достигать спортивных целей")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .setIcon(R.mipmap.ic_fitness) //Иконка приложения
                        .show();
                return true; //Возвращаем true, но не запускаем новую активность
            } /*else if (id == R.id.nav_achievements) {
                openAchievements(); //Открыть достижения
            } else if (id == R.id.nav_friends) {
                openFriends(); //Открыть раздел друзей
            } else if (id == R.id.nav_support) {
                openSupport(); //Откыть поддержку
            }*/

            //Закрываем боковое меню после выбора пункта
            //GravityCompat.START - закрывает меню с начала (слева для LTR, справа для RTL)
            drawerLayout.closeDrawer(GravityCompat.START);

            return true; //Возвращаем true, указывая что событие обработано
        });
    }

    private void setupClickListeners() {
        findViewById(R.id.workoutsButton).setOnClickListener(v -> {
            //Обработка клика по кнопке тренировок
        });

        //Добавление обработчика для кнопки уведомлений
        notificationsBtn.setOnClickListener(v -> {
            showFitnessNotification(); //Показываем уведомление при клике
        });

        //Добавление обработчика для кнопки поиска
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFitnessSearch();
            }
        });

        //Обработчик для кнопки "Добавить аккаунт"
        if (addAccountBtn != null)
        {
            addAccountBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //Закрываем боковое меню при нажатии
                    if (drawerLayout.isDrawerOpen(GravityCompat.START))
                    {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                    //Вызываем метод для показа диалога добавления аккаунта
                    showAddAccountDialog();
                }
            });
        } else {
            Log.e("MainActivity", "addAccountBtn is null, не удалось установить обработчик");
        }
    }

    private void showFitnessNotification() {
        Intent intent = new Intent(this, NotificationsActivity.class);
        startActivity(intent);
    }


    private void showFitnessSearch() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Поиск тренировок");

        final EditText input = new EditText(this);
        input.setHint("Введите название тренировки...");
        input.setSingleLine(true);

        input.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                String searchQuery = input.getText().toString().trim();
                checkAndPerformSearch(searchQuery);
                return true;
            }
            return false;
        });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(48, 16, 48, 16);
        input.setLayoutParams(lp);

        builder.setView(input);

        builder.setPositiveButton("Найти", (dialog, which) -> {
            String searchQuery = input.getText().toString().trim();
            checkAndPerformSearch(searchQuery);
        });

        builder.setNegativeButton("Отмена", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();

        input.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    private void checkAndPerformSearch(String searchQuery) {
        if (searchQuery.isEmpty()) {
            Toast.makeText(MainActivity.this, "Введите запрос для поиска", Toast.LENGTH_SHORT).show();
            return;
        }

        //Проверяем существование тренировки
        if (isWorkoutExists(searchQuery)) {
            //Если тренировка найдена - переходим
            Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
            intent.putExtra("search_query", searchQuery);
            startActivity(intent);
        } else {
            //Если тренировка не найдена - показываем сообщение
            Toast.makeText(MainActivity.this, "Тренировка '" + searchQuery + "' не найдена", Toast.LENGTH_LONG).show();

            //Можно предложить похожие тренировки
            showSimilarWorkouts(searchQuery);
        }
    }

    //Метод для проверки существования тренировки
    private boolean isWorkoutExists(String searchQuery) {
        //Здесь должна быть ваша реальная логика проверки
        //Например, поиск в базе данных, списке тренировок и т.д.

        //Временный пример - список существующих тренировок
        List<String> availableWorkouts = Arrays.asList(
                "Скручивание согнутой ноги", "Велосипедные скручивания", "Ягодичный мостик", "Планка", "Скручивания с хлопком",
                "Скручивания со скрещенными руками", "Упражнение мертвый жук", "Бег в упоре лежа", "Упражнение для пресса", "Скручивание согнутых ног"
        );

        //Проверяем совпадение (можно сделать более сложную логику)
        for (String workout : availableWorkouts) {
            if (workout.toLowerCase().contains(searchQuery.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    //Дополнительный метод для показа похожих тренировок
    private void showSimilarWorkouts(String searchQuery) {
        List<String> similarWorkouts = findSimilarWorkouts(searchQuery);

        if (!similarWorkouts.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Возможно вы искали:");

            String[] workoutsArray = similarWorkouts.toArray(new String[0]);
            builder.setItems(workoutsArray, (dialog, which) -> {
                //При выборе похожей тренировки переходим на неё
                Intent intent = new Intent(MainActivity.this, WorkoutActivity.class);
                intent.putExtra("search_query", similarWorkouts.get(which));
                startActivity(intent);
            });

            builder.setNegativeButton("Отмена", null);
            builder.show();
        }
    }

    //Поиск похожих тренировок
    private List<String> findSimilarWorkouts(String searchQuery) {
        List<String> allWorkouts = Arrays.asList(
                "Скручивание согнутой ноги", "Велосипедные скручивания", "Ягодичный мостик", "Планка", "Скручивания с хлопком",
                "Скручивания со скрещенными руками", "Упражнение мертвый жук", "Бег в упоре лежа", "Упражнение для пресса", "Скручивание согнутых ног"
        );

        List<String> similar = new ArrayList<>();
        for (String workout : allWorkouts) {
            if (workout.toLowerCase().contains(searchQuery.toLowerCase()) ||
                    searchQuery.toLowerCase().contains(workout.toLowerCase())) {
                similar.add(workout);
            }
        }
        return similar;
    }


    @Override
    public void onBackPressed() {
        //Проверяем, открыто ли в данный момент боковое меню
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //Если меню открыто - закрываем его при нажатии кнопки "Назад"
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            //Если меню закрыто - выполняем стандартное поведение кнопки "Назад"
            //(закрытие текущей активности или возврат к предыдущей)
            super.onBackPressed();
        }
    }

    //Методы для обработки пунктов бокового меню
    private void openMyProfile() {
        //Toast.makeText(this, "Открыть профиль", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
    }

    private void openWorkouts() {
        //Toast.makeText(this, "Открыть тренировки", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, WorkoutActivity.class);
        startActivity(intent);
    }

    private void openStatistics() {
        //Toast.makeText(this, "Открыть статистику", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    private void openSettings() {
        //Toast.makeText(this, "Открыть настройки", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void toggleNightMode() {
        isNightMode = !isNightMode;

        //Сохраняем настройку
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NIGHT_MODE, isNightMode);
        editor.apply();

        //Перезапускаем активность для применения темы
        recreate();

        //Показываем сообщение
        String message = isNightMode ? "Ночной режим включен" : "Дневной режим включен";
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();

        //Обновляем иконку в меню (опционально)
        updateNightModeIcon();
    }

    /**
     * Обновление иконки ночного режима в меню
     */
    private void updateNightModeIcon() {
        Menu menu = navigationView.getMenu();
        MenuItem nightModeItem = menu.findItem(R.id.nav_night_mode);

        if (isNightMode) {
            nightModeItem.setIcon(R.drawable.ic_day); // Иконка дня
            nightModeItem.setTitle("Дневной режим");
        } else {
            nightModeItem.setIcon(R.drawable.ic_night); // Иконка ночи
            nightModeItem.setTitle("Ночной режим");
        }
    }

    /**
     * Применение цветов для динамических элементов
     */
    private void applyDynamicColors() {
        //Пример применения цветов к элементам, которые не меняются через тему
        if (isNightMode) {
            //Устанавливаем темные цвета для конкретных элементов
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_surface));
            //Другие элементы...
        } else {
            //Устанавливаем светлые цвета
            toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void openAchievements() {
        Toast.makeText(this, "Открыть достижения", Toast.LENGTH_SHORT).show();
    }

    private void openFriends() {
        Toast.makeText(this, "Открыть друзей", Toast.LENGTH_SHORT).show();
    }

    private void openSupport() {
        Toast.makeText(this, "Открыть поддержку", Toast.LENGTH_SHORT).show();
    }

    private void showAddAccountDialog() {
        //Создание функционального диалога
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Выберите тип аккаунта:");

        String[] options = {
                "➕ Создать новый аккаунт",
                "🔐 Войти в существующий аккаунт",
                "⚙️ Войти как администратор",
                "❌ Отмена"
        };

        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                case 0: //Новый пользователь
                    Intent registerIntent = new Intent(this, RegisterActivity.class);
                    startActivity(registerIntent);
                    break;
                case 1: //Существующий пользователь
                    Intent loginIntent = new Intent(this, LoginActivity.class);
                    startActivity(loginIntent);
                    break;
                case 2: //Администратор
                    checkAdminAccess();
                    break;
                case 3: //Отмена
                    dialog.dismiss();
                    break;
            }
        });

        builder.show();
    }

    /* Проверка доступа к админ-панели */
    private void checkAdminAccess() {
        if (!adminManager.isAdminRegistered()) {
            AdminRegisterDialog registerDialog = new AdminRegisterDialog(this,
                    new AdminRegisterDialog.AdminDialogListener() {
                        @Override
                        public void onSuccess() {
                            navigateToAdminPanel();
                        }

                        @Override
                        public void onFailure() {
                            // Обработка неудачи
                        }
                    });
            registerDialog.show();
        } else {
            AdminLoginDialog loginDialog = new AdminLoginDialog(this,
                    new AdminLoginDialog.AdminDialogListener() {
                        @Override
                        public void onSuccess() {
                            navigateToAdminPanel(); //Переход при успешном входе
                        }

                        @Override
                        public void onFailure() {
                            // Обработка неудачи
                        }
                    });
            loginDialog.show();
        }
    }

    /* Переход к админ-панели */
    public void navigateToAdminPanel() {
        Intent intent = new Intent(MainActivity.this, AdminPanel.class);
        startActivity(intent);
        overridePendingTransition(0, 0); //Анимация без перехода
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyDynamicColors();
        updateNightModeIcon();
    }
}