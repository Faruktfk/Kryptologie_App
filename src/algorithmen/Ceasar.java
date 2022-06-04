package algorithmen;

public class Ceasar extends Verschl�sselung {
	
	public Ceasar() {
		super("Ceasar", new String[] { "Schl�ssel" });
	}

	public String verschl�sseln(String k, int s) {
		eingabe�berpr�fen(k);
		String output = "";

		for (int i = 0; i < k.length(); i++) {
			char b = k.charAt(i);
			boolean upperCase = Character.isUpperCase(b);
			b = Character.toUpperCase(b);
			int index = (ALPHABET.indexOf(b + "") + s) % ALPHABET.length();
			if (index < 0) {
				index = 26 + index;
			}

			if (upperCase) {
				output += ALPHABET.charAt(index);
			} else {
				output += Character.toLowerCase(ALPHABET.charAt(index));
			}

		}
		return output;
	}

	public String entschl�sseln(String c, int s) {
		eingabe�berpr�fen(c);
		return verschl�sseln(c, -s);

	}

}
