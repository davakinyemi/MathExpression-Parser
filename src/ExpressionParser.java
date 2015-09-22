/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.*;
/**
 *
 * @author dav
 */

public class ExpressionParser {

    private static ArrayList<Double> numberList = new ArrayList<>();
    private static ArrayList<String> operatorList = new ArrayList<>();
    private static LinkedHashMap<String, Integer> operators = new LinkedHashMap<>();
    
    // operator precedence values
    final private static int power = 4, multiply = 3, divide = 2, add_subtract = 1;   
    
    // add operator names and precedence values to map
    public static void fillMap(){
        operators.put("multiply", multiply);
        operators.put("divide", divide);
        operators.put("add", add_subtract);
        operators.put("minus", add_subtract);
        operators.put("power", power);
    }
    
    // check if brackets are matched
    private static boolean bracketMatched(String expression){
        int count1 = 0, count2 = 0;        
        
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) == '(')
                count1++;
            else if(expression.charAt(i) == ')')
                count2++;
        }
        return count1 == count2;
    }
    
    // remove spaces from expression
    private static String removeSpaces(String expression){
        String replace = expression.replaceAll(" ", "");
        return replace;
    }
    
    // check if character is an operator
    private static boolean isOperator(char c){
        return c == '%' || c == '*' || c == '+' || c == '-' || c == '/' || c == '^';
    }
    
    private static double solveSum(String operator, double a, double b){
        switch (operator){
            case "power":
                return Math.pow(a, b);
            case "multiply":
                return a * b;
            case "divide":
                if (b == 0)
                    throw new
                    UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case "add":
                return a + b;
            case "minus":
                return a - b;
        }
        return 0;
    }
    
    // solve sum by operator level of precedence
    private static double solvePrecedence(){
        int precedent = 0;
        String operator = "";
        
        int index = 0;
        double value = 0;
                
        while(!operatorList.isEmpty()){
            for(int i = 0; i < operatorList.size(); i++){
                if(operators.get(operatorList.get(i)) > precedent){ // if value of operator is greater than precedent
                    precedent = operators.get(operatorList.get(i)); // set precedent to the value of operator
                    operator = operatorList.get(i); // set precedentString to operator
                    index = i; // set index to value of i
                }
            }
            
            value = solveSum(operator, numberList.get(index), numberList.get(index + 1)); // solve sum with operator
            
            operatorList.remove(index); // remove used operator from list
            numberList.set(index, value); // replace sum with value
            numberList.remove(index + 1); // remove redundant sum
            precedent = 0; // reset precedent
        }
        
        return value;
    }
    
    private static double solveBracket(String expression){
        char[] tokens = expression.toCharArray(); // convert expression to character array
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tokens.length; i++){
            if(isOperator(tokens[i])){                             // is character an operator?
                numberList.add(Double.parseDouble(sb.toString())); // convert string to number and add to arraylist
                
                switch(tokens[i]){
                    case '*':
                        operatorList.add("multiply");
                        break;
                    case '/':
                        operatorList.add("divide");
                        break;
                    case '+':
                        operatorList.add("add");
                        break;
                    case '-':
                        operatorList.add("minus");
                        break;
                    case '^':
                        operatorList.add("power");
                }
                                
                sb.setLength(0);                                   // reset stringbuilder
                continue;
            }                
                
            if(tokens[i] >= '0' && tokens[i] <= '9'){ // is token a digit between 0 and 9?          
                sb.append(tokens[i]);
                
                if(i + 1 < tokens.length && tokens[i + 1] == '.'){ // does digit contain decimal point?
                    sb.append(tokens[++i]);
                    i++;
                    while(i < tokens.length && tokens[i] >= '0' && tokens[i] <= '9'){
                        sb.append(tokens[i++]);                        
                    }
                    i--;
                }
            }
        }
        numberList.add(Double.parseDouble(sb.toString())); // add last number to list
        
        return solvePrecedence();
    }
    
    private static double solveExp2(String expression){
        String subExp;
        double answer = 0;
        StringBuilder sb = new StringBuilder();
        StringBuilder exp = new StringBuilder(expression);
        
        while(exp.toString().contains("(")){
            for(int i = 0; i < exp.length(); i++){
                if(exp.charAt(i) == ')'){
                    exp.deleteCharAt(i); // remove first closing bracket
                    --i;
                    while(exp.charAt(i) != '('){ // loop backwards
                        sb.append(exp.charAt(i--)); // append token to stringbuilder
                    }
                    exp.deleteCharAt(i); // remove corresponding opening bracket
                    subExp = sb.reverse().toString();  // reverse sb to get correct sum
                    answer = solveBracket(subExp); // solve sum
                    exp.replace(i, i + subExp.length(), Double.toString(answer)); // replace sum with answer
                    numberList.clear(); // clear remaining numbers in numberList
                    sb.setLength(0); // reset sb
                    i = 0;
                }
            }            
        }        
        
        for(int i = 0; i < exp.toString().length(); i++){ 
            if(isOperator(exp.toString().charAt(i))){ // if sum still contains any operators
                answer = solveBracket(exp.toString()); // solve bracket
                break;
            }
        }
            
        return answer;
    }
    
    public static double solveExp(String expression){
        expression = removeSpaces(expression); // remove spaces from expression
        
        if((expression.contains("(") || expression.contains(")")) && !bracketMatched(expression)){ // check if expression contains brackets and check if brackets are matched
            System.out.println("Mismatched expression: brackets do not match");
            return -1;
        }
                
        return solveExp2(expression);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        fillMap(); // add keys (operator name and values (precedence) to map
        System.out.println(solveExp("22-2^2+(10-3)+1+50"));
    }
    
}
