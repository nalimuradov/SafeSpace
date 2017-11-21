
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.RedBlackBST;

/**
 * @author Prince
 * @version 1.3; 
 * safespace; 
 *         
 */
public class RBSearch {
	// file info
	private static String file = "campusoffencses.csv";
	private static int keyrow = 2; // row info the user will search by
	private static int valuerow = 6; // value to be outputed starting from the row
	private static int rowsafter = 13; // from row 6 to 8 which are all the crime rates
	private static int campusrow = 4;
	private static RedBlackBST<String, int[]> dt;
	private static RedBlackBST<String, int[]> ut;
	private static RedBlackBST<String, ArrayList<String>> ct;
	
	//redblack for campuses in uni
	public static void setupCampusRB(){
		try {
			Scanner datafile = new Scanner(new File("data/" + file));
			ct = new RedBlackBST<String, ArrayList<String>>();
			// extracting every rowinfo line by line
			int skipfirstline = 0;
			while (datafile.hasNextLine()) {
				String line = datafile.nextLine();
				if (skipfirstline >= 1) { // skipping firstline

					String[] splitline = line.split(",");
					String key = splitline[keyrow];
					ArrayList<String> campus = new ArrayList<String>();
					campus.add(splitline[campusrow]);
					//if same uni comes up again in the data set add the crime rates
					if (ct.contains(key)){
						for (String s : ct.get(key)) {
							campus.add(s);
						};
						}
					
					ct.put(key, campus);
				}
				skipfirstline++;
			}
		
		} catch (IOException e) {
			System.out.println("file does not exist");
			
		}
		
		
	}
	
	//red and blackk for uni calls
	public static void setupAllUni(){
		try {
			Scanner datafile = new Scanner(new File("data/" + file));
			ut = new RedBlackBST<String, int[]>();
			// extracting every rowinfo line by line
			int skipfirstline = 0;
			while (datafile.hasNextLine()) {
				String line = datafile.nextLine();
				if (skipfirstline >= 1) { // skipping firstline

					String[] splitline = line.split(",");
					String key = splitline[keyrow] ;
					int val[] = new int[rowsafter];
					//putting crime rates in array
					for (int i = 0; i < rowsafter; i++) {
						String temp = splitline[valuerow + i];
						//if row is empty set it to 0
						if (temp.equals("")) {
							temp = "0";
						}
						val[i] = Integer.parseInt(temp);
						
					}
					//if same uni comes up again in the data set add the crime rates
					if (ut.contains(key)){
						for (int i = 0; i < val.length; i++) {
							val[i] = ut.get(key)[i] + val[i];
						}
					}
					ut.put(key, val);
				}
				skipfirstline++;
			}
		
		} catch (IOException e) {
			System.out.println("file does not exist");
			
		}
		
	}
	//red and black for uni and campus call
	public static void setupUniCampus(){
		try {
			Scanner datafile = new Scanner(new File("data/" + file));
			dt = new RedBlackBST<String, int[]>();
			// extracting every rowinfo line by line
			int skipfirstline = 0;
			while (datafile.hasNextLine()) {
				String line = datafile.nextLine();
				if (skipfirstline >= 1) { // skipping firstline

					String[] splitline = line.split(",");
					String key = splitline[keyrow] + splitline[campusrow];
					int val[] = new int[rowsafter];
					//putting crime rates in array
					for (int i = 0; i < rowsafter; i++) {
						String temp = splitline[valuerow + i];
						//if row is empty set it to 0
						if (temp.equals("")) {
							temp = "0";
						}
						val[i] = Integer.parseInt(temp);
						
					}
					//if same uni comes up again in the data set add the crime rates
					if (dt.contains(key)){
						for (int i = 0; i < val.length; i++) {
							val[i] = dt.get(key)[i] + val[i];
						}
					}
					dt.put(key, val);
				}
				skipfirstline++;
			}
		
		} catch (IOException e) {
			System.out.println("file does not exist");
			
		}
		
	}
	//get campus given uni using hash set to filter the repeated values in dataset
	public static ArrayList<String> Campuses(String uni){
		ArrayList<String> camp = ct.get(uni);
		if (ct.contains(uni)) {
			HashSet<String> temp = new HashSet<String>();
			temp.addAll(camp);
			camp.clear();
			camp.addAll(temp);
			//System.out.println(camp.toString());
			return camp;
		}

		System.out.println("uni not in file");
		return null;
	}
	
	//get crime data given campus and uni
	public static int[] getUniCampus(String uni, String campus) {
		int[] rates = dt.get(uni+campus);
		if (dt.contains(uni+campus)) {

			System.out.println(Arrays.toString(rates));
			return rates;
		}

		System.out.println("uni not in file");
		return null;
	}
	
	//get crime data of overall uni
	public static int[] Getuni(String uni) {
		int[] rates = ut.get(uni);
		if (ut.contains(uni)) {

			System.out.println(Arrays.toString(rates));
			return rates;
		}

		System.out.println("uni not in file");
		return null;
	}

	// kvr - key,value,row
	private static void changekvr(int ckeyrow, int valrow, int rowafter) {
		keyrow = ckeyrow;
		valrow = valuerow;
		rowsafter = rowafter;
	}

	
	// Unit test the methods.
	public static void main(String[] args) {
		setupUniCampus();
		 getUniCampus("Yeshiva University","RESNICK CAMPUS");
		 setupCampusRB();
		 Campuses("Yeshiva University");
		 setupAllUni();
		 Getuni("Yeshiva University");

	}

}
