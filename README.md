# Aplicación Calorías 🥗🔥

Una aplicación en Java para calcular y gestionar calorías de alimentos y comidas. Sencilla, extensible y lista para convertirse en tu asistente de seguimiento de energía diaria.

> Descripción
> Una herramienta para registrar alimentos, calcular calorías por porción y obtener reportes sencillos. Ideal como proyecto educativo, demo o base para una app más grande.

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
Suponiendo que `Main` es el punto de entrada:

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
- Integración con base de datos (H2 / SQLite)
- Interfaz web simple (Spring Boot + Thymeleaf / REST)
- Exportar/importar datos (CSV/JSON)
- App móvil ligera que consuma una API

## Contacto
Creado por Jjuanki.
