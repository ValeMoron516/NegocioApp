### Obtener lista de categorías
* **URL:** `/api/v1/categorias`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista de todas las categorías disponibles en el sistema.

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    [
      {
        "id": 1,
        "nombre": "Hogar",
        "descripcion": "Productos para el hogar"
      },
      {
        "id": 2,
        "nombre": "Tecnología",
        "descripcion": "Dispositivos electrónicos y accesorios"
      }
    ]
    ```

* **Código:** `200 OK` (Éxito sin resultados)
* **Response Body:**
    ```json
    []
    ```

##################################################

### Crear categorías
* **URL:** `/api/v1/categorias`
* **Método HTTP:** `POST`
* **Descripción:** Crea una nueva categoria en el sistema.
* **Request Body:**
    ```json
    {
        "nombre": "Computacion",
        "descripcion": "Productos relacionados a la computacion"
    }
    ```

#### Respuestas
* **Código:** `201` (Creado exitosamente)
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
