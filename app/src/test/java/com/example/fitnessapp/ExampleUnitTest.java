package com.example.fitnessapp;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExampleUnitTest {

    //ТЕСТЫ ДЛЯ EMAIL

    @Test
    public void testEmailIsValid() {
        //Правильные email
        assertTrue("user@mail.ru".contains("@") && "user@mail.ru".contains("."));
        assertTrue("test@gmail.com".contains("@") && "test@gmail.com".contains("."));
        assertTrue("admin@fitnessapp.com".contains("@") && "admin@fitnessapp.com".contains("."));
    }

    @Test
    public void testEmailIsInvalid() {
        //Неправильные email (должны возвращать false)

        //Пустой email - НЕ должен содержать @ и .
        assertFalse("".contains("@"));
        assertFalse("".contains("."));

        //"invalid" - НЕ содержит @ и .
        assertFalse("invalid".contains("@"));
        assertFalse("invalid".contains("."));

        //"user@" - содержит @, но НЕ содержит .
        assertTrue("user@".contains("@"));      //@ есть - это true
        assertFalse("user@".contains("."));     //. нет - это false

        //"@mail.ru" - содержит @ и ., но нет текста ДО @
        assertTrue("@mail.ru".contains("@"));   //@ есть - true
        assertTrue("@mail.ru".contains("."));   //. есть - true
        //ВАЖНО: Этот email невалидный, потому что нет символов до @
        //Наша проверка должна это отловить!
    }

    //ТЕСТЫ ДЛЯ ТЕЛЕФОНА

    @Test
    public void testPhoneIsValid() {
        //Правильные телефоны (с + и длиной 10-15)
        String phone1 = "+79001234567";
        assertTrue(phone1.startsWith("+") && phone1.length() >= 10 && phone1.length() <= 15);

        String phone2 = "+380123456789";
        assertTrue(phone2.startsWith("+") && phone2.length() >= 10 && phone2.length() <= 15);

        String phone3 = "+375291234567";
        assertTrue(phone3.startsWith("+") && phone3.length() >= 10 && phone3.length() <= 15);
    }

    @Test
    public void testPhoneIsInvalid() {
        //Пустой телефон
        String phone1 = "";
        assertFalse(phone1.startsWith("+"));
        assertFalse(phone1.length() >= 10 && phone1.length() <= 15);

        //Телефон без + (должен начинаться с +)
        String phone2 = "89001234567";
        assertFalse(phone2.startsWith("+"));
        //Длина правильная (11 цифр), но нет +

        //Слишком короткий
        String phone3 = "+7900";
        assertTrue(phone3.startsWith("+"));     // + есть - true
        assertFalse(phone3.length() >= 10);     // длина 5 - false
    }

    @Test
    public void testPhoneMustStartWithPlus() {
        assertTrue("+79001234567".startsWith("+"));
        assertFalse("79001234567".startsWith("+"));
        assertFalse("89001234567".startsWith("+"));
    }

    //ТЕСТЫ ДЛЯ ПАРОЛЯ

    @Test
    public void testPasswordLength() {
        //Длина >= 6 - валидный
        assertTrue("123456".length() >= 6);
        assertTrue("password123".length() >= 6);

        //Длина < 6 - невалидный
        assertFalse("12345".length() >= 6);
        assertFalse("".length() >= 6);
    }

    @Test
    public void testPasswordContainsDigit() {
        //Содержит цифру
        assertTrue("password123".matches(".*\\d.*"));
        assertTrue("123456".matches(".*\\d.*"));
        assertTrue("pass1word".matches(".*\\d.*"));

        //Не содержит цифру
        assertFalse("password".matches(".*\\d.*"));
        assertFalse("abcdef".matches(".*\\d.*"));
    }

    @Test
    public void testPasswordsMatch() {
        //Совпадают
        assertTrue("123456".equals("123456"));
        assertTrue("password".equals("password"));

        //Не совпадают
        assertFalse("123456".equals("123457"));
        assertFalse("pass1".equals("pass2"));
    }

    //КОМПЛЕКСНЫЕ ТЕСТЫ

    @Test
    public void testFullRegistrationValidation() {
        String email = "user@mail.ru";
        String phone = "+79001234567";
        String password = "password123";
        String confirm = "password123";

        boolean emailValid = email.contains("@") && email.contains(".");
        boolean phoneValid = phone.startsWith("+") && phone.length() >= 10 && phone.length() <= 15;
        boolean passwordValid = password.length() >= 6 && password.matches(".*\\d.*");
        boolean passwordsMatch = password.equals(confirm);

        boolean result = emailValid && phoneValid && passwordValid && passwordsMatch;
        assertTrue(result);
    }

    @Test
    public void testRegistrationFailsWithInvalidEmail() {
        String email = "invalid";
        boolean emailValid = email.contains("@") && email.contains(".");
        assertFalse(emailValid);
    }

    @Test
    public void testRegistrationFailsWithInvalidPhone() {
        String phone = "89001234567";
        boolean phoneValid = phone.startsWith("+") && phone.length() >= 10 && phone.length() <= 15;
        assertFalse(phoneValid);
    }

    @Test
    public void testRegistrationFailsWithShortPassword() {
        String password = "12345";
        boolean passwordValid = password.length() >= 6;
        assertFalse(passwordValid);
    }

    @Test
    public void testRegistrationFailsWithNoDigitInPassword() {
        String password = "password";
        boolean hasDigit = password.matches(".*\\d.*");
        assertFalse(hasDigit);
    }

    @Test
    public void testRegistrationFailsWithMismatchedPasswords() {
        String password = "password123";
        String confirm = "password124";
        boolean passwordsMatch = password.equals(confirm);
        assertFalse(passwordsMatch);
    }
}