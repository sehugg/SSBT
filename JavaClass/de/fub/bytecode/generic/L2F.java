package de.fub.bytecode.generic;

/**
 * L2F - Convert long to float
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result</PRE>
 *
 * @version $Id: L2F.java,v 1.1.1.1 1999/05/04 13:14:15 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2F extends ConversionInstruction {
  public L2F() {
    super(L2F);
  }
}

