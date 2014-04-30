package de.fub.bytecode.generic;

/** 
 * DADD - Add doubles
 * <PRE>Stack: ..., value1.word1, value1.word2, value2.word1, value2.word2 -&gt;</PRE>
 *        ..., result.word1, result1.word2
 *
 * @version $Id: DADD.java,v 1.1.1.1 1999/05/04 13:12:59 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class DADD extends ArithmeticInstruction {
  public DADD() {
    super(DADD);
  }
}

