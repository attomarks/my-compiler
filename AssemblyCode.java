import java.util.*;
import java.io.*;

public class AssemblyCode {

    static File  AssemblyFile;
    static PrintWriter pw;
    static List<String> funcName = new ArrayList<>();
    static String funcNameNow = null;
    static int strLabel = 0;
    static String lastReg = "noRegister";
    static String savingReg = null;
    static List<String> varList = new ArrayList<>();
    static List<Integer> varAddress = new ArrayList<>();
    static List<String> varType = new ArrayList<>();
    static int stack = 0;
    static int paraNum = 1;
    static int argNum = 1;

    static void fileCreate(){
	try{
	    AssemblyFile = new File("Assembly.s");
	    pw = new PrintWriter(new BufferedWriter(new FileWriter(AssemblyFile)));
	}catch(IOException e){
	    System.out.println(e.getMessage());
	}
    }
	
    static void headTemp1(){
	pw.println("    .file   \"test.c\"");
    }

    static void setString(){
	pw.println("    .section    .rodata");
	for(int i=0;i<IRGenerator.getNumOfString();i++){
	    pw.println(".LC"+strLabel+":");
	    strLabel++;
	    pw.println("    .string "+IRGenerator.getString(i));
	}
    }

    static void headTemp2(){
	pw.println("    .text");
	//pw.println("    .globl  main");

	//pw.println("    .type   main, @function");
	//pw.println("main:");
	//pw.println("    pushq   %rbp");
	//pw.println("    movq    %rsp, %rbp");
    }
    
    static void footTemp(){
	//pw.println("    movl    $0, %eax");
	//pw.println("    movq    %rbp, %rsp");
	//pw.println("    popq    %rbp");
	//pw.println("    leave");
	//pw.println("    ret");
	//pw.println("    .size   main,.-main");
	//pw.println("    .ident  \"GCC: (GNU) 8.1.0\"");
	//pw.println("    .section    .note.GNU-stack,\"\",@progbits");
	pw.close();
    }

    /*static void expandStack(int size){
	pw.println("    subq    $"+size+", %rsp");
	}*/

    static void function(int flame, String name){
	pw.println("    .globl  "+name);
	pw.println("    .type   "+name+", @function");
	pw.println(name+":");
	pw.println("    pushq   %rbp");
	pw.println("    movq    %rsp, %rbp");
	pw.println("    subq    $"+flame+", %rsp");

	funcName.add(name);
	funcNameNow = name;
    }

    static void parameter(String type, String name){
	switch(type){
	case "int":
	    stack -= 4;
	    break;
	}

	varList.add(name);
	varAddress.add(stack);
	varType.add(type);

	String reg = null;
	switch(paraNum){
	case 1:
	    reg = "edi";
	    break;
	case 2:
	    reg = "esi";
	    break;
	case 3:
	    reg = "edx";
	    break;
	case 4:
	    reg = "ecx";
	    break;
	case 5:
	    reg = "r8d";
	    break;
	}
	paraNum++;
	
	pw.println("	movl	%"+reg+", "+stack+"(%rbp)");
    }
	    
    static void argument(String name){
	String reg = null;
	switch(argNum){
	case 1:
	    reg = "edi";
	    break;
	case 2:
	    reg = "esi";
	    break;
	case 3:
	    reg = "edx";
	    break;
	case 4:
	    reg = "ecx";
	    break;
	case 5:
	    reg = "r8d";
	    break;
	}
	argNum++;

	int address = searchAddress(name);
	pw.println("	movl	"+address+"(%rbp), %"+reg);
    }
    
    static void label(String label){
	pw.println(label+":");
    }

    static void variable(String type, String name){
	switch(type){
	case "int":
	    stack -= 4;
	    break;
	}

	varList.add(name);
	varAddress.add(stack);
	varType.add(type);
    }
    
    static void assign(String lhs, String rhs){
	int lhsAddress = searchAddress(lhs);
	int rhsAddress = searchAddress(rhs);
	if(rhsAddress == -1){
	    if(lastReg.equals(rhs))
		pw.println("    movl    %eax, "+lhsAddress+"(%rbp)");
	    else if(funcName.indexOf(rhs) != -1){
		pw.println("    call    "+rhs);
		pw.println("    movl    %eax, "+lhsAddress+"(%rbp)");
		argNum = 1;
	    }else
		pw.println("    movl    $"+rhs+", "+lhsAddress+"(%rbp)");
	}else{
	    pw.println("    movl    "+rhsAddress+"(%rbp), %eax");
	    pw.println("    movl    %eax, "+lhsAddress+"(%rbp)");
	}
    }

