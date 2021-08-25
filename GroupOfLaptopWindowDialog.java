import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/* 		Program: Aplikacja okienkowa z GUI, która umo¿liwia zarz¹dzanie
 *   grupami obiektów klasy Laptopy.
 * 
 *    Plik: GroupOfLaptopWindowDialog.java
 *          
 *   Autor: Artur Sobolewski 248913
 *    Data: grudzieñ 2019 r.
 *
 * Klasa GroupOfLaptopWindowDialog odpowiada za pomocnicze okno do tworzenia nowych grup lub edytowania
 * ju¿ istniej¹cych. Klasa ta wykorzystuje klasê LaptopWindowDialog do tworzenia nowych Laptopów lub 
 * edytowania ju¿ utworzonych w grupach.
 */
public class GroupOfLaptopWindowDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private GroupOfLaptop currentGroup;
	private static final String GREETING_MESSAGE = "Edytor grupy laptopów - wersja okienkowa\n\n"
			+ "Autor: Artur Sobolewski\n" + "Data:  grudzieñ 2019r.\n";

	public static void main(String[] args) {

		try {
			final GroupOfLaptop currentGroup = new GroupOfLaptop(GroupType.VECTOR, "Grupa testowa");
			new GroupOfLaptopWindowDialog(null, currentGroup);
		} catch (LaptopException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", 0);
		}

	}

	private static String enterGroupName(final Window parent) {
		return JOptionPane.showInputDialog(parent, "Podaj nazwê dla tej grupy: ");
	}

	private static GroupType chooseGroupType(final Window parent, final GroupType current_type) {
		final Object[] possibilities = GroupType.values();
		final GroupType type = (GroupType) JOptionPane.showInputDialog(parent,
				"Wybierz typ kolekcji (Lista, Zbiór)\n i sposób implementacji:", "Zmieñ typ kolekcji", 3, null,
				possibilities, current_type);
		return type;
	}

	public static GroupOfLaptop createNewGroupOfLaptop(final Window parent) {
		final String name = enterGroupName(parent);
		if (name == null || name.equals("")) {
			return null;
		}
		final GroupType type = chooseGroupType(parent, null);
		if (type == null) {
			return null;
		}
		GroupOfLaptop new_group;
		try {
			new_group = new GroupOfLaptop(type, name);
		} catch (LaptopException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "B³¹d", 0);
			return null;
		}
		final GroupOfLaptopWindowDialog dialog = new GroupOfLaptopWindowDialog(parent, new_group);
		return dialog.currentGroup;
	}

	public static void changeGroupOfLaptop(final Window parent, final GroupOfLaptop group) {
		new GroupOfLaptopWindowDialog(parent, group);
	}

	JMenuBar menu = new JMenuBar();
	JMenu m1 = new JMenu("Lista laptopów");
	JMenu m2 = new JMenu("Sortowanie");
	JMenu m3 = new JMenu("W³aœciwoœci");
	JMenu m4 = new JMenu("O programie");

	JLabel tableName = new JLabel("Lista laptopów");

	JMenuItem mNew = new JMenuItem("Dodaj nowy laptop");
	JMenuItem mEdit = new JMenuItem("Edytuj laptop");
	JMenuItem mDelete = new JMenuItem("Usuñ laptop");
	JMenuItem mLoadFromFile = new JMenuItem("Wczytaj laptop z pliku");
	JMenuItem mSaveToFile = new JMenuItem("Zapisz osobê do pliku");

	JMenuItem mSortAlf = new JMenuItem("Sortuj alfabetycznie");
	JMenuItem mSortHDD = new JMenuItem("Sortuj wg. pojemnoœci HDD");
	JMenuItem mSortHz = new JMenuItem("Sortuj wg. szybkoœci procesora");
	JMenuItem mSortMatryca = new JMenuItem("Sortuj wg. rozdzielczoœci ekranu");
	JMenuItem mSortRAM = new JMenuItem("Sortuj wg. pojemnoœci RAM");

	JMenuItem mChangeName = new JMenuItem("Zmieñ nazwê grupy");
	JMenuItem mChangeType = new JMenuItem("Zmieñ typ grupy");

	JMenuItem mAuthor = new JMenuItem("Autor");

	JLabel groupNameLabel = new JLabel("    Nazwa grupy");
	JLabel groupCollectionTypeLabel = new JLabel("Rodzaj kolekcji");

	JTextField groupNameField = new JTextField(10);
	JTextField groupCollectionTypeField = new JTextField(12);

	JButton addButton = new JButton("Dodaj nowy laptop");
	JButton editButton = new JButton("Edytuj laptop");
	JButton deleteButton = new JButton("Usuñ laptop");
	JButton openButton = new JButton("Wczytaj laptop z pliku");
	JButton saveButton = new JButton("Zapisz laptop do pliku");

	ViewLaptopList viewList;

	public GroupOfLaptopWindowDialog(final Window parent, final GroupOfLaptop group) {

		setTitle("Modyfikacja grupy laptopów");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(530, 470);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(new FlowLayout());

		this.currentGroup = group;

		m1.add(mNew);
		m1.add(mEdit);
		m1.add(mDelete);
		m1.add(mLoadFromFile);
		m1.add(mSaveToFile);

		m2.add(mSortAlf);
		m2.add(mSortRAM);
		m2.add(mSortMatryca);
		m2.add(mSortHDD);
		m2.add(mSortHz);

		m3.add(mChangeName);
		m3.add(mChangeType);

		m4.add(mAuthor);

		menu.add(m1);
		menu.add(m2);
		menu.add(m3);
		menu.add(m4);
		setJMenuBar(menu);

		groupNameField.setEditable(false);
		groupCollectionTypeField.setEditable(false);

		addButton.addActionListener(this);
		editButton.addActionListener(this);
		deleteButton.addActionListener(this);
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		mNew.addActionListener(this);
		mEdit.addActionListener(this);
		mDelete.addActionListener(this);
		mLoadFromFile.addActionListener(this);
		mSaveToFile.addActionListener(this);
		mSortAlf.addActionListener(this);
		mSortRAM.addActionListener(this);
		mSortMatryca.addActionListener(this);
		mSortHDD.addActionListener(this);
		mSortHz.addActionListener(this);
		mChangeName.addActionListener(this);
		mChangeType.addActionListener(this);
		mAuthor.addActionListener(this);
		m4.addActionListener(this);

		this.viewList = new ViewLaptopList(this.currentGroup, 500, 300);
		viewList.refreshView();

		JPanel panel = new JPanel();

		panel.add(groupNameLabel);
		panel.add(groupNameField);
		panel.add(groupCollectionTypeLabel);
		panel.add(groupCollectionTypeField);
		panel.add(viewList);
		panel.add(addButton);
		panel.add(editButton);
		panel.add(deleteButton);
		panel.add(openButton);
		panel.add(saveButton);

		this.groupNameField.setText(this.currentGroup.getName());
		this.groupCollectionTypeField.setText(this.currentGroup.getType().toString());

		setContentPane(panel);
		setVisible(true);
	}

	@Override
	public void actionPerformed(final ActionEvent event) {
		Object source = event.getSource();
		try {
			if (source == addButton || source == mNew) {
				final Laptopy newLaptop = LaptopyWindowDialog.createNewLaptop(this);
				if (newLaptop != null) {
					this.currentGroup.add(newLaptop);
				}
			}
			if (source == editButton || source == mEdit) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					final Iterator<Laptopy> iterator = this.currentGroup.iterator();
					while (index-- > 0) {
						iterator.next();
					}
					LaptopyWindowDialog.changeLaptopData(this, iterator.next());
				}

			}
			if (source == this.deleteButton || source == this.mDelete) {
				int index = this.viewList.getSelectedIndex();
				if (index >= 0) {
					final Iterator<Laptopy> iterator = this.currentGroup.iterator();
					while (index-- >= 0) {
						iterator.next();
					}
					iterator.remove();
				}
			}
			if (source == mLoadFromFile || source == openButton) {
				JFileChooser chooser = new JFileChooser(".");
				int returnVal = chooser.showOpenDialog(this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					Laptopy group = Laptopy.readFromFile(chooser.getSelectedFile().getName());
					currentGroup.add(group);
				}
			}
			if (source == mSaveToFile || source == saveButton) {
				int index = viewList.getSelectedIndex();
				if (index >= 0) {
					Iterator<Laptopy> iterator = currentGroup.iterator();
					while (index-- > 0)
						iterator.next();
					Laptopy group = iterator.next();

					JFileChooser chooser = new JFileChooser(".");
					int returnVal = chooser.showSaveDialog(this);
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						Laptopy.printToFile(chooser.getSelectedFile().getName(), group);
					}
				}
			}
			if (source == mChangeName) {
				final String newName = enterGroupName(this);
				if (newName == null) {
					return;
				}
				this.currentGroup.setName(newName);
				this.groupNameField.setText(newName);
			}
			if (source == mChangeType) {
				final GroupType newType = chooseGroupType(null, null);
				if (newType == null) {
					return;
				}
				this.currentGroup.setType(newType);
				this.groupCollectionTypeField.setText(newType.toString());
			}
			if (source == mAuthor) {
				JOptionPane.showMessageDialog(this, GREETING_MESSAGE);
			}
			if (source == mSortAlf) {
				currentGroup.sortMark();
			}
			if (source == mSortHDD) {
				currentGroup.sortHDD();
			}
			if (source == mSortHz) {
				currentGroup.sortCzestotliwoscTaktowania();
				;
			}
			if (source == mSortMatryca) {
				currentGroup.sortMatryca();
			}
			if (source == mSortRAM) {
				currentGroup.sortPamiecRAM();
			}

		} catch (LaptopException e) {
			JOptionPane.showMessageDialog(this, e.getMessage(), "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		viewList.refreshView();
	}

}

