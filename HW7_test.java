import java.io.*; 				//import java.io.* for File Input/Output
import java.util.Arrays;
import java.util.Scanner;			//import Scanner class for print out in the console


public class HW7_test {

	public static void main(String[] args) throws IOException {

		final int maxAccts = 100;									//the bank is small, only can hold 100 accounts
		boolean notDone = true;
		int[] acctNum = new int[maxAccts];								//create the array of account number information
		double[] balance = new double[maxAccts];							//create the array of account balance 
		char choice;											//selection character of menu
		int numAccts;											//how many accounts in the account array from the initAccts file
		int[] NewAcct= new int[maxAccts];										 
		double[] NewBalance = new double[maxAccts];

		//create an output file object using the PrintWriter class
		PrintWriter outputFile = new PrintWriter("outputFile.txt");

		//read account information from initial accounts file
		numAccts = readAccts(acctNum, balance);
		//System.out.println(numAccts);

		if (numAccts > 0 && numAccts <= 100){

			// print the account information array
			printAccts(acctNum,balance,numAccts,outputFile);

		}else {												//if the account has more than 100 in this bank, it will occur an error
			outputFile.println("Error! Accounts are over bank limit!");
		}

		//call menu method to produce the menu
		menu(outputFile);
		//open test input file
		File myFile = new File("testCases.txt");

		//create an output file object using the PrintWriter class
		Scanner inputFile = new Scanner(myFile);

		do {
			//input a character from testCases file(make a selection from ATM menu)
			choice=inputFile.next().charAt(0);

			switch(choice) {

			//input Q or q to quit
			case 'Q':
			case 'q':
				notDone=false;
				break;

			//call withdraw method
			case 'W':	
			case 'w':

				withdrawal(acctNum, balance, numAccts,inputFile, outputFile);
				outputFile.println();
				break;

			//call method of deposit
			case 'D':
			case 'd':
				deposit(acctNum, balance,numAccts,inputFile,outputFile);
				outputFile.println();
				break;

			//create new account 
			case 'N':
			case 'n':

				int NewAcctNum = newAcct(acctNum, balance,numAccts,inputFile, outputFile);		//add an account when call this method each time
				double Balance=inputFile.nextDouble();

				NewAcct= new int[numAccts+1];														 		 //same array size with old array
				NewBalance = new double[numAccts+1];

				//print out transaction information
				if(NewAcctNum==-1) {
					outputFile.println("Your transaction is done!");
				}else {
					for(int n=0;n<numAccts;n++) {
						NewAcct[n]=acctNum[n];
						NewBalance[n]= balance[n];
						NewAcct[numAccts] = NewAcctNum;
						NewBalance[numAccts] = Balance;
					}
					outputFile.println("New Account Number: " + NewAcctNum);
					//outputFile.println("New Account Number: " + NewAcctNum);
					outputFile.println("New Account Balance: " + Balance);
					//outputFile.println("You added a new account!");
					outputFile.println();
					numAccts++;		
					acctNum = NewAcct;
					balance = NewBalance;
				}

				//System.out.println(Arrays.toString(acctNum));
				//System.out.println(Arrays.toString(balance));
				outputFile.println();
				break;

			//create balance method to check account balance
			case 'B':
			case 'b':
				balance(acctNum, balance,numAccts,inputFile,outputFile);
				outputFile.println();
				break;

			//create method to delete account
			case 'X':
			case 'x':


				int i = deleteAcct(acctNum, balance, numAccts,inputFile, outputFile);

				if(i==-1) {
					outputFile.println("Your transaction is done!");
				}else {
					for(int n = i; n < numAccts-1; n++){
						acctNum[n] = acctNum[n+1];
						balance[n] = balance[n+1];
					}
					numAccts--;
					outputFile.println("You have deleted your account!");
				}

				
				System.out.println(Arrays.toString(acctNum));
				System.out.println(Arrays.toString(balance));
				System.out.println(numAccts);
				outputFile.println();
				break;

				//if enter a character not in the menu options
			default:
				outputFile.println("You put "+choice);
				outputFile.println("Error! Since this character is not in menu options!");				//other characters will show an error
				outputFile.println();
				break;	

			}

		}while(notDone);

		outputFile.println();
		outputFile.println();
		outputFile.println("Account Number     Balance");

		for (int count = 0; count < numAccts; count++){
			//System.out.printf("\t%d\t\t%s",acctNum[count],balance[count]);
			//System.out.println();
			outputFile.printf("   %d\t   $%s",acctNum[count],balance[count]);
			outputFile.println();
			outputFile.println();
		}

		outputFile.println();
		outputFile.println();
		outputFile.println("Thanks for using AMT, goodbye!");
		System.out.print("Project is done");

		//flush the outputFile buffer
		outputFile.flush();
		//close the inputFile
		inputFile.close();
		//close the outputFile
		outputFile.close();
	}


