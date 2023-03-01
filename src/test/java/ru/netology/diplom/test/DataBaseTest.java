package ru.netology.web.test;

import com.codeborne.selenide.logevents.SelenideLogger;
import ru.netology.web.data.Card;
import ru.netology.web.data.DataHelper;
import ru.netology.web.data.DataBase;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.*;
import ru.netology.web.page.CreditPage;
import ru.netology.web.page.PaymentPage;
import ru.netology.web.page.TravelPage;

import java.sql.SQLException;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DataBaseTest {

        Card validCard = DataHelper.getValidCard();
        Card declinedCard = DataHelper.getDeclinedCard();
        Card fakeCard = DataHelper.getFakeCard();

        @BeforeEach
        public void openPage() throws SQLException {
        //DbUtils.clearTables();
        String url = System.getProperty("sut.url");
        open(url);
        }

        @BeforeAll
        static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
        }

        @AfterAll
        static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        }

        @Test
        @DisplayName("Должен подтверждать покупку по карте со статусом APPROVED")
        void shouldConfirmPaymentWithValidCard() throws SQLException {
        TravelPage travelPage = new TravelPage();
        PaymentPage paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(validCard);
        paymentPage.successfulPayment();
        assertEquals("APPROVED", DataBase.findPaymentStatus());
        System.out.println(DataBase.findPaymentStatus());

        }

        @Test
        @DisplayName("Должен подтверждать кредит по карте со статусом APPROVED")
        void shouldConfirmCreditWithValidCard() throws SQLException {
        TravelPage travelPage = new TravelPage();
        CreditPage creditPage = travelPage.goToCreditPage();
        creditPage.inputCardData(validCard);
        creditPage.successfulCredit();
        assertEquals("APPROVED", DataBase.findCreditStatus());
        System.out.println(DataBase.findCreditStatus());
        }

        @Test
        @DisplayName("Не должен подтверждать покупку по карте со статусом DECLINED")
        void shouldNotConfirmPaymentWithDeclinedCard() throws SQLException {
        TravelPage travelPage = new TravelPage();
        PaymentPage paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(declinedCard);
        paymentPage.checkErrorMessageIsDisplayed();
        assertEquals("DECLINED", DataBase.findPaymentStatus());
        System.out.println(DataBase.findPaymentStatus());
        }

        @Test
        @DisplayName("Не должен подтверждать кредит по карте со статусом DECLINED")
        void shouldNotConfirmCreditWithDeclinedCard() throws SQLException {
        TravelPage travelPage = new TravelPage();
        CreditPage creditPage = travelPage.goToCreditPage();
        creditPage.inputCardData(declinedCard);
        creditPage.showErrorMessage();
        assertEquals("DECLINED", DataBase.findCreditStatus());
        System.out.println(DataBase.findCreditStatus());

        }

        @Test
        @DisplayName("Не должен подтверждать покупку по несуществующей карте")
        void shouldNotConfirmPaymentWithFakeCard() throws SQLException {
        DataBase.clearTables();
        TravelPage travelPage = new TravelPage();
        PaymentPage paymentPage = travelPage.goToPaymentPage();
        paymentPage.fillData(fakeCard);
        paymentPage.checkErrorMessageIsDisplayed();
        assertEquals("0", DataBase.countRecords());
        System.out.println(DataBase.countRecords());

        }

        @Test
        @DisplayName("Не должен подтверждать кредит по несуществующей карте")
        void shouldNotConfirmCreditWithFakeCard() throws SQLException {
        TravelPage travelPage = new TravelPage();
        CreditPage creditPage = travelPage.goToCreditPage();
        creditPage.inputCardData(fakeCard);
        creditPage.showErrorMessage();
        assertEquals("0", DataBase.countRecords());
        System.out.println(DataBase.countRecords());
        }
}
