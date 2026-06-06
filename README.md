# FreshSense - Backend

API REST del sistema FreshSense construida con Spring Boot 3.5.7 y Java 17, siguiendo Domain-Driven Design (DDD). Arquitectura de monolito modular con microservicios extraidos para `recipes` y `alerts`, coordinados mediante Eureka Server.

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

Crear la base de datos en MySQL antes de levantar cualquier servicio:

```sql
CREATE DATABASE freshsense_db;
```

### 2. Archivos de propiedades locales

Los archivos `application-local.properties` **no estan en el repositorio** (estan en `.gitignore`) porque contienen credenciales privadas. Los valores sensibles deben ser solicitados al lider del proyecto (Fabricio).

Se necesitan crear tres archivos en las siguientes rutas:

---

**Monolito principal** — `src/main/resources/application-local.properties`

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

---

**Microservicio de alertas** — `alerts-service/src/main/resources/application-local.properties`

```properties
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=
```

---

**Microservicio de recetas** — `recipes-service/src/main/resources/application-local.properties`

```properties
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=
```

---

Crear cada archivo en su ruta correspondiente con los valores proporcionados por Fabricio. **Nunca commitear estos archivos.**

---

## Variables de entorno disponibles

Para referencia, estas son las variables que configuran los archivos locales:

| Variable               | Descripcion                                      | Aplica a           |
|------------------------|--------------------------------------------------|--------------------|
| `DB_HOST`              | Host de la base de datos                         | Todos              |
| `DB_PORT`              | Puerto de la base de datos                       | Todos              |
| `DB_NAME`              | Nombre de la base de datos                       | Todos              |
| `DB_USER`              | Usuario de MySQL                                 | Todos              |
| `DB_PASSWORD`          | Password de MySQL                                | Todos              |
| `JWT_SECRET`           | Clave para firmar JWT (minimo 32 caracteres)     | Solo monolito      |
| `AES_SECRET`           | Clave AES-256 para cifrado en reposo (32+ chars) | Solo monolito      |
| `JWT_ACCESS_MINUTES`   | Duracion del access token en minutos             | Solo monolito      |
| `JWT_REFRESH_DAYS`     | Duracion del refresh token en dias               | Solo monolito      |
| `GOOGLE_CLIENT_ID`     | Client ID de Google OAuth2                       | Solo monolito      |
| `GOOGLE_CLIENT_SECRET` | Client Secret de Google OAuth2                   | Solo monolito      |
| `FRONTEND_URL`         | URL base del frontend (redirect post-OAuth2)     | Solo monolito      |
| `EUREKA_URL`           | URL del servidor Eureka                          | Monolito y microservicios |
| `PORT`                 | Puerto HTTP del servidor                         | Todos              |

---

## Levantar todos los servicios

Los servicios deben levantarse **en este orden**. Abrir una terminal por servicio y esperar a que cada uno diga `Started` antes de continuar con el siguiente.

### Terminal 1 - Eureka Server (puerto 8761)

```bash
cd eureka-server
../mvnw spring-boot:run
```

Windows CMD:
```cmd
cd eureka-server
..\mvnw.cmd spring-boot:run
```

Esperar hasta ver en consola:
```
Started EurekaServerApplication
```

El panel de Eureka queda disponible en `http://localhost:8761`.

---

### Terminal 2 - Alerts Service (puerto 8083)

```bash
cd alerts-service
../mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Windows CMD:
```cmd
cd alerts-service
..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Esperar hasta ver:
```
Started AlertsServiceApplication
```

---

### Terminal 3 - Recipes Service (puerto 8082)

```bash
cd recipes-service
../mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Windows CMD:
```cmd
cd recipes-service
..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

Esperar hasta ver:
```
Started RecipesServiceApplication
```

---

### Terminal 4 - Monolito principal (puerto 8080)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Windows CMD:
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

