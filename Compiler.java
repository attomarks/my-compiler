import java.io.*;

class Compiler{
    public static void main(String args[]){

	String sourceName = null, option = null;
	boolean advance = false, ASTdebug = false, IRdebug = false;
	if(args.length == 2){
	    sourceName = args[1];
	    option = args[0];
	    switch(option){
	    case "-A":
		advance = true;
		break;
	    case "-AD":
		ASTdebug = true;
		break;
	    case "-ID":
		IRdebug = true;
		break;
	    default:
		System.out.println("Error : No such option \"" + option + "\".");
		System.exit(1);
	    }
	}else if(args.length == 1){
	    sourceName = args[0];
	}else{
	    System.out.println("Error : Invalid number of arguments.");
	    System.exit(1);
	}
	
	try{
	    FileReader source;

	    if(advance == true){
		source = new FileReader(new File(sourceName));
		Parser.advancedParsing(source);
	    }
	    if(ASTdebug == true){
		source = new FileReader(new File(sourceName));
		Parser.ASTdebug(source);
	    }
	    if(IRdebug == true){
		source = new FileReader(new File(sourceName));
		Parser.IRdebug(source);
	    }
	    
	    source = new FileReader(new File(sourceName));
   	    Parser.generalParsing(source);

	    System.out.println("Generating assembly code...");
	    AssemblyGenerator.Generate();

	    Runtime r = Runtime.getRuntime();
	    Process p = r.exec("as Assembly.s -o Object.o");
	    p.waitFor();
	    p = r.exec("gcc Object.o -o a.out");
	    p.waitFor();
	    
	}catch(Exception e){
	    e.printStackTrace();
	    System.exit(0);
	}
    }
}
