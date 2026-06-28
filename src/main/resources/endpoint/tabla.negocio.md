##Crear un nuevo negocio en la app
**URL:**`~/api/v1/negocio`
**Método HTTP:** `POST`
**Descripción:** Registra un nuevo negocio.
**Request Body:**
```json
    {
      "usuario_id": 1,
      "nombre": "Pintureria Lucci",
      "descripcion": "Pintureria en general, insumos y todo tipos de productos para tu casa"
    }
**Respuestas HTTP:**
    `201 create`-> El nuevo negocio ha sido creada sin problemas. Retorna el objeto negocio con su ID asignado.
    `400 Bad Request`-> La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos
    `500 Internal Server Error` -> Error interno en el servidor o problemas con la base de datos

##Buscar negocios por nombre
**URL:**`~/api/v1/negocio/buscar?nombre={texto}`
**Método HTTP:** `GET`
**Descripción:** Devuelve el negocio por su nombre
**Request Body:**
**Respuestas HTTP:**
    `200 Ok`->Se pudo buscar negocio por su nombre o parte de este
    `400 Bad Request`-> La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos
    `404 Not Found`-> No existe el nombre o parte del nombre  ingresado por el usuario
    `500 Internal Server Error` -> Error interno en el servidor o problemas con la base de datos

##Buscar negocios por producto
**URL:**`~/api/v1/negocio/buscar?producto={nombre}`
**Método HTTP:** `GET`
**Descripción:** Devuelve todos los negocios que venden un producto cuyo nombre coincida (parcial o exactamente) con el término de búsqueda.
**Request Body:**
    `200 Ok`->Se pudo buscar negocios por el producto vende
    `400 Bad Request`-> La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos
    `404 Not Found`-> No existe el producto ingresado por el usuario
    `500 Internal Server Error` -> Error interno en el servidor o problemas con la base de datos

##listar un negocio por su id
**URL:**`~/api/v1/negocio/{id}`
**Método HTTP:** `GET`
**Descripción:** Devuelve el negocio que tiene un id en particular
**Request Body:**
    `200 Ok`->Se pudo listar los negocios del ID asignado
    `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
    `404 Not Found`-> No existe el ID ingresado por el usuario
    `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

##listar todos los negocios de un usuario en particular
**URL:**`~/api/v1/usuarios/{id}/negoscio`
**Método HTTP:** `GET`
**Descripción:** Devuelve todos los negocios registrados por un usuario en particular.
**Request Body:**
    `200 Ok`->Se pudo listar todos los negocios que pertenecen a usuario del ID asignado
    `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
    `404 Not Found`-> No existe el ID ingresado por el usuario
    `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

##Eliminar un negocio por id
**URL:**`~/api/v1/negocio/{id}`
**Método HTTP:** `DELETE`
**Descripción:** Eliminar un negocio por id
**Request Body:**
**Respuestas HTTP:**
    `204 No Content`->Se pudo Borrar el negocio con el ID asignado y no devuele ningun contenido
    `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
    `404 Not Found`-> No existe el ID ingresado por el usuario
    `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

##Actualizar nombre y descripcion de un negocio
**URL:**`~/api/v1/negocio/{id}`
**Método HTTP:** `PUT`
**Descripción:** Actualiza el nombre y descripcion de un negocio.
**Request Body:**
```json
    {
      "nombre": "Pintureria Lucci",
      "descripcion": "Pintureria en general, insumos y todo tipos de productos para tu casa"
    }
 **Respuestas HTTP:**
   `200 OK`: Se pudo actualizar el negocio por su ID en particular sin problemas.
   `400 Bad Request`: La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos.
   `404 Not Found`: No existe un negocio con el ID proporcionado.
   `500 Internal Server Error`: Error interno en el servidor o problemas con la base de datos.