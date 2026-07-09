# Aplicación Calorías 🥗🔥

Una aplicación en Java para calcular y gestionar calorías de alimentos y comidas. Sencilla, extensible y lista para convertirse en tu asistente de seguimiento de energía diaria.

> Descripción
> Una herramienta para registrar alimentos, calcular calorías por porción y obtener reportes sencillos. Ideal como proyecto educativo, demo o base para una app más grande.

---

## Arquitectura de datos

La aplicación sigue una arquitectura recomendada para separar responsabilidades en la capa de datos y persistencia:

- ViewModel → Repository → Room
  - ViewModel: expone observables para la UI y contiene la lógica de presentación. Orquesta llamadas al Repository y mantiene el estado de la interfaz.
  - Repository: fuente única de verdad para datos locales/remotos. Centraliza la lógica de obtención, almacenamiento y transformación de datos antes de entregarlos al ViewModel.
  - Room: base de datos local con entidades y DAOs que gestionan la persistencia (por ejemplo: entradas de calorías, comidas, usuarios). Room se encarga de las consultas, transacciones y migraciones.

Nota: si tu implementación actual no usa exactamente ViewModel/Room (p. ej. si es una app Java/CLI o usa otra persistencia), considera esta sección una propuesta de arquitectura. Si quieres, puedo inspeccionar el código y adaptar la descripción automáticamente.

---

## APIs y notificaciones

- APIs externas usadas:
  - Ninguna API externa detectada por defecto en este README. Si tu proyecto consume una API REST o SDK externo, por favor indica la librería o el archivo de configuración (por ejemplo `build.gradle`, `pom.xml` o clases de servicio) y lo documentaré aquí con endpoints y credenciales necesarias (si procede).

- Notificaciones:
  - No se detectaron configuraciones de notificaciones en la descripción. Si usas notificaciones locales o push (por ejemplo Firebase Cloud Messaging), indícame las dependencias (ej. `com.google.firebase:firebase-messaging`) o los archivos donde están configuradas y completaré la documentación.

> Pista: si quieres que haga una búsqueda automática en el repo para completar estas secciones, dime y haré un scan de `build.gradle`, `pom.xml`, y paquetes de servicios/receivers para listar dependencias y configuraciones.

---

## Capturas actuales de la aplicación

Incluye aquí las capturas de pantalla actuales de la aplicación. Recomendación de estructura dentro del repo:

- docs/screenshots/
  - docs/screenshots/screen1.png
  - docs/screenshots/screen2.png

Ejemplo de inserción de imágenes en Markdown:

![Pantalla principal](docs/screenshots/screen1.png)

Sustituye los archivos de ejemplo por las capturas reales y haz commit.

Si quieres, puedo añadir las imágenes por ti si me las proporcionas (las subiré a `docs/screenshots/` y actualizaré las rutas automáticamente).

---

## Cómo probar el CRUD

Sigue estos pasos para que otro desarrollador pueda probar las operaciones Create, Read, Update y Delete en la aplicación.

Prerequisitos
- JDK 11 o superior instalado (`java -version`).
- Maven o Gradle si el proyecto usa un sistema de build (revisa si hay `pom.xml` o `build.gradle`).
- Git para clonar el repositorio.
- Si tu app es Android, Android Studio y un emulador/dispositivo con depuración habilitada.

Pasos para ejecutar
1. Clona el repositorio:

   git clone https://github.com/Jjuanki/aplicacion_calorias.git
   cd aplicacion_calorias

2. Abre el proyecto en tu IDE preferido (IntelliJ/Android Studio/Eclipse) o usa la línea de comandos.
3. Si usas Maven:
   mvn clean package
   java -jar target/aplicacion_calorias.jar

   Si usas Gradle:
   ./gradlew build
   java -jar build/libs/aplicacion_calorias.jar

   Si no hay archivo de build, puedes compilar manualmente:
   javac -d out $(find src -name "*.java")
   java -cp out tu.paquete.Principal

Probar el flujo CRUD (ejemplos generales)
- Create (Crear):
  - Vía CLI: usar el comando de ejemplo para añadir alimentos:
    java -jar aplicacion_calorias.jar add-food --name "Manzana" --calories 52 --unit "100g"
  - Vía UI: abrir el formulario "Añadir" o botón "+" y completar los campos; pulsar Guardar.
  - Verifica que la nueva entrada aparece en la lista.

- Read (Leer):
  - Listar todos los alimentos/entradas:
    java -jar aplicacion_calorias.jar list-foods
  - Verificar la salida o la UI principal para confirmar que las entradas están presentes.

