### Trae todas las valoraciones de negocios
*   **URL:** `/api/v1/valoraciones-negocios`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene todas las valoraciones registradas en el sistema.
*   **Response Body:**
    ```json
    [{
    "id": 1,
    "estrellas": 5,
    "comentario": "Excelente atención",
    "fecha": "2025-06-23T10:30:00"
    },
    {
    "id": 2,
    "estrellas": 4,
    "comentario": "Muy recomendable",
    "fecha": "2025-06-23T11:15:00"
    }
    ]

### Respuestas:
-   **200 OK**: La solicitud se realizó con éxito.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Crear una nueva valoración de negocio
*   **URL:** `/api/v1/valoracion-negocio`
*   **Método HTTP:** `POST`
*   **Descripción:** Registra una nueva valoración realizada por un cliente hacia un negocio.
*   **Request Body**
    ```json
    [{
        "negocioId": 3,
        "clienteId": 8,
        "estrellas": 5,
        "comentario": "Excelente atención y servicio."
        }
        ]

#### Respuestas:
-   **201 Created**: La valoración fue creada correctamente.
-   **400 Bad Request**: El cuerpo de la solicitud contiene datos inválidos o incompletos.
-   **404 Not Found**: El negocio o el cliente especificado no existen.
-   **409 Conflict**: El cliente ya registró una valoración para ese negocio.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener una valoración de negocio por ID
*   **URL:** `/api/v1/valoracion-negocio/{id}`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene la información de una valoración específica mediante su identificador.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **200 OK**: La valoración fue encontrada correctamente.
-   **404 Not Found**: No existe una valoración con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Actualizar una valoración de negocio
*   **URL:** `/api/v1/valoracion-negocio/{id}`
*   **Método HTTP:** `PUT`
*   **Descripción:** Actualiza la información de una valoración previamente registrada.
*   **Parametro de la URL:** `id`
*   **Request Body:**
    ```json
    [{
        "estrellas": 4,
        "comentario": "Muy buena atención, aunque hubo una pequeña demora."
        }
        ]

#### Respuestas:
-   **200 OK**: La valoración fue actualizada correctamente.
-   **400 Bad Request**: Los datos enviados no cumplen con las validaciones requeridas.
-   **404 Not Found**: No existe la valoración indicada.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Eliminar una valoración de negocio
*   **URL:** `/api/v1/valoracion-negocio/{id}`
*   **Método HTTP:** `DELETE`
*   **Descripción:** Elimina una valoración registrada en el sistema.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **204 No Content**: La valoración fue eliminada correctamente.
-   **404 Not Found**: No existe una valoración con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener todas las valoraciones de un negocio
*   **URL:** `/api/v1/negocio/{id}/valoraciones`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene todas las valoraciones realizadas por los clientes sobre un negocio específico.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **200 OK**: Las valoraciones del negocio fueron obtenidas correctamente.
-   **204 No Content**: El negocio no posee valoraciones registradas.
-   **404 Not Found**: No existe un negocio con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener todas las valoraciones de un negocio
*   **URL:** `/api/v1/cliente/{id}/valoraciones-negocios`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene todas las valoraciones de negocios realizadas por un cliente determinado.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **200 OK**: Las valoraciones del cliente fueron obtenidas correctamente.
-   **204 No Content**: El cliente no posee valoraciones registradas.
-   **404 Not Found**: No existe un cliente con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.