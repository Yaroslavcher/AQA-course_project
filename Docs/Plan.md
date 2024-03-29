## Автоматизация тестирования веб-формы сервиса продажи туров через интернет-банк
# 1. Тестовые сценарии
[Ссылка](https://docs.google.com/spreadsheets/d/1P-PjIUU3yXNJYhJvM8ZUqNgFNAz91ZCtcAcTBT-AIQE/edit?usp=sharing)

# 2. Используемые инструменты:
- доступ в Интернет,
- IBM-совместимый ПК с 64-разрядным ЦП,
- ОС - Windows 10 64-разрядная,
- браузер - Google Chrome,
- бесплатная коммерческая интегрированная среда разработки Java - IntelliJ IDEA 2022.2.1,
- пакет инструментов для разработки на Java - JDK 11,
- среда тестирования для приложений на Java - JUnit5,
- инструмент для написания UI-тестов - Selenide,
- система автоматической сборки проекта - Gradle,
- приложение для доставки и запуска контейнерного приложения (СУБД) под Windows - Docker Desktop,
- система контроля версий - Git,
- репозиторий для командной работы над проектом - Github.org

Обоснование выбора: поскольку project-manager не задал стек инструментов для данного проекта, я выбрал привычные в моей работе инструменты с поддержкой.

# 3. Возможные риски при автоматизации:
1. Искажение или потеря данных о платежах в БД в результате прогона тестов (из-за неизолированности тестов).
2. Проблемы с запуском приложения, подключением БД.
3. Некорректность тестовых данных при работе с Faker.
4. Низкая тестируемость селекторов из-за вероятного изменения структуры страницы (html, css).
Как следствие пп. 1-4: требуется пересчет (повышение) стоимости автоматизации и поддержания автотестов.


# 4. Интервальная оценка с учётом рисков в часах:
- разработка плана тестирования - 18 часов,
- подготовка необходимых инструментов, написание кода автотестов - 88 часов,
- подготовка отчетной документации - 18 часов,
- форс-мажор - 48 часов.

# 5. План сдачи работ:

- готовность автотестов - 8 рабочих дней после утверждения плана и разрешения вопросов,
- результат работы автотестов - документы (отчеты, репорты) по итогам тестирования - 5 рабочих дней,
- подготовка отчёта по автоматизации - 3 рабочих дня по окончании всех работ.
