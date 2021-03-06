/* SignedObject.java --- Signed Object Class
   
  Copyright (c) 1999 by Free Software Foundation, Inc.
  Written by Mark Benvenuto <ivymccough@worldnet.att.net>

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU Library General Public License as published 
  by the Free Software Foundation, version 2. (see COPYING.LIB)

  This program is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software Foundation
  Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307 USA. */

package java.security;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
	SignedObject is used for storing rutime objects whose integrity
	cannot be compromised without being detected.

	SignedObject contains a Serializable object which is yet to be 
	signed and its signature.

	The signed copy is a "deep copy" (in serialized form) of the 
	original object. Any changes to the original will not affect 
	the original.

	Several things to note are that, first there is no need to 
	initialize the signature engine as this class will handle that 
	automatically.	Second, verification will only succeed if the
	public key corresponds to the private key used to generate 
	the SignedObject.

	For fexibility, the signature engine can be specified in the 
	constructor or the verify method. The programmer who writes 
	code that verifies the SignedObject has not changed should be 
	aware of the Signature engine they use. A malicious Signature 
	may choose to always return true on verification and 
	bypass the secrity check.

	The GNU provider provides the NIST standard DSA which uses DSA 
	and SHA-1. It can be specified by SHA/DSA, SHA-1/DSA or its 
	OID. If the RSA signature algorithm is provided then
	it could be MD2/RSA. MD5/RSA, or SHA-1/RSA. The algorithm must
	be specified because there is no default.

	@author Mark Benvenuto <ivymccough@worldnet.att.net>

	@since JDK 1.2
*/
public final class SignedObject implements Serializable
{
private byte[] content;
private byte[] signature;
private String thealgorithm;

/**
	Constructs a new SignedObject from a Serializeable object. The 
	object is signed with private key and signature engine

	@param object the object to sign
	@param signingKey the key to sign with
	@param signingEngine the signature engine to use

	@throws IOException serialization error occured
	@throws InvalidKeyException invalid key
	@throws SignatureException signing error
*/
public SignedObject(Serializable object, PrivateKey signingKey, Signature signingEngine)
             throws IOException, InvalidKeyException, SignatureException
{
	thealgorithm = signingEngine.getAlgorithm();

        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        ObjectOutputStream p = new ObjectOutputStream(ostream);
        p.writeObject(object);
        p.flush();

	content = ostream.toByteArray();

	signingEngine.initSign( signingKey );
	signingEngine.update( content );
	signature = signingEngine.sign();
}

/**
	Returns the encapsulated object. The object is 
	de-serialized before being returned.

	@return the encapsulated object

	@throws IOException de-serialization error occured
	@throws ClassNotFoundException de-serialization error occured
*/
public Object getObject()
                 throws IOException, ClassNotFoundException
{
	ByteArrayInputStream istream = new ByteArrayInputStream( content );

	return new ObjectInputStream( istream ).readObject();
}

/**
	Returns the signature of the encapsulated object.

	@return a byte array containing the signature
*/
public byte[] getSignature()
{
	return signature;
}

/**
	Returns the name of the signature algorithm.

	@return the name of the signature algorithm.
*/
public String getAlgorithm()
{
	return thealgorithm;
}

/**
	Verifies the SignedObject by checking that the signature that 
	this class contains for the encapsulated object.

	@param verificationKey the public key to use
	@param verificationEngine the signature engine to use

	@return true if signature is correct, false otherwise

	@throws InvalidKeyException invalid key
	@throws SignatureException signature verification failed
*/
public boolean verify(PublicKey verificationKey, Signature verificationEngine)
               throws InvalidKeyException, SignatureException
{
	verificationEngine.initVerify( verificationKey );
	verificationEngine.update( content );
	return verificationEngine.verify( signature );
}

//     readObject is called to restore the state of the SignedObject from a
//     stream.
//private void readObject(ObjectInputStream s)
//                 throws IOException, ClassNotFoundException

}
