import java.util.Arrays;
import java.util.Scanner;

public class SimpleDES{
	public static void main(String[] args) {
		
		boolean plainText[], key[], encAnswer[], decAnswer[];
		String plainTextInput, keyInput;
		Scanner scan = new Scanner(System.in);
		
		System.out.print("Enter the 12-bit plain text to be encrypted: ");
		plainTextInput = scan.nextLine();
		plainText = new boolean[plainTextInput.length()];
		plainText = createBooleanArray(plainTextInput);
		
		System.out.print("Enter the 9-bit key: ");
		keyInput = scan.nextLine();
		key = new boolean[keyInput.length()];
		key = createBooleanArray(keyInput);
		
		System.out.print("Enter the number of rounds: ");
		int rounds = scan.nextInt();
		
		encAnswer = new boolean[plainTextInput.length()];
		encAnswer = encrypt(plainText, key, rounds);
		System.out.println("Ciphertext = " + arrtoString(encAnswer));
		
		decAnswer = new boolean[plainTextInput.length()];
		decAnswer = decrypt(encAnswer, key, rounds);
		System.out.println("Plaintext = " + arrtoString(decAnswer));
		
		scan.close();
	}

	public static boolean[] encrypt(boolean plainText[], boolean key[], int numofRounds)
	{
		boolean cipherText[] = new boolean[plainText.length];
		boolean roundKey[] = new boolean[key.length-1];
		boolean leftSide[] = new boolean[plainText.length/2];
		boolean rightSide[] = new boolean[plainText.length/2];
		for(int i=0; i<numofRounds; i++){
			roundKey = roundKey(key, i);
			cipherText = simpleDES(plainText, roundKey);
			plainText = cipherText;
		}
		leftSide = Arrays.copyOfRange(cipherText, 0, cipherText.length/2);
		rightSide = Arrays.copyOfRange(cipherText, cipherText.length/2, cipherText.length);
		String lside = arrtoString(leftSide);
		String rside = arrtoString(rightSide);
		String result = rside + lside;
		cipherText = createBooleanArray(result);
		return cipherText;
	}
	
	public static boolean[] decrypt(boolean cipherText[], boolean key[], int numofRounds)
	{
		boolean plainText[] = new boolean[cipherText.length];
		boolean roundKey[] = new boolean[key.length-1];
		boolean leftSide[] = new boolean[cipherText.length/2];
		boolean rightSide[] = new boolean[cipherText.length/2];
		for(int i=numofRounds; i>0; i--){
			roundKey = roundKey(key, i-1);
			plainText = simpleDES(cipherText, roundKey);
			cipherText = plainText;
		}
		leftSide = Arrays.copyOfRange(plainText, 0, plainText.length/2);
		rightSide = Arrays.copyOfRange(plainText, plainText.length/2, plainText.length);
		String lside = arrtoString(leftSide);
		String rside = arrtoString(rightSide);
		String result = rside + lside;
		plainText = createBooleanArray(result);
		return plainText;
	}
	
	public static boolean[] simpleDES(boolean input[], boolean roundKey[])
	{
		boolean output[] = new boolean[input.length];
		boolean leftSide[] = new boolean[input.length/2];
		boolean rightSide[] = new boolean[input.length/2];
		boolean rndfncResult[] = new boolean[rightSide.length];
		boolean xorResult[] = new boolean[leftSide.length];
		leftSide = Arrays.copyOfRange(input, 0, input.length/2);
		rightSide = Arrays.copyOfRange(input, input.length/2, input.length);
		rndfncResult = roundFunction(rightSide, roundKey);
		xorResult = XOR(leftSide, rndfncResult);
		leftSide = rightSide;
		rightSide = xorResult;
		String lside = arrtoString(leftSide);
		String rside = arrtoString(rightSide);
		String result = lside + rside;
		output = createBooleanArray(result);
		return output;
	}
	
