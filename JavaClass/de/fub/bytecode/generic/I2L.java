package de.fub.bytecode.generic;

/**
 * I2L - Convert int to long
 * <PRE>Stack: ..., value -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: I2L.java,v 1.1.1.1 1999/05/04 13:13:35 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2L extends ConversionInstruction {
  public I2L() {
    super(I2L);
  }
}

