package de.fub.bytecode.classfile;

/**
 * Traverses a JavaClass with another Visitor object 'piggy-backed'
 * that is applied to all components of a JavaClass object. I.e. this
 * class supplies the traversal strategy, other classes can make use
 * of it.
 *
 * @version $Id: DefaultVisitor.java,v 1.1.1.1 1999/05/04 13:12:14 hugg Exp $
 * @author <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A> 
 */
public class DefaultVisitor implements Visitor {
  private JavaClass clazz;
  private Visitor   visitor;

  /**
   * @param clazz Class to traverse
   * @param visitor visitor object to apply to all components
   */
  public DefaultVisitor(JavaClass clazz, Visitor visitor) {
    this.clazz   = clazz;
    this.visitor = visitor;
  }

  /**
   * Start traversal.
   */
  public void visit() { clazz.accept(this); }

  public void visitJavaClass(JavaClass clazz) {
    clazz.accept(visitor);

    Field[] fields = clazz.getFields();
    for(int i=0; i < fields.length; i++)
      fields[i].accept(this);

    Method[] methods = clazz.getMethods();
    for(int i=0; i < methods.length; i++)
      methods[i].accept(this);

    Attribute[] attributes = clazz.getAttributes();
    for(int i=0; i < attributes.length; i++)
      attributes[i].accept(this);

    clazz.getConstantPool().accept(this);
  }

  public void visitField(Field field) {
    field.accept(visitor);
    
    Attribute[] attributes = field.getAttributes();
    for(int i=0; i < attributes.length; i++)
      attributes[i].accept(this);
  }

  public void visitConstantValue(ConstantValue cv) { cv.accept(visitor); }

  public void visitMethod(Method method) {
    method.accept(visitor);
    
    Attribute[] attributes = method.getAttributes();
    for(int i=0; i < attributes.length; i++)
      attributes[i].accept(this);
  }

  public void visitExceptionTable(ExceptionTable table) { table.accept(visitor);  }

  public void visitCode(Code code) {
    code.accept(visitor);

    CodeException[] table = code.getExceptionTable();
    for(int i=0; i < table.length; i++)
      table[i].accept(this);

    Attribute[] attributes = code.getAttributes();
    for(int i=0; i < attributes.length; i++)
      attributes[i].accept(this);
  }

  public void visitCodeException(CodeException ce) { ce.accept(visitor); }

  public void visitLineNumberTable(LineNumberTable table) {
    table.accept(visitor);

    LineNumber[] numbers = table.getLineNumberTable();
    for(int i=0; i < numbers.length; i++)
      numbers[i].accept(this);
  }

  public void visitLineNumber(LineNumber number) { number.accept(visitor); }

  public void visitLocalVariableTable(LocalVariableTable table) {
    table.accept(visitor);

    LocalVariable[] vars = table.getLocalVariableTable();
    for(int i=0; i < vars.length; i++)
      vars[i].accept(this);
  }

  public void visitLocalVariable(LocalVariable var) { var.accept(visitor); }

  public void visitConstantPool(ConstantPool cp) {
    cp.accept(visitor);

    Constant[] constants = cp.getConstantPool();
    for(int i=1; i < constants.length; i++) {
      if(constants[i] != null)
	constants[i].accept(this);
    }
  }

  public void visitConstantClass(ConstantClass constant) { constant.accept(visitor); }
  public void visitConstantDouble(ConstantDouble constant) { constant.accept(visitor); }
  public void visitConstantFieldref(ConstantFieldref constant) { constant.accept(visitor); }
  public void visitConstantFloat(ConstantFloat constant) { constant.accept(visitor); }
  public void visitConstantInteger(ConstantInteger constant) { constant.accept(visitor); }
  public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref constant) {
    constant.accept(visitor);
  }
  public void visitConstantLong(ConstantLong constant) { constant.accept(visitor); }
  public void visitConstantMethodref(ConstantMethodref constant) {
    constant.accept(visitor); 
  }
  public void visitConstantNameAndType(ConstantNameAndType constant) {
    constant.accept(visitor);
  }
  public void visitConstantString(ConstantString constant) { constant.accept(visitor); }
  public void visitConstantUtf8(ConstantUtf8 constant) { constant.accept(visitor); }

  public void visitInnerClasses(InnerClasses ic) {
    ic.accept(visitor);

    InnerClass[] ics = ic.getInnerClasses();
    for(int i=0; i < ics.length; i++)
      ics[i].accept(this);
  }

  public void visitInnerClass(InnerClass inner) { inner.accept(visitor); }

  public void visitDeprecated(Deprecated attribute) { attribute.accept(visitor); }
  public void visitSourceFile(SourceFile attribute) { attribute.accept(visitor); }
  public void visitSynthetic(Synthetic attribute) { attribute.accept(visitor); }
  public void visitUnknown(Unknown attribute) { attribute.accept(visitor); }
}
