/*************************************************************************
/* UnresolvedPermission.java -- Placeholder for unresolved permissions.
/*
/* Copyright (c) 1998 Free Software Foundation, Inc.
/* Written by Aaron M. Renn (arenn@urbanophile.com)
/*
/* This library is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, either version 2 of the License, or
/* (at your option) any later verion.
/*
/* This library is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU Library General Public License for more details.
/*
/* You should have received a copy of the GNU Library General Public License
/* along with this library; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/

package java.security;

import java.io.Serializable;

/**
  * This class is used to hold instances of all permissions that cannot
  * be resolved to available permission classes when the security 
  * <code>Policy</code> object is instantiated.  This may happen when the
  * necessary security class has not yet been downloaded from the network.
  * <p>
  * Instances of this class are re-resolved when <code>AccessController</code>
  * check is done.  At that time, a scan is made of all existing
  * <code>UnresolvedPermission</code> objects and they are converted to
  * objects of the appropriate permission type if the class for that type
  * is then available.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class UnresolvedPermission extends Permission implements Serializable
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * The list of actions associated with this permission object
  */
private String actions;

/**
  * The list of <code>Certificates</code> associated with this object
  */
private Certificate[] certs;

/**
  * The name of the class this object should be resolved to.
  */
private String type;
 
/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>UnresolvedPermission</code>
  * with all the information necessary to resolve it to an instance of the
  * proper class at a future time.
  *
  * @param type The name of the desired class this permission should be resolved to
  * @param name The name of this permission
  * @param actions The action list for this permission
  * @param certs The list of certificates this permission's class was signed with
  */
public
UnresolvedPermission(String type, String name, String actions, 
                     Certificate[] certs)
{
  super(name);

  this.type = type;
  this.actions = actions;
  this.certs = certs;
}

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This method returns the list of actions associated with this
  * permission.
  *
  * @return The action list
  */
public String
getActions()
{
  return(actions);
} 

/*************************************************************************/

/**
  * This method returns <code>false</code> always to indicate that this
  * permission does not imply the specified permission.  An 
  * <code>UnresolvedPermission</code> never grants any permissions.
  *
  * @param perm The <code>Permission</code> object to test against - ignored by this class
  *
  * @return <code>false</code> to indicate this permission does not imply the specified permission.
  */
public boolean
implies(Permission perm)
{
  return(false);
}

/*************************************************************************/

/**
  * This method tests this permission for equality against the specified
  * <code>Object</code>.  This will be true if and only if the following
  * conditions are met:
  * <p>
  * <ul>
  * <li>The specified <code>Object</code> is an instance of 
  * <code>UnresolvedPermission</code>, or a subclass.
  * <li>The specified permission has the same type (i.e., desired class name)
  * as this permission.
  * <li>The specified permission has the same name as this one.
  * <li>The specified permissoin has the same action list as this one.
  * <li>The specified permission has the same certificate list as this one.
  * </ul>
  *
  * @param obj The <code>Object</code> to test for equality
  *
  * @return <code>true</code> if the specified object is equal to this one, <code>false</code> otherwise.
  */
public boolean
equals(Object obj)
{
  if (!(obj instanceof UnresolvedPermission))
    return(false);

  UnresolvedPermission up = (UnresolvedPermission)obj;

  if (!getName().equals(up.getName()))
    return(false);

  if (!getActions().equals(up.getActions()))
    return(false);

  if (!type.equals(up.type))
    return(false);

  if (!certs.equals(up.certs))
    return(false);

  return(true);
}

/*************************************************************************/

/**
  * Returns a hash code value for this object.
  *
  * @return A hash value
  */
public int
hashCode()
{
  return(System.identityHashCode(this));
}

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * class.  The format is: '(unresolved "ClassName "name" "actions")'
  *
  * @return A <code>String</code> representation of this object
  */
public String
toString()
{
  return("(unresolved " + type + " " + getName() + " " + getActions() + ")");
}

/*************************************************************************/

/**
  * This class returns a <code>PermissionCollection</code> object that can
  * be used to store instances of <code>UnresolvedPermission</code>.  If
  * <code>null</code> is returned, the caller is free to use any desired
  * <code>PermissionCollection</code>.
  *
  * @return A new <code>PermissionCollection</code>.
  */
public PermissionCollection
newPermissionCollection()
{
  return(null);
}

} // class UnresolvedPermission

