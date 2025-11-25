
import java.util.Scanner;

public class InfortacticsUVa {

    public static void main(String[] args) {
        // Creo el objeto in Scanner
        Scanner in = new Scanner(System.in);

        // Baraja del jugador 
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR];
        int elixir = Assets.INITIAL_ELIXIR;

        // Limpiamos la terminal para mayor claridad visual
        Methods.flushScreen();

        // Mostramos el menu inicial
        String option = "";
        option = printMenu(in, option);

        // Bucle para el juego entero
        boolean finJuego = false;
        while (!finJuego) {
            switch (option) {
                case "1" -> // Nueva Partida
                    System.out.println("Iniciando nueva partida...");
                // Aqui iria la logica para iniciar una nueva partida

                case "2" -> {// Configurar Baraja
                    // 1. Limpiamos
                    Methods.flushScreen();

                    // 2. Mostramos la situacion actual del tablero y la informacion de los personajes
                    printBoard(playerDeck);
                    printCharactersInfo();
                    printElixir(elixir);

                    // 3. Configuramos la baraja del jugador
                    cofigureDeck(in, playerDeck, playerDeck);

                    // 4. Volvemos a imprimir el menu inicial
                    option = printMenu(in, option);
                }

                case "3" -> // Guardar Baraja
                    System.out.println("Guardando baraja...");
                // Aqui iria la logica para guardar la baraja

                case "4" -> // Cargar Baraja
                    System.out.println("Cargando baraja...");
                // Aqui iria la logica para cargar la baraja

                case "5" -> {
                    // Salir
                    System.out.println("Saliendo del juego. ¡Hasta luego!");
                    System.exit(0);
                }
                default -> {
                    System.out.println("Opción no válida. Por favor, seleccione una opción del 1 al 5.");
                    option = in.nextLine();
                }
            }
        }

