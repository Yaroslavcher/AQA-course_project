package ru.netology.diplom.data;


import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class DataHelper {
    public static Faker faker = new Faker(new Locale("EN"));

    private DataHelper() {
    }

    public static Card getValidCard() {
        return new Card("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getDeclinedCard() {
        return new Card("4444 4444 4444 4442", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getFakeCard() {
        return new Card("4444 4444 4444 4443", getCurrentMonth(), getCurrentYear(), "Card Holder", "123");
    }

    public static Card getInvalidHolderCard() {
        return new Card("4444 4444 4444 4441", getCurrentMonth(), getCurrentYear(), "ЙцукенФыва", "123");
    }
    public static String plusMonth(int plusMonth) {
        return LocalDate.now().plusMonths(plusMonth).format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
    }

    public static String getCurrentYear() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getFirstCard() {
        return "4444 4444 4444 4441";
    }

    public static String getSecondCard() {
        return "4444 4444 4444 4442";
    }

    public static String getRandomNumbers(int number) {
        return faker.number().digits(number);
    }

    public static String invalidCard(int numbers) {
        return getRandomNumbers(numbers);
    }

    public static String invalidCardOneMinus() {
        return getRandomNumbers(15);
    }

    public static String oneZeroCard() {
        return "0";
    }

    public static String allZeroCard() {
        return "0000 0000 0000 0000";
    }

    public static String oneDigitCard() {
        return getRandomNumbers(1);
    }

    public static String getValidMonth(int plusMonth) {
        return plusMonth(plusMonth);
    }

    public static String getZeroMonth() {
        return "00";
    }


    public static String getInvalidMonth() {
        int month = parseInt(getCurrentMonth());
        return "2" + month;
    }

    public static String getInvalidYear() {
        int years = Integer.parseInt(getRandomNumbers(1));
        if (years == 0) {
            years = years + 1;
        }
        int year = parseInt(getCurrentYear()) - years;
        return String.valueOf(year);
    }

    public static String plusYears(int years) {
        return LocalDate.now().plusYears(years).format(DateTimeFormatter.ofPattern("yy"));
    }

    public static String getZeroYear() {
        return "00";
    }

    public static String oneDigitYear() {
        return getRandomNumbers(1);
    }

    public static String treeDigitYear() {
        return getRandomNumbers(3);
    }

    public static String getValidHolderName() {
        return faker.name().name();
    }

    public static String getHolderNamePlusDigits() {
        return faker.name().firstName() + getRandomNumbers(5);
    }

    public static String getHolderNamePlusSymbols() {
        return faker.name().name() + "~!@#$%";
    }

    public static String getNameNotEnglish() {

        return "Неанглийскими";
    }

    public static String getOneLetterName() {
        return faker.letterify("?");
    }

    public static String getLongName() {
        return String.valueOf(faker.lorem());
    }

    public static String getValidCVC() {
        return getRandomNumbers(3);
    }

    public static String getInvalidCVC() {
        return getRandomNumbers(2);
    }

}

