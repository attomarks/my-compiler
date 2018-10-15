import java.util.*;

public class ParserVisitorImplements implements ParserVisitor {

    static List<String> var_temp = new ArrayList<>();
    List<String> labels = new ArrayList<>();
    int label_count = 0;
    IRGenerator IRG = new IRGenerator();
    
	@Override
	public Object visit(SimpleNode node, Object data){
	       return null;
	}

	@Override
	public Object visit(ASTbody node, Object data){
	    //System.out.println("visited ASTbody");
	    node.jjtGetChild(0).jjtAccept(this,null);
	    return null;
	}

	@Override
	public Object visit(ASTdefines node, Object data){
	    //System.out.println("visited ASTdefines");
	    for(int i=0; i<node.jjtGetNumChildren(); i++)
		node.jjtGetChild(i).jjtAccept(this,null);
	    return node.jjtGetNumChildren();
	}

	@Override
	public Object visit(ASTdef_fun node, Object data){
	    //System.out.println("visited ASTdef_fun");
	    String type = node.jjtGetChild(0).jjtAccept(this,null).toString();
	    String name = node.jjtGetChild(1).jjtAccept(this,null).toString();
	    IRG.IRdef_fun(type,name);
	    node.jjtGetChild(2).jjtAccept(this,null);
	    node.jjtGetChild(3).jjtAccept(this,null);
	    return name;
	}
    
	@Override
	public Object visit(ASTparams node, Object data){
	    //System.out.println("visited ASTparams");
	    if(node.jjtGetNumChildren() == 0)
		return "void";
	    else
		return node.jjtGetChild(0).jjtAccept(this,null);
	}

    @Override
	public Object visit(ASTfixedparams node, Object data){
	    //System.out.println("visited ASTfixedparams");
	for(int i=0; i<node.jjtGetNumChildren(); i++)
	    node.jjtGetChild(i).jjtAccept(this,null);
	return node.jjtGetNumChildren();
	}

    @Override
	public Object visit(ASTparam node, Object data){
	    //System.out.println("visited ASTparam");
	    String type = node.jjtGetChild(0).jjtAccept(this,null).toString();
	    String name = node.jjtGetChild(1).jjtAccept(this,null).toString();
	    IRG.IRparam(type,name);
	    return null;
	}

	@Override
	public Object visit(ASTblock node, Object data){
	    //System.out.println("visited ASTblock");
	    int size = node.jjtGetNumChildren();
	    for(int i=0;i<size;i++){
		node.jjtGetChild(i).jjtAccept(this,null);
	    }
	    return size;
	}

        @Override
	public Object visit(ASTdef_var_list node, Object data){
	    //System.out.println("visited ASTdef_var_list");
	    return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTdef_vars node, Object data){
	    //System.out.println("visited ASTdef_vars");
	    int size = node.jjtGetNumChildren();
	    List<String> var_name = new ArrayList<>();
	    List<String> var_value = new ArrayList<>();
	    String type = node.jjtGetChild(0).jjtAccept(this,null).toString();
	    for(int i=1;i<size;i++){
		if(node.jjtGetChild(i).toString().equals("name")){
		    var_name.add(node.jjtGetChild(i).jjtAccept(this,null).toString());
		    IRG.IRdef_var(type,var_name.get(var_name.size()-1));
		    if(i != size-1 && node.jjtGetChild(i+1).toString().equals("expr")){
			var_value.add(node.jjtGetChild(i+1).jjtAccept(this,null).toString());
			i++;
		    }else var_value.add("null");
		}
		if(var_name.size() == var_value.size())
		    IRG.IRvar_ini(type,var_name.get(var_name.size()-1),var_value.get(var_value.size()-1));
	    }
	    /*for(int i=0;i<=var_value.size()-1;i++)
		IRG.IRdef_vars(type,var_name.get(i),var_value.get(i));*/
	    return type;
	}

		/*if(i%2==1){
		    var_name.add(node.jjtGetChild(i).jjtAccept(this,null).toString());
		    //IRG.IRdef_vars(type,var_name.get(var_name.size()-1));
		}else if(i%2==0 && i!=size){
		    var_value.add(node.jjtGetChild(i).jjtAccept(this,null).toString());
		}else var_value.add("null");
	    }
	    for(int i=0;i<=var_value.size()-1;i++) IRG.IRdef_vars(type,var_name.get(i),var_value.get(i));
	    return type;
	    }*/ 

	@Override
	public Object visit(ASTdef_const node, Object data){
	    //System.out.println("visited ASTdef_const");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTtype node, Object data){
	    //System.out.println("visited ASTtype");
	    return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTtype_ref node, Object data){
	    //System.out.println("visited ASTtype_ref");
	    return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTtype_basic node, Object data){
	    //System.out.println("visited ASTtype_basic");
	    return node.jjtGetValue();
	}

