### Obtener lista de valoraciones de negocios
* **URL:** `/api/v1/valoraciones-negocios`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista paginada de todas las valoraciones de negocios registradas en el sistema.
* **Parámetros de consulta (Query Params):**
  * `page` (entero, opcional): Número de página a consultar (por defecto `1`).
  * `limit` (entero, opcional): Cantidad de registros por página (por defecto `20`).

#### Ejemplo de Petición Completa
`GET /api/v1/valoraciones-negocios?page=1&limit=20`

#### Respuestas
* **Código:** `200 OK` (Éxito con resultados)
* **Response Body:**
```json
{
    "data": [
        {
            "id": 1,
            "negocioId": 3,
            "clienteId": 8,
            "estrellas": 5,
            "comentario": "Excelente atención.",
            "fecha": "2026-07-07T18:30:00"
        },
        {
            "id": 2,
            "negocioId": 5,
            "clienteId": 10,
            "estrellas": 4,
            "comentario": "Muy recomendable.",
            "fecha": "2026-07-07T19:15:00"
        }
    ],
    "meta": {
        "totalItems": 2,
        "itemCount": 2,
        "itemsPerPage": 20,
        "totalPages": 1,
        "currentPage": 1
    }
}
```
* **Código:** `200 OK` (Éxito sin resultados)
* **Response Body**
```json
{
    "data": [],
    "meta": {
        "totalItems": 0,
        "itemCount": 0,
        "itemsPerPage": 20,
        "totalPages": 0,
        "currentPage": 1
    }
}
```

* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Obtener valoración de negocio por ID
* **URL:** `/api/v1/valoraciones-negocios/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene la información de una valoración de negocio mediante su identificador.

* **Parámetros de la URL (Path Params):**
  * `id` (Long): Identificador único de la valoración.

#### Ejemplo de Petición Completa
`GET /api/v1/valoraciones-negocios/1`

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "id": 1,
    "negocioId": 3,
    "clienteId": 8,
    "estrellas": 5,
    "comentario": "Excelente atención.",
    "fecha": "2026-07-07T18:30:00"
}
```

* **Código:** `404 Not Found` (La valoración no existe.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Crear una valoración de negocio
* **URL:** `/api/v1/valoraciones-negocios`
* **Método HTTP:** `POST`
* **Descripción:** Registra una nueva valoración realizada por un cliente hacia un negocio.

* **Request Body:**

```json
{
    "negocioId": 3,
    "clienteId": 8,
    "estrellas": 5,
    "comentario": "Excelente atención y servicio."
}
```

#### Respuestas

* **Código:** `201 Created`

* **Response Body:**

```json
{
    "id": 15,
    "negocioId": 3,
    "clienteId": 8,
    "estrellas": 5,
    "comentario": "Excelente atención y servicio.",
    "fecha": "2026-07-07T20:15:00"
}
```

* **Código:** `400 Bad Request` (Datos enviados inválidos o mal formados.)
* **Código:** `404 Not Found` (El negocio o el cliente no existen.)
* **Código:** `409 Conflict` (El cliente ya registró una valoración para ese negocio.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Actualizar una valoración de negocio
* **URL:** `/api/v1/valoraciones-negocios/{id}`
* **Método HTTP:** `PATCH`
* **Descripción:** Actualiza la información de una valoración existente.

* **Parámetros de la URL (Path Params):**
  * `id` (Long): Identificador de la valoración.

* **Request Body:**
```json
{
    "estrellas": 4,
    "comentario": "Muy buena atención."
}
```

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "id": 15,
    "negocioId": 3,
    "clienteId": 8,
    "estrellas": 4,
    "comentario": "Muy buena atención.",
    "fecha": "2026-07-07T20:15:00"
}
```

* **Código:** `400 Bad Request` (Datos enviados inválidos.)
* **Código:** `404 Not Found` (La valoración no existe.)
* **Código:** `409 Conflict` (No fue posible actualizar el recurso debido a un conflicto.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Eliminar una valoración de negocio
* **URL:** `/api/v1/valoraciones-negocios/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina una valoración de negocio registrada en el sistema.

* **Parámetros de la URL (Path Params):**
  * `id` (Long): Identificador de la valoración.

#### Ejemplo de Petición Completa
`DELETE /api/v1/valoraciones-negocios/15`

#### Respuestas
* **Código:** `204 No Content` (La valoración fue eliminada correctamente.)
* **Código:** `404 Not Found` (La valoración no existe.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Obtener todas las valoraciones de un negocio
* **URL:** `/api/v1/negocios/{id}/valoraciones`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene todas las valoraciones realizadas sobre un negocio específico.

* **Parámetros de la URL (Path Params):**
  * `id` (Long): Identificador del negocio.

#### Ejemplo de Petición Completa
`GET /api/v1/negocios/3/valoraciones`

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "data": [
        {
            "id": 1,
            "clienteId": 8,
            "estrellas": 5,
            "comentario": "Excelente atención.",
            "fecha": "2026-07-07T18:30:00"
        },
        {
            "id": 2,
            "clienteId": 15,
            "estrellas": 4,
            "comentario": "Muy recomendable.",
            "fecha": "2026-07-08T10:20:00"
        }
    ]
}
```

* **Código:** `200 OK` (El negocio no posee valoraciones.)
```json
{
    "data": []
}
```
* **Código:** `404 Not Found` (El negocio no existe.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Obtener todas las valoraciones realizadas por un cliente
* **URL:** `/api/v1/clientes/{id}/valoraciones-negocios`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene todas las valoraciones de negocios realizadas por un cliente específico.

* **Parámetros de la URL (Path Params):**

  * `id` (Long): Identificador único del cliente.

#### Ejemplo de Petición Completa
`GET /api/v1/clientes/8/valoraciones-negocios`

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "data": [
        {
            "id": 1,
            "negocioId": 3,
            "estrellas": 5,
            "comentario": "Excelente atención.",
            "fecha": "2026-07-07T18:30:00"
        },
        {
            "id": 7,
            "negocioId": 5,
            "estrellas": 4,
            "comentario": "Muy recomendable.",
            "fecha": "2026-07-08T10:20:00"
        }
    ]
}
```

* **Código:** `200 OK` (El negocio no posee valoraciones.)
```json
{
    "data": []
}
```
* **Código:** `404 Not Found` (El cliente no existe.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`

##################################################

### Obtener valoraciones según la cantidad de estrellas
* **URL:** `/api/v1/valoraciones-negocios/estrellas/{estrellas}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene todas las valoraciones de negocios que poseen una determinada cantidad de estrellas.

* **Parámetros de la URL (Path Params):**
  * `estrellas` (Integer): Cantidad de estrellas por la cual se desea filtrar. El valor debe estar comprendido entre 1 y 5.

#### Ejemplo de Petición Completa
`GET /api/v1/valoraciones-negocios/estrellas/5`

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "data": [
        {
            "id": 1,
            "negocioId": 3,
            "clienteId": 8,
            "estrellas": 5,
            "comentario": "Excelente atención.",
            "fecha": "2026-07-07T18:30:00"
        },
        {
            "id": 10,
            "negocioId": 6,
            "clienteId": 15,
            "estrellas": 5,
            "comentario": "Excelente servicio.",
            "fecha": "2026-07-08T15:45:00"
        }
    ]
}
```

* **Código:** `200 OK` (No existen valoraciones con esa cantidad de estrellas.)
```json
{
    "data": []
}
```
* **Código:** `400 Bad Request` (La cantidad de estrellas debe estar entre 1 y 5.)
* **Código:** `500 Internal Server Error`
* **Código:** `503 Service Unavailable`