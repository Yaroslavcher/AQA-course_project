package ru.netology.diplom.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.diplom.data.Card;
import ru.netology.diplom.data.DataBase;
import ru.netology.diplom.page.CreditPage;
import ru.netology.diplom.page.TravelPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.diplom.data.DataHelper.*;

public class CreditCardTest {

    String url = System.getProperty("sut.url");
    @BeforeEach
    void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @BeforeEach
    public void setUp() {
        Configuration.headless = true;
        open(url);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/invalidCardData.csv", numLinesToSkip = 1)
    @DisplayName("Должен показывать сообщение об ошибке при заполнении полей невалидными значениями")
    void shouldShowWarningIfValueIsIncorrectForCredit(String number, String month, String year, String holder, String cvc, String message) {
        Card incorrectValues = new Card(number, month, year, holder, cvc);
        TravelPage travelPage = new TravelPage();
        CreditPage creditPage = travelPage.goToCreditPage();
        creditPage.fillData(incorrectValues);
        assertTrue(creditPage.showErrorMessage(), message);
    }

    @Test
    void shouldPayByApprovedCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(4), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.successfulCredit();
        String actual = DataBase.getStatusCredit();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldCheckApprovedCardExpired() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getCurrentMonth(), getCurrentYear(), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        String actual = DataBase.getStatusCredit();
        creditPage.successfulCredit();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldCheckDeclinedCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getSecondCard(), getValidMonth(0), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        String actual = DataBase.getStatusCredit();
        creditPage.invalidPaymentCreditCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldCheckDeclinedCardExpired() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getSecondCard(), getCurrentMonth(), getCurrentYear(), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        String actual = DataBase.getStatusCredit();
        creditPage.invalidPaymentCreditCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldCheckInvalidCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                invalidCard(14), getValidMonth(2), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckShortCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                invalidCardOneMinus(), getValidMonth(5), plusYears(3), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckOneZeroCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                oneZeroCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckAllZeroCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                allZeroCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.invalidPaymentCreditCard();
    }

    @Test
    void shouldCheckOneDigitCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                oneDigitCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckExpiredCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(-1), plusYears(-1), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkCardExpired();
    }

    @Test
    void shouldCheckIncorrectExpiredCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(-1), getCurrentYear(), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckMoreThanFiveYearsCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getCurrentMonth(), plusYears(6), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckOneDigitYearCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), oneDigitYear(), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckNameNotEnglish() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(3), getNameNotEnglish(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidHolder();
    }

    @Test
    void shouldCheckLongName() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(3), getLongName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidHolder();
    }

    @Test
    void shouldCheckNameWithDigits() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(4), plusYears(3),
                getHolderNamePlusDigits(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.incorrectHolder();
    }

    @Test
    void shouldCheckOneLetterName() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(9), plusYears(3),
                getOneLetterName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.incorrectHolder();
    }

    @Test
    void shouldCheckNamePlusSymbols() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(1),
                getHolderNamePlusSymbols(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.incorrectHolder();
    }

    @Test
    void shouldCheckInvalidCardValidityPeriod() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getInvalidMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckZeroMonth() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getZeroMonth(), plusYears(4), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckEmptyFieldForCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                null, getValidMonth(1), plusYears(2), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForMonth() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), null, plusYears(2), getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForYear() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), null, getValidHolderName(), getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForHolder() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), null, getValidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkEmptyField();
    }

    @Test
    void shouldCheckEmptyFieldForCvc() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), null);
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkEmptyField();
    }

    @Test
    void shouldCheckAllEmptyFields() {
        var travelPage = new TravelPage();
        Card card = new Card(
                null, null, null, null, null);
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkAllFieldsAreRequired();
    }
    @Test
    void shouldCheckInvalidCvcFormat() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), getInvalidCVC());
        var creditPage = travelPage.goToCreditPage();
        creditPage.fillData(card);
        creditPage.checkInvalidFormat();
    }
}