
import java.io.*;
import java.util.Random;
import java.util.Scanner;

public class InfortacticsUVa {

    // Constantes ANSI para Colores y Estilos
    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";
    public static final String BOLD = "\u001B[1m";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR / 2];
        String[] barajaJ1 = new String[Assets.INITIAL_ELIXIR / 2];
        String[] barajaJ2 = new String[Assets.INITIAL_ELIXIR / 2];
        Methods.initializeDeck(playerDeck);
        Methods.initializeDeck(barajaJ1);
        Methods.initializeDeck(barajaJ2);
        Methods.flushScreen();
        String option = printMenu(sc);
        boolean exit = false;
        while (!exit) {
            switch (option) {
                case "1":       // --- Nueva Partida --- //
                    logicaNuevaPartida(sc, playerDeck);
                    option = printMenu(sc);
                    break;

                case "2":       // --- Configurar Baraja --- //
                    logicaConfigurarBaraja(sc, playerDeck);
                    option = printMenu(sc);
                    break;

                case "3":       // --- Guardar Baraja --- //
                    logicaGuardarBaraja(playerDeck);
                    option = printMenu(sc);
                    break;

                case "4":       // --- Cargar Baraja --- //
                    logicaCargarBaraja(playerDeck);
                    option = printMenu(sc);
                    break;

                case "5":       // --- Gestionar Barajas --- //
                    logicaGestionarBarajas(sc, playerDeck);
                    option = printMenu(sc);
                    break;

                case "6":       // --- PvP --- //
                    logicaPvP(barajaJ1, barajaJ2, sc);
                    Methods.flushScreen();
                    option = printMenu(sc);
                    break;

                case "7":       // --- Creditos --- //
                    Methods.flushScreen();
                    Methods.flushScreen();
                    printStudentInfo();
                    System.out.print("Enter para volver: ");
                    sc.nextLine();
                    Methods.flushScreen();
                    Methods.flushScreen();
                    option = printMenu(sc);
                    break;

                case "8":       // --- Salir --- //
                    Methods.flushScreen();
                    System.out.println(PURPLE + BOLD + "¡Hasta luego!" + RESET);
                    exit = true;
                    break;

                default:        // --- Opción no válida o no contemplada --- //
                    Methods.flushScreen();
                    System.out.println(RED + BOLD + "Opción no válida." + RESET);
                    option = printMenu(sc);
                    break;
            }
        }
        sc.close();
    }

    // ###### METODOS ###### //
    /**
     * Muestra el menú inicial y lee la opción del usuario.
     *
     * @param in Scanner para entrada.
     * @return Opción seleccionada.
     */
    public static String printMenu(Scanner in) {
        System.out.println(YELLOW + BOLD);
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│      🏯 InforTactics UVa 🏯     │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│   1. Nueva Partida              │");
        System.out.println("│   2. Configurar Baraja          │");
        System.out.println("│   3. Guardar Baraja             │");
        System.out.println("│   4. Cargar Baraja              │");
        System.out.println("│   5. Gestión Barajas            │");
        System.out.println("│   6. JcJ                        │");
        System.out.println("│   7. Créditos                   │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│   8. Salir                      │");
        System.out.println("└─────────────────────────────────┘");
        System.out.print("Inserte una opción [1-8]: " + RESET);
        return in.nextLine();
    }

    /**
     * Logica principal para iniciar la partida e imprime la partida completa
     *
     * @param in Scanner
     * @param option String
     * @param playerDeck String[]
     */
    public static void logicaNuevaPartida(Scanner in, String[] playerDeck) {
        if (hasCharacters(playerDeck)) {
            String[] enemyDeck = loadRandomEnemyDeck();
            if (enemyDeck != null) {
                Methods.flushScreen();
                System.out.println(YELLOW + BOLD + "Baraja enemiga cargada: " + RESET);
                printEnemyDeckDetails(enemyDeck);
                printBoard(enemyDeck);
                System.out.println();
                System.out.print(YELLOW + BOLD + "Presiona [Enter] para comenzar" + RESET);
                in.nextLine();
                Methods.flushScreen();
                Methods.startGame(in, playerDeck, enemyDeck);
            } else {
                Methods.flushScreen();
                System.out.println(RED + BOLD + "[ERROR] -> Problema al cargar la baraja enemiga." + RESET);
                System.out.println(RED + BOLD + "[ERROR] -> Verifica que la ruta /Barajas/BarajasEnemigas.txt exista y tenga contenido." + RESET);
            }
        } else {
            Methods.flushScreen();
            System.out.println(RED + BOLD + "¡Configura tu baraja antes!" + RESET);
        }
    }

    /**
     * Logica para el caso 2 del switch, configurar la baraja del jugador
     *
     * @param sc Scanner
     * @param playerDeck String[]
     */
    public static void logicaConfigurarBaraja(Scanner sc, String[] playerDeck) {
        configureDeck(sc, playerDeck, "", "oasdfg", "");
        Methods.flushScreen();
    }

    /**
     * Logica para guardar la baraja dentro de la ruta
     * /Barajas/BarajasGuardadas.txt
     *
     * @param playerDeck String[]
     */
    public static void logicaGuardarBaraja(String[] playerDeck) {
        if (saveDeck(playerDeck)) {
            Methods.flushScreen();
            System.out.println(GREEN + BOLD + "Baraja guardada correctamente." + RESET);
        } else {
            Methods.flushScreen();
            System.out.println(RED + BOLD + "Error al guardar la baraja." + RESET);
        }
    }

    /**
     * Logica para cargar la baraja guardada en el archivo
     * /Baraja/BarajasGuardadas.txt
     *
     * @param playerDeck String[]
     * @param elixir int
     */
    public static void logicaCargarBaraja(String[] playerDeck) {
        if (loadDeck(playerDeck)) {
            Methods.flushScreen();
            System.out.println(GREEN + BOLD + "Baraja cargada correctamente." + RESET);

        } else {
            Methods.flushScreen();
            System.out.println(RED + BOLD + "Error al cargar la baraja." + RESET);
        }
    }

    /**
     * Logica para gestionar barajas: guardar, cargar, listar
     *
     * @param sc Scanner
     * @param playerDeck String[]
     */
    public static void logicaGestionarBarajas(Scanner sc, String[] playerDeck) {
        boolean back = false;
        while (!back) {
            Methods.flushScreen();
            System.out.println(YELLOW + BOLD);
            System.out.println("┌─────────────────────────────┐");
            System.out.println("│     Gestionar Barajas       │");
            System.out.println("├─────────────────────────────┤");
            System.out.println("│   1. Guardar Baraja         │");
            System.out.println("│   2. Cargar Baraja          │");
            System.out.println("│   3. Listar Barajas         │");
            System.out.println("│   4. Volver                 │");
            System.out.println("└─────────────────────────────┘");
            System.out.print("Inserte una opción [1-4]: ");
            System.out.print(RESET);
            String subOption = sc.nextLine();
            switch (subOption) {
                case "1":       // --- Guardar Baraja --- //
                    System.out.print(BOLD + "Introduce un nombre para guardar esta baraja: " + RESET);
                    String name = sc.nextLine();
                    if (saveDeckNamed(playerDeck, name)) {
                        System.out.println(GREEN + BOLD + "Baraja guardada como '" + name + "'." + RESET);
                    } else {
                        System.out.println(RED + BOLD + "[ERROR] -> Problema al guardar la baraja." + RESET);
                    }
                    System.out.print(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                    sc.nextLine();
                    break;
                case "2":       // --- Cargar Baraja --- //
                    listDecks();
                    System.out.print(BOLD + "Introduce el nombre de la baraja que deseas cargar: " + RESET);
                    String loadName = sc.nextLine();
                    if (loadDeckNamed(playerDeck, loadName)) {
                        System.out.println(GREEN + BOLD + "¡Baraja '" + loadName + "' cargada exitosamente!." + RESET);
                    } else {
                        System.out.println(RED + BOLD + "[ERROR] -> Problema al cargar la baraja." + RESET);
                    }
                    System.out.print(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                    sc.nextLine();
                    break;
                case "3":       // --- Listar Barajas --- //
                    System.out.println(BOLD + "Barajas guardadas:" + RESET);
                    listDecks();
                    System.out.print(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                    sc.nextLine();
                    break;
                case "4":       // --- Volver --- //
                    back = true;
                    break;
                default:        // --- Opcion no válida --- //
                    System.out.println(RED + BOLD + "[ERROR] -> Opción insertada no válida." + RESET);
                    System.out.print(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                    sc.nextLine();
                    break;
            }
        }
    }

    /**
     * Guarda la baraja con un nombre específico en Barajas/[name].txt.
     *
     * @param playerDeck Baraja.
     * @param name Nombre del archivo.
     * @return True si exitoso.
     */
    public static boolean saveDeckNamed(String[] playerDeck, String name) {
        String rutaArchivo = "Barajas/" + name + ".txt";
        try (PrintWriter escribe = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < playerDeck.length; i++) {
                String p = playerDeck[i];
                if (p != null && !p.isEmpty()) {
                    escribe.print(p + " ");
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println(RED + BOLD + "Error I/O al guardar la baraja: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Carga la baraja con un nombre específico desde Barajas/[name].txt.
     *
     * @param playerDeck Baraja a cargar.
     * @param name Nombre del archivo.
     * @return True si exitoso.
     */
    public static boolean loadDeckNamed(String[] playerDeck, String name) {
        String filePath = "Barajas/" + name + ".txt";
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Archivo " + filePath + " no encontrado.");
            return false;
        }
        Methods.initializeDeck(playerDeck);
        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextLine()) {
                String content = fileScanner.nextLine();
                try (Scanner lineScanner = new Scanner(content)) {
                    int i = 0;
                    while (lineScanner.hasNext() && i < playerDeck.length) {
                        String part = lineScanner.next();
                        if (part.length() == 3) {
                            playerDeck[i] = part;
                        }
                        i++;
                    }
                }
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error al acceder al archivo: " + e.getMessage());
            return false;
        }
    }

    /**
     * Lista las barajas guardadas en Barajas/.
     */
    public static void listDecks() {
        File dir = new File("Barajas/");
        if (!dir.exists() || !dir.isDirectory()) {
            System.out.println("[ERROR] -> Directorio Barajas/ no encontrado.");
            return;
        }
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            System.out.println("[ERROR] -> No hay barajas guardadas.");
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (file.isFile() && file.getName().endsWith(".txt")) {
                String name = file.getName();
                // Remover .txt del nombre
                String deckName = name.substring(0, name.length() - 4);
                System.out.println("- " + deckName);
            }
        }
    }

    /*
     * Logica para la funcion de pvp (jugador contra jugador)
     *  
     */
    public static void logicaPvP(String[] barajaJ1, String[] barajaJ2, Scanner sc) {
        Methods.initializeDeck(barajaJ1);
        Methods.initializeDeck(barajaJ2);
        Methods.flushScreen();
        System.out.print("Nombre del jugador 1: ");
        String jugador1 = sc.nextLine();
        Methods.flushScreen();
        boolean conf = false;
        while (!conf) {
            configureDeck(sc, barajaJ1, jugador1, jugador1, "");
            if (!hasCharacters(barajaJ1)) {
                Methods.flushScreen();
                System.out.print(RED + BOLD + "[ERROR] -> Configura tu baraja antes [J1]" + jugador1 + "!" + RESET);
                sc.nextLine();
                Methods.flushScreen();
            } else {
                conf = true;
            }
        }

        Methods.flushScreen();
        System.out.print("Nombre del jugador 2: ");
        String jugador2 = sc.nextLine();
        conf = false;
        while (!conf) {
            configureDeck(sc, barajaJ2, jugador2, jugador1, jugador2);
            if (!hasCharacters(barajaJ2)) {
                Methods.flushScreen();
                System.out.println(RED + BOLD + "[ERROR] -> Configura tu baraja antes [J2]" + jugador2 + "!" + RESET);
                sc.nextLine();
                Methods.flushScreen();
            } else {
                conf = true;
            }
        }
        Methods.startGame(sc, barajaJ1, barajaJ2);
    }

    /**
     * Muestra informacion sobre los responsables de la practica
     */
    public static void printStudentInfo() {
        System.out.println(BOLD);
        System.out.println("┌───────────────────────────────────────────┐");
        System.out.println("│       Práctica FPRO - Curso 25/26         │");
        System.out.println("├───────────────────────────────────────────┤");
        System.out.println("│ Practica realizada por:                   │");
        System.out.println("│ - Alejandro García Lavandera (X4)         │");
        System.out.println("│ - Beltran Gil Esteban (X9)                │");
        System.out.println("└───────────────────────────────────────────┘");
        System.out.println(RESET);
    }

    /**
     * Imprime la información de los personajes en formato tabular (con emojis)
     * con alineación mejorada.
     */
    public static void printCharactersInfo() {

        System.out.println(BOLD + "\nPERSONAJES DISPONIBLES:");

        // Encabezado: Reducimos el ancho del Personaje y el Símbolo para reducir el espacio blanco excesivo
        System.out.printf("%-18s %-5s %6s %8s %8s%n", "Personaje", "Símb.", "Elixir", "%Ataque", "%Defensa");
        System.out.println("---------------------------------------------------"); // Longitud ajustada a la nueva suma de anchuras (aprox 48)

        // Datos de la tabla:
        // 1. Arquera
        System.out.printf("%-18s %-5s %6d %8d %8d%n",
                Assets.ARCHER_IMAGE + " " + Assets.ARCHER_NAME,
                Assets.ARCHER_SYMBOL, Assets.ARCHER_ELIXIR, Assets.ARCHER_ATTACK, Assets.ARCHER_DEFENSE);

        // 2. Dragón
        System.out.printf("%-18s %-5s %6d %8d %8d%n",
                Assets.DRAGON_IMAGE + " " + Assets.DRAGON_NAME,
                Assets.DRAGON_SYMBOL, Assets.DRAGON_ELIXIR, Assets.DRAGON_ATTACK, Assets.DRAGON_DEFENSE);

        // 3. Princesa
        System.out.printf("%-18s %-5s %6d %8d %8d%n",
                Assets.PRINCESS_IMAGE + " " + Assets.PRINCESS_NAME,
                Assets.PRINCESS_SYMBOL, Assets.PRINCESS_ELIXIR, Assets.PRINCESS_ATTACK, Assets.PRINCESS_DEFENSE);

        // 4. Valquiria
        System.out.printf("%-18s %-5s %7s %8d %8d%n",
                Assets.VALKYRIE_IMAGE + "  " + Assets.VALKYRIE_NAME + "  ",
                " " + Assets.VALKYRIE_SYMBOL, "  " + Assets.VALKYRIE_ELIXIR, Assets.VALKYRIE_ATTACK, Assets.VALKYRIE_DEFENSE);

        // 5. Goblin
        System.out.printf("%-18s %-5s %6d %8d %8d%n",
                Assets.GOBLIN_IMAGE + " " + Assets.GOBLIN_NAME,
                Assets.GOBLIN_SYMBOL, Assets.GOBLIN_ELIXIR, Assets.GOBLIN_ATTACK, Assets.GOBLIN_DEFENSE);

        // 6. P.E.K.K.A
        System.out.printf("%-18s %-5s %6d %8d %8d%n",
                Assets.PK_IMAGE + " " + Assets.PK_NAME,
                Assets.PK_SYMBOL, Assets.PK_ELIXIR, Assets.PK_ATTACK, Assets.PK_DEFENSE);

        System.out.println("---------------------------------------------------" + RESET);
    }

    /**
     * Imprime el elixir restante.
     *
     * @param elixir Cantidad de elixir.
     */
    public static void printElixir(int elixir) {
        System.out.println(PURPLE + BOLD + "---------------------------------------------------" + RESET);
        System.out.println(PURPLE + BOLD + "Elixir Restante 🩸: " + elixir + RESET);
        System.out.println(PURPLE + BOLD + "---------------------------------------------------" + RESET);
    }

    /**
     * Procedimiento que muestra el tablero con emojis y alineación ajustada (x=
     * columna, y= fila). Cada celda tiene un ancho fijo de 4 caracteres.
     *
     * @param deck Vector de personajes.
     */
    public static void printBoard(String[] deck) {
        String[][] board = new String[Assets.BOARD_ROWS][Assets.BOARD_COLUMNS];
        // Rellenamos el tablero con caracter especial y espacios vacios
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                String noPosition = "" + Assets.NO_POSITION + Assets.NO_POSITION;
                board[i][j] = (i < 3) ? noPosition : "";
            }
        }

        // Colocar personajes en el tablero
        for (int i = 0; i < deck.length; i++) {
            String character = deck[i];
            if (character != null && character.length() == 3) {
                char symbol = character.charAt(0);
                int x = character.charAt(1) - '0'; // x = fila
                int y = character.charAt(2) - '0'; // y = columna
                if (x >= 0 && x < Assets.BOARD_ROWS && y >= 0 && y < Assets.BOARD_COLUMNS) {
                    String characterImage = Methods.getCharacterImage(symbol);
                    board[x][y] = characterImage;
                }
            }
        }
        // --- Imprimir el tablero con bordes y alineación --- //
        System.out.println(YELLOW + BOLD + "TABLERO" + RESET);
        System.out.print("    ");
        // Imprimir números de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(YELLOW + BOLD + "  " + j + "  " + RESET);
        }
        System.out.println();
        // --- BORDES SUPERIORES --- //
        String colorTopBorder = RED + BOLD;
        System.out.print(colorTopBorder + "    ┌" + RESET);
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(colorTopBorder + "────" + RESET);
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print(colorTopBorder + "┬" + RESET);
            }
        }
        System.out.println(colorTopBorder + "┐" + RESET);

        // --- Imprimir filas del tablero --- //
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            // Selector de color en funcion de la fila (Rojo enemigo) (CYAN jugador)
            String fgColor = (i < 3) ? RED + BOLD : CYAN + BOLD;
            // Imprimir número de fila
            System.out.print(YELLOW + BOLD + String.format("%3d", i) + RESET);
            System.out.print(fgColor + " │" + RESET);
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                String cellContent = String.format(" %-2s ", board[i][j]);
                System.out.print(fgColor + cellContent + RESET);
                System.out.print(fgColor + "│" + RESET);
            }
            System.out.println();
            if (i < Assets.BOARD_ROWS - 1) {
                String interBorderColor;
                if (i == 2) {// Si i=2, es la línea entre fila 2 y 3 (ahora WHITE)
                    interBorderColor = RESET + BOLD;
                } else if (i < 2) { // i=0, 1: Bordes internos del enemigo (RED)
                    interBorderColor = RED + BOLD;
                } else { // i=2 (Línea divisoria principal) o i=4 (Borde interno del jugador)
                    interBorderColor = CYAN + BOLD;
                }
                System.out.print(interBorderColor + "    ├" + RESET);
                for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                    System.out.print(interBorderColor + "────" + RESET);
                    if (j < Assets.BOARD_COLUMNS - 1) {
                        System.out.print(interBorderColor + "┼" + RESET);
                    }
                }
                System.out.println(interBorderColor + "┤" + RESET);
            }
        }
        String colorBottomBorder = CYAN + BOLD;
        System.out.print(colorBottomBorder + "    └" + RESET);
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(colorBottomBorder + "────" + RESET);
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print(colorBottomBorder + "┴" + RESET);
            }
        }
        System.out.println(colorBottomBorder + "┘" + RESET);
    }

    /**
     * Imprime los detalles de la baraja enemiga: emoji, nombre y posición.
     *
     * @param enemyDeck Baraja enemiga.
     */
    public static void printEnemyDeckDetails(String[] enemyDeck) {
        for (int i = 0; i < enemyDeck.length; i++) {
            if (enemyDeck[i] != null && enemyDeck[i].length() == 3) {
                char symbol = enemyDeck[i].charAt(0);
                int x = (int) enemyDeck[i].charAt(1) - '0'; // X = fila
                int y = (int) enemyDeck[i].charAt(2) - '0'; // Y = columna
                // Imprimir detalles del personaje
                String name = Methods.getCharacterName(symbol);
                String image = Methods.getCharacterImage(symbol);
                System.out.println(BOLD + image + " " + name + " en [" + x + "][" + y + "]" + RESET);
            }
        }
    }

    /**
     * Configura la baraja del jugador con validaciones (x= columna, y= fila).
     *
     * @param in Scanner.
     * @param playerDeck Baraja del jugador.
     */
    public static void configureDeck(Scanner in, String[] playerDeck, String jugador, String jugador1, String jugador2) {
        int currentElixir;
        // Bucle hasta que el usuario decida salir
        boolean finished = false;
        while (!finished) {
            // Determinar zona permitida según el jugador: Jugador 1 -> filas 0-2, Jugador 2 -> filas 3-5
            int minRow = 3;
            int maxRow = 5;
            if (jugador != null && jugador.equals(jugador1)) {
                minRow = 0;
                maxRow = 2;
            } else if (jugador != null && jugador.equals(jugador2)) {
                minRow = 3;
                maxRow = 5;
            }
            // 1. Limpiamos la pantalla
            Methods.flushScreen();
            System.out.println("Configura tu baraja " + jugador);
            // 2. Mostramos el tablero, info personajes y elixir restante
            printBoard(playerDeck); // Tablero del jugador
            printCharactersInfo();  // Info personajes
            currentElixir = calculateCurrentElixir(playerDeck); // Calculamos el elixir restante
            printElixir(currentElixir); // Elixir restante
            // 3. Pedimos jugada al usuario
            System.out.println(BOLD + "[X] para borrar [0] para guardar y salir" + RESET);
            System.out.print(BOLD + "Inserte una jugada [SXY]: " + RESET);
            // Leer entrada del usuario
            String input = in.nextLine();
            String errorMessage = ""; // Variable para almacenar mensaje de error
            // Procesar entrada del usuario
            switch (input.length()) {
                case 1:     // --- Comandos Especiales --- //
                    switch (input) {    // Switch comandos especiales
                        case "x":       // --- Borrar Personaje --- //
                        case "X":       // --- Borrar Personaje --- //
                            System.out.print(BOLD + "Inserte posición a borrar [XY]: " + RESET);
                            String pos = in.nextLine();
                            if (pos.length() == 2) {
                                int x = (int) pos.charAt(0) - '0'; // X = fila
                                int y = (int) pos.charAt(1) - '0'; // Y = columna
                                // Validar rango -- filas dependientes del jugador, Columnas 0-5
                                if (x >= minRow && x <= maxRow && y >= 0 && y <= 5) {
                                    boolean found = false;
                                    for (int i = 0; i < playerDeck.length; i++) {
                                        if (playerDeck[i] != null && playerDeck[i].length() == 3
                                                && (playerDeck[i].charAt(1) - '0') == x
                                                && (playerDeck[i].charAt(2) - '0') == y) {
                                            playerDeck[i] = "";
                                            found = true;
                                        }
                                    }
                                    if (!found) { // Posicion vacia
                                        errorMessage = "[ERROR] -> Posición vacia. Compruebe las coordenadas.";
                                    }
                                } else {// Posición fuera de rango
                                    errorMessage = "[ERROR] -> Posición inválida (filas " + minRow + "-" + maxRow + ", columnas 0-5).";
                                }
                            } else {// Formato inválido
                                errorMessage = "[ERROR] -> Formato inválido.";
                            }
                            break;
                        case "0":       // --- Guardar y Salir --- //
                            Methods.flushScreen();
                            System.out.println(GREEN + BOLD + "¡Baraja configurada exitosamente!" + RESET);
                            finished = true;
                            break;
                        default:        // --- Comando no válido --- //
                            Methods.flushScreen();
                            errorMessage = RED + BOLD + "[ERROR] -> Comando no válido." + RESET;
                            break;
                    }
                    break;
                case 3:     // --- Colocar Personaje --- //
                    char symbol = input.charAt(0); // Personaje
                    if (symbol >= 'a' && symbol <= 'z') {
                        symbol = (char) (symbol - 32); // Convertimos en mayuscula si es minuscula
                    }
                    int x = (int) input.charAt(1) - '0'; // X = fila
                    int y = (int) input.charAt(2) - '0'; // Y = columna
                    if (isValidSymbol(symbol) && x >= minRow && x <= maxRow && y >= 0 && y <= 5 && currentElixir >= Methods.getCharacterElixir(symbol)) {
                        boolean occupied = false;
                        int pos = 0;
                        while (pos < playerDeck.length && !occupied) {
                            String p = playerDeck[pos];
                            if (p != null && p.length() == 3 && (p.charAt(1) - '0') == x && (p.charAt(2) - '0') == y) {
                                occupied = true;
                            }
                            pos++;
                        }
                        if (!occupied) {
                            int posInsertar = -1;
                            boolean espacioLibreEncontrado = false;

                            int i = 0;
                            while (i < playerDeck.length && !espacioLibreEncontrado) {
                                if (playerDeck[i] == null || playerDeck[i].isEmpty()) {
                                    posInsertar = i;
                                    espacioLibreEncontrado = true;
                                }
                                i++;
                            }
                            if (posInsertar != -1) {
                                String personajeInsertar = "" + symbol + x + y;
                                playerDeck[posInsertar] = personajeInsertar;
                                System.out.println(YELLOW + BOLD + "Personaje " + Methods.getCharacterName(symbol) + " colocado en [" + x + "][" + y + "]." + RESET);
                            } else {
                                System.out.println(RED + BOLD + "[ERROR] -> No es posible insertar el personaje en la baraja." + RESET);
                            }
                        } else {
                            errorMessage = "[ERROR] -> Posición ocupada.";
                        }
                    } else {
                        errorMessage = "[ERROR] -> Jugada inválida o elixir insuficiente (columnas 0-5, filas " + minRow + "-" + maxRow + ").";
                    }
                    break;
                default:    // --- Formato inválido --- //
                    errorMessage = "[ERROR] -> Formato inválido.";
                    break;
            }
            if (!errorMessage.isEmpty()) {
                Methods.flushScreen();
                System.out.println(RED + BOLD + errorMessage + RESET);
                System.out.println(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                in.nextLine();
            }
        }
    }

    /**
     * Calcula el elixir actual basado en personajes colocados.
     *
     * @param playerDeck Baraja del jugador.
     * @return elixir Elixir restante.
     */
    public static int calculateCurrentElixir(String[] playerDeck) {
        int used = 0;
        for (int i = 0; i < playerDeck.length; i++) {
            if (playerDeck[i] != null && !playerDeck[i].isEmpty()) {
                used += Methods.getCharacterElixir(playerDeck[i].charAt(0));
            }
        }
        return Assets.INITIAL_ELIXIR - used;
    }

    /**
     * Verifica si la baraja tiene al menos un personaje.
     *
     * @param deck Baraja.
     * @return True si tiene personajes.
     */
    public static boolean hasCharacters(String[] deck) {
        for (int i = 0; i < deck.length; i++) {
            if (deck[i] != null && !deck[i].isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Valida si el símbolo es de un personaje válido.
     *
     * @param symbol Símbolo.
     * @return True si válido.
     */
    public static boolean isValidSymbol(char symbol) {
        return symbol == Assets.ARCHER_SYMBOL || symbol == Assets.DRAGON_SYMBOL
                || symbol == Assets.PRINCESS_SYMBOL || symbol == Assets.VALKYRIE_SYMBOL
                || symbol == Assets.GOBLIN_SYMBOL || symbol == Assets.PK_SYMBOL;
    }

    /**
     * Guarda la baraja en Barajas/BarajaGuardada.txt. Damos por hecho que la
     * ruta existe ya que se necesita la misma ruta para el archivo de las
     * barajas enemigas
     *
     * @param playerDeck Baraja.
     * @return True si exitoso.
     */
    public static boolean saveDeck(String[] playerDeck) {
        String rutaArchivo = "Barajas/BarajaGuardada.txt";
        try (PrintWriter escribe = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < playerDeck.length; i++) {
                String p = playerDeck[i];
                if (p != null && !p.isEmpty()) {
                    escribe.print(p + " ");
                }
            }
            return true;
        } catch (IOException e) {
            System.out.println(RED + BOLD + "Error I/O al guardar la baraja: " + e.getMessage() + RESET);
            return false;
        }
    }

    /**
     * Carga la baraja desde Barajas/BarajaGuardada.txt.
     *
     * @param playerDeck Baraja a cargar.
     * @return True si exitoso.
     */
    public static boolean loadDeck(String[] playerDeck) {
        final String filePath = "Barajas/BarajaGuardada.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("Archivo " + filePath + " no encontrado. Imposible cargar baraja.");
            return false;
        }
        Methods.initializeDeck(playerDeck);
        try (Scanner fileScanner = new Scanner(file)) {
            if (fileScanner.hasNextLine()) {
                String content = fileScanner.nextLine();
                try (Scanner lineScanner = new Scanner(content)) {
                    int i = 0;
                    while (lineScanner.hasNext() && i < playerDeck.length) {
                        String part = lineScanner.next();
                        if (part.length() == 3) {
                            playerDeck[i] = part;
                        } else {
                            System.err.println("[ADVERTENCIA] -> Elemento de baraja con longitud incorrecta (" + part + "). Saltando.");
                        }
                        i++;
                    }
                }
            } else {
                System.out.println("[ERROR] -> Archivo " + filePath + " está vacío.");
            }
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("Error de I/O al cargar " + filePath + ": " + e.getMessage());
            return false;
        }
    }

    /**
     * Carga una baraja enemiga aleatoria desde Barajas/BarajasEnemigas.txt.
     * Mapea Y >=3 a filas enemigas (0-2) para compatibilidad con el nuevo
     * sistema de ejes.
     *
     * @return Baraja enemiga o null si error.
     */
    public static String[] loadRandomEnemyDeck() {
        final String filePath = "Barajas/BarajasEnemigas.txt";
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println(RED + BOLD + "[ERROR] -> Archivo " + filePath + " no encontrado." + RESET);
            return null;
        }
        // --- PRIMERA PASADA: Extraemos las líneas válidas --- //
        int totalValidLines = 0;
        try (Scanner counterScanner = new Scanner(file)) {
            while (counterScanner.hasNextLine()) {
                String line = counterScanner.nextLine();
                if (!line.isEmpty()) {
                    totalValidLines++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(RED + BOLD + "[ERROR] -> Archivo " + filePath + " no encontrado durante el conteo." + RESET);
            return null;
        }

        if (totalValidLines == 0) {
            System.out.println(RED + BOLD + "[ERROR] -> Archivo " + filePath + " está vacío o solo contiene líneas en blanco." + RESET);
            return null;
        }

        // --- SEGUNDA PASADA: Encontrar y procesar la línea aleatoria --- //
        Random random = new Random();
        int targetIndex = random.nextInt(totalValidLines);
        String selectedLine = null;
        int currentValidLineIndex = 0;
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine() && selectedLine == null) {
                String line = fileScanner.nextLine();
                if (!line.isEmpty()) {
                    if (currentValidLineIndex == targetIndex) {
                        selectedLine = line;
                    }
                    currentValidLineIndex++;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(RED + BOLD + "[ERROR] -> Archivo no encontrado, compruebe que exista.");
            return null;
        }

        if (selectedLine == null) {
            System.out.println(RED + BOLD + "[Error] -> La línea aleatoria no fue encontrada." + RESET);
            return null;
        }
        String[] enemyDeck = new String[Assets.INITIAL_ELIXIR / 2];
        Methods.initializeDeck(enemyDeck);
        try (Scanner lineScanner = new Scanner(selectedLine)) {
            int i = 0;
            while (lineScanner.hasNext() && i < Assets.INITIAL_ELIXIR) {
                String part = lineScanner.next();
                if (part.length() == 3) {
                    char symbol = part.charAt(0);
                    int x = part.charAt(1) - '0'; // x = Fila
                    int y = part.charAt(2) - '0'; // y = Columna
                    enemyDeck[i] = "" + symbol + x + y;
                }
                i++;
            }
        }
        return enemyDeck;
    }
}
