package de.fub.bytecode.generic;
import de.fub.bytecode.Constants;
import de.fub.bytecode.classfile.*;
import java.util.StringTokenizer;

/**
 * Super class for the INVOKExxx family of instructions.
 *
 * @version $Id: InvokeInstruction.java,v 1.1.1.1 1999/09/22 13:00:11 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class InvokeInstruction extends FieldOrMethod implements ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  InvokeInstruction() {}

  /**
   * @param index to constant pool
   */
  protected InvokeInstruction(short tag, int index) {
    super(tag, index);
  }

  /**
   * @return mnemonic for instruction with symbolic references resolved
   */
  public String toString(ConstantPool cp) {
    Constant        c   = cp.getConstant(index);
    StringTokenizer tok = new StringTokenizer(cp.constantToString(c));

    return OPCODE_NAMES[tag] + " " +
      tok.nextToken().replace('.', '/') + tok.nextToken();
  }

  /**
   * Also works for instructions whose stack effect depends on the
   * constant pool entry they reference.
   * @return Number of words consumed from stack by this instruction
   */
  public int consumeStack(ConstantPoolGen cpg) {
      String signature = getSignature(cpg);
      Type[] args      = Type.getArgumentTypes(signature);
      int    sum;

      if(tag == Constants.INVOKESTATIC)
	sum = 0;
      else
	sum = 1;  // this reference

      int n = args.length;
      for (int i = 0; i < n; i++)
	sum += args[i].getSize();

      return sum;
   }

  /**
   * Also works for instructions whose stack effect depends on the
   * constant pool entry they reference.
   * @return Number of words produced onto stack by this instruction
   */
  public int produceStack(ConstantPoolGen cpg) {
    return getReturnType(cpg).getSize();
  }

  /** @return return type of referenced method.
   */
  public Type getType(ConstantPoolGen cpg) {
    return getReturnType(cpg);
  }

  /** @return name of referenced method.
   */
  public String getMethodName(ConstantPoolGen cpg) {
    return getName(cpg);
  }

  /** @return return type of referenced method.
   */
  public Type getReturnType(ConstantPoolGen cpg) {
    return Type.getReturnType(getSignature(cpg));
  }

  /** @return argument types of referenced method.
   */
  public Type[] getArgumentTypes(ConstantPoolGen cpg) {
    return Type.getArgumentTypes(getSignature(cpg));
  }
}
