package com.example.fitnessapp.admin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminPanel extends AppCompatActivity {

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    // UI
    private TextView tvTotalUsers, tvTotalWorkouts, tvTotalTrainers, tvTotalHalls;
    private EditText etSearch;
    private ListView lvUsers;
    private Spinner spinnerReports;

    // Данные
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<String> userIds = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Инициализация
        initViews();
        loadUsers();
        loadStats();
        setupListeners();
        setupSpinner();
    }

    private void initViews() {
        tvTotalUsers = findViewById(R.id.tvTotalUsers);
        tvTotalWorkouts = findViewById(R.id.tvTotalWorkouts);
        tvTotalTrainers = findViewById(R.id.tvTotalTrainers);
        tvTotalHalls = findViewById(R.id.tvTotalHalls);

        etSearch = findViewById(R.id.etSearch);
        lvUsers = findViewById(R.id.lvUsers);
        spinnerReports = findViewById(R.id.spinnerReports);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        lvUsers.setAdapter(adapter);
    }

    private void setupSpinner() {
        String[] reports = {"Посещаемость", "Популярные тренировки", "Активность тренеров"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, reports);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerReports.setAdapter(spinnerAdapter);
    }

    // ==================== ЗАГРУЗКА ПОЛЬЗОВАТЕЛЕЙ ====================
    private void loadUsers() {
        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                userIds.clear();

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.getKey();
                    String email = userSnapshot.child("email").getValue(String.class);
                    String name = userSnapshot.child("name").getValue(String.class);
                    String phone = userSnapshot.child("phone").getValue(String.class);

                    if (email != null) {
                        userIds.add(uid);

                        String display = "";
                        if (name != null && !name.isEmpty()) {
                            display = name;
                        } else {
                            display = email;
                        }

                        if (phone != null && !phone.isEmpty()) {
                            display += " - " + phone;
                        }

                        userList.add(display);
                    }
                }

                adapter.notifyDataSetChanged();
                tvTotalUsers.setText(String.valueOf(userList.size()));

                Toast.makeText(AdminPanel.this,
                        "Загружено: " + userList.size() + " пользователей",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminPanel.this,
                        "Ошибка: " + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    // ==================== ЗАГРУЗКА СТАТИСТИКИ ====================
    private void loadStats() {
        // Тренировки
        mDatabase.child("workouts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalWorkouts.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Тренеры
        mDatabase.child("trainers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalTrainers.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        // Залы
        mDatabase.child("halls").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tvTotalHalls.setText(String.valueOf(snapshot.getChildrenCount()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    // ==================== НАСТРОЙКА КНОПОК ====================
    private void setupListeners() {
        // Поиск
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Клик по пользователю
        lvUsers.setOnItemClickListener((parent, view, position, id) -> {
            if (position < userList.size()) {
                Toast.makeText(this, "Выбран: " + userList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        // Добавить
        findViewById(R.id.btnAddUser).setOnClickListener(v ->
                Toast.makeText(this, "Добавление пользователя", Toast.LENGTH_SHORT).show());

        // Изменить
        findViewById(R.id.btnEditUser).setOnClickListener(v -> {
            int pos = lvUsers.getCheckedItemPosition();
            if (pos != ListView.INVALID_POSITION && pos < userList.size()) {
                Toast.makeText(this, "Редактирование: " + userList.get(pos), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Выберите пользователя", Toast.LENGTH_SHORT).show();
            }
        });

        // Удалить
        findViewById(R.id.btnDeleteUser).setOnClickListener(v -> {
            int pos = lvUsers.getCheckedItemPosition();
            if (pos != ListView.INVALID_POSITION && pos < userIds.size()) {
                deleteUser(userIds.get(pos));
            } else {
                Toast.makeText(this, "Выберите пользователя", Toast.LENGTH_SHORT).show();
            }
        });

        // Тренировки
        findViewById(R.id.btnManageWorkouts).setOnClickListener(v ->
                Toast.makeText(this, "Управление тренировками", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnManageSchedule).setOnClickListener(v ->
                Toast.makeText(this, "Управление расписанием", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btnManageTrainers).setOnClickListener(v ->
                Toast.makeText(this, "Управление тренерами", Toast.LENGTH_SHORT).show());

        // Залы
        findViewById(R.id.btnManageHalls).setOnClickListener(v ->
                Toast.makeText(this, "Управление залами", Toast.LENGTH_SHORT).show());

        // Отчеты
        findViewById(R.id.btnGenerateReport).setOnClickListener(v -> {
            String type = spinnerReports.getSelectedItem().toString();
            Toast.makeText(this, "Отчет: " + type, Toast.LENGTH_SHORT).show();
        });

        // Настройки
        findViewById(R.id.btnSystemSettings).setOnClickListener(v ->
                Toast.makeText(this, "Расширенные настройки", Toast.LENGTH_SHORT).show());

        // Выход
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            mAuth.signOut();
            finish();
        });
    }

    // ==================== УДАЛЕНИЕ ПОЛЬЗОВАТЕЛЯ ====================
    private void deleteUser(String uid) {
        mDatabase.child("Users").child(uid).removeValue()
                .addOnSuccessListener(aVoid ->
                        Toast.makeText(this, "Пользователь удален", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}