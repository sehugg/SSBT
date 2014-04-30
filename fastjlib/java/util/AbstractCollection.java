/////////////////////////////////////////////////////////////////////////////
// AbstractCollection.java -- Abstract implementation of most of Collection
//
// Copyright (c) 1998 by Stuart Ballard (stuart.ballard@mcmail.com)
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

import java.lang.reflect.Array;

/**
 * A basic implementation of most of the methods in the Collection interface to
 * make it easier to create a collection. To create an unmodifiable Collection,
 * just subclass AbstractCollection and provide implementations of the
 * iterator() and size() methods. The Iterator returned by iterator() need only
 * provide implementations of hasNext() and next() (that is, it may throw an
 * UnsupportedOperationException if remove() is called). To create a modifiable
 * Collection, you must in addition provide an implementation of the
 * add(Object) method and the Iterator returned by iterator() must provide an
 * implementation of remove(). Other methods should be overridden if the
 * backing data structure allows for a more efficient implementation. The
 * precise implementation used by AbstractCollection is documented, so that
 * subclasses can tell which methods could be implemented more efficiently.
 */
public abstract class AbstractCollection implements Collection {

  /**
   * Return an Iterator over this collection. The iterator must provide the
   * hasNext and next methods and should in addition provide remove if the
   * collection is modifiable.
   */
  public abstract Iterator iterator();

  /**
   * Return the number of elements in this collection.
   */
  public abstract int size();

  /**
   * Add an object to the collection. This implementation always throws an
   * UnsupportedOperationException - it should be overridden if the collection
   * is to be modifiable.
   *
   * @param o the object to add
   * @return true if the add operation caused the Collection to change
   * @exception UnsupportedOperationException if the add operation is not
   *   supported on this collection
   */
  public boolean add(Object o) {
    throw new java.lang.UnsupportedOperationException();
  }

  /**
   * Add all the elements of a given collection to this collection. This
   * implementation obtains an Iterator over the given collection and iterates
   * over it, adding each element with the add(Object) method (thus this method
   * will fail with an UnsupportedOperationException if the add method does).
   *
   * @param c the collection to add the elements of to this collection
   * @return true if the add operation caused the Collection to change
   * @exception UnsupportedOperationException if the add operation is not
   *   supported on this collection
   */
  public boolean addAll(Collection c) {
    Iterator i = c.iterator();
    boolean modified = false;
    while (i.hasNext()) {
      modified |= add(i.next());
    }
    return modified;
  }

  /**
   * Remove all elements from the collection. This implementation obtains an
   * iterator over the collection and calls next and remove on it repeatedly
   * (thus this method will fail with an UnsupportedOperationException if the
   * Iterator's remove method does) until there are no more elements to remove.
   * Many implementations will have a faster way of doing this.
   *
   * @exception UnsupportedOperationException if the Iterator returned by
   *   iterator does not provide an implementation of remove
   */
  public void clear() {
    Iterator i = iterator();
    while (i.hasNext()) {
      i.next();
      i.remove();
    }
  }

