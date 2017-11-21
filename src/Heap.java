/*
 * Nariman Alimuradov - v1.1
 * 
 * Heap Sort - Obtained from Algorithms 4th edition, by Sedgewick and Wayne.
 * 
 * Used in our program to sort the dataset by institution Size, which is then outputted to "data/CrimeDataSet_SizeSorted.txt".
 * 
 */
import edu.princeton.cs.algs4.*;

public class Heap {

	private static Comparable<String>[] inputLine;

    private Heap() { }

    /*
     * The below three methods are the usual heap sort methods.
     */
    public static void sort(Comparable[] pq, Comparable[] line) {
    	inputLine = line;
        int n = pq.length;
        for (int k = n/2; k >= 1; k--)
            sink(pq, k, n);
        while (n > 1) {
            exch(pq, 1, n--);
            sink(pq, 1, n);
        }
    }

    private static void sink(Comparable[] pq, int k, int n) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && less(pq, j, j+1)) j++;
            if (!less(pq, k, j)) break;
            exch(pq, k, j);
            k = j;
        }
    }

    private static boolean less(Comparable[] pq, int i, int j) {
        return pq[i-1].compareTo(pq[j-1]) < 0;
    }

    
    // Here we swap the values as usual, but I also made sure to swap the "inputLine", which is 
    // the line in the dataset file. When the sort is complete, the data set will be re-arranged.
    private static void exch(Object[] pq, int i, int j) {
        Object swap = pq[i-1];
        pq[i-1] = pq[j-1];
        pq[j-1] = swap;
        
        Object swapLine = inputLine[i-1];
        inputLine[i-1] = inputLine[j-1];
        inputLine[j-1] = (Comparable<String>) swapLine;
    }
    
    // A method that returns all the lines in the sorted dataset.
    public static String[] getInputLine(){
    	return (String[]) inputLine;
    }

}

