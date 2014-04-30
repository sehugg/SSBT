package de.fub.bytecode.generic;

/** 
 * I2F - Convert int to float
 * <PRE>Stack: ..., value -&gt; ..., result</PRE>
 *
 * @version $Id: I2F.java,v 1.1.1.1 1999/05/04 13:13:35 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class I2F extends ConversionInstruction {
  public I2F() {
    super(I2F);
  }
}

