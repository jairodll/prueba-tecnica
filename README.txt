================================================================================
SISTEMA DE GESTIÓN DE COBROS AUTOMÁTICOS
================================================================================

Sistema API REST desarrollado en Spring Boot para la gestión de cobros 
automáticos simulados, con procesamiento individual y por lotes, incluyendo 
trazabilidad completa mediante auditoría.

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
ENDPOINTS DE LA API
================================================================================

1. CREAR USUARIO/CLIENTE
-------------------------
POST /umg/usuarios/save
Content-Type: application/json

{
  "cui": "3033202650108",
  "nombre": "Juan Pérez",
  "correo": "juan@email.com",
  "password": "123",
  "rol": 1
}

VALIDACIONES:
- CUI/DPI único (no puede haber duplicados)
- Campos obligatorios: cui, nombre, correo, password

RESPUESTA EXITOSA (200 OK):
{
  "idUsuario": 14,
  "cui": "3033202650108",
  "nombre": "Juan Pérez",
  "correo": "juan@email.com",
  "estado": "Activo",
  "fechaCreacion": "2026-02-14"
}

ERROR - CUI DUPLICADO (400 Bad Request):
{
  "success": false,
  "message": "Ya existe un usuario con el CUI/DPI proporcionado",
  "status": 400
}

2. REGISTRAR COBRO
------------------
POST /umg/cobros/save
Content-Type: application/json

{
  "idUsuario": 14,
  "monto": 750.50,
  "moneda": "GTQ",
  "descripcion": "Cobro mensual"
}

VALIDACIONES:
- Monto debe ser mayor a 0
- Usuario debe existir
- Se registra inicialmente en estado PENDIENTE

RESPUESTA EXITOSA (200 OK):
{
  "idCobro": 1,
  "idUsuario": 14,
  "monto": 750.50,
  "moneda": "GTQ",
  "estado": "PENDIENTE",
  "fechaCreacion": "2026-02-14T10:30:00",
  "referenciaExterna": "REF-A7B3C9D1",
  "descripcion": "Cobro mensual"
}

3. PROCESAR COBRO INDIVIDUAL
-----------------------------
POST /umg/cobros/{id}/procesar

REGLAS DE SIMULACIÓN:
- Monto <= 1000 → Estado: PROCESADO
- Monto > 1000 → Estado: FALLIDO

EJEMPLO:
POST /umg/cobros/1/procesar

RESPUESTA (200 OK):
{
  "idCobro": 1,
  "estado": "PROCESADO",
  "fechaProceso": "2026-02-14T11:00:00",
  "monto": 750.50
}

4. PROCESAR LOTE DE COBROS
---------------------------
POST /umg/cobros/lotes/procesar
Content-Type: application/json

{
  "idsCobros": [1, 2, 3, 4, 5]
}

CARACTERÍSTICAS:
- Idempotente: Si un cobro ya fue procesado, no se vuelve a procesar
- Devuelve resumen con totales
- Registra evento en auditoría

RESPUESTA (200 OK):
{
  "total": 5,
  "procesados": 3,
  "fallidos": 2,
  "pendientes": 0,
  "resultados": [
    {
      "idCobro": 1,
      "estadoAnterior": "PENDIENTE",
      "estadoNuevo": "PROCESADO",
      "mensaje": "Procesado exitosamente"
    },
    {
      "idCobro": 2,
      "estadoAnterior": "PROCESADO",
      "estadoNuevo": "PROCESADO",
      "mensaje": "Cobro ya procesado anteriormente"
    }
  ]
}

5. CONSULTAR COBROS POR USUARIO
--------------------------------
GET /umg/cobros/usuario/{id}/cobros

FILTROS OPCIONALES:
- estado: PENDIENTE | PROCESADO | FALLIDO
- desde: Fecha inicio (formato: yyyy-MM-dd)
- hasta: Fecha fin (formato: yyyy-MM-dd)

EJEMPLOS:

Todos los cobros de un usuario:
GET /umg/cobros/usuario/14/cobros

Filtrar por estado:
GET /umg/cobros/usuario/14/cobros?estado=PROCESADO

