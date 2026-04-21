package com.example.fitnessapp.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.util.List;

public class StudentsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private DatabaseReference studentsRef;
    private List<Student> studentList = new ArrayList<>();
    private List<String> studentIdList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_list);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter();
        recyclerView.setAdapter(adapter);

        studentsRef = FirebaseDatabase.getInstance().getReference("students");
        loadStudents();
    }

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
                if (studentList.isEmpty()) {
                    Toast.makeText(StudentsListActivity.this, "Нет учеников", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentsListActivity.this, "Ошибка: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ========== ВНУТРЕННИЙ КЛАСС АДАПТЕРА ==========
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

            // Отключаем старый слушатель, чтобы избежать зацикливания
            holder.radioGroup.setOnCheckedChangeListener(null);

            // Устанавливаем текущую группу
            if ("main".equals(student.getGroupId())) {
                holder.radioMain.setChecked(true);
            } else if ("subgroup".equals(student.getGroupId())) {
                holder.radioSubgroup.setChecked(true);
            } else if ("exempt".equals(student.getGroupId())) {
                holder.radioExempt.setChecked(true);
            } else {
                holder.radioGroup.clearCheck();
            }

            // Включаем слушатель
            holder.radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
                String newGroup;
                if (checkedId == R.id.radioMain) {
                    newGroup = "main";
                } else if (checkedId == R.id.radioSubgroup) {
                    newGroup = "subgroup";
                } else if (checkedId == R.id.radioExempt) {
                    newGroup = "exempt";
                } else {
                    return;
                }

                Toast.makeText(StudentsListActivity.this,
                        "Выбрано: " + newGroup + " для " + student.getFullName(),
                        Toast.LENGTH_SHORT).show();

                studentsRef.child(studentId).child("groupId").setValue(newGroup)
                        .addOnSuccessListener(aVoid -> {
                            student.setGroupId(newGroup);
                            Toast.makeText(StudentsListActivity.this, "Сохранено!", Toast.LENGTH_SHORT).show();
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