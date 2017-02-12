package testIDA;

import java.util.Arrays;

public class IDA {

	
	public static void main(String[] args){
		IDA d =  new IDA(4,2);
		byte[] encodedSeq = new byte[]{2,99,107,99,-48,99,-71,99};
		
		 
		d.decode(encodedSeq);
		
	}
	private static final int FIELD_SIZE = 256; // field size

	private int n, m;

	private int[][] encodedMatrix;
	private final int[][] mulsInGF256 = new int[FIELD_SIZE][FIELD_SIZE];
	private int[][] vandermonde;
	private int[][] inverseVandermonde;
	private int[] inverse = new int[FIELD_SIZE];

	public IDA(int n, int m) {
		this.n = n;
		this.m = m;

		// Compute multiplication table
		for (int a = 0; a < 256; a++)
			for (int b = 0; b < 256; b++)
				mulsInGF256[a][b] = mulInGF256(a, b);

		// creates the vandermonde table
		vandermonde = new int[n][m];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < m; j++)
				vandermonde[i][j] = (j == 0 ? 1 : mulsInGF256[vandermonde[i][j - 1]][i]);

		// inverse the vandermonde table
		inverseVandermonde = new int[m][m];

		for (int a = 1; a < 256; a++)
			if (inverse[a] == 0)
				for (int b = 1; b < 256; b++)
					if (mulsInGF256[a][b] == 1) {
						inverse[a] = b;
						inverse[b] = a;
					}
	
	}

	/**
	 * encodes the String with vandermonde matrix
	 * 
	 * @param sequence
	 * @return
	 */
	public byte[] encode(byte[] sequence) {
		byte[] encodedData;
		int[] integerSeq;

		// convert to integer under GF(256)
		if (sequence.length % m != 0) {
			integerSeq = new int[sequence.length + m - sequence.length % m];
			for (int x = 0; x < integerSeq.length; x++) {
				// if (x==0)
				// integerSeq[x]=sequence.length;
				// else if(x<sequence.length+1 && x>0)
				// integerSeq[x]=sequence[x-1];
				if (x < sequence.length)
					integerSeq[x] = (int) (sequence[x] + 128);
				else
					integerSeq[x] = 0;
			}

		} else {
			integerSeq = new int[sequence.length + m];
			for (int x = 0; x < integerSeq.length; x++) {
				if (x == 0)
					integerSeq[x] = sequence.length;
				else if (x < sequence.length + 1 && x > 0)
					integerSeq[x] = sequence[x - 1];
				else
					integerSeq[x] = 0;
			}
		}

		encodedMatrix = new int[n][integerSeq.length / m];

		int index = 0;

		for (int k = 0; k < n; k++) {
			for (int i = 0; i < integerSeq.length / m; i++) {
				for (int j = 0; j < m; j++) {
					encodedMatrix[k][i] = addInGF256(encodedMatrix[k][i],
							mulsInGF256[vandermonde[k][j]][integerSeq[index]]);
					index++;
				}
			}
			index = 0;
		}
		System.out.println("ENC MAtrix " + Arrays.deepToString(encodedMatrix));
 
		encodedData = new byte[encodedMatrix.length * encodedMatrix[0].length];

		// set to data buffer
		for (int x = 0; x < encodedMatrix.length; x++)
			for (int y = 0; y < encodedMatrix[0].length; y++)
				encodedData[x * encodedMatrix[0].length + y] = (byte) encodedMatrix[x][y];
			System.out.println("ENC " + Arrays.toString(encodedData));
		return encodedData;
	}

	/**
	 * 
	 * @param encodedMatrix
	 * @param labels
	 * @return
	 */
	
	
	public byte[] decode(byte[] encodedSeq) {
	
		int[][] encodedMAtrix = new int[][]{{2,99},{107,99},{208,99},{185,99}};
		int[] s = new int[]{2,2};
		
		int[] decodedSeq = new int[2];
		byte[] originalSeq;
	 	int[][] encodedShares = new int[m][3];
		int index = 0;

		// for (int i=0; i<m; i++)
		// for (int j=0; j<encodedMatrix[0].length; j++)
		// encodedShares[i][j]=encodedMatrix[labels[i]-1][j];

	 	inverseMatrix(s);

	 	for (int i=0; i<encodedMatrix[0].length; i++)
			for (int j=0; j<m;j++){
				for (int k=0; k<m; k++)
					decodedSeq[index]=addInGF256(decodedSeq[index],
							mulsInGF256[encodedShares[k][i]][inverseVandermonde[j][k]]);
				index++;
			}
		
	//	System.out.println(Arrays.toString(decodedSeq));
		System.out.println(decodedSeq[1]);
		System.out.println("inverse vander " + Arrays.deepToString(inverseVandermonde));
	//	System.out.println("mus " + Arrays.deepToString(mulsInGF256));

 		originalSeq = new byte[2];

		for (int x = 0; x < 2; x++)
			originalSeq[x] = (byte) (decodedSeq[x + 1] - 128);
System.out.println("original " + Arrays.toString(originalSeq));
		return originalSeq;
	}

	// Compute matrix multiplicative inverse
	public void inverseMatrix(int[] labels) {
		int[][] tmpMatrix = new int[m][m];
		System.out.println(labels[1]);
		// clone matrix
		for (int i = 0; i < m; i++)
			for (int j = 0; j < m; j++)
				tmpMatrix[i][j] = vandermonde[labels[i] - 1][j];

		// initialize inverse matrix as unit matrix
		for (int i = 0; i < m; i++)
			inverseVandermonde[i][i] = 1;

		// simultaneously compute Gaussian reduction of tmpMatrix and unit
		// matrix
		for (int i = 0; i < m; i++) {

			// normalize i-th row
			int coef = tmpMatrix[i][i];
			int invCoef = inverse[coef];
			multRowWithElementThis(tmpMatrix[i], invCoef);
			multRowWithElementThis(inverseVandermonde[i], invCoef);

			// normalize all other rows
			for (int j = 0; j < tmpMatrix.length; j++)
				if (j != i) {
					coef = tmpMatrix[j][i];
					if (coef != 0) {
						int[] tmpRow = multRowWithElement(tmpMatrix[i], coef);
						int[] tmpInvRow = multRowWithElement(inverseVandermonde[i], coef);
						addToRow(tmpRow, tmpMatrix[j]);
						addToRow(tmpInvRow, inverseVandermonde[j]);
					}

				}
		}
	}

	// addition in GF(2^8)
	public int addInGF256(int a, int b) {
		return a ^ b;
	}

	// Multiplication in GF(2^8)
	public int mulInGF256(int a, int b) {
		int p = 0;
		for (; b != 0; b >>= 1) {
			p ^= (a * (b & 1));
			a = ((a >> 7) == 1 ? ((a << 1) ^ 0x1B) : (a << 1)) & 0xFF;
		}
		return p;
	}

	private void multRowWithElementThis(int[] row, int element) {
		for (int i = 0; i < row.length; i++)
			row[i] = mulsInGF256[row[i]][element];

	}

	private int[] multRowWithElement(int[] row, int element) {
		int[] result = new int[row.length];
		for (int i = 0; i < row.length; i++)
			result[i] = mulsInGF256[row[i]][element];
		return result;
	}

	private void addToRow(int[] fromRow, int[] toRow) {
		for (int i = 0; i < toRow.length; i++)
			toRow[i] = addInGF256(fromRow[i], toRow[i]);

	}

	public void printInverse(int element) {

		System.out.print(inverse[element]);
	}

}
