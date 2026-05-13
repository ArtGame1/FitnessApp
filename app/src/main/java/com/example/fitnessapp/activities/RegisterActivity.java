package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ RegisterActivity (ЭКРАН РЕГИСТРАЦИИ С FIREBASE)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * RegisterActivity является экраном регистрации нового пользователя в приложении
 * Fitness App. Сюда пользователь попадает при нажатии на кнопку "Регистрация"
 * на экране входа (LoginActivity).
 *
 * Этот экран позволяет новому пользователю:
 * - Ввести адрес электронной почты
 * - Ввести номер телефона (для возможности входа по телефону в будущем)
 * - Создать пароль и подтвердить его
 * - Зарегистрироваться в Firebase Authentication
 * - После успешной регистрации данные (включая телефон) сохраняются в
 *   Realtime Database для последующего входа по телефону
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Экран регистрации содержит следующие элементы:
 *
 * Поля ввода:
 * - Email (emailEditText / emailEdt) — поле для ввода электронной почты
 * - Телефон (phoneEditText / phoneEdit) — поле для ввода номера телефона
 * - Пароль (passwordEditText / passEdt) — поле для ввода пароля
 * - Подтверждение пароля (passwordConfirmEditText / passConfirm) — поле для
 *   повторного ввода пароля (проверка на совпадение)
 *
 * Кнопки и ссылки:
 * - Кнопка "Зарегистрироваться" (registerButton / signupBtn) — выполняет
 *   проверку данных и регистрацию через Firebase
 * - Текст "Войти" (loginTextView / textView4) — переход на экран входа
 *   для уже зарегистрированных пользователей
 *
 *
 * ЧАСТЬ 3. АУТЕНТИФИКАЦИЯ ЧЕРЕЗ FIREBASE (ДЕТАЛЬНО)
 * --------------------------------------------------
 *
 * Firebase Authentication — это сервис Google, который позволяет:
 * - Создавать новых пользователей с email и паролем
 * - Выполнять вход существующих пользователей
 * - Управлять пользователями через консоль Firebase
 * - Обеспечивать безопасное хранение учётных данных
 *
 * Для работы с Firebase используется объект FirebaseAuth, который
 * предоставляет методы:
 * - createUserWithEmailAndPassword() — создание нового пользователя
 * - signInWithEmailAndPassword() — вход существующего пользователя
 *
 * ДОПОЛНИТЕЛЬНО: Для хранения телефона используется Realtime Database.
 * Это позволяет в дальнейшем реализовать вход по телефону через поиск
 * email, ассоциированного с номером телефона.
 *
 *
 * ЧАСТЬ 4. ПРОЦЕСС РЕГИСТРАЦИИ (ДЕТАЛЬНО)
 * -----------------------------------------
 *
 * 1. Пользователь вводит email, телефон, пароль и подтверждение пароля
 * 2. Нажимает кнопку "Зарегистрироваться"
 * 3. Выполняется валидация всех введённых данных
 * 4. Firebase Auth создаёт пользователя с email и паролем
 * 5. При успехе — дополнительные данные (телефон, роль, дата создания)
 *    сохраняются в Realtime Database
 * 6. При ошибке — показывается сообщение с причиной
 *
 *
 * ЧАСТЬ 5. ВАЛИДАЦИЯ ВВОДИМЫХ ДАННЫХ (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * Перед отправкой данных в Firebase выполняется последовательная проверка:
 *
 * 5.1. Проверка email:
 * - Проверяется, что поле email не пустое
 * - Если поле пустое, показывается ошибка "Введите email"
 * - Фокус устанавливается на поле email
 *
 * 5.2. Проверка телефона:
 * - Проверяется, что поле телефона не пустое
 * - Если поле пустое, показывается ошибка "Введите номер телефона"
 * - Проверяется, что номер начинается со знака "+" (международный формат)
 * - Фокус устанавливается на поле телефона
 *
 * 5.3. Проверка пароля на пустоту:
 * - Проверяется, что поле пароля не пустое
 * - Если поле пустое, показывается ошибка "Введите пароль"
 * - Фокус устанавливается на поле пароля
 *
 * 5.4. Проверка совпадения паролей:
 * - Сравнивается введённый пароль и подтверждение
 * - Если не совпадают, показывается ошибка "Пароли не совпадают"
 * - Фокус устанавливается на поле подтверждения пароля
 *
 * 5.5. Проверка длины пароля:
 * - Пароль должен содержать не менее 6 символов
 * - Firebase требует минимум 6 символов
 * - Если пароль короче, показывается ошибка
 *
 * 5.6. Проверка наличия цифры в пароле:
 * - Пароль должен содержать хотя бы одну цифру
 * - Если цифр нет, показывается ошибка
 *
 *
 * ЧАСТЬ 6. СОХРАНЕНИЕ ДАННЫХ В REALTIME DATABASE (НОВОВВЕДЕНИЕ)
 * -------------------------------------------------------------
 *
 * При успешной регистрации в Firebase Auth, дополнительные данные
 * сохраняются в Realtime Database в узле "Users":
 *
 * Структура базы данных:
 * Users/
 *   └── {userId}/
 *       ├── email: "user@example.com"
 *       ├── phone: "+79001234567"
 *       ├── role: "user"
 *       └── createdAt: "1703318400000"
 *
 * Это позволяет:
 * - В дальнейшем реализовать вход по телефону
 * - Хранить дополнительную информацию о пользователе
 * - Управлять ролями пользователей
 *
 *
 * ЧАСТЬ 7. ОБРАБОТКА ОШИБОК FIREBASE (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * Firebase может вернуть следующие ошибки при регистрации:
 *
 * | Ошибка | Описание | Решение |
 * |--------|----------|---------|
 * | ERROR_EMAIL_ALREADY_IN_USE | Email уже используется | Использовать другой email |
 * | ERROR_INVALID_EMAIL | Неверный формат email | Ввести корректный email |
 * | ERROR_WEAK_PASSWORD | Слишком слабый пароль | Использовать более сложный пароль |
 * | ERROR_NETWORK_REQUEST_FAILED | Нет интернета | Проверить подключение |
 *
 * Все ошибки выводятся пользователю через Toast.
 *
 *
 * ЧАСТЬ 8. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * -----------------------------
 *
 * При запуске RegisterActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 * Это создаёт иммерсивный опыт и делает экран регистрации более красивым.
 *
 *
 * ЧАСТЬ 9. МЕТОДЫ КЛАССА (ДЕТАЛЬНО)
 * ---------------------------------
 *
 * 9.1. onCreate()
 * - Инициализирует активность
 * - Устанавливает полноэкранный режим
 * - Инициализирует Firebase Auth и Database Reference
 * - Привязывает UI компоненты из XML
 * - Настраивает обработчики нажатий
 *
 * 9.2. registerUser()
 * - Получает данные из всех полей ввода
 * - Выполняет последовательную валидацию
 * - Отправляет запрос на создание пользователя в Firebase Auth
 * - При успехе вызывает saveUserToDatabase()
 *
 * 9.3. saveUserToDatabase()
 * - Создаёт HashMap с данными пользователя
 * - Сохраняет данные в Realtime Database по пути Users/{userId}
 * - При успехе показывает сообщение и переходит на LoginActivity
 * - При ошибке показывает сообщение об ошибке
 *
 * 9.4. containsDigit()
 * - Проверяет, содержит ли строка пароля хотя бы одну цифру
 * - Возвращает true, если цифра найдена
 *
 *
 * ЧАСТЬ 10. НАВИГАЦИЯ (ДЕТАЛЬНО)
 * -------------------------------
 *
 * 10.1. Переход после успешной регистрации:
 *
 * При успешной регистрации и сохранении данных создаётся Intent для перехода
 * на LoginActivity. Текущая активность завершается через finish(), чтобы
 * пользователь не мог вернуться на экран регистрации кнопкой "Назад".
 *
 * 10.2. Переход по ссылке "Войти":
 *
 * При нажатии на текст "Войти" (loginTextView) создаётся Intent для перехода
 * на LoginActivity. finish() не вызывается, чтобы пользователь мог вернуться
 * на экран регистрации (например, если случайно нажал).
 *
 *
 * ЧАСТЬ 11. ОСОБЕННОСТИ РЕАЛИЗАЦИИ
 * ---------------------------------
 *
 * 11.1. Двойное сохранение:
 *
 * Данные пользователя сохраняются в двух местах:
 * - Firebase Auth: email и пароль (для аутентификации)
 * - Realtime Database: телефон, роль, дата создания (для дополнительных данных)
 *
 * 11.2. Асинхронная регистрация:
 *
 * Firebase работает асинхронно через addOnCompleteListener.
 * Это значит, что регистрация не блокирует UI поток.
 *
 * 11.3. Последовательная валидация:
 *
 * Валидация выполняется с использованием цепочки if-else, где каждое следующее
 * условие проверяется только после того, как предыдущее было пройдено.
 *
 *
 * ЧАСТЬ 12. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * --------------------------------------------
 *
 * | Метод | Назначение |
 * |-------|-------------|
 * | onCreate() | Инициализация активности и UI компонентов |
 * | registerUser() | Главная логика регистрации и валидации |
 * | saveUserToDatabase() | Сохранение телефона и данных в БД |
 * | containsDigit() | Проверка наличия цифры в пароле |
 *
 *
 * ЧАСТЬ 13. ТРЕБОВАНИЯ К ФОРМАТУ ТЕЛЕФОНА
 * ----------------------------------------
 *
 * Телефон должен вводиться в международном формате:
 * - Начинаться со знака "+"
 * - Пример: +79001234567 (Россия)
 * - Пример: +380123456789 (Украина)
 * - Пример: +375123456789 (Беларусь)
 *
 * Это требование проверяется через startsWith("+") при валидации.
 *
 *
 * ЧАСТЬ 14. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Нет проверки формата телефона:
 *    - Проверяется только наличие знака "+"
 *    - Нужно добавить проверку количества цифр
 *
 * 2. Нет проверки формата email:
 *    - Firebase сам проверяет формат, но можно добавить свою проверку
 *
 * 3. Нет индикатора загрузки:
 *    - При регистрации нет прогресс-бара
 *    - Нужно добавить ProgressBar и блокировать кнопку на время регистрации
 *
 * 4. Нет верификации телефона:
 *    - Телефон сохраняется без подтверждения
 *    - Можно добавить SMS-верификацию через PhoneAuthProvider
 *
 * 5. Нет проверки уникальности телефона:
 *    - Один телефон могут использовать несколько аккаунтов
 *    - Нужно добавить проверку перед регистрацией
 *
 *
 * ЧАСТЬ 15. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - Firebase Authentication (com.google.firebase:firebase-auth)
 * - Firebase Realtime Database (com.google.firebase:firebase-database)
 * - Material Design (com.google.android.material:material) — для TextInputLayout
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 16. ИТОГИ
 * ----------------
 *
 * RegisterActivity — это экран регистрации нового пользователя с интеграцией
 * Firebase Authentication и Realtime Database.
 *
 * Ключевые возможности:
 * - Регистрация по email и паролю
 * - Сохранение номера телефона в базу данных
 * - Подтверждение пароля
 * - Полная валидация всех полей
 * - Обработка всех ошибок Firebase
 *
 * Он выполняет все необходимые проверки вводимых данных:
 * - Email (не пустой)
 * - Телефон (не пустой, начинается с +)
 * - Пароль (не пустой, не менее 6 символов, содержит цифру)
 * - Подтверждение пароля (совпадает с паролем)
 *
 * При успешной регистрации:
 * 1. Пользователь создаётся в Firebase Authentication
 * 2. Телефон и роль сохраняются в Realtime Database
 * 3. Пользователь перенаправляется на экран входа
 *
 * При ошибке показывается конкретная причина через Toast.
 *
 * =============================================================================
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    //ОБЪЯВЛЕНИЕ ПЕРЕМЕННЫХ (UI компоненты)

    private EditText emailEditText;
    private TextInputEditText phoneEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private Button registerButton;
    private TextView loginTextView;

    //ОБЪЯВЛЕНИЕ ПЕРЕМЕННЫХ (Firebase)

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);


        //Получаем экземпляр FirebaseAuth (один на всё приложение)
        firebaseAuth = FirebaseAuth.getInstance();

        //Получаем ссылку на узел "Users" в Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //НАСТРОЙКА ПОЛНОЭКРАННОГО РЕЖИМА

        //Получаем корневое представление окна и скрываем системные панели
        getWindow().getDecorView().setSystemUiVisibility(
                // SYSTEM_UI_FLAG_FULLSCREEN - скрывает статус-бар (часы, батарея)
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        // SYSTEM_UI_FLAG_HIDE_NAVIGATION - скрывает навигационные кнопки (Назад, Домой)
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        // SYSTEM_UI_FLAG_LAYOUT_STABLE - стабилизирует макет при скрытии панелей
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );

        //ПРИВЯЗКА UI КОМПОНЕНТОВ (СВЯЗЫВАЕМ JAVA КОД С XML)

        //findViewById() - находит элемент по ID из XML и возвращает его как Java-объект
        emailEditText = findViewById(R.id.emailEdt);
        phoneEditText = findViewById(R.id.phoneEdt);
        passwordEditText = findViewById(R.id.passEdt);
        passwordConfirmEditText = findViewById(R.id.passConfirm);
        registerButton = findViewById(R.id.signupBtn);
        loginTextView = findViewById(R.id.textView4);

        //НАСТРОЙКА ОБРАБОТЧИКОВ СОБЫТИЙ
        registerButton.setOnClickListener(v -> registerUser());

        // При нажатии на текст "Войти" переходим на экран входа
        loginTextView.setOnClickListener(v -> {
            //Создаем Intent для перехода от текущей активности к LoginActivity
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
    }

    //МЕТОД registerUser() - ГЛАВНАЯ ЛОГИКА РЕГИСТРАЦИИ
    private void registerUser() {

        //ПОЛУЧАЕМ ДАННЫЕ ИЗ ПОЛЕЙ ВВОДА
        //getText() - получает текст из поля
        //toString() - преобразует в строку
        //trim() - удаляет пробелы в начале и конце

        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String passwordConfirm = passwordConfirmEditText.getText().toString().trim();

        //ВАЛИДАЦИЯ EMAIL
        //TextUtils.isEmpty() - проверяет, пустая ли строка
        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Введите email");  //Показываем красную ошибку под полем
            emailEditText.requestFocus();              //Ставим курсор в это поле
            return;                                    //Выходим из метода, дальше не идём
        }

        if (!isValidEmail(email)) {
            emailEditText.setError("Введите корректный email (пример: user@mail.ru)");
            emailEditText.requestFocus();
            return;
        }

        //ВАЛИДАЦИЯ ТЕЛЕФОНА
        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Введите номер телефона");
            phoneEditText.requestFocus();
            return;
        }

        //Проверяем, что номер начинается со знака +
        //startsWith() - проверяет начало строки
        if (!phone.startsWith("+")) {
            phoneEditText.setError("Номер должен начинаться с + (например: +79001234567)");
            phoneEditText.requestFocus();
            return;
        }

        if (!isValidPhone(phone)) {
            phoneEditText.setError("Номер телефона должен содержать 10-15 цифр после +");
            phoneEditText.requestFocus();
            return;
        }

        //ВАЛИДАЦИЯ ПАРОЛЯ
        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Введите пароль");
            passwordEditText.requestFocus();
            return;
        }

        //ПРОВЕРКА СОВПАДЕНИЯ ПАРОЛЕЙ
        //equals() - сравнивает строки на равенство
        if (!password.equals(passwordConfirm)) {
            passwordConfirmEditText.setError("Пароли не совпадают");
            passwordConfirmEditText.requestFocus();
            return;
        }

        //ПРОВЕРКА ДЛИНЫ ПАРОЛЯ (Firebase требует минимум 6 символов)
        if (password.length() < 6) {
            passwordEditText.setError("Пароль должен содержать не менее 6 символов");
            passwordEditText.requestFocus();
            return;
        }

        //ПРОВЕРКА НАЛИЧИЯ ЦИФРЫ В ПАРОЛЕ
        //! - оператор отрицания (если НЕТ цифр)
        if (!containsDigit(password)) {
            passwordEditText.setError("Пароль должен содержать хотя бы одну цифру");
            passwordEditText.requestFocus();
            return;
        }

        //РЕГИСТРАЦИЯ В FIREBASE AUTH
        //createUserWithEmailAndPassword() - создаёт нового пользователя в Firebase
        //Этот метод работает АСИНХРОННО (не блокирует экран, выполняется в фоне)
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    //Этот метод вызовется, когда Firebase закончит регистрацию (успех или ошибка)
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //task.isSuccessful() - true, если регистрация прошла успешно
                        if (task.isSuccessful()) {

                            //Получаем текущего пользователя (только что созданного)
                            //getCurrentUser() - возвращает объект FirebaseUser
                            FirebaseUser user = firebaseAuth.getCurrentUser();

                            //Проверяем, что пользователь не null (существует)
                            if (user != null) {
                                // Сохраняем телефон и другие данные в Realtime Database
                                saveUserToDatabase(user.getUid(), email, phone);
                            }
                        } else {
                            //Если регистрация не удалась - показываем ошибку
                            //getException() - получает исключение
                            //getMessage() - получает текст ошибки
                            String error = task.getException().getMessage();
                            Toast.makeText(RegisterActivity.this, "Ошибка: " + error, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //МЕТОД saveUserToDatabase() - СОХРАНЯЕТ ДАННЫЕ ПОЛЬЗОВАТЕЛЯ В БД
    //Параметры:
    //- userId: уникальный ID пользователя из Firebase Auth
    //- email: email пользователя
    //- phone: телефон пользователя


    private void saveUserToDatabase(String userId, String email, String phone) {

        Map<String, Object> userData = new HashMap<>();

        userData.put("email", email);
        userData.put("phone", phone);
        userData.put("role", "user");
        userData.put("createdAt", System.currentTimeMillis());
        userData.put("name", "");
        userData.put("nickname", "");
        userData.put("bio", "");
        userData.put("birthDate", "");


        userData.put("photoUrl", "");

        databaseReference.child(userId).setValue(userData)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
                    }
                });
    }


