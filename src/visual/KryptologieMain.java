package visual;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class KryptologieMain extends JFrame {

	public static void main(String[] args) {

		// Main-Klasse starten
		new KryptologieMain();

		
	}
	public KryptologieMain() {
		setTitle("Kryptologie");
		setContentPane(new KryptPanel());
		pack();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);

	}

}
