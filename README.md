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

## Opcion rapida: levantar TODO con Docker (recomendado)

Si tienes **Docker Desktop**, no necesitas instalar Java, Maven, Node ni MySQL. Un solo comando levanta MySQL (con las 3 bases creadas automaticamente), Eureka, el monolito, los dos microservicios y el frontend con Nginx.

Desde la **raiz del proyecto** (la carpeta que contiene `docker-compose.yml`, un nivel arriba de `BackEnd/`):

```bash
docker compose build     # compila los 4 servicios Java + el frontend (la 1ra vez tarda varios minutos)
docker compose up        # levanta todo
```

Cuando veas `Started BackendFreshSenseApplication`, abre:

- App: `http://localhost`
- Swagger: `http://localhost/swagger-ui/index.html`
- Usuario demo: `demo@freshsense.com` / `Demo1234!`

Para apagar: `Ctrl + C` (o `docker compose down`). Si cambias codigo: `docker compose up --build`.

> Nota: el `docker-compose.yml` ya trae credenciales de desarrollo (JWT/AES/DB) para correr en local sin pedir nada. No usa las credenciales de produccion.

---

## Configuracion local (manual, sin Docker)

### 1. Base de datos

Cada servicio usa **su propia base de datos**. Crearlas en MySQL antes de levantar (solo si corres en modo manual; con Docker se crean solas):

```sql
CREATE DATABASE freshsense_db;   -- monolito
CREATE DATABASE alerts_db;       -- alerts-service
CREATE DATABASE recipes_db;      -- recipes-service
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
| `achievements`  | `/api/achievements`                | Logros del usuario autenticado (userId del token)  |
| `challenges`    | `/api/challenges`                  | Retos, enroll, leaderboard y progreso              |
| `monitoring`    | `/api/devices`                     | Registro de dispositivos IoT del usuario           |
| `monitoring`    | `/api/edge/readings`               | **Ingesta de lecturas desde el Edge** (X-Device-Key) |
| `monitoring`    | `/api/thresholds`                  | Umbrales temp/humedad por categoria (referencia)   |

---

## API del Edge / IoT (para el equipo del dispositivo)

Flujo del proyecto: **IoT (ESP32) -> Edge -> Backend -> App**. El dispositivo fisico envia sus lecturas al backend a traves de este endpoint.

### 1. Registrar el dispositivo (una vez)

Antes de enviar lecturas, el usuario registra el dispositivo desde la app (vista **Dispositivos**) o por API:

```
POST /api/devices          (requiere estar logueado)
Body: { "deviceId": "esp32-freshsense-1", "name": "Refrigerador cocina" }
```

La respuesta incluye un `secretKey` que **solo se muestra al registrar**. Esa es la clave que el Edge usara en la cabecera `X-Device-Key`. Guardala.

### 2. Enviar lecturas desde el Edge

```
POST  http://<IP-DEL-SERVIDOR>:8080/api/edge/readings
Headers:
  Content-Type: application/json
  X-Device-Key: <secretKey del dispositivo registrado>
Body:
{
  "deviceId": "esp32-freshsense-1",
  "temperature": 24,
  "humidity": 40,
  "time": "21/06/2026 10:48",
  "id": "34c0c05ad9f7e4397335"
}
```

| Campo         | Obligatorio | Notas                                                        |
|---------------|-------------|--------------------------------------------------------------|
| `deviceId`    | Si          | Identifica el sensor; debe coincidir con el registrado.      |
| `temperature` | Si          | Numero (°C).                                                 |
| `humidity`    | Si          | Numero (%).                                                  |
| `time`        | No          | Formato **`dd/MM/yyyy HH:mm`**. Si falta o es invalido, se usa la hora actual. |
| `id`          | No          | Identificador de la lectura generado en el Edge.            |

**Autenticacion:** el Edge NO usa JWT. Se autentica solo con la cabecera `X-Device-Key`. El usuario dueño de la lectura se resuelve a partir del dispositivo registrado.

**Respuestas:**
- `201` — lectura guardada (devuelve el JSON de la lectura).
- `401 "Dispositivo no autorizado"` — `deviceId` o `X-Device-Key` incorrectos.

**Ejemplo con curl:**
```bash
curl -X POST http://localhost:8080/api/edge/readings \
  -H "Content-Type: application/json" \
  -H "X-Device-Key: <tu-clave>" \
  -d '{ "deviceId":"esp32-freshsense-1","temperature":24,"humidity":40,"time":"21/06/2026 10:48","id":"34c0c05ad9f7e4397335" }'