	/*Method deleteAcct() - delete an account in the bank system
	 * Input:
	 * acctNum - account number array
	 * balance - account balance array
	 * numAccts - old account total elements
	 * maxAccts - maximum account number is 100 for new array
	 * myout - output file, print the result on the file
	 * Process:
	 * create a new array
	 * check if the account has already in the system. If yes, print error message and return old account array
	 * check if the account still have balance in it, if yes, return the old account array
	 * otherwise, delete the account and return the new account into the system. 
	 * Output:
	 * print out the account balance information
	 */
	public static int deleteAcct(int[] acctNum, double[] balance, int numAccts, Scanner input, PrintWriter output) {

		output.println("Transaction: Delete Account");
		int i=findAcct(acctNum,numAccts,input);

		if(i==-1) {
			output.println("Error! Your account does not exist.");	
			return -1;
		}else if(i!=-1 && balance[i]>0){										//if the account does exist, but it has money in it, can't be deleted
			output.println("Error! Your account has money, can't delete.");
			return -1;
		}else {														//if the account exist and the balance is zero, delete account
			output.println("You want to delete account "+acctNum[i]);
			return i;
		}

	}	



	/*Method newAcct() - add new account in the bank system
	 * Input:
	 * acctNum - account number array
	 * balance - account balance array
	 * numAccts - old account total elements
	 * myout - output file, print the result on the file
	 * input - import new account number from testCases file
	 * Process:
	 * import new account number, check if the system has it already. If yes, print error message and return -1
	 * otherwise, return the new account number
	 * Output:
	 * return -1 or account number
	 */
	public static int newAcct(int[] acctNum, double[] balance,int numAccts, Scanner input, PrintWriter myout) {

		//import new account number
		int Account=input.nextInt();

		//print out transaction information
		myout.println("Transaction Type: Add New Account");	

		for(int i=0;i<numAccts;i++) {
			if(acctNum[i]==Account) {
				myout.println("Error! Your account has already existed.");
				return -1;
			}else{
				//return Account;
			}
		}
		return Account;
	}

	/*Method balance()
	 * Input:
	 * acctNum - account number array
	 * balance - account balance array
	 * i - index of the array for both account number array and balance array
	 * myout - output file, print the result on the file
	 * Process:
	 * if index i=-1, the account does not exist. Show error
	 * otherwise, print out the account number and account balance
	 * Output:
	 * print out the account balance information
	 */
	public static void balance(int[] acctNum, double[] balance, int numAccts, Scanner input, PrintWriter myout) {
		int i=findAcct(acctNum,numAccts,input);
		//print out transaction information
		myout.println("Transaction Type: Check Balance");
		if(i==-1) {																				//when index is -1, the account does not exist
			myout.println("Error! Your account does not exist.");
		}else {										
			myout.println("Account Number: " + acctNum[i]);								//print account balance information
			myout.println("Current Balance: $" + balance[i]);
		}
	}

	/*Method deposit()
	 * Input:
	 * acctNum - account number array
	 * balance - account balance array
	 * i - index of the array for both account number array and balance array
	 * myin - input file, scan testCases to get deposit number
	 * myout - output file, print the result on the file
	 * Process:
	 * if index i=-1, the account does not exist. Show error
	 * if withdraw amount is greater than account balance, show insufficient funds available
	 * if withdraw negative amount, show invalid withdraw numbers
	 * otherwise, calculate the balance number after withdraw
	 * Output:
	 * print out the withdraw transaction result
	 */
	public static void deposit(int[] acctNum, double[] balance, int numAccts, Scanner myin, PrintWriter myout){

		//declare local variables
		double deposit;
		double NewBalance;

		int i=findAcct(acctNum,numAccts,myin);

		//import much money you want to withdraw from testCases file
		deposit = myin.nextDouble();

		//print out transaction type
		myout.println("Transaction Type: Deposit");

		if(i==-1) {																				//when index is -1, the account does not exist
			myout.println("Error! Your account does not exist.");
		}else {										
			myout.println("Account Number: " + acctNum[i]);								//print account information
			myout.println("Current Balance: $" + balance[i]);
			myout.println("Amount to Deposit: $" + deposit);
			if(deposit>=0) {											//if withdraw amount is greater than the account balance, it will show error
				NewBalance = balance[i]+deposit;								//new account balance will be calculated after withdraw
				balance[i] = NewBalance;
				myout.printf("New Balance: $%.2f", NewBalance);
				myout.println();
			}else {
				myout.println("Invalid deposit numbers!");
			}
		}

	}

