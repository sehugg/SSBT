/*************************************************************************
/* EncoderUTF8.java -- Encoding class for the UTF-8 scheme
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

package gnu.java.io.encode;

import java.io.OutputStream;
import java.io.IOException;

/**
  * This class implements an encoder for the UCS Transformation Format 8
  * (UTF-8) encoding scheme.  This is defined in RFC-2279.  We only handle
  * the 1-3 byte encodings for characters in the Unicode set.
  *
  * @version 0.0
  *
  * @author Aaron M. Renn (arenn@urbanophile.com)
  */
public class EncoderUTF8 extends Encoder
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
EncoderUTF8(OutputStream out)
{
  super(out);
}

/*************************************************************************/

/*
 * Instance Methods
 */

/**
  * Returns the number of bytes the specified char array will be encoded
  * into
  */
public int
bytesInCharArray(char[] buf, int offset, int len)
{
  int num_bytes = 0;

  for (int i = offset; i < len; i++)
    {
      if (buf[i] <= 0x007F)
        ++num_bytes;
      else if (buf[i] <= 0x07FF)
        num_bytes += 2;
      else
        num_bytes += 3;
    }

  return(num_bytes);
}

/*************************************************************************/

/**
  * This method converts a char array to bytes
  */
public byte[]
convertToBytes(char[] buf, int buf_offset, int len, byte[] bbuf,
               int bbuf_offset)
{
  for (int i = buf_offset; i < len; i++)
    {
      if (buf[i] <= 0x007F)
        {
          bbuf[bbuf_offset] = (byte)(buf[i] & 0xFF);
          ++bbuf_offset;
        }
      else if (buf[i] <= 0x07FF)
        {
          bbuf[bbuf_offset] = (byte)(0xC0 | (buf[i] >> 6));
          ++bbuf_offset;
          bbuf[bbuf_offset] = (byte)(0x80 | (buf[i] & 0x3F));
          ++bbuf_offset;
        }
      else 
        {
          bbuf[bbuf_offset] = (byte)(0xE0 | (buf[i] >> 12));
          ++bbuf_offset;
          bbuf[bbuf_offset] = (byte)(0xC0 | (buf[i] >> 6));
          ++bbuf_offset;
          bbuf[bbuf_offset] = (byte)(0x80 | (buf[i] & 0x3F));
          ++bbuf_offset;
        }
    }

  return(bbuf);
}

/*************************************************************************/

/**
  * Writes a char array as bytes to the underlying stream.
  */
public void
write(char[] buf, int offset, int len) throws IOException
{
  byte[] bbuf = new byte[bytesInCharArray(buf, offset, len)];
  convertToBytes(buf, offset, len, bbuf, 0);
  out.write(bbuf);
}

} // class EncoderUTF8

