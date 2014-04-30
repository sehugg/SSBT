/*************************************************************************
/* DefaultBreakIterator.java -- Default BreakIterator implementation
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

package java.text;

/**
  * This class provides a concrete subclass implementation of
  * <code>BreakIterator</code> that handles all four types of iteration
  * methods.  Warning!!!  This class really only works for simple
  * English like locales.  It also doesn't handle things like merging
  * consecutive spaces into a single break character.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
class DefaultBreakIterator extends BreakIterator
{

/*************************************************************************/

/*
 * Instance Variables
 */

/**
  * This is the <code>CharacterIterator</code> that holds the text we
  * are iterating over.
  */
private CharacterIterator ci;

/**
  * This is the list of break sequences we are using.
  */
private String[] breaks;

/**
  * This variable indicates whether or not to break both before and
  * after the break sequence.
  */
private boolean before_and_after;

/*************************************************************************/

/*
 * Constructors
 */

/**
  * This constructor defaults this instance to iterating over characters.
  */
public
DefaultBreakIterator()
{
  ;
}

/*************************************************************************/

/**
  * This method initializes to use the specified break sequences and
  * break semantics.
  */
public
DefaultBreakIterator(String[] breaks, boolean before_and_after)
{
  this.breaks = breaks;
  this.before_and_after = before_and_after;
}

/*************************************************************************/

/*
 * Instance Methods
 */

public int
current()
{
  return(ci.getIndex());
}

public int
first()
{
  if (ci.first() == CharacterIterator.DONE)
    return(DONE);
  return(ci.getIndex());
}

public int
last()
{
  if (ci.last() == CharacterIterator.DONE)
    return(DONE);
  return(ci.getIndex());
}

public int
next()
{
  // Handle character case
  if (breaks == null)
    {
      if (ci.next() == CharacterIterator.DONE)
        return(DONE);
      return(ci.getIndex());
    }

  // Handle all other cases
  StringBuffer sb = new StringBuffer("");
  int start_index = ci.getIndex();
  for (;;)
    {
      char c = ci.next();
      if (c == CharacterIterator.DONE)
        return(DONE);

      sb.append(c);

      int i;
      for (i = 0; i < breaks.length; i++)
        {
          if (breaks[i].equals(sb.toString()))
            {
              // Check to see if we already broke at beginning of break seq
              if (before_and_after)
                {
                  if ((ci.getIndex() - start_index) == breaks[i].length())
                    return(ci.getIndex());

                  ci.setIndex(start_index);
                  return(start_index);
                }
              else
                {
                  return(ci.getIndex());
                }
            }

          if (breaks[i].startsWith(sb.toString()))
            break;
        }

      if (i == breaks.length)
        sb = new StringBuffer("");
    }
}

public int
next(int index)
{
  for (int i = 0; i < index; i++)
    if (next() == DONE)
      return(DONE);

  return(current());
}

public int
previous()
{
  // Handle character case
  if (breaks == null)
    {
      if (ci.previous() == CharacterIterator.DONE)
        return(DONE);
      return(ci.getIndex());
    }

  // Handle all other cases
  StringBuffer sb = new StringBuffer("");
  int start_index = ci.getIndex();
  for (;;)
    {
      char c = ci.previous();
      if (c == CharacterIterator.DONE)
        return(DONE);

      sb.insert(0, c);

      int i;
      for (i = 0; i < breaks.length; i++)
        {
          if (breaks[i].equals(sb.toString()))
            {
              // Check to see if we already broke at beginning of break seq
              if (before_and_after)
                {
                  if ((start_index - ci.getIndex()) == breaks[i].length())
                    return(ci.getIndex());

                  ci.setIndex(start_index);
                  return(start_index);
                }
              else
                {
                  return(ci.getIndex());
                }
            }

          if (breaks[i].startsWith(sb.toString()))
            break;
        }

      if (i == breaks.length)
        sb = new StringBuffer("");
    }
}

public int
previous(int index)
{
  for (int i = 0; i < index; i++)
    if (previous() == DONE)
      return(DONE);

  return(current());
}

public int
following(int index)
{
  ci.setIndex(index);
  return(next());
}

public int
preceding(int index)
{
  ci.setIndex(index);
  return(previous());
}

public CharacterIterator
getText()
{
  return(ci);
}

public void
setText(CharacterIterator ci)
{
  this.ci = ci;
}

public boolean
isBoundary(int index)
{
  // Handle character case
  if (breaks == null)
    {
      return(true);
    }

  // Handle other cases
  int save_index = ci.getIndex();
  ci.setIndex(index);
  StringBuffer sb = new StringBuffer("");
  for (;;)
    {
      char c = ci.next();
      if (c == CharacterIterator.DONE)
        return(false);

      sb.append(c);

      int i;
      for (i = 0; i < breaks.length; i++)
        {
          if (breaks[i].equals(sb.toString()))
            {
              ci.setIndex(save_index);
              return(true);
            }

          if (breaks[i].startsWith(sb.toString()))
            break;
        }

      if (i == breaks.length)
        {
          ci.setIndex(save_index);
          return(false);
        }
    }
}

} // class DefaultBreakIterator

