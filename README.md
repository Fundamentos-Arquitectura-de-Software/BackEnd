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

Crear la base de datos en MySQL antes de levantar el servidor:

```sql
CREATE DATABASE freshsense_db;
```

### 2. Variables de entorno del monolito

Crear el archivo `src/main/resources/application-local.properties` (no esta en el repositorio, en `.gitignore`):

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=freshsense_db
DB_USER=root
DB_PASSWORD=root
JWT_SECRET=local-dev-secret-key-at-least-32-chars-long
AES_SECRET=local-aes-secret-key-32chars-pad!
```

Para OAuth2 Google agregar tambien:
```properties
GOOGLE_CLIENT_ID=
GOOGLE_CLIENT_SECRET=
```

Los valores de OAuth2 no se publican en el repositorio. Contactar al lider del proyecto para recibirlos.

### 3. Variables de entorno de los microservicios

Cada microservicio tiene su propio `application-local.properties` en su carpeta `src/main/resources/`:

`alerts-service/src/main/resources/application-local.properties`
`recipes-service/src/main/resources/application-local.properties`

Ambos archivos comparten el mismo formato (solo credenciales de BD, sin JWT ni AES):

```properties
DB_HOST=localhost
DB_PORT=3306
DB_NAME=freshsense_db
DB_USER=root
DB_PASSWORD=root
```

---

## Variables de entorno disponibles

| Variable               | Descripcion                                      | Default               |
|------------------------|--------------------------------------------------|-----------------------|
| `DB_HOST`              | Host de la base de datos                         | —                     |
| `DB_PORT`              | Puerto de la base de datos                       | —                     |
| `DB_NAME`              | Nombre de la base de datos                       | —                     |
| `DB_USER`              | Usuario de MySQL                                 | —                     |
| `DB_PASSWORD`          | Password de MySQL                                | —                     |
| `JWT_SECRET`           | Clave para firmar JWT (minimo 32 caracteres)     | —                     |
| `AES_SECRET`           | Clave AES-256 para cifrado en reposo (32+ chars) | —                     |
| `JWT_ACCESS_MINUTES`   | Duracion del access token en minutos             | `15`                  |
| `JWT_REFRESH_DAYS`     | Duracion del refresh token en dias               | `7`                   |
| `PORT`                 | Puerto HTTP del servidor                         | `8080`                |
| `GOOGLE_CLIENT_ID`     | Client ID de Google OAuth2                       | —                     |
| `GOOGLE_CLIENT_SECRET` | Client Secret de Google OAuth2                   | —                     |
| `FRONTEND_URL`         | URL base del frontend (redirect post-OAuth2)     | `http://localhost:4200` |
| `EUREKA_URL`           | URL del servidor Eureka                          | `http://localhost:8761/eureka/` |

En produccion (Railway, Render, etc.) se usan `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD` que provee la plataforma automaticamente.

---

## Levantar todos los servicios

Los servicios deben levantarse en este orden. Abrir una terminal por servicio:

### Terminal 1 - Eureka Server (puerto 8761)

```bash
cd eureka-server
..\mvnw.cmd spring-boot:run
```

Esperar hasta ver `Started EurekaServerApplication` antes de continuar.

### Terminal 2 - Recipes Service (puerto 8082)

```bash
cd recipes-service
..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

### Terminal 3 - Alerts Service (puerto 8083)

```bash
cd alerts-service
..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

### Terminal 4 - Monolito principal (puerto 8080)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

Windows CMD:
```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
```

IntelliJ IDEA: Edit Configurations -> Active profiles -> `local` -> Run.

Panel de Eureka disponible en `http://localhost:8761`. Los tres servicios deben aparecer como `UP`.

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

## OAuth2 - Login con Google

El backend soporta autenticacion via Google OAuth2 ademas del login tradicional con email/password.

**Flujo:**
1. El frontend redirige al usuario a `http://localhost:8080/oauth2/authorization/google`
2. Google autentica al usuario y redirige de vuelta al backend
3. El backend crea o recupera el usuario en la BD con rol `USER_STANDARD`
4. Se emiten las cookies HttpOnly `authToken` y `refreshToken` igual que en el login tradicional
5. El usuario es redirigido al frontend en `{FRONTEND_URL}/dashboard`

**Configuracion necesaria en Google Cloud Console:**
- Crear un proyecto en Google Cloud Console
- Habilitar la People API
- Crear credenciales OAuth2 de tipo "Aplicacion web"
- Agregar como URI de redireccion autorizado: `http://localhost:8080/login/oauth2/code/google`
- Copiar el Client ID y Client Secret a `GOOGLE_CLIENT_ID` y `GOOGLE_CLIENT_SECRET`

