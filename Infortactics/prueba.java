
import java.util.Scanner;

public class prueba {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // Creo el objeto sc
        Scanner sc = new Scanner(System.in);

        // Creamos el tablero, en este caso es 6x6
        int DIMENSIONES = 6;
        String[][] tablero = new String[DIMENSIONES][DIMENSIONES];

        // Elixir de la partida
        int elixir = 8;

        // Inicio programa
        limpiador();
        imprime_menu();
        String opcion_elegida = sc.nextLine();

        // Filtramos la entrada para el menu
        // Longitud 1
        if (opcion_elegida.length() > 1) {
            limpiador();
            imprime_menu();
            System.out.print("Longitud de la opcion_elegida insertada no valida [1 - 5]: ");
            opcion_elegida = sc.nextLine();

        } else {
            switch (opcion_elegida) {
                case "1" -> {
                }

                // Configurar baraja
                case "2" -> {
                    limpiador();
                    imprime_tablero(tablero);

                    System.out.println();
                    System.out.println();

                    info_personajes(elixir);
                    System.out.print("Personaje a añadir [x para borrar - 0 para guardar]: ");

                    // Pido el personaje
                    // Compruebo que la entrada es valida
                    // Elixir suficiente?
                    // Pido ubicacion filas [3- 5] columnas [0 - 5] (XY)
                    // Valido que es valida
                    // Inserto el personaje
                    // Resto elixir
                    // Actualizo tablero
                }

                case "3" -> {
                }

                case "4" -> {
                }

                case "5" -> {
                    limpiador();
                    System.out.println("Nos vemos la proxima vez!");
                    System.out.println("Saliendo del programa...");
                    System.exit(0);
                }

                default -> {
                    limpiador();
                    imprime_menu();
                    opcion_elegida = sc.nextLine();
                }
            }// Fin del switch
        }

        // Cierro el objeto sc
        sc.close();
    }// Fin del main

    // Funcion para limpiar la terminal
    public static void limpiador() {
        // Imprimimos espacios en blanco varias veces
        for (int i = 0; i < 100; i++) {
            System.out.println();
        }
    }

    // Funcion para el menu principal
    public static void imprime_menu() {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│      🏯 InforTactics UVa 🏯     │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│   1. NUEVA PARTIDA              │");
        System.out.println("│   2. CONFIGURAR BARAJA          │");
        System.out.println("│   3. GUARDAR BARAJA             │");
        System.out.println("│   4. CARGAR BARAJA              │");
        System.out.println("│   5. SALIR                      │");
        System.out.println("└─────────────────────────────────┘");
        System.out.println();
        System.out.print("Inserte la opcion: ");
    }

    // Funcion para mostrar por pantalla el tablero
    public static void imprime_tablero(String[][] tablero) {
        int filas = tablero.length;
        int cols = tablero[0].length;

        // --- Imprimir Números de Columna ---
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            System.out.print("  " + c + " ");
        }
        System.out.println();

        // --- Imprimir Fila Superior ---
        System.out.print("   ");
        System.out.print("┌");
        for (int c = 0; c < cols; c++) {
            System.out.print("───");
            if (c < cols - 1) {
                System.out.print("┬");
            }
        }
        System.out.println("┐");

        // --- Imprimir Filas de Contenido y Separadores ---
        for (int f = 0; f < filas; f++) {

            // Fila de Contenido
            System.out.print(" " + f + " ");
            System.out.print("│");

            for (int c = 0; c < cols; c++) {
                String contenido = (tablero[f][c] == null) ? " " : tablero[f][c];

                // --- (NUEVA LÓGICA) ---
                // Comprueba si la fila actual (f) está entre 0 y 2
                if (f <= 2) {
                    // Imprime con fondo sombreado
                    System.out.print("░" + contenido + "░");
                } else {
                    // Imprime con fondo normal (espacio)
                    System.out.print(" " + contenido + " ");
                }
                // --- (FIN DE LA LÓGICA) ---

                System.out.print("│"); // Separador vertical
            }
            System.out.println(); // Fin de la fila de contenido

            // Fila Separadora o Fila Inferior
            if (f < filas - 1) {
                System.out.print("   ");
                System.out.print("├");
                for (int c = 0; c < cols; c++) {
                    System.out.print("───");
                    if (c < cols - 1) {
                        System.out.print("┼");
                    }
                }
                System.out.println("┤");
            } else {
                // Fila inferior
                System.out.print("   ");
                System.out.print("└");
                for (int c = 0; c < cols; c++) {
                    System.out.print("───");
                    if (c < cols - 1) {
                        System.out.print("┴");
                    }
                }
                System.out.println("┘");
            }
        }
    }

    // Funcion para mostrar los datos de los personajes
    public static void info_personajes(int elixir) {
        System.out.println("┌────────────────────────────────────────────────────────────────────────┐");
        System.out.println("│ Personaje          Simb.       Elixir       Ataque(%)       Defensa(%) │");
        System.out.println("├────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ 🏹 Arquera           A            2             20              15     │");
        System.out.println("│ 🐲 Dragon            D            3             25              35     │");
        System.out.println("│ 👑 Princesa          P            4             20              25     │");
        System.out.println("│ ⚔️  Valquiria         V            3             35              40     │");
        System.out.println("│ 👹 Goblin            G            2             30              15     │");
        System.out.println("│ 🤖 P.E.K.K.A         K            4             75              40     │");
        System.out.println("├────────────────────────────────────────────────────────────────────────┤");
        System.out.println("│ Elixir restante 💧: " + elixir + "                                                  │");
        System.out.println("└────────────────────────────────────────────────────────────────────────┘");

    }
}
