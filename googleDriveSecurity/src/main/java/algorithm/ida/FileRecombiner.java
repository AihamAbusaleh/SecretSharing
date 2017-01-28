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
	public static void recombineMyOriginalFile(File slice1, File slice2) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidAlgorithmParameterException, CryptoException {

		List<File> allFile = new ArrayList<File>();
		allFile.add(slice1);
		allFile.add(slice2);

		// split the added extension in the slices and make the new original
		// file and store it in D:/TEST or in the project directory, here need
		// to remove D:/TEST
		int index;
		index = slice1.getName().lastIndexOf('_');
		File dest = new File("D:/TEST/" + slice1.getName().substring(0, index - 10));
 		List<Integer> list = new ArrayList<Integer>();
		int[] arraylist;
		int[][] ccMatrix;

		Scanner scan;
		try {

			for (File f : allFile) {
				scan = new Scanner(f);
				// copy the content of 2 slices into a list
				while (scan.hasNext()) {
					String str = scan.nextLine();
					System.out.println(str);
					String[] splitString = str.split(" ");
					for (String number : splitString) {
						list.add(Integer.parseInt(number));

					}
				}
			}
			// make 2D array from the list
			arraylist = Ints.toArray(list);
			ccMatrix = Converting.convert1Dto2D(arraylist, 2, list.size() / 2);

			byte[] fileArray = myFile(ccMatrix);
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
	 * @return the original array (the content of the original file )
	 */
	public static byte[] myFile(int[][] array) {

		SimpleMatrix inverse = createInverseMatrix();

		SimpleMatrix inverting = new SimpleMatrix(
				Converting.castingTo2dDoubleFrom2dInt(Converting.castingTo2dInt(inverse)));

		SimpleMatrix ccMatrix = new SimpleMatrix(Converting.castingTo2dDoubleFrom2dInt(array));
		SimpleMatrix theFileArray = inverting.mult(ccMatrix);
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
	public static void restoreOriginalFile(byte[] fileArray, File dest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
  		 
  
		try {

			FileOutputStream fileOuputStream = new FileOutputStream(dest.getAbsolutePath());
			fileOuputStream.write( fileArray);
			fileOuputStream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * this is the inverse of A` Matrix (extracted Matrix of A), used to
	 * recombined the F Matrix, by multiplying it with C`
	 * 
	 * @return inverse Matrix of A
	 */
	public static SimpleMatrix createInverseMatrix() {
		// create A Matrix (3x2 Matrix)
		SimpleMatrix aMatrix = FileSplitter.createMatrixA();

		SimpleMatrix aaMatrix = new SimpleMatrix();
		SimpleMatrix inverse = new SimpleMatrix();

		// create A` Matrix, this is extracted from A
		aaMatrix = aMatrix.extractMatrix(0, aMatrix.numCols(), 0, aMatrix.numCols());
		// create the inverse of A`
		inverse = aaMatrix.invert();

		return inverse;
	}

	 public static void main(String ar[]) throws IOException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, CryptoException {
	 String name1 = "D:/testen.txt.encrypted_0.splt";
	 String name2 = "D:/testen.txt.encrypted_1.splt";
	 File slice1 = new File(name1);
	 File slice2 = new File(name2);
	 recombineMyOriginalFile(slice1, slice2);
	 }

}
