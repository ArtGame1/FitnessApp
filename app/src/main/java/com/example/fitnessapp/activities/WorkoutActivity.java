package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ WorkoutActivity (ЭКРАН СПИСКА ТРЕНИРОВОК)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * WorkoutActivity является экраном со списком всех доступных тренировок
 * в приложении Fitness App. Сюда пользователь попадает при нажатии на кнопку
 * "Тренировки" на главном экране, из нижней панели навигации, из бокового меню
 * или через поиск.
 *
 * Этот экран позволяет пользователю:
 * - Просматривать список доступных упражнений с анимациями
 * - Выбирать упражнение для выполнения
 * - Добавлять свои собственные тренировки
 * - Редактировать, делиться, обновлять и удалять тренировки
 * - Искать тренировки по названию
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель:
 * - Кнопка "Назад" (btnBackArrowWorkout) — возврат на главный экран
 * - Кнопка меню (btnMenu) — открывает PopupMenu с дополнительными действиями
 *
 * Основная область (список упражнений):
 * - 10 предустановленных упражнений с анимациями:
 *   1. Скручивание согнутой ноги (fitnessIcon1)
 *   2. Велосипедные скручивания (fitnessIcon2)
 *   3. Ягодичный мостик (fitnessIcon3)
 *   4. Планка (fitnessIcon4)
 *   5. Скручивания с хлопком (fitnessIcon5)
 *   6. Скручивания со скрещенными руками (fitnessIcon6)
 *   7. Упражнение мертвый жук (fitnessIcon7)
 *   8. Бег в упоре лежа (fitnessIcon8)
 *   9. Упражнение для пресса (fitnessIcon9)
 *   10. Скручивание согнутых ног (fitnessIcon10)
 *
 * - Контейнер для динамически добавляемых тренировок (workoutsContainer)
 *
 *
 * ЧАСТЬ 3. ПОИСК ТРЕНИРОВОК (ДЕТАЛЬНО)
 * --------------------------------------
 *
 * 3.1. Получение поискового запроса:
 *
 * При запуске WorkoutActivity проверяется, был ли передан search_query
 * через Intent (например, из MainActivity при поиске).
 *
 * 3.2. Фильтрация результатов:
 *
 * Если поисковый запрос не пустой, вызывается метод filterWorkouts(searchQuery)
 * для фильтрации списка тренировок (пока не реализован).
 *
 * Показывается Toast с сообщением "Результаты поиска: " + searchQuery.
 *
 *
 * ЧАСТЬ 4. АНИМАЦИИ УПРАЖНЕНИЙ (ДЕТАЛЬНО)
 * ----------------------------------------
 *
 * 4.1. Запуск всех анимаций (startAllAnimations):
 *
 * Для каждого из 10 ImageView устанавливается ресурс анимации
 * (AnimationDrawable) и запускается анимация.
 *
 * Сопоставление иконок и анимаций:
 * - fitnessIcon1 → bent_leg_twist_animation
 * - fitnessIcon2 → bicycle_crunches_animation
 * - fitnessIcon3 → butt_bridge_animation
 * - fitnessIcon4 → plunk_animation
 * - fitnessIcon5 → clapping_crunches_animation
 * - fitnessIcon6 → cross_arm_crunches_animation
 * - fitnessIcon7 → dead_bug_animation
 * - fitnessIcon8 → mountain_climbers_animation
 * - fitnessIcon9 → dead_bug_animation
 * - fitnessIcon10 → bent_leg_twist_animation
 *
 * 4.2. Запуск анимации для одного ImageView (startAnimationForView):
 *
 * Проверяет, является ли drawable анимацией (instanceof AnimationDrawable).
 * Если анимация ещё не запущена, запускает её.
 *
 * 4.3. Остановка анимаций (onPause):
 *
 * При уходе с экрана анимации останавливаются для экономии ресурсов.
 *
 *
 * ЧАСТЬ 5. ПЕРЕХОД К ВЫПОЛНЕНИЮ УПРАЖНЕНИЙ (ДЕТАЛЬНО)
 * ----------------------------------------------------
 *
 * 5.1. Обработчики нажатий:
 *
 * Для каждого LinearLayout с упражнением установлен слушатель, который
 * вызывает соответствующий метод открытия активности:
 *
 * - arrowRunning → openRunningActivity()
 * - arrowPushUps → openPushUpsActivity()
 * - arrowSquats → openSquatsActivity()
 * - arrowJumpingJacks → openJumpingJacksActivity()
 * - arrowPlank → openPlankActivity()
 * - arrowLunges → openLungesActivity()
 * - arrowCrunches → openCrunchesActivity()
 * - arrowBicepCurls → openBicepCurclsActivity()
 * - arrowTricepDips → openTricepDipsActivity()
 * - arrowLegRaises → openLegRaisesActivity()
 *
 * 5.2. Передача данных в ExerciseActivity:
 *
 * Каждый метод создаёт Intent и передаёт:
 * - day: 1 (номер дня тренировки)
 * - exerciseName: название упражнения (на русском)
 * - exerciseImageResource: ID ресурса анимации
 *
 * Пример для открытия упражнения "Скручивание согнутой ноги":
 * intent.putExtra("day", 1);
 * intent.putExtra("exerciseName", "Скручивание согнутой ноги");
 * intent.putExtra("exerciseImageResource", R.drawable.bent_leg_twist_animation);
 *
 *
 * ЧАСТЬ 6. POPUPMENU (ВЫПАДАЮЩЕЕ МЕНЮ) (ДЕТАЛЬНО)
 * ------------------------------------------------
 *
 * 6.1. Открытие меню:
 *
 * При нажатии на btnMenu вызывается showPopupMenu(), который создаёт
 * PopupMenu и загружает меню из ресурса R.menu.workout_menu.
 *
 * 6.2. Пункты меню:
 *
 * - Редактирование (menu_edit) — пока Toast "Редактирование"
 * - Добавить (menu_add) — вызывает addWorkout() для добавления тренировки
 * - Поделиться (menu_share) — вызывает shareWorkout() для отправки
 * - Обновить (menu_refresh) — вызывает refreshWorkout()
 * - Удалить (menu_delete) — вызывает deleteWorkout()
 *
 *
 * ЧАСТЬ 7. ДОБАВЛЕНИЕ НОВОЙ ТРЕНИРОВКИ (ДЕТАЛЬНО)
 * ------------------------------------------------
 *
 * 7.1. Диалог добавления (addWorkout):
 *
 * Создаётся AlertDialog с двумя полями ввода:
 * - Название тренировки (titleInput)
 * - Описание тренировки (descriptionInput)
 *
 * При нажатии "Добавить" проверяется, что оба поля заполнены,
 * и вызывается addWorkoutToLayout().
 *
 * 7.2. Создание карточки тренировки (addWorkoutToLayout):
 *
 * Динамически создаётся CardView со следующим содержимым:
 * - ImageView с иконкой упражнения (анимированной)
 * - TextView с названием тренировки (жирный шрифт, 18sp)
 * - TextView с описанием тренировки (серый цвет, 14sp)
 * - ImageView со стрелкой (ic_media_next)
 *
 * 7.3. Определение анимации:
 *
 * Для новой тренировки анимация определяется по названию через
 * getExerciseImageResource(). Если упражнение не распознано,
 * выбирается случайная анимация из доступных.
 *
 * 7.4. Обработчик клика:
 *
 * При нажатии на карточку открывается ExerciseActivity с передачей
 * названия и ресурса анимации.
 *
 *
 * ЧАСТЬ 8. ОСТАЛЬНЫЕ ФУНКЦИИ (ДЕТАЛЬНО)
 * --------------------------------------
 *
 * 8.1. Поделиться тренировкой (shareWorkout):
 *
 * Создаёт Intent.ACTION_SEND для отправки текста.
 * Тип: "text/plain".
 * Текст: "Посмотрите мою программу тренировок!"
 *
 * 8.2. Обновить (refreshWorkout):
 *
 * Показывает Toast "Данные обновлены".
 *
 * 8.3. Удалить тренировку (deleteWorkout):
 *
 * Показывает диалог подтверждения:
 * - Заголовок: "Удаление тренировки"
 * - Сообщение: "Вы уверены, что хотите удалить эту тренировку?"
 * - Кнопка "Удалить" — закрывает активность
 * - Кнопка "Отмена" — закрывает диалог
 *
 *
 * ЧАСТЬ 9. ОСНОВНЫЕ МЕТОДЫ (КРАТКОЕ ОПИСАНИЕ)
 * --------------------------------------------
 *
 * onCreate() — инициализация активности: поисковый запрос, инициализация
 *              View, запуск анимаций, настройка слушателей,
 *              полноэкранный режим, установка обработчиков для упражнений
 *
 * filterWorkouts() — фильтрация списка тренировок по запросу (пока не реализован)
 *
 * startAllAnimations() — запуск анимаций для всех 10 упражнений
 *
 * startAnimationForView() — запуск анимации для конкретного ImageView
 *
 * onPause() — остановка анимаций при уходе с экрана
 *
 * initViews() — инициализация кнопок и текстовых полей
 *
 * setupClickListeners() — настройка слушателей для кнопок
 *
 * showPopupMenu() — отображение выпадающего меню
 *
 * shareWorkout() — отправка тренировки через Intent.ACTION_SEND
 *
 * addWorkout() — диалог добавления новой тренировки
 *
 * addWorkoutToLayout() — динамическое создание карточки тренировки
 *
 * getExerciseImageResource() — получение ресурса анимации по названию
 *
 * dpToPx() — конвертация dp в пиксели
 *
 * refreshWorkout() — обновление данных (заглушка)
 *
 * deleteWorkout() — удаление тренировки с подтверждением
 *
 * openRunningActivity() и аналоги — переход к выполнению упражнения
 *
 *
 * ЧАСТЬ 10. ОСОБЕННОСТИ РЕАЛИЗАЦИИ
 * ---------------------------------
 *
 * 10.1. Два способа открытия упражнений:
 *
 * В коде есть закомментированный блок с использованием ImageButton,
 * который открывает ExerciseActivity без передачи параметров.
 *
 * Актуальная реализация использует LinearLayout и передаёт конкретное
 * название упражнения и ресурс анимации.
 *
 * 10.2. Поиск контейнера для добавления тренировок:
 *
 * При добавлении новой тренировки метод ищет workoutsContainer.
 * Если не найден, ищет ScrollView и берёт его первый дочерний элемент
 * как LinearLayout.
 *
 * 10.3. Генерация случайной анимации:
 *
 * Если название упражнения не распознано, выбирается случайная анимация
 * из массива availableAnimations с помощью Random.
 *
 *
 * ЧАСТЬ 11. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Поиск не реализован:
 *    - filterWorkouts() пустой
 *    - Нужно реализовать фильтрацию списка упражнений
 *
 * 2. Редактирование не реализовано:
 *    - Пункт меню "Редактирование" показывает только Toast
 *    - Нужно добавить диалог редактирования тренировки
 *
 * 3. Обновление не реализовано:
 *    - refreshWorkout() только показывает Toast
 *    - Нужно добавить перезагрузку списка из источника данных
 *
 * 4. Нет сохранения добавленных тренировок:
 *    - Динамически добавленные тренировки исчезают при перезапуске
 *    - Нужно сохранять их в SharedPreferences или базу данных
 *
 * 5. Нет возможности удалить отдельную тренировку:
 *    - deleteWorkout() удаляет всю активность
 *    - Нужно добавить удаление конкретной карточки
 *
 * 6. Все упражнения открываются с day = 1:
 *    - Нет привязки к дню тренировки
 *    - Нужно передавать реальный номер дня
 *
 * 7. Анимации не останавливаются при паузе:
 *    - onPause останавливает только одну анимацию
 *    - Нужно останавливать все 10 анимаций
 *
 *
 * ЧАСТЬ 12. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - CardView (для оформления карточек тренировок)
 *
 * Ресурсы:
 * - Анимации: 8 различных AnimationDrawable для упражнений
 * - Меню: R.menu.workout_menu
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 13. ИТОГИ
 * ----------------
 *
 * WorkoutActivity — это экран со списком тренировок, который позволяет
 * пользователю просматривать доступные упражнения, выбирать их для выполнения,
 * добавлять свои собственные тренировки, а также делиться, обновлять и удалять
 * тренировки через выпадающее меню.
 *
 * Реализованы следующие функции:
 * - Отображение 10 упражнений с анимированными иконками
 * - Переход к выполнению каждого упражнения с передачей параметров
 * - Динамическое добавление новых тренировок с карточками
 * - PopupMenu с действиями: редактирование, добавление, поделиться, обновить, удалить
 * - Полноэкранный режим
 * - Обработка поискового запроса из MainActivity
 *
 * Экран имеет удобный интерфейс для выбора и управления тренировками.
 *
 * =============================================================================
 */


