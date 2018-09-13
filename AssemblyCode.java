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
    
    static void eqRhsIsNum(int address, int value){
	pw.println("    movl    $"+value+", "+address+"(%rbp)");
    }
    
    static void eqRhsIsLit(int lAddress, int rAddress){
	pw.println("    movl    "+rAddress+"(%rbp), %eax");
	pw.println("    movl    %eax, "+lAddress+"(%rbp)");
    }
    
    static void mulAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    imull   $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void mulLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    imull   %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void mulRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    imull   $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void mulAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    imull   %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void addAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    addl    $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void addLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    addl    %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void addRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    addl    $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }
    
    static void addAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    addl    %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void subAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    subl    $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void subLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    subl    %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void subRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    subl    $"+rlValue+", %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void subAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    subl    %ecx, %eax");
	pw.println("    movl    %eax, "+rAddress+"(%rbp)");
    }

    static void gtAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setg    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void gtLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setg    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void gtRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setg    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void gtAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setg    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void ltAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setl    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void ltLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setl    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void ltRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setl    %al");
	pw.println("    movzbl  %al, %eax");
        pw.println("    testl    %eax, %eax");
    }

    static void ltAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setl    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void geAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setge   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void geLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setge   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void geRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setge   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void geAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setge   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void leAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setle   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void leLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setle   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void leRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setle   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void leAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setle   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void eqAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    sete    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void eqLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    sete    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void eqRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    sete    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void eqAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    sete    %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void neAllNum(int llValue, int rlValue, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setne   %al");
	pw.println("    movzbl  %al, %eax");
        pw.println("    testl    %eax, %eax");
    }
    
    static void neLlhsIsNum(int llValue, int rlAddress, int rAddress){
	pw.println("    movl    $"+llValue+", %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setne   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }
    
    static void neRlhsIsNum(int llAddress, int rlValue, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    cmpl    $"+rlValue+", %eax");
	pw.println("    setne   %al");
	pw.println("    movzbl  %al, %eax");
	pw.println("    testl    %eax, %eax");
    }

    static void neAllLit(int llAddress, int rlAddress, int rAddress){
	pw.println("    movl    "+llAddress+"(%rbp), %eax");
	pw.println("    movl    "+rlAddress+"(%rbp), %ecx");
	pw.println("    cmpl    %ecx, %eax");
	pw.println("    setne   %al");
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
