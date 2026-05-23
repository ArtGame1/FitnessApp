package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ SettingsActivity (ЭКРАН НАСТРОЕК ПРИЛОЖЕНИЯ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * SettingsActivity является экраном настроек приложения Fitness App.
 * Сюда пользователь попадает при нажатии на кнопку "Настройки" на главном экране,
 * из бокового меню или из нижней панели навигации.
 *
 * Этот экран позволяет пользователю настраивать различные параметры приложения:
 * - Включение/выключение уведомлений
 * - Включение/выключение звуковых эффектов
 * - Включение/выключение автоматической паузы между упражнениями
 * - Настройка времени отдыха между подходами
 * - Настройка количества повторений по умолчанию
 * - Сброс прогресса тренировок
 * - Экспорт данных тренировок (в разработке)
 *
 * Все настройки сохраняются в SharedPreferences и применяются при следующем
 * запуске соответствующих функций приложения.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель (Toolbar):
 * - Кнопка "Назад" (стрелка влево) — возврат на предыдущий экран
 * - Заголовок "Настройки"
 *
 * Основная область (настройки):
 * - Переключатель "Уведомления" (switch_notifications) — включение/выключение
 *   ежедневных напоминаний о тренировках в 19:00
 *
 * - Переключатель "Звук" (switch_sound) — включение/выключение звуковых
 *   эффектов во время тренировок
 *
 * - Переключатель "Автопауза" (switch_auto_pause) — автоматическая пауза
 *   между упражнениями
 *
 * - Строка "Время отдыха между подходами" (tv_rest_time) с кнопкой "Изменить"
 *   (btn_set_rest_time) — выбор времени отдыха из предустановленных вариантов
 *
 * - Строка "Повторения по умолчанию" (tv_default_reps) с кнопкой "Изменить"
 *   (btn_set_reps) — выбор количества повторений по умолчанию
 *
 * Нижняя часть (дополнительные действия):
 * - Кнопка "Сбросить прогресс упражнений" (btn_reset_exercises) — удаление
 *   всех данных о выполненных тренировках
 *
 * - Кнопка "Экспортировать данные" (btn_export_data) — экспорт данных
 *   тренировок (функция в разработке)
 *
 * - Кнопка "Сохранить настройки" (btn_save_settings) — сохранение всех
 *   изменённых настроек
 *
 * - Кнопка "Отмена" (btn_cancel_settings) — отмена изменений и возврат
 *   на предыдущий экран
 *
 *
 * ЧАСТЬ 3. СИСТЕМА ВРЕМЕННОГО ХРАНЕНИЯ НАСТРОЕК (ДЕТАЛЬНО)
 * ---------------------------------------------------------
 *
 * 3.1. Почему нужны временные переменные:
 *
 * В классе есть два набора переменных для хранения настроек:
 * - Постоянные: хранятся в SharedPreferences и загружаются при запуске
 * - Временные: tempNotifications, tempSound, tempAutoPause, tempRestTime,
 *   tempDefaultReps — хранят изменения, которые пользователь сделал,
 *   но ещё не сохранил.
 *
 * Это позволяет:
 * - Отменить все изменения, если пользователь нажал "Отмена"
 * - Показать диалог "Сохранить изменения?" при попытке выйти
 * - Сравнивать текущие значения с сохранёнными
 *
 * 3.2. Жизненный цикл временных настроек:
 *
 * 1. При запуске экрана: loadSettingsToTemp() — копирует значения из
 *    SharedPreferences во временные переменные
 *
 * 2. При изменении переключателей или выборе новых значений: временные
 *    переменные обновляются, но в SharedPreferences ничего не сохраняется
 *
 * 3. При нажатии "Сохранить": saveAllSettings() — копирует значения из
 *    временных переменных в SharedPreferences
 *
 * 4. При нажатии "Отмена": экран закрывается без сохранения, временные
 *    переменные просто уничтожаются
 *
 *
 * ЧАСТЬ 4. НАСТРОЙКА УВЕДОМЛЕНИЙ (ДЕТАЛЬНО)
 * ------------------------------------------
 *
 * 4.1. Включение уведомлений:
 *
 * При включении переключателя "Уведомления" вызывается метод
 * manageNotifications(), который запускает планировщик ежедневных
 * уведомлений через NotificationHelper.scheduleDailyReminder().
 *
 * Уведомления запланированы на 19:00 каждый день.
 *
 * 4.2. Разрешение для точных уведомлений (Android 12+):
 *
 * Для Android 12 (S) и выше требуется специальное разрешение
 * SCHEDULE_EXACT_ALARM. Если разрешение не предоставлено, показывается
 * диалог с предложением включить его.
 *
 * При нажатии "Включить" открывается системное окно настроек через
 * NotificationHelper.requestExactAlarmPermission().
 *
 * 4.3. Выключение уведомлений:
 *
 * При выключении переключателя вызывается NotificationHelper.cancelReminder(),
 * который отменяет все запланированные уведомления.
 *
 *
 * ЧАСТЬ 5. ДИАЛОГИ ВЫБОРА ЗНАЧЕНИЙ (ДЕТАЛЬНО)
 * --------------------------------------------
 *
 * 5.1. Выбор времени отдыха (showRestTimeDialog):
 *
 * Отображается диалог со списком вариантов:
 * - 15 секунд
 * - 30 секунд
 * - 45 секунд
 * - 60 секунд
 * - 90 секунд
 *
 * При выборе варианта:
 * - Временная переменная tempRestTime обновляется
 * - Текстовое поле tvRestTime обновляется на экране
 *
 * 5.2. Выбор повторений по умолчанию (showRepsDialog):
 *
 * Отображается диалог со списком вариантов:
 * - 8 повторений
 * - 10 повторений
 * - 12 повторений
 * - 15 повторений
 * - 20 повторений
 *
 * При выборе варианта:
 * - Временная переменная tempDefaultReps обновляется
 * - Текстовое поле tvDefaultReps обновляется на экране
 *
 *
 * ЧАСТЬ 6. СБРОС ПРОГРЕССА ТРЕНИРОВОК (ДЕТАЛЬНО)
 * -----------------------------------------------
 *
 * При нажатии на кнопку "Сбросить прогресс упражнений" вызывается метод
 * showResetConfirmation(), который показывает диалог подтверждения:
 *
 * - Заголовок: "Сброс прогресса"
 * - Сообщение: "Вы уверены? Все данные о тренировках будут удалены
 *   безвозвратно."
 * - Кнопка "Сбросить" — очищает SharedPreferences "WorkoutProgress"
 *   и показывает Toast "Прогресс сброшен"
 * - Кнопка "Отмена" — закрывает диалог без действий
 *
 * Это полезно, когда пользователь хочет начать тренировки "с чистого листа".
 *
 *
 * ЧАСТЬ 7. ПРОВЕРКА НЕСОХРАНЕННЫХ ИЗМЕНЕНИЙ (ДЕТАЛЬНО)
 * -----------------------------------------------------
 *
 * 7.1. Метод hasUnsavedChanges():
 *
 * Сравнивает текущие временные значения с сохранёнными в SharedPreferences.
 * Возвращает true, если хотя бы одна настройка отличается.
 *
 * Сравниваются:
 * - tempNotifications vs сохранённые уведомления
 * - tempSound vs сохранённый звук
 * - tempAutoPause vs сохранённая автопауза
 * - tempRestTime vs сохранённое время отдыха
 * - tempDefaultReps vs сохранённые повторения
 *
 * 7.2. Где используется:
 *
 * - При нажатии на кнопку "Назад" (стрелка влево)
 * - При нажатии на кнопку "Отмена"
 * - При нажатии системной кнопки "Назад"
 *
 * Если есть несохранённые изменения, показывается диалог
 * showUnsavedChangesDialog() с вопросом "Сохранить изменения?".
 *
 *
 * ЧАСТЬ 8. ДИАЛОГ НЕСОХРАНЕННЫХ ИЗМЕНЕНИЙ (ДЕТАЛЬНО)
 * ---------------------------------------------------
 *
 * При попытке выйти с несохранёнными изменениями показывается диалог:
 *
 * - Заголовок: "Несохраненные изменения"
 * - Сообщение: "Сохранить изменения?"
 * - Кнопка "Сохранить" — вызывает saveAllSettings() и закрывает экран
 * - Кнопка "Не сохранять" — закрывает экран без сохранения
 * - Кнопка "Отмена" — закрывает диалог, оставаясь на экране настроек
 *
 *
 * ЧАСТЬ 9. СОХРАНЕНИЕ НАСТРОЕК (ДЕТАЛЬНО)
 * ----------------------------------------
 *
 * Метод saveAllSettings():
 *
 * 1. Получает редактор SharedPreferences
 * 2. Сохраняет все временные значения в SharedPreferences:
 *    - KEY_NOTIFICATIONS = tempNotifications
 *    - KEY_SOUND = tempSound
 *    - KEY_AUTO_PAUSE = tempAutoPause
 *    - KEY_REST_TIME = tempRestTime
 *    - KEY_DEFAULT_REPS = tempDefaultReps
 * 3. Применяет изменения через apply() (асинхронное сохранение)
 * 4. Вызывает manageNotifications() для применения настроек уведомлений
 * 5. Показывает Toast "Настройки сохранены"
 * 6. Закрывает активность (finish())
 *
 *
 * ЧАСТЬ 10. ОФОРМЛЕНИЕ И СТИЛИ
 * -----------------------------
 *
 * Статус-бар:
 * - Для Android 5.0 (Lollipop) и выше устанавливается цвет statusBarColor
 * - Цвет берётся из ресурсов: R.color.menuColor (синий)
 *
 * Toolbar:
 * - Устанавливается как ActionBar
 * - Включается кнопка "Назад" (setDisplayHomeAsUpEnabled)
 * - Устанавливается заголовок "Настройки"
 *
 *
 * ЧАСТЬ 11. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * ---------------------------------------------
 *
 * onCreate() — инициализация активности: установка статус-бара, установка
 *              layout, создание канала уведомлений, инициализация
 *              SharedPreferences, вызов методов настройки
 *
 * setupToolbar() — настройка верхней панели: поиск Toolbar, установка
 *                  ActionBar, включение кнопки "Назад", установка заголовка
 *
 * initViews() — поиск всех UI-элементов на экране по ID из XML
 *
 * loadSettingsToTemp() — загрузка сохранённых настроек из SharedPreferences
 *                        во временные переменные
 *
 * displayTempSettings() — отображение временных настроек на экране
 *
 * setupListeners() — установка обработчиков для всех кнопок и переключателей
 *
 * showRestTimeDialog() — диалог выбора времени отдыха между подходами
 *
 * showRepsDialog() — диалог выбора количества повторений по умолчанию
 *
 * saveAllSettings() — сохранение всех настроек в SharedPreferences
 *
 * manageNotifications() — управление уведомлениями (включение/выключение,
 *                         запрос разрешений для Android 12+)
 *
 * showResetConfirmation() — диалог подтверждения сброса прогресса
 *
 * exportWorkoutData() — экспорт данных тренировок (заглушка)
 *
 * showUnsavedChangesDialog() — диалог о несохранённых изменениях
 *
 * onBackPressed() — обработка системной кнопки "Назад"
 *
 * hasUnsavedChanges() — проверка наличия несохранённых изменений
 *
 *
 * ЧАСТЬ 12. КЛЮЧЕВЫЕ КОНСТАНТЫ
 * -----------------------------
 *
 * PREFS_NAME = "FitnessAppSettings" — имя файла SharedPreferences
 *
 * KEY_NOTIFICATIONS = "notifications" — ключ для настройки уведомлений
 * KEY_SOUND = "sound" — ключ для настройки звука
 * KEY_AUTO_PAUSE = "auto_pause" — ключ для настройки автопаузы
 * KEY_REST_TIME = "rest_time" — ключ для времени отдыха
 * KEY_DEFAULT_REPS = "default_reps" — ключ для повторений по умолчанию
 *
 *
 * ЧАСТЬ 13. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Экспорт данных не реализован:
 *    - Кнопка показывает сообщение "Экспорт будет доступен в следующей версии"
 *    - Нужно реализовать экспорт в CSV, JSON или Google Drive
 *
 * 2. Нет выбора времени уведомлений:
 *    - Уведомления жёстко зафиксированы на 19:00
 *    - Нужно добавить TimePicker для выбора времени
 *
 * 3. Нет выбора дней для уведомлений:
 *    - Уведомления приходят каждый день
 *    - Нужно добавить выбор конкретных дней недели
 *
 * 4. Нет настройки языка:
 *    - Отсутствует переключение между русским/английским
 *    - Нужно добавить выбор языка приложения
 *
 * 5. Нет настройки темы:
 *    - Переключение темы вынесено в боковое меню
 *    - Нужно дублировать эту настройку в SettingsActivity
 *
 * 6. Нет резервного копирования:
 *    - Нет возможности создать резервную копию настроек
 *    - Нужно добавить экспорт/импорт настроек
 *
 * 7. Нет сброса только настроек:
 *    - Есть сброс прогресса, но нет сброса настроек
 *    - Нужно добавить кнопку "Сбросить настройки по умолчанию"
 *
 *
 * ЧАСТЬ 14. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - AndroidX Core (для работы с цветами ContextCompat)
 *
 * Вспомогательные классы:
 * - NotificationHelper (com.example.fitnessapp.utils) — управление уведомлениями
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 15. ИТОГИ
 * ----------------
 *
 * SettingsActivity — это полноценный экран настроек приложения, который
 * позволяет пользователю гибко настраивать параметры Fitness App.
 *
 * Реализованы следующие функции:
 * - Включение/выключение уведомлений с запросом разрешений для Android 12+
 * - Включение/выключение звука и автопаузы
 * - Выбор времени отдыха из 5 вариантов
 * - Выбор количества повторений из 5 вариантов
 * - Сброс прогресса тренировок с подтверждением
 * - Сохранение всех настроек в SharedPreferences
 * - Отмена изменений с проверкой несохранённых данных
 * - Диалог при попытке выхода с несохранёнными изменениями
 *
 * Все настройки сохраняются и восстанавливаются при следующем запуске.
 * Экран имеет интуитивно понятный интерфейс и корректно обрабатывает
 * все действия пользователя.
 *
 * =============================================================================
 */

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.example.fitnessapp.R;
import com.example.fitnessapp.utils.NotificationHelper;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FitnessAppSettings";

    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_AUTO_PAUSE = "auto_pause";
    private static final String KEY_REST_TIME = "rest_time";
    private static final String KEY_DEFAULT_REPS = "default_reps";

    private Switch switchNotifications, switchSound, switchAutoPause;
    private Button btnSetRestTime, btnSetReps, btnSaveSettings, btnCancelSettings;
    private TextView tvRestTime, tvDefaultReps;

    private boolean tempNotifications, tempSound, tempAutoPause;
    private String tempRestTime, tempDefaultReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        NotificationHelper.createNotificationChannel(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setupToolbar();
        initViews();
        loadSettingsToTemp();
        displayTempSettings();
        setupListeners();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            setTitle("Настройки");
        }
    }

    private void initViews() {
        switchNotifications = findViewById(R.id.switch_notifications);
        switchSound = findViewById(R.id.switch_sound);
        switchAutoPause = findViewById(R.id.switch_auto_pause);
        tvRestTime = findViewById(R.id.tv_rest_time);
        tvDefaultReps = findViewById(R.id.tv_default_reps);
        btnSetRestTime = findViewById(R.id.btn_set_rest_time);
        btnSetReps = findViewById(R.id.btn_set_reps);
        btnSaveSettings = findViewById(R.id.btn_save_settings);
        btnCancelSettings = findViewById(R.id.btn_cancel_settings);
    }

    private void loadSettingsToTemp() {
        tempNotifications = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true);
        tempSound = sharedPreferences.getBoolean(KEY_SOUND, true);
        tempAutoPause = sharedPreferences.getBoolean(KEY_AUTO_PAUSE, true);
        tempRestTime = sharedPreferences.getString(KEY_REST_TIME, "30 секунд");
        tempDefaultReps = sharedPreferences.getString(KEY_DEFAULT_REPS, "15 повторений");
    }

    private void displayTempSettings() {
        switchNotifications.setChecked(tempNotifications);
        switchSound.setChecked(tempSound);
        switchAutoPause.setChecked(tempAutoPause);
        tvRestTime.setText(tempRestTime);
        tvDefaultReps.setText(tempDefaultReps);
    }

    private void setupListeners() {
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tempNotifications = isChecked;
            if (isChecked) {
                Toast.makeText(this, "Уведомления будут приходить в 19:00", Toast.LENGTH_LONG).show();
            }
        });

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> tempSound = isChecked);
        switchAutoPause.setOnCheckedChangeListener((buttonView, isChecked) -> tempAutoPause = isChecked);
        btnSetRestTime.setOnClickListener(v -> showRestTimeDialog());
        btnSetReps.setOnClickListener(v -> showRepsDialog());
        btnSaveSettings.setOnClickListener(v -> saveAllSettings());
        btnCancelSettings.setOnClickListener(v -> {
            if (hasUnsavedChanges()) {
                showUnsavedChangesDialog();
            } else {
                finish();
            }
        });
    }

    private void showRestTimeDialog() {
        final String[] restTimes = {"15 секунд", "30 секунд", "45 секунд", "60 секунд", "90 секунд"};
        new AlertDialog.Builder(this)
                .setTitle("Время отдыха между подходами")
                .setItems(restTimes, (dialog, which) -> {
                    tempRestTime = restTimes[which];
                    tvRestTime.setText(tempRestTime);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showRepsDialog() {
        final String[] repsOptions = {"8 повторений", "10 повторений", "12 повторений", "15 повторений", "20 повторений"};
        new AlertDialog.Builder(this)
                .setTitle("Повторения по умолчанию")
                .setItems(repsOptions, (dialog, which) -> {
                    tempDefaultReps = repsOptions[which];
                    tvDefaultReps.setText(tempDefaultReps);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void saveAllSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATIONS, tempNotifications);
        editor.putBoolean(KEY_SOUND, tempSound);
        editor.putBoolean(KEY_AUTO_PAUSE, tempAutoPause);
        editor.putString(KEY_REST_TIME, tempRestTime);
        editor.putString(KEY_DEFAULT_REPS, tempDefaultReps);
        editor.apply();

        manageNotifications();
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void manageNotifications() {
        if (tempNotifications) {
            NotificationHelper.scheduleDailyReminder(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!NotificationHelper.checkExactAlarmPermission(this)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Разрешение на уведомления")
                            .setMessage("Для точных напоминаний о тренировках нужно разрешение. Хотите его включить?")
                            .setPositiveButton("Включить", (dialog, which) -> NotificationHelper.requestExactAlarmPermission(this))
                            .setNegativeButton("Отмена", null)
                            .show();
                }
            }
        } else {
            NotificationHelper.cancelReminder(this);
        }
    }

    private void showUnsavedChangesDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Несохраненные изменения")
                .setMessage("Сохранить изменения?")
                .setPositiveButton("Сохранить", (dialog, which) -> saveAllSettings())
                .setNegativeButton("Не сохранять", (dialog, which) -> finish())
                .setNeutralButton("Отмена", null)
                .show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (hasUnsavedChanges()) {
                showUnsavedChangesDialog();
            } else {
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (hasUnsavedChanges()) {
            showUnsavedChangesDialog();
        } else {
            super.onBackPressed();
        }
    }

    private boolean hasUnsavedChanges() {
        return tempNotifications != sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true) ||
                tempSound != sharedPreferences.getBoolean(KEY_SOUND, true) ||
                tempAutoPause != sharedPreferences.getBoolean(KEY_AUTO_PAUSE, true) ||
                !tempRestTime.equals(sharedPreferences.getString(KEY_REST_TIME, "30 секунд")) ||
                !tempDefaultReps.equals(sharedPreferences.getString(KEY_DEFAULT_REPS, "15 повторений"));
    }
}