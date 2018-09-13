import java.util.*;
import java.io.*;

class DivideIntoBlocks{
    static List<String> blocks = new ArrayList<>();
    static int Dividing(FileReader source){
	List<Boolean> usedBlockNum = new ArrayList<>();

	try{
	    int ch;
	    int index;
	    String str;
	    String temp;

	    ch = source.read();
	    blocks.add(String.valueOf((char)ch));
	    usedBlockNum.add(false);
	    while((ch = source.read()) != -1){
		str = String.valueOf((char)ch);
		if(str.equals("{")){
		    index = usedBlockNum.lastIndexOf(false);
		    //System.out.println("1st{"+index);
		    temp = blocks.get(index);
		    blocks.set(index,temp+str);
		    usedBlockNum.add(false);
		    //System.out.println("2nd{"+(usedBlockNum.size()-1));
		    blocks.add(str);
		}else if(str.equals("}")){
		    index = usedBlockNum.lastIndexOf(false);
		    //System.out.println("true{"+index);
		    temp = blocks.get(index);
		    blocks.set(index,temp+str);
		    usedBlockNum.set(index,true);
		    index = usedBlockNum.lastIndexOf(false);
		    //System.out.println("false{"+index);
		    temp = blocks.get(index);
		    blocks.set(index,temp+str);
		}else{
		    index = usedBlockNum.lastIndexOf(false);
		    temp = blocks.get(index);
		    blocks.set(index,temp+str);
		}
	    }
	}catch (Exception e){
	    e.printStackTrace();
	}
	return blocks.size();
    }
    
    static void Generate(){
	int size = blocks.size();
	System.out.println("size="+size);
	for(int i=0;i<size;i++){
	    System.out.println("block"+(i+1));
	    System.out.println(blocks.get(i));
	    System.out.println("");
	}
    }

    static void Generate(int num){
	System.out.println(blocks.get(num));
    }
    
    static Reader blockRef(int index){
	Reader reader = new StringReader(blocks.get(index));
	return reader;
    }
}
