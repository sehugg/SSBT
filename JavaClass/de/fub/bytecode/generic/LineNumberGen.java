package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;
import de.fub.bytecode.classfile.*;

/** 
 * This class represents a line number within a method, i.e. give an instruction
 * a line number corresponding to the source code line.
 *
 * @version $Id: LineNumberGen.java,v 1.1.1.1 1999/08/13 08:18:33 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     LineNumber
 * @see     MethodGen
 */
public class LineNumberGen {
  private InstructionHandle ih;
  private int               src_line;

  /**
   * Create a line number.
   *
   * @param ih instruction to tag
   * @return new line number object
   */
  public LineNumberGen(InstructionHandle ih, int src_line) {
    this.ih       = ih;
    this.src_line = src_line;
  }

  /**
   * Get LineNumber object.
   *
   * This relies on that the instruction list has already been dumped to byte code or
   * or that the `setPositions' methods has been called for the instruction list.
   *
   * @param cp constant pool
   */
  public LineNumber getLineNumber(ConstantPoolGen cp) {
    return new LineNumber(ih.getPosition(), src_line);
  }

  public void              setInstruction(InstructionHandle ih) { this.ih = ih; }
  public InstructionHandle getInstruction()                     { return ih; }
  public void              setSourceLine(int src_line)    { this.src_line = src_line; }
  public int               getSourceLine()                { return src_line; }
}
