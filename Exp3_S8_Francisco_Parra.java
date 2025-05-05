/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.exp3_s8_francisco_parra;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Exp3_S8_Francisco_Parra {
    private static Scanner scanner = new Scanner(System.in);
    private static final int FILAS = 3;
    private static final int COLUMNAS = 10;
    private static String[][] zona1 = new String[FILAS][COLUMNAS];
    private static String[][] zona2 = new String[FILAS][COLUMNAS];
    private static String[][] zona3 = new String[FILAS][COLUMNAS];

    private static final double PRECIO_1 = 20000;
    private static final double PRECIO_2 = 15000;
    private static final double PRECIO_3 = 10000;

    private static List<Entrada> carritoCompras = new ArrayList<>();
    private static List<Entrada> reservas = new ArrayList<>();
    private static List<String> promociones = List.of("10% Estudiantes", "15% Tercera Edad");
    private static double ingresosTotales = 0;
    private static Timer timer = new Timer();

    private static int contadorEntradas = 1;
    private static int contadorClientes = 1;
    
    public static void main(String[] args) {
        inicializarTeatro();
        menu();
    }

    private static void inicializarTeatro() {
        for (int i = 0; i < FILAS; i++) {
            Arrays.fill(zona1[i], "Disponible");
            Arrays.fill(zona2[i], "Disponible");
            Arrays.fill(zona3[i], "Disponible");
        }
    }

    private static void menu() {
        int opcion;
        do {
            System.out.println("\n=== Sistema de Gestión de Entradas S8 ===");
            System.out.println("1. Comprar Entrada");
            System.out.println("2. Reservar Entrada");
            System.out.println("3. Confirmar Reserva");
            System.out.println("4. Visualizar Promociones");
            System.out.println("5. Modificar Entrada Comprada");
            System.out.println("6. Visualizar Resumen de Ventas");
            System.out.println("7. Imprimier Boleta y Salir");
            System.out.print("Seleccione una opción: ");
            opcion = leerEnteroConValidacion();

            switch (opcion) {
                case 1 -> comprarEntrada(false);
                case 2 -> comprarEntrada(true);
                case 3 -> confirmarReserva();
                case 4 -> mostrarPromociones();
                case 5 -> modificarEntrada();
                case 6 -> visualizarResumenVentas();
                case 7 -> imprimirBoleta();
                default -> System.out.println("Opción inválida.");
            }
        } while (opcion != 7);
    }

    private static int leerEnteroConValidacion() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void comprarEntrada(boolean esReserva) {
        int zona = solicitarZona();
        if (zona == -1) return;
        mostrarAsientos(zona);

        int fila = solicitarFila(zona);
        if (fila == -1) return;

        int columna = solicitarColumna();
        if (columna == -1) return;

        if (!asientoDisponible(zona, fila, columna)) {
            System.out.println("Asiento no disponible.");
            return;
        }

        System.out.print("Ingrese nombre del cliente: ");
        String nombre = scanner.nextLine();

        int edad = solicitarEdad();
        if (edad == -1) return;

        boolean estudiante = solicitarEstudiante();

        Cliente cliente = new Cliente(nombre, edad, estudiante);

        double precio = obtenerPrecioZona(zona);
        double descuento = (edad >= 60) ? 0.15 : (estudiante ? 0.10 : 0);
        double precioFinal = precio * (1 - descuento);

        Entrada entrada = new Entrada(zona, fila, columna, precio, descuento, precioFinal, cliente);

        if (esReserva) {
            reservas.add(entrada);
            bloquearAsiento(zona, fila, columna, "Reservado");
            programarExpiracionReserva(entrada);
            System.out.println("Entrada reservada por 2 minutos.\n" + entrada);
        } else {
            carritoCompras.add(entrada);
            bloquearAsiento(zona, fila, columna, "Comprado");
            ingresosTotales += precioFinal;
            System.out.println("Entrada comprada exitosamente.\n" + entrada);
        }
    }

    private static int solicitarZona() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Seleccione zona (1-VIP, 2-PLATEA, 3-BALCÓN): ");
            int zona = leerEnteroConValidacion();
            if (zona >= 1 && zona <= 3) return zona;
            System.out.println("Zona inválida.");
        }
        return -1;
    }

    private static int solicitarFila(int zona) {
        for (int i = 0; i < 2; i++) {
            System.out.print("Seleccione fila (letra): ");
            String filaLetra = scanner.nextLine().toUpperCase();
            int fila = convertirFilaALetra(zona, filaLetra);
            if (fila != -1) return fila;
            System.out.println("Fila inválida.");
        }
        return -1;
    }

    private static int solicitarColumna() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Seleccione columna (1-10): ");
            int col = leerEnteroConValidacion() - 1;
            if (col >= 0 && col < COLUMNAS) return col;
            System.out.println("Columna inválida.");
        }
        return -1;
    }

    private static int solicitarEdad() {
        for (int i = 0; i < 2; i++) {
            System.out.print("Ingrese edad: ");
            int edad = leerEnteroConValidacion();
            if (edad >= 0 && edad <= 120) return edad;
            System.out.println("Edad inválida.");
        }
        return -1;
    }

    private static boolean solicitarEstudiante() {
        for (int i = 0; i < 2; i++) {
            System.out.print("¿Es estudiante? (s/n): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("s")) return true;
            if (input.equals("n")) return false;
            System.out.println("Entrada inválida.");
        }
        return false;
    }

    private static void mostrarPromociones() {
        System.out.println("Promociones disponibles:");
        promociones.forEach(p -> System.out.println("- " + p));
    }

    private static void modificarEntrada() {
        if (carritoCompras.isEmpty()) {
            System.out.println("No hay entradas para modificar.");
            return;
        }
        for (int i = 0; i < carritoCompras.size(); i++) {
            System.out.println((i + 1) + ". " + carritoCompras.get(i));
        }
        System.out.print("Seleccione entrada a modificar: ");
        int index = leerEnteroConValidacion() - 1;
        if (index < 0 || index >= carritoCompras.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Entrada entrada = carritoCompras.remove(index);
        bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Disponible");
        ingresosTotales -= entrada.precioFinal;
        comprarEntrada(false);
    }

    private static void visualizarResumenVentas() {
        System.out.println("Resumen de ventas:");
        carritoCompras.forEach(System.out::println);
        System.out.println("Total Ingresos: $" + ingresosTotales);
    }

    private static void confirmarReserva() {
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas activas.");
            return;
        }
        for (int i = 0; i < reservas.size(); i++) {
            System.out.println((i + 1) + ". " + reservas.get(i));
        }
        System.out.print("Seleccione reserva a confirmar: ");
        int index = leerEnteroConValidacion() - 1;
        if (index < 0 || index >= reservas.size()) {
            System.out.println("Selección inválida.");
            return;
        }
        Entrada entrada = reservas.remove(index);
        carritoCompras.add(entrada);
        bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Comprado");
        ingresosTotales += entrada.precioFinal;
        System.out.println("Reserva confirmada como compra.");
    }

    private static void programarExpiracionReserva(Entrada entrada) {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (reservas.remove(entrada)) {
                    bloquearAsiento(entrada.zona, entrada.fila, entrada.columna, "Disponible");
                    System.out.println("Reserva expirada: " + entrada);
                }
            }
        }, 120000);
    }

    private static boolean asientoDisponible(int zona, int fila, int columna) {
        return obtenerZona(zona)[fila][columna].equals("Disponible");
    }

    private static void bloquearAsiento(int zona, int fila, int columna, String estado) {
        obtenerZona(zona)[fila][columna] = estado;
    }

    private static double obtenerPrecioZona(int zona) {
        return switch (zona) {
            case 1 -> PRECIO_1;
            case 2 -> PRECIO_2;
            case 3 -> PRECIO_3;
            default -> 0;
        };
    }

    private static String[][] obtenerZona(int zona) {
        return switch (zona) {
            case 1 -> zona1;
            case 2 -> zona2;
            case 3 -> zona3;
            default -> new String[0][0];
        };
    }

    private static int convertirFilaALetra(int zona, String letra) {
    switch (zona) {
        case 1 -> { if ("A".equals(letra)) return 0; if ("B".equals(letra)) return 1; if ("C".equals(letra)) return 2; }
        case 2 -> { if ("D".equals(letra)) return 0; if ("E".equals(letra)) return 1; if ("F".equals(letra)) return 2; }
        case 3 -> { if ("G".equals(letra)) return 0; if ("H".equals(letra)) return 1; if ("I".equals(letra)) return 2; }
    }
    return -1;
}
    private static String obtenerLetraFila(int zona, int fila) {
    return switch (zona) {
        case 1 -> String.valueOf((char) ('A' + fila)); // VIP: A-C
        case 2 -> String.valueOf((char) ('D' + fila)); // PLATEA: D-F
        case 3 -> String.valueOf((char) ('G' + fila)); // BALCÓN: G-I
        default -> "?";
    };
}
   public static class Cliente {
    int idCliente;
    String nombre;
    int edad;
    boolean esEstudiante;

    Cliente(String nombre, int edad, boolean esEstudiante) {
        this.idCliente = contadorClientes++;
        this.nombre = nombre;
        this.edad = edad;
        this.esEstudiante = esEstudiante;
    }

    @Override
    public String toString() {
        return "ID Cliente: " + idCliente + ", " + nombre + ", Edad: " + edad + ", Estudiante: " + (esEstudiante ? "Sí" : "No");
    }
}

   public static class Entrada {
    int idEntrada;
    int zona, fila, columna;
    double precioBase, descuento, precioFinal;
    Cliente nuevoCliente;

    Entrada(int zona, int fila, int columna, double precioBase, double descuento, double precioFinal, Cliente nuevoCliente) {
        this.idEntrada = contadorEntradas++;
        this.zona = zona;
        this.fila = fila;
        this.columna = columna;
        this.precioBase = precioBase;
        this.descuento = descuento;
        this.precioFinal = precioFinal;
        this.nuevoCliente = nuevoCliente;
    }

    @Override
    public String toString() {
        return "ID Entrada: " + idEntrada + ", Zona: " + zona + " (" + obtenerNombreZona(zona) + "), Fila: " + obtenerLetraFila(zona, fila) +
               ", Columna: " + (columna + 1) + ", Precio Final: $" + precioFinal + ", Cliente: [" + nuevoCliente + "]";
    }
}
    
    private static void mostrarAsientos(int zona) {
    String[][] area = obtenerZona(zona);
    System.out.println("Estado de los Asientos (Ubicación " + obtenerNombreZona(zona) + "):");
    for (int i = 0; i < FILAS; i++) {
        String letraFila = obtenerLetraFila(zona, i);
        System.out.print("Fila " + letraFila + ": ");
        for (int j = 0; j < COLUMNAS; j++) {
            System.out.print(area[i][j].equals("Disponible") ? "[" + (j + 1) + "] " : "[X] ");
        }
        System.out.println();
    }
}
    private static String obtenerNombreZona(int zona) {
    return switch (zona) {
        case 1 -> "VIP";
        case 2 -> "PLATEA";
        case 3 -> "BALCÓN";
        default -> "Desconocida";
    };
}
    
    private static void imprimirBoleta() {
    System.out.println("\n=== Boleta ===");
    if (carritoCompras.isEmpty()) {
        System.out.println("No hay ventas registradas.");
        return;
    }
    int boleta = 0;
    for (Entrada e : carritoCompras) {
        System.out.println("Boleta N°" + (boleta + 1));
        System.out.println("Ubicación: " + obtenerNombreZona(e.zona) + ", Fila: " + obtenerLetraFila(e.zona, e.fila) + ", Columna: " + (e.columna + 1));
        System.out.println("Precio Base: $" + e.precioBase);
        System.out.println("Descuento Aplicado: $" + (e.precioBase * e.descuento));
        System.out.println("Precio Final: $" + e.precioFinal);
        System.out.println("----------------------------");
        boleta++;
    }
    System.out.println("Gracias por su compra. Hasta pronto!");
}
}
