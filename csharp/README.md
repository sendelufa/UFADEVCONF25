# Введение

C#-версия повторяет учебный пример Value Object (VO) на современном .NET 8 и минимальных API. Центральный VO — `Weight` (`src/Domain/Weight.cs`), который хранит вес в граммах, валидирует вход, конвертирует килограммы и предоставляет удобные методы сравнения. Архитектура полностью зеркалирует Java-проект:

- `Domain` — чистая доменная модель (VO, сущности `Parcel`/`PickupPoint`, интерфейс `IPickupPointRepository`).
- `Application` — сервис прикладного слоя `PickupPointApplicationService` и DTO-команды/запросы/ответы + исключение `PickupPointNotFoundException`.
- `Infrastructure` — ASP.NET Core минимальный API, DI-конфигурация и in-memory репозиторий.
- `tests/Domain.Tests` — xUnit-тесты, демонстрирующие поведение VO и `PickupPoint`.

# Начало работы

## Требования

- .NET SDK 8.0+

## Сборка и тесты

```bash
cd csharp
dotnet restore
dotnet test
```

## Запуск HTTP API

```bash
cd csharp
dotnet run --project src/Infrastructure
```

Сервис стартует на `http://localhost:5000` (или `https://localhost:5001`).

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

Ответ:

```json
{
  "pickupPointCode": "SPB-101",
  "trackingNumber": "TRACK-1",
  "fits": true
}
```

> Вес в килограммах принимается только при создании пункта выдачи. Контроллер конвертирует значения во внутренние граммы через VO `Weight`.

# Архитектура

- **Domain** — не зависит от других проектов. VO реализован как `readonly record struct` с проверками и вспомогательными методами.
- **Application** — оркестрирует сценарии, опираясь на абстракции домена.
- **Infrastructure** — настраивает DI, REST-эндпойнты и хранение (in-memory). Слой легко заменить на БД/сообщения.

Таким образом, бизнес-логика остаётся чистой и переиспользуемой в любой инфраструктуре.
