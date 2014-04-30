/////////////////////////////////////////////////////////////////////////////
// Bucket.java -- a class providing a hash-bucket data structure (a lightweight
//                linked list)
//
// Copyright (c) 1998 by Jon A. Zeppieri (jon@eease.com)
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU Library General Public License as published
// by the Free Software Foundation, version 2. (see COPYING.LIB)
//
// This program is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU Library General Public License for more details.
//
// You should have received a copy of the GNU Library General Public License
// along with this program; if not, write to the Free Software Foundation
// Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/////////////////////////////////////////////////////////////////////////////

package java.util;

/**
 * a class representing a simple, lightweight linked-list, using Node
 * objects as its linked nodes; this is used by Hashtable and HashMap
 *
 * @author        Jon Zeppieri
 * @version       $Revision: 1.2 $
 * @modified      $Id: Bucket.java,v 1.2 2000/02/29 20:34:28 hugg Exp $
 */
class Bucket
{
    /** the first node of the lined list, originally null */
    Node first;
    
    /** trivial constructor for a Bucket */
    Bucket()
    {
    }
    
    /** add this key / value pair to the list
     *
     * @param    entry    a Map.Entry object to be added to this list
     */
    Map.Entry add(Node newNode)
    {
	Object oKey;
	Object oTestKey = newNode.getKey();
	Node it = first;
	Node prev = null;
	if (it == null) // if the list is empty (the ideal case), we make a new single-node list
	    {
		first = newNode;
		return null;
	    }
	else // otherwise try to find where this key already exists in the list,
	    {// and if it does, replace the value with the new one (and return the old one)
		while (it != null)  
		    {
			oKey = it.getKey();
			if ((oKey == null) ? (oTestKey == null) :
			    oKey.equals(oTestKey))
			    {
			    	it.value = newNode.getValue();
					return it;
			    }
			prev = it;  
			it = it.next;
		    }
		prev.next = newNode; // otherwise, just stick this at the 
		return null;                   // end of the list
	    }
    }
    
    /**
     * remove a Map.Entry in this list with the supplied key and return its value,
     * if it exists, else return null
     *
     * @param     key       the key we are looking for in this list
     */
    Object removeByKey(Object key)
    {
	Object oEntryKey;
	Node prev = null;
	Node it = first;
	while (it != null)
	    {
		oEntryKey = it.getKey();
		if ((oEntryKey == null) ? (key == null) : oEntryKey.equals(key))
		    {
			if (prev == null) // we are removing the first element
			    first = it.next;
			else
			    prev.next = it.next;
			return it.getValue();
		    }
		else
		    {
			prev = it;
			it = it.next;
		    }
	    }
	return null;
    }
    
    /**
     * return the value which the supplied key maps to, if it maps to anything in this list,
     * otherwise, return null
     *
     * @param      key       the key mapping to a value that we are looking for
     */
    Object getValueByKey(Object key)
    {
	Node entry = getEntryByKey(key);
	return (entry == null) ? null : entry.getValue();
    }

    /**
     * return the Map.Entry which the supplied key is a part of, if such a Map.Entry exists,
     * null otherwise
     *
     * this method is important for HashMap, which can hold null values and the null key
     *
     * @param      key         the key for which we are finding the corresponding Map.Entry
     */
    Node getEntryByKey(Object key)
    {
	Object oEntryKey;
	Node it = first;
	while (it != null)
	    {
		oEntryKey = it.getKey();
		if ((oEntryKey == null) ? (key == null) : oEntryKey.equals(key))
		    return it;
		it = it.next;
	    }
	return null;
    }
    
    /**
     * return true if this list has a Map.Entry whose value equals() the supplied value
     *
     * @param      value         the value we are looking to match in this list
     */
    boolean containsValue(Object value)
    {
	Object oEntryValue;
	Node it = first;
	while (it != null)
	    {
		oEntryValue = it.getValue();
		if ((oEntryValue == null) ? (value == null) : oEntryValue.equals(value))
		    return true;
		it = it.next;
	    }
	return false;
    }

    // INNSER CLASSES ----------------------------------------------------------
    
    /**
     * a class represnting a node in our lightweight linked-list
     * that we use for hash buckets;  a Node object contains a Map.Entry as its
     * <pre>value</pre> property and a reference (possibly, even hopefully, null) 
     * to another Node as its <pre>next</pre> property.
     *
     * There <i>is</i> a reason for not using a highly generic "LinkedNode" type
     * class:  we want to eliminate runtime typechecks.
     *
     * @author       Jon Zeppieri
     * @version      $Revision: 1.2 $
     * @modified     $Id: Bucket.java,v 1.2 2000/02/29 20:34:28 hugg Exp $
     */
    static class Node extends BasicMapEntry implements Map.Entry
    {
	/** a reference to the next node in the linked list */
	Node next;
	
	/** non-trivial contructor -- sets the <pre>value</pre> of the Bucket upon instantiation */
	Node(Object key, Object value)
	{
	    super(key, value);
	}

	
    }
    // EOF ------------------------------------------------------------------------
}
