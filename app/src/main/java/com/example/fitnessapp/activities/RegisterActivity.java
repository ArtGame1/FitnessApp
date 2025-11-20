package com.example.fitnessapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;


public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText; //Объявляет переменную для поля ввода имени.

    private EditText emailEditText; //Объявляет переменную для поля ввода email.
    private EditText phoneEditText; //Объявляет переменную для поля ввода телефона.
    private EditText passwordEditText; //Объявляет переменную для поля ввода пароля.
    private Button registerButton; //Объявляет переменную для кнопки "Зарегистрироваться"
    private TextView loginTextView; //Объявляет переменную для текста Login


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); //Устанавливает макет для этой активности.

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        nameEditText = findViewById(R.id.userEdt); //Ищет поле ввода имени по его ID и присваивает переменной.
        phoneEditText = findViewById(R.id.phoneEdt); //Ищет поле ввода телефона по его ID и присваивает переменной.
        emailEditText = findViewById(R.id.emailEdt); //Ищет поле ввода email по его ID и присваивает переменной.
        passwordEditText = findViewById(R.id.passEdt); //Ищет поле ввода пароля по его ID и присваивает переменной.
        registerButton = findViewById(R.id.signupBtn); //Ищет кнопку "Зарегистрироваться" по её ID и присваивает переменной.
        loginTextView = findViewById(R.id.textView4); //Ищет текст "Login" по его ID и присваивает переменной.

        //Обработчик нажатия на кнопку "Зарегистрироваться".
        registerButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString().trim(); //Получает текст из поля ввода имени и удаляет пробелы
            String email = emailEditText.getText().toString().trim(); //Получает текст из поля ввода email и удаляет пробелы
            String password = passwordEditText.getText().toString().trim(); //Получает текст из поля ввода пароля и удаляет пробелы
            String phone = phoneEditText.getText().toString().trim(); //Получает текст из поля ввода телефона и удаляет пробелы.

                    //Проверка, введено ли имя
                    if (TextUtils.isEmpty(name)) {
                        nameEditText.setError("Введите имя"); //Устанавливает ошибку в поле ввода имени, если оно пустое
                        nameEditText.requestFocus(); //Устанавливает фокус на поле ввода имени
                    }
                    //Проверка, введен ли email
                    else if (TextUtils.isEmpty(email)) {
                        emailEditText.setError("Введите email"); //Устанавливает ошибку в поле ввода email, если оно пустое
                        emailEditText.requestFocus(); //Устанавливает фокус на поле ввода email
                    }
                    //Проверка, введен ли телефон
                    else if (TextUtils.isEmpty(phone)) {
                        phoneEditText.setError("Введите номер телефона"); //Устанавливает ошибку в поле ввода телефона, если оно пустое
                        phoneEditText.requestFocus(); //Устанавливает фокус на поле ввода телефона
                    }
                    //Проверка, введен ли пароль
                    else if (TextUtils.isEmpty(password)) {
                        passwordEditText.setError("Введите пароль"); //Устанавливает ошибку в поле ввода пароля, если оно пустое
                        passwordEditText.requestFocus(); //Устанавливает фокус на поле ввода пароля
                    }
                    //Проверка, достаточно ли длинный пароль
                    else if (password.length() < 6) {
                        passwordEditText.setError("Пароль должен содержать не менее 6 символов"); //Устанавливает ошибку, если пароль слишком короткий
                        passwordEditText.requestFocus(); //Устанавливает фокус на поле ввода пароля
                    }
                    //Проверка, содержит ли пароль хотя бы одну цифру
                    else if (!containsDigit(password)) {
                        passwordEditText.setError("Пароль должен содержать хотя бы одну цифру"); //Устанавливает ошибку, если пароль не содержит цифр
                        passwordEditText.requestFocus(); //Устанавливает фокус на поле ввода пароля
                    }
                    //Если все проверки пройдены, переход на экран входа
                    else {
                        // Здесь можно сохранить данные пользователя (имя, email, телефон, пароль)
                        // Пока просто переходим на экран входа
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); //Создает Intent для перехода на LoginActivity
                        startActivity(intent); //Запускаем LoginActivity
                        //finish(); //Завершение текущей активности
                    }
        });

        //Обработчик нажатия на текст "Войти"
        loginTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class); //Создает Intent для перехода на LoginActivity
            startActivity(intent); //Запускаем LoginActivity
            //finish(); //Завершение текущей активности
        });
    }


    //Метод для проверки наличия хотя бы одной цифры в пароле
    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) { //Проходит по каждому символу в пароле
            if (Character.isDigit(c)) { //Проверяет, является ли символ цифрой
                return true; //Возвращает true, если найдена цифра
            }
        }
        return false; //Возвращает false, если цифр не найдено
    }
}
