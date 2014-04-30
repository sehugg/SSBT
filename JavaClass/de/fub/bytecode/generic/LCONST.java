package de.fub.bytecode.generic;

/** 
 * LCONST - Push 0 or 1, other values cause an exception
 *
 * <PRE>Stack: ... -&gt; ..., <i></PRE>
 *
 * @version $Id: LCONST.java,v 1.1.1.1 1999/05/04 13:14:20 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LCONST extends Instruction implements ConstantPushInstruction {
  private long value;

  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  LCONST() {}

  public LCONST(long l) {
    super(LCONST_0, (short)1);

    if(l == 0)
      tag = LCONST_0;
    else if(l == 1)
      tag = LCONST_1;
    else
      throw new ClassGenException("LCONST can be used only for 0 and 1: " + l);

    value = l;
  }

  public Number getValue() { return new Long(value); }
}
