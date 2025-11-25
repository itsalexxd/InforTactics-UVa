
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class prueba {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Baraja del jugador
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR];

        // Elixir inicial
        int elixir = Assets.INITIAL_ELIXIR;

        // Inicializar baraja del jugador vacía
        Methods.initializeDeck(playerDeck);

        // Limpiar pantalla inicial
        Methods.flushScreen();

        String option = printMenu(in);

        while (true) {
            switch (option) {
                case "1" -> { // Nueva Partida
                    if (hasCharacters(playerDeck)) {
                        String[] enemyDeck = loadRandomEnemyDeck();
                        if (enemyDeck != null) {
                            System.out.println("Baraja enemiga cargada:");
                            printBoard(enemyDeck);
                            System.out.println("\nPresiona Enter para comenzar...");
                            in.nextLine();
                            Methods.startGame(in, playerDeck, enemyDeck);
                        } else {
                            System.out.println("Error al cargar baraja enemiga.");
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
     * Imprime la información de los personajes en formato tabular.
     */
    public static void printCharactersInfo() {
        System.out.println("\nPERSONAJES DISPONIBLES:");
        System.out.printf("%-15s %-6s %-6s %-8s %-8s%n", "Personaje", "Símb.", "Elixir", "%Ataque", "%Defensa");
        System.out.println("-----------------------------------------------------");
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.ARCHER_IMAGE + " " + Assets.ARCHER_NAME, String.valueOf(Assets.ARCHER_SYMBOL),
                Assets.ARCHER_ELIXIR, Assets.ARCHER_ATTACK, Assets.ARCHER_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.DRAGON_IMAGE + " " + Assets.DRAGON_NAME, String.valueOf(Assets.DRAGON_SYMBOL),
                Assets.DRAGON_ELIXIR, Assets.DRAGON_ATTACK, Assets.DRAGON_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PRINCESS_IMAGE + " " + Assets.PRINCESS_NAME, String.valueOf(Assets.PRINCESS_SYMBOL),
                Assets.PRINCESS_ELIXIR, Assets.PRINCESS_ATTACK, Assets.PRINCESS_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.VALKYRIE_IMAGE + " " + Assets.VALKYRIE_NAME, String.valueOf(Assets.VALKYRIE_SYMBOL),
                Assets.VALKYRIE_ELIXIR, Assets.VALKYRIE_ATTACK, Assets.VALKYRIE_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.GOBLIN_IMAGE + " " + Assets.GOBLIN_NAME, String.valueOf(Assets.GOBLIN_SYMBOL),
                Assets.GOBLIN_ELIXIR, Assets.GOBLIN_ATTACK, Assets.GOBLIN_DEFENSE);
        System.out.printf("%-15s %-6s %-6d %-8d %-8d%n",
                Assets.PK_IMAGE + " " + Assets.PK_NAME, String.valueOf(Assets.PK_SYMBOL),
                Assets.PK_ELIXIR, Assets.PK_ATTACK, Assets.PK_DEFENSE);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Imprime el elixir restante.
     *
     * @param elixir Cantidad de elixir.
     */
    public static void printElixir(int elixir) {
        System.out.println("Elixir Restante 🩸: " + elixir);
        System.out.println("-----------------------------------------------------");
    }

    /**
     * Procedimiento que muestra el tablero. Corrige la lógica para posiciones
     * vacías.
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
                int x = Character.getNumericValue(character.charAt(1));
                int y = Character.getNumericValue(character.charAt(2));
                if (x >= 0 && x < Assets.BOARD_ROWS && y >= 0 && y < Assets.BOARD_COLUMNS) {
                    board[x][y] = Methods.getCharacterImage(symbol);
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
            System.out.print("───");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┬");
            }
        }
        System.out.println("┐");
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            System.out.print(" " + i + " │");
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                System.out.print(" " + board[i][j] + " │");
            }
            System.out.println();
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
        System.out.print("   └");
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("───");
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┴");
            }
        }
        System.out.println("┘");
    }

    /**
     * Configura la baraja del jugador con validaciones.
     *
     * @param in Scanner.
     * @param playerDeck Baraja del jugador.
     */
    public static void configureDeck(Scanner in, String[] playerDeck) {
        boolean finished = false;
        while (!finished) {
            Methods.flushScreen();

            printBoard(playerDeck);
            printCharactersInfo();
            int currentElixir = calculateCurrentElixir(playerDeck);
            printElixir(currentElixir);

            System.out.println("[X] para borrar [0] para guardar y salir");
            System.out.print("Inserte una jugada [SXY]: ");
            String input = in.nextLine().toUpperCase().trim();
            switch (input.length()) {
                case 1:
                    switch (input) {
                        case "X":
                            System.out.print("Inserte posición a borrar [XY]: ");
                            String pos = in.nextLine().toUpperCase().trim();
                            if (pos.length() == 2) {
                                int x = Character.getNumericValue(pos.charAt(0));
                                int y = Character.getNumericValue(pos.charAt(1));
                                if (x >= 3 && x < 6 && y >= 0 && y < 6) {
                                    for (int i = 0; i < playerDeck.length; i++) {
                                        if (playerDeck[i] != null && playerDeck[i].length() == 3
                                                && Character.getNumericValue(playerDeck[i].charAt(1)) == x
                                                && Character.getNumericValue(playerDeck[i].charAt(2)) == y) {
                                            playerDeck[i] = "";
                                            break;
                                        }
                                    }
                                } else {
                                    System.out.println("Posición inválida.");
                                }
                            } else {
                                System.out.println("Formato inválido.");
                            }
                            break;
                        case "0":
                            finished = true;
                            break;
                        default:
                            System.out.println("Comando no válido.");
                            break;
                    }
                    break;

                case 3:
                    char symbol = input.charAt(0);
                    int x = Character.getNumericValue(input.charAt(1));
                    int y = Character.getNumericValue(input.charAt(2));
                    if (isValidSymbol(symbol) && x >= 3 && x < 6 && y >= 0 && y < 6 && currentElixir >= Methods.getCharacterElixir(symbol)) {
                        boolean occupied = false;
                        for (String p : playerDeck) {
                            if (p != null && p.length() == 3 && Character.getNumericValue(p.charAt(1)) == x && Character.getNumericValue(p.charAt(2)) == y) {
                                occupied = true;
                                break;
                            }
                        }
                        if (!occupied) {
                            for (int i = 0; i < playerDeck.length; i++) {
                                if (playerDeck[i] == null || playerDeck[i].isEmpty()) {
                                    playerDeck[i] = "" + symbol + x + y;
                                    break;
                                }
                            }
                        } else {
                            System.out.println("Posición ocupada.");
                        }
                    } else {
                        System.out.println("Jugada inválida o elixir insuficiente.");
                    }
                    break;
                default:
                    System.out.println("Formato inválido.");
                    break;
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
     * Carga una baraja enemiga aleatoria desde BarajasEnemigas.txt.
     *
     * @return Baraja enemiga o null si error.
     */
    public static String[] loadRandomEnemyDeck() {
        try {
            String[] lines = new String(Files.readAllBytes(Paths.get("Barajas/BarajasEnemigas.txt"))).split("\\n");
            String line = lines[(int) (Math.random() * lines.length)].trim();
            String[] enemyDeck = new String[Assets.INITIAL_ELIXIR];
            Methods.initializeDeck(enemyDeck);
            String[] parts = line.split("\\s+");
            for (int i = 0; i < parts.length && i < enemyDeck.length; i++) {
                if (parts[i].length() == 3) {
                    enemyDeck[i] = parts[i];
                }
            }
            return enemyDeck;
        } catch (IOException e) {
            return null;
        }
    }
}
