
#include "fjallocator.hpp"

int main()
{
	jobjallocator jalloc;
	
	void* x = jalloc.allocate(42);
	void* y = jalloc.allocate(32);

	return 0;	
}
