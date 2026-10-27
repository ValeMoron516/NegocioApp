# NegocioApp

Backend de una plataforma de comercio electrónico. El sistema permite gestionar usuarios, negocios, productos, categorías, compras, direcciones y valoraciones, proporcionando una base para conectar una futura interfaz web o aplicación móvil.

## Descripción del proyecto

El objetivo de este proyecto es desarrollar una API REST que permita a los usuarios comprar productos y a los vendedores administrar sus propios negocios dentro de una misma plataforma.

Cada usuario puede tener uno o varios negocios, publicar productos, administrar su stock y recibir valoraciones. Los clientes pueden consultar productos, realizar compras, gestionar sus direcciones y calificar tanto los productos como los negocios.

La aplicación utiliza una base de datos relacional para mantener la información organizada, garantizar la integridad de los datos y representar las relaciones entre las distintas entidades del sistema.

### Objetivos

* Implementar una API REST utilizando Java y Spring Boot.
* Gestionar usuarios y sus respectivos roles.
* Permitir el registro y la administración de múltiples negocios por usuario.
* Administrar productos, categorías, precios y stock.
* Registrar ventas y sus correspondientes detalles.
* Gestionar direcciones de entrega.
* Permitir valoraciones de productos y negocios.
* Mantener la integridad de los datos mediante claves primarias, claves foráneas y restricciones.
* Facilitar la futura integración con un frontend web o móvil.

## Funcionalidades principales

### Gestión de usuarios

* Registro de usuarios.
* Almacenamiento de nombre, apellido, teléfono y correo electrónico.
* Utilización del correo electrónico como identificador de inicio de sesión.
* Almacenamiento de contraseñas mediante hash seguro.
* Asignación de roles y permisos.

### Gestión de negocios

* Registro de negocios asociados a un usuario.
* Posibilidad de administrar varios negocios desde una misma cuenta.
* Almacenamiento del nombre y la descripción del negocio.
* Asociación de productos y ventas con cada negocio.

### Gestión de productos

* Registro y administración de productos.
* Asociación de productos con un negocio.
* Clasificación mediante categorías.
* Gestión de precios y stock disponible.
* Validación de precios y cantidades no negativas.

### Gestión de compras y ventas

* Registro de ventas asociadas a un cliente y un negocio.
* Asociación de una dirección de entrega.
* Almacenamiento del total de la venta.
* Registro de los productos comprados, sus cantidades y sus precios al momento de la compra.
* Seguimiento del estado de cada venta.

### Valoraciones

* Calificación de productos mediante una puntuación del 1 al 5.
* Calificación de negocios mediante una puntuación del 1 al 5.
* Almacenamiento de comentarios y fechas de publicación.

### Categorías

* Organización de productos por categorías.
* Registro de nombres únicos y descripciones.
* Posibilidad de ampliar el catálogo de categorías según las necesidades del marketplace.


## Tecnologías utilizadas

| Tecnología      | Propósito                                                                          |
| --------------- | ---------------------------------------------------------------------------------- |
| Java            | Lenguaje de programación principal.                                                |
| Spring Boot     | Desarrollo y configuración de la aplicación backend.                               |
| Spring Web      | Implementación de endpoints REST, si está incluido como dependencia.               |
| Spring Data JPA | Persistencia y acceso a datos mediante entidades y repositorios, si está incluido. |
| PostgreSQL      | Base de datos relacional.                                                          |
| Maven           | Gestión de dependencias y construcción del proyecto, si se utiliza Maven.          |
| Git y GitHub    | Control de versiones y alojamiento del código fuente.                              |


## Arquitectura y organización del backend

Se propone una arquitectura por capas para separar las responsabilidades y facilitar el mantenimiento del sistema.

```text
Cliente HTTP / Frontend
         |
         v
    Controllers
         |
         v
     Services
         |
         v
    Repositories
         |
         v
      Database
      PostgreSQL
```

**Responsabilidades de cada capa:**

* **Controllers:** reciben las solicitudes HTTP, validan la entrada a nivel de API y devuelven las respuestas correspondientes.
* **Services:** contienen las reglas de negocio, coordinan operaciones y gestionan las transacciones.
* **Repositories:** se encargan del acceso y la persistencia de los datos.
* **Entities / Models:** representan las entidades y relaciones del dominio.
* **DTOs:** permiten definir los datos que se reciben y devuelven en la API, evitando exponer directamente las entidades cuando no corresponde.

