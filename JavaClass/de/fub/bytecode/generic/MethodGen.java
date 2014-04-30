package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;
import de.fub.bytecode.classfile.*;
import java.util.*;

/** 
 * Template class for building up a method. This is done by defining exception
 * handlers, adding thrown exceptions, local variables and attributes, whereas
 * the `LocalVariableTable' and `LineNumberTable' attributes will be set
 * automatically for the code.
 *
 * While generating code it may be necessary to insert NOP operations. You can
 * use the `removeNOPs' method to get rid off them.
 * The resulting method object can be obtained via the `getMethod()' method.
 *
 * @version $Id: MethodGen.java,v 1.1.1.1 2000/01/24 16:46:57 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @author  <A HREF="http://www.vmeng.com/beard">Patrick C. Beard</A>
 * @see     InstructionList
 * @see     Method
 */
public class MethodGen extends AccessFlags implements Constants {
  private String          method_name;
  private String          class_name;
  private Type            return_type;
  private Type[]          arg_types;
  private String[]        arg_names;
  private int             max_locals;
  private int             max_stack;
  private InstructionList il;
  private ConstantPoolGen cp;
  private boolean         strip_attributes;

  private Vector          variable_vec    = new Vector();
  private Vector          line_number_vec = new Vector();
  private Vector          attribute_vec   = new Vector();
  private Vector          exception_vec   = new Vector();
  private Vector          throws_vec      = new Vector();
  private Vector          code_attrs_vec  = new Vector();

  /**
   * Declare method. If the method is non-static the constructor
   * automatically declares a local variable `$this' in slot 0. The
   * actual code is contained in the `il' parameter, which may further
   * manipulated by the user. But he must take care not to remove any
   * instruction (handles) that are still referenced from this object.
   *
   * For example one may not add a local variable and later remove the
   * instructions it refers to without causing havoc. It is safe
   * however if you remove that local variable, too.
   *
   * @param access_flags access qualifiers
   * @param return_type  method type
   * @param arg_types argument types
   * @param arg_names argument names (if this is null, default names will be provided
   * for them)
   * @param method_name name of method
   * @param class_name class name containing this method (may be null, if you don't care)
   * @param il instruction list associated with this method, may be null only for
   * abstract or native methods
   * @param cp constant pool
   */
  public MethodGen(int access_flags, Type return_type, Type[] arg_types,
		   String[] arg_names, String method_name, String class_name,
		   InstructionList il, ConstantPoolGen cp) {
    this.access_flags = access_flags;
    this.return_type  = return_type;
    this.arg_types    = arg_types;
    this.arg_names    = arg_names;
    this.method_name  = method_name;
    this.class_name   = class_name;
    this.il           = il;
    this.cp           = cp;

    if((access_flags & (ACC_ABSTRACT | ACC_NATIVE)) == 0) {
      InstructionHandle start = il.getStart();
      InstructionHandle end   = il.getEnd();

      /* Add local variables, namely the implicit `this' and the arguments
       */
      if(!isStatic() && (class_name != null)) // Instance method -> `this' is local var 0
	addLocalVariable("this", new ObjectType(class_name), start, end); // Valid from start to end
    
      if(arg_types != null) {
	int size = arg_types.length;
	
	if(arg_names != null) { // Names for variables provided?
	  if(size != arg_names.length)
	    throw new ClassGenException("Mismatch in argument array lengths: " +
				      size + " vs. " + arg_names.length);
	} else { // Give them dummy names
	  arg_names = new String[size];

	  for(int i=0; i < size; i++)
	    arg_names[i] = "arg" + i;
	}

	for(int i=0; i < size; i++)
	  addLocalVariable(arg_names[i], arg_types[i], start, end);
      }
    }
  }

