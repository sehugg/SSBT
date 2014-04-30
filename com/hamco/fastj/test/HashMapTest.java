package com.hamco.fastj.test;

import java.util.*;

public class HashMapTest
{
	static void hashtest()
	{
		HashMap hm = new HashMap();
		hm.put("foo", "bar");
		Iterator it = hm.entrySet().iterator();
		it.next();
		hm.put("foo", "baz");
		it.remove();
	}
	
	static void vectest()
	{
		Vector v = new Vector();
		v.addElement("bar");
		//Iterator it = v.iterator();
		Enumeration e = Collections.enumeration(v);
		v.insertElementAt("baz", 0);
		System.out.println(e.nextElement().equals("baz"));
		System.out.println(e.nextElement().equals("bar"));
	}
	
	public static void main(String[] args)
	{
		hashtest();
		vectest();
	}
}
