package ru.netology.web.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$;

public class TravelPage {
    private SelenideElement heading = $$("h2").find(text("Путешествие дня"));
    private SelenideElement payButton = $$("button").find(exactText("Купить"));
    private SelenideElement creditButton = $$("button").find(exactText("Купить в кредит"));

    public TravelPage() {
        heading.shouldBe(visible);
    }

    public PaymentPage goToPaymentPage() {
        payButton.click();
        return new PaymentPage();
    }

    public CreditPage goToCreditPage() {
        creditButton.click();
        return new CreditPage();
    }
}
