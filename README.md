## Запуск тестов

### Предусловия
    установлены:
      * Docker
      * Git
      * Java 11
    скачан образ MySQL или PostgreSQL.

* склонировать репозиторий - команда `git clone`
* запустить контейнер с MySQL - `docker-compose up -d --build`
* запустить приложение, работающее с MySQL, - `java -jar aqa-shop.jar -P:jdbc.url=jdbc:mysql://localhost:3306/database -P:jdbc.user=user -P:jdbc.password=password`
    ```
    или
    ```
* запустить приложение, работающее с PostgreSQL, - `java -jar aqa-shop.jar -P:jdbc.url=jdbc:postgresql://localhost:5432/database -P:jdbc.user=user -P:jdbc.password=password`

    ```
* запустить тесты с Allure:
    * для запуска с MySQL:
   ```
   gradlew -Ddb.url=jdbc:mysql://localhost:3306/database clean test
   ```
    * для запуска с PostgreSQL:
   ```
   gradlew -Ddb.url=jdbc:postgresql://localhost:5432/database clean test
   ```
* по умолчанию приложение открыто по http://localhost:8080/ Для изменения адреса в команду запуска тестов добавить `-Dsut.url=<адрес>`
* для подключения с другим именем пользователя и паролем к БД в команду запуска тестов добавить `-Ddb.user=<имя_пользователя>` и `-Ddb.password=<пароль>`
* получить отчет о тестировании (Allure) - `gradlew allureServe`
* по окончании тестов закрыть приложение (Ctrl+C)
* остановить контейнер - `docker-compose down`
