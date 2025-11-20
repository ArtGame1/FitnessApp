package com.example.fitnessapp.admin;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;

import java.util.ArrayList;
import java.util.Arrays;

public class AdminPanel extends AppCompatActivity {

    private ListView lvUsers;
    private Spinner spinnerReports;
    private Switch switchNotifications, switchAutoBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        initializeViews();
        setupUserList();
        setupEventListeners();
    }

    private void initializeViews() {
        lvUsers = findViewById(R.id.lvUsers);
        spinnerReports = findViewById(R.id.spinnerReports);
        switchNotifications = findViewById(R.id.switchNotifications);
        switchAutoBackup = findViewById(R.id.switchAutoBackup);
    }

    private void setupUserList() {
        //Пример данных пользователей
        ArrayList<String> users = new ArrayList<>(Arrays.asList(
                "Иван Петров (premium)",
                "Мария Сидорова (basic)",
                "Алексей Козлов (premium)",
                "Елена Новикова (basic)",
                "Дмитрий Волков (blocked)"
        ));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, users);
        lvUsers.setAdapter(adapter);
    }

    private void setupEventListeners() {
        // Кнопки управления пользователями
        findViewById(R.id.btnAddUser).setOnClickListener(v ->
                showToast("Добавление пользователя"));

        findViewById(R.id.btnEditUser).setOnClickListener(v ->
                showToast("Редактирование пользователя"));

        findViewById(R.id.btnBlockUser).setOnClickListener(v ->
                showToast("Блокировка пользователя"));

        // Кнопки управления контентом
        findViewById(R.id.btnManageWorkouts).setOnClickListener(v ->
                showToast("Управление тренировками"));

        findViewById(R.id.btnManageMaterials).setOnClickListener(v ->
                showToast("Управление материалами"));

        findViewById(R.id.btnManageArticles).setOnClickListener(v ->
                showToast("Управление статьями"));
        // Кнопки платежей и аналитики
        findViewById(R.id.btnManagePayments).setOnClickListener(v ->
                showToast("Управление платежами"));

        findViewById(R.id.btnGenerateReport).setOnClickListener(v ->
                showToast("Генерация отчета: " +
                        spinnerReports.getSelectedItem()));

        // Кнопка настроек системы
        findViewById(R.id.btnSystemSettings).setOnClickListener(v ->
                showToast("Расширенные настройки"));

        // Переключатели
        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) ->
                showToast("Уведомления: " + (isChecked ? "вкл" : "выкл")));

        switchAutoBackup.setOnCheckedChangeListener((buttonView, isChecked) ->
                showToast("Авто-бэкап: " + (isChecked ? "вкл" : "выкл")));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}