package de.fub.bytecode.generic;

/** 
 * D2L - Convert double to long
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: D2L.java,v 1.1.1.1 1999/05/04 13:12:59 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class D2L extends ConversionInstruction {
  public D2L() {
    super(D2L);
  }
}

