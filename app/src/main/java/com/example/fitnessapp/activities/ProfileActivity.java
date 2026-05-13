package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ ProfileActivity (ЭКРАН ПРОФИЛЯ ПОЛЬЗОВАТЕЛЯ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * ProfileActivity является экраном профиля пользователя в приложении Fitness App.
 * Сюда пользователь попадает при нажатии на кнопку "Профиль" на главном экране
 * или из бокового меню (пункт "Мой профиль").
 *
 * Этот экран позволяет пользователю:
 * - Просматривать свои данные (имя, фамилию, email)
 * - Редактировать информацию профиля
 * - Сменить пароль
 * - Выйти из приложения
 *
 * В текущей версии данные профиля (имя, фамилия, email) загружаются из
 * статических значений, а не из базы данных или Firebase. Это демо-версия,
 * которая будет доработана в будущем.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Экран профиля содержит следующие элементы:
 *
 * Верхняя панель:
 * - Кнопка "Назад" (arrow_back) — возврат на главный экран
 * - Текст "Редактировать профиль" (edtProfile) — переход к редактированию
 * - Текст "Сменить пароль" (chgPassword) — переход к смене пароля
 *
 * Основная область (поля ввода):
 * - Поле ввода имени (nameEdtText)
 * - Поле ввода фамилии (surnameEdtText)
 * - Поле ввода email (emailEdtText)
 *
 * Нижняя часть:
 * - Кнопка "Сохранить" (saveBtn) — сохраняет нового пользователя в список
 * - Кнопка "Выйти" (exitView) — показывает диалог выхода из приложения
 *
 *
 * ЧАСТЬ 3. РЕДАКТИРОВАНИЕ ПРОФИЛЯ (ДЕТАЛЬНО)
 * --------------------------------------------
 *
 * 3.1. Переход к редактированию:
 *
 * При нажатии на текст "Редактировать профиль" (edtProfile) создаётся Intent
 * для перехода на UserManagementActivity. В Intent добавляется extra-параметр
 * "MODE" со значением "EDIT_PROFILE", чтобы UserManagementActivity знала,
 * в каком режиме она открыта (редактирование профиля, а не управление
 * пользователями).
 *
 * Для запуска используется startActivityForResult() с кодом запроса 1.
 * Это означает, что ProfileActivity ожидает результат обратно от
 * UserManagementActivity после завершения редактирования.
 *
 * 3.2. Получение результата (onActivityResult):
 *
 * Когда пользователь завершает редактирование в UserManagementActivity,
 * вызывается метод onActivityResult() в ProfileActivity. Если результат
 * успешный (RESULT_OK), из Intent извлекаются новые значения:
 * - NAME — новое имя пользователя
 * - SURNAME — новая фамилия пользователя
 * - EMAIL — новый email пользователя
 *
 * Полученные данные:
 * - Устанавливаются в соответствующие поля ввода (EditText)
 * - Сохраняются в переменные экземпляра (name, surname, email)
 * - Вызывается метод saveProfileData() для сохранения (пока не реализован)
 *
 * Если редактирование было отменено, показывается Toast с сообщением
 * "Обновление профиля отменено!".
 *
 *
 * ЧАСТЬ 4. СМЕНА ПАРОЛЯ (ДЕТАЛЬНО)
 * ---------------------------------
 *
 * При нажатии на текст "Сменить пароль" (chgPassword) создаётся Intent для
 * перехода на UserManagementActivity. В Intent добавляется extra-параметр
 * "MODE" со значением "CHANGE_PASSWORD", чтобы UserManagementActivity
 * открылась в режиме смены пароля.
 *
 * В отличие от редактирования профиля, здесь используется startActivity()
 * без ожидания результата, так как смена пароля не требует возврата данных
 * на экран профиля.
 *
 *
 * ЧАСТЬ 5. ДОБАВЛЕНИЕ НОВОГО ПОЛЬЗОВАТЕЛЯ (ДЕТАЛЬНО)
 * ---------------------------------------------------
 *
 * 5.1. Кнопка "Сохранить" (saveBtn):
 *
 * При нажатии на кнопку "Сохранить" вызывается метод saveUserData(),
 * который выполняет следующие действия:
 *
 * 1. Считывает данные из полей ввода: имя, фамилия, email
 * 2. Проверяет, что все поля заполнены (если есть пустые — Toast с ошибкой)
 * 3. Генерирует уникальный ID пользователя на основе текущего времени
 *    (System.currentTimeMillis())
 * 4. Создаёт объект User с полученными данными
 * 5. Открывает SharedPreferences "AppSettingsUserManagement"
 * 6. Загружает существующий список пользователей (если есть)
 * 7. Добавляет нового пользователя в список
 * 8. Сохраняет обновлённый список обратно в SharedPreferences в формате JSON
 * 9. Показывает Toast "Пользователь успешно добавлен!"
 * 10. Переходит на UserManagementActivity
 *
 * 5.2. Формат хранения данных:
 *
 * Список пользователей хранится в SharedPreferences в виде JSON-строки
 * с ключом "ManagedUsers". Для сериализации и десериализации используется
 * библиотека Gson.
 *
 * Пример структуры JSON:
 * [
 *   {
 *     "userId": "1734567890123",
 *     "firstName": "Mickey",
 *     "lastName": "Faisal",
 *     "email": "fpecial3@gmail.com",
 *     "password": ""
 *   }
 * ]
 *
 *
 * ЧАСТЬ 6. ВЫХОД ИЗ ПРИЛОЖЕНИЯ (ДЕТАЛЬНО)
 * ----------------------------------------
 *
 * При нажатии на кнопку "Выйти" (exitView) вызывается метод exitApplication().
 *
 * Метод exitApplication() показывает диалоговое окно подтверждения:
 * - Заголовок: "Выход приложения"
 * - Сообщение: "Вы уверены, что хотите выйти?"
 * - Кнопка "Exit" — завершает все активности (finishAffinity()) и закрывает
 *   приложение (System.exit(0))
 * - Кнопка "Cancel" — закрывает диалог без выхода
 *
 * Используется finishAffinity() для закрытия всех активностей в текущем
 * приложении, а затем System.exit(0) для полного завершения процесса.
 *
 *
 * ЧАСТЬ 7. ЗАГРУЗКА ДАННЫХ ПРОФИЛЯ (ДЕТАЛЬНО)
 * --------------------------------------------
 *
 * В методе loadProfileData() сейчас установлены статические значения:
 * - name = "Mickey"
 * - surname = "Faisal"
 * - email = "fpecial3@gmail.com"
 *
 * Важно: строки с установкой текста в поля ввода закомментированы:
 * //nameEdtText.setText(name);
 * //surnameEdtText.setText(surname);
 * //emailEdtText.setText(email);
 *
 * Это означает, что данные профиля НЕ отображаются в полях ввода при запуске
 * экрана. Поля ввода остаются пустыми. Это ограничение текущей версии.
 *
 * В будущем здесь должна быть загрузка реальных данных пользователя из
 * SharedPreferences, Firebase или другого источника.
 *
 *
 * ЧАСТЬ 8. НАВИГАЦИЯ И АНИМАЦИИ
 * ------------------------------
 *
 * 8.1. Кнопка "Назад" (arrow_back):
 *
 * При нажатии создаётся Intent для перехода на MainActivity (главный экран).
 * ProfileActivity при этом не закрывается, а открывается новая MainActivity
 * поверх неё. Это может привести к накоплению активностей в стеке.
 *
 * Рекомендуется использовать finish() вместо startActivity(), если нужно
 * просто закрыть текущий экран.
 *
 * 8.2. Анимация возврата:
 *
 * В методе finish() переопределена анимация перехода:
 * - slide_in_left — новая активность въезжает слева
 * - slide_out_right — текущая активность уезжает вправо
 *
 *
 * ЧАСТЬ 9. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * -----------------------------
 *
 * При запуске ProfileActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 *
 * ЧАСТЬ 10. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * ---------------------------------------------
 *
 * onCreate() — инициализация активности: поиск View, установка слушателей,
 *              загрузка данных профиля
 *
 * exitApplication() — показывает диалог подтверждения и завершает приложение
 *
 * saveUserData() — сохраняет нового пользователя в SharedPreferences
 *
 * loadProfileData() — загружает данные профиля (сейчас статические значения)
 *
 * onActivityResult() — обрабатывает результат редактирования профиля
 *
 * saveProfileData() — метод для сохранения данных профиля (пока не реализован)
 *
 * onResume() — вызывается при возвращении на экран (пока пустой)
 *
 * finish() — переопределён для установки анимации закрытия
 *
 *
 * ЧАСТЬ 11. МОДЕЛЬ ДАННЫХ (User)
 * ------------------------------
 *
 * Класс User (из пакета com.example.fitnessapp.models) содержит поля:
 * - userId (String) — уникальный идентификатор пользователя
 * - firstName (String) — имя пользователя
 * - lastName (String) — фамилия пользователя
 * - email (String) — электронная почта
 * - password (String) — пароль (в текущей версии пустая строка)
 *
 *
 * ЧАСТЬ 12. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Данные профиля не загружаются:
 *    - Поля ввода остаются пустыми при запуске
 *    - Нужно реализовать загрузку данных текущего пользователя
 *
 * 2. Сохранение профиля не реализовано:
 *    - Метод saveProfileData() пустой
 *    - После редактирования изменения не сохраняются в постоянное хранилище
 *
 * 3. Нет привязки к текущему пользователю:
 *    - Нет идентификации, какой пользователь сейчас авторизован
 *    - Нужно добавить систему авторизации и хранение ID текущего пользователя
 *
 * 4. Кнопка "Назад" создаёт новую MainActivity:
 *    - Вместо закрытия текущей активности открывается новая
 *    - Нужно заменить startActivity() на finish()
 *
 * 5. Сохранение пользователя работает, но нет отображения списка:
 *    - Пользователи сохраняются, но их нельзя увидеть на этом экране
 *    - Нужно добавить отображение текущего профиля
 *
 * 6. Нет валидации email:
 *    - Не проверяется корректность формата email
 *    - Нужно добавить проверку на @ и домен
 *
 * 7. Нет редактирования существующего пользователя:
 *    - Сейчас можно только добавлять новых
 *    - Нужно добавить возможность редактировать текущего пользователя
 *
 * 8. Пароль не сохраняется и не проверяется:
 *    - В методе saveUserData() пароль передаётся пустой строкой
 *    - Нужно добавить поле для ввода и сохранения пароля
 *
 *
 * ЧАСТЬ 13. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - Gson (для сериализации/десериализации JSON)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 14. ИТОГИ
 * ----------------
 *
 * ProfileActivity — это экран профиля пользователя, который находится в стадии
 * разработки. В текущей версии реализованы: навигация на другие экраны
 * (редактирование профиля, смена пароля, управление пользователями), добавление
 * новых пользователей в SharedPreferences, выход из приложения с подтверждением,
 * обработка результатов редактирования и анимации переходов.
 *
 * Однако есть важные ограничения: данные профиля не загружаются в поля ввода,
 * сохранение отредактированного профиля не работает, нет привязки к текущему
 * пользователю. Эти функции требуют доработки в следующих версиях.
 *
 * =============================================================================
 */


import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    //Объявляет переменных для кнопок
    private ImageView arrowBackBtn;
    private TextView edtProfile;
    private TextView chgPassword;
    private TextView exitView;
    private EditText nameEdtText, surnameEdtText, emailEdtText;
    private String name, surname, email;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); //Устанавливает макет для этой активности

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //Ищет значения по её ID и присваивает переменной
        arrowBackBtn = findViewById(R.id.arrow_back);
        edtProfile = findViewById(R.id.edtProfile);
        chgPassword = findViewById(R.id.chgPassword);

        nameEdtText = findViewById(R.id.nameEdtText);
        surnameEdtText = findViewById(R.id.surnameEdtText);
        emailEdtText = findViewById(R.id.emailEdtText);

        saveBtn = findViewById(R.id.saveBtn);

        exitView = findViewById(R.id.exit);
        //Устанавливает слушатель нажатия на кнопку "Выйти".
        exitView.setOnClickListener(v -> {
            exitApplication(); //Вызывает метод для выхода из приложения.
        });

        saveBtn.setOnClickListener(v -> saveUserData()); //Устанавливает слушатель нажатия на кнопку "Сохранить", который вызывает метод сохранения данных.

        arrowBackBtn.setOnClickListener(v -> { //Устанавливает слушатель нажатия на кнопку "Назад".
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class); //Создает Intent для перехода на MainActivity.
            startActivity(intent); //Запускает MainActivity.
        });

        edtProfile.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UserManagementActivity.class);
            intent.putExtra("MODE", "EDIT_PROFILE");
            startActivityForResult(intent, 1);
        });

        chgPassword.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, UserManagementActivity.class);
            intent.putExtra("MODE", "CHANGE_PASSWORD");
            startActivity(intent);
        });
        loadProfileData(); //Загружает данные профиля при создании активности.
    }

    //Метод для выхода из приложения.
    private void exitApplication() {
        new AlertDialog.Builder(this)
                .setTitle("Выход приложения")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Exit", (dialog, which) -> { //Устанавливает кнопку "Выход" с обработчиком нажатия.
                    finishAffinity(); //Завершает все активности в текущем приложении.
                    System.exit(0);
                })
                .setNegativeButton("Cancel", null) //Устанавливает кнопку "Отмена" и ничего не делает при нажатии.
                .show(); //Показывает диалог.
    }

    private void saveUserData() { //Метод для сохранения данных пользователя.
        String firstName = nameEdtText.getText().toString();
        String lastName = surnameEdtText.getText().toString();
        String email = emailEdtText.getText().toString();

        //Проверка, заполнены ли все обязательные поля.
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show();
            return; //Выходит из метода, если есть пустые поля.
        }

        String userId = String.valueOf(System.currentTimeMillis()); //Генерирует уникальный идентификатор для пользователя на основе текущего времени.
        User newUser = new User(userId, firstName, lastName, email, ""); //Создает нового пользователя с введенными данными.

        SharedPreferences prefs = getSharedPreferences("AppSettingsUserManagement", MODE_PRIVATE); //Открывает SharedPreferences для сохранения данных.

        String jsonUsers = prefs.getString("ManagedUsers", null); //Получает строку JSON с пользователями, если такая существует.
        List<User> userList = new ArrayList<>(); //Создает новый список для хранения пользователей.

        //Если jsonUsers не равен null, значит, данные уже есть.
        if (jsonUsers != null) {
            Type type = new TypeToken<ArrayList<User>>() {}.getType(); //Определяет тип для десериализации списка пользователей.
            userList = new Gson().fromJson(jsonUsers, type); //Преобразует строку JSON в список объектов User.
            if (userList == null) { //Если преобразование вернуло null...
                userList = new ArrayList<>(); //Инициализирует пустой список.
            }
        }

        userList.add(newUser); //Добавляет нового пользователя в список.

        SharedPreferences.Editor editor = prefs.edit(); //Получает редактор для изменения SharedPreferences.
        editor.putString("ManagedUsers", new Gson().toJson(userList)); //Сохраняет обновленный список пользователей обратно в SharedPreferences в формате JSON.
        editor.apply(); //Применяет изменения.

        Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_SHORT).show(); //Показывает сообщение об успешном добавлении пользователя.

        Intent intent = new Intent(this, UserManagementActivity.class); //Создает Intent для перехода на SettingsActivity.
        startActivity(intent); //Запускает SettingsActivity.
    }

    private void loadProfileData() {
        name = "Mickey"; //Устанавливает имя (возможно, загруженное из источника данных).
        surname = "Faisal";
        email = "fpecial3@gmail.com";

        //nameEdtText.setText(name); //Устанавливает текст поля ввода имени (пока закомментировано).
        //surnameEdtText.setText(surname);
        //emailEdtText.setText(email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Проверяет, соответствует ли код запроса ожидаемому значению.
            if (resultCode == RESULT_OK) { //Проверяет, был ли результат успешным.
                String newName = data.getStringExtra("NAME");
                String newSurname = data.getStringExtra("SURNAME");
                String newEmail = data.getStringExtra("EMAIL");

                nameEdtText.setText(newName); //Устанавливает новое имя в поле ввода.
                surnameEdtText.setText(newSurname);
                emailEdtText.setText(newEmail);

                Toast.makeText(this, "Успешное обновление профиля!", Toast.LENGTH_SHORT).show();

                name = newName; //Сохраняет новое имя в переменную экземпляра.
                surname = newSurname;
                email = newEmail;
                saveProfileData();
            } else {
                Toast.makeText(this, "Обновление профиля отменено!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProfileData() {

    }

    @Override
    protected void onResume() { // Переопределяет метод onResume, который вызывается, когда активность становится видимой для пользователя.
        super.onResume();
    }

    @Override
    public void finish() {
        super.finish();
        //Анимация при возврате назад
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}