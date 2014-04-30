;; Produced by JasminVisitor (JavaClass package)
;; http://www.inf.fu-berlin.de/~dahm/JavaClass/
;; Fri Sep 10 10:47:55 CEST 1999

.source JasminVisitor.java
.class public JasminVisitor
.super java/lang/Object
.implements de/fub/bytecode/classfile/Visitor
.implements de/fub/bytecode/Constants

.field private clazz Lde/fub/bytecode/classfile/JavaClass;
.field private out Ljava/io/PrintWriter;
.field private class_name Ljava/lang/String;
.field private cp Lde/fub/bytecode/generic/ConstantPoolGen;
.field private method Lde/fub/bytecode/classfile/Method;
.field private map Ljava/util/Hashtable;

.method public disassemble()V
.limit stack 4
.limit locals 1
.var 0 is this LJasminVisitor; from Label0 to Label0

.line 32
	new de/fub/bytecode/classfile/DefaultVisitor
	dup
	aload_0
	getfield JasminVisitor.clazz Lde/fub/bytecode/classfile/JavaClass;
	aload_0
	invokespecial de/fub/bytecode/classfile/DefaultVisitor/<init>(Lde/fub/bytecode/classfile/JavaClass;Lde/fub/bytecode/classfile/Visitor;)V
	invokevirtual de/fub/bytecode/classfile/DefaultVisitor/visit()V
.line 33
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	invokevirtual java/io/PrintWriter/close()V
Label0:
.line 34
	return

.end method

.method public visitJavaClass(Lde/fub/bytecode/classfile/JavaClass;)V
.limit stack 5
.limit locals 4
.var 0 is this LJasminVisitor; from Label2 to Label2
.var 1 is arg0 Lde/fub/bytecode/classfile/JavaClass; from Label2 to Label2

