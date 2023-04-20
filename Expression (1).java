import java.lang.Math;
import java.util.HashMap;

/**
 * Trata uma expressão infixa, realizando o pedido de input, atribuição de valores às variáveis, conversão para posfixa e avaliação da expressão.
 * 
 * @author Felipe Ribeiro - 32212720
 * @author Enzo Koji      - 32273754
 * @author Nicolas Rondon - 32235208
*/
public class Expression {
    /**
     * Valida uma expressão infixa de acordo com os requisitos dados pelo enunciado do projeto.
     * Utilizado no método expressionInput.
     * 
     * @param infixExpression  String contendo uma expressão infixa
     * @return                 true se a expressão é válida, false caso contrário
     */
    private static boolean expressionValidation(String infixExpression) {
        Pilha parentheses = new Pilha();
        boolean balancedParentheses = true;
        boolean isValidExpression;

        for (int i = 0; i < infixExpression.toCharArray().length; i++) {
            char currentChar = infixExpression.toCharArray()[i];

            if (currentChar == '(') {
                parentheses.push(currentChar);
            } else if (currentChar == ')') {
                if (parentheses.isEmpty()) {
                    balancedParentheses = false;
                    break;
                }

                char popChar = (char)parentheses.pop();
                if (popChar != '(') {
                    balancedParentheses = false;
                    break;
                }
            }
        }
        
        balancedParentheses = parentheses.isEmpty()? balancedParentheses:false;
        isValidExpression = infixExpression.replaceAll("[()]", "").matches("[A-Z](\s*[()+*/^-]\s*[A-Z]){1,}");

        return balancedParentheses && isValidExpression;
    }

    /**
     * Recebe o input da expressão infixa como uma String, valida o input por meio do método expressionValidation, e retorna a String caso seja válida.
     * 
     * @return expressão infixa como String, após passar pela validação
     */
    public static String expressionInput() {
        System.out.print("\nInsira a expressão na notação infixa para conversão: ");
        String infixExpression = Main.s.nextLine();
        infixExpression = Main.s.nextLine();
        
        while (!expressionValidation(infixExpression)) {
            System.out.print("\nERRO: A expressão pode conter apenas variáveis de uma letra maiúscula, parenteses e os cinco operadores (+, -, *, / e ^).\n\nInsira a expressão na notação infixa para conversão: ");
            infixExpression = Main.s.nextLine();
        }

        return infixExpression;
    }

    /**
     * Corresponde valores às variáveis da expressão infixa por meio de um HashMap.
     * 
     * @param infixExpression  String contendo uma expressão infixa
     * @return                 HashMap<Character, Double> contendo pares de
     *                         uma variável de uma letra e seu valor numérico
     *                         como Double, respectivamente
     */
    public static HashMap<Character, Double> expressionValuesInput(String infixExpression) {
        char[] expressionVariables = infixExpression.trim().replaceAll("(?![A-Z]).", "").toCharArray();
        HashMap<Character, Double> expressionValues = new HashMap<Character, Double>();
        System.out.println();

        for (int i = 0; i < expressionVariables.length; i++) {
            if (expressionValues.containsKey(expressionVariables[i])) {
                continue;
            }
            System.out.print("Insira o valor de " + expressionVariables[i] + ": ");
            expressionValues.put(expressionVariables[i], Main.s.nextDouble());
        }
        
        return expressionValues;
    }
    
