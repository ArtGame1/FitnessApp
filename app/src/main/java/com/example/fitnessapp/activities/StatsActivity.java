package com.example.fitnessapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;

public class StatsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FitnessAppStats";
    private static final String KEY_STARS_COUNT = "stars_count";

    //UI элементы для звезд
    private ImageView star1, star2, star3, star4, star5;
    private TextView starsCountText, starsProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        setContentView(R.layout.activity_stats);

        //Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Инициализация UI элементов
        initViews();

        //Загрузка и отображение звезд
        loadAndDisplayStars();

        setupToolbar();
    }

    /**
     * Инициализация UI элементов
     */
    private void initViews() {
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        starsCountText = findViewById(R.id.stars_count_text);
        starsProgressText = findViewById(R.id.stars_progress_text);
    }

    /**
     * Загрузка и отображение количества звезд
     */
    private void loadAndDisplayStars() {
        int starsCount = getStarsCount();
        updateStarsDisplay(starsCount);
    }

    /**
     * Обновление отображения звезд
     */
    private void updateStarsDisplay(int starsCount) {
        // Устанавливаем заполненные звезды
        int starFilled = R.drawable.ic_star_filled; //Заполненная звезда
        int starEmpty = R.drawable.ic_star_empty;   //Пустая звезда

        star1.setImageResource(starsCount >= 1 ? starFilled : starEmpty);
        star2.setImageResource(starsCount >= 2 ? starFilled : starEmpty);
        star3.setImageResource(starsCount >= 3 ? starFilled : starEmpty);
        star4.setImageResource(starsCount >= 4 ? starFilled : starEmpty);
        star5.setImageResource(starsCount >= 5 ? starFilled : starEmpty);

        // Обновляем тексты
        starsCountText.setText("Звезд: " + starsCount);
        starsProgressText.setText("Собрано " + starsCount + " из 5 звезд");
    }

    /**
     * Получение количества звезд из SharedPreferences
     */
    public int getStarsCount() {
        return sharedPreferences.getInt(KEY_STARS_COUNT, 0);
    }

    /**
     * Добавление звезды (вызывается из ExerciseActivity)
     */
    public void addStar() {
        int currentStars = getStarsCount();
        if (currentStars < 5) {
            currentStars++;
            saveStarsCount(currentStars);
            updateStarsDisplay(currentStars);
        }
    }

    /**
     * Сохранение количества звезд в SharedPreferences
     */
    private void saveStarsCount(int starsCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_STARS_COUNT, starsCount);
        editor.apply();
    }

    /**
     * Сброс звезд (для тестирования)
     */
    public void resetStars() {
        saveStarsCount(0);
        updateStarsDisplay(0);
    }

    /**
     * Настройка верхней панели (Toolbar)
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Статистика");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем отображение звезд при возвращении на экран
        loadAndDisplayStars();
    }
}