	@Override
	public Object visit(ASTname node, Object data){
	    //System.out.println("visited ASTname");
	    return node.jjtGetValue();
	}

	@Override
	public Object visit(ASTstmts node, Object data){
	    //System.out.println("visited ASTstmts");
	       int size = node.jjtGetNumChildren();
	       for(int i=0;i<size;i++){
		   node.jjtGetChild(i).jjtAccept(this,null);
	       }
	       return size;
	}

	@Override
	public Object visit(ASTstmt node, Object data){
	    //System.out.println("visited ASTstmt");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

    @Override
    public Object visit(ASTif_stmt node, Object data){
	//System.out.println("visited ASTif_stmt");
	boolean else_def = false;
	if(node.jjtGetNumChildren() == 3) else_def = true;
	
	node.jjtGetChild(0).jjtAccept(this,null);
	labels.add(".L"+label_count);
	String label1 = labels.get(labels.size()-1);
	label_count++;
	IRGenerator.IRjnz(label1);
	labels.add(".L"+label_count);
	String label2 = labels.get(labels.size()-1);
	label_count++;
	IRGenerator.IRjmp(label2);
	IRGenerator.IRlabel(label1);
	node.jjtGetChild(1).jjtAccept(this,null);
	if(else_def == true){
	    labels.add(".L"+label_count);
	    String label3 = labels.get(labels.size()-1);
	    label_count++;
	    IRGenerator.IRjmp(label3);
	    IRGenerator.IRlabel(label2);
	    node.jjtGetChild(2).jjtAccept(this,null);
	    IRGenerator.IRlabel(label3);
	}else{
	    IRGenerator.IRlabel(label2);
	}
	return label1;
    }

    @Override
    public Object visit(ASTfor_stmt node, Object data){
	//System.out.println("visited ASTfor_stmt");
	node.jjtGetChild(0).jjtAccept(this,null);
	labels.add(".L"+label_count);
	String label1 = labels.get(labels.size()-1);
	label_count++;
	IRGenerator.IRlabel(label1);

	node.jjtGetChild(1).jjtAccept(this,null);
	labels.add(".L"+label_count);
	String label2 = labels.get(labels.size()-1);
	label_count++;
	IRGenerator.IRjnz(label2);
	labels.add(".L"+label_count);
	String label3 = labels.get(labels.size()-1);
	label_count++;
	IRGenerator.IRjmp(label3);

	IRGenerator.IRlabel(label2);
	node.jjtGetChild(3).jjtAccept(this,null);
	node.jjtGetChild(2).jjtAccept(this,null);
	IRGenerator.IRjmp(label1);
	IRGenerator.IRlabel(label3);

	return label3;
    }

    @Override
    public Object visit(ASTprintf_stmt node, Object data){
	//System.out.println("visited ASTprintf_stmt");
	String str = node.jjtGetValue().toString();
	int size = node.jjtGetNumChildren();
	int i;
	List<String> var = new ArrayList<>();
	for(i=size;i>0;i--){
	    var.add(node.jjtGetChild(i-1).jjtAccept(this,null).toString());
	}
        for(i=size;i>0;i--){
	    IRGenerator.IRprintf_stmt(var.get(size-i),i);
	}
	IRGenerator.IRprintf_stmt(str);
	return str;
    }

	@Override
	public Object visit(ASTreturn_stmt node, Object data){
	    //System.out.println("visited ASTreturn_stmt");
	    String name = node.jjtGetChild(0).jjtAccept(this,null).toString();
	    IRG.IRreturn_stmt(name);
	    return name;
	}

	@Override
	public Object visit(ASTexpr node, Object data){
	    //System.out.println("visited ASTexpr");
	    //System.out.println("expr_Children = " + node.jjtGetNumChildren());
	    String term = node.jjtGetChild(0).jjtAccept(this,null).toString();
	    if(node.jjtGetValue().toString() == "="){
		IRG.IRexpr(term,node.jjtGetChild(1).jjtAccept(this,null).toString());
		return term;
	    }
	    return term;
	}

	@Override
	public Object visit(ASTexpr10 node, Object data){
	    //System.out.println("visited ASTexpr10");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTexpr9 node, Object data){
	    //System.out.println("visited ASTexpr9");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTexpr8 node, Object data){
	    //System.out.println("visited ASTexpr8");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTexpr7 node, Object data){
	    //System.out.println("visited ASTexpr7");
	     
       	      List<String> ops = (List<String>)node.jjtGetValue();
	      String op;
	      String lhs = node.jjtGetChild(0).jjtAccept(this,null).toString();
	      String rhs;
	      String return_str = null;

	      if(node.jjtGetNumChildren()>=2){
		  for(int i=1;i<=node.jjtGetNumChildren()-1;i++){
		      //System.out.println("expr7:i="+i);
		      op = ops.get(i-1);
		      rhs = node.jjtGetChild(i).jjtAccept(this,null).toString();
		      var_temp.add("wk" + var_temp.size());
		      String eq = var_temp.get(var_temp.size()-1);
		      IRG.IRexpr(op,lhs,rhs,eq);
		      lhs = eq;
		      return_str = eq;
		  }return return_str;
	      }else return node.jjtGetChild(0).jjtAccept(this,null).toString();
	}

    @Override
	public Object visit(ASTexpr6 node, Object data){
	//System.out.println("visited ASTexpr6");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}


	@Override
	public Object visit(ASTexpr5 node, Object data){
	    //System.out.println("visited ASTexpr5");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTexpr4 node, Object data){
	    //System.out.println("visited ASTexpr4");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTexpr3 node, Object data){
	    //System.out.println("visited ASTexpr3");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

        @Override
       public Object visit(ASTexpr2 node, Object data){
	    //System.out.println("visited ASTexpr2");
       	      List<String> ops = (List<String>)node.jjtGetValue();
	      String op;
	      String lhs = node.jjtGetChild(0).jjtAccept(this,null).toString();
	      String rhs;
	      String return_str = null;

	      if(node.jjtGetNumChildren()>=2){
		  for(int i=1;i<=node.jjtGetNumChildren()-1;i++){
		      //System.out.println("expr2:i="+i);
		      op = ops.get(i-1);
		      rhs = node.jjtGetChild(i).jjtAccept(this,null).toString();
		      var_temp.add("wk" + var_temp.size());
		      String eq = var_temp.get(var_temp.size()-1);
		      IRG.IRexpr(op,lhs,rhs,eq);
		      lhs = eq;
		      return_str = eq;
		  }return return_str;
	      }else return node.jjtGetChild(0).jjtAccept(this,null).toString();
	}

	@Override
	public Object visit(ASTexpr1 node, Object data){
	    //System.out.println("visited ASTexpr1");
	      List<String> ops = (List<String>)node.jjtGetValue();
	      String op;
	      String lhs = node.jjtGetChild(0).jjtAccept(this,null).toString();
	      String rhs;
	      String return_str = null;

	      if(node.jjtGetNumChildren()>=2){
		  for(int i=1;i<=node.jjtGetNumChildren()-1;i++){
		      //System.out.println("expr1:i="+i);
		      op = ops.get(i-1);
		      rhs = node.jjtGetChild(i).jjtAccept(this,null).toString();
		      var_temp.add("wk" + var_temp.size());
		      String eq = var_temp.get(var_temp.size()-1);
		      IRG.IRexpr(op,lhs,rhs,eq);
		      lhs = eq;
		      return_str = eq;
		  }return return_str;
	      }else return node.jjtGetChild(0).jjtAccept(this,null).toString();
        }

        @Override
	public Object visit(ASTterm node, Object data){
	    //System.out.println("visited ASTterm");
	    String type;
	    if((int)node.jjtGetNumChildren() == 2){
		type = node.jjtGetChild(0).jjtAccept(this,null).toString();
		
	    }
	
	    return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTunary node, Object data){
	    //System.out.println("visited ASTunary");
	       return node.jjtGetChild(0).jjtAccept(this,null);
	}

	@Override
	public Object visit(ASTpostfix node, Object data){
	    //System.out.println("visited ASTpostfix");
	    int size = node.jjtGetNumChildren();
	    if(size == 2){
		String name = node.jjtGetChild(0).jjtAccept(this,null).toString();
		node.jjtGetChild(1).jjtAccept(this,null);
		return name;
	    }else
		return node.jjtGetChild(0).jjtAccept(this,null);
	}

    	@Override
	public Object visit(ASTargs node, Object data){
	    //System.out.println("visited ASTargs");
	    int size = node.jjtGetNumChildren();
	    //for(int i=size-1; i>=0; i--){
	    for(int i=0; i<size; i++){
		String name = node.jjtGetChild(i).jjtAccept(this,null).toString();
		IRG.IRarg(name);
	    }
	    return null;
	}

	@Override
	public Object visit(ASTprimary node, Object data){
	    //System.out.println("visited ASTprimary");
	    //System.out.println("ASTprimary " + node.jjtGetValue().toString());
	       return node.jjtGetValue();
	}

    static List<String> importWk(){
	return var_temp;
    }
}
