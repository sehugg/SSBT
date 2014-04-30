package de.fub.bytecode.generic;
import java.io.*;

/** 
 * NEW - Create new object
 * <PRE>Stack: ... -&gt; ..., objectref</PRE>
 *
 * @version $Id: NEW.java,v 1.1.1.1 1999/09/09 07:42:58 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class NEW extends CPInstruction implements LoadClass, AllocationInstruction, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  NEW() {}

  public NEW(int index) {
    super(NEW, index);
  }

  public Class[] getExceptions(){
    Class[] cs = new Class[2 + EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];

    System.arraycopy(EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0,
		     cs, 0, EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length-1] = INSTANTIATION_ERROR;
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length]   = ILLEGAL_ACCESS_ERROR;
    return cs;
  }

}

