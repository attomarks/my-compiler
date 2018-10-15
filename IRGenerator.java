import java.util.*;
import java.io.*;

public class IRGenerator {
    static List<String> var_list = new ArrayList<>();
    static List<Integer> var_address = new ArrayList<>();
    static String func_type = null;
    static int flame_size = 0;
    static List<Integer> func_flame = new ArrayList<>();
    static List<String> printStr = new ArrayList<>();
    public static List<String> IR = new ArrayList<>();
    
    public static void IRlabel(String label){
	IR.add("label,,,"+label);
    }

    public static void IRdef_fun(String type, String name){
	IR.add("func,,"+type+","+name);
	func_type = type;
    }

    static void IRdef_var(String type, String name){
	IR.add("var,,"+type+","+name);
    }
    
    public static void IRvar_ini(String type, String name, String value){
	switch(type){
	case "int": var_list.add(name);
	    flame_size += 4;
	    var_address.add(-flame_size);
	    break;
	case "double": var_list.add(name); flame_size += 8; break;
	default : System.out.println("Error in IRdef_vars.");
	}
	if(!(value.equals("null"))) IR.add("=,"+value+",,"+name);
    }

    static void IRparam(String type, String name){
	switch(type){
	case "int": var_list.add(name);
	    flame_size += 4;
	    var_address.add(-flame_size);
	    break;
	case "double": var_list.add(name); flame_size += 8; break;
	default : System.out.println("Error in IRparam.");
	}
	IR.add("param,,"+type+","+name);
    }

    public static void IRreturn_stmt(String value){
	IR.add("rtn,,,"+value);
	func_type = null;
	//System.out.println(flame_size);
	func_flame.add(flame_size);
	flame_size = 0;
    }

    public static void IRprintf_stmt(String str){
	int strNum;
	if(printStr.indexOf(str) == -1){
	    printStr.add(str);
	    strNum = printStr.size()-1;
	}else{
	    strNum = printStr.indexOf(str);
	}
	IR.add("printf,,0,.LC"+strNum);
    }

    public static void IRprintf_stmt(String var, int varNum){
	IR.add("printf,,"+varNum+","+var);
    }
	
    public static void IRexpr(String op, String lhs, String rhs, String eq){
	IR.add(op+","+lhs+","+rhs+","+eq);
    }

    public static void IRexpr(String temp, String eq){
	//if(Simple_Assign == true){
	    IR.add("=,"+eq+",,"+temp);
	    /*}else{
	    IR.add(temp+","+eq);
	    }*/
    }

    static void IRarg(String name){
	IR.add("arg,,,"+name);
    }

    public static void IRjnz(String label){
	IR.add("jnz,,,"+label);
    }

    public static void IRjmp(String label){
	IR.add("jmp,,,"+label);
    }

    public static void IRjne(String label){
	IR.add("jne,,,"+label);
    }

    static void IRGenerate(){
	//System.out.println(var_list);
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
	//System.out.println(str+":"+index);
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

    static int getFlame(int num){
	return func_flame.get(num);
    }
}
