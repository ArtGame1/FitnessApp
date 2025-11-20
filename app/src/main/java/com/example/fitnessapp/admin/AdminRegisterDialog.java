package com.example.fitnessapp.admin;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AdminRegisterDialog extends Dialog {
    private Context context;
    private AdminManager adminManager;
    private AdminDialogListener listener;

    private EditText etAdminEmail, etAdminPassword, etConfirmPassword;
    private Button btnRegister, btnCancel;

    public interface AdminDialogListener {
        void onSuccess();
        void onFailure();
    }

    public AdminRegisterDialog(Context context, AdminDialogListener listener) {
        super(context);
        this.context = context;
        this.adminManager = new AdminManager(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Создаем layout программно
        LinearLayout mainLayout = new LinearLayout(context);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setPadding(50, 40, 50, 40);
        mainLayout.setBackgroundColor(0xFFFFFFFF);

        // Заголовок
        TextView title = new TextView(context);
        title.setText("Регистрация администратора");
        title.setTextSize(18);
        title.setTypeface(null, android.graphics.Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(0xFF000000);
        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        titleParams.bottomMargin = 30;
        title.setLayoutParams(titleParams);

        // Поле email
        etAdminEmail = new EditText(context);
        etAdminEmail.setHint("Email администратора");
        etAdminEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        etAdminEmail.setBackgroundResource(android.R.drawable.edit_text);
        LinearLayout.LayoutParams emailParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        emailParams.bottomMargin = 20;
        etAdminEmail.setLayoutParams(emailParams);

        // Поле пароля
        etAdminPassword = new EditText(context);
        etAdminPassword.setHint("Пароль");
        etAdminPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etAdminPassword.setBackgroundResource(android.R.drawable.edit_text);
        LinearLayout.LayoutParams passwordParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        passwordParams.bottomMargin = 20;
        etAdminPassword.setLayoutParams(passwordParams);

        // Поле подтверждения пароля
        etConfirmPassword = new EditText(context);
        etConfirmPassword.setHint("Подтвердите пароль");
        etConfirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etConfirmPassword.setBackgroundResource(android.R.drawable.edit_text);
        LinearLayout.LayoutParams confirmParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        confirmParams.bottomMargin = 30;
        etConfirmPassword.setLayoutParams(confirmParams);

        // Layout для кнопок
        LinearLayout buttonsLayout = new LinearLayout(context);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));

        // Кнопка Отмена
        btnCancel = new Button(context);
        btnCancel.setText("Отмена");
        btnCancel.setTextColor(0xFF000000);
        btnCancel.setBackgroundColor(0xFFFFFFFF);
        LinearLayout.LayoutParams cancelParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        cancelParams.weight = 1;
        cancelParams.rightMargin = 10;
        cancelParams.height = 120;
        btnCancel.setLayoutParams(cancelParams);

        // Кнопка Регистрация
        btnRegister = new Button(context);
        btnRegister.setText("Регистрация");
        btnRegister.setTextColor(0xFFFFFFFF);
        btnRegister.setBackgroundColor(0xFF6200EE);
        LinearLayout.LayoutParams registerParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        registerParams.weight = 1;
        registerParams.rightMargin = 10;
        registerParams.height = 120;
        btnRegister.setLayoutParams(registerParams);

        // Добавляем элементы
        buttonsLayout.addView(btnCancel);
        buttonsLayout.addView(btnRegister);

        mainLayout.addView(title);
        mainLayout.addView(etAdminEmail);
        mainLayout.addView(etAdminPassword);
        mainLayout.addView(etConfirmPassword);
        mainLayout.addView(buttonsLayout);

        setContentView(mainLayout);

        // Обработчики кликов
        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFailure();
            }
            dismiss();
        });

        btnRegister.setOnClickListener(v -> {
            String email = etAdminEmail.getText().toString().trim();
            String password = etAdminPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(context, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 4) {
                Toast.makeText(context, "Пароль должен быть не менее 4 символов", Toast.LENGTH_SHORT).show();
                return;
            }

            if (adminManager.registerAdmin(email, password)) {
                Toast.makeText(context, "Администратор зарегистрирован!", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onSuccess();
                }
                dismiss();
            } else {
                Toast.makeText(context, "Ошибка регистрации", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }
}