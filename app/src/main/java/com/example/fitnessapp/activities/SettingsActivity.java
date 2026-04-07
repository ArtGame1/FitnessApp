package com.example.fitnessapp.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;
import com.example.fitnessapp.utils.NotificationHelper;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FitnessAppSettings";

    // Ключи для настроек
    private static final String KEY_NOTIFICATIONS = "notifications";
    private static final String KEY_SOUND = "sound";
    private static final String KEY_AUTO_PAUSE = "auto_pause";
    private static final String KEY_REST_TIME = "rest_time";
    private static final String KEY_DEFAULT_REPS = "default_reps";

    // UI элементы
    private Switch switchNotifications, switchSound, switchAutoPause;
    private Button btnSetRestTime, btnSetReps, btnResetExercises, btnExportData, btnSaveSettings, btnCancelSettings;
    private TextView tvRestTime, tvDefaultReps;

    // Временные переменные
    private boolean tempNotifications, tempSound, tempAutoPause;
    private String tempRestTime, tempDefaultReps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        setContentView(R.layout.activity_settings);

        // Создаем канал уведомлений
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
            setTitle("Настройки");
        }
        toolbar.setNavigationOnClickListener(v -> {
            if (hasUnsavedChanges()) {
                showUnsavedChangesDialog();
            } else {
                finish();
            }
        });
    }

    private void initViews() {
        switchNotifications = findViewById(R.id.switch_notifications);
        switchSound = findViewById(R.id.switch_sound);
        switchAutoPause = findViewById(R.id.switch_auto_pause);

        tvRestTime = findViewById(R.id.tv_rest_time);
        tvDefaultReps = findViewById(R.id.tv_default_reps);

        btnSetRestTime = findViewById(R.id.btn_set_rest_time);
        btnSetReps = findViewById(R.id.btn_set_reps);
        btnResetExercises = findViewById(R.id.btn_reset_exercises);
        btnExportData = findViewById(R.id.btn_export_data);
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
        // Обновляем временные переменные
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            tempNotifications = isChecked;
            // Если уведомления включают/выключают, показываем подсказку
            if (isChecked) {
                Toast.makeText(this, "Уведомления будут приходить в 19:00", Toast.LENGTH_LONG).show();
            }
        });

        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> tempSound = isChecked);
        switchAutoPause.setOnCheckedChangeListener((buttonView, isChecked) -> tempAutoPause = isChecked);

        btnSetRestTime.setOnClickListener(v -> showRestTimeDialog());
        btnSetReps.setOnClickListener(v -> showRepsDialog());
        btnResetExercises.setOnClickListener(v -> showResetConfirmation());
        btnExportData.setOnClickListener(v -> exportWorkoutData());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Время отдыха между подходами");
        final String[] restTimes = {"15 секунд", "30 секунд", "45 секунд", "60 секунд", "90 секунд"};
        builder.setItems(restTimes, (dialog, which) -> {
            tempRestTime = restTimes[which];
            tvRestTime.setText(tempRestTime);
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void showRepsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Повторения по умолчанию");
        final String[] repsOptions = {"8 повторений", "10 повторений", "12 повторений", "15 повторений", "20 повторений"};
        builder.setItems(repsOptions, (dialog, which) -> {
            tempDefaultReps = repsOptions[which];
            tvDefaultReps.setText(tempDefaultReps);
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void saveAllSettings() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_NOTIFICATIONS, tempNotifications);
        editor.putBoolean(KEY_SOUND, tempSound);
        editor.putBoolean(KEY_AUTO_PAUSE, tempAutoPause);
        editor.putString(KEY_REST_TIME, tempRestTime);
        editor.putString(KEY_DEFAULT_REPS, tempDefaultReps);
        editor.apply();

        // Управляем уведомлениями
        manageNotifications();

        Toast.makeText(this, "Настройки сохранены", Toast.LENGTH_SHORT).show();
        finish();
    }

    // Управление уведомлениями (включение/выключение)
    private void manageNotifications() {
        if (tempNotifications) {
            // Включаем уведомления
            NotificationHelper.scheduleDailyReminder(this);

            // Для Android 12+ проверяем разрешение
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!NotificationHelper.checkExactAlarmPermission(this)) {
                    new AlertDialog.Builder(this)
                            .setTitle("Разрешение на уведомления")
                            .setMessage("Для точных напоминаний о тренировках нужно разрешение. Хотите его включить?")
                            .setPositiveButton("Включить", (dialog, which) -> {
                                NotificationHelper.requestExactAlarmPermission(this);
                            })
                            .setNegativeButton("Отмена", null)
                            .show();
                }
            }
        } else {
            // Выключаем уведомления
            NotificationHelper.cancelReminder(this);
        }
    }

    private void showResetConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Сброс прогресса")
                .setMessage("Вы уверены? Все данные о тренировках будут удалены безвозвратно.")
                .setPositiveButton("Сбросить", (dialog, which) -> {
                    SharedPreferences progressPrefs = getSharedPreferences("WorkoutProgress", MODE_PRIVATE);
                    progressPrefs.edit().clear().apply();
                    Toast.makeText(this, "Прогресс сброшен", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void exportWorkoutData() {
        Toast.makeText(this, "Экспорт будет доступен в следующей версии", Toast.LENGTH_SHORT).show();
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