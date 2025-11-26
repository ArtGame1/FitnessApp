package com.example.fitnessapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;

public class StatsActivity extends AppCompatActivity {


    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "FitnessAppStats";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }


        setContentView(R.layout.activity_stats);


        //Инициализация SharedPreferences - хранилища настроек
        //MODE_PRIVATE означает, что статистика доступны только этому приложению
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        setupToolbar();             //Настройка верхней панели
    }

    /**
     * Настройка верхней панели (Toolbar)
     * Добавляет кнопку "Назад" и заголовок
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Статистика");
        }

        //УБРАТЬ кастомный обработчик - система сама обработает кнопку назад
        //toolbar.setNavigationOnClickListener(v -> { ... });
    }
}