
#include "fastj.hpp"

class jsmartref
{
private:
	jobject* ref;
public:
	jsmartref() : ref(NULL) { }
	jsmartref(jobject* aref) : ref(aref) { }
	~jsmartref() 
	{
		if (ref)
			fj_free_object(ref);
	}
	jobject& operator*() const
	{
		return *ref;
	}
	jobject* operator=(jobject* obj)
	{
		if (ref)
			fj_free_object(ref);
		ref = obj;
	}
	operator jobject*() { return ref; }
};

