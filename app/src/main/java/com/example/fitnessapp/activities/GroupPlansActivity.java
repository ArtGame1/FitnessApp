package com.example.fitnessapp.activities;

import android.os.Bundle;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.fitnessapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GroupPlansActivity extends AppCompatActivity {
    private DatabaseReference groupsRef;
    private EditText editMain, editSubgroup, editExempt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_plans);

        groupsRef = FirebaseDatabase.getInstance().getReference("groups");

        editMain = findViewById(R.id.editMainPlan);
        editSubgroup = findViewById(R.id.editSubgroupPlan);
        editExempt = findViewById(R.id.editExemptPlan);

        //Загружаем текущие планы
        loadPlans();

        findViewById(R.id.saveMainPlan).setOnClickListener(v ->
                groupsRef.child("main").child("plan").setValue(editMain.getText().toString())
        );
        findViewById(R.id.saveSubgroupPlan).setOnClickListener(v ->
                groupsRef.child("subgroup").child("plan").setValue(editSubgroup.getText().toString())
        );
        findViewById(R.id.saveExemptPlan).setOnClickListener(v ->
                groupsRef.child("exempt").child("plan").setValue(editExempt.getText().toString())
        );
    }

    private void loadPlans() {
        groupsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("main").hasChild("plan"))
                    editMain.setText(snapshot.child("main").child("plan").getValue(String.class));
                if (snapshot.child("subgroup").hasChild("plan"))
                    editSubgroup.setText(snapshot.child("subgroup").child("plan").getValue(String.class));
                if (snapshot.child("exempt").hasChild("plan"))
                    editExempt.setText(snapshot.child("exempt").child("plan").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }
}