- Update (Actualizar):
  - Vía CLI: si hay un comando update, ejecutar:
    java -jar aplicacion_calorias.jar update-food --id 12 --name "Manzana Verde" --calories 48
  - Vía UI: abrir la entrada, pulsar Editar, modificar campos y Guardar.
  - Verifica que los cambios se reflejan en la lista y en la vista detalle.

- Delete (Borrar):
  - Vía CLI:
    java -jar aplicacion_calorias.jar delete-food --id 12
  - Vía UI: usar el botón Eliminar o gesto swipe y confirmar.
  - Verifica que la entrada ya no aparece.

Verificación adicional
- Si usas Room/SQLite/H2: inspecciona la base de datos con el inspector del IDE o herramientas como DB Browser para SQLite.
- Logs: revisa la salida estándar o Logcat (Android) para ver errores o confirmaciones de las operaciones.

Tests
- Ejecuta tests unitarios e instrumentados si existen:
  - Unit tests: ./gradlew test  (o mvn test)
  - Instrumented tests (Android): ./gradlew connectedAndroidTest

Consejos para pruebas manuales
- Crea 3-5 entradas de ejemplo y comprueba operaciones de actualización y borrado en distintos ordenes.
- Prueba casos límite: campos vacíos, valores negativos o tamaños grandes.
- Revisa la persistencia tras reiniciar la app (para validar que Room/H2/archivo escrito funciona).

---

## Funcionalidades implementadas
- Login de usuario (funcionando).
- Registro de usuarios (funcionando).
- Validaciones en formularios (funcionando) — comprobación de campos requeridos, formatos y mensajes de error amistosos.
- Navegación básica implementada (parcial) — pantallas principales enlazadas y flujo principal operativo; quedan por pulir transiciones y rutas secundarias.

---

## Características principales
- Registrar alimentos (nombre, calorías por 100g/porción, categoría).
- Crear comidas sumando calorías de varios alimentos.
- Consultar historial y totales por día.
- API/CLI básica para integración rápida.
- Estructura modular para escalar (persistencia, UI, API REST).

---

## Tecnología
- Lenguaje: Java 11+ (100% del repo)
- Opcional: Maven o Gradle para build y dependencias
- Recomendado para pruebas: JUnit

---

## Requisitos
- Java 11 o superior
- Maven o Gradle (opcional, para builds automatizados)
- Git (para clonar el repo)

---

## Instalación rápida

1. Clona el repositorio:
   git clone https://github.com/Jjuanki/aplicacion_calorias.git
   cd aplicacion_calorias

2. Compilar y ejecutar (opción 1: sin tool de build)
   - Compilar:
     javac -d out $(find src -name "*.java")
   - Ejecutar:
     java -cp out tu.paquete.Principal

3. Compilar y ejecutar con Maven (si el proyecto usa Maven):
   mvn clean package
   java -jar target/aplicacion_calorias.jar

4. Compilar y ejecutar con Gradle (si el proyecto usa Gradle):
   ./gradlew build
   java -jar build/libs/aplicacion_calorias.jar

(Si aún no tienes un pom.xml o build.gradle, puedo ayudarte a crear uno.)

---

## Uso (ejemplo CLI)

- Agregar alimento:
  java -jar aplicacion_calorias.jar add-food --name "Manzana" --calories 52 --unit "100g"

- Crear comida:
  java -jar aplicacion_calorias.jar create-meal --name "Desayuno" --items "Manzana:150g,Cereal:30g,Leche:200ml"

- Consultar total diario:
  java -jar aplicacion_calorias.jar totals --date 2026-06-21

Ejemplo de salida:
{
  "date": "2026-06-21",
  "totalCalories": 650,
  "meals": [
    { "name": "Desayuno", "calories": 350 },
    { "name": "Almuerzo", "calories": 300 }
  ]
}

---

## Estructura sugerida de carpetas
- src/main/java/... (código fuente)
- src/test/java/... (tests)
- data/ (opcional: persistencia simple en JSON/CSV para demo)
- docs/ (documentación adicional)

---

## Cómo contribuir
1. Haz fork del repo.
2. Crea una rama feature/tu-cosa:
   git checkout -b feature/nueva-funcionalidad
3. Haz commits claros y descriptivos.
4. Abre un Pull Request describiendo el cambio.
5. Añade tests si corresponde.

Buenas ideas de contribución:
- Integración con base de datos (H2 / SQLite / Room)
- Interfaz web simple (Spring Boot + Thymeleaf / REST)
- Exportar/importar datos (CSV/JSON)
- App móvil ligera que consuma una API

---

## Contacto
Creado por Jjuanki.
