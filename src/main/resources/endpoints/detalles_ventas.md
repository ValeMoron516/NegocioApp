### Agregar un producto a una venta
*   **URL:** `/api/v1/detalle-venta/{id}`
*   **Método HTTP:** `POST`
*   **Descripción:** Agrega un producto al detalle de una venta existente, registrando la cantidad comprada y el precio unitario correspondiente al momento de la compra.
*   **Request Body:**
   ```json
   [{
    "ventaId": 10,
    "productoId": 5,
    "cantidad": 2
    }
    ]

#### Respuestas:
-   **201 Created**: El producto fue agregado correctamente a la venta.
-   **400 Bad Request**: El cuerpo de la solicitud contiene datos inválidos o incompletos.
-   **404 Not Found**: La venta o el producto especificado no existen.
-   **409 Conflict**: No hay stock suficiente para realizar la operación.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener todos los detalles de venta
*   **URL:** `/api/v1/detalles-ventas`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene el listado completo de todos los detalles de ventas registrados en el sistema.

#### Respuestas:
-   **200 OK**: Los detalles de ventas fueron obtenidos correctamente.
-   **204 No Content**: No existen detalles de ventas registrados.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener un detalle de venta por ID
*   **URL:** `/api/v1/detalle-venta/{id}`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene la información de un detalle de venta específico mediante su identificador.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **200 OK**: El detalle de venta fue encontrado correctamente.
-   **404 Not Found**: No existe un detalle de venta con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Actualizar un detalle de venta
*   **URL:** `/api/v1/detalle-venta/{id}`
*   **Método HTTP:** `PUT`
*   **Descripción:** Actualiza la cantidad de un producto registrado dentro de una venta.
*   **Parametro de la URL:** `id`
*   **Request Body:**
    ```json
    [{
    "cantidad": 3
    }
    ]

#### Respuestas:
-   **200 OK**: El detalle de venta fue actualizado correctamente.
-   **400 Bad Request**: Los datos enviados no cumplen con las validaciones requeridas.
-   **404 Not Found**: No existe el detalle de venta indicado.
-   **409 Conflict**: No hay stock suficiente para actualizar la cantidad solicitada.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Eliminar un detalle de venta
*   **URL:** `/api/v1/detalle-venta/{id}`
*   **Método HTTP:** `DELETE`
*   **Descripción:** Elimina un producto del detalle de una venta y actualiza el total correspondiente.
*   **Parametro de la URL:** `id`

#### Respuestas:
-   **204 No Content**: El detalle de venta fue eliminado correctamente.
-   **404 Not Found**: No existe un detalle de venta con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.



### Obtener todos los productos de una venta
*   **URL:** `/api/v1/venta/{id}/detalles`
*   **Método HTTP:** `GET`
*   **Descripción:** Obtiene todos los productos asociados a una venta específica, incluyendo la cantidad comprada y el precio unitario registrado al momento de la compra.
* **Parametro de la URL:** `id`

#### Respuestas:
-   **200 OK**: Los productos de la venta fueron obtenidos correctamente.
-   **204 No Content**: La venta no posee productos registrados.
-   **404 Not Found**: No existe una venta con el ID especificado.
-   **500 Internal Server Error**: Se produjo un error inesperado en el servidor.
-   **503 Service Unavailable**: El servidor no está preparado para manejar la petición en este momento.