package testIDA;

 
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import algorithm.ida.Extension;


public class RecombineFile{
	
	
	public File recombineFile(int shares){
		File[] fileShares= new File[shares];
		int i,index, count;
		try{
			//make file extension filter
			File dir=new File(".");
			File[] filesInDir=dir.listFiles(new Extension());
			i=0;
			
			//gets files with extension 'splt'
			for (File f: filesInDir){
				fileShares[i]=new File(f.getAbsolutePath());
				i++;
			}
			
			index =fileShares[0].getName().lastIndexOf('_');
			File dest= new File(fileShares[0].getName().substring(0, index));
			
			FileOutputStream fos= new FileOutputStream(dest);
			IDA d = new IDA(4,2);
			for (File f: fileShares){
				byte[] buffer;
				FileInputStream fis= new FileInputStream(f);
				buffer = new byte[(int)f.length()];
				count=fis.read(buffer);
				byte[] b = d.decode(buffer);
				fos.write(b);
				fis.close();
			}
			
			fos.close();
			
		}
		catch (FileNotFoundException ex){
			System.out.println("the file does not exist");
		}
		catch (IOException ex){
			System.out.println("cannot read/write from/to a file");
		}
		return null;
		
	}

	
}
