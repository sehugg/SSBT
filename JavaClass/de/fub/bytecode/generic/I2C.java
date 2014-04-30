package de.fub.bytecode.generic;

/** 
 * I2C - Convert int to char
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id: I2C.java,v 1.1.1.1 1999/05/04 13:13:33 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2C extends ConversionInstruction {
  public I2C() {
    super(I2C);
  }
}

