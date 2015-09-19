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
    private static ArrayList<Integer> operatorList = new ArrayList<>();
    
    // operator precedence values
    final private static int power = 5, multiply = 4, divide = 3, add = 2, minus = 1;    
    
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
    
    private static double solveSum(int operator, double a, double b){
        switch (operator){
            case 5:
                return Math.pow(a, b);
            case 4:
                return a * b;
            case 3:
                if (b == 0)
                    throw new
                    UnsupportedOperationException("Cannot divide by zero");
                return a / b;
            case 2:
                return a + b;
            case 1:
                return a - b;
        }
        return 0;
    }
    
    private static double solvePrecedence(){
        int precedent = 0;
        int index = 0;
        double value = 0;
        
        while(!operatorList.isEmpty()){
            for(int i = 0; i < operatorList.size(); i++){
                if(operatorList.get(i) > precedent){
                    precedent = operatorList.get(i);
                    index = i;
                }
            }

            value = solveSum(precedent, numberList.get(index), numberList.get(index + 1));

            operatorList.remove(index); // remove used operator
            numberList.set(index, value); // replace value in numberList
            numberList.remove(index + 1); // remove used value in numberList
            precedent = 0; // reset precedent
        }
        return value;
    }
    
    private static double solveBracket(String expression){
        char[] tokens = expression.toCharArray();
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < tokens.length; i++){
            if(isOperator(tokens[i])){                             // is character an operator?
                numberList.add(Double.parseDouble(sb.toString())); // convert string to number and add to arraylist
                
                switch(tokens[i]){
                    case '*':
                        operatorList.add(multiply);
                        break;
                    case '/':
                        operatorList.add(divide);
                        break;
                    case '+':
                        operatorList.add(add);
                        break;
                    case '-':
                        operatorList.add(minus);
                        break;
                    case '^':
                        operatorList.add(power);
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
                    exp.deleteCharAt(i);
                    --i;
                    while(exp.charAt(i) != '('){
                        sb.append(exp.charAt(i--));
                    }
                    exp.deleteCharAt(i);
                    subExp = sb.reverse().toString();  
                    answer = solveBracket(subExp);
                    exp.replace(i, i + subExp.length(), Double.toString(answer));
                    //test
                    System.out.println(exp.toString());
                    //
                    numberList.clear();
                    operatorList.clear();
                    sb.setLength(0);
                    i = 0;
                }
            }            
        }        
        
        for(int i = 0; i < exp.toString().length(); i++){
            if(isOperator(exp.toString().charAt(i))){
                answer = solveBracket(exp.toString());
                break;
            }
        }
            
        return answer;
    }
    
    public static void solveExp(String expression){
        expression = removeSpaces(expression); // remove spaces from expression
        
        if(expression.contains("(")){
            if(!bracketMatched(expression)){
                System.out.println("Mismatched expression: brackets do not match");
                return;
            }   
        }
                
        System.out.println(solveExp2(expression));
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        solveExp("22-2+(50-4)+(10-3)+50");
    }
    
}
