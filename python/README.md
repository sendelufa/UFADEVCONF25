# Введение

Python-версия повторяет учебный пример Value Object из Java-проекта. Центральный VO — `Weight` (`src/vo_ufadevconf/domain/weight.py`), который хранит вес в граммах, валидирует ввод и умеет переводить килограммы в граммы. Архитектура разделена на три слоя в духе DDD/Clean Architecture:

- `vo_ufadevconf.domain` — чистая доменная модель (VO, сущности, репозиторий) без зависимостей от инфраструктуры.
- `vo_ufadevconf.application` — прикладной сервис и DTO-команды/запросы/ответы.
- `vo_ufadevconf.infrastructure` — HTTP-адаптер FastAPI, глобальный обработчик ошибок, in-memory репозиторий и точка входа.

# Начало работы

## Требования

- Python 3.14+
- [uv](https://github.com/astral-sh/uv) или `pip` для установки зависимостей

## Установка

```bash
uv sync
```

## Сборка и тесты

```bash
uv run pytest
```

Тесты находятся в `tests/domain` и покрывают доменные правила (`Weight`, `PickupPoint`).

## Запуск HTTP API

```bash
uv run uvicorn vo_ufadevconf.infrastructure.main:app --reload
```

Приложение будет доступно на `http://localhost:8000`.

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

Ответ: `{ "pickupPointCode": "SPB-101", "trackingNumber": "TRACK-1", "fits": true }`.

> Примечание: килограммы принимаются только в запросе создания пункта выдачи. Контроллер переводит их во внутренние граммы через VO `Weight`.

# Архитектура

- **Domain** — `Weight`, `Parcel`, `PickupPoint` и интерфейс `PickupPointRepository`. Слой ничего не знает об инфраструктуре.
- **Application** — `PickupPointApplicationService`, DTO (`CreatePickupPointCommand`, `CheckParcelFitsQuery`, `ParcelFitResult`) и исключение `PickupPointNotFoundError`.
- **Infrastructure** — FastAPI-роутер, in-memory репозиторий и провайдер зависимостей.

Такое разделение позволяет переиспользовать бизнес-логику с любыми адаптерами (CLI, очереди, разные БД и т.д.).
