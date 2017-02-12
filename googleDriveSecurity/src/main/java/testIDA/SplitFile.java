package testIDA;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.ejml.simple.SimpleMatrix;

import classes.GF2N;
import classes.Matrix;
import classes.MatrixGF2N;

public class SplitFile {

	public File[] splitFile(File source, int totalShares, int minShares) {
		long irreduciblePolynomial = 265;
		GF2N galoisField = new GF2N(irreduciblePolynomial);
		MatrixGF2N matGf = new MatrixGF2N(galoisField);
		Matrix m = new Matrix(6, 3, galoisField.getFieldSize());
		Matrix k = new Matrix(3, 3);
	 
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				k.setElement(i, j, m.getElement(i, j));			}
		}
		
		
		
		System.out.println(m);
		System.out.println(k);
	 
		 Matrix inver = matGf.inverse(k);
		 System.out.println(inver.getElement(0, 1));
		 System.out.println(inver.getElement(1, 1));
		
		 System.out.println(inver.getElement(2, 1));
		
		 System.out.println(inver);

		File[] fileShares = new File[totalShares];
		String fName;
		int shareSize, lastShare, count;
		long fSize;
		fName = source.getName();
		fSize = source.length();
		// set share size
		if (fSize % totalShares == 0)
			shareSize = lastShare = (int) fSize / (totalShares);
		else {
			// shares-1 gets equal size
			shareSize = (int) fSize / (totalShares - 1);
			// last share is
			lastShare = (int) fSize - shareSize * (totalShares - 1);
		}
		count = 0;
		try {
			FileInputStream fis = new FileInputStream(source);
			IDA file = new IDA(totalShares, minShares);
			for (int i = 0; i < fileShares.length; i++) {
				byte[] buffer;
				byte[] data;
				fileShares[i] = new File(fName + "_" + i + ".splt");
				FileOutputStream fos = new FileOutputStream(fileShares[i]);
				if (i == fileShares.length - 1) {
					buffer = new byte[lastShare];
					count = fis.read(buffer);
					data = new byte[lastShare];
				} else {
					buffer = new byte[shareSize];
					count = fis.read(buffer);
					data = new byte[lastShare];
				}
				System.out.println("Original " + Arrays.toString(buffer));
				data = file.encode(buffer);
				fos.write(data);
				fos.close();

			}
		} catch (FileNotFoundException ex) {
			System.out.println("the file does not exist");
		} catch (IOException ex) {
			System.out.println("cannot read/write from/to a file");
		}

		return fileShares;
	}
}
