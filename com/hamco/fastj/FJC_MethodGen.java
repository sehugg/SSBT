package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import java.util.*;

// todo: fix integer overflow
// todo: cut back on number of jinit__* methods called
// todo: catch exception in initializer error
// todo: interfaces should maybe extend jo_...Object, not jobject

public class FJC_MethodGen
implements Constants
{
	FJMethod m;
	MethodGen mg;
	FJContext ctx;
	InstructionList il;
	InstructionHandle curih;
	ConstantPoolGen cpg;
	//Stack stack = new Stack();
	int sp;
	FJMethodInfo.Frame curframe;
	PrintStream out;
	HashMap localvars = new HashMap();
	Set localconsts = new HashSet();
	String[] argnames, argslotnames;
	int numparamslots;
	Set depends = new HashSet();
	
	static boolean debug = false;
	static boolean optimize = true;

	public FJC_MethodGen(FJMethod m)
	{
		this.m = m;
		this.mg = m.mg;
		this.ctx = m.c.ctx;
		this.cpg = m.c.jcg.getConstantPool();
		this.il = mg.getInstructionList();
		if (this.il != null)
			this.curih = il.getStart();
		initParamNames();
	}
	
	static final String VOID = "__JVOID__";

	// todo: should make more type distinctions	
	String type2var(Type t)
	{
		if (t instanceof BasicType)
			return t.toString();
		//else if (t instanceof ArrayType)
		//	return "arr_" + type2var( ((ArrayType)t).getElementType() );
		else
			return "ref";
	}
	
	String type2str(Type t)
	{
		return ctx.type2str(t);
	}
	
	String getLabel(InstructionHandle ih)
	{
		return "jlab__"+ih.getPosition();
	}
	
	String getLabel(Instruction insn)
	{
		return getLabel( ((BranchInstruction)insn).getTarget() );
	}
	
	String getStackName(Type t)
	{
		return getStackName(t, sp);
	}
	
	Type getSlotType(Type t)
	{
		if (t instanceof BasicType)
			return t;
		//if (t instanceof ArrayType)
		//	return new ArrayType(getSlotType( ((ArrayType)t).getElementType() ), 1);
		return Type.NULL;
	}
	
	// returns name of stack, or null if type is VOID
	String getStackName(Type t, int sptr)
	{
		t = getSlotType(t);
		if (t.equals(Type.VOID))
			return null;
		String sname = "js__" + sptr + '_' + type2var(t);
		localvars.put(sname, type2str(t));
		return sname;
	}
	
	String getStackName(int sptr)
	{
		return getStackName(getStackType(sptr, curih), sptr);
	}
	
	String getStackName(int sptr, InstructionHandle ih)
	{
		return getStackName(getStackType(sptr, ih), sptr);
	}
	
	Type getStackType(int sptr, InstructionHandle ih)
	{
		return getStackAssumptions(sptr, ih).tass.type;
	}
	
	FJMethodInfo getInfo()
	{
		try {
			return m.getInfo();
		} catch (RecursiveIntrospectionException e) {
			throw new RuntimeException(e.toString());
		}
	}
	
	FJMethodInfo.Frame getFrame(InstructionHandle ih)
	{
		FJMethodInfo.Frame f = getInfo().getFrame(ih);
		// the frame can be null if the instruction has not been visited
		// for example, if there is an exception handler around a set of
		// statements that do not throw exceptions 
		if (f == null)
			throw new RuntimeException("Frame for " + ih + " is null");
		return f;
	}
	
	FJMethodInfo.AssumptionSet getStackAssumptions(int sptr, InstructionHandle ih)
	{
		FJMethodInfo.Frame f = getFrame(ih);
		FJMethodInfo.AssumptionSet as = f.getStackInfo(sptr);
		if (as == null || as.tass == null)
		{
			f.dumpStack();
			throw new RuntimeException("FOO looking up stack " + sptr + " @ " + ih);
		}
		return as;
	}
	
	Type getLocalType(int lindex, InstructionHandle ih)
	{
		return getLocalAssumptions(lindex, ih).tass.type;
	}
	
	FJMethodInfo.AssumptionSet getLocalAssumptions(int lindex, InstructionHandle ih)
	{
		FJMethodInfo.Frame f = getFrame(ih);
		FJMethodInfo.AssumptionSet as = f.getLocalInfo(lindex);
		if (as == null || as.tass == null)
		{
			f.dumpStack();
			throw new RuntimeException("FOO looking up local " + lindex + " @ " + ih);
		}
		return as;
	}
	
	String getLocalName(int index, InstructionHandle ih)
	{
		if (index == 0 && !mg.isStatic())
			return "_this";
		else {
			String lname;
			/*
			LocalVariableGen[] lvars = mg.getLocalVariables();
			for (int i=0; i<lvars.length; i++)
			{
				if (lvars[i].containsTarget(ih))
					lname = "jl__" + lvars[i].getName();
			}
			*/
			if (index >= numparamslots)
			{
				Type t = getLocalType(index, ih);
				t = getSlotType(t);
				lname = "jl__" + index + '_' + type2var(t);
				localvars.put(lname, type2str(t));
			} else {
				lname = argslotnames[index];
			}
			return lname;
		}
	}
	
	int getLocalObjectIndex(int sp, InstructionHandle ih)
	{
		if (!optimize)
			return -1;
		FJMethodInfo.AssumptionSet as = getStackAssumptions(sp, ih);
		int loindex = as.getLocalObjectIndex();
		if (loindex >= 0 && getInfo().isLocalObject(loindex))
		{
			localvars.put(getLocalObjectName(loindex), ctx.inlinetype2str(as.tass.type));
			return loindex;
		} else
			return -1;
	}
	
	int getLocalObjectIndex()
	{
		return getLocalObjectIndex(sp, curih.getNext());
	}
	
	String getLocalObjectName(int lindex)
	{
		return "jilo__" + lindex;
	}
	
	String cast(Type t, String s)
	{
		if (t == Type.NULL)
			return "(NULL)"; // NULL is always a ref type
		FJClass.addDepends(t, depends);
		return "((" + ctx.type2str(t) + ")(" + s + "))";
	}
	
	void push(Type t, String s)
	{
		out.println('\t' + getStackName(t) + " = " + s + ';');
		sp += t.getSize();
	}
	
	void push(String s)
	{
		push(getStackType(sp, curih.getNext()), s);
	}
	
	void pushConstant(Constant c)
	{
		push(FJC_ClassGen.constantToString(c));
	}
	
	void pop()
	{
		sp--;
	}
	
	// move a stack item from (current offset + s) to (current offset + d)
	void moveStack(int s, int d)
	{
		String sname = getStackName(sp+s-1, (s>0)?curih.getNext():curih);
		String dname = getStackName(sp+d-1, curih.getNext());
		if (sname != null || dname != null)
		{
			if (sname == null || dname == null)
				throw new RuntimeException("Misaligned DUP at " + curih + " : " + sname + ", " + dname);
			out.println('\t' + dname + " = " + sname + ';');
		}
	}
	
	String consume()
	{
		Type t;
		t = getStackType(--sp, curih);
		//System.err.println("Consume 1: " + t);
		if (t.equals(Type.VOID))
		{
			t = getStackType(--sp, curih);
			//System.err.println("Consume 2: " + t);
		}
		if (t instanceof BasicType)
			return getStackName(t);
		else
			return cast(t, getStackName(t));
	}
	
	String consumeNoCast()
	{
		return consume(null);
	}
	
	String consume(Type usert)
	{
		Type t;
		t = getStackType(--sp, curih);
		//System.err.println("Consume 1: " + t);
		if (t.equals(Type.VOID))
		{
			t = getStackType(--sp, curih);
			//System.err.println("Consume 2: " + t);
		}
		if (usert != null)
			return cast(usert, getStackName(t));
		else
			return getStackName(t);
	}
	
	void pushFromLocal(int index)
	{
		// todo: fix
		push(getLocalName(index, curih));
	}
	
	void consumeToLocal(int index)
	{
		String localname = getLocalName(index, curih.getNext());
		String value = consume();
		// if local is a parameter, we have to cast it to jobject
		if (localname.startsWith("jp__"))
			value = cast(getLocalType(index, curih.getNext()), value);
		out.println('\t' + localname + " = " + value + ';');
	}

	String getMethodTableForType(Type t)
	{
		if (t instanceof ArrayType)
		{
			Type elt = ((ArrayType)t).getElementType();
			if (elt instanceof BasicType)
				return "jarray_" + elt.toString() + "_type";
			else
				return "fj_getarrayclass(" + getMethodTableForType(elt) + ")";
		}
		else if (t instanceof ObjectType)
			return '&' + ctx.lookupFJClass( ((ObjectType)t).getClassName() ).getMethodTableName();
		else
			throw new RuntimeException("Can't get method table on type " + t);
	}
	
	Set classdeps = new HashSet();
	
	// calls the init func of a class
	// only if we haven't called it in this method before
	// todo: this ain't right because of data flow
	void addClassDependency(FJClass clazz)
	{
		if (!classdeps.contains(clazz))
		{
			out.println("\t" + clazz.getClassInitFuncName() + "();");
			//classdeps.add(clazz);
		}
	}

	static final String ARITH_OP[] = {
		"+","+","+","+",
		"-","-","-","-",
		"*","*","*","*",
		"/","/","/","/",
		"%","%","%","%",
		"-","-","-","-",
		"<<","<<",">>",">>",">>",">>",
		"&","&","|","|","^","^"
	};
	
	static final String COMPARE_OP[] = {
		"==","!=","<",">=",">","<="
	};
	
	static final String[] CONVERSION_OP = {
		"I2L","I2F","I2D","L2I","L2F","L2D","F2I","F2L","F2D",
		"D2I","D2L","D2F","I2B","I2C","I2S"
	};
	
	void checkForTryBlock()
	{
		CodeExceptionGen[] cegs = m.mg.getExceptionHandlers();
		for (int i=0; i<cegs.length; i++) 
		{
			if (cegs[i].getStartPC().equals(curih))
			{
				out.println("\tFJ_TRY_BEGIN");
			}
		}
	}
	
	void checkForCatchBlock()
	{
		CodeExceptionGen[] cegs = m.mg.getExceptionHandlers();
		boolean gotone = false;
		for (int i=0; i<cegs.length; i++) 
		{
			if (cegs[i].getEndPC().equals(curih))
			{
				if (false && !gotone)
				{
					out.println("\tFJ_TRY_CATCH");
					gotone = true;
				}
				ObjectType t = cegs[i].getCatchType();
				FJClass exclass;
				if (t == null)
					exclass = ctx.lookupFJClass("java.lang.Throwable");
				else
					exclass = ctx.lookupFJClass(t.getClassName());
				out.println("\tFJ_CATCH(" + exclass.getStructName() + ", " + 
					getLabel(cegs[i].getHandlerPC()) + ")");
			}
		}
		if (false && gotone)
			out.println("\tFJ_TRY_END");
	}
	
	
	// returns true if expr is DEFINITELY null
	boolean checkNPE(String expr)
	{
		out.println("\tFJ_CHECKNPE(" + expr + ");");
		return (expr.equals("(NULL)"));
	}
	
	void outputStatement()
	{
		Type t;
		boolean dolabel = false;
		// call the init method if this isn't already a class init method
		String methname = m.mg.getMethodName();
		if (!methname.equals("<clinit>"))
			addClassDependency(m.c);
		do
		{
			// if targeted, output label
			if (dolabel || curih.hasTargeters())
			{
				out.println(getLabel(curih) + ':');
				dolabel = false;
			}
			
			// if we are at the beginning of a try block, output it
			checkForTryBlock();
			if (debug)
				System.err.println(curih);
			try {
				// the frame can be null if the instruction has not been visited
				// for example, if there is an exception handler around a set of
				// statements that do not throw exceptions 
				curframe = m.getInfo().getFrame(curih);
				if (curframe == null)
				{
					out.println("\t/* no instruction @ " + curih + " */ ;");
				} else
					sp = curframe.sp;
			} catch (RecursiveIntrospectionException rie) { 
				throw new RuntimeException(rie.toString());
			}
			
			Instruction insn = curih.getInstruction();
			int tag = insn.getTag();
			if (curframe != null)
				switch (tag)
			{
				case NOP:
					break;
					
				case ACONST_NULL:
					push("NULL");
					break;
					
				case ICONST_M1:
				case ICONST_0:
				case ICONST_1:
				case ICONST_2:
				case ICONST_3:
				case ICONST_4:
				case ICONST_5:
					push(Integer.toString(tag-ICONST_0));
					break;
					
				// todo: add values to all these
					
				case BIPUSH:
					push(((BIPUSH)insn).getValue().toString() );
					break;
					
				case SIPUSH:
					push(((SIPUSH)insn).getValue().toString() );
					break;
				
				case LCONST_0:
				case LCONST_1:
					push(Integer.toString(tag-LCONST_0));
					break;
					
				case FCONST_0:
				case FCONST_1:
				case FCONST_2:
					push(Float.toString(tag-FCONST_0));
					break;
					
				case DCONST_0:
				case DCONST_1:
					push(Double.toString(tag-DCONST_0));
					break;
					
				case LDC:
				case LDC_W:
				case LDC2_W:
					{
					Constant c = cpg.getConstant( ((CPInstruction)insn).getIndex() );
					pushConstant(c);
					}
					break;

				case ILOAD:				
				case ILOAD_0:
				case ILOAD_1:
				case ILOAD_2:
				case ILOAD_3:
				case LLOAD:				
				case LLOAD_0:
				case LLOAD_1:
				case LLOAD_2:
				case LLOAD_3:
				case FLOAD:				
				case FLOAD_0:
				case FLOAD_1:
				case FLOAD_2:
				case FLOAD_3:
				case DLOAD:				
				case DLOAD_0:
				case DLOAD_1:
				case DLOAD_2:
				case DLOAD_3:
				case ALOAD:	
				case ALOAD_0:
				case ALOAD_1:
				case ALOAD_2:
				case ALOAD_3:
					pushFromLocal( ((LocalVariableInstruction)insn).getIndex() );
					break;
					
				case ISTORE:
				case ISTORE_0:
				case ISTORE_1:
				case ISTORE_2:
				case ISTORE_3:
				case LSTORE:
				case LSTORE_0:
				case LSTORE_1:
				case LSTORE_2:
				case LSTORE_3:
				case ASTORE:
				case ASTORE_0:
				case ASTORE_1:
				case ASTORE_2:
				case ASTORE_3:
				case FSTORE:
				case FSTORE_0:
				case FSTORE_1:
				case FSTORE_2:
				case FSTORE_3:
				case DSTORE:
				case DSTORE_0:
				case DSTORE_1:
				case DSTORE_2:
				case DSTORE_3:
					consumeToLocal( ((LocalVariableInstruction)insn).getIndex() );
					break;
				
				// array load insns
				case IALOAD:
		      case BALOAD:
            case CALOAD:
            case SALOAD:
            case LALOAD:
            case FALOAD:
            case DALOAD:
            case AALOAD:
            	{
            	t = ((ArrayInstruction)insn).getArrayType();
            	String index = consume();
            	String array = consume();
            	if (!checkNPE(array))
	            	push(array + "->get(" + index + ')');
            	}
            	break;
            	
           	// array store:
				case IASTORE:
		      case BASTORE:
            case CASTORE:
            case SASTORE:
            case LASTORE:
            case FASTORE:
            case DASTORE:
            case AASTORE:
            	{
            	t = ((ArrayInstruction)insn).getArrayType();
            	String value = consume(); // value
            	String index = consume(); // index
            	String array = consume(new ArrayType(t,1));
            	if (!checkNPE(array))
	            	out.println('\t' + array + "->set(" + index + ", " + value + ");");
            	}
            	break;
            	
            // stack ops

				case POP2:
					pop();            	
            case POP:
            	pop();
            	break;
            	
          	
            case DUP:
            	moveStack(0, 1);
            	break;
            	
            case DUP_X1:
            	{
            		moveStack(0, 1);
            		moveStack(-1, 0);
            		moveStack(1, -1);
            	}
            	break;
            	
            case DUP_X2:
            	{
            		moveStack(0, 1);
            		moveStack(-1, 0);
            		moveStack(-2, -1);
            		moveStack(1, -2);
            	}
            	break;
            	
				case DUP2:
					{
            		moveStack(0, 2);
            		moveStack(-1, 1);
					}
					break;
					
				case DUP2_X1:
					{
            		moveStack(0, 2);
            		moveStack(-1, 1);
            		moveStack(-2, 0);
            		moveStack(2, -1);
            		moveStack(1, -2);
					}
					break;
					
				case DUP2_X2:
					{
            		moveStack(0, 2);
            		moveStack(-1, 1);
            		moveStack(-2, 0);
            		moveStack(-3, -1);
            		moveStack(2, -2);
            		moveStack(1, -3);
					}
					break;
				
				case SWAP:
					{
						// todo: not right
						out.println("// SWAP BROKEN");
						moveStack(0, -1);
						moveStack(-1, 0);
					}
					break;
					
				// arithmetic & logical insns
				
				case IADD:
				case LADD:
				case FADD:
				case DADD:
				case ISUB:
				case LSUB:
				case FSUB:
				case DSUB:
				case IMUL:
				case LMUL:
				case FMUL:
				case DMUL:
				case IDIV:
				case LDIV:
				case FDIV:
				case DDIV:
				case IREM:
				case LREM:
				case FREM:
				case DREM:
				case ISHL:
				case LSHL:
				case ISHR:
				case LSHR:
				case IUSHR:
				case LUSHR:
				case IAND:
				case LAND:
				case IOR:
				case LOR:
				case IXOR:
				case LXOR:
					{
						Type[] types = ((ArithmeticInstruction)insn).getTypes();
						String arg2 = consume();
						String arg1 = consume();
						switch (tag)
						{
							case FREM:
								push("FREM(" + arg1 + ", " + arg2 + ")");
								break;
							case DREM:
								push("DREM(" + arg1 + ", " + arg2 + ")");
								break;
							case IUSHR:
								push("IUSHR(" + arg1 + ", " + arg2 + ")");
								break;
							case LUSHR:
								push("LUSHR(" + arg1 + ", " + arg2 + ")");
								break;
							default:
								push('(' + arg1 + ARITH_OP[tag-IADD] + arg2 + ')');
								break;
						}
					}
					break;

				case INEG:
				case LNEG:
				case FNEG:
				case DNEG:
					{
						Type[] types = ((ArithmeticInstruction)insn).getTypes();
						String arg1 = consume();
						push("(-" + arg1 + ')');
					}
					break;

				case IINC:
					{
						out.println('\t' + getLocalName( ((IINC)insn).getIndex(), curih ) + " += " +
							((IINC)insn).getIncrement() + ";");
					}
					break;

				// conversion insns

					
				case I2L:
				case I2F:
				case I2D:
				case L2I:
				case L2F:
				case L2D:
				case F2I:
				case F2L:
				case F2D:
				case D2I:
				case D2L:
				case D2F:
				case I2B:
				case I2C:
				case I2S:
					// todo
					{
					String s = consume();
					push("FJ_" + CONVERSION_OP[tag-I2L] + '(' + s + ')' );
					}
					break;

				// compare insns
				
				case LCMP:
					{
					String arg2 = consume();
					String arg1 = consume();
					push("FJ_LCMP(" + arg1 + "," + arg2 + ")");
					}
					break;
					
				case FCMPL:
				case FCMPG:
					{
					String arg2 = consume();
					String arg1 = consume();
					push("FJ_" + (tag==FCMPL?"FCMPL":"FCMPG") + "(" + arg1 + "," + arg2 + ")");
					}
					break;
					
				case DCMPL:
				case DCMPG:
					{
					String arg2 = consume();
					String arg1 = consume();
					push("FJ_" + (tag==DCMPL?"DCMPL":"DCMPG") + "(" + arg1 + "," + arg2 + ")");
					}
					break;
					
				// if instructions
				
				case IFEQ:
		      case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
            	out.println("\tif (" + consume() + COMPARE_OP[tag-IFEQ] +
            		"0) goto " + getLabel(insn) + ';');
            	break;

		      case IF_ICMPEQ:
		      case IF_ICMPNE:
      		case IF_ICMPLT:
		      case IF_ICMPGT:
      		case IF_ICMPGE:
		      case IF_ICMPLE:
		      	{
		      	String arg2 = consume();
		      	String arg1 = consume();
		      	out.println("\tif (" + arg1 + COMPARE_OP[tag-IF_ICMPEQ] + arg2 + ") goto "
		      		+ getLabel(insn) + ';');
            	}
            	break;

            case IF_ACMPEQ:
            case IF_ACMPNE:
            	{
            	// todo: compiler gives error.  is this "nocast" wrong?
            	String arg2 = consumeNoCast();
            	String arg1 = consumeNoCast();
            	String op = (tag == IF_ACMPEQ ? "==" : "!=");
		      	out.println("\tif (" + arg1 + op + arg2 + ") goto " + getLabel(insn) + ';');
            	}
            	break;
            	
				case IFNULL:
				case IFNONNULL:
					out.println("\tif (" + consumeNoCast() + (tag==IFNULL?"==":"!=") + 
						"NULL) goto " + getLabel(insn) + ';');
					break;
					
           // goto insns
            	
				case GOTO:
				case GOTO_W:
					out.println("\tgoto " + getLabel(insn) + ';');
					break;
					
				case JSR:
				case JSR_W:
					// todo: i don't like this cast
					push(Type.OBJECT, "(jobject*)&&"+getLabel(curih.getNext()));
					dolabel = true;
					break;
					
				// return functions

				case RET:
				 	out.println("\tgoto *(void*)" + getLocalName( ((RET)insn).getIndex(), curih ) + ";");
				 	break;

				case ARETURN:
					// if we return an interface, we have to cast it to the return type
					out.println("\treturn " + consume( mg.getReturnType() ) + ';');
					break;
				case IRETURN:
				case LRETURN:
				case FRETURN:
				case DRETURN:
					out.println("\treturn " + consume() + ';');
					break;
				case RETURN:
					out.println("\treturn;");
					break;
					
				// switch functions
					
				case LOOKUPSWITCH:
				case TABLESWITCH:
					{
						out.println("\tswitch (" + consume() + ") {");
						InstructionHandle[] targets = ((Select)insn).getTargets();
						int[] matches = ((Select)insn).getMatchs();
						for (int i=0; i<targets.length; i++)
							out.println("\t\tcase " + matches[i] + ": goto " + getLabel(targets[i]) + ";");
						out.println("\t\tdefault: goto " + getLabel( ((Select)insn).getTarget() ) + ";");
						out.println("\t}");
					}
					break;
					
				// get & put field functions
					
				case GETFIELD:
					{
					FieldInstruction finsn = (FieldInstruction)insn;
					String obj = consume();
					if (!checkNPE(obj))
						push(obj + "->" + ctx.field2ident(finsn.getFieldName(cpg)));
					}
					break;

				case GETSTATIC:
					{
					FieldInstruction finsn = (FieldInstruction)insn;
					FJClass fjc = ctx.lookupFJClass(finsn.getClassName(cpg));
					if (fjc != m.c)
						addClassDependency(fjc);
					push(
						fjc.getStructName() + 
						"::" + ctx.field2ident(finsn.getFieldName(cpg)));
					}
					break;

				// todo: check isConst(), don't do it if true
				
				case PUTFIELD:
					{
					FieldInstruction finsn = (FieldInstruction)insn;
					t = finsn.getFieldType(cpg);
					String value = consume(t);
					String lexpr = consume();
					if (!checkNPE(lexpr))
						out.println('\t' + lexpr + "->" + ctx.field2ident(finsn.getFieldName(cpg)) +
							" = " + value + ';');
					}
					break;
				
				case PUTSTATIC:
					{
					FieldInstruction finsn = (FieldInstruction)insn;
					t = finsn.getFieldType(cpg);
					String value = consume(t);
					FJClass fjc = ctx.lookupFJClass(finsn.getClassName(cpg));
					String lexpr = fjc.getStructName();
					if (fjc != m.c)
						addClassDependency(fjc);
					out.println('\t' + lexpr + "::" + ctx.field2ident(finsn.getFieldName(cpg)) +
						" = " + value + ';');
					}
					break;
					
				// invoke functions
				
				case INVOKEINTERFACE:
				case INVOKESPECIAL:
				case INVOKESTATIC:
				case INVOKEVIRTUAL:
					{
						InvokeInstruction invk = (InvokeInstruction)insn;
						boolean statik = (tag==INVOKESTATIC);
						int argbias = statik?0:1;
						// consume the arguments from the stack
						Type[] types = invk.getArgumentTypes(cpg);
						String[] args = new String[types.length+argbias];
						for (int i=types.length-1; i>=0; i--)
						{
							args[i+argbias] = consume(types[i]);
						}
						// consume the object ref
						FJMethodInfo.TypeAssumption objtype = null;
						if (!statik)
						{
							// todo: is this bad?? we may have to recast this if we change from virtual 2 static
							args[0] = consume(invk.getClassType(cpg));
							//args[0] = consume();
							// if we are dereferencing a NULL, just break
							if (checkNPE(args[0]))
								break;
							if (optimize && tag != INVOKESPECIAL) {
								objtype = getStackAssumptions(sp, curih).tass;
							}
						}
						// now lookup the method; we'll need some info about it
						String invoked_cname = invk.getClassName(cpg);
						// optimization: if type is exact, we can demote the
						// virtual call to a direct call
						if (objtype != null && objtype.exact && (objtype.type instanceof ObjectType))
						{
							tag = INVOKESPECIAL;
							invoked_cname = ((ObjectType)objtype.type).getClassName();
							args[0] = cast(objtype.type, args[0]);
							if (debug)
								System.err.println("Demoting virtual call to " + invoked_cname);
						}
						FJClass invoked_class = m.c.ctx.lookupFJClass(invoked_cname);
						if (invoked_class == null)
							throw new RuntimeException("Class " + invoked_cname + " not found");
						FJMethod invoked_method = invoked_class.findMethod(invk.getMethodName(cpg), invk.getSignature(cpg));
						if (invoked_method == null)
							throw new RuntimeException("Method " + invoked_cname + "." + invk.getSignature(cpg) + " not found");
						if (objtype != null && objtype.exact)
						{
							invoked_class = invoked_method.c;
						}
						// sometimes Java will issue INVOKEVIRTUAL when a
						// class or method is final and doesn't override a method in the
						// subclass -- so we override it
						if (tag == INVOKEVIRTUAL && invoked_method.vtblindex < 0)
							tag = INVOKESPECIAL;
						// call the method
						StringBuffer st = new StringBuffer();
						// todo: support interfaces
						t = invk.getReturnType(cpg);
						switch (tag)
						{
							case INVOKESTATIC :
							case INVOKESPECIAL :
								st.append(invoked_class.getStructName());
								st.append("::");
								st.append(getFuncName(invoked_method));
								break;
							case INVOKEVIRTUAL :
								st.append(" /* " + invoked_method + " */");
								//if (!Type.VOID.equals(t))
								//	st.append('(' + type2str(getSlotType(t)) + ')'); // todo: why??
								st.append("FJ_CALLVIRTUAL(");
								st.append(args[0]);
								st.append(", ");
								st.append(invoked_method.vtblindex);
								st.append(", ");
								st.append(invoked_method.getMethodTypedef());
								st.append(')');
								break;
							case INVOKEINTERFACE :
								st.append(" /* " + invoked_method + " */");
								st.append("FJ_CALLINTERFACE(");
								st.append(args[0]);
								st.append(", ");
								st.append(invoked_method.vtblindex);
								st.append(", ");
								st.append(invoked_method.getMethodTypedef());
								st.append(", &");
								st.append(invoked_class.getMethodTableName());
								st.append(')');
								break;
						}
						st.append('(');
						for (int i=0; i<args.length; i++)
						{
							if (i>0)
								st.append(',');
							String arg = args[i];
							//Type argt = ((i-argbias)<0)?invk.getClassType(cpg):types[i-argbias];
							//if (!(argt.equals(invoked_method.getParamType(i))))
							//	arg = cast(argt, arg);
							st.append(arg);
						}
						st.append(')');
						// push it good
						if (!Type.VOID.equals(t))
						{
							push(st.toString());
						} else {
							out.println('\t' + st.toString() + ';');
						}
					}
					break;
					
				// allocation insns
				
				case NEW:
					{
						t = ((NEW)insn).getType(cpg);
						boolean islocal = getInfo().isLocalObject(curih.getPosition());
						if (!islocal)
						{
							push( "FJ_NEWOBJECT(" + 
								ctx.lookupFJClass(((ObjectType)t).getClassName()).getStructName() + ")" );
						} else {
							push( "FJ_NEWOBJECTLOCAL(" + 
								ctx.lookupFJClass(((ObjectType)t).getClassName()).getStructName() + ")" );
							localvars.put("jlalloc", "jobjallocator");
						}
					}
					break;
					
				case NEWARRAY:
					{
						t = ((NEWARRAY)insn).getType();
						Type eltype = ((ArrayType)t).getElementType();
						String arraylen = consume();
						boolean islocal = getInfo().isLocalObject(curih.getPosition());
						if (!islocal)
						{
							push( "FJ_NEWARRAY(" + type2str(eltype).substring(1) + 
								", " + arraylen + ")" );
						} else {
							push( "FJ_NEWARRAYLOCAL(" + type2str(eltype).substring(1) + 
								", " + arraylen + ")" );
							localvars.put("jlalloc", "jobjallocator");
						}
					}
					break;
					
				case ANEWARRAY:
					{
						t = ((ANEWARRAY)insn).getType(cpg);
						Type eltype = new ArrayType(t, 1);
						String arraylen = consume();
						boolean islocal = getInfo().isLocalObject(curih.getPosition());
						if (!islocal)
						{
							push( "FJ_ANEWARRAY(" + getMethodTableForType(eltype) + 
								", " + arraylen + ")" );
						} else {
							push( "FJ_ANEWARRAYLOCAL(" + getMethodTableForType(eltype) +
								", " + arraylen + ")" );
							localvars.put("jlalloc", "jobjallocator");
						}
					}
					break;

				case MULTIANEWARRAY:
					{
						MULTIANEWARRAY mana = (MULTIANEWARRAY)insn;
						t = mana.getType(cpg);
						int ndims = mana.getDimensions();
						String args = getMethodTableForType(t) + ", " + ndims;
						for (int i=0; i<ndims; i++)
						{
							args = args + ", " + consume();
						}
						push("fj_new_amultiarray(" + args + ")");
					}
					break;

				// misc insns

				case ARRAYLENGTH:
					{
					String array = consume();
					if (!checkNPE(array))
						push( array + "->length()" );
					}
					break;

				case ATHROW:
					{
					String ex = consume();
					if (!checkNPE(ex))
						out.println("\tFJ_ATHROW(" + ex + ");");
					}
					break;

				case CHECKCAST:
					{
					t = ((CHECKCAST)insn).getType(cpg);
					String obj = consume();
					out.println("\tFJ_CHECKCAST(" + obj + ", " + getMethodTableForType(t) + ");");
					}
					break;
					
				case INSTANCEOF:
					{
					t = ((INSTANCEOF)insn).getType(cpg);
					String obj = consume();
					push("FJ_INSTANCEOF(" + obj + ", " + getMethodTableForType(t) + ")");
					}
					break;
					
				case MONITORENTER:
					out.println("\tFJ_MONITORENTER(" + consume() + ");");
					break;
					
				case MONITOREXIT:
					out.println("\tFJ_MONITOREXIT(" + consume() + ");");
					break;
				
				default:
					System.err.println("Unknown instruction " + curih);
					out.println("// unknown instruction " + curih);
					//throw new RuntimeException("*** UNRECOGNIZED INSTRUCTION " + insn);
			}
			checkForCatchBlock();
			// advance to next instruction until all done
			curih = curih.getNext();
		} while (curih != null);
	}
	
	public void outputLocals()
	{
		Iterator it = localvars.entrySet().iterator();
		while (it.hasNext())
		{
			Map.Entry entry = (Map.Entry)it.next();
			out.println("\t" + entry.getValue() + ' ' + entry.getKey() + ';');
		}
		it = localconsts.iterator();
		while (it.hasNext())
		{
			String line = (String)it.next();
			out.println("\t" + line);
		}
	}
	
	public void outputMethodCode(PrintStream out)
	{
		this.out = out;
		// first output prototype
		outputPrototype(out, false);
		// output block
		out.println("{");
		
		if (curih != null)
		{
			// output the code into a buffer first
			ByteArrayOutputStream codebuf = new ByteArrayOutputStream();
			this.out = new PrintStream(codebuf);
			try {
				outputStatement();
			} catch (RuntimeException re) {
				System.err.println("Error at insn " + curih + " in method " + m);
				throw re;
			} catch (Error er) {
				System.err.println("Error at insn " + curih + " in method " + m);
				throw er;
			}
		
			// now output the locals that the code created
			this.out = out;
			outputLocals();
			// now finally output the code
			out.println();
			out.print(codebuf.toString());
		} else {
			// it's abstract or native, generate code to this effect
			// todo: throw exception
			out.println("\tfj_throw_abstracterror();");
		}
		
		out.println("}");
	}

	public String getParamLocalName(int lindex)
	{
		return argnames[lindex];
	}
		
	public String getFuncName(FJMethod jfm)
	{
		/*
		return c.ctx.sig2ident(mg.getMethodName() + "_" + mg.getMethodSignature()) + 
			"_" + c.ctx.class2ident(c.jc.getClassName()); //todo: ambig?
		return ctx.sig2ident("jm_" + jfm.mg.getMethodName() + "_" + jfm.mg.getMethodSignature());
		*/
		return jfm.getMethodFuncName();
	}
	
	public String getFuncName()
	{
		return getFuncName(m);
	}
	
	private void initParamNames()
	{
		// todo: should set byte, etc params to local ints?
		boolean statik = mg.isStatic();
		Type[] args = mg.getArgTypes();
		LocalVariableGen[] lvg = mg.getLocalVariables();
		numparamslots = statik?0:1;
		/*
		if (lvg.length < args.length+(statik?0:1))
			System.err.println("***WARNING: Have " + args.length + " params, but " + lvg.length + " locals");
		*/
		argnames = new String[args.length];
		argslotnames = new String[args.length*2+1];
		for (int i=0; i<args.length; i++)
		{
			int j = i+(statik?0:1);
			argnames[i] = "jp__" + ((j < lvg.length) ? lvg[j].getName() : Integer.toString(i));
			argslotnames[numparamslots] = argnames[i];
			numparamslots += args[i].getSize();
		}
	}
	
	public void outputPrototype(PrintStream out, boolean inlined)
	{
		boolean statik = mg.isStatic();
		if (inlined)
			out.print("static ");
		if (mg.isNative())
			out.print("FJ_NATIVE ");
		out.print(ctx.type2str(mg.getReturnType()));
		out.print(' ');
		if (!inlined)
		{
			out.print(m.c.getStructName());
			out.print("::");
		}
		out.print(getFuncName());
		Type[] args = mg.getArgTypes();
		out.print('(');
		if (!statik)
		{
			out.print(ctx.type2str(new ObjectType(m.c.jc.getClassName())));
			if (!inlined)
			{
				out.print(' ');
				out.print("_this");
			}
		}
		for (int i=0; i<args.length; i++)
		{
			if (i>0 || !statik)
				out.print(", ");
			out.print(ctx.type2str(args[i]));
			if (!inlined)
			{
				out.print(' ');
				out.print(argnames[i]);
			}
		}
		if (inlined)
			out.println(");");
		else
			out.println(")");
	}

}
