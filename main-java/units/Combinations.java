package units;

/**
 * @author ZoftWhere
 * @version 27 July 2007
 */

public class Combinations {
	

	/**
	 * @param Args
	 */
	public static void main (String [] Args) {
		System.out.println (nCr (11, 9));
		System.out.println (nPr (10, 8));
	}
	
	
	/**
	 * @param n Number of slots to move in.
	 * @param r Number of elements to move around.
	 * @return Number of combinations nCr. -1 invalid input, -2 output too large
	 */
	public static long nCr (long n, long r) {
		double answer = -1;
		
		if ((n > 0) && (r <= n)) {
			answer = 1;
			
			if ((n - r) > r) {
				if (r > 0) {
					for (long i = n - r + 1; i <= n; i++ ) {
						answer = answer * i;
						answer = answer / (i - n + r);
					}
					if (answer > 9223372036854775807L)
						answer = -2;
				}
			}
			else {
				if (n > r) {
					for (long i = r + 1; i <= n; i++ ) {
						answer = answer * i;
						answer = answer / (i - r);
					}
					if (answer > 9223372036854775807L)
						answer = -2;
				}
			}
		}
		
		return (long) answer;
	}
	
	
	/**
	 * @param n Number of elements to fit.
	 * @param r Number of slots to exchange them in.
	 * @return Number of permutations nPr.
	 */
	public static double nPr (long n, long r) {
		double answer = 1;
		
		if (r < n)
			for (long i = (n - r + 1); i <= n; i++ )
				answer *= (double) (i);
		
		return answer;
	}
	
}