class ViewLaptopList extends JScrollPane {
	private static final long serialVersionUID = 1L;

	private GroupOfLaptop list;
	private JTable table;
	private DefaultTableModel tableModel;

	public ViewLaptopList(final GroupOfLaptop list, int width, int height) {
		this.list = list;
		setPreferredSize(new Dimension(width, height));
		setBorder(BorderFactory.createTitledBorder("Lista laptopów:"));

		String[] tableHeader = { "Marka", "Pamiêæ RAM", "Matryca", "HDD", "CPU [GHz]", "Kraj produkcji" };

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

	void refreshView() {
		tableModel.setRowCount(0);
		for (final Laptopy l : this.list) {
			if (l != null) {
				final String[] row = { l.getLaptopKind().toString(), "" + l.getPamiecRAM(), "" + l.getMatryca(),
						"" + l.getHdd(), "" + l.getCzestotliwoscTaktowania(), l.getKrajProdukcji() };
				this.tableModel.addRow(row);
			}
		}
	}

	int getSelectedIndex() {
		int index = table.getSelectedRow();
		if (index < 0) {
			JOptionPane.showMessageDialog(this, "¯aden laptop nie jest zaznaczony.", "B³¹d", JOptionPane.ERROR_MESSAGE);
		}
		return index;
	}

}
