package com.hamco.fastj.test;

public class C2
{
	static void assert(boolean b)
	{
		if (!b)
			throw new RuntimeException();
	}

	// test some basic math operations, lamely
	static void test1()
	{
		System.out.println("Hello");
		System.out.println("Hello" + ' ' + "there");
		System.err.println("STDERR");
		System.out.println(42);
		System.out.println(42.0f);
		System.out.println(C2.class.toString());
		System.out.println(new String("this is a string".getBytes()));
		System.out.print("Current time: ");
		System.out.println(System.currentTimeMillis());
	}
	
	public static void main(String[] args)
	{
		test1();
	}

}
