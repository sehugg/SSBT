package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;

/** 
 * GETFIELD - Fetch field from object
 * <PRE>Stack: ..., objectref -&gt; ..., value</PRE>
 * OR
 * <PRE>Stack: ..., objectref -&gt; ..., value.word1, value.word2</PRE>
 *
 * @version $Id: GETFIELD.java,v 1.1.1.1 1999/09/09 07:42:47 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class GETFIELD extends FieldInstruction implements ExceptionThrower{
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  GETFIELD() {}

  public GETFIELD(int index) {
    super(Constants.GETFIELD, index);
  }

  public int produceStack(ConstantPoolGen cpg) { return getFieldSize(cpg); }

  public Class[] getExceptions(){
    Class[] cs = new Class[2 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-1] = INCOMPATIBLE_CLASS_CHANGE_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length]   = NULL_POINTER_EXCEPTION;
    return cs;
  }
}
