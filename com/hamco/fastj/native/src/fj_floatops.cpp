/*
 * soft.c
 * Soft instruction support.
 *
 * Copyright (c) 1996 T. J. Wilkinson & Associates, London, UK.
 *
 * See the file "license.terms" for information on usage and redistribution
 * of this file, and for a DISCLAIMER OF ALL WARRANTIES.
 *
 * Written by Tim Wilkinson <tim@tjwassoc.demon.co.uk>, 1996.
 */

#define	MDBG(s)
#define	ADBG(s)
#define	CDBG(s)
#define	IDBG(s)

#if MDBG(1) - 1 == 0
#undef CDBG
#define	CDBG(s) s
#endif

#include "fastj.hpp"
#include <stdarg.h>
#include <math.h>

/*
 * fj_soft_dcmpg
 */
jint fj_soft_dcmpg(jdouble v1, jdouble v2)
{
	jint ret;
	if ((!isinf(v1) && isnan(v1)) || (!isinf(v2) && isnan(v2))) {
		ret = 1;
	}
	else if (v1 > v2) {
		ret = 1;
	}
	else if (v1 == v2) {
		ret = 0;
	}
	else {
		ret = -1;
	}

	return (ret);
}

/*
 * fj_soft_dcmpl
 */
jint fj_soft_dcmpl(jdouble v1, jdouble v2)
{
        jint ret;
	if ((!isinf(v1) && isnan(v1)) || (!isinf(v2) && isnan(v2))) {
		ret = -1;
	}
        else if (v1 > v2) {
                ret = 1;
        }
        else if (v1 == v2) {
                ret = 0;
        }
        else {
                ret = -1;
        }
	return (ret);
}

/*
 * fj_soft_fcmpg
 */
jint fj_soft_fcmpg(jfloat v1, jfloat v2)
{
        jint ret;
	if ((!isinf(v1) && isnan(v1)) || (!isinf(v2) && isnan(v2))) {
		ret = 1;
	}
        else if (v1 > v2) {
                ret = 1;
        }
        else if (v1 == v2) {
                ret = 0;
        }
        else {
                ret = -1;
        }
	return (ret);
}

/*
 * fj_soft_fcmpg
 */
jint fj_soft_fcmpl(jfloat v1, jfloat v2)
{
        jint ret;
	if ((!isinf(v1) && isnan(v1)) || (!isinf(v2) && isnan(v2))) {
		ret = -1;
	}
        else if (v1 > v2) {
                ret = 1;
        }
        else if (v1 == v2) {
                ret = 0;
        }
        else {
                ret = -1;
        }
	return (ret);
}

jlong fj_soft_lmul(jlong v1, jlong v2)
{
	return (v1 * v2);
}

jlong fj_soft_ldiv(jlong v1, jlong v2)
{
	return (v1 / v2);
}

jlong fj_soft_lrem(jlong v1, jlong v2)
{
	return (v1 % v2);
}

jfloat fj_soft_fdiv(jfloat v1, jfloat v2)
{
	jfloat k = 1e300;

	if (v2 == 0.0) {
		if (v1 > 0) {
			return (k*k);
		}
		else {
			return (-k*k);
		}
	}
	return (v1 / v2);
}

jdouble fj_soft_fdivl(jdouble v1, jdouble v2)
{
	jdouble k = 1e300;

	if (v2 == 0.0) {
		if (v1 > 0) {
			return (k*k);
		}
		else {
			return (-k*k);
		}
	}
	return (v1 / v2);
}

jfloat fj_soft_frem(jfloat v1, jfloat v2)
{
	return (remainderf(v1, v2));
}

jdouble fj_soft_freml(jdouble v1, jdouble v2)
{
	return (remainder(v1, v2));
}

jlong fj_soft_lshll(jlong v1, jint v2)
{
	return (v1 << (v2 & 63));
}

jlong fj_soft_ashrl(jlong v1, jint v2)
{
	return (v1 >> (v2 & 63));
}

jlong fj_soft_lshrl(jlong v1, jint v2)
{
	return (((julong)v1) >> (v2 & 63));
}

jint fj_soft_lcmp(jlong v1, jlong v2)
{
	jlong lcc = v2 - v1;
	if (lcc < 0) {
		return (-1);
	}
	else if (lcc > 0) {
		return (1);
	}
	else {
		return (0);
	}
}

jint fj_soft_mul(jint v1, jint v2)
{
	return (v1*v2);
}

jint fj_soft_div(jint v1, jint v2)
{
	return (v1/v2);
}

jint fj_soft_rem(jint v1, jint v2)
{
	return (v1%v2);
}

jfloat fj_soft_cvtlf(jlong v)
{
	return ((jfloat)v);
}

jdouble fj_soft_cvtld(jlong v)
{
	return ((jdouble)v);
}

/*
 * The following functions round the float/double to an int/long.
 * They round the value toward zero.
 */

jlong fj_soft_cvtfl(jfloat v)
{
	if (v < 0.0) {
		return ((jlong)ceil(v));
	}
	else {
		return ((jlong)floor(v));
	}
}

jlong fj_soft_cvtdl(jdouble v)
{
	if (v < 0.0) {
		return ((jlong)ceil(v));
	}
	else {
		return ((jlong)floor(v));
	}
}

jint fj_soft_cvtfi(jfloat v)
{
	if (v < 0.0) {
		return ((jint)ceil(v));
	}
	else {
		return ((jint)floor(v));
	}
}

jint fj_soft_cvtdi(jdouble v)
{
	if (v < 0.0) {
		return ((jint)ceil(v));
	}
	else {
		return ((jint)floor(v));
	}
}
