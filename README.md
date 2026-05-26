# FreshSense — Backend

Backend del sistema FreshSense. API REST construida con Spring Boot 3.5.7 y Java 17, siguiendo Domain-Driven Design (DDD) con un monolito modular orientado a migración futura a microservicios.

---

## Requisitos

| Herramienta | Version minima |
|-------------|----------------|
| Java        | 17             |
| Maven       | 3.9+ (incluido via wrapper `./mvnw`) |
| MySQL       | 8.0            |

---

## Configuracion local

### 1. Base de datos

Crear la base de datos en MySQL antes de levantar el servidor:

```sql
CREATE DATABASE freshsense_db;
```

### 2. Variables de entorno (archivo local)

Crear el archivo `src/main/resources/application-local.properties` (no esta en el repositorio, esta en `.gitignore`).

La estructura del archivo es la siguiente:

```properties
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=
JWT_SECRET=
AES_SECRET=
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```

Los valores de estas variables no se publican en el repositorio por razones de seguridad. Contactar al lider del proyecto para recibirlos de forma privada antes de levantar el entorno local.

Para obtener las credenciales de Google (`GOOGLE_CLIENT_ID` y `GOOGLE_CLIENT_SECRET`), el lider del proyecto debe crearlas en Google Cloud Console bajo el proyecto de FreshSense y compartirlas junto con las demas variables.

Las variables de entorno disponibles y sus valores por defecto son:

| Variable             | Descripcion                                      | Default en produccion |
|----------------------|--------------------------------------------------|-----------------------|
| `DB_HOST`            | Host de la base de datos                         | —                     |
| `DB_PORT`            | Puerto de la base de datos                       | —                     |
| `DB_NAME`            | Nombre de la base de datos                       | —                     |
| `DB_USER`            | Usuario de MySQL                                 | —                     |
| `DB_PASSWORD`        | Password de MySQL                                | —                     |
| `JWT_SECRET`         | Clave para firmar JWT (minimo 32 caracteres)     | —                     |
| `AES_SECRET`         | Clave AES-256 para cifrado en reposo (32+ chars) | —                     |
| `JWT_ACCESS_MINUTES`   | Duracion del access token en minutos              | `15`                  |
| `JWT_REFRESH_DAYS`     | Duracion del refresh token en dias                | `7`                   |
| `PORT`                 | Puerto HTTP del servidor                          | `8080`                |
| `GOOGLE_CLIENT_ID`     | Client ID de Google OAuth2                        | —                     |
| `GOOGLE_CLIENT_SECRET` | Client Secret de Google OAuth2                    | —                     |
| `FRONTEND_URL`         | URL base del frontend (redirect post-OAuth2)      | `http://localhost:4200` |

En produccion (Railway, Render, etc.) se usan las variables `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD` que provee la plataforma automaticamente.

---

## Levantar el servidor

**Git Bash / Linux / Mac:**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

**Windows CMD:**
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

**IntelliJ IDEA:**
Edit Configurations → Active profiles → `local` → Run.

El servidor inicia en `http://localhost:8080`.

---

## Documentacion de la API

Swagger UI disponible en:

```
http://localhost:8080/swagger-ui/index.html
```

La especificacion OpenAPI (JSON) esta en:

```
http://localhost:8080/v3/api-docs
```

---

## OAuth2 — Login con Google

El backend soporta autenticacion via Google OAuth2 ademas del login tradicional con email/password.

**Flujo:**
1. El frontend redirige al usuario a `http://localhost:8080/oauth2/authorization/google`
2. Google autentica al usuario y redirige de vuelta al backend
3. El backend crea o recupera el usuario en la BD con rol `USER_STANDARD`
4. Se emiten las cookies HttpOnly `authToken` y `refreshToken` exactamente igual que en el login tradicional
5. El usuario es redirigido al frontend en `{FRONTEND_URL}/home`

