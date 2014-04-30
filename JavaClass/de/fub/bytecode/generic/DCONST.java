package de.fub.bytecode.generic;

/** 
 * DCONST - Push 0.0 or 1.0, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., <i></PRE>
 *
 * @version $Id: DCONST.java,v 1.1.1.1 1999/05/04 13:13:03 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DCONST extends Instruction implements ConstantPushInstruction {
  private double value;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  DCONST() {}

  public DCONST(double f) {
    super(DCONST_0, (short)1);

    if(f == 0.0)
      tag = DCONST_0;
    else if(f == 1.0)
      tag = DCONST_1;
    else
      throw new ClassGenException("DCONST can be used only for 0.0 and 1.0: " + f);

    value = f;
  }

  public Number getValue() { return new Double(value); }
}

