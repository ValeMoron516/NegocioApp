
### Registrar nueva direccion
* **URL:** `/api/v1/direccion`
* **Método HTTP:** `POST`
* **Descripción:** Registra la nueva direccion en la base de datos y la vincula con el id del usuario logueado.
* **Request Body:**
```json
{
  "calle" : "Lavalle",
  "ciudad" : "Rojas",
  "numero" : "123",
  "codigoPostal": "2705"
}
Respuestas:

201 Created (Éxito): Se creó correctamente. Devuelve el objeto completo con su ID y el estado predeterminado inicializado.
{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "123",
  "ciudad": "Rojas",
  "codigoPostal": "2705",
  "predeterminado": false
}

**400 Bad Request (Error): Faltan datos obligatorios en el JSON (ej: el código postal).**
    ```json
    {
       "timestamp": "2026-06-28T18:05:00",
       "status": 400,
       "error": "Bad Request",
       "message": "El campo 'codigoPostal' no puede ser nulo o vacío."
    }


### Obtener direcciones de un usuario

* **URL:** `/api/v1/direccion/usuario/{usuarioId}`
* **Método HTTP:** `GET`
* **Descripción:** Retorna la lista completa de direcciones asociadas a un ID de usuario específico.
* **Respuestas:**
  * **200 OK (Éxito):** Devuelve un arreglo con todas las direcciones registradas por el usuario. Si el usuario existe pero no cargó direcciones, devuelve la lista vacía `[]`.
```json
[
  {
    "id": 1,
    "usuarioId": 10,
    "calle": "Lavalle",
    "numero": "123",
    "ciudad": "Rojas",
    "codigoPostal": "2705",
    "predeterminado": false
  }
]
   **404 Not Found (Error)**: El ID del usuario ingresado no existe en la base de datos.
    {
     "timestamp": "2026-06-28T18:30:00",
     "status": 404,
     "error": "Not Found",
     "message": "No se encontró el usuario con ID: 10"
    }

### Modificar direccion existente
* **URL:** `/api/v1/direccion/{id}`
* **Método HTTP:** `PUT`
* **Descripción:** Actualiza los datos de una direccion especifica buscada por su id.
* **Request Body:**
```json
{
  "calle" : "Lavalle",
  "ciudad" : "Rojas",
  "numero" : "456",
  "codigoPostal": "2705"
}

** Respuestas**
 200 OK (Éxito): Modificación exitosa. Devuelve el objeto completo con los cambios aplicados en la base de datos.

 {
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "456",
  "ciudad": "Rojas",
  "codigoPostal": "2705",
  "predeterminado": false
}
400 Bad Request (Error): Los datos enviados no cumplen con el formato o están incompletos (por ejemplo, dejar la calle vacía).

{
  "timestamp": "2026-06-28T18:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'calle' es obligatorio."
}


### Eliminar direccion
* **URL:** `/api/v1/direccion/{id}`
* **Método HTTP:** `DELETE`
* **Descripción:** Borra definitivamente el registro de una dirección de la base de datos utilizando su ID.

* **Respuestas:**
  * **204 No Content (Éxito):** Eliminación exitosa. El servidor procesó la solicitud correctamente pero no devuelve ningún cuerpo de respuesta.
  * **404 Not Found (Error):** Intento de borrar una dirección con un ID que no existe en el sistema.
```json
{
  "timestamp": "2026-06-28T18:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se puede eliminar. No existe la dirección con ID: 1"
}

### Obtener direccion especifica por su id
* **URL:** `/api/v1/direccion/{id}`
* **Método HTTP:** `GET`
* **Descripción:** Trae los datos de un único domicilio específico mediante su ID para cargarlo en un formulario.
* **Respuestas:**
  * **200 OK (Éxito):** Retorna el objeto de la dirección encontrada.
```json
{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "456",
  "ciudad": "Rojas",
  "codigoPostal": "2705",
  "predeterminado": false
}

   * **404 Not Found (Error):** No existe ninguna dirección con ese ID en el sistema.
{
  "timestamp": "2026-06-28T19:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "La dirección con ID 1 no existe."
}

### Establecer dirección como predeterminada
* **URL:** `/api/v1/direccion/{id}/predeterminado`
* **Método HTTP:** `PUT`
* **Descripción:** Define una dirección como favorita/principal y desmarca automáticamente las demás del mismo usuario.
* **Respuestas:**
  * **200 OK (Éxito):** Estado actualizado correctamente. Devuelve la dirección modificada mostrando el cambio en el campo correspondiente.
```json
{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "456",
  "ciudad": "Rojas",
  "codigoPostal": "2705",
  "predeterminado": true
}

 * **404 Not Found (Error):** El ID de la dirección no corresponde a ningún registro válido del sistema.
{
  "timestamp": "2026-06-28T19:10:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se pudo actualizar. Dirección no encontrada."
}