package algorithmen;

import java.math.BigInteger;

public class DiffieHellman {

	private BigInteger g, p;
	private Person alice, bob;

	public String getG() {
		return g + "";
	}

	public String getP() {
		return p + "";
	}

	public DiffieHellman(String gStr, String pStr) {
		g = new BigInteger(gStr);
		p = new BigInteger(pStr);

		if (g.compareTo(new BigInteger("2")) == -1) {
			throw new IllegalArgumentException("Fehler - g muss mindestens 2 sein!");
		}

		if (!p.isProbablePrime(5)) {
			throw new IllegalArgumentException("Fehler - p muss eine Primzahl sein!");
		}

	}

	public void setAlice(String zahlStr) {
		alice = new Person(zahlStr);
	}

	public void setBob(String zahlStr) {
		bob = new Person(zahlStr);
	}

	public String getSchlüsseltausch() {
		BigInteger a = alice.schlüsselRechnen(bob.geheimeZahlSchicken());
		BigInteger b = bob.schlüsselRechnen(alice.geheimeZahlSchicken());

		if (!a.equals(b)) {
			throw new IllegalArgumentException("Fehler - K ist nicht gleich!");
		}
		return a + "";
	}

	private class Person {
		private BigInteger gA;
		private BigInteger a;

		public Person(String zahlStr) {
			a = new BigInteger(zahlStr);
			try {
				gA = g.modPow(a, p);
			} catch (Exception e) {
				throw new IllegalArgumentException("Fehler - Ungültige Eingabe!");
			}
		}

		public BigInteger geheimeZahlSchicken() {
			return gA;
		}

		public BigInteger schlüsselRechnen(BigInteger b) {
			return b.modPow(a, p);
		}

	}
}
