package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import algorithmen.Ceasar;
import algorithmen.DiffieHellman;
import algorithmen.RSA;
import algorithmen.Verschlüsselung;
import algorithmen.Vigenere;

@SuppressWarnings("serial")
public class KryptPanel extends JPanel implements ActionListener {
	
	public static final int WIDTH = 900, HEIGHT = 700;
	private final String VERSCHLÜSSELN = "verschlüsseln", RESET = "reset", REVERSE = "reverse",
			ENTSCHLÜSSELN = "entschlüsseln", HINZUFÜGEN = "hinzu", RECHNEN = "rechnen", RESET_2 = "resetTab2", AUS_SYMBOL=">>";
	private JTextPane eingabeTPane, ausgabeTPane;
	private JPanel panel_algContent;
	private JScrollPane algContent_scroll;
	private ArrayList<AlgorithmusPanel> benutzteMethoden;
	private Border klarTextBorder, chiffreBorder;
	private JTextArea fehlerT1, fehlerT2;

	private JTextField[][] diffieH;
	private String K = "";

	public KryptPanel() {

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setLayout(new CardLayout());

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		add(tabbedPane, "tabbedPane");

		JPanel panel_tab1 = new JPanel();
		tabbedPane.addTab("Ver-/Entschlüsseln", null, panel_tab1);
		panel_tab1.setLayout(null);

		JPanel panel_tab2 = new JPanel();
		tabbedPane.addTab("Schlüsseltausch", null, panel_tab2);
		panel_tab2.setLayout(null);

		tabbedPane.setSelectedIndex(0);

		tabbedPane.addChangeListener(e -> {

			if (tabbedPane.getSelectedIndex() == 0) {
				panel_tab2.removeAll();
				loadTab1(panel_tab1);
			} else {
				panel_tab1.removeAll();
				loadTab2(panel_tab2);

			}
		});

		if (tabbedPane.getSelectedIndex() == 0) {
			loadTab1(panel_tab1);
		} else {
			loadTab2(panel_tab2);

		}

		setVisible(true);
	}

