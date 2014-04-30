/*************************************************************************
/* IllegalArgumentException.java -- exception may be thrown when a method
/* is passed an illegal or inappropriate argument.
/*
/* Copyright (c) 1998 by Free Software Foundation, Inc.
/*
/* This program is free software; you can redistribute it and/or modify
/* it under the terms of the GNU Library General Public License as published 
/* by the Free Software Foundation, version 2. (see COPYING.LIB)
/*
/* This program is distributed in the hope that it will be useful, but
/* WITHOUT ANY WARRANTY; without even the implied warranty of
/* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
/* GNU General Public License for more details.
/*
/* You should have received a copy of the GNU General Public License
/* along with this program; if not, write to the Free Software Foundation
/* Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307 USA
/*************************************************************************/
package java.lang;

/**
 * Exceptions may be thrown by one part of a Java program and caught
 * by another in order to deal with exceptional conditions.  
 * When a method is passed an illegal or inappropriate argument 
 * this exception may be thrown.
 *
 * @since JDK 1.0
 * 
 * @author Brian Jones
 */
public class IllegalArgumentException extends RuntimeException
{
  static final long serialVersionUID = -5365630128856068164L;
  
  /**
   * Create an exception without a message.
   */
  public IllegalArgumentException()
    {
      super();
    }

  /**
   * Create an exception with a message.
   */
  public IllegalArgumentException(String s)
    {
      super(s);
    }
}
