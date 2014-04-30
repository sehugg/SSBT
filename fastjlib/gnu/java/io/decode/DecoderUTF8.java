/*************************************************************************
/* DecoderUTF8.java -- Decoder for the UTF-8 character encoding.
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

package gnu.java.io.decode;

import java.io.InputStream;
import java.io.CharConversionException;
import java.io.IOException;

/**
  * This class implements character decoding in the UCS Transformation
  * Format 8 (UTF-8) encoding scheme.  This is defined in RFC-2279.
  * We only handle the 1-3 byte encodings for characters in the Unicode
  * set.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class DecoderUTF8 extends Decoder
{

static
{
  scheme_name = "UTF8";
  scheme_description = "UCS Transformation Format 8 (see RFC-2279)";
}

/*************************************************************************/

/*
 * Constructors
 */

public
DecoderUTF8(InputStream in)
{
  super(in);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Counts the number of characters in a given byte array
  */
public int
charsInByteArray(byte[] buf, int offset, int len) throws CharConversionException
{
  int num_chars = 0;

  // Scan the buffer with minimal validation checks
  for (int i = offset; i < len; i++)
    {
      // Three byte encoding case.
      if ((buf[i] & 0xE0) == 0xE0) // 224
        {
          i += 2;
          if (i >= buf.length)
            throw new CharConversionException("Incomplete input");

          ++num_chars;
        }
      // Two byte encoding case
      else if ((buf[i] & 0xC0) == 0xC0) // 192
        {
          ++i;
          if (i >= buf.length)
            throw new CharConversionException("Incomplete input");

          ++num_chars;
        }
      // One byte encoding case
      else if (buf[i] < 0x80) // 128
        {
          ++num_chars;
        }
      else
        {
          throw new CharConversionException("Bad input encountered: " + buf[i]);
        }
    }

  return(num_chars);
}

/*************************************************************************/

/**
  * Transform the specified UTF8 encoded buffer to Unicode characters
  */
public char[]
convertToChars(byte[] buf, int buf_offset, int len, char cbuf[],
               int cbuf_offset) throws CharConversionException
{
  int val;

  // Scan the buffer with full validation checks
  for (int i = buf_offset; i < len; i++)
    {
      // Three byte encoding case.
      if ((buf[i] & 0xE0) == 0xE0) // 224
        {
          if ((i + 2) >= buf.length)
            throw new CharConversionException("Incomplete input");

          val = (buf[i] & 0x0F) << 12;
          ++i;

          if ((buf[i] & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + buf[i]);
          val |= (buf[i] & 0x3F) << 6; 
          ++i;

          if ((buf[i] & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + buf[i]);
          val |= (buf[i] & 0x3F);
        }
      // Two byte encoding case
      else if ((buf[i] & 0xC0) == 0xC0) // 192
        {
          if ((i + 1) >= buf.length)
            throw new CharConversionException("Incomplete input");

          val = (buf[i] & 0x1F) << 6;
          ++i;

          if ((buf[i] & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + buf[i]);
          val |= (buf[i] & 0x3F);
        }
      // One byte encoding case
      else if (buf[i] < 0x80) // 128
        {
          val = buf[i];
        }
      else
        {
          throw new CharConversionException("Bad input encountered: " + buf[i]);
        }

      cbuf[cbuf_offset] = (char)val;
      ++cbuf_offset;
    }

  return(cbuf);
}

/*************************************************************************/

/**
  * Reads chars from a UTF8 encoded byte stream
  */
public int
read(char[] cbuf, int offset, int len) throws IOException
{
  int val;

  // Note that this method of reading a single byte at a time is 
  // inefficient and should probably be replaced

  for (int i = offset; i < len; i++)
    {
      // Read a byte
      int b = in.read();
      if (b == -1)
        if (i == offset)
          return(-1);
        else
          return(i - offset);

      // Three byte encoding case.
      if ((b & 0xE0) == 0xE0) // 224
        {
          val = (b & 0x0F) << 12;
 
          if ((b = in.read()) == -1)
            throw new CharConversionException("Pre-mature end of input"); 

          if ((b & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + b);
          val |= (b & 0x3F) << 6; 

          if ((b = in.read()) == -1)
            throw new CharConversionException("Pre-mature end of input"); 

          if ((b & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + b);
          val |= (b & 0x3F);
        }
      // Two byte encoding case
      else if ((b & 0xC0) == 0xC0) // 192
        {
          val = (b & 0x1F) << 6;

          if ((b = in.read()) == -1)
            throw new CharConversionException("Pre-mature end of input"); 

          if ((b & 0x80) != 0x80)
            throw new CharConversionException("Bad input byte: " + b);
          val |= (b & 0x3F);
        }
      // One byte encoding case
      else if (b < 0x80) // 128
        {
          val = b;
        }
      else
        {
          throw new CharConversionException("Bad input encountered: " + b);
        }

      cbuf[i] = (char)val;
    }

  return(len);
}

} // class DecoderUTF8