//    private void saveUserToDatabase(String userId, String email, String phone) {
//
//        //HashMap - коллекция для хранения пар "ключ-значение"
//        //Это как словарь: по ключу "email" можно получить значение email
//        Map<String, Object> userData = new HashMap<>();
//
//        //put() - добавляем данные в коллекцию
//        userData.put("email", email);      //Сохраняем email
//        userData.put("phone", phone);      //Сохраняем телефон
//        userData.put("role", "user");      //Роль по умолчанию (обычный пользователь)
//
//        //String.valueOf() - преобразует число в строку
//        //System.currentTimeMillis() - текущее время в миллисекундах (для истории)
//        userData.put("createdAt", System.currentTimeMillis());
//
//        userData.put("name", "");           //Имя пользователя (пустое)
//        userData.put("nickname", "");       //Никнейм (пустой)
//        userData.put("bio", "");            //О себе (пустое)
//        userData.put("birthDate", "");      //Дата рождения (пустая)
//        userData.put("avatarUrl", "");      //Ссылка на аватар (пустая)
//
//        //Сохраняем в Firebase Realtime Database
//        //child(userId) - создаём узел с именем = ID пользователя
//        //setValue() - записываем данные в этот узел
//        databaseReference.child(userId).setValue(userData)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//
//                        //Если данные успешно сохранились
//                        if (task.isSuccessful()) {
//                            //Показываем сообщение об успехе
//                            Toast.makeText(RegisterActivity.this, "Регистрация успешна!", Toast.LENGTH_SHORT).show();
//
//                            //Переходим на экран входа
//                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                            startActivity(intent);
//
//                            //finish() - закрываем текущую активность
//                            //Пользователь не сможет вернуться на регистрацию кнопкой "Назад"
//                            finish();
//                        } else {
//                            //Если данные не сохранились - ошибка
//                            Toast.makeText(RegisterActivity.this, "Ошибка сохранения данных", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//    }

    // Добавь этот метод для проверки email
    private boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        return email.contains("@") && email.contains(".");
    }

    // Добавь этот метод для проверки телефона
    private boolean isValidPhone(String phone) {
        if (phone == null || phone.isEmpty()) return false;
        return phone.startsWith("+") && phone.length() >= 10 && phone.length() <= 15;
    }

    //МЕТОД containsDigit() - ПРОВЕРЯЕТ, ЕСТЬ ЛИ В ПАРОЛЕ ХОТЯ БЫ ОДНА ЦИФРА
    //Параметр: String password - пароль для проверки
    //Возвращает: true (есть цифра) или false (нет цифр)
    private boolean containsDigit(String password) {

        //toCharArray() - преобразует строку в массив символов
        //Например: "abc123" → ['a','b','c','1','2','3']
        for (char c : password.toCharArray()) {

            //Character.isDigit(c) - проверяет, является ли символ цифрой (0-9)
            if (Character.isDigit(c)) {
                return true;  //Нашли цифру - сразу возвращаем true
            }
        }
        return false;  //Прошли весь пароль, цифр нет - возвращаем false
    }
}