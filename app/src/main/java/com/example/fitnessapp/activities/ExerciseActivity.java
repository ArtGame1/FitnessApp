package com.example.fitnessapp.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.fitnessapp.R;

public class ExerciseActivity extends AppCompatActivity {
    ImageView exerciseAnimationImageView; //Теперь это основная анимация упражнения
    ImageView btnBackArrow; //Кнопка "Назад"
    TextView textViewTimer; //Текстовое поле, отображающее таймер
    TextView textViewDayOfExercise; //Текстовое поле, отображающее день упражнения
    AppCompatButton btnStartTimer; //Кнопка для запуска/остановки таймера
    //AppCompatButton btnSetTimer; //Кнопка для установки таймера

    TimePicker timePicker; //TimePicker для прокрутки времени

    NumberPicker numberPickerHours; //Выбор часов (0-23)
    NumberPicker numberPickerMinutes; //Выбор минут (0-59)
    NumberPicker numberPickerSeconds; //Выбор секунд (0-59)

    //EditText editTextTimeInput; //Поле ввода времени
    Boolean isTimerOn = false; //Флаг, указывающий, запущен ли таймер
    int seconds; //Время в секундах
    int day; //Переменная для хранения дня упражнения
    String exerciseName; //Переменная для хранения названия упражнения
    int exerciseImageResource; //Переменная для хранения ресурса изображения упражнения

    //ProgressBar progressBar; //Полоса прогресса для отображения оставшегося времени
    CountDownTimer countDownTimer; //Объект CountDownTimer для обратного отсчета
    private int initialSeconds = 0; //Используется для сброса таймера к исходному значению
    private ImageView starAnimationView; //Отображает мотивационную звезду без дополнительного текста

    private RelativeLayout motivationLayout; //Основной контейнер для мотивационного окна
    private TextView motivationText, motivationSubText; //Основной текст ("Молодец!", "Отлично!" и т.д.)
    private ImageView starImageView; //Анимированная звезда внутри мотивационного окна
    private AppCompatButton btnCloseMotivation; //Кнопка "Продолжить" для закрытия окна

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
        exerciseAnimationImageView = findViewById(R.id.exerciseAnimationImageView); //Новый ID
        //progressBar = findViewById(R.id.progressBar); //Убираем ProgressBar
        textViewDayOfExercise = findViewById(R.id.textViewDayOfExercise);
        textViewTimer = findViewById(R.id.textViewTimer);
        btnBackArrow = findViewById(R.id.btnBackArrow);
        //editTextTimeInput = findViewById(R.id.editTextTimeInput); //Убираем EditText
        //btnSetTimer = findViewById(R.id.btnSetTimer); //Убираем кнопку установки времени
        btnStartTimer = findViewById(R.id.btnStartTimer);

        //Инициализация TimePicker
        //timePicker = findViewById(R.id.timePicker);

        //Инициализация NumberPickers
        numberPickerHours = findViewById(R.id.numberPickerHours);
        numberPickerMinutes = findViewById(R.id.numberPickerMinutes);
        numberPickerSeconds = findViewById(R.id.numberPickerSeconds);

        //Инициализация мотивационного окна
        motivationLayout = findViewById(R.id.motivationLayout);
        motivationText = findViewById(R.id.motivationText);
        motivationSubText = findViewById(R.id.motivationSubText);
        starImageView = findViewById(R.id.starImageView);
        btnCloseMotivation = findViewById(R.id.btnCloseMotivation);

        starAnimationView = findViewById(R.id.starAnimationView); //Инициализация анимационной звезды

        //НАСТРОЙКА NUMBERPICKERS (ДОБАВЛЕНО)
        setupNumberPickers();

