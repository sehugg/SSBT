import de.fub.bytecode.generic.*;
import de.fub.bytecode.Constants;

/**
 * Create HelloWorld class:
 * <PRE>
 * import java.io.*;
 *
 * public class HelloWorld {
 *   public static void main(String[] argv) {
 *     BufferedReader in   = new BufferedReader(new InputStreamReader(System.in));
 *     String name = null;
 * 
 *     try {
 *       System.out.print("Please enter your name> ");
 *       name = in.readLine();
 *     } catch(IOException e) { return; }
 * 
 *     System.out.println("Hello, " + name);
 *   }
 * }
 * </PRE>
 *
 * @version $Id: HelloWorldBuilder.java,v 1.1.1.1 1999/06/23 14:56:29 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class HelloWorldBuilder implements InstructionConstants {
  public static void main(String[] argv) {
    ClassGen        cg = new ClassGen("HelloWorld", "java.lang.Object",
				      "<generated>", Constants.ACC_PUBLIC |
				      Constants.ACC_SUPER,
				      null);
    ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
    InstructionList il = new InstructionList();
    MethodGen       mg = new MethodGen(Constants.ACC_STATIC |
				       Constants.ACC_PUBLIC,// access flags
				       Type.VOID,              // return type
				       new Type[] {            // argument types
					 new ArrayType(Type.STRING, 1) 
				       },
				       new String[] { "argv" }, // arg names
				       "main", "HelloWorld",    // method, class
				       il, cp);

    /* Add often needed constants to constant pool.
     */
    int br_index   = cp.addClass("java.io.BufferedReader");
    int ir_index   = cp.addClass("java.io.InputStreamReader");
    int system_out = cp.addFieldref("java.lang.System", "out", // System.out
				    "Ljava/io/PrintStream;");
    int system_in  = cp.addFieldref("java.lang.System", "in",  // System.in
				    "Ljava/io/InputStream;");

    /* Create BufferedReader object and store it in local variable `in'.
     */
    il.append(new NEW(br_index));
    il.append(DUP);
    il.append(new NEW(ir_index));
    il.append(DUP);
    il.append(new GETSTATIC(system_in));

    /* Call constructors, i.e. BufferedReader(InputStreamReader())
     */
    il.append(new INVOKESPECIAL(cp.addMethodref("java.io.InputStreamReader",
						"<init>",
						"(Ljava/io/InputStream;)V")));
    il.append(new INVOKESPECIAL(cp.addMethodref("java.io.BufferedReader",
						"<init>",
						"(Ljava/io/Reader;)V")));
    /* Create local variable `in'
     */
    LocalVariableGen lg = mg.addLocalVariable("in",
					      new ObjectType("java.io.BufferedReader"),
					      null, null);
    int in = lg.getIndex();
    lg.setStart(il.append(new ASTORE(in))); // `i' valid from here

    /* Create local variable `name'
     */
    lg = mg.addLocalVariable("name", Type.STRING, null, null);
    int name = lg.getIndex();
    il.append(ACONST_NULL);
    lg.setStart(il.append(new ASTORE(name))); // `name' valid from here

    /* try { ...
     */
    InstructionHandle try_start = il.append(new GETSTATIC(system_out));
    il.append(new PUSH(cp, "Please enter your name> "));
    il.append(new INVOKEVIRTUAL(cp.addMethodref("java.io.PrintStream",
						"print",
						"(Ljava/lang/String;)V")));
    il.append(new ALOAD(in));
    il.append(new INVOKEVIRTUAL(cp.addMethodref("java.io.BufferedReader",
						"readLine",
						"()Ljava/lang/String;")));
    il.append(new ASTORE(name));

    /* Upon normal execution we jump behind exception handler, 
     * the target address is not known yet.
     */
    GOTO g = new GOTO(null);
    InstructionHandle try_end = il.append(g);

    /* } catch() { ... }
     * Add exception handler: simply return from method
     */
    InstructionHandle handler = il.append(RETURN);
    mg.addExceptionHandler(try_start, try_end, handler, new ObjectType("java.io.IOException"));

    /* Normal code continues, now we can set the branch target of the GOTO.
     */
    InstructionHandle ih = il.append(new GETSTATIC(system_out));
    g.setTarget(ih);

    /* String concatenation compiles to StringBuffer operations.
     */
    il.append(new NEW(cp.addClass("java.lang.StringBuffer")));
    il.append(DUP);
    il.append(new PUSH(cp, "Hello, "));
    il.append(new INVOKESPECIAL(cp.addMethodref("java.lang.StringBuffer",
						"<init>",
						"(Ljava/lang/String;)V")));
    il.append(new ALOAD(name));
    
    /* One can also abstract from using the ugly signature syntax by using
     * the getMethodSignature() method. For example:
     */
    String sig = Type.getMethodSignature(Type.STRINGBUFFER,
					 new Type[] { Type.STRING });
    il.append(new INVOKEVIRTUAL(cp.addMethodref("java.lang.StringBuffer",
						"append", sig)));

    il.append(new INVOKEVIRTUAL(cp.addMethodref("java.lang.StringBuffer",
						"toString",
						"()Ljava/lang/String;")));
    
    il.append(new INVOKEVIRTUAL(cp.addMethodref("java.io.PrintStream",
						"println",
						"(Ljava/lang/String;)V")));
    il.append(RETURN);

    mg.setMaxStack(5); // Needed stack size
    cg.addMethod(mg.getMethod());

    /* Add public <init> method, i.e. empty constructor
     */
    cg.addEmptyConstructor(Constants.ACC_PUBLIC);

    /* Get JavaClass object and dump it to file.
     */
    try {
      cg.getJavaClass().dump("HelloWorld.class");
    } catch(java.io.IOException e) { System.err.println(e); }
  }
}
