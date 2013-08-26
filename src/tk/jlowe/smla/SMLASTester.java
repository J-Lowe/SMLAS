package tk.jlowe.smla;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * The testing environment for the SMLASimulator
 * @author Jonathan Lowe
 */
public class SMLASTester {

	/**
	 * Creates a command line style UI that prompts the user for a file that hold commands for the SMLAS, as well as offer debugging mode and output
	 */
	public static void main(String[] args) {
		JFileChooser fileChooser = new JFileChooser();
		SMLASimulator smlas;
		JOptionPane.showMessageDialog(null, "Welcome to my Simple Machice Language Assembler Simulation!\nTo start, open a file on the next screen that contains the\ncommands for the SMLAS to run.");
		int fileInt = fileChooser.showOpenDialog(null);
		switch(fileInt) {
			case JFileChooser.APPROVE_OPTION:
				int debug = JOptionPane.showOptionDialog(null, "Do you want to run the program in debugging mode?", "Debug?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, JOptionPane.NO_OPTION);
				if(debug == JOptionPane.YES_OPTION)
					smlas = new SMLASimulator(fileChooser.getSelectedFile(), true);
				else
					smlas = new SMLASimulator(fileChooser.getSelectedFile(), false);
				smlas.runSimulation();
				break;
			case JFileChooser.CANCEL_OPTION:
				break;
			default:
				break;
		}
	}

}
