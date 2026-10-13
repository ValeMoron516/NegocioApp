### Buscar y filtrar productos

* **URL:** `/api/v1/productos`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista de productos que coinciden con los criterios de búsqueda y filtros especificados.
* **Parámetros de consulta (Query Params):**
  * `texto` (opcional, cadena): Término de búsqueda para el nombre o descripción del producto.
  * `categoria` (opcional, entero): ID de la categoría para filtrar.
  * `precio_min` (opcional, decimal): Límite inferior de precio.
  * `precio_max` (opcional, decimal): Límite superior de precio.
  * `page` (entero, opcional): Número de página a consultar (por defecto `1`).
  * `limit` (entero, opcional): Cantidad de registros a mostrar por página (por defecto `20`).

#### Ejemplo de Petición Completa
`GET /api/v1/productos?texto=monitor&categoria=1&precio_max=200000&page=1&limit=20`

#### Respuestas
* **Código:** `200` (Éxito)
* **Response Body:**
    ```json
    {
      "data": [
        {
          "id": 1,
          "negocio_id": 1,
          "categoria_id": 1,
          "nombre": "Monitor 24 pulgadas",
          "descripcion": "Monitor de 24 pulgadas 1920x1080 OLED",
          "precio": 149999,
          "stock": 10
        }
      ],
      "meta": {
        "totalItems": 1,
        "itemCount": 1,
        "itemsPerPage": 20,
        "totalPages": 1,
        "currentPage": 1
      }
    }
    ```

* **Código:** `200` (Éxito sin resultados / Filtros muy restrictivos)
* **Response Body:**
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

##################################################

### Obtener producto por ID
* **URL:** `/api/v1/productos/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene el producto por ID disponible en el sistema.

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "negocio_id": 1,
      "categoria_id": 1,
      "nombre": "Monitor 24 pulgadas",
      "descripcion": "Monitor de 24 pulgadas 1920x1080 OLED",
      "precio": 149999,
      "stock": 10
    }
    ```

* **Código:** `404 Not Found` (El producto con el ID provisto no existe)


##################################################

### Crear producto
* **URL:** `/api/v1/productos`
* **Método HTTP:** `POST`
* **Descripción:** Crea un nuevo producto en el sistema.
* **Request Body:**
    ```json
    {
      "negocio_id": 1,
      "categoria_id": 1,
      "nombre": "Monitor 24 pulgadas",
      "descripcion": "Monitor de 24 pulgadas 1920x1080 OLED",
      "precio": 149999,
      "stock": 10
    }
    ```

#### Respuestas
* **Código:** `201` (Creado exitosamente)
* **Response Body:**
    ```json (registro creado)
    {
      "id": 1,
      "negocio_id": 1,
      "categoria_id": 1,
      "nombre": "Monitor 24 pulgadas",
      "descripcion": "Monitor de 24 pulgadas 1920x1080 OLED",
      "precio": 149999,
      "stock": 10
    }
    ```

* **Código:** `400 Bad Request` (Datos enviados inválidos o mal formados)

##################################################

### Actualizar producto
* **URL:** `/api/v1/productos/{id}`
* **Método HTTP:** `PATCH`
* **Descripción:** Actualiza el producto en el sistema.
* **Request Body:**
    ```json
    {
        "precio": 199999,
        "stock": 15
    }
    ```

#### Respuestas
* **Código:** `200 OK` (Actualizado exitosamente)
* **Response Body:**
    ```json (registro actualizado)
    {
      "id": 1,
      "negocio_id": 1,
      "categoria_id": 1,
      "nombre": "Monitor 24 pulgadas",
      "descripcion": "Monitor de 24 pulgadas 1920x1080 OLED",
      "precio": 199999,
      "stock": 15
    }
    ```

* **Código:** `400 Bad Request` (Datos enviados inválidos o mal formados)
* **Código:** `404 Not Found` (El producto con el ID provisto no existe)

##################################################

### Borrar producto
* **URL:** `/api/v1/productos/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina un producto del sistema.

#### Respuestas
* **Código:** `204 No Content` (Borrado exitosamente)
* **Código:** `404 Not Found` (El producto con el ID provisto no existe)

