package algorithmen;

import java.math.BigDecimal;
import java.util.ArrayList;

public class RSA extends Verschl�sselung {

	public int n, e, blockl�nge;
	private static final char PLACEHOLDER = '9';
	private Empf�nger empf�nger;
	private Sender sender;

	public RSA(int p, int q, int e, int blockl�nge) {
		super("RSA-Verfahren mit EEA und WQM", new String[] { "p", "q", "e", "Blockl�nge" }, new String[] { "313", "311", "19", "5" });
		empf�nger = new Empf�nger(p, q, e, blockl�nge);
		sender = new Sender();
	}

	public String verschl�sseln(String klarText) {
		eingabe�berpr�fen(klarText);
		return sender.verschl�sseln(klarText);
	}

	public String entschl�sseln(String chiffre) {
		return empf�nger.entschl�sseln(chiffre);
	}

	private String convertString(String str, boolean forward) {
		String output = "";

		for (int i = 0; forward ? i < str.length() : i < str.length() - 1; i += (forward ? 1 : 2)) {
			String step = "";
			if (forward) {
				step = Character.toUpperCase(str.charAt(i)) + "";
				step = ALPHABET.indexOf(step) + "";
				step = step.length() != 1 ? step : "0" + step;
			} else {
				String temp = str.charAt(i) + "" + str.charAt(i + 1);
				if (temp.equals(PLACEHOLDER + "" + PLACEHOLDER)) {
					continue;
				}
				int index = Integer.parseInt(temp);
				if (index >= ALPHABET.length() || index < 0) {
					if ((index + "").charAt(1) == PLACEHOLDER) {
						index = index / 10;
					} else {
						throw new IllegalArgumentException("Fehler - ung�ltige Ausgabe!");
					}
				}
				step = ALPHABET.charAt(index) + "";
			}
			output += step;
		}

		return output;
	}

//	+#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#+

	private class Sender {
		public Sender() {
		}

		public String verschl�sseln(String klarText) {
			String output = "";
			String temp = convertString(klarText, true);
			String block = "";
			for (int i = 0; i <= temp.length(); i++) {
				if (i != 0 && i % blockl�nge == 0 || i == temp.length()) {
					if (block.length() != blockl�nge) {
						while (block.length() < blockl�nge) {
							block = block + PLACEHOLDER;
						}
					}
					String c = new BigDecimal(block).pow(e).remainder(new BigDecimal(n)) + "";
					while (c.length() < blockl�nge) {
						c = "0" + c;
					}
					if (c.length() > blockl�nge || Integer.parseInt(block) > n) {
						throw new IllegalArgumentException(
								"Fehler - ung�ltige Parameter! (p, q, e oder Blockl�nge �ndern!)");
					}
					output += c;
					block = "";
				}
				if (i < temp.length())
					block += temp.charAt(i);

			}
			return output;
		}
	}

//	+#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#++#+

	private class Empf�nger {
		private int pS, qS, phi;

		public Empf�nger(int pS, int qS, int eS, int bL�nge) {

			for (int i = 2; i <= pS / 2; i++) {
				if (pS % i == 0) {
					i = pS;
					throw new IllegalArgumentException("Fehler - p ist keine Primzahl");
				}
			}
			for (int i = 2; i <= qS / 2; i++) {
				if (qS % i == 0) {
					i = qS;
					throw new IllegalArgumentException("Fehler - q ist keine Primzahl!");
				}
			}

			ArrayList<Integer> teiler = new ArrayList<>();
			for (int i = 2; i <= eS; i++) {
				if (eS % i == 0) {
					teiler.add(i);
				}
			}
			phi = (pS - 1) * (qS - 1);
			for (Integer t : teiler) {
				if (phi % t == 0) {
					throw new IllegalArgumentException("Fehler - e ist ung�ltig!");
				}
			}

			this.pS = pS;
			this.qS = qS;
			n = this.pS * this.qS;
			e = eS;
			if (bL�nge < 3 || (phi + "").length() < bL�nge) {
				throw new IllegalArgumentException("Fehler - ung�ltige Blockl�nge!");
			}
			blockl�nge = bL�nge;
		}

		public String entschl�sseln(String c) {
			for(int i = 0; i<c.length(); i++) {
				if(!Character.isDigit(c.charAt(i))) {
					throw new IllegalArgumentException("Fehler - ung�ltiges Zeichen in der Eingabe");
				}
			}
			String output = "";
			long d = EEA();

			String block = "";
			for (int i = 0; i <= c.length(); i++) {
				if (i != 0 && i % blockl�nge == 0 || i == c.length()) {
					String k = WQM(block, d);
					while (k.length() < blockl�nge) {
						k = '0' + k;
					}
					output += k;
					block = "";

				}
				if (i < c.length()) {
					block += c.charAt(i);
				}
			}

			return convertString(output, false);
		}

		private long EEA() {
			long[][] speicher = { { phi, 1, 0 }, { e, 0, 1 } };
			while (speicher[1][0] != 1) {
				int times = (int) (speicher[0][0] / speicher[1][0]);
				long[] newRow = { speicher[0][0] - (speicher[1][0] * times), speicher[0][1] - (speicher[1][1] * times),
						speicher[0][2] - (speicher[1][2] * times) };
				speicher[0] = speicher[1];
				speicher[1] = newRow;
			}

			if (speicher[1][1] * phi + speicher[1][2] * e != 1) {
				throw new IllegalArgumentException("Fehler bei EEA! - ung�ltige Eingabe");
			}
			if (speicher[1][2] < 0) {
				speicher[1][2] += phi;
			}
			return speicher[1][2];
		}

		private String WQM(String c, long d) {
			String path = Integer.toBinaryString((int) d).substring(1).replace("1", "QM").replace("0", "Q");
			BigDecimal temp = new BigDecimal(c);
			for (int i = 0; i < path.length(); i++) {
				if (path.charAt(i) == 'Q') {
					temp = temp.pow(2).remainder(new BigDecimal(n));
				} else {
					temp = temp.multiply(new BigDecimal(c)).remainder(new BigDecimal(n));
				}
			}
			return temp + "";
		}

	}

}
