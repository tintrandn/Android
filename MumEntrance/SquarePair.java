public class SquarePair {

	public static void main(String[] args) {
		System.out.println(countSquarePairs(new int[] { 9, 0, 2, -5, 7 }));
		System.out.println(countSquarePairs(new int[] { 9 }));
		System.out.println(countSquarePairs(new int[] { 11, 5, 4, 20 }));
	}

//	static int[] sort(int[] a) {
//		int temp;
//		for (int i = 0 ; i< a.length; i++) {
//			for (int j = i+ 1; j < a.length; j++) {
//				if (a[i] > a[j]){
//					temp = a[i];
//					a[i] = a[j];
//					a[j] = temp;
//				}
//			}
//		}
//		return a;
//	}

	static int countSquarePairs(int[] a) {
		int sum = 0;
		if (a.length == 1) {
			return sum;
		}
//		a = sort(a);
		for (int i = 0; i < a.length; i++) {
			sum += check(i, a);
		}
		return sum;
	}

	static int check(int i, int[] a) {
		int result = 0;
		for (int j = i + 1; j < a.length; j++) {
			if (Math.sqrt(a[i] + a[j]) % 1 == 0 && a[i] > 0 && a[j] >0)
				result++;
		}
		return result;
	}
}
