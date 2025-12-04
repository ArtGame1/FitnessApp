package com.example.fitnessapp.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.emoji2.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ChatActivity extends AppCompatActivity implements EmojiAdapter.EmojiClickListener {

    private ListView listOfMessages;
    private RelativeLayout activity_chat;
    private FirebaseListAdapter<Message> adapter;
    private EmojiEditText emojiEditText;
    private ImageView emojiBtn, submitBtn;
    private FrameLayout emojiContainer;
    private RecyclerView emojiRecyclerView;
    private boolean isEmojiPickerVisible = false;
    private EmojiAdapter emojiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //Решение проблемы - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        //Инициализация
        activity_chat = findViewById(R.id.activity_chat);
        submitBtn = findViewById(R.id.submitBtn);
        emojiBtn = findViewById(R.id.emojiBtn);
        emojiEditText = findViewById(R.id.textField);
        listOfMessages = findViewById(R.id.listOfMessages);
        emojiContainer = findViewById(R.id.emojiContainer);
        emojiRecyclerView = findViewById(R.id.emojiRecyclerView);

        //Настройка Emoji RecyclerView
        setupEmojiPicker();

        //Показываем чат сразу БЕЗ проверки авторизации
        Toast.makeText(this, "Добро пожаловать в чат!", Toast.LENGTH_SHORT).show();

        //Загружаем сообщения
        displayAllMessages();

        //Обработка отправки сообщения
        submitBtn.setOnClickListener(v -> sendMessage());

        //Обработка нажатия на emojiBtn
        emojiBtn.setOnClickListener(v -> toggleEmojiPicker());

        //Скрываем emoji picker при наборе текста
        emojiEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Автоматически скрываем emoji picker при наборе текста
                if (isEmojiPickerVisible) {
                    hideEmojiPicker();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Скрытие emoji picker при нажатии на список сообщений
        listOfMessages.setOnTouchListener((v, event) -> {
            if (isEmojiPickerVisible) {
                hideEmojiPicker();
                return true;
            }
            return false;
        });
    }

    private void setupEmojiPicker() {
        //Настройка RecyclerView для эмодзи
        emojiAdapter = new EmojiAdapter(this, this);
        emojiRecyclerView.setLayoutManager(new GridLayoutManager(this, 8)); // 8 колонок
        emojiRecyclerView.setAdapter(emojiAdapter);

        //Скрываем контейнер по умолчанию
        emojiContainer.setVisibility(View.GONE);
    }

    private void toggleEmojiPicker() {
        if (isEmojiPickerVisible) {
            hideEmojiPicker();
        } else {
            showEmojiPicker();
        }
    }

    private void showEmojiPicker() {
        isEmojiPickerVisible = true;
        emojiContainer.setVisibility(View.VISIBLE);
        emojiBtn.setImageResource(R.drawable.ic_keyboard); //Меняем иконку на клавиатуру
        emojiContainer.bringToFront();

        //Прокручиваем RecyclerView к началу
        emojiRecyclerView.scrollToPosition(0);
    }

    private void hideEmojiPicker() {
        isEmojiPickerVisible = false;
        emojiContainer.setVisibility(View.GONE);
        emojiBtn.setImageResource(R.drawable.smile); //Возвращаем иконку эмодзи

        //Фокусируемся на поле ввода
        emojiEditText.requestFocus();
    }

    @Override
    public void onEmojiClick(String emoji) {
        //Вставляем эмодзи в поле ввода
        int start = emojiEditText.getSelectionStart();
        int end = emojiEditText.getSelectionEnd();

        if (start < 0) {
            //Если нет выделения, добавляем в конец
            emojiEditText.append(emoji);
        } else {
            //Заменяем выделенный текст
            emojiEditText.getText().replace(
                    Math.min(start, end),
                    Math.max(start, end),
                    emoji,
                    0,
                    emoji.length()
            );
        }

        //Прокручиваем к последнему эмодзи (опционально)
        emojiRecyclerView.scrollToPosition(emojiAdapter.getItemCount() - 1);
    }

    private void sendMessage() {
        String messageText = emojiEditText.getText().toString().trim();
        if (!messageText.isEmpty()) {
            //Отправляем как "Пользователь" без авторизации
            Message message = new Message("Пользователь", messageText);
            FirebaseDatabase.getInstance().getReference("messages")
                    .push()
                    .setValue(message);

            emojiEditText.setText("");

            //Скрываем emoji picker после отправки
            if (isEmojiPickerVisible) {
                hideEmojiPicker();
            }

            //Прокручиваем список сообщений вниз
            listOfMessages.post(() -> listOfMessages.smoothScrollToPosition(adapter.getCount() - 1));
        }
    }

    private void displayAllMessages() {
        //1. Создаем запрос к базе данных
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("messages");
        Query query = databaseRef.orderByChild("timestamp");

        //2. Создаем опции для адаптера
        FirebaseListOptions<Message> options = new FirebaseListOptions.Builder<Message>()
                .setQuery(query, Message.class)
                .setLayout(R.layout.list_item)
                .setLifecycleOwner(this)
                .build();

        //3. Создаем адаптер
        adapter = new FirebaseListAdapter<Message>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull Message model, int position) {
                //Находим View элементы и заполняем их
                androidx.appcompat.widget.AppCompatTextView messageText = v.findViewById(R.id.message_text);
                androidx.appcompat.widget.AppCompatTextView messageUser = v.findViewById(R.id.message_user);
                androidx.appcompat.widget.AppCompatTextView messageTime = v.findViewById(R.id.message_time);

                messageUser.setText(model.getUserName());
                messageText.setText(model.getTextMessage());

                if (model.getMessageTime() != null) {
                    messageTime.setText(android.text.format.DateFormat.format("HH:mm",
                            model.getMessageTime()));
                }
            }
        };

        //4. Устанавливаем адаптер в ListView
        listOfMessages.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }

    @Override
    public void onBackPressed() {
        if (isEmojiPickerVisible) {
            hideEmojiPicker();
        } else {
            super.onBackPressed();
        }
    }
}