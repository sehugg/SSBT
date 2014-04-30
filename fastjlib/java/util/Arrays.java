/////////////////////////////////////////////////////////////////////////////
// Arrays.java -- Utility class with methods to operate on arrays
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

// TO DO:
// ~ Fix the behaviour of sort and binarySearch as applied to float and double
//   arrays containing NaN values. See the JDC, bug ID 4143272.

package java.util;

/**
 * This class contains various static utility methods performing operations on
 * arrays, and a method to provide a List "view" of an array to facilitate
 * using arrays with Collection-based APIs.
 */
public class Arrays {

  /**
   * This class is non-instantiable.
   */
  private Arrays() {
  }

  /**
   * Perform a binary search of a byte array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(byte[] a, byte key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final byte d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a char array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(char[] a, char key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final char d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a double array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(double[] a, double key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final double d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a float array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(float[] a, float key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final float d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of an int array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(int[] a, int key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final int d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a long array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(long[] a, long key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final long d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of a short array for a key. The array must be
   * sorted (as by the sort() method) - if it is not, the behaviour of this
   * method is undefined, and may be an infinite loop. If the array contains
   * the key more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   */
  public static int binarySearch(short[] a, short key) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final short d = a[mid];
      if (d == key) {
        return mid;
      } else if (d > key) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Compare two objects with or without a Comparator. If c is null, uses the
   * natural ordering. Slightly slower than doing it inline if the JVM isn't
   * clever, but worth it for removing a duplicate of the sort and search code.
   * Note: This same code is used in Collections
   */
  private static int compare(Object o1, Object o2, Comparator c) {
    if (c == null) {
      return ((Comparable)o1).compareTo(o2);
    } else {
      return c.compare(o1, o2);
    }
  }

  /**
   * This method does the work for the Object binary search methods. If the
   * specified comparator is null, uses the natural ordering.
   */
  private static int objectSearch(Object[] a, Object key, final Comparator c) {
    int low = 0;
    int hi = a.length - 1;
    int mid = 0;
    while (low <= hi) {
      mid = (low + hi) >> 1;
      final int d = compare(key, a[mid], c);
      if (d == 0) {
        return mid;
      } else if (d < 0) {
        hi = mid - 1;
      } else {
        low = ++mid; // This gets the insertion point right on the last loop
      }
    }
    return -mid - 1;
  }

  /**
   * Perform a binary search of an Object array for a key, using the natural
   * ordering of the elements. The array must be sorted (as by the sort()
   * method) - if it is not, the behaviour of this method is undefined, and may
   * be an infinite loop. Further, the key must be comparable with every item
   * in the array. If the array contains the key more than once, any one of
   * them may be found. Note: although the specification allows for an infinite
   * loop if the array is unsorted, it will not happen in this (JCL)
   * implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   * @exception ClassCastException if key could not be compared with one of the
   *   elements of a
   * @exception NullPointerException if a null element has compareTo called
   */
  public static int binarySearch(Object[] a, Object key) {
    return objectSearch(a, key, null);
  }

  /**
   * Perform a binary search of an Object array for a key, using a supplied
   * Comparator. The array must be sorted (as by the sort() method with the
   * same Comparator) - if it is not, the behaviour of this method is
   * undefined, and may be an infinite loop. Further, the key must be
   * comparable with every item in the array. If the array contains the key
   * more than once, any one of them may be found. Note: although the
   * specification allows for an infinite loop if the array is unsorted, it
   * will not happen in this (JCL) implementation.
   *
   * @param a the array to search (must be sorted)
   * @param key the value to search for
   * @param c the comparator by which the array is sorted
   * @returns the index at which the key was found, or -n-1 if it was not
   *   found, where n is the index of the first value higher than key or
   *   a.length if there is no such value.
   * @exception ClassCastException if key could not be compared with one of the
   *   elements of a
   */
  public static int binarySearch(Object[] a, Object key, Comparator c) {
    if (c == null) {
      throw new NullPointerException();
    }
    return objectSearch(a, key, c);
  }

