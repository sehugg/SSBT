package de.fub.bytecode.generic;

/**
 * I2S - Convert int to short
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id: I2S.java,v 1.1.1.1 1999/05/04 13:13:36 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2S extends ConversionInstruction {
  public I2S() {
    super(I2S);
  }
}

