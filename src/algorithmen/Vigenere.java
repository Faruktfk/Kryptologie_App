package algorithmen;

public class Vigenere extends Verschl�sselung {

	public Vigenere() {
		super("Vigen�re", new String[] { "Schl�ssel" });
	}

	public String verschl�sseln(String k, String s) {
		eingabe�berpr�fen(k);
		String output = "";

		for (int i = 0; i < k.length(); i++) {

			char b = k.charAt(i);
			boolean upperCase = Character.isUpperCase(b);
			b = Character.toUpperCase(b);
			int index = (ALPHABET.indexOf(b + "")
					+ ALPHABET.indexOf(Character.toUpperCase(s.charAt(i % s.length())) + "")) % ALPHABET.length();
			if (upperCase) {
				output += ALPHABET.charAt(index);
			} else {
				output += Character.toLowerCase(ALPHABET.charAt(index));
			}

		}

		return output;
	}

	public String entschl�sseln(String c, String s) {
		eingabe�berpr�fen(s);
		String output = "";

		for (int i = 0; i < c.length(); i++) {

			char b = c.charAt(i);
			boolean upperCase = Character.isUpperCase(b);
			b = Character.toUpperCase(b);
			int index = (ALPHABET.indexOf(b + "")
					- ALPHABET.indexOf(Character.toUpperCase(s.charAt(i % s.length())) + "")) % ALPHABET.length();
			if (index < 0) {
				index += ALPHABET.length();
			}

			if (upperCase) {
				output += ALPHABET.charAt(index);
			} else {
				output += Character.toLowerCase(ALPHABET.charAt(index));
			}

		}
		return output;
	}

}
