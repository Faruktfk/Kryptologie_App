package algorithmen;

public class Ceasar extends Verschlüsselung {
	
	public Ceasar() {
		super("Ceasar", new String[] { "Schlüssel" });
	}

	public String verschlüsseln(String k, int s) {
		eingabeÜberprüfen(k);
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

	public String entschlüsseln(String c, int s) {
		eingabeÜberprüfen(c);
		return verschlüsseln(c, -s);

	}

}
