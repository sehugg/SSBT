package com.hamco.fastj.test;

import java.util.*;

public class C1
{
	static void assert(boolean b)
	{
		if (!b)
			throw new RuntimeException();
	}

	// test some basic math operations, lamely
	static void test1()
	{
		assert(true);
		assert((1+1)==2);
		assert((1-1)==0);
		assert((2*2)==4);
		assert((4/2)==2);
		assert((4%2)==0);
	}
	
	// test object creation & instanceof
	static void test2()
	{
		Object o = new Object();
		assert(o != null);
		assert(o instanceof Object);
		assert(!(o instanceof Class));
		assert(!(o instanceof Object[]));
		assert(!(null instanceof Object));
		assert(o == o);
		assert(o.equals(o));
	}

	// test array creation and instanceof on arrays
	static void test3()
	{
		Object[] o = new Object[10];
		assert(o != null);
		assert(o == o);
		assert(o.equals(o));
		assert(o instanceof Object);
		assert(o instanceof Object[]);
		assert(!(o instanceof Class[]));
		assert(o.length == 10);
		o = new Object[0];
		assert(o.length == 0);
		int[] i = new int[10];
		i[0] = 1;
		i[1] = 2;
		assert(i[0] == 1);
		assert(i[1] == 2);
		assert(i[0]+i[1] == 3);
		Object[] ii = new Integer[10];
		assert(ii instanceof Integer[]);
		assert(ii instanceof Number[]);
		assert(ii instanceof Object);
		assert(!(ii instanceof Long[]));
	}
	
	// test class casts
	static void test4()
	{
		Object o = null;
		Integer i = (Integer)o;
		o = new Integer(4);
		i = (Integer)o;
		try {
			o = (Long)o;
		} catch (Exception e) {
			i = null;
		}
		assert(i == null);
	}
	
	// test certain native fns
	static void test5()
	{
		byte[] a = { 1,2,3 };
		byte[] b = { 4,5,6 };
		System.arraycopy(b, 0, a, 0, 3);
		assert(a[0]==4 && a[1]==5 && a[2]==6);
		try {
			System.arraycopy(b, 0, a, 0, 4);
		} catch (IndexOutOfBoundsException e) {
			a = null;
		}
		assert(a == null);
		assert(System.identityHashCode(a) == System.identityHashCode(a));
		assert(System.identityHashCode(a) != System.identityHashCode(b));
		long t = System.currentTimeMillis();
		assert(System.currentTimeMillis() >= t);
	}
	
	// test strings
	static void test6()
	{
		String s = "hi there";
		assert(s.length() == 8);
		assert(s.equals("hi there"));
		assert(s.substring(3).equals("there"));
		assert( ("hi" + ' ' + "there").equals(s) );
		assert("".length() == 0);
		s = "foo" + "bar";
		assert(s.intern() == "foobar".intern());
	}
	
	// tests finally
	static void test7()
	{
		int b = 0;
		try {
			Object o = null;
			b++;
			o.equals(o);
			b--;
		} catch (Exception e) {
			b++;
			assert(b==2);
			return;
		} finally {
			try {
				b++;
			} finally {
				assert(b==3);
			}
		}
	}
	
	// test multiarray
	static void test8()
	{
		Integer[][] iii = new Integer[4][8];
		assert(iii != null);
		assert(iii.length == 4);
		assert(iii[0].length == 8);
		assert(iii instanceof Number[][]);
		assert(iii[0] instanceof Integer[]);
		iii[0][0] = new Integer(3);
		assert(iii[0][0] instanceof Integer);
	}
	
	// test interfaces
	static void test9()
	{
		assert (Boolean.FALSE instanceof java.io.Serializable);
		assert ("foo" instanceof java.io.Serializable);
		assert ("foo" instanceof Comparable);
		Vector v = new Vector();
		v.addElement("foo");
		Iterator it = v.iterator();
		assert(it instanceof Iterator);
		assert(it.hasNext());
		assert(it.next().equals("foo"));
	}
	
	// test forName and newInstance()
	static void test10()
	throws Exception
	{
		Class c = Class.forName("java.lang.StringBuffer");
		assert(c != null);
		assert(c.getName().equals("java.lang.StringBuffer"));
		assert(c instanceof Class);
		Object o = c.newInstance();
		assert(o != null);
		assert(o instanceof StringBuffer);
		StringBuffer st = (StringBuffer)o;
		assert(st.length() == 0);
		st.append("foo");
		st.append("bar");
		assert(st.toString().equals("foobar"));
	}

	public static void main(String[] args)
	throws Exception
	{
		test1();
		test2();
		test3();
		test4();
		test5();
		test6();
		test7();
		test8();
		test9();
		test10();
	}

}
