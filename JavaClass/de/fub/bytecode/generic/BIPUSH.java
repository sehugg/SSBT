package de.fub.bytecode.generic;

import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/** 
 * BIPUSH - Push byte
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 *
 * @version $Id: BIPUSH.java,v 1.1.1.1 1999/05/04 13:12:44 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class BIPUSH extends Instruction implements ConstantPushInstruction {
  private byte b;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  BIPUSH() {}

  public BIPUSH(byte b) {
    super(BIPUSH, (short)2);
    this.b = b;
  }

  /**
   * Dump instruction as byte code to stream out.
   */
  public void dump(DataOutputStream out) throws IOException {
    super.dump(out);
    out.writeByte(b);
  }

  /**
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    return super.toString(verbose) + " " + b;
  }

  /**
   * Read needed data (e.g. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException
  {
    length = 2;
    b      = bytes.readByte();
  }

  public Number getValue() { return new Integer(b); }
}

