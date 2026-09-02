
# 🧪 Автотесты для api Яндекс.Диска

[![CI](https://github.com/0xc777/YandexAPITest/actions/workflows/ci.yml/badge.svg)](https://github.com/0xc777/YandexAPITest/actions/workflows/ci.yml)
[![Allure Reports](https://img.shields.io/badge/Allure_Reports-View-1e90ff)](https://0xc777.github.io/YandexAPITest/)

### 📊Allure Reports📊
- [Главная страница со всеми отчётами](https://0xc777.github.io/YandexAPITest/)
- [Smoke-тесты](https://0xc777.github.io/YandexAPITest/allure-report-smoke/)
- [Positive-тесты](https://0xc777.github.io/YandexAPITest/allure-report-positive/)
- [Negative-тесты](https://0xc777.github.io/YandexAPITest/allure-report-negative/)

## 📖 О проекте 

Проект представляет собой специализированный фреймворк автоматизированного тестирования публичного REST API Яндекс.Диска.

Тесты написаны на Java с использованием REST Assured и JUnit 5. В проекте реализованы позитивные, негативные и smoke-сценарии, параметризованные тесты, обработка асинхронных операций, изолированное тестирование с использованием WireMock и автоматический запуск тестов в CI/CD.

Проект поддерживает два режима работы:
- **Real API** — выполнение тестов с использованием API Яндекс.Диска;
- **Mock API** — выполнение тестов с использованием WireMock для изолированного окружения.

### 🚀 Что умеют тесты

| Категория | Что проверяется | Ожидаемый статус |
|:----------|:----------------|:-----------------|
| **Smoke** | Авторизация, получение информации о диске, создание/удаление папки | `200`, `201`, `204` |
| **Позитивные** | Публикация, копирование, перемещение, загрузка по URL, корзина | `200`, `201`, `202`, `204` |
| **Параметризованные позитивные** | Кириллица, пробелы, разные расширения, разные имена | `201` |
| **Негативные** | Авторизация, валидация, ресусурс не обнаружен, крнфликт  | `401`, `400`, `404`, `409`|
| **Параметризованные негативные** | Невалидные, Неподдерживаемые методы `overwrite`, `limit`, `offset` | `400` `405` |

---
## 📁 Структура проекта
```text
src
└── test
    └── java
        └── api
            ├── client
            ├── constants
            ├── mocks
            │   └── wiremock
            ├── tests
            │   ├── assertions
            │   ├── dto
            │   ├── negative
            │   ├── positive
            │   ├── smoketests
            │   └── steps
            │       ├── assured
            │       └── interfaces
            └── utils
``` 
---
## 🛠 Стек технологий

| Технология | Версия | Назначение |
|:-----------|:-------|:-----------|
| **Java** | 17+ | Язык программирования |
| **REST Assured** | 5.5.0 | Тестирование REST api |
| **JUnit 5** | 5.10.2 | Фреймворк для модульного тестирования |
| **Allure** | 2.29.0 | Генерация красивых отчётов |
| **Awaitility** | 4.2.0 | Ожидание асинхронных операций |
| **WireMock** | — | Изолированное тестирование API |
| **Jackson** | 2.18.2 | Сериализация/десериализация JSON |
| **Maven** | — | Сборка проекта |
| **GitHub Actions** | — | CI/CD для автоматического запуска тестов |
---
## ⚙️ CI/CD

Для автоматизации запуска используется GitHub Actions.

**Workflow запускается:**

- при push в ветки main и develop;
- при создании Pull Request в main;
- вручную через workflow_dispatch.

### Матрица тестов
Тесты запускаются параллельно в трёх группах:

- ┌─ Smoke
- ├─ Positive
- └─ Negative

Для каждой группы создаётся отдельный matrix job.

### После завершения тестов отдельный job:

publish-reports собирает Allure-отчёты и публикует их на GitHub Pages.

Что делает pipeline:
- 1.Клонирует репозиторий.
- 2.Устанавливает JDK 17.
- 3.Использует Maven cache.
- 4.Передаёт OAuth-токен через GitHub Secrets.
- 5.Запускает выбранную группу API-тестов.
- 6.Генерирует Allure-отчёт.
- 7.Сохраняет отчёт как artifact.
- 8.Публикует отчёты на GitHub Pages.


Файл конфигурации: .github/workflows/ci.yml
## 🔧 Подготовка к запуску

### 1. Переменные окружения
Для работы тестов требуется **OAuth-токен** Яндекс.Диска.

**Получить токен можно здесь:**  
👉 (https://yandex.ru/dev/disk/poligon/)

После получения токена добавьте его в переменные окружения:
bash(**ОТ ИМЕНИ АДМИНИСТРАТОРА**)
- export YANDEX_TOKEN="ваш_токен"
- IDEA: Run → Edit Configurations → Environment variables → добавить YANDEX_TOKEN=ваш_токен

## 🔧 Установка зависимостей
mvn clean install
## 🚀 Запуск тестов
### Запуск всех тестов(Через  IntelliJ IDEA)
- ALT+F12
- mvn clean test
### Запуск по группам
### 💨Только дымовые тесты
mvn test -Dgroups="Smoke"

### ✅Только позитивные тесты  
mvn test -Dgroups="positive"

### 🛑Только негативные тесты
mvn test -Dgroups="negative"

### Mock-режим

**Для запуска тестов с использованием WireMock используется Maven profile mock**
#### mvn clean test -Pmock

## 📊 Генерация Allure-отчёта
После выполнения тестов сгенерируйте красивый отчёт:

### 📄Сгенерировать отчёт
mvn allure:report

### 🌐Открыть отчёт в браузере
mvn allure:serve

**Отчёт доступен в папке target/site/allure-maven-plugin/.**






