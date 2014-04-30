/*************************************************************************
 * Cloneable.java -- Interface for implementing a method to override 
 * Object.clone()comparaing objects to obtain an ordering
 *
 * Copyright (c) 1998 by Free Software Foundation, Inc.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Library General Public License as published 
 * by the Free Software Foundation, version 2. (see COPYING.LIB)
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation
 * Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
 *************************************************************************/

package java.lang;

/**
 * This interface should be implemented by classes wishing to
 * support or override Object.clone().  Cloning an object generally
 * results in a deep copy of that object.  If Object.clone() is called
 * on an object which does not implement the Cloneable interface,
 * a CloneNotSupportedException will be thrown.
 *
 * @see java.lang.Object#clone
 * @see java.lang.CloneNotSupportedException
 */
public abstract interface Cloneable { }
