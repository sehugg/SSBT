
#include "fastj.hpp"
#include "jo__java__lang__String.hpp"
#include "jo__java__lang__Class.hpp"

void fj_init_primitive_classes();
void fj_init_array_classes();
void fj_init_signals();

// here's how it works:
// - first init java.lang.Class
// - that'll init java.lang.Object
// - during this, Strings are created but it don't use the initializer so it's ok
//   that array classes aren't created yet (it uses their static address)
// - then we init primitive classes and primitive array classes
// - then we do full init of java.lang.String

void fj_init_VM()
{
	jinit__java__lang__Class();
	fj_init_primitive_classes();
	fj_init_array_classes();
	//fj_init_signals();
	jinit__java__lang__String();
}
