# Seguridad de contraseñas: por qué BCrypt

## 1. El problema
La tabla `usuario` guarda las credenciales de todos los usuarios (clientes y negocios). Si la base de datos se filtra (backup expuesto, inyección SQL, un compañero que sube un dump a un repo público), todo lo que esté guardado como texto plano queda comprometido. Y como mucha gente reutiliza contraseñas, el daño se extiende a otros servicios.

Por eso la columna se llama `password_hash`: **nunca guardamos la contraseña, guardamos una huella irreversible de ella.**

## 2. Qué decisión tomamos
- Las contraseñas se hashean con **BCrypt** antes de guardarse (`PasswordEncoder` + `BCryptPasswordEncoder`).
- Usamos solo la librería `spring-security-crypto`, no todo Spring Security.
- Los DTO de respuesta **nunca** incluyen la contraseña ni el hash.
- El único lugar donde la contraseña existe en texto plano es el cuerpo del request, en memoria, durante el instante en que se hashea.

## 3. Por qué BCrypt y no otra opción

| Opción | Problema |
|---|---|
| Texto plano | Cualquier filtración expone todas las contraseñas. |
| MD5 / SHA-256 | Son hashes **rápidos**: una GPU prueba miles de millones por segundo. Sin salt, además, dos usuarios con la misma clave tienen el mismo hash y existen tablas precalculadas (rainbow tables). |
| Cifrado reversible (AES) | Si se roba la clave de cifrado, se recuperan todas las contraseñas. Una contraseña no necesita poder recuperarse, solo verificarse. |
| **BCrypt** | Hash **lento a propósito**, con **salt aleatorio incluido** y **costo configurable**. |

Ventajas concretas de BCrypt:
1. **Salt automático:** cada hash incluye un valor aleatorio propio. Dos usuarios con la misma contraseña obtienen hashes distintos, y las rainbow tables dejan de servir.
2. **Costo adaptable (work factor):** hoy usamos el valor por defecto (10, es decir 2^10 rondas). Si el hardware mejora, se sube el costo sin cambiar el diseño.
3. **Todo en un solo string:** el resultado (`$2a$10$...`, 60 caracteres) contiene versión, costo, salt y hash. Cabe de sobra en `VARCHAR(255)` y no hace falta una columna extra para el salt.
4. **Es un estándar probado:** está en el ecosistema Spring, con más de 25 años de uso, y es una de las opciones que recomienda OWASP para almacenar contraseñas (junto con Argon2, scrypt y PBKDF2).

## 4. Por qué `spring-security-crypto` y no `spring-boot-starter-security`
El starter completo activa por defecto autenticación en **todos** los endpoints: todos los endpoints de los compañeros devolverían `401` hasta configurar login, usuarios y filtros. Eso es otro trabajo y no está en el alcance de esta entrega. `spring-security-crypto` aporta solo el hashing, no cambia el comportamiento de la API y deja la puerta abierta para sumar Spring Security completo más adelante.

## 5. Decisiones de diseño asociadas
- **Largo de contraseña:** mínimo 8 caracteres y máximo 72 bytes. BCrypt solo procesa los primeros 72 bytes; en lugar de truncar en silencio, se rechaza con `400`.
- **Verificación:** nunca se compara el hash con la contraseña. Se usa `passwordEncoder.matches(passwordIngresada, hashGuardado)`. Esto es lo que usará el login cuando se implemente.
- **Actualizar contraseña:** al modificarla se genera un hash nuevo (con un salt nuevo).

## 6. Por qué sumar esto se ve mejor en la defensa oral
Se puede mostrar que el proyecto no es solo CRUD, sino que incluye decisiones de seguridad **justificadas**, que es justo lo que pide el README ("qué decisiones se tomaron y por qué"):
- Muestra criterio técnico: se compararon alternativas (texto plano, SHA-256, AES, BCrypt) y se eligió con fundamento.
- Conecta con el diseño de datos: el diccionario de entidades ya definía `password_hash` (VARCHAR(255)), y la implementación es coherente con él.
- Permite una demostración en vivo: se crea un usuario por Postman y se muestra en la base el hash `$2a$10$...` en lugar de la clave; se crea otro con la misma clave y se ve que el hash es distinto.
- Tiene costo bajo y riesgo bajo: son pocas líneas, sin romper el resto de la API.

> Una buena defensa no garantiza una nota, pero esta decisión da respuestas concretas a preguntas típicas del docente.

### Preguntas probables y cómo responderlas
- **¿Por qué no SHA-256?** Porque es rápido. Para contraseñas se busca lo contrario: un algoritmo lento y con salt, que encarezca los ataques de fuerza bruta.
- **¿Se puede recuperar una contraseña olvidada?** No, y es a propósito. Se debe generar una nueva (flujo de restablecimiento).
- **¿Qué pasa si dos usuarios tienen la misma clave?** Los hashes son distintos por el salt aleatorio.
- **¿Dónde se verifica el login?** Con `matches()`, que lee el salt y el costo del propio hash.
- **¿Qué limitaciones tiene?** Límite de 72 bytes, y falta implementar el login con tokens y autorización por rol (queda como mejora futura).