Esta organización permite modificar una parte del sistema sin concentrar toda la lógica en los controladores.


## Modelo de datos


### Entidades principales

| Tabla                   | Descripción                                             |
| ----------------------- | ------------------------------------------------------- |
| `usuarios`              | Información personal y credenciales de los usuarios.    |
| `roles`                 | Roles disponibles en la plataforma.                     |
| `usuario_roles`         | Relación entre usuarios y roles.                        |
| `negocios`              | Negocios registrados por los usuarios.                  |
| `categorias`            | Categorías disponibles para clasificar productos.       |
| `productos`             | Productos publicados por los negocios.                  |
| `direcciones`           | Direcciones de entrega asociadas a usuarios.            |
| `ventas`                | Información general de cada compra.                     |
| `detalle_ventas`        | Productos, cantidades y precios incluidos en una venta. |
| `valoraciones_producto` | Calificaciones y comentarios de los productos.          |
| `valoraciones_negocio`  | Calificaciones y comentarios de los negocios.           |

### Relaciones entre entidades

* **Usuarios y roles:** relación muchos a muchos mediante `usuario_roles`.
* **Usuarios y negocios:** relación uno a muchos; un usuario puede administrar varios negocios.
* **Negocios y productos:** relación uno a muchos; cada producto pertenece a un negocio.
* **Categorías y productos:** una categoría puede agrupar múltiples productos.
* **Usuarios y direcciones:** un usuario puede registrar varias direcciones.
* **Usuarios y ventas:** un usuario puede realizar múltiples compras.
* **Negocios y ventas:** cada venta queda asociada a un negocio.
* **Ventas y detalles:** una venta puede incluir múltiples líneas de productos.
* **Productos y detalles de venta:** un producto puede aparecer en diferentes ventas.
* **Clientes y valoraciones:** un cliente puede publicar valoraciones de productos y negocios.


## Decisiones de diseño y justificación

### 1. Uso de una base de datos relacional

Se selecciona PostgreSQL porque permite representar relaciones complejas entre usuarios, negocios, productos y ventas, además de ofrecer restricciones para mantener la consistencia de los datos.

El modelo relacional resulta apropiado para las operaciones comerciales, donde es importante mantener la integridad de las referencias y los importes registrados.

### 2. Identificadores numéricos y claves foráneas

Las tablas utilizan identificadores como claves primarias. En PostgreSQL, `BIGSERIAL` permite generar identificadores numéricos automáticamente.

Las claves foráneas permiten establecer relaciones entre las entidades y evitar referencias a registros inexistentes.

### 3. Relación muchos a muchos entre usuarios y roles

La tabla `usuario_roles` permite asignar uno o varios roles a cada usuario sin duplicar la información del usuario ni del rol.

La clave primaria compuesta por `usuario_id` y `rol_id` evita asignar dos veces el mismo rol al mismo usuario.

### 4. Separación de negocios y usuarios

Los negocios se almacenan en una tabla independiente de los usuarios.

Esta decisión permite que un mismo usuario administre varios negocios y que cada negocio tenga sus propios productos y ventas.

### 5. Separación de ventas y detalles de venta

Se utilizan las tablas `ventas` y `detalle_ventas` para separar los datos generales de una compra de los productos que contiene.

La tabla `ventas` almacena información como el cliente, el negocio, la dirección, el estado y el total. Por su parte, `detalle_ventas` registra cada producto, su cantidad y su precio unitario.

Esto permite representar compras con múltiples productos sin repetir los datos generales de la venta.

### 6. Conservación del precio histórico

La columna `precio_unitario` de `detalle_ventas` registra el precio del producto en el momento de la compra.

De esta manera, una modificación posterior del precio en `productos` no altera el importe histórico de una venta ya registrada.

### 7. Uso de DECIMAL para importes monetarios

Los precios y los totales se representan mediante `DECIMAL(10,2)`.

Este tipo de dato permite trabajar con dos decimales sin utilizar la representación aproximada propia de los tipos de punto flotante.

### 8. Restricciones de integridad

El esquema incorpora restricciones para proteger la consistencia de los datos:

* Correos electrónicos y nombres de categorías únicos.
* Campos obligatorios mediante `NOT NULL`.
* Precios y totales no negativos.
* Stock no negativo.
* Cantidades de compra mayores que cero.
* Valoraciones limitadas al rango del 1 al 5.

Estas restricciones complementan las validaciones que deben realizarse en la capa de servicios.

