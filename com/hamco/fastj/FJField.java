package com.hamco.fastj;

import java.io.*;
import de.fub.bytecode.*;
import de.fub.bytecode.classfile.*;
import de.fub.bytecode.generic.*;


class FJField
{
	FJClass c;
	Field f;
	FieldGen fg;
	Type type;
	int size;
	int offset;
		
	FJField(FJClass c, Field f)
	{
		this.c = c;
		this.f = f;
		fg = new FieldGen(f, c.jcg.getConstantPool());
		type = fg.getType();
		size = type.getSize()*4; // todo: wanky
		if (!fg.isStatic())
		{
			offset = c.size;
			c.size += size;
			//System.err.println("offset of " + type + " " + f.getName() + " = " + offset);
		}
	}
	
	public String getMemberName()
	{
		return c.ctx.field2ident(f.getName());
	}
	
	// we only allow int, boolean, char, short, byte, float, double, or NULL to be const
	// todo: right?
	public boolean isConst()
	{
		return (f.isFinal() && f.getConstantValue() != null &&
			((type instanceof BasicType) && !type.equals(Type.LONG)) );
	}
	
	public Constant getConstantValue()
	{
		ConstantValue v = f.getConstantValue();
		if (v == null)
			return null;
		Constant cons = v.getConstantPool().getConstant(v.getConstantValueIndex());
		return cons;
	}

	// todo: move it out	
	// todo: set unit'ed static vars to 0 (or NULL)
	public void outputPrototype(PrintStream out, boolean inlined)
	{
		// todo: problem when final but not static
		if (inlined && (f.isStatic() || isConst()))
			out.print("static ");
		if (isConst())
			out.print("const ");
		out.print(c.ctx.type2str(type));
		out.print(' ');
		if (!inlined)
		{
			out.print(c.getStructName());
			out.print("::");
		}
		out.print(getMemberName());
		// output it inlined when it is const (basic type + final)
		// output it outside when it is not
		if (inlined ^ !isConst())
		{
			Constant cons = getConstantValue();
			if (cons != null)
			{
				out.print(" = ");
				out.print(FJC_ClassGen.constantToString(cons));
			}
		}
		out.println(';');
	}
}
	
