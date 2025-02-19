# Boats-Rental

## Guía de Configuración Local

Este documento proporciona instrucciones paso a paso para configurar y ejecutar la aplicación Boats-Rental localmente.

## Prerrequisitos

* Java Development Kit (JDK)
* Maven (o Gradle)
* PostgreSQL
* Git
* Un cliente SQL (psql, DBeaver, etc.)


## Pasos de Configuración

1. **Clonar el Repositorio:**

```bash
git clone [URL del repositorio Git]

```

2. **Configurar la Base de Datos:**

Crea una base de datos PostgreSQL local con el nombre boats_rentals.

3. **Ejecutar los scriptsSQL:**

Conéctate a tu base de datos PostgreSQL usando cliente SQL como psql y ejecuta los siguientes scripts en el que estan dentro de src/main/resources  en el siguiente orden:

### create_tables.sql
### create_functions_triggers.sql
### insert_data.sql

Desde un cliente gráfico: Abre cada archivo .sql en tu cliente y ejecútalo.

4. **Configurar tu aplicación:**

La configuración de la base de datos se realiza generalmente en el archivo application.properties o
application.yml dentro del directorio src/main/resources en tu proyecto Spring Boot:

```conf
spring.datasource.url=jdbc:postgresql://localhost:5432/[nombre_de_la_base_de_datos]
spring.datasource.username=[usuario_de_la_base_de_datos]
spring.datasource.password=[contraseña_de_la_base_de_datos]
```

5. **Comiplar y Ejecutar la aplicacion:**

# windows: 
    ```bash
        ./mvnw.cmd clean package 
        java -jar boats-rental-0.0.1-SNAPSHOT.jar
    ```
# linux:
    ```bash
        ./mvnw clean package 
        java -jar boats-rental-0.0.1-SNAPSHOT.jar
    ```
6. **Verificar la Ejecución y Documentacion de la API:**

Accede a la aplicación en la URL que se muestre en la consola (generalmente http://localhost:8080).

http://localhost:8080/swagger-ui/index.html (Ajusta el puerto si es diferente)

7. **Funcionalidad Adicional**

# Creación Automática de Usuario Administrador:
Al iniciar la aplicación, se crea automáticamente un usuario administrador con las siguientes credenciales:

Email: admin@example.com
Contraseña: adminpassword
Este usuario tiene permisos de administrador y puede gestionar la aplicación.

# Confirmación de Rentas por Email:
Cuando se solicita una renta, esta se confirma a través de un correo electrónico que se envía al usuario. Asegúrate de configurar correctamente las credenciales de correo electrónico en las variables de entorno para que esta funcionalidad funcione.

# Cancelación de Rentas
Una renta solo se puede cancelar con al menos 2 días de anticipación. Si intentas cancelar una renta con menos de 2 días de anticipación, recibirás un mensaje de error indicando que la cancelación no es posible.