Filtrar por rango de fechas:
GET /umg/cobros/usuario/14/cobros?desde=2026-02-01&hasta=2026-02-28

Combinación de filtros:
GET /umg/cobros/usuario/14/cobros?estado=PROCESADO&desde=2026-02-01&hasta=2026-02-28

OTROS ENDPOINTS:
----------------
GET /umg/cobros/listCobros - Listar todos los cobros
GET /umg/cobros/{id} - Obtener cobro por ID
GET /umg/cobros/estado/{estado} - Listar cobros por estado
GET /umg/cobros/usuario/{idUsuario} - Listar cobros por usuario

================================================================================
EJEMPLOS DE USO COMPLETOS
================================================================================

CASO 1: REGISTRAR Y PROCESAR UN COBRO EXITOSO
----------------------------------------------

1. Autenticarse
POST /umg/auth/authenticate
{ "correo": "admin@email.com", "password": "123" }

2. Crear usuario/cliente
POST /umg/usuarios/save
{ "cui": "1234567890101", "nombre": "María García", ... }

3. Registrar cobro
POST /umg/cobros/save
{ "idUsuario": 15, "monto": 500.00, "moneda": "GTQ" }

4. Procesar el cobro
POST /umg/cobros/1/procesar
Resultado: estado = "PROCESADO" (porque 500 <= 1000)

5. Consultar cobros del usuario
GET /umg/cobros/usuario/15/cobros?estado=PROCESADO

CASO 2: PROCESAMIENTO POR LOTES CON IDEMPOTENCIA
-------------------------------------------------

1. Crear varios cobros
POST /umg/cobros/save { "idUsuario": 14, "monto": 300, ... }
POST /umg/cobros/save { "idUsuario": 14, "monto": 1500, ... }
POST /umg/cobros/save { "idUsuario": 14, "monto": 800, ... }

2. Procesar todos en lote
POST /umg/cobros/lotes/procesar
{ "idsCobros": [10, 11, 12] }

Resultado:
- Cobro 10: PROCESADO (300 <= 1000)
- Cobro 11: FALLIDO (1500 > 1000)
- Cobro 12: PROCESADO (800 <= 1000)

3. Ejecutar el mismo lote de nuevo (Idempotencia)
POST /umg/cobros/lotes/procesar
{ "idsCobros": [10, 11, 12] }

Resultado: Ningún cobro se vuelve a procesar
Mensaje: "Cobro ya procesado anteriormente"

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

VENTAJAS:
- Mensajes consistentes
- Fácil mantenimiento
- Código de error único por tipo
- Estado HTTP asociado

EJEMPLO:
C_CUI_DUPLICADO(6000, BAD_REQUEST, "Ya existe un usuario con el CUI/DPI...")

6. SISTEMA DE AUDITORÍA AUTOMÁTICO
-----------------------------------
Cada operación importante se registra en la tabla de auditoría.

EVENTOS AUDITADOS:
- Creación de usuarios
- Registro de cobros
- Procesamiento individual
- Procesamiento por lotes

VENTAJAS:
- Trazabilidad completa
- Debugging facilitado
- Cumplimiento normativo
- Análisis de operaciones

7. PROCESAMIENTO IDEMPOTENTE DE LOTES
--------------------------------------
El endpoint de procesamiento por lotes es idempotente.

IMPLEMENTACIÓN:
if (!"PENDIENTE".equals(cobro.getEstado())) {
    return cobroYaProcesado;
}

VENTAJAS:
- Seguro ante reintentos
- Sin efectos secundarios duplicados
- Resiliencia ante fallos de red

8. VALIDACIONES EN MÚLTIPLES NIVELES
-------------------------------------
1. DTOs: Validaciones con Jakarta Bean Validation
2. Servicios: Validaciones de negocio
3. Base de Datos: Constraints (UNIQUE, CHECK, FK)

EJEMPLO:
// Nivel Service
if (monto <= 0) throw new IllegalArgumentException(...);

// Nivel BD
CONSTRAINT CHK_cobros_monto CHECK (monto > 0)

================================================================================
TRADE-OFFS
================================================================================

1. SIMPLICIDAD VS ESCALABILIDAD
--------------------------------
DECISIÓN: Implementación simple con procesamiento síncrono

