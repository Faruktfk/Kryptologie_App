package algorithmen;

public abstract class  Verschl�sselung {
	public final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	private String name;
	private String[] eingabeNamen, eingabenBeispiel;
	

	public Verschl�sselung(String name, String[] eingabeNamen, String[] eingabenBeispiel) {
		this.name = name;
		this.eingabenBeispiel = eingabenBeispiel;
		this.eingabeNamen = eingabeNamen;
	}
	
	public Verschl�sselung(String name, String[] eingabeNamen) {
		this.name = name;
		this.eingabenBeispiel = null;
		this.eingabeNamen = eingabeNamen;
	}
	
	public String getName() {
		return name;
	}
	
	public String[] getEingabeNamen() {
		return eingabeNamen;
	}
	
	public String[] getEingabeBeispiel() {
		return eingabenBeispiel;
	}
	
	
	
	public void eingabe�berpr�fen(String k) {
		for(int i = 0; i<k.length(); i++) {
			if(!ALPHABET.contains(Character.toUpperCase(k.charAt(i))+"")) {
				throw new IllegalArgumentException("Fehler - unbekanntes Zeichen in der Eingabe!");
			}
		}	
	}

}
