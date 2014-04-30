package de.fub.bytecode.generic;

/**
 * I2D - Convert int to double
 * <PRE>Stack: ..., value -&gt; ..., result.word1, result.word2</PRE>
 *
 * @version $Id: I2D.java,v 1.1.1.1 1999/05/04 13:13:34 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2D extends ConversionInstruction {
  public I2D() {
    super(I2D);
  }
}

