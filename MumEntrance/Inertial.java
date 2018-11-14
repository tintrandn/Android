public class Inertial {

	static int maxEven;
	
	public static void main(String[] args) {
		System.out.println(isInertial(new int[] { 1 }));
		System.out.println(isInertial(new int[] { 2 }));
		System.out.println(isInertial(new int[] { 1, 2, 3, 4 }));
		System.out.println(isInertial(new int[] { 1, 1, 1, 1, 1, 1, 2 }));
		System.out.println(isInertial(new int[] { 2, 12, 4, 6, 8, 11 }));
		System.out.println(isInertial(new int[] { 2, 12, 12, 4, 6, 8, 11 }));
		System.out.println(isInertial(new int[] { -2, -4, -6, -8, -11 }));
		System.out.println(isInertial(new int[] { 2, 3, 5, 7 }));
		System.out.println(isInertial(new int[] { 2, 4, 6, 8, 10 }));
	}

	static int isInertial(int[] a) {
		int minOdd;
		if (checkMaxEven(a)) return 0;
		minOdd = minOdd(a);
		if (minOdd == -1) return 0;
		if (checkLarger(a,maxEven,minOdd)) return 0;
		return 1;
	}
	
	static boolean checkLarger(int[] a, int maxEven, int minOdd) {
		for (int i = 0; i < a.length; i++) {
			if (a[i] % 2 == 0 && a[i] > minOdd && a[i] != maxEven) {
				return true;
			}
		}
		return false;
	}

	static int minOdd(int[] a) {
		int minOdd = -1;
		for (int i = 0; i < a.length; i++) {
			if (a[i] % 2 != 0) {
				minOdd = a[i];
				break;
			}
		}

		for (int i = 0; i < a.length; i++) {
			if (a[i] % 2 != 0 && a[i] < minOdd) {
				minOdd = a[i];
			}
		}
		return minOdd;
	}

	static boolean checkMaxEven(int[] a) {
		int max = a[0];
		for (int i = 0; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		if (max % 2 == 0) {
			maxEven = max;
			return false;
		}
		return true;
	}

}
