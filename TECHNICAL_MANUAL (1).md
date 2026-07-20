# Manual Técnico — Healthy Calories

> Aplicación Android para el registro y seguimiento de calorías diarias.

---

## 1. Información general

| Campo | Valor |
|---|---|
| Nombre de la app | Healthy Calories |
| Application ID | `com.juanc.aplicacion_calorias` |
| Versión | 1.0 (versionCode 1) |
| Lenguaje | **Java 11** (el proyecto está implementado íntegramente en Java, no en Kotlin) |
| minSdk | 24 (Android 7.0) |
| targetSdk / compileSdk | 36 |
| Sistema de build | Gradle (Kotlin DSL, `build.gradle.kts`) con Version Catalog (`libs.versions.toml`) |

---

## 2. Librerías y dependencias

Extraídas de `app/build.gradle.kts` y `gradle/libs.versions.toml`:

| Librería | Versión | Propósito |
|---|---|---|
| `androidx.appcompat:appcompat` | 1.7.1 | Compatibilidad de componentes de UI y temas |
| `com.google.android.material:material` | 1.14.0 | Componentes Material Design (botones, `TextInputLayout`, Snackbar, FAB) |
| `androidx.activity:activity` | 1.13.0 | APIs de `Activity` y Edge-to-Edge |
| `androidx.constraintlayout:constraintlayout` | 2.2.1 | Layouts de UI |
| `androidx.room:room-runtime` | 2.8.4 | Persistencia local (base de datos SQLite abstraída) |
| `androidx.room:room-compiler` | 2.8.4 | Generación de código de Room (annotation processor) |
| `androidx.room:room-common-jvm` | 2.8.4 | Anotaciones comunes de Room |
| `androidx.room3:room3-common-jvm` | 3.0.0-rc01 | Dependencia común asociada a Room |
| `androidx.work:work-runtime` | 2.10.0 / 2.11.2 | Ejecución de tareas en segundo plano (`CalorieWorker`) |
| `junit:junit` | 4.13.2 | Tests unitarios |
| `androidx.test.ext:junit` | 1.3.0 | Tests instrumentados |
| `androidx.test.espresso:espresso-core` | 3.7.0 | Tests de UI instrumentados |

**Plugin de Gradle:** `com.android.application` v9.1.1 (AGP).

---

## 3. Arquitectura

El proyecto sigue una variante simplificada de **MVVM**, sin capa explícita de `Repository`:

```
Activity/Adapter (UI)
      │
      ▼
ViewModel (ComidaViewModel)
      │
      ▼
DAO (Room) ──► AppDatabase ──► SQLite
```

- **Capa de UI (Activities/Adapters):** `LoginActivity`, `RegisterActivity`, `MainActivity`, `SettingsActivity`, `ProgressActivity` y `ComidaAdapter` (RecyclerView). Gestionan la interacción del usuario, la validación de formularios y la navegación.
- **Capa de ViewModel:** `ComidaViewModel` (extiende `AndroidViewModel`) expone `LiveData` con la lista de comidas y delega las operaciones de escritura a un `ExecutorService` para no bloquear el hilo principal.
- **Capa de datos (Room):** `AppDatabase` (clase abstracta `RoomDatabase`) expone los DAOs `UserDao` y `ComidaDao`.
- **Acceso directo a DAO desde Activities:** `LoginActivity` y `RegisterActivity` acceden directamente a `AppDatabase`/`UserDao` desde un `Thread` propio, sin pasar por un ViewModel ni un Repository. Esto es una desviación del patrón MVVM puro que conviene tener en cuenta para futuras refactorizaciones.
- **Trabajo en segundo plano:** `CalorieWorker` (extiende `Worker`, de WorkManager) calcula el total de calorías del día y dispara una notificación local si se supera el objetivo diario configurado en `SharedPreferences`.
- **Validación:** clase utilitaria `Validador` centraliza las reglas de validación de correo y contraseña.
- **Visualización:** `BarChartView` (vista personalizada) dibuja un gráfico de barras simple para `ProgressActivity`.

---

## 4. Modelo de datos

Persistencia gestionada con **Room** (base de datos `app_database`, versión de esquema `5`, con `fallbackToDestructiveMigration()`).