    /**
     * Converte uma expressão do formato infixo para posfixo.
     * 
     * @param infixExpression  String contendo uma expressão infixa
     * @return                 String contendo a expressão posfixa correspondente
     */
    public static String expressionConversion(String infixExpression) {
        Pilha conversion = new Pilha();
        String output = "";
        char[] infixExpressionAsCharArray = infixExpression.toCharArray();
        
        for (int i = 0; i < infixExpressionAsCharArray.length; i++) {
            char currentChar = infixExpressionAsCharArray[i];
            if (currentChar >= 65 && currentChar <= 90) { // A-Z
                output += currentChar;
            } else if (currentChar == 40) { // (
                conversion.push(currentChar);
            } else if (currentChar == 41) { // )
                while ((char)conversion.peek() != 40) {
                    output+= conversion.pop();
                }
                
                conversion.pop();
            } else if (currentChar == 42 || currentChar == 43 || currentChar == 45 || currentChar == 47 || currentChar == 94) {
                switch (currentChar) {
                    case 42: // *
                        while (!conversion.isEmpty() && ((char)conversion.peek() == 94 || (char)conversion.peek() == 47 || (char)conversion.peek() == 42)) {
                            output += conversion.pop();
                        }
                        
                        conversion.push(currentChar);
                        break;
                    case 43: // +
                        while (!conversion.isEmpty() && ((char)conversion.peek() == 94 || (char)conversion.peek() == 47 || (char)conversion.peek() == 42 || (char)conversion.peek() == 45 || (char)conversion.peek() == 43)) {
                            output += conversion.pop();
                        }
                        
                        conversion.push(currentChar);
                        break;
                    case 45: // -
                        while (!conversion.isEmpty() && ((char)conversion.peek() == 94 || (char)conversion.peek() == 47 || (char)conversion.peek() == 42 || (char)conversion.peek() == 43 || (char)conversion.peek() == 45)) {
                            output += conversion.pop();
                        }

                        conversion.push(currentChar);
                        break;
                    case 47: // /
                        while (!conversion.isEmpty() && ((char)conversion.peek() == 94 || (char)conversion.peek() == 42 || (char)conversion.peek() == 47)) {
                            output += conversion.pop();
                        }
                        
                        conversion.push(currentChar);
                        break;
                    case 94: // ^
                        while (!conversion.isEmpty() && (char)conversion.peek() == 94) {
                            output += conversion.pop();
                        }

                        conversion.push(currentChar);
                }
            }
        }

        while (!conversion.isEmpty()) {
            output += conversion.pop();
        }

        return output;
    }

    /**
     * Avalia a expressão utilizando seus valores numéricos.
     * 
     * @param postfixExpression  String contendo uma expressão posfixa
     * @param expressionValues   HashMap<Character, Double> contendo pares de
     *                           uma variável de uma letra e seu valor numérico
     *                           como Double, respectivamente
     * @return                   resultado numérico da expressão como Double
     */
    public static double evaluateExpression(String postfixExpression, HashMap<Character, Double> expressionValues) {
        Pilha evaluation = new Pilha();
        char[] postfixExpressionAsCharArray = postfixExpression.toCharArray();
        
        Object[] postfixExpressionAsObjectArray = new Object[postfixExpressionAsCharArray.length];
        
        for (int i = 0; i < postfixExpressionAsCharArray.length; i++) {
            char currentChar = postfixExpressionAsCharArray[i];
            
            if (currentChar >= 65 && currentChar <= 90) {
                postfixExpressionAsObjectArray[i] = expressionValues.get(currentChar);
            } else {
                postfixExpressionAsObjectArray[i] = postfixExpressionAsCharArray[i];
            }
        }
        
        for (int i = 0; i < postfixExpressionAsObjectArray.length; i++) {
            Object currentChar = postfixExpressionAsObjectArray[i];
            
            if (currentChar instanceof Double) {
                evaluation.push(currentChar);
            } else {
                double x, y, result;
                switch ((char)currentChar) {
                    case 42: // *
                        result = ((double)evaluation.pop() * (double)evaluation.pop());
                        evaluation.push(result);          // product
                        break;
                    case 43: // +
                        result = ((double)evaluation.pop() + (double)evaluation.pop());
                        evaluation.push(result);          // sum
                        break;
                    case 45: // -
                        y = (double)evaluation.pop();     // subtrahend
                        x = (double)evaluation.pop();     // minuend
                        result = (x - y);                 // difference
                        evaluation.push(result);
                        break;
                    case 47: // /
                        y = (double)evaluation.pop();     // divisor
                        x = (double)evaluation.pop();     // dividend
                        result = x/y;                     // quotient
                        evaluation.push(result);
                        break;
                    case 94: // ^
                        y = (double)evaluation.pop();     // exponent
                        x = (double)evaluation.pop();     // base
                        result = (double) Math.pow(x, y); // power
                        evaluation.push(result);
                        break;
                }
            }
        }

        return (double)evaluation.pop();
    }
}