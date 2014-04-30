package de.fub.bytecode.generic;

/**
 * L2I - Convert long to int
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result</PRE>
 *
 * @version $Id: L2I.java,v 1.1.1.1 1999/05/04 13:14:16 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class L2I extends ConversionInstruction {
  public L2I() {
    super(L2I);
  }
}

