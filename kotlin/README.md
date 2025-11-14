# Введение

Котлин-версия учебного проекта демонстрирует применение Value Object (VO) в предметной области доставки. Код написан на Kotlin 1.9+/JDK 21 и организован по тем же правилам DDD и Clean Architecture, что и Java-ориентированный пример. Центральный VO — `Weight` (`domain/src/main/kotlin/dev/ufadevconf/domain/Weight.kt`), который хранит вес в граммах, валидирует вход, умеет конвертировать килограммы и предоставляет удобные методы сравнения. Многомодульная сборка Gradle включает:

- `domain` — чистая доменная модель (VO, сущности, репозитории) без инфраструктурных зависимостей.
- `application` — сервисы прикладного слоя и DTO-команды/запросы.
- `infrastructure` — адаптеры Spring Boot 3 (REST, in-memory репозиторий) и точка входа приложения.

# Начало работы

## Требования

- JDK 21
- Gradle Wrapper (поставляется в репозитории)

## Сборка и тесты

```bash
./gradlew clean test
```

Юнит-тесты находятся в модуле `domain` и покрывают VO/доменную логику.

## Запуск приложения

```bash
./gradlew :infrastructure:bootRun
```

Сервис запускается на `http://localhost:8080`.

# REST API

## Создать пункт выдачи

- **POST** `/api/pickup-points`
- Тело запроса:

```json
{
  "code": "SPB-101",
  "address": "Санкт-Петербург, Невский проспект, 1",
  "minWeightKg": 1.0,
  "maxWeightKg": 5.0
}
```

## Проверить, помещается ли отправление

- **POST** `/api/pickup-points/{code}/fit-check`
- Тело запроса:

```json
{
  "trackingNumber": "TRACK-1",
  "contentsDescription": "Документы",
  "parcelWeightGrams": 2300
}
```

В ответе приходит `fits=true/false`.

> Примечание: веса в килограммах принимаются только при создании пункта выдачи. Контроллер конвертирует их во внутренние граммы посредством VO `Weight`.

# Архитектура

- **Domain** — `Weight`, `Parcel`, `PickupPoint`, интерфейс `PickupPointRepository` и тесты `WeightTest`/`PickupPointTest`.
- **Application** — `PickupPointApplicationService` и DTO (`CreatePickupPointCommand`, `CheckParcelFitsQuery`, `ParcelFitResult`) + исключение `PickupPointNotFoundException`.
- **Infrastructure** — Spring Boot REST-контроллер, глобальный обработчик ошибок, in-memory репозиторий и конфигурация, которая собирает приложение.

Такое разделение позволяет переиспользовать домен и прикладной слой независимо от транспорта или базы данных.
