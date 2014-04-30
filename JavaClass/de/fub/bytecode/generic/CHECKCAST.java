package de.fub.bytecode.generic;

/** 
 * CHECKCAST - Check whether object is of given type
 * <PRE>Stack: ..., objectref -&gt; ..., objectref</PRE>
 *
 * @version $Id: CHECKCAST.java,v 1.1.1.1 1999/09/09 07:42:47 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class CHECKCAST extends CPInstruction implements LoadClass, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  CHECKCAST() {}

  public CHECKCAST(int index) {
    super(CHECKCAST, index);
  }

  public Class[] getExceptions() {
    Class[] cs = new Class[1 + EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];

    System.arraycopy(EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0,
		     cs, 0, EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] = CLASS_CAST_EXCEPTION;
    return cs;
  }


}

