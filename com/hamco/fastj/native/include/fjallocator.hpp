#ifndef _FJALLOCATOR_HPP
#define _FJALLOCATOR_HPP


#include "fastj.hpp"
#include <stdlib.h>
#include <new>

class jobjallocator
{
	void** recs;
	int size, limit;
	static const int LIMIT_INCREMENT=16;
public:
	jobjallocator() :
		recs(NULL), size(0), limit(0)
	{ 
	}
	~jobjallocator();
	void* allocate(int nbytes);
	void* allocate(jmethod_table* jmtab);
	__jarray* allocarray(jmethod_table* jmtab, int len, int size)
	{
		// allocate the array and the array data contiguously
		void *buf = allocate(sizeof(__jarray)+len*size);
		// note the (buf) placement
		// the 2nd param says to place the array data right after the __jarray
		__jarray* arr = new (buf) __jarray(jmtab, len, (void*)(((char*)buf)+sizeof(__jarray)));
		return arr;
	}
};

#endif /* _FJALLOCATOR_HPP */
