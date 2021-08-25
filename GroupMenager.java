import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;


/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia
 *          zarz¹dzanie grupami obiektów klasy Laptopy.
 *    Plik: GroupMenager.java
 *
 *   Autor: Artur Sobolewski 248913
 *    Data: grudzieñ 2019 r.
 *    
 *    Klasa GroupMenager implementuje g³ówne okno aplikacji, które umo¿liwia zarz¹dzanie utowrzonymi grupami oraz
 *    tworzenie nowych.
 */

public class GroupMenager extends JFrame implements ActionListener{

	private static final long serialVersionUID = 1L;
	
	private static final String GREETING_MESSAGE =
			"Program do zarz¹dzania grupami laptopów " +
	        "- wersja okienkowa\n\n" +
	        "Autor: Artur Sobolewski\n" +
			"Data:  grudzieñ 2019 r.\n";
	
	private static final String ALL_GROUPS_FILE = "LISTA_GRUP.BIN";

	public static void main(String[] args) {

		new GroupMenager();

	}
	
	WindowAdapter windowListener = new WindowAdapter() {
		@Override
		public void windowClosed(WindowEvent e) 
		{
			JOptionPane.showMessageDialog(null, "Program zakoñczy³ dzia³anie!");

		}
		@Override
		public void windowClosing(WindowEvent e) {
			windowClosed(e);
		}
	};
	
	private List<GroupOfLaptop> currentList = new ArrayList<GroupOfLaptop>();
	
	JMenuBar menu= new JMenuBar();
	JMenu m1 =new JMenu("Grupy");
	JMenu m2 =new JMenu("Grupy specjalne");
	JMenu m3 =new JMenu("O programie");
	
	JLabel tableName = new JLabel("Lista grup");
	
	JMenuItem mNew = new JMenuItem("Utwórz grupê");
	JMenuItem mEdit = new JMenuItem("Edytuj grupê");
	JMenuItem mDelete = new JMenuItem("Usuñ grupê");
	JMenuItem mLoadFromFile = new JMenuItem("Za³aduj grupê z pliku");
	JMenuItem mSaveToFile = new JMenuItem("Zapisz grupê do pliku");
	
	JMenuItem mCombine = new JMenuItem("Po³¹czenie grup");
	JMenuItem mAnd = new JMenuItem("Czêœæ wspólna");
	JMenuItem mGroupDifference = new JMenuItem("Ró¿nica grup");
	JMenuItem mSymmetricDiff = new JMenuItem("Ró¿nica symetryczna grup");
	
	JMenuItem mAuthor = new JMenuItem("Autor");
	
	JButton newButton    = new JButton("Utwórz");
	JButton editButton   = new JButton("Edytuj");
	JButton deleteButton = new JButton("Usuñ");
	JButton openButton = new JButton("Otwórz");
	JButton saveButton   = new JButton("Zapisz");
	JButton orButton   = new JButton("Suma");
	JButton andButton   = new JButton("Iloczyn");
	JButton differenceButton   = new JButton("Ró¿nica");
	JButton symmetricDiffButton= new JButton("Ró¿nica symetryczna");
	
	ViewGroupList viewList;
	
