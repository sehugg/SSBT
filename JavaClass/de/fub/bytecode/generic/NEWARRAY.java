package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/** 
 * NEWARRAY -  Create new array of basic type (int, short, ...)
 * <PRE>Stack: ..., count -&gt; ..., arrayref</PRE>
 * type mus be one of T_INT, T_SHORT, ...
 * 
 * @version $Id: NEWARRAY.java,v 1.1.1.1 1999/11/26 13:54:30 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class NEWARRAY extends Instruction implements AllocationInstruction, ExceptionThrower{
  private byte type;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  NEWARRAY() {}

  public NEWARRAY(byte type) {
    super(NEWARRAY, (short)2);
    this.type = type;
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    out.writeByte(tag);
    out.writeByte(type);
  }

  /**
   * @return numeric code for basic element type
   */
  public final byte getTypecode() { return type; }

  /**
   * @return type of constructed array
   */
  public final Type getType() {
    return new ArrayType(BasicType.getType(type), 1);
  }

  /**
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    return super.toString(verbose) + " " + TYPE_NAMES[type];
  }
  /**
   * Read needed data (e.g. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException
  {
    type   = bytes.readByte();
    length = 2;
  }

  public Class[] getExceptions() { return new Class[] { NEGATIVE_ARRAY_SIZE_EXCEPTION }; }
}

