import java.util.*;
import java.io.*;

public class AssemblyCode {

    static File  AssemblyFile;
    static PrintWriter pw;
    static int strLabel = 0;

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
	pw.println("    .globl  main");

	pw.println("    .type   main, @function");
	pw.println("main:");
	pw.println("    pushq   %rbp");
	pw.println("    movq    %rsp, %rbp");
    }
    
    static void footTemp(){
	pw.println("    movl    $0, %eax");
	pw.println("    movq    %rbp, %rsp");
	pw.println("    popq    %rbp");
	//pw.println("    leave");
	pw.println("    ret");
	pw.println("    .size   main,.-main");
	//pw.println("    .ident  \"GCC: (GNU) 8.1.0\"");
	//pw.println("    .section    .note.GNU-stack,\"\",@progbits");
	pw.close();
    }

    static void expandStack(int size){
	pw.println("    subq    $"+size+", %rsp");
    }

    static void label(String label){
	pw.println(label+":");
    }
    
    static void assign(String lhs, String rhs){
	int lhsAddress = IRGenerator.getAddress(lhs);
	int rhsAddress = IRGenerator.getAddress(rhs);
	if(rhsAddress == -1)
	    pw.println("    movl    $"+rhs+", "+lhsAddress+"(%rbp)");
	else{
	    pw.println("    movl    "+rhsAddress+"(%rbp), %eax");
	    pw.println("    movl    %eax, "+lhsAddress+"(%rbp)");
	}
    }

    static void add(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    addl    %ecx, %eax");
	movres(res);
    }

    static void sub(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    subl    %ecx, %eax");
	movres(res);
    }

    static void mul(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    imull    %ecx, %eax");
	movres(res);
    }

    static void eq(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    sete    %al");
	test();
    }

    static void ne(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setne    %al");
	test();
    }

    static void gt(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setg    %al");
	test();
    }

    static void lt(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setl    %al");
	test();
    }

    static void ge(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setge    %al");
	test();
    }

    static void le(String arg1, String arg2, String res){
	moveax(arg1);
	movecx(arg2);
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setle    %al");
	test();
    }

    static void moveax(String arg){
	int address = IRGenerator.getAddress(arg);
	if(address == -1)
	    pw.println("    movl    $"+arg+", %eax");
	else
	    pw.println("    movl    "+address+"(%rbp), %eax");
    }

    static void movecx(String arg){
	int address = IRGenerator.getAddress(arg);
	if(address == -1)
	    pw.println("    movl    $"+arg+", %ecx");
	else
	    pw.println("    movl    "+address+"(%rbp), %ecx");
    }

    static void movres(String res){
	int address = IRGenerator.getAddress(res);
	pw.println("    movl    %eax, "+address+"(%rbp)");
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

    
}
