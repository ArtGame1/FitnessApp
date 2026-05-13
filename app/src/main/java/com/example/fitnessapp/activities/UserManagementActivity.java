package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ UserManagementActivity (УПРАВЛЕНИЕ ПОЛЬЗОВАТЕЛЯМИ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * UserManagementActivity является экраном управления пользователями в приложении
 * Fitness App. Сюда пользователь попадает при нажатии на кнопку "Настройки"
 * на главном экране (через MainActivity) или при редактировании профиля.
 *
 * Этот экран позволяет:
 * - Просматривать список всех добавленных пользователей
 * - Добавлять новых пользователей (имя, фамилия, email, дополнительная информация)
 * - Редактировать существующих пользователей
 * - Удалять пользователей из списка
 *
 * Все данные о пользователях сохраняются в SharedPreferences в формате JSON
 * и сохраняются между сессиями приложения.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя часть (форма добавления пользователя):
 * - Поле ввода имени (edtNewUserFirstName)
 * - Поле ввода фамилии (edtNewUserLastName)
 * - Поле ввода email (edtNewUserEmail)
 * - Поле ввода дополнительной информации (edtNewUserAdditional)
 * - Кнопка "Добавить пользователя" (btnAddUser)
 *
 * Нижняя часть (список пользователей):
 * - RecyclerView (recyclerViewUsers) для отображения списка пользователей
 * - Каждый элемент списка содержит: имя, фамилию, email и кнопки действий
 *
 *
 * ЧАСТЬ 3. ХРАНЕНИЕ ДАННЫХ (SHAREDPREFERENCES) (ДЕТАЛЬНО)
 * --------------------------------------------------------
 *
 * 3.1. Имя файла настроек:
 *
 * private static final String PREFS_NAME = "AppSettingsUserManagement";
 *
 * Это отдельный файл SharedPreferences,专门 для хранения списка пользователей.
 *
 * 3.2. Ключ для списка пользователей:
 *
 * private static final String USER_LIST_KEY = "ManagedUsers";
 *
 * 3.3. Формат хранения:
 *
 * Список пользователей хранится в виде JSON-строки. Библиотека Gson используется
 * для сериализации (превращения списка в JSON) и десериализации (превращения
 * JSON обратно в список).
 *
 * Пример JSON:
 * [
 *   {
 *     "userId": "1734567890123",
 *     "firstName": "Иван",
 *     "lastName": "Иванов",
 *     "email": "ivan@example.com",
 *     "additionalInfo": "Дополнительная информация"
 *   },
 *   {
 *     "userId": "1734567890456",
 *     "firstName": "Мария",
 *     "lastName": "Петрова",
 *     "email": "maria@example.com",
 *     "additionalInfo": ""
 *   }
 * ]
 *
 *
 * ЧАСТЬ 4. ОСНОВНЫЕ МЕТОДЫ (ДЕТАЛЬНО)
 * ------------------------------------
 *
 * 4.1. onCreate():
 *
 * - Устанавливает полноэкранный режим (скрывает статус-бар и навигацию)
 * - Устанавливает layout activity_usermanagement.xml
 * - Инициализирует все UI-элементы через findViewById()
 * - Загружает существующих пользователей через loadUsers()
 * - Создаёт адаптер UserListAdapter и подключает его к RecyclerView
 * - Устанавливает слушатель на кнопку добавления пользователя
 *
 * 4.2. addNewUser():
 *
 * Метод для добавления нового пользователя:
 *
 * 1. Считывает данные из полей ввода (имя, фамилия, email, доп. информация)
 * 2. Проверяет, что обязательные поля (имя, фамилия, email) не пустые
 * 3. Генерирует уникальный ID пользователя на основе текущего времени
 * 4. Создаёт объект User с полученными данными
 * 5. Добавляет пользователя в список userList
 * 6. Уведомляет адаптер о добавлении элемента (notifyItemInserted)
 * 7. Сохраняет обновлённый список через saveUsers()
 * 8. Очищает поля ввода
 * 9. Показывает Toast "Пользователь успешно добавлен!"
 *
 * 4.3. loadUsers():
 *
 * Метод для загрузки пользователей из SharedPreferences:
 *
 * 1. Открывает SharedPreferences с именем PREFS_NAME
 * 2. Получает JSON-строку по ключу USER_LIST_KEY
 * 3. Если строка не пустая (jsonUsers != null):
 *    - Определяет тип для десериализации через TypeToken
 *    - Преобразует JSON в список пользователей через gson.fromJson()
 *    - Если результат null, создаёт пустой список
 * 4. Если данных нет (jsonUsers == null):
 *    - Создаёт пустой список
 *
 * 4.4. saveUsers():
 *
 * Метод для сохранения пользователей в SharedPreferences:
 *
 * 1. Открывает SharedPreferences с именем PREFS_NAME
 * 2. Получает редактор для изменения SharedPreferences
 * 3. Преобразует список пользователей в JSON-строку через gson.toJson()
 * 4. Сохраняет JSON-строку по ключу USER_LIST_KEY
 * 5. Применяет изменения через apply()
 *
 * 4.5. onEditUser(int position):
 *
 * Метод для редактирования пользователя (реализация интерфейса
 * UserListAdapter.OnUserActionListener):
 *
 * 1. Получает пользователя из списка по указанной позиции
 * 2. Заполняет поля ввода данными выбранного пользователя:
 *    - Имя, фамилия, email, дополнительная информация
 * 3. Удаляет пользователя из списка (старая запись)
 * 4. Уведомляет адаптер об удалении элемента
 *
 * После этого пользователь может отредактировать данные в полях ввода
 * и нажать "Добавить пользователя" для сохранения изменений.
 *
 * 4.6. onDeleteUser(int position):
 *
 * Метод для удаления пользователя (реализация интерфейса
 * UserListAdapter.OnUserActionListener):
 *
 * 1. Удаляет пользователя из списка по указанной позиции
 * 2. Уведомляет адаптер об удалении элемента
 * 3. Сохраняет обновлённый список через saveUsers()
 * 4. Показывает Toast "Пользователь удален!"
 *
 * 4.7. onDeleteUserClick(User user):
 *
 * Метод-заглушка для обработки клика по удалению (пока не реализован).
 * Требуется интерфейсом UserListAdapter.OnUserActionListener.
 *
 *
 * ЧАСТЬ 5. МОДЕЛЬ ДАННЫХ (USER)
 * -----------------------------
 *
 * Класс User (из пакета com.example.fitnessapp.models) содержит поля:
 *
 * - userId (String) — уникальный идентификатор пользователя
 * - firstName (String) — имя пользователя
 * - lastName (String) — фамилия пользователя
 * - email (String) — электронная почта
 * - additionalInfo (String) — дополнительная информация (пароль, заметки)
 *
 * Методы доступа:
 * - getUserId(), setUserId()
 * - getFirstName(), setFirstName()
 * - getLastName(), setLastName()
 * - getEmail(), setEmail()
 * - getAdditionalInfo(), setAdditionalInfo()
 *
 *
 * ЧАСТЬ 6. ADAPTER И RECYCLERVIEW (ДЕТАЛЬНО)
 * -------------------------------------------
 *
 * 6.1. UserListAdapter:
 *
 * Адаптер для отображения списка пользователей в RecyclerView.
 * Реализует интерфейс OnUserActionListener для передачи событий
 * (редактирование, удаление) обратно в активность.
 *
 * 6.2. Настройка RecyclerView:
 *
 * recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
 * - Устанавливает вертикальный LinearLayoutManager (список в столбик)
 *
 * recyclerViewUsers.setAdapter(userListAdapter);
 * - Подключает адаптер к RecyclerView
 *
 * 6.3. Обновление данных:
 *
 * При добавлении: userListAdapter.notifyItemInserted(userList.size() - 1)
 * При удалении: userListAdapter.notifyItemRemoved(position)
 *
 *
 * ЧАСТЬ 7. ПОЛНОЭКРАННЫЙ РЕЖИМ
 * -----------------------------
 *
 * При запуске UserManagementActivity скрываются системные панели:
 * - Скрыт статус-бар (строка с часами, батареей, сигналом)
 * - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *
 * Код:
 * getWindow().getDecorView().setSystemUiVisibility(
 *     View.SYSTEM_UI_FLAG_FULLSCREEN |
 *     View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
 *     View.SYSTEM_UI_FLAG_LAYOUT_STABLE
 * );
 *
 *
 * ЧАСТЬ 8. ОСНОВНЫЕ МЕТОДЫ (КРАТКИЙ СПИСОК)
 * ------------------------------------------
 *
 * onCreate() — инициализация активности: полноэкранный режим, установка layout,
 *              инициализация View, загрузка пользователей, настройка RecyclerView
 *
 * addNewUser() — добавление нового пользователя в список
 *
 * loadUsers() — загрузка списка пользователей из SharedPreferences
 *
 * saveUsers() — сохранение списка пользователей в SharedPreferences
 *
 * onEditUser() — редактирование пользователя (заполнение полей и удаление старого)
 *
 * onDeleteUser() — удаление пользователя из списка
 *
 * onDeleteUserClick() — заглушка для обработки клика удаления
 *
 *
 * ЧАСТЬ 9. РАБОТА С GSON (ДЕТАЛЬНО)
 * ----------------------------------
 *
 * 9.1. Сериализация (объект → JSON):
 *
 * String jsonUsers = gson.toJson(userList);
 *
 * Превращает список объектов User в JSON-строку.
 *
 * 9.2. Десериализация (JSON → объект):
 *
 * Type type = new TypeToken<ArrayList<User>>() {}.getType();
 * userList = gson.fromJson(jsonUsers, type);
 *
 * Превращает JSON-строку обратно в список объектов User.
 *
 * TypeToken нужен, чтобы Gson знал, что мы хотим получить не просто
 * объект, а коллекцию (ArrayList) объектов User.
 *
 *
 * ЧАСТЬ 10. ОСОБЕННОСТИ РЕДАКТИРОВАНИЯ
 * -------------------------------------
 *
 * Редактирование пользователя реализовано через "удаление старого + добавление нового":
 *
 * 1. При нажатии на кнопку "Редактировать" вызывается onEditUser(position)
 * 2. Данные пользователя загружаются в поля ввода
 * 3. Старый пользователь удаляется из списка
 * 4. Пользователь изменяет данные в полях
 * 5. Пользователь нажимает "Добавить пользователя"
 * 6. Создаётся новый пользователь с изменёнными данными
 *
 * Недостаток этого подхода: теряется связь с original ID пользователя,
 * создаётся новый ID. При необходимости сохранять оригинальный ID нужно
 * модифицировать логику редактирования.
 *
 *
 * ЧАСТЬ 11. ИНТЕРФЕЙС ONDELETEUSERCLICK
 * --------------------------------------
 *
 * UserManagementActivity реализует интерфейс UserListAdapter.OnUserActionListener
 * с тремя методами:
 *
 * - onEditUser(int position) — редактирование пользователя
 * - onDeleteUser(int position) — удаление пользователя
 * - onDeleteUserClick(User user) — дополнительный метод (пока не используется)
 *
 * Это позволяет адаптеру общаться с активностью и передавать события нажатий.
 *
 *
 * ЧАСТЬ 12. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Нет поиска пользователей:
 *    - При большом количестве пользователей трудно найти нужного
 *    - Нужно добавить строку поиска и фильтрацию списка
 *
 * 2. Нет сортировки:
 *    - Пользователи отображаются в порядке добавления
 *    - Нужно добавить сортировку по имени, фамилии, дате
 *
 * 3. Нет валидации email:
 *    - Не проверяется корректность формата email
 *    - Нужно добавить проверку на @ и домен
 *
 * 4. Нет проверки на дубликаты:
 *    - Можно добавить двух пользователей с одинаковым email
 *    - Нужно проверять уникальность email
 *
 * 5. Редактирование изменяет ID:
 *    - При редактировании создаётся новый ID, старый теряется
 *    - Нужно изменить логику: обновлять существующего пользователя
 *
 * 6. Нет подтверждения удаления:
 *    - Пользователь удаляется сразу без подтверждения
 *    - Нужно добавить диалог "Вы уверены?"
 *
 * 7. Нет пагинации:
 *    - При большом количестве пользователей RecyclerView может тормозить
 *    - Нужно добавить постраничную загрузку
 *
 * 8. Нет экспорта/импорта:
 *    - Нельзя выгрузить список пользователей или загрузить из файла
 *    - Нужно добавить функции экспорта/импорта
 *
 *
 * ЧАСТЬ 13. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - AndroidX RecyclerView (для отображения списка пользователей)
 * - Gson (для сериализации/десериализации JSON)
 *
 * Вспомогательные классы:
 * - UserListAdapter (com.example.fitnessapp.adapters) — адаптер для списка
 * - User (com.example.fitnessapp.models) — модель данных пользователя
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 14. ИТОГИ
 * ----------------
 *
 * UserManagementActivity — это полноценный экран для управления пользователями
 * в приложении Fitness App.
 *
 * Реализованы следующие функции:
 * - Добавление новых пользователей с проверкой обязательных полей
 * - Отображение списка пользователей в RecyclerView
 * - Редактирование существующих пользователей
 * - Удаление пользователей из списка
 * - Сохранение данных в SharedPreferences через JSON
 * - Загрузка данных при запуске приложения
 *
 * Все данные сохраняются между сессиями приложения. Экран имеет удобный
 * интерфейс для управления пользователями и готов к дальнейшему расширению.
 *
 * =============================================================================
 */



import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.adapters.UserListAdapter;
import com.example.fitnessapp.models.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class UserManagementActivity extends AppCompatActivity implements UserListAdapter.OnUserActionListener{

    //переменные класса
    private EditText edtNewUserFirstName;
    private EditText edtNewUserLastName;
    private EditText edtNewUserEmail;
    private EditText edtNewUserAdditional;
    private Button btnAddUser;
    private RecyclerView recyclerViewUsers;
    private UserListAdapter userListAdapter;

    private List<User> userList = new ArrayList<>(); //Список для хранения пользователей.
    private Gson gson = new Gson(); //Объект Gson для работы с JSON.

    private static final String PREFS_NAME = "AppSettingsUserManagement"; //Имя SharedPreferences для сохранения данных пользователей.
    private static final String USER_LIST_KEY = "ManagedUsers"; //Ключ для сохранения списка пользователей в SharedPreferences.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermanagement);

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //Привязывает элементы пользовательского интерфейса к переменным.
        edtNewUserFirstName = findViewById(R.id.edtNewUserName);
        edtNewUserLastName = findViewById(R.id.edtNewLastName);
        edtNewUserEmail = findViewById(R.id.edtNewUserEmail);
        edtNewUserAdditional = findViewById(R.id.edtNewUserPassword);
        btnAddUser = findViewById(R.id.btnAddUser);
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers);

        loadUsers(); //Загружает пользователей из SharedPreferences.

        userListAdapter = new UserListAdapter(userList, this);
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewUsers.setAdapter(userListAdapter);

        //Устанавливает слушатель на кнопку добавления пользователя.
        btnAddUser.setOnClickListener(v -> addNewUser());
    }

    private void addNewUser() { //Метод для добавления нового пользователя.
        String firstName = edtNewUserFirstName.getText().toString().trim();
        String lastName = edtNewUserLastName.getText().toString().trim();
        String email = edtNewUserEmail.getText().toString().trim();
        String additionalInfo = edtNewUserAdditional.getText().toString().trim();

        //Проверяет, заполнены ли все обязательные поля.
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = String.valueOf(System.currentTimeMillis()); //Генерирует уникальный идентификатор для нового пользователя.
        User newUser = new User(userId, firstName, lastName, email, additionalInfo);

        userList.add(newUser);
        userListAdapter.notifyItemInserted(userList.size() - 1);
        saveUsers(); //Сохраняет обновленный список пользователей.

        //Очищает поля ввода.
        edtNewUserFirstName.setText("");
        edtNewUserLastName.setText("");
        edtNewUserEmail.setText("");
        edtNewUserAdditional.setText("");

        Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_SHORT).show();
    }

    private void loadUsers() { //Метод для загрузки пользователей из SharedPreferences.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String jsonUsers = prefs.getString(USER_LIST_KEY, null);

        if (jsonUsers != null) { //Если данные пользователей существуют...
            Type type = new TypeToken<ArrayList<User>>() {}.getType();
            userList = gson.fromJson(jsonUsers, type);
            if (userList == null) {
                userList = new ArrayList<>();
            }
        } else {
            userList = new ArrayList<>(); //Если данных нет, инициализирует пустой список.
        }
    }

    private void saveUsers() { //Метод для сохранения пользователей в SharedPreferences.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String jsonUsers = gson.toJson(userList);
        editor.putString(USER_LIST_KEY, jsonUsers);
        editor.apply();
    }

    @Override
    public void onEditUser(int position) { //Метод для обработки редактирования пользователя.
        User user = userList.get(position);

        //Устанавливает данные пользователя в соответствующие поля ввода.
        edtNewUserFirstName.setText(user.getFirstName());
        edtNewUserLastName.setText(user.getLastName());
        edtNewUserEmail.setText(user.getEmail());
        edtNewUserAdditional.setText(user.getAdditionalInfo());

        userList.remove(position); //Удаляет пользователя из списка.
        userListAdapter.notifyItemRemoved(position); //Уведомляет адаптер о удалении пользователя.
    }

    @Override
    public void onDeleteUser(int position) { //Метод для обработки удаления пользователя.
        userList.remove(position);
        userListAdapter.notifyItemRemoved(position);
        saveUsers();
        Toast.makeText(this, "Пользователь удален!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteUserClick(User user) {
        //Переопределяет метод для действий при клике на удаление пользователя (пока не реализован).

    }
}