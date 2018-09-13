import java.util.*;
import java.io.*;

public class IRGenerator {
    static List<String> var_list = new ArrayList<>();
    static List<Integer> var_address = new ArrayList<>();
    static String func_name = null;
    static int flame_size = 0;
    static List<String> printStr = new ArrayList<>();
    public static List<String> IR = new ArrayList<>();
    
    public static void IRlabel(String label){
	IR.add("label,null,null,"+label);
    }

    public static void IRdef_fun(String type, String name){
	switch(type){
	case "int": //IR.add("name"); break;
	case "double": /*IR.add("def_" + name + ": local 8");*/ break;
	default : System.out.println("Error in IRdef_fun.");
	}
	func_name = name;
    }
    
    public static void IRdef_vars(String type, String name, String value){
	switch(type){
	case "int": var_list.add(name);
	    flame_size += 4;
	    var_address.add(-flame_size);
	    break;
	case "double": var_list.add(name); flame_size += 8; break;
	default : System.out.println("Error");
	}
	if(!(value.equals("null"))) IR.add("=,"+value+",null,"+name);
    }

    public static void IRreturn_stmt(String value){
	//IR.add("=,"+value+",null,"+func_name);
	func_name = null;
    }

    public static void IRprintf_stmt(String str){
	int strNum;
	if(printStr.indexOf(str) == -1){
	    printStr.add(str);
	    strNum = printStr.size()-1;
	}else{
	    strNum = printStr.indexOf(str);
	}
	IR.add("printf,null,0,.LC"+strNum);
    }

    public static void IRprintf_stmt(String var, int varNum){
	IR.add("printf,null,"+varNum+","+var);
    }
	
    public static void IRexpr(String op, String lhs, String rhs, String eq){
	IR.add(op+","+lhs+","+rhs+","+eq);
    }

    public static void IRexpr(String temp, String eq){
	//if(Simple_Assign == true){
	    IR.add("=,"+eq+",null,"+temp);
	    /*}else{
	    IR.add(temp+","+eq);
	    }*/
    }

    public static void IRjnz(String label){
	IR.add("jnz,null,null,"+label);
    }

    public static void IRjmp(String label){
	IR.add("jmp,null,null,"+label);
    }

    public static void IRjne(String label){
	IR.add("jne,null,null,"+label);
    }

    static void IRGenerate(){
	for(int i=0;i<IR.size();i++){
	    System.out.println(IR.get(i));
	}
    }

    static String[] IRRead(int line){
	String[] IR_element = IR.get(line).split(",",0);
	return IR_element;
    }

    static int IRNumOfLines(){
	return IR.size();
    }

    static void addWkToList(){
	List<String> wk = ParserVisitorImplements.importWk();
	var_list.addAll(wk);
	for(int i=0;i<wk.size();i++){
	    flame_size += 4;
	    var_address.add(-flame_size);
	}
    }	
    
    static int getAddress(String str){
	int index = var_list.indexOf(str);
	System.out.println(str+":"+index);
	if(index == -1) return -1;
	else return var_address.get(index);
    }

    static int getStackSize(){
	return flame_size;
    }

    static int getNumOfString(){
	return printStr.size();
    }

    static String getString(int stringNum){
	return printStr.get(stringNum);
    }
}
