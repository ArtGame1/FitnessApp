package com.example.fitnessapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;

public class SettingsActivity extends AppCompatActivity {

    //Хранилище настроек приложения
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FitnessAppSettings";

    //Ключи для сохранения настроек в SharedPreferences
    //Каждый ключ соответствует определенной настройке
    private static final String KEY_NOTIFICATIONS = "notifications";      //Уведомления
    private static final String KEY_SOUND = "sound";                      //Звук
    private static final String KEY_DARK_MODE = "dark_mode";              //Темная тема
    private static final String KEY_AUTO_PAUSE = "auto_pause";            //Автопауза
    private static final String KEY_REST_TIME = "rest_time";              //Время отдыха
    private static final String KEY_DEFAULT_REPS = "default_reps";        //Повторения по умолчанию
    private static final String KEY_DIFFICULTY = "difficulty_level";      //Уровень сложности
    private static final String KEY_UNITS_METRIC = "units_metric";        //Единицы измерения

    //Переменные для элементов интерфейса
    private Switch switchNotifications, switchSound, switchDarkMode, switchAutoPause;
    private Button btnSetRestTime, btnSetReps, btnResetExercises, btnExportData, btnSaveSettings;
    private TextView tvRestTime, tvDefaultReps, tvDifficulty;
    private Spinner spinnerDifficulty;
    private RadioGroup radioGroupUnits;
    private RadioButton radioMetric, radioImperial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        setContentView(R.layout.activity_settings);

