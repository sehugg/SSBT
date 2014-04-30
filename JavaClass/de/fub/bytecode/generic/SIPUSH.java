package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/**
 * SIPUSH - Push short
 *
 * <PRE>Stack: ... -&gt; ..., value</PRE>
 *
 * @version $Id: SIPUSH.java,v 1.1.1.1 1999/05/04 13:14:53 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class SIPUSH extends Instruction implements ConstantPushInstruction {
  private short b;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  SIPUSH() {}

  public SIPUSH(short b) {
    super(SIPUSH, (short)3);
    this.b = b;
  }

  /**
   * Dump instruction as short code to stream out.
   */
  public void dump(DataOutputStream out) throws IOException {
    super.dump(out);
    out.writeShort(b);
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
    length = 3;
    b      = bytes.readShort();
  }

  public Number getValue() { return new Integer(b); }
}

