package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/**
 * IINC - Increment local variable by constant
 *
 * @version $Id: IINC.java,v 1.1.1.1 1999/05/04 13:13:53 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class IINC extends Instruction {
  private boolean wide;
  private int     n, c;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  IINC() {}

  public IINC(int n, int c) {
    super(IINC, (short)3);

    setIndex(n);    // May set wide as side effect
    setIncrement(c);

    this.n = n;
    this.c = c;
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    if(wide) // Need WIDE prefix ?
      out.writeByte(WIDE);

    out.writeByte(tag);

    if(wide) {
      out.writeShort(n);
      out.writeShort(c);
    }
    else {
      out.writeByte(n);
      out.writeByte(c);
    }
  }

  private final void setWide() {
    if(wide = ((n > MAX_SHORT) || (Math.abs(c) > Byte.MAX_VALUE)))
      length = 6; // wide byte included  
    else
      length = 3;
  }

  /**
   * Read needed data (e.g. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide) throws IOException
  {
    this.wide = wide;

    if(wide) {
      length = 6;
      n = bytes.readUnsignedShort();
      c = bytes.readShort();
    }
    else {
      length = 3;
      n = bytes.readUnsignedByte();
      c = bytes.readByte();
    }
  }

  /**
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    return super.toString(verbose) + " " + n + " " + c;
  }  

  /**
   * @return index of local variable referred by this instruction.
   */
  public final int getIndex() { return n; }

  /**
   * Set index of local variable.
   */
  public final void setIndex(int n) { 
    if(n < 0)
      throw new ClassGenException("Negative index value: " + n);

    this.n = n;
    setWide();
  }

  /**
   * @return increment factor
   */
  public final int getIncrement() { return c; }

  /**
   * Set increment factor.
   */
  public final void setIncrement(int c) {
    this.c = c;
    setWide();
  }
}