  /**
   * Instantiate from existing method.
   *
   * @param m method
   * @param class_name class name containing this method
   * @param cp constant pool (must contain the same entries as the method's constant pool)
   */
  public MethodGen(Method m, String class_name, ConstantPoolGen cp) {
    this(m.getAccessFlags(), Type.getReturnType(m.getSignature()),
	 Type.getArgumentTypes(m.getSignature()), null /* may be overridden anyway */,
	 m.getName(), class_name,
	 ((m.getAccessFlags() & (ACC_ABSTRACT | ACC_NATIVE)) == 0)?
	 new InstructionList(m.getCode().getCode()) : null,
	 cp);

    Attribute[] attributes = m.getAttributes();
    for(int i=0; i < attributes.length; i++) {
      Attribute a = attributes[i];

      if(a instanceof Code) {
	Code c = (Code)a;
	setMaxStack(c.getMaxStack());
	setMaxLocals(c.getMaxLocals());
	
	CodeException[] ces = c.getExceptionTable();
	
	if(ces != null) {
	  for(int j=0; j < ces.length; j++) {
            CodeException ce     = ces[j];
            int           type   = ce.getCatchType();
            ObjectType    c_type = null;
	    
	    if(type > 0) // contains bla/blubb, but ObjectType() doesn't care
	      c_type = new ObjectType(cp.getConstantPool().getConstantString(type, CONSTANT_Class));
	    int end_pc = ce.getEndPC();
	    int length = m.getCode().getCode().length;
	    
	    if(length == end_pc) // May happen, because end_pc is exclusive
	      end_pc = end_pc - il.getEnd().getInstruction().getLength();

	    addExceptionHandler(il.findHandle(ce.getStartPC()), il.findHandle(end_pc),
				il.findHandle(ce.getHandlerPC()), c_type);
	  }
	}

	Attribute[] c_attributes = c.getAttributes();
	for(int j=0; j < c_attributes.length; j++) {
	  a = c_attributes[j];

	  if(a instanceof LineNumberTable) {
	    LineNumber[] ln = ((LineNumberTable)a).getLineNumberTable();
	    for(int k=0; k < ln.length; k++) {
	      LineNumber l = ln[k];
	      addLineNumber(il.findHandle(l.getStartPC()), l.getLineNumber());
	    }
	  }
	  else if(a instanceof LocalVariableTable) {
	    LocalVariable[] lv = ((LocalVariableTable)a).getLocalVariableTable();
	    for(int k=0; k < lv.length; k++) {
	      LocalVariable     l     = lv[k];
	      InstructionHandle start = il.findHandle(l.getStartPC());
	      InstructionHandle end   = il.findHandle(l.getStartPC() + l.getLength());

	      // Repair malformed handles
	      if(start == null)
		start = il.getStart();
	      if(end == null)
		end = il.getEnd();

	      addLocalVariable(l.getName(), Type.getType(l.getSignature()),
			       l.getIndex(), start, end);
	    }
	  }
	  else
	    System.err.println("Unknown Code attribute " + a + " ignored.");
	}
      }
      else if(a instanceof ExceptionTable) {
	String[] names = ((ExceptionTable)a).getExceptionNames();
	for(int j=0; j < names.length; j++)
	  addException(names[j]);
      }
      else
	addAttribute(a);
    }
  }

  /**
   * Adds a local variable to this method.
   *
   * @param name variable name
   * @param type variable type
   * @param slot the index of the local variable, if type is long or double, the next available
   * index is slot+2
   * @param start from where the variable is valid
   * @param end until where the variable is valid
   * @return new local variable object
   * @see LocalVariable
   */
  public LocalVariableGen addLocalVariable(String name, Type type, int slot,
					   InstructionHandle start,
					   InstructionHandle end) {
    byte t   = type.getType();
    int  add = type.getSize();
    
    if(slot + add > max_locals) 
      max_locals = slot + add;

    LocalVariableGen l = new LocalVariableGen(slot, name, type, start, end);
    int i;

    if((i = variable_vec.indexOf(l)) >= 0) // Overwrite if necessary
      variable_vec.setElementAt(l, i);
    else
      variable_vec.addElement(l);
    return l;
  }

  /**
   * Adds a local variable to this method and assigns an index automatically.
   *
   * @param name variable name
   * @param type variable type
   * @param start from where the variable is valid, if this is null,
   * it is valid from the start
   * @param end until where the variable is valid, if this is null,
   * it is valid to the end
   * @return new local variable object
   * @see LocalVariable
   */
  public LocalVariableGen addLocalVariable(String name, Type type,
					   InstructionHandle start,
					   InstructionHandle end) {
    return addLocalVariable(name, type, max_locals, start, end);
  }

