package de.fub.bytecode.generic;

/** 
 * ANEWARRAY -  Create new array of references
 * <PRE>Stack: ..., count -&gt; ..., arrayref</PRE>
 *
 * @version $Id: ANEWARRAY.java,v 1.1.1.1 1999/09/09 07:42:43 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class ANEWARRAY extends CPInstruction
  implements LoadClass, AllocationInstruction, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  ANEWARRAY() {}

  public ANEWARRAY(int index) {
    super(ANEWARRAY, index);
  }

  public Class[] getExceptions(){
    Class[] cs = new Class[1 + EXCS_CLASS_AND_INTERFACE_RESOLUTION.length];

    System.arraycopy(EXCS_CLASS_AND_INTERFACE_RESOLUTION, 0,
		     cs, 0, EXCS_CLASS_AND_INTERFACE_RESOLUTION.length);
    cs[EXCS_CLASS_AND_INTERFACE_RESOLUTION.length] =  NEGATIVE_ARRAY_SIZE_EXCEPTION;
    return cs;
  }


}

