### Obtener lista de categorías
* **URL:** `/api/v1/categorias`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista paginada de todas las categorías disponibles en el sistema.
* **Parámetros de consulta (Query Params):**
  * `page` (entero, opcional): Número de página a consultar (por defecto `1`).
  * `limit` (entero, opcional): Cantidad de registros a mostrar por página (por defecto `20`).

#### Ejemplo de Petición Completa
`GET /api/v1/categorias?page=1&limit=20`

#### Respuestas
* **Código:** `200 OK` (Éxito con resultados)
* **Response Body:**
    ```json
    {
      "data": [
        {
          "id": 1,
          "nombre": "Hogar"
        },
        {
          "id": 2,
          "nombre": "Tecnología"
        }
      ],
      "meta": {
        "totalItems": 42,
        "itemCount": 2,
        "itemsPerPage": 20,
        "totalPages": 3,
        "currentPage": 1
      }
    }
    ```

* **Código:** `200 OK` (Éxito sin resultados)
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

### Obtener categoría por ID
* **URL:** `/api/v1/categorias/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene la categoría por ID disponible en el sistema.

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "nombre": "Hogar"
    }
    ```

* **Código:** `404 Not Found` (La categoría con el ID provisto no existe)

##################################################

### Crear categorías
* **URL:** `/api/v1/categorias`
* **Método HTTP:** `POST`
* **Descripción:** Crea una nueva categoria en el sistema.
* **Parámetros de consulta (Query Params):**
  * `nombre` (texto, obligatorio): Nombre de la nueva categoría.
  * `descripcion` (texto, obligatorio): Descripción de la nueva categoría.

#### Ejemplo de Petición Completa
`POST /api/v1/categorias?nombre=Computacion&descripcion=Productos%20relacionados%20a%20la%20computacion`

#### Respuestas
* **Código:** `201 Created` (Creado exitosamente)
* **Response Body:**
    ```json (registro creado)
    {
        "id": 1,
        "nombre": "Computacion"
    }
    ```

* **Código:** `400 Bad Request` (Datos enviados inválidos o mal formados)
* **Código:** `409 Conflict` (El nombre ya está siendo utilizado por otra categoría)

##################################################

### Actualizar categorías
* **URL:** `/api/v1/categorias/{id}`
* **Método HTTP:** `PATCH`
* **Descripción:** Actualiza la categoria en el sistema.
* **Request Body:**
    ```json
    {
        "nombre": "Tecnologia"
    }
    ```

#### Respuestas
* **Código:** `200 OK` (Actualizado exitosamente)
* **Response Body:**
    ```json (registro actualizado)
    {
      "id": 1,
      "nombre": "Tecnologia",
      "descripcion": "Productos para el hogar"
    }
    ```

* **Código:** `400 Bad Request` (Datos enviados inválidos o mal formados)
* **Código:** `404 Not Found` (La categoría con el ID provisto no existe)
* **Código:** `409 Conflict` (El nombre ya está siendo utilizado por otra categoría)

##################################################

### Borrar categorías
* **URL:** `/api/v1/categorias/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina una categoria del sistema.

#### Respuestas
* **Código:** `204 No Content` (Borrado exitosamente)
* **Código:** `404 Not Found` (La categoría con el ID provisto no existe)
* **Código:** `409 Conflict` (No se puede eliminar; la categoría tiene productos asociados)
