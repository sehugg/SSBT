package com.hamco.fastj.test;

public class Test3
{
	public void foo(boolean boo)
	{
		Number n;
		if (boo)
			n = new Integer(3);
		else
			n = new Integer(4);
		System.out.println(n.toString());
		try {
			System.out.println(n.intValue());
		} catch (Error e) {
			throw new RuntimeException();
		}
	}
}
