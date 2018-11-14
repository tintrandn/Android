public class Madhav {

	public static void main(String[] args) {
		System.out.println(isMadhavArray(new int[] { 2, 1, 1 }));
		System.out.println(isMadhavArray(new int[] { 2, 1, 1, 4, -1, -1 }));
		System.out.println(isMadhavArray(new int[] { 6, 2, 4, 2, 2, 2, 1, 5, 0,
				0 }));
		System.out.println(isMadhavArray(new int[] { 18, 9, 10, 6, 6, 6 }));
		System.out.println(isMadhavArray(new int[] { -6, -3, -3, 8, -5, -4 }));
		System.out.println(isMadhavArray(new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 1, 1, 1, -2, -1 }));
		System.out.println(isMadhavArray(new int[] { 3, 1, 2, 3, 0 }));
	}

	static int isMadhavArray(int[] a) {
		int start = 0;
		int end = 0;
		int i = 1;
		while (end < a.length) {
			start = start + i;
			end = end + i + 1;
			i++;
			// size not match
			if (end >= a.length)
				return 0;
			// a0 not equal
			if (check(a, start, end) == false)
				return 0;
			// end of array
			if (end == a.length - 1)
				return 1;
		}
		return 1;
	}

	static boolean check(int[] a, int start, int end) {
		int sum = 0;
		for (int i = start; i <= end; i++) {
			sum += a[i];
		}
		return a[0] == sum;
	}
}
