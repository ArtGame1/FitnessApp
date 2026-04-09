package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ LoginActivity
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * LoginActivity является экраном авторизации в приложении Fitness App.
 * Сюда пользователь попадает при запуске приложения. Здесь он может войти
 * в свой аккаунт, используя email и пароль или номер телефона. Также есть
 * возможность перейти на экран регистрации, если у пользователя ещё нет аккаунта.
 *
 * Экран поддерживает три роли пользователей: обычный пользователь, тренер и
 * администратор. В зависимости от роли после входа открывается соответствующий
 * главный экран.
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
 * - Переключатель "Email / Телефон" (RadioGroup с двумя RadioButton) —
 *   позволяет выбрать способ входа: по email или по номеру телефона.
 *
 * - Поле ввода email или телефона (EditText) — в зависимости от выбранного
 *   способа отображается либо поле для email, либо поле для телефона.
 *
 * - Поле ввода пароля (EditText) — общее для обоих способов входа.
 *
 * - Кнопка "Войти" (loginButton) — выполняет проверку введённых данных и
 *   перенаправляет пользователя на соответствующий экран.
 *
 * - Текст "Регистрация" (signUp) — при нажатии открывает экран регистрации
 *   нового пользователя (RegisterActivity).
 *
 *
 * ЧАСТЬ 3. ПЕРЕКЛЮЧЕНИЕ МЕЖДУ EMAIL И ТЕЛЕФОНОМ (ДЕТАЛЬНО)
 * ---------------------------------------------------------
 *
 * Когда пользователь выбирает "Email", поле для телефона скрывается,
 * а поле для email становится видимым. Хинт поля меняется на "Email почта".
 *
 * Когда пользователь выбирает "Телефон", поле для email скрывается,
 * а поле для телефона становится видимым. Хинт поля меняется на "Номер телефона".
 *
 * Это реализовано через слушатель OnCheckedChangeListener на RadioGroup.
 *
 *
 * ЧАСТЬ 4. ВХОДНЫЕ ДАННЫЕ (ТЕСТОВЫЕ УЧЁТНЫЕ ЗАПИСИ)
 * --------------------------------------------------
 *
 * В текущей версии используется статическая проверка учётных данных
 * (без подключения к Firebase или базе данных). Доступны следующие аккаунты:
 *
 * ОБЫЧНЫЙ ПОЛЬЗОВАТЕЛЬ (по email):
 * - Email: test@example.com
 * - Пароль: password123
 *
 * ОБЫЧНЫЙ ПОЛЬЗОВАТЕЛЬ (по телефону):
 * - Телефон: 71234567890 (без пробелов и скобок)
 * - Пароль: password123
 *
 * ТРЕНЕР (только по email):
 * - Email: coach@fitnessapp.com
 * - Пароль: coach123
 * - После входа открывается CoachActivity
 *
 * АДМИНИСТРАТОР (только по email):
 * - Email: admin@fitnessapp.com
 * - Пароль: admin123
 * - После входа открывается AdminPanel
 *
 *
 * ЧАСТЬ 5. ВАЛИДАЦИЯ ВВОДИМЫХ ДАННЫХ (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * Перед попыткой входа система проверяет следующие условия:
 *
 * 1. Проверка на пустоту:
 *    - Если выбран вход по email, проверяется что поле email не пустое
 *    - Если выбран вход по телефону, проверяется что поле телефона не пустое
 *    - Если поле пустое, показывается ошибка и фокус ставится на это поле
 *
 * 2. Проверка пароля на пустоту:
 *    - Если пароль не введён, показывается ошибка "Введите пароль"
 *
 * 3. Проверка длины пароля:
 *    - Пароль должен содержать не менее 6 символов
 *    - Если короче, показывается ошибка с требованием увеличить длину
 *
 * 4. Проверка наличия цифры в пароле:
 *    - Пароль должен содержать хотя бы одну цифру
 *    - Если цифр нет, показывается соответствующая ошибка
 *
 * 5. Проверка соответствия учётным данным:
 *    - Сравнение введённого email/телефона и пароля с тестовыми значениями
 *    - Если данные не совпадают, показывается Toast с сообщением об ошибке
 *
 *
 * ЧАСТЬ 6. РАСПРЕДЕЛЕНИЕ ПО РОЛЯМ (ДЕТАЛЬНО)
 * -------------------------------------------
 *
 * После успешной проверки учётных данных пользователь перенаправляется
 * на разные экраны в зависимости от роли:
 *
 * АДМИНИСТРАТОР:
 * - Проверяется совпадение email с ADMIN_EMAIL и пароля с ADMIN_PASSWORD
 * - При успехе открывается AdminPanel (панель управления администратора)
 *
 * ТРЕНЕР:
 * - Проверяется совпадение email с COACH_EMAIL и пароля с COACH_PASSWORD
 * - При успехе открывается CoachActivity (главный экран тренера)
 *
 * ОБЫЧНЫЙ ПОЛЬЗОВАТЕЛЬ (по email):
 * - Проверяется совпадение email с CORRECT_EMAIL
 * - Проверяется пароль через метод isValidPassword()
 * - При успехе открывается MainActivity (главный экран пользователя)
 *
 * ОБЫЧНЫЙ ПОЛЬЗОВАТЕЛЬ (по телефону):
 * - Проверяется номер телефона через метод isValidPhoneNumber()
 * - Проверяется пароль через метод isValidPassword()
 * - При успехе открывается MainActivity
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
 * ЧАСТЬ 8. МЕТОДЫ ПРОВЕРКИ (ДЕТАЛЬНО)
 * ------------------------------------
 *
 * containsDigit(String password):
 * - Назначение: проверяет, содержит ли пароль хотя бы одну цифру
 * - Реализация: проходим по каждому символу строки, если находим цифру,
 *   возвращаем true. Если цифр нет — false.
 * - Используется для надёжности пароля
 *
 * isValidPassword(String password):
 * - Назначение: проверяет, соответствует ли введённый пароль эталонному
 * - Реализация: сравнивает строку пароля с константой CORRECT_PASSWORD
 * - Возвращает true только для пароля "password123"
 *
 * isValidPhoneNumber(String phone):
 * - Назначение: проверяет, соответствует ли введённый номер телефона эталонному
 * - Реализация: удаляет из строки все пробелы, дефисы и скобки с помощью
 *   регулярного выражения, затем сравнивает с эталоном "71234567890"
 * - Это позволяет пользователю вводить номер в разных форматах:
 *   +7 (123) 456-78-90 → 71234567890
 *
 *
 * ЧАСТЬ 9. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * -----------------------------
 *
 * При запуске LoginActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 * Это создаёт иммерсивный опыт и делает экран входа более красивым,
 * без отвлекающих элементов интерфейса Android.
 *
 *
 * ЧАСТЬ 10. НАВИГАЦИЯ
 * -------------------
 *
 * Кнопка "Войти" (loginButton):
 * - После успешной авторизации открывает соответствующий главный экран
 * - Текущая LoginActivity при этом НЕ завершается (нет finish()),
 *   поэтому пользователь может вернуться назад кнопкой "Назад"
 *
 * Текст "Регистрация" (signUp):
 * - При нажатии открывается RegisterActivity
 * - Пользователь может создать новый аккаунт
 *
 *
 * ЧАСТЬ 11. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Отсутствие подключения к Firebase:
 *    - Сейчас все учётные данные хранятся в виде констант в коде
 *    - В планах — подключить Firebase Authentication для реальной работы
 *
 * 2. Нет проверки на существование email в системе:
 *    - Сейчас проверяется только совпадение с одним тестовым email
 *    - Нужно добавить запрос к базе данных
 *
 * 3. Телефон только один тестовый:
 *    - Сейчас доступен только номер 71234567890
 *    - В будущем нужно добавить поддержку любых номеров из БД
 *
 * 4. Нет восстановления пароля:
 *    - Нет кнопки "Забыли пароль?"
 *    - Нужно добавить функцию сброса пароля по email
 *
 * 5. Нет сохранения сессии:
 *    - При каждом запуске приложения нужно вводить логин и пароль заново
 *    - Нужно добавить "Запомнить меня" и автоматический вход
 *
 * 6. Нет биометрической авторизации:
 *    - Нет входа по отпечатку пальца или Face ID
 *    - Можно добавить в будущем
 *
 *
 * ЧАСТЬ 12. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - CircleImageView (для круглого логотипа)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * КОНЕЦ ДОКУМЕНТАЦИИ
 * =============================================================================
 */

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
            Toast.makeText(LoginActivity.this, "Админ доступ: admin@fitnessapp.com / admin123\nТренера доступ: coach@fitnessapp.com / coach123", Toast.LENGTH_SHORT).show();
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