  /**
   * Remove a local variable, its slot will not be reused, if you do not use addLocalVariable
   * with an explicit `slot' argument.
   */
  public void removeLocalVariable(LocalVariableGen l) {
    variable_vec.removeElement(l);  
  }

  /*
   * If the range of the variable has not been set yet, it will be set to be valid from
   * the start to the end of the instruction list.
   * 
   * @return array of declared local variables
   */
  public LocalVariableGen[] getLocalVariables() {
    int                size = variable_vec.size();
    LocalVariableGen[] lg   = new LocalVariableGen[size];
    variable_vec.copyInto(lg);
    
    for(int i=0; i < size; i++) {
      if(lg[i].getStart() == null)
	lg[i].setStart(il.getStart());

      if(lg[i].getEnd() == null)
	lg[i].setEnd(il.getEnd());
    }

    return lg;
  }

  /**
   * @return `LocalVariableTable' attribute of all the local variables of this method.
   */
  public LocalVariableTable getLocalVariableTable(ConstantPoolGen cp) {
    LocalVariableGen[] lg   = getLocalVariables();
    int                size = lg.length;
    LocalVariable[]    lv   = new LocalVariable[size];

    for(int i=0; i < size; i++)
      lv[i] = lg[i].getLocalVariable(cp);

    return new LocalVariableTable(cp.addUtf8("LocalVariableTable"),
				  2 + lv.length * 10, lv, cp.getConstantPool());
  }

  /**
   * Give an instruction a line number corresponding to the source code line.
   *
   * @param ih instruction to tag
   * @return new line number object
   * @see LineNumber
   */
  public LineNumberGen addLineNumber(InstructionHandle ih, int src_line) {
    LineNumberGen l = new LineNumberGen(ih, src_line);
    line_number_vec.addElement(l);
    return l;
  }

  /**
   * Remove a line number.
   */
  public void removeLineNumber(LineNumberGen l) {
    line_number_vec.removeElement(l);  
  }

  /*
   * @return array of line numbers
   */
  public LineNumberGen[] getLineNumbers() {
    LineNumberGen[] lg   = new LineNumberGen[line_number_vec.size()];
    line_number_vec.copyInto(lg);
    return lg;
  }

  /**
   * @return `LineNumberTable' attribute of all the local variables of this method.
   */
  public LineNumberTable getLineNumberTable(ConstantPoolGen cp) {
    int          size = line_number_vec.size(); 
    LineNumber[] ln   = new LineNumber[size];

    try {
      for(int i=0; i < size; i++)
	ln[i] = ((LineNumberGen)line_number_vec.elementAt(i)).getLineNumber(cp);
    } catch(ArrayIndexOutOfBoundsException e) {} // Never occurs

    return new LineNumberTable(cp.addUtf8("LineNumberTable"),
			       2 + ln.length * 4, ln, cp.getConstantPool());
  }

