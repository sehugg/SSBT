package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/** 
 * LDC - Push item from constant pool
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id: LDC.java,v 1.1.1.1 1999/09/09 07:42:55 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDC extends CPInstruction implements PushInstruction, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  LDC() {}

  public LDC(int index) {
    super(LDC_W, index);
    setSize();
  }
  
  // Adjust to proper size
  protected final void setSize() {
    if(index <= MAX_BYTE) { // Fits in one byte?
      tag    = LDC;
      length = 2;
    } else {
      tag    = LDC_W;
      length = 3;
    }
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    out.writeByte(tag);

    if(length == 2)
      out.writeByte(index);
    else // Applies for LDC_W
      out.writeShort(index);
  }

  /**
   * Set the index to constant pool and adjust size.
   */
  public final void setIndex(int index) { 
    super.setIndex(index);
    setSize();
  }

  /**
   * Read needed data (e.g. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide)
       throws IOException
  {
    length = 2;
    index  = bytes.readUnsignedByte();
  }

  public Type getType(ConstantPoolGen cpg) {
    switch(cpg.getConstantPool().getConstant(index).getTag()) {
    case CONSTANT_String:  return Type.STRING;
    case CONSTANT_Float:   return Type.FLOAT;
    case CONSTANT_Integer: return Type.INT;
    default: // Never reached
      throw new RuntimeException("Unknown constant type " + tag);
    }
  }

  public Class[] getExceptions() { return EXCS_STRING_RESOLUTION; }
}