Si `GOOGLE_CLIENT_ID` esta vacio, el endpoint OAuth2 no estara disponible pero el resto del sistema funciona normalmente.

---

## Arquitectura de microservicios

El sistema utiliza Spring Cloud Netflix Eureka para service discovery y Spring Cloud OpenFeign para comunicacion entre servicios.

```
eureka-server/      # Registro de servicios (puerto 8761)
recipes-service/    # Microservicio de recetas (puerto 8082)
alerts-service/     # Microservicio de alertas (puerto 8083)
src/                # Monolito principal (puerto 8080)
```

El monolito actua como API Gateway: expone los controladores REST, aplica seguridad JWT y delega la logica de negocio de recetas y alertas a los microservicios via Feign Client usando Eureka para resolver las direcciones.

La seguridad (JWT, roles, RBAC) se aplica unicamente en el monolito. Los microservicios no tienen Spring Security.

---

## Modulos del monolito

Cada modulo sigue la misma estructura de capas DDD:

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
    ├── feign/       # Clientes Feign (solo en modulos delegados a microservicios)
    └── web/         # Controladores REST
```

### Endpoints por modulo

| Modulo          | Base URL                           | Descripcion                                       |
|-----------------|------------------------------------|---------------------------------------------------|
| `accounts`      | `/api/accounts`                    | Registro, login, logout, refresh token, perfil   |
| `inventory`     | `/api/products`                    | CRUD de productos en inventario                   |
| `alerts`        | `/api/alerts`                      | Alertas (delegado a alerts-service via Feign)     |
| `monitoring`    | `/api/monitoring`                  | Registro de lecturas de sensores IoT              |
| `recipes`       | `/api/recipes`                     | Recetas (delegado a recipes-service via Feign)    |
| `reports`       | `/api/history`                     | Historial de consumo; `/advanced` requiere premium |
| `billing`       | `/api/billing`                     | Planes y suscripciones                            |
| `notifications` | `/api/notifications`               | Bandeja in-app, preferencias, envio (solo ADMIN)  |
| `achievements`  | `/api/users/{userId}/achievements` | Logros y gamificacion por usuario                 |
| `challenges`    | `/api/challenges`                  | Retos, enroll y leaderboard                       |

---

## Seguridad

### Autenticacion

- JWT stateless. El token viaja en cookie HttpOnly `authToken` (preferido) o en header `Authorization: Bearer <token>`.
- Al hacer login o registro se setean dos cookies:
  - `authToken` - access token, expira en 15 minutos.
  - `refreshToken` - refresh token, expira en 7 dias, path `/api/accounts/refresh`.
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

El campo `paymentReference` en suscripciones esta cifrado con AES-256-GCM mediante un `JPA AttributeConverter`. La clave se configura con `AES_SECRET`.

### Rate limiting

Se aplica rate limiting en los endpoints de autenticacion mediante Bucket4j para prevenir fuerza bruta.

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
# Monolito principal
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
./mvnw clean package -DskipTests
./mvnw test

# Desde subcarpetas de microservicios
cd eureka-server  && ..\mvnw.cmd spring-boot:run
cd recipes-service && ..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
cd alerts-service  && ..\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
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
    │   ├── application.properties        # Configuracion base
    │   └── application-local.properties  # Configuracion local (NO commitear)
    └── java/com/acme/backendfreshsense/
        ├── BackendFreshSenseApplication.java  # Entry point
        ├── shared/infrastructure/security/    # JwtService, JwtAuthFilter, SecurityConfig
        ├── shared/infrastructure/crypto/      # AES256Converter
        └── shared/domain/event/               # Clases base de eventos de dominio
```

---

## Despliegue en produccion

La aplicacion esta preparada para despliegue en plataformas PaaS (Railway, Render, Heroku).

1. Configurar las variables de entorno en la plataforma (ver tabla arriba).
2. La plataforma debe proveer una instancia MySQL 8.0 con `MYSQLHOST`, `MYSQLPORT`, `MYSQLDATABASE`, `MYSQLUSER`, `MYSQLPASSWORD`.
3. Hibernate creara y actualizara el schema automaticamente (`ddl-auto=update`).
4. Los planes de billing se seedean automaticamente al primer arranque si la tabla `plans` esta vacia.
5. Asegurarse de que `JWT_SECRET` y `AES_SECRET` tengan al menos 32 caracteres y sean valores aleatorios seguros.
6. Para microservicios en produccion, configurar `EUREKA_URL` apuntando al servidor Eureka desplegado.
