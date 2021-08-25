import java.awt.Color;
import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia testowanie 
 *          operacji wykonywanych na obiektach klasy Laptopy.
 *    Plik: LaptopyWindowDialog.java
 *          
 *   Autor: Artur Sobolewski 248913
 *    Data: 10.11.2019 r.
 *    
 *    Klasa LaptopyWindowDialog odpowiada za procedurê tworzenia nowych Laptopów.
 */
public class LaptopyWindowDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	JLabel pamiecRAMLabel               = new JLabel("              Pamiêæ RAM: ");
	JLabel matrycaLabel                 = new JLabel("        Rozmiar matrycy: ");
	JLabel hddLabel                     = new JLabel("              Pamiêæ HDD: ");
	JLabel czestotliwoscTaktowaniaLabel = new JLabel("Czêstotliwoœæ procesora: ");
	JLabel krajProdukcjiLabel           = new JLabel("          Kraj produkcji: ");
	JLabel markaLabel                   = new JLabel("                   Marka:                        ");

	JTextField pamiecRAMField = new JTextField(10);
	JTextField matrycaField = new JTextField(10);
	JTextField hddField = new JTextField(10);
	JTextField czestotliwoscTaktowaniaField = new JTextField(10);
	JTextField krajProdukcjiField = new JTextField(10);
	JTextField markaField = new  JTextField(10);
	JComboBox<LaptopKinds> markaBox = new JComboBox<LaptopKinds>(LaptopKinds.values());

	JButton OKButton = new JButton("  OK  ");
	JButton CancelButton = new JButton("Anuluj");
	
	private LaptopyWindowDialog(Window parent, Laptopy laptop) 
	{
		super(parent, Dialog.ModalityType.DOCUMENT_MODAL);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(330, 250);
		setLocationRelativeTo(parent);
		
		this.laptop = laptop;
		
		if (laptop==null){
			setTitle("Nowy laptop");
		} else{
			setTitle(laptop.toString());
			pamiecRAMField.setText("" + laptop.getPamiecRAM());
			matrycaField.setText("" + laptop.getMatryca());
			hddField.setText("" + laptop.getHdd());
			czestotliwoscTaktowaniaField.setText("" + laptop.getCzestotliwoscTaktowania());
			krajProdukcjiField.setText("" + laptop.getKrajProdukcji());
			markaBox.setSelectedItem(laptop.getLaptopKind());
		}
		
		OKButton.addActionListener( this );
		CancelButton.addActionListener( this );
		
		JPanel panel = new JPanel();
		
		panel.setBackground(Color.orange);
		
		panel.add(pamiecRAMLabel);
		panel.add(pamiecRAMField);
		
		panel.add(matrycaLabel);
		panel.add(matrycaField);
		
		panel.add(hddLabel);
		panel.add(hddField);
		
		panel.add(czestotliwoscTaktowaniaLabel);
		panel.add(czestotliwoscTaktowaniaField);
		
		panel.add(krajProdukcjiLabel);
		panel.add(krajProdukcjiField);
		
		panel.add(markaLabel);
		panel.add(markaBox);
		
		panel.add(OKButton);
		panel.add(CancelButton);
		
		setContentPane(panel);
		setVisible(true);
	}
	
	private Laptopy laptop;
	
	public static Laptopy createNewLaptop(Window parent)
	{
		LaptopyWindowDialog dialog = new LaptopyWindowDialog(parent, null);
		return dialog.laptop;
	}
	
	public static void changeLaptopData(Window parent, Laptopy laptop) 
	{
		new LaptopyWindowDialog(parent, laptop);
	}

	@Override
	public void actionPerformed(ActionEvent e) 
	{
		Object eventSource = e.getSource();
		try {
			if(eventSource == OKButton)
			{
					if (laptop == null && pamiecRAMField.getText() != "" && matrycaField.getText() != ""
							&& hddField.getText() != "" && czestotliwoscTaktowaniaField.getText() != ""
							&& krajProdukcjiField.getText() != "") 
					{
						laptop = new Laptopy(Integer.parseInt(pamiecRAMField.getText()), Float.parseFloat(matrycaField.getText()),
								Integer.parseInt(hddField.getText()), Float.parseFloat(czestotliwoscTaktowaniaField.getText()),
								krajProdukcjiField.getText());
					}else if(pamiecRAMField.getText() != "" && matrycaField.getText() != ""
							&& hddField.getText() != "" && czestotliwoscTaktowaniaField.getText() != ""
							&& krajProdukcjiField.getText() != "") 
					{
						laptop.setPamiecRAM(pamiecRAMField.getText());
						laptop.setMatryca(matrycaField.getText());
						laptop.setHdd(hddField.getText());
						laptop.setCzestotliwoscTaktowania(czestotliwoscTaktowaniaField.getText());
						laptop.setKrajProdukcji(krajProdukcjiField.getText());
					}				
					laptop.setLaptopKind((LaptopKinds) markaBox.getSelectedItem());
					dispose();
			}
			if(eventSource == CancelButton)
			{
				dispose();
			}
		}catch (LaptopException exc) {
			JOptionPane.showMessageDialog(this, exc.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
	}
}
