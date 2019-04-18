import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Hash {

	public static void main(String[] args) throws FileNotFoundException
	{
		File file = new File("input.txt");
		Scanner sc = new Scanner(file);
		System.setOut(new PrintStream(new FileOutputStream("output.txt")));
		
		int hashTable[][] = new int[128][3]; //key,init hashadr, probe count
		for(int i=0; i<hashTable.length; i++)
			hashTable[i][0] = i;
		
		String str[] = new String[999]; int i = 0;
		byte[] ascii;
		int hashAdrr;
		while(sc.hasNextLine())
		{
			str[i] = sc.nextLine();
			ascii = str[i].getBytes();
			String asciiString = Arrays.toString(ascii);
			hashAdrr = getHashAddress(ascii);
//			hashTable = hashLinear(hashTable,hashAdrr,.9);
			hashTable = hashRandom(hashTable,hashAdrr,.5);
//			System.out.println(str[i]+" : "+hashAdrr+" : "+asciiString);
			i++;
		}
		
//		printFirstThirty(hashTable);
//		printLastThirty(hashTable);
		
		int taken = 0;
		for(int x=0; x<hashTable.length; x++)
		{
			//System.out.println(hashTable[x][0]+", \t"+hashTable[x][1]+", \t"+hashTable[x][2]);
			if(hashTable[x][1] != 0)
				System.out.format("%4d, %4d, %2d\n", hashTable[x][0], hashTable[x][1], hashTable[x][2]);
			if(hashTable[x][1] != 0)
				taken++;
		}
		
		System.out.println(taken+" :: "+Arrays.deepToString(hashTable));

		
	}

	private static int[][] hashRandom(int[][] hashTable, int hashAdrr, double d) 
	{
		Random getRnd = new Random(123);
		
		double cap = 0;
		for(int i=0; i<hashTable.length; i++)
			if(hashTable[i][1] != 0)
				cap++;
		
		HashSet<Integer> used = new HashSet<Integer>();
		int probe = 0;
		int newHash = hashAdrr;
		if((cap/hashTable.length)<d)
		{
			while(hashTable[newHash][1] != 0)
			{
				probe++;
				newHash = getRnd.nextInt(hashTable.length);
				System.out.println(newHash);
			}
			hashTable[newHash][1] = hashAdrr;
			hashTable[newHash][2] = probe;
		}
		
		System.out.println("====");
		return hashTable;
	}

	@SuppressWarnings("unused")
	private static int[][] hashLinear(int[][] hashTable, int hashAdrr, double d) 
	{
		double cap = 0;
		for(int i=0; i<hashTable.length; i++)
			if(hashTable[i][1] != 0)
				cap++;
		
//		System.out.println(cap/128);
		
		int probe = 0;
		if((cap/hashTable.length)<d)
		{
			while(hashTable[(hashAdrr+probe)%128][1] != 0)
			{
				probe++;
			}
			hashTable[(hashAdrr+probe)%128][1] = hashAdrr;
			hashTable[(hashAdrr+probe)%128][2] = probe;
		}
				
		return hashTable;
	}

	private static int getHashAddress(byte[] ascii)
	{
		int rngOne = addRng(ascii,4,5);
		int rngTwo = addRng(ascii,1,2);
		int hashVal = Math.abs(((rngOne-rngTwo)/256+ascii[2])/256+ascii[0]) % 128;
		return hashVal;
	}

	private static int addRng(byte[] ascii, int start, int end)
	{
		start -= 1;
		end -= 1;
		int sum = 0;
		
		for(int i = start; i <= end; i++)
			sum += ascii[i];
		
		return sum;
	}

}