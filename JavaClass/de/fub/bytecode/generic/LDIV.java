package de.fub.bytecode.generic;

/**
 * LDIV - Divide longs
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result.word1, result.word2
 *
 * @version $Id: LDIV.java,v 1.1.1.1 1999/09/22 13:00:11 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDIV extends ArithmeticInstruction implements ExceptionThrower  {
  public LDIV() {
    super(LDIV);
  }

  public Class[] getExceptions() { return new Class[] { ARITHMETIC_EXCEPTION }; }
}