IntelliJ IDEA: Edit Configurations → Active profiles → `local` → Run.

Esperar hasta ver:
```
Started BackendFreshSenseApplication
```

---

### Verificar que todo esta corriendo

Abrir `http://localhost:8761` en el navegador. Deben aparecer los tres servicios con status `UP`:

- `BACKEND-FRESHSENSE` (8080)
- `ALERTS-SERVICE` (8083)
- `RECIPES-SERVICE` (8082)

Si alguno no aparece, revisar que su terminal no haya tenido errores de conexion a la base de datos.

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

Todos los endpoints estan documentados con ejemplos de request y response. Para endpoints protegidos, usar el boton **Authorize** con un JWT obtenido desde `/api/accounts/login`.

---

## Arquitectura de microservicios

```
eureka-server/      # Registro de servicios (puerto 8761)
alerts-service/     # Microservicio de alertas (puerto 8083)
recipes-service/    # Microservicio de recetas (puerto 8082)
src/                # Monolito principal (puerto 8080)
```

El monolito actua como punto de entrada unico: expone los controladores REST, aplica seguridad JWT y delega la logica de negocio de recetas y alertas a los microservicios via Feign Client. Eureka resuelve las direcciones dinamicamente.

La seguridad (JWT, roles, RBAC) se aplica **unicamente en el monolito**. Los microservicios no tienen Spring Security — solo son accesibles via Feign desde el monolito.

---

## Modulos del monolito

Estructura de capas DDD por modulo:

```
{modulo}/
├── application/
│   ├── dto/         # DTOs de entrada y salida
│   ├── mapper/      # Mappers entidad <-> DTO
│   └── service/     # Logica de negocio
├── domain/
│   ├── model/       # Entidades de dominio (POJOs)
│   └── repository/  # Interfaces de repositorio
└── infrastructure/
    ├── persistence/ # Entidades JPA, adapters, configuracion
    ├── feign/       # Clientes Feign (alerts y recipes)
    └── web/         # Controladores REST
```

### Endpoints por modulo

| Modulo          | Base URL                           | Descripcion                                        |
|-----------------|------------------------------------|----------------------------------------------------|
| `accounts`      | `/api/accounts`                    | Registro, login, logout, refresh token, perfil     |
| `inventory`     | `/api/products`                    | CRUD de productos en inventario                    |
| `alerts`        | `/api/alerts`                      | Alertas (delegado a alerts-service via Feign)      |
| `monitoring`    | `/api/monitoring`                  | Registro de lecturas de sensores IoT               |
| `recipes`       | `/api/recipes`                     | Recetas (delegado a recipes-service via Feign)     |
| `reports`       | `/api/history`                     | Historial de consumo; `/advanced` requiere premium |
| `billing`       | `/api/billing`                     | Planes y suscripciones                             |
| `notifications` | `/api/notifications`               | Bandeja in-app, preferencias, envio (solo ADMIN)   |
| `achievements`  | `/api/users/{userId}/achievements` | Logros y gamificacion por usuario                  |
| `challenges`    | `/api/challenges`                  | Retos, enroll y leaderboard                        |

---

## Seguridad

### Autenticacion

- JWT stateless. El token viaja en cookie HttpOnly `authToken` (preferido) o en header `Authorization: Bearer <token>`.
- Al hacer login o registro se setean dos cookies:
  - `authToken` — access token, expira en 15 minutos.
  - `refreshToken` — refresh token, expira en 7 dias, path `/api/accounts/refresh`.
- `POST /api/accounts/refresh` renueva el access token sin re-autenticacion.

### Roles

| Rol              | Descripcion                                                          |
|------------------|----------------------------------------------------------------------|
| `USER_STANDARD`  | Usuario registrado sin suscripcion activa                            |
| `USER_PREMIUM`   | Usuario con suscripcion activa; accede a analytics y recetas premium |
| `ADMIN`          | Administrador; puede crear recetas y enviar notificaciones           |

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