	private void loadTab2(JPanel panel_tab2) {

		diffieH = new JTextField[3][2];

		fehlerT2 = new JTextArea();
		fehlerT2.setBounds(325, 200, 250, 200);
		fehlerT2.setOpaque(true);
		fehlerT2.setEditable(false);
		fehlerT2.setFocusable(false);
		fehlerT2.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
								BorderFactory.createLoweredBevelBorder()),
						"Console", TitledBorder.LEFT, TitledBorder.TOP));
		fehlerT2.setBackground(Color.white);
		fehlerT2.setForeground(Color.red);
		panel_tab2.add(fehlerT2);

		diffieH[0] = generatePanel(panel_tab2, " Öffentlich", new String[] { "g", "p" }, (WIDTH - 400) / 2, 50, 400,
				100); // öffentlich
		diffieH[1] = generatePanel(panel_tab2, " Alice", new String[] { "a", "K" }, 80, 230, 200, 250); // alice
		diffieH[2] = generatePanel(panel_tab2, " Bob", new String[] { "b", "K" }, WIDTH - 280, 230, 200, 250); // bob

		JButton rechnenBtn = new JButton("RECHNEN");
		rechnenBtn.setBounds(395, 450, 118, 51);
		rechnenBtn.addActionListener(this);
		rechnenBtn.setActionCommand(RECHNEN);
		rechnenBtn.setFocusable(false);
		panel_tab2.add(rechnenBtn);

		JButton resetBtn = new JButton("RESET");
		resetBtn.addActionListener(this);
		resetBtn.setActionCommand(RESET_2);
		resetBtn.setBounds(395, 530, 118, 51);
		resetBtn.setFocusable(false);
		panel_tab2.add(resetBtn);

	}

	private JTextField[] generatePanel(JPanel parent, String title, String[] comps, int x, int y, int width,
			int height) {
		JTextField[] output = new JTextField[2];
		JPanel child = new JPanel();
		child.setBounds(x, y, width, height);
		child.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createLoweredBevelBorder()));
		child.setLayout(new BorderLayout());

		child.add(new JLabel(title), BorderLayout.NORTH);

		JPanel compPanel = new JPanel();
		compPanel.setLayout(new GridLayout(comps.length, 2));
		child.add(compPanel, BorderLayout.CENTER);

		for (int i = 0; i < comps.length; i++) {
			JLabel lbl = new JLabel(comps[i]);
			lbl.setHorizontalAlignment(SwingConstants.CENTER);
			compPanel.add(lbl);

			JTextField tf = new JTextField();
			if (!title.contains("Öffentlich") && i == 1) {
				tf.setEditable(false);
				tf.setBackground(Color.white);
				tf.setFocusable(false);
			}
			output[i] = tf;
			compPanel.add(tf);
		}
		parent.add(child);

		return output;
	}

	private void loadTab1(JPanel panel_tab1) {

		benutzteMethoden = new ArrayList<>();
		klarTextBorder = BorderFactory
				.createTitledBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
								BorderFactory.createLoweredBevelBorder()),
						"KlarText", TitledBorder.LEFT, TitledBorder.TOP);
		chiffreBorder = BorderFactory
				.createTitledBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
								BorderFactory.createLoweredBevelBorder()),
						"Chiffre", TitledBorder.LEFT, TitledBorder.TOP);

		JLabel noticeLbl = new JLabel("Bitte ohne Leerzeichen eingeben!");
		noticeLbl.setBounds(0, -12, WIDTH, 50);
		noticeLbl.setHorizontalAlignment(JLabel.CENTER);
		noticeLbl.setFont(new Font("Calibri", Font.BOLD, 17));
		noticeLbl.setForeground(Color.red);
		panel_tab1.add(noticeLbl);

		JLabel eingabenLbl = new JLabel("  Eingabe:");
		eingabenLbl.setBounds(31, 10, 200, 51);
		eingabenLbl.setFont(new Font("Calibri", Font.BOLD, 15));
		panel_tab1.add(eingabenLbl);

		eingabeTPane = new JTextPane();
		eingabeTPane.setBorder(klarTextBorder);
		eingabeTPane.setAlignmentY(50.5f);
		JScrollPane scrollEingabe = new JScrollPane(eingabeTPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollEingabe.setBounds(31, 45, 400, 240);
		panel_tab1.add(scrollEingabe);

		JLabel ausgabeLbl = new JLabel("  Ausgabe:");
		ausgabeLbl.setBounds(460, 10, 200, 51);
		ausgabeLbl.setFont(new Font("Calibri", Font.BOLD, 15));
		panel_tab1.add(ausgabeLbl);

		ausgabeTPane = new JTextPane();
		ausgabeTPane.setBorder(chiffreBorder);
		ausgabeTPane.setAlignmentY(50.5f);
		ausgabeTPane.setEditable(false);
		JScrollPane scrollAusgabe = new JScrollPane(ausgabeTPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollAusgabe.setBounds(460, 45, 400, 240);
		panel_tab1.add(scrollAusgabe);

		JButton verschlüsselnBtn = new JButton("VERSCHL\u00DCSSELN");
		verschlüsselnBtn.setFocusable(false);
		verschlüsselnBtn.setBackground(Color.LIGHT_GRAY);
		verschlüsselnBtn.setBounds(690, 420, 150, 50);
		verschlüsselnBtn.addActionListener(this);
		verschlüsselnBtn.setActionCommand(VERSCHLÜSSELN);
		panel_tab1.add(verschlüsselnBtn);

		JButton reverseBtn = new JButton("");
		try {
			reverseBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/reverseArrow.jpg"))
					.getScaledInstance(70, 50, java.awt.Image.SCALE_SMOOTH)));
			reverseBtn.setBackground(Color.white);
		} catch (IOException e) {
			reverseBtn.setBackground(Color.LIGHT_GRAY);
			reverseBtn.setText("REVERSE");
		}
		reverseBtn.setFocusable(false);
		reverseBtn.setBounds(720, 480, 90, 40);
		reverseBtn.addActionListener(this);
		reverseBtn.setActionCommand(REVERSE);
		panel_tab1.add(reverseBtn);

		JButton entschlüsselnBtn = new JButton("ENTSCHL\u00DCSSELN");
		entschlüsselnBtn.setFocusable(false);
		entschlüsselnBtn.setBackground(Color.LIGHT_GRAY);
		entschlüsselnBtn.setBounds(690, 530, 150, 50);
		entschlüsselnBtn.addActionListener(this);
		entschlüsselnBtn.setActionCommand(ENTSCHLÜSSELN);
		panel_tab1.add(entschlüsselnBtn);

		JButton resetBtn = new JButton("RESET");
		resetBtn.setFocusable(false);
		resetBtn.setBackground(Color.LIGHT_GRAY);
		resetBtn.setBounds(720, 600, 89, 40);
		resetBtn.addActionListener(this);
		resetBtn.setActionCommand(RESET);
		panel_tab1.add(resetBtn);

		fehlerT1 = new JTextArea();
		fehlerT1.setLineWrap(true);
		fehlerT1.setWrapStyleWord(true);
		fehlerT1.setEditable(false);
		fehlerT1.setBorder(
				BorderFactory.createTitledBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
								BorderFactory.createLoweredBevelBorder()),
						"Console", TitledBorder.LEFT, TitledBorder.TOP));
		fehlerT1.setForeground(Color.RED);
		JScrollPane scrollFehler = new JScrollPane(fehlerT1, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollFehler.setBounds(655, 303, 214, 101);
		panel_tab1.add(scrollFehler);

		JPanel methodenPanel = new JPanel();
		methodenPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		methodenPanel.setBounds(30, 290, 610, 350);
		methodenPanel
				.setBorder(BorderFactory.createTitledBorder(
						BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(),
								BorderFactory.createLoweredBevelBorder()),
						"Algorithmen", TitledBorder.LEFT, TitledBorder.TOP));
		panel_tab1.add(methodenPanel);
		methodenPanel.setLayout(new BorderLayout(0, 0));

		panel_algContent = new JPanel();
		panel_algContent.setOpaque(true);
		panel_algContent.setPreferredSize(new Dimension(0, 1));
		panel_algContent.setBackground(Color.LIGHT_GRAY);
		panel_algContent.setLayout(new GridLayout(0, 1, 0, 5));
		algContent_scroll = new JScrollPane(panel_algContent, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		methodenPanel.add(algContent_scroll, BorderLayout.CENTER);

		JComboBox<String> methoden_ComBox = new JComboBox<>(
				new String[] { "Hinzufügen", "Ceasar", "Vigenère", "RSA-Verfahren" });
		methoden_ComBox.setFocusable(false);
		methodenPanel.add(methoden_ComBox, BorderLayout.NORTH);
		methoden_ComBox.addActionListener(this);
		methoden_ComBox.setActionCommand(HINZUFÜGEN);

	}

	private void eingabeÜberprüfen(String eingabe, int dataTyp) throws Exception {
		eingabe = eingabe.strip().trim();
		for (int i = 0; i < eingabe.length(); i++) {
			switch (dataTyp) {
			case 0: // String
				if (!Character.isLetter(eingabe.charAt(i))) {
					throw new IllegalArgumentException("Fehler - nur Buchstaben erlaubt!");
				}
				break;
			case 1: // Integer
				if (!Character.isDigit(eingabe.charAt(i))) {
					throw new IllegalArgumentException("Fehler - nur Ziffern erlaubt!");
				}
				break;
			}
		}
	}

	private String schlüsseln(String raw, boolean forward) {
		raw = raw.trim().strip().replace("ö", "oe").replace("ü", "ue").replace("ä", "ae").replace("ß", "ss");
		String temp = raw;
		String output = "";
		if (!raw.isBlank()) {
			try {
				ArrayList<AlgorithmusPanel> tempBMethoden = new ArrayList<>();
				tempBMethoden.addAll(benutzteMethoden);
				if (!forward) {
					Collections.reverse(tempBMethoden);
				}

				for (AlgorithmusPanel ap : tempBMethoden) {
					Verschlüsselung v = ap.getVerschlüsselung();
					if (v instanceof Ceasar) {
						try {
							eingabeÜberprüfen(ap.getEingaben().get(0), 1);
							temp = forward
									? ((Ceasar) v).verschlüsseln(temp, Integer.parseInt(ap.getEingaben().get(0)))
									: ((Ceasar) v).entschlüsseln(temp, Integer.parseInt(ap.getEingaben().get(0)));
						} catch (Exception e1) {
							throw new IllegalArgumentException("Fehler - Ceasar Schlüssel fehlerhaft! "
									+ (e1 instanceof IllegalArgumentException ? e1.getMessage() : ""));
						}

					} else if (v instanceof Vigenere) {
						try {
							temp = forward ? ((Vigenere) v).verschlüsseln(temp, ap.getEingaben().get(0))
									: ((Vigenere) v).entschlüsseln(temp, ap.getEingaben().get(0));
						} catch (Exception e1) {
							throw new IllegalArgumentException("Fehler - Vigenère Schlüssel fehlerhaft! "
									+ (e1 instanceof IllegalArgumentException ? e1.getMessage() : ""));
						}

					} else if (v instanceof RSA) {
						try {
							for (String ein : ap.getEingaben()) {
								eingabeÜberprüfen(ein, 1);
							}
							ap.setVerschlüsselung(new RSA(Integer.parseInt(ap.getEingaben().get(0)),
									Integer.parseInt(ap.getEingaben().get(1)),
									Integer.parseInt(ap.getEingaben().get(2)),
									Integer.parseInt(ap.getEingaben().get(3))));
							temp = forward ? ((RSA) v).verschlüsseln(temp) : ((RSA) v).entschlüsseln(temp);
						} catch (Exception e1) {
							throw new IllegalArgumentException("Fehler - RSA Eingaben fehlerhaft! "
									+ (e1 instanceof IllegalArgumentException ? e1.getMessage() : ""));
						}
					}
					output += AUS_SYMBOL + " " + temp + "\n";
				}
			} catch (Exception e2) {
				new Thread(() -> {
					fehlerT1.setText(e2.getMessage());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					fehlerT1.setText("");

				}).start();
				return "";

			}

			fehlerT1.setText("");
			return output.isBlank() ? raw : output;
		}
		return "";
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(VERSCHLÜSSELN)) {
			ausgabeTPane.setText(schlüsseln(eingabeTPane.getText(), true));
			eingabeTPane.setBorder(klarTextBorder);
			ausgabeTPane.setBorder(chiffreBorder);

		} else if (e.getActionCommand().equals(ENTSCHLÜSSELN)) {
			eingabeTPane.setBorder(chiffreBorder);
			ausgabeTPane.setBorder(klarTextBorder);
			ausgabeTPane.setText(schlüsseln(eingabeTPane.getText(), false));

		} else if (e.getActionCommand().equals(REVERSE)) {
			String temp = ausgabeTPane.getText();
			if(temp.contains(AUS_SYMBOL)) {
				String[] ts = temp.split(AUS_SYMBOL);
				temp = ts[ts.length-1].strip().trim();
			}
			eingabeTPane.setText(temp);
			ausgabeTPane.setText("");
			
			
//			String temp = eingabeTPane.getText();
//			eingabeTPane.setText(ausgabeTPane.getText());
//			ausgabeTPane.setText(temp);

		} else if (e.getActionCommand().equals(RESET)) {
			eingabeTPane.setText("");
			ausgabeTPane.setText("");
			fehlerT1.setText("");
			eingabeTPane.setBorder(klarTextBorder);
			ausgabeTPane.setBorder(chiffreBorder);
			K = "";
			
		} else if (e.getActionCommand().equals(HINZUFÜGEN)) {
			@SuppressWarnings("unchecked")
			JComboBox<String> mCb = (JComboBox<String>) e.getSource();
			String s = (String) (mCb.getSelectedItem());
			Verschlüsselung v = null;
			if (s.equals(mCb.getItemAt(1))) {
				v = new Ceasar();
			} else if (s.equals(mCb.getItemAt(2))) {
				v = new Vigenere();
			} else if (s.equals(mCb.getItemAt(3))) {
				v = new RSA(313, 311, 19, 5);
			}

			if (v != null) {
				AlgorithmusPanel ap = new AlgorithmusPanel(v);
				panel_algContent.add(ap);
				benutzteMethoden.add(ap);
			}

			mCb.setSelectedIndex(0);
			panel_algContent.setPreferredSize(new Dimension(0, panel_algContent.getPreferredSize().height + 100));
			algContent_scroll.revalidate();

		} else if (e.getActionCommand().equals(RESET_2)) {
			for (JTextField[] tfs : diffieH) {
				tfs[0].setText("");
				tfs[1].setText("");
			}
			fehlerT2.setText("");
			diffieH[0][0].requestFocusInWindow();
			K = "";

		} else if (e.getActionCommand().equals(RECHNEN)) {
			try {
				for (JTextField[] tfs : diffieH) {
					if (tfs[0].getText().isBlank() || diffieH[0][1].getText().isBlank()) {
						return;
					}
					eingabeÜberprüfen(tfs[0].getText(), 1);
					eingabeÜberprüfen(tfs[1].getText(), 1);
				}
				DiffieHellman dh = new DiffieHellman(diffieH[0][0].getText().strip().trim(), diffieH[0][1].getText().strip().trim());
				dh.setAlice(diffieH[1][0].getText().strip().trim());
				dh.setBob(diffieH[2][0].getText().strip().trim());
				K = dh.getSchlüsseltausch();
				diffieH[1][1].setText(K);
				diffieH[2][1].setText(K);
				fehlerT2.setText("");

			} catch (Exception e2) {
				new Thread(() -> {
					fehlerT2.setText(e2.getMessage());
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e3) {
						e3.printStackTrace();
					}
					fehlerT2.setText("");
				}).start();
			}
		}

	}

	private class AlgorithmusPanel extends JPanel {
		private Verschlüsselung v;

		public AlgorithmusPanel(Verschlüsselung v) {
			this.v = v;
			setBackground(Color.yellow);
			setPreferredSize(new Dimension(0, 10));
			setLayout(new BorderLayout(0, 0));
			setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));

			JButton delBtn = new JButton("X");
			delBtn.setFocusable(false);
			delBtn.addActionListener(e -> {
				panel_algContent.remove(this);
				panel_algContent.repaint();
				benutzteMethoden.remove(this);
				panel_algContent.setPreferredSize(new Dimension(0, panel_algContent.getPreferredSize().height - 100));
				algContent_scroll.revalidate();
			});
			this.add(delBtn, BorderLayout.EAST);

			JLabel alg_name = new JLabel(v.getName());
			alg_name.setOpaque(true);
			this.add(alg_name, BorderLayout.NORTH);

			JPanel requirementsP = new JPanel();
			this.add(requirementsP, BorderLayout.CENTER);
			requirementsP.setLayout(new GridLayout(0, 2, 0, 2));

			for (int i = 0; i < v.getEingabeNamen().length; i++) {
				JLabel lblNewLabel_2 = new JLabel(v.getEingabeNamen()[i] + ":");
				requirementsP.add(lblNewLabel_2);

				JTextField textField = new JTextField(v instanceof RSA ? v.getEingabeBeispiel()[i] : v instanceof Ceasar ? K : "");
				textField.setPreferredSize(new Dimension(50, 5));
				requirementsP.add(textField);
				textField.setColumns(10);

			}

		}

		public Verschlüsselung getVerschlüsselung() {
			return v;
		}

		public void setVerschlüsselung(Verschlüsselung v) {
			this.v = v;
		}

		public ArrayList<String> getEingaben() {
			ArrayList<String> output = new ArrayList<>();
			for (Component c : ((Container) this.getComponent(2)).getComponents()) {
				if (c instanceof JTextField) {
					String eingabe = ((JTextField) c).getText().strip().trim();
					if (!eingabe.isBlank())
						output.add(eingabe);
				}
			}
			return output;
		}

	}
}
