package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ StatsActivity (ЭКРАН СТАТИСТИКИ И ДОСТИЖЕНИЙ)
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * StatsActivity является экраном статистики и достижений в приложении Fitness App.
 * Сюда пользователь попадает при нажатии на кнопку "Статистика" на главном экране,
 * из бокового меню или из нижней панели навигации.
 *
 * Этот экран отображает прогресс пользователя в виде системы наград (звёзд),
 * которые зарабатываются за успешное выполнение упражнений.
 *
 * Основные функции экрана:
 * - Отображение количества заработанных звёзд (от 0 до 5)
 * - Визуальное представление звёзд (заполненные и пустые иконки)
 * - Текстовое отображение прогресса ("Звезд: X" и "Собрано X из 5 звезд")
 * - Автоматическое обновление при возвращении на экран
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * Верхняя панель (Toolbar):
 * - Кнопка "Назад" (стрелка влево) — возврат на предыдущий экран
 * - Заголовок "Статистика"
 *
 * Основная область:
 * - Пять иконок звёзд (star1, star2, star3, star4, star5) — отображаются
 *   в ряд, показывают количество заработанных звёзд
 * - Текстовое поле "Звезд: X" (stars_count_text) — показывает количество
 *   звёзд цифрой
 * - Текстовое поле "Собрано X из 5 звезд" (stars_progress_text) — показывает
 *   прогресс в виде текста
 *
 *
 * ЧАСТЬ 3. СИСТЕМА ЗВЁЗД (НАГРАДЫ) (ДЕТАЛЬНО)
 * --------------------------------------------
 *
 * 3.1. Как зарабатываются звёзды:
 *
 * Звёзды начисляются в ExerciseActivity при успешном завершении упражнения.
 * За каждое полностью выполненное упражнение пользователь получает одну звезду.
 *
 * 3.2. Максимальное количество:
 *
 * Максимум можно накопить 5 звёзд. После достижения 5 звёзд новые не начисляются,
 * и пользователь видит сообщение "У вас уже максимальное количество звезд!".
 *
 * 3.3. Хранение звёзд:
 *
 * Количество звёзд сохраняется в SharedPreferences с именем "FitnessAppStats"
 * по ключу "stars_count". При первом запуске значение по умолчанию — 0.
 *
 * 3.4. Визуальное отображение:
 *
 * Звёзды отображаются в виде двух типов иконок:
 * - ic_star_filled — заполненная (золотая/жёлтая) звезда
 * - ic_star_empty — пустая (серая) звезда
 *
 * Пример: если у пользователя 3 звезды, будут показаны:
 * - star1: заполненная
 * - star2: заполненная
 * - star3: заполненная
 * - star4: пустая
 * - star5: пустая
 *
 *
 * ЧАСТЬ 4. ОСНОВНЫЕ МЕТОДЫ (ДЕТАЛЬНО)
 * ------------------------------------
 *
 * 4.1. onCreate():
 *
 * - Устанавливает цвет статус-бара (синий, из ресурсов R.color.menuColor)
 * - Устанавливает layout activity_stats.xml
 * - Инициализирует SharedPreferences для работы с настройками
 * - Вызывает initViews() для поиска UI-элементов
 * - Вызывает loadAndDisplayStars() для загрузки и отображения звёзд
 * - Вызывает setupToolbar() для настройки верхней панели
 *
 * 4.2. initViews():
 *
 * Находит все UI-элементы по их ID из XML:
 * - Пять ImageView для звёзд (star1, star2, star3, star4, star5)
 * - TextView для отображения количества звёзд (stars_count_text)
 * - TextView для отображения прогресса (stars_progress_text)
 *
 * 4.3. loadAndDisplayStars():
 *
 * - Получает текущее количество звёзд через getStarsCount()
 * - Передаёт это значение в updateStarsDisplay() для отображения
 *
 * 4.4. updateStarsDisplay(int starsCount):
 *
 * Главный метод визуализации звёзд:
 *
 * - Определяет иконки: starFilled = R.drawable.ic_star_filled,
 *                      starEmpty = R.drawable.ic_star_empty
 *
 * - Для каждой из 5 звёзд проверяет: если starsCount >= номер звезды,
 *   то устанавливает заполненную иконку, иначе — пустую
 *
 * - Обновляет текстовые поля:
 *   starsCountText.setText("Звезд: " + starsCount)
 *   starsProgressText.setText("Собрано " + starsCount + " из 5 звезд")
 *
 * 4.5. getStarsCount():
 *
 * - Читает из SharedPreferences значение по ключу KEY_STARS_COUNT
 * - Если значение не найдено, возвращает 0 (по умолчанию)
 * - Возвращает int — количество звёзд
 *
 * 4.6. addStar():
 *
 * - Получает текущее количество звёзд
 * - Если текущее количество меньше 5, увеличивает на 1
 * - Сохраняет новое значение через saveStarsCount()
 * - Обновляет отображение через updateStarsDisplay()
 *
 * ВНИМАНИЕ: Этот метод вызывается из ExerciseActivity при завершении
 * упражнения. Он публичный (public), чтобы к нему был доступ из другого класса.
 *
 * 4.7. saveStarsCount(int starsCount):
 *
 * - Получает редактор SharedPreferences
 * - Сохраняет значение по ключу KEY_STARS_COUNT
 * - Применяет изменения через apply()
 *
 * 4.8. resetStars():
 *
 * - Сохраняет значение 0 в SharedPreferences
 * - Обновляет отображение через updateStarsDisplay(0)
 *
 * ВНИМАНИЕ: Этот метод предназначен для тестирования. Позволяет сбросить
 * прогресс звёзд без выполнения упражнений.
 *
 * 4.9. setupToolbar():
 *
 * - Находит Toolbar по ID
 * - Устанавливает Toolbar как ActionBar
 * - Включает кнопку "Назад" (setDisplayHomeAsUpEnabled)
 * - Включает отображение иконки "Домой" (setDisplayShowHomeEnabled)
 * - Устанавливает заголовок "Статистика"
 *
 * 4.10. onResume():
 *
 * - Вызывается каждый раз, когда пользователь возвращается на этот экран
 * - Обновляет отображение звёзд через loadAndDisplayStars()
 * - Это гарантирует, что если звёзды были изменены в другом месте
 *   (например, в ExerciseActivity), на экране статистики отобразятся
 *   актуальные данные
 *
 *
 * ЧАСТЬ 5. ХРАНЕНИЕ ДАННЫХ (SHAREDPREFERENCES) (ДЕТАЛЬНО)
 * --------------------------------------------------------
 *
 * 5.1. Имя файла настроек:
 *
 * private static final String PREFS_NAME = "FitnessAppStats";
 *
 * Это отдельный файл настроек,专门 для хранения статистики пользователя.
 *
 * 5.2. Ключ для звёзд:
 *
 * private static final String KEY_STARS_COUNT = "stars_count";
 *
 * 5.3. Структура данных:
 *
 * FitnessAppStats.xml (внутреннее хранилище приложения):
 * <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
 * <map>
 *     <int name="stars_count" value="3" />
 * </map>
 *
 *
 * ЧАСТЬ 6. ОФОРМЛЕНИЕ И СТИЛИ
 * ----------------------------
 *
 * 6.1. Статус-бар:
 *
 * Для Android 5.0 (Lollipop) и выше устанавливается цвет статус-бара.
 * Цвет берётся из ресурсов: R.color.menuColor (синий).
 *
 * Код:
 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
 *     getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
 * }
 *
 * 6.2. Toolbar:
 *
 * - Устанавливается как ActionBar
 * - Включается кнопка "Назад"
 * - Заголовок "Статистика"
 *
 *
 * ЧАСТЬ 7. ВЗАИМОДЕЙСТВИЕ С ДРУГИМИ КЛАССАМИ (ДЕТАЛЬНО)
 * ------------------------------------------------------
 *
 * 7.1. ExerciseActivity → StatsActivity:
 *
 * В ExerciseActivity при успешном завершении упражнения вызывается метод
 * saveStarToStats(), который работает напрямую с SharedPreferences.
 *
 * Код из ExerciseActivity:
 * SharedPreferences statsPrefs = getSharedPreferences("FitnessAppStats", MODE_PRIVATE);
 * int currentStars = statsPrefs.getInt("stars_count", 0);
 * currentStars++;
 * editor.putInt("stars_count", currentStars);
 *
 * 7.2. StatsActivity ← SharedPreferences:
 *
 * StatsActivity читает то же самое SharedPreferences и отображает
 * актуальное количество звёзд.
 *
 * Таким образом, данные централизованно хранятся в одном месте, а два
 * разных экрана работают с ними независимо.
 *
 *
 * ЧАСТЬ 8. ОСОБЕННОСТИ РЕАЛИЗАЦИИ
 * ---------------------------------
 *
 * 8.1. Публичные методы:
 *
 * Методы getStarsCount(), addStar(), resetStars() объявлены как public.
 * Это сделано для того, чтобы другие классы (например, ExerciseActivity)
 * могли получить доступ к системе звёзд.
 *
 * 8.2. Обновление при возвращении:
 *
 * В onResume() вызывается loadAndDisplayStars(), что гарантирует актуальность
 * данных при каждом показе экрана. Это полезно, если пользователь:
 * - Выполнил упражнение и получил звезду
 * - Вернулся на экран статистики через навигацию
 *
 *
 * ЧАСТЬ 9. ОСНОВНЫЕ МЕТОДЫ (КРАТКИЙ СПИСОК)
 * ------------------------------------------
 *
 * onCreate() — инициализация активности: статус-бар, layout, SharedPreferences,
 *              инициализация View, загрузка звёзд, настройка Toolbar
 *
 * initViews() — поиск всех UI-элементов на экране
 *
 * loadAndDisplayStars() — загрузка количества звёзд и отображение
 *
 * updateStarsDisplay() — визуальное обновление иконок и текста звёзд
 *
 * getStarsCount() — получение количества звёзд из SharedPreferences
 *
 * addStar() — добавление одной звезды (вызывается из ExerciseActivity)
 *
 * saveStarsCount() — сохранение количества звёзд в SharedPreferences
 *
 * resetStars() — сброс звёзд (для тестирования)
 *
 * setupToolbar() — настройка верхней панели инструментов
 *
 * onResume() — обновление данных при возвращении на экран
 *
 *
 * ЧАСТЬ 10. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * -----------------------------------------------------
 *
 * 1. Только звёзды:
 *    - Сейчас отображается только система звёзд
 *    - Нужно добавить другую статистику: общее время тренировок,
 *      количество выполненных упражнений, сожжённые калории
 *
 * 2. Нет графиков:
 *    - Отсутствует визуализация прогресса в виде графиков
 *    - Нужно добавить графики прогресса по дням/неделям
 *
 * 3. Нет истории достижений:
 *    - Нет списка полученных достижений с датами
 *    - Нужно добавить экран с историей наград
 *
 * 4. Нет социального сравнения:
 *    - Нет возможности сравнить свой прогресс с друзьями
 *    - Нужно добавить таблицу лидеров
 *
 * 5. Нет подробной статистики по упражнениям:
 *    - Не показывается, какие упражнения выполнены, какие нет
 *    - Нужно добавить детализацию по каждому упражнению
 *
 * 6. Нет экспорта статистики:
 *    - Нельзя выгрузить данные для анализа
 *    - Нужно добавить экспорт в CSV или PDF
 *
 * 7. Сброс звёзд только для тестирования:
 *    - resetStars() не должен быть доступен обычным пользователям
 *    - Нужно скрыть или защитить этот метод
 *
 *
 * ЧАСТЬ 11. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * -----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 * - AndroidX Core (для работы с цветами ContextCompat)
 *
 * Ресурсы:
 * - R.drawable.ic_star_filled — иконка заполненной звезды
 * - R.drawable.ic_star_empty — иконка пустой звезды
 * - R.color.menuColor — цвет статус-бара
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 *
 *
 * ЧАСТЬ 12. ИТОГИ
 * ----------------
 *
 * StatsActivity — это экран статистики и достижений, который отображает
 * прогресс пользователя в виде системы звёзд, зарабатываемых за выполнение
 * упражнений.
 *
 * Реализованы следующие функции:
 * - Отображение количества звёзд (от 0 до 5)
 * - Визуальное представление заполненных и пустых звёзд
 * - Текстовое отображение прогресса
 * - Сохранение данных в SharedPreferences
 * - Автоматическое обновление при возвращении на экран
 * - Интеграция с ExerciseActivity через общее хранилище
 *
 * Экран имеет простой и понятный интерфейс, корректно сохраняет и отображает
 * прогресс пользователя, а также готов к расширению для добавления других
 * видов статистики и достижений.
 *
 * =============================================================================
 */


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;

