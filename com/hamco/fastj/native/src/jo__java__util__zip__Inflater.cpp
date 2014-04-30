#include "fastj.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__util__zip__Inflater.hpp"
#include "jo__java__util__zip__DataFormatException.hpp"
#include "jo__java__lang__InternalError.hpp"

#include <zlib.h>

static jboolean flush_flag;

FJ_NATIVE void jo__java__util__zip__Inflater::jm__end(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native void end()\n");

}

FJ_NATIVE jint jo__java__util__zip__Inflater::jm__getAdler(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native int getAdler()\n");

}

FJ_NATIVE jint jo__java__util__zip__Inflater::jm__getRemaining(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native int getRemaining()\n");

}

FJ_NATIVE jint jo__java__util__zip__Inflater::jm__getTotalIn(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native int getTotalIn()\n");

}

FJ_NATIVE jint jo__java__util__zip__Inflater::jm__getTotalOut(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native int getTotalOut()\n");

}

FJ_NATIVE jint jo__java__util__zip__Inflater::jm__inflate(jo__java__util__zip__Inflater* _this, jarray< jbyte >* jp__0, jint jp__1, jint jp__2)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native int inflate(byte[], int, int)
		throws java.util.zip.DataFormatException\n");

}

FJ_NATIVE void jo__java__util__zip__Inflater::jm__init(jo__java__util__zip__Inflater* _this, jboolean no_header)
{
  z_stream_s *stream = (z_stream_s *) malloc(sizeof (z_stream_s));
  stream->next_in = Z_NULL;
  stream->avail_in = 0;
  stream->zalloc = 0;
  stream->zfree = 0;
  stream->opaque = NULL;

  // Handle NO_HEADER using undocumented zlib feature.
  int wbits = MAX_WBITS;
  if (no_header)
    wbits = - wbits;

  if (inflateInit2 (stream, wbits) != Z_OK)
    {
      jo__java__lang__String* msg = NULL;
      if (stream->msg != NULL)
      	msg = FJ_MAKESTRING(stream->msg);
      FJ_THROWNEWMSG(jo__java__lang__InternalError, msg);
    }

  _this->jf__zstream = (jint)stream;
  _this->jf__is__finished = false;
  flush_flag = 0;
}

FJ_NATIVE void jo__java__util__zip__Inflater::jm__reset(jo__java__util__zip__Inflater* _this)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native void reset()\n");

}

FJ_NATIVE void jo__java__util__zip__Inflater::jm__setDictionary(jo__java__util__zip__Inflater* _this, jarray< jbyte >* jp__0, jint jp__1, jint jp__2)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native void setDictionary(byte[], int, int)\n");

}

FJ_NATIVE void jo__java__util__zip__Inflater::jm__setInput(jo__java__util__zip__Inflater* _this, jarray< jbyte >* jp__0, jint jp__1, jint jp__2)
{
	FJ_DEBUGMSG("*** incomplete native method java.util.zip.Inflater.public native void setInput(byte[], int, int)\n");

}
