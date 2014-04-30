package com.hamco.fastj;

import de.fub.bytecode.generic.ReferenceType;

public class UnknownType
extends ReferenceType
{
	public static final UnknownType TYPE = new UnknownType();
	
	public UnknownType()
	{
		super(T_OBJECT, "<UNKNOWN>");
	}

	public boolean equals(Object o)
	{
		return (o instanceof UnknownType);
	}
	
}
