### Obtener lista de roles
* **URL:** `/api/v1/roles`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista paginada de todos los roles disponibles en el sistema (por ejemplo `CLIENTE` y `NEGOCIO`).
* **Parámetros de consulta (Query Params):**
  * `page` (entero, opcional): Número de página a consultar (por defecto `1`).
  * `limit` (entero, opcional): Cantidad de registros a mostrar por página (por defecto `20`).

#### Ejemplo de Petición Completa
`GET /api/v1/roles?page=1&limit=20`

#### Respuestas
* **Código:** `200 OK` (Éxito con resultados)
* **Response Body:**
    ```json
    {
      "data": [
        { "id": 1, "nombre": "CLIENTE" },
        { "id": 2, "nombre": "NEGOCIO" }
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

* **Código:** `400 Bad Request` (`page` o `limit` menores a 1)

##################################################

### Obtener rol por ID
* **URL:** `/api/v1/roles/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene el rol correspondiente al ID indicado.

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "nombre": "CLIENTE"
    }
    ```

* **Código:** `404 Not Found` (El rol no existe)

##################################################

### Crear rol
* **URL:** `/api/v1/roles`
* **Método HTTP:** `POST`
* **Descripción:** Crea un nuevo rol. El nombre se guarda siempre en mayúsculas (`cliente` y `CLIENTE` son el mismo rol).
* **Request Body:**
    ```json
    {
      "nombre": "Cliente"
    }
    ```
    * `nombre` (texto, obligatorio): máximo 50 caracteres, único.

#### Respuestas
* **Código:** `201 Created` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "nombre": "CLIENTE"
    }
    ```

* **Código:** `400 Bad Request` (Nombre vacío, nulo o de más de 50 caracteres)
* **Código:** `409 Conflict` (Ya existe un rol con ese nombre)

##################################################

### Actualizar rol por ID
* **URL:** `/api/v1/roles/{id}`
* **Método HTTP:** `PUT`
* **Descripción:** Modifica el nombre de un rol existente.
* **Request Body:**
    ```json
    {
      "nombre": "Vendedor"
    }
    ```

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 2,
      "nombre": "VENDEDOR"
    }
    ```

* **Código:** `400 Bad Request` (Nombre inválido)
* **Código:** `404 Not Found` (El rol no existe)
* **Código:** `409 Conflict` (El nombre ya pertenece a otro rol)

##################################################

### Eliminar rol por ID
* **URL:** `/api/v1/roles/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina un rol. No se puede eliminar si hay usuarios asignados a ese rol.

#### Respuestas
* **Código:** `204 No Content` (Éxito)
* **Código:** `404 Not Found` (El rol no existe)
* **Código:** `409 Conflict` (El rol tiene usuarios asociados)
