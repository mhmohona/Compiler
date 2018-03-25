/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package regular_expression_matcher;

public class Regular_Expressions_Array {
    
    String[] RE_Array;
    
    public Regular_Expressions_Array(char[] getArray){
        //ConvertToStringArray
        RE_Array = new String[getArray.length+1];
        int i = 0;
        for(; i<getArray.length; i++){
            RE_Array[i] = getArray[i]+"";
        }
        RE_Array[i] = "$";
    }
    
}