.line 37
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc ";; Produced by JasminVisitor (JavaClass package)"
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 38
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc ";; http://www.inf.fu-berlin.de/~dahm/JavaClass/"
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 39
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ";; "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	new java/util/Date
	dup
	invokespecial java/util/Date/<init>()V
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/Object;)Ljava/lang/StringBuffer;
	ldc "\n"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 41
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".source "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getSourceFileName()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 42
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "."
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/AccessFlags/getAccessFlags()I
	invokestatic de/fub/bytecode/classfile/Utility/classOrInterface(I)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/AccessFlags/getAccessFlags()I
	iconst_1
	invokestatic de/fub/bytecode/classfile/Utility/accessToString(IZ)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getClassName()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 45
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".super "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getSuperclassName()Ljava/lang/String;
	bipush 46
	bipush 47
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 47
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getInterfaceNames()[Ljava/lang/String;
	astore_2
.line 49
	iconst_0
	istore_3
	goto Label0
Label1:
.line 50
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".implements "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_2
	iload_3
	aaload
	bipush 46
	bipush 47
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 49
	iinc 3 1
Label0:
	iload_3
	aload_2
	arraylength
	if_icmplt Label1
.line 52
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc "\n"
	invokevirtual java/io/PrintWriter/print(Ljava/lang/String;)V
Label2:
.line 53
	return

.end method

.method public visitField(Lde/fub/bytecode/classfile/Field;)V
.limit stack 3
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/Field; from Label0 to Label0

.line 56
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".field "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/AccessFlags/getAccessFlags()I
	invokestatic de/fub/bytecode/classfile/Utility/accessToString(I)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getName()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getSignature()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/print(Ljava/lang/String;)V
.line 58
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getAttributes()[Lde/fub/bytecode/classfile/Attribute;
	arraylength
	ifne Label0
.line 59
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc "\n"
	invokevirtual java/io/PrintWriter/print(Ljava/lang/String;)V
Label0:
.line 60
	return

.end method

.method public visitConstantValue(Lde/fub/bytecode/classfile/ConstantValue;)V
.limit stack 3
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantValue; from Label0 to Label0

.line 63
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc " = "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/Object;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label0:
.line 64
	return

.end method

.method private final printEndMethod(Lde/fub/bytecode/classfile/Attribute;)V
.limit stack 4
.limit locals 3
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/Attribute; from Label0 to Label0

.line 74
	aload_0
	getfield JasminVisitor.method Lde/fub/bytecode/classfile/Method;
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getAttributes()[Lde/fub/bytecode/classfile/Attribute;
	astore_2
.line 75
	aload_1
	aload_2
	aload_2
	arraylength
	iconst_1
	isub
	aaload
	if_acmpne Label0
.line 76
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc ".end method"
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label0:
.line 77
	return

.end method

.method public visitMethod(Lde/fub/bytecode/classfile/Method;)V
.limit stack 3
.limit locals 3
.var 0 is this LJasminVisitor; from Label1 to Label1
.var 1 is arg0 Lde/fub/bytecode/classfile/Method; from Label1 to Label1

.line 80
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "\n.method "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/AccessFlags/getAccessFlags()I
	invokestatic de/fub/bytecode/classfile/Utility/accessToString(I)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getName()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getSignature()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 83
	aload_0
	aload_1
	putfield JasminVisitor.method Lde/fub/bytecode/classfile/Method;
.line 85
	aload_1
	invokevirtual de/fub/bytecode/classfile/FieldOrMethod/getAttributes()[Lde/fub/bytecode/classfile/Attribute;
	astore_2
.line 86
	aload_2
	ifnull Label0
	aload_2
	arraylength
	ifne Label1
Label0:
.line 87
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc ".end method"
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label1:
.line 88
	return

.end method

.method public visitExceptionTable(Lde/fub/bytecode/classfile/ExceptionTable;)V
.limit stack 5
.limit locals 4
.var 0 is this LJasminVisitor; from Label2 to Label2
.var 1 is arg0 Lde/fub/bytecode/classfile/ExceptionTable; from Label2 to Label2

.line 91
	aload_1
	invokevirtual de/fub/bytecode/classfile/ExceptionTable/getExceptionNames()[Ljava/lang/String;
	astore_2
.line 92
	iconst_0
	istore_3
	goto Label0
Label1:
.line 93
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".throws "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_2
	iload_3
	aaload
	bipush 46
	bipush 47
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 92
	iinc 3 1
Label0:
	iload_3
	aload_2
	arraylength
	if_icmplt Label1
.line 95
	aload_0
	aload_1
	invokespecial JasminVisitor/printEndMethod(Lde/fub/bytecode/classfile/Attribute;)V
Label2:
.line 96
	return

.end method

.method public visitCode(Lde/fub/bytecode/classfile/Code;)V
.limit stack 5
.limit locals 17
.var 0 is this LJasminVisitor; from Label31 to Label31
.var 1 is arg0 Lde/fub/bytecode/classfile/Code; from Label31 to Label31

.line 101
	iconst_0
	istore_2
.line 103
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".limit stack "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/Code/getMaxStack()I
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 104
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc ".limit locals "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_1
	invokevirtual de/fub/bytecode/classfile/Code/getMaxLocals()I
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 106
	new de/fub/bytecode/generic/MethodGen
	dup
	aload_0
	getfield JasminVisitor.method Lde/fub/bytecode/classfile/Method;
	aload_0
	getfield JasminVisitor.class_name Ljava/lang/String;
	aload_0
	getfield JasminVisitor.cp Lde/fub/bytecode/generic/ConstantPoolGen;
	invokespecial de/fub/bytecode/generic/MethodGen/<init>(Lde/fub/bytecode/classfile/Method;Ljava/lang/String;Lde/fub/bytecode/generic/ConstantPoolGen;)V
	astore_3
.line 107
	aload_3
	invokevirtual de/fub/bytecode/generic/MethodGen/getInstructionList()Lde/fub/bytecode/generic/InstructionList;
	astore 4
.line 108
	aload 4
	invokevirtual de/fub/bytecode/generic/InstructionList/getInstructionHandles()[Lde/fub/bytecode/generic/InstructionHandle;
	astore 5
.line 113
	aload_0
	new java/util/Hashtable
	dup
	invokespecial java/util/Hashtable/<init>()V
	putfield JasminVisitor.map Ljava/util/Hashtable;
.line 115
	iconst_0
	istore 6
	goto Label0
Label5:
.line 116
	aload 5
	iload 6
	aaload
	instanceof de/fub/bytecode/generic/BranchHandle
	ifeq Label1
.line 117
	aload 5
	iload 6
	aaload
	invokevirtual de/fub/bytecode/generic/InstructionHandle/getInstruction()Lde/fub/bytecode/generic/Instruction;
	checkcast de/fub/bytecode/generic/BranchInstruction
	astore 7
.line 119
	aload 7
	instanceof de/fub/bytecode/generic/Select
	ifeq Label2
.line 120
	aload 7
	checkcast de/fub/bytecode/generic/Select
	invokevirtual de/fub/bytecode/generic/Select/getTargets()[Lde/fub/bytecode/generic/InstructionHandle;
	astore 8
.line 122
	iconst_0
	istore 9
	goto Label3
Label4:
.line 123
	aload_0
	aload 8
	iload 9
	aaload
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 122
	iinc 9 1
Label3:
	iload 9
	aload 8
	arraylength
	if_icmplt Label4
Label2:
.line 126
	aload 7
	invokevirtual de/fub/bytecode/generic/BranchInstruction/getTarget()Lde/fub/bytecode/generic/InstructionHandle;
	astore 8
.line 127
	aload_0
	aload 8
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
Label1:
.line 115
	iinc 6 1
Label0:
	iload 6
	aload 5
	arraylength
	if_icmplt Label5
.line 131
	aload_3
	invokevirtual de/fub/bytecode/generic/MethodGen/getLocalVariables()[Lde/fub/bytecode/generic/LocalVariableGen;
	astore 6
.line 132
	iconst_0
	istore 7
	goto Label6
Label7:
.line 133
	aload 6
	iload 7
	aaload
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getStart()Lde/fub/bytecode/generic/InstructionHandle;
	astore 8
.line 134
	aload_0
	aload 8
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 135
	aload 6
	iload 7
	aaload
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getEnd()Lde/fub/bytecode/generic/InstructionHandle;
	astore 8
.line 136
	aload_0
	aload 8
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 132
	iinc 7 1
Label6:
	iload 7
	aload 6
	arraylength
	if_icmplt Label7
.line 139
	aload_3
	invokevirtual de/fub/bytecode/generic/MethodGen/getExceptionHandlers()[Lde/fub/bytecode/generic/CodeExceptionGen;
	astore 7
.line 140
	iconst_0
	istore 8
	goto Label8
Label9:
.line 141
	aload 7
	iload 8
	aaload
	astore 9
.line 142
	aload 9
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getStartPC()Lde/fub/bytecode/generic/InstructionHandle;
	astore 10
.line 144
	aload_0
	aload 10
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 145
	aload 9
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getEndPC()Lde/fub/bytecode/generic/InstructionHandle;
	astore 10
.line 146
	aload_0
	aload 10
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 147
	aload 9
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getHandlerPC()Lde/fub/bytecode/generic/InstructionHandle;
	astore 10
.line 148
	aload_0
	aload 10
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc "Label"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload_2
	iinc 2 1
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc ":"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 140
	iinc 8 1
Label8:
	iload 8
	aload 7
	arraylength
	if_icmplt Label9
.line 151
	aload_3
	invokevirtual de/fub/bytecode/generic/MethodGen/getLineNumbers()[Lde/fub/bytecode/generic/LineNumberGen;
	astore 8
.line 152
	iconst_0
	istore 9
	goto Label10
Label11:
.line 153
	aload 8
	iload 9
	aaload
	invokevirtual de/fub/bytecode/generic/LineNumberGen/getInstruction()Lde/fub/bytecode/generic/InstructionHandle;
	astore 10
.line 154
	aload_0
	aload 10
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w ".line "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 8
	iload 9
	aaload
	invokevirtual de/fub/bytecode/generic/LineNumberGen/getSourceLine()I
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial JasminVisitor/put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.line 152
	iinc 9 1
Label10:
	iload 9
	aload 8
	arraylength
	if_icmplt Label11
.line 159
	iconst_0
	istore 9
	goto Label12
Label13:
.line 160
	aload 6
	iload 9
	aaload
	astore 10
.line 161
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w ".var "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 10
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getIndex()I
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc_w " is "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 10
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getName()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 10
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getType()Lde/fub/bytecode/generic/Type;
	invokevirtual de/fub/bytecode/generic/Type/getSignature()Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w " from "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 10
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getStart()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w " to "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 10
	invokevirtual de/fub/bytecode/generic/LocalVariableGen/getEnd()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 159
	iinc 9 1
Label12:
	iload 9
	aload 6
	arraylength
	if_icmplt Label13
.line 167
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc "\n"
	invokevirtual java/io/PrintWriter/print(Ljava/lang/String;)V
.line 169
	iconst_0
	istore 9
	goto Label14
Label26:
.line 170
	aload 5
	iload 9
	aaload
	astore 10
.line 171
	aload 10
	invokevirtual de/fub/bytecode/generic/InstructionHandle/getInstruction()Lde/fub/bytecode/generic/Instruction;
	astore 11
.line 172
	aload_0
	getfield JasminVisitor.map Ljava/util/Hashtable;
	aload 10
	invokevirtual java/util/Hashtable/get(Ljava/lang/Object;)Ljava/lang/Object;
	checkcast java/lang/String
	astore 12
.line 174
	aload 12
	ifnull Label15
.line 175
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	aload 12
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label15:
.line 177
	aload 11
	instanceof de/fub/bytecode/generic/BranchInstruction
	ifeq Label16
.line 178
	aload 11
	instanceof de/fub/bytecode/generic/Select
	ifeq Label17
.line 179
	aload 11
	checkcast de/fub/bytecode/generic/Select
	astore 13
.line 180
	aload 13
	invokevirtual de/fub/bytecode/generic/Select/getMatchs()[I
	astore 14
.line 181
	aload 13
	invokevirtual de/fub/bytecode/generic/Select/getTargets()[Lde/fub/bytecode/generic/InstructionHandle;
	astore 15
.line 183
	aload 13
	instanceof de/fub/bytecode/generic/TABLESWITCH
	ifeq Label18
.line 184
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\ttableswitch "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 14
	iconst_0
	iaload
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 14
	aload 14
	arraylength
	iconst_1
	isub
	iaload
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 187
	iconst_0
	istore 16
	goto Label19
Label20:
.line 188
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\t\t"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 15
	iload 16
	aaload
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 187
	iinc 16 1
Label19:
	iload 16
	aload 15
	arraylength
	if_icmplt Label20
	goto Label21
Label18:
.line 191
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc_w "\tlookupswitch "
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 193
	iconst_0
	istore 16
	goto Label22
Label23:
.line 194
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\t\t"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 14
	iload 16
	iaload
	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;
	ldc_w " : "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 15
	iload 16
	aaload
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 193
	iinc 16 1
Label22:
	iload 16
	aload 15
	arraylength
	if_icmplt Label23
Label21:
.line 197
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\t\tdefault: "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 13
	invokevirtual de/fub/bytecode/generic/BranchInstruction/getTarget()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
	goto Label24
Label17:
.line 199
	aload 11
	checkcast de/fub/bytecode/generic/BranchInstruction
	astore 13
.line 200
	aload 13
	invokevirtual de/fub/bytecode/generic/BranchInstruction/getTarget()Lde/fub/bytecode/generic/InstructionHandle;
	astore 10
.line 201
	aload_0
	aload 10
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	astore 12
.line 202
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\t"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	getstatic de.fub.bytecode.Constants.OPCODE_NAMES [Ljava/lang/String;
	aload 13
	invokevirtual de/fub/bytecode/generic/Instruction/getTag()S
	aaload
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc " "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 12
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label24:
	goto Label25
Label16:
.line 206
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w "\t"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 11
	aload_0
	getfield JasminVisitor.cp Lde/fub/bytecode/generic/ConstantPoolGen;
	invokevirtual de/fub/bytecode/generic/ConstantPoolGen/getConstantPool()Lde/fub/bytecode/classfile/ConstantPool;
	invokevirtual de/fub/bytecode/generic/Instruction/toString(Lde/fub/bytecode/classfile/ConstantPool;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
Label25:
.line 169
	iinc 9 1
Label14:
	iload 9
	aload 5
	arraylength
	if_icmplt Label26
.line 209
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	ldc "\n"
	invokevirtual java/io/PrintWriter/print(Ljava/lang/String;)V
.line 211
	iconst_0
	istore 9
	goto Label27
Label30:
.line 212
	aload 7
	iload 9
	aaload
	astore 10
.line 213
	aload 10
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getCatchType()Lde/fub/bytecode/generic/ObjectType;
	astore 11
.line 214
	aload 11
	ifnonnull Label28
	ldc_w "all"
	goto Label29
Label28:
	aload 11
	invokevirtual de/fub/bytecode/generic/ObjectType/getClassName()Ljava/lang/String;
	bipush 46
	bipush 47
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
Label29:
	astore 12
.line 217
	aload_0
	getfield JasminVisitor.out Ljava/io/PrintWriter;
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	ldc_w ".catch "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 12
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w " from "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 10
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getStartPC()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w " to "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 10
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getEndPC()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w " using "
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_0
	aload 10
	invokevirtual de/fub/bytecode/generic/CodeExceptionGen/getHandlerPC()Lde/fub/bytecode/generic/InstructionHandle;
	invokespecial JasminVisitor/get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/io/PrintWriter/println(Ljava/lang/String;)V
.line 211
	iinc 9 1
Label27:
	iload 9
	aload 7
	arraylength
	if_icmplt Label30
.line 222
	aload_0
	aload_1
	invokespecial JasminVisitor/printEndMethod(Lde/fub/bytecode/classfile/Attribute;)V
Label31:
.line 223
	return

.end method

.method private final get(Lde/fub/bytecode/generic/InstructionHandle;)Ljava/lang/String;
.limit stack 4
.limit locals 3
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/generic/InstructionHandle; from Label0 to Label0

.line 227
	new java/util/StringTokenizer
	dup
	aload_0
	getfield JasminVisitor.map Ljava/util/Hashtable;
	aload_1
	invokevirtual java/util/Hashtable/get(Ljava/lang/Object;)Ljava/lang/Object;
	checkcast java/lang/String
	ldc "\n"
	invokespecial java/util/StringTokenizer/<init>(Ljava/lang/String;Ljava/lang/String;)V
	invokevirtual java/util/StringTokenizer/nextToken()Ljava/lang/String;
	astore_2
.line 228
	aload_2
	iconst_0
	aload_2
	invokevirtual java/lang/String/length()I
	iconst_1
	isub
	invokevirtual java/lang/String/substring(II)Ljava/lang/String;
Label0:
	areturn

.end method

.method private final put(Lde/fub/bytecode/generic/InstructionHandle;Ljava/lang/String;)V
.limit stack 4
.limit locals 4
.var 0 is this LJasminVisitor; from Label1 to Label1
.var 1 is arg0 Lde/fub/bytecode/generic/InstructionHandle; from Label1 to Label1
.var 2 is arg1 Ljava/lang/String; from Label1 to Label1

.line 232
	aload_0
	getfield JasminVisitor.map Ljava/util/Hashtable;
	aload_1
	invokevirtual java/util/Hashtable/get(Ljava/lang/Object;)Ljava/lang/Object;
	checkcast java/lang/String
	astore_3
.line 234
	aload_3
	ifnonnull Label0
.line 235
	aload_0
	getfield JasminVisitor.map Ljava/util/Hashtable;
	aload_1
	aload_2
	invokevirtual java/util/Hashtable/put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
	pop
	goto Label1
Label0:
.line 237
	aload_2
	ldc "Label"
	invokevirtual java/lang/String/startsWith(Ljava/lang/String;)Z
	ifne Label2
	aload_3
	aload_2
	invokevirtual java/lang/String/endsWith(Ljava/lang/String;)Z
	ifeq Label3
Label2:
.line 238
	return
Label3:
.line 240
	aload_0
	getfield JasminVisitor.map Ljava/util/Hashtable;
	aload_1
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	aload_3
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc "\n"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload_2
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokevirtual java/util/Hashtable/put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
	pop
Label1:
.line 242
	return

.end method

.method public visitCodeException(Lde/fub/bytecode/classfile/CodeException;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/CodeException; from Label0 to Label0

Label0:
.line 244
	return

.end method

.method public visitConstantClass(Lde/fub/bytecode/classfile/ConstantClass;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantClass; from Label0 to Label0

Label0:
.line 245
	return

.end method

.method public visitConstantDouble(Lde/fub/bytecode/classfile/ConstantDouble;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantDouble; from Label0 to Label0

Label0:
.line 246
	return

.end method

.method public visitConstantFieldref(Lde/fub/bytecode/classfile/ConstantFieldref;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantFieldref; from Label0 to Label0

Label0:
.line 247
	return

.end method

.method public visitConstantFloat(Lde/fub/bytecode/classfile/ConstantFloat;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantFloat; from Label0 to Label0

Label0:
.line 248
	return

.end method

.method public visitConstantInteger(Lde/fub/bytecode/classfile/ConstantInteger;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantInteger; from Label0 to Label0

Label0:
.line 249
	return

.end method

.method public visitConstantInterfaceMethodref(Lde/fub/bytecode/classfile/ConstantInterfaceMethodref;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantInterfaceMethodref; from Label0 to Label0

Label0:
.line 250
	return

.end method

.method public visitConstantLong(Lde/fub/bytecode/classfile/ConstantLong;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantLong; from Label0 to Label0

Label0:
.line 251
	return

.end method

.method public visitConstantMethodref(Lde/fub/bytecode/classfile/ConstantMethodref;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantMethodref; from Label0 to Label0

Label0:
.line 252
	return

.end method

.method public visitConstantNameAndType(Lde/fub/bytecode/classfile/ConstantNameAndType;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantNameAndType; from Label0 to Label0

Label0:
.line 253
	return

.end method

.method public visitConstantPool(Lde/fub/bytecode/classfile/ConstantPool;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantPool; from Label0 to Label0

Label0:
.line 254
	return

.end method

.method public visitConstantString(Lde/fub/bytecode/classfile/ConstantString;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantString; from Label0 to Label0

Label0:
.line 255
	return

.end method

.method public visitConstantUtf8(Lde/fub/bytecode/classfile/ConstantUtf8;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/ConstantUtf8; from Label0 to Label0

Label0:
.line 256
	return

.end method

.method public visitDeprecated(Lde/fub/bytecode/classfile/Deprecated;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/Deprecated; from Label0 to Label0

Label0:
.line 257
	return

.end method

.method public visitInnerClass(Lde/fub/bytecode/classfile/InnerClass;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/InnerClass; from Label0 to Label0

Label0:
.line 258
	return

.end method

.method public visitInnerClasses(Lde/fub/bytecode/classfile/InnerClasses;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/InnerClasses; from Label0 to Label0

Label0:
.line 259
	return

.end method

.method public visitLineNumber(Lde/fub/bytecode/classfile/LineNumber;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/LineNumber; from Label0 to Label0

Label0:
.line 260
	return

.end method

.method public visitLineNumberTable(Lde/fub/bytecode/classfile/LineNumberTable;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/LineNumberTable; from Label0 to Label0

Label0:
.line 261
	return

.end method

.method public visitLocalVariable(Lde/fub/bytecode/classfile/LocalVariable;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/LocalVariable; from Label0 to Label0

Label0:
.line 262
	return

.end method

.method public visitLocalVariableTable(Lde/fub/bytecode/classfile/LocalVariableTable;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/LocalVariableTable; from Label0 to Label0

Label0:
.line 263
	return

.end method

.method public visitSourceFile(Lde/fub/bytecode/classfile/SourceFile;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/SourceFile; from Label0 to Label0

Label0:
.line 264
	return

.end method

.method public visitSynthetic(Lde/fub/bytecode/classfile/Synthetic;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/Synthetic; from Label0 to Label0

Label0:
.line 265
	return

.end method

.method public visitUnknown(Lde/fub/bytecode/classfile/Unknown;)V
.limit stack 0
.limit locals 2
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/Unknown; from Label0 to Label0

Label0:
.line 266
	return

.end method

.method public static main([Ljava/lang/String;)V
.limit stack 5
.limit locals 9
.var 0 is arg0 [Ljava/lang/String; from Label9 to Label9

.line 269
	aconst_null
	astore_1
.line 270
.line 271
	new de/fub/bytecode/ClassPath
	dup
	invokespecial de/fub/bytecode/ClassPath/<init>()V
	astore_3
Label11:
.line 273
.line 274
	aload_0
	arraylength
	ifne Label0
.line 275
	getstatic java.lang.System.err Ljava/io/PrintStream;
	ldc_w "disassemble: No input files specified"
	invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
	goto Label1
Label0:
.line 278
	iconst_0
	istore 4
	goto Label2
Label6:
.line 279
	aload_0
	iload 4
	aaload
	ldc_w ".class"
	invokevirtual java/lang/String/endsWith(Ljava/lang/String;)Z
	ifeq Label3
.line 280
	new de/fub/bytecode/classfile/ClassParser
	dup
	aload_0
	iload 4
	aaload
	invokespecial de/fub/bytecode/classfile/ClassParser/<init>(Ljava/lang/String;)V
	astore_1
	goto Label4
Label3:
.line 282
	aload_3
	aload_0
	iload 4
	aaload
	invokevirtual de/fub/bytecode/ClassPath/getInputStream(Ljava/lang/String;)Ljava/io/InputStream;
	astore 5
.line 283
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	aload_0
	iload 4
	aaload
	bipush 46
	bipush 47
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w ".class"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	astore 6
.line 285
	new de/fub/bytecode/classfile/ClassParser
	dup
	aload 5
	aload 6
	invokespecial de/fub/bytecode/classfile/ClassParser/<init>(Ljava/io/InputStream;Ljava/lang/String;)V
	astore_1
Label4:
.line 288
	aload_1
	invokevirtual de/fub/bytecode/classfile/ClassParser/parse()Lde/fub/bytecode/classfile/JavaClass;
	astore_2
.line 290
	aload_2
	invokevirtual de/fub/bytecode/classfile/JavaClass/getClassName()Ljava/lang/String;
	astore 5
.line 291
	aload 5
	bipush 46
	invokevirtual java/lang/String/lastIndexOf(I)I
	istore 6
.line 292
	aload 5
	iconst_0
	iload 6
	iconst_1
	iadd
	invokevirtual java/lang/String/substring(II)Ljava/lang/String;
	bipush 46
	getstatic java.io.File.separatorChar C
	invokevirtual java/lang/String/replace(CC)Ljava/lang/String;
	astore 7
.line 293
	aload 5
	iload 6
	iconst_1
	iadd
	invokevirtual java/lang/String/substring(I)Ljava/lang/String;
	astore 5
.line 295
	aload 7
	ldc_w ""
	invokevirtual java/lang/String/equals(Ljava/lang/Object;)Z
	ifne Label5
.line 296
	new java/io/File
	dup
	aload 7
	invokespecial java/io/File/<init>(Ljava/lang/String;)V
	astore 8
.line 297
	aload 8
	invokevirtual java/io/File/mkdirs()Z
	pop
Label5:
.line 300
	new java/io/FileOutputStream
	dup
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	aload 7
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 5
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	ldc_w ".j"
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	invokespecial java/io/FileOutputStream/<init>(Ljava/lang/String;)V
	astore 8
.line 301
	new JasminVisitor
	dup
	aload_2
	aload 8
	invokespecial JasminVisitor/<init>(Lde/fub/bytecode/classfile/JavaClass;Ljava/io/OutputStream;)V
	invokevirtual JasminVisitor/disassemble()V
.line 278
	iinc 4 1
Label2:
	iload 4
	aload_0
	arraylength
	if_icmplt Label6
Label1:
	goto Label7
Label12:
	nop
Label13:
	astore 4
.line 305
	aload 4
	invokevirtual java/lang/Throwable/printStackTrace()V
	goto Label7
Label7:
	nop
Label9:
.line 307
	return

.catch java/lang/Exception from Label11 to Label12 using Label13
.end method

.method public <init>(Lde/fub/bytecode/classfile/JavaClass;Ljava/io/OutputStream;)V
.limit stack 4
.limit locals 3
.var 0 is this LJasminVisitor; from Label0 to Label0
.var 1 is arg0 Lde/fub/bytecode/classfile/JavaClass; from Label0 to Label0
.var 2 is arg1 Ljava/io/OutputStream; from Label0 to Label0

.line 21
	aload_0
	invokespecial java/lang/Object/<init>()V
.line 22
	aload_0
	aload_1
	putfield JasminVisitor.clazz Lde/fub/bytecode/classfile/JavaClass;
.line 23
	aload_0
	new java/io/PrintWriter
	dup
	aload_2
	invokespecial java/io/PrintWriter/<init>(Ljava/io/OutputStream;)V
	putfield JasminVisitor.out Ljava/io/PrintWriter;
.line 24
	aload_0
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getClassName()Ljava/lang/String;
	putfield JasminVisitor.class_name Ljava/lang/String;
.line 25
	aload_0
	new de/fub/bytecode/generic/ConstantPoolGen
	dup
	aload_1
	invokevirtual de/fub/bytecode/classfile/JavaClass/getConstantPool()Lde/fub/bytecode/classfile/ConstantPool;
	invokespecial de/fub/bytecode/generic/ConstantPoolGen/<init>(Lde/fub/bytecode/classfile/ConstantPool;)V
	putfield JasminVisitor.cp Lde/fub/bytecode/generic/ConstantPoolGen;
Label0:
.line 26
	return

.end method
