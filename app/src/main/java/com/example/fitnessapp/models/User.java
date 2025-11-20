package com.example.fitnessapp.models;

import java.io.Serializable; //Импортируем интерфейс Serializable для сериализации объектов
import java.util.Objects; //Импортируем класс Objects для работы с объектами

public class User implements Serializable {
    private String id; //Уникальный идентификатор пользователя
    private String firstName; //Имя пользователя
    private String lastName; //Фамилия пользователя
    private String email; //Email пользователя
    private String additionalInfo; //Дополнительная информация о пользователе

    //Конструктор класса User, инициализирует поля объекта.
    public User(String id, String firstName, String lastName, String email, String additionalInfo) {
        this.id = id; //Устанавливает значение уникального идентификатора.
        this.firstName = firstName; // Устанавливает имя.
        this.lastName = lastName; //Устанавливает фамилию.
        this.email = email; //Устанавливает email.
        this.additionalInfo = additionalInfo; //Устанавливает дополнительную информацию.
    }

    //Геттер для дополнительной информации
    public String getAdditionalInfo() {
        return additionalInfo; //Возвращает значение дополнительной информации
    }

    //Сеттер для дополнительной информации
    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo; //Устанавливает новое значение для дополнительной информации
    }

    //Геттер для email
    public String getEmail() {
        return email; //Возвращает email пользователя
    }

    //Сеттер для email
    public void setEmail(String email) {
        this.email = email; //Устанавливает новое значение для email
    }

    //Геттер для уникального идентификатора.
    public String getId() {
        return id; //Возвращает уникальный идентификатор пользователя.
    }

    //Сеттер для уникального идентификатора.
    public void setId(String id) {
        this.id = id; //Устанавливает новое значение для уникального идентификатора
    }

    //Геттер для имени
    public String getFirstName() {
        return firstName; //Возвращает имя пользователя
    }

    //Сеттер для имени
    public void setFirstName(String firstName) {
        this.firstName = firstName; //Устанавливает новое значение для имени
    }

    //Геттер для фамилии
    public String getLastName() {
        return lastName; //Возвращает фамилию пользователя
    }

    //Сеттер для фамилии
    public void setLastName(String lastName) {
        this.lastName = lastName; //Устанавливает новое значение для фамилии
    }

    //Метод для получения полного имени пользователя
    public String getFullName() {
        return firstName + " " + lastName; //Возвращает полное имя пользователя, объединяя имя и фамилию
    }

    //Переопределяет метод equals для сравнения объектов User по их уникальному идентификатору
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; //Если ссылки совпадают, объекты равны
        if (o == null || getClass() != o.getClass()) return false; //Если о null или не тот класс, возвращает false
        User user = (User) o; //Приводит объект к типу User
        return Objects.equals(id, user.id); //Сравнивает уникальные идентификаторы пользователей
    }

    //Переопределяет метод hashCode для генерации хэш-кода на основе уникального идентификатора
    @Override
    public int hashCode() {
        return Objects.hash(id); //Возвращает хэш-код уникального идентификатора
    }
}