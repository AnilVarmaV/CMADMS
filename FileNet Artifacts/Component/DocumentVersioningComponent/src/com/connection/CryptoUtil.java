package com.connection;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

/**
 * 
 *CryptoUtil class is used to Decrypt and encrypt the Strings based on AES algorithm
 *
 */
public class CryptoUtil 
{
	
	static Logger logger=Logger.getLogger(CryptoUtil.class);
	
	private static final String AES = "AES";

	// This key would be of 32 characters and will be generated by keyGenerator method. 
	// Pls refer keyGenerator method.

	private static final String secretKey = "6116E4275C796244D00150100E45DCAF";

	/** 
	 * @return the key to be used for encryption as well as decryption.
	 * @throws NoSuchAlgorithmException
	 */
	public static String keyGenerator() throws NoSuchAlgorithmException
	{
		//logger.info("Start of keyGenerator ");
		KeyGenerator keyGen = KeyGenerator.getInstance(CryptoUtil.AES);
		keyGen.init(128);
		SecretKey sk = keyGen.generateKey();
		//return sk;
		//logger.info("End of keyGenerator ----byteArrayToHexString(sk.getEncoded()) is--->"+byteArrayToHexString(sk.getEncoded()));
		return  byteArrayToHexString(sk.getEncoded());
		
	}

	/**
	 * encrypt a value using the key mentioned in this java file.
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static String encryptPhrase(String value) throws GeneralSecurityException, IOException 
	{
		//logger.info("Start of encryptPhrase---value is---> "+value);
		SecretKeySpec sks = getSecretKeySpec();
		Cipher cipher = Cipher.getInstance(CryptoUtil.AES);
		cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
		byte[] encrypted = cipher.doFinal(value.getBytes());
		//logger.info("End of encryptPhrase---byteArrayToHexString(encrypted) is---> "+byteArrayToHexString(encrypted));
		return byteArrayToHexString(encrypted);
	}

	/**
	 * decrypt a value[Password]
	 * @throws Exception 
	 * @throws GeneralSecurityException
	 * @throws IOException
	 */
	public static String decryptPhrase(String message) 
	{
		//logger.info("start of decryptPhrase ----message is--->"+message);
		try
		{
			SecretKeySpec sks = getSecretKeySpec();
			Cipher cipher = Cipher.getInstance(CryptoUtil.AES);
			cipher.init(Cipher.DECRYPT_MODE, sks);
			byte[] decrypted = cipher.doFinal(hexStringToByteArray(message));
			//logger.info("End of decryptPhrase ---new String(decrypted) is--->"+new String(decrypted));
			return new String(decrypted);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}

		// return blank string in case of exception
		//logger.info("End of decryptPhrase ");
		return "";

	}

	/**
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	private static SecretKeySpec getSecretKeySpec()throws NoSuchAlgorithmException, IOException 
	{
		//logger.info("---Start of getSecretKeySpe---");
		byte[] key =hexStringToByteArray(secretKey); 
		SecretKeySpec sks = new SecretKeySpec(key, CryptoUtil.AES);
		//logger.info("---Start of getSecretKeySpe---SecretKeySpec is--->"+sks);
		return sks;
	}


	private static String byteArrayToHexString(byte[] b) 
	{
		//logger.info("Start of byteArrayToHexString ---byte--->"+b);
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) 
		{
			int v = b[i] & 0xff;
			if (v < 16) 
			{
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		//logger.info("End of byteArrayToHexString ---sb.toString().toUpperCase() is--->"+sb.toString().toUpperCase());
		return sb.toString().toUpperCase();
	}

	private static byte[] hexStringToByteArray(String s) 
	{
		//logger.info("start of hexStringToByteArray ---String is--->"+s);
		byte[] b = new byte[s.length() / 2];
		
		for (int i = 0; i < b.length; i++) {
			int index = i * 2;
			int v = Integer.parseInt(s.substring(index, index + 2), 16);
			b[i] = (byte) v;
		}
		//logger.info("End of hexStringToByteArray ---byte is--->"+b);
		return b;
	}
	
	public static void main(String[] args) {
		try {
		//String	emailPWd ="8BB952802879AE331EBFE6583105FEC0";
		//String encryptPhrase = encryptPhrase("D120120*");
		String encryptPhrase = encryptPhrase("mits123$");

		System.out.println("Encript::"+encryptPhrase);
		System.out.println("Decrpt::"+decryptPhrase(encryptPhrase));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
