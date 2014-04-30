package com.hamco.fastj;

import de.fub.bytecode.generic.ReferenceType;

public class ReturnAddressType
extends ReferenceType
{
	public static final ReturnAddressType TYPE = new ReturnAddressType();
	
	public ReturnAddressType()
	{
		super(T_OBJECT, "<return address>");
	}

	public boolean equals(Object o)
	{
		return (o instanceof ReturnAddressType);
	}
	
}
