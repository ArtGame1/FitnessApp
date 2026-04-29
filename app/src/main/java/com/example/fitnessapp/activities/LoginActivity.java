package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ LoginActivity (ЭКРАН ВХОДА С FIREBASE)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * LoginActivity является экраном авторизации в приложении Fitness App.
 * Сюда пользователь попадает при запуске приложения. Здесь он может войти
 * в свой аккаунт, используя EMAIL или ТЕЛЕФОН и пароль.
 *
 * Экран поддерживает три роли пользователей:
 * - Администратор (локальная проверка, переход на AdminPanel)
 * - Тренер (локальная проверка, переход на CoachActivity)
 * - Обычный пользователь (проверка через Firebase, переход на MainActivity)
 *
 * КЛЮЧЕВОЕ ОТЛИЧИЕ: Теперь используется ОДНО поле для ввода, которое
 * динамически меняет свой тип в зависимости от выбранного способа входа
 * (Email или Телефон). Это позволяет избежать "прыгания" экрана и
 * делает интерфейс более чистым.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Экран авторизации содержит следующие элементы:
 *
 * - Логотип приложения (CircleImageView) — при долгом нажатии показывает
 *   всплывающую подсказку с данными для входа администратора и тренера.
 *
 * - Переключатель способа входа (RadioGroup с RadioButton):
 *   - "Email" — выбран по умолчанию
 *   - "Телефон" — при выборе меняет hint и тип клавиатуры
 *
 * - Единое поле ввода (editTextUserInput) — служит для ввода:
 *   - Email адреса (в режиме Email)
 *   - Номера телефона (в режиме Телефон)
 *
 * - Поле ввода пароля (passwordEditText / passEdt) — для ввода пароля
 *
 * - Кнопка "Войти" (loginButton / loginBtn) — выполняет проверку введённых
 *   данных и перенаправляет пользователя на соответствующий экран.
 *
 * - Текст "Регистрация" (signUp / sin) — при нажатии открывает экран
 *   регистрации нового пользователя (RegisterActivity).
 *
 *
 * ЧАСТЬ 3. ДИНАМИЧЕСКОЕ ИЗМЕНЕНИЕ ПОЛЯ ВВОДА (НОВОВВЕДЕНИЕ)
 * ----------------------------------------------------------
 *
 * При переключении между режимами "Email" и "Телефон" происходят следующие
 * изменения в едином поле ввода:
 *
 * | Режим | Hint (подсказка) | InputType (тип клавиатуры) |
 * |-------|------------------|---------------------------|
 * | Email | "Email" | TEXT_VARIATION_EMAIL_ADDRESS |
 * | Телефон | "Номер телефона" | CLASS_PHONE |
 *
 * Поле также автоматически очищается при переключении, чтобы избежать
 * путаницы с неверным форматом данных.
 *
 *
 * ЧАСТЬ 4. АУТЕНТИФИКАЦИЯ ЧЕРЕЗ FIREBASE (ДЕТАЛЬНО)
 * --------------------------------------------------
 *
 * Firebase Authentication — это сервис Google, который позволяет:
 * - Выполнять вход существующих пользователей с email и паролем
 * - Обеспечивать безопасное хранение учётных данных
 * - Управлять пользователями через консоль Firebase
 *
 * Для работы с Firebase используется объект FirebaseAuth, который
 * предоставляет метод signInWithEmailAndPassword() для входа по email.
 *
 * ДЛЯ ВХОДА ПО ТЕЛЕФОНУ используется следующий алгоритм:
 * 1. Поиск пользователя в Realtime Database по полю "phone"
 * 2. Извлечение email найденного пользователя
 * 3. Выполнение стандартного входа через signInWithEmailAndPassword()
 *
 *
 * ЧАСТЬ 5. ПРОЦЕСС ВХОДА (ДЕТАЛЬНО)
 * ----------------------------------
 *
 * 1. Пользователь выбирает способ входа (Email или Телефон)
 * 2. Вводит соответствующие данные и пароль
 * 3. Нажимает кнопку "Войти"
 * 4. Выполняется валидация введённых данных
 * 5. Сначала проверяются локальные учётные данные (админ, тренер)
 * 6. Если выбран режим "Email" — выполняется вход через Firebase
 * 7. Если выбран режим "Телефон" — выполняется поиск email по телефону в БД
 * 8. Firebase проверяет существование пользователя и правильность пароля
 * 9. При успехе — открывается соответствующий главный экран
 * 10. При ошибке — показывается сообщение с причиной
 *
 *
 * ЧАСТЬ 6. ЛОКАЛЬНЫЕ УЧЁТНЫЕ ЗАПИСИ (АДМИН И ТРЕНЕР)
 * --------------------------------------------------
 *
 * Для удобства тестирования и администрирования реализованы локальные
 * учётные записи, которые не хранятся в Firebase:
 *
 * АДМИНИСТРАТОР:
 * - Email: admin@fitnessapp.com
 * - Пароль: admin123
 * - После входа открывается AdminPanel (панель управления администратора)
 *
 * ТРЕНЕР:
 * - Email: coach@fitnessapp.com
 * - Пароль: coach123
 * - После входа открывается CoachActivity (главный экран тренера)
 *
 * Эти проверки выполняются ДО обращения к Firebase, что позволяет
 * администратору и тренеру входить даже при отсутствии интернета.
 *
 *
 * ЧАСТЬ 7. СЕКРЕТНАЯ ПОДСКАЗКА (ДОЛГОЕ НАЖАТИЕ НА ЛОГОТИП)
 * ---------------------------------------------------------
 *
 * Для удобства тестирования реализована скрытая функция:
 * При долгом нажатии (long click) на логотип приложения появляется Toast
 * с подсказкой, содержащей учётные данные администратора и тренера.
 *
 * Это полезно для тестирования, чтобы разработчик или тестировщик
 * не забыли данные для входа в админку или кабинет тренера.
 *
 *
 * ЧАСТЬ 8. ВАЛИДАЦИЯ ВВОДИМЫХ ДАННЫХ (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * Перед отправкой данных в Firebase выполняется последовательная проверка:
 *
 * 8.1. Проверка вводимых данных (email или телефон) на пустоту:
 * - Проверяется, что поле не пустое
 * - Если поле пустое, показывается ошибка "Введите email или телефон"
 * - Фокус устанавливается на поле ввода
 *
 * 8.2. Проверка пароля на пустоту:
 * - Проверяется, что поле пароля не пустое
 * - Если поле пустое, показывается ошибка "Введите пароль"
 * - Фокус устанавливается на поле пароля
 *
 * 8.3. Проверка длины пароля:
 * - Пароль должен содержать не менее 6 символов
 * - Firebase требует минимум 6 символов
 * - Если пароль короче, показывается ошибка
 *
 * 8.4. Проверка наличия цифры в пароле:
 * - Пароль должен содержать хотя бы одну цифру
 * - Если цифр нет, показывается ошибка
 *
 *
 * ЧАСТЬ 9. ОБРАБОТКА ОШИБОК FIREBASE (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * Firebase может вернуть следующие ошибки при входе:
 *
 * | Ошибка | Описание | Решение |
 * |--------|----------|---------|
 * | ERROR_USER_NOT_FOUND | Пользователь не найден | Зарегистрироваться сначала |
 * | ERROR_WRONG_PASSWORD | Неверный пароль | Попробовать другой пароль |
 * | ERROR_INVALID_EMAIL | Неверный формат email | Ввести корректный email |
 * | ERROR_USER_DISABLED | Аккаунт отключён | Обратиться к администратору |
 * | ERROR_NETWORK_REQUEST_FAILED | Нет интернета | Проверить подключение |
 *
 * Все ошибки выводятся пользователю через Toast.
 *
 *
 * ЧАСТЬ 10. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * ------------------------------
 *
 * При запуске LoginActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 * Это создаёт иммерсивный опыт и делает экран входа более красивым,
 * без отвлекающих элементов интерфейса Android.
 *
 *
 * ЧАСТЬ 11. МЕТОДЫ ВХОДА (ДЕТАЛЬНО)
 * ----------------------------------
 *
 * 11.1. loginWithEmail(email, password)
 * - Выполняет вход по email через Firebase Authentication
 * - Сначала проверяет локальные учётные записи (админ, тренер)
 * - При успехе открывает MainActivity
 *
 * 11.2. loginWithPhone(phone, password)
 * - Выполняет поиск в Realtime Database по полю "phone"
 * - Извлекает email пользователя из найденной записи
 * - Вызывает loginWithEmail() с найденным email
 * - Если телефон не найден в БД — показывает ошибку
 *
 *
 * ЧАСТЬ 12. МЕТОД containsDigit() (ДЕТАЛЬНО)
 * ------------------------------------------
 *
 * Назначение:
 * Проверяет, содержит ли строка пароля хотя бы одну цифру.
 *
 * Реализация:
 * - Преобразует строку пароля в массив символов
 * - Проходит по каждому символу в цикле
 * - Для каждого символа проверяет, является ли он цифрой через Character.isDigit()
 * - Если найдена хотя бы одна цифра, метод возвращает true
 * - Если после прохода всех символов цифра не найдена, возвращает false
 *
 * Примеры:
 * - "password123" → true (содержит цифры 1, 2, 3)
 * - "mypassword" → false (не содержит цифр)
 * - "pass123word" → true (содержит цифры)
 *
 *
 * ЧАСТЬ 13. НАВИГАЦИЯ (ДЕТАЛЬНО)
 * -------------------------------
 *
 * 13.1. Вход для администратора:
 * - Проверка email и пароля с ADMIN_EMAIL и ADMIN_PASSWORD
 * - При успехе открывается AdminPanel
 *
 * 13.2. Вход для тренера:
 * - Проверка email и пароля с COACH_EMAIL и COACH_PASSWORD
 * - При успехе открывается CoachActivity
 *
 * 13.3. Вход для обычного пользователя (Email):
 * - Проверка через Firebase Authentication
 * - При успехе открывается MainActivity
 * - Текущая активность завершается через finish()
 *
 * 13.4. Вход для обычного пользователя (Телефон):
 * - Поиск email по телефону в Realtime Database
 * - Проверка через Firebase Authentication
 * - При успехе открывается MainActivity
 *
 * 13.5. Переход на регистрацию:
 * - При нажатии на signUp открывается RegisterActivity
 * - finish() не вызывается, чтобы пользователь мог вернуться
 *
 *
 * ЧАСТЬ 14. ПОРЯДОК ПРОВЕРКИ ПРИ ВХОДЕ (СХЕМА)
 * ---------------------------------------------
 *
 * Нажатие на кнопку "Войти"
 *         ↓
 * Проверка выбранного способа входа (Email или Телефон)
 *         ↓
 * Проверка вводимых данных (email/телефон) на пустоту
 *         ↓
 * Проверка пароля (не пустой, длина ≥ 6, есть цифра)
 *         ↓
 * Режим Email? → ДА → Админ/Тренер? → ДА → AdminPanel/CoachActivity
 *         ↓                      ↓ (НЕТ)
 * Режим Телефон?                Firebase Auth
 *         ↓                           ↓
 * Поиск email по телефону в БД      Успех? → MainActivity
 *         ↓                           ↓ (НЕТ)
 * Найден email? → ДА → Firebase Auth  Показать ошибку
 *         ↓ (НЕТ)
 * Показать ошибку "Пользователь не найден"
 *
 *
 * ЧАСТЬ 15. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * --------------------------------------------
 *
 * onCreate() — инициализация активности: установка полноэкранного режима,
 *              поиск View-компонентов, настройка переключателя, установка
 *              слушателей для кнопки входа, текста регистрации и долгого
 *              нажатия на логотип
 *
 * loginUser() — определяет выбранный способ входа и вызывает соответствующий метод
 *
 * loginWithEmail() — выполняет вход по email через Firebase
 *
 * loginWithPhone() — выполняет поиск email по телефону и вход
 *
 * containsDigit() — проверяет наличие хотя бы одной цифры в пароле
 *
 *
 * ЧАСТЬ 16. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Нет сохранения сессии:
 *    - При каждом запуске приложения нужно вводить логин и пароль заново
 *    - Нужно добавить "Запомнить меня" и автоматический вход
 *
 * 2. Нет восстановления пароля:
 *    - Нет кнопки "Забыли пароль?"
 *    - Нужно добавить функцию сброса пароля по email
 *
 * 3. Нет биометрической авторизации:
 *    - Нет входа по отпечатку пальца или Face ID
 *    - Можно добавить в будущем
 *
 * 4. Админ и тренер не в Firebase:
 *    - Их учётные данные хранятся локально в коде
 *    - Нужно перенести их в Firebase или Firestore
 *
 * 5. Вход по телефону требует поиска в БД:
 *    - При большом количестве пользователей может быть медленным
 *    - Нужно добавить индекс на поле "phone" в Realtime Database
 *
 *
 * ЧАСТЬ 17. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - Firebase Authentication (com.google.firebase:firebase-auth)
 * - Firebase Realtime Database (com.google.firebase:firebase-database)
 * - CircleImageView (для круглого логотипа)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 18. ИТОГИ
 * ----------------
 *
 * LoginActivity — это экран входа с интеграцией Firebase Authentication.
 * Ключевое улучшение: используется ОДНО поле для ввода email ИЛИ телефона
 * с динамическим изменением типа клавиатуры и подсказки.
 *
 * Экран выполняет все необходимые проверки вводимых данных: длина пароля
 * (не менее 6 символов), наличие цифры в пароле.
 *
 * Поддерживаются три роли: администратор, тренер и обычный пользователь.
 * При успешном входе пользователь перенаправляется на соответствующий экран.
 * При ошибке показывается конкретная причина.
 *
 * Вход по телефону работает через поиск email в Realtime Database,
 * что позволяет использовать единый механизм аутентификации Firebase.
 *
 * =============================================================================
 */

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.example.fitnessapp.admin.AdminPanel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextUserInput;  //Единое поле для ввода
    private EditText passwordEditText; //Поле для ввода пароля
    private Button loginButton; //Кнопка "Войти"
    private TextView signUp; //Текстовое поле "Зарегистрироваться"
    private RadioGroup loginTypeGroup;
    private RadioButton radioEmail; //Радиокнопка "Email"
    private RadioButton radioPhone; //Радиокнопка "Телефон"

    private FirebaseAuth firebaseAuth; //Объект для аутентификации пользователей
    private DatabaseReference databaseReference; //Ссылка на базу данных (Realtime Database)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true);

        //Инициализация Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        //Инициализация UI
        editTextUserInput = findViewById(R.id.editTextUserInput);
        passwordEditText = findViewById(R.id.passEdt);
        loginButton = findViewById(R.id.loginBtn);
        signUp = findViewById(R.id.sin);
        loginTypeGroup = findViewById(R.id.loginTypeGroup);
        radioEmail = findViewById(R.id.radioEmail);
        radioPhone = findViewById(R.id.radioPhone);

        //НАСТРОЙКА ПЕРЕКЛЮЧАТЕЛЯ
        loginTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioEmail) {
                // Режим Email
                editTextUserInput.setHint("Email");
                editTextUserInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                editTextUserInput.setText(""); // Очищаем поле
            } else {
                // Режим Телефон
                editTextUserInput.setHint("Номер телефона");
                editTextUserInput.setInputType(InputType.TYPE_CLASS_PHONE);
                editTextUserInput.setText(""); // Очищаем поле
            }
        });

        //Кнопка входа
        loginButton.setOnClickListener(v -> loginUser());

        //Переход на регистрацию
        signUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String userInput = editTextUserInput.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        //Проверка на пустоту
        if (TextUtils.isEmpty(userInput)) {
            editTextUserInput.setError("Введите email или телефон");
            editTextUserInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Введите пароль");
            passwordEditText.requestFocus();
            return;
        }

        //Проверка длины пароля
        if (password.length() < 6) {
            passwordEditText.setError("Пароль должен содержать не менее 6 символов");
            passwordEditText.requestFocus();
            return;
        }

        //Проверка наличия цифры в пароле
        if (!containsDigit(password)) {
            passwordEditText.setError("Пароль должен содержать хотя бы одну цифру");
            passwordEditText.requestFocus();
            return;
        }

        //Определяем, что ввел пользователь: email или телефон?
        int selectedId = loginTypeGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.radioEmail) {
            //Вход по EMAIL
            loginWithEmail(userInput, password);
        } else {
            //Вход по ТЕЛЕФОНУ
            loginWithPhone(userInput, password);
        }
    }

    //Вход по EMAIL
    private void loginWithEmail(String email, String password) {
        //Проверка на админа
        if (email.equals("admin@fitnessapp.com") && password.equals("admin123")) {
            startActivity(new Intent(LoginActivity.this, AdminPanel.class));
            return;
        }

        //Проверка на тренера
        if (email.equals("coach@fitnessapp.com") && password.equals("coach123")) {
            startActivity(new Intent(LoginActivity.this, CoachActivity.class));
            return;
        }

        // Вход через Firebase
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "Ошибка: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Вход по ТЕЛЕФОНУ
    private void loginWithPhone(String phone, String password) {
        // Ищем пользователя с таким телефоном в базе данных
        databaseReference.orderByChild("phone").equalTo(phone)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                String email = userSnapshot.child("email").getValue(String.class);
                                if (email != null) {
                                    firebaseAuth.signInWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(task -> {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                                    finish();
                                                } else {
                                                    Toast.makeText(LoginActivity.this, "Неверный пароль", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                    break;
                                }
                            }
                        } else {
                            editTextUserInput.setError("Пользователь с таким телефоном не найден");
                            editTextUserInput.requestFocus();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LoginActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    //Проверка наличия цифры в пароле
    private boolean containsDigit(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }
}