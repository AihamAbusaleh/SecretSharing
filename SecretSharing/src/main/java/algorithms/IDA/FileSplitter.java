package algorithms.IDA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import com.google.api.client.testing.util.TestableByteArrayInputStream;

import lib.finiteFieldLibrary.GF2N;
import lib.finiteFieldLibrary.Matrix;
import lib.finiteFieldLibrary.MatrixGF2N;

/**
 * @author AIN Class to split the original File into slices " IDA Algorithm "
 */
public class FileSplitter {

	static long irreduciblePolynomial = 265;
	static GF2N galoisField = new GF2N(irreduciblePolynomial);
	static MatrixGF2N matGf = new MatrixGF2N(galoisField);

	/**
	 * @param originalFile
	 *            to be read into byte array
	 * @param min
	 *            the number of slices in order to recombine the original file
	 * @return a Matrix of bytes
	 * @throws Exception
	 */
	public static Matrix createMatrixFromOriginalFile(File originalFile, int min) throws Exception {

		byte[] originalFileInBytes = new byte[(int) originalFile.length()];
		Matrix originalF = null;
		Matrix toMatrix = null;

		byte[][] matrixF;
		long[][] toLong;

		if (originalFile.length() % min == 0) {
			matrixF = new byte[(int) originalFileInBytes.length / min][min];

		} else
			matrixF = new byte[(int) originalFileInBytes.length / min + 1][min];

		try {
			FileInputStream input = new FileInputStream(originalFile);
			input.read(originalFileInBytes);
			input.close();
			int start = 0;

			for (int i = 0; i < matrixF.length; i++) {
				matrixF[i] = Arrays.copyOfRange(originalFileInBytes, start, start + min);
				start += min;
			}

			toLong = Casting.castByteToLong(matrixF);
			toMatrix = new Matrix(toLong);
			toMatrix.transpose();

			originalF = new Matrix(toMatrix, galoisField.getFieldSize());

		} catch (FileNotFoundException ex) {
			System.out.println("the file does not exist");
		} catch (IOException ex) {
			System.out.println("cannot read/write from/to a file");
		}

		return originalF;
	}

	/**
	 * @param originalFile
	 *            to be split into slices
	 * @param max
	 *            the number of slices
	 * @param min
	 *            the number of slices in order to recombine the original file
	 * @throws Exception
	 */
	public static void splitOriginalFile(File originalFile, int max, int min) throws Exception {

		Matrix matrixF = createMatrixFromOriginalFile(originalFile, min);

		Matrix matrixA = createEncryptingMatrix(max, min);
		Matrix matrixC = matGf.multiply(matrixA, matrixF);

		File[] slices = new File[matrixC.getRows()];

		try {
			for (int i = 0; i < matrixC.getRows(); i++) {
				slices[i] = new File("_" + i + ".splt");
				PrintWriter out = new PrintWriter(originalFile.getAbsolutePath() + slices[i].getName());

				for (int j = 0; j < matrixC.getColumns(); j++) {

					out.print(matrixC.getElement(i, j) + " ");

				}
				for (int k = 0; k < matrixA.getColumns(); k++) {

					out.print("?" + matrixA.getElement(i, k));
				}
				out.flush();
				out.close();

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * @param max
	 *            number of rows , define the number of slices, that the file
	 *            should be split
	 * @param min
	 *            number of columns
	 * @return random encrypting matrix in GF256
	 */
	public static Matrix createEncryptingMatrix(int max, int min) {

		// Matrix matrixA = new Matrix(max, min, galoisField.getFieldSize());
		 
		Matrix v = new Matrix();

		Matrix vandermonde = new Matrix(v.vandermonde(max, min, galoisField.getFieldSize()));
		return vandermonde;
	}

//	public static void main(String[] args) throws Exception {
//		  splitOriginalFile(new File("D:/TEST/testen.txt"), 4, 2);
//	
//	}
}
