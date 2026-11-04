# Laboratorio de Pruebas de Software – Semana 13

## Objetivo
Implementar y ejecutar pruebas unitarias con JUnit 5 y Mockito, además de pruebas de sistema con Postman, para verificar el funcionamiento del login de Veterinaria Patitas.

## Resultados obtenidos

- CP-01 a CP-09: 9 pruebas unitarias aprobadas.
- Login correcto: HTTP 200.
- Credenciales incorrectas: HTTP 401.
- Campos vacíos: HTTP 400.
- Cuenta bloqueada: HTTP 423.

## Evidencias

**Pruebas unitarias con JUnit**

![Evidencia 1](src/Evidencias/Captura%20de%20pantalla%202026-11-04%20150447.png)

**Bloqueo de cuenta en Postman**

![Evidencia 2](src/Evidencias/Captura%20de%20pantalla%202026-11-04%20150532.png)

## Defecto identificado y corregido

En LoginService se identificó un error en la condición de bloqueo: `> 3`, que permitía un intento fallido adicional. Se corrigió por `>= 3` y se volvieron a ejecutar las pruebas para verificar que el problema estuviera resuelto.

## Metacognición

**1. ¿Qué aprendí?**

Aprendí que `@Mock` permite simular el repositorio, `@InjectMocks` incorpora ese repositorio simulado al servicio y `assertThrows` verifica que se lance la excepción esperada.

**2. ¿Cómo lo aprendí?**

Al realizar las pruebas en Postman encontré inicialmente el error HTTP 405 porque había utilizado GET. Lo corregí cambiando el método a POST y configurando el cuerpo JSON. Después comprobé las respuestas correctas.

**3. ¿Para qué me sirve?**

Me sirve para verificar reglas de negocio antes de entregar un sistema. En mi proyecto integrador podría probar el registro de ventas, incluyendo el caso límite de intentar comprar un producto sin stock disponible.