### 9. Política de eliminación de registros

Se utilizan distintas políticas según la relación:

* `ON DELETE CASCADE` permite eliminar registros dependientes cuando se elimina su registro principal.
* `ON DELETE SET NULL` permite conservar un producto cuando se elimina su categoría, dejando la categoría sin asignar.
* `ON DELETE RESTRICT` impide eliminar determinados registros mientras existan referencias que deban conservarse.

### 10. Contraseñas almacenadas mediante hash

Las contraseñas no deben guardarse en texto plano. Se debe utilizar un algoritmo de hash adecuado para contraseñas, por ejemplo BCrypt, con una configuración segura.

El hash almacenado no debe poder utilizarse como si fuera la contraseña original. La autenticación debe verificar la contraseña proporcionada contra el hash guardado.

### 11. Separación de responsabilidades

La arquitectura por capas permite mantener separadas las reglas de negocio, las solicitudes HTTP y el acceso a la base de datos. Esto facilita las pruebas, el mantenimiento y la ampliación de las funcionalidades.

### 12. Gestión del estado de las ventas

El estado de una venta se almacena como texto en la base de datos. Si se adopta esta alternativa, la aplicación debe validar los valores permitidos.

Como estados previstos se consideran `PENDIENTE`, `PAGADO`, `ENVIADO` y `CANCELADO`. Si el negocio requiere otros estados, como `ENTREGADO`, deben incorporarse de manera consistente en el modelo, las validaciones y la documentación.

## Requisitos previos

Antes de ejecutar el proyecto, es necesario disponer de:

* **Java JDK:** versión compatible con la configurada en el proyecto.
* **PostgreSQL:** servidor de base de datos instalado y en funcionamiento.
* **Maven:** si el proyecto no incluye Maven Wrapper.
* **Git:** para clonar el repositorio.
* Un editor o IDE compatible con Java, como IntelliJ IDEA, Eclipse o Visual Studio Code.

## Cómo poner en marcha el proyecto

### 1. Clonar el repositorio

```bash
git clone https://github.com/ValeMoron516/NegocioApp.git
cd NegocioApp
```

### 2. Crear la base de datos

Desde PostgreSQL, creá una base de datos para el proyecto:

```sql
CREATE DATABASE NegocioApp;
```

Crear tablas de la base de datos:

```sql
-- 1. TABLAS INDEPENDIENTES (No dependen de otras)

CREATE TABLE usuarios (
          id BIGSERIAL PRIMARY KEY,
          nombre VARCHAR(100) NOT NULL,
          apellido VARCHAR(100) NOT NULL,
          telefono VARCHAR(20) NOT NULL,
          email VARCHAR(150) UNIQUE NOT NULL,
          password VARCHAR(255) NOT NULL,
          fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE roles (
          id BIGSERIAL PRIMARY KEY,
          nombre VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE categorias (
          id BIGSERIAL PRIMARY KEY,
          nombre VARCHAR(100) UNIQUE NOT NULL,
          descripcion VARCHAR(255)
);

-- 2. TABLAS INTERMEDIAS Y DE PRIMER NIVEL DE DEPENDENCIA

CREATE TABLE usuario_roles (
          usuario_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          rol_id BIGINT REFERENCES roles(id) ON DELETE CASCADE,
          PRIMARY KEY (usuario_id, rol_id)
);

CREATE TABLE negocios (
          id BIGSERIAL PRIMARY KEY,
          usuario_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          nombre VARCHAR(150) NOT NULL,
          descripcion TEXT
);

CREATE TABLE direcciones (
          id BIGSERIAL PRIMARY KEY,
          usuario_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          calle VARCHAR(150) NOT NULL,
          numero VARCHAR(20) NOT NULL,
          ciudad VARCHAR(100) NOT NULL,
          codigo_postal VARCHAR(20) NOT NULL
);

-- 3. TABLAS DE SEGUNDO NIVEL

CREATE TABLE productos (
          id BIGSERIAL PRIMARY KEY,
          negocio_id BIGINT REFERENCES negocios(id) ON DELETE CASCADE,
          categoria_id BIGINT REFERENCES categorias(id) ON DELETE SET NULL,
          nombre VARCHAR(150) NOT NULL,
          descripcion TEXT,
          precio DECIMAL(10,2) NOT NULL CHECK (precio >= 0),
          stock INTEGER NOT NULL CHECK (stock >= 0)
);

CREATE TABLE ventas (
          id BIGSERIAL PRIMARY KEY,
          negocio_id BIGINT REFERENCES negocios(id) ON DELETE CASCADE,
          usuario_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          direccion_id BIGINT REFERENCES direcciones(id) ON DELETE RESTRICT,
          fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
          estado VARCHAR(50) NOT NULL DEFAULT 'PENDIENTE',
          total DECIMAL(10,2) DEFAULT 0.0 CHECK (total >= 0)
);

-- 4. TABLAS DE TERCER NIVEL 

CREATE TABLE detalle_ventas (
          id BIGSERIAL PRIMARY KEY,
          venta_id BIGINT REFERENCES ventas(id) ON DELETE CASCADE,
          producto_id BIGINT REFERENCES productos(id) ON DELETE RESTRICT,
          cantidad INTEGER NOT NULL CHECK (cantidad > 0),
          precio_unitario DECIMAL(10,2) NOT NULL CHECK (precio_unitario >= 0)
);

CREATE TABLE valoraciones_producto (
          id BIGSERIAL PRIMARY KEY,
          producto_id BIGINT REFERENCES productos(id) ON DELETE CASCADE,
          cliente_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          estrellas INTEGER NOT NULL CHECK (estrellas BETWEEN 1 AND 5),
          comentario TEXT,
          fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE valoraciones_negocio (
          id BIGSERIAL PRIMARY KEY,
          negocio_id BIGINT REFERENCES negocios(id) ON DELETE CASCADE,
          cliente_id BIGINT REFERENCES usuarios(id) ON DELETE CASCADE,
          estrellas INTEGER NOT NULL CHECK (estrellas BETWEEN 1 AND 5),
          comentario TEXT,
          fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

Ingresar datos de prueba:

```sql
BEGIN;

