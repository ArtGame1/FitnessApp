// Объявление пакета (папки), где находится этот класс
package com.example.fitnessapp.activities;

// Импорт класса для работы с SharedPreferences (хранение настроек)
import android.content.SharedPreferences;
// Импорт класса для проверки версии Android
import android.os.Build;
// Импорт класса для работы с жизненным циклом активности
import android.os.Bundle;
// Импорт класса для кнопки
import android.widget.Button;
// Импорт класса для переключателя Switch
import android.widget.Switch;
// Импорт класса для текстового поля
import android.widget.TextView;
// Импорт класса для всплывающих сообщений
import android.widget.Toast;

// Импорт класса для создания диалоговых окон
import androidx.appcompat.app.AlertDialog;
// Импорт базового класса для всех активностей
import androidx.appcompat.app.AppCompatActivity;
// Импорт класса для верхней панели инструментов
import androidx.appcompat.widget.Toolbar;
// Импорт класса для работы с цветами
import androidx.core.content.ContextCompat;

// Импорт ресурсов приложения (R - автоматический класс)
import com.example.fitnessapp.R;
// Импорт нашего класса для работы с уведомлениями
import com.example.fitnessapp.utils.NotificationHelper;

// Объявление класса SettingsActivity, который наследуется от AppCompatActivity
public class SettingsActivity extends AppCompatActivity {

    // Объявление переменной для хранения настроек
    private SharedPreferences sharedPreferences;
    // Константа с именем файла настроек (статическая - одна на весь класс)
    private static final String PREFS_NAME = "FitnessAppSettings";

    // Ключи для настроек (константы, чтобы не ошибиться в написании)
    private static final String KEY_NOTIFICATIONS = "notifications";  // Ключ для уведомлений
    private static final String KEY_SOUND = "sound";                  // Ключ для звука
    private static final String KEY_AUTO_PAUSE = "auto_pause";        // Ключ для автопаузы
    private static final String KEY_REST_TIME = "rest_time";          // Ключ для времени отдыха
    private static final String KEY_DEFAULT_REPS = "default_reps";    // Ключ для повторений

    // UI элементы (экранные компоненты)
    private Switch switchNotifications, switchSound, switchAutoPause;  // Переключатели
    private Button btnSetRestTime, btnSetReps, btnResetExercises, btnExportData, btnSaveSettings, btnCancelSettings; // Кнопки
    private TextView tvRestTime, tvDefaultReps;  // Текстовые поля для отображения значений

    // Временные переменные для хранения НЕСОХРАНЕННЫХ изменений
    private boolean tempNotifications, tempSound, tempAutoPause;  // Булевы значения
    private String tempRestTime, tempDefaultReps;                 // Строковые значения

    // Метод onCreate - вызывается при создании активности
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Вызов родительского метода (обязательно)
        super.onCreate(savedInstanceState);

        // Проверка: если версия Android 5.0 (Lollipop) или выше
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            // Устанавливаем цвет статус бара (верхняя панель с часами и батареей)
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        // Устанавливаем layout (экран) для этой активности из файла activity_settings.xml
        setContentView(R.layout.activity_settings);

        // Создаем канал уведомлений (нужно для Android 8+)
        NotificationHelper.createNotificationChannel(this);

