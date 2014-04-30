package de.fub.bytecode.generic;
import java.io.*;
import de.fub.bytecode.util.ByteSequence;
import de.fub.bytecode.classfile.Utility;

/** 
 * Abstract super class for instructions dealing with local variables.
 *
 * @version $Id: LocalVariableInstruction.java,v 1.1.1.1 1999/08/13 08:18:34 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public abstract class LocalVariableInstruction extends Instruction {
  private int     n;     // index of referenced variable
  private short   c_tag; // compact version, such as ILOAD_0
  private short   canon_tag; // canonical tag such as ILOAD

  private final boolean wide() { return n > MAX_BYTE; }

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   * tag and length are defined in readInstruction and initFromFile, respectively.
   */
  LocalVariableInstruction(short canon_tag, short c_tag) {
    super();
    this.canon_tag = canon_tag;
    this.c_tag     = c_tag;
  }

  /**
   * @param tag Instruction number
   * @param c_tag Instruction number for compact version, ALOAD_0, e.g.
   * @param n local variable index (unsigned short)
   */
  protected LocalVariableInstruction(short tag, short c_tag, int n) {
    super(tag, (short)2);

    this.c_tag = c_tag;
    canon_tag  = tag;

    setIndex(n);
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    if(wide()) // Need WIDE prefix ?
      out.writeByte(WIDE);

    out.writeByte(tag);

    if(length > 1) { // Otherwise ILOAD_n, instruction, e.g.
      if(wide())
	out.writeShort(n);
      else
	out.writeByte(n);
    }
  }

  /**
   * Long output format:
   *
   * &lt;name of opcode&gt; "["&lt;opcode number&gt;"]" 
   * "("&lt;length of instruction&gt;")" "&lt;"&lt; local variable index&gt;"&gt;"
   *
   * @param verbose long/short format switch
   * @return mnemonic for instruction
   */
  public String toString(boolean verbose) {
    if(((tag >= ILOAD_0) && (tag <= ALOAD_3)) ||
       ((tag >= ISTORE_0) && (tag <= ASTORE_3)))
      return super.toString(verbose);
    else
      return super.toString(verbose) + " " + n;
  }

  /**
   * Read needed data (e.g. index) from file.
   * PRE: (ILOAD <= tag <= ALOAD_3) || (ISTORE <= tag <= ASTORE_3)
   */
  protected void initFromFile(ByteSequence bytes, boolean wide)
    throws IOException
  {
    if(wide) {
      n         = bytes.readUnsignedShort();
      length    = 4;
    } else if(((tag >= ILOAD)  && (tag <= ALOAD)) ||
	    ((tag >= ISTORE) && (tag <= ASTORE))) {
      n      = bytes.readUnsignedByte();
      length = 2;
    } else if(tag <= ALOAD_3) { // compact load instruction such as ILOAD_2
      n      = (tag - ILOAD_0) % 4;
      length = 1;
    } else { // Assert ISTORE_0 <= tag <= ASTORE_3
      n      = (tag - ISTORE_0) % 4;
      length = 1;
    }
 }

  /**
   * @return local variable index  referred by this instruction.
   */
  public final int getIndex() { return n; }

  /**
   * Set the local variable index
   */
  public final void setIndex(int n) { 
    if((n < 0) || (n > MAX_SHORT))
      throw new ClassGenException("Illegal value: " + n);

    this.n = n;

    if(n >= 0 && n <= 3) { // Use more compact instruction xLOAD_n
      tag    = (short)(c_tag + n);
      length = 1;
    } else {
      tag = canon_tag;
      
      if(wide()) // Need WIDE prefix ?
	length = 4;
      else
	length = 2;
    }
  }
}