    static void add(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    addl    %ecx, %eax");
	//moveaxres(res);
	lastReg = res;
    }

    static void sub(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    subl    %ecx, %eax");
	//moveaxres(res);
	lastReg = res;
    }

    static void mul(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    imull    %ecx, %eax");
	//moveaxres(res);
	lastReg = res;
    }

    static void div(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cltd");
	pw.println("    idivl   %ecx");
	//moveaxres(res);
	lastReg = res;
    }

    static void mod(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cltd");
	pw.println("    idivl   %ecx");	
	pw.println("    movl    %edx, %eax");
	//movedxres(res);
	lastReg = res;
    }

    static void eq(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    sete    %al");
	test();
	lastReg = res;
    }

    static void ne(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setne    %al");
	test();
	lastReg = res;
    }

    static void gt(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setg    %al");
	test();
	lastReg = res;
    }

    static void lt(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setl    %al");
	test();
	lastReg = res;
    }

    static void ge(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setge    %al");
	test();
	lastReg = res;
    }

    static void le(String arg1, String arg2, String res){
	usingLastReg(arg1,arg2);
	movecx(arg2);
	moveax(arg1);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setle    %al");
	test();
	lastReg = res;
    }

    static void moveax(String arg){
	//int address = IRGenerator.getAddress(arg);
	int address = searchAddress(arg);
	if(address == -1){
	    if(lastReg.equals(arg))
		;
	    else if(savingReg.equals(arg))
		pw.println("    movl    %ebx, %eax");
	    else
		pw.println("    movl    $"+arg+", %eax");
	}
	else
	    pw.println("    movl    "+address+"(%rbp), %eax");
    }

    static void movecx(String arg){
	//int address = IRGenerator.getAddress(arg);
	int address = searchAddress(arg);
	if(address == -1){
	    if(lastReg.equals(arg))
		pw.println("    movl    %eax, %ecx");
	    else if(savingReg.equals(arg))
		pw.println("    movl    %ebx, %ecx");
	    else
		pw.println("    movl    $"+arg+", %ecx");
	}
	else
	    pw.println("    movl    "+address+"(%rbp), %ecx");
    }

    static void moveaxres(String res){
	int address = searchAddress(res);
	pw.println("    movl    %eax, "+address+"(%rbp)");
    }

    static void movedxres(String res){
	int address = searchAddress(res);
	pw.println("    movl    %edx, "+address+"(%rbp)");
    }

    static void usingLastReg(String arg1, String arg2){
	if(!(lastReg.equals(arg1)) && !(lastReg.equals(arg2))){
	    pw.println("    movl    %eax, %ebx");
	    savingReg = lastReg;
	}
    }

    static void test(){
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void jnz(String label){
	pw.println("    jnz "+label);
    }

    static void jmp(String label){
	pw.println("    jmp "+label);
    }

    static void jne(String label){
	pw.println("    jne "+label);
    }

    static void printf(String StringLabel){
	pw.println("    leaq    "+StringLabel+"(%rip), %rdi");
	pw.println("    movl    $0, %eax");
	pw.println("    call    printf@PLT");
    }

    static void printf(int varNum, int address){
	pw.println("    movl    "+address+"(%rbp), "+printfRegister(varNum));
    }

    static String printfRegister(int varNum){
	String Register = null;
	switch(varNum){
	case 1: Register = "%esi"; break;
	case 2: Register = "%edx"; break;
	case 3: Register = "%ecx"; break;
	}
	return Register;
    }

    static void ret(String str){
	int index = varList.indexOf(str);
	if(index == -1)
	    pw.println("    movl    $"+str+", %eax");
	else
	    pw.println("    movl    "+varAddress.get(index)+"(%rbp), %eax");
	pw.println("    leave");
	pw.println("    ret");
	pw.println("    .size   "+funcNameNow+",.-"+funcNameNow);

	varList.clear();
	varAddress.clear();
	varType.clear();
	stack = 0;
	paraNum = 1;
	funcNameNow = null;
    }

    static int searchAddress(String name){
	int index = varList.indexOf(name);
	if(index == -1)
	    return -1;
	else
	    return varAddress.get(index);
    }
    
}