-- 1. TABLAS INDEPENDIENTES
INSERT INTO usuarios (nombre, apellido, telefono, email, password) VALUES
('Juan', 'Pérez', '+541198765432', 'juan.perez@email.com', '$2b$10$wXyZ1'),
('María', 'Gómez', '+541123456789', 'maria.gomez@email.com', '$2b$10$wXyZ2'),
('Carlos', 'Rodríguez', '+541134567890', 'carlos.rod@email.com', '$2b$10$wXyZ3'),
('Ana', 'Martínez', '+541145678901', 'ana.mtz@email.com', '$2b$10$wXyZ4'),
('Pedro', 'López', '+541155556666', 'pedro.lopez@email.com', '$2b$10$wXyZ5'),
('Lucía', 'Fernández', '+541177778888', 'lucia.fer@email.com', '$2b$10$wXyZ6');

INSERT INTO roles (nombre) VALUES
('ADMINISTRADOR'),
('DUEÑO_NEGOCIO'),
('CLIENTE');

INSERT INTO categorias (nombre, descripcion) VALUES
('Electrónica', 'Dispositivos, cables, gadgets y tecnología en general'),
('Indumentaria', 'Ropa, calzado y accesorios para todas las edades'),
('Hogar y Decoración', 'Muebles, iluminación y artículos para el hogar'),
('Gastronomía', 'Alimentos preparados, bebidas y delicias culinarias');


-- 2. TABLAS INTERMEDIAS
INSERT INTO usuario_roles (usuario_id, rol_id) VALUES
(1, 1), -- Juan es Admin
(2, 2), -- María es Dueña de negocio
(3, 3), -- Carlos es Cliente
(4, 3), -- Ana es Cliente
(5, 2), -- Pedro también es Dueño de negocio
(6, 3); -- Lucía es Cliente

INSERT INTO negocios (usuario_id, nombre, descripcion) VALUES
(2, 'TecnoGanga', 'La mejor tecnología al alcance de tu mano (Propiedad de María)'),
(2, 'Sublime Ropa', 'Moda urbana y tendencias actuales (Propiedad de María)'),
(5, 'El Rincón del Sabor', 'Las empanadas más ricas del barrio (Propiedad de Pedro)'),
(5, 'Hogar Dulce Hogar', 'Decoración mágica para tu casita (Propiedad de Pedro)');

INSERT INTO direcciones (usuario_id, calle, numero, ciudad, codigo_postal) VALUES
(3, 'Av. Corrientes', '1234', 'CABA', 'C1043AAL'),
(4, 'Calle Falsa', '123', 'Rosario', 'S2000AAB'),
(3, 'Calle Secundaria', '456', 'CABA', 'C1043AAM'), -- Segunda dirección de Carlos
(6, 'Las Flores', '888', 'Córdoba', 'X5000AAA');


