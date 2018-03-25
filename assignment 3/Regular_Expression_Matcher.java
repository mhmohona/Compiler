/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regular_expression_matcher;



import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Regular_Expression_Matcher {

    /**
     * @param args the command line arguments
     */
    
    private static String input_path = "C:\\Users\\14301028\\Desktop\\Assignment_03\\Regular_Expression_Matcher\\src\\regular_expression_matcher\\input_02.txt";
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        String input_file = input_path;//+"//input_01.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(input_file));
            int number_of_regular_expressions = Integer.parseInt(br.readLine());
            
            Regular_Expressions_Array[] RegularExpressions = new Regular_Expressions_Array[number_of_regular_expressions];
                                                                                 // RE er object er array
            //saving variables with value in arrays
            for(int i = 0; i<number_of_regular_expressions; i++){
                String getAnExpression = br.readLine(); 
                char[] RE_CharArray = getAnExpression.toCharArray(); // saving the RE in the Array
                
                Regular_Expressions_Array RE_Array_Object = new Regular_Expressions_Array(RE_CharArray);
                RegularExpressions[i] = (Regular_Expressions_Array)RE_Array_Object;
            }
            int number_of_equations = Integer.parseInt(br.readLine());
            for(int i = 0; i<number_of_equations; i++){
                String getAnEquation = br.readLine(); 
                String[] getEquation_Array = ConvertToStringArray(getAnEquation);
                int j = 0;
                for(; j<number_of_regular_expressions; j++){
                    String[] getRE_Array = RegularExpressions[j].RE_Array;
                    if(isExpression_Matched(getRE_Array,getEquation_Array)){
                        break;
                    }
                }
                if(j == number_of_regular_expressions){
                    System.out.println("NO, 0");
                }
                else{
                    System.out.println("YES, "+(j+1));
                }
                
            }
        } 
        catch (FileNotFoundException ex) {
            Logger.getLogger(Regular_Expression_Matcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static String[] ConvertToStringArray(String getString){
        char[] Eq_CharArray = getString.toCharArray();
        String[] Eq_Array = new String[Eq_CharArray.length+1];
        int i = 0;
        for(; i<Eq_CharArray.length; i++){
            Eq_Array[i] = Eq_CharArray[i]+"";
        }
        Eq_Array[i] = "$";
        return Eq_Array;
    }
    
    public static boolean isExpression_Matched(String[] getRE_Array,String[] getEquation_Array){
        String Bracket = "Off";                                     // first bracket
        int pointer = 0;
        String Concated = "";
        int appearenceCounter = 0;
        String ConcatedEquation_Part = "";
        String ThirdBracket = "Off";
        String ExceptNot = "Off";                                      // for the ^
        String[] rangeArray = new String[0];                           // for [range]
        
        for(int i = 0; i<getRE_Array.length; i++){
            String getWord = getRE_Array[i];                        //taking one letter of RE
            String getNextWord = "";
            if(!(getWord.equals("$"))){
                getNextWord = getRE_Array[i+1];
            }
            
            if(getWord.equals("[")){
                ThirdBracket = "On";
                if(!(getNextWord.equals("^"))){
                    ExceptNot = "alwaysOff";
                }
            }
            else if(ThirdBracket.equals("On")){
                if(getWord.equals("]")){
                    ThirdBracket = "Off";
                    if(getNextWord.equals("{")){
                        i = i+2;
                        getWord = getRE_Array[i];
                        String numberOfOccurrence = "";                // {no of occurance}
                        while(!(getWord.equals("}"))){ 
                           numberOfOccurrence = numberOfOccurrence+getWord;
                           i++;
                           getWord = getRE_Array[i];
                        }
                        int getStringLength = Integer.parseInt(numberOfOccurrence);
                        for(int c = pointer; c<(getStringLength+pointer); c++){
                            if(!(terminalExist(rangeArray,getEquation_Array[c]))){
                                return false;
                            }
                        }
                        pointer = getStringLength+pointer;
                    }
                    else if(getNextWord.equals("*")){
                        while(terminalExist(rangeArray,getEquation_Array[pointer])){
                            pointer++;
                        }
                    }
                    else if(getNextWord.equals("+")){
                        if(appearenceCounter == 0){
                            if(terminalExist(rangeArray,getEquation_Array[pointer])){
                                pointer++;
                                appearenceCounter++;
                                i--;
                            }
                            else if(terminalExist(rangeArray,getEquation_Array[pointer-1])){
                            }
                            else{ 
                                return false;
                            }
                        }
                        while(terminalExist(rangeArray,getEquation_Array[pointer])){
                            pointer++;
                        }
                        appearenceCounter = 0;
                        
                    }
                    else if(getNextWord.equals("$")){
                        if(terminalExist(rangeArray,getEquation_Array[pointer])){
                            pointer++;
                        }
                    }
                }
                else if(getWord.equals("^")){
                    ExceptNot = "On";
                }
                else if(ExceptNot.equals("alwaysOff")){
                    if(getNextWord.equals("-")){
                        rangeArray = makeTerminalRange("accept", getWord, getRE_Array[i+2]);
                        i = i+2;
                    }
                }
                else if(ExceptNot.equals("On")){
                    if(getNextWord.equals("-")){
                        rangeArray = makeTerminalRange("reject", getWord, getRE_Array[i+2]);
                        i = i+2;
                    }
                    else{
                        rangeArray = makeTerminalRange("accept", "a", "z");
                        while(isTerminal(getWord)){ 
                           rangeArray = makeRejecterArray(rangeArray, getWord);
                           i++;
                           getWord = getRE_Array[i];
                        }
                        i--;
                    }
                }
            }
            
            else if(isTerminal(getWord)){               //*********************************************
                
                if(Bracket.equals("Off")){
                    if(isTerminal(getNextWord)){
                        if(!(getEquation_Array[pointer].equals(getWord))){
                            return false;
                        }
                        pointer++;
                    }
                    else if(getNextWord.equals("*")){
                        
                        if(getEquation_Array[pointer].equals(getWord)){
                            pointer++;
                            i--;
                        }
                    }
                    else if(getNextWord.equals("?")){
                        if(getEquation_Array[pointer].equals(getWord)){
                            pointer++;
                        }
                    }
                    else if(getNextWord.equals("(")){
                        if(getEquation_Array[pointer].equals(getWord)){
                            pointer++;
                        }
                        else{
                            return false;
                        }
                    }
                    else if(getNextWord.equals("$")){
                        if(!(getEquation_Array[pointer].equals(getWord))){
                            return false;
                        }
                        pointer++;
                        if(!(getEquation_Array[pointer].equals("$"))){
                            return false;
                        }
                        return true;
                    }
                    else if(getNextWord.equals("[")){
                        if(!(getEquation_Array[pointer].equals(getWord))){
                            return false;
                        }
                        pointer++;
                        
                    }
                    else if(getNextWord.equals("+")){  
                        
                        if(appearenceCounter == 0){
                            if(getEquation_Array[pointer].equals(getWord)){
                                
                                pointer++;
                                appearenceCounter++;
                                i--;
                            }
                            else if(getEquation_Array[pointer-1].equals(getWord)){
                                appearenceCounter++;
                            }
                            else{ 
                                return false;
                            }
                        }
                        else{
                            if(getEquation_Array[pointer].equals(getWord)){
                                pointer++;
                                i--;
                            }
                            else{
                                appearenceCounter = 0;
                            }
                        }
                    }
                }
                
                else if(Bracket.equals("on")){                   // for ()
                    Concated = Concated+getWord;
                }
            }
            
            else if(getWord.equals("(")){
                Bracket = "on";
            }
            else if(getWord.equals(")")){
                Bracket = "Off";
                int getStringLength = Concated.length();
                if(getNextWord.equals("*")){
                    for(int c = pointer; c<(getStringLength+pointer); c++){
                        if(c == getEquation_Array.length){
                            return false;
                        }
                        ConcatedEquation_Part = ConcatedEquation_Part+getEquation_Array[c];
                    }
                    if(ConcatedEquation_Part.equals(Concated)){
                        pointer = pointer+getStringLength;
                        ConcatedEquation_Part = "";
                        i--;
                    }
                    else{
                        Concated = "";
                        ConcatedEquation_Part = "";
                    }
                }
                else if(getNextWord.equals("?")){
                    for(int c = pointer; c<(getStringLength+pointer); c++){
                        if(c == getEquation_Array.length){
                            return false;
                        }
                        ConcatedEquation_Part = ConcatedEquation_Part+getEquation_Array[c];
                    }
                    if(ConcatedEquation_Part.equals(Concated)){
                        pointer = pointer+getStringLength;
                        ConcatedEquation_Part = "";
                        Concated = "";
                    }
                    else{
                        Concated = "";
                        ConcatedEquation_Part = "";
                    }
                }
                else if(getNextWord.equals("+")){
                    for(int c = pointer; c<(getStringLength+pointer); c++){
                        if(c == getEquation_Array.length){
                            return false;
                        }
                        ConcatedEquation_Part = ConcatedEquation_Part+getEquation_Array[c];
                    }
                    if(appearenceCounter == 0){
                        if(ConcatedEquation_Part.equals(Concated)){
                            appearenceCounter++;
                            pointer = pointer+getStringLength;
                            ConcatedEquation_Part = "";
                            i--;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        if(ConcatedEquation_Part.equals(Concated)){
                            appearenceCounter++;
                            pointer = pointer+getStringLength;
                            ConcatedEquation_Part = "";
                            i--;
                        }
                        else{
                            appearenceCounter = 0;
                            Concated = "";
                            ConcatedEquation_Part = "";
                        }
                    }
                }
            }
            else if((getWord.equals("$"))&&(getEquation_Array[pointer].equals("$"))){
                return true;
            }
        }
        return false;
    }
    
    public static boolean isTerminal(String getWord){
        for(char c = 'a'; c <= 'z'; c++) {
            if(getWord.equals(c+"")){
                return true;
            }
        }
        return false;
    }
    
    public static String[] makeTerminalRange(String acceptOrReject, String start, String end){
        char startingChar = start.charAt(0);
        char endingChar = end.charAt(0);
        String[] rangeArray = new String[0];
        
        if(acceptOrReject.equals("accept")){
            for(char c = startingChar; c <= endingChar; c++) {
                rangeArray = insertIntoRangeArray(rangeArray,c);
            }
        }
        else if(acceptOrReject.equals("reject")){
            String rejectRegion = "Off";
            for(char c = 'a'; c <= 'z'; c++) {
                if(c == startingChar){
                    rejectRegion = "On";
                }
                if(rejectRegion.equals("Off")){
                    rangeArray = insertIntoRangeArray(rangeArray,c);
                }
                if(c == endingChar){
                    rejectRegion = "On";
                }
                
            }
        }
        
        return rangeArray;
    }
    
    public static String[] makeRejecterArray(String[] getArray, String getToReject){
        String[] tempArray = new String[getArray.length-1];
        
        for(int c = 0,i = 0; c < getArray.length; c++) {
            if(!(getArray[c].equals(getToReject))){
                tempArray[i] = getArray[c];
                i++;
            }
        }
        
        
        return tempArray;
    }
    
    public static String[] insertIntoRangeArray(String[] getArray, char getChar){
        String[] tempArray = new String[getArray.length];
        
        System.arraycopy(getArray, 0, tempArray, 0, getArray.length); // saving in temp array
        
        getArray = new String[getArray.length+1];         // increasing array length
        
        System.arraycopy(tempArray, 0, getArray, 0, tempArray.length);   //saving in original array
        
        getArray[getArray.length-1] = getChar+"";                    // inserting the new char
        return getArray;
    }
    
    public static boolean terminalExist(String[] getArray, String getWord){
        for(int c = 0; c < getArray.length; c++) {
            if(getArray[c].equals(getWord)){
                return true;
            }
        }
        return false;
    }
    
}