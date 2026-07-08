### Registrar venta

- **URL:**`~/api/v1/ventas`
- **Método HTTP:** `POST`
- **Descripción:** Registra una nueva venta.
- **Request Body:**

```json
{
  "usuarioId": 1,
  "direccionId": 1,
  "​fechaVenta": "2026-5-26",
  "estado": "pendiente",
  "total": 25000.0
}
```

- **Respuestas HTTP:**
  `201 create`-> La nueva venta ha sido creada sin problemas. Retorna el objeto de la venta con su ID asignado.
  - **Request Body:**

```json
{
  "usuarioId": 1,
  "direccionId": 1,
  "​fechaVenta": "2026-5-26",
  "estado": "pendiente",
  "total": 25000.0
}
```

`400 Bad Request`-> La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos
`500 Internal Server Error` -> Error interno en el servidor o problemas con la base de datos

####################################################################

### listar ventas por id

- **URL:**`~/api/v1/ventas/{id}`
- **Método HTTP:** `GET`
- **Descripción:** Devuelve la venta que tiene un id en particular
- **Respuestas HTTP:**
  `200 Ok`->Se pudo listar las ventas del ID asignado
  - **Request Body:**

```json
{
  "usuarioId": 1,
  "direccionId": 1,
  "​fechaVenta": "2026-5-26",
  "estado": "pendiente",
  "total": 25000.0
}
```

`400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
`404 Not Found`-> No existe el ID ingresado por el usuario
`500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

###############################################################

### listar el total de ventas registardas

- **URL:**`~/api/v1/ventas`
- **Método HTTP:** `GET`
- **Descripción:** Devuelve todas las ventas registradas
- **Respuestas HTTP:**
  `200 Ok`->Se pudo listar las ventas en su totalidad
  - **Request Body:**

  ```json
  [
    {
      "usuarioId": 1,
      "direccionId": 1,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 25000.0
    },
    {
      "usuarioId": 2,
      "direccionId": 2,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 12000.0
    }
  ]
  ```

  `400 Bad Request`-> La peticion del usuario tiene la sintaxis incorrecta
  `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

###################################################################

### listar el total de ventas registardas en un lapso de tiempo

- **URL:**`~/api/v1/ventas/buscar?fecha_desde=2026-05-01&fecha_hasta=2026-05-31`
- **Método HTTP:** `GET`
- **Descripción:** Devuelve todas las ventas registradas entre una fecha y otra.
- **Respuestas HTTP:**
  `200 Ok`->Se pudo listar las ventas en rango que espesifico el usuario
  - **Request Body:**
  ```json
  {
    "usuarioId": 1,
    "direccionId": 1,
    "​fechaVenta": "2026-5-26",
    "estado": "pendiente",
    "total": 25000.0
  }
  ```
  `400 Bad Request`-> La petición del usuario tiene sintaxis incorrecta o las fechas no tienen el formato válido (YYYY-MM-DD), o la fecha de inicio es mayor a la fecha de fin.
  `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

####################################################################

### Listar ventas de un usuario

- **URL:**`~/api/v1/usuarios/{id}/ventas`
- **Método HTTP:** `GET`
- **Descripción:** Devuelve todas las ventas registradas de un usuario en particular identificado con el id correspondiene a la misma
- **Respuestas HTTP:**
  `200 Ok`->Se pudo listar las ventas de un usuario en particular sin problemas
  - **Request Body:**

  ```json
  [
    {
      "usuarioId": 1,
      "direccionId": 1,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 25000.0
    },
    {
      "usuarioId": 1,
      "direccionId": 1,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 12000.0
    }
  ]
  ```

  `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
  `404 Not Found`-> No existe el ID ingresado por el usuario
  `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

###################################################################

### Obtener total de ventas de un negocio

- **URL:** `~/api/v1/negocios/{id}/ventas/total`
- **Método HTTP:** `GET`
- **Descripción:** Devuelve el número total de ventas registradas de un negocio en particular, identificado con el ID correspondiente al mismo.
- **Respuestas HTTP:**
  `200 OK`: Se pudo obtener el total de ventas del negocio sin problemas.
  - **Request Body:**

  ```json
  [
    {
      "usuarioId": 1,
      "direccionId": 1,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 25000.0
    },
    {
      "usuarioId": 1,
      "direccionId": 1,
      "​fechaVenta": "2026-5-26",
      "estado": "pendiente",
      "total": 12000.0
    }
  ]
  `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
  `404 Not Found`: No existe un negocio con el ID proporcionado.
  `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.
  ```

  ####################################################################

### Eliminar una venta por id

- **URL:**`~/api/v1/ventas/{id}`
- **Método HTTP:** `DELETE`
- **Descripción:** Eliminar una venta por id
- **Respuestas HTTP:**
  `204 No Content`->Se pudo Borrar las ventas y no devuele ningun contenido
  `400 Bad Request`: El ID proporcionado no tiene un formato numérico válido.
  `404 Not Found`-> No existe el ID ingresado por el usuario
  `500 Internal Server Error`: Error interno del servidor o problema con la base de datos.

####################################################################

### Actualizar venta

**URL:** `~/api/v1/ventas/{id}`

- **Método HTTP:** `PUT`
- **Descripción:** Actualiza la venta en su totalidad de sus datos.
- **Request Body:**

  ```json
  {
    "usuarioId": 1,
    "direccionId": 1,
    "fechaVenta": "2026-05-26",
    "estado": "cancelado",
    "total": 25000.0
  }
  ```

- **Respuestas HTTP:**
  `200 OK`: Se pudo actualizar la venta por su ID en particular sin problemas.
  - **Request Body:**

  ```json
  {
    "usuarioId": 2,
    "direccionId": 2,
    "fechaVenta": "2026-06-26",
    "estado": "Pendiente",
    "total": 18000.0
  }
  ```

  `400 Bad Request`: La petición del usuario tiene sintaxis incorrecta o los datos enviados no son válidos.
  `404 Not Found`: No existe una venta con el ID proporcionado.
  `500 Internal Server Error`: Error interno en el servidor o problemas con la base de datos.
