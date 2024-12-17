# LiterALura - Proyecto de gestión de libros y autores

## Descripción

**LiterALura** es un proyecto desarrollado como parte del curso en Alura LATAM, diseñado para gestionar libros y autores utilizando Spring Boot y JPA. El sistema permite buscar libros desde la API pública de [Gutendex](https://gutendex.com/), guardar información en una base de datos, y generar estadísticas útiles como listar autores vivos en un año específico o contar libros en un idioma.

## Características principales

1. **Buscar libros por título o autor** a través de la API de Gutendex.
2. **Guardar libros** en una base de datos, incluyendo autores y sus detalles.
3. **Listar libros y autores** registrados.
4. **Listar autores vivos** en un año específico utilizando consultas derivadas.
5. **Estadísticas por idioma**, como la cantidad de libros registrados en un idioma determinado.

## Tecnologías utilizadas

- **Java 17**: Lenguaje principal.
- **Spring Boot 3.x**: Framework para el desarrollo de aplicaciones.
- **Spring Data JPA**: Gestión de persistencia.
- **H2 Database**: Base de datos en memoria para desarrollo y pruebas.
- **Gutendex API**: API pública para obtener datos de libros.
- **Maven**: Gestión de dependencias.
- **Jakarta Persistence (JPA)**: Anotaciones para la persistencia de datos.

## Requisitos previos

1. Tener Java 17 instalado en tu sistema.
2. Contar con Maven para gestionar el proyecto.
3. Un IDE compatible con Spring Boot (IntelliJ IDEA, Eclipse, VS Code, etc.).

## Configuración del proyecto

1. Clonar el repositorio:
   ```bash
   git clone https://github.com/tu-usuario/literalura.git
   cd literalura

2. Compilar el proyecto:
   ```bash
   mvn clean install

3. Ejecutar el proyecto:
   ```bash
   mvn spring-boot:run
4. Acceder a la aplicación:
La aplicación se ejecutará en: [http://localhost:8080](http://localhost:8080).

## Funcionalidades

### Buscar libros y guardarlos en la base de datos
- Utiliza la API de Gutendex para buscar libros por título o autor.  
- Los libros se pueden guardar en la base de datos con sus autores e idiomas.

### Listar autores vivos en un año específico
- Recupera todos los autores que estaban vivos en un año dado.  
- Implementado con una consulta derivada para mayor eficiencia.

### Contar libros por idioma
- Muestra estadísticas sobre la cantidad de libros en un idioma registrado en la base de datos.

## Pruebas

Asegúrate de probar los casos límite, como:
- Año inválido para los autores vivos.  
- Libros sin autores asociados.  
- Idiomas no definidos.

## Contribución

1. Realiza un fork del repositorio.  
2. Crea una nueva rama para tus cambios:

   ```bash
   git checkout -b mi-feature

   

