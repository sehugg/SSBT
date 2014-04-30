package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;

/** 
 * GETSTATIC - Fetch static field from class
 * <PRE>Stack: ..., -&gt; ..., value</PRE>
 * OR
 * <PRE>Stack: ..., -&gt; ..., value.word1, value.word2</PRE>
 *
 * @version $Id: GETSTATIC.java,v 1.1.1.1 1999/09/09 07:42:48 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class GETSTATIC extends FieldInstruction implements PushInstruction, ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  GETSTATIC() {}

  public GETSTATIC(int index) {
    super(Constants.GETSTATIC, index);
  }

  public int produceStack(ConstantPoolGen cpg) { return getFieldSize(cpg); }

  public Class[] getExceptions(){
    Class[] cs = new Class[1 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length] = INCOMPATIBLE_CLASS_CHANGE_ERROR;

    return cs;
  }
}
