package com.example.fitnessapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etNickname, etPhone, etBirthDate, etBio;
    private Button btnSave;
    private ImageView ivAvatar;
    private Button btnChangeAvatar;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private String userId;
    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //Привязываем поля
        etName = findViewById(R.id.etName);
        etNickname = findViewById(R.id.etNickname);
        etPhone = findViewById(R.id.etPhone);
        etBirthDate = findViewById(R.id.etBirthDate);
        etBio = findViewById(R.id.etBio);
        btnSave = findViewById(R.id.btnSave);

        ivAvatar = findViewById(R.id.iv_avatar);
        btnChangeAvatar = findViewById(R.id.btn_change_avatar);

        //Firebase
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userId);
            storageReference = FirebaseStorage.getInstance().getReference();
            loadUserData(); //Загружаем текущие данные
        }

        //Кнопка "Сохранить"
        btnSave.setOnClickListener(v -> saveUserData());

        if (btnChangeAvatar != null) {
            btnChangeAvatar.setOnClickListener(v -> pickImageFromGallery());
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            ivAvatar.setImageURI(imageUri);
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Показываем сообщение о загрузке
        Toast.makeText(this, "Загрузка фото...", Toast.LENGTH_SHORT).show();

        // Создаём путь для файла: avatars/userId.jpg
        StorageReference fileRef = storageReference.child("avatars/" + userId + ".jpg");

        // Загружаем файл
        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Получаем URL загруженного файла
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String photoUrl = uri.toString();
                                // Сохраняем URL в базу данных
                                databaseReference.child("photoUrl").setValue(photoUrl)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EditProfileActivity.this, "Аватар обновлён!", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EditProfileActivity.this, "Ошибка сохранения URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfileActivity.this, "Ошибка получения URL: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfileActivity.this, "Ошибка загрузки: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadUserData() {
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                Map<String, Object> data = (Map<String, Object>) task.getResult().getValue();
                if (data != null) {
                    if (data.get("name") != null) etName.setText(data.get("name").toString());
                    if (data.get("nickname") != null) etNickname.setText(data.get("nickname").toString());
                    if (data.get("phone") != null) etPhone.setText(data.get("phone").toString());
                    if (data.get("birthDate") != null) etBirthDate.setText(data.get("birthDate").toString());
                    if (data.get("bio") != null) etBio.setText(data.get("bio").toString());
                }
            }
        });
    }

    private void saveUserData() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", etName.getText().toString());
        updates.put("nickname", etNickname.getText().toString());
        updates.put("phone", etPhone.getText().toString());
        updates.put("birthDate", etBirthDate.getText().toString());
        updates.put("bio", etBio.getText().toString());

        databaseReference.updateChildren(updates).addOnCompleteListener(aVoid -> {
            Toast.makeText(this, "Профиль обновлён!", Toast.LENGTH_SHORT).show();
            finish(); //Возвращает на страницу профиля
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Ошибка: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}