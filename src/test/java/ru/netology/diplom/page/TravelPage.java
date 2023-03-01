package ru.netology.diplom.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class TravelPage {
    private SelenideElement headingMain = $("h2.heading");
    private SelenideElement paymentButton = $$(".button").find(exactText("Купить"));
    private SelenideElement creditPay = $$(".button").find(exactText("Купить в кредит"));

    public TravelPage() {
        headingMain.should(visible);
    }

    public PaymentPage goToPaymentPage() {
        paymentButton.click();
        return new PaymentPage();
    }

    public CreditPage goToCreditPage() {
        creditPay.click();
        return new CreditPage();
    }
}



