package com.hamco.fastj;

public class Test
{
	private Integer ai;
	
	public int mathTest()
	{
		int x = 1;
		int y = 17;
		while (y>x)
		{
			x*=y;
			y/=2;
		}
		return y;
	}

	public void loop(Integer a)
	{
		Integer b = null;
		Integer c = null;
		for (int i=0; i<2; i++)
		{
			c = b;
			b = a;
		}
		ai = c;
	}
	
	public void loop2(Integer a)
	{
		if (a == a)
			loop(a);
		else
			a = a;
	}
	
	public void finallyTest()
	{
		try {
			String[] arr = new String[3];
			Object o = arr;
			arr[4] = null;
			arr[5] = null;
			try {
				arr[6] = null;
			} finally {
				System.out.println("goodbye");
			}
		} finally {
			System.out.println("hello");
			/*
			try {
				System.exit(0);
			} finally {
				System.out.println("yo");
			}
			*/
		}
	}
	
	public Object returnNewTest(boolean b)
	{
		Integer c = new Integer(1);
		if (b)
			return c;
		else {
			loop2(c);
			return c;
		}
	}
	
	public Object returnNewTest2()
	{
		return new Test().returnNewTest(true);
	}
	
	public Object typeTest(boolean b)
	{
		Object o = new Integer(3);
		if (b)
		{
			o = new Object();
		}
		return o;
	}
	
	public static void main(String[] args)
	{
		System.out.println(args instanceof Object);
		System.out.println(int.class.getName());
		System.out.println(args.getClass().getName());
	}
}
