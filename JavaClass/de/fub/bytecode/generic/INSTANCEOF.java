package de.fub.bytecode.generic;

/** 
 * INSTANCEOF - Determine if object is of given type
 * <PRE>Stack: ..., objectref -&gt; ..., result</PRE>
 *
 * @version $Id: INSTANCEOF.java,v 1.1.1.1 1999/09/09 07:42:49 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INSTANCEOF extends CPInstruction implements LoadClass, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  INSTANCEOF() {}

  public INSTANCEOF(int index) {
    super(INSTANCEOF, index);
  }

  public Class[] getExceptions() { return EXCS_CLASS_AND_INTERFACE_RESOLUTION; }

}

