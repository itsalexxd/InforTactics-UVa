
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class pruebas {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR]; // Baraja del jugador (máx 8 personajes)
        int elixir = Assets.INITIAL_ELIXIR;

        // Inicializar baraja del jugador vacía
        Methods.initializeDeck(playerDeck);

        // Limpiar pantalla inicial
        Methods.flushScreen();

        // Variable para la gestion del menu
        String option = printMenu(in);

        while (true) {
            switch (option) {
                // Nueva Partida
                case "1" -> {
                    if (hasCharacters(playerDeck)) {
                        String[] enemyDeck = loadRandomEnemyDeck();
                        if (enemyDeck != null) {
                            System.out.println("Baraja enemiga cargada:");
                            printEnemyDeckDetails(enemyDeck); // Agregar esta línea
                            printBoard(enemyDeck);
                            System.out.println("\nPresiona Enter para comenzar...");
                            in.nextLine();
                            Methods.startGame(in, playerDeck, enemyDeck);
                        } else {
                            System.out.println("Error al cargar baraja enemiga. Verifica que Barajas/BarajasEnemigas.txt exista y tenga contenido.");
                        }
                    } else {
                        System.out.println("¡Tienes que configurar tu baraja antes!");
                    }
                    option = printMenu(in);
                }
                case "2" -> { // Configurar Baraja
                    configureDeck(in, playerDeck);
                    elixir = calculateCurrentElixir(playerDeck); // Recalcular elixir después de configurar
                    option = printMenu(in);
                }
                case "3" -> { // Guardar Baraja
                    if (saveDeck(playerDeck)) {
                        System.out.println("Baraja guardada correctamente.");
                    } else {
                        System.out.println("Error al guardar la baraja.");
                    }
                    option = printMenu(in);
                }
                case "4" -> { // Cargar Baraja
                    if (loadDeck(playerDeck)) {
                        elixir = calculateCurrentElixir(playerDeck);
                        System.out.println("Baraja cargada correctamente.");
                    } else {
                        System.out.println("Error al cargar la baraja.");
                    }
                    option = printMenu(in);
                }
                case "5" -> { // Salir
                    System.out.println("¡Hasta luego!");
                    in.close();
                    System.exit(0);
                }
                default -> {
                    System.out.println("Opción no válida.");
                    option = printMenu(in);
                }
            }
        }
    }

    /**
     * Muestra el menú inicial y lee la opción del usuario.
     *
     * @param in Scanner para entrada.
     * @return Opción seleccionada.
     */
    public static String printMenu(Scanner in) {
        System.out.println("┌─────────────────────────────────┐");
        System.out.println("│      🏯 InforTactics UVa 🏯     │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│   1. NUEVA PARTIDA              │");
        System.out.println("│   2. CONFIGURAR BARAJA          │");
        System.out.println("│   3. GUARDAR BARAJA             │");
        System.out.println("│   4. CARGAR BARAJA              │");
        System.out.println("│   5. SALIR                      │");
        System.out.println("└─────────────────────────────────┘");
        System.out.print("Inserte una opción [1-5]: ");
        return in.nextLine().trim();
    }

    /**
     * Imprime la información de los personajes en formato tabular (con emojis).
     */
    public static void printCharactersInfo() {
        System.out.println("\nPERSONAJES DISPONIBLES:");
        System.out.printf("%-15s %-6s %-6s %-8s %-8s%n", "Personaje", "Símb.", "Elixir", "%Ataque", "%Defensa");
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.ARCHER_IMAGE + " " + Assets.ARCHER_NAME,
                Assets.ARCHER_SYMBOL, Assets.ARCHER_ELIXIR, Assets.ARCHER_ATTACK, Assets.ARCHER_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.DRAGON_IMAGE + " " + Assets.DRAGON_NAME,
                Assets.DRAGON_SYMBOL, Assets.DRAGON_ELIXIR, Assets.DRAGON_ATTACK, Assets.DRAGON_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PRINCESS_IMAGE + " " + Assets.PRINCESS_NAME,
                Assets.PRINCESS_SYMBOL, Assets.PRINCESS_ELIXIR, Assets.PRINCESS_ATTACK, Assets.PRINCESS_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.VALKYRIE_IMAGE + " " + Assets.VALKYRIE_NAME,
                Assets.VALKYRIE_SYMBOL, Assets.VALKYRIE_ELIXIR, Assets.VALKYRIE_ATTACK, Assets.VALKYRIE_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.GOBLIN_IMAGE + " " + Assets.GOBLIN_NAME,
                Assets.GOBLIN_SYMBOL, Assets.GOBLIN_ELIXIR, Assets.GOBLIN_ATTACK, Assets.GOBLIN_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PK_IMAGE + " " + Assets.PK_NAME,
                Assets.PK_SYMBOL, Assets.PK_ELIXIR, Assets.PK_ATTACK, Assets.PK_DEFENSE);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Imprime el elixir restante.
     *
     * @param elixir Cantidad de elixir.
     */
    public static void printElixir(int elixir) {
        System.out.println("Elixir Restante: " + elixir);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Procedimiento que muestra el tablero con emojis y alineación ajustada (x=
     * columna, y= fila).
     *
     * @param deck Vector de personajes.
     */
    public static void printBoard(String[] deck) {
        String[][] board = new String[Assets.BOARD_ROWS][Assets.BOARD_COLUMNS];
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                board[i][j] = (i < 3) ? String.valueOf(Assets.NO_POSITION) : " ";
            }
        }
        for (String character : deck) {
            if (character != null && character.length() == 3) {
                char symbol = character.charAt(0);
                int x = Character.getNumericValue(character.charAt(1)); // x = columna
                int y = Character.getNumericValue(character.charAt(2)); // y = fila
                if (x >= 0 && x < Assets.BOARD_COLUMNS && y >= 0 && y < Assets.BOARD_ROWS) {
                    board[y][x] = Methods.getCharacterImage(symbol); // Colocar en [fila][columna]
                }
            }
        }
        System.out.println("TABLERO");
        System.out.print("   ");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("  " + j + " ");
        }
        System.out.println();
        System.out.print("   ┌");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("────");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┬");
            }
        }
        System.out.println("┐");
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            System.out.print(" " + i + " │");
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                System.out.print(String.format(" %-2s │", board[i][j]));
            }
            System.out.println();
            if (i < Assets.BOARD_ROWS - 1) {
                System.out.print("   ├");
                for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                    System.out.print("────");
                    if (j < Assets.BOARD_COLUMNS - 1) {
                        System.out.print("┼");
                    }
                }
                System.out.println("┤");
            }
        }
        System.out.print("   └");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("────");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┴");
            }
        }
        System.out.println("┘");
    }

    /**
     * Imprime los detalles de la baraja enemiga: emoji, nombre y posición.
     *
     * @param enemyDeck Baraja enemiga.
     */
    public static void printEnemyDeckDetails(String[] enemyDeck) {
        System.out.println("Cartas enemigas:");
        for (String character : enemyDeck) {
            if (character != null && character.length() == 3) {
                char symbol = character.charAt(0);
                int x = Character.getNumericValue(character.charAt(1)); // X = columna
                int y = Character.getNumericValue(character.charAt(2)); // Y = fila
                String name = Methods.getCharacterName(symbol);
                String image = Methods.getCharacterImage(symbol);
                System.out.println(image + " " + name + " en [" + x + "][" + y + "]");
            }
        }
    }

    /**
     * Configura la baraja del jugador con validaciones (x = columna, y = fila).
     *
     * @param in Scanner.
     * @param playerDeck Baraja del jugador.
     */
    public static void configureDeck(Scanner in, String[] playerDeck) {
        boolean finished = false;
        while (!finished) {
            // 1. Limpiamos la terminal
            Methods.flushScreen();

            // 2. Mostramos la informacon del tablero en pantalla
            printBoard(playerDeck);
            printCharactersInfo();
            int currentElixir = calculateCurrentElixir(playerDeck);
            printElixir(currentElixir);

            // Pedimos la jugada a realizar
            System.out.println("[X] para borrar [0] para guardar y salir");
            System.out.print("Inserte una jugada [SXY]: ");
            String input = in.nextLine().toUpperCase().trim();

            // Validamos la entrada recibida
            // Variable para almacenar mensaje de error
            String errorMessage = "";
            switch (input.length()) {
                // Comandos especiales -> longitud 1
                case 1:
                    // Comprobamos que caso de comando especial es
                    switch (input) {
                        // Borramos jugada
                        case "X":
                            // Pedimos que indique la posicion a jugar
                            System.out.print("Inserte posición a borrar [XY]: ");
                            String pos = in.nextLine().toUpperCase().trim();

                            // Validamos que la jugada sea valida
                            if (pos.length() == 2) {
                                // Recogemos las coords de la posicion a borrar
                                int x = Character.getNumericValue(pos.charAt(0)); // x = columna
                                int y = Character.getNumericValue(pos.charAt(1)); // y = fila

                                // Validamos que las coordenadas son validas
                                if (x >= 0 && x < 6 && y >= 3 && y < 6) { // Columnas 0-5, filas 3-5 para jugador
                                    // Variable para mostrar mensaje en caso de error
                                    boolean found = false;
                                    // Rcorremos el deck del jugador en busca de la posicion insertada
                                    for (int i = 0; i < playerDeck.length; i++) {
                                        if (playerDeck[i] != null && playerDeck[i].length() == 3
                                                && Character.getNumericValue(playerDeck[i].charAt(1)) == x
                                                && Character.getNumericValue(playerDeck[i].charAt(2)) == y) {
                                            // Borramos la jugada
                                            playerDeck[i] = "";
                                            // Cambiamos la variable para que no haya mensaje de error
                                            found = true;
                                        }
                                    }
                                    // Mostramos mensaje de error
                                    if (!found) {
                                        errorMessage = "Posición no ocupada.";
                                    }

                                } else {
                                    errorMessage = "Posición inválida (columnas 0-5, filas 3-5).";
                                }

                                // Si no es de longitud 2, mostramo mensaje de error
                            } else {
                                errorMessage = "Formato inválido.";
                            }
                            break; // Fin caso X
                        case "0":
                            finished = true;
                            break; // Fin caso 0

                        // Comandos no expectados
                        default:
                            errorMessage = "Comando no válido.";
                            break; // Fin casos excepcionales
                    }
                    break;// Fin casos especiales

                // Fin casos especiales
                // Jugada normal -> longitud 3
                case 3:
                    // Recogemos los datos de la jugada a insertar
                    // Simbolo del personaje
                    char symbol = input.charAt(0);
                    // Columna
                    int x = Character.getNumericValue(input.charAt(1));
                    // Fila 
                    int y = Character.getNumericValue(input.charAt(2));

                    // Comprobamos si la jugada insertada es valida
                    if (isValidSymbol(symbol) && x >= 0 && x < 6 && y >= 3 && y < 6 && currentElixir >= Methods.getCharacterElixir(symbol)) {
                        // Variable para almacenar si la posicion esta ocupada o no
                        boolean occupied = false;

                        // Recorremos el deck del jugador
                        // CHECK
                        for (String p : playerDeck) {
                            // Validamos 
                            if (p != null && p.length() == 3 && Character.getNumericValue(p.charAt(1)) == x && Character.getNumericValue(p.charAt(2)) == y) {
                                occupied = true;
                                break;
                            }
                        }
                        // Si no esta ocupado
                        if (!occupied) {
                            // CHECK
                            for (int i = 0; i < playerDeck.length; i++) {
                                if (playerDeck[i] == null || playerDeck[i].isEmpty()) {
                                    playerDeck[i] = "" + symbol + x + y;
                                    break;
                                }
                            }
                            // Si esta ocupada, mostramos mensaje de error
                        } else {
                            errorMessage = "Posición ocupada.";
                        }
                        // La jugada insertada no es valida, mostramos mensaje de error 
                    } else {
                        errorMessage = "Jugada inválida o elixir insuficiente (columnas 0-5, filas 3-5).";
                    }
                    break;// Fin casos normales

                // Error -> Cualquier otra longitud
                default:
                    errorMessage = "Formato inválido.";
                    break;// Fin switch por longitud
            }
            // Mostrar mensaje de error después de limpiar y antes de imprimir tablero
            if (!errorMessage.isEmpty()) {
                Methods.flushScreen();
                System.out.println(errorMessage);
                System.out.println("Presiona Enter para continuar...");
                in.nextLine();
            }
        }
    }

    /**
     * Calcula el elixir actual basado en personajes colocados.
     *
     * @param playerDeck Baraja del jugador.
     * @return Elixir restante.
     */
    public static int calculateCurrentElixir(String[] playerDeck) {
        int used = 0;
        for (String p : playerDeck) {
            if (p != null && !p.isEmpty()) {
                used += Methods.getCharacterElixir(p.charAt(0));
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
        for (String p : deck) {
            if (p != null && !p.isEmpty()) {
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
     * Guarda la baraja en Barajas/BarajaGuardada.txt.
     *
     * @param playerDeck Baraja.
     * @return True si exitoso.
     */
    public static boolean saveDeck(String[] playerDeck) {
        try {
            Path dir = Paths.get("Barajas");
            if (!Files.exists(dir)) {
                Files.createDirectory(dir);
            }
            try (PrintWriter writer = new PrintWriter(new FileWriter("Barajas/BarajaGuardada.txt"))) {
                for (String p : playerDeck) {
                    if (p != null && !p.isEmpty()) {
                        writer.print(p + " ");
                    }
                }
            }
            return true;
        } catch (IOException e) {
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
        try {
            Methods.initializeDeck(playerDeck);
            String content = new String(Files.readAllBytes(Paths.get("Barajas/BarajaGuardada.txt")));
            String[] parts = content.trim().split("\\s+");
            for (int i = 0; i < parts.length && i < playerDeck.length; i++) {
                if (parts[i].length() == 3) {
                    playerDeck[i] = parts[i];
                }
            }
            return true;
        } catch (IOException e) {
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
        try {
            Path filePath = Paths.get("Barajas/BarajasEnemigas.txt");
            if (!Files.exists(filePath)) {
                System.out.println("Archivo Barajas/BarajasEnemigas.txt no encontrado.");
                return null;
            }
            String[] lines = new String(Files.readAllBytes(filePath)).split("\\n");
            if (lines.length == 0) {
                System.out.println("Archivo Barajas/BarajasEnemigas.txt está vacío.");
                return null;
            }
            String line = lines[(int) (Math.random() * lines.length)].trim();
            if (line.isEmpty()) {
                System.out.println("Línea seleccionada en Barajas/BarajasEnemigas.txt está vacía.");
                return null;
            }
            String[] enemyDeck = new String[Assets.INITIAL_ELIXIR];
            Methods.initializeDeck(enemyDeck);
            String[] parts = line.split("\\s+");
            for (int i = 0; i < parts.length && i < enemyDeck.length; i++) {
                if (parts[i].length() == 3) {
                    char symbol = parts[i].charAt(0);
                    int x = Character.getNumericValue(parts[i].charAt(1)); // X = columna
                    int y = Character.getNumericValue(parts[i].charAt(2)); // Y = fila
                    // Mapear filas del jugador (3-5) a filas enemigas (0-2)
                    if (y >= 3 && y <= 5) {
                        y -= 3; // 3->0, 4->1, 5->2
                    }
                    enemyDeck[i] = "" + symbol + x + y;
                }
            }
            return enemyDeck;
        } catch (IOException e) {
            System.out.println("Error de I/O al cargar Barajas/BarajasEnemigas.txt: " + e.getMessage());
            return null;
        }
    }
}
