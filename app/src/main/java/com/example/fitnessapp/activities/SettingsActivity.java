package com.example.fitnessapp.activities;

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

public class SettingsActivity extends AppCompatActivity implements UserListAdapter.OnUserActionListener{

    private EditText edtNewUserFirstName; //Поле ввода для имени нового пользователя.
    private EditText edtNewUserLastName; //Поле ввода для фамилии нового пользователя.
    private EditText edtNewUserEmail; //Поле ввода для email нового пользователя.
    private EditText edtNewUserAdditional; //Поле ввода для дополнительной информации о новом пользователе.
    private Button btnAddUser; //Кнопка для добавления нового пользователя.
    private RecyclerView recyclerViewUsers; //RecyclerView для отображения списка пользователей.
    private UserListAdapter userListAdapter; //Адаптер для работы со списком пользователей.

    private List<User> userList = new ArrayList<>(); //Список для хранения пользователей.
    private Gson gson = new Gson(); //Объект Gson для работы с JSON.

    private static final String PREFS_NAME = "AppSettingsUserManagement"; //Имя SharedPreferences для сохранения данных пользователей.
    private static final String USER_LIST_KEY = "ManagedUsers"; //Ключ для сохранения списка пользователей в SharedPreferences.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings); //Устанавливает макет для этой активности.

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //Привязывает элементы пользовательского интерфейса к переменным.
        edtNewUserFirstName = findViewById(R.id.edtNewUserName); //Поле ввода имени.
        edtNewUserLastName = findViewById(R.id.edtNewLastName); //Поле ввода фамилии.
        edtNewUserEmail = findViewById(R.id.edtNewUserEmail); //Поле ввода email.
        edtNewUserAdditional = findViewById(R.id.edtNewUserPassword); //Поле ввода дополнительной информации.
        btnAddUser = findViewById(R.id.btnAddUser); //Кнопка для добавления пользователя.
        recyclerViewUsers = findViewById(R.id.recyclerViewUsers); //RecyclerView для списка пользователей.

        loadUsers(); //Загружает пользователей из SharedPreferences.

        userListAdapter = new UserListAdapter(userList, this); //Инициализирует адаптер с текущим списком пользователей и текущей активностью в качестве слушателя.
        recyclerViewUsers.setLayoutManager(new LinearLayoutManager(this)); //Устанавливает LinearLayoutManager для RecyclerView.
        recyclerViewUsers.setAdapter(userListAdapter); //Устанавливает адаптер для RecyclerView.

        //Устанавливает слушатель на кнопку добавления пользователя.
        btnAddUser.setOnClickListener(v -> addNewUser()); //Вызывает метод добавления нового пользователя при нажатии на кнопку.
    }

    private void addNewUser() { //Метод для добавления нового пользователя.
        //Получает данные пользователя из полей ввода.
        String firstName = edtNewUserFirstName.getText().toString().trim();
        String lastName = edtNewUserLastName.getText().toString().trim();
        String email = edtNewUserEmail.getText().toString().trim();
        String additionalInfo = edtNewUserAdditional.getText().toString().trim();

        //Проверяет, заполнены ли все обязательные поля.
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show();//Сообщает пользователю о необходимости заполнить поля.
            return; //Выходит из метода, если поля пустые.
        }

        String userId = String.valueOf(System.currentTimeMillis()); //Генерирует уникальный идентификатор для нового пользователя.
        User newUser = new User(userId, firstName, lastName, email, additionalInfo); //Создает нового пользователя.

        userList.add(newUser); //Добавляет нового пользователя в список.
        userListAdapter.notifyItemInserted(userList.size() - 1); //Уведомляет адаптер о добавлении нового пользователя.
        saveUsers(); //Сохраняет обновленный список пользователей.

        //Очищает поля ввода.
        edtNewUserFirstName.setText("");
        edtNewUserLastName.setText("");
        edtNewUserEmail.setText("");
        edtNewUserAdditional.setText("");

        Toast.makeText(this, "Пользователь успешно добавлен!", Toast.LENGTH_SHORT).show(); //Сообщает о успешном добавлении пользователя.
    }

    private void loadUsers() { //Метод для загрузки пользователей из SharedPreferences.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //Получает SharedPreferences.
        String jsonUsers = prefs.getString(USER_LIST_KEY, null); //Получает строку JSON со списком пользователей.

        if (jsonUsers != null) { //Если данные пользователей существуют...
            Type type = new TypeToken<ArrayList<User>>() {}.getType(); //Определяет тип для десериализации списка пользователей.
            userList = gson.fromJson(jsonUsers, type); //Преобразует JSON-строку в список пользователей.
            if (userList == null) { //Если результат null, инициализирует пустой список.
                userList = new ArrayList<>();
            }
        } else {
            userList = new ArrayList<>(); //Если данных нет, инициализирует пустой список.
        }
    }

    private void saveUsers() { //Метод для сохранения пользователей в SharedPreferences.
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE); //Получает SharedPreferences.
        SharedPreferences.Editor editor = prefs.edit(); //Получает редактор для изменения SharedPreferences.
        String jsonUsers = gson.toJson(userList); //Преобразует список пользователей в JSON-строку.
        editor.putString(USER_LIST_KEY, jsonUsers); //Сохраняет JSON-строку в SharedPreferences.
        editor.apply(); //Применяет изменения.
    }

    @Override
    public void onEditUser(int position) { //Метод для обработки редактирования пользователя.
        User user = userList.get(position); //Получает пользователя по позиции.

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
        userList.remove(position); //Удаляет пользователя из списка.
        userListAdapter.notifyItemRemoved(position); //Уведомляет адаптер о удалении.
        saveUsers(); //Сохраняет обновленный список пользователей.
        Toast.makeText(this, "Пользователь удален!", Toast.LENGTH_SHORT).show(); //Сообщает о успешном удалении.
    }

    @Override
    public void onDeleteUserClick(User user) {
        //Переопределяет метод для действий при клике на удаление пользователя (пока не реализован).

    }
}