package com.product.util;

import java.util.Random;

public class RandomString {
	public static void main(String a[]) {
		String alphabet = "abcdefghijklmnopqrstuvwxyz";
		int N = alphabet.length();
		Random r = new Random();
		String result = "";
		for (;;) {
			for (int i = 0; i < 3; i++) {
				// System.out.print(alphabet.charAt(r.nextInt(N)));
				for (int j = 0; j < 3; j++) {
					result += alphabet.charAt(r.nextInt(N));

				}
				result = result + "/";
				// System.out.println("res==" + result);
			}
			System.out.println("=====" + result);
			result = "";
		}
	}
}
