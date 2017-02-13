package algorithm.ida;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import org.ejml.simple.SimpleMatrix;

import com.google.common.primitives.Ints;

import algorithm.aes256sha256.CryptoException;
import algorithm.aes256sha256.CryptoUtils;
import classes.GF2N;
import classes.Matrix;
import classes.MatrixGF2N;

/**
 * this class is used to recombine the original file from two files(slices.splt)
 * 
 * @author AIN
 *
 */
public class FileRecombiner {

	static long irreduciblePolynomial = 265;
	static GF2N galoisField = new GF2N(irreduciblePolynomial);
	static MatrixGF2N matGf = new MatrixGF2N(galoisField);

	public static void recombineMyOriginalFile(int min) throws NoSuchAlgorithmException, UnsupportedEncodingException,
			InvalidAlgorithmParameterException, CryptoException {
		File[] allSlices = new File[min];
		File dir = new File("D:/");
		File[] filesInDir = dir.listFiles(new Extension());
		int i = 0;

		List<File> allFile = new ArrayList<File>();

		for (File f : filesInDir) {
			if (i != min) {
				allSlices[i] = new File(f.getAbsolutePath());

				allFile.add(allSlices[i]);
				i++;
			}
		}

		int index;
		index = allSlices[0].getName().lastIndexOf('_');
		File dest = new File("D:/TEST/" + allSlices[0].getName().substring(0, index ));
		List<Integer> list = new ArrayList<Integer>();
		List<Integer> listKey = new ArrayList<Integer>();

		int[] arraylist;
		int[] arrayKeys;
		int[][] ccMatrix;
		int[][] subAMatrix;
		String firstPart = " ";
		String secoundPart = " ";

		Scanner scan;
		try {

			for (File f : allFile) {
				scan = new Scanner(f);
				// copy the content of 2 slices into a list
				while (scan.hasNext()) {
					String str = scan.nextLine();
					firstPart = str.substring(0, str.indexOf("?") - 1);

					String[] splitString = firstPart.split(" ");

					for (String number : splitString) {
						list.add(Integer.parseInt(number));

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
						listKey.add(Integer.parseInt(number));

					}
				}
			}

			// make 2D array from the list
			arraylist = Ints.toArray(list);

			ccMatrix = Converting.convert1Dto2D(arraylist, min, list.size() / min);
			Matrix sl = new Matrix(Converting.catingTo2dLongFromInt(ccMatrix));
			Matrix inGF = new Matrix(sl, galoisField.getFieldSize());
		//	System.out.println("Matrixssss " + inGF);
			arrayKeys = Ints.toArray(listKey);

			subAMatrix = Converting.convert1Dto2D(arrayKeys, min, min);
			long[][] con = Converting.catingTo2dLongFromInt(subAMatrix);
			Matrix subGF = new Matrix(con);
			Matrix subOverGF = new Matrix(subGF, galoisField.getFieldSize());
		//	System.out.println("Sub A Matrix " + subOverGF);
			byte[] fileArray = myFile(inGF, subOverGF);

			restoreOriginalFile(fileArray, dest);
		//	CryptoUtils.decrypt(dest, new File(dest.getAbsolutePath()));

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static byte[] myFile(Matrix array, Matrix subAMatrix) {

		Matrix invertedMatrix = matGf.inverse(subAMatrix);
		System.out.println("inverse " + invertedMatrix);
	 	Matrix result = matGf.multiply(invertedMatrix, array);
	 	result.transpose();
 		int[] fileArray = Converting.convert2Dto1D(Converting.catingTo2dLongFromInt(result));

		byte[] fileArrayInt = Converting.castingTo1dByte(fileArray);
		System.out.println("Result " + Arrays.toString(fileArrayInt));
 		return fileArrayInt;
	}

	public static void restoreOriginalFile(byte[] fileArray, File dest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		try {

			FileOutputStream fileOuputStream = new FileOutputStream(dest.getAbsolutePath());
			fileOuputStream.write(fileArray);
			fileOuputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
 

	public static void main(String ar[])
			throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CryptoException {

		recombineMyOriginalFile(3);

	}

}
