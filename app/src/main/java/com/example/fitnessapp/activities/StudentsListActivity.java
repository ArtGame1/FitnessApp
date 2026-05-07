package com.example.fitnessapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fitnessapp.R;
import com.example.fitnessapp.models.Student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentsListActivity extends AppCompatActivity {

    //переменные класса
    private RecyclerView recyclerView; // список учеников
    private StudentAdapter adapter; // адаптер для списка
    private DatabaseReference studentsRef; // ссылка на students в firebase
    private DatabaseReference assignmentsRef; // ссылка на assignments в firebase (задания)
    private List<Student> studentList = new ArrayList<>(); // список учеников
    private List<String> studentIdList = new ArrayList<>(); // список id учеников

    // массив упражнений для основной группы (10 штук)
    private final String[] MAIN_GROUP_EXERCISES = {
            "🏋️ Приседания со штангой - 3x10",
            "🏋️ Жим лежа - 3x8",
            "🏋️ Становая тяга - 3x8",
            "🤸 Подтягивания - 3x максимум",
            "🤸 Отжимания на брусьях - 3x10",
            "🏋️ Выпады с гантелями - 3x12",
            "🏋️ Жим гантелей сидя - 3x10",
            "🏋️ Тяга штанги в наклоне - 3x10",
            "🧘 Скручивания на пресс - 3x20",
            "🧘 Планка - 3x60 секунд"
    };

    // массив упражнений для подгруппы (10 штук)
    private final String[] SUBGROUP_EXERCISES = {
            "🤸 Отжимания от пола - 3x12",
            "🤸 Приседания без веса - 3x15",
            "🏃 Берпи - 3x10",
            "🏃 Выпрыгивания - 3x12",
            "🤸 Боковые выпады - 3x10 (каждая нога)",
            "🏃 Скалолаз - 3x15 (каждая нога)",
            "🧘 Русский твист с весом - 3x12",
            "🤸 Подъем ног в висе - 3x8",
            "🏃 Зашагивания на платформу - 3x10",
            "🏋️ Махи гирей - 3x12"
    };

    // массив заданий для освобожденных (8 штук)
    private final String[] EXEMPTED_TASKS = {
            "📝 Реферат: 'Влияние физических упражнений на сердце'",
            "📊 Презентация: 'Здоровый образ жизни'",
            "🎤 Доклад: 'История фитнеса'",
            "📹 Видео-разбор техники упражнений",
            "📖 Эссе: 'План восстановления после травмы'",
            "📋 План тренировок для начинающих",
            "🧠 Кроссворд по анатомии",
            "🎯 Исследование: 'Питание и спорт'"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        //настраиваем recyclerview
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter();
        recyclerView.setAdapter(adapter);

        //подключаемся к firebase
        studentsRef = FirebaseDatabase.getInstance().getReference("students");
        assignmentsRef = FirebaseDatabase.getInstance().getReference("assignments");

        //кнопка "Назад"
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        //загружаем учеников
        loadStudents();
    }

    //загрузка учеников из firebase
    private void loadStudents() {
        studentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                studentList.clear();
                studentIdList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Student student = child.getValue(Student.class);
                    if (student != null) {
                        studentList.add(student);
                        studentIdList.add(child.getKey());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentsListActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // сохранение задания в firebase
    private void saveAssignmentToFirebase(String studentId, String studentName, String assignmentText, String groupType, String groupName) {
        Toast.makeText(this, "🔥 saveAssignmentToFirebase ВЫЗВАН!", Toast.LENGTH_SHORT).show();

        Map<String, Object> assignment = new HashMap<>();
        assignment.put("studentId", studentId);
        assignment.put("studentName", studentName);
        assignment.put("group", groupName);
        assignment.put("groupType", groupType);
        assignment.put("task", assignmentText);
        assignment.put("timestamp", System.currentTimeMillis());
        assignment.put("status", "pending");

        String key = assignmentsRef.push().getKey();
        Toast.makeText(this, "🔑 Ключ: " + key, Toast.LENGTH_SHORT).show();

        if (key != null) {
            assignmentsRef.child(key).setValue(assignment)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "✅ СОХРАНЕНО! Задание для " + studentName, Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "❌ Ошибка: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
        }
    }

    // модальное окно со списком заданий
    private void showExercisesDialog(String studentId, String studentName, String groupType, String groupName) {
        Toast.makeText(this, "📢 Открываем диалог для " + studentName, Toast.LENGTH_SHORT).show();

        String title;
        String[] exercisesList;

        // выбираем какой список показать в зависимости от группы
        if (groupType.equals("main")) {
            title = "🏋️ " + groupName + " - упражнения для " + studentName;
            exercisesList = MAIN_GROUP_EXERCISES;
        } else if (groupType.equals("subgroup")) {
            title = "🤸 " + groupName + " - упражнения для " + studentName;
            exercisesList = SUBGROUP_EXERCISES;
        } else {
            title = "📚 " + groupName + " - задания для " + studentName;
            exercisesList = EXEMPTED_TASKS;
        }

        // создаем и показываем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setItems(exercisesList, (dialog, which) -> {
                    String selected = exercisesList[which];
                    Toast.makeText(this, "💾 СОХРАНЯЮ: " + selected, Toast.LENGTH_LONG).show();
                    saveAssignmentToFirebase(studentId, studentName, selected, groupType, groupName);
                })
                .setPositiveButton("Закрыть", null)
                .show();
    }

    // адаптер для списка учеников
    private class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Student student = studentList.get(position);
            String studentId = studentIdList.get(position);
            holder.nameTextView.setText(student.getFullName());

            // отключаем старый слушатель
            holder.radioGroup.setOnCheckedChangeListener(null);

            // устанавливаем галочку на выбранную группу
            if ("main".equals(student.getGroupId())) {
                holder.radioMain.setChecked(true);
            } else if ("subgroup".equals(student.getGroupId())) {
                holder.radioSubgroup.setChecked(true);
            } else if ("exempt".equals(student.getGroupId())) {
                holder.radioExempt.setChecked(true);
            } else {
                holder.radioGroup.clearCheck();
            }

            // слушатель на радиокнопки - срабатывает при выборе группы
            holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                String newGroupType = null;
                String newGroupName = "";

                if (checkedId == R.id.radioMain) {
                    newGroupType = "main";
                    newGroupName = "Основная группа";
                } else if (checkedId == R.id.radioSubgroup) {
                    newGroupType = "subgroup";
                    newGroupName = "Подгруппа";
                } else if (checkedId == R.id.radioExempt) {
                    newGroupType = "exempt";
                    newGroupName = "Освобожденные";
                }

                if (newGroupType == null) return;

                String finalNewGroupType = newGroupType;
                String finalNewGroupName = newGroupName;

                // сохраняем группу в firebase
                studentsRef.child(studentId).child("groupId").setValue(newGroupType)
                        .addOnSuccessListener(aVoid -> {
                            student.setGroupId(finalNewGroupType);
                            Toast.makeText(StudentsListActivity.this,
                                    "✅ " + student.getFullName() + " → " + finalNewGroupName,
                                    Toast.LENGTH_SHORT).show();

                            // показываем модальное окно с заданиями
                            showExercisesDialog(studentId, student.getFullName(), finalNewGroupType, finalNewGroupName);
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(StudentsListActivity.this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            });
        }

        @Override
        public int getItemCount() {
            return studentList.size();
        }

        // viewholder - хранит ссылки на виджеты в ячейке
        class ViewHolder extends RecyclerView.ViewHolder {
            TextView nameTextView;
            RadioGroup radioGroup;
            RadioButton radioMain, radioSubgroup, radioExempt;

            ViewHolder(View itemView) {
                super(itemView);
                nameTextView = itemView.findViewById(R.id.studentName);
                radioGroup = itemView.findViewById(R.id.radioGroup);
                radioMain = itemView.findViewById(R.id.radioMain);
                radioSubgroup = itemView.findViewById(R.id.radioSubgroup);
                radioExempt = itemView.findViewById(R.id.radioExempt);
            }
        }
    }
}