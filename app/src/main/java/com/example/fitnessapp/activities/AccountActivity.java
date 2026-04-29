package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ AccountActivity
 * =============================================================================
 *
 *
 * ЧТО РЕАЛИЗОВАНО В ДАННОМ КЛАССЕ
 * --------------------------------
 *
 * В классе AccountActivity на данный момент реализована только одна функция:
 * переход на главный экран приложения (MainActivity) при нажатии на кнопку
 * "Вернуться на главную страницу".
 *
 *
 * ЧТО ЕСТЬ НА ЭКРАНЕ (ИЗ XML-РАЗМЕТКИ)
 * -------------------------------------
 *
 * Экран аккаунта содержит следующие элементы:
 *
 * 1. Фоновое изображение в верхней части экрана (imageView)
 * 2. Аватар пользователя (imageView2)
 * 3. Имя пользователя — "Артур Бутырев" (textView)
 * 4. Email пользователя — "fitness@gmail.com" (textView2)
 *
 * А также шесть кнопок:
 *
 * 5. Кнопка "Мои отзывы" (button)
 * 6. Кнопка "Настройки учетной записи" (button2)
 * 7. Кнопка "Личные данные" (button3)
 * 8. Кнопка "Уведомление" (button4)
 * 9. Кнопка "Настройки отпечатков пальцев" (button5)
 * 10. Кнопка "Вернуться на главную страницу" (btn_back_to_main_page)
 *
 *
 * ЧТО РАБОТАЕТ ИЗ ЭТОГО
 * ----------------------
 *
 * В текущей версии работает ТОЛЬКО одна кнопка:
 * - "Вернуться на главную страницу" — при нажатии открывает MainActivity
 *
 * Остальные пять кнопок (Мои отзывы, Настройки учетной записи, Личные данные,
 * Уведомление, Настройки отпечатков пальцев) пока НЕ работают, так как для них
 * не добавлены обработчики нажатий в коде. Они только отображаются на экране.
 *
 *
 * КАК РАБОТАЕТ ЕДИНСТВЕННАЯ РАБОЧАЯ КНОПКА
 * -----------------------------------------
 *
 * При нажатии на кнопку "Вернуться на главную страницу" происходит следующее:
 *
 * 1. Создаётся Intent (намерение) перейти с текущего экрана AccountActivity
 *    на экран MainActivity
 * 2. Запускается MainActivity
 * 3. Пользователь видит главный экран приложения
 *
 *
 * ЧТО НУЖНО ДОБАВИТЬ В БУДУЩЕМ
 * -----------------------------
 *
 * Для каждой из пяти неработающих кнопок нужно:
 * - Добавить переменную в классе (private Button button;)
 * - Найти кнопку через findViewById
 * - Установить слушатель setOnClickListener
 * - Внутри слушателя создать Intent для перехода на нужный экран
 *
 * Например, для кнопки "Мои отзывы" нужно будет открыть экран со списком отзывов,
 * для "Настройки учетной записи" — экран настроек и так далее.
 *
 *
 * ИТОГ
 * ----
 *
 * На данный момент AccountActivity — это экран-заглушка с красивым интерфейсом,
 * но из всего функционала работает только кнопка возврата на главный экран.
 * Остальные кнопки ждут своей реализации в следующих версиях приложения.
 *
 * =============================================================================
 */

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountActivity extends AppCompatActivity {

    private Button btn_back_to_main_page;
    private Button btnEditProfile;      //кнопка "Редактировать профиль"
    private Button btnSettings;          //Кнопка "Настройки учетной записи"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        //Кнопка "Вернуться на главную страницу"
        btn_back_to_main_page = findViewById(R.id.btn_back_to_main_page);
        btn_back_to_main_page.setOnClickListener(v -> {
            Intent intent = new Intent(AccountActivity.this, MainActivity.class);
            startActivity(intent);
        });

        //КНОПКА "РЕДАКТИРОВАТЬ ПРОФИЛЬ"
        btnEditProfile = findViewById(R.id.btn_edit_profile);
        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }

        //КНОПКА "НАСТРОЙКИ УЧЕТНОЙ ЗАПИСИ" — тоже открывает редактирование
        btnSettings = findViewById(R.id.button2);
        if (btnSettings != null) {
            btnSettings.setOnClickListener(v -> {
                Intent intent = new Intent(AccountActivity.this, EditProfileActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String email = currentUser.getEmail();

            TextView userName = findViewById(R.id.textView);
            TextView userEmail = findViewById(R.id.textView2);
            ImageView userAvatar = findViewById(R.id.imageView2);  // ← ВАЖНО: такой ID есть в твоём XML

            if (userEmail != null) {
                userEmail.setText(email);
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
            databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists() && userName != null) {
                        String name = snapshot.child("name").getValue(String.class);
                        if (name != null && !name.isEmpty()) {
                            userName.setText(name);
                        } else {
                            String defaultName = email.split("@")[0];
                            userName.setText(defaultName);
                        }

                        // ✅ ЗАГРУЖАЕМ АВАТАРКУ
                        String photoUrl = snapshot.child("photoUrl").getValue(String.class);
                        if (photoUrl != null && !photoUrl.isEmpty()) {
                            Glide.with(AccountActivity.this)
                                    .load(photoUrl)
                                    .circleCrop()
                                    .placeholder(R.drawable.profile)
                                    .error(R.drawable.profile)
                                    .into(userAvatar);
                        } else if (currentUser.getPhotoUrl() != null) {
                            Glide.with(AccountActivity.this)
                                    .load(currentUser.getPhotoUrl())
                                    .circleCrop()
                                    .into(userAvatar);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Ошибка
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUserData();
    }
}