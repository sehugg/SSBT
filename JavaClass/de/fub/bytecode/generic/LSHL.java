package de.fub.bytecode.generic;

/** 
 * LSHL - Arithmetic shift left long
 * <PRE>Stack: ..., value1.word1, value1.word2, value2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: LSHL.java,v 1.1.1.1 1999/05/04 13:14:30 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LSHL extends ArithmeticInstruction {
  public LSHL() {
    super(LSHL);
  }
}

