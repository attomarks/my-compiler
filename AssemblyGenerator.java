import java.util.*;
import java.io.*;

public class AssemblyGenerator {
    static void Generate(){
	System.out.println("Generating assembly code...");
	AssemblyCode.fileCreate();
	AssemblyCode.headTemp1();
	AssemblyCode.setString();
	AssemblyCode.headTemp2();
	IRGenerator.addWkToList();
	AssemblyCode.expandStack(IRGenerator.getStackSize());

	int lhs,llhs,rlhs,rhs;
	for(int i=0;i<IRGenerator.IRNumOfLines();i++){
	    String[] IR = IRGenerator.IRRead(i);
	    switch(IR[0]){
	    case "=": 
		if(IRGenerator.getAddress(IR[1]) == -1){
		    lhs = IRGenerator.getAddress(IR[3]);
		    rhs = Integer.parseInt(IR[1]);
		    AssemblyCode.eqRhsIsNum(lhs,rhs);
		}else{
		    lhs = IRGenerator.getAddress(IR[3]);
		    rhs = IRGenerator.getAddress(IR[1]);
		    AssemblyCode.eqRhsIsLit(lhs,rhs);
		}
		break;
	    case "*": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.mulAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.mulRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.mulLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.mulAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "+": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.addAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.addRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.addLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.addAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "-": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.subAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.subRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.subLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.subAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "/": break;
	    case "==": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.eqAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.eqRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.eqLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.eqAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "!=": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.neAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.neRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.neLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.neAllLit(llhs,rlhs,rhs);
		}
		break;
	    case ">": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.gtAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.gtRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.gtLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.gtAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "<": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.ltAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.ltRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.ltLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.ltAllLit(llhs,rlhs,rhs);
		}
		break;
	    case ">=": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.geAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.geRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.geLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.geAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "<=": rhs = IRGenerator.getAddress(IR[3]);
		if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.leAllNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) != -1 && IRGenerator.getAddress(IR[2]) == -1){
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = Integer.parseInt(IR[2]);
		    AssemblyCode.leRlhsIsNum(llhs,rlhs,rhs);
		}else if(IRGenerator.getAddress(IR[1]) == -1 && IRGenerator.getAddress(IR[2]) != -1){
		    llhs = Integer.parseInt(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.leLlhsIsNum(llhs,rlhs,rhs);
		}else{
		    llhs = IRGenerator.getAddress(IR[1]);
		    rlhs = IRGenerator.getAddress(IR[2]);
		    AssemblyCode.leAllLit(llhs,rlhs,rhs);
		}
		break;
	    case "label": AssemblyCode.label(IR[3]);
		break;
	    case "jnz": AssemblyCode.jnz(IR[3]);
		break;
	    case "jmp": AssemblyCode.jmp(IR[3]);
		break;
	    case "jne": AssemblyCode.jne(IR[3]);
		break;
	    case "printf": int address = IRGenerator.getAddress(IR[3]);
		if(address == -1){
		    AssemblyCode.printf(IR[3]);
		}else{
		    AssemblyCode.printf(Integer.parseInt(IR[2]),address);
		}break;

	    }
	}
	AssemblyCode.footTemp();
    }
}
