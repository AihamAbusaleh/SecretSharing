/**Information Dispersal Algorythm (IDA)
 * 
 * this is a Java implementation for the IDA of Michael Rabin
 * read the README file for additional instruction 
 * 
 * 
 * @author Rotem Hungem
 */

package testIDA;
import java.io.File;


public class TestDrive {

	/**
	 * @param args
	 */
	public static void main(String ar[]){  
	    SplitFile sf= new SplitFile();
	    
	    //insert file name, total shares and minimum shares to recombine the file
	    sf.splitFile(new File("D:/mytest.txt"), 4,2);
	    
	//    RecombineFile rf= new RecombineFile();
	 //   rf.recombineFile(2);
	  }

}
