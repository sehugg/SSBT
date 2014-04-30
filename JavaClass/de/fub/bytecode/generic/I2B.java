package de.fub.bytecode.generic;

/** 
 * I2B - Convert int to byte
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id: I2B.java,v 1.1.1.1 1999/05/04 13:13:32 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2B extends ConversionInstruction {
  public I2B() {
    super(I2B);
  }
}

