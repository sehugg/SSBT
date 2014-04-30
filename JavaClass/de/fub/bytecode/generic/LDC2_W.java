package de.fub.bytecode.generic;

/** 
 * LDC2_W - Push long or double from constant pool
 *
 * <PRE>Stack: ... -&gt; ..., item.word1, item.word2</PRE>
 *
 * @version $Id: LDC2_W.java,v 1.1.1.1 1999/11/22 09:03:34 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class LDC2_W extends CPInstruction implements PushInstruction {
  /**
   * Empty constructor needed for the Class.newInstance() statement in
   * Instruction.readInstruction(). Not to be used otherwise.
   */
  LDC2_W() {}

  public LDC2_W(int index) {
    super(LDC2_W, index);
  }

  public Type getType(ConstantPoolGen cpg) {
    switch(cpg.getConstantPool().getConstant(index).getTag()) {
    case CONSTANT_Long:   return Type.LONG;
    case CONSTANT_Double: return Type.DOUBLE;
    default: // Never reached
      throw new RuntimeException("Unknown constant type " + tag);
    }
  }
}
