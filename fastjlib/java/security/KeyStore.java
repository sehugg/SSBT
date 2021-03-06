/* KeyStore.java --- Key Store Class
   
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
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream; 
import java.security.cert.CertificateException;
import java.util.Date;
import java.util.Enumeration ;

/**
	Keystore represents an in-memory collection of keys and 
	certificates. There are two types of entries:

	* Key Entry

	This type of keystore entry store sensitive crytographic key
	information in a protected format.Typically this is a secret 
	key or a private key with a certificate chain.


	* Trusted Ceritificate Entry

	This type of keystore entry contains a single public key 
	certificate belonging to annother entity. It is called trusted
	because the keystore owner trusts that the certificates
	belongs to the subject (owner) of the certificate.

	The keystore contains an "alias" string for each entry. 

	The structure and persistentence of the key store is not 
	specified. Any method could be used to protect sensitive 
	( private or secret) keys. Smart cards or integrated 
	cryptographic engines could be used or the keystore could 
	be simply stored in a file. 
*/
public class KeyStore
{

private KeyStoreSpi keyStoreSpi;
private Provider provider;
private String type;

/**
	Creates an instance of KeyStore

	@param keyStoreSpi A KeyStore engine to use
	@param provider A provider to use
	@param type The type of KeyStore
*/
protected KeyStore(KeyStoreSpi keyStoreSpi, Provider provider, String type)
{
	this.keyStoreSpi = keyStoreSpi;
	this.provider = provider;
	this.type = type;
}

/** 
	Gets an instance of the KeyStore class representing
	the specified keystore. If the type is not 
	found then, it throws CertificateException.

	@param type the type of certificate to choose

	@return a KeyStore repesenting the desired type

	@throws KeyStoreException if the type of keystore is not implemented by providers
*/
public static KeyStore getInstance(String type)
                            throws KeyStoreException
{
    Provider[] p = Security.getProviders ();

    for (int i = 0; i < p.length; i++)
    {
      String classname = p[i].getProperty ("KeyStore." + type);
      if (classname != null)
	return getInstance (classname, type, p[i]);
    }

    throw new KeyStoreException(type);
}

/** 
	Gets an instance of the KeyStore class representing
	the specified key store from the specified provider. 
	If the type is not found then, it throws CertificateException. 
	If the provider is not found, then it throws 
	NoSuchProviderException.

	@param type the type of certificate to choose

	@return a KeyStore repesenting the desired type

	@throws KeyStoreException if the type of keystore is not implemented by providers
	@throws NoSuchProviderException if the provider is not found
*/
public static KeyStore getInstance(String type, String provider)
                            throws KeyStoreException, NoSuchProviderException
{
	Provider p = Security.getProvider(provider);
	if( p == null)
		throw new NoSuchProviderException();

    return getInstance (p.getProperty ("KeyStore." + type),
			type, p);
}

private static KeyStore getInstance (String classname,
					String type,
					Provider provider)
	throws KeyStoreException
{
        try {
                return new KeyStore( (KeyStoreSpi)Class.forName( classname ).newInstance(), provider, type );
        } catch( ClassNotFoundException cnfe) {
		throw new KeyStoreException("Class not found");
	} catch( InstantiationException ie) {
		throw new KeyStoreException("Class instantiation failed");
	} catch( IllegalAccessException iae) {
		throw new KeyStoreException("Illegal Access");
        }
}


/**
	Gets the provider that the class is from.

	@return the provider of this class
*/
public final Provider getProvider()
{
	return provider;
}

/**
	Returns the type of the KeyStore supported

	@return A string with the type of KeyStore
*/
public final String getType()
{
	return type;
}

/**
	Returns the key associated with given alias using the 
	supplied password.

	@param alias an alias for the key to get
	@param password password to access key with
	
	@return the requested key, or null otherwise

	@throws NoSuchAlgorithmException if there is no algorithm
		for recovering the key
	@throws UnrecoverableKeyException key cannot be reocovered
		(wrong password).
*/
public final Key getKey(String alias, char[] password)
                 throws KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException
{
	return keyStoreSpi.engineGetKey( alias, password);
}

/**
	Gets a Certificate chain for the specified alias.

	@param alias the alias name

	@return a chain of Certificates ( ordered from the user's 
		certificate to the Certificate Authority's ) or 
		null if the alias does not exist or there is no
		certificate chain for the alias ( the alias refers
		to a trusted certificate entry or there is no entry).
*/
public final java.security.cert.Certificate[] getCertificateChain(String alias)
                                        throws KeyStoreException
{
	return keyStoreSpi.engineGetCertificateChain( alias );
}

/**
	Gets a Certificate for the specified alias.

	If there is a trusted certificate entry then that is returned.
	it there is a key entry with a certificate chain then the
	first certificate is return or else null.

	@param alias the alias name

	@return a Certificate or null if the alias does not exist 
		or there is no certificate for the alias
*/
public final java.security.cert.Certificate getCertificate(String alias)
                                 throws KeyStoreException
{
	return keyStoreSpi.engineGetCertificate( alias );
}

/**
	Gets entry creation date for the specified alias.

	@param alias the alias name
	
	@returns the entry creation date or null
*/
public final Date getCreationDate(String alias)
                           throws KeyStoreException
{
	return keyStoreSpi.engineGetCreationDate( alias );
}

/**
	Assign the key to the alias in the keystore, protecting it
	with the given password. It will overwrite an existing 
	entry and if the key is a PrivateKey, also add the 
	certificate chain representing the corresponding public key.

	@param alias the alias name
	@param key the key to add
	@password the password to protect with
	@param chain the certificate chain for the corresponding
		public key

	@throws KeyStoreException if it fails
*/
public final void setKeyEntry(String alias, Key key, char[] password, java.security.cert.Certificate[] chain)
                       throws KeyStoreException
{
	keyStoreSpi.engineSetKeyEntry( alias, key, password, chain );
}

/**
	Assign the key to the alias in the keystore. It will overwrite
	an existing entry and if the key is a PrivateKey, also 
	add the certificate chain representing the corresponding 
	public key.

	@param alias the alias name
	@param key the key to add
	@param chain the certificate chain for the corresponding
		public key

	@throws KeyStoreException if it fails
*/
public final void setKeyEntry(String alias, byte[] key, java.security.cert.Certificate[] chain)
                       throws KeyStoreException
{
	keyStoreSpi.engineSetKeyEntry( alias, key, chain);
}

/**
	Assign the certificate to the alias in the keystore. It 
	will overwrite an existing entry.

	@param alias the alias name
	@param cert the certificate to add

	@throws KeyStoreException if it fails
*/
public final void setCertificateEntry(String alias, java.security.cert.Certificate cert)
                               throws KeyStoreException
{
	keyStoreSpi.engineSetCertificateEntry(alias, cert);
}

/**
	Deletes the entry for the specified entry.

	@param alias the alias name

	@throws KeyStoreException if it fails
*/
public final void deleteEntry(String alias)
                       throws KeyStoreException
{
	keyStoreSpi.engineDeleteEntry( alias );
}

/**
	Generates a list of all the aliases in the keystore.

	@return an Enumeration of the aliases
*/
public final Enumeration aliases()
                          throws KeyStoreException
{
	return keyStoreSpi.engineAliases();
}

/**
	Determines if the keystore contains the specified alias.

	@param alias the alias name

	@return true if it contains the alias, false otherwise
*/
public final boolean containsAlias(String alias)
                            throws KeyStoreException
{
	return keyStoreSpi.engineContainsAlias( alias );
}

/**
	Returns the number of entries in the keystore.

	@returns the number of keystore entries.
*/
public final int size()
               throws KeyStoreException
{
	return keyStoreSpi.engineSize();
}

/**
	Determines if the keystore contains a key entry for 
	the specified alias.

	@param alias the alias name

	@return true if it is a key entry, false otherwise
*/
public final boolean isKeyEntry(String alias)
                         throws KeyStoreException
{
	return keyStoreSpi.engineIsKeyEntry( alias );
}


/**
	Determines if the keystore contains a certificate entry for 
	the specified alias.

	@param alias the alias name

	@return true if it is a certificate entry, false otherwise
*/
public final boolean isCertificateEntry(String alias)
                                 throws KeyStoreException
{
	return keyStoreSpi.engineIsCertificateEntry( alias );
}

/**
	Determines if the keystore contains the specified certificate 
	entry and returns the alias.

	It checks every entry and for a key entry checks only the
	first certificate in the chain.

	@param cert Certificate to look for

	@return alias of first matching certificate, null if it 
		does not exist.
*/
public final String getCertificateAlias(java.security.cert.Certificate cert)
                                 throws KeyStoreException
{
	return keyStoreSpi.engineGetCertificateAlias( cert );
}

/**
	Stores the keystore in the specified output stream and it
	uses the specified key it keep it secure.

	@param stream the output stream to save the keystore to
	@param password the password to protect the keystore integrity with

	@throws IOException if an I/O error occurs.
	@throws NoSuchAlgorithmException the data integrity algorithm 
		used cannot be found.
	@throws CertificateException if any certificates could not be
		stored in the output stream.
*/
public final void store(OutputStream stream, char[] password)
                 throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException
{
	keyStoreSpi.engineStore( stream, password);
}

/**
	Loads the keystore from the specified input stream and it
	uses the specified password to check for integrity if supplied.

	@param stream the input stream to load the keystore from
	@param password the password to check the keystore integrity with

	@throws IOException if an I/O error occurs.
	@throws NoSuchAlgorithmException the data integrity algorithm 
		used cannot be found.
	@throws CertificateException if any certificates could not be
		stored in the output stream.
*/
public final void load(InputStream stream, char[] password)
                throws IOException, NoSuchAlgorithmException, CertificateException
{
	keyStoreSpi.engineLoad( stream, password);
}

/**
	Returns the default KeyStore type. This method looks up the
	type in <JAVA_HOME>/lib/security/java.security with the 
	property "keystore.type" or if that fails then "jks" .
*/
public static final String getDefaultType()
{
	String tmp;
	//Security reads every property in java.security so it 
	//will return this property if it exists. 
	tmp = Security.getProperty( "keystore.type" );

	if( tmp == null)
		tmp = "jks";

	return tmp;
}

}
