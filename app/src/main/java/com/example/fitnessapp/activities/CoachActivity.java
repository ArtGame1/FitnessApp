package com.example.fitnessapp.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;
import com.example.fitnessapp.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CoachActivity extends AppCompatActivity {

    private Button btnAddSession;
    private ImageView workBtn;
    private ImageView statsBtn;
    private ImageView settBtn;
    private ImageView chatBtn;

    //Массив типов тренировок для выпадающего списка
    private final String[] WORKOUT_TYPES = {
            "Силовая",
            "Кардио",
            "Йога",
            "Пилатес",
            "Кроссфит",
            "Функциональная",
            "Стретчинг",
            "Аэробика",
            "Бокс",
            "Танцы"
    };

    //Массив уровней сложности
    private final String[] DIFFICULTY_LEVELS = {
            "Начинающий",
            "Легкий",
            "Средний",
            "Сложный",
            "Профессиональный"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        btnAddSession = findViewById(R.id.btnAddSession);

        chatBtn = findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(v -> {
            checkAuthAndOpenChat();
        });

        btnAddSession.setOnClickListener(v -> {
            showAddWorkoutDialog();
        });

        workBtn = findViewById(R.id.workBtn);

        workBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutActivity.class);
            startActivity(intent);
        });

        statsBtn = findViewById(R.id.statsBtn);

        statsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
        });

        settBtn = findViewById(R.id.settBtn);

        settBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }

    private void checkAuthAndOpenChat() {
        //Проверяем, авторизован ли пользователь
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            //Пользователь авторизован - открываем чат
            Intent intent = new Intent(CoachActivity.this, ChatActivity.class);
            startActivity(intent);
        } else {
            //Пользователь не авторизован - показываем диалог
            showAuthDialog();
        }
    }

    private void showAuthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Авторизация")
                .setMessage("Для доступа к чату необходимо войти в аккаунт")
                .setPositiveButton("Войти", (dialog, which) -> {
                    //Переходим на экран логина
                    Intent intent = new Intent(CoachActivity.this, ChatActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showAddWorkoutDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        //1. Название - ручной ввод
        EditText etName = new EditText(this);
        etName.setHint("Введите название тренировки");
        layout.addView(etName);

        //2. Тип тренировки - Spinner
        android.widget.Spinner spinnerType = new android.widget.Spinner(this);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                WORKOUT_TYPES
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        layout.addView(spinnerType);

        //3. Длительность
        EditText etDuration = new EditText(this);
        etDuration.setHint("Длительность (минуты)");
        etDuration.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etDuration);

        //4. Сложность - Spinner
        android.widget.Spinner spinnerDifficulty = new android.widget.Spinner(this);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                DIFFICULTY_LEVELS
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        layout.addView(spinnerDifficulty);

        //5. Время
        EditText etTime = new EditText(this);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        etTime.setHint("Время (например: " + currentTime + ")");
        layout.addView(etTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Создание новой тренировки")
                .setView(layout)
                .setPositiveButton("Создать", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String type = spinnerType.getSelectedItem().toString();
                    String durationStr = etDuration.getText().toString().trim();
                    String difficulty = spinnerDifficulty.getSelectedItem().toString();
                    String time = etTime.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Введите название!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (durationStr.isEmpty()) {
                        Toast.makeText(this, "Введите длительность!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (time.isEmpty()) {
                        time = currentTime;
                    }

                    try {
                        int duration = Integer.parseInt(durationStr);

                        Intent intent = new Intent(this, WorkoutActivity.class);
                        intent.putExtra("workout_name", name);
                        intent.putExtra("workout_type", type);
                        intent.putExtra("workout_duration", duration);
                        intent.putExtra("workout_difficulty", difficulty);
                        intent.putExtra("workout_date",
                                new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
                        intent.putExtra("workout_time", time);
                        intent.putExtra("is_new_workout", true);

                        startActivity(intent);

                        Toast.makeText(this,
                                "Тренировка '" + name + "' добавлена!", Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Ошибка в длительности!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}