  /**
   * Test whether this collection contains a given object. That is, if the
   * collection has an element e such that (o == null ? e == null :
   * o.equals(e)). This implementation obtains an iterator over the collection
   * and iterates over it, testing each element for equality with the given
   * object. If it is equal, true is returned. Otherwise false is returned when
   * the end of the collection is reached.
   *
   * @param o the object to remove from this collection
   * @return true if this collection contains an object equal to o
   */
  public boolean contains(Object o) {
    Iterator i = iterator();

    // This looks crazily inefficient, but it takes the test o==null outside
    // the loop, saving time, and also saves needing to store the result of
    // i.next() each time.
    if (o == null) {
      while (i.hasNext()) {
        if (i.next() == null) {
          return true;
        }
      }
    } else {
      while (i.hasNext()) {
        if (o.equals(i.next())) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Tests whether this collection contains all the elements in a given
   * collection. This implementation iterates over the given collection,
   * testing whether each element is contained in this collection. If any one
   * is not, false is returned. Otherwise true is returned.
   *
   * @param c the collection to test against
   * @return true if this collection contains all the elements in the given
   *   collection
   */
  public boolean containsAll(Collection c) {
    Iterator i = c.iterator();
    while (i.hasNext()) {
      if (!contains(i.next())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Test whether this collection is empty. This implementation returns
   * size() == 0.
   *
   * @return true if this collection is empty.
   */
  public boolean isEmpty() {
    return size() == 0;
  }

  /**
   * Remove a single instance of an object from this collection. That is,
   * remove one element e such that (o == null ? e == null : o.equals(e)), if
   * such an element exists. This implementation obtains an iterator over the
   * collection and iterates over it, testing each element for equality with
   * the given object. If it is equal, it is removed by the iterator's remove
   * method (thus this method will fail with an UnsupportedOperationException
   * if the Iterator's remove method does). After the first element has been
   * removed, true is returned; if the end of the collection is reached, false
   * is returned.
   *
   * @param o the object to remove from this collection
   * @return true if the remove operation caused the Collection to change, or
   *   equivalently if the collection did contain o.
   * @exception UnsupportedOperationException if this collection's Iterator
   *   does not support the remove method
   */
  public boolean remove(Object o) {
    Iterator i = iterator();

    // This looks crazily inefficient, but it takes the test o==null outside
    // the loop, saving time, and also saves needing to store the result of
    // i.next() each time.
    if (o == null) {
      while (i.hasNext()) {
        if (i.next() == null) {
          i.remove();
	  return true;
        }
      }
    } else {
      while (i.hasNext()) {
        if (o.equals(i.next())) {
          i.remove();
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Remove from this collection all its elements that are contained in a given
   * collection. This implementation iterates over this collection, and for
   * each element tests if it is contained in the given collection. If so, it
   * is removed by the Iterator's remove method (thus this method will fail
   * with an UnsupportedOperationException if the Iterator's remove method
   * does).
   *
   * @param c the collection to remove the elements of
   * @return true if the remove operation caused the Collection to change
   * @exception UnsupportedOperationException if this collection's Iterator
   *   does not support the remove method
   */
  public boolean removeAll(Collection c) {
    Iterator i = iterator();
    boolean changed = false;
    while (i.hasNext()) {
      if (c.contains(i.next())) {
        i.remove();
        changed = true;
      }
    }
    return changed;
  }

  /**
   * Remove from this collection all its elements that are not contained in a
   * given collection. This implementation iterates over this collection, and
   * for each element tests if it is contained in the given collection. If not,
   * it is removed by the Iterator's remove method (thus this method will fail
   * with an UnsupportedOperationException if the Iterator's remove method
   * does).
   *
   * @param c the collection to retain the elements of
   * @return true if the remove operation caused the Collection to change
   * @exception UnsupportedOperationException if this collection's Iterator
   *   does not support the remove method
   */
  public boolean retainAll(Collection c) {
    Iterator i = iterator();
    boolean changed = false;
    while (i.hasNext()) {
      if (!c.contains(i.next())) {
        i.remove();
        changed = true;
      }
    }
    return changed;
  }

  /**
   * Return an array containing the elements of this collection. This
   * implementation creates an Object array of size size() and then iterates
   * over the collection, setting each element of the array from the value
   * returned by the iterator.
   *
   * @return an array containing the elements of this collection
   */
  public Object[] toArray() {
    Object[] a = new Object[size()];
    Iterator i = iterator();
    for (int pos = 0; pos < a.length; pos++) {
      a[pos] = i.next();
    }
    return a;
  }

  /**
   * Copy the collection into a given array if it will fit, or into a
   * dynamically created array of the same run-time type as the given array if
   * not. If there is space remaining in the array, the first element after the
   * end of the collection is set to null (this is only useful if the
   * collection is known to contain no null elements, however). This
   * implementation first tests whether the given array is large enough to hold
   * all the elements of the collection. If not, the reflection API is used to
   * allocate a new array of the same run-time type. Next an iterator is
   * obtained over the collection and the elements are placed in the array as
   * they are returned by the iterator. Finally the first spare element, if
   * any, of the array is set to null, and the created array is returned.
   *
   * @param a the array to copy into, or of the correct run-time type
   * @return the array that was produced
   * @exception ClassCastException if the type of the array precludes holding
   *   one of the elements of the Collection
   */
  public Object[] toArray(Object[] a) {
    final int n = size();
    if (a.length < n) {
      a = (Object[])Array.newInstance(a.getClass().getComponentType(), n);
    }
    Iterator i = iterator();
    for (int pos = 0; pos < n; pos++) {
      a[pos] = i.next();
    }
    if (a.length > n) {
      a[n] = null;
    }
    return a;
  }

  /**
   * Creates a String representation of the Collection. The string returned is
   * of the form "[a, b, ...]" where a and b etc are the results of calling
   * toString on the elements of the collection. This implementation obtains an
   * Iterator over the Collection and adds each element to a StringBuffer as it
   * is returned by the iterator.
   *
   * @return a String representation of the Collection
   */
  public String toString() {
    StringBuffer s = new StringBuffer();
    s.append('[');
    Iterator i = iterator();
    boolean more = i.hasNext();
    while(more) {
      s.append(i.next());
      if (more = i.hasNext()) {
        s.append(", ");
      }
    }
    s.append(']');
    return s.toString();
  }
}
