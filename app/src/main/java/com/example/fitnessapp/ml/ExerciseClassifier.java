package com.example.fitnessapp.ml;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import java.util.LinkedList;
import java.util.Queue;

public class ExerciseClassifier {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private OnExerciseTrackedListener listener;

    private Queue<Float> accHistory = new LinkedList<>();
    private final int HISTORY_SIZE = 20;

    private int squatCount = 0;
    private int pushupCount = 0;
    private long plankTime = 0;
    private boolean isTracking = false;

    public interface OnExerciseTrackedListener {
        void onSquatCounted(int count);
        void onPushupCounted(int count);
        void onPlankTime(long seconds);
        void onCaloriesBurned(float calories);
    }

    public ExerciseClassifier(Context context) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }
    }

    public void startTracking() {
        if (accelerometer != null && !isTracking) {
            sensorManager.registerListener(sensorListener, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL);
            isTracking = true;
            Log.d("ExerciseTracker", "Трекинг начат");
        }
    }

    public void stopTracking() {
        if (isTracking) {
            sensorManager.unregisterListener(sensorListener);
            isTracking = false;
            Log.d("ExerciseTracker", "Трекинг остановлен");
        }
    }

    public void setListener(OnExerciseTrackedListener listener) {
        this.listener = listener;
    }

    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                float x = event.values[0];
                float y = event.values[1];
                float z = event.values[2];

                // Общее ускорение
                float acceleration = (float) Math.sqrt(x*x + y*y + z*z) - 9.81f;
                acceleration = Math.abs(acceleration);

                // Анализ
                analyzeAcceleration(acceleration);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
    };

    private void analyzeAcceleration(float acceleration) {
        // Добавляем в историю
        accHistory.add(acceleration);
        if (accHistory.size() > HISTORY_SIZE) {
            accHistory.poll();
        }

        // Определяем упражнения по простым порогам
        if (acceleration > 15.0f && accHistory.size() >= 10) {
            // Приседание
            squatCount++;
            if (listener != null) {
                listener.onSquatCounted(squatCount);
                float calories = squatCount * 0.5f;
                listener.onCaloriesBurned(calories);
            }
            Log.d("ExerciseTracker", "Приседание! Всего: " + squatCount);

            // Очищаем историю чтобы не считать несколько раз одно движение
            accHistory.clear();
        }
        else if (acceleration > 12.0f && acceleration < 15.0f) {
            // Отжимание
            pushupCount++;
            if (listener != null) {
                listener.onPushupCounted(pushupCount);
                float calories = pushupCount * 0.4f;
                listener.onCaloriesBurned(calories);
            }
            Log.d("ExerciseTracker", "Отжимание! Всего: " + pushupCount);
            accHistory.clear();
        }
        else if (acceleration < 3.0f && isPlankPosture()) {
            // Планка
            plankTime++;
            if (listener != null && plankTime % 10 == 0) { // Каждые 10 "тиков"
                listener.onPlankTime(plankTime / 10);
            }
        }
    }

    private boolean isPlankPosture() {
        // Простая проверка на планку
        if (accHistory.size() < 5) return false;

        float sum = 0;
        for (float acc : accHistory) sum += acc;
        float avg = sum / accHistory.size();

        // Если ускорение стабильно низкое - это планка
        return avg < 4.0f;
    }

    public int getSquatCount() {
        return squatCount;
    }

    public int getPushupCount() {
        return pushupCount;
    }

    public long getPlankTime() {
        return plankTime / 10; // в секундах
    }

    public void reset() {
        squatCount = 0;
        pushupCount = 0;
        plankTime = 0;
        accHistory.clear();
    }
}