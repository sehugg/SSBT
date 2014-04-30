package de.fub.bytecode.generic;

/**
 * NOP - Do nothing
 *
 * @version $Id: NOP.java,v 1.1.1.1 1999/05/04 13:14:42 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class NOP extends Instruction {
  public NOP() {
    super(NOP, (short)1);
  }
}

