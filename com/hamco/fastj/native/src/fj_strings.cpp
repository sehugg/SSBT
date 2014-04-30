
#include "fastj.hpp"
#include "jo__java__lang__String.hpp"

#ifdef BOEHM_GC
#include "gc.h"
#endif

char* fj_jstring_to_utf(jo__java__lang__String* str)
{
	int len = str->jf__count;
	const jchar* chars = &((*str->jf__value)[0]);
	char* buf = (char*)malloc(sizeof(char)*(len*3+1));
	fj_encode_utf(chars, buf, len);
	return buf;
}

int fj_count_utf_length(const char* chars)
{
	const char* p = chars;
	int n = 0;
	while (*p)
	{
		if ((*p & 0xe0) == 0xe0)
			p += 3;
		else if (*p & 0x80)
			p += 2;
		else
			p++;
		n++;
	}
	return n;
}

void fj_decode_utf(const char* src, jchar* dest)
{
	const char* p = src;
	while (*p)
	{
		if ((*p & 0xe0) == 0xe0) {
			*dest = ((p[0]&0xf)<<12) + ((p[1]&0x3f)<<6) + (p[2]&0x3f);
			p += 3;
		} else if (*p & 0x80) {
			*dest = ((p[0]&0x1f)<<6) + (p[1]&0x3f);
			p += 2;
		} else {
			*dest = (p[0]&0x7f);
			p++;
		}
		dest++;
	}
}

char* fj_encode_utf(const jchar* src, char* dest, int len)
{
	const jchar* p = src;
	while (len--)
	{
		jchar s = *p;
		if (s < 0x80) {
			dest[0] = (char)s;
			dest++;
		} else if (s < 0x800) {
			dest[0] = (((s>>6)&0x1f)|0xc0);
			dest[1] = ((s&0x3f)|0x80);
			dest += 2;
		} else {
			dest[0] = (((s>>12)&0xf)|0xe0);
			dest[1] = (((s>>6)&0x3f)|0x80);
			dest[2] = ((s&0x3f)|0x80);
			dest += 3;
		}
		p++;
	}
	*dest = 0;
	return dest;
}

jo__java__lang__String* fj_new_string(const char* chars)
{
	// todo: shouldn't have to alloc this way
	jo__java__lang__String* str = (jo__java__lang__String*)malloc(sizeof(jo__java__lang__String));
	str->jmtab = &jmtjo__java__lang__String;
	int len = fj_count_utf_length(chars);
	jarray<jchar>* destarr = FJ_NEWARRAY(char, len);
	str->jf__value = destarr;
	fj_decode_utf(chars, &((*destarr)[0]));
	str->jf__count = len;
	str->jf__offset = 0;
	str->jf__cachedHashCode = 0;
	return str;
}
