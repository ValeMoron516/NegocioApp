> **Seguridad:** las contraseñas se guardan hasheadas con BCrypt (columna `password_hash`).
> Ninguna respuesta de la API devuelve la contraseña ni su hash.
> Ver `docs/seguridad-bcrypt.md`.

### Obtener lista de usuarios
* **URL:** `/api/v1/usuarios`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene una lista paginada de todos los usuarios registrados en el sistema.
* **Parámetros de consulta (Query Params):**
  * `page` (entero, opcional): Número de página a consultar (por defecto `1`).
  * `limit` (entero, opcional): Cantidad de registros a mostrar por página (por defecto `20`).

#### Ejemplo de Petición Completa
`GET /api/v1/usuarios?page=1&limit=20`

#### Respuestas
* **Código:** `200 OK` (Éxito con resultados)
* **Response Body:**
    ```json
    {
      "data": [
        {
          "id": 1,
          "telefono": "2914567890",
          "rolId": 1,
          "rolNombre": "CLIENTE",
          "creadoEn": "2026-10-20T15:30:00",
          "ultimoAcceso": null
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

### Obtener usuario por ID
* **URL:** `/api/v1/usuarios/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Obtiene el usuario correspondiente al ID indicado.

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "telefono": "2914567890",
      "rolId": 1,
      "rolNombre": "CLIENTE",
      "creadoEn": "2026-10-20T15:30:00",
      "ultimoAcceso": null
    }
    ```

* **Código:** `404 Not Found` (El usuario no existe)

##################################################

### Crear usuario
* **URL:** `/api/v1/usuarios`
* **Método HTTP:** `POST`
* **Descripción:** Registra un nuevo usuario. La contraseña se recibe en texto plano, se hashea con BCrypt y solo se guarda el hash. La fecha `creadoEn` la asigna el servidor.
* **Request Body:**
    ```json
    {
      "telefono": "2914567890",
      "password": "miClave1234",
      "rolId": 1
    }
    ```
    * `telefono` (texto, obligatorio): solo números, con `+` opcional al inicio, entre 6 y 19 dígitos. Único.
    * `password` (texto, obligatorio): mínimo 8 caracteres y máximo 72 bytes (límite de BCrypt).
    * `rolId` (entero, obligatorio): ID de un rol existente.

#### Respuestas
* **Código:** `201 Created` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "telefono": "2914567890",
      "rolId": 1,
      "rolNombre": "CLIENTE",
      "creadoEn": "2026-10-20T15:30:00",
      "ultimoAcceso": null
    }
    ```

* **Código:** `400 Bad Request` (Teléfono o contraseña inválidos, `rolId` nulo o inexistente)
* **Código:** `409 Conflict` (Ya existe un usuario con ese teléfono)

##################################################

### Actualizar usuario por ID
* **URL:** `/api/v1/usuarios/{id}`
* **Método HTTP:** `PUT`
* **Descripción:** Modifica los datos de un usuario. Solo se actualizan los campos enviados; los omitidos (`null`) quedan sin cambios. Si se envía `password`, se vuelve a hashear.
* **Request Body (todos los campos son opcionales):**
    ```json
    {
      "telefono": "2910000000",
      "password": "nuevaClave5678",
      "rolId": 2
    }
    ```

#### Respuestas
* **Código:** `200 OK` (Éxito)
* **Response Body:**
    ```json
    {
      "id": 1,
      "telefono": "2910000000",
      "rolId": 2,
      "rolNombre": "NEGOCIO",
      "creadoEn": "2026-10-20T15:30:00",
      "ultimoAcceso": null
    }
    ```

* **Código:** `400 Bad Request` (Teléfono o contraseña inválidos, `rolId` inexistente)
* **Código:** `404 Not Found` (El usuario no existe)
* **Código:** `409 Conflict` (El teléfono ya pertenece a otro usuario)

##################################################

### Eliminar usuario por ID
* **URL:** `/api/v1/usuarios/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Elimina un usuario. No se puede eliminar si tiene negocios, direcciones o ventas asociadas.

#### Respuestas
* **Código:** `204 No Content` (Éxito)
* **Código:** `404 Not Found` (El usuario no existe)
* **Código:** `409 Conflict` (El usuario tiene datos asociados)