  /**
   * Add an exception handler, i.e. specify region where a handler is active and an
   * instruction where the actual handling is done.
   *
   * @param start_pc Start of region
   * @param end_pc End of region
   * @param handler_pc Where handling is done
   * @param catch_type fully qualified class name of handled exception or null if any
   * exception is handled
   * @return new exception handler object
   */
  public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc,
					      InstructionHandle end_pc,
					      InstructionHandle handler_pc,
					      ObjectType catch_type) {
    if((start_pc == null) || (end_pc == null) || (handler_pc == null))
      throw new ClassGenException("Exception handler target is null instruction");
    
    CodeExceptionGen c = new CodeExceptionGen(start_pc, end_pc,
					      handler_pc, catch_type);
    exception_vec.addElement(c);
    return c;
  }

  /**
   * @deprecated Use above method
   */
  public CodeExceptionGen addExceptionHandler(InstructionHandle start_pc,
					      InstructionHandle end_pc,
					      InstructionHandle handler_pc,
					      String catch_type) {
    return addExceptionHandler(start_pc, end_pc, handler_pc, catch_type == null?
			       null : new ObjectType(catch_type));
  }

  /**
   * Remove an exception handler.
   */
  public void removeExceptionHandler(CodeExceptionGen c) {
    exception_vec.removeElement(c);  
  }

  /*
   * @return array of declared exception handlers
   */
  public CodeExceptionGen[] getExceptionHandlers() {
    CodeExceptionGen[] cg   = new CodeExceptionGen[exception_vec.size()];
    exception_vec.copyInto(cg);
    return cg;
  }

  /**
   * @return code exceptions for `Code' attribute
   */
  private CodeException[] getCodeExceptions() {
    int             size  = exception_vec.size(); 
    CodeException[] c_exc = new CodeException[size];

    try {
      for(int i=0; i < size; i++) {
	CodeExceptionGen c = (CodeExceptionGen)exception_vec.elementAt(i);
	c_exc[i] = c.getCodeException(cp);
      }
    } catch(ArrayIndexOutOfBoundsException e) {}
    
    return c_exc;
  }

  /**
   * Add an exception possibly thrown by this method.
   *
   * @param class_name (fully qualified) name of exception
   */
  public void addException(String class_name) {
    throws_vec.addElement(class_name);
  }

  /**
   * Remove an exception.
   */
  public void removeException(String c) {
    throws_vec.removeElement(c);  
  }

  /*
   * @return array of thrown exceptions
   */
  public String[] getExceptions() {
    String[] e = new String[throws_vec.size()];
    throws_vec.copyInto(e);
    return e;
  }

  /**
   * @return `Exceptions' attribute of all the exceptions thrown by this method.
   */
  private ExceptionTable getExceptionTable(ConstantPoolGen cp) {
    int   size = throws_vec.size();
    int[] ex   = new int[size];
      
    try {
      for(int i=0; i < size; i++)
	ex[i] = cp.addClass((String)throws_vec.elementAt(i));
    } catch(ArrayIndexOutOfBoundsException e) {}
    
    return new ExceptionTable(cp.addUtf8("Exceptions"),
			      2 + 2 * size, ex, cp.getConstantPool());
  }

  /**
   * Add an attribute to this method. Currently, the JVM knows about the `Code' and
   * `Exceptions' attribute, which will be generated automatically. Other attributes
   * will be ignored by the JVM but do no harm.
   *
   * @param a attribute to be added
   */
  public void addAttribute(Attribute a) { attribute_vec.addElement(a); }

  /**
   * Remove an attribute.
   */
  public void removeAttribute(Attribute a) { attribute_vec.removeElement(a); }
  
  /**
   * @return all attributes of this method.
   */
  public Attribute[] getAttributes() {
    Attribute[] attributes = new Attribute[attribute_vec.size()];
    attribute_vec.copyInto(attributes);
    return attributes;
  }

  /**
   * Add an attribute to the code. Currently, the JVM knows about the `LineNumberTable' and
   * `LocalVariableTable' attributes, which will be generated automatically. Other attributes
   * will be ignored by the JVM but do no harm.
   *
   * @param a attribute to be added
   */
  public void addCodeAttribute(Attribute a) { code_attrs_vec.addElement(a); }

  /**
   * Remove a code attribute.
   */
  public void removeCodeAttribute(Attribute a) { code_attrs_vec.removeElement(a); }
  
  /**
   * @return all attributes of this method.
   */
  public Attribute[] getCodeAttributes() {
    Attribute[] attributes = new Attribute[code_attrs_vec.size()];
    code_attrs_vec.copyInto(attributes);
    return attributes;
  }

  /**
   * Get method object. Never forget to call setMaxStack() or setMaxStack(max), respectively,
   * before calling this method. This method should be called exactly once when the buildup
   * is finished.
   *
   * @return method object
   */
  public Method getMethod() {
    String signature       = Type.getMethodSignature(return_type, arg_types);
    int    name_index      = cp.addUtf8(method_name);
    int    signature_index = cp.addUtf8(signature);

    /* Also updates positions of instructions, i.e. their indices
     */
    byte[] byte_code = null;

    if(il != null)
      byte_code = il.getByteCode();

    /* Create LocalVariableTable and LineNumberTable attributes (for debuggers, e.g.)
     */
    if((variable_vec.size() > 0) && !strip_attributes)
      addCodeAttribute(getLocalVariableTable(cp));

    if((line_number_vec.size() > 0) && !strip_attributes)
      addCodeAttribute(getLineNumberTable(cp));

    Attribute[] code_attrs = getCodeAttributes();

    /* Each attribute causes 6 additional header bytes
     */
    int                attrs_len  = 0;
    for(int i=0; i < code_attrs.length; i++)
      attrs_len += (code_attrs[i].getLength() + 6);

    CodeException[] c_exc   = getCodeExceptions();
    int             exc_len = c_exc.length * 8; // Every entry takes 8 bytes

    if((il != null) && !isAbstract()) {
      Code code = new Code(cp.addUtf8("Code"),
			   8 + byte_code.length + // prologue byte code
			   2 + exc_len +          // exceptions
			   2 + attrs_len,         // attributes
			   max_stack, max_locals,
			   byte_code, c_exc,
			   code_attrs,
			   cp.getConstantPool());

      addAttribute(code);
    }
    
    if(throws_vec.size() > 0)
      addAttribute(getExceptionTable(cp)); // Add `Exceptions' if there are "throws" clauses

    return new Method(access_flags, name_index, signature_index,
		      getAttributes(), cp.getConstantPool());
  }

  /**
   * Remove all NOPs from the instruction list (if possible) and update every
   * object refering to them, i.e. branch instructions, local variables and
   * exception handlers.
   */
  public void removeNOPs() {
    if(il != null) {
      InstructionHandle next;
      /* Check branch instructions.
       */
      for(InstructionHandle ih = il.getStart(); ih != null; ih = next) {
	next = ih.next;

	if((next != null) && (ih.getInstruction() instanceof NOP)) {
	  try {
	    il.delete(ih);
	  } catch(TargetLostException e) {
	    InstructionHandle[] targets = e.getTargets();
	    
	    for(int i=0; i < targets.length; i++) {
	      InstructionTargeter[] targeters = targets[i].getTargeters();
	      
	      for(int j=0; j < targeters.length; j++)
		targeters[j].updateTarget(targets[i], next);
	    }
	  }
	}
      }
    }
  }

  /**
   * Set maximum number of local variables.
   */
  public void   setMaxLocals(int m)  { max_locals = m; }

  /**
   * Set maximum stack size for this method.
   */
  public void   setMaxStack(int m)  { max_stack = m; }

  public int    getMaxLocals() { return max_locals; }
  public int    getMaxStack()  { return max_stack; }

  public void   setMethodName(String method_name)  { this.method_name = method_name; }
  public String getMethodName()                    { return method_name; }
  public String getClassName()                     { return class_name; }
  public void   setReturnType(Type return_type)    { this.return_type = return_type; }
  public Type   getReturnType()                    { return return_type; }
  public void   setArgTypes(Type[] arg_types)      { this.arg_types = arg_types; }
  public Type[] getArgTypes()                      { return arg_types; }

  public void   setArgType(int i, Type type)       { arg_types[i] = type; }
  public Type   getArgType(int i)                  { return arg_types[i]; }

  public InstructionList getInstructionList()      { return il; }
  public void setInstructionList(InstructionList il)      { this.il = il; }
  public ConstantPoolGen getConstantPool()         { return cp; }
  public void setConstantPool(ConstantPoolGen cp) { this.cp = cp; }    

  public String getMethodSignature() { 
    return Type.getMethodSignature(return_type, arg_types);
  }

  /**
   * Computes max. stack size by performing control flow analysis.
   * @author  <A HREF="http://www.vmeng.com/beard">Patrick C. Beard</A>
   */
  public void setMaxStack() {
    if(il != null)
      max_stack = getMaxStack(cp, il, getExceptionHandlers());
    else
      max_stack = 0;
  }

  /**
   * Compute maximum number of local variables. May be a little bit to large, but who cares ...
   */
  public void setMaxLocals() {
    if(il != null) {
      int max = 0;

      if(arg_types != null)
	for(int i=0; i < arg_types.length; i++)
	  max += arg_types[i].getSize();

      for(InstructionHandle ih = il.getStart(); ih != null; ih = ih.getNext()) {
	Instruction ins = ih.getInstruction();
	if(ins instanceof LocalVariableInstruction) {
	  int index = ((LocalVariableInstruction)ins).getIndex() + 1;

	  if(index > max)
	    max = index;
	}
      }

      max_locals = max + 1; // double, long take two slots
    } else
      max_locals = 0;
  }

  /** Do not/Do produce attributes code attributesLineNumberTable and
   * LocalVariableTable, like javac -O
   */
  public void stripAttributes(boolean flag) { strip_attributes = flag; }

  static final class BranchTarget {
    InstructionHandle target;
    int               stackDepth;
		
    BranchTarget(InstructionHandle target, int stackDepth) {
      this.target = target;
      this.stackDepth = stackDepth;
    }
  }
	
  static final class BranchStack {
    Stack     branchTargets  = new Stack();
    Hashtable visitedTargets = new Hashtable();

    public void push(InstructionHandle target, int stackDepth) {
      if(visited(target))
	return;

      branchTargets.push(visit(target, stackDepth));
    }
		
    public BranchTarget pop() {
      if(!branchTargets.empty()) {
	BranchTarget bt = (BranchTarget) branchTargets.pop();
	return bt;
      }

      return null;
    }
		
    private final BranchTarget visit(InstructionHandle target, int stackDepth) {
      BranchTarget bt = new BranchTarget(target, stackDepth);
      visitedTargets.put(target, bt);

      return bt;
    }
		
    private final boolean visited(InstructionHandle target) {
      return (visitedTargets.get(target) != null);
    }
  }

  /**
   * Computes stack usage of an instruction list by performing control flow analysis.
   *
   * @return maximum stack depth used by method
   */
  public static int getMaxStack(ConstantPoolGen cp, InstructionList il, CodeExceptionGen[] et) {
    BranchStack branchTargets = new BranchStack();
    	
    /* Initially, populate the branch stack with the exception
     * handlers, because these aren't (necessarily) branched to
     * explicitly. in each case, the stack will have depth 1,
     * containing the exception object.
     */
    for (int i = 0; i < et.length; i++) {
      InstructionHandle handler_pc = et[i].getHandlerPC();
      if (handler_pc != null)
	branchTargets.push(handler_pc, 1);
    }
    	
    int               stackDepth = 0, maxStackDepth = 0;
    InstructionHandle ih         = il.getStart();

    while(ih != null) {
      Instruction instruction = ih.getInstruction();
      short tag = instruction.getTag();
      int delta = instruction.produceStack(cp) - instruction.consumeStack(cp);

      stackDepth += delta;
      if(stackDepth > maxStackDepth)
	maxStackDepth = stackDepth;

      // choose the next instruction based on whether current is a branch.
      if(instruction instanceof BranchInstruction) {
	BranchInstruction branch = (BranchInstruction) instruction;
	if(instruction instanceof Select) {
	  // explore all of the select's targets. the default target is handled below.
	  Select select = (Select) branch;
	  InstructionHandle[] targets = select.getTargets();
	  for (int i = 0; i < targets.length; i++)
	    branchTargets.push(targets[i], stackDepth);
	  // nothing to fall through to.
	  ih = null;
	} else if(!(branch instanceof IfInstruction)) {
	  // if an instruction that comes back to following PC,
	  // push next instruction, with stack depth reduced by 1.
	  if(tag == JSR || tag == JSR_W)
	    branchTargets.push(ih.getNext(), stackDepth - 1);
	  ih = null;
	}
	// for all branches, the target of the branch is pushed on the branch stack.
	// conditional branches have a fall through case, selects don't, and
	// jsr/jsr_w return to the next instruction.
	branchTargets.push(branch.getTarget(), stackDepth);
      } else {
	// check for instructions that terminate the method.
	if(tag == ATHROW || tag == RET || (tag >= IRETURN && tag <= RETURN))
	  ih = null;
      }
      // normal case, go to the next instruction.
      if(ih != null)
	ih = ih.getNext();
      // if we have no more instructions, see if there are any deferred branches to explore.
      if(ih == null) {
	BranchTarget bt = branchTargets.pop();
	if (bt != null) {
	  ih = bt.target;
	  stackDepth = bt.stackDepth;
	}
      }
    }

    return maxStackDepth;
  }
}
