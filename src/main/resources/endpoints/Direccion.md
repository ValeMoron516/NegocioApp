
Módulo: Direcciones
1. Registrar nueva dirección
URL: /api/v1/direccion

Método HTTP: POST

Descripción: Crea una nueva dirección en la base de datos y la vincula con el ID del usuario logueado en el sistema.

Request Body:

{
  "calle": "Lavalle",
  "ciudad": "Rojas",
  "numero": "123",
  "codigoPostal": "2705"
}
Respuestas:

Código: 201 (Creado exitosamente)

Response Body:


{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "123",
  "ciudad": "Rojas",
  "codigoPostal": "2705"
}
Código: 400 Bad Request (Datos enviados inválidos o mal formados)

Response Body:


{
  "timestamp": "2026-06-28T18:05:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'codigoPostal' no puede ser nulo o vacío."
}
##################################################

2. Obtener direcciones de un usuario
URL: /api/v1/direccion/usuario/{usuarioId}

Método HTTP: GET

Descripción: Obtiene la lista completa de direcciones asociadas a un ID de usuario específico.

Respuestas:

Código: 200 OK (Éxito con resultados)

Response Body:


[
  {
    "id": 1,
    "usuarioId": 10,
    "calle": "Lavalle",
    "numero": "123",
    "ciudad": "Rojas",
    "codigoPostal": "2705"
  }
]
Código: 200 OK (Éxito sin resultados)

Response Body:


[]
Código: 404 Not Found (El usuario con el ID provisto no existe)

Response Body:


{
  "timestamp": "2026-06-28T18:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el usuario con ID: 10"
}
##################################################

3. Obtener dirección específica por ID
URL: /api/v1/direccion/{id}

Método HTTP: GET

Descripción: Obtiene los datos de un único domicilio específico mediante su ID.

Respuestas:

Código: 200 OK (Éxito)

Response Body:


{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "456",
  "ciudad": "Rojas",
  "codigoPostal": "2705"
}
Código: 404 Not Found (La dirección con el ID provisto no existe)

Response Body:


{
  "timestamp": "2026-06-28T19:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "La dirección con ID 1 no existe."
}
##################################################

4. Modificar dirección existente
URL: /api/v1/direccion/{id}

Método HTTP: PUT

Descripción: Actualiza de forma completa los datos de una dirección específica buscada por su ID.

Request Body:

{
  "calle": "Lavalle",
  "ciudad": "Rojas",
  "numero": "456",
  "codigoPostal": "2705"
}
Respuestas:

Código: 200 OK (Actualizado exitosamente)

Response Body:

{
  "id": 1,
  "usuarioId": 10,
  "calle": "Lavalle",
  "numero": "456",
  "ciudad": "Rojas",
  "codigoPostal": "2705"
}
Código: 400 Bad Request (Datos enviados inválidos o mal formados)

Response Body:

{
  "timestamp": "2026-06-28T18:40:00",
  "status": 400,
  "error": "Bad Request",
  "message": "El campo 'calle' es obligatorio."
}
Código: 404 Not Found (La dirección con el ID provisto no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:45:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se puede actualizar. Dirección no encontrada."
}
##################################################

5. Borrar dirección
URL: /api/v1/direccion/{id}

Método HTTP: DELETE

Descripción: Elimina una dirección del sistema definitivamente mediante su ID.

Respuestas:

Código: 204 No Content (Borrado exitosamente)

Código: 404 Not Found (La dirección con el ID provisto no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se puede eliminar. No existe la dirección con ID: 1"
}