	public static boolean[] roundFunction(boolean oneHalf[], boolean roundKey[])
	{
		boolean output[] = new boolean[oneHalf.length];
		boolean expOutput[] = new boolean[oneHalf.length+2];
		boolean xorResult[] = new boolean[expOutput.length];
		boolean boxInput1[] = new boolean[xorResult.length/2];
		boolean boxInput2[] = new boolean[boxInput1.length];
		boolean boxOutput1[] = new boolean[boxInput2.length-1];
		boolean boxOutput2[] = new boolean[boxOutput1.length];
		expOutput = expander(oneHalf);
		xorResult = XOR(expOutput,roundKey);
		boxInput1 = Arrays.copyOfRange(xorResult, 0, xorResult.length/2);
		boxInput2 = Arrays.copyOfRange(xorResult, xorResult.length/2, xorResult.length);
		boxOutput1 = sbox1(boxInput1);
		boxOutput2 = sbox2(boxInput2);
		String lside = arrtoString(boxOutput1);
		String rside = arrtoString(boxOutput2);
		String result = lside + rside;
		output = createBooleanArray(result);
		return output;
	}
	
	public static boolean[] XOR(boolean left[], boolean right[])
	{
		boolean result[] = new boolean[left.length];
		for(int i=0; i<result.length; i++)
		{
			if(left[i] != right[i])
				result[i] = true;
			else
				result[i] = false;
		}
		return result;
	}
	
	public static boolean[] expander(boolean oneHalf[])
	{
		boolean output[] = new boolean[oneHalf.length+2];
		output[0] = oneHalf[0];
		output[1] = oneHalf[1];
		output[2] = oneHalf[3];
		output[3] = oneHalf[2];
		output[4] = oneHalf[3];
		output[5] = oneHalf[2];
		output[6] = oneHalf[4];
		output[7] = oneHalf[5];
		return output;
	}
	
	public static boolean[] sbox1(boolean oneHalf[])
	{
		boolean output[] = new boolean[oneHalf.length-1];
		boolean temp[] = new boolean[oneHalf.length-1];
		int row, col;
		String arr, boxValue;
		String sbox1[][] = {{"101", "010", "001", "110", "011", "100", "111", "000"},
							{"001", "100", "110", "010", "000", "111", "101", "011"}};
		if(oneHalf[0] == false)
			row = 0;
		else
			row = 1;
		for(int i=1; i<oneHalf.length; i++)
			temp[i-1] = oneHalf[i];
		arr = arrtoString(temp);
		col = Integer.parseInt(arr, 2);
		boxValue = sbox1[row][col];
		output = createBooleanArray(boxValue);
		return output;
	}
	
	public static boolean[] sbox2(boolean oneHalf[])
	{
		boolean output[] = new boolean[oneHalf.length-1];
		int row, col;
		String arr, boxValue;
		String sbox2[][] = {{"100", "000", "110", "101", "111", "001", "011", "010"},
							{"101", "011", "000", "111", "110", "010", "001", "100"}};
		if(oneHalf[0] == false)
			row = 0;
		else
			row = 1;
		for(int i=1; i<oneHalf.length; i++)
			output[i-1] = oneHalf[i];
		arr = arrtoString(output);
		col = Integer.parseInt(arr, 2);
		boxValue = sbox2[row][col];
		output = createBooleanArray(boxValue);
		return output;
	}
	
	public static boolean[] roundKey(boolean key[], int round)
	{
		boolean roundkey[] = new boolean[key.length-1];
		for(int i = 0; i<roundkey.length; i++)
			roundkey[i] = key[(i+round)%9];
		return roundkey;
	}
	
	public static boolean[] createBooleanArray(String text)
	{
		boolean tempArr[] = new boolean[text.length()];
		Arrays.fill(tempArr, false);
		for(int i=0; i<text.length(); i++)
		{
			if(text.charAt(i) == '1')
				tempArr[i] = true;
		}
		return tempArr;
	}
	
	public static String arrtoString(boolean arr[])
	{
		String arrString = "";
		for(int i=0; i<arr.length; i++)
		{
			if(arr[i] == true)
				arrString += "1";
			else
				arrString += "0";
		}
		return arrString;
	}
}