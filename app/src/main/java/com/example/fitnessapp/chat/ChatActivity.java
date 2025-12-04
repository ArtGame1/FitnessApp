package com.example.fitnessapp.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.emoji2.widget.EmojiEditText;

import com.example.fitnessapp.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class ChatActivity extends AppCompatActivity {

    private ListView listOfMessages;
    private RelativeLayout activity_chat;
    private FirebaseListAdapter<Message> adapter;
    private EmojiEditText emojiEditText;
    private ImageView emojiBtn, submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        //Инициализация
        activity_chat = findViewById(R.id.activity_chat);
        submitBtn = findViewById(R.id.submitBtn);
        emojiBtn = findViewById(R.id.emojiBtn);
        emojiEditText = findViewById(R.id.textField);
        listOfMessages = findViewById(R.id.listOfMessages);

        //Показываем чат сразу БЕЗ проверки авторизации
        Toast.makeText(this, "Добро пожаловать в чат!", Toast.LENGTH_SHORT).show();

        //Загружаем сообщения
        displayAllMessages();

        //Обработка отправки сообщения
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = emojiEditText.getText().toString().trim();
                if (!messageText.isEmpty()) {
                    // Отправляем как "Пользователь" без авторизации
                    Message message = new Message("Пользователь", messageText);
                    FirebaseDatabase.getInstance().getReference("messages")
                            .push()
                            .setValue(message);

                    emojiEditText.setText("");
                }
            }
        });
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
                // Находим View элементы и заполняем их
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
}