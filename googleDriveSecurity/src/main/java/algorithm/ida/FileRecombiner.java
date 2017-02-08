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

/**
 * this class is used to recombine the original file from two files(slices.splt)
 * 
 * @author AIN
 *
 */
public class FileRecombiner {

	/**
	 * recombine original file
	 * 
	 * @param slice1
	 *            the first slice
	 * @param slice2
	 *            the secound slice
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws CryptoException
	 * @throws InvalidAlgorithmParameterException
	 */
	public static void recombineMyOriginalFile(int min) throws NoSuchAlgorithmException, UnsupportedEncodingException,
			InvalidAlgorithmParameterException, CryptoException {
		File[] allSlices = new File[min];
		File dir = new File("D:/");
		File[] filesInDir = dir.listFiles(new Extension());
		int i = 0;

		List<File> allFile = new ArrayList<File>();
		if (filesInDir.length != min) {
			System.out.println("please select just " + min + " files");
			return;
		}
		// gets files with extension 'splt'
		for (File f : filesInDir) {

			allSlices[i] = new File(f.getAbsolutePath());

			allFile.add(allSlices[i]);
			i++;
		}

		// split the added extension in the slices and make the new original
		// file and store it in D:/TEST or in the project directory, here need
		// to remove D:/TEST
		int index;
		index = allSlices[0].getName().lastIndexOf('_');
		File dest = new File("D:/TEST/" + allSlices[0].getName().substring(0, index - 10));
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
					System.out.println(str);
					firstPart = str.substring(0, str.indexOf("?") - 1);
					System.out.println(firstPart);

					String[] splitString = firstPart.split(" ");

					for (String number : splitString) {
						list.add(Integer.parseInt(number));

					}
					secoundPart = str.substring(firstPart.length() + 2, str.length());
					String s = secoundPart.replaceAll("\\?", " ");

					System.out.println(s);
					String[] splitMatrix = s.split(" ");

					for (String number : splitMatrix) {
						listKey.add(Integer.parseInt(number));

					}
				}
			}

			// make 2D array from the list
			arraylist = Ints.toArray(list);
			System.out.println("Arraylist " + Arrays.toString(arraylist));
			System.out.println("min " + min);
			System.out.println("List size " + list.size());
			ccMatrix = Converting.convert1Dto2D(arraylist, min, list.size() / min);
			// for (int j = 0; j < ccMatrix.length; j++) {
			// System.out.println("Matrix " + Arrays.toString(ccMatrix[j]) + "L
			// " + ccMatrix[j].length);
			//
			// }

			arrayKeys = Ints.toArray(listKey);

			System.out.println("arrayKeys " + Arrays.toString(arrayKeys) + "Length " + arraylist.length);
			subAMatrix = Converting.convert1Dto2D(arrayKeys, min, min);

			byte[] fileArray = myFile(ccMatrix, subAMatrix);
			// System.out.println("fileArray " + Arrays.toString(fileArray) + "L
			// " + fileArray.length);

			restoreOriginalFile(fileArray, dest);

			CryptoUtils.decrypt(dest, dest);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param array
	 *            the 2D array, that has to be converted to 1D byte array , in
	 *            order to write it into new File
	 * @param subAMatrix
	 * @return the original array (the content of the original file )
	 */
	public static byte[] myFile(int[][] array, int[][] subAMatrix) {

		SimpleMatrix inverse = createInverseMatrix(subAMatrix);
		inverse.print();

		SimpleMatrix ccMatrix = new SimpleMatrix(Converting.castingTo2dDoubleFrom2dInt(array));
		SimpleMatrix theFileArray = inverse.mult(ccMatrix);
		SimpleMatrix transposed = theFileArray.transpose();

		int[] fileArray = Converting.convert2Dto1D(Converting.castingTo2dInt(transposed));

		byte[] fileArrayInt = Converting.castingTo1dByte(fileArray);

		return fileArrayInt;
	}

	/**
	 * 
	 * @param fileArray
	 *            the original array (the content of the original file )
	 * @param dest
	 *            create file to write data into it (original File)
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 */
	public static void restoreOriginalFile(byte[] fileArray, File dest)
			throws NoSuchAlgorithmException, UnsupportedEncodingException {
		System.out.println("Bin Da " + Arrays.toString(fileArray));

		try {

			FileOutputStream fileOuputStream = new FileOutputStream(dest.getAbsolutePath());
			fileOuputStream.write(fileArray);
			fileOuputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * this is the inverse of A` Matrix (extracted Matrix of A), used to
	 * recombined the F Matrix, by multiplying it with C`
	 * 
	 * @param subAMatrix
	 * 
	 * @return inverse Matrix of A
	 */
	public static SimpleMatrix createInverseMatrix(int[][] subAMatrix) {
		// create A Matrix (3x2 Matrix)
		SimpleMatrix aMatrix = new SimpleMatrix(Converting.castingTo2dDoubleFrom2dInt(subAMatrix));
		SimpleMatrix inverse = new SimpleMatrix();

		// create the inverse of A`
		inverse = aMatrix.invert();
		return inverse;
	}

	public static void main(String ar[])
			throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CryptoException {

		recombineMyOriginalFile(3);
		// String s = "Ich bin da";
		//
		// restoreOriginalFile(s.getBytes(), new File("D:/TEST/testen.txt"));
	}

}
