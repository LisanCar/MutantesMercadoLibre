# 🧬 Mutant Detector API

API REST desarrollada en **Spring Boot** para detectar mutantes basándose en su secuencia de ADN. Proyecto realizado para el desafío técnico de MercadoLibre.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.0-brightgreen.svg)](https://spring.io/)
[![Coverage](https://img.shields.io/badge/Coverage-94%25-success.svg)]()
[![Docker](https://img.shields.io/badge/Docker-Enabled-blue.svg)](https://www.docker.com/)

## 📝 Descripción
Magneto quiere reclutar la mayor cantidad de mutantes para poder luchar contra los X-Men. Este sistema permite detectar si un humano es mutante basándose en su secuencia de ADN.

Se considera mutante si se encuentran **más de una secuencia de cuatro letras iguales**, de forma oblicua, horizontal o vertical.

## 🛠️ Tecnologías Utilizadas
*   **Java 21**: Lenguaje de programación.
*   **Spring Boot 3.3.0**: Framework principal.
*   **H2 Database**: Base de datos en memoria para persistencia de alta velocidad.
*   **Spring Data JPA**: Abstracción de persistencia.
*   **Lombok**: Reducción de código boilerplate.
*   **JUnit 5 & Mockito**: Testing unitario y de integración.
*   **JaCoCo**: Reportes de cobertura de código.
*   **OpenAPI (Swagger)**: Documentación interactiva de la API.
*   **Docker**: Contenedorización de la aplicación.

## ⚡ Optimizaciones y Rendimiento
El algoritmo ha sido diseñado para soportar alta concurrencia y grandes volúmenes de datos:

1.  **Early Termination:** El análisis se detiene inmediatamente al encontrar la segunda secuencia válida, evitando recorrer el resto de la matriz innecesariamente.
2.  **Acceso O(1):** Conversión inicial de `String[]` a `char[][]` para evitar el overhead de métodos de cadena en el bucle principal.
3.  **Caché de Resultados (Base de Datos):** Antes de procesar un ADN, se calcula su Hash (SHA-256). Si el ADN ya fue analizado previamente, se recupera el resultado de la base de datos, saltando todo el proceso algorítmico.
4.  **Índices:** La base de datos cuenta con índices en el hash del ADN para búsquedas instantáneas.

---

## ⚙️ Instrucciones de Ejecución

### 1. Ejecución Local (con Gradle)
Necesitas tener **Java 21** instalado.

```bash
# Clonar el repositorio
git clone <URL_TU_REPO>
cd Mutantes

# Ejecutar la aplicación
./gradlew bootRun       # Linux/Mac
.\gradlew.bat bootRun   # Windows
La aplicación iniciará en: http://localhost:8080
```
### 2. Ejecución con Docker
Si tienes Docker instalado, no necesitas Java.

```bash
# Construir la imagen
docker build -t mutant-api .

# Correr el contenedor
docker run -p 8080:8080 mutant-api
```
### 3. Ejecutar Tests y Cobertura
El proyecto cuenta con una suite de 35 tests que cubren lógica, controladores y servicios.

```bash
# Correr tests
./gradlew test

# Generar reporte de cobertura (JaCoCo)
./gradlew test jacocoTestReport
El reporte HTML se generará en: build/reports/jacoco/test/html/index.html
```

---
## 📖 Documentación de la API
### Swagger UI
Una vez levantada la aplicación, accede a la documentación interactiva:
👉 http://localhost:8080/swagger-ui.html

### Base de Datos (H2 Console)
Para inspeccionar los registros guardados:
👉 http://localhost:8080/h2-console

* **JDBC URL**: jdbc:h2:mem:testdb
* **User**: sa
* **Password**: (dejar vacío)

---

## 🧪 Endpoints Principales

### 1. Detectar Mutante
**POST** `/mutant/`

Envía una secuencia de ADN para ser analizada.

**Request Body:**
```json
{
    "dna": [
        "ATGCGA",
        "CAGTGC",
        "TTATGT",
        "AGAAGG",
        "CCCCTA",
        "TCACTG"
    ]
}
```
Respuestas:

* `200 OK`: Es Mutante.

* `403 Forbidden`: Es Humano.

* `400 Bad Request`: ADN inválido (caracteres erróneos, matriz no cuadrada, nulos).

### 2. Estadísticas
**GET** `/stats`

Devuelve las estadísticas de las verificaciones realizadas.

**Response Body:**

```json
{
    "count_mutant_dna": 40,
    "count_human_dna": 100,
    "ratio": 0.4
}
```
---

## 📷 Diagrama de Secuencia 

![Diagrama de Secuencia de la API](secuencia.png)

---

## 👤 Autor

**[Lisandro Carrillo]** 

**[Universidad Tecnológica Nacional - FRM]**
**[Cátedra: Desarrollo de Software]**

*   **Email:** [carrillo.lisan@gmail.com]
*   **LinkedIn:** [linkedin.com/in/lisandro-carrillo-9b9228233/](linkedin.com/in/lisandro-carrillo-9b9228233/)
*   **GitHub:** [https://github.com/LisanCar](https://github.com/LisanCar)
---
© 2024 - Examen Técnico MercadoLibre
