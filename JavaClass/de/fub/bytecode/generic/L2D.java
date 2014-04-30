package de.fub.bytecode.generic;

/** 
 * L2D - Convert long to double
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: L2D.java,v 1.1.1.1 1999/05/04 13:14:15 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2D extends ConversionInstruction {
  public L2D() {
    super(L2D);
  }
}

