package tk.jlowe.smla;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Simulates a simple machine language assembler
 * @author Jonathan Lowe
 */
public class SMLASimulator {
	
	private Scanner fileReader;
	private boolean debugMode;
	private int accumulator;
	private int instructionPointer;
	private Scanner kboard;
	private int instructionRegister;
	private int[] memoryArray;
	
	/**
	 * Creates a new SMALS with a command list from a certain file.
	 * @param file the file containing the commands for the SMALS
	 * @param debug true for debug mode, false for regular mode
	 */
	public SMLASimulator(File file, boolean debug) {
		debugMode = debug;
		memoryArray = new int[100];
		instructionPointer = -1;
		instructionRegister = 0;
		accumulator = 0;
		kboard = new Scanner(System.in);
		try {
			fileReader = new Scanner(file);
			int commandNum;
			for(commandNum = 0; fileReader.hasNextInt(); commandNum++) {
				int command = fileReader.nextInt();
				memoryArray[commandNum] = command;
			}
			while(commandNum < 100) {
				memoryArray[commandNum] = 0;
				commandNum++;
			}
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	public void runSimulation() {
		boolean halt = false;
		while(!halt) {
			if(accumulator > 9999) 
				runErrorMessage(1);
			if(instructionPointer > 99)
				runErrorMessage(3);
			instructionPointer++;
			if(instructionPointer > 99) {
				runErrorMessage(3);
			}
			instructionRegister = memoryArray[instructionPointer];
			if(debugMode) {
				System.out.println("");
				System.out.println("Accumulator:   " + accumulator);
				System.out.println("Pointer:       " + instructionPointer);
				System.out.println("Register:      " + instructionRegister);
			}
			halt = runCommand();
			if(accumulator > 9999) {
				runErrorMessage(1);
			}
		}
		if(debugMode){
			System.out.print("Do you wish to see the memory dump [y/n]: ");
			kboard = new Scanner(System.in);
			String answer = kboard.nextLine();
			while(!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n")){
				System.out.println("Error: Uknown command");
				System.out.print("Do you wish to see the memory dump [y/n]: ");
				answer = kboard.nextLine();
			}
			if(answer.equalsIgnoreCase("y"))
				runMemoryDump();
		}
	}
	
	private boolean runCommand() {
		int command = instructionRegister / 100;
		int location = instructionRegister % 100;
		if(command < 0 || command > 99) {
			runErrorMessage(2);
			return true;
		}
		if(command != 43) {
			switch(command) {
				case 10:
					System.out.print("Enter number: ");
					int foo = kboard.nextInt();
					while(foo > 9999) {
						System.out.println("Overflow Error: The value entered is above 4 digits.");
						System.out.print("Enter number: ");
						foo = kboard.nextInt();
					}
					memoryArray[location] = foo;
					break;
				case 11:
					System.out.println(memoryArray[location]);
					break;
				case 20:
					accumulator = memoryArray[location];
					break;
				case 21:
					memoryArray[location] = accumulator;
					break;
				case 30:
					accumulator += memoryArray[location]; 
					break;
				case 31:
					accumulator -= memoryArray[location];
					break;
				case 32:
					int number = memoryArray[location];
					if(number == 0) {
						runErrorMessage(0);
					}
					accumulator /= number;
					break;
				case 33:
					accumulator *= memoryArray[location];
					break;
				case 40:
					instructionPointer = location - 1;
					break;
				case 41:
					if(accumulator < 0)
						instructionPointer = location -1 ;
					break;
				case 42:
					if(accumulator == 0)
						instructionPointer = location -1;
					break;
				default:
					System.out.println("Operation Error: " + command + " is not a valid operation code");
					System.exit(0);
					break;
			}
			return false;
		}
		else {
			return true;
		}
	}
	private void runErrorMessage(int errorCode) {
		switch(errorCode) {
			case 0:
				System.out.println("Operation Error: Cannot divide by 0");
				break;
			case 1:
				System.out.println("Overflow Error: Accumulator is greater than 4 digits");
				break;
			case 2:
				System.out.println("Operation Error: Index out of bounds");
				break;
			case 3:
				System.out.println("Overflow Error: Out of memory");
				break;
		}
		if(debugMode) {
			runMemoryDump();
		}
		System.exit(0);
	}
	
	private void runMemoryDump() {
		String memoryDump = "";
		int cells = 0;
		for(int ii = 0; ii < 100; ii ++) {
			if(memoryArray[ii] != 0) {
				String loc = "[" + ii;
				if(ii < 10)
					loc = "[0" + ii;
				String spaces = "";
				for(int less = 4 - Integer.toString(memoryArray[ii]).length(); less > 0; less--) {
					spaces += " ";
				}
				memoryDump += loc + ":" + spaces + memoryArray[ii] + "]";
				if((cells % 10) + 1 == 10)
					memoryDump += "\n";
				cells++;
			}
		}
		System.out.println("Memory Dump:");
		System.out.println(memoryDump.toString());
	}
}
