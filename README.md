
WHAT THIS IS
============

In February 2000 I got this wild idea to create a Java --> native code
translator.  I was sick of Java's lousy speed and set out to make it right.
There were projects like GJC out then, but they weren't really up to snuff
yet.  So I started this mad project.  I did it for about a month and then
it fizzled.

Why did it fizzle?  Well, Hotspot was plenty fast, and there are 
<a href="http://www.bearcave.com/software/java/comp_java.html">other, mature, commercial offerings</a>
out there.

So what we are left with is an experimental Java to C++ converter, that
almost compiles javac, does not support AWT or anything funky like that,
and doesn't do GC.  Use at your own peril, and don't expect to do anything
serious with it.

I started calling it <b>fastj</b>, but there is now a product with the same
name, so let's call it <b>Silly Sailor's Bytecode Translator</b> (SSBT).  
Why not?


OPTIMIZATION

My goal was aggressive optimization.  I wanted to perform was is now known
as <i>escape analysis</i> (that'll be 50 cents, please) -- it amounts to
finding out which objects can be allocated on the stack and then freed when
the method exits.


TARGET LANGUAGE

I decided my target language would be C++.  I started with a Java --> C
translator but class inheritance was just too ugly.  One of the goals was
to make C++ classes that are ugly on the inside, but perfectly approachable
from the outside.  I used C++'s native exception handling routines, which
seemed to work out OK.


GC

Garbage collector?  Well, there's boehm-gc, but that's, ummm... we won't
talk about that now :)  GC is tough.


LIBRARIES

I used the LGPL'ed 
<a href="http://www.gnu.org/software/classpath/classpath.html">classpath</a>
library as my open source Java-compatible runtime library.  I unfortunately
had to hack it a little bit because I'm lazy, so I've included the distro
that I used in this archive, under "fastjlib".

I also used the <a href="http://bcel.sourceforge.net/">JavaClass</a> library
to introspect class bytecode.  I hacked that a bit too, so it is also
included here.

The only thing I <i>didn't</i> hack was
<a href="http://www.cacas.org/java/gnu/regexp/">gnu.regexp</a>, which you'll
need to download to compile JavaClass.


INSTALLATION

There is no automated installation process.  Sorry.  Look at INSTALL.txt for
my hastily scratched notes.  You have to use JDK 1.2.2, I think, because JDK
1.3 is funky.  But then I am not sure about this, who can tell?

NOTE: You might run out of memory <i>very</i> quickly because the
optimization processs is horribly memory-intensive.  Use the -Q flag to Make
or just start it again after it gives you an OutOfMemoryError.

Oh, you'll have to edit Makefile.in and very possibly a few other files
to point to your home directory, and set the FASTJ_HOME variable.  Might
also have to sacrifice a few chickens here and there.


RUNNING

Once you've magically compiled everything (yeah right) you can run the
main program which is "Make".  This takes .class files and turns them into
.hpp (headers) and .cpp (source).  It does a simple dependency analysis
and compares dates, so you don't have to compile the entire Java class
tree every time.

A short guide to Make's usage:

java com.hamco.fastj.Make [classes] [@classlist] options

   [classes] is a list of classes to convert -- full package names.

   [@classlist] means to read a list of classes from a file.

   -cp 
	Set the classpath that the translator reads class files from.
   -r
	Also visits classes that the interface of a class
	depends on.  If this doesn't make sense, view the source.
   -R
	Also visit all dependencies.
   -h
	Just generate header files.
   -N
	Make stubs for native code.
   -F
	Force rebuild -- do not do a timestamp check.
   -Q
	Don't do the spiffy recursive method analysis.
   -d
	Debug mode.
   -dd
	Super debug.


Make sure you have the src and include directories created when
running this thing.


LICENSE

Since everything else here is LGPL, I guess this is too!


FINAL WORD

Don't expect this to work!  Go get yourself an evaluation of JET
at www.excelsior-usa.com ... it actually works!


===========

Steven Hugg
3/22/01
