package com.example.fitnessapp.activities;

/**
 * =============================================================================
 * ДОКУМЕНТАЦИЯ К МОДУЛЮ CoachActivity
 * =============================================================================
 *
 *
 * ЧАСТЬ 1. ОБЩЕЕ ОПИСАНИЕ
 * ------------------------
 *
 * CoachActivity является главным экраном для тренера в приложении Fitness App.
 * Сюда пользователь попадает после авторизации и отсюда управляет своей работой:
 * создаёт тренировки, просматривает список учеников, настраивает расписание,
 * а также переходит в другие разделы: статистику, настройки и чат.
 *
 * Экран выполняет роль "рабочего стола" тренера. Здесь собраны все основные
 * инструменты, которые нужны для ежедневной работы с учениками и тренировками.
 *
 *
 * ЧАСТЬ 2. СТРУКТУРА ЭКРАНА
 * --------------------------
 *
 * В верхней части экрана расположена текущая дата в формате "Сегодня: 9 апреля 2026".
 * Это помогает тренеру ориентироваться во времени при планировании занятий.
 *
 * Основные элементы управления:
 *
 * - Кнопка "Добавить тренировку" (btnAddSession) — открывает форму для быстрого
 *   создания новой тренировки. Тренер вводит название, выбирает тип, сложность,
 *   длительность и время. После сохранения происходит переход на экран WorkoutActivity
 *   для более детального заполнения.
 *
 * - Кнопка "Ученики" (btn_Students) — показывает диалоговое окно со списком всех
 *   учеников, которые закреплены за этим тренером. При нажатии на имя ученика
 *   появляется всплывающее сообщение с подтверждением выбора. Это база для будущего
 *   функционала просмотра профиля ученика и его прогресса.
 *
 * - Кнопка "Расписание" (btn_schedule) — позволяет настроить еженедельные
 *   напоминания о тренировках. Тренер выбирает дни недели и время, после чего
 *   система автоматически будет показывать уведомления. Напоминания работают даже
 *   при закрытом приложении.
 *
 * - Кнопка "Чат" (chatBtn) — открывает экран общения с учениками. Перед переходом
 *   система проверяет, авторизован ли пользователь. Если нет — показывает диалог
 *   с предложением войти в аккаунт.
 *
 * - Кнопка "Тренировки" (workBtn) — переход на экран со всеми тренировками,
 *   где можно просматривать, редактировать и удалять созданные занятия.
 *
 * - Кнопка "Статистика" (statsBtn) — переход на экран с графиками и показателями
 *   прогресса тренера и его учеников.
 *
 * - Кнопка "Настройки" (settBtn) — переход в раздел настроек приложения,
 *   где можно изменить профиль, тему оформления и другие параметры.
 *
 *
 * ЧАСТЬ 3. РАБОТА СО СПИСКОМ УЧЕНИКОВ (ДЕТАЛЬНО)
 * -----------------------------------------------
 *
 * Кнопка "Ученики" реализована через AlertDialog. Это стандартный компонент
 * Android, который показывает всплывающее окно. Внутри диалога отображается
 * список имён. В текущей версии используется статический массив из 15 человек:
 * Иван Иванов, Мария Сидорова, Алексей Петров, Елена Козлова, Дмитрий Волков,
 * Ольга Новикова, Сергей Морозов, Анна Павлова, Игорь Федоров, Татьяна Соколова,
 * Андрей Кузнецов, Наталья Васильева, Артем Попов, Юлия Михайлова, Виктор Степанов.
 *
 * Когда тренер нажимает на любое имя, срабатывает слушатель, который получает
 * индекс нажатого элемента и показывает Toast с текстом "Выбран: Имя Фамилия".
 * Кнопка "Закрыть" просто закрывает диалог без каких-либо действий.
 *
 * В будущем этот блок будет доработан: данные станут загружаться из Firebase,
 * а при клике на ученика будет открываться его личный профиль с историей тренировок.
 *
 *
 * ЧАСТЬ 4. СОЗДАНИЕ НОВОЙ ТРЕНИРОВКИ (ДЕТАЛЬНО)
 * ----------------------------------------------
 *
 * При нажатии на "Добавить тренировку" вызывается метод showAddWorkoutDialog().
 * Он динамически создаёт форму с пятью полями:
 *
 * 1. Название тренировки — текстовое поле, обязательное для заполнения.
 * 2. Тип тренировки — выпадающий список с десятью вариантами: Силовая, Кардио,
 *    Йога, Пилатес, Кроссфит, Функциональная, Стретчинг, Аэробика, Бокс, Танцы.
 * 3. Длительность — поле только для цифр, указывается в минутах.
 * 4. Сложность — выпадающий список: Начинающий, Лёгкий, Средний, Сложный,
 *    Профессиональный.
 * 5. Время — текстовое поле в формате ЧЧ:ММ. Если оставить пустым,
 *    подставится текущее время.
 *
 * После нажатия кнопки "Создать" происходит проверка: название и длительность
 * не могут быть пустыми. Если всё заполнено верно, создаётся Intent для перехода
 * на WorkoutActivity, и через метод putExtra передаются все параметры:
 * название, тип, длительность, сложность, текущая дата, время и специальный флаг
 * is_new_workout, который указывает, что это новая тренировка.
 *
 * Если какое-то поле заполнено неверно (например, в длительности буквы),
 * пользователь увидит всплывающее сообщение с ошибкой.
 *
 *
 * ЧАСТЬ 5. НАПОМИНАНИЯ И РАСПИСАНИЕ (ДЕТАЛЬНО)
 * ---------------------------------------------
 *
 * Система напоминаний работает через AlarmManager — это встроенный механизм
 * Android для выполнения задач в заданное время, даже если приложение закрыто.
 *
 * Когда тренер нажимает кнопку "Расписание", открывается диалог с чекбоксами
 * для каждого дня недели (понедельник — воскресенье) и TimePicker для выбора
 * времени. После нажатия "Сохранить" система проходит по всем дням, и для каждого
 * выбранного дня создаёт отдельное повторяющееся напоминание.
 *
 * Технически это работает так: для каждого дня вычисляется, когда наступит
 * ближайшее указанное время. Если время сегодняшнего дня уже прошло,
 * напоминание ставится на следующую неделю. Затем AlarmManager запускается
 * в режиме setRepeating с интервалом ровно в 7 дней.
 *
 * Когда наступает заданное время и день, срабатывает внутренний класс
 * ReminderBroadcast. Он получает системное событие и показывает уведомление
 * с заголовком "🏋️ Пора на тренировку!" и текстом "У вас запланирована тренировка
 * сегодня! Не пропустите."
 *
 * Для корректной работы уведомлений на Android 8 и выше создаётся канал
 * уведомлений с названием "Напоминания о тренировках" и высоким приоритетом.
 * На Android 13 и выше дополнительно запрашивается разрешение POST_NOTIFICATIONS.
 *
 *
 * ЧАСТЬ 6. ЧАТ И ПРОВЕРКА АВТОРИЗАЦИИ
 * ------------------------------------
 *
 * Чат доступен только авторизованным пользователям. При нажатии на кнопку чата
 * вызывается метод checkAuthAndOpenChat(). Он получает текущий экземпляр
 * FirebaseAuth и проверяет, есть ли авторизованный пользователь.
 *
 * Если пользователь авторизован, сразу открывается ChatActivity.
 * Если нет — появляется диалог с сообщением "Для доступа к чату необходимо
 * войти в аккаунт" и двумя кнопками: "Войти" и "Отмена". При нажатии "Войти"
 * открывается ChatActivity, где пользователь сможет пройти авторизацию.
 *
 *
 * ЧАСТЬ 7. ДАТА И ОФОРМЛЕНИЕ
 * ---------------------------
 *
 * Дата в верхней части экрана обновляется при каждом запуске Activity.
 * Используется SimpleDateFormat с русской локалью, поэтому месяц выводится
 * словом (например, "апреля"), а не цифрой.
 *
 * Статус-бар (верхняя полоска с часами и батареей) окрашивается в синий цвет,
 * заданный в ресурсах как R.color.menuColor. Это работает на Android версии
 * 5.0 (Lollipop) и выше.
 *
 *
 * ЧАСТЬ 8. ИЗВЕСТНЫЕ ОГРАНИЧЕНИЯ И ПЛАНЫ ПО ДОРАБОТКЕ
 * ----------------------------------------------------
 *
 * 1. Список учеников сейчас статический. В планах — загружать его из Firebase
 *    Firestore, чтобы тренер видел только своих реальных учеников.
 *
 * 2. При клике на ученика сейчас только Toast. Нужно добавить переход на
 *    экран с подробной информацией об ученике: его прогресс, выполненные
 *    тренировки, личные достижения.
 *
 * 3. Созданные тренировки пока не сохраняются в базу данных. Они просто
 *    передаются в WorkoutActivity и теряются при перезапуске приложения.
 *    Необходимо добавить сохранение в Firebase или локальную базу.
 *
 * 4. Напоминания работают, но у пользователя нет возможности отключить их
 *    без удаления всего расписания. Нужно добавить кнопку "Отключить все
 *    напоминания" и возможность редактировать уже созданное расписание.
 *
 *
 * ЧАСТЬ 9. ЗАВИСИМОСТИ И ТРЕБОВАНИЯ
 * ----------------------------------
 *
 * Для работы модуля необходимы следующие библиотеки:
 * - Firebase Auth (для проверки авторизации)
 * - AndroidX AppCompat (для совместимости с разными версиями Android)
 *
 * Минимальная версия Android: API 21 (Android 5.0 Lollipop)
 * Рекомендуемая версия: API 33 (Android 13) и выше
 *
 *
 * КОНЕЦ ДОКУМЕНТАЦИИ
 * =============================================================================
 */

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.example.fitnessapp.R;
import com.example.fitnessapp.chat.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CoachActivity extends AppCompatActivity {

    //Кнопка для добавления новой тренировки (открывает диалог создания тренировки)
    private Button btnAddSession;

    //Кнопка-иконка для перехода в раздел "Тренировки" (WorkoutActivity)
    private ImageView workBtn;

    //Кнопка-иконка для перехода в раздел "Статистика" (StatsActivity)
    private ImageView statsBtn;

    //Кнопка-иконка для перехода в раздел "Настройки" (SettingsActivity)
    private ImageView settBtn;

    //Кнопка-иконка для перехода в раздел "Чат" (ChatActivity) с проверкой авторизации
    private ImageView chatBtn;

    //Текстовое поле для отображения текущей даты в формате "Сегодня: 9 апреля 2026"
    private TextView tvDate;

    //Кнопка-иконка для открытия диалога со списком всех учеников тренера
    private ImageView btnStudents;

    //Кнопка-иконка для настройки расписания и еженедельных напоминаний о тренировках
    private ImageView btnSchedule;

    //Уникальный идентификатор канала уведомлений для Android 8+ (Oreo и выше)
    //Используется для отправки напоминаний о предстоящих тренировках
    private static final String CHANNEL_ID = "fitness_channel";

    //Массив типов тренировок для выпадающего списка
    private final String[] WORKOUT_TYPES = {
            "Силовая",
            "Кардио",
            "Йога",
            "Пилатес",
            "Кроссфит",
            "Функциональная",
            "Стретчинг",
            "Аэробика",
            "Бокс",
            "Танцы"
    };

    //Массив уровней сложности
    private final String[] DIFFICULTY_LEVELS = {
            "Начинающий",
            "Легкий",
            "Средний",
            "Сложный",
            "Профессиональный"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coach);

        //РЕШЕНИЕ ПРОБЛЕМЫ - сделать статус бар синим
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.menuColor));
        }

        //Находим TextView для даты
        tvDate = findViewById(R.id.tvDate);
        updateDate(); //Обновляем дату

        btnAddSession = findViewById(R.id.btnAddSession);

        chatBtn = findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(v -> {
            checkAuthAndOpenChat();
        });

        btnAddSession.setOnClickListener(v -> {
            showAddWorkoutDialog();
        });

        //1. Создаем массив данных
        String[] students = {
                "Иван Иванов", "Мария Сидорова", "Алексей Петров",
                "Елена Козлова", "Дмитрий Волков", "Ольга Новикова",
                "Сергей Морозов", "Анна Павлова", "Игорь Федоров",
                "Татьяна Соколова", "Андрей Кузнецов", "Наталья Васильева",
                "Артем Попов", "Юлия Михайлова", "Виктор Степанов"
        };

        btnStudents = findViewById(R.id.btn_Students); //Нахождение кнопки по его идентификатору

        //Устанавливаем слушатель клика на кнопку
        btnStudents.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(CoachActivity.this);
            builder.setTitle("Список зарегистрированных учеников")
                    //Устанавливаем список и слушатель нажатий
                    .setItems(students, (dialog, which) -> {
                        //which - это индекс нажатого элемента (от 0 до 14)
                        String selectedStudent = students[which];
                        Toast.makeText(CoachActivity.this, "Выбран: " + selectedStudent, Toast.LENGTH_SHORT).show();
                    }).setNegativeButton("Закрыть", (dialog, id) -> dialog.dismiss());

            AlertDialog alert = builder.create();
            alert.show();
        });

        workBtn = findViewById(R.id.workBtn);

        workBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, WorkoutActivity.class);
            startActivity(intent);
        });

        createNotificationChannel(); //Создаем канал уведомлений

        // Запрос разрешения прямо здесь
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }


        btnSchedule = findViewById(R.id.btn_schedule);

        btnSchedule.setOnClickListener(v -> {
            Toast.makeText(this, "Кнопка расписания нажата!", Toast.LENGTH_SHORT).show();
            //Показываем диалог с выбором времени для напоминания
            showScheduleDialog();
        });

        statsBtn = findViewById(R.id.statsBtn);

        statsBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, StatsActivity.class);
            startActivity(intent);
        });

        settBtn = findViewById(R.id.settBtn);

        settBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        });
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Напоминания о тренировках",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Напоминания о предстоящих тренировках");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    private void showScheduleDialog() {
        // Создаем диалог с выбором дней и времени
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 30);

        // Заголовок
        TextView title = new TextView(this);
        title.setText("Выберите дни тренировок:");
        title.setTextSize(16);
        title.setPadding(0, 0, 0, 15);
        layout.addView(title);

        // Дни недели с чекбоксами
        String[] days = {"Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота", "Воскресенье"};
        CheckBox[] checkBoxes = new CheckBox[7];

        for (int i = 0; i < days.length; i++) {
            checkBoxes[i] = new CheckBox(this);
            checkBoxes[i].setText(days[i]);
            layout.addView(checkBoxes[i]);
        }

        // Выбор времени
        TextView timeLabel = new TextView(this);
        timeLabel.setText("Выберите время:");
        timeLabel.setTextSize(16);
        timeLabel.setPadding(0, 20, 0, 10);
        layout.addView(timeLabel);

        TimePicker timePicker = new TimePicker(this);
        timePicker.setIs24HourView(true);
        layout.addView(timePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Настройка расписания")
                .setView(layout)
                .setPositiveButton("Сохранить", (dialog, which) -> {
                    // Собираем выбранные дни
                    StringBuilder selectedDays = new StringBuilder();
                    for (int i = 0; i < days.length; i++) {
                        if (checkBoxes[i].isChecked()) {
                            if (selectedDays.length() > 0) selectedDays.append(", ");
                            selectedDays.append(days[i]);
                        }
                    }

                    int hour = timePicker.getHour();
                    int minute = timePicker.getMinute();
                    String time = String.format("%02d:%02d", hour, minute);

                    if (selectedDays.length() > 0) {
                        // Устанавливаем напоминания на выбранные дни
                        setRemindersForSelectedDays(checkBoxes, hour, minute);

                        Toast.makeText(this,
                                "Расписание сохранено!\nДни: " + selectedDays +
                                        "\nВремя: " + time + "\nНапоминания установлены!",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(this, "Выберите хотя бы один день", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void setRemindersForSelectedDays(CheckBox[] checkBoxes, int hour, int minute) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Получаем текущий день недели (Calendar.SUNDAY = 1, MONDAY = 2, etc.)
        Calendar now = Calendar.getInstance();
        int currentDay = now.get(Calendar.DAY_OF_WEEK);

        // Для каждого выбранного дня устанавливаем напоминание
        for (int i = 0; i < checkBoxes.length; i++) {
            if (checkBoxes[i].isChecked()) {
                // i=0 - ПН (Calendar.MONDAY = 2)
                // i=1 - ВТ (Calendar.TUESDAY = 3)
                // i=2 - СР (Calendar.WEDNESDAY = 4)
                // i=3 - ЧТ (Calendar.THURSDAY = 5)
                // i=4 - ПТ (Calendar.FRIDAY = 6)
                // i=5 - СБ (Calendar.SATURDAY = 7)
                // i=6 - ВС (Calendar.SUNDAY = 1)

                int targetDay;
                if (i == 6) {
                    targetDay = Calendar.SUNDAY;
                } else {
                    targetDay = i + 2; // ПН=2, ВТ=3, СР=4, ЧТ=5, ПТ=6, СБ=7
                }

                scheduleWeeklyReminder(alarmManager, targetDay, hour, minute);
            }
        }
    }

    private void scheduleWeeklyReminder(AlarmManager alarmManager, int targetDay, int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Устанавливаем нужный день недели
        int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        calendar.set(Calendar.DAY_OF_WEEK, targetDay);

        // Если этот день уже прошел на этой неделе, переносим на следующую
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }

        // Создаем уникальный ID для каждого дня (чтобы не перезаписывать)
        int uniqueId = targetDay * 100 + hour * 10 + minute;

        Intent intent = new Intent(this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Устанавливаем повторяющееся напоминание на каждую неделю
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent);
        }
    }

    // ВНУТРЕННИЙ КЛАСС ДЛЯ УВЕДОМЛЕНИЙ
    public static class ReminderBroadcast extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("🏋️ Пора на тренировку!")
                    .setContentText("У вас запланирована тренировка сегодня! Не пропустите.")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            // Проверяем разрешение для Android 13+
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {
                    notificationManager.notify((int) System.currentTimeMillis(), builder.build());
                }
            } else {
                notificationManager.notify((int) System.currentTimeMillis(), builder.build());
            }
        }
    }

    //Метод для обновления даты
    private void updateDate() {
        //Получаем текущую дату
        Date currentDate = new Date();

        //Форматируем дату в нужный вид (русский язык)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", new Locale("ru"));
        String formattedDate = dateFormat.format(currentDate);

        //Устанавливаем текст
        tvDate.setText("Сегодня: " + formattedDate);
    }

    private void checkAuthAndOpenChat() {
        //Проверяем, авторизован ли пользователь
        FirebaseAuth auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            //Пользователь авторизован - открываем чат
            Intent intent = new Intent(CoachActivity.this, ChatActivity.class);
            startActivity(intent);
        } else {
            //Пользователь не авторизован - показываем диалог
            showAuthDialog();
        }
    }

    private void showAuthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Авторизация")
                .setMessage("Для доступа к чату необходимо войти в аккаунт")
                .setPositiveButton("Войти", (dialog, which) -> {
                    //Переходим на экран логина
                    Intent intent = new Intent(CoachActivity.this, ChatActivity.class);
                    startActivity(intent);
                })
                .setNegativeButton("Отмена", null)
                .show();
    }

    private void showAddWorkoutDialog() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 30, 50, 10);

        //1. Название - ручной ввод
        EditText etName = new EditText(this);
        etName.setHint("Введите название тренировки");
        layout.addView(etName);

        //2. Тип тренировки - Spinner
        android.widget.Spinner spinnerType = new android.widget.Spinner(this);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                WORKOUT_TYPES
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);
        layout.addView(spinnerType);

        //3. Длительность
        EditText etDuration = new EditText(this);
        etDuration.setHint("Длительность (минуты)");
        etDuration.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(etDuration);

        //4. Сложность - Spinner
        android.widget.Spinner spinnerDifficulty = new android.widget.Spinner(this);
        ArrayAdapter<String> difficultyAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                DIFFICULTY_LEVELS
        );
        difficultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDifficulty.setAdapter(difficultyAdapter);
        layout.addView(spinnerDifficulty);

        //5. Время
        EditText etTime = new EditText(this);
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        etTime.setHint("Время (например: " + currentTime + ")");
        layout.addView(etTime);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Создание новой тренировки")
                .setView(layout)
                .setPositiveButton("Создать", (dialog, which) -> {
                    String name = etName.getText().toString().trim();
                    String type = spinnerType.getSelectedItem().toString();
                    String durationStr = etDuration.getText().toString().trim();
                    String difficulty = spinnerDifficulty.getSelectedItem().toString();
                    String time = etTime.getText().toString().trim();

                    if (name.isEmpty()) {
                        Toast.makeText(this, "Введите название!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (durationStr.isEmpty()) {
                        Toast.makeText(this, "Введите длительность!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (time.isEmpty()) {
                        time = currentTime;
                    }

                    try {
                        int duration = Integer.parseInt(durationStr);

                        Intent intent = new Intent(this, WorkoutActivity.class);
                        intent.putExtra("workout_name", name);
                        intent.putExtra("workout_type", type);
                        intent.putExtra("workout_duration", duration);
                        intent.putExtra("workout_difficulty", difficulty);
                        intent.putExtra("workout_date",
                                new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date()));
                        intent.putExtra("workout_time", time);
                        intent.putExtra("is_new_workout", true);

                        startActivity(intent);

                        Toast.makeText(this,
                                "Тренировка '" + name + "' добавлена!", Toast.LENGTH_SHORT).show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Ошибка в длительности!", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Отмена", null)
                .show();
    }
}