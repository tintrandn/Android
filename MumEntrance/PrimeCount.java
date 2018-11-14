public class PrimeCount {

	public static void main(String[] args) {
		System.out.println(primeCount(10, 30));
		System.out.println(primeCount(11, 29));
		System.out.println(primeCount(20, 22));
		System.out.println(primeCount(1, 1));
		System.out.println(primeCount(5, 5));
		System.out.println(primeCount(6, 2));
		System.out.println(primeCount(-10, 6));
	}

	static int primeCount(int start, int end) {
		int result = 0;
		if (start < 0) start = 0;
		if (end < 0 ) end = 0;
		if (start > end) return 0;
		for (int i = start; i <= end; i++) {
			if (checkPrime(i))
				result++;
		}
		return result;
	}

	static boolean checkPrime(int n) {
		if (n < 2 ) return false;
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
}
