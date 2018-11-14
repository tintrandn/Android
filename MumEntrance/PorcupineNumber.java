public class PorcupineNumber {
	public static void main(String[] args) {
		System.out.println(findPorcupineNumber(0));
		System.out.println(findPorcupineNumber(9));
		System.out.println(findPorcupineNumber(138));
		System.out.println(findPorcupineNumber(139));
	}

	static int findPorcupineNumber(int n) {
		int prime1;
		int prime2;
		while (true) {
		n++;
		prime1 = findNextPrimeDigit9(n);
		prime2 = findNextPrime(prime1+1);
		if (prime2 % 10 == 9) return prime1;
		}
	}

	static int findNextPrimeDigit9(int n) {
		while (true) {
			if (checkPrime(n) && n%10 == 9) return n;
			n++;
		}
	}
	
	static int findNextPrime(int n) {
		while (true) {
			if (checkPrime(n)) return n;
			n++;
		}
	}

	static boolean checkPrime(int n) {
		for (int i = 2; i < n; i++) {
			if (n % i == 0)
				return false;
		}
		return true;
	}
}