**Configuracion necesaria en Google Cloud Console:**
- Crear un proyecto en [Google Cloud Console](https://console.cloud.google.com)
- Habilitar la API de Google+ o People API
- Crear credenciales OAuth2 de tipo "Aplicacion web"
- Agregar como URI de redireccion autorizado: `http://localhost:8080/login/oauth2/code/google`
- Copiar el Client ID y Client Secret a las variables de entorno `GOOGLE_CLIENT_ID` y `GOOGLE_CLIENT_SECRET`

**Nota:** Si `GOOGLE_CLIENT_ID` esta vacio, el endpoint OAuth2 no estara disponible pero el resto del sistema funciona normalmente.

---

## Modulos

La aplicacion esta organizada en bounded contexts. Cada modulo sigue la misma estructura de capas:

```
{modulo}/
├── application/
│   ├── dto/        # DTOs de entrada y salida
│   ├── mapper/     # Mappers entidad <-> DTO
│   └── service/    # Logica de negocio
├── domain/
│   ├── model/      # Entidades de dominio (POJOs)
│   └── repository/ # Interfaces de repositorio
└── infrastructure/
    ├── persistence/ # Entidades JPA, adapters, configuracion
    └── web/         # Controladores REST
```

### Endpoints por modulo

| Modulo          | Base URL                           | Descripcion                                      |
|-----------------|------------------------------------|--------------------------------------------------|
| `accounts`      | `/api/accounts`                    | Registro, login, logout, refresh token, perfil  |
| `inventory`     | `/api/products`                    | CRUD de productos en inventario                  |
| `alerts`        | `/api/alerts`                      | Alertas por vencimiento o stock bajo             |
| `monitoring`    | `/api/monitoring`                  | Registro de lecturas de sensores IoT             |
| `recipes`       | `/api/recipes`                     | Recetas sugeridas; `/premium` requiere suscripcion |
| `reports`       | `/api/history`                     | Historial de consumo; `/advanced` requiere premium |
| `billing`       | `/api/billing`                     | Planes y suscripciones                           |
| `notifications` | `/api/notifications`               | Bandeja in-app, preferencias, envio (solo ADMIN) |
| `achievements`  | `/api/users/{userId}/achievements` | Logros y gamificacion por usuario                |
| `challenges`    | `/api/challenges`                  | Retos, enroll y leaderboard                      |

---

## Seguridad

### Autenticacion

- JWT stateless. El token viaja en cookie HttpOnly `authToken` (preferido) o en el header `Authorization: Bearer <token>`.
- Al hacer login o registro se setean dos cookies:
  - `authToken` — access token, expira en 15 minutos.
  - `refreshToken` — refresh token, expira en 7 dias, path `/api/accounts/refresh`.
- El endpoint `POST /api/accounts/refresh` renueva el access token sin necesidad de re-autenticacion.

### Roles

| Rol            | Descripcion                                                           |
|----------------|-----------------------------------------------------------------------|
| `USER_FREE`    | Usuario registrado sin suscripcion activa                             |
| `USER_PREMIUM` | Usuario con suscripcion activa; accede a analytics y recetas premium  |
| `ADMIN`        | Administrador; puede crear recetas y enviar notificaciones            |

El rol se actualiza automaticamente mediante eventos de dominio cuando el usuario activa o cancela una suscripcion.

### Endpoints publicos (sin autenticacion)

- `POST /api/accounts/register`
- `POST /api/accounts/login`
- `POST /api/accounts/logout`
- `POST /api/accounts/refresh`
- `GET /api/billing/plans`
- `GET /api/recipes`, `GET /api/recipes/{id}`
- `/v3/api-docs/**`, `/swagger-ui/**`

### Cifrado en reposo

El campo `paymentReference` en suscripciones esta cifrado con AES-256-GCM mediante un `JPA AttributeConverter`. La clave se configura con la variable `AES_SECRET`.

### Rate limiting

Se aplica rate limiting en los endpoints de autenticacion mediante Bucket4j para prevenir fuerza bruta.

---

## Eventos de dominio

Los modulos se comunican de forma desacoplada mediante `Spring ApplicationEventPublisher`. Eventos implementados:

| Evento                        | Publicado por | Consumido por     | Efecto                              |
|-------------------------------|---------------|-------------------|-------------------------------------|
| `UserRegisteredEvent`         | `accounts`    | —                 | Log / extension futura              |
| `UserRoleChangedEvent`        | `accounts`    | —                 | Auditoria de cambios de rol         |
| `SubscriptionActivatedEvent`  | `billing`     | `accounts` (RoleService) | Upgrade a `USER_PREMIUM`   |
| `SubscriptionCancelledEvent`  | `billing`     | `accounts` (RoleService) | Downgrade a `USER_FREE`    |
| `ExpirationAlertRaisedEvent`  | `alerts`      | `notifications`   | Envia notificacion in-app al usuario |

---

## Comandos utiles

```bash
# Compilar sin ejecutar tests
./mvnw clean package -DskipTests

# Ejecutar tests
./mvnw test

# Build de produccion
./mvnw clean package

# Limpiar target
./mvnw clean
```

---

## Estructura de archivos relevantes

```
src/main/resources/
├── application.properties            # Configuracion base (variables de entorno)
└── application-local.properties      # Configuracion local (NO commitear, en .gitignore)

src/main/java/com/acme/backendfreshsense/
├── BackendFreshSenseApplication.java # Entry point
├── shared/
│   ├── infrastructure/security/      # JwtService, JwtAuthFilter, SecurityConfig, RateLimitFilter
│   └── infrastructure/crypto/        # AES256Converter
└── shared/domain/event/              # Clases base de eventos de dominio
```

---

## Despliegue en produccion

La aplicacion esta preparada para despliegue en plataformas PaaS (Railway, Render, Heroku).

1. Configurar las variables de entorno en la plataforma (ver tabla de variables arriba).
2. La plataforma debe proveer una instancia MySQL 8.0 con las variables `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`.
3. Hibernate creara y actualizara el schema automaticamente (`ddl-auto=update`).
4. Los planes de billing (Basic, Premium) se seedean automaticamente al primer arranque si la tabla `plans` esta vacia.
5. Asegurarse de que `JWT_SECRET` y `AES_SECRET` tengan al menos 32 caracteres y sean valores aleatorios seguros.
