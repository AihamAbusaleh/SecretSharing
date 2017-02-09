package algorithm.ida;

import java.util.Arrays;
import java.util.Random;

import org.ejml.simple.SimpleMatrix;

/**
 * 
 * @author AIN this class used to 1) casting arrays to another types 2) convert
 *         2 Dimensional Array to 1D, and reverse (1D to 2D)
 */
public class Converting {
	/**
	 * 
	 * @param source
	 *            the 2D array of bytes
	 * @return 2D of double values
	 */
	public static double[][] catingTo2dDouble(byte[][] source) {
		double[][] dest = new double[source.length][source[0].length];
		for (int row = 0; row < source.length; row++) {
			for (int col = 0; col < source[row].length; col++) {
				dest[row][col] = (double) source[row][col];

			}

		}
		return dest;
	}

	/**
	 * 
	 * @param source
	 *            array of int values
	 * @return array of byte values
	 */
	public static byte[] castingTo1dByte(int[] source) {
		byte[] dest = new byte[source.length];

		for (int row = 0; row < source.length; row++) {
			dest[row] = (byte) source[row];
		}
		return dest;
	}

	/**
	 * 
	 * @param source
	 *            is a SimpleMatrix with Values of doubles (SimpleMatrix is from
	 *            the "ejml" Library)
	 * @return 2D of int values
	 */
	public static int[][] castingTo2dInt(SimpleMatrix source) {
		int[][] dest = new int[source.numRows()][source.numCols()];

		for (int row = 0; row < source.numRows(); row++) {
			for (int col = 0; col < source.numCols(); col++) {
				if (source.get(row, col) < 0) {
					dest[row][col] = (int) (Math.floor(source.get(row, col)));

				} else
					dest[row][col] = (int) (Math.ceil(source.get(row, col)));
			}
		}
		return dest;
	}

	/**
	 * 
	 * @param source
	 *            2D of int values
	 * @return 2D of double values
	 */
	public static double[][] castingTo2dDoubleFrom2dInt(int[][] source) {
		double[][] dest = new double[source.length][source[0].length];
		for (int row = 0; row < source.length; row++) {
			for (int col = 0; col < source[row].length; col++) {
				dest[row][col] = (double) source[row][col];

			}
		//	System.out.println(Arrays.deepToString(dest));

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

/*	public static double[][] RandomArray(int max, int min) {
	    double[][] randomMatrix = new double [max][min];

	    Random rand = new Random(); 
	    rand.setSeed(System.currentTimeMillis()); 
	    for (int i = 0; i < max; i++) {     
	        for (int j = 0; j < min; j++) {
	            Integer r = rand.nextInt()% 10; 
	            randomMatrix[i][j] = Math.abs(r);
	        }
 	    }
	    

	    return randomMatrix;
	}
	*/
	
	
	public static int mulInGF256(int a, int b) {
		int p = 0;
		for (; b != 0; b >>= 1) {
			p ^= (a * (b & 1));
			a = ((a >> 7) == 1 ? ((a << 1) ^ 0x1B) : (a << 1)) & 0xFF;
		}
		return p;
	}

	public static int[][] vandermonde(int n, int m) {
		int[][] mulsInGF256 = new int[256][256];
		int[][] vandermonde = new int[n][m];

		for (int a = 0; a < 256; a++)
			for (int b = 0; b < 256; b++)
				mulsInGF256[a][b] = mulInGF256(a, b);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				vandermonde[i][j] = (j == 0 ? 1 : mulsInGF256[vandermonde[i][j - 1]][i]);
			}
		}
		return vandermonde;
	}
}
