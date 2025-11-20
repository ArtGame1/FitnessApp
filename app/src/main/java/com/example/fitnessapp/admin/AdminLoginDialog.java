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

public class AdminLoginDialog extends Dialog {
    private Context context;
    private AdminManager adminManager;
    private AdminDialogListener listener;

    private EditText etAdminEmail, etAdminPassword;
    private Button btnLogin, btnCancel;

    public interface AdminDialogListener {
        void onSuccess();
        void onFailure();
    }

    public AdminLoginDialog(Context context, AdminDialogListener listener) {
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
        title.setText("Вход для администратора");
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

        // Предзаполняем email если есть
        if (adminManager.isAdminRegistered()) {
            etAdminEmail.setText(adminManager.getAdminEmail());
        }

        // Поле пароля
        etAdminPassword = new EditText(context);
        etAdminPassword.setHint("Пароль");
        etAdminPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        etAdminPassword.setBackgroundResource(android.R.drawable.edit_text);
        LinearLayout.LayoutParams passwordParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        passwordParams.bottomMargin = 30;
        etAdminPassword.setLayoutParams(passwordParams);

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
        btnCancel.setLayoutParams(cancelParams);

        // Кнопка Войти
        btnLogin = new Button(context);
        btnLogin.setText("Войти");
        btnLogin.setTextColor(0xFFFFFFFF);
        btnLogin.setBackgroundColor(0xFF6200EE);
        LinearLayout.LayoutParams loginParams = new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        loginParams.weight = 1;
        btnLogin.setLayoutParams(loginParams);

        // Добавляем элементы
        buttonsLayout.addView(btnCancel);
        buttonsLayout.addView(btnLogin);

        mainLayout.addView(title);
        mainLayout.addView(etAdminEmail);
        mainLayout.addView(etAdminPassword);
        mainLayout.addView(buttonsLayout);

        setContentView(mainLayout);

        // Обработчики кликов
        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onFailure();
            }
            dismiss();
        });

        btnLogin.setOnClickListener(v -> {
            String email = etAdminEmail.getText().toString().trim();
            String password = etAdminPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show();
                return;
            }

            if (adminManager.isAdmin(email, password)) {
                Toast.makeText(context, "Успешный вход!", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onSuccess();
                }
                dismiss();
            } else {
                Toast.makeText(context, "Неверные данные администратора", Toast.LENGTH_SHORT).show();
                if (listener != null) {
                    listener.onFailure();
                }
            }
        });
    }
}