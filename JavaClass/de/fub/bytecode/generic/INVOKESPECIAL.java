package de.fub.bytecode.generic;

/** 
 * INVOKESPECIAL - Invoke instance method; special handling for superclass, private
 * and instance initialization method invocations
 *
 * <PRE>Stack: ..., objectref, [arg1, [arg2 ...]] -&gt; ...</PRE>
 *
 * @version $Id: INVOKESPECIAL.java,v 1.1.1.1 1999/09/22 13:00:07 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INVOKESPECIAL extends InvokeInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  INVOKESPECIAL() {}

  public INVOKESPECIAL(int index) {
    super(INVOKESPECIAL, index);
  }

  public Class[] getExceptions() {
    Class[] cs = new Class[4 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-3] = UNSATISFIED_LINK_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-2] = ABSTRACT_METHOD_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-1] = INCOMPATIBLE_CLASS_CHANGE_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length]   = NULL_POINTER_EXCEPTION;
    return cs;
  }

}

