Sistema de Gestión de Cobros Automáticos
Sistema API REST desarrollado en Spring Boot para la gestión de cobros automáticos simulados, con procesamiento individual y por lotes, incluyendo trazabilidad completa mediante auditoría.

================================================================================
TECNOLOGÍAS UTILIZADAS
================================================================================

- Java 21
- Spring Boot 3.3.5
- Spring Data JPA (Persistencia)
- Spring Security + JWT (Autenticación)
- SQL Server (Base de datos)
- Hibernate (ORM)
- Lombok (Reducción de código)
- Swagger/OpenAPI (Documentación)
- Maven (Gestión de dependencias)

================================================================================
REQUISITOS PREVIOS
================================================================================

- JDK 21 o superior
- Maven 3.8+
- SQL Server instalado y en ejecución
- Git
- IDE (IntelliJ IDEA, Eclipse, NetBeans)

================================================================================
INSTALACIÓN Y CONFIGURACIÓN
================================================================================

1. CLONAR EL REPOSITORIO
-------------------------
git clone <url-del-repositorio>
cd prueba-tecnica

2. CONFIGURAR BASE DE DATOS
----------------------------
Crear la base de datos:

CREATE DATABASE gestion_cobros;
GO

Ejecutar el script de creación de tablas incluido en el repositorio.

3. CONFIGURAR application.properties
-------------------------------------
Editar src/main/resources/application.properties:

spring.datasource.url=jdbc:sqlserver://localhost;databaseName=gestion_cobros;encrypt=false
spring.datasource.username=sa
spring.datasource.password=TU_PASSWORD

4. COMPILAR Y EJECUTAR
----------------------
mvn clean install
mvn spring-boot:run

La aplicación estará disponible en: http://localhost:8080/umg

5. ACCEDER A SWAGGER UI
------------------------
Documentación interactiva de la API:
http://localhost:8080/umg/swagger-ui/index.html

================================================================================
AUTENTICACIÓN
================================================================================

El sistema utiliza JWT (JSON Web Tokens) para autenticación.

OBTENER TOKEN
-------------
POST /umg/auth/authenticate
Content-Type: application/json

{
  "correo": "jairodll48@gmail.com",
  "password": "123"
}

RESPUESTA:
{
  "jwt": "eyJhbGciOiJIUzI1NiJ9...",
  "correo": "jairodll48@gmail.com",
  "nombre": "Jairo",
  "rol": "administrador"
}

USAR EL TOKEN
-------------
Incluir en todas las peticiones subsecuentes:
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

================================================================================
DECISIONES TÉCNICAS
================================================================================

1. ARQUITECTURA EN CAPAS
-------------------------
Se implementó una arquitectura en capas clara:
- Controllers: Manejo de peticiones HTTP
- Services: Lógica de negocio
- Repositories: Acceso a datos
- DTOs: Transferencia de datos
- Models/Entities: Mapeo objeto-relacional

JUSTIFICACIÓN:
- Separación de responsabilidades
- Facilita el mantenimiento
- Permite testing independiente
- Escalabilidad

2. SPRING DATA JPA CON SQL SERVER
----------------------------------
VENTAJAS:
- Soporte robusto para transacciones ACID
- Excelente para auditoría y trazabilidad
- Integración nativa con Spring Boot
- Constraints a nivel de BD (validación de integridad)

3. JWT PARA AUTENTICACIÓN
-------------------------
VENTAJAS:
- Sin sesión en servidor (escalable)
- Información del usuario en el token
- Firma digital para seguridad
- Expiración automática

4. MANEJO DE ERRORES CENTRALIZADO
----------------------------------
Implementación con GlobalExceptionHandler y @ControllerAdvice

VENTAJAS:
- Respuestas consistentes en toda la API
- Códigos HTTP apropiados
- Mensajes de error claros
- Evita duplicación de código

5. ERRORENUM PARA MENSAJES ESTANDARIZADOS
------------------------------------------
Todos los mensajes de error están centralizados en un enum.
