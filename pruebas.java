import java.util.Scanner;
public class pruebas {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        char[] characters = {'A', 'D', 'P', 'V', 'G', 'K'};

        char symbol = sc.nextLine().toUpperCase().charAt(0);

        if (characters.equals(symbol)) {
            System.out.println("El personaje existe.");
        } else {
            System.out.println("El personaje no existe.");
        }
        


        sc.close();
    }
    
}
