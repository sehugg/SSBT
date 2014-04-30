package de.fub.bytecode.generic;

/** 
 * DDIV -  Divide doubles
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result.word1, result.word2
 *
 * @version $Id: DDIV.java,v 1.1.1.1 1999/05/04 13:13:04 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DDIV extends ArithmeticInstruction {
  public DDIV() {
    super(DDIV);
  }
}

