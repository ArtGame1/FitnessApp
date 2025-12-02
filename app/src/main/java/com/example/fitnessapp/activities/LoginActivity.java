package com.example.fitnessapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.example.fitnessapp.admin.AdminPanel;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText; //Объявляем переменную для поля ввода email
    private EditText passwordEditText; //Объявляем переменную для поля ввода пароля
    private EditText phoneEditText; //Объявляем переменную для поля ввода телефона
    private RadioGroup loginTypeGroup; //Объявляем переменную для группы переключателя (Email/Телефон)
    private RadioButton radioEmail, radioPhone; //Объявляем переменные для кнопок переключателя (Email и Телефон)
    private Button loginButton; //Объявляем переменную для кнопки "Войти"
    private TextView signUp; //Объявляем переменную для текста регистрации


    private static final String CORRECT_EMAIL = "test@example.com"; //Определяет корректный email для входа.
    private static final String CORRECT_PASSWORD = "password123"; //Определяет корректный пароль для входа.

    //Данные для администратора
    public static final String ADMIN_EMAIL = "admin@fitnessapp.com"; //Email администратора
    public static final String ADMIN_PASSWORD = "admin123"; //Пароль администратора

    //Данные для тренера
    public static final String COACH_EMAIL = "coach@fitnessapp.com"; //Email тренера
    public static final String COACH_PASSWORD = "coach123"; //Пароль тренера

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); //Устанавливаем макет (XML файл) активности из ресурсов

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        emailEditText = findViewById(R.id.userEdt); //Ищет поле ввода email по его ID и присваивает переменной
        passwordEditText = findViewById(R.id.passEdt); //Ищет поле ввода пароля по его ID и присваивает переменной
        phoneEditText = findViewById(R.id.phoneEdt); //Ищет поле ввода телефона по его ID
        loginTypeGroup = findViewById(R.id.loginTypeGroup);
        radioEmail = findViewById(R.id.radioEmail);
        radioPhone = findViewById(R.id.radioPhone);
        loginButton = findViewById(R.id.loginBtn); //Ищет кнопку "Войти" по её ID и присваивает переменной
        signUp = findViewById(R.id.sin); //Ищет кнопку "Зарегистрироваться" по её ID и присваивает переменной


        //Обработчик переключения между email и телефоном
        loginTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEmail) {
                emailEditText.setVisibility(View.VISIBLE);
                phoneEditText.setVisibility(View.GONE);
                emailEditText.setHint("Email почта");
            } else {
                emailEditText.setVisibility(View.GONE);
                phoneEditText.setVisibility(View.VISIBLE);
                phoneEditText.setHint("Номер телефона");
            }
        });


        //Секретная подсказка по долгому нажатию на логотип
        CircleImageView logo = findViewById(R.id.imageView);
        logo.setOnLongClickListener(v -> {
            Toast.makeText(LoginActivity.this, "Админ доступ: admin@fitnessapp.com / admin123", Toast.LENGTH_SHORT).show();
            return true;
        });


        //Обработчик нажатия на кнопку "Войти"
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim(); //Получает текст из поля
            //ввода email и удаляет пробелы.
            String phone = phoneEditText.getText().toString().trim(); //Получает текст из поля телефона
            String password = passwordEditText.getText().toString().trim(); //Получает текст из
            //поля ввода пароля и удаляет пробелы.

            // Определяем, какой способ входа выбран
            boolean isEmailLogin = radioEmail.isChecked();
            String loginCredential = isEmailLogin ? email : phone;

            //Проверка, введен ли email или телефон
            if (TextUtils.isEmpty(loginCredential)) {
                if (isEmailLogin) {
                    emailEditText.setError("Введите email"); //Устанавливает ошибку в
                    //поле ввода email, если оно пустое.
                    emailEditText.requestFocus(); //Устанавливает фокус на поле ввода
                    //email
                } else {
                    phoneEditText.setError("Введите номер телефона");
                    phoneEditText.requestFocus();
                }
                return;
            } else if (TextUtils.isEmpty(password)) { //Проверка, введен ли пароль.
                passwordEditText.setError("Введите пароль"); //Устанавливает ошибку
                //в поле ввода пароля, если оно пустое
                passwordEditText.requestFocus(); //Устанавливает фокус на
                //поле ввода пароля.
            } else if (password.length() <= 6) { //Проверка, достаточно ли
                //длинный пароль
                passwordEditText.setError("Пароль должен содержать не менее 6 символов"); //Устанавливает
                //ошибку, если пароль слишком короткий.
                passwordEditText.requestFocus(); //Устанавливает фокус на поле ввода пароля.
            } else if (!containsDigit(password)) { //Проверка, содержит ли пароль хотя
                //бы одну цифру.
                passwordEditText.setError("Пароль должен содержать хотя бы одну цифру"); //Устанавливает
                //ошибку, если пароль не содержит цифр.
                passwordEditText.requestFocus(); //Устанавливает фокус на поле ввода пароля.
            } else { //Проверка на правильность введенных данных.
                //Проверка для администратора (только по email)
                if (isEmailLogin && email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                    Intent intent = new Intent(LoginActivity.this, AdminPanel.class); //Переход
                    //на AdminPanel для администратора
                    startActivity(intent);
                }
                else if (isEmailLogin && email.equals(COACH_EMAIL) && password.equals(COACH_PASSWORD)) {
                    Intent intent = new Intent(LoginActivity.this, CoachActivity.class);
                    startActivity(intent);
                }
                //Проверка для обычного пользователя по email
                else if (isEmailLogin && email.equals(CORRECT_EMAIL) && isValidPassword(password)) { //Если введенный
                    //email и пароль корректны...
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class); //Создает
                    //Intent для перехода на MainActivity.
                    startActivity(intent); //Запускает MainActivity.
                    //finish(); //Завершает текущую активность (LoginActivity), чтобы пользователь
                    //не мог вернуться назад.
                }
                //Проверка для входа по номеру телефона
                else if (!isEmailLogin && isValidPhoneNumber(phone) && isValidPassword(password)) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    if (isEmailLogin) {
                        Toast.makeText(LoginActivity.this, "Неверный email или пароль", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Неверный номер телефона или пароль", Toast.LENGTH_SHORT).show();
                    }
                    //Показывает сообщение об ошибке в случае неверного ввода данных.
                }
            }
        });

        //Обработчик нажатия на текст "Регистрация".
        signUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class); //Создает
            //Intent для перехода на RegisterActivity.
            startActivity(intent); //Запускает RegisterActivity.
        });
    }

    //Метод для проверки наличия хотя бы одной цифры в пароле.
    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) { //Проходит по каждому символу в пароле.
            if (Character.isDigit(c)) { //Проверяет является ли символ цифрой.
                return true; //Возвращает true, если найдена цифра.
            }
        }
        return false; //Возвращает false, если цифр не найдено.
    }

    //Метод для проверки валидности пароля.
    private boolean isValidPassword(String password) {
        return password.equals(CORRECT_PASSWORD); //Возвращает true,
        //если введенный пароль соответствует корректному паролю.
    }

    private boolean isValidPhoneNumber(String phone) {
        //Убираем все пробелы, дефисы и скобки для сравнения
        String cleanPhone = phone.replaceAll("[\\\\s\\\\-\\\\(\\\\)]", "");
        return cleanPhone.equals("71234567890"); //Возвращает true, если номер соответствует
    }
}