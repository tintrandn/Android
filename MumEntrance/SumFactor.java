public class SumFactor {
	public static void main(String[] args) {
		System.out.println(sumFactor(new int[] { 3,0,2,-5,0 }));
		System.out.println(sumFactor(new int[] { 9,-3,-3,-1,-1 }));
		System.out.println(sumFactor(new int[] { 1 }));
		System.out.println(sumFactor(new int[] { 0,0,0 }));
	}

	static int sumFactor(int[] a) {
		int sum = 0;
		if (a.length == 0)
			return 0;
		for (int i = 0; i < a.length; i++)
			sum += a[i];
		return countOccur(sum, a);
	}

	static int countOccur(int o, int[] a) {
		int count = 0;
		for (int i = 0; i < a.length; i++) {
			if (o == a[i])
				count++;
		}
		return count;
	}
}
