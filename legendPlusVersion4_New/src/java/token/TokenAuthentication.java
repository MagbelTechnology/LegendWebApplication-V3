Express (Distributed operating systems), Version 7.0 > Reference > Developer examples
File name: xsec_authenttoken.html

Example: A com.ibm.wsspi.security.token.AuthenticationToken implementation
The following example illustrates an authentication token implementation. The following sample code does not extend an abstract class, but rather implements the com.ibm.wsspi.security.token.AuthenticationToken interface directly. You can implement the interface directly, but it might cause you to write duplicate code. However, you might choose to implement the interface directly if considerable differences exist between how you handle the various token implementations.

package com.ibm.websphere.security.token;

import com.ibm.websphere.security.WSSecurityException;
import com.ibm.websphere.security.auth.WSLoginFailedException;
import com.ibm.wsspi.security.token.*;
import com.ibm.websphere.security.WebSphereRuntimePermission;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class CustomAuthenticationTokenImpl implements com.ibm.wsspi.security.
   token.AuthenticationToken
{
	private java.util.Hashtable hashtable = new java.util.Hashtable();
	private byte[] tokenBytes = null;
  // 2 hours in millis, by default
  	private static long expire_period_in_millis = 2*60*60*1000; 
	private String oidName = "your_oid_name";  
  // This string can really be anything if you do not want to use an OID.
    
/**
 * Constructor used to create initial AuthenticationToken instance
 */
	public CustomAuthenticationTokenImpl (String principal)
	{
		// Sets the principal in the token
		addAttribute("principal", principal);
		// Sets the token version
		addAttribute("version", "1");
		// Sets the token expiration
		addAttribute("expiration", new Long(System.currentTimeMillis() 
        + expire_period_in_millis).toString());
	}

/**
 * Constructor used to deserialize the token bytes received during a 
 * propagation login.
 */
	public CustomAuthenticationTokenImpl (byte[] token_bytes)
	{
		try
		{
       // The data in token_bytes should be signed and encrypted if the
       // hashtable is acting as an authentication token.
			hashtable = (java.util.Hashtable) custom_decryption_algorithm (token_bytes);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

/**
 * Validates the token including expiration, signature, and so on.
 * @return boolean
 */

	public boolean isValid ()
	{
		long expiration = getExpiration();

		// If you set the expiration to 0, the token does not expire
		if (expiration != 0)
		{
			// Returns a response that identifies whether this token is still valid
			long current_time = System.currentTimeMillis();
			
			boolean valid = ((current_time < expiration) ? true : false);
			System.out.println("isValid: returning " + valid);
			return valid;
		}
		else
		{
			System.out.println("isValid: returning true by default");
			return true;
		}
	}

/**
 * Gets the expiration as a long type.
 * @return long
 */
	public long getExpiration()
	{
		// Gets the expiration value from the hashtable
		String[] expiration = getAttributes("expiration");

		if (expiration != null && expiration[0] != null)
		{
			// The expiration is the first element and there should only be one expiration
			System.out.println("getExpiration: returning " + expiration[0]);
			return new Long(expiration[0]).longValue();
		}

		System.out.println("getExpiration: returning 0");
		return 0;
	}

/**
 * Returns if this token should be forwarded/propagated downstream.
 * @return boolean
 */
	public boolean isForwardable()
	{
     // You can choose whether your token gets propagated. In some cases
     // you might want it to be local only.
		return true;  
	}

/**
 * Gets the principal to which this token belongs. If this is an 
 * authorization token, this principal string must match the 
 * authentication token principal string or the message is rejected.
 * @return String
 */
	public String getPrincipal()
	{
		// This value might be any combination of attributes
		String[] principal = getAttributes("principal");

		if (principal != null && principal[0] != null)
		{
			return principal[0];
		}

		System.out.println("getExpiration: returning null");
		return null;
	}

/**
 * Returns a unique identifier of the token based upon information the provider
 * considers makes this a unique token. This identifier is used for caching purposes
 * and can be used in combination with other token unique IDs that are part of
 * the same Subject.
 *
 * This method should return null if you want the accessID of the user to represent
 * uniqueness.  This is the typical scenario.
 *
 * @return String
 */
	public String getUniqueID()
	{
     // If you do not want to affect the cache lookup, just return NULL here.
     return null;

		String cacheKeyForThisToken = "dynamic attributes";

     // If you do want to affect the cache lookup, return a string of
     // attributes that you want factored into the lookup.
		return cacheKeyForThisToken;
	}

/**
 * Gets the bytes to be sent across the wire. The information in the byte[]
 * needs to be enough to recreate the token object at the target server.
 * @return byte[]
 */
	public byte[] getBytes ()
	{
		if (hashtable != null)
		{
			try
			{
          // Do this if the object is set read-only during login commit
          // because this ensures that new data is not set.
				if (isReadOnly() && tokenBytes == null)
					tokenBytes = custom_encryption_algorithm (hashtable);

				return tokenBytes;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}

		System.out.println("getBytes: returning null");
		return null;
	}

/**
 * Gets the name of the token, which is used to identify the byte[] in the 
 * protocol message.
 * @return String
 */
	public String getName()
	{
		return oidName;
	}

/**
 * Gets the version of the token as a short type.  This also is used
 * to identify the byte[] in the protocol message.
 * @return short
 */
	public short getVersion()
	{
		String[] version = getAttributes("version");

		if (version != null && version[0] != null)
			return new Short(version[0]).shortValue();

		System.out.println("getVersion: returning default of 1");
		return 1;
    }

/**
 * When called, the token becomes irreversibly read-only.  The implementation
 * needs to ensure that any set methods check that this state has been set.
 */
	public void setReadOnly()
	{
		addAttribute("readonly", "true");
	}

/**
 * Called internally to see if the token is read-only
 */
	private boolean isReadOnly()
	{
		String[] readonly = getAttributes("readonly");

		if (readonly != null && readonly[0] != null)
			return new Boolean(readonly[0]).booleanValue();

		System.out.println("isReadOnly: returning default of false");
		return false;
	}

/**
 * Gets the attribute value based on the named value.
 * @param String key
 * @return String[]
 */
	public String[] getAttributes(String key)
	{
		ArrayList array = (ArrayList) hashtable.get(key);

		if (array != null && array.size() > 0)
		{
			return (String[]) array.toArray(new String[0]);
		}

		return null;
	}

/**
 * Sets the attribute name/value pair.  Returns the previous values set for key,
 * or null if not previously set.
 * @param String key
 * @param String value
 * @returns String[];
 */
	public String[] addAttribute(String key, String value)
	{
		// Gets the current value for the key
		ArrayList array = (ArrayList) hashtable.get(key);
        
		if (!isReadOnly())
		{
			// Copies the ArrayList to a String[] as it currently exists
			String[] old_array = null;
			if (array != null && array.size() > 0)
				old_array = (String[]) array.toArray(new String[0]);

			// Allocates a new ArrayList if one was not found
			if (array == null)
				array = new ArrayList();

			// Adds the String to the current array list
			array.add(value);

			// Adds the current ArrayList to the Hashtable
			hashtable.put(key, array);

			// Returns the old array
			return old_array;
		}

		return (String[]) array.toArray(new String[0]);
	}

    
/**
 * Gets the list of all attribute names present in the token.
 * @return java.util.Enumeration
 */
	public java.util.Enumeration getAttributeNames()
	{
		return hashtable.keys();
	}

/**
 * Returns a deep copying of this token, if necessary.
 * @return Object
 */
	public Object clone()
	{
		com.ibm.wsspi.security.token.AuthenticationToken deep_clone = 
			new com.ibm.websphere.security.token.CustomAuthenticationTokenImpl();

		java.util.Enumeration keys = getAttributeNames();

		while (keys.hasMoreElements()) 
		{
			String key = (String) keys.nextElement();

			String[] list = (String[]) getAttributes(key);
            
			for (int i=0; i<list.length; i++)
				deep_clone.addAttribute(key, list[i]);
		}
    
    		return deep_clone;
	}

/**
 * This method returns true if this token is storing a user ID and password 
 * instead of a token.
 * @return boolean
 */
	public boolean isBasicAuth()
	{
		return false;
	}
}


//   http://pic.dhe.ibm.com/infocenter/wasinfo/v7r0/index.jsp?topic=%2Fcom.ibm.websphere.express.doc%2Finfo%2Fexp%2Fae%2Ftsec_custauthentimpl.html