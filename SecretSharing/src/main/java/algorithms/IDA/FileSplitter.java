package algorithm.ida;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import lib.finiteFieldLibrary.GF2N;
import lib.finiteFieldLibrary.Matrix;
import lib.finiteFieldLibrary.MatrixGF2N;
 

public class FileSplitter {

	static long irreduciblePolynomial = 265;
	static GF2N galoisField = new GF2N(irreduciblePolynomial);
	static MatrixGF2N matGf = new MatrixGF2N(galoisField);

	public static Matrix createMatrixF(File encryptedFile, int min) throws Exception {
		Matrix originalF = null;
		byte[] fileF = new byte[(int) encryptedFile.length()];
		byte[][] matrixF;
		long[][] matrixFToDouble;
		Matrix transposedF = null;

		if (encryptedFile.length() % min == 0) {
			matrixF = new byte[(int) fileF.length / min][min];

		} else
			matrixF = new byte[(int) fileF.length / min + 1][min];

		try {
			FileInputStream input = new FileInputStream(encryptedFile);
			input.read(fileF);

			input.close();

			int start = 0;

			for (int i = 0; i < matrixF.length; i++) {
				matrixF[i] = Arrays.copyOfRange(fileF, start, start + min);
				start += min;

			}

			int[][] fileFinteger = Converting.castingFromByteToInt(matrixF);

			matrixFToDouble = Converting.catingTo2dLong(fileFinteger);

			transposedF = new Matrix(matrixFToDouble);

			transposedF.transpose();

			originalF = new Matrix(transposedF, galoisField.getFieldSize());

		} catch (FileNotFoundException ex) {
			System.out.println("the file does not exist");
		} catch (IOException ex) {
			System.out.println("cannot read/write from/to a file");
		}

		return originalF;
	}

	public static void splitMyOriginalFileIntoSlices(File encryptedFile, int max, int min) throws Exception {

	 
 		Matrix matrixF = createMatrixF(encryptedFile, min); // F Matrix
		Matrix aMatrix = createMatrixA(max, min);

 		Matrix cMatrix = matGf.multiply(aMatrix, matrixF);
		File[] fileShares = new File[cMatrix.getRows()]; // make all files
		try {
			for (int i = 0; i < cMatrix.getRows(); i++) {
				fileShares[i] = new File("_" + i + ".splt");
				PrintWriter out = new PrintWriter(encryptedFile.getAbsolutePath() + fileShares[i].getName());
 
				for (int j = 0; j < cMatrix.getColumns(); j++) {

					out.print(cMatrix.getElement(i, j) + " ");

				}
				for (int k = 0; k < aMatrix.getColumns(); k++) {

					out.print("?" + aMatrix.getElement(i, k));
				}
				out.flush();
				out.close();
	 
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
  
	}

	public static Matrix createMatrixA(int max, int min) {

		Matrix aMatrix = new Matrix(max, min, galoisField.getFieldSize());

		return aMatrix;
	}

//	public static void main(String... aArgs) throws Exception {
//		FileSplitter.splitMyOriginalFileIntoSlices(new File("D:/TEST/testen.txt"), 4, 2);
//
//	}

}
