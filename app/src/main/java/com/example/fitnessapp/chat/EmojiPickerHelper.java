package com.example.fitnessapp.chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import androidx.emoji2.widget.EmojiEditText;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

public class EmojiPickerHelper {

    private AlertDialog emojiDialog;
    private Context context;
    private EmojiEditText editText;
    private ImageView emojiButton;

    public EmojiPickerHelper(Context context, EmojiEditText editText, ImageView emojiButton) {
        this.context = context;
        this.editText = editText;
        this.emojiButton = emojiButton;
        setupEmojiButton();
    }

    private void setupEmojiButton() {
        emojiButton.setOnClickListener(v -> showEmojiPicker());
    }

    private void showEmojiPicker() {
        if (emojiDialog == null) {
            View dialogView = LayoutInflater.from(context)
                    .inflate(R.layout.dialog_emoji_picker, null);

            RecyclerView recyclerView = dialogView.findViewById(R.id.emoji_recycler_view);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 8));
            recyclerView.setAdapter(new EmojiAdapter(context, emoji -> {
                insertEmoji(emoji);
                emojiDialog.dismiss();
            }));

            emojiDialog = new AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle("Эмодзи")
                    .setNegativeButton("Закрыть", (dialog, which) -> dialog.dismiss())
                    .create();
        }

        emojiDialog.show();
    }

    private void insertEmoji(String emoji) {
        int cursorPosition = editText.getSelectionStart();
        String text = editText.getText().toString();

        if (cursorPosition >= 0) {
            String newText = text.substring(0, cursorPosition) +
                    emoji +
                    text.substring(cursorPosition);

            editText.setText(newText);
            editText.setSelection(cursorPosition + emoji.length());
        } else {
            editText.append(emoji);
        }
    }

    public void close() {
        if (emojiDialog != null && emojiDialog.isShowing()) {
            emojiDialog.dismiss();
        }
    }
}