package src;

public class Input {
    public static int scanInt(String message) {
        while (true) {
            try {
                int resultado = Integer.parseInt(IO.readln(message));
                return resultado;
            } catch (Exception e) {
                IO.println("Valor inválido! Digite um número inteiro.");
            }
        }
    }
}