### 4.1 Entidad `Usuario` (tabla `usuarios`)

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` (PK, autogenerado) | Identificador único del usuario |
| `nombre` | `String` | Nombre del usuario |
| `correo` | `String` | Correo electrónico (usado como credencial de login) |
| `password` | `String` | Contraseña (almacenada en texto plano; ver §7 recomendaciones) |

### 4.2 Entidad `Comida` (tabla `comidas`)

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | `int` (PK, autogenerado) | Identificador único de la comida |
| `nombre` | `String` | Nombre del alimento/comida registrada |
| `calorias` | `int` | Calorías asociadas al registro |
| `timestamp` | `long` | Marca de tiempo de creación (epoch millis) |
| `usuarioId` | `int` (FK lógica) | Id del usuario propietario del registro |

### 4.3 DAOs

**`UserDao`**
- `insertUser(Usuario)` → `long`
- `login(correo, password)` → `Usuario`
- `findByEmail(correo)` → `Usuario`

**`ComidaDao`**
- `insert(Comida)`, `update(Comida)`, `delete(Comida)`
- `getAllComidas(userId)` → `LiveData<List<Comida>>`
- `getComidasSince(userId, since)` → `LiveData<List<Comida>>`
- `getAllComidasSync(userId)` → `List<Comida>` (consulta síncrona, usada por `CalorieWorker`)

---

## 5. Persistencia y almacenamiento local

- **Room** es el único mecanismo de persistencia estructurada (no se usa Firebase ni ninguna API REST externa).
- Las **preferencias de usuario** (sesión activa, tema, objetivo diario de calorías, estado de notificaciones) se guardan en `SharedPreferences`, bajo las claves definidas en `SettingsActivity` (`PREFS_NAME`, `KEY_USER_ID`, `KEY_USER_NAME`, `KEY_THEME`, `KEY_CALORIE_LIMIT`, `KEY_NOTIFICATIONS`).
- El singleton `AppDatabase.getDatabase(context)` garantiza una única instancia de la base de datos en toda la app.

---

## 6. Funcionalidades principales

- **Autenticación:** registro (`RegisterActivity`) y login (`LoginActivity`) contra la tabla `usuarios`, con sesión persistida en `SharedPreferences`.
- **Registro de comidas:** alta, edición y borrado de comidas asociadas al usuario autenticado, mostradas en un `RecyclerView` (`ComidaAdapter`).
- **Seguimiento diario:** cálculo de calorías consumidas frente al objetivo diario configurable, mostrado en `MainActivity`.
- **Progreso histórico:** `ProgressActivity` visualiza el historial mediante `BarChartView`.
- **Notificaciones:** `CalorieWorker` (WorkManager) revisa periódicamente si se ha superado el límite diario y envía una notificación local (requiere el permiso `POST_NOTIFICATIONS`, declarado en el manifiesto).
- **Ajustes:** `SettingsActivity` permite configurar tema (claro/oscuro/sistema), objetivo calórico diario y activar/desactivar notificaciones.

### Pantallas (Activities) declaradas en el manifiesto

| Activity | Exportada | Rol |
|---|---|---|
| `LoginActivity` | Sí (`LAUNCHER`) | Punto de entrada de la app |
| `RegisterActivity` | No | Alta de nuevos usuarios |
| `MainActivity` | No | Pantalla principal / registro de comidas |
| `SettingsActivity` | No | Configuración de usuario |
| `ProgressActivity` | No | Historial y gráfico de progreso |

### Permisos

- `android.permission.POST_NOTIFICATIONS`

---

## 7. Instrucciones de compilación

### Requisitos previos

- Android Studio (versión compatible con AGP 9.1.1 y compileSdk 36).
- JDK 11 o superior.
- SDK de Android con la plataforma 36 instalada.

### Pasos

1. Clonar el repositorio desde la versión publicada:
   ```bash
   git clone https://github.com/Jjuanki/aplicacion_calorias.git
   cd aplicacion_calorias
   git checkout v1.0
   ```
   (Release: *Release Versión 1.0 - MVP Final*, tag `v1.0`)

2. Abrir el proyecto en **Android Studio** (`Open` → seleccionar la carpeta raíz del repositorio).

3. Esperar la **sincronización de Gradle** (Android Studio la lanza automáticamente; si no, `File → Sync Project with Gradle Files`).

4. Ejecutar la app:
   - Seleccionar un emulador o dispositivo físico (mínimo Android 7.0 / API 24).
   - Pulsar **Run ▶** o ejecutar:
     ```bash
     ./gradlew installDebug
     ```

5. (Opcional) Ejecutar tests:
   ```bash
   ./gradlew test                  # tests unitarios
   ./gradlew connectedAndroidTest  # tests instrumentados
   ```

---

## 8. Notas y recomendaciones técnicas

- **Seguridad de credenciales:** actualmente `Usuario.password` se almacena y compara en texto plano (`UserDao.login`). Se recomienda aplicar hashing (p. ej. BCrypt o `PBKDF2`) antes de producción.
- **Migraciones de Room:** la base de datos usa `fallbackToDestructiveMigration()`, lo que borra los datos del usuario ante cualquier cambio de versión del esquema. Conviene definir migraciones explícitas antes de publicar actualizaciones.
- **Acceso a datos desde la UI:** `LoginActivity`/`RegisterActivity` acceden al DAO directamente en un `Thread`, en lugar de pasar por un `ViewModel`/`Repository` como el resto de la app. Se recomienda unificar el patrón de acceso a datos.
- **Sin backend remoto:** la app es 100% local (Room + SharedPreferences); no integra Firebase ni ninguna API REST.

---

## 9. Estructura del proyecto

```
app/src/main/java/com/juanc/aplicacion_calorias/
├── AppDatabase.java        # Configuración de Room (entidades, versión, singleton)
├── Usuario.java             # Entidad de usuario
├── UserDao.java             # DAO de usuario
├── Comida.java               # Entidad de comida
├── ComidaDao.java            # DAO de comida
├── ComidaViewModel.java       # ViewModel de comidas
├── ComidaAdapter.java         # Adapter del RecyclerView
├── CalorieWorker.java          # Worker de notificaciones (WorkManager)
├── Validador.java               # Validaciones de formularios
├── BarChartView.java             # Vista personalizada del gráfico de progreso
├── LoginActivity.java             # Pantalla de login
├── RegisterActivity.java           # Pantalla de registro
├── MainActivity.java                # Pantalla principal
├── SettingsActivity.java             # Pantalla de ajustes
└── ProgressActivity.java              # Pantalla de progreso
```
