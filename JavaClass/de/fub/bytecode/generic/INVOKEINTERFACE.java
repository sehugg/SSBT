package de.fub.bytecode.generic;
import de.fub.bytecode.classfile.ConstantPool;
import de.fub.bytecode.Constants;

import java.io.*;
import de.fub.bytecode.util.ByteSequence;

/** 
 * INVOKEINTERFACE - Invoke interface method
 * <PRE>Stack: ..., objectref, [arg1, [arg2 ...]] -&gt; ...</PRE>
 *
 * @version $Id: INVOKEINTERFACE.java,v 1.1.1.1 1999/09/22 13:00:06 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public final class INVOKEINTERFACE extends InvokeInstruction {
  private int nargs; // Number of arguments on stack

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  INVOKEINTERFACE() {}

  public INVOKEINTERFACE(int index, int nargs) {
    super(Constants.INVOKEINTERFACE, index);
    length = 5;

    if(nargs < 1)
      throw new ClassGenException("Number of arguments must be > 0 " + nargs);

    this.nargs = nargs;
  }

  /**
   * Dump instruction as byte code to stream out.
   * @param out Output stream
   */
  public void dump(DataOutputStream out) throws IOException {
    out.writeByte(tag);
    out.writeShort(index);
    out.writeByte(nargs);
    out.writeByte(0);
  }

  public int getNoArguments() { return nargs; }

  /**
   * Read needed data (i.e. index) from file.
   */
  protected void initFromFile(ByteSequence bytes, boolean wide)
       throws IOException
  {
    super.initFromFile(bytes, wide);

    length = 5;
    nargs = bytes.readUnsignedByte();
    bytes.readByte(); // Skip 0 byte
  }

  /**
   * @return mnemonic for instruction with symbolic references resolved
   */
  public String toString(ConstantPool cp) {
    return super.toString(cp) + " " + nargs;
  }

  public int consumeStack(ConstantPoolGen cpg)  // nargs is given in byte-code
   {  return nargs; }   // nargs includes this reference

  public Class[] getExceptions(){
    Class[] cs = new Class[4 + EXCS_INTERFACE_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_INTERFACE_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_INTERFACE_METHOD_RESOLUTION.length);
    cs[EXCS_INTERFACE_METHOD_RESOLUTION.length-3] = INCOMPATIBLE_CLASS_CHANGE_ERROR;
    cs[EXCS_INTERFACE_METHOD_RESOLUTION.length-2] = ILLEGAL_ACCESS_ERROR;
    cs[EXCS_INTERFACE_METHOD_RESOLUTION.length-1] = ABSTRACT_METHOD_ERROR;
    cs[EXCS_INTERFACE_METHOD_RESOLUTION.length]   = UNSATISFIED_LINK_ERROR;
    return cs;
  }


}
