package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;

/** 
 * PUTSTATIC - Put static field in class
 * <PRE>Stack: ..., objectref, value -&gt; ...</PRE>
 * OR
 * <PRE>Stack: ..., objectref, value.word1, value.word2 -&gt; ...</PRE>
 *
 * @version $Id: PUTSTATIC.java,v 1.1.1.1 1999/09/09 07:43:00 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class PUTSTATIC extends FieldInstruction implements ExceptionThrower {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  PUTSTATIC() {}

  public PUTSTATIC(int index) {
    super(Constants.PUTSTATIC, index);
  }

  public int consumeStack(ConstantPoolGen cpg) { return getFieldSize(cpg); }

  public Class[] getExceptions(){
    Class[] cs = new Class[1 + EXCS_FIELD_AND_METHOD_RESOLUTION.length];

    System.arraycopy(EXCS_FIELD_AND_METHOD_RESOLUTION, 0,
		     cs, 0, EXCS_FIELD_AND_METHOD_RESOLUTION.length);
    cs[EXCS_FIELD_AND_METHOD_RESOLUTION.length] = INCOMPATIBLE_CLASS_CHANGE_ERROR;

    return cs;
  }
}

