package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;
import de.fub.bytecode.classfile.ConstantPool;

/** 
 * MULTIANEWARRAY - Create new mutidimensional array of references
 * <PRE>Stack: ..., count1, [count2, ...] -&gt; ..., arrayref</PRE>
 *
 * @version $Id: MULTIANEWARRAY.java,v 1.1.1.1 1999/11/26 13:52:02 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class MULTIANEWARRAY extends CPInstruction implements LoadClass, AllocationInstruction, ExceptionThrower {
  private short dimensions;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  MULTIANEWARRAY() {}

  public MULTIANEWARRAY(int index, short dimensions) {
    super(MULTIANEWARRAY, index);

    if(dimensions < 1)
      throw new ClassGenException("Invalid dimensions value: " + dimensions);

    this.dimensions = dimensions;
    length = 4;
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    out.writeByte(tag);
    out.writeShort(index);
    out.writeByte(dimensions);
  }

  /**
   * Read needed data (i.e. no. dimension) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide)
       throws IOException
  {
    super.initFromFile(bytes, wide);
    dimensions = bytes.readByte();
    length     = 4;
  }

  /**
   * @return number of dimensions to be created
   */
  public final short getDimensions() { return dimensions; }

  /**
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    return super.toString(verbose) + " " + index + " " + dimensions;
  }

  /**
   * @return mnemonic for instruction with symbolic references resolved
   */
  public String toString(ConstantPool cp) {
    return super.toString(cp) + " " + dimensions;
  }

  /**
   * Also works for instructions whose stack effect depends on the
   * constant pool entry they reference.
   * @return Number of words consumed from stack by this instruction
   */
  public int consumeStack(ConstantPoolGen cpg)
   { return dimensions; }

  public Class[] getExceptions() {
    Class[] cs = new Class[2 + EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];

    System.arraycopy(EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0,
		     cs, 0, EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length-1] = NEGATIVE_ARRAY_SIZE_EXCEPTION;
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length]   = ILLEGAL_ACCESS_ERROR;
    return cs;
  }
}

