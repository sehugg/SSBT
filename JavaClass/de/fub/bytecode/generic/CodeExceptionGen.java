package de.fub.bytecode.generic;

import de.fub.bytecode.Constants;
import de.fub.bytecode.classfile.*;

/** 
 * This class represents an exception handler, i.e. specifies the  region where
 * a handler is active and an instruction where the actual handling is done.
 * pool as parameters.
 *
 * @version $Id: CodeExceptionGen.java,v 1.1.1.1 1999/08/30 13:54:31 hugg Exp $
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     MethodGen
 * @see     CodeException
 */
public final class CodeExceptionGen implements Constants, InstructionTargeter {
  private InstructionHandle start_pc;
  private InstructionHandle end_pc;
  private InstructionHandle handler_pc;
  private ObjectType        catch_type;
  
  /**
   * Add an exception handler, i.e. specify region where a handler is active and an
   * instruction where the actual handling is done.
   *
   * @param start_pc Start of region
   * @param end_pc End of region
   * @param handler_pc Where handling is done
   * @param catch_type which exception is handled
   */
  public CodeExceptionGen(InstructionHandle start_pc, InstructionHandle end_pc,
			  InstructionHandle handler_pc, ObjectType catch_type) {
    setStartPC(start_pc);
    setEndPC(end_pc);
    setHandlerPC(handler_pc);
    this.catch_type = catch_type;
  }

  /**
   * Get CodeException object.
   *
   * This relies on that the instruction list has already been dumped to byte code or
   * or that the `setPositions' methods has been called for the instruction list.
   *
   * @param cp constant pool
   */
  public CodeException getCodeException(ConstantPoolGen cp) {
    return new CodeException(start_pc.getPosition(),
			     end_pc.getPosition(),
			     handler_pc.getPosition(),
			     (catch_type == null)? 0 : cp.addClass(catch_type));
  }

  public void setStartPC(InstructionHandle start_pc) {
    BranchInstruction.notifyTarget(this.start_pc, start_pc, this);
    this.start_pc = start_pc; 
  }

  public void setEndPC(InstructionHandle end_pc) {
    BranchInstruction.notifyTarget(this.end_pc, end_pc, this);
    this.end_pc = end_pc;
  }

  public void setHandlerPC(InstructionHandle handler_pc) {
    BranchInstruction.notifyTarget(this.handler_pc, handler_pc, this);
    this.handler_pc = handler_pc;
  }

  /**
   * @param old_ih old target, either start or end
   * @param new_ih new target
   */
  public void updateTarget(InstructionHandle old_ih, InstructionHandle new_ih) {
    boolean targeted = false;

    if(start_pc == old_ih) {
      targeted = true;
      setStartPC(new_ih);
    }

    if(end_pc == old_ih) {
      targeted = true;
      setEndPC(new_ih);
    }

    if(handler_pc == old_ih) {
      targeted = true;
      setHandlerPC(new_ih);
    }

    if(!targeted)
      throw new ClassGenException("Not targeting " + old_ih + ", but {" + start_pc + ", " +
				  end_pc + ", " + handler_pc + "}");
  }

  /**
   * @return true, if ih is target of this handler
   */
  public boolean containsTarget(InstructionHandle ih) {
    return (start_pc == ih) || (end_pc == ih) || (handler_pc == ih);
  }

  public void              setCatchType(ObjectType catch_type)        { this.catch_type = catch_type; }
  public ObjectType        getCatchType()                             { return catch_type; }
  public InstructionHandle getStartPC()                               { return start_pc; }
  public InstructionHandle getHandlerPC()                             { return handler_pc; }
  public InstructionHandle getEndPC()                                 { return end_pc; }

  public String toString() {
    return "CodeExceptionGen(" + start_pc + ", " + end_pc + ", " + handler_pc + ")";
  }
}
