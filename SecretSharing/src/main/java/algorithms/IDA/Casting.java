package algorithms.IDA;

import java.util.Arrays;
import java.util.Random;

import lib.finiteFieldLibrary.Matrix;

/**
 * 
 * @author AIN
 */
public class Casting {

	public static long[][] castByteToLong(byte[][] source) {
		long[][] dest = new long[source.length][source[0].length];
		for (int row = 0; row < source.length; row++) {
			for (int col = 0; col < source[row].length; col++) {
				dest[row][col] = (long) (source[row][col] & 0xff);

			}

		}
		return dest;
	}

 

	public static long[][] castToLong(int[][] source) {
		long[][] dest = new long[source.length][source[0].length];
		for (int row = 0; row < source.length; row++) {
			for (int col = 0; col < source[row].length; col++) {
				dest[row][col] = (long) source[row][col];

			}

		}
		return dest;
	}
	 

	public static int[][] castFromMatrixToInt(Matrix source) {
		int[][] dest = new int[source.getRows()][source.getColumns()];
		for (int row = 0; row < source.getRows(); row++) {
			for (int col = 0; col < source.getColumns(); col++) {
				dest[row][col] = (int) source.getElement(row, col);

			}

		}
		return dest;
	}

	/**
	 * 
	 * @param array
	 *            2D array of int values
	 * @return 1D array of int values
	 */
	public static int[] convert2Dto1D(final int[][] array) {
		int rows = array.length, cols = array[0].length;
		int[] mono = new int[(rows * cols)];
		for (int i = 0; i < rows; i++)
			System.arraycopy(array[i], 0, mono, (i * cols), cols);
		return mono;
	}

	public static byte[] castToByte(int[] array){
		byte[] newArray = new byte[array.length];
		for (int i = 0; i < array.length; i++) {
			newArray[i] = (byte) array[i];
		}
		return newArray;
		
	}
	/**
	 * 
	 * @param array
	 *            1D array of int values
	 * @param rows
	 *            number of rows
	 * @param cols
	 *            number of cols
	 * @return 2D array of int values
	 */
	public static int[][] convert1Dto2D(final int[] array, final int rows, final int cols) {
		if (array.length != (rows * cols))
			throw new IllegalArgumentException("Invalid array length");

		int[][] bidi = new int[rows][cols];
		for (int i = 0; i < rows; i++) {
			System.arraycopy(array, (i * cols), bidi[i], 0, cols);
		}
		return bidi;
	}
	
	
	
}
