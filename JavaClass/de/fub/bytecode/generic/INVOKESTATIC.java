package de.fub.bytecode.generic;

/** 
 * INVOKESTATIC - Invoke a class (static) method
 *
 * <PRE>Stack: ..., [arg1, [arg2 ...]] -&gt; ...</PRE>
 *
 * @version $Id: INVOKESTATIC.java,v 1.1.1.1 1999/09/22 13:00:08 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class INVOKESTATIC extends InvokeInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  INVOKESTATIC() {}

  public INVOKESTATIC(int index) {
    super(INVOKESTATIC, index);
  }

  public Class[] getExceptions(){
    Class[] cs = new Class[2 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-3] = UNSATISFIED_LINK_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-1] = INCOMPATIBLE_CLASS_CHANGE_ERROR;

    return cs;
  }


}

