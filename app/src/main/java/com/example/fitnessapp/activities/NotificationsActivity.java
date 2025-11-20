package com.example.fitnessapp.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView btnMenu;
    private LinearLayout dropdownMenu;
    private boolean isMenuVisible = false;
    private LinearLayout notificationsContainer;
    private List<NotificationItem> notificationItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        btnMenu = findViewById(R.id.btnMenu);
        dropdownMenu = findViewById(R.id.dropdownMenu);
        notificationsContainer = findViewById(R.id.notificationsContainer);

        // Инициализируем уведомления
        initializeNotifications();

        setupToolbar();
        setupMenuListeners();
        setupOutsideClickListener();
    }

    private void initializeNotifications() {
        // Добавляем тестовые уведомления
        notificationItems.add(new NotificationItem(
                "Тренеры никогда не спрашивают пароли",
                "Никому не сообщайте данные для входа в аккаунт — ими интересуются только мошенники",
                "14 ноября, 13:15"
        ));

        notificationItems.add(new NotificationItem(
                "Настройте напоминания о тренировках",
                "Экономьте время и не пропускайте тренировки — настроив напоминания, вы будете получать уведомления перед началом занятий. Чтобы настроить, перейдите в раздел Настройки → Уведомления.",
                "14 ноября, 10:30"
        ));

        notificationItems.add(new NotificationItem(
                "Новая тренировка доступна",
                "Добавлена новая кардио-тренировка для начинающих. Попробуйте её в разделе Тренировки → Кардио.",
                "13 ноября, 15:45"
        ));

        // Отображаем уведомления
        displayNotifications();
    }

    private void displayNotifications() {
        notificationsContainer.removeAllViews();

        for (int i = 0; i < notificationItems.size(); i++) {
            NotificationItem item = notificationItems.get(i);
            if (item.isVisible()) {
                addNotificationCard(item, i);
            }
        }

        // Если все уведомления удалены, показываем сообщение
        if (notificationsContainer.getChildCount() == 0) {
            showEmptyState();
        }
    }

    private void addNotificationCard(NotificationItem item, int index) {
        CardView cardView = new CardView(this);
        CardView.LayoutParams cardParams = new CardView.LayoutParams(
                CardView.LayoutParams.MATCH_PARENT,
                CardView.LayoutParams.WRAP_CONTENT
        );
        cardParams.setMargins(0, 0, 0, dpToPx(16));
        cardView.setLayoutParams(cardParams);
        cardView.setCardBackgroundColor(getColor(android.R.color.white));
        cardView.setCardElevation(dpToPx(2));
        cardView.setRadius(dpToPx(8));

        LinearLayout cardContent = new LinearLayout(this);
        cardContent.setOrientation(LinearLayout.VERTICAL);
        cardContent.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

        // Заголовок
        TextView titleText = new TextView(this);
        titleText.setText(item.getTitle());
        titleText.setTextSize(16);
        titleText.setTypeface(titleText.getTypeface(), Typeface.BOLD);
        titleText.setTextColor(getColor(android.R.color.black));
        titleText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) titleText.getLayoutParams()).bottomMargin = dpToPx(8);

        // Сообщение
        TextView messageText = new TextView(this);
        messageText.setText(item.getMessage());
        messageText.setTextSize(14);
        messageText.setTextColor(getColor(android.R.color.darker_gray));
        messageText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) messageText.getLayoutParams()).bottomMargin = dpToPx(12);
        messageText.setLineSpacing(dpToPx(4), 1);

        // Дата
        TextView dateText = new TextView(this);
        dateText.setText(item.getDate());
        dateText.setTextSize(12);
        dateText.setTextColor(getColor(android.R.color.darker_gray));
        dateText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        cardContent.addView(titleText);
        cardContent.addView(messageText);
        cardContent.addView(dateText);
        cardView.addView(cardContent);

        // Добавляем возможность удалять отдельные уведомления по долгому нажатию
        cardView.setOnLongClickListener(v -> {
            showDeleteSingleDialog(index);
            return true;
        });

        notificationsContainer.addView(cardView);
    }

    private void showEmptyState() {
        TextView emptyText = new TextView(this);
        emptyText.setText("Уведомлений нет");
        emptyText.setTextSize(18);
        emptyText.setTextColor(getColor(android.R.color.darker_gray));
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        ((LinearLayout.LayoutParams) emptyText.getLayoutParams()).topMargin = dpToPx(100);

        notificationsContainer.addView(emptyText);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }

    private void setupMenuListeners() {
        btnMenu.setOnClickListener(v -> toggleMenu());

        TextView menuDeleteAll = findViewById(R.id.menuDeleteAll);
        menuDeleteAll.setOnClickListener(v -> {
            deleteAllNotifications();
            hideMenu();
        });
    }

    private void setupOutsideClickListener() {
        View rootView = findViewById(android.R.id.content);
        rootView.setOnClickListener(v -> {
            if (isMenuVisible) {
                hideMenu();
            }
        });

        dropdownMenu.setOnClickListener(v -> {
            // Ничего не делаем, чтобы меню не закрывалось при клике на него
        });
    }

    private void toggleMenu() {
        if (isMenuVisible) {
            hideMenu();
        } else {
            showMenu();
        }
    }

    private void showMenu() {
        dropdownMenu.setVisibility(View.VISIBLE);
        isMenuVisible = true;
    }

    private void hideMenu() {
        dropdownMenu.setVisibility(View.GONE);
        isMenuVisible = false;
    }

    private void deleteAllNotifications() {
        if (notificationItems.isEmpty()) {
            Toast.makeText(this, "Уведомлений нет", Toast.LENGTH_SHORT).show();
            return;
        }

        new android.app.AlertDialog.Builder(this)
                .setTitle("Удалить все уведомления?")
                .setMessage("Все уведомления будут удалены. Это действие нельзя отменить.")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    // Помечаем все уведомления как невидимые
                    for (NotificationItem item : notificationItems) {
                        item.setVisible(false);
                    }

                    // Обновляем отображение
                    displayNotifications();

                    Toast.makeText(this, "Все уведомления удалены", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showDeleteSingleDialog(int index) {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Удалить уведомление?")
                .setMessage("Уведомление будет удалено.")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    notificationItems.get(index).setVisible(false);
                    displayNotifications();
                    Toast.makeText(this, "Уведомление удалено", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    @Override
    public void onBackPressed() {
        if (isMenuVisible) {
            hideMenu();
        } else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }
    }
}