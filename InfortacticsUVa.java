/*
    Split en loadDeck y loadRandomEnemyDeck
    Elixir, variable que no se usa

    Optimizar en la medida de lo posible
 */
import java.io.*;
import java.nio.file.*;
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
        // Creamos el objeto sc de tipo Scanner para entrada de usuario por teclado en la consola
        Scanner sc = new Scanner(System.in);
        // Baraja del jugador
        String[] playerDeck = new String[Assets.INITIAL_ELIXIR];
        // Elixir inicial del jugador
        int elixir = Assets.INITIAL_ELIXIR;
        // Inicializar baraja del jugador vacía
        Methods.initializeDeck(playerDeck);
        // Limpiar pantalla inicial
        Methods.flushScreen();
        // Mostrar menú inicial y leer opcion insertada
        String option = printMenu(sc);
        // Bucle principal del menu
        boolean exit = false;
        while (!exit) {
            // En funcion de la opción seleccionada realizar acción
            switch (option) {
                case "1":       // --- Nueva Partida --- //
                    // Ejecuto la funcion en cuestion
                    logicaNuevaPartida(sc, option, playerDeck);
                    // Pedimos la nueva opcion del menu
                    option = printMenu(sc);
                    break;

                case "2":       // --- Configurar Baraja --- //
                    logicaConfigurarBaraja(sc, playerDeck, elixir);
                    // Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(sc);
                    break;

                case "3":       // --- Guardar Baraja --- //
                    logicaGuardarBaraja(playerDeck);
                    // 3. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(sc);
                    break;

                case "4":       // --- Cargar Baraja --- //
                    logicaCargarBaraja(playerDeck, elixir);
                    // 4. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(sc);
                    break;

                case "5":       // --- Salir --- //
                    // 1. Limpiamos la pantalla
                    Methods.flushScreen();
                    // 2. Despedida
                    System.out.println(PURPLE + BOLD + "¡Hasta luego!" + RESET);
                    exit = true;
                    break;

                default:        // --- Opción no válida o no contemplada --- //
                    // 1. Limpiamos la pantalla
                    Methods.flushScreen();
                    // 2. Informamos al usuario
                    System.out.println(RED + BOLD + "Opción no válida." + RESET);
                    // 3. Volvemos a mostrar el menu y pedimos opcion
                    option = printMenu(sc);
                    break;
            }// Fin switch
        }// Fin while
        // Cerramos el scanner
        sc.close();
    } // Fin main

    // ###### METODOS ###### //
    /*
     * Logica principal para iniciar la partida e imprime la partida completa
     * 
     * @param in Scanner, option String, playerDeck String[]
     */
    public static void logicaNuevaPartida(Scanner in, String option, String[] playerDeck) {
        // 1. Comprobamos que el jugador ha realizado cambios en su baraja y tiene al menos un personaje
        if (hasCharacters(playerDeck)) { // Baraja configurada
            // Cargamos baraja enemiga aleatoria
            String[] enemyDeck = loadRandomEnemyDeck();
            // Comprobamos que la baraja enemiga ha sido cargada correctamente
            if (enemyDeck != null) {
                // 1. Limpiamos pantalla para mayor claridad
                Methods.flushScreen();
                // Mostramos la baraja enemiga y los detalles relacionados
                System.out.println(YELLOW + BOLD + "Baraja enemiga cargada: " + RESET);
                printEnemyDeckDetails(enemyDeck);
                printBoard(enemyDeck);
                // 3. Esperamos a que el usuario presione "Enter" para comenzar la partida
                System.out.println(); // Espaciado para mejor claridad visual en la salida
                System.out.println(YELLOW + BOLD + "Presiona [Enter] para comenzar..." + RESET);
                in.nextLine();
                // 4. Iniciamos la partida
                Methods.startGame(in, playerDeck, enemyDeck);
            } else { // En caso de que no se haya cargado la baraja enemiga correctamente: 
                // 1. Limpiamos la pantalla 
                Methods.flushScreen();
                // 2. Mostramos mensaje de error
                System.out.println(RED + BOLD + "Error al cargar la baraja enemiga." + RESET);
                System.out.println(RED + BOLD + "Verifica que la ruta /Barajas/BarajasEnemigas.txt exista y tenga contenido." + RESET);
            }
        } else { // Baraja no configurada
            // 1. Limpiamos la pantalla
            Methods.flushScreen();
            // Mostramos mensaje de error
            System.out.println(RED + BOLD + "¡Configura tu baraja antes!" + RESET);
        } // Fin if inicial
        // Limpiamos la terminal
        Methods.flushScreen();
    } // Fin nuevaPartida

    /*
     * Logica para el caso 2 del switch, configurar la baraja del jugador
     * 
     * @param 
     */
    public static void logicaConfigurarBaraja(Scanner sc, String[] playerDeck, int elixir) {
        // 1. Configuramos la baraja del jugador
        configureDeck(sc, playerDeck);
        // 2. Recalculamos el elixir actual
        elixir = calculateCurrentElixir(playerDeck);
        // 3. Limpiamos la pantalla
        Methods.flushScreen();
    } // Fin logicaConfigurarBaraja

    /*
     * Logica para guardar la baraja dentro de la ruta /Barajas/BarajasGuardadas.txt
     * 
     * @param playerDeck String[]
     */
    public static void logicaGuardarBaraja(String[] playerDeck) {
        // 1. Guardamos la baraja del jugador 
        if (saveDeck(playerDeck)) {
            // Limpiamos la pantalla
            Methods.flushScreen();
            // 2. Informamos al usuario
            System.out.println(GREEN + BOLD + "Baraja guardada correctamente." + RESET);
        } else { // En caso de que no se haya guardado correctamente notificamos
            // 1. Limpiamos la pantalla
            Methods.flushScreen();
            // 2. Mostramos mensaje de error
            System.out.println(RED + BOLD + "Error al guardar la baraja." + RESET);
        }
        // Limpiamos la pantalla
        Methods.flushScreen();
    } // Fin logicaGuardarBaraja

    /*
     * Logica para cargar la baraja guardada en el archivo /Baraja/BarajasGuardadas.txt
     * 
     * @param playerDeck String[], elixir int
     */
    public static void logicaCargarBaraja(String[] playerDeck, int elixir) {
        // 1. Comprobamos que la bara ya se ha cargado correctamente
        if (loadDeck(playerDeck)) {
            // 2. Recalculamos el elixir actual
            elixir = calculateCurrentElixir(playerDeck);
            // Limpiamos la pantalla
            Methods.flushScreen();
            // 3. Informamos al usuario
            System.out.println(GREEN + BOLD + "Baraja cargada correctamente." + RESET);

        } else { // En caso de que no se haya cargado correctamente notificamos
            // 1. Limpiamos la pantalla
            Methods.flushScreen();
            // 2. Mostramos mensaje de error
            System.out.println(RED + BOLD + "Error al cargar la baraja." + RESET);
        }
    } // Fin logicaCargarBaraja

    /**
     * Muestra el menú inicial y lee la opción del usuario.
     *
     * @param in Scanner para entrada.
     * @return Opción seleccionada.
     */
    public static String printMenu(Scanner in) {
        System.out.println(YELLOW + BOLD + "┌─────────────────────────────────┐");
        System.out.println("│      🏯 InforTactics UVa 🏯     │");
        System.out.println("├─────────────────────────────────┤");
        System.out.println("│   1. NUEVA PARTIDA              │");
        System.out.println("│   2. CONFIGURAR BARAJA          │");
        System.out.println("│   3. GUARDAR BARAJA             │");
        System.out.println("│   4. CARGAR BARAJA              │");
        System.out.println("│   5. SALIR                      │");
        System.out.println("└─────────────────────────────────┘");
        System.out.print("Inserte una opción [1-5]: " + RESET);
        return in.nextLine();
    }

    /**
     * Imprime la información de los personajes en formato tabular (con emojis)
     * con alineación mejorada.
     */
    public static void printCharactersInfo() {
        // Personaje (18), Símb. (5), Elixir (6), %Ataque (8), %Defensa (8)

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
        // Inicializar tablero vacío
        String[][] board = new String[Assets.BOARD_ROWS][Assets.BOARD_COLUMNS];

        // Rellenar tablero con posiciones vacías
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                String noPosition = "" + Assets.NO_POSITION + Assets.NO_POSITION;
                board[i][j] = (i < 3) ? noPosition : "";
            }
        }// Fin for rellenar espacios vacios

        // Colocar personajes en el tablero según la baraja
        for (int i = 0; i < deck.length; i++) {
            String character = deck[i];
            // Cada personaje es una cadena de 3 caracteres: [Símbolo][X][Y]
            if (character != null && character.length() == 3) {
                // Obtener símbolo y coordenadas
                char symbol = character.charAt(0);
                int x = character.charAt(1) - '0'; // x = columna
                int y = character.charAt(2) - '0'; // y = fila
                // Colocar el personaje en el tablero si las coordenadas son válidas
                if (x >= 0 && x < Assets.BOARD_COLUMNS && y >= 0 && y < Assets.BOARD_ROWS) {
                    String characterImage = Methods.getCharacterImage(symbol);
                    if (symbol == Assets.VALKYRIE_SYMBOL) {
                        board[y][x] = characterImage + " "; // Añadir espacio extra para Valquiria y mantener alineación;
                    } else {
                        board[y][x] = characterImage; // Colocar en [fila][columna]
                    }
                }
            }
        } // Fin for colocar personajes

        // Imprimir el tablero con bordes y alineación
        System.out.println(YELLOW + BOLD + "TABLERO" + RESET);
        // AUMENTAMOS EL ESPACIADO DE 3 A 4 ESPACIOS para mantener alineación horizontal
        System.out.print("    ");

        // Imprimir números de columna
        // Aplicamos BOLD y el color del EJE X (lo dejaré en AMARILLO para que destaque)
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(YELLOW + BOLD + "  " + j + "  " + RESET);
        }

        System.out.println();

        // --- BORDES SUPERIORES ---
        // El borde superior siempre es de la zona enemiga (ROJO)
        String colorTopBorder = RED + BOLD;
        // AUMENTAMOS EL ESPACIADO DE 3 A 4 ESPACIOS antes del borde
        System.out.print(colorTopBorder + "    ┌" + RESET);

        // Imprimir separadores de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(colorTopBorder + "────" + RESET);
            // Imprimir cruces entre columnas
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print(colorTopBorder + "┬" + RESET);
            }
        }
        // Cerrar borde superior
        System.out.println(colorTopBorder + "┐" + RESET);

        // Imprimir filas del tablero
        for (int i = 0; i < Assets.BOARD_ROWS; i++) {

            // Determinar el color para la fila actual y su contenido (RED para enemigo, BLUE para jugador)
            String fgColor = (i < 3) ? RED + BOLD : BLUE + BOLD;

            // Imprimir número de fila
            // CAMBIO CLAVE: Usamos %4d para crear 3 espacios de padding y el dígito (e.g., "   0")
            // Esto reserva 4 caracteres, añadiendo un espacio de separación con la línea vertical.
            System.out.print(YELLOW + BOLD + String.format("%3d", i) + RESET);

            // Imprimir el separador vertical al inicio de la fila
            System.out.print(fgColor + " │" + RESET);

            // Imprimir celdas de la fila
            for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {

                // Aplicar el color de primer plano al contenido de la celda
                String cellContent = String.format(" %-2s ", board[i][j]);

                // Imprimimos el contenido coloreado
                System.out.print(fgColor + cellContent + RESET);

                // Imprimimos el separador vertical (│) de la zona
                System.out.print(fgColor + "│" + RESET);
            }

            System.out.println();

            // Imprimir separador entre filas
            if (i < Assets.BOARD_ROWS - 1) {

                // --- LÓGICA DE COLOR DE BORDE INTERMEDIO ACTUALIZADA ---
                String interBorderColor;
                if (i == 2) {
                    // Si i=2, es la línea entre fila 2 y 3 (ahora WHITE)
                    interBorderColor = RESET + BOLD;
                } else if (i < 2) {
                    // i=0, 1: Bordes internos del enemigo (RED)
                    interBorderColor = RED + BOLD;
                } else {
                    // i=2 (Línea divisoria principal) o i=4 (Borde interno del jugador)
                    interBorderColor = BLUE + BOLD;
                }

                // Imprimir borde intermedio
                // AUMENTAMOS EL ESPACIADO DE 3 A 4 ESPACIOS
                System.out.print(interBorderColor + "    ├" + RESET);

                // Imprimir separadores de columna
                for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
                    System.out.print(interBorderColor + "────" + RESET);
                    // Imprimir cruces entre columnas
                    if (j < Assets.BOARD_COLUMNS - 1) {
                        System.out.print(interBorderColor + "┼" + RESET);
                    }
                }
                // Cerrar borde intermedio
                System.out.println(interBorderColor + "┤" + RESET);
            }
        }

        // Imprimir borde inferior (Final de la zona Azul)
        String colorBottomBorder = BLUE + BOLD;
        // AUMENTAMOS EL ESPACIADO DE 3 A 4 ESPACIOS
        System.out.print(colorBottomBorder + "    └" + RESET);

        // Imprimir separadores de columna
        for (int j = 0; j < Assets.BOARD_COLUMNS; j++) {
            System.out.print(colorBottomBorder + "────" + RESET);
            // Imprimir cruces entre columnas
            if (j < Assets.BOARD_COLUMNS - 1) {
                System.out.print(colorBottomBorder + "┴" + RESET);
            }
        }
        // Cerrar borde inferior
        System.out.println(colorBottomBorder + "┘" + RESET);

    }// Fin printBoard

    /**
     * Imprime los detalles de la baraja enemiga: emoji, nombre y posición.
     *
     * @param enemyDeck Baraja enemiga.
     */
    public static void printEnemyDeckDetails(String[] enemyDeck) {
        // Imprimir detalles de la baraja enemiga
        // Cada personaje es una cadena de 3 caracteres: [Símbolo][X][Y]
        for (int i = 0; i < enemyDeck.length; i++) {
            // Verificar que el personaje no sea nulo y tenga la longitud correcta
            if (enemyDeck[i] != null && enemyDeck[i].length() == 3) {
                // Obtener símbolo y coordenadas
                char symbol = enemyDeck[i].charAt(0);
                int x = (int) enemyDeck[i].charAt(1) - '0'; // X = columna
                int y = (int) enemyDeck[i].charAt(2) - '0'; // Y = fila
                // Imprimir detalles del personaje
                String name = Methods.getCharacterName(symbol);
                String image = Methods.getCharacterImage(symbol);
                System.out.println(BOLD + image + " " + name + " en [" + x + "][" + y + "]" + RESET);
            }
        }
    } // Fin funcion printEnemyDeckDetails

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
                            // Pedimos posición a borrar
                            System.out.print(BOLD + "Inserte posición a borrar [XY]: " + RESET);
                            String pos = in.nextLine();
                            // Validamos posición
                            if (pos.length() == 2) {    // Formato correcto
                                // Obtener coordenadas
                                int x = (int) pos.charAt(0) - '0'; // X = columna
                                int y = (int) pos.charAt(1) - '0'; // Y = fila
                                // Validar rango -- Columnas 0-5, filas 3-5 para jugador
                                if (x >= 0 && x < 6 && y >= 3 && y < 6) {
                                    // Buscar y borrar personaje en la posición indicada
                                    boolean found = false;
                                    for (int i = 0; i < playerDeck.length; i++) {
                                        // Si la posicion playerDeck[i] no esta vacia - Coincide con las coordenadas - Tiene longitud 3
                                        // Borramos (cambiamos caracter por cadena vacia)
                                        if (playerDeck[i] != null && playerDeck[i].length() == 3
                                                && (playerDeck[i].charAt(1) - '0') == x
                                                && (playerDeck[i].charAt(2) - '0') == y) {
                                            playerDeck[i] = "";
                                            // Marcamos como encontrado
                                            found = true;
                                        }
                                    } // Fin for buscar personaje

                                    // Si no se ha encontrado personaje en la posición indicada
                                    if (!found) {
                                        // Mostramos mensaje de error
                                        errorMessage = "Posición ocupada.";
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
                            // 1. Limpiamos la pantalla
                            Methods.flushScreen();
                            // Marcamos como finalizado
                            finished = true;
                            break;
                        default:        // --- Comando no válido --- //
                            // 1. Limpiamos la pantalla
                            Methods.flushScreen();
                            // 2. Mostramos mensaje de error
                            errorMessage = RED + BOLD + "Comando no válido." + RESET;
                            break;
                    }// Fin switch comandos especiales
                    break;

                case 3:     // --- Colocar Personaje --- //
                    // Obtener símbolo y coordenadas 
                    char symbol = input.charAt(0); // Personaje
                    // Convierto a mayuscula en caso de que sea insertada una minuscula
                    if (symbol >= 'a' && symbol <= 'z') {
                        symbol = (char) (symbol - 32);
                    }
                    int x = (int) input.charAt(1) - '0'; // X = columna
                    int y = (int) input.charAt(2) - '0'; // Y = fila

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
                            if (p != null && p.length() == 3 && (p.charAt(1) - '0') == x && (p.charAt(2) - '0') == y) {
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
                                System.out.println(YELLOW + BOLD + "Personaje " + Methods.getCharacterName(symbol) + " colocado en [" + x + "][" + y + "]." + RESET);

                            } else {
                                System.out.println(RED + BOLD + "No es posible insertar el personaje en la baraja." + RESET);
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
                System.out.println(RED + BOLD + errorMessage + RESET);
                // 3. Esperamos a que el usuario presione Enter para continuar 
                System.out.println(YELLOW + BOLD + "Presiona Enter para continuar..." + RESET);
                in.nextLine();
            } // Fin mostrar mensaje de error
        } // Fin while configurar baraja
    } // Fin configureDeck

    /**
     * Calcula el elixir actual basado en personajes colocados.
     *
     * @param playerDeck Baraja del jugador.
     * @return elixir Elixir restante.
     */
    public static int calculateCurrentElixir(String[] playerDeck) {
        // Variable para almacenar el elixir usado
        int used = 0;
        // Recorrer la baraja y sumar el elixir de cada personaje insertado
        for (int i = 0; i < playerDeck.length; i++) {
            // Si la posición no está vacía, sumar el elixir del personaje
            if (playerDeck[i] != null && !playerDeck[i].isEmpty()) {
                // Sumar el elixir del personaje
                used += Methods.getCharacterElixir(playerDeck[i].charAt(0));
            }
        }
        // Devolver elixir restante
        return Assets.INITIAL_ELIXIR - used;
    } // Fin calculateCurrentElixir

    /**
     * Verifica si la baraja tiene al menos un personaje.
     *
     * @param deck Baraja.
     * @return True si tiene personajes.
     */
    public static boolean hasCharacters(String[] deck) {
        // Recorrer la baraja y verificar si hay al menos un personaje
        for (int i = 0; i < deck.length; i++) {
            // Si la posición no está vacía, hay al menos un personaje
            if (deck[i] != null && !deck[i].isEmpty()) {
                // Devolver true
                return true;
            }
        }
        // No se encontraron personajes, devolver false
        return false;
    } // Fin hasCharacters

    /**
     * Valida si el símbolo es de un personaje válido.
     *
     * @param symbol Símbolo.
     * @return True si válido.
     */
    public static boolean isValidSymbol(char symbol) {
        // Verificar si el símbolo corresponde a un personaje válido
        return symbol == Assets.ARCHER_SYMBOL || symbol == Assets.DRAGON_SYMBOL
                || symbol == Assets.PRINCESS_SYMBOL || symbol == Assets.VALKYRIE_SYMBOL
                || symbol == Assets.GOBLIN_SYMBOL || symbol == Assets.PK_SYMBOL;
    } // Fin isValidSymbol

    /**
     * Guarda la baraja en Barajas/BarajaGuardada.txt. Damos por hecho que la
     * ruta existe ya que se necesita la misma ruta para el archivo de las
     * barajas enemigas
     *
     * @param playerDeck Baraja.
     * @return True si exitoso.
     */
    public static boolean saveDeck(String[] playerDeck) {
        // Definimos la ruta de acceso al archivo donde se guardan las barajas
        String rutaArchivo = "Barajas/BarajaGuardada.txt";
        // 1. Intentamos abrir el fichero
        try (PrintWriter escribe = new PrintWriter(new FileWriter(rutaArchivo))) {
            // 2. Escribimos dentro del archivo como esta la configuracion actual del archivo en cuestion
            for (int i = 0; i < playerDeck.length; i++) {
                String p = playerDeck[i];
                // Escribimos solo las posiciones no nulas
                if (p != null && !p.isEmpty()) {
                    // Escribimos en el fichero
                    escribe.print(p + " ");
                }// Fin if posicion no vacia
            } // Fin for recorrer baraja
            // Si todo ha salido bien
            return true;
        } catch (IOException e) {
            // Capturamos cualquier error que haya y lo mostramos por pantalla
            System.out.println(RED + BOLD + "Error I/O al guardar la baraja: " + e.getMessage() + RESET);
            return false;
        }
    } // Fin saveDeck

    /**
     * Carga la baraja desde Barajas/BarajaGuardada.txt.
     *
     * @param playerDeck Baraja a cargar.
     * @return True si exitoso.
     */
    public static boolean loadDeck(String[] playerDeck) {
        try {
            // Inicializar baraja vacía
            Methods.initializeDeck(playerDeck);
            // Leer contenido del archivo
            // 
            String content = new String(Files.readAllBytes(Paths.get("Barajas/BarajaGuardada.txt")));
            // Dividir contenido en partes y cargar en la baraja
            //
            String[] parts = content.trim().split("\\s+");
            // Cargar personajes en la baraja
            for (int i = 0; i < parts.length && i < playerDeck.length; i++) {
                // Validar longitud del personaje
                if (parts[i].length() == 3) {
                    // Asignar personaje a la baraja
                    playerDeck[i] = parts[i];
                }
            }

            // Devolver true si exitoso
            return true;

            // Capturar excepciones de I/O
        } catch (IOException e) {
            // Devolver false si error
            return false;
        } // Fin try-catch
    } // Fin loadDeck

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
            // Creamos e inicializamos la baraja enemiga
            String[] enemyDeck = new String[Assets.INITIAL_ELIXIR];
            Methods.initializeDeck(enemyDeck);

            String[] parts = line.split("\\s+");
            System.out.println("-> LINE: " + line);

            for (int i = 0; i < parts.length && i < enemyDeck.length; i++) {
                if (parts[i].length() == 3) {
                    // Extraemos los datos
                    char symbol = parts[i].charAt(0);
                    int x = (parts[i].charAt(1) - '0'); // X = columna
                    int y = (parts[i].charAt(2) - '0'); // Y = fila
                    // Insertamos el personaje en la baraja
                    enemyDeck[i] = "" + symbol + y + x;
                }
            }
            return enemyDeck;
        } catch (IOException e) {
            System.out.println("Error de I/O al cargar Barajas/BarajasEnemigas.txt: " + e.getMessage());
            return null;
        }
    } // Fin loadRandomEnemyDeck
} // Fin clase InfortacticsUVa
