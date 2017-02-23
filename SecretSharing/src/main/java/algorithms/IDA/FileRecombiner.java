package algorithms.IDA;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import org.glassfish.hk2.utilities.binding.ScopedNamedBindingBuilder;

import com.google.api.client.testing.util.TestableByteArrayInputStream;
import com.google.common.primitives.Ints;

import algorithms.AES.CryptoException;
import algorithms.AES.CryptoUtils;
import fm4j.matrix.util.mul.RowByRow;
import lib.finiteFieldLibrary.GF2N;
import lib.finiteFieldLibrary.Matrix;
import lib.finiteFieldLibrary.MatrixGF2N;

/**
 * this class is used to recombine the original file from min of slices
 * 
 * @author AIN
 *
 */
public class FileRecombiner {

	static long irreduciblePolynomial = 1033;
	static GF2N galoisField = new GF2N(irreduciblePolynomial);
	static MatrixGF2N matGf = new MatrixGF2N(galoisField);
	static final String PATH = "D:/";
	/**
	 * @param min
	 *            the number of slices
	 * @return a list of the slices in order to recombine the file from them
	 * @throws InvalidAlgorithmParameterException
	 * @throws CryptoException
	 * @throws IOException
	 */
	public static File[] getEncryptedSlices(int min)
			throws InvalidAlgorithmParameterException, CryptoException, IOException {

		File dir = new File(PATH); 
		File[] filesInDir = dir.listFiles(new Extension());
		File[] filesInDirMin = new File[min];
		int i = 0;

		for (File f : filesInDir) {

			File dec = null;
			File temp = File.createTempFile(f.getAbsolutePath(), ".tmp");

			temp.deleteOnExit();
			if (i != min) {

				dec = CryptoUtils.decrypt(f, temp);
				dec = CryptoUtils.decrypt(f, temp);
				dec = CryptoUtils.decrypt(f, temp);
				filesInDirMin[i] = dec;

				i++;
			}

		}

		return filesInDirMin;

	}

	/**
	 * @param slices
	 *            the reconstructed matrix from min slices
	 * @param subAMatrix
	 *            the sub matrix from the encrypting Matrix
	 * @return
	 */
	public static byte[] getTheOriginalByteArray(Matrix slices, Matrix subAMatrix) {

		Matrix invertSubMatrix = matGf.inverse(subAMatrix);
		Matrix originalMatrix = matGf.multiply(invertSubMatrix, slices);
		originalMatrix.transpose();

		int[][] test = Casting.castFromMatrixToInt(originalMatrix);
		int[] matrixToArray = Casting.convert2Dto1D(test);
		byte[] intTobyte = Casting.castToByte(matrixToArray);
		return intTobyte;
	}

	@SuppressWarnings("resource")
	public static void reconstructTheOriginalFileFromMinSlices(int min)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, CryptoException, IOException {

		File[] filesInDir = getEncryptedSlices(min);
		int index;

		index = filesInDir[0].getName().lastIndexOf('_');
		File dest = new File(PATH + filesInDir[0].getName().substring(0, index)); // change the path
		// File dest = new File("D:/" + "original");

		List<File> allSlices = new ArrayList<File>();

		for (File file : filesInDir) {
			allSlices.add(file);
		}

		List<Integer> listOfStoredIntegers = new ArrayList<Integer>();
		List<Integer> listOfStoredKeys = new ArrayList<Integer>();

		int[] convertListOfStoredIntegers;
		int[] convertListOfStoredKeys;
		int[][] subCMatrix;
		int[][] subAMatrix;
		String firstPart = " ";
		String secoundPart = " ";

		Scanner scan;
		try {
			for (File f : allSlices) {
				scan = new Scanner(f);
				// copy the content of slices into a list
				while (scan.hasNext()) {
					String str = scan.nextLine();
					firstPart = str.substring(0, (str.indexOf("?") - 1));
					String[] splitString = firstPart.split(" ");

					for (String number : splitString) {
						listOfStoredIntegers.add(Integer.parseInt(number));

					}
					secoundPart = str.substring(firstPart.length() + 2, str.length());

					String s = secoundPart.replaceAll("\\?", " ");

					String[] splitMatrix = s.split(" ");
					if (splitMatrix.length != min) {
						System.out.println(
								"you need " + splitMatrix.length + " Files in order to recombine the original file !");
						return;
					}
					for (String number : splitMatrix) {
						listOfStoredKeys.add(Integer.parseInt(number));

					}

				}

			}

			convertListOfStoredIntegers = Ints.toArray(listOfStoredIntegers);

			subCMatrix = Casting.convert1Dto2D(convertListOfStoredIntegers, min,
					convertListOfStoredIntegers.length / min);
			Matrix toMatrix = new Matrix(Casting.castToLong(subCMatrix));
			Matrix subCMatrixInGF256 = new Matrix(toMatrix, galoisField.getFieldSize());

			convertListOfStoredKeys = Ints.toArray(listOfStoredKeys);

			subAMatrix = Casting.convert1Dto2D(convertListOfStoredKeys, min, min);
			Matrix toMatrix2 = new Matrix(Casting.castToLong(subAMatrix));
			Matrix subAMatrixInGF256 = new Matrix(toMatrix2, galoisField.getFieldSize());

			byte[] fileArray = getTheOriginalByteArray(subCMatrixInGF256, subAMatrixInGF256);

			createTheOriginalFile(trim(fileArray), dest);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static void createTheOriginalFile(byte[] fileArray, File dest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {

		try {

			FileOutputStream fileOuputStream = new FileOutputStream(dest.getAbsolutePath());
			fileOuputStream.write(fileArray);
			fileOuputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	static byte[] trim(byte[] bytes) {
		int i = bytes.length - 1;
		while (i >= 0 && bytes[i] == 0) {
			--i;
		}

		return Arrays.copyOf(bytes, i + 1);
	}

	public static void main(String[] args)
			throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, CryptoException, IOException {

  	reconstructTheOriginalFileFromMinSlices(12);

	}

}
