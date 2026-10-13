### Agregar un producto a una venta
* **URL:** `/api/v1/detalle-venta/{id}`
* **Método HTTP:** `POST`
* **Descripción:** Agrega un producto al detalle de una venta existente, registrando la cantidad comprada y el precio unitario correspondiente al momento de la compra.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador de la venta.
* **Request Body:**
```json
{
    "ventaId": 10,
    "productoId": 5,
    "cantidad": 2
}
```

#### Respuestas
* **Código:** `201 Created` (El producto fue agregado correctamente a la venta.)
* **Código:** `400 Bad Request` (El cuerpo de la solicitud contiene datos inválidos o incompletos.)
* **Código:** `404 Not Found` (La venta o el producto especificado no existen.)
* **Código:** `409 Conflict` (No hay stock suficiente para realizar la operación.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Obtener todos los detalles de venta
* **URL:** `/api/v1/detalles-ventas`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene el listado completo de todos los detalles de ventas registrados en el sistema.

#### Respuestas
* **Código:** `200 OK` (Los detalles de ventas fueron obtenidos correctamente.)
* **Código:** `204 No Content` (No existen detalles de ventas registrados.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Obtener un detalle de venta por ID
* **URL:** `/api/v1/detalle-venta/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene la información de un detalle de venta específico mediante su identificador.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador único del detalle de venta.

#### Respuestas
* **Código:** `200 OK` (El detalle de venta fue encontrado correctamente.)
* **Código:** `404 Not Found` (No existe un detalle de venta con el ID especificado.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Actualizar un detalle de venta
* **URL:** `/api/v1/detalle-venta/{id}`
* **Método HTTP:** `PUT`
* **Descripción:** Actualiza la cantidad de un producto registrado dentro de una venta.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador único del detalle de venta.
* **Request Body:**
```json
{
    "cantidad": 3
}
```

#### Respuestas
* **Código:** `200 OK` (El detalle de venta fue actualizado correctamente.)
* **Código:** `400 Bad Request` (Los datos enviados no cumplen con las validaciones requeridas.)
* **Código:** `404 Not Found` (No existe el detalle de venta indicado.)
* **Código:** `409 Conflict` (No hay stock suficiente para actualizar la cantidad solicitada.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Eliminar un detalle de venta
* **URL:** `/api/v1/detalle-venta/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina un producto del detalle de una venta y actualiza el total correspondiente.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador único del detalle de venta.

#### Respuestas
* **Código:** `204 No Content` (El detalle de venta fue eliminado correctamente.)
* **Código:** `404 Not Found` (No existe un detalle de venta con el ID especificado.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Obtener todos los productos de una venta
* **URL:** `/api/v1/venta/{id}/detalles`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene todos los productos asociados a una venta específica, incluyendo la cantidad comprada y el precio unitario registrado al momento de la compra.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador único de la venta.

#### Respuestas
* **Código:** `200 OK` (Los productos de la venta fueron obtenidos correctamente.)
* **Código:** `204 No Content` (La venta no posee productos registrados.)
* **Código:** `404 Not Found` (No existe una venta con el ID especificado.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)

##################################################

### Obtener los detalles de venta de un producto
* **URL:** `/api/v1/productos/{id}/detalles-ventas`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene todos los detalles de venta en los que se encuentra registrado un producto específico.
* **Parámetros de la URL (Path Params):**
    * `id` (Long): Identificador único del producto.

#### Respuestas
* **Código:** `200 OK`
* **Response Body:**
```json
{
    "data": [
        {
            "id": 1,
            "ventaId": 10,
            "cantidad": 2,
            "precioUnitario": 1500.00
        }
    ]
}
```
* **Código:** `200 OK` (El producto no se encuentra registrado en ninguna venta.)
* **Código:** `404 Not Found` (El producto no existe.)
* **Código:** `500 Internal Server Error` (Se produjo un error inesperado en el servidor.)
* **Código:** `503 Service Unavailable` (El servidor no está preparado para manejar la petición en este momento.)