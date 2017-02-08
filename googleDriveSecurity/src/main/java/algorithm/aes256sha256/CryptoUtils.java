package algorithm.aes256sha256;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * A utility class that encrypts or decrypts a file. 
 * @author www.codejava.net
 *
 */
public class CryptoUtils {
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	
	private static final String ENCRYPTION_KEY = "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk";
	private static final String ENCRYPTION_IV = "4e5Wa71fYoT7MFEX";

	public static File encrypt(  File inputFile, File outputFile)
			throws CryptoException, InvalidAlgorithmParameterException {
		try {
			doCrypto(Cipher.ENCRYPT_MODE,   inputFile, outputFile);
			 
			return outputFile;
		} catch (Exception e) {
 			e.printStackTrace();
		}
		return outputFile;
	}

	public static File  decrypt(  File inputFile, File outputFile)
			throws CryptoException, InvalidAlgorithmParameterException {
		doCrypto(Cipher.DECRYPT_MODE,   inputFile, outputFile);
		return outputFile;
	}
	
	
	
	static AlgorithmParameterSpec makeIv() {
		try {
			return new IvParameterSpec(ENCRYPTION_IV.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	static Key makeKey() throws NoSuchAlgorithmException, UnsupportedEncodingException {

		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] key = md.digest(ENCRYPTION_KEY.getBytes("UTF-8"));

		key = Arrays.copyOf(key, 32);

		return new SecretKeySpec(key, ALGORITHM);

	}
	private static void doCrypto(int cipherMode,  File inputFile,
			File outputFile) throws CryptoException, InvalidAlgorithmParameterException {
		try {
			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(cipherMode, makeKey(), makeIv());
			
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);
			
			byte[] outputBytes = cipher.doFinal(inputBytes);
			
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
		

			inputStream.close();
			outputStream.close();
			
		} catch (NoSuchPaddingException | NoSuchAlgorithmException
				| InvalidKeyException | BadPaddingException
				| IllegalBlockSizeException | IOException ex) {
			throw new CryptoException("Error encrypting/decrypting file", ex);
		}
	}
	
	public static void main(String[] args) throws InvalidAlgorithmParameterException, CryptoException{
	 //	encrypt(new File("D:/testen.txt.encrypted_0.splt"), new File("D:/ENCENCENCENCENC.txt"));
	 	decrypt(new File("D:/ENCENCENCENCENC.txt"), new File("D:/DECCCCCCCCC.txt"));
	}
}