	/*Method withdrawal()
	 * Input:
	 * acctNum - account number array
	 * balance - account balance array
	 * i - index of the array for both account number array and balance array
	 * myin - input file, scan testCases to get deposit number
	 * myout - output file, print the result on the file
	 * Process:
	 * if index i=-1, the account does not exist. Show error
	 * if withdraw amount is greater than account balance, show insufficient funds available
	 * if withdraw negative amount, show invalid withdraw numbers
	 * otherwise, calculate the balance number after withdraw
	 * Output:
	 * print out the withdraw transaction result
	 */
	public static void withdrawal(int[] acctNum, double[] balance, int numAccts, Scanner myin, PrintWriter myout) {
		//find account
		int i=findAcct(acctNum,numAccts,myin);
		
		//declare local variables
		double withdraw;
		double NewBalance;

		//import much money you want to withdraw from testCases file
		withdraw = myin.nextDouble();

		//print out transaction type
		myout.println("Transaction Type: Withdrawal");

		if(i==-1) {																				//when index is -1, the account does not exist
			myout.println("Error! Your account does not exist.");
		}else if(i!=-1){										
			myout.println("Account Number: " + acctNum[i]);								//print account information
			myout.println("Current Balance: $" + balance[i]);
			myout.println("Amount to Withdraw: $" + withdraw);
			if(withdraw>=0 && withdraw<=balance[i]) {								//if withdraw amount is greater than the account balance, it will show error
				NewBalance = balance[i]-withdraw;								//new account balance will be calculated after withdraw
				balance[i] = NewBalance;
				myout.printf("New Balance: $%.2f", NewBalance);
				myout.println();
			}else if(withdraw>balance[i]){
				myout.println("Error: Insufficient Funds Available");
			}else if(withdraw<0){
				myout.println("Invalid withdraw numbers!");
			}
		}

	}

	/*Method findAcct()
	 * Input:
	 * acctNum - account number array
	 * numAccts - account size
	 * account - exact account number, read from test file
	 * input - input file, scan testCases to get account number
	 * Process:
	 * Find if there an account number match in account number array. If there is one, return index i, if there is not, return -1
	 * Output:
	 * return index of element, return -1 if there is no account exist
	 */
	public static int findAcct(int[] acctNum, int numAccts, Scanner input) {

		//read account number from input file
		int account=input.nextInt();

		int i=0;										//set first index is 0

		while(i<numAccts) {
			if(acctNum[i]==account) {							//if the i-th element is account, then return the index i
				return i;
			}else {
				i=i+1;
			}
		}
		return -1;										//if there is no account match in array, index return to -1
	}


	/*Method readAccts()
	 * Input:
	 * acctNum - bank account numbers
	 * balance - bank account balance
	 * Process:
	 * read account numbers and balance from initial account file
	 * Output:
	 * the number of elements read into the array
	 */
	public static int readAccts(int[] acctNum, double[] balance) throws IOException{

		//local variable declarations
		int x=0;										//count the array elements

		//input initial account file
		File initAccts = new File("initAccts.txt");

		//Create a Scanner object to read the input file
		Scanner inputAccts = new Scanner(initAccts);

		while(inputAccts.hasNext()) {
			acctNum[x]=inputAccts.nextInt();
			balance[x]=inputAccts.nextDouble();
			x++;
		}

		inputAccts.close();
		return x;
	}


	/* Method printAccts()
	 * Input:
	 *  acctNum - reference to account number information
	 *  balance - reference to account balance
	 *  numAccts - the number of elements in the array
	 *  myout -  print out account information to a file
	 * Process:
	 *  prints the array
	 * Output:
	 *  prints the table of 2 columns
	 */
	public static void printAccts(int[] acctNum, double[] balance, int numAccts, PrintWriter myout){

		//System.out.println("User Account Number      Balance");
		myout.println("Account Number     Balance");

		for (int count = 0; count < numAccts; count++){
			//System.out.printf("\t%d\t\t%s",acctNum[count],balance[count]);
			//System.out.println();
			myout.printf("   %d\t   $%s",acctNum[count],balance[count]);
			myout.println();
			myout.println();
		}
	}


	/* Method menu()
	 * Input:
	 * myout - the number of elements in the array
	 * Process:
	 * prints the ATM menu
	 * Output:
	 *  prints the menu of options
	 */
	public static void menu(PrintWriter myout) {

		myout.println("Select one of the following: ");
		myout.println("W - Withdrawal");
		myout.println("D - Deposit");
		myout.println("N - New account");
		myout.println("B - Balance");
		myout.println("Q – Quit");
		myout.println("X – Delete Account");
		myout.println();
	}

}
