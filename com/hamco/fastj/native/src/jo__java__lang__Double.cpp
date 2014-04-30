#include "fastj.hpp"
#include "jo__java__lang__Comparable.hpp"
#include "jo__java__lang__Object.hpp"
#include "jo__java__lang__Class.hpp"
#include "jo__java__lang__Number.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__NullPointerException.hpp"
#include "jo__java__lang__NumberFormatException.hpp"
#include "jo__java__lang__System.hpp"
#include "jo__java__lang__Double.hpp"

#include "fjnative.hpp"
#include <math.h>
#include "fjmath.hpp"

FJ_NATIVE jlong jo__java__lang__Double::jms__doubleToLongBits(jdouble x)
{
	return *((jlong*)&x);
}

FJ_NATIVE jdouble jo__java__lang__Double::jms__longBitsToDouble(jlong x)
{
	return *((jdouble*)&x);
}

FJ_NATIVE jdouble jo__java__lang__Double::jms__parseDouble(jo__java__lang__String* x)
{
	// todo: throw exception (use strtod)
	char* s = fj_jstring_to_utf(x);
	double f = atof(s);
	free(s);
	return f;
}

FJ_NATIVE jo__java__lang__String* jo__java__lang__Double::jms__toString(jdouble x)
{
	int decpt, sign;
	char result[64];
	
	if (x == jo__java__lang__Double::jf__NEGATIVE__INFINITY)
		return fj_new_string("-Infinity");
	if (x == jo__java__lang__Double::jf__POSITIVE__INFINITY)
		return fj_new_string("Infinity");
	if (isnan(x))
		return fj_new_string("NaN");
	
	char *buffer = dtoa(x, 0, 6, &decpt, &sign, NULL);
	
	// check for non-numbers (Inf & NaN)
	if (buffer[0] < '0' || buffer[0] > '9')
		return fj_new_string(buffer);

  char *s = buffer;
  char *d = result;

  if (sign)
    *d++ = '-';

  if (x >= 1e-3 && x < 1e7 || x == 0)
    {
      if (decpt <= 0)
        *d++ = '0';
      else
        {
          for (int i = 0; i < decpt; i++)
            if (*s)
              *d++ = *s++;
            else
              *d++ = '0';
        }

      *d++ = '.';

      if (*s == 0)
        {
          *d++ = '0';
          decpt++;
        }
          
      while (decpt++ < 0)
        *d++ = '0';      
      
      while (*s)
        *d++ = *s++;

      *d = 0;

      return fj_new_string(result);
    }

  *d++ = *s++;
  decpt--;
  *d++ = '.';
  
  if (*s == 0)
    *d++ = '0';

  while (*s)
    *d++ = *s++;

  *d++ = 'E';
  
  if (decpt < 0)
    {
      *d++ = '-';
      decpt = -decpt;
    }

  {
    char exp[4];
    char *e = exp + sizeof exp;
    
    *--e = 0;
    do
      {
        *--e = '0' + decpt % 10;
        decpt /= 10;
      }
    while (decpt > 0);

    while (*e)
      *d++ = *e++;
  }
  
  *d = 0;

  return fj_new_string(result);
}
