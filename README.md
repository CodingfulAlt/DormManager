# DormManager - Cucumber BDD проект

Система за управление на студентско общежитие. BDD тестване чрез Cucumber + JUnit (Задача 2).

## Тествани функционалности (3)

1. **Кандидатстване за стая** (UC2) - `application.feature`
2. **Одобряване / отхвърляне на кандидатура** (UC6) - `approval.feature`
3. **Плащане на наем** (UC4) - `payment.feature`

Всяка функционалност има 5 сценария (позитивни и негативни), общо 15 сценария.

## Структура

```
src/main/java/uni/pu/fmi/
  model/      - User, Room, Application, RentPayment + enums
  repo/       - in-memory mock хранилища
  service/    - бизнес логика (ApplicationService, PaymentService, AuthService)
  exception/  - BusinessException
src/test/java/uni/pu/fmi/
  DormManagerSteps.java  - step definitions (Gherkin -> Java)
  RunCucumberTest.java   - JUnit runner
src/test/resources/features/
  application.feature
  approval.feature
  payment.feature
```

## Изпълнение

```bash
mvn test
```

Cucumber докладът се генерира в `target/cucumber-report.html`.

## Технологии

- Java 17
- Maven
- Cucumber 7.18.0 (cucumber-java, cucumber-junit)
- JUnit 4.13.2
- Mock данни (in-memory), без external API
