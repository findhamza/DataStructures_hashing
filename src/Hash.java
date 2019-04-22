import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Hash {

	private static int firstThirty[] = new int[3]; //min,max,avg
	private static int lastThirty[] = new int[3]; //min,max,avg
	private static int last=0;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		File file = new File("input.txt");
		Scanner sc = new Scanner(file);
		System.setOut(new PrintStream(new FileOutputStream("CustomRandom90.txt")));
		
		firstThirty[0] = Integer.MAX_VALUE;
		lastThirty[0] = Integer.MAX_VALUE;
		
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
//			hashAdrr = getHashAddress(ascii);
			hashAdrr = customHash(ascii);
//			hashTable = hashLinear(hashTable,hashAdrr,.9);
			hashTable = hashRandom(hashTable,hashAdrr,.9);
//			System.out.println(str[i]+" : "+hashAdrr+" : "+asciiString);
			i++;
		}
		
//		printFirstThirty(hashTable);
//		printLastThirty(hashTable);

		System.out.println("First Thirty Probes :: "+firstThirty[0]+", "+firstThirty[1]+", "+firstThirty[2]/30);
		System.out.println("Last Thirty Probes :: "+lastThirty[0]+", "+lastThirty[1]+", "+lastThirty[2]/30+"\n\n");
		
		int taken = 0;
		for(int x=0; x<hashTable.length; x++)
		{
			//System.out.println(hashTable[x][0]+", \t"+hashTable[x][1]+", \t"+hashTable[x][2]);
//			if(hashTable[x][2] > 30 && hashTable[x][1] != 0)
			if(hashTable[x][1] == 0 && hashTable[x][2] == 0)
			{
				System.out.format("<%4d Is Empty >\n",hashTable[x][0]);
				if(hashTable[(x+1)%hashTable.length][1] != 0)
					System.out.println();
			}
			else
			{
				System.out.format("[%4d, %4d, %2d]\n", hashTable[x][0], hashTable[x][1], hashTable[x][2]);
				if(hashTable[(x+1)%hashTable.length][1] == 0)
					System.out.println();
			}
			
			if(hashTable[x][1] != 0)
				taken++;
		}
		
//		System.out.println(taken+" :: "+Arrays.deepToString(hashTable));

		
	}

	private static int[][] hashRandom(int[][] hashTable, int hashAdrr, double d) 
	{
		Random getRnd = new Random(hashAdrr);
		List<Integer> randList = new ArrayList<Integer>();
		for(int i=0; i<hashTable.length; i++)
			if(i != hashAdrr)
				randList.add(i);
		
		Collections.shuffle(randList, getRnd);
		
		double cap = 0;
		for(int i=0; i<hashTable.length; i++)
			if(hashTable[i][1] != 0)
				cap++;
		
		int probe = 0;
		int newHash = hashAdrr;
		if((cap/hashTable.length)<d)
		{
			while(hashTable[newHash][1] != 0)
			{
				probe++;
				newHash = randList.remove(0);
//				System.out.println(newHash);
			}
			hashTable[newHash][1] = hashAdrr;
			hashTable[newHash][2] = probe;
		}
		
		if(cap<30)
		{
			//get min first thirty
			if(hashTable[newHash][2] < firstThirty[0])
				firstThirty[0] = hashTable[newHash][2];
			//get max first thirty
			if(hashTable[newHash][2] > firstThirty[1])
				firstThirty[1] = hashTable[newHash][2];
			//get avg first thirty
			firstThirty[2] += hashTable[newHash][2];
		}
		
		if(cap<(hashTable.length*d) && cap>(hashTable.length*d)-30)
		{
		//	System.out.println("Last" + last + " :: Probe"+probe+" "+hashTable.length); last++;
			//get min first thirty
			if(hashTable[newHash][2] < lastThirty[0])
				lastThirty[0] = hashTable[newHash][2];
			//get max first thirty
			if(hashTable[newHash][2] > lastThirty[1])
				lastThirty[1] = hashTable[newHash][2];
			//get avg first thirty
			lastThirty[2] += hashTable[newHash][2];
		}
		
//		System.out.println("====");
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
				
		if(cap<30)
		{
			//get min first thirty
			if(hashTable[(hashAdrr+probe)][2] < firstThirty[0])
				firstThirty[0] = hashTable[(hashAdrr+probe)][2];
			//get max first thirty
			if(hashTable[(hashAdrr+probe)][2] > firstThirty[1])
				firstThirty[1] = hashTable[(hashAdrr+probe)][2];
			//get avg first thirty
			firstThirty[2] += hashTable[(hashAdrr+probe)][2];
		}
		
		if(cap<(hashTable.length*d) && cap>(hashTable.length*d)-30)
		{
//			System.out.println("Last" + last + " :: Probe"+probe+" "+hashTable.length); last++;
			//get min first thirty
			if(hashTable[(hashAdrr+probe)%128][2] < lastThirty[0])
				lastThirty[0] = hashTable[(hashAdrr+probe)%128][2];
			//get max first thirty
			if(hashTable[(hashAdrr+probe)%128][2] > lastThirty[1])
				lastThirty[1] = hashTable[(hashAdrr+probe)%128][2];
			//get avg first thirty
			lastThirty[2] += hashTable[(hashAdrr+probe)%128][2];
		}
		
		return hashTable;
	}

	private static int customHash(byte[] ascii)
	{
		for(int i=0; i<ascii.length; i++)
		{
			ascii[i] = (byte) (ascii[i]+ascii[(i+2)%ascii.length]);
			ascii[i] = (byte) (ascii[i]^ascii[(i+2)%ascii.length]);
		}
		
//		System.out.println(Arrays.toString(ascii));
		
		int rngOne = addRng(ascii,4,9);
		int rngTwo = addRng(ascii,1,6);
		int hashVal = Math.abs(((rngOne-rngTwo)/256+ascii[2])/256+ascii[0]) % 128;
		return hashVal;
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
