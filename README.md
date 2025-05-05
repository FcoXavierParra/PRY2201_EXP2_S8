# Exp_S8 – Sistema de Gestión de Entradas

**Versión:** S8  
**Autor:** Francisco Parra  
**Lenguaje:** Java  
**IDE sugerido:** NetBeans

## Descripción general
Exp_S8 es una aplicación de consola en Java que permite gestionar la compra, reserva, modificación y confirmación de entradas para un teatro. El sistema considera zonas diferenciadas de asientos, aplica descuentos según condiciones del cliente y genera un resumen de ventas con boletas.

---

## FUNCIONALIDADES PRINCIPALES

1. **Compra y reserva de entradas:**
   - Permite elegir zona (VIP, Platea, Balcón).
   - Solicita fila y columna, mostrando el estado de los asientos.
   - Solicita nombre, edad y si el cliente es estudiante.
   - Aplica descuentos personalizados.

2. **Confirmación de reservas:**
   - Las reservas expiran automáticamente tras 2 minutos si no son confirmadas.

3. **Visualización de promociones:**
   - Promociones disponibles: 10% para estudiantes, 15% para tercera edad.

4. **Modificar entrada comprada:**
   - Permite cancelar una entrada ya pagada y realizar una nueva compra.

5. **Visualizar resumen de ventas y boletas:**
   - Muestra detalles de cada entrada comprada y el total acumulado de ingresos.

---

## CAMBIOS PRINCIPALES RESPECTO A Exp_S7

- Se agregó la clase `Cliente`, con ID, nombre, edad y condición de estudiante.
- Se implementó identificación única por entrada (`idEntrada`).
- Se incorporó una opción en el menú para ver promociones.
- Se añadió funcionalidad para modificar entradas ya compradas.
- El menú pasó de 6 a 7 opciones, con nuevas funciones agregadas.
- Se mejoró la validación de entradas de usuario usando `scanner.nextLine` y manejo de errores con `try-catch`.
- Se reestructuró la clase `Entrada` para incluir más detalles del cliente y del asiento.
- Se mejoró la visualización de los asientos ocupados y disponibles.

---

## ESTRUCTURA DE CLASES

- `Exp3_S8_Francisco_Parra.java`: contiene el flujo principal y el menú del sistema.
- `Cliente`: almacena los datos personales de quien compra la entrada.
- `Entrada`: representa una entrada, incluyendo asiento, precios y datos del cliente.
