
## Módulo: Valoración de Productos

### 1. Registrar nueva valoración de producto
* **URL:** `/api/v1/valoracion`
* **Método HTTP:** `POST`
* **Descripción:** Registra una nueva reseña y puntuación para un producto, vinculándolo al ID del usuario logueado.
* **Request Body:**
```json
{
  "productoId": 105,
  "puntuacion": 5,
  "comentario": "Excelente producto, llegó super rápido y cumple con todo."
}
Respuestas:
201 Created (Éxito): Valoración registrada correctamente. Devuelve el objeto completo generado con su ID y la fecha de creación.


{
  "id": 1,
  "usuarioId": 10,
  "productoId": 105,
  "puntuacion": 5,
  "comentario": "Excelente producto, llegó super rápido y cumple con todo.",
  "fecha": "2026-06-28T18:48:00"
}
400 Bad Request (Error): Error de validación (por ejemplo, si la puntuación está fuera del rango de 1 a 5, o si falta el ID del producto).


{
  "timestamp": "2026-06-28T18:48:00",
  "status": 400,
  "error": "Bad Request",
  "message": "La puntuación debe ser un número entero entre 1 y 5."
}
404 Not Found (Error): El producto que se intenta valorar no existe en el sistema.


{
  "timestamp": "2026-06-28T18:48:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el producto con ID: 105"
}
2. Obtener las valoraciones de un producto
URL: /api/v1/valoracion/producto/{productoId}

Método HTTP: GET

Descripción: Retorna la lista completa de comentarios y puntuaciones asociados a un producto específico.

Respuestas:
200 OK (Éxito): Devuelve un arreglo con todas las valoraciones de ese producto. Si el producto existe pero nadie lo valoró, devuelve una lista vacía [].


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
404 Not Found (Error): El ID del producto ingresado no existe en la base de datos.


{
  "timestamp": "2026-06-28T18:50:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró el producto con ID: 105"
}
3. Obtener el promedio de puntuación de un producto
URL: /api/v1/valoracion/producto/{productoId}/promedio

Método HTTP: GET

Descripción: Calcula y devuelve el puntaje promedio de estrellas de un producto (útil para mostrar las "estrellitas" en el frontend).

Respuestas:
200 OK (Éxito): Devuelve el ID del producto junto con su promedio matemático y el total de personas que opinaron.

{
  "productoId": 105,
  "puntuacionPromedio": 4.75,
  "totalValoraciones": 12
}
404 Not Found (Error): El ID del producto no corresponde a ningún registro.


{
  "timestamp": "2026-06-28T18:52:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se puede calcular el promedio. El producto con ID 105 no existe."
}
4. Eliminar una valoración
URL: /api/v1/valoracion/{id}

Método HTTP: DELETE

Descripción: Borra definitivamente una reseña de la base de datos mediante su ID (por ejemplo, si el usuario decide borrar su propio comentario).

Respuestas:
204 No Content (Éxito): Se eliminó correctamente de la base de datos. El cuerpo de la respuesta está vacío.

404 Not Found (Error): Se intentó eliminar una valoración que no existe.


{
  "timestamp": "2026-06-28T18:55:00",
  "status": 404,
  "error": "Not Found",
  "message": "No se encontró la valoración con ID: 1"
}