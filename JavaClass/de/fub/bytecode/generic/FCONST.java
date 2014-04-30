package de.fub.bytecode.generic;

/** 
 * FCONST - Push 0.0, 1.0 or 2.0, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., <i></PRE>
 *
 * @version $Id: FCONST.java,v 1.1.1.1 1999/05/04 13:13:20 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class FCONST extends Instruction implements ConstantPushInstruction {
  private float value;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  FCONST() {}

  public FCONST(float f) {
    super(FCONST_0, (short)1);

    if(f == 0.0)
      tag = FCONST_0;
    else if(f == 1.0)
      tag = FCONST_1;
    else if(f == 2.0)
      tag = FCONST_2;
    else
      throw new ClassGenException("FCONST can be used only for 0.0, 1.0 and 2.0: " + f);

    value = f;
  }

  public Number getValue() { return new Float(value); }
}