import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.fitnessapp.R;

import java.util.Random;

public class WorkoutActivity extends AppCompatActivity {

    private ImageView btnBackArrowWorkout, btnMenu; //Кнопка "Назад" и добавляем btnMenu
    private TextView textViewDayOfExercise;
    private ImageView fitnessIcon1, fitnessIcon2, fitnessIcon3, fitnessIcon4,
            fitnessIcon5, fitnessIcon6, fitnessIcon7, fitnessIcon8,
            fitnessIcon9, fitnessIcon10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout); //Устанавливает макет для этого экрана

        // Получаем поисковый запрос
        String searchQuery = getIntent().getStringExtra("search_query");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            // Выполняем поиск и фильтрацию тренировок
            filterWorkouts(searchQuery);
            // Показываем что идет поиск
            Toast.makeText(this, "Результаты поиска: " + searchQuery, Toast.LENGTH_SHORT).show();
        }


        fitnessIcon1 = findViewById(R.id.fitnessIcon1);
        fitnessIcon2 = findViewById(R.id.fitnessIcon2);
        fitnessIcon3 = findViewById(R.id.fitnessIcon3);
        fitnessIcon4 = findViewById(R.id.fitnessIcon4);
        fitnessIcon5 = findViewById(R.id.fitnessIcon5);
        fitnessIcon6 = findViewById(R.id.fitnessIcon6);
        fitnessIcon7 = findViewById(R.id.fitnessIcon7);
        fitnessIcon8 = findViewById(R.id.fitnessIcon8);
        fitnessIcon9 = findViewById(R.id.fitnessIcon9);
        fitnessIcon10 = findViewById(R.id.fitnessIcon10);

        //Запускаем все анимации
        startAllAnimations();


        initViews();
        setupClickListeners();

        //Скрытие статус-бара и навигационной панели
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );


        //Получаем ссылки на LinearLayout элементы по их ID
        LinearLayout arrowRunning = findViewById(R.id.arrowRunning);
        LinearLayout arrowPushUps = findViewById(R.id.arrowPushUps);
        LinearLayout arrowSquats = findViewById(R.id.arrowSquats);
        LinearLayout arrowJumpingJacks = findViewById(R.id.arrowJumpingJacks);
        LinearLayout arrowPlank = findViewById(R.id.arrowPlank);
        LinearLayout arrowLunges = findViewById(R.id.arrowLunges);
        LinearLayout arrowCrunches = findViewById(R.id.arrowCrunches);
        LinearLayout arrowBicepCurls = findViewById(R.id.arrowBicepCurls);
        LinearLayout arrowTricepDips = findViewById(R.id.arrowTricepDips);
        LinearLayout arrowLegRaises = findViewById(R.id.arrowLegRaises);


        //Устанавливаем слушатели нажатия на каждый LinearLayout
        //При нажатии вызывается соответствующий метод для открытия новой активности
        arrowRunning.setOnClickListener(v -> openRunningActivity());

        arrowPushUps.setOnClickListener(v -> openPushUpsActivity());

        arrowSquats.setOnClickListener(v -> openSquatsActivity());

        arrowJumpingJacks.setOnClickListener(v -> openJumpingJacksActivity());

        arrowPlank.setOnClickListener(v -> openPlankActivity());

        arrowLunges.setOnClickListener(v -> openLungesActivity());

        arrowCrunches.setOnClickListener(v -> openCrunchesActivity());

        arrowBicepCurls.setOnClickListener(v -> openBicepCurclsActivity());

        arrowTricepDips.setOnClickListener(v -> openTricepDipsActivity());

        arrowLegRaises.setOnClickListener(v -> openLegRaisesActivity());


        //Получаем ссылку на кнопку "Назад"
        btnBackArrowWorkout = findViewById(R.id.btnBackArrowWorkout);

        //Устанавливает слушатель на кнопку "Назад"
        btnBackArrowWorkout.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, MainActivity.class);
            startActivity(intent);
        });



        //Инициализация кнопок с изображениями для различных упражнений
        /*
        ImageButton arrowRunning = findViewById(R.id.arrowRunning);
        ImageButton arrowPushUps = findViewById(R.id.arrowPushUps);
        ImageButton arrowSquats = findViewById(R.id.arrowSquats);
        ImageButton arrowPlank = findViewById(R.id.arrowPlank);
        ImageButton arrowJumpingJacks = findViewById(R.id.arrowJumpingJacks);
        ImageButton arrowLunges = findViewById(R.id.arrowLunges);
        ImageButton arrowCrunches = findViewById(R.id.arrowCrunches);
        ImageButton arrowBicepCurls = findViewById(R.id.arrowBicepCurls);
        ImageButton arrowTricepDips = findViewById(R.id.arrowTricepDips);
        ImageButton arrowLegRaises = findViewById(R.id.arrowLegRaises);

        //Установка слушателей нажатий для каждой кнопки, запускающих активность ExcerciseActivity
        arrowRunning.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowPushUps.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowSquats.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowPlank.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowJumpingJacks.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowLunges.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowCrunches.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowBicepCurls.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowTricepDips.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });

        arrowLegRaises.setOnClickListener(v -> {
            Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class); //Создает новый Intent для перехода к ExerciseActivity
            startActivity(intent); //Запускает ExcerciseActivity
            //finish(); //Отрицательно завершает эту активность, если нужно
        });*/
    }

    private void filterWorkouts(String query) {

    }

    private void startAllAnimations() {
        //Устанавливаем анимации для каждого ImageView
        fitnessIcon1.setImageResource(R.drawable.bent_leg_twist_animation);
        fitnessIcon2.setImageResource(R.drawable.bicycle_crunches_animation);
        fitnessIcon3.setImageResource(R.drawable.butt_bridge_animation);
        fitnessIcon4.setImageResource(R.drawable.plunk_animation);
        fitnessIcon5.setImageResource(R.drawable.clapping_crunches_animation);
        fitnessIcon6.setImageResource(R.drawable.cross_arm_crunches_animation);
        fitnessIcon7.setImageResource(R.drawable.dead_bug_animation);
        fitnessIcon8.setImageResource(R.drawable.mountain_climbers_animation);
        fitnessIcon9.setImageResource(R.drawable.dead_bug_animation);
        fitnessIcon10.setImageResource(R.drawable.bent_leg_twist_animation);

        //Запускаем все анимации
        startAnimationForView(fitnessIcon1);
        startAnimationForView(fitnessIcon2);
        startAnimationForView(fitnessIcon3);
        startAnimationForView(fitnessIcon4);
        startAnimationForView(fitnessIcon5);
        startAnimationForView(fitnessIcon6);
        startAnimationForView(fitnessIcon7);
        startAnimationForView(fitnessIcon8);
        startAnimationForView(fitnessIcon9);
        startAnimationForView(fitnessIcon10);
    }

    private void startAnimationForView(ImageView imageView) {
        if (imageView.getDrawable() instanceof AnimationDrawable) {
            AnimationDrawable animation = (AnimationDrawable) imageView.getDrawable();
            if (!animation.isRunning()) {
                animation.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Останавливаем анимацию при выходе из активити
        AnimationDrawable bicycleAnimation = (AnimationDrawable) fitnessIcon1.getDrawable();
        if (bicycleAnimation != null) {
            bicycleAnimation.stop();
        }
    }

    private void initViews() {
        btnMenu = findViewById(R.id.btnMenu);
        textViewDayOfExercise = findViewById(R.id.textViewDayOfExercise);
    }

    private void setupClickListeners() {
        btnMenu.setOnClickListener(v -> showPopupMenu());
    }

    //Добавляем метод showPopupMenu
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(this, btnMenu);
        popupMenu.inflate(R.menu.workout_menu);

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();

            if (id == R.id.menu_edit) {
                Toast.makeText(this, "Редактирование", Toast.LENGTH_SHORT).show();
                return true;
            } else if (id == R.id.menu_add) {
                addWorkout();
                return true;
            } else if (id == R.id.menu_share) {
                shareWorkout();
                return true;
            } else if (id == R.id.menu_refresh) {
                refreshWorkout();
                return true;
            } else if (id == R.id.menu_delete) {
                deleteWorkout();
                return true;
            } else {
                return false;
            }
        });
        popupMenu.show();
    }


    private void shareWorkout() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Моя тренировка");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Посмотрите мою программу тренировок!");
        if (shareIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(shareIntent, "Поделиться тренировкой"));
        } else {
            Toast.makeText(this, "Нет приложений для отправки", Toast.LENGTH_SHORT).show();
        }
        /*Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Посмотрите мою программу тренировок!");
        startActivity(Intent.createChooser(shareIntent, "Поделиться тренировкой"));*/
    }

    // ДОБАВЛЯЕМ ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    private void addWorkout() {
        //Создаем диалог для ввода данных
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавить тренировку");

        //Создаем layout для диалога
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        //Поле для названия тренировки
        final EditText titleInput = new EditText(this);
        titleInput.setHint("Название тренировки");
        layout.addView(titleInput);

        //Поле для описания
        final EditText descriptionInput = new EditText(this);
        descriptionInput.setHint("Описание тренировки");
        layout.addView(descriptionInput);

        builder.setView(layout);

        //Кнопки диалога
        builder.setPositiveButton("Добавить", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();

            if (!title.isEmpty() && !description.isEmpty()) {
                addWorkoutToLayout(title, description);
            } else {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }

    private void addWorkoutToLayout(String title, String description)
    {
        //Находим родительский контейнер
        LinearLayout workoutsContainer = findViewById(R.id.workoutsContainer);

        //Если контейнер не найден, ищем ScrollView
        if (workoutsContainer == null) {
            ScrollView scrollView = findViewById(R.id.scrollView);
            if (scrollView != null && scrollView.getChildAt(0) instanceof LinearLayout) {
                workoutsContainer = (LinearLayout) scrollView.getChildAt(0);
            }
        }

        if (workoutsContainer != null) {
            // Создаем CardView для новой тренировки
            CardView cardView = new CardView(this);

            // Настройки CardView
            CardView.LayoutParams cardParams = new CardView.LayoutParams(
                    CardView.LayoutParams.MATCH_PARENT,
                    CardView.LayoutParams.WRAP_CONTENT
            );
            cardParams.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            cardView.setLayoutParams(cardParams);
            cardView.setCardElevation(dpToPx(4));
            cardView.setRadius(dpToPx(8));
            cardView.setClickable(true);
            cardView.setFocusable(true);
            cardView.setCardBackgroundColor(getResources().getColor(android.R.color.white));

            // Создаем LinearLayout для содержимого
            LinearLayout contentLayout = new LinearLayout(this);
            contentLayout.setOrientation(LinearLayout.HORIZONTAL);
            contentLayout.setGravity(Gravity.CENTER_VERTICAL);
            contentLayout.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16));

            // Создаем ImageView для иконки упражнения
            ImageView exerciseIcon = new ImageView(this);
            exerciseIcon.setLayoutParams(new LinearLayout.LayoutParams(
                    dpToPx(64),
                    dpToPx(64)
            ));
            exerciseIcon.setScaleType(ImageView.ScaleType.CENTER_CROP);

            // Создаем TextView для текста
            LinearLayout textLayout = new LinearLayout(this);
            textLayout.setOrientation(LinearLayout.VERTICAL);
            textLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
            ));

            // TextView для названия
            TextView titleTextView = new TextView(this);
            titleTextView.setText(title);
            titleTextView.setTextSize(18);
            titleTextView.setTypeface(titleTextView.getTypeface(), Typeface.BOLD);
            titleTextView.setTextColor(getResources().getColor(android.R.color.black));
            textLayout.addView(titleTextView);

            // TextView для описания
            TextView descTextView = new TextView(this);
            descTextView.setText(description);
            descTextView.setTextSize(14);
            descTextView.setTextColor(getResources().getColor(android.R.color.darker_gray));
            descTextView.setPadding(0, dpToPx(4), 0, 0);
            textLayout.addView(descTextView);

            // Создаем ImageView для стрелки
            ImageView arrowImage = new ImageView(this);
            arrowImage.setImageResource(android.R.drawable.ic_media_next);
            arrowImage.setLayoutParams(new LinearLayout.LayoutParams(
                    dpToPx(24),
                    dpToPx(24)
            ));

            // Устанавливаем анимацию в зависимости от названия упражнения
            int exerciseImageResource = getExerciseImageResource(title);
            exerciseIcon.setImageResource(exerciseImageResource);

            // Запускаем анимацию
            AnimationDrawable animation = (AnimationDrawable) exerciseIcon.getDrawable();
            if (animation != null) {
                animation.start();
            }

            // Добавляем элементы в layout
            contentLayout.addView(exerciseIcon);
            contentLayout.addView(textLayout);
            contentLayout.addView(arrowImage);

            // Добавляем contentLayout в CardView
            cardView.addView(contentLayout);

            // Добавляем обработчик клика
            cardView.setOnClickListener(v -> {
                Intent intent = new Intent(WorkoutActivity.this, ExerciseActivity.class);
                intent.putExtra("day", 1);
                intent.putExtra("exerciseName", title);
                intent.putExtra("exerciseImageResource", exerciseImageResource);
                startActivity(intent);
            });

            // Добавляем CardView в контейнер
            workoutsContainer.addView(cardView);

            Toast.makeText(this, "Тренировка добавлена", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка: контейнер не найден", Toast.LENGTH_SHORT).show();
        }
    }

    // Метод для получения ресурса изображения по названию упражнения
    private int getExerciseImageResource(String exerciseName) {
        switch (exerciseName.toLowerCase()) {
            case "скручивание согнутой ноги":
                return R.drawable.bent_leg_twist_animation;
            case "велосипедные скручивания":
                return R.drawable.bicycle_crunches_animation;
            case "ягодичный мостик":
                return R.drawable.butt_bridge_animation;
            case "планка":
                return R.drawable.plunk_animation;
            case "скручивания с хлопком":
                return R.drawable.clapping_crunches_animation;
            case "скручивания со скрещенными руками":
                return R.drawable.cross_arm_crunches_animation;
            case "упражнение мертвый жук":
                return R.drawable.dead_bug_animation;
            case "бег в упоре лежа":
                return R.drawable.mountain_climbers_animation;
            case "упражнение для пресса":
                return R.drawable.neo_butt_bridge_b;
            case "скручивание согнутых ног":
                return R.drawable.bent_leg_twist_animation;
            default:
                // Если упражнение не распознано, используем случайную анимацию
                int[] availableAnimations = {
                        R.drawable.bent_leg_twist_animation,
                        R.drawable.bicycle_crunches_animation,
                        R.drawable.butt_bridge_animation,
                        R.drawable.plunk_animation,
                        R.drawable.clapping_crunches_animation,
                        R.drawable.cross_arm_crunches_animation,
                        R.drawable.dead_bug_animation,
                        R.drawable.mountain_climbers_animation
                };
                Random random = new Random();
                return availableAnimations[random.nextInt(availableAnimations.length)];
        }
    }

    // Вспомогательный метод для конвертации dp в pixels
    private int dpToPx(int dp) {
        return (int) (dp * getResources().getDisplayMetrics().density);
    }

    private void refreshWorkout() {
        Toast.makeText(this, "Данные обновлены", Toast.LENGTH_SHORT).show();
    }

    private void deleteWorkout() {
        new AlertDialog.Builder(this)
                .setTitle("Удаление тренировки")
                .setMessage("Вы уверены, что хотите удалить эту тренировку?")
                .setPositiveButton("Удалить", (dialog, which) -> {
                    Toast.makeText(this, "Тренировка удалена", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    /**
     * Метод для открытия активности с упражнениями.
     * Создает Intent для перехода к ExerciseActivity и запускает эту активность.
     */
    private void openRunningActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Скручивание согнутой ноги");
        intent.putExtra("exerciseImageResource", R.drawable.bent_leg_twist_animation);
        startActivity(intent);
    }

    private void openPushUpsActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Велосипедные скручивания");
        intent.putExtra("exerciseImageResource", R.drawable.bicycle_crunches_animation);
        startActivity(intent);
    }

    private void openSquatsActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Ягодичный мостик");
        intent.putExtra("exerciseImageResource", R.drawable.butt_bridge_animation);
        startActivity(intent);
    }

    private void openJumpingJacksActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Скручивания с хлопком");
        intent.putExtra("exerciseImageResource", R.drawable.clapping_crunches_animation);
        startActivity(intent);
    }

    private void openPlankActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Планка");
        intent.putExtra("exerciseImageResource", R.drawable.plunk_animation);
        startActivity(intent);
    }

    private void openLungesActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Скручивания со скрещенными руками");
        intent.putExtra("exerciseImageResource", R.drawable.cross_arm_crunches_animation);
        startActivity(intent);
    }

    private void openCrunchesActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Упражнение мертвый жук");
        intent.putExtra("exerciseImageResource", R.drawable.dead_bug_animation);
        startActivity(intent);
    }

    private void openBicepCurclsActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Бег в упоре лежа");
        intent.putExtra("exerciseImageResource", R.drawable.mountain_climbers_animation);
        startActivity(intent);
    }

    private void openTricepDipsActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Упражнение для пресса");
        intent.putExtra("exerciseImageResource", R.drawable.neo_butt_bridge_b);
        startActivity(intent);
    }

    private void openLegRaisesActivity() {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("day", 1);
        intent.putExtra("exerciseName", "Скручивание согнутых ног");
        intent.putExtra("exerciseImageResource", R.drawable.bent_leg_twist_animation);
        startActivity(intent);
    }
}