```

> URL segun despliegue: en local con Docker desde la misma maquina usa `http://localhost:8080/...`; si el ESP32 esta en otro equipo de la red, usa la IP del servidor (ej. `http://192.168.1.50:8080/...`); detras de Nginx tambien sirve `http://localhost/api/edge/readings`.

Las lecturas entrantes se ven en la vista **Monitoreo** y alimentan el semaforo de frescura del inventario (comparando contra los umbrales de `/api/thresholds`).

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
- `POST /api/edge/readings` (autenticado por `X-Device-Key`, no por JWT)
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

## Despliegue en produccion (Azure)

La infraestructura de produccion corre en **Azure Container Apps** con imagen en **Azure Container Registry**.

### URLs de produccion

| Servicio | URL |
|----------|-----|
| API REST (monolito) | `https://freshsense-backend.mangoground-03a86fb8.eastus.azurecontainerapps.io` |
| Swagger UI | `https://freshsense-backend.mangoground-03a86fb8.eastus.azurecontainerapps.io/swagger-ui/index.html` |
| Eureka Server | interno (`http://eureka-server`) — sin ingress externo |
| Alerts Service | interno — solo accesible via Feign desde el monolito |
| Recipes Service | interno — solo accesible via Feign desde el monolito |

### Infraestructura Azure

| Recurso | Nombre |
|---------|--------|
| Resource Group | `freshsense-rg` |
| Container Registry | `freshsenseacr.azurecr.io` |
| Container Apps Environment | `freshsense-env` (East US) |
| Container App (monolito) | `freshsense-backend` |
| Container App (eureka) | `eureka-server` |
| Container App (alerts) | `alerts-service` |
| Container App (recipes) | `recipes-service` |

### Base de datos en produccion

**TiDB Cloud Serverless** (MySQL-compatible). Las tres bases de datos estan creadas:
- `freshsense_db` — monolito principal
- `alerts_db` — alerts-service
- `recipes_db` — recipes-service

Credenciales en poder del lider del proyecto (Fabricio) — NO commitear.

### CI/CD

**GitHub Actions** configurado automaticamente via Azure Portal (Continuous Deployment).

- Workflow: `.github/workflows/freshsense-backend-AutoDeployTrigger-*.yml`
- Cada push a `main` compila la imagen Docker, la sube a `freshsenseacr.azurecr.io` y despliega una nueva revision en `freshsense-backend`.
- Autenticacion con Azure via **User-Assigned Managed Identity** (sin Service Principal).

### Variables de entorno en produccion

Configuradas directamente en cada Container App via Azure Portal o CLI. Las variables sensibles (`JWT_SECRET`, `AES_SECRET`, credenciales TiDB, Google OAuth2) estan en Azure Container Apps secrets — solicitarlas a Fabricio.

### Notas de arquitectura en produccion

- Eureka: los microservicios se registran usando `EUREKA_INSTANCE_HOSTNAME=<nombre-del-servicio>` y el monolito los descubre via DNS interno de Container Apps (`http://eureka-server`).
- Google OAuth2 redirect URI registrado en Google Cloud Console para produccion: `https://freshsense-backend.mangoground-03a86fb8.eastus.azurecontainerapps.io/login/oauth2/code/google`
- CORS configurado en `SecurityConfig.java` para aceptar requests desde el frontend de produccion y localhost:4200.
- TiDB requiere TLS: la JDBC URL usa `sslMode=VERIFY_IDENTITY&enabledTLSProtocols=TLSv1.2,TLSv1.3`.