ALTERNATIVA CONSIDERADA: Cola de mensajes (RabbitMQ, Kafka) para 
procesamiento asíncrono

JUSTIFICACIÓN:
- Para el alcance de una prueba técnica, la complejidad adicional no está 
  justificada
- En producción con alto volumen, se recomendaría procesamiento asíncrono
- Más fácil de entender y mantener
- Suficiente para volúmenes medios

CUÁNDO CAMBIAR: Si se procesan >1000 cobros/minuto

2. REUTILIZACIÓN VS SEPARACIÓN
-------------------------------
DECISIÓN: Usar tabla 'usuarios' existente como clientes

ALTERNATIVA CONSIDERADA: Crear tabla 'clientes' separada

JUSTIFICACIÓN:
- Aprovecha infraestructura existente
- Menos complejidad
- Los usuarios ya tienen CUI/DPI
- En un sistema real grande, deberían estar separados

CUÁNDO CAMBIAR: Si clientes y usuarios tienen ciclos de vida diferentes

3. AUDITORÍA EN BD VS SISTEMA EXTERNO
--------------------------------------
DECISIÓN: Tabla de auditoría en la misma base de datos

ALTERNATIVA CONSIDERADA: Sistema de logging centralizado (ELK Stack, Splunk)

JUSTIFICACIÓN:
- Más simple de implementar
- Queries SQL directas
- Transacciones ACID
- Un sistema de logging centralizado sería más robusto para gran escala

CUÁNDO CAMBIAR: Si se generan >100k eventos de auditoría por día

4. ESTADOS DE COBRO FIJOS VS CONFIGURABLES
-------------------------------------------
DECISIÓN: Estados fijos (PENDIENTE, PROCESADO, FALLIDO)

ALTERNATIVA CONSIDERADA: Tabla 'estados_cobro' configurable

JUSTIFICACIÓN:
- Más simple
- Estados bien definidos por negocio
- Validación con CHECK constraint
- Menos flexible

CUÁNDO CAMBIAR: Si el negocio requiere estados dinámicos

================================================================================
BASE DE DATOS
================================================================================

TABLAS PRINCIPALES:

1. usuarios: Información de clientes/usuarios
2. cobros: Registros de cobros
3. auditoria: Trazabilidad de eventos
4. roles: Roles del sistema
5. usuario_roles: Relación usuarios-roles

DIAGRAMA DE RELACIONES:

usuarios (1) ----< (N) cobros
    |
    | (N)
    |
usuario_roles
    |
    | (N)
    |
roles (1)

================================================================================
ESTRUCTURA DEL PROYECTO
================================================================================

src/
├── main/
│   ├── java/
│   │   └── gt/umg/gestionCobros/
│   │       ├── controllers/
│   │       │   ├── usuarioController.java
│   │       │   ├── cobroController.java
│   │       │   └── authController.java
│   │       ├── dtos/
│   │       │   ├── usuariodto.java
│   │       │   ├── cobrodto.java
│   │       │   └── loteCobrodto.java
│   │       ├── models/
│   │       │   ├── usuarios.java
│   │       │   ├── cobros.java
│   │       │   └── auditoria.java
│   │       ├── repositories/
│   │       │   ├── usuarioRepository.java
│   │       │   ├── cobroRepository.java
│   │       │   └── auditoriaRepository.java
│   │       ├── services/
│   │       │   ├── usuarioSvcImpl.java
│   │       │   ├── cobroSvcImpl.java
│   │       │   └── auditoriaSvcImpl.java
│   │       ├── security/
│   │       │   ├── WebSecurityConfig.java
│   │       │   ├── JWTUtil.java
│   │       │   └── JwtFilterRequest.java
│   │       └── exceptions/
│   │           ├── ErrorEnum.java
│   │           └── GlobalExceptionHandler.java
│   └── resources/
│       └── application.properties

================================================================================
AUTOR
================================================================================

Jairo De León
Email: jairodll48@gmail.com
Fecha: Febrero 2026

================================================================================
LICENCIA
================================================================================

Este proyecto es una prueba técnica educativa.

================================================================================
FIN DEL DOCUMENTO
================================================================================
