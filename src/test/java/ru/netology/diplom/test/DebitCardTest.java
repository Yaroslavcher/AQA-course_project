package ru.netology.diplom.test;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import ru.netology.diplom.data.Card;
import ru.netology.diplom.data.DataBase;
import ru.netology.diplom.page.PaymentPage;
import ru.netology.diplom.page.TravelPage;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.netology.diplom.data.DataHelper.*;

public class DebitCardTest {
    String url = System.getProperty("sut.url");

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide().screenshots(true).savePageSource(false));
    }

    @SneakyThrows
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
    @DisplayName("Должно отображаться сообщение об ошибке при заполнении полей невалидными значениями")
    void shouldShowWarningIfValueIsIncorrectForPayment(String number, String month, String year, String holder, String cvc, String message) {
        Card incorrectValuesCard = new Card(number, month, year, holder, cvc);
        TravelPage travelPage = new TravelPage();
        PaymentPage paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(incorrectValuesCard);
        assertTrue(paymentPage.showErrorMessage(), message);
    }

    @Test
    void shouldPayByApprovedCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.successfulPayment();
        String actual = DataBase.getStatusPayment();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldCheckApprovedCardExpired() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getZeroMonth(), getZeroYear(), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkCardExpired();
        String actual = DataBase.getStatusPayment();
        assertEquals("APPROVED", actual);
    }

    @Test
    void shouldCheckDeclinedCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getSecondCard(), getCurrentMonth(), plusYears(5), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        String actual = DataBase.getStatusPayment();
        paymentPage.invalidPaymentDebitCard();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldCheckDeclinedCardExpired() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getSecondCard(), getValidMonth(1), getZeroYear(), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        String actual = DataBase.getStatusPayment();
        paymentPage.checkCardExpired();
        assertEquals("DECLINED", actual);
    }

    @Test
    void shouldCheckInvalidCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                invalidCard(16), getValidMonth(2), plusYears(3), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.invalidPaymentDebitCard();
    }

    @Test
    void shouldCheckShortCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                invalidCardOneMinus(), getValidMonth(5), plusYears(3), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckOneZeroCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                oneZeroCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckAllZeroCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                allZeroCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.invalidPaymentDebitCard();
    }

    @Test
    void shouldCheckOneDigitCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                oneDigitCard(), getValidMonth(3), plusYears(1), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckExpiredCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(10), plusYears(-1), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkCardExpired();
    }

    @Test
    void shouldCheckIncorrectExpiredCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(-1), getCurrentYear(), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckMoreThanFiveYearsCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getCurrentMonth(), plusYears(6), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckOneDigitYearCard() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), oneDigitYear(), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();

    }

    @Test
    void shouldCheckNameNotEnglish() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(3), getNameNotEnglish(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidOwner();
    }

    @Test
    void shouldCheckLongName() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(3), getLongName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidOwner();
    }

    @Test
    void shouldCheckNameWithDigits() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(4), plusYears(3),
                getHolderNamePlusDigits(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.incorrectOwner();
    }

    @Test
    void shouldCheckOneLetterName() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(9), plusYears(3),
                getOneLetterName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.incorrectOwner();
    }

    @Test
    void shouldCheckNamePlusSymbols() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(1), plusYears(1),
                getHolderNamePlusSymbols(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.incorrectOwner();
    }

    @Test
    void shouldCheckInvalidCardValidityPeriod() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getInvalidMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidCardValidityPeriod();
    }

    @Test
    void shouldCheckZeroMonth() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getZeroMonth(), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForCardNumber() {
        var travelPage = new TravelPage();
        Card card = new Card(
                null, getValidMonth(1), plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForMonth() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), null, plusYears(2), getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForYear() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), null, getValidHolderName(), getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }

    @Test
    void shouldCheckEmptyFieldForHolder() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), null, getValidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldCheckEmptyFieldForCvc() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), null);
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkEmptyField();
    }

    @Test
    void shouldCheckAllEmptyFields() {
        var travelPage = new TravelPage();
        Card card = new Card(
                null, null, null, null, null);
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkAllFieldsAreRequired();
    }

    @Test
    void shouldCheckInvalidCvcFormat() {
        var travelPage = new TravelPage();
        Card card = new Card(
                getFirstCard(), getValidMonth(2), plusYears(3), getValidHolderName(), getInvalidCVC());
        var paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(card);
        paymentPage.checkInvalidFormat();
    }
}
