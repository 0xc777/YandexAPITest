![Allure Report](screenshots/allure-report.png)



## Технологии 
- Java 17
- Maven
- JUnit 5
- Allure Report

  
## Реализованные тесты

### Positive tests
- Получение информации о диске
- Создание папки
- Копирование папки
- Создание нескольких папок

### Negative tests
- Создание дубликата папки
- Копирование несуществующей папки
- Проверка невалидных endpoint

---

## Запуск тестов

```bash
mvn clean test
```

## Запуск тестов по тегам

### Positive tests

```bash
mvn test -Dgroups=positive
```

### Negative tests

```bash
mvn test -Dgroups=negative
```
---

## Генерация Allure отчета

```bash
allure serve target/allure-results
```

---

## Environment Variables

Для запуска необходимо указать токен