        // Cerramos el objeto Scanner in
        in.close();
    }// Fin main

    // =================================================================
    //                          MÉTODOS AUXILIARES
    // =================================================================
    //
    // Funcion para mostrar el menu inicial del juego
    public static String printMenu(Scanner in, String option) {
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
        System.out.print("Inserte una opción [1-5]: ");
        option = in.nextLine();
        return option;
    }

    // Funcion para imprimir por pantalla la informacion de los personajes
    public static void printCharactersInfo() {
        System.out.println("PERSONAJES DISPONIBLES:");
        System.out.println("-----------------------------------------------------");

        // 1. Cabecera (6 campos: Icono/Nombre, Símb, Elixir, Ataque[d], Defensa[d])
        // Usaremos %-15s para el campo de Icono/Nombre.
        System.out.printf("%-15s %-6s %-6s %-8s %-8s%n", "Personaje", "Símb.", "Elixir", "%Ataque", "%Defensa");
        System.out.println("-----------------------------------------------------");

        // 2. Líneas de Datos (Icono + Nombre combinados, y usar %d para números)
        // Formato de Datos: %-15s (Icono + Nombre) %-6s (Símbolo) %-6d (Elixir) %-8d (Ataque) %-8d (Defensa)
        // Arquera
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.ARCHER_IMAGE + " " + Assets.ARCHER_NAME,
                Assets.ARCHER_SYMBOL, Assets.ARCHER_ELIXIR, Assets.ARCHER_ATTACK, Assets.ARCHER_DEFENSE);

        // Dragón
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.DRAGON_IMAGE + " " + Assets.DRAGON_NAME,
                Assets.DRAGON_SYMBOL, Assets.DRAGON_ELIXIR, Assets.DRAGON_ATTACK, Assets.DRAGON_DEFENSE);

        // Princesa
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PRINCESS_IMAGE + " " + Assets.PRINCESS_NAME,
                Assets.PRINCESS_SYMBOL, Assets.PRINCESS_ELIXIR, Assets.PRINCESS_ATTACK, Assets.PRINCESS_DEFENSE);

        // Valquiria
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.VALKYRIE_IMAGE + "  " + Assets.VALKYRIE_NAME,
                Assets.VALKYRIE_SYMBOL, Assets.VALKYRIE_ELIXIR, Assets.VALKYRIE_ATTACK, Assets.VALKYRIE_DEFENSE);

        // Goblin
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.GOBLIN_IMAGE + " " + Assets.GOBLIN_NAME,
                Assets.GOBLIN_SYMBOL, Assets.GOBLIN_ELIXIR, Assets.GOBLIN_ATTACK, Assets.GOBLIN_DEFENSE);

        // P.E.K.K.A
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PK_IMAGE + " " + Assets.PK_NAME,
                Assets.PK_SYMBOL, Assets.PK_ELIXIR, Assets.PK_ATTACK, Assets.PK_DEFENSE);

        System.out.println("-----------------------------------------------------");
    }// Fin printCharactersInfo

    // Funcion para imprimir el elixir del jugador
    public static void printElixir(int elixir) {
        System.out.println("Elixir Restante 🩸: " + elixir);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Procedimiento que muestra por pantalla el tablero de juego. Mapea los
     * personajes de un vector de Strings a una matriz 6x6 para su
     * visualización.
     *
     * @param deck Referencia a un vector de Strings que contiene los personajes
     * y sus posiciones en el formato "SXY"
     */
    public static void printBoard(String[] deck) {
        // 1. Inicializamos la matriz temporal del tablero
        String[][] board = new String[Assets.BOARD_ROWS][Assets.BOARD_COLUMNS];

        // Rellenamos con el symbol de posicion vacia
        for (int fil = 0; fil < Assets.BOARD_ROWS; fil++) {
            for (int col = 0; col < Assets.BOARD_COLUMNS; col++) {
                board[fil][col] = String.valueOf(Assets.NO_POSITION);
            }// Fin for
        }// Fin for

        // 2. Colocamos los personajes en la matriz
        for (String character : deck) {
            if (character != null && character.length() == 3) {
                // Recogemos los datos del personaje y la jugada
                char symbol = character.charAt(0);
                // Fila
                int x = character.charAt(1);
                // Columna
                int y = character.charAt(2);

                // Usamos el symbol del personaje correspondiente
                if (x >= 0 && x < Assets.BOARD_ROWS && y >= 0 && y < Assets.BOARD_COLUMNS) {
                    board[x][y] = Methods.getCharacterImage(symbol);
                }// Fin if
            }// Fin if
        }// Fin for

        // 3. Imprimimos el tablero
        System.out.println("TABLERO");
        System.out.println("-----------------------------------------------------");
        System.out.println();

        // Imprimir encabezado de columnas (0 1 2 3 4 5)
        System.out.print("   ");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("  " + j + " ");
        }
        System.out.println();

        // Borde superior
        System.out.print("   ┌");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("───");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┬");
            }
        }
        System.out.println("┐");

        // Imprimir filas
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            // Imprimir el contenido de la fila
            System.out.print(" " + i + " │"); // Número de fila y borde izquierdo
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                String cellContent = board[i][j];

                // Si la celda está vacía, usamos el símbolo NO_POSITION para las filas del enemigo (0, 1, 2)
                if (cellContent.trim().isEmpty() || cellContent.equals(" ")) {
                    if (i < Assets.BOARD_ROWS / 2) { // Filas 0, 1, 2 son del enemigo
                        cellContent = String.valueOf(Assets.NO_POSITION);
                    } else {
                        cellContent = " "; // Filas 3, 4, 5 son del jugador, mostramos como espacio
                    }
                }

                // Imprimir el contenido de la celda (separado por espacios para el formato)
                System.out.print(" " + cellContent + " │");
            }
            System.out.println(); // Salto de línea después de la fila

            // Imprimir el separador de filas (si no es la última fila)
            if (i < Assets.BOARD_ROWS - 1) {
                System.out.print("   ├");
                for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                    System.out.print("───");
                    if (j < Assets.BOARD_COLUMNS - 1) {
                        System.out.print("┼");
                    }
                }
                System.out.println("┤");
            }
        }

        // Borde inferior
        System.out.print("   └");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("───");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┴");
            }
        }
        System.out.println("┘");

        System.out.println();
        System.out.println();

    }// Fin printBoard

    // Funcion para configurar el tablero
    /**
     * @param in
     * @param gameDeck
     * @param playerDeck
     */
    public static void cofigureDeck(Scanner in, String[] gameDeck, String[] playerDeck) {
        // Variable para el input del usuario
        String input;
        String borrar;

        // Variable para el elixir actual del jugador
        int currentElixir;

        // Variable de control del bucle
        boolean terminado = false;

        // Bucle para la configuracion de la baraja del jugador
        while (!terminado) {
            // Limpiamos la terminal
            Methods.flushScreen();

            // 1. Mostramos el tablero, la informacion de los personajes y el elixir actual
            printBoard(gameDeck);
            printCharactersInfo();
            currentElixir = calculateCurrentElixir(playerDeck);
            printElixir(currentElixir);

            // 2. Pedimos la jugada al usuario
            System.out.println("[X] para borrar [0] para guardar y salir");
            System.out.print("Inserte una jugada [SXY]: ");
            input = in.nextLine().toUpperCase();

            // Limpiamos la terinal
            Methods.flushScreen();

            // Input tiene que ser de 3 caracteres (SXY) o comandos especiales
            // Comprobamos si es un comando especial
            switch (input.length()) {
                // Longitud 1: Comando especial [X, borrar personaje] [O, guardar baraja y salir]
                case 1:
                    // Caad caso de los comandos especiales
                    switch (input) {
                        // Borrar jugada
                        case "X":
                            // Limpiamos la terminal
                            Methods.flushScreen();

                            // 1. Mostramos el tablero, la informacion de los personajes y el elixir actual
                            printBoard(gameDeck);
                            printCharactersInfo();
                            currentElixir = calculateCurrentElixir(playerDeck);
                            printElixir(currentElixir);

                            // Pedimos la jugada a borrar
                            System.out.print("Inserta la jugada a borrar [XY]: ");
                            borrar = in.nextLine().toUpperCase().trim();
                            // Validamos que la entrada es correcta
                            switch (borrar.length()) {
                                // Longitud 2: Borrar jugada XY
                                case 2:
                                    // Fila
                                    int x = borrar.charAt(0);
                                    // Columna
                                    int y = borrar.charAt(1);

                                    // Validamos que la entrada es valida
                                    if (x >= '0' && x <= '5' && y >= '3' && y <= '5') {

                                        // Posiciones no validas, mostramos el error y lo volvemos a pedir
                                    } else {
                                        // Limpiamos la terminal
                                        Methods.flushScreen();

                                        // 1. Mostramos el tablero, la informacion de los personajes y el elixir actual
                                        printBoard(gameDeck);
                                        printCharactersInfo();
                                        currentElixir = calculateCurrentElixir(playerDeck);
                                        printElixir(currentElixir);

                                        // Mensaje de error
                                        System.out.println("Coordenadas fuera de rango. Deben estar en la mitad inferior del tablero (X: 0-5, Y: 3-5).");

                                        // 2. Pedimos la jugada al usuario
                                        System.out.println("[X] para borrar [0] para guardar y salir");
                                        System.out.print("Inserte una jugada [SXY]: ");
                                        input = in.nextLine().toUpperCase();
                                    }
                                    break;// Fin case longitud 2 borrar
                                default:
                                    // Limpiamos la terminal
                                    Methods.flushScreen();

                                    // 1. Mostramos el tablero, la informacion de los personajes y el elixir actual
                                    printBoard(gameDeck);
                                    printCharactersInfo();
                                    currentElixir = calculateCurrentElixir(playerDeck);
                                    printElixir(currentElixir);
                                    System.out.println("Entrada no válida. Debe ser de la forma [XY].");

                                    // 2. Pedimos la jugada al usuario
                                    System.out.println("[X] para borrar [0] para guardar y salir");
                                    System.out.print("Inserte una jugada [SXY]: ");
                                    input = in.nextLine().toUpperCase();
                                    break;
                            }// Fin switch validar entrada borrar
                            break;

                        // Salir
                        case "0":
                            // Limpiamos la terminal
                            Methods.flushScreen();
                            // Salimos de la configuracion
                            terminado = true;
                            break;

                        // Error en la entrada del usuario    
                        default:
                            // Limpiamos la terminal
                            Methods.flushScreen();

                            // Mostramos el tablero, la informacion de los personajes y el elixir actual
                            printBoard(gameDeck);
                            printCharactersInfo();
                            currentElixir = calculateCurrentElixir(playerDeck);
                            printElixir(currentElixir);

                            System.out.println("Comando no válido.");
                            System.out.print("Inserte una jugada valida [SXY]: ");
                            input = in.nextLine().toUpperCase();
                            break;
                    }
                    break;// Fin switch comandos especiales

                // Longitud 3: Jugada SXY
                case 3:
                    // Recogemos los datos del personaje y la jugada
                    char symbol = input.charAt(0);
                    // Fila
                    int x = input.charAt(1);
                    // Columna
                    int y = input.charAt(2);
                    // Comprobamos si la jugada es valida
                    if (symbol == 'A' || symbol == 'D' || symbol == 'P' || symbol == 'V' || symbol == 'G' || symbol == 'K') {
                        if (x >= '0' && x <= '5' && y >= '3' && y <= '5') {
                            // Comprobamos si hay suficiente elixir
                            int characterElixir = Methods.getCharacterElixir(symbol);
                            if (currentElixir + characterElixir <= Assets.INITIAL_ELIXIR) {
                                // Añadimos la jugada a la baraja del jugador
                                // playerDeck[] = input;
                                // Restamos el elixir correspondiente
                                System.out.println("Jugada añadida correctamente.");
                            } else {
                                System.out.println("No tienes suficiente elixir para esta jugada.");
                                System.out.print("Inserte una jugada valida [SXY]: ");
                                input = in.nextLine().toUpperCase();
                            }

                            // Coordenadas no validas
                        } else {
                            System.out.println("Coordenadas fuera de rango. Deben estar en la mitad inferior del tablero (X: 0-5, Y: 3-5).");
                            System.out.print("Inserte una jugada valida [SXY]: ");
                            input = in.nextLine().toUpperCase();
                        }

                        // Personaje no valido
                    } else {
                        System.out.println("Símbolo de personaje no válido.");
                        System.out.print("Inserte una jugada valida [SXY]: ");
                        input = in.nextLine().toUpperCase();

                    }
                    break;
                default:
                    System.out.println("Jugada no válida.");
                    System.out.print("Inserte una jugada valida [SXY]: ");
                    input = in.nextLine().toUpperCase();
                    break;
            }
        }
    }

    // Funcion para calcular el elixir actual del jugador
    public static int calculateCurrentElixir(String[] playerDeck) {
        int totalElixir = 0;
        return totalElixir;
    }

}
