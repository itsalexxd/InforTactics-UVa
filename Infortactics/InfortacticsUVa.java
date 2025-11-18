
import java.util.Scanner;

public class InfortacticsUVa {

    public static void main(String[] args) {
        // Creo el objeto in Scanner
        Scanner in = new Scanner(System.in);

        // Limpiamos la terminal para mayor claridad visual
        Methods.flushScreen();
        

        // Cerramos el objeto Scanner in
        in.close();
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
    }// Fin printBoard

}