        //Настройка TimePicker с секундами
        if (timePicker != null) {
            timePicker.setIs24HourView(true);

            //ВКЛЮЧАЕМ секунды (это ключевое изменение!)
            try {
                //Получаем доступ к полю секунд через рефлексию
                java.lang.reflect.Field[] fields = timePicker.getClass().getDeclaredFields();
                for (java.lang.reflect.Field field : fields) {
                    if (field.getName().equals("mMinuteSpinner") || field.getName().equals("mHourSpinner")) {
                        field.setAccessible(true);
                        Object minuteSpinner = field.get(timePicker);

                        //Устанавливаем диапазон секунд
                        java.lang.reflect.Method setMinValue = minuteSpinner.getClass().getMethod("setMinValue", int.class);
                        java.lang.reflect.Method setMaxValue = minuteSpinner.getClass().getMethod("setMaxValue", int.class);

                        setMinValue.invoke(minuteSpinner, 0);
                        setMaxValue.invoke(minuteSpinner, 59);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //Устанавливаем начальное время
            timePicker.setHour(0);
            timePicker.setMinute(30);
            //К сожалению, стандартный TimePicker не имеет setSecond, но покажет 3 колонки

            //Слушатель изменений TimePicker
            timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> updateTimeFromTimePicker());
        }

        //ИНИЦИАЛИЗАЦИЯ МОТИВАЦИОННОГО ОКНА (ДОБАВЛЕНО)
        btnCloseMotivation.setOnClickListener(v -> hideMotivationWindow());

        //Получает день из предыдущей активности
        day = getIntent().getIntExtra("day", 0);
        //Получаем название упражнения и ресурс изображения из предыдущей активности
        exerciseName = getIntent().getStringExtra("exerciseName");
        exerciseImageResource = getIntent().getIntExtra("exerciseImageResource", 0);

        // ДОБАВЛЕНО: Устанавливаем конкретную анимацию упражнения
        setExerciseAnimation();

        //Восстанавливает состояние таймера при пересоздании активности
        if (savedInstanceState != null) {
            seconds = savedInstanceState.getInt("seconds"); //Восстанавливает время
            isTimerOn = savedInstanceState.getBoolean("isTimerOn"); //Восстанавливает состояние таймера
            //progressBar.setProgress(seconds); //Убираем ProgressBar
            setTimer(seconds); //Устанавливает текст таймера
            if (isTimerOn) { //Если таймер был включен...
                startExerciseTimer(seconds); //Запускает таймер
            }
        } else {
            //Устанавливаем начальное время из NumberPickers
            updateTimeFromNumberPickers();
        }

        //Устанавливает слушатель на кнопку для установки таймера
        //btnSetTimer.setOnClickListener(v -> { //Убираем старую логику установки времени
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
            if (!isTimerOn) {
                //updateTimeFromTimePicker();

                if (seconds > 0) {
                    btnStartTimer.setText(R.string.stop);
                    startExerciseTimer(seconds); //Запускаем с текущим значением seconds
                } else {
                    //Только если время не установлено, берем из NumberPickers
                    updateTimeFromNumberPickers();
                    if (seconds > 0) {
                        btnStartTimer.setText(R.string.stop);
                        startExerciseTimer(seconds);
                    } else {
                        Toast.makeText(this, "Сначала установите время", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                stopCountdownTimer();
            }
        });

        //Устанавливает слушатель на кнопку "Назад"
        btnBackArrow.setOnClickListener(v -> onBackPressed()); //Возвращает к предыдущей активности

        //При завершении упражнения
        //showStarAnimation(); //Убираем отсюда, будем показывать мотивационное окно
    }

    //МЕТОД ДЛЯ НАСТРОЙКИ NUMBERPICKERS (ДОБАВЛЕНО)
    private void setupNumberPickers() {
        //Настройка часов (0-23)
        numberPickerHours.setMinValue(0);
        numberPickerHours.setMaxValue(23);
        numberPickerHours.setValue(0);

        //Настройка минут (0-59)
        numberPickerMinutes.setMinValue(0);
        numberPickerMinutes.setMaxValue(59);
        numberPickerMinutes.setValue(30);

        //Настройка секунд (0-59)
        numberPickerSeconds.setMinValue(0);
        numberPickerSeconds.setMaxValue(59);
        numberPickerSeconds.setValue(0);

        //Форматирование отображения (добавляем ведущие нули)
        numberPickerHours.setFormatter(value -> String.format("%02d", value));
        numberPickerMinutes.setFormatter(value -> String.format("%02d", value));
        numberPickerSeconds.setFormatter(value -> String.format("%02d", value));

        //Слушатели изменений
        NumberPicker.OnValueChangeListener valueChangeListener = (picker, oldVal, newVal) -> updateTimeFromNumberPickers();

        numberPickerHours.setOnValueChangedListener(valueChangeListener);
        numberPickerMinutes.setOnValueChangedListener(valueChangeListener);
        numberPickerSeconds.setOnValueChangedListener(valueChangeListener);
    }

    //МЕТОД ДЛЯ ОБНОВЛЕНИЯ ВРЕМЕНИ ИЗ NUMBERPICKERS (ДОБАВЛЕНО)
    private void updateTimeFromNumberPickers() {
        int hours = numberPickerHours.getValue();
        int minutes = numberPickerMinutes.getValue();
        int secondsValue = numberPickerSeconds.getValue();

        initialSeconds = hours * 3600 + minutes * 60 + secondsValue;
        seconds = initialSeconds;
        setTimer(seconds);
    }

    //МЕТОДЫ ДЛЯ МОТИВАЦИОННОГО ОКНА (ДОБАВЛЕНО)
    /**
     * ПОКАЗ МОТИВАЦИОННОГО ОКНА
     * -------------------------
     * Отображает полноценное мотивационное окно с анимацией появления
     * и запускает анимацию звезды внутри окна
     */
    private void showMotivationWindow() {
        motivationLayout.setVisibility(View.VISIBLE);

        //АНИМАЦИЯ ПОЯВЛЕНИЯ ОКНА: масштабирование + прозрачность
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(motivationLayout, "scaleX", 0f, 1.1f, 1f); // Увеличение по X с эффектом "перескакивания"
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(motivationLayout, "scaleY", 0f, 1.1f, 1f); // Увеличение по Y с эффектом "перескакивания"
        ObjectAnimator alpha = ObjectAnimator.ofFloat(motivationLayout, "alpha", 0f, 1f);         // Плавное появление из невидимого состояния

        //ЗАПУСК ВСЕХ АНИМАЦИЙ ОДНОВРЕМЕННО
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(600); // Длительность анимации: 600ms
        animatorSet.start();

        animateStar(); // Запуск отдельной анимации звезды
    }

    /**
     * СКРЫТИЕ МОТИВАЦИОННОГО ОКНА
     * ---------------------------
     * Плавно скрывает мотивационное окно с анимацией исчезновения
     */
    private void hideMotivationWindow() {
        //АНИМАЦИЯ ИСЧЕЗНОВЕНИЯ ОКНА: уменьшение масштаба + прозрачность
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(motivationLayout, "scaleX", 1f, 0f); // Уменьшение по X до 0
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(motivationLayout, "scaleY", 1f, 0f); // Уменьшение по Y до 0
        ObjectAnimator alpha = ObjectAnimator.ofFloat(motivationLayout, "alpha", 1f, 0f);   // Плавное исчезновение

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(400); // Длительность анимации: 400ms (быстрее чем появление)
        animatorSet.start();

        //СЛУШАТЕЛЬ ЗАВЕРШЕНИЯ АНИМАЦИИ: скрываем окно после завершения анимации
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                motivationLayout.setVisibility(View.GONE); // Полностью скрываем окно из layout
            }
        });
    }

    /**
     * АНИМАЦИЯ ЗВЕЗДЫ В МОТИВАЦИОННОМ ОКНЕ
     * ------------------------------------
     * Создает сложную анимацию для звезды: вращение + масштабирование
     * Используется внутри мотивационного окна
     */
    private void animateStar() {
        //КОМБИНИРОВАННАЯ АНИМАЦИЯ ЗВЕЗДЫ:
        ObjectAnimator rotation = ObjectAnimator.ofFloat(starImageView, "rotation", 0f, 360f);      // Полное вращение на 360 градусов
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(starImageView, "scaleX", 0f, 1.2f, 1f);      // Масштабирование по X с "перескакиванием"
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(starImageView, "scaleY", 0f, 1.2f, 1f);      // Масштабирование по Y с "перескакиванием"

        AnimatorSet starAnimator = new AnimatorSet();
        starAnimator.playTogether(rotation, scaleX, scaleY); //Все анимации запускаются одновременно
        starAnimator.setDuration(800); //Длительность анимации: 800ms
        starAnimator.start();
    }

    /**
     * ПОКАЗ ПРОСТОЙ АНИМАЦИИ ЗВЕЗДЫ (РЕЗЕРВНЫЙ ВАРИАНТ)
     * ------------------------------------------------
     * Альтернативная упрощенная анимация звезды без мотивационного окна
     * Автоматически скрывается через 2 секунды
     */
    private void showStarAnimation() {
        //Показываем звезду
        starAnimationView.setVisibility(View.VISIBLE);

        //Анимация появления (аналогичная мотивационному окну, но для простой звезды)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(starAnimationView, "scaleX", 0f, 1.2f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(starAnimationView, "scaleY", 0f, 1.2f, 1f);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(starAnimationView, "alpha", 0f, 1f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, alpha);
        animatorSet.setDuration(800);
        animatorSet.start();

        //Автоматическое скрытие через 2 секунды с помощью Handler
        new Handler().postDelayed(() -> hideStarAnimation(), 2000);
    }

    private void hideStarAnimation() {
        //Создаем анимацию для уменьшения масштаба по оси X до 0 (скрытие)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(starAnimationView, "scaleX", 1f, 0f);
        //Создаем анимацию для уменьшения масштаба по оси Y до 0 (скрытие)
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(starAnimationView, "scaleY", 1f, 0f);
        //Создаем анимацию для постепенного исчезновения (альфа-канал от 1 до 0)
        ObjectAnimator alpha = ObjectAnimator.ofFloat(starAnimationView, "alpha", 1f, 0f);

        //Объединяем все созданные анимации в один набор
        AnimatorSet animatorSet = new AnimatorSet();
        //Запускаем все анимации одновременно
        animatorSet.playTogether(scaleX, scaleY, alpha);
        //Устанавливаем продолжительность анимации в 500 миллисекунд
        animatorSet.setDuration(500);
        //Запускаем набор анимаций
        animatorSet.start();

        //Добавляем слушатель для обработки окончания анимации
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //После завершения анимации, устанавливаем видимость view в GONE,
                //чтобы оно перестало занимать место на экране
                starAnimationView.setVisibility(View.GONE);
            }
        });
    }

    //Метод для установки конкретной анимации упражнения
    private void setExerciseAnimation() {
        if (exerciseImageResource != 0) {
            exerciseAnimationImageView.setImageResource(exerciseImageResource);

            //Сразу получаем AnimationDrawable для дальнейшего управления
            if (exerciseAnimationImageView.getDrawable() instanceof AnimationDrawable) {
                //Анимация будет запущена в startExerciseAnimation()
            }
        } else if (exerciseName != null) {
            //Пытаемся найти анимацию по названию упражнения
            int drawableId = getDrawableIdForExercise(exerciseName);
            if (drawableId != 0) {
                exerciseAnimationImageView.setImageResource(drawableId);
            } else {
                //Если анимация не найдена, показываем сообщение
                Toast.makeText(this, "Анимация для упражнения не найдена", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //Метод для получения ID drawable по названию упражнения (fallback)
    private int getDrawableIdForExercise(String exerciseName) {
        String resourceName = exerciseName.toLowerCase()
                .replace(" ", "_")
                .replace("упражнение", "")
                .trim();

        return getResources().getIdentifier(resourceName, "drawable", getPackageName());
    }

    //Метод для обновления времени из TimePicker
    private void updateTimeFromTimePicker() {
        if (timePicker != null) {
            int hours = timePicker.getHour();
            int minutes = timePicker.getMinute();
            //Поскольку стандартный TimePicker не дает секунды, устанавливаем 0
            int secondsValue = 0;

            initialSeconds = hours * 3600 + minutes * 60 + secondsValue;
            seconds = initialSeconds;
            setTimer(seconds);
        }
    }

    //Метод для установки текста таймера.
    private void setTimer(int seconds) {
        if (seconds < 0) {
            seconds = 0;
        }

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        int secs = seconds % 60;

        //Всегда показываем в формате ЧЧ:ММ:СС
        textViewTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, secs));
    }

    //Метод для запуска таймера
    private void startExerciseTimer(int duration) {
        isTimerOn = true;

        //Убедимся что анимация запущена при старте упражнения
        startExerciseAnimation();

        countDownTimer = new CountDownTimer(duration * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000); //Вычисляем оставшееся время
                setTimer(seconds);
            }

            public void onFinish() {
                seconds = 0;
                setTimer(0);
                //showStarAnimation(); //Показываем мотивационное окно вместо простой звезды
                showMotivationWindow(); //Показываем мотивационное окно
                Toast.makeText(ExerciseActivity.this, "Упражнение завершено, начинаем перерыв", Toast.LENGTH_SHORT).show();

                //Автоматически запускаем перерыв через 3 секунды (ДОБАВЛЕНО)
                new Handler().postDelayed(() -> {
                    if (motivationLayout.getVisibility() == View.VISIBLE) {
                        hideMotivationWindow();
                    }
                    startBreakTimer();
                }, 3000);
            }
        }.start();
    }

    private void startBreakTimer() {
        int breakDuration = 300; //Устанавливает продолжительность перерыва в секундах.
        seconds = breakDuration; //Инициализирует переменную seconds с длительностью перерыва.
        //progressBar.setMax(breakDuration); //Убираем ProgressBar
        //progressBar.setProgress(0); //Убираем ProgressBar
        setTimer(breakDuration); //Устанавливает текст таймера на значение продолжительности перерыва.

        //Создает новый CountDownTimer для обратного отсчета перерыва.
        countDownTimer = new CountDownTimer(breakDuration * 1000, 1000) {
            public void onTick(long millisUntilFinished) { //Метод, который вызывается каждую секунду.
                seconds = (int) (millisUntilFinished / 1000); //Вычисляет оставшееся время.
                //progressBar.setProgress(breakDuration - seconds); //Убираем ProgressBar
                setTimer(seconds); //Обновляет текст таймера.
            }

            public void onFinish() {//Метод, вызываемый по окончании перерыва.
                seconds = 0; //Устанавливаем 0.
                setTimer(0);
                //Показываем мотивационное окно и для завершения перерыва (ДОБАВЛЕНО)
                motivationSubText.setText("Перерыв завершен!");
                showMotivationWindow();

                new Handler().postDelayed(() -> {
                    if (motivationLayout.getVisibility() == View.VISIBLE) {
                        hideMotivationWindow();
                    }
                    resetTimer();
                }, 3000);
            }
        }.start(); //Запускает таймер перерыва.
    }

    //Метод для запуска анимации упражнения
    private void startExerciseAnimation() {
        if (exerciseAnimationImageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) exerciseAnimationImageView.getDrawable();
            if (!animation.isRunning()) {
                animation.start();
            }
        }
    }

    //Метод для остановки анимации упражнения
    private void stopExerciseAnimation() {
        if (exerciseAnimationImageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) exerciseAnimationImageView.getDrawable();
            if (animation.isRunning()) {
                animation.stop();
            }
        }
    }

    private void stopCountdownTimer() { //Метод для остановки текущего таймера.
        if (countDownTimer != null) { //Проверяет, что таймер существует.
            countDownTimer.cancel(); //Отменяет активный таймер.
            isTimerOn = false; //Устанавливает флаг, что таймер не работает.
            btnStartTimer.setText(R.string.start); //Меняет текст кнопки на "Начать".
        }
    }

    private void resetTimer() {
        stopCountdownTimer();
        seconds = initialSeconds; //Сбрасываем к исходному времени, а не к 0
        setTimer(seconds);

        //Сброс TimePicker к начальным значениям
        if (timePicker != null) {
            timePicker.setHour(0);
            timePicker.setMinute(30);
        }

        //Сброс NumberPickers к начальным значениям (ДОБАВЛЕНО)
        if (numberPickerHours != null) {
            numberPickerHours.setValue(0);
        }
        if (numberPickerMinutes != null) {
            numberPickerMinutes.setValue(30);
        }
        if (numberPickerSeconds != null) {
            numberPickerSeconds.setValue(0);
        }

        //Перезапускаем анимацию при сбросе таймера
        startExerciseAnimation();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) { //Переопределяет метод для сохранения состояния активности.
        outState.putInt("seconds", seconds); //Сохраняет текущее значение секунд.
        outState.putBoolean("isTimerOn", isTimerOn); //Сохраняет состояние активного таймера.
        super.onSaveInstanceState(outState); //Вызывает родительский метод для сохранения состояния.
    }

    //Перезапуск анимации при возвращении на экран
    @Override
    protected void onResume() {
        super.onResume();
        startExerciseAnimation();
    }

    //Остановка анимации при уходе с экрана (опционально)
    @Override
    protected void onPause() {
        super.onPause();
        stopExerciseAnimation();
    }
}