
Módulo: Valoración de Productos
###################################

1. Registrar nueva valoración de producto
URL: /api/v1/valoracion

Método HTTP: POST

Descripción: Crea una nueva reseña y puntuación para un producto en el sistema, vinculándola al usuario logueado.

Request Body:

{
  "productoId": 105,
  "puntuacion": 5,
  "comentario": "Excelente producto, llegó super rápido y cumple con todo."
}
Respuestas:

Código: 201 (Creado exitosamente)

Response Body:

{
  "id": 1,
  "usuarioId": 10,
  "productoId": 105,
  "puntuacion": 5,
  "comentario": "Excelente producto, llegó super rápido y cumple con todo.",
  "fecha": "2026-06-28T18:48:00"
}
Código: 400 Bad Request (Datos enviados inválidos o mal formados)

Response Body:

{
  "timestamp": "2026-06-28T18:48:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La puntuación debe ser un número entero entre 1 y 5."
}
Código: 404 Not Found (El producto que se intenta valorar no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:48:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el producto con ID: 105"
}
##################################################

2. Obtener las valoraciones de un producto
URL: /api/v1/valoracion/producto/{id}

Método HTTP: GET

Descripción: Obtiene la lista completa de comentarios y puntuaciones asociados a un producto específico.

Respuestas:

Código: 200 OK (Éxito con resultados)

Response Body:
[
  {
    "id": 1,
    "usuarioId": 10,
    "productoId": 105,
    "puntuacion": 5,
    "comentario": "Excelente producto, llegó super rápido y cumple con todo.",
    "fecha": "2026-06-28T18:48:00"
  }
]
Código: 200 OK (Éxito sin resultados)

Response Body:

[]
Código: 404 Not Found (El producto con el ID provisto no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el producto con ID: 105"
}
##################################################

3. Obtener el promedio de puntuación de un producto
URL: /api/v1/valoracion/producto/{id}/promedio

Método HTTP: GET

Descripción: Calcula y devuelve el puntaje promedio de estrellas de un producto junto con el total de opiniones.

Respuestas:

Código: 200 OK (Éxito)

Response Body:

{
  "productoId": 105,
  "puntuacionPromedio": 4.75,
  "totalValoraciones": 12
}
Código: 404 Not Found (El producto con el ID provisto no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:52:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se puede calcular el promedio. El producto con ID 105 no existe."
}
##################################################

4. Borrar valoración
URL: /api/v1/valoracion/{id}

Método HTTP: DELETE

Descripción: Elimina una valoración del sistema definitivamente mediante su ID.

Respuestas:

Código: 204 No Content (Borrado exitosamente)

Código: 404 Not Found (La valoración con el ID provisto no existe)

Response Body:

{
  "timestamp": "2026-06-28T18:55:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró la valoración con ID: 1"
}