package com.example.fitnessapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;

public class AccountActivity extends AppCompatActivity {

    private Button btn_back_to_main_page; //Объявляем переменную для кнопки
    //"Вернуться на главную страницу"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Ищет кнопку по её ID в макете и присваивает её переменной btn_back_to_main_page.
        btn_back_to_main_page = findViewById(R.id.btn_back_to_main_page);

        btn_back_to_main_page.setOnClickListener(v -> { //Устанавливает слушатель нажатия
            //для кнопки "Вернуться на главную страницу"
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            //Создает новый Intent для перехода на MainActivity.
            startActivity(intent); //Запускает активность MainActivity.
        });
    }
}