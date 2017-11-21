import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class RBSearchTest {
	
	@Before
	public void setUp() throws Exception {
		RBSearch.setupAllUni();
		RBSearch.setupCampusRB();
		RBSearch.setupUniCampus();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		int[] list1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		int[] list2 = {0, 0, 9, 3, 4, 0, 0, 0, 3, 5, 157, 31, 2};
		int[] list3 = {0, 0, 12, 3, 2, 0, 0, 0, 13, 10, 122, 45, 3};
		int[] list4 = {1, 0, 289, 68, 13, 4, 0, 0, 35, 59, 3376, 69, 3};
		
		// test whether red-black tree stores the total crimes for each university
		
		assertTrue(Arrays.equals(RBSearch.Getuni("Aaron's Academy of Beauty"), list1));
		assertTrue(Arrays.equals(RBSearch.Getuni("Abilene Christian University"), list2));
		assertTrue(Arrays.equals(RBSearch.Getuni("Youngstown State University"), list3));
		assertTrue(Arrays.equals(RBSearch.Getuni("Harvard University"), list4));
		
		ArrayList<String> campusList1 = new ArrayList<String>();
		campusList1.add("Cambridge Campus");
		campusList1.add("Southborough Campus");
		campusList1.add("Concord Field Station");
		campusList1.add("Longwood Campus");
		campusList1.add("Harvard Forest");
		campusList1.add("Arnold Arboretum");
		
		ArrayList<String> campusList2 = new ArrayList<String>();
		campusList2.add("Main Campus");
		campusList2.add("James Forrestal Campus");

		// check whether the campuses are correct for particular universities
		
		for (int i = 0; i < campusList1.size(); i++) {
			assertTrue(campusList1.get(i).equalsIgnoreCase(RBSearch.Campuses("Harvard University").get(i)));
		}
		
		for (int i = 0; i < campusList2.size(); i++) {
			assertTrue(campusList2.get(i).equalsIgnoreCase(RBSearch.Campuses("Princeton University").get(i)));
		}
		
		// check whether the crime rates are accurate for a given campus at a university
		
		int[] crimeList1 = {1, 0, 54, 10, 8, 0, 0, 0, 14, 44, 560, 42, 7};
		assertTrue(Arrays.equals(RBSearch.getUniCampus("Indiana State University", "Main Campus"), crimeList1));
		
		int[] crimeList2 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0};
		assertTrue(Arrays.equals(RBSearch.getUniCampus("Abilene Christian University", "ACU- City Square Dallas"), crimeList2));
	}

}

