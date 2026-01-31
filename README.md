# Supermarket API

Proyecto full‑stack con foco en backend, diseñado para demostrar prácticas profesionales de desarrollo backend, CI/CD y despliegue en producción.

Este repositorio sigue un enfoque **monorepo**, conteniendo una API backend y un frontend mínimo utilizado únicamente para mostrar el funcionamiento de la API.

---

## Objetivos del Proyecto

* Construir un backend limpio y bien estructurado usando Spring Boot
* Aplicar principios de diseño de dominio a una escala realista
* Practicar flujos de trabajo profesionales con Git (branches, PRs, CI/CD)
* Desplegar y operar el sistema en producción

Este proyecto es **intencionalmente backend‑first**. La infraestructura y DevOps se mantienen mínimos y pragmáticos.

---

## Stack Tecnológico

### Backend

* Java 21
* Spring Boot
* Maven
* MySQL
* JUnit + Mockito

### Frontend

* HTML / CSS / JavaScript plano
* UI mínima para interactuar con la API

### Infraestructura

* Fly.io (backend y frontend desplegados como apps separadas)
* GitHub Actions (CI/CD)

---

## Estructura del Repositorio

```text
supermarket/
├── supermarket-api/        # Backend Spring Boot
├── supermarket-frontend/   # Frontend estático
├── docker-compose.yml      # Soporte para desarrollo local
└── .github/workflows/      # Pipelines de CI/CD
```

---

## Backend

El backend expone una API REST para gestionar ventas de supermercado, productos y reglas de negocio.

Características principales:

* Modelo de dominio claro
* Estados de ciclo de vida explícitos (por ejemplo: Sale OPEN / FINISHED / CANCELLED)
* Excepciones de dominio personalizadas
* Manejo global de errores
* Tests automatizados mínimos pero significativos

---

## Frontend

El frontend es intencionalmente simple y existe únicamente para:

* Demostrar la funcionalidad del backend
* Proveer una interfaz básica para pruebas manuales

No se utilizan frameworks de frontend.

---

## Entornos y Perfiles

El backend utiliza perfiles de Spring para separar responsabilidades:

* `dev` – desarrollo local
* `test` – tests automatizados
* `prod` – producción (Fly.io)

Cada perfil utiliza su propia configuración y base de datos.

---

## CI/CD

Este proyecto utiliza GitHub Actions con un **flujo basado en Pull Requests**:

* Los Pull Requests ejecutan CI (build + tests)
* La rama `main` está protegida
* El merge a `main` dispara el despliegue automático

Los pipelines están aislados:

* Los workflows del backend solo corren ante cambios en el backend
* Los workflows del frontend solo corren ante cambios en el frontend

---

## Despliegue

* Backend y frontend se despliegan como **aplicaciones separadas en Fly.io**
* Los despliegues son completamente automáticos vía GitHub Actions
* No se requieren despliegues manuales una vez configurado el pipeline

---

## Estado

Proyecto en mantenimiento activo como proyecto de aprendizaje y portfolio.

Focos principales:

* Corrección del backend
* Arquitectura limpia
* Preparación para producción

---

## Autor

Desarrollador backend enfocado en Java, Spring Boot y prácticas de ingeniería del mundo real.