public class StatsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FitnessAppStats";
    private static final String KEY_STARS_COUNT = "stars_count";

    //UI элементы для звезд
    private ImageView star1, star2, star3, star4, star5;
    private TextView starsCountText, starsProgressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        setContentView(R.layout.activity_stats);

        //Инициализация SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        //Инициализация UI элементов
        initViews();

        //Загрузка и отображение звезд
        loadAndDisplayStars();

        setupToolbar();
    }

    /**
     * Инициализация UI элементов
     */
    private void initViews() {
        star1 = findViewById(R.id.star1);
        star2 = findViewById(R.id.star2);
        star3 = findViewById(R.id.star3);
        star4 = findViewById(R.id.star4);
        star5 = findViewById(R.id.star5);
        starsCountText = findViewById(R.id.stars_count_text);
        starsProgressText = findViewById(R.id.stars_progress_text);
    }

    /**
     * Загрузка и отображение количества звезд
     */
    private void loadAndDisplayStars() {
        int starsCount = getStarsCount();
        updateStarsDisplay(starsCount);
    }

    /**
     * Обновление отображения звезд
     */
    private void updateStarsDisplay(int starsCount) {
        // Устанавливаем заполненные звезды
        int starFilled = R.drawable.ic_star_filled; //Заполненная звезда
        int starEmpty = R.drawable.ic_star_empty;   //Пустая звезда

        star1.setImageResource(starsCount >= 1 ? starFilled : starEmpty);
        star2.setImageResource(starsCount >= 2 ? starFilled : starEmpty);
        star3.setImageResource(starsCount >= 3 ? starFilled : starEmpty);
        star4.setImageResource(starsCount >= 4 ? starFilled : starEmpty);
        star5.setImageResource(starsCount >= 5 ? starFilled : starEmpty);

        // Обновляем тексты
        starsCountText.setText("Звезд: " + starsCount);
        starsProgressText.setText("Собрано " + starsCount + " из 5 звезд");
    }

    /**
     * Получение количества звезд из SharedPreferences
     */
    public int getStarsCount() {
        return sharedPreferences.getInt(KEY_STARS_COUNT, 0);
    }

    /**
     * Добавление звезды (вызывается из ExerciseActivity)
     */
    public void addStar() {
        int currentStars = getStarsCount();
        if (currentStars < 5) {
            currentStars++;
            saveStarsCount(currentStars);
            updateStarsDisplay(currentStars);
        }
    }

    /**
     * Сохранение количества звезд в SharedPreferences
     */
    private void saveStarsCount(int starsCount) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_STARS_COUNT, starsCount);
        editor.apply();
    }

    /**
     * Сброс звезд (для тестирования)
     */
    public void resetStars() {
        saveStarsCount(0);
        updateStarsDisplay(0);
    }

    /**
     * Настройка верхней панели (Toolbar)
     */
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("Статистика");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Обновляем отображение звезд при возвращении на экран
        loadAndDisplayStars();
    }
}