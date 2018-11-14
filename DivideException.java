class DivideException extends Exception{
    public DivideException(int block){
	System.out.println("##############################################################");
	DivideIntoBlocks.Generate(block);
	System.out.println("Some blocks are not closed properly.\n"
	      + "Please refer to the last block written above and its contents.");
	System.out.println("##############################################################");
    }
}
