/*
 * java.util.StringTokenizer: part of the Java Class Libraries project.
 * Copyright (C) 1998 Jochen Hoenicke
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

package java.util;

/**
 * This class splits a string into tokens.  The caller can set on which 
 * delimiters the string should be split and if the delimiters should be
 * returned.
 *
 * You may change the delimiter set on the fly by calling
 * nextToken(String).  But the semantic is quite difficult; it even
 * depends on calling <code>hasMoreTokens()</code>.  You should call
 * <code>hasMoreTokens()</code> before, otherwise the old delimiters
 * after the last token are returned.
 *
 * If you want to get the delimiters, you have to use the three argument
 * constructor.  The delimiters are returned as token consisting of a
 * single character.  
 *
 * @author Jochen Hoenicke */
public class StringTokenizer implements Enumeration {

    /**
     * The position in the str, where we currently are.
     */
    private int pos;
    /**
     * The string that should be split into tokens.
     */
    private String str;
    /**
     * The string containing the delimiter characters.
     */
    private String delim;
    /**
     * Tells, if we should return the delimiters.
     */
    private boolean retDelims;

    /*{ 
        invariant {
            pos >= 0 :: "position is negative";
	    pos <= str.length() :: "position is out of string";
	    str != null :: "String is null";
	    delim != null :: "Delimiters are null";
        }
    }*/


    /**
     * Creates a new StringTokenizer for the string <code>str</code>,
     * that should split on the default delimiter set (space, tap,
     * newline, return and formfeed), and which doesn't return the
     * delimiters.
     * @param str The string to split.
     */
    public StringTokenizer(String str)
        /*{ require { str != null :: "str must not be null"; } }*/
    {
        this(str, " \t\n\r\f", false);
    }

    /**
     * Create a new StringTokenizer, that splits the given string on 
     * the given delimiter characters.  It doesn't return the delimiter
     * characters.
     *
     * @param str The string to split.
     * @param delim A string containing all delimiter characters.
     */
    public StringTokenizer(String str, String delim)
        /*{ require { str != null :: "str must not be null";
	              delim != null :: "delim must not be null"; } }*/
    {
        this(str, delim, false);
    }

    /**
     * Create a new StringTokenizer, that splits the given string on
     * the given delimiter characters.  If you set
     * <code>returnDelims</code> to <code>true</code>, the delimiter
     * characters are returned as tokens of their own.  The delimiter
     * tokens always consist of a single character.
     *
     * @param str The string to split.
     * @param delim A string containing all delimiter characters.
     * @param returnDelims Tells, if you want to get the delimiters.
     */
    public StringTokenizer(String str, String delim, boolean returnDelims)
        /*{ require { str != null :: "str must not be null";
	              delim != null :: "delim must not be null"; } }*/
    {
        this.str = str;
        this.delim = delim;
        this.retDelims = returnDelims;
        this.pos = 0;
    }

    /**
     * Tells if there are more tokens.
     * @return True, if the next call of nextToken() succeeds, false otherwise.
     */
    public boolean hasMoreTokens() {
        if (!retDelims) {
            while (pos < str.length()
                   && delim.indexOf(str.charAt(pos)) > -1) {
                pos++;
            }
        }
        return pos < str.length();
    }

    /**
     * Returns the nextToken, changing the delimiter set to the given
     * <code>delim</code>.  The change of the delimiter set is
     * permanent, ie. the next call of nextToken(), uses the same
     * delimiter set.
     * @param delim a string containing the new delimiter characters.
     * @return the next token with respect to the new delimiter characters.
     * @exception NoSuchElementException if there are no more tokens.
     */
    public String nextToken(String delim) throws NoSuchElementException 
        /*{ require { hasMoreTokens() :: "no more Tokens available";
	    ensure { $return != null && $return.length() > 0; } }*/
    {
        this.delim = delim;
        return nextToken();
    }

    /**
     * Returns the nextToken of the string.
     * @param delim a string containing the new delimiter characters.
     * @return the next token with respect to the new delimiter characters.
     * @exception NoSuchElementException if there are no more tokens.
     */
    public String nextToken() throws NoSuchElementException
        /*{ require { hasMoreTokens() :: "no more Tokens available";
	    ensure { $return != null && $return.length() > 0; } }*/
    {
        if (pos < str.length() 
            && delim.indexOf(str.charAt(pos)) > -1) {
            if (retDelims)
                return str.substring(pos, ++pos);

            while (++pos < str.length() 
                   && delim.indexOf(str.charAt(pos)) > -1) {
                /* empty */
            }
        }
        if (pos < str.length()) {
            int start = pos;
            while (++pos < str.length()
                   && delim.indexOf(str.charAt(pos)) == -1) {
                /* empty */
            }
            return str.substring(start, pos);
        }
	throw new NoSuchElementException();
    }

    /**
     * This does the same as hasMoreTokens. This is the
     * <code>Enumeration</code interface method.
     * @return True, if the next call of nextElement() succeeds, false
     * otherwise.  
     * @see #hasMoreTokens
     */
    public boolean hasMoreElements() {
        return hasMoreTokens();
    }

    /**
     * This does the same as nextTokens. This is the
     * <code>Enumeration</code interface method.
     * @return the next token with respect to the new delimiter characters.
     * @exception NoSuchElementException if there are no more tokens.
     * @see #nextToken
     */
    public Object nextElement() throws NoSuchElementException {
        return nextToken();
    }

    /**
     * This counts the number of remaining tokens in the string, with
     * respect to the current delimiter set.
     * @return the number of times <code>nextTokens()</code> will
     * succeed.  
     * @see #nextToken
     */
    public int countTokens() {
        int count = 0;
        int tmpPos = pos;
        while (tmpPos < str.length()) {
            if (delim.indexOf(str.charAt(tmpPos)) > -1) {
                if (retDelims) {
                    count++;
                    continue;
                }
                
                while (++tmpPos < str.length() 
                       && delim.indexOf(str.charAt(tmpPos)) > -1) {
                    /* empty */
                }
            }
            if (tmpPos < str.length()) {
                while (++tmpPos < str.length()
                       && delim.indexOf(str.charAt(tmpPos)) == -1) {
                    /* empty */
                }
                count++;
            }
        }
        return count;
    }
}