-- 3. TABLAS DE SEGUNDO NIVEL 
INSERT INTO productos (negocio_id, categoria_id, nombre, descripcion, precio, stock) VALUES
(1, 1, 'Auriculares Bluetooth X-20', 'Auriculares con cancelación de ruido', 45000.00, 15),
(1, 1, 'Cargador Rápido 20W', 'Cargador de pared USB-C', 12000.00, 50),
(2, 2, 'Remera Oversize Negra', 'Remera 100% algodón', 18500.00, 30),
(2, 2, 'Pantalón Cargo Verde', 'Pantalón con muchos bolsillos', 35000.00, 12),
(3, 4, 'Docena de Empanadas', 'Carne cortada a cuchillo, ¡riquísimas!', 12000.00, 100),
(4, 3, 'Lámpara de Nochecita', 'Lámpara con forma de estrellita', 25000.00, 10);

INSERT INTO ventas (negocio_id, usuario_id, direccion_id, estado, total) VALUES
(1, 3, 1, 'ENTREGADO', 57000.00), -- Venta en TecnoGanga (negocio 1) para Carlos
(2, 4, 2, 'PENDIENTE', 35000.00), -- Venta en Sublime Ropa (negocio 2) para Ana
(3, 6, 4, 'ENTREGADO', 24000.00), -- Venta en El Rincón del Sabor (negocio 3) para Lucía
(4, 3, 3, 'ENVIADO', 25000.00);   -- Venta en Hogar Dulce Hogar (negocio 4) para Carlos


-- 4. TABLAS DE TERCER NIVEL
INSERT INTO detalle_ventas (venta_id, producto_id, cantidad, precio_unitario) VALUES
-- Detalles de la venta 1 (TecnoGanga)
(1, 1, 1, 45000.00), 
(1, 2, 1, 12000.00), 
-- Detalles de la venta 2 (Sublime Ropa)
(2, 4, 1, 35000.00), 
-- Detalles de la venta 3 (El Rincón del Sabor)
(3, 5, 2, 12000.00), -- Lucía compró 2 docenas de empanadas
-- Detalles de la venta 4 (Hogar Dulce Hogar)
(4, 6, 1, 25000.00); -- Carlos compró la lámpara

INSERT INTO valoraciones_producto (producto_id, cliente_id, estrellas, comentario) VALUES
(1, 3, 5, 'Excelente sonido, la batería dura muchísimo. Muy recomendado.'),
(2, 3, 4, 'Carga rápido, cumple con lo que promete.'),
(5, 6, 5, '¡Las empanadas más ricas que comí en mi vida!');

INSERT INTO valoraciones_negocio (negocio_id, cliente_id, estrellas, comentario) VALUES
(1, 3, 5, 'Atención al cliente super rápida y el envío llegó en el día.'),
(2, 4, 3, 'El producto está bien, pero demoraron un poco en empaquetarlo.'),
(3, 6, 5, 'Llegaron súper calentitas, volveré a comprar.');

COMMIT;
```

### 3. Configurar las credenciales de PostgreSQL

Configurá la conexión de la aplicación con la base de datos.

El proyecto utiliza `application-secret.properties` en `src/main/resources`, una configuración típica sería:

```properties
spring.datasource.url=jdbc:{DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.datasource.driver-class-name=org.postgresql.Driver
```

Los nombres de las propiedades pueden variar según la configuración existente.

Definí las variables de entorno `DB_USERNAME` y `DB_PASSWORD` con las credenciales de PostgreSQL de tu entorno.

### 4. Verificar la configuración del proyecto

Antes de iniciar la aplicación, revisá:

* La versión de Java configurada.
* Las dependencias de Spring Boot.
* La dependencia del driver de PostgreSQL.
* La configuración de conexión a la base de datos.
* Las propiedades de seguridad, si están implementadas.
* La ubicación de los scripts SQL o las migraciones.

### 5. Ejecutar el backend

```bash
mvn spring-boot:run
```

### 6. Comprobar que la aplicación funciona

Consultá los registros de inicio para comprobar que Spring Boot haya arrancado correctamente.

La URL local habitual es:

```text
http://localhost:8080/api/v1/categorias
```

## Endpoints de la API

Los endpoints disponibles pueden encontrarse en `src/main/resources/endpoints`
