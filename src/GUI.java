/*
 * Nariman Alimuradov - v1.6
 */

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.ST;
import javax.imageio.ImageIO;
import javax.swing.*;

public class GUI extends JFrame implements ActionListener {

	/*
	 * A lot of instance variables were needed for the user interface. Making
	 * some of them into arrays created very strange errors, so that is why
	 * there are some variables called text1, text2... instead of text[0],
	 * text[1]...
	 */

	/*
	 * importanceArray is the list of the crimes in order of importance (0th
	 * index is most important, 4th index is least important).
	 */
	private static String[] importanceArray;

	private int lowestRank;
	private String lowestUni;
	private String lowestCampus;

	// Simply declaring some labels and stuff here.
	private JTextArea output;
	private JTextField title;
	private JTextField uniLabel;
	private JTextField campusLabel;
	private JTextField crimeLabel;

	private JTextField[] numCrimes;

	private JButton button;
	private JButton abstractButton;
	private JButton sizeButton;

	/*
	 * The combo boxes below are just the dropdowns on the GUI. uniBox refers to
	 * the university dropdowns, campusBox for campus dropdowns, and crime for
	 * the crime dropdowns.
	 */
	private JComboBox uniBox1;
	private JComboBox uniBox2;
	private JComboBox uniBox3;
	private JComboBox uniBox4;
	private JComboBox uniBox5;

	private JComboBox campusBox1;
	private JComboBox campusBox2;
	private JComboBox campusBox3;
	private JComboBox campusBox4;
	private JComboBox campusBox5;

	private JComboBox crime1;
	private JComboBox crime2;
	private JComboBox crime3;
	private JComboBox crime4;
	private JComboBox crime5;

	/*
	 * The five 'safety rankings' of the universities the user chose. Again, for
	 * some reason putting these values into an array creating a crazy error, so
	 * they were made into separate int variables.
	 */
	private int safetyRank1;
	private int safetyRank2;
	private int safetyRank3;
	private int safetyRank4;
	private int safetyRank5;

	/*
	 * the 'Final' variables below are arrays that contain what the user
	 * entered. uniFinal contains the five universities in an array, and the
	 * same idea is repeated for campus and rank*.
	 * 
	 * *rank is the 'safety rank' of the respective university.
	 * 
	 * The ones that include SIZE in their name are for the dataset ordered by
	 * size, where we compare to uni's of similar size.
	 */
	private String[] uniFinal;
	private String[] campusFinal;
	private int[] rankFinal;
	private String[] uniFinal_SIZE;
	private String[] campusFinal_SIZE;
	private int[] rankFinal_SIZE;

	private static final int ROWS = 119081;
	private Comparable<Integer>[] institutionSize;
	private String[] inputLine;

	// ----------------------------------------------------------------------------------------------------------------
	// CONSTRUCTOR
	private GUI() throws IOException {

		// Here we create the layout of the user interface.
		super("SafeSpace");
		importanceArray = new String[5];
		setSize(1000, 700);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(null);
		setLocation(100, 100);
		// getContentPane().setBackground(Color.BLACK);

		JLabel label = new JLabel(new ImageIcon("data/castleResized.jpg"));
		label.setVisible(true);
		label.setSize(994, 671);
		label.setLocation(0, 0);

		// Local methods. Makes the program more modular and easier to test.
		createUniversityDropdowns();
		createButtons();
		createCrimeDropdowns();

		// Below is for the output textArea in the GUI
		createOutputBox();

		makeLabels();
		createCampusDropdowns();
		instantiateCrimeDropdowns();
		add(label);
		performHeapSort();

	}

	// ----------------------------------------------------------------------------------------------------------------
	// The actionPerformed method is run when any 'action' is performed (eg.
	// clicking the "Enter" button).

