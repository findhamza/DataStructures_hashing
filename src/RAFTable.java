import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class RAFTable {
	private final String rafFile = "table.dat";
	
	RAFTable()
	{
		
	}
	
	RAFTable(int size) throws IOException
	{
		for(int i=0; i<size; i++)
			writeraf(0,i);
		
	}

	public void writeraf(int i, int i2) throws IOException
	{
		writeraf(Integer.toString(i),i2);
	}

	public void writeraf(String string, int i2) throws IOException
	{
		RandomAccessFile file = new RandomAccessFile(rafFile,"rw");
		file.seek(i2);
		file.write(string.getBytes());
		file.close();
	}
	
	public String readraf(int pos, int size) throws IOException
	{
		RandomAccessFile file = new RandomAccessFile(rafFile,"r");
		file.seek(pos);
		byte[] bytes = new byte[size];
		file.read(bytes);
		file.close();
		return new String(bytes);
	}
}
