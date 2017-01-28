package algorithm.ida;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import org.ejml.simple.SimpleMatrix;

 import algorithm.aes256sha256.CryptoUtils;

public class FileSplitter {

	/**
	 * 
	 * @param theOrigignalFile
	 *            to be divided into 2 slices
	 * 
	 * @return Matrix F transposed from the original File
	 * @throws Exception 
	 */
	public static SimpleMatrix createMatrixF(File encryptedFile) throws Exception {

		
		final int min = 2; // the number of slices in order to recombine File is 2
		
		
		byte[] fileF = new byte[(int) encryptedFile.length()];
		byte[][] matrixF;
		// in order to work with the ejml library , need a double 2D matrix
		double[][] matrixFToDouble;

		SimpleMatrix transposedF = new SimpleMatrix();
		SimpleMatrix fMatrix = new SimpleMatrix();

		if (encryptedFile.length() % min == 0) {
			matrixF = new byte[(int) fileF.length / min][min];

		} else
			matrixF = new byte[(int) fileF.length / min + 1][min];

		try {
			FileInputStream input = new FileInputStream(encryptedFile);
			input.read(fileF);
			input.close();

			int start = 0;
			// divide the 1D array into 2D array with 2 cols and a number of
			// rows
			for (int i = 0; i < matrixF.length; i++) {
				matrixF[i] = Arrays.copyOfRange(fileF, start, start + min);
				start += min;

			}
			// in order to use "ejml" library
			matrixFToDouble = Converting.catingTo2dDouble(matrixF);
			fMatrix = new SimpleMatrix(matrixFToDouble);
			// transpose the matrix F(thefile) to get 2 rows and number of cols
			transposedF = fMatrix.transpose();
 		} catch (FileNotFoundException ex) {
			System.out.println("the file does not exist");
		} catch (IOException ex) {
			System.out.println("cannot read/write from/to a file");
		}

		return transposedF;
	}

	/**
	 * this is the method that creates slices from the original file, this
	 * method is to be invoked in order to split the original file. Each row in
	 * the C matrix (A*F=C) is a slice, we need just 2 slices in order to
	 * recombine the original file
	 * 
	 * @param source
	 *            the file to be divided into slices
	 * @throws Exception 
	 */
	public static File splitMyOriginalFileIntoSlices(File theOrigignalFile) throws Exception {

		File newSource = new File(theOrigignalFile.getAbsolutePath() + ".encrypted");
		File encryptedFile =  CryptoUtils.encrypt(theOrigignalFile, newSource);
	 
		SimpleMatrix matrixF = createMatrixF(encryptedFile); // F Matrix
		SimpleMatrix aMatrix = createMatrixA(); // A Matrix
		SimpleMatrix cMatrix = aMatrix.mult(matrixF); // A * F = C
		// extract the first 2 slices (first 2 rows ) from the C matrix and make
		// file from each one
		SimpleMatrix ccMatrix = cMatrix.extractMatrix(0, matrixF.numRows(), 0, cMatrix.numCols());

		int[][] ccMAtrixInt = Converting.castingTo2dInt(ccMatrix);
		File[] fileShares = new File[ccMatrix.numRows()]; // make 2 files

		try {
			for (int i = 0; i < ccMatrix.numRows(); i++) {
				// make file with new extensions , ( orginal.txt_0.splt )
				fileShares[i] = new File(encryptedFile.getName() + "_" + i + ".splt");
 				// store them in Parent directory or in the project, here need to remove getParent
				PrintWriter out = new PrintWriter(theOrigignalFile.getParent() + fileShares[i]);
				// print the first row to file and the secound row to another
				// file
 				for (int j = 0; j < ccMatrix.numCols(); j++) {
					out.print(( ccMAtrixInt[i][j])  + " ");
				}
				out.close();
			}
		//	CryptoUtils.encrypt( fileShares[0], fileShares[0] );
			
		} catch (Exception e) {
			e.printStackTrace();
		}
  		return fileShares[0];

	}

	/**
	 * this matrix is used to get the Slices (C Matrix) by multiplying it with F
	 * Matrix (the orginal file)
	 * 
	 * @return A Matrix
	 */
	public static SimpleMatrix createMatrixA() {

		double a[][] = new double[][] { { 5, 3 }, { 7, 4 }, { 2, 1 } };
		SimpleMatrix aMatrix = new SimpleMatrix(a);

		return aMatrix;
	}

	public static void main(String... aArgs) throws Exception {
		FileSplitter.splitMyOriginalFileIntoSlices(new File("D:/testen.txt"));
		
	}
	
	 
}