	@Override
	public void actionPerformed(ActionEvent e) {

		String name = e.getActionCommand();

		/*
		 * When the "Enter" button is pressed, perform the code below.
		 */
		if (name.equals("UniEnter")) {

			// Here we assign the values stored in the dropdown boxes into
			// arrays.

			importanceArray[0] = (String) crime1.getSelectedItem();
			importanceArray[1] = (String) crime2.getSelectedItem();
			importanceArray[2] = (String) crime3.getSelectedItem();
			importanceArray[3] = (String) crime4.getSelectedItem();
			importanceArray[4] = (String) crime5.getSelectedItem();

			calculateOutput();

			uniFinal = new String[5];
			rankFinal = new int[5];
			campusFinal = new String[5];

			rankFinal[0] = safetyRank1;
			rankFinal[1] = safetyRank2;
			rankFinal[2] = safetyRank3;
			rankFinal[3] = safetyRank4;
			rankFinal[4] = safetyRank5;

			uniFinal[0] = (String) uniBox1.getSelectedItem();
			uniFinal[1] = (String) uniBox2.getSelectedItem();
			uniFinal[2] = (String) uniBox3.getSelectedItem();
			uniFinal[3] = (String) uniBox4.getSelectedItem();
			uniFinal[4] = (String) uniBox5.getSelectedItem();

			campusFinal[0] = (String) campusBox1.getSelectedItem();
			campusFinal[1] = (String) campusBox2.getSelectedItem();
			campusFinal[2] = (String) campusBox3.getSelectedItem();
			campusFinal[3] = (String) campusBox4.getSelectedItem();
			campusFinal[4] = (String) campusBox5.getSelectedItem();

			lowestRank = rankFinal[0];
			lowestUni = uniFinal[0];
			lowestCampus = campusFinal[0];

			for (int i = 1; i < 5; i++) {
				if (lowestRank > rankFinal[i]) {

					lowestRank = rankFinal[i];
					lowestUni = uniFinal[i];
					lowestCampus = campusFinal[i];
				}
			}

			// Output the safest institution in the output box.
			output.setText(null);
			output.append("The safest institution is: " + lowestUni + " - "
					+ lowestCampus + " with a safety rank of " + lowestRank);
			output.append("\n");

			abstractButton.setVisible(true);

		}

		/*
		 * When the "Compare Similar Size" button is pressed, perform the code
		 * below.
		 */
		if (name.equals("sizeButton")) {

			
			int crimeOriginal = 0;
			int crime1 = 0;
			int crime2 = 0;
			int crime3 = 0;
			int crime4 = 0;
			
			sizeButton.setVisible(false);
			// output.setText(null);
			Scanner in = null;

			try {
				in = new Scanner(new File("data/CrimeDataSet_SizeSorted.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int counter = 0;
			in.nextLine();
			for (int i = 0; i < ROWS; i++) {
				String line = in.nextLine();
				String[] split = line.split(",");
				if (split[2].equals(lowestUni) && split[4].equals(lowestCampus)) {
					
					
					for (int j = 0; j < split.length; j++){
						if (split[j].equals("")){
							split[j] = "0";
						}
					}
					
					crimeOriginal = Integer.parseInt(split[6]) + Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + 
							Integer.parseInt(split[10]) + Integer.parseInt(split[11]) + Integer.parseInt(split[12]) + Integer.parseInt(split[13]) + 
							Integer.parseInt(split[14]) + Integer.parseInt(split[15]) + Integer.parseInt(split[16]) + Integer.parseInt(split[17]) + Integer.parseInt(split[18]);
					
					if (in.hasNextLine()){
						in.nextLine();
						crime1 = Integer.parseInt(split[6]) + Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + 
								Integer.parseInt(split[10]) + Integer.parseInt(split[11]) + Integer.parseInt(split[12]) + Integer.parseInt(split[13]) + 
								Integer.parseInt(split[14]) + Integer.parseInt(split[15]) + Integer.parseInt(split[16]) + Integer.parseInt(split[17]) + Integer.parseInt(split[18]);	
					}
					else {
						crime1 = 0;
					}
					
					if (in.hasNextLine()){
						in.nextLine();
						crime2 = Integer.parseInt(split[6]) + Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + 
								Integer.parseInt(split[10]) + Integer.parseInt(split[11]) + Integer.parseInt(split[12]) + Integer.parseInt(split[13]) + 
								Integer.parseInt(split[14]) + Integer.parseInt(split[15]) + Integer.parseInt(split[16]) + Integer.parseInt(split[17]) + Integer.parseInt(split[18]);	
					}
					else {
						crime2 = 0;
					}
					
					if (in.hasNextLine()){
						in.nextLine();
						crime3 = Integer.parseInt(split[6]) + Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + 
								Integer.parseInt(split[10]) + Integer.parseInt(split[11]) + Integer.parseInt(split[12]) + Integer.parseInt(split[13]) + 
								Integer.parseInt(split[14]) + Integer.parseInt(split[15]) + Integer.parseInt(split[16]) + Integer.parseInt(split[17]) + Integer.parseInt(split[18]);	
					}
					else {
						crime3 = 0;
					}
					
					if (in.hasNextLine()){
						in.nextLine();
						crime4 = Integer.parseInt(split[6]) + Integer.parseInt(split[7]) + Integer.parseInt(split[8]) + Integer.parseInt(split[9]) + 
								Integer.parseInt(split[10]) + Integer.parseInt(split[11]) + Integer.parseInt(split[12]) + Integer.parseInt(split[13]) + 
								Integer.parseInt(split[14]) + Integer.parseInt(split[15]) + Integer.parseInt(split[16]) + Integer.parseInt(split[17]) + Integer.parseInt(split[18]);	
					}
					else {
						crime4 = 0;
					}
					
					break;
				}
				counter++;
			}

			Scanner newIn = null;
			try {
				newIn = new Scanner(
						new File("data/CrimeDataSet_SizeSorted.txt"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			int[] crimeSum = new int[5];

			uniFinal_SIZE = new String[5];
			rankFinal_SIZE = new int[5];
			campusFinal_SIZE = new String[5];

			// -------------------------
			
			// Below we get the universities of similar size to the safest one for comparing.
			
	
			lowestRank = rankFinal_SIZE[0];
			lowestUni = uniFinal_SIZE[0];
			lowestCampus = campusFinal_SIZE[0];

			for (int i = 1; i < 5; i++) {
				if (lowestRank > rankFinal[i]) {

					lowestRank = rankFinal[i];
					lowestUni = uniFinal[i];
					lowestCampus = campusFinal[i];
				}
			}

			// -------------------------
		
			output.append("\n");
			
			int rank = 0;
			if (crimeOriginal > crime1) {
				rank += 1;
			}
			if (crimeOriginal > crime2) {
				rank += 1;
			}
			if (crimeOriginal > crime3) {
				rank += 1;
			}
			if (crimeOriginal > crime4) {
				rank += 1;
			}
			if (crimeOriginal == 0){ // If there are no crimes, it is very safe.
				rank = 0;
			}

			
			System.out.println(rank);
			if (rank == 0) {
				output.append("Your chosen university is Very Safe relative to other universities of similar size.");
			} else if (rank == 1) {
				output.append("Your chosen university is Safe relative to other universities of similar size.");
			} else if (rank == 2) {
				output.append("Your chosen university is Unsafe relative to other universities of similar size.");
			} else if (rank == 3) {
				output.append("Your chosen university is Very Unsafe relative to other universities of similar size.");
			}

			output.append("\n");

		}

		/*
		 * When the "Show More" button is pressed, perform the code below.
		 */
		if (name.equals("abstractButton")) {

			output.append("\n");
			for (int i = 0; i < 5; i++) {
				output.append(uniFinal[i] + " - " + campusFinal[i] + ": "
						+ rankFinal[i] + "\n");
			}
			abstractButton.setVisible(false);
			sizeButton.setVisible(true);

		}

		/*
		 * When the user selects a university in the dropdown, it will perform
		 * the code below. depending on which dropdown he selected.
		 */

		/*
		 * IMPORTANT: This was supposed to be what we used the graph algorithm
		 * for.
		 * 
		 * The initial idea was that we would create an adjacency list of sorts
		 * and have the campuses be adjacent to the university, but that creates
		 * a graph where every campus node is just connected to its university
		 * node. This would make the graph search algorithms pointless, and so I
		 * just implemented it as below. We tried implementing the graphing
		 * algorithm into this section but this implementation was easier to
		 * create and also ran on Mac's. (for some reason the graph one kept
		 * crashing on mac computers).
		 */
		if (name.equals("uniBox1")) {
			RBSearch search = new RBSearch();
			search.setupCampusRB();
			String uni = (String) uniBox1.getSelectedItem();
			int numCampuses = search.Campuses(uni).size();
			int[] adjacency = new int[10];

			// Graph G = new Graph(numCampuses + 1);
			// BreadthFirstSearch breadth = new BreadthFirstSearch(G, 0);

			// for (int j = 0; j < 10; j++){
			// for (int x : G.adj(j)){
			// G.addEdge(0, i);
			// }
			// }

			campusBox1.removeAllItems();
			for (String i : search.Campuses(uni)) {
				campusBox1.addItem(i);
			}

		} else if (name.equals("uniBox2")) {

			RBSearch search = new RBSearch();
			search.setupCampusRB();
			String uni = (String) uniBox2.getSelectedItem();
			int numCampuses = search.Campuses(uni).size();
			// Graph G = new Graph(numCampuses + 1);

			campusBox2.removeAllItems();
			for (String i : search.Campuses(uni)) {
				campusBox2.addItem(i);
			}
		} else if (name.equals("uniBox3")) {

			RBSearch search = new RBSearch();
			search.setupCampusRB();
			String uni = (String) uniBox3.getSelectedItem();
			int numCampuses = search.Campuses(uni).size();
			// Graph G = new Graph(numCampuses + 1);

			campusBox3.removeAllItems();
			for (String i : search.Campuses(uni)) {
				campusBox3.addItem(i);
			}
		} else if (name.equals("uniBox4")) {

			RBSearch search = new RBSearch();
			search.setupCampusRB();
			String uni = (String) uniBox4.getSelectedItem();
			int numCampuses = search.Campuses(uni).size();
			// Graph G = new Graph(numCampuses + 1);

			campusBox4.removeAllItems();
			for (String i : search.Campuses(uni)) {
				campusBox4.addItem(i);
			}
		} else if (name.equals("uniBox5")) {

			RBSearch search = new RBSearch();
			search.setupCampusRB();
			String uni = (String) uniBox5.getSelectedItem();

			// Graph G = new Graph(numCampuses + 1);

			campusBox5.removeAllItems();
			for (String i : search.Campuses(uni)) {
				campusBox5.addItem(i);
			}
		}

	}

	// ----------------------------------------------------------------------------------------------------------------
	private void createUniversityDropdowns() throws FileNotFoundException {

		// The university dropdowns are created here.
		// We had to use the default combo box model so that we can obtain
		// whatever is selected in the dropdown when the user selects an option.
		Scanner crimeFile = new Scanner(new File("data/CrimeDataSet.txt"));
		crimeFile.nextLine();

		DefaultComboBoxModel def1 = new DefaultComboBoxModel(new String[1]);
		DefaultComboBoxModel def2 = new DefaultComboBoxModel(new String[1]);
		DefaultComboBoxModel def3 = new DefaultComboBoxModel(new String[1]);
		DefaultComboBoxModel def4 = new DefaultComboBoxModel(new String[1]);
		DefaultComboBoxModel def5 = new DefaultComboBoxModel(new String[1]);

		uniBox1 = new JComboBox(def1);
		uniBox1.setVisible(true);
		uniBox1.setSize(300, 20);
		uniBox1.setLocation(30, 120 + 40 * 0);
		uniBox1.setActionCommand("uniBox1");
		uniBox1.addActionListener(this);

		uniBox2 = new JComboBox(def2);
		uniBox2.setVisible(true);
		uniBox2.setSize(300, 20);
		uniBox2.setLocation(30, 120 + 40 * 1);
		uniBox2.setActionCommand("uniBox2");
		uniBox2.addActionListener(this);

		uniBox3 = new JComboBox(def3);
		uniBox3.setVisible(true);
		uniBox3.setSize(300, 20);
		uniBox3.setLocation(30, 120 + 40 * 2);
		uniBox3.setActionCommand("uniBox3");
		uniBox3.addActionListener(this);

		uniBox4 = new JComboBox(def4);
		uniBox4.setVisible(true);
		uniBox4.setSize(300, 20);
		uniBox4.setLocation(30, 120 + 40 * 3);
		uniBox4.setActionCommand("uniBox4");
		uniBox4.addActionListener(this);

		uniBox5 = new JComboBox(def5);
		uniBox5.setVisible(true);
		uniBox5.setSize(300, 20);
		uniBox5.setLocation(30, 120 + 40 * 4);
		uniBox5.setActionCommand("uniBox5");
		uniBox5.addActionListener(this);

		// Here we go through every line in the data set and store them in
		// arrays and such for future use.

		for (int j = 1; j < 23801; j++) {

			String line = crimeFile.nextLine();
			String[] split = line.split(",");

			// split[2] will be the university name

			if (def1.getIndexOf(split[2]) == -1) {
				uniBox1.addItem(split[2]);
			}

			if (def2.getIndexOf(split[2]) == -1) {
				uniBox2.addItem(split[2]);
			}

			if (def3.getIndexOf(split[2]) == -1) {
				uniBox3.addItem(split[2]);
			}

			if (def4.getIndexOf(split[2]) == -1) {
				uniBox4.addItem(split[2]);
			}

			if (def5.getIndexOf(split[2]) == -1) {
				uniBox5.addItem(split[2]);
			}
		}

		add(uniBox1);
		add(uniBox2);
		add(uniBox3);
		add(uniBox4);
		add(uniBox5);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// This method creates all the labels on the GUI.
	private void makeLabels() {

		// Title Boxes and Other Stuff

		// AR CHRISTY
		Font titleFont = new Font("Californian FB", Font.CENTER_BASELINE, 40);

		title = new JTextField("SafeSpace");
		title.setEditable(false);
		title.setVisible(true);
		title.setLocation(430, 10);
		title.setSize(165, 45);
		title.setFont(titleFont);
		add(title);

		uniLabel = new JTextField("Universities");
		uniLabel.setEditable(false);
		uniLabel.setVisible(true);
		uniLabel.setLocation(135, 80);
		uniLabel.setSize(70, 25);
		add(uniLabel);

		campusLabel = new JTextField("Campuses");
		campusLabel.setEditable(false);
		campusLabel.setVisible(true);
		campusLabel.setLocation(475, 80);
		campusLabel.setSize(67, 25);
		add(campusLabel);

		crimeLabel = new JTextField("Crimes");
		crimeLabel.setEditable(false);
		crimeLabel.setVisible(true);
		crimeLabel.setLocation(850, 80);
		crimeLabel.setSize(45, 25);
		add(crimeLabel);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// The method below creates the dropdown lists for the crimes the user wants
	// to avoid.
	// (Displayed on the right hand side of the interface).

	private void createCrimeDropdowns() {
		crime1 = new JComboBox();
		crime1.addItem("Murder");
		crime1.addItem("Negligent Manslaughter");
		crime1.addItem("Forcible Sex Offences");
		crime1.addItem("Rape");
		crime1.addItem("Fondling");
		crime1.addItem("Non Forcible Sex Offences");
		crime1.addItem("Incest");
		crime1.addItem("Statutory Rape");
		crime1.addItem("Robbery");
		crime1.addItem("Aggravated Assault");
		crime1.addItem("Burglary");
		crime1.addItem("Motor Vehicle Theft");
		crime1.addItem("Arson");
		crime1.setVisible(true);
		crime1.setSize(200, 20);
		crime1.setLocation(770, 120);
		add(crime1);

		crime2 = new JComboBox();
		crime2.addItem("Murder");
		crime2.addItem("Negligent Manslaughter");
		crime2.addItem("Forcible Sex Offences");
		crime2.addItem("Rape");
		crime2.addItem("Fondling");
		crime2.addItem("Non Forcible Sex Offences");
		crime2.addItem("Incest");
		crime2.addItem("Statutory Rape");
		crime2.addItem("Robbery");
		crime2.addItem("Aggravated Assault");
		crime2.addItem("Burglary");
		crime2.addItem("Motor Vehicle Theft");
		crime2.addItem("Arson");
		crime2.setVisible(true);
		crime2.setSize(200, 20);
		crime2.setLocation(770, 160);
		add(crime2);

		crime3 = new JComboBox();
		crime3.addItem("Murder");
		crime3.addItem("Negligent Manslaughter");
		crime3.addItem("Forcible Sex Offences");
		crime3.addItem("Rape");
		crime3.addItem("Fondling");
		crime3.addItem("Non Forcible Sex Offences");
		crime3.addItem("Incest");
		crime3.addItem("Statutory Rape");
		crime3.addItem("Robbery");
		crime3.addItem("Aggravated Assault");
		crime3.addItem("Burglary");
		crime3.addItem("Motor Vehicle Theft");
		crime3.addItem("Arson");
		crime3.setVisible(true);
		crime3.setSize(200, 20);
		crime3.setLocation(770, 200);
		add(crime3);

		crime4 = new JComboBox();
		crime4.addItem("Murder");
		crime4.addItem("Negligent Manslaughter");
		crime4.addItem("Forcible Sex Offences");
		crime4.addItem("Rape");
		crime4.addItem("Fondling");
		crime4.addItem("Non Forcible Sex Offences");
		crime4.addItem("Incest");
		crime4.addItem("Statutory Rape");
		crime4.addItem("Robbery");
		crime4.addItem("Aggravated Assault");
		crime4.addItem("Burglary");
		crime4.addItem("Motor Vehicle Theft");
		crime4.addItem("Arson");
		crime4.setVisible(true);
		crime4.setSize(200, 20);
		crime4.setLocation(770, 240);
		add(crime4);

		crime5 = new JComboBox();
		crime5.addItem("Murder");
		crime5.addItem("Negligent Manslaughter");
		crime5.addItem("Forcible Sex Offences");
		crime5.addItem("Rape");
		crime5.addItem("Fondling");
		crime5.addItem("Non Forcible Sex Offences");
		crime5.addItem("Incest");
		crime5.addItem("Statutory Rape");
		crime5.addItem("Robbery");
		crime5.addItem("Aggravated Assault");
		crime5.addItem("Burglary");
		crime5.addItem("Motor Vehicle Theft");
		crime5.addItem("Arson");
		crime5.setVisible(true);
		crime5.setSize(200, 20);
		crime5.setLocation(770, 280);
		add(crime5);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Create the three buttons here to add to the GUI.
	private void createButtons() {
		button = new JButton("Enter");
		button.setSize(85, 25);
		button.setLocation(466, 340);
		button.setActionCommand("UniEnter");
		button.addActionListener(this);
		add(button);

		abstractButton = new JButton("Show All");
		abstractButton.setSize(85, 25);
		abstractButton.setLocation(880, 620);
		abstractButton.setActionCommand("abstractButton");
		abstractButton.setVisible(false);
		abstractButton.addActionListener(this);
		add(abstractButton);

		sizeButton = new JButton("Compare Similar Size");
		sizeButton.setSize(185, 25);
		sizeButton.setLocation(780, 620);
		sizeButton.setActionCommand("sizeButton");
		sizeButton.setVisible(false);
		sizeButton.addActionListener(this);
		add(sizeButton);
	}

	// ----------------------------------------------------------------------------------------------------------------
	// calculateOutput performs the 'Safety Ranking' calculations to determine
	// which campus is the safest.
	// It's a secret.

	private void calculateOutput() {

		RBSearch search = new RBSearch();
		search.setupUniCampus();
		int[] uni1 = search.getUniCampus((String) uniBox1.getSelectedItem(),
				(String) campusBox1.getSelectedItem());
		int[] uni2 = search.getUniCampus((String) uniBox2.getSelectedItem(),
				(String) campusBox2.getSelectedItem());
		int[] uni3 = search.getUniCampus((String) uniBox3.getSelectedItem(),
				(String) campusBox3.getSelectedItem());
		int[] uni4 = search.getUniCampus((String) uniBox4.getSelectedItem(),
				(String) campusBox4.getSelectedItem());
		int[] uni5 = search.getUniCampus((String) uniBox5.getSelectedItem(),
				(String) campusBox5.getSelectedItem());
		System.out.println("----------------------------------");

		safetyRank1 = 0;
		safetyRank2 = 0;
		safetyRank3 = 0;
		safetyRank4 = 0;
		safetyRank5 = 0;

		for (int i = 0; i < 5; i++) {

			switch (importanceArray[i]) {
			case "Murder":
				safetyRank1 += uni1[0] * (5 - i);
				safetyRank2 += uni2[0] * (5 - i);
				safetyRank3 += uni3[0] * (5 - i);
				safetyRank4 += uni4[0] * (5 - i);
				safetyRank5 += uni5[0] * (5 - i);
				break;

			case "Negligent Manslaughter":
				safetyRank1 += uni1[1] * (5 - i);
				safetyRank2 += uni2[1] * (5 - i);
				safetyRank3 += uni3[1] * (5 - i);
				safetyRank4 += uni4[1] * (5 - i);
				safetyRank5 += uni5[1] * (5 - i);
				break;

			case "Forcible Sex Offences":
				safetyRank1 += uni1[2] * (5 - i);
				safetyRank2 += uni2[2] * (5 - i);
				safetyRank3 += uni3[2] * (5 - i);
				safetyRank4 += uni4[2] * (5 - i);
				safetyRank5 += uni5[2] * (5 - i);
				break;

			case "Rape":
				safetyRank1 += uni1[3] * (5 - i);
				safetyRank2 += uni2[3] * (5 - i);
				safetyRank3 += uni3[3] * (5 - i);
				safetyRank4 += uni4[3] * (5 - i);
				safetyRank5 += uni5[3] * (5 - i);
				break;

			case "Fondling":
				safetyRank1 += uni1[4] * (5 - i);
				safetyRank2 += uni2[4] * (5 - i);
				safetyRank3 += uni3[4] * (5 - i);
				safetyRank4 += uni4[4] * (5 - i);
				safetyRank5 += uni5[4] * (5 - i);
				break;

			case "Non Forcible Sex Offences":
				safetyRank1 += uni1[5] * (5 - i);
				safetyRank2 += uni2[5] * (5 - i);
				safetyRank3 += uni3[5] * (5 - i);
				safetyRank4 += uni4[5] * (5 - i);
				safetyRank5 += uni5[5] * (5 - i);
				break;

			case "Incest":
				safetyRank1 += uni1[6] * (5 - i);
				safetyRank2 += uni2[6] * (5 - i);
				safetyRank3 += uni3[6] * (5 - i);
				safetyRank4 += uni4[6] * (5 - i);
				safetyRank5 += uni5[6] * (5 - i);
				break;

			case "Statutory Rape":
				safetyRank1 += uni1[7] * (5 - i);
				safetyRank2 += uni2[7] * (5 - i);
				safetyRank3 += uni3[7] * (5 - i);
				safetyRank4 += uni4[7] * (5 - i);
				safetyRank5 += uni5[7] * (5 - i);
				break;

			case "Robbery":
				safetyRank1 += uni1[8] * (5 - i);
				safetyRank2 += uni2[8] * (5 - i);
				safetyRank3 += uni3[8] * (5 - i);
				safetyRank4 += uni4[8] * (5 - i);
				safetyRank5 += uni5[8] * (5 - i);
				break;

			case "Aggravated Assault":
				safetyRank1 += uni1[9] * (5 - i);
				safetyRank2 += uni2[9] * (5 - i);
				safetyRank3 += uni3[9] * (5 - i);
				safetyRank4 += uni4[9] * (5 - i);
				safetyRank5 += uni5[9] * (5 - i);
				break;

			case "Burglary":
				safetyRank1 += uni1[10] * (5 - i);
				safetyRank2 += uni2[10] * (5 - i);
				safetyRank3 += uni3[10] * (5 - i);
				safetyRank4 += uni4[10] * (5 - i);
				safetyRank5 += uni5[10] * (5 - i);
				break;

			case "Motor Vehicle Theft":
				safetyRank1 += uni1[11] * (5 - i);
				safetyRank2 += uni2[11] * (5 - i);
				safetyRank3 += uni3[11] * (5 - i);
				safetyRank4 += uni4[11] * (5 - i);
				safetyRank5 += uni5[11] * (5 - i);
				break;

			case "Arson":
				safetyRank1 += uni1[12] * (5 - i);
				safetyRank2 += uni2[12] * (5 - i);
				safetyRank3 += uni3[12] * (5 - i);
				safetyRank4 += uni4[12] * (5 - i);
				safetyRank5 += uni5[12] * (5 - i);
				break;
			}
		}

		System.out.println("First University: " + safetyRank1);
		System.out.println("Second University: " + safetyRank2);
		System.out.println("Third University: " + safetyRank3);
		System.out.println("Fourth University: " + safetyRank4);
		System.out.println("Fifth University: " + safetyRank5);

	}

	// This method creates the big textArea that will show the user the results.
	private void createOutputBox() {
		Font outputFont = new Font("Thames", Font.CENTER_BASELINE, 18);
		output = new JTextArea();
		output.setVisible(true);
		output.setEditable(false);
		output.setLocation(30, 400);
		output.setSize(940, 250);
		output.setFont(outputFont);
		output.setMargin(new Insets(10, 10, 10, 10));
		add(output);
		output.append("Please enter 5 universities and their respective campuses in the lists above.");
		output.append("\n");
		output.append("\n");
		output.append("After that, select the 5 most important crimes of the options available (1 is most important, 5 is least important) and");
		output.append("\n");
		output.append("press Enter.");
		output.setBackground(Color.white);
	}

	// This simply adds the dropdowns to the GUI.
	// The other method for crime dropdowns adds crimes to the list.
	private void instantiateCrimeDropdowns() {
		numCrimes = new JTextField[5];

		for (int i = 0; i < 5; i++) {

			numCrimes[i] = new JTextField(Integer.toString(i + 1));
			numCrimes[i].setEditable(false);
			numCrimes[i].setVisible(true);
			numCrimes[i].setLocation(742, 120 + 40 * i);
			numCrimes[i].setSize(12, 20);
			add(numCrimes[i]);
		}
	}

	// ----------------------------------------------------------------------------------------------------------------
	// This method creates the dropdowns for the campuses on the GUI.
	// NOTE: it does not insert the campuses into it yet. That happens when they
	// select a university.
	private void createCampusDropdowns() {
		campusBox1 = new JComboBox();
		campusBox1.setVisible(true);
		campusBox1.setSize(300, 20);
		campusBox1.setLocation(360, 120);
		add(campusBox1);

		campusBox2 = new JComboBox();
		campusBox2.setVisible(true);
		campusBox2.setSize(300, 20);
		campusBox2.setLocation(360, 160);
		add(campusBox2);

		campusBox3 = new JComboBox();
		campusBox3.setVisible(true);
		campusBox3.setSize(300, 20);
		campusBox3.setLocation(360, 200);
		add(campusBox3);

		campusBox4 = new JComboBox();
		campusBox4.setVisible(true);
		campusBox4.setSize(300, 20);
		campusBox4.setLocation(360, 240);
		add(campusBox4);

		campusBox5 = new JComboBox();
		campusBox5.setVisible(true);
		campusBox5.setSize(300, 20);
		campusBox5.setLocation(360, 280);
		add(campusBox5);
	}

	// This method performs the sort that orders the dataset by institution
	// size.
	private void performHeapSort() throws IOException {
		// HeapSort for sizes is performed here.

		Scanner data = new Scanner(new File("data/CrimeDataSet.txt"));
		data.nextLine();
		institutionSize = new Comparable[ROWS];
		inputLine = new String[ROWS];

		FileWriter sizeFile = new FileWriter(new File(
				"data/CrimeDataSet_SizeSorted.txt"));

		for (int i = 0; i < ROWS; i++) {

			String line = data.nextLine();
			String[] split = line.split(",");
			if (split[5].equals("")) {
				split[5] = "0";
			}
			inputLine[i] = line;
			institutionSize[i] = Integer.parseInt(split[5]);
		}

		Heap.sort(institutionSize, inputLine);

		String[] newInputLine = Heap.getInputLine();
		for (int i = 0; i < ROWS; i++) {
			sizeFile.write(newInputLine[i] + "\n");
		}
		sizeFile.close();
	}

	// ----------------------------------------------------------------------------------------------------------------
	// Main method simply used to display GUI
	// Everything else runs in the constructor.
	public static void main(String[] args) throws IOException {
		new GUI().setVisible(true);
	}

}