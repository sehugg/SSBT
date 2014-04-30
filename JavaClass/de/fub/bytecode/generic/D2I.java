package de.fub.bytecode.generic;

/** 
 * D2I - Convert double to int
 * <PRE>Stack: ..., value.word1, value.word2 -&gt; ..., result</PRE>
 *
 * @version $Id: D2I.java,v 1.1.1.1 1999/05/04 13:12:58 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class D2I extends ConversionInstruction {
  public D2I() {
    super(D2I);
  }
}