El campo `paymentReference` en suscripciones esta cifrado con AES-256-GCM mediante un `JPA AttributeConverter`.

### Rate limiting

Los endpoints `/api/accounts/login` y `/api/accounts/register` tienen limite de 5 peticiones por minuto por IP para prevenir fuerza bruta.

---

## OAuth2 - Login con Google

El backend soporta autenticacion via Google OAuth2 ademas del login tradicional.

**Flujo:**
1. El frontend redirige al usuario a `http://localhost:8080/oauth2/authorization/google`
2. Google autentica al usuario y redirige de vuelta al backend
3. El backend crea o recupera el usuario en la BD con rol `USER_STANDARD`
4. Se emiten las cookies HttpOnly `authToken` y `refreshToken`
5. El usuario es redirigido al frontend en `{FRONTEND_URL}/dashboard`

Las credenciales de Google OAuth2 (`GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET`) son privadas y se deben solicitar al lider del proyecto. Si no se configuran, el endpoint OAuth2 no estara disponible pero el resto del sistema funciona normalmente.

---

## Eventos de dominio

Los modulos se comunican de forma desacoplada mediante `Spring ApplicationEventPublisher`:

| Evento                        | Publicado por | Consumido por            | Efecto                               |
|-------------------------------|---------------|--------------------------|--------------------------------------|
| `UserRegisteredEvent`         | `accounts`    | —                        | Log / extension futura               |
| `UserRoleChangedEvent`        | `accounts`    | —                        | Auditoria de cambios de rol          |
| `SubscriptionActivatedEvent`  | `billing`     | `accounts` (RoleService) | Upgrade a `USER_PREMIUM`             |
| `SubscriptionCancelledEvent`  | `billing`     | `accounts` (RoleService) | Downgrade a `USER_STANDARD`          |
| `ExpirationAlertRaisedEvent`  | `alerts`      | `notifications`          | Envia notificacion in-app al usuario |

---

## Comandos utiles

```bash
# Build sin tests
./mvnw clean package -DskipTests

# Ejecutar tests
./mvnw test

# Limpiar y recompilar
./mvnw clean compile
```

---

## Estructura de archivos relevantes

```
BackEnd/
├── eureka-server/                        # Servidor Eureka (Spring Cloud 2023.0.3)
├── recipes-service/                      # Microservicio de recetas (puerto 8082)
├── alerts-service/                       # Microservicio de alertas (puerto 8083)
└── src/main/
    ├── resources/
    │   ├── application.properties        # Configuracion base (en el repo)
    │   └── application-local.properties  # Credenciales locales (NO commitear)
    └── java/com/acme/backendfreshsense/
        ├── BackendFreshSenseApplication.java
        ├── shared/infrastructure/security/    # JwtService, JwtAuthFilter, SecurityConfig
        ├── shared/infrastructure/crypto/      # AES256Converter
        ├── shared/infrastructure/openapi/     # OpenApiConfig (Swagger)
        └── shared/domain/event/               # Clases base de eventos de dominio
```

---

## Despliegue en produccion

La aplicacion esta preparada para despliegue en plataformas PaaS (Railway, Render, Heroku).

1. Configurar todas las variables de entorno en la plataforma (ver tabla de variables).
2. La plataforma debe proveer una instancia MySQL 8.0. Las variables `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD` son usadas automaticamente si estan presentes.
3. Hibernate crea y actualiza el schema automaticamente (`ddl-auto=update`).
4. Los planes de billing se seedean automaticamente al primer arranque si la tabla `plans` esta vacia.
5. `JWT_SECRET` y `AES_SECRET` deben tener minimo 32 caracteres y ser valores aleatorios seguros — nunca usar los mismos valores de desarrollo en produccion.
6. Para microservicios en produccion, configurar `EUREKA_URL` apuntando al servidor Eureka desplegado.
