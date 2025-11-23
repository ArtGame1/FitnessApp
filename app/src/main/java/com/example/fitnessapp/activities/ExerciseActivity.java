package com.example.fitnessapp.activities;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fitnessapp.R;

import java.util.Random;

public class ExerciseActivity extends AppCompatActivity {
    ImageView backExerciseImageView; //Изображение для фона экрана
    ImageView btnBackArrow; //Кнопка "Назад"
    TextView textViewTimer; //Текстовое поле, отображающее таймер
    TextView textViewDayOfExercise; //Текстовое поле, отображающее день упражнения
    AppCompatButton btnStartTimer; //Кнопка для запуска/остановки таймера
    AppCompatButton btnSetTimer; //Кнопка для установки таймера

    TimePicker timePicker; //TimePicker для прокрутки времени

    EditText editTextTimeInput; //Поле ввода времени
    Boolean isTimerOn = false; //Флаг, указывающий, запущен ли таймер
    int seconds; //Время в секундах
    int day; //Переменная для хранения дня упражнения

    ProgressBar progressBar; //Полоса прогресса для отображения оставшегося времени
    CountDownTimer countDownTimer; //Объект CountDownTimer для обратного отсчета

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise); //Устанавливает макет для этой активности

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        // Установите флаг, чтобы скрыть строку состояния.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        View decorView = getWindow().getDecorView(); //Получаем DecorView окна

        //Устанавливаем флаги для скрытия навигационной панели и перевода приложения в полноэкранный режим
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION //Флаг для скрытия навигационной панели
                | View.SYSTEM_UI_FLAG_FULLSCREEN; //Флаг для перевода приложения в полноэкранный режим

        //Применяем указанные UI опции к DecorView
        decorView.setSystemUiVisibility(uiOptions);

        //Привязывает элементы пользовательского интерфейса к переменным
        backExerciseImageView = findViewById(R.id.backExerciseImageView);
        //progressBar = findViewById(R.id.progressBar); // ЗАКОММЕНТИРОВАНО: убрали ProgressBar
        textViewDayOfExercise = findViewById(R.id.textViewDayOfExercise);
        textViewTimer = findViewById(R.id.textViewTimer);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        //editTextTimeInput = findViewById(R.id.editTextTimeInput); // ЗАКОММЕНТИРОВАНО: убрали EditText
        //btnSetTimer = findViewById(R.id.btnSetTimer); // ЗАКОММЕНТИРОВАНО: убрали кнопку установки времени
        btnStartTimer = findViewById(R.id.btnStartTimer);

        // ДОБАВЛЕНО: Инициализация TimePicker
        timePicker = findViewById(R.id.timePicker);

        // ДОБАВЛЕНО: Настройка TimePicker
        if (timePicker != null) {
            timePicker.setIs24HourView(true); // Устанавливаем 24-часовой формат
            timePicker.setHour(0); // Начальное значение часов
            timePicker.setMinute(30); // Начальное значение минут

            // ДОБАВЛЕНО: Слушатель изменений TimePicker
            timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    // Обновляем время при прокрутке
                    updateTimeFromTimePicker();
                }
            });
        }

        //Получает день из предыдущей активности
        day = getIntent().getIntExtra("day", 0);

        //Устанавливает случайное изображение фона
        setRandomBackgroundImage();

        //Восстанавливает состояние таймера при пересоздании активности
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds"); //Восстанавливает время
            isTimerOn = savedInstanceState.getBoolean("isTimerOn"); //Восстанавливает состояние таймера
            //progressBar.setProgress(seconds); // ЗАКОММЕНТИРОВАНО: убрали ProgressBar
            setTimer(seconds); //Устанавливает текст таймера
            if (isTimerOn) { //Если таймер был включен...
                startExerciseTimer(seconds); //Запускает таймер
            }
        } else {
            // ДОБАВЛЕНО: Устанавливаем начальное время из TimePicker
            updateTimeFromTimePicker();
        }

        //Устанавливает слушатель на кнопку для установки таймера
        //btnSetTimer.setOnClickListener(v -> { // ЗАКОММЕНТИРОВАНО: убрали старую логику установки времени
        //    String input = editTextTimeInput.getText().toString(); //Получает текст из поля
        //    if (!input.isEmpty()) { //Проверяет, не пустое ли поле
        //        seconds = Integer.parseInt(input); //Преобразует текст в секунды
        //        if (seconds > 0) { //Если введенное время положительное...
        //            progressBar.setMax(seconds); //Устанавливает максимальное значение прогресс-бара
        //            progressBar.setProgress(0); //Сбрасывает прогресс
        //            setTimer(seconds); //Устанавливает таймер
        //        } else {
        //            Toast.makeText(this, "Введите положительное время", Toast.LENGTH_SHORT).show(); //Сообщает пользователю, если время отрицательное
        //        }
        //    }
        //});

        //Устанавливает слушатель на кнопку для запуска/остановки таймера
        btnStartTimer.setOnClickListener(v -> {
            if (!isTimerOn) { //Если таймер не включен...
                // ДОБАВЛЕНО: Обновляем время из TimePicker перед запуском
                updateTimeFromTimePicker();
                if (seconds > 0) { //Если время установлено...
                    btnStartTimer.setText(R.string.stop); //Меняет текст на "Стоп"
                    startExerciseTimer(seconds); //Запускает таймер
                } else {
                    Toast.makeText(this, "Сначала установите время", Toast.LENGTH_SHORT).show(); //Предупреждает пользователя установить время
                }
            } else {
                stopCountdownTimer(); //Останавливает таймер, если он уже идет
            }
        });

        //Устанавливает слушатель на кнопку "Назад"
        btnBackArrow.setOnClickListener(v -> onBackPressed()); //Возвращает к предыдущей активности
    }

    // ДОБАВЛЕНО: Метод для обновления времени из TimePicker
    private void updateTimeFromTimePicker() {
        if (timePicker != null) {
            int hours = timePicker.getHour();
            int minutes = timePicker.getMinute();
            seconds = hours * 3600 + minutes * 60;
            setTimer(seconds); // Обновляем отображение таймера
        }
    }

    //Метод для установки случайного фонового изображения
    private void setRandomBackgroundImage() {
        Random random = new Random(); //Создает объект Random
        int randomImageNumber = 1 + random.nextInt(5); // Генерация случайного числа от 1 до 5

        //В зависимости от сгенерированного номера, устанавливает изображение
        switch (randomImageNumber) {
            case 1:
                backExerciseImageView.setImageResource(R.drawable.back_06);
                break;
            case 2:
                backExerciseImageView.setImageResource(R.drawable.back_07);
                break;
            case 3:
                backExerciseImageView.setImageResource(R.drawable.back_08);
                break;
            case 4:
                backExerciseImageView.setImageResource(R.drawable.back_09);
                break;
            case 5:
                backExerciseImageView.setImageResource(R.drawable.back_10);
                break;
        }
    }

    //Метод для установки текста таймера.
    private void setTimer(int seconds) {
        // ДОБАВЛЕНО: Форматирование времени в ЧЧ:ММ:СС
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        if (hours > 0) {
            textViewTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
        } else {
            textViewTimer.setText(String.format("%02d:%02d", minutes, secs));
        }
    }

    // Метод для запуска таймера
    private void startExerciseTimer(int duration) {
        isTimerOn = true; // Устанавливает флаг, что таймер работает.
        countDownTimer = new CountDownTimer(duration * 1000L, 1000) { // Создает новый таймер с указанной продолжительностью.
            public void onTick(long millisUntilFinished) { // Метод, который вызывается каждую секунду.
                seconds--; // Уменьшает оставшееся время на каждую секунду.
                //progressBar.setProgress(duration - seconds); // ЗАКОММЕНТИРОВАНО: убрали ProgressBar
                setTimer(seconds); // Обновляет текстовое поле таймера.
            }

            // Метод, который вызывается, когда таймер завершает обратный отсчет.
            public void onFinish() {
                Toast.makeText(ExerciseActivity.this, "Упражнение завершено, начинаем перерыв", Toast.LENGTH_SHORT).show(); // Показывает сообщение о завершении упражнения.
                startBreakTimer(); // Запускает таймер перерыва.
            }
        }.start(); // Запускает таймер.
    }

    private void startBreakTimer() {
        int breakDuration = 300; //Устанавливает продолжительность перерыва в секундах.
        seconds = breakDuration; //Инициализирует переменную seconds с длительностью перерыва.
        //progressBar.setMax(breakDuration); // ЗАКОММЕНТИРОВАНО: убрал ProgressBar
        //progressBar.setProgress(0); // ЗАКОММЕНТИРОВАНО: убрал ProgressBar
        setTimer(breakDuration); //Устанавливает текст таймера на значение продолжительности перерыва.

        //Создает новый CountDownTimer для обратного отсчета перерыва.
        countDownTimer = new CountDownTimer(breakDuration * 1000, 1000) {
            public void onTick(long millisUntilFinished) { //Метод, который вызывается каждую секунду.
                seconds--; //Уменьшает оставшееся время.
                //progressBar.setProgress(breakDuration - seconds); // ЗАКОММЕНТИРОВАНО: убрали ProgressBar
                setTimer(seconds); //Обновляет текст таймера.
            }

            public void onFinish() { //Метод, вызываемый по окончании перерыва.
                Toast.makeText(ExerciseActivity.this, "Перерыв завершен", Toast.LENGTH_SHORT).show(); //Показывает сообщение о завершении перерыва.
                resetTimer(); //Сбрасывает таймер.
            }
        }.start(); //Запускает таймер перерыва.
    }

    private void stopCountdownTimer() { //Метод для остановки текущего таймера.
        if (countDownTimer != null) { //Проверяет, что таймер существует.
            countDownTimer.cancel(); //Отменяет активный таймер.
            isTimerOn = false; //Устанавливает флаг, что таймер не работает.
            btnStartTimer.setText(R.string.start); //Меняет текст кнопки на "Начать".
        }
    }

    private void resetTimer() { //Метод для сброса таймера в начальное состояние.
        stopCountdownTimer(); //Останавливает таймер.
        seconds = 0; //Сбрасывает секунды до 0.
        //progressBar.setProgress(0); // ЗАКОММЕНТИРОВАНО: убрали ProgressBar
        setTimer(0); //Устанавливает текст таймера на 0.

        // ДОБАВЛЕНО: Сброс TimePicker к начальным значениям
        if (timePicker != null) {
            timePicker.setHour(0);
            timePicker.setMinute(30);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //Переопределяет метод для сохранения состояния активности.
        outState.putInt("seconds", seconds); //Сохраняет текущее значение секунд.
        outState.putBoolean("isTimerOn", isTimerOn); //Сохраняет состояние активного таймера.
        super.onSaveInstanceState(outState); //Вызывает родительский метод для сохранения состояния.
    }
}