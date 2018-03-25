
import java.util.*;


public class CSE420_Assignment2 {

    static Stack<String> stack=new Stack<String> ();
    static Character [] idn;
    static int [] val;
    
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);        
        System.out.println("Give avalue of n");
        int n=sc.nextInt();
        idn = new Character [n];
        val = new int [n];
    
    
        for(int i=0;i<n;i++){

          boolean a = true;
          System.out.println("Give an identifier (Characters only)");
          char x = sc.next().charAt(0);
          
          for(int j=0; j<i; j++)
          {             
              if(idn[j].equals(x))
              {
                  System.out.println("This character was already entered. Please enter a different one");
                  i--;
                  a=false;
                  break;
              }
         }
          
            if(a==true)
             {
               idn[i] = x;
               System.out.println("give the value of "+ x);
               int y = sc.nextInt();
               val[i] = y;
            }                                       

        }
        
        
        
        //test 1
        /*
        for(int i=0;i<n;i++)
        {
            System.out.println(idn[i]+"= "+val[i]);
            
        }
        */
        
        
        
        System.out.println("Give the number of expression");
        int num = sc.nextInt();
        
        
        for(int m1=0;m1<num;m1++)                                                           
        {
            System.out.println("Give the expression");
            String s=sc.next();
      
     
            int l=s.length();
            //the string is being split and stored in an array
            //it is redundant here
            //String ar[]=s.split(""); 
            
            //expression manipulation as per the algorithm.
            postfix(s);
            
                
        }
    
        //postfix("g + p * t - w * p");
    }
    
        
        
    
    
    
    
    
    static boolean parenthesis(String c)
    {
        if(c.equals("(") || c.equals(")"))
        {
            return true;
        }
    
        else
        {
            return false;        
        }
    }
    
    
    static boolean operator(char ch)
    {
 if(ch=='/'||ch=='*'||ch=='+'||ch=='-')
  return true;
 else
  return false;
 }

    
    
    static boolean isOperend(char ch)
    {
        if(ch>='a'&&ch<='z'||ch>'0'&&ch=='9')
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    static int pre(char ch)
    {
        switch(ch)
        {
            case '-':return 1;
            case '+':return 1;
            case '*':return 2;
            case '/':return 2;
        }       
        return 0;        
    }
    
    static void postfix(String str)
    {
        char output[]=new char[str.length()];
        Character ch;
        int p=0, i;
        for(i=0;i<str.length();i++)
        {
            ch=str.charAt(i);
            if(ch=='(')
            {
                stack.push(ch+"");                
            }
            else if(isOperend(ch))
            {
                output[p++]=ch;
            }
            else if(operator(ch))
            {
                if(stack.isEmpty()||stack.peek().equals("(")||(pre(ch)>pre(stack.peek().charAt(0))))
                {
                    stack.push(ch+"");
                }
                else if(pre(ch)<=pre(stack.peek().charAt(0)))
                {
                    output[p++]=stack.pop().charAt(0);
                    while(!stack.isEmpty() && pre(ch)<=pre(stack.peek().charAt(0)) && stack.peek().charAt(0)!='('){
                        output[p++]=stack.pop().charAt(0);
                    }
                    stack.push(ch+"");
                }
            }
            else if(ch==')')
            {                                               
                while((ch=stack.pop().charAt(0))!='(')
                {                        
                        
                    output[p++]=ch;
                    
                }
                    
            }
        }
            
            
            
        while(!(stack.empty()))
        {
            //System.out.println("Debugging " + stack.peek().charAt(0));
            
            output[p++]=stack.pop().charAt(0);
            
                    
        }
        String x="";
        for(int j= 0; j<output.length;j++){
            x=x+output[j];
        }
        String trm= x.trim();    
        //string array where the calculation wil take place
        String [] arr = new String [trm.length()];
        for(int j= 0; j<trm.length();j++)
        {
            //values are fed into the arr ARRAY
            if(operator(output[j])){
                arr[j]=output[j]+"";
            }
            else{
                for(int f= 0; f<val.length; f++){
                    if(output[j]== idn[f]){
                        //System.out.println("j="+j);
                        arr[j] = val[f]+"";
                        break;
                    }
                }
            }
            
            
            System.out.print(output[j]); 
        }
        System.out.println("");
        
        //debugginh the arr ARRAY
        /*
        System.out.println(arr.length);
        System.out.print("[");
        for(int j= 0; j<arr.length;j++)
        {
            System.out.print(arr[j]+"|");
        }
        System.out.println("]");
        */
        
        
        //here the values are being calculated.
        int beg, end;
        int size = arr.length;
        for(beg=0;beg<size;beg++){
            
            end = beg+2;
            if(end<size && operator(arr[end].charAt(0))){
                
                if(arr[end].charAt(0)=='+'){
                    int frstVal = Integer.parseInt(arr[beg]);
                    int scndVal = Integer.parseInt(arr[beg+1]);
                    int res = frstVal+scndVal;
                    arr[beg]=res+"";
                    //leftshift array;
                    for(int lsh= beg+1; lsh<size-2; lsh++){
                        arr[lsh]=arr[lsh+2];
                    }
                    size-=2;
                    beg=-1;
                }
                
                else if(arr[end].charAt(0)=='-'){
                    int frstVal = Integer.parseInt(arr[beg]);
                    int scndVal = Integer.parseInt(arr[beg+1]);
                    int res = frstVal-scndVal;
                    arr[beg]=res+"";
                    //leftshift array;
                    for(int lsh= beg+1; lsh<size-2; lsh++){
                        arr[lsh]=arr[lsh+2];
                    }
                    size-=2;
                    beg=-1;
                }
                
                else if(arr[end].charAt(0)=='*'){
                    int frstVal = Integer.parseInt(arr[beg]);
                    int scndVal = Integer.parseInt(arr[beg+1]);
                    int res = frstVal*scndVal;
                    arr[beg]=res+"";
                    //leftshift array;
                    for(int lsh= beg+1; lsh<size-2; lsh++){
                        arr[lsh]=arr[lsh+2];
                    }
                    size-=2; 
                    beg=-1;
                }
                
                else if(arr[end].charAt(0)=='/'){
                    int frstVal = Integer.parseInt(arr[beg]);
                    int scndVal = Integer.parseInt(arr[beg+1]);
                    int res = frstVal/scndVal;
                    arr[beg]=res+"";
                    //leftshift array;
                    for(int lsh= beg+1; lsh<size-2; lsh++){
                        arr[lsh]=arr[lsh+2];
                    }
                    size-=2;                    
                    beg=-1;
                }
            
            
            }
            
         
    
        
        
        }
        System.out.println("Output is: "+arr[0]);
    }
}
