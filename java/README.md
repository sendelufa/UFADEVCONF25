# Введение

Учебный проект демонстрирует применение Value Object (VO) в предметной области доставки с использованием Java 21, DDD и Clean Architecture. Центральный VO — `Weight` ([domain/src/main/java/dev/ufadevconf/domain/Weight.java](domain/src/main/java/dev/ufadevconf/domain/Weight.java)), который обеспечивает валидацию, преобразование килограмм в граммы и удобные методы сравнения. Код разделён на три модуля Gradle:

- `domain` — чистая доменная модель (VO, сущности, репозитории).
- `application` — сервисы прикладного слоя, реализующие сценарии использования.
- `infrastructure` — адаптеры (REST, in-memory репозиторий) и точка входа Spring Boot 3.x.

Проект использует только необходимые зависимости: Spring Boot в инфраструктуре и Lombok в моделях, чтобы убрать шаблонный код, но сохранить читаемую архитектуру.

# Начало работы

## Требования

- JDK 21
- Gradle Wrapper (идёт в репозитории)

## Сборка и тесты

```bash
./gradlew clean test
```

Тесты есть только в модуле `domain`, т.к. инфраструктура по условию не покрывается тестами.

## Запуск приложения

```bash
./gradlew :infrastructure:bootRun
```

Приложение стартует на `http://localhost:8080`.

# REST API

## Создать пункт выдачи

- **POST** `/api/pickup-points`
- Пример запроса:

```json
{
  "code": "SPB-101",
  "address": "Санкт-Петербург, Невский проспект, 1",
  "minWeightKg": 1.0,
  "maxWeightKg": 5.0
}
```

`curl`:

```bash
curl -X POST http://localhost:8080/api/pickup-points \
  -H "Content-Type: application/json" \
  -d '{
        "code": "SPB-101",
        "address": "Санкт-Петербург, Невский проспект, 1",
        "minWeightKg": 1.0,
        "maxWeightKg": 5.0
      }'
```

## Проверить, помещается ли отправление

- **POST** `/api/pickup-points/{code}/fit-check`
- Пример запроса:

```json
{
  "trackingNumber": "TRACK-1",
  "contentsDescription": "Документы",
  "parcelWeightGrams": 2300
}
```

`curl`:

```bash
curl -X POST http://localhost:8080/api/pickup-points/SPB-101/fit-check \
  -H "Content-Type: application/json" \
  -d '{
        "trackingNumber": "TRACK-1",
        "contentsDescription": "Документы",
        "parcelWeightGrams": 2300
      }'
```

В ответе возвращается `fits=true/false`.

> Примечание: только запрос создания пункта выдачи принимает вес в килограммах. Внутри адаптера значения конвертируются в граммы через VO `Weight`.

# Архитектура

- **Domain** — `Weight` (VO), `Parcel`, `PickupPoint`, интерфейс `PickupPointRepository`. Никаких зависимостей от инфраструктуры. Примеры использования VO см. в тестах [domain/src/test/java/dev/ufadevconf/domain/WeightTest.java](domain/src/test/java/dev/ufadevconf/domain/WeightTest.java) и боевом коде `Parcel`/`PickupPoint`.
- **Application** — `PickupPointApplicationService` и DTO-команды/запросы. Работает только с абстракциями домена.
- **Infrastructure** — адаптеры Spring Boot: REST-контроллер, обработчик ошибок, in-memory реализация репозитория и точка входа.

Такой подход позволяет заменять инфраструктуру (БД, транспорт) без изменения бизнес-логики.
