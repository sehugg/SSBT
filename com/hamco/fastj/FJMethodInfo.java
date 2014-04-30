package com.hamco.fastj;

import Acme.IntHashtable;
import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;
import java.util.*;

class FJMethodInfo
implements Constants
{
	static boolean debug = false;
	static boolean recurse_method_calls = true;

	FJMethod m;
	FJContext ctx;
	Stack framelist = new Stack();
	HashMap bblocks = new HashMap();
	ConstantPoolGen cpg;
	AssumptionSet returntype;
	/**
	 * Which parameters are migrated after the execution of this method?
	 * The ways this could happen are:
	 * - PUTFIELD or PUTSTATIC on a reference type containing the parameter
	 * - storage in an array that is not local to the method
	 * - call to a method that migrates this parameter
	 *   (must check return types to make sure the parameter itself is not returned)
	 */
	BitSet params_migrated = new BitSet();
	/**
	 * If this method returns a parameter, we want to know about it
	 */
	BitSet params_returned = new BitSet();

	/**
	 * objects that can be safely deleted after method exits
	 */
	private IntHashtable localobjset = new IntHashtable();
	
	Frame startf;
	
	static final Type ADDRESS_TYPE = ReturnAddressType.TYPE;
	static final Type NULL_TYPE = Type.NULL; /// todo: should be TYPE.NULL?
	static final Type UNKNOWN_TYPE = UnknownType.TYPE;
	
	// we like to save memory
	static WeakHashMap interncache = new WeakHashMap();
	
	static Object intern(Object io)
	{
		Object o = interncache.get(io);
		if (o == null)
		{
			interncache.put(io, io);
			return io;
		} else
			return o;
	}
	static Assumption intern(Assumption ass)
	{
		return (Assumption)intern((Object)ass);
	}
	static TypeAssumption intern(TypeAssumption ass)
	{
		return (TypeAssumption)intern((Object)ass);
	}
	static Item intern(Item ass)
	{
		return (Item)intern((Object)ass);
	}

	public static abstract class Item
	{
	}
	
	public static class StackItem extends Item
	{
		int index;
		StackItem(int i) { index=i; }
		public String toString() { return "S"+index; }
		public int hashCode() { return index; }
		public boolean equals(Object o) { return ((o instanceof StackItem) && ((StackItem)o).index==index); }
	}
	
	public static class LocalItem extends Item
	{
		int index;
		LocalItem(int i) { index=i; }
		public String toString() { return "L"+index; }
		public int hashCode() { return index+65536; }
		public boolean equals(Object o) { return ((o instanceof LocalItem) && ((LocalItem)o).index==index); }
	}
	
	public static abstract class Assumption
	{
	}
	
	public class AssumptionSet
	{
		TypeAssumption tass;
		Set set;
//		AssumptionSet() { }
		AssumptionSet(Type t) { tass=intern(new TypeAssumption(ctx, t)); }
		AssumptionSet(TypeAssumption t) { tass=t; }
		AssumptionSet(AssumptionSet as) { tass=as.tass; set=(as.set==null)?null:new HashSet(as.set); }
		void add(Assumption ass) { if (set==null) { set=new HashSet(); } set.add(ass); }
		void addAll(AssumptionSet as) 
		{ 
			if (as.set != null) { 
				if (set==null)
					set = new HashSet();
				set.addAll(as.set);
			}
		}
		Set getAssumptionsOfClass(Class clazz)
		{
			HashSet result = new HashSet();
			if (set == null)
				return result;
			Iterator it = set.iterator();
			while (it.hasNext())
			{
				Assumption ass = (Assumption)it.next();
				if (clazz.isInstance(ass))
					result.add(ass);
			}
			return result;
		}
		Assumption getAssumption(Class clazz)
		{
			if (set == null)
				return null;
			Assumption bestass = null;
			Iterator it = set.iterator();
			while (it.hasNext())
			{
				Assumption ass = (Assumption)it.next();
				if (clazz.isInstance(ass))
				{
					if (bestass == null)
						bestass = ass;
					else
						throw new RuntimeException("Assumption set had more than one of " + clazz);
				}
			}
			return bestass;
		}
		public boolean equals(Object o)
		{
			if (!(o instanceof AssumptionSet))
				return false;
			AssumptionSet as = (AssumptionSet)o;
			return (tass.equals(as.tass) && (set==as.set || (set!=null && set.equals(as.set))));
		}
		public String toString()
		{
			StringBuffer st = new StringBuffer();
			st.append('[');
			st.append(tass);
			if (set != null)
			{
				Iterator it = set.iterator();
				while (it.hasNext())
				{
					st.append(' ');
					st.append(it.next().toString());
				}
			}
			st.append(']');
			return st.toString();
		}
		public int getLocalObjectIndex()
		{
			NewObjectAssumption noass = (NewObjectAssumption)getAssumption(NewObjectAssumption.class);
			return (noass != null) ? noass.objindex : -1;
		}
		
	}

	static Type mergeTypes(Type ta, Type tb)
	{
//		System.err.println("Merge " + ta.getClass() + typeToStr(ta) + " and " + tb.getClass() + typeToStr(tb));
		// if equal, return
		if (ta == tb || ta.equals(tb))
			return ta;
		boolean taref = ta instanceof ReferenceType;
		boolean tbref = tb instanceof ReferenceType;
		// todo: this merging sucks
		// if one is null, use the other
		if (ta == NULL_TYPE && tbref)
			return tb;
		else if (tb == NULL_TYPE && taref)
			return ta;
		// if both ref types, figure out if all good
		else if (taref && tbref) 
		{
			if (ta instanceof ArrayType && tb instanceof ArrayType)
			{
				return FJContext.getArrayType(mergeTypes( ((ArrayType)ta).getElementType(), ((ArrayType)tb).getElementType() ));
			} 
			else if (ta instanceof ObjectType && tb instanceof ObjectType) 
			{
				ObjectType oa = (ObjectType)ta;
				ObjectType ob = (ObjectType)tb;
				if ( Repository.instanceOf(oa.getClassName(), ob.getClassName()) )
					return ob;
				else if ( Repository.instanceOf(ob.getClassName(), oa.getClassName()) )
					return oa;
				else
					return mergeTypes( 
						FJContext.getObjectType( Repository.getSuperClasses(oa.getClassName())[0].getClassName() ),
						FJContext.getObjectType( Repository.getSuperClasses(ob.getClassName())[0].getClassName() )
					);
			}
			else if (ta instanceof ArrayType && tb.equals(Type.OBJECT))
				return tb;
			else if (tb instanceof ArrayType && ta.equals(Type.OBJECT))
				return ta;
		}
		return UNKNOWN_TYPE;
	}
	
	public static class TypeAssumption extends Assumption
	{
		FJContext ctx;
		Type type;
		boolean exact;
		TypeAssumption(FJContext ctx, Type t) { this.ctx=ctx; type=t; exact=isExactType(t); }
		TypeAssumption(FJContext ctx, Type t, boolean ex) { this(ctx,t); exact=ex; }
		public String toString() { return "type=" + typeToStr(type) + '/' + exact; }
		public boolean equals(Object o)
		{
			if (!(o instanceof TypeAssumption))
				return false;
			TypeAssumption ta = (TypeAssumption)o;
			return (ta.type.equals(type) && ta.exact==exact);
		}
		 // todo: lose this method
		public TypeAssumption merge(TypeAssumption ta)
		{
			Type mt = mergeTypes(type, ta.type);
			// exactness is preserved if both types are exact and both are equal
			boolean ex = exact && ta.exact && type.equals(ta.type);
			return intern(new TypeAssumption(ctx, mt, ex));
		}
		boolean isExactType(Type t)
		{
			//if (t instanceof BasicType || ((t==NULL_TYPE)||(t==ADDRESS_TYPE)))
			//	return true;
			if (t instanceof ArrayType && isExactType( ((ArrayType)t).getElementType() ))
				return true;
			if (t instanceof ObjectType && ctx.lookupFJClass( ((ObjectType)t).getClassName() ).jc.isFinal())
				return true;
			return false;
		}		
	}
	
	public static class HoldsParamAssumption extends Assumption
	{
		byte param;
		HoldsParamAssumption(int p) { param=(byte)p; }
		public String toString() { return "param=" + param; }
		public int hashCode() { return param; }
		public boolean equals(Object o)
		{
			return (o instanceof HoldsParamAssumption && ((HoldsParamAssumption)o).param==param);
		}
	}
	
	public static class ReturnAddressAssumption extends Assumption
	{
		InstructionHandle ih;
		ReturnAddressAssumption(InstructionHandle ih) { this.ih=ih; }
		public String toString() { return "rtnaddr=" + ih; }
		public int hashCode() { return ih.hashCode(); }
		public boolean equals(Object o)
		{
			return (o instanceof ReturnAddressAssumption && ((ReturnAddressAssumption)o).ih.equals(ih));
		}
	}
	
	public static class NewObjectAssumption extends Assumption
	{
		int objindex;
		public NewObjectAssumption(int objindex) { this.objindex = objindex; }
		public int hashCode() { return 99990; }
		public boolean equals(Object o) { return (o instanceof NewObjectAssumption) && ((NewObjectAssumption)o).objindex==objindex; }
		public String toString() { return "isnew="+objindex; }
	}
	
	public static class StoredAssumption extends Assumption
	{
		public int hashCode() { return 99991; }
		public boolean equals(Object o) { return (o instanceof StoredAssumption); }
		public String toString() { return "stored"; }
	}
	
	public static class LoadedAssumption extends Assumption
	{
		public int hashCode() { return 99992; }
		public boolean equals(Object o) { return (o instanceof LoadedAssumption); }
		public String toString() { return "loaded"; }
	}
	
	// todo: this sucks
	static final String typeToStr(Type t)
	{
		if (t == UNKNOWN_TYPE)
			return "<UNKNOWN>";
		else
			return t.toString();
	}
	
	class Frame
	{
		Frame parent;
		HashMap item_assums;
		int sp; // stack pointer
		InstructionHandle start;
		
		static final int MAX_ROOT_DIST = 8;

		Frame()
		{
			item_assums = new HashMap();
		}

		// param_assums is a mapping of LocalItems to TypeAssumptions
		// if a mapping does not exist for a param, assume the default
		void startMethod(Map param_assums)
		{
			// stick parameters on the locals array
			int n = 0;
			boolean statik = m.mg.isStatic();
			if (!statik)
			{
				Item local = intern(new LocalItem(n));
				if (param_assums != null && param_assums.get(new Integer(0)) != null)
					addAssumption( local, (TypeAssumption)intern(param_assums.get(new Integer(0))) );
				else
					addAssumption( local, intern(m.getParamInternalType(0)) );
				addAssumption( local, intern(new HoldsParamAssumption(0)) );
				if (debug) {
					System.err.println("Added this : " + m.mg.getClassName());
					System.err.println(getAssumptions(local));
				}
				n++; // ok because 'this' can't be long or double
			}
			Type[] mtypes = m.mg.getArgTypes();
			for (int i=0; i<mtypes.length; i++)
			{
				Item local = intern(new LocalItem(n));
				Type t = mtypes[i];
				int parami = i+(statik?0:1);
				if (param_assums != null && param_assums.get(new Integer(parami)) != null) {
					addAssumption( local, (TypeAssumption)intern(param_assums.get(new Integer(parami))) );
				} else {
					// convert boolean, etc params to int -- we never store booleans in locals
					addAssumption( local, m.getParamInternalType(parami) );
				}
				addAssumption( local, intern(new HoldsParamAssumption(parami)) );
				if (debug)
					System.err.println("Added local " + i + " @ " + n + " : " + mtypes[i]);
				n += t.getSize();
			}
		}

		Frame(Frame f, InstructionHandle ih)
		{
			if (true || f.getRootDist() > MAX_ROOT_DIST) {
				item_assums = (HashMap)f.item_assums.clone();
				parent = null;
			} else {
				item_assums = new HashMap();
				parent = f;
			}
			sp = f.sp;
			start = ih;
		}
		
		Set getAllItems()
		{
			Frame f = this;
			HashSet set = new HashSet();
			while (f != null)
			{
				set.addAll(f.item_assums.keySet());
				f = f.parent;
			}
			return set;
		}
		
		void addAssumption(Item item, Assumption ass)
		{
			AssumptionSet as = (AssumptionSet)item_assums.get(item);
			if (as == null)
			{
				AssumptionSet paras = null;
				if (parent != null)
					paras = parent.getAssumptions(item);
				if (paras == null)
					as = new AssumptionSet((TypeAssumption)ass);	// first assumption must be type
				else
					as = new AssumptionSet(paras);
				item_assums.put(item, as);
				if (ass instanceof TypeAssumption)
				{
					as.tass = (TypeAssumption)ass;
					return;
				}
			} else {
				if (ass instanceof TypeAssumption)
					throw new RuntimeException("Type already set in " + as);
			}
			as.add(ass);
			if (debug)
				System.err.println("item " + item + " -> " + as);
		}
		
		AssumptionSet getAssumptions(Item item)
		{
			AssumptionSet as;
			Frame f = this;
			while (f != null)
			{
				as = (AssumptionSet)f.item_assums.get(item);
				//System.err.println("checked " + f + " for " + item + " got " + as);
				if (as != null)
					return as;
				f = f.parent;
			}
			return null;
		}
		
		void setAssumptions(Item item, AssumptionSet assums)
		{
			item_assums.put(item, assums);
		}
		
		AssumptionSet getLocal(int i)
		{
			return getAssumptions(new LocalItem(i));
		}
		
		AssumptionSet pushStack(Type t)
		{
			AssumptionSet as = new AssumptionSet(t);
			pushStack(as);
			return as;
		}
		
		Type type2internaltype(Type t)
		{
			if (t.equals(Type.BYTE) || t.equals(Type.CHAR) || t.equals(Type.SHORT) || t.equals(Type.BOOLEAN))
				return Type.INT;
			else
				return t;
		}
		
		AssumptionSet pushStack(AssumptionSet as)
		{
			Type t = type2internaltype(as.tass.type);
			if (t != as.tass.type)
			{
				as = new AssumptionSet(as);
				as.tass = intern(new TypeAssumption(ctx, t)); //todo: dangerous
			}
			if (debug)
				System.err.println("Push " + as);
			pushStackRaw(as);
			if (t.getSize() == 2)
				pushStackRaw(new AssumptionSet(Type.VOID));
			//sp += t.getSize()-1;
			return as;
		}
		
		void pushStackRaw(AssumptionSet as)
		{
			setAssumptions(intern(new StackItem(sp)), as);
			sp++;
		}
		
		void pushStackInt(Type t, int value)
		{
			pushStack(t);
			//todo: use 'value'
		}
		
		// todo: handle 2-word items in these methods
		
		void pushStackFromLocal(Type t, int localidx)
		{
			AssumptionSet localt = getLocal(localidx);
			if (localt == null)
				throw new RuntimeException("Local " + localidx + " not assigned");
			// todo: booleans break
			if (!isAssignable(t, localt.tass.type))
			{
				throw new RuntimeException("Type mismatch (got " + localt + ", expected " + t + ")");
			}
			//localt = new AssumptionSet(localt);
			//localt.add(new LoadedAssumption());
			pushStack(localt);
		}
		
		// todo: check for stack underflow
		
		AssumptionSet getStack(int i)
		{
			if (i < 0)
				throw new RuntimeException("Stack underflow");
			if (i >= sp)
				return null;
			return getAssumptions(new StackItem(i));
		}
		
		AssumptionSet peekStack()
		{
			return getStack(sp-1);
		}
		
		AssumptionSet popStackRaw()
		{
			AssumptionSet as = peekStack();
			sp--;
			return as;
		}
		
		// can we do "a == b"
		// or can we assign a field of type 'a' with a variable of type 'b' from stack
		boolean isAssignable(Type a, Type b)
		{
			if (a.equals(b))
				return true;
			if (a instanceof ArrayType && b instanceof ArrayType)
			{
				if (debug)
					System.err.println("ARRAY " + a + " " + b);
				return isAssignable( ((ArrayType)a).getElementType(), ((ArrayType)b).getElementType() );
				//System.err.println("ARRAY " + ((ArrayType)a).getDimensions() + " " + ((ArrayType)b).getDimensions());
				//System.err.println("ARRAY " + ((ArrayType)a).getBasicType() + " " + ((ArrayType)b).getBasicType());
			}
			if ((b.equals(Type.NULL) || b.equals(NULL_TYPE)) && a instanceof ReferenceType)
				return true;
			if ((a.equals(Type.NULL) || a.equals(NULL_TYPE)) && b instanceof ReferenceType)
				return true;
			// todo: just a bit presumptious?
			if (a instanceof BasicType && b instanceof BasicType && a.getSize() == b.getSize())
				return true;
			/*
			if ((a.equals(Type.BOOLEAN) || a.equals(Type.BYTE) || a.equals(Type.CHAR) || a.equals(Type.SHORT))
				 && (b.equals(Type.INT)))
				return true;
			if (a.equals(Type.BYTE) && b.equals(Type.BOOLEAN))
				return true;
			*/
//			System.err.println(a.getClass() + " " + b.getClass());
			if (a.equals(Type.OBJECT) && b instanceof ReferenceType)
				return true;
			if (a instanceof ObjectType &&
				b instanceof ObjectType &&
				Repository.instanceOf(((ObjectType)b).getClassName(), ((ObjectType)a).getClassName()))
				return true;
			return false;
		}
		
		AssumptionSet consumeStack(Type t)
		{
			// todo check for void here and in pushStack
			sp -= (t.getSize()-1);
			AssumptionSet as = popStackRaw();
			if (debug)
				System.err.println("Consume " + as);
			if (!isAssignable(t, as.tass.type))
			{
				dumpStack();
				System.err.println("t's class: " + t.getClass());
				System.err.println("as's class: " + as.tass.type.getClass());
				System.err.println("*"+as.tass.type+"*");
				System.err.println("*"+t+"*");
				System.err.println(t.equals(as.tass.type));
				throw new RuntimeException("Type mismatch (got " + as + ", expected " + t + ")");
			}
			return as;
		}
		
		void consumeStackToLocal(Type t, int localidx)
		{
			// consume an item off the stack and store it in the locals
			AssumptionSet localt = consumeStack(t);
			// log a use of this variable
			//localuses.add(new LocalUse(t, localidx, start));
			//localt = new AssumptionSet(localt);
			//localt.add(new StoredAssumption());
			setAssumptions(intern(new LocalItem(localidx)), localt);
		}
		
		AssumptionSet consumeStackArray(Type t)
		{
			Type t2 = FJContext.getArrayType(t);
			AssumptionSet as = consumeStack(t2);
			if (as.tass.type instanceof ArrayType)
			{
				return new AssumptionSet(((ArrayType)as.tass.type).getElementType());
			} else
				return as;
		}
		
		void dumpStack()
		{
			if (!debug)
				return;
			Set s = item_assums.keySet();
			Iterator it = s.iterator();
			while (it.hasNext())
			{
				Item item = (Item)it.next();
				AssumptionSet as = getAssumptions(item);
				System.err.println(item + " -> " + as);
			}
		}
		
		void registerNewObject(AssumptionSet as)
		{
			int pos = start.getPosition();
			if (localobjset.get(pos) == Boolean.FALSE)
				return;
			localobjset.put(pos, as);
			Assumption noass = intern(new NewObjectAssumption(pos));
			as.add(noass);
		}
		
		/*
		 * Returns 'true' if we don't need to merge w/ next instruction
		 */
		boolean processInsn(InstructionHandle ih, Instruction insn)
		{
			Type t;
			AssumptionSet as;
			int tag = insn.getTag();

			switch (tag)
			{
				case NOP:
					break;
					
				case ACONST_NULL:
					pushStack(NULL_TYPE);
					break;
					
				case ICONST_M1:
				case ICONST_0:
				case ICONST_1:
				case ICONST_2:
				case ICONST_3:
				case ICONST_4:
				case ICONST_5:
					pushStackInt(Type.INT, tag-ICONST_0);
					break;
					
				// todo: add values to all these
					
				case BIPUSH:
					pushStack(Type.INT);
					break;
					
				case SIPUSH:
					pushStack(Type.SHORT);
					break;
				
				case LCONST_0:
				case LCONST_1:
					pushStackInt(Type.LONG, tag-LCONST_0);
					break;
					
				case FCONST_0:
				case FCONST_1:
				case FCONST_2:
					pushStack(Type.FLOAT);
					break;
					
				case DCONST_0:
				case DCONST_1:
					pushStack(Type.DOUBLE);
					break;
					
				case LDC:
				case LDC_W:
				case LDC2_W:
					t = ((CPInstruction)insn).getType(cpg);
					pushStack(t);
					break;

				case ILOAD:				
				case ILOAD_0:
				case ILOAD_1:
				case ILOAD_2:
				case ILOAD_3:
					pushStackFromLocal(Type.INT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case LLOAD:				
				case LLOAD_0:
				case LLOAD_1:
				case LLOAD_2:
				case LLOAD_3:
					pushStackFromLocal(Type.LONG, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case FLOAD:				
				case FLOAD_0:
				case FLOAD_1:
				case FLOAD_2:
				case FLOAD_3:
					pushStackFromLocal(Type.FLOAT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case DLOAD:				
				case DLOAD_0:
				case DLOAD_1:
				case DLOAD_2:
				case DLOAD_3:
					pushStackFromLocal(Type.DOUBLE, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case ALOAD:	
				case ALOAD_0:
				case ALOAD_1:
				case ALOAD_2:
				case ALOAD_3:
					pushStackFromLocal(Type.OBJECT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case ISTORE:
				case ISTORE_0:
				case ISTORE_1:
				case ISTORE_2:
				case ISTORE_3:
					consumeStackToLocal(Type.INT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case LSTORE:
				case LSTORE_0:
				case LSTORE_1:
				case LSTORE_2:
				case LSTORE_3:
					consumeStackToLocal(Type.LONG, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case ASTORE:
				case ASTORE_0:
				case ASTORE_1:
				case ASTORE_2:
				case ASTORE_3:
					consumeStackToLocal(Type.OBJECT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case FSTORE:
				case FSTORE_0:
				case FSTORE_1:
				case FSTORE_2:
				case FSTORE_3:
					consumeStackToLocal(Type.FLOAT, ((LocalVariableInstruction)insn).getIndex());
					break;
					
				case DSTORE:
				case DSTORE_0:
				case DSTORE_1:
				case DSTORE_2:
				case DSTORE_3:
					consumeStackToLocal(Type.DOUBLE, ((LocalVariableInstruction)insn).getIndex());
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
            	consumeStack(Type.INT); // index
            	t = ((ArrayInstruction)insn).getArrayType();
            	AssumptionSet at = consumeStackArray(t);
            	at = new AssumptionSet(at);
            	at.add(intern(new LoadedAssumption()));
            	pushStack(at);
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
            	as = consumeStack(t); // value
            	if (tag == AASTORE)
	            	markValueStored(as);
            	consumeStack(Type.INT); // index
            	consumeStackArray(t);	// array
            	}
            	break;
            	
            // stack ops

				case POP2:
					popStackRaw();            	
            case POP:
            	popStackRaw();
            	break;
            	
            case DUP:
            	pushStackRaw(peekStack());	//todo: check for 32-bit value
            	break;
            	
            case DUP_X1:
            	{
            		AssumptionSet t1 = popStackRaw(); // todo: check for 32-bit value
            		AssumptionSet t2 = popStackRaw();
            		pushStackRaw(t1);
            		pushStackRaw(t2);
            		pushStackRaw(t1);
            	}
            	break;
            	
            case DUP_X2:
            	{
            		AssumptionSet t1 = popStackRaw();	// todo: check for 32-bit value  
            		AssumptionSet t2 = popStackRaw();
            		AssumptionSet t3 = popStackRaw();
            		pushStackRaw(t1);
            		pushStackRaw(t3);
            		pushStackRaw(t2);
            		pushStackRaw(t1);
            	}
            	break;
            	
				case DUP2:
					{
            		AssumptionSet t1 = popStackRaw();	// todo: check for 32-bit or 64-bit value  
            		AssumptionSet t2 = popStackRaw();
            		pushStackRaw(t2);
            		pushStackRaw(t1);
            		pushStackRaw(t2);
            		pushStackRaw(t1);
					}
					break;
					
				case DUP2_X1:
					{
            		AssumptionSet t1 = popStackRaw();	// todo: check for 32-bit or 64-bit value  
            		AssumptionSet t2 = popStackRaw();
            		AssumptionSet t3 = popStackRaw();
            		pushStackRaw(t2);
            		pushStackRaw(t1);
            		pushStackRaw(t3);
            		pushStackRaw(t2);
            		pushStackRaw(t1);
					}
					break;
					
				case DUP2_X2:
					{
            		AssumptionSet t1 = popStackRaw();	// todo: check for 32-bit or 64-bit value  
            		AssumptionSet t2 = popStackRaw();
            		AssumptionSet t3 = popStackRaw();
            		AssumptionSet t4 = popStackRaw();
            		pushStackRaw(t2);
            		pushStackRaw(t1);
            		pushStackRaw(t4);
            		pushStackRaw(t3);
            		pushStackRaw(t2);
            		pushStackRaw(t1);
					}
					break;
				
				case SWAP:
					{
            		AssumptionSet t1 = popStackRaw();	// todo: check for 32-bit or 64-bit value  
            		AssumptionSet t2 = popStackRaw();
            		pushStackRaw(t1);
            		pushStackRaw(t2);
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
				case INEG:
				case LNEG:
				case FNEG:
				case DNEG:
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
						for (int i=1; i<types.length; i++)
							consumeStack(types[i]);
						pushStack(types[0]);
					}
					break;
					
				case IINC:
					// todo: check type of local var
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
					consumeStack(((ConversionInstruction)insn).getConsumeType());
					pushStack(((ConversionInstruction)insn).getProduceType());
					break;
					
				// compare insns
				
				case LCMP:
					consumeStack(Type.LONG);
					consumeStack(Type.LONG);
					pushStack(Type.INT);
					break;
					
				case FCMPL:
				case FCMPG:
					consumeStack(Type.FLOAT);
					consumeStack(Type.FLOAT);
					pushStack(Type.INT);
					break;
					
				case DCMPL:
				case DCMPG:
					consumeStack(Type.DOUBLE);
					consumeStack(Type.DOUBLE);
					pushStack(Type.INT);
					break;
					
				// if instructions
				
				case IFEQ:
		      case IFNE:
            case IFLT:
            case IFGE:
            case IFGT:
            case IFLE:
            	consumeStack(Type.INT);
            	merge( ((BranchInstruction)insn).getTarget() );
            	return false;

		      case IF_ICMPEQ:
		      case IF_ICMPNE:
      		case IF_ICMPLT:
		      case IF_ICMPGT:
      		case IF_ICMPGE:
		      case IF_ICMPLE:
            	consumeStack(Type.INT);
            	consumeStack(Type.INT);
            	merge( ((BranchInstruction)insn).getTarget() );
            	return false;
            	
            case IF_ACMPEQ:
            case IF_ACMPNE:
            	consumeStack(Type.OBJECT);
            	consumeStack(Type.OBJECT);
            	merge( ((BranchInstruction)insn).getTarget() );
            	return false;
            	
				case IFNULL:
				case IFNONNULL:
					consumeStack(Type.OBJECT);
					merge( ((BranchInstruction)insn).getTarget() );
					break;
					
           // goto insns
            	
				case GOTO:
				case GOTO_W:
					merge( ((BranchInstruction)insn).getTarget() );
					return true;
					
				case JSR:
				case JSR_W:
					// todo: support nested finally's
					as = new AssumptionSet(ADDRESS_TYPE);
					// add attribute designating this is a return address
					as.add(new ReturnAddressAssumption(ih.getNext()));
					pushStack(as);
					// special merge
					merge( ((BranchInstruction)insn).getTarget() );
					return true;
					
				// return functions
					
				case RET:
					{
					as = getLocal( ((RET)insn).getIndex() );
					Set set = as.getAssumptionsOfClass(ReturnAddressAssumption.class);
					if (set.isEmpty())
						throw new RuntimeException("RET needs to return to an address, not " + as);
					Iterator it = set.iterator();
					// todo: not have to return to all of them?
					while (it.hasNext())
					{
						ReturnAddressAssumption ras = (ReturnAddressAssumption)it.next();
						mergeRET( ras.ih );	 //todo???
					}
					return true;
					}
					
				case ARETURN:
				case IRETURN:
				case LRETURN:
				case FRETURN:
				case DRETURN:
					as = consumeStack( ((ReturnInstruction)insn).getType() );
					if (tag == ARETURN)
					  	markValueReturned(as);
					if (returntype == null)
						returntype = new AssumptionSet(as);
					else
						returntype = merge(returntype, as);
				case RETURN:
					return true;
					
				// switch functions
					
				case LOOKUPSWITCH:
				case TABLESWITCH:
					{
						consumeStack(Type.INT);
						// consume the default case
						merge( ((Select)insn).getTarget() );
						// consume all case targets
						InstructionHandle[] targets = ((Select)insn).getTargets();
						for (int i=0; i<targets.length; i++)
							merge( targets[i] );
						return true;
					}
					
				// get & put field functions
					
				case GETFIELD:
					consumeStack(Type.OBJECT);
				case GETSTATIC:
					t = ((FieldInstruction)insn).getType(cpg);
					as = new AssumptionSet(t);
					as.add(intern(new LoadedAssumption()));
					pushStack(as);
					break;
					
				case PUTFIELD:
					t = ((FieldInstruction)insn).getType(cpg);
					as = consumeStack(t);
				  	markValueStored(as);
					consumeStack(Type.OBJECT);
					break;
				
				case PUTSTATIC:
					t = ((FieldInstruction)insn).getType(cpg);
					as = consumeStack(t);
					markValueStored(as);
					break;
					
				// invoke functions
				
				case INVOKEINTERFACE:
				case INVOKESPECIAL:
				case INVOKESTATIC:
				case INVOKEVIRTUAL:
					{
						boolean doInfo = false; // assume we won't need to recurse into the called function
						InvokeInstruction invk = (InvokeInstruction)insn;
						boolean statik = (tag==INVOKESTATIC);
						int argbias = statik?0:1;
						if (debug)
							System.err.println("Invoke " + invk.getMethodName(cpg) + " : " + 
								invk.getType(cpg) + ", " + invk.getReturnType(cpg));
						// consume the arguments from the stack
						Type[] types = invk.getArgumentTypes(cpg);
						AssumptionSet[] args = new AssumptionSet[types.length+argbias];
						for (int i=types.length-1; i>=0; i--)
						{
							args[i+argbias] = consumeStack(types[i]);
							// if argument is not a ref type, we may have to recurse
							if (!doInfo && args[i+argbias].tass.type instanceof ReferenceType)
								doInfo = true;
							//System.err.println(i + " -> " + args[i+argbias]);
						}
						// consume the object ref
						if (!statik)
						{
							args[0] = consumeStack(invk.getClassType(cpg));
							doInfo = true; // if not static, we may have to recurse
							if (args[0].tass.type == NULL_TYPE)
								System.err.println("*** WARNING: invoking NULL in " + invk);
						}
						// if not optimizing, don't get info
						if (!recurse_method_calls)
							doInfo = false;
						// now lookup the method; we'll need some info about it
						String invoked_cname = invk.getClassName(cpg);
						// if we know the exact type of object being called, use its class name 
						if ((tag == INVOKEVIRTUAL || tag == INVOKEINTERFACE) && args[0].tass.exact
							&& (args[0].tass.type instanceof ObjectType))
							invoked_cname = ((ObjectType)args[0].tass.type).getClassName();
						FJClass invoked_class = ctx.lookupFJClass(invoked_cname);
						if (invoked_class == null)
							throw new RuntimeException("Class " + invoked_cname + " not found");
						FJMethod invoked_method = invoked_class.findMethod(invk.getMethodName(cpg), invk.getSignature(cpg));
						if (invoked_method == null)
							throw new RuntimeException("Method " + invoked_cname + "." + invk.getMethodName(cpg) + " " +
								invk.getSignature(cpg) + " not found");
						// now we got the method; try to introspect it (like we do here)
						FJMethodInfo minfo = null;
						try {
							boolean fini = (tag == INVOKESPECIAL || tag == INVOKESTATIC);
							// if we know the type of object -- if the call is static,
							// or the type is marked as exact -- we can do the analysis
							//System.err.println(doInfo + " - " + fini);
							if (doInfo && (fini || args[0].tass.exact))
								minfo = invoked_method.getInfo(getInfoMap(args, invoked_method));
						// if we caught this exception, assume the worst
						} catch (RecursiveIntrospectionException rie) {
							System.err.println("Recursion detected in method " + invoked_method);
						}
						// now find out params were migrated as a result of this madness
						for (int i=0; i<args.length; i++)
						{
							// if we couldn't get the method info, just mark everything as stored
							if (minfo == null || minfo.params_migrated.get(i))
								markValueStored(args[i]);
						}
						// process exceptions
						// todo: see if return type was migrated
						t = invk.getReturnType(cpg);
						if (!Type.VOID.equals(t))
						{
							// push the return type
							// if we have the method info, we can find out the exact type
							if (minfo != null && minfo.returntype != null)
								pushStack(new AssumptionSet(minfo.returntype.tass));	// todo: should we use rest of returntype?
							else
								pushStack(t);
						}
						minfo = null; // just in case
					}
					break;
					
				// allocation insns
					
				case NEW:
					as = new AssumptionSet( 
						intern(new TypeAssumption(ctx, ((NEW)insn).getType(cpg), true )) ); // it is exact
					registerNewObject(as);
					pushStack(as);
					break;
					
				case NEWARRAY:
					as = new AssumptionSet(
						intern(new TypeAssumption(ctx, ((NEWARRAY)insn).getType(), true )) ); // also exact
					registerNewObject(as);
					consumeStack(Type.INT);
					pushStack(as);
					break;
					
				case ANEWARRAY:
					as = new AssumptionSet(
						intern(new TypeAssumption(ctx, FJContext.getArrayType( ((ANEWARRAY)insn).getType(cpg) ), true )) ); // exact
					registerNewObject(as);
					consumeStack(Type.INT);
					pushStack(as);
					break;
					
				case MULTIANEWARRAY:
					{	
						MULTIANEWARRAY mana = (MULTIANEWARRAY)insn;
						t = mana.getType(cpg);
						as = new AssumptionSet(	intern(new TypeAssumption(ctx, t, true )) ); // exact
						registerNewObject(as);
						//System.err.println("ARRAY TYPE = " + t + ", dimensions = " + mana.getDimensions());
						for (int i=mana.getDimensions(); i>0; i--)
						{
							consumeStack(Type.INT);
						}
						pushStack(as);
					}
					break;
					
				// misc insns
					
				case ARRAYLENGTH:
					consumeStack(Type.OBJECT);
					pushStack(Type.INT);
					break;
					
				case ATHROW:
					as = consumeStack(Type.OBJECT);
				  	markValueStored(as);
					return true;
					
				case CHECKCAST:
					consumeStack(Type.OBJECT);
					pushStack( ((CHECKCAST)insn).getType(cpg) );
					break;
					
				case INSTANCEOF:
					consumeStack(Type.OBJECT);
					pushStack(Type.INT);
					break;
					
				case MONITORENTER:
				case MONITOREXIT:
					consumeStack(Type.OBJECT);
					break;
					
				default:
					throw new RuntimeException("*** UNRECOGNIZED INSTRUCTION " + insn);
			}
			return false;
		}
		
		void doInstruction()
		{
			if (debug)
				System.err.println("(" + sp + ") " + start);
			InstructionHandle ih = start;
			Frame f = new Frame(this, ih);
			Instruction insn = start.getInstruction(); 
			boolean b = processInsn(ih, insn);
			if (insn instanceof ExceptionThrower)
			{
				// todo: is this right?
				CodeExceptionGen[] cegs = m.mg.getExceptionHandlers();
				for (int i=0; i<cegs.length; i++)
				{
					if (debug)
						System.err.println("Want to merge with " + cegs[i]);
					int pos = ih.getPosition();
					if (pos >= cegs[i].getStartPC().getPosition() &&
						pos <= cegs[i].getEndPC().getPosition())
					{
						mergeException(cegs[i]);
					}
				}
			}
			if (b)
				return;
			else {
				ih = ih.getNext();
				if (ih == null)
					throw new RuntimeException("Fell off end of method");
				merge(ih);
			}
		}
		
		void merge(InstructionHandle ih)
		{
			pushFrame(new Frame(this, ih));
		}
		
		void mergeRET(InstructionHandle ih)
		{
			Frame f = new Frame(this, ih);
			f.parent = null;
			// get rid of all unknowns
			Set itemset = getAllItems();
			Iterator it = itemset.iterator();
			while (it.hasNext())
			{
				Item item = (Item)it.next();
				AssumptionSet as = getAssumptions(item);
//				System.err.println("RET " + item + ", " + as);
				if (as.tass.type != UNKNOWN_TYPE)
				{
					f.setAssumptions(item, as);
				}
//					System.err.println("RET removed item " + item + ", " + as);
			}
			pushFrame(f);
		}
		
		void mergeException(CodeExceptionGen ceg)
		{
			InstructionHandle ih = ceg.getHandlerPC();
			Frame f = new Frame(this, ih);
			f.sp = 0;
			// exception handlers like only 1 object on stack : a Throwable
			// basselopes, on the other hand, like Pop Tarts.  And cold showers.
			Type catchtype = ceg.getCatchType();
			if (catchtype == null)
				catchtype = ctx.getObjectType("java.lang.Throwable");
			f.pushStack(catchtype);
			pushFrame(f);
		}
		
		AssumptionSet merge(AssumptionSet a, AssumptionSet b)
		{
			AssumptionSet res = new AssumptionSet((TypeAssumption)a.tass.merge(b.tass));
			if (res.tass.type == UNKNOWN_TYPE)
				return res;
			res.addAll(a);
			res.addAll(b);
			return res;
		}
		
		// distance to root
		int getRootDist()
		{
			int d = 0;
			Frame f = parent;
			while (f != null)
			{
				d++;
				f = f.parent;
			}
			return d;
		}
		
		boolean merge(Frame mf)
		{
			/*
			int framedist = getRootDist();
			boolean keyframe = (framedist >= 8);
			if (debug && keyframe)
				System.err.println("Root dist = " + framedist);
			*/
				
			// check stack sizes
			if (mf.sp != this.sp)
				throw new RuntimeException("Stack mismatch: " + this.sp + " vs " + mf.sp);
			// get all the items that are to be merged
			Set my_items = this.getAllItems();
			Set mf_items = mf.getAllItems();
			Set items = new HashSet(my_items);
			items.addAll(mf_items);
			// iterate thru the items
			boolean changed = false;
			Iterator it = items.iterator();
			while (it.hasNext())
			{
				Item item = (Item)it.next();
				AssumptionSet a = this.getAssumptions(item);
				AssumptionSet b = mf.getAssumptions(item);
				if (a == null && b != null)
				{
					this.setAssumptions(item, new AssumptionSet(b));
					changed = true;
				} 
				else if (a != null && b != null) 
				{
					// gotta merge
					if (a != b && !a.equals(b))
					{
						if (debug)
							System.err.print(item + " : " + a + " + " + b + " = ");
						AssumptionSet merged = merge(a, b);
						this.setAssumptions(item, merged);
						if (debug)
							System.err.println(merged);
						changed = true;
					} else {
						//if (keyframe)
						//	this.setAssumptions(item, a);
						if (debug)
							System.err.println(item + " : " + a + " == " + b);
					}
				}
			}
			// todo: why does this not work?
			//if (keyframe)
			//	parent = null;
			return changed;
		}
		
		public AssumptionSet getStackInfo(int sptr)
		{
			return getAssumptions(new StackItem(sptr));
		}

		public AssumptionSet getLocalInfo(int sptr)
		{
			return getAssumptions(new LocalItem(sptr));
		}
		
		public String toString()
		{
			return "FRAME " + start;
		}

	}
	
	FJMethodInfo(FJMethod m)
	{
		this(m, null);
	}
	
	FJMethodInfo(FJMethod m, Map param_assums)
	{
		this.m = m;
		this.ctx = m.c.ctx;
		this.cpg = m.c.jcg.getConstantPool();
		InstructionList il = m.mg.getInstructionList();
		if (il == null)
			return;
		// define the initial frame
		startf = new Frame();
		startf.startMethod(param_assums);
		// derive a frame at the 1st instruction and
		// add to frame list
		Frame f = new Frame(startf, il.getStart());
		pushFrame(f);
		// while we still got frames to pop, pop 'em off
		while (!framelist.isEmpty())
		{
			f = (Frame)framelist.pop();
			f = new Frame(f, f.start);
			f.doInstruction();
		}
		if (debug)
		{
			System.err.println("Method " + m.m.getName() + " migrates " +
				params_migrated + ", returns " + params_returned + ", locals are " + localobjset);
			System.err.println("Return type is " + returntype);
		}
	}
	
	void markParamValues(AssumptionSet as, BitSet bitset)
	{
		Set set = as.getAssumptionsOfClass(HoldsParamAssumption.class);
		Iterator it = set.iterator();
		while (it.hasNext())
		{
			HoldsParamAssumption hpa = (HoldsParamAssumption)it.next();
			bitset.set(hpa.param);
		}
	}
	
	void removeFromLocalSet(AssumptionSet as)
	{
		// if new object, this means it's no longer "local"
		Set set = as.getAssumptionsOfClass(NewObjectAssumption.class);
		Iterator it = set.iterator();
		while (it.hasNext())
		{
			NewObjectAssumption noa = (NewObjectAssumption)it.next();
			localobjset.put(noa.objindex, Boolean.FALSE);
			if (debug)
				System.err.println("Removing object " + noa.objindex + " from the local set");
		}
	}
	
	void markValueStored(AssumptionSet as)
	{
		removeFromLocalSet(as);
		// if it's a reference type, mark it as stored
		if (as.tass.type instanceof ReferenceType)
		{
			markParamValues(as, params_migrated);
			as.add(intern(new StoredAssumption())); // todo: don't mutate it!!!!
		}
	}
	
	void markValueReturned(AssumptionSet as)
	{
		removeFromLocalSet(as);
		if (as.tass.type instanceof ReferenceType)
		{
			markParamValues(as, params_returned);
		}
	}
	
	boolean isLocalObject(int objindex)
	{
		return (localobjset.get(objindex) instanceof AssumptionSet);
	}
		
	Frame getFrame(InstructionHandle ih)
	{
		return (Frame)bblocks.get(ih);
	}
	
	void pushFrame(Frame f)
	{
		Frame mf = getFrame(f.start);
		if (mf != null)
		{
			if (debug)
				System.err.println("Merging with " + mf + " at " + f.start);
			// merge -- if no change, return
			if (!f.merge(mf))
			{
				if (debug)
					System.err.println("Frame not changed");
				return;
			}
		}
		bblocks.put(f.start, f);
		framelist.push(f);
	}

	// fill the Map with (Local-Type) entries
	// for passing to getInfo()
	// todo: wasteful
	public Map getInfoMap(AssumptionSet[] args, FJMethod method)
	throws RecursiveIntrospectionException
	{
		// we know this method is OK --right?
		if (method.mg.getMethodName().equals("<init>") && 
			method.c.jc.getClassName().equals("java.lang.Object"))
			return null;
		// create a map where the arguments would be other than defaults
		Map map = null;
		for (int i=0; i<args.length; i++)
		{
			TypeAssumption t1 = args[i].tass;
			TypeAssumption t2 = method.getParamInternalType(i);
			//System.err.println(i + ", " + t1 + ", " +t2);
			if (t1.type instanceof ObjectType && !t2.equals(t1))
			{
				if (map == null)
					map = new HashMap();
				map.put(new Integer(i), args[i].tass);
			}
		}
		return map;
	}
	
}