  /**
   * Compare two byte arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(byte[] a1, byte[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two char arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(char[] a1, char[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two double arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(double[] a1, double[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two float arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(float[] a1, float[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two long arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(long[] a1, long[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two short arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(short[] a1, short[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two boolean arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(boolean[] a1, boolean[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two int arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a2 is of the same length
   *   as a1, and for each 0 <= i < a1.length, a1[i] == a2[i]
   */
  public static boolean equals(int[] a1, int[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (a1[i] != a2[i]) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Compare two Object arrays for equality.
   *
   * @param a1 the first array to compare
   * @param a2 the second array to compare
   * @returns true if a1 and a2 are both null, or if a1 is of the same length
   *   as a2, and for each 0 <= i < a.length, a1[i] == null ? a2[i] == null :
   *   a1[i].equals(a2[i]).
   */
  public static boolean equals(Object[] a1, Object[] a2) {

    // Quick test which saves comparing elements of the same array, and also
    // catches the case that both are null.
    if (a1 == a2) {
      return true;
    }
    try {

      // If they're the same length, test each element
      if (a1.length == a2.length) {
        for (int i = 0; i < a1.length; i++) {
          if (!(a1[i] == null ? a2[i] == null : a1[i].equals(a2[i]))) {
            return false;
          }
        }
        return true;
      }

    // If a1 == null or a2 == null but not both then we will get a NullPointer
    } catch (NullPointerException e) {
    }

    return false;
  }

  /**
   * Fill an array with a boolean value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(boolean[] a, boolean val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a boolean value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(boolean[] a, int fromIndex, int toIndex,
                          boolean val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a byte value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(byte[] a, byte val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a byte value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(byte[] a, int fromIndex, int toIndex, byte val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a char value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(char[] a, char val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a char value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(char[] a, int fromIndex, int toIndex, char val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a double value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(double[] a, double val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a double value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(double[] a, int fromIndex, int toIndex, double val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a float value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(float[] a, float val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a float value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(float[] a, int fromIndex, int toIndex, float val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with an int value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(int[] a, int val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with an int value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(int[] a, int fromIndex, int toIndex, int val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a long value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(long[] a, long val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a long value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(long[] a, int fromIndex, int toIndex, long val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with a short value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   */
  public static void fill(short[] a, short val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with a short value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   */
  public static void fill(short[] a, int fromIndex, int toIndex, short val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  /**
   * Fill an array with an Object value.
   *
   * @param a the array to fill
   * @param val the value to fill it with
   * @exception ClassCastException if val is not an instance of the element
   *   type of a.
   */
  public static void fill(Object[] a, Object val) {
    // This implementation is slightly inefficient timewise, but the extra
    // effort over inlining it is O(1) and small, and I refuse to repeat code
    // if it can be helped.
    fill(a, 0, a.length, val);
  }

  /**
   * Fill a range of an array with an Object value.
   *
   * @param a the array to fill
   * @param fromIndex the index to fill from, inclusive
   * @param toIndex the index to fill to, exclusive
   * @param val the value to fill with
   * @exception ClassCastException if val is not an instance of the element
   *   type of a.
   */
  public static void fill(Object[] a, int fromIndex, int toIndex, Object val) {
    for (int i = fromIndex; i < toIndex; i++) {
      a[i] = val;
    }
  }

  // Thanks to Paul Fisher <rao@gnu.org> for finding this quicksort algorithm
  // as specified by Sun and porting it to Java.

  /**
   * Sort a byte array into ascending order. The sort algorithm is an optimised
   * quicksort, as described in Jon L. Bentley and M. Douglas McIlroy's
   * "Engineering a Sort Function", Software-Practice and Experience, Vol.
   * 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort.
   *
   * @param a the array to sort
   */
  public static void sort(byte[] a) {
    qsort(a, 0, a.length);
  }

  private static short cmp(byte i, byte j) {
    return (short)(i-j);
  }

  private static int med3(int a, int b, int c, byte[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }
  
  private static void swap(int i, int j, byte[] a) {
    byte c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(byte[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    short r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, byte[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort a char array into ascending order. The sort algorithm is an optimised
   * quicksort, as described in Jon L. Bentley and M. Douglas McIlroy's
   * "Engineering a Sort Function", Software-Practice and Experience, Vol.
   * 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort.
   *
   * @param a the array to sort
   */
  public static void sort(char[] a) {
    qsort(a, 0, a.length);
  }

  private static int cmp(char i, char j) {
    return i-j;
  }

  private static int med3(int a, int b, int c, char[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }
  
  private static void swap(int i, int j, char[] a) {
    char c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(char[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    int r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, char[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort a double array into ascending order. The sort algorithm is an
   * optimised quicksort, as described in Jon L. Bentley and M. Douglas
   * McIlroy's "Engineering a Sort Function", Software-Practice and Experience,
   * Vol. 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort. Note that this implementation, like Sun's, has undefined
   * behaviour if the array contains any NaN values.
   *
   * @param a the array to sort
   */
  public static void sort(double[] a) {
    qsort(a, 0, a.length);
  }

  private static double cmp(double i, double j) {
    return i-j;
  }

  private static int med3(int a, int b, int c, double[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }
  
  private static void swap(int i, int j, double[] a) {
    double c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(double[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    double r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, double[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort a float array into ascending order. The sort algorithm is an
   * optimised quicksort, as described in Jon L. Bentley and M. Douglas
   * McIlroy's "Engineering a Sort Function", Software-Practice and Experience,
   * Vol. 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort. Note that this implementation, like Sun's, has undefined
   * behaviour if the array contains any NaN values.
   *
   * @param a the array to sort
   */
  public static void sort(float[] a) {
    qsort(a, 0, a.length);
  }

  private static float cmp(float i, float j) {
    return i-j;
  }

  private static int med3(int a, int b, int c, float[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }

  private static void swap(int i, int j, float[] a) {
    float c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(float[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    float r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, float[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort an int array into ascending order. The sort algorithm is an optimised
   * quicksort, as described in Jon L. Bentley and M. Douglas McIlroy's
   * "Engineering a Sort Function", Software-Practice and Experience, Vol.
   * 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort.
   *
   * @param a the array to sort
   */
  public static void sort(int[] a) {
    qsort(a, 0, a.length);
  }

  private static long cmp(int i, int j) {
    return (long)i-(long)j;
  }

  private static int med3(int a, int b, int c, int[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }
  
  private static void swap(int i, int j, int[] a) {
    int c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(int[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    long r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, int[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort a long array into ascending order. The sort algorithm is an optimised
   * quicksort, as described in Jon L. Bentley and M. Douglas McIlroy's
   * "Engineering a Sort Function", Software-Practice and Experience, Vol.
   * 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort.
   *
   * @param a the array to sort
   */
  public static void sort(long[] a) {
    qsort(a, 0, a.length);
  }

  // The "cmp" method has been removed from here and replaced with direct
  // compares in situ, to avoid problems with overflow if the difference
  // between two numbers is bigger than a long will hold.
  // One particular change as a result is the use of r1 and r2 in qsort

  private static int med3(int a, int b, int c, long[] d) {
    return d[a] < d[b] ? 
      (d[b] < d[c] ? b : d[a] < d[c] ? c : a)
    : (d[b] > d[c] ? b : d[a] > d[c] ? c : a);
  }
  
  private static void swap(int i, int j, long[] a) {
    long c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(long[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && a[j-1] > a[j]; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    long r1, r2;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r1 = a[pb]) <= (r2 = a[pv])) {
        if (r1 == r2) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r1 = a[pc]) >= (r2 = a[pv])) {
        if (r1 == r2) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, long[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * Sort a short array into ascending order. The sort algorithm is an
   * optimised quicksort, as described in Jon L. Bentley and M. Douglas
   * McIlroy's "Engineering a Sort Function", Software-Practice and Experience,
   * Vol. 23(11) P. 1249-1265 (November 1993). This algorithm gives nlog(n)
   * performance on many arrays that would take quadratic time with a standard
   * quicksort.
   *
   * @param a the array to sort
   */
  public static void sort(short[] a) {
    qsort(a, 0, a.length);
  }

  private static int cmp(short i, short j) {
    return i-j;
  }

  private static int med3(int a, int b, int c, short[] d) {
    return cmp(d[a], d[b]) < 0 ? 
      (cmp(d[b], d[c]) < 0 ? b : cmp(d[a], d[c]) < 0 ? c : a)
    : (cmp(d[b], d[c]) > 0 ? b : cmp(d[a], d[c]) > 0 ? c : a);
  }
  
  private static void swap(int i, int j, short[] a) {
    short c = a[i];
    a[i] = a[j];
    a[j] = c;
  }

  private static void qsort(short[] a, int start, int n) {
    // use an insertion sort on small arrays
    if (n < 7) {
      for (int i = start + 1; i < start + n; i++)
        for (int j = i; j > 0 && cmp(a[j-1], a[j]) > 0; j--)
          swap(j, j-1, a);
      return;
    }

    int pm = n/2;       // small arrays, middle element
    if (n > 7) {
      int pl = start;
      int pn = start + n-1;

      if (n > 40) {     // big arrays, pseudomedian of 9
        int s = n/8;
        pl = med3(pl, pl+s, pl+2*s, a);
        pm = med3(pm-s, pm, pm+s, a);
        pn = med3(pn-2*s, pn-s, pn, a);
      }
      pm = med3(pl, pm, pn, a); // mid-size, med of 3
    }

    int pa, pb, pc, pd, pv;
    int r;

    pv = start; swap(pv, pm, a);
    pa = pb = start;
    pc = pd = start + n-1;
    
    for (;;) {
      while (pb <= pc && (r = cmp(a[pb], a[pv])) <= 0) {
        if (r == 0) { swap(pa, pb, a); pa++; }
        pb++;
      }
      while (pc >= pb && (r = cmp(a[pc], a[pv])) >= 0) {
        if (r == 0) { swap(pc, pd, a); pd--; }
        pc--;
      }
      if (pb > pc) break;
      swap(pb, pc, a);
      pb++;
      pc--;
    }
    int pn = start + n;
    int s;
    s = Math.min(pa-start, pb-pa); vecswap(start, pb-s, s, a);
    s = Math.min(pd-pc, pn-pd-1); vecswap(pb, pn-s, s, a);
    if ((s = pb-pa) > 1) qsort(a, start, s);
    if ((s = pd-pc) > 1) qsort(a, pn-s, s);
  }

  private static void vecswap(int i, int j, int n, short[] a) {
    for (; n > 0; i++, j++, n--)
      swap(i, j, a);
  }

  /**
   * The bulk of the work for the object sort routines. If c is null,
   * uses the natural ordering.
   * In general, the code attempts to be simple rather than fast, the idea
   * being that a good optimising JIT will be able to optimise it better than I
   * can, and if I try it will make it more confusing for the JIT. The
   * exception is in declaring values "final", which I do whenever there is no
   * need for them ever to change - this may give slight speedups.
   */
  private static void mergeSort(Object[] a, final Comparator c) {
    final int n = a.length;
    Object[] x = a;
    Object[] y = new Object[n];
    Object[] t = null; // t is used for swapping x and y

    // The merges are done in this loop
    for (int sizenf = 1; sizenf < n; sizenf <<= 1) {
      final int size = sizenf; // Slightly inelegant but probably speeds us up

      for (int startnf = 0; startnf < n; startnf += size << 1) {
        final int start = startnf; // see above with size and sizenf

        // size2 is the size of the second sublist, which may not be the same
        // as the first if we are at the end of the list.
        final int size2 = n - start - size < size ? n - start - size : size;

        // The second list is empty or the elements are already in order - no
        // need to merge
        if (size2 <= 0 ||
            compare(x[start + size - 1], x[start + size], c) <= 0) {
          System.arraycopy(x, start, y, start, size + size2);

        // The two halves just need swapping - no need to merge
        } else if (compare(x[start], x[start + size + size2 - 1], c) >= 0) {
          System.arraycopy(x, start, y, start + size2, size);
          System.arraycopy(x, start + size, y, start, size2);

        } else {
          // Declare a lot of variables to save repeating calculations.
          // Hopefully a decent JIT will put these in registers and make this
          // fast
          int p1 = start;
          int p2 = start + size;
          int i = start;
          int d1;

	  // initial value added to placate javac
	  int d2 = -1;

          // The main merge loop; terminates as soon as either half is ended
          // You'd think you needed to use & rather than && to make sure d2
          // gets calculated even if d1 == 0, but in fact if this is the case,
          // d2 hasn't changed since the last iteration.
          while ((d1 = start + size - p1) > 0 &&
                 (d2 = start + size + size2 - p2) > 0) {
            y[i++] = x[(compare(x[p1], x[p2], c) <= 0) ? p1++ : p2++];
          }

          // Finish up by copying the remainder of whichever half wasn't
          // finished.
          System.arraycopy(x, d1 > 0 ? p1 : p2, y, i, d1 > 0 ? d1 : d2);
        } 
      }
      t = x; x = y; y = t; // swap x and y ready for the next merge
    }

    // make sure the result ends up back in the right place.
    if (x != a) {
      System.arraycopy(x, 0, a, 0, n);
    }
  }

  /**
   * Sort an array of Objects according to their natural ordering. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(nlog(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @exception ClassCastException if any two elements are not mutually
   *   comparable
   * @exception NullPointerException if an element is null (since
   *   null.compareTo cannot work)
   */
  public static void sort(Object[] a) {
    mergeSort(a, null);
  }

  /**
   * Sort an array of Objects according to a Comparator. The sort is
   * guaranteed to be stable, that is, equal elements will not be reordered.
   * The sort algorithm is a mergesort with the merge omitted if the last
   * element of one half comes before the first element of the other half. This
   * algorithm gives guaranteed O(nlog(n)) time, at the expense of making a
   * copy of the array.
   *
   * @param a the array to be sorted
   * @param c a Comparator to use in sorting the array
   * @exception ClassCastException if any two elements are not mutually
   *   comparable by the Comparator provided
   */
  public static void sort(Object[] a, Comparator c) {

    // Passing null to mergeSort would use the natural ordering. This is wrong
    // by the spec and not in the reference implementation.
    if (c == null) {
      throw new NullPointerException();
    }
    mergeSort(a, c);
  }

  /**
   * Returns a list "view" of the specified array. This method is intended to
   * make it easy to use the Collections API with existing array-based APIs and
   * programs.
   *
   * @param a the array to return a view of
   * @returns a fixed-size list, changes to which "write through" to the array
   */
  public static List asList(final Object[] a) {

    // Check for a null argument
    if (a == null) {
      throw new NullPointerException();
    }

    return new ListImpl( a );
  }


  /**
   * Inner class used by asList(Object[]) to provide a list interface
   * to an array. The methods are all simple enough to be self documenting.
   * Note: When Sun fully specify serialized forms, this class will have to
   * be renamed.
   */
  private static class ListImpl extends AbstractList {

    ListImpl(Object[] a) {
      this.a = a;
    }

    public Object get(int index) {
      return a[index];
    }

    public int size() {
      return a.length;
    }

    public Object set(int index, Object element) {
      Object old = a[index];
      a[index] = element;
      return old;
    }

    private Object[] a;
  }
    
}
