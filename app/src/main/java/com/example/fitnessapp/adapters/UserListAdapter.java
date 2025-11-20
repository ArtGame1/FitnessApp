package com.example.fitnessapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.models.User;

import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserViewHolder> {

    private List<User> userList; //Список пользователей для отображения
    private OnUserActionListener listener; //Слушатель для обработки действий с пользователями
    public interface OnUserActionListener {
        void onEditUser(int position); //Метод для редактирования пользователя

        void onDeleteUser(int position); //Метод для удаления пользователя

        void onDeleteUserClick(User user); //Метод для обработки нажатия на кнопку удаления пользователя
    }

    public UserListAdapter(List<User> userList, OnUserActionListener listener) {
        this.userList = userList; //Устанавливает пользовательский список
        this.listener = listener; //Устанавливает слушателя действий
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_list, parent, false); //Создает представление из XML макета.
        return new UserViewHolder(view); //Возвращает новый экземпляр UserViewHolder, привязанный к созданному представлению
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User currentUser = userList.get(position); //Получает текущего пользователя по позиции

        holder.textViewUserName.setText(currentUser.getFullName()); //Устанавливает полное имя пользователя.
        holder.textViewUserEmail.setText(currentUser.getEmail()); //Устанавливает полное email пользователя.
        holder.textViewUserAdditionalInfo.setText("Пароль: " + currentUser.getAdditionalInfo()); //Устанавливает дополнительную информацию (например, пароль)

        holder.buttonDeleteUser.setOnClickListener(v -> {
            if (listener != null) { //Проверяет, инициализирован ли слушатель
                int currentPosition = holder.getAdapterPosition(); //Получает текущую позицию элемента в адаптере
                if (currentPosition != RecyclerView.NO_POSITION) { //Проверяет, что позиция действительна
                    listener.onDeleteUser(currentPosition); //Вызывает метод удаления пользователя у слушателя
                }
            }
        });
    }

    @Override
    public int getItemCount() { //Метод для получения общего количества элементов в списке
        return userList.size(); //Возвращает размер списка пользователей
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUserName; //Поле для отображения имени пользователя.
        TextView textViewUserEmail; //Поле для отображения email пользователя.
        TextView textViewUserAdditionalInfo; //Поле для отображения дополнительной информации о пользователе.
        ImageButton buttonDeleteUser; //Кнопка для удаления пользователя.

        public UserViewHolder(@NonNull View itemView) {
            super(itemView); //Вызывает родительский конструктор.
            //Привязывает элементы пользовательского интерфейса к переменным.
            textViewUserName = itemView.findViewById(R.id.txtUserName); //Ищет текстовое поле имени.
            textViewUserEmail = itemView.findViewById(R.id.txtUserEmail); //Ищет текстовое поле email.
            textViewUserAdditionalInfo = itemView.findViewById(R.id.textViewUserAdditionalInfo); //Ищет текстовое поле дополнительной информации.
            buttonDeleteUser = itemView.findViewById(R.id.btnDeleteUser); //Ищет кнопку удаления пользователя.
        }
    }

    //Метод для обновления списка пользователей.
    public void updateList(List<User> newList) {
        this.userList = newList; //Обновляет список пользователей.
        notifyDataSetChanged(); //Уведомляет адаптер о том, что данные изменились.
    }
}