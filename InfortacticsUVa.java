
import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class InfortacticsUVa {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR]; // Baraja del jugador (máx 8 personajes)
        int elixir = Assets.INITIAL_ELIXIR;

        // Inicializar baraja del jugador vacía
        Methods.initializeDeck(playerDeck);

        // Limpiar pantalla inicial
        Methods.flushScreen();

        // Mostrar menú inicial y leer opcion insertada
        String option = printMenu(in);

        // Bucle principal del menu
        boolean exit = false;
        while (!exit) {
            // En funcion de la opción seleccionada realizar acción
            switch (option) {
                case "1":       // --- Nueva Partida --- //
                    // 1. Comprobar que la baraja del jugador tiene al menos un personaje
                    if (hasCharacters(playerDeck)) {
                        // Cargar baraja enemiga aleatoria
                        String[] enemyDeck = loadRandomEnemyDeck();
                        // Si se ha cargado correctamente, iniciar partida
                        if (enemyDeck != null) {
                            // 1. Limpiamos la pantalla
                            Methods.flushScreen();
                            // 2. Mostramos la baraja enemiga y el tablero
                            System.out.println("Baraja enemiga cargada:");
                            printEnemyDeckDetails(enemyDeck);
                            printBoard(enemyDeck);
                            // 3. Esperamos a que el usuario presione Enter para comenzar
                            System.out.println("\nPresiona Enter para comenzar...");
                            in.nextLine();
                            // 4. Iniciamos la partida
                            Methods.startGame(in, playerDeck, enemyDeck);

                        } else {// En caso de que no se haya cargado correctamente notificamos
                            // 1. Limpiamos la pantalla
                            Methods.flushScreen();
                            // 2. Mostramos mensaje de error
                            System.out.println("Error al cargar baraja enemiga. Verifica que Barajas/BarajasEnemigas.txt exista y tenga contenido.");
                        }
                        // 2. En caso contrario, informar al usuario
                    } else {
                        // 1. Limpiamos la pantalla
                        Methods.flushScreen();
                        // 2. Mostramos mensaje de error
                        System.out.println("¡Configura tu baraja antes!");
                    }
                    // Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(in);
                    break;

                case "2":       // --- Configurar Baraja --- //
                    // 1. Configuramos la baraja del jugador
                    configureDeck(in, playerDeck);
                    // 2. Recalculamos el elixir actual
                    elixir = calculateCurrentElixir(playerDeck);
                    // 3. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(in);
                    break;

                case "3":       // --- Guardar Baraja --- //
                    // 1. Guardamos la baraja del jugador 
                    if (saveDeck(playerDeck)) {
                        // 2. Informamos al usuario
                        System.out.println("Baraja guardada correctamente.");

                        // En caso de que no se haya guardado correctamente notificamos
                    } else {
                        // 1. Limpiamos la pantalla
                        Methods.flushScreen();
                        // 2. Mostramos mensaje de error
                        System.out.println("Error al guardar la baraja.");
                    }
                    // 3. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(in);
                    break;

                case "4":       // --- Cargar Baraja --- //
                    // 1. Comprobamos que la bara ya se ha cargado correctamente
                    if (loadDeck(playerDeck)) {
                        // 2. Recalculamos el elixir actual
                        elixir = calculateCurrentElixir(playerDeck);
                        // 3. Informamos al usuario
                        System.out.println("Baraja cargada correctamente.");

                        // En caso de que no se haya cargado correctamente notificamos
                    } else {
                        // 1. Limpiamos la pantalla
                        Methods.flushScreen();

                        // 2. Mostramos mensaje de error
                        System.out.println("Error al cargar la baraja.");
                    }

                    // 4. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(in);
                    break;

                case "5":       // --- Salir --- //

                    // 1. Limpiamos la pantalla
                    Methods.flushScreen();
                    // 2. Despedida
                    System.out.println("¡Hasta luego!");
                    exit = true;
                    break;

                default:        // --- Opción no válida o no contemplada --- //
                    // 1. Limpiamos la pantalla
                    Methods.flushScreen();

                    // 2. Informamos al usuario
                    System.out.println("Opción no válida.");

                    // 3. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(in);
                    break;
            }// Fin switch
        }// Fin while
        // Cerramos el scanner
        in.close();
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
        // Inicializar tablero vacío
        String[][] board = new String[Assets.BOARD_ROWS][Assets.BOARD_COLUMNS];

        // Rellenar tablero con posiciones vacías
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                board[i][j] = (i < 3) ? String.valueOf(Assets.NO_POSITION) : " ";
            }
        }// Fin for rellenar espacios vacios

        // Colocar personajes en el tablero según la baraja
        for (String character : deck) {
            // Cada personaje es una cadena de 3 caracteres: [Símbolo][X][Y]
            if (character != null && character.length() == 3) {
                // Obtener símbolo y coordenadas
                char symbol = character.charAt(0);
                int x = Character.getNumericValue(character.charAt(1)); // x = columna
                int y = Character.getNumericValue(character.charAt(2)); // y = fila
                // Colocar el personaje en el tablero si las coordenadas son válidas
                if (x >= 0 && x < Assets.BOARD_COLUMNS && y >= 0 && y < Assets.BOARD_ROWS) {
                    board[y][x] = Methods.getCharacterImage(symbol); // Colocar en [fila][columna]
                }
            }
        }// Fin for colocar personajes

        // Imprimir el tablero con bordes y alineación
        System.out.println("TABLERO");
        System.out.print("   ");

        // Imprimir números de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("  " + j + " ");
        }

        System.out.println();

        // Imprimir borde superior
        System.out.print("   ┌");
        // Imprimir separadores de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("────");
            // Imprimir cruces entre columnas
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┬");
            }
        }
        // Cerrar borde superior
        System.out.println("┐");

        // Imprimir filas del tablero
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            // Imprimir número de fila
            System.out.print(" " + i + " │");
            // Imprimir celdas de la fila
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                System.out.print(String.format(" %-2s │", board[i][j]));
            }

            System.out.println();
            // Imprimir separador entre filas
            if (i < Assets.BOARD_ROWS - 1) {
                // Imprimir borde intermedio
                System.out.print("   ├");
                // Imprimir separadores de columna
                for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                    System.out.print("────");
                    // Imprimir cruces entre columnas
                    if (j < Assets.BOARD_COLUMNS - 1) {
                        System.out.print("┼");
                    }
                }
                // Cerrar borde intermedio
                System.out.println("┤");
            }
        }

        // Imprimir borde inferior
        System.out.print("   └");
        // Imprimir separadores de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print("────");
            // Imprimir cruces entre columnas
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print("┴");
            }
        }
        // Cerrar borde inferior
        System.out.println("┘");

    }// Fin printBoard

    /**
     * Imprime los detalles de la baraja enemiga: emoji, nombre y posición.
     *
     * @param enemyDeck Baraja enemiga.
     */
    public static void printEnemyDeckDetails(String[] enemyDeck) {
        // Imprimir detalles de la baraja enemiga
        System.out.println("Cartas enemigas:");
        // Cada personaje es una cadena de 3 caracteres: [Símbolo][X][Y]
        for (String character : enemyDeck) {
            // Verificar que el personaje no sea nulo y tenga la longitud correcta
            if (character != null && character.length() == 3) {
                // Obtener símbolo y coordenadas
                char symbol = character.charAt(0);
                int x = Character.getNumericValue(character.charAt(1)); // X = columna
                int y = Character.getNumericValue(character.charAt(2)); // Y = fila
                // Imprimir detalles del personaje
                String name = Methods.getCharacterName(symbol);
                String image = Methods.getCharacterImage(symbol);
                System.out.println(image + " " + name + " en [" + x + "][" + y + "]");
            }
        }
    }

    /**
     * Configura la baraja del jugador con validaciones (x= columna, y= fila).
     *
     * @param in Scanner.
     * @param playerDeck Baraja del jugador.
     */
    public static void configureDeck(Scanner in, String[] playerDeck) {
        int currentElixir;
        // Bucle hasta que el usuario decida salir
        boolean finished = false;
        while (!finished) {
            // 1. Limpiamos la pantalla
            Methods.flushScreen();
            // 2. Mostramos el tablero, info personajes y elixir restante
            printBoard(playerDeck); // Tablero del jugador
            printCharactersInfo();  // Info personajes
            currentElixir = calculateCurrentElixir(playerDeck); // Calculamos el elixir restante
            printElixir(currentElixir); // Elixir restante
            // 3. Pedimos jugada al usuario
            System.out.println("[X] para borrar [0] para guardar y salir");
            System.out.print("Inserte una jugada [SXY]: ");
            // Leer entrada del usuario
            String input = in.nextLine().toUpperCase().trim();
            String errorMessage = ""; // Variable para almacenar mensaje de error
            // Procesar entrada del usuario
            switch (input.length()) {
                case 1:     // --- Comandos Especiales --- //
                    switch (input) {    // Switch comandos especiales
                        case "X":       // --- Borrar Personaje --- //
                            // Pedimos posición a borrar
                            System.out.print("Inserte posición a borrar [XY]: ");
                            String pos = in.nextLine().toUpperCase().trim();
                            // Validamos posición
                            if (pos.length() == 2) {    // Formato correcto
                                // Obtener coordenadas
                                int x = Character.getNumericValue(pos.charAt(0)); // x = columna
                                int y = Character.getNumericValue(pos.charAt(1)); // y = fila

                                // Validar rango -- Columnas 0-5, filas 3-5 para jugador
                                if (x >= 0 && x < 6 && y >= 3 && y < 6) {
                                    // Buscar y borrar personaje en la posición indicada
                                    boolean found = false;
                                    for (int i = 0; i < playerDeck.length; i++) {
                                        // Si la posicion playerDeck[i] no esta vacia - Coincide con las coordenadas - Tiene longitud 3
                                        // Borramos (cambiamos caracter por cadena vacia)
                                        if (playerDeck[i] != null && playerDeck[i].length() == 3
                                                && Character.getNumericValue(playerDeck[i].charAt(1)) == x
                                                && Character.getNumericValue(playerDeck[i].charAt(2)) == y) {
                                            playerDeck[i] = "";
                                            // Marcamos como encontrado
                                            found = true;
                                        }
                                    } // Fin for buscar personaje

                                    // Si no se ha encontrado personaje en la posición indicada
                                    if (!found) {
                                        // Mostramos mensaje de error
                                        errorMessage = "Posición no ocupada.";
                                    }

                                } else {
                                    // Posición fuera de rango
                                    errorMessage = "Posición inválida (columnas 0-5, filas 3-5).";
                                }
                            } else {
                                // Formato inválido
                                errorMessage = "Formato inválido.";
                            }
                            break; // Fin borrar personaje
                        case "0":       // --- Guardar y Salir --- //
                            // Marcamos como finalizado
                            finished = true;
                            break;
                        default:        // --- Comando no válido --- //
                            // 1. Limpiamos la pantalla
                            Methods.flushScreen();
                            // 2. Mostramos mensaje de error
                            errorMessage = "Comando no válido.";
                            break;
                    }// Fin switch comandos especiales
                    break;

                case 3:     // --- Colocar Personaje --- //
                    // Obtener símbolo y coordenadas 
                    char symbol = input.charAt(0);
                    int x = Character.getNumericValue(input.charAt(1)); // x = columna
                    int y = Character.getNumericValue(input.charAt(2)); // y = fila

                    // Validar símbolo, rango y elixir suficiente
                    if (isValidSymbol(symbol) && x >= 0 && x < 6 && y >= 3 && y < 6 && currentElixir >= Methods.getCharacterElixir(symbol)) {
                        // Verificar si la posición ya está ocupada
                        boolean occupied = false;

                        // Recorrer la baraja para verificar si algun personaje ocupa la posición (x,y)
                        int pos = 0;
                        // Bucle hasta encontrar posición ocupada o recorrer toda la baraja
                        while (pos < playerDeck.length && !occupied) {
                            // Obtener personaje en la posición actual
                            String p = playerDeck[pos];
                            // Verificar si el personaje coincide con la posición (x,y)
                            if (p != null && p.length() == 3 && Character.getNumericValue(p.charAt(1)) == x && Character.getNumericValue(p.charAt(2)) == y) {
                                // Posición ocupada, actualizamos variable
                                occupied = true;
                            }
                            // Incrementar posición para siguiente iteración
                            pos++;
                        } // Fin while verificar posición ocupada

                        // Si no está ocupada, colocar el personaje
                        if (!occupied) {
                            int posInsertar = -1;
                            boolean espacioLibreEncontrado = false;

                            int i = 0;
                            while (i < playerDeck.length && !espacioLibreEncontrado) {
                                // Si la posición está libre, la guardamos para insertar
                                if (playerDeck[i] == null || playerDeck[i].isEmpty()) {
                                    posInsertar = i;
                                    espacioLibreEncontrado = true;
                                }
                                // Pasamos a la siguiente posicion
                                i++;
                            }

                            if (posInsertar != -1) {
                                String personajeInsertar = String.valueOf(symbol) + x + y;
                                playerDeck[posInsertar] = personajeInsertar;
                                System.out.println("Personaje " + Methods.getCharacterName(symbol) + " colocado en [" + x + "][" + y + "].");

                            } else {
                                System.out.println("No es posible insertar el personaje en la baraja.");
                            }

                            // Si está ocupada, mostrar mensaje de error
                        } else {
                            // Posición ocupada
                            errorMessage = "Posición ocupada.";
                        }
                    } else {
                        // Símbolo inválido, posición fuera de rango o elixir insuficiente
                        errorMessage = "Jugada inválida o elixir insuficiente (columnas 0-5, filas 3-5).";
                    }
                    break;
                default:    // --- Formato inválido --- //
                    errorMessage = "Formato inválido.";
                    break;
            } // Fin switch longitud input

            // Mostrar mensaje de error después de limpiar y antes de imprimir tablero
            if (!errorMessage.isEmpty()) {
                // 1. Limpiamos la pantalla
                Methods.flushScreen();
                // 2. Mostramos el mensaje de error
                System.out.println(errorMessage);
                // 3. Esperamos a que el usuario presione Enter para continuar 
                System.out.println("Presiona Enter para continuar...");
                in.nextLine();
            } // Fin mostrar mensaje de error
        } // Fin while configurar baraja
    } // Fin configureDeck

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
            Path filePath = Paths.get("/Users/aalexgarc_/Documents/GIT/InforTactics-UVa/BarajasEnemigas.txt");
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