        //Инициализация SharedPreferences - хранилища настроек
        //MODE_PRIVATE означает, что настройки доступны только этому приложению
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Последовательная настройка всех компонентов активности
        setupToolbar();             //Настройка верхней панели
        initViews();                //Инициализация всех View элементов
        setupSwitches();            //Настройка переключателей (Switch)
        setupButtons();             //Настройка кнопок
        setupDifficultySpinner();   //Настройка выпадающего списка сложности
        setupUnitsRadioGroup();     //Настройка радиокнопок единиц измерения
        loadSettings();             //Загрузка сохраненных настроек
    }

    /**
     * Настройка верхней панели (Toolbar)
     * Добавляет кнопку "Назад" и заголовок
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Добавить кнопку назад в Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Настройки");
        }

        //Обработчик клика по кнопке назад
        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }

    /**
     * Инициализация всех View элементов из layout
     * Связываем переменные Java с элементами в XML
     */
    private void initViews() {
        //Switches (переключатели) - находим по ID из XML
        switchNotifications = findViewById(R.id.switch_notifications);  //Уведомления
        switchSound = findViewById(R.id.switch_sound);                  //Звук
        switchDarkMode = findViewById(R.id.switch_dark_mode);           //Темная тема
        switchAutoPause = findViewById(R.id.switch_auto_pause);         //Автопауза

        //TextViews (текстовые поля) - для отображения текущих значений
        tvRestTime = findViewById(R.id.tv_rest_time);          //Время отдыха
        tvDefaultReps = findViewById(R.id.tv_default_reps);    //Повторения по умолчанию
        tvDifficulty = findViewById(R.id.tv_difficulty);       //Уровень сложности

        //Buttons (кнопки) - для различных действий
        btnSetRestTime = findViewById(R.id.btn_set_rest_time);        //Изменить время отдыха
        btnSetReps = findViewById(R.id.btn_set_reps);                //Изменить повторения
        btnResetExercises = findViewById(R.id.btn_reset_exercises);   //Сбросить прогресс
        btnExportData = findViewById(R.id.btn_export_data);          //Экспорт данных
        btnSaveSettings = findViewById(R.id.btn_save_settings);      //Сохранить настройки

        //Spinner (выпадающий список) и RadioGroup (группа радиокнопок)
        spinnerDifficulty = findViewById(R.id.spinner_difficulty);    //Список сложности
        radioGroupUnits = findViewById(R.id.radio_group_units);       //Группа единиц измерения
        radioMetric = findViewById(R.id.radio_metric);                //Метрическая система
        radioImperial = findViewById(R.id.radio_imperial);            //Имперская система
    }

    /**
     * Настройка обработчиков для Switch элементов
     * Каждый Switch сохраняет свое состояние при изменении
     */
    private void setupSwitches() {
        //Уведомления - сохраняем состояние при переключении
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting(KEY_NOTIFICATIONS, isChecked);
        });

        //Звук - сохраняем состояние при переключении
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting(KEY_SOUND, isChecked);
        });

        //Темная тема - сохраняем и применяем тему
        switchDarkMode.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting(KEY_DARK_MODE, isChecked);
            applyTheme(isChecked);  //Применяем выбранную тему
        });

        //Автопауза - сохраняем состояние при переключении
        switchAutoPause.setOnCheckedChangeListener((buttonView, isChecked) -> {
            saveSetting(KEY_AUTO_PAUSE, isChecked);
        });
    }

    /**
     * Настройка обработчиков нажатия для кнопок
     * Каждая кнопка выполняет определенное действие
     */
    private void setupButtons() {
        //Кнопка установки времени отдыха - показывает диалог выбора
        btnSetRestTime.setOnClickListener(v -> showRestTimeDialog());

        //Кнопка установки повторений - показывает диалог выбора
        btnSetReps.setOnClickListener(v -> showRepsDialog());

        //Кнопка сброса прогресса - показывает подтверждающий диалог
        btnResetExercises.setOnClickListener(v -> showResetConfirmation());

        //Кнопка экспорта данных - запускает процесс экспорта
        btnExportData.setOnClickListener(v -> exportWorkoutData());

        //Кнопка сохранения настроек - сохраняет все и закрывает активность
        btnSaveSettings.setOnClickListener(v -> saveAllSettings());
    }

    /**
     * Настройка выпадающего списка (Spinner) для выбора уровня сложности
     * Данные берутся из массива difficulty_levels в arrays.xml
     */
    private void setupDifficultySpinner() {
        //Создаем адаптер для Spinner из массива строк в resources
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.difficulty_levels,  //Массив из arrays.xml
                android.R.layout.simple_spinner_item  //Стандартный layout для элемента
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(adapter);

        //Слушатель изменений выбора в Spinner
        spinnerDifficulty.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                //При выборе элемента сохраняем его и обновляем TextView
                String selectedDifficulty = parent.getItemAtPosition(position).toString();
                saveSetting(KEY_DIFFICULTY, selectedDifficulty);
                tvDifficulty.setText(selectedDifficulty);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                //Ничего не выбрано - ничего не делаем
            }
        });
    }

    /**
     * Настройка радиогруппы для выбора системы единиц измерения
     */
    private void setupUnitsRadioGroup() {
        radioGroupUnits.setOnCheckedChangeListener((group, checkedId) -> {
            //Определяем какая система выбрана и сохраняем
            boolean isMetric = checkedId == R.id.radio_metric;
            saveSetting(KEY_UNITS_METRIC, isMetric);
        });
    }

    /**
     * Показывает диалог для выбора времени отдыха между подходами
     */
    private void showRestTimeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Время отдыха");
        builder.setMessage("Выберите продолжительность отдыха между подходами:");

        //Варианты времени отдыха
        final String[] restTimes = {"15 секунд", "30 секунд", "45 секунд", "60 секунд", "90 секунд"};

        //Обработчик выбора варианта
        builder.setItems(restTimes, (dialog, which) -> {
            String selectedTime = restTimes[which];
            tvRestTime.setText(selectedTime);              //Обновляем TextView
            saveSetting(KEY_REST_TIME, selectedTime);      //Сохраняем в настройки
        });

        builder.setNegativeButton("Отмена", null);  //Кнопка отмены
        builder.show();  //Показываем диалог
    }

    /**
     * Показывает диалог для выбора количества повторений по умолчанию
     */
    private void showRepsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Количество повторений");
        builder.setMessage("Установите количество повторений по умолчанию:");

        //Варианты количества повторений
        final String[] repsOptions = {"10 повторений", "12 повторений", "15 повторений", "20 повторений", "25 повторений"};

        builder.setItems(repsOptions, (dialog, which) -> {
            String selectedReps = repsOptions[which];
            tvDefaultReps.setText(selectedReps);           //Обновляем TextView
            saveSetting(KEY_DEFAULT_REPS, selectedReps);   //Сохраняем в настройки
        });

        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    /**
     * Показывает диалог подтверждения сброса всего прогресса
     * Защита от случайного сброса
     */
    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Сброс прогресса")
                .setMessage("Вы уверены, что хотите сбросить весь прогресс тренировок? Это действие нельзя отменить.")
                .setPositiveButton("Сбросить", (dialog, which) -> {
                    resetAllProgress();  // Выполняем сброс
                    Toast.makeText(this, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)  //Отмена - ничего не делаем
                .show();
    }

    /**
     * Сбрасывает весь прогресс тренировок
     * Очищает SharedPreferences с прогрессом
     */
    private void resetAllProgress() {
        //Получаем отдельное хранилище для прогресса тренировок
        SharedPreferences progressPrefs = getSharedPreferences("WorkoutProgress", MODE_PRIVATE);
        SharedPreferences.Editor editor = progressPrefs.edit();
        editor.clear();    //Очищаем все данные прогресса
        editor.apply();    //Применяем изменения

        Toast.makeText(this, "Весь прогресс тренировок сброшен", Toast.LENGTH_LONG).show();
    }

    /**
     * Экспорт данных тренировок в файл
     * В текущей реализации просто показывает сообщение
     */
    private void exportWorkoutData() {
        //Заглушка - в реальном приложении здесь будет логика экспорта
        Toast.makeText(this, "Функция экспорта в разработке", Toast.LENGTH_SHORT).show();

        /* РЕАЛЬНАЯ РЕАЛИЗАЦИЯ МОГЛА БЫ ВЫГЛЯДЕТЬ ТАК:
        try {
            // Создание файла с данными тренировок
            String data = generateWorkoutData();
            saveToFile(data);
            Toast.makeText(this, "Данные экспортированы", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Ошибка экспорта", Toast.LENGTH_SHORT).show();
        }
        */
    }

    /**
     * Сохраняет все настройки и закрывает активность
     * В данном случае настройки сохраняются автоматически при изменении,
     * поэтому просто показываем сообщение и закрываем
     */
    private void saveAllSettings() {
        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        finish();  //Закрываем активность
    }

    /**
     * Сохраняет настройку темы без немедленного применения
     * @param isDarkMode true - темная тема, false - светлая
     */
    private void applyTheme(boolean isDarkMode) {
        //Просто сохраняем настройку темы
        saveSetting(KEY_DARK_MODE, isDarkMode);

        //Информируем пользователя
        String message = isDarkMode ?
                "🌙 Темная тема сохранена. Закройте и откройте приложение для применения." :
                "☀️ Светлая тема сохранена. Закройте и откройте приложение для применения.";

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Загружает сохраненные настройки и устанавливает их в элементы интерфейса
     * Вызывается при создании активности для восстановления состояния
     */
    private void loadSettings() {
        //Загрузка настроек Switch элементов
        //Второй параметр - значение по умолчанию если настройка не найдена
        switchNotifications.setChecked(sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true));
        switchSound.setChecked(sharedPreferences.getBoolean(KEY_SOUND, true));
        switchDarkMode.setChecked(sharedPreferences.getBoolean(KEY_DARK_MODE, false));
        switchAutoPause.setChecked(sharedPreferences.getBoolean(KEY_AUTO_PAUSE, true));

        //Время отдыха - загружаем строку или используем значение по умолчанию
        String restTime = sharedPreferences.getString(KEY_REST_TIME, "30 секунд");
        tvRestTime.setText(restTime);

        //Повторения по умолчанию
        String defaultReps = sharedPreferences.getString(KEY_DEFAULT_REPS, "15 повторений");
        tvDefaultReps.setText(defaultReps);

        //Уровень сложности
        String difficulty = sharedPreferences.getString(KEY_DIFFICULTY, "Средний");
        tvDifficulty.setText(difficulty);

        //Установка выбранного элемента в Spinner
        ArrayAdapter adapter = (ArrayAdapter) spinnerDifficulty.getAdapter();
        int position = adapter.getPosition(difficulty);
        if (position >= 0) {
            spinnerDifficulty.setSelection(position);  //Выбираем сохраненную сложность
        }

        // Единицы измерения - устанавливаем соответствующую радиокнопку
        boolean isMetric = sharedPreferences.getBoolean(KEY_UNITS_METRIC, true);
        if (isMetric) {
            radioMetric.setChecked(true);      //Метрическая система
        } else {
            radioImperial.setChecked(true);    //Имперская система
        }
    }

    /**
     * Сохраняет строковую настройку в SharedPreferences
     * @param key ключ настройки
     * @param value строковое значение
     */
    private void saveSetting(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();  //apply() - асинхронное сохранение (лучше чем commit())
    }

    /**
     * Сохражает булеву настройку в SharedPreferences
     * @param key ключ настройки
     * @param value булево значение
     */
    private void saveSetting(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * Сохраняет целочисленную настройку в SharedPreferences
     * @param key ключ настройки
     * @param value целочисленное значение
     */
    private void saveSetting(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * Генерирует данные тренировок для экспорта
     * В реальном приложении здесь будет логика получения данных из БД
     * @return строка с данными в формате CSV
     */
    private String generateWorkoutData() {
        //Здесь генерируйте данные в формате CSV или JSON
        StringBuilder data = new StringBuilder();
        data.append("Дата,Упражнение,Подходы,Повторения,Вес\n");
        //Добавьте реальные данные из базы данных
        return data.toString();
    }
}