package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;

/** 
 * PUTFIELD - Put field in object
 * <PRE>Stack: ..., objectref, value -&gt; ...</PRE>
 * OR
 * <PRE>Stack: ..., objectref, value.word1, value.word2 -&gt; ...</PRE>
 *
 * @version $Id: PUTFIELD.java,v 1.1.1.1 1999/09/09 07:43:00 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class PUTFIELD extends FieldInstruction implements ExceptionThrower{
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  PUTFIELD() {}

  public PUTFIELD(int index) {
    super(Constants.PUTFIELD, index);
  }

  public int consumeStack(ConstantPoolGen cpg) { return getFieldSize(cpg) + 1; }

  public Class[] getExceptions(){
    Class[] cs = new Class[2 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length-1] = INCOMPATIBLE_CLASS_CHANGE_ERROR;
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length]   = NULL_POINTER_EXCEPTION;
    return cs;
  }
}
