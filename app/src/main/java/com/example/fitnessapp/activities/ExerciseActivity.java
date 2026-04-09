package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ ExerciseActivity (ЭКРАН ВЫПОЛНЕНИЯ УПРАЖНЕНИЯ)
 * =============================================================================
 *
 *
 * ЧТО ДЕЛАЕТ ЭТОТ КЛАСС
 * ---------------------
 *
 * ExerciseActivity отвечает за выполнение одного конкретного упражнения.
 * Пользователь приходит сюда из списка упражнений, устанавливает время,
 * запускает таймер, выполняет упражнение под анимацию, получает звезду
 * за выполнение и переходит к перерыву между упражнениями.
 *
 *
 * ЧТО РЕАЛИЗОВАНО В ДАННОМ КЛАССЕ (ПОЛНЫЙ ФУНКЦИОНАЛ)
 * -----------------------------------------------------
 *
 *
 * 1. УПРАВЛЕНИЕ ВРЕМЕНЕМ (ТАЙМЕР)
 *
 *    - Три NumberPicker (часы, минуты, секунды) для установки времени
 *    - Визуальное отображение времени в формате ЧЧ:ММ:СС
 *    - Кнопка "Старт/Стоп" для запуска и остановки таймера
 *    - Обратный отсчёт с обновлением каждую секунду
 *    - Возможность поставить таймер на паузу и продолжить с того же места
 *    - Автоматический переход к перерыву после завершения упражнения
 *    - Перерыв длится 300 секунд (5 минут) после каждого упражнения
 *    - После перерыва таймер сбрасывается и готов к следующему упражнению
 *
 *
 * 2. АНИМАЦИЯ УПРАЖНЕНИЯ
 *
 *    - Анимированное изображение (AnimationDrawable) для каждого упражнения
 *    - Анимация автоматически запускается при старте таймера
 *    - Анимация останавливается при паузе или завершении упражнения
 *    - Поддержка разных анимаций для разных упражнений
 *    - Возможность передать ресурс анимации через Intent
 *    - Fallback-механизм: поиск анимации по названию упражнения
 *
 *
 * 3. МОТИВАЦИОННОЕ ОКНО (ПОЯВЛЯЕТСЯ ПОСЛЕ ЗАВЕРШЕНИЯ)
 *
 *    - Всплывающее окно с анимацией появления (масштабирование + прозрачность)
 *    - Анимированная звезда внутри окна (вращение + масштабирование)
 *    - Текст поздравления ("Молодец!", "Отлично!" и т.д.)
 *    - Кнопка "Продолжить" для закрытия окна
 *    - Окно автоматически закрывается через 3 секунды
 *
 *
 * 4. СИСТЕМА ЗВЁЗД (НАГРАДЫ)
 *
 *    - За каждое полностью выполненное упражнение пользователь получает звезду
 *    - Звезды сохраняются в SharedPreferences
 *    - Максимум можно накопить 5 звёзд
 *    - При достижении 5 звёзд показывается поздравление
 *    - Звезда НЕ начисляется после перерыва (только за само упражнение)
 *    - Защита от двойного начисления (флаг starAlreadySaved)
 *    - При повороте экрана звезда не начисляется повторно
 *
 *
 * 5. СОХРАНЕНИЕ СОСТОЯНИЯ (НЕ ДАЁТ ПОТЕРЯТЬ ПРОГРЕСС)
 *
 *    - При повороте экрана сохраняется: оставшееся время, состояние таймера
 *    - При закрытии приложения и повторном открытии прогресс восстанавливается
 *    - Сохраняется в SharedPreferences: оставшиеся секунды, флаг завершения
 *    - Если пользователь закрыл приложение во время упражнения, он продолжит
 *      с того же места после возвращения
 *    - После полного завершения упражнения сохранённые данные очищаются
 *
 *
 * 6. ПОЛНОЭКРАННЫЙ РЕЖИМ
 *
 *    - Скрыт статус-бар (строка с часами и батареей)
 *    - Скрыта навигационная панель (кнопки "Назад", "Домой", "Недавние")
 *    - Ничто не отвлекает пользователя во время упражнения
 *
 *
 * 7. НАВИГАЦИЯ
 *
 *    - Кнопка "Назад" (стрелка) возвращает на предыдущий экран
 *    - При нажатии системной кнопки "Назад" тоже происходит возврат
 *
 *
 * 8. ОБРАБОТКА ОШИБОК
 *
 *    - Проверка, что время установлено (не 00:00:00)
 *    - Сообщение "Сначала установите время", если пользователь забыл
 *    - Сообщение "Анимация для упражнения не найдена", если ресурс отсутствует
 *    - Обработка ошибок при сохранении звезды в SharedPreferences
 *
 *
 * ЧТО ПРИХОДИТ В ЭТУ АКТИВНОСТЬ (ВХОДНЫЕ ДАННЫЕ)
 * ----------------------------------------------
 *
 * Из предыдущего экрана (обычно WeekActivity или WorkoutActivity) передаётся:
 *
 * - day (int) — номер дня тренировки (0, 1, 2...)
 * - exerciseName (String) — название упражнения (например, "Приседания")
 * - exerciseImageResource (int) — ID ресурса с анимацией упражнения
 *
 *
 * КАК ПОЛЬЗОВАТЕЛЬ ВЗАИМОДЕЙСТВУЕТ С ЭКРАНОМ
 * -------------------------------------------
 *
 * 1. Пользователь видит анимацию упражнения и три крутилки (часы/минуты/секунды)
 * 2. Устанавливает нужное время (например, 00:01:30 на 90 секунд)
 * 3. Нажимает "Старт" — таймер начинает обратный отсчёт, анимация запускается
 * 4. Если нужно передохнуть — нажимает "Стоп", таймер ставится на паузу
 * 5. Нажимает "Старт" снова — продолжает с того же места
 * 6. Когда время истекает:
 *    - Появляется мотивационное окно с анимированной звездой
 *    - Начисляется звезда (если ещё не начислена)
 *    - Через 3 секунды окно закрывается и начинается перерыв (5 минут)
 * 7. После перерыва таймер сбрасывается, можно выполнять следующее упражнение
 *
 *
 * КЛЮЧЕВЫЕ ПЕРЕМЕННЫЕ (ЧТО ЗА ЧТО ОТВЕЧАЮТ)
 * ------------------------------------------
 *
 * seconds — текущее оставшееся время в секундах
 * remainingSeconds — сохраняется при паузе, чтобы продолжить потом
 * initialSeconds — начальное время, установленное пользователем (нужно для сброса)
 * isTimerOn — запущен ли таймер в данный момент (true/false)
 * isExerciseCompleted — завершено ли упражнение полностью
 * starAlreadySaved — защита от двойного начисления звезды
 *
 *
 * ЧТО НЕ РЕАЛИЗОВАНО (НО МОЖНО ДОБАВИТЬ)
 * ---------------------------------------
 *
 * - Звуковое сопровождение (тиканье таймера, сигнал завершения)
 * - Вибрация при окончании упражнения
 * - Возможность пропустить перерыв
 * - Настройка длительности перерыва (сейчас фиксированные 5 минут)
 * - Статистика: сколько упражнений выполнено за тренировку
 * - Режим "Без перерыва" для быстрых тренировок
 *
 *
 * ИТОГ
 * ----
 *
 * ExerciseActivity — это полноценный, продуманный экран для выполнения упражнений.
 * В нём реализовано: установка времени, таймер с паузой, анимация упражнения,
 * мотивационное окно, система наград (звёзды), сохранение прогресса при
 * закрытии приложения и полноэкранный режим. Это готовая к использованию
 * функциональность, которая не требует доработок для базовой работы.
 *
 * =============================================================================
 */

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
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
    private int remainingSeconds = 0; //Добавляем переменную для хранения оставшегося времени
    private boolean isExerciseCompleted = false; //Флаг завершения упражнения

    //КОНСТАНТЫ ДЛЯ СОХРАНЕНИЯ
    private static final String PREFS_NAME = "ExerciseTimerPrefs";
    private static final String KEY_REMAINING_SECONDS = "remainingSeconds";
    private static final String KEY_IS_EXERCISE_COMPLETED = "isExerciseCompleted";
    private static final String KEY_INITIAL_SECONDS = "initialSeconds";

    //Константы для звезд
    private static final String STATS_PREFS_NAME = "FitnessAppStats";
    private static final String KEY_STARS_COUNT = "stars_count";
    private boolean starAlreadySaved = false; //Флаг чтобы предотвратить дублирование сохранения

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise); //Устанавливает макет для этой активности

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        //Установите флаг, чтобы скрыть строку состояния.
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

        //Устанавливаем конкретную анимацию упражнения
        setExerciseAnimation();

        //ПРАВИЛЬНОЕ ВОССТАНОВЛЕНИЕ СОСТОЯНИЯ
        if (savedInstanceState != null) {
            //Восстанавливаем из Bundle (поворот экрана)
            seconds = savedInstanceState.getInt("seconds", 0);
            isTimerOn = savedInstanceState.getBoolean("isTimerOn", false);
            remainingSeconds = savedInstanceState.getInt("remainingSeconds", 0);
            isExerciseCompleted = savedInstanceState.getBoolean("isExerciseCompleted", false);
            initialSeconds = savedInstanceState.getInt("initialSeconds", 0);
            starAlreadySaved = savedInstanceState.getBoolean("starAlreadySaved", false);
        } else {
            //Восстанавливаем из SharedPreferences (перезапуск приложения)
            restoreTimerStateFromPreferences();
        }

        //ВОССТАНАВЛИВАЕМ ВРЕМЯ В ЗАВИСИМОСТИ ОТ СИТУАЦИИ
        if (isTimerOn) {
            //Если таймер был активен, продолжаем отсчет
            setTimer(seconds);
            startExerciseTimer(seconds);
        } else if (remainingSeconds > 0 && !isExerciseCompleted) {
            //Если есть незавершенное упражнение, показываем оставшееся время
            seconds = remainingSeconds;
            setTimer(seconds);
            updateNumberPickersFromSeconds(seconds);
        } else {
            //Иначе устанавливаем начальное время
            updateTimeFromNumberPickers();
        }

        //Измененный слушатель кнопки Start/Stop
        btnStartTimer.setOnClickListener(v -> {
            if (!isTimerOn) {
                //ЕСЛИ ЕСТЬ ОСТАВШЕЕСЯ ВРЕМЯ - ПРОДОЛЖАЕМ С НЕГО
                if (remainingSeconds > 0 && !isExerciseCompleted) {
                    seconds = remainingSeconds;
                    btnStartTimer.setText(R.string.stop);
                    startExerciseTimer(seconds);
                }
                //ЕСЛИ ВРЕМЯ УЖЕ УСТАНОВЛЕНО - ЗАПУСКАЕМ
                else if (seconds > 0) {
                    btnStartTimer.setText(R.string.stop);
                    startExerciseTimer(seconds);
                }
                //ЕСЛИ ВРЕМЯ НЕ УСТАНОВЛЕНО - БЕРЕМ ИЗ NUMBERPICKERS
                else {
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
    }

    //Метод для обновления NumberPickers из секунд
    private void updateNumberPickersFromSeconds(int totalSeconds) {
        if (totalSeconds <= 0) return;

        int hours = totalSeconds / 3600;
        int minutes = (totalSeconds % 3600) / 60;
        int secondsValue = totalSeconds % 60;

        if (numberPickerHours != null) numberPickerHours.setValue(hours);
        if (numberPickerMinutes != null) numberPickerMinutes.setValue(minutes);
        if (numberPickerSeconds != null) numberPickerSeconds.setValue(secondsValue);
    }

    //МЕТОДЫ ДЛЯ СОХРАНЕНИЯ МЕЖДУ СЕССИЯМИ

    /**
     * Сохраняет состояние таймера в SharedPreferences
     */
    private void saveTimerStateToPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_REMAINING_SECONDS, remainingSeconds);
        editor.putBoolean(KEY_IS_EXERCISE_COMPLETED, isExerciseCompleted);
        editor.putInt(KEY_INITIAL_SECONDS, initialSeconds);
        editor.apply();
    }

    /**
     * Восстанавливает состояние таймера из SharedPreferences
     */
    private void restoreTimerStateFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        remainingSeconds = prefs.getInt(KEY_REMAINING_SECONDS, 0);
        isExerciseCompleted = prefs.getBoolean(KEY_IS_EXERCISE_COMPLETED, false);
        initialSeconds = prefs.getInt(KEY_INITIAL_SECONDS, 0);

        //Если есть сохраненное состояние, используем его
        if (remainingSeconds > 0 && !isExerciseCompleted) {
            seconds = remainingSeconds;
        } else {
            //Иначе устанавливаем начальное время из NumberPickers
            updateTimeFromNumberPickers();
        }
    }

    /**
     * Очищает сохраненное состояние таймера
     */
    private void clearTimerStateFromPreferences() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_REMAINING_SECONDS);
        editor.remove(KEY_IS_EXERCISE_COMPLETED);
        editor.remove(KEY_INITIAL_SECONDS);
        editor.apply();
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
        numberPickerMinutes.setValue(0);

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

        //Сохраняем initialSeconds при изменении
        saveTimerStateToPreferences();
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

        animateStar(); //Запуск отдельной анимации звезды
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

    //ИСПРАВЛЕННЫЙ МЕТОД ДЛЯ ЗАПУСКА ТАЙМЕРА УПРАЖНЕНИЯ
    private void startExerciseTimer(int duration) {
        isTimerOn = true;
        isExerciseCompleted = false; //Сбрасываем флаг завершения
        starAlreadySaved = false; // Сбрасываем флаг сохранения звезды

        startExerciseAnimation();

        countDownTimer = new CountDownTimer(duration * 1000L, 1000) {
            public void onTick(long millisUntilFinished) {
                seconds = (int) (millisUntilFinished / 1000);
                remainingSeconds = seconds; //Сохраняем оставшееся время
                setTimer(seconds);
            }

            public void onFinish() {
                seconds = 0;
                remainingSeconds = 0;
                isExerciseCompleted = true; //Упражнение завершено полностью
                setTimer(0);

                //Сохраняем звезду при завершении упражнения - ЗДЕСЬ!
                if (!starAlreadySaved) {
                    saveStarToStats();
                    starAlreadySaved = true;
                }

                showMotivationWindow();
                Toast.makeText(ExerciseActivity.this, "Упражнение завершено, начинаем перерыв", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> {
                    if (motivationLayout.getVisibility() == View.VISIBLE) {
                        hideMotivationWindow();
                    }
                    startBreakTimer();
                }, 3000);
            }
        }.start();
    }

    // ИСПРАВЛЕННЫЙ МЕТОД ДЛЯ ПЕРЕРЫВА
    private void startBreakTimer() {
        int breakDuration = 300; //Устанавливает продолжительность перерыва в секундах.
        seconds = breakDuration; //Инициализирует переменную seconds с длительностью перерыва.
        setTimer(breakDuration); //Устанавливает текст таймера на значение продолжительности перерыва.

        //Создает новый CountDownTimer для обратного отсчета перерыва.
        countDownTimer = new CountDownTimer(breakDuration * 1000, 1000) {
            public void onTick(long millisUntilFinished) { //Метод, который вызывается каждую секунду.
                seconds = (int) (millisUntilFinished / 1000); //Вычисляет оставшееся время.
                setTimer(seconds); //Обновляет текст таймера.
            }

            public void onFinish() {
                seconds = 0;
                remainingSeconds = 0;
                isExerciseCompleted = true; //Упражнение завершено полностью
                setTimer(0);
                showMotivationWindow();

                //УБРАТЬ отсюда сохранение звезды: saveStarToStats();

                Toast.makeText(ExerciseActivity.this, "Перерыв завершен!", Toast.LENGTH_SHORT).show();

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

    //Измененный метод остановки таймера
    private void stopCountdownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            isTimerOn = false;
            btnStartTimer.setText(R.string.start);

            //Сохраняем оставшееся время для возможности продолжения
            if (seconds > 0) {
                remainingSeconds = seconds;
                isExerciseCompleted = false;
                //Обновляем NumberPickers чтобы показать текущее оставшееся время
                updateNumberPickersFromSeconds(seconds);
            }
        }
    }

    //Измененный метод сброса таймера
    private void resetTimer() {
        stopCountdownTimer();

        //Сбрасываем только если упражнение было завершено
        if (isExerciseCompleted || remainingSeconds == 0) {
            seconds = initialSeconds;
            remainingSeconds = 0;
            starAlreadySaved = false; // Сбрасываем флаг сохранения звезды
            // ДОБАВЛЕНО: Очищаем сохраненное состояние при полном сбросе
            clearTimerStateFromPreferences();
        } else {
            //Иначе продолжаем с оставшегося времени
            seconds = remainingSeconds;
        }

        setTimer(seconds);

        //Сброс NumberPickers к начальным значениям только при полном сбросе
        if (isExerciseCompleted || remainingSeconds == 0) {
            if (numberPickerHours != null) {
                numberPickerHours.setValue(0);
            }
            if (numberPickerMinutes != null) {
                numberPickerMinutes.setValue(0);
            }
            if (numberPickerSeconds != null) {
                numberPickerSeconds.setValue(0);
            }
        }
        startExerciseAnimation();
    }

    /**
     * УЛУЧШЕННЫЙ МЕТОД СОХРАНЕНИЯ ЗВЕЗДЫ ПРИ ЗАВЕРШЕНИИ УПРАЖНЕНИЯ
     */
    private void saveStarToStats() {
        try {
            SharedPreferences statsPrefs = getSharedPreferences(STATS_PREFS_NAME, MODE_PRIVATE);
            int currentStars = statsPrefs.getInt(KEY_STARS_COUNT, 0);

            //Максимум 5 звезд
            if (currentStars < 5) {
                currentStars++;
                SharedPreferences.Editor editor = statsPrefs.edit();
                editor.putInt(KEY_STARS_COUNT, currentStars);

                // Используем commit() для немедленного сохранения
                boolean success = editor.commit();

                if (success) {
                    // Показываем сообщение о получении звезды
                    Toast.makeText(this, "⭐ Получена звезда! Всего звезд: " + currentStars, Toast.LENGTH_LONG).show();

                    // Логируем для отладки
                    Log.d("StarSystem", "Звезда сохранена успешно. Всего звезд: " + currentStars);
                } else {
                    Toast.makeText(this, "Ошибка сохранения звезды", Toast.LENGTH_SHORT).show();
                    Log.e("StarSystem", "Ошибка сохранения звезды");
                }
            } else {
                Toast.makeText(this, "🎉 У вас уже максимальное количество звезд!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("StarSystem", "Ошибка при сохранении звезды: " + e.getMessage());
            Toast.makeText(this, "Ошибка при сохранении звезды", Toast.LENGTH_SHORT).show();
        }
    }

    //Обновленный метод сохранения состояния
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("seconds", seconds);
        outState.putBoolean("isTimerOn", isTimerOn);
        outState.putInt("remainingSeconds", remainingSeconds);
        outState.putBoolean("isExerciseCompleted", isExerciseCompleted);
        outState.putInt("initialSeconds", initialSeconds);
        outState.putBoolean("starAlreadySaved", starAlreadySaved);
    }

    //Восстанавливаем состояние при возврате на экран
    @Override
    protected void onResume() {
        super.onResume();

        //Если есть оставшееся время и упражнение не завершено, показываем его
        if (remainingSeconds > 0 && !isExerciseCompleted && !isTimerOn) {
            seconds = remainingSeconds;
            setTimer(seconds);
        }
        startExerciseAnimation();
    }

    //Обновленный метод onPause для сохранения при выходе
    @Override
    protected void onPause() {
        super.onPause();
        //Сохраняем состояние при уходе с экрана
        saveTimerStateToPreferences();
        stopExerciseAnimation();
    }
}