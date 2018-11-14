public class StantonMeasure {
	public static void main(String[] args) {
		System.out.println(stantonMeasure(new int[] { 1 }));
		System.out.println(stantonMeasure(new int[] { 0 }));
		System.out.println(stantonMeasure(new int[] { 3, 1, 1, 4 }));
		System.out.println(stantonMeasure(new int[] { 1, 3, 1, 1, 3, 3, 2, 3,
				3, 3, 4 }));
		System.out.println(stantonMeasure(new int[] { 1, 1, 1, 1, 1, 1, 2 }));
		System.out.println(stantonMeasure(new int[] {}));
	}

	static int stantonMeasure(int[] a) {
		if (a.length == 0)
			return 0;
		if (a.length == 1)
			return 1;
		int result = 0;
		int temp;
		for (int i = 0; i < a.length; i++) {
			temp = countOccur(a[i], a);
			if (result < temp)
				result = temp;
		}
		return result;
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