        // Инициализируем SharedPreferences (получаем доступ к файлу настроек)
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Вызываем методы настройки (разбили код на логические блоки)
        setupToolbar();       // Настройка верхней панели
        initViews();         // Поиск всех элементов на экране
        loadSettingsToTemp(); // Загрузка настроек во временные переменные
        displayTempSettings(); // Отображение настроек на экране
        setupListeners();     // Настройка обработчиков нажатий
    }

    // Метод настройки верхней панели (Toolbar)
    private void setupToolbar() {
        // Находим Toolbar на экране по его ID
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Устанавливаем Toolbar как ActionBar (верхняя панель)
        setSupportActionBar(toolbar);
        // Проверяем, что ActionBar существует (не null)
        if (getSupportActionBar() != null) {
            // Включаем кнопку "Назад" (стрелка влево)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            // Устанавливаем заголовок "Настройки"
            setTitle("Настройки");
        }
        // Устанавливаем обработчик нажатия на кнопку "Назад"
        toolbar.setNavigationOnClickListener(v -> {
            // Если есть несохраненные изменения
            if (hasUnsavedChanges()) {
                // Показываем диалог с вопросом о сохранении
                showUnsavedChangesDialog();
            } else {
                // Если изменений нет - просто закрываем активность
                finish();
            }
        });
    }

    // Метод инициализации всех View элементов (поиск на экране)
    private void initViews() {
        // Находим переключатели по ID из XML файла
        switchNotifications = findViewById(R.id.switch_notifications);
        switchSound = findViewById(R.id.switch_sound);
        switchAutoPause = findViewById(R.id.switch_auto_pause);

        // Находим текстовые поля по ID
        tvRestTime = findViewById(R.id.tv_rest_time);
        tvDefaultReps = findViewById(R.id.tv_default_reps);

        // Находим кнопки по ID
        btnSetRestTime = findViewById(R.id.btn_set_rest_time);
        btnSetReps = findViewById(R.id.btn_set_reps);
        btnResetExercises = findViewById(R.id.btn_reset_exercises);
        btnExportData = findViewById(R.id.btn_export_data);
        btnSaveSettings = findViewById(R.id.btn_save_settings);
        btnCancelSettings = findViewById(R.id.btn_cancel_settings);
    }

    // Метод загрузки сохраненных настроек во временные переменные
    private void loadSettingsToTemp() {
        // Загружаем уведомления (второй параметр - значение по умолчанию)
        tempNotifications = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true);
        // Загружаем звук (по умолчанию true)
        tempSound = sharedPreferences.getBoolean(KEY_SOUND, true);
        // Загружаем автопаузу (по умолчанию true)
        tempAutoPause = sharedPreferences.getBoolean(KEY_AUTO_PAUSE, true);
        // Загружаем время отдыха (по умолчанию "30 секунд")
        tempRestTime = sharedPreferences.getString(KEY_REST_TIME, "30 секунд");
        // Загружаем повторения (по умолчанию "15 повторений")
        tempDefaultReps = sharedPreferences.getString(KEY_DEFAULT_REPS, "15 повторений");
    }

    // Метод отображения временных настроек на экране
    private void displayTempSettings() {
        // Устанавливаем состояние переключателя уведомлений
        switchNotifications.setChecked(tempNotifications);
        // Устанавливаем состояние переключателя звука
        switchSound.setChecked(tempSound);
        // Устанавливаем состояние переключателя автопаузы
        switchAutoPause.setChecked(tempAutoPause);
        // Устанавливаем текст для времени отдыха
        tvRestTime.setText(tempRestTime);
        // Устанавливаем текст для повторений
        tvDefaultReps.setText(tempDefaultReps);
    }

    // Метод настройки обработчиков событий
    private void setupListeners() {
        // Обработчик изменения переключателя уведомлений
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Сохраняем новое состояние во временную переменную
            tempNotifications = isChecked;
            // Если уведомления включены
            if (isChecked) {
                // Показываем подсказку о времени уведомлений
                Toast.makeText(this, "Уведомления будут приходить в 19:00", Toast.LENGTH_LONG).show();
            }
        });

        // Обработчик изменения переключателя звука
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) ->
                tempSound = isChecked  // Сохраняем новое состояние
        );

        // Обработчик изменения переключателя автопаузы
        switchAutoPause.setOnCheckedChangeListener((buttonView, isChecked) ->
                tempAutoPause = isChecked  // Сохраняем новое состояние
        );

        // Обработчик нажатия на кнопку "Изменить" время отдыха
        btnSetRestTime.setOnClickListener(v -> showRestTimeDialog());

        // Обработчик нажатия на кнопку "Изменить" повторения
        btnSetReps.setOnClickListener(v -> showRepsDialog());

        // Обработчик нажатия на кнопку сброса прогресса
        btnResetExercises.setOnClickListener(v -> showResetConfirmation());

        // Обработчик нажатия на кнопку экспорта
        btnExportData.setOnClickListener(v -> exportWorkoutData());

        // Обработчик нажатия на кнопку сохранения
        btnSaveSettings.setOnClickListener(v -> saveAllSettings());

        // Обработчик нажатия на кнопку отмены
        btnCancelSettings.setOnClickListener(v -> {
            // Если есть несохраненные изменения
            if (hasUnsavedChanges()) {
                // Показываем диалог
                showUnsavedChangesDialog();
            } else {
                // Если изменений нет - закрываем
                finish();
            }
        });
    }

    // Метод показа диалога выбора времени отдыха
    private void showRestTimeDialog() {
        // Создаем строитель диалога
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Устанавливаем заголовок
        builder.setTitle("Время отдыха между подходами");
        // Массив вариантов времени отдыха
        final String[] restTimes = {"15 секунд", "30 секунд", "45 секунд", "60 секунд", "90 секунд"};
        // Устанавливаем список вариантов
        builder.setItems(restTimes, (dialog, which) -> {
            // Сохраняем выбранное значение
            tempRestTime = restTimes[which];
            // Обновляем текст на экране
            tvRestTime.setText(tempRestTime);
        });
        // Добавляем кнопку "Отмена"
        builder.setNegativeButton("Отмена", null);
        // Показываем диалог
        builder.show();
    }

    // Метод показа диалога выбора повторений
    private void showRepsDialog() {
        // Создаем строитель диалога
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Устанавливаем заголовок
        builder.setTitle("Повторения по умолчанию");
        // Массив вариантов повторений
        final String[] repsOptions = {"8 повторений", "10 повторений", "12 повторений", "15 повторений", "20 повторений"};
        // Устанавливаем список вариантов
        builder.setItems(repsOptions, (dialog, which) -> {
            // Сохраняем выбранное значение
            tempDefaultReps = repsOptions[which];
            // Обновляем текст на экране
            tvDefaultReps.setText(tempDefaultReps);
        });
        // Добавляем кнопку "Отмена"
        builder.setNegativeButton("Отмена", null);
        // Показываем диалог
        builder.show();
    }

    // Метод сохранения всех настроек
    private void saveAllSettings() {
        // Получаем редактор для изменения SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Сохраняем состояние уведомлений
        editor.putBoolean(KEY_NOTIFICATIONS, tempNotifications);
        // Сохраняем состояние звука
        editor.putBoolean(KEY_SOUND, tempSound);
        // Сохраняем состояние автопаузы
        editor.putBoolean(KEY_AUTO_PAUSE, tempAutoPause);
        // Сохраняем время отдыха
        editor.putString(KEY_REST_TIME, tempRestTime);
        // Сохраняем повторения
        editor.putString(KEY_DEFAULT_REPS, tempDefaultReps);
        // Применяем изменения (асинхронное сохранение)
        editor.apply();

        // Управляем уведомлениями (включаем или выключаем)
        manageNotifications();

        // Показываем сообщение об успешном сохранении
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        // Закрываем активность
        finish();
    }

    // Метод управления уведомлениями (включение/выключение)
    private void manageNotifications() {
        // Если уведомления включены
        if (tempNotifications) {
            // Запускаем планировщик ежедневных уведомлений
            NotificationHelper.scheduleDailyReminder(this);

            // Проверяем для Android 12 и выше
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                // Если нет разрешения на точные уведомления
                if (!NotificationHelper.checkExactAlarmPermission(this)) {
                    // Создаем диалог запроса разрешения
                    new AlertDialog.Builder(this)
                            .setTitle("Разрешение на уведомления")  // Заголовок
                            .setMessage("Для точных напоминаний о тренировках нужно разрешение. Хотите его включить?")  // Текст
                            .setPositiveButton("Включить", (dialog, which) -> {  // Кнопка "Включить"
                                // Открываем настройки для разрешения
                                NotificationHelper.requestExactAlarmPermission(this);
                            })
                            .setNegativeButton("Отмена", null)  // Кнопка "Отмена"
                            .show();  // Показываем диалог
                }
            }
        } else {
            // Если уведомления выключены - отменяем все уведомления
            NotificationHelper.cancelReminder(this);
        }
    }

    // Метод показа подтверждения сброса прогресса
    private void showResetConfirmation() {
        // Создаем диалог
        new AlertDialog.Builder(this)
                .setTitle("Сброс прогресса")  // Заголовок
                .setMessage("Вы уверены? Все данные о тренировках будут удалены безвозвратно.")  // Сообщение
                .setPositiveButton("Сбросить", (dialog, which) -> {  // Кнопка "Сбросить"
                    // Получаем доступ к хранилищу прогресса
                    SharedPreferences progressPrefs = getSharedPreferences("WorkoutProgress", MODE_PRIVATE);
                    // Очищаем все данные
                    progressPrefs.edit().clear().apply();
                    // Показываем сообщение
                    Toast.makeText(this, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)  // Кнопка "Отмена"
                .show();  // Показываем диалог
    }

    // Метод экспорта данных тренировок
    private void exportWorkoutData() {
        // Показываем сообщение, что функция в разработке
        Toast.makeText(this, "Экспорт будет доступен в следующей версии", Toast.LENGTH_SHORT).show();
    }

    // Метод показа диалога о несохраненных изменениях
    private void showUnsavedChangesDialog() {
        // Создаем диалог
        new AlertDialog.Builder(this)
                .setTitle("Несохраненные изменения")  // Заголовок
                .setMessage("Сохранить изменения?")   // Сообщение
                .setPositiveButton("Сохранить", (dialog, which) -> saveAllSettings())  // Сохранить и закрыть
                .setNegativeButton("Не сохранять", (dialog, which) -> finish())  // Закрыть без сохранения
                .setNeutralButton("Отмена", null)  // Отмена (ничего не делать)
                .show();  // Показываем диалог
    }

    // Метод, вызываемый при нажатии кнопки "Назад" (системной)
    @Override
    public void onBackPressed() {
        // Если есть несохраненные изменения
        if (hasUnsavedChanges()) {
            // Показываем диалог
            showUnsavedChangesDialog();
        } else {
            // Если изменений нет - вызываем родительский метод (закрытие)
            super.onBackPressed();
        }
    }

    // Метод проверки наличия несохраненных изменений
    private boolean hasUnsavedChanges() {
        // Сравниваем временные значения с сохраненными
        return tempNotifications != sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true) ||  // Уведомления изменились?
                tempSound != sharedPreferences.getBoolean(KEY_SOUND, true) ||  // Звук изменился?
                tempAutoPause != sharedPreferences.getBoolean(KEY_AUTO_PAUSE, true) ||  // Автопауза изменилась?
                !tempRestTime.equals(sharedPreferences.getString(KEY_REST_TIME, "30 секунд")) ||  // Время отдыха изменилось?
                !tempDefaultReps.equals(sharedPreferences.getString(KEY_DEFAULT_REPS, "15 повторений"));  // Повторения изменились?
    }
}  // Конец класса SettingsActivity