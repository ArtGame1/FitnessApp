package com.example.fitnessapp.activities;

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

    private ImageView arrowBackBtn; //Объявляет переменную для кнопки "Назад" (стрелка)
    private TextView edtProfile; //Объявляет переменную для текста "Редактировать профиль"
    private TextView chgPassword; //Объявляет переменную для текста "Сменить пароль"
    private TextView exitView; //Объявляет переменную для кнопки "Выйти"
    private EditText nameEdtText, surnameEdtText, emailEdtText; //Объявляет переменные для полей ввода имени, фамилии и email
    private String name, surname, email; //Объявляет переменные для хранения имени, фамилии и email
    private Button saveBtn; //Объявляет переменную для кнопки "Сохранить"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); //Устанавливает макет для этой активности

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        arrowBackBtn = findViewById(R.id.arrow_back); //Ищет кнопку "Назад" по её ID и присваивает переменной
        edtProfile = findViewById(R.id.edtProfile); //Ищет текст "Редактировать профиль" по его ID и присваивает переменной
        chgPassword = findViewById(R.id.chgPassword); //Ищет текст "Сменить пароль" по его ID и присваивает переменной

        nameEdtText = findViewById(R.id.nameEdtText); //Ищет поле ввода имени по его ID и присваивает переменной
        surnameEdtText = findViewById(R.id.surnameEdtText); //Ищет поле ввода фамилии по его ID и присваивает переменной
        emailEdtText = findViewById(R.id.emailEdtText); //Ищет поле ввода email по его ID и присваивает переменной

        saveBtn = findViewById(R.id.saveBtn); //Ищет кнопку "Сохранить" по её ID и присваивает переменной

        exitView = findViewById(R.id.exit); //Ищет кнопку "Выйти" по её ID и присваивает переменной.
        exitView.setOnClickListener(new View.OnClickListener() { //Устанавливает слушатель нажатия на кнопку "Выйти".
            @Override
            public void onClick(View v) { //Переопределяет метод onClick для обработки нажатия.
                exitApplication(); //Вызывает метод для выхода из приложения.
            }
        });

        saveBtn.setOnClickListener(v -> saveUserData()); //Устанавливает слушатель нажатия на кнопку "Сохранить", который вызывает метод сохранения данных.

        arrowBackBtn.setOnClickListener(v -> { //Устанавливает слушатель нажатия на кнопку "Назад".
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class); //Создает Intent для перехода на MainActivity.
            startActivity(intent); //Запускает MainActivity.
        });

        edtProfile.setOnClickListener(v -> { //Устанавливает слушатель нажатия на текст "Редактировать профиль".
            Intent intent = new Intent(ProfileActivity.this, UserManagementActivity.class); //Создает Intent для перехода на SettingsActivity.
            intent.putExtra("MODE", "EDIT_PROFILE"); //Передает дополнительную информацию о режиме редактирования профиля.
            startActivityForResult(intent, 1); //Запускает SettingsActivity с ожиданием результата.
        });

        chgPassword.setOnClickListener(v -> { //Устанавливает слушатель нажатия на текст "Сменить пароль".
            Intent intent = new Intent(ProfileActivity.this, UserManagementActivity.class); //Создает Intent для перехода на SettingsActivity.
            intent.putExtra("MODE", "CHANGE_PASSWORD"); //Передает дополнительную информацию о режиме смены пароля.
            startActivity(intent); // Запускает SettingsActivity.
        });
        loadProfileData(); //Загружает данные профиля при создании активности.
    }

    //Метод для выхода из приложения.
    private void exitApplication() {
        new AlertDialog.Builder(this) //Создает новый диалог.
                .setTitle("Выход приложения") //Устанавливает заголовок диалога.
                .setMessage("Вы уверены, что хотите выйти?") //Устанавливает сообщение диалога.
                .setPositiveButton("Exit", (dialog, which) -> { //Устанавливает кнопку "Выход" с обработчиком нажатия.
                    finishAffinity(); //Завершает все активности в текущем приложении.
                    System.exit(0); //Закрывает приложение.
                })
                .setNegativeButton("Cancel", null) //Устанавливает кнопку "Отмена" и ничего не делает при нажатии.
                .show(); //Показывает диалог.
    }

    private void saveUserData() { //Метод для сохранения данных пользователя.
        String firstName = nameEdtText.getText().toString(); //Получает введенное имя из поля ввода.
        String lastName = surnameEdtText.getText().toString(); //Получает введенную фамилию из поля ввода.
        String email = emailEdtText.getText().toString(); //Получает введенный email из поля ввода.

        //Проверка, заполнены ли все обязательные поля.
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show(); //Показывает сообщение, если поля пустые.
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
        surname = "Faisal"; //Устанавливает фамилию (возможно, загруженную из источника данных).
        email = "fpecial3@gmail.com"; //Устанавливает email (возможно, загруженный из источника данных).

        //nameEdtText.setText(name); //Устанавливает текст поля ввода имени (пока закомментировано).
        //surnameEdtText.setText(surname); //Устанавливает текст поля ввода фамилии (пока закомментировано).
        //emailEdtText.setText(email); //Устанавливает текст поля ввода email (пока закомментировано).
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Переопределяет метод для обработки результатов активностей.
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) { // Проверяет, соответствует ли код запроса ожидаемому значению.
            if (resultCode == RESULT_OK) { //Проверяет, был ли результат успешным.
                String newName = data.getStringExtra("NAME"); //Получает новое имя из возвращенных данных.
                String newSurname = data.getStringExtra("SURNAME"); //Получает новую фамилию из возвращенных данных.
                String newEmail = data.getStringExtra("EMAIL"); //Получает новый email из возвращенных данных.

                nameEdtText.setText(newName); //Устанавливает новое имя в поле ввода.
                surnameEdtText.setText(newSurname); //Устанавливает новую фамилию в поле ввода.
                emailEdtText.setText(newEmail); //Устанавливает новый email в поле ввода.

                Toast.makeText(this, "Успешное обновление профиля!", Toast.LENGTH_SHORT).show(); //Показывает сообщение об успешном обновлении профиля.

                name = newName; //Сохраняет новое имя в переменную экземпляра.
                surname = newSurname; //Сохраняет новую фамилию в переменную экземпляра.
                email = newEmail; //Сохраняет новый email в переменную экземпляра.
                saveProfileData(); //Вызывает метод для сохранения обновленных данных профиля.
            } else {
                Toast.makeText(this, "Обновление профиля отменено!", Toast.LENGTH_SHORT).show(); //Показывает сообщение, если обновление было отменено.
            }
        }
    }

    private void saveProfileData() { //Метод для сохранения данных профиля (пока не реализован).
        //В этом методе можно сохранить данные профиля пользователя, если это необходимо.
    }

    @Override
    protected void onResume() { // Переопределяет метод onResume, который вызывается, когда активность становится видимой для пользователя.
        super.onResume(); // Вызывает родительский метод.
    }

    @Override
    public void finish() {
        super.finish();
        //Анимация при возврате назад
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}