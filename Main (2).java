import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

class Main {
    public static void wait(int ms) {
        try {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }
    static DecimalFormat format = new DecimalFormat("0.#");
    public static final Scanner s = new Scanner(System.in);

    public static void main(String[] args) {

        String infixExpression = "";
        String postfixExpression = "";
        HashMap<Character, Double> expressionValues = new HashMap<Character, Double>();
        int maxCompleted = 0;
        String notCompleted = "   ";
        String completed = " x ";
        int opcao;

        while (true) {
            System.out.println("\n\n" + new String(new char[21]).replace("\0", "#") + " MENU " + new String(new char[21]).replace("\0", "#"));
            System.out.println((maxCompleted >= 1? completed:notCompleted) + "1. Inserir a expressao na notacao infixa");
            System.out.println((maxCompleted >= 2? completed:notCompleted) + "2. Inserir os valores numericos das variaveis");
            System.out.println((maxCompleted >= 3? completed:notCompleted) + "3. Converter a expressao para notacao posfixa");
            System.out.println((maxCompleted >= 4? completed:notCompleted) + "4. Avaliar a expressao");
            System.out.println(notCompleted + "5. Encerrar o programa");
            System.out.println(new String(new char[48]).replace("\0", "#"));
            System.out.print("\nDigite a opcao desejada: ");
            
            opcao = s.nextInt();
            
            if (opcao == 5) {
                break;
            }
            
            String unavailable = "\nPara acessar esta opção, é necessário executar todas as anteriores.";

            switch (opcao) {
                case 1:
                    infixExpression = Expression.expressionInput();
                    System.out.print("\nA notação infixa inserida foi: " + infixExpression);
                    maxCompleted = 1;
                    wait(3500);
                    break;
                case 2:
                    if (maxCompleted < opcao - 1) {
                        System.out.println(unavailable);
                        break;
                    }
                    expressionValues = Expression.expressionValuesInput(infixExpression);
                    System.out.println("\nOs valores inseridos foram:");
                    for (char i : expressionValues.keySet()) {
                        System.out.print("\n" + i + " = " + expressionValues.get(i));
                    }
                    maxCompleted = 2;
                    wait(3500);
                    break;
                case 3:
                    if (maxCompleted < opcao - 1) {
                        System.out.print(unavailable);
                        wait(3500);
                        break;
                    }
                    postfixExpression = Expression.expressionConversion(infixExpression);
                    System.out.print("\nA expressão convertida em notação posfixa é: " + postfixExpression);
                    maxCompleted = 3;
                    wait(3500);
                    break;
                case 4:
                    if (maxCompleted < opcao - 1) {
                        System.out.print(unavailable);
                        wait(3500);
                        break;
                    }

                    System.out.print('\n' + "Com os valores " + expressionValues.entrySet() + ", " + postfixExpression + " = " + format.format(Expression.evaluateExpression(postfixExpression, expressionValues)));
                    maxCompleted = 4;
                    wait(3500);
                    break;
            }
        }
    }
}