	public GroupMenager()
	{
		setTitle("Group Menager");  
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(450, 400);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout()); 

		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent event) {
				try {
					saveGroupListToFile(ALL_GROUPS_FILE);
					JOptionPane.showMessageDialog(null, "Dane zosta³y zapisane do pliku " + ALL_GROUPS_FILE);
				} catch (LaptopException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
			}
			@Override
			public void windowClosing(WindowEvent e) {
				windowClosed(e);
			}

		});
		
		try {
			loadGroupListFromFile(ALL_GROUPS_FILE);
			JOptionPane.showMessageDialog(null, "Dane zosta³y wczytane z pliku " + ALL_GROUPS_FILE);
		} catch (LaptopException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		
		
		
		m1.add(mNew);
		m1.add(mEdit);
		m1.add(mDelete);
		m1.add(mLoadFromFile);
		m1.add(mSaveToFile);
		
		m2.add(mCombine);
		m2.add(mAnd);
		m2.add(mGroupDifference);
		m2.add(mSymmetricDiff);
		
		m3.add(mAuthor);
		
		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		setJMenuBar(menu);
		
		newButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		orButton.addActionListener(this);
		andButton.addActionListener(this);
		differenceButton.addActionListener(this);
		symmetricDiffButton.addActionListener(this);
		mNew.addActionListener(this);
		mEdit.addActionListener(this);
		mDelete.addActionListener(this);
		mLoadFromFile.addActionListener(this);
		mSaveToFile.addActionListener(this);
		mCombine.addActionListener(this);
		mAnd.addActionListener(this);
		mGroupDifference.addActionListener(this);
		mSymmetricDiff.addActionListener(this);
		mAuthor.addActionListener(this);
		
		viewList = new ViewGroupList(currentList, 400, 250);
		viewList.refreshView();
		
		
		JPanel panel = new JPanel();
		
		panel.add(viewList);
		panel.add(newButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(openButton);
		panel.add(saveButton);
		panel.add(orButton);
		panel.add(andButton);
		panel.add(differenceButton);
		panel.add(symmetricDiffButton);
		
		setContentPane(panel);
		setVisible(true);
	}
	
	@SuppressWarnings("unchecked")
	void loadGroupListFromFile(String file_name) throws LaptopException {
		try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file_name))) {
		currentList = (List<GroupOfLaptop>)in.readObject();
		} catch (FileNotFoundException e) {
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		} catch (Exception e) {
			throw new LaptopException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}


	void saveGroupListToFile(String file_name) throws LaptopException {
		try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file_name))) {
			out.writeObject(currentList);
		} catch (FileNotFoundException e) {
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		} catch (IOException e) {
			throw new LaptopException("Wyst¹pi³ b³¹d podczas zapisu danych do pliku.");
		}
	}
	
	private  GroupOfLaptop chooseGroup(Window parent, String message){
		Object[] groups = currentList.toArray();
		GroupOfLaptop group = (GroupOfLaptop)JOptionPane.showInputDialog(
		                    parent, message,
		                    "Wybierz grupê",
		                    JOptionPane.QUESTION_MESSAGE,
		                    null,
		                    groups,
		                    null);
		return group;
	}
	
	
	@Override
	public void actionPerformed(ActionEvent event) {

		Object source = event.getSource();		
		try {
			if (source == newButton || source == mNew) {
				GroupOfLaptop group = GroupOfLaptopWindowDialog.createNewGroupOfLaptop(this);
				if (group != null) {
					currentList.add(group);
				}
			}
			if (source == mEdit || source == editButton) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfLaptop> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					new GroupOfLaptopWindowDialog(this, iterator.next());
				}
			}
			if (source == mDelete || source == deleteButton) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfLaptop> iterator = currentList.iterator();
					while (index-- >= 0)
						iterator.next();
					iterator.remove();
				}
			}
			if (source == mLoadFromFile || source == openButton) {
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					GroupOfLaptop group = GroupOfLaptop.readFromFile(chooser.getSelectedFile().getName());
					currentList.add(group);
				}
			}
			if (source == mSaveToFile || source == saveButton) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<GroupOfLaptop> iterator = currentList.iterator();
					while (index-- > 0)
						iterator.next();
					GroupOfLaptop group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						GroupOfLaptop.printToFile( chooser.getSelectedFile().getName(), group );
					}
				}
			}
			if (source == mCombine || source == orButton) {
				String message1 =
						"SUMA GRUP\n\n" +
			            "Tworzenie grupy zawieraj¹cej wszystkie laptopy z grupy pierwszej\n" +
						"oraz wszystkie laptopy z grupy drugiej.\n" +
			            "Wybierz pierwsz¹ grupê:";
				String message2 =
						"SUMA GRUP\n\n" +
					    "Tworzenie grupy zawieraj¹cej wszystkie laptopyy z grupy pierwszej\n" +
						"oraz wszystkie laptopy z grupy drugiej.\n" +
					    "Wybierz drug¹ grupê:";
				GroupOfLaptop group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfLaptop group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add(GroupOfLaptop.createGroupUnion(group1, group2) );
			}
			if (source == mAnd || source == andButton) {
				String message1 =
						"ILOCZYN GRUP\n\n" +
				        "Tworzenie grupy laptopów, które nale¿¹ zarówno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" +
				        "Wybierz pierwsz¹ grupê:";
				String message2 =
						"ILOCZYN GRUP\n\n" +
						"Tworzenie grupy laptopów, które nale¿¹ zarówno do grupy pierwszej,\n" +
						"jak i do grupy drugiej.\n" +
						"Wybierz drug¹ grupê:";
				GroupOfLaptop group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfLaptop group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfLaptop.createGroupIntersection(group1, group2) );
			}
			if (source == mGroupDifference || source == differenceButton) {
				String message1 =
						"RÓ¯NICA GRUP\n\n" +
				        "Tworzenie grupy laptopów, które nale¿¹ do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" +
				        "Wybierz pierwsz¹ grupê:";
				String message2 =
						"RÓ¯NICA GRUP\n\n" +
						"Tworzenie grupy laptopów, które nale¿¹ do grupy pierwszej\n" +
						"i nie ma ich w grupie drugiej.\n" +
						"Wybierz drug¹ grupê:";
				GroupOfLaptop group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfLaptop group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfLaptop.createGroupDifference(group1, group2) );
				
			}
			if (source == mSymmetricDiff || source == symmetricDiffButton) {
				String message1 = "RÓ¯NICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj¹cej laptopy nale¿¹ce tylko do jednej z dwóch grup,\n"
						+ "Wybierz pierwsz¹ grupê:";
				String message2 = "RÓ¯NICA SYMETRYCZNA GRUP\n\n"
						+ "Tworzenie grupy zawieraj¹cej laptopy nale¿¹ce tylko do jednej z dwóch grup,\n"
						+ "Wybierz drug¹ grupê:";
				GroupOfLaptop group1 = chooseGroup(this, message1);
				if (group1 == null)
					return;
				GroupOfLaptop group2 = chooseGroup(this, message2);
				if (group2 == null)
					return;
				currentList.add( GroupOfLaptop.createGroupSymmetricDiff(group1, group2) );
			}
			if (source == mAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
		}
		catch (LaptopException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		viewList.refreshView();
	}
}

class ViewGroupList extends JScrollPane {
private static final long serialVersionUID = 1L;

	private List<GroupOfLaptop> list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewGroupList(List<GroupOfLaptop> list, int width, int height){
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista grup:"));

		String[] tableHeader = { "Nazwa grupy", "Typ kolekcji", "Liczba laptopów" };
		tableModel = new DefaultTableModel(tableHeader, 0);
		table = new JTable(tableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int rowIndex, int colIndex) {
				return false; 
			}
		};
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(true);
		setViewportView(table);
	}

	void refreshView(){
		tableModel.setRowCount(0);
		for (GroupOfLaptop group : list) {
			if (group != null) {
				String[] row = { group.getName(), group.getType().toString(), "" + group.size() };
				tableModel.addRow(row);
			}
		}
	}

	int getSelectedIndex(){
		int index = table.getSelectedRow();
		if (index<0) {
			JOptionPane.showMessageDialog(this, "¯adana grupa nie jest zaznaczona.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}
