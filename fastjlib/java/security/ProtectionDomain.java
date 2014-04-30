/*************************************************************************
/* ProtectionDomain.java -- A security domain
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

/**
  * This class represents a group of classes, along with the permissions
  * they are granted.  The classes are identified by a <code>CodeSource</code>.
  * Thus, any class loaded from the specified <code>CodeSource</code> is
  * treated as part of this domain.  The set of permissions is represented
  * by a <code>PermissionCollection</code>.
  * <p>
  * Every class in the system will belong to one and only one
  * <code>ProtectionDomain</code>.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class ProtectionDomain
{

/*************************************************************************/

/*
 * Class Variables
 */

private static final String linesep = System.getProperty("line.separator");

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the <code>CodeSource</code> for this protection domain
  */
private CodeSource code_source;

/**
  * This is the set of permissions granted to this domain
  */
private PermissionCollection perms;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This method initializes a new instance of <code>ProtectionDomain</code>
  * representing the specified <code>CodeSource</code> and permission set.
  *
  * @param code_source The <code>CodeSource</code> for this domain
  * @param perms The permission set for this domain
  */
public
ProtectionDomain(CodeSource code_source, PermissionCollection perms)
{
  this.code_source = code_source;
  this.perms = perms;
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * This method returns the <code>CodeSource</code> for this domain.
  *
  * @return This domain's <code>CodeSource</code>.
  */
public final CodeSource
getCodeSource()
{
  return(code_source);
}

/*************************************************************************/

/**
  * This method returns the set of permissions granted to this domain.
  *
  * @return The permission set for this domain
  */
public final PermissionCollection
getPermissions()
{
  return(perms);
}

/*************************************************************************/

/**
  * This method tests whether or not the specified <code>Permission</code> is
  * implied by the set of permissions granted to this domain.
  *
  * @param perm The <code>Permission</code> to test.
  *
  * @return <code>true</code> if the specified <code>Permission</code> is implied for this domain, <code>false</code> otherwise.
  */
public boolean
implies(Permission perm)
{
  PermissionCollection pc = getPermissions();
  if (pc == null)
    return(false);

  return(pc.implies(perm));
}

/*************************************************************************/

/**
  * This method returns a <code>String</code> representation of this
  * object.  It will print the <code>CodeSource</code> and 
  * permission set associated with this domain.
  *
  * @return A <code>String</code> representation of this object.
  */
public String
toString()
{
  StringBuffer sb = new StringBuffer("");

  sb.append(super.toString() + " (" + linesep);
  sb.append(code_source.toString());
  sb.append(perms.toString());
  sb.append(")" + linesep);

  return(sb.toString());
}

} // class ProtectionDomain

