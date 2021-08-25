import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/* Program: Aplikacja okienkowa z GUI, która umo¿liwia zarz¹dzanie
*          grupami obiektów klasy Laptopy.
*    Plik: GroupOfLaptop.java
*
*   Autor: Artur Sobolewski 248913
*    Data: grudzieñ 2019 r.
*    
*    Klasa enumerate GroupType zawiera informacje o grupach mo¿liwych do utworzenia,
*    oraz metodê s³u¿¹c¹ do stworzenia kolekcji danego typu.
*/

enum GroupType {
	VECTOR("Lista   (klasa Vector)"),
	ARRAY_LIST("Lista   (klasa ArrayList)"),
	LINKED_LIST("Lista   (klasa LinkedList)"),
	HASH_SET("Zbiór   (klasa HashSet)"),
	TREE_SET("Zbiór   (klasa TreeSet)");

	String typeName;

	private GroupType(String type_name) {
		typeName = type_name;
	}


	@Override
	public String toString() {
		return typeName;
	}


	public static GroupType find(String type_name){
		for(GroupType type : values()){
			if (type.typeName.equals(type_name)){
				return type;
			}
		}
		return null;
	}
	public Collection<Laptopy> createCollection() throws LaptopException {
		switch (this) {
		case VECTOR:      return new Vector<Laptopy>();
		case ARRAY_LIST:  return new ArrayList<Laptopy>();
		case HASH_SET:    return new HashSet<Laptopy>();
		case LINKED_LIST: return new LinkedList<Laptopy>();
		case TREE_SET:    return new TreeSet<Laptopy>();
		default:          throw new LaptopException("Podany typ kolekcji nie zosta³ zaimplementowany.");
		}
	}
}
/*
 * Klasa GroupOfLaptop zawiera pola okreœlaj¹ce w³aœciwoœci kolekcji oraz implementuje metody
 * do wykonywania operacji na kolekcjach.
 */
public class GroupOfLaptop implements Iterable<Laptopy>, Serializable{

	private static final long serialVersionUID = 1L;

	private String name;
	private GroupType type;
	private Collection<Laptopy> collection;

	public GroupOfLaptop(GroupType type, String name) throws LaptopException {
		setName(name);
		if (type==null){
			throw new LaptopException("Nieprawid³owy typ kolekcji.");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	public GroupOfLaptop(String type_name, String name) throws LaptopException {
		setName(name);
		GroupType type = GroupType.find(type_name);
		if (type==null){
			throw new LaptopException("Nieprawid³owy typ kolekcji.");
		}
		this.type = type;
		collection = this.type.createCollection();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) throws LaptopException {
		if ((name == null) || name.equals(""))
			throw new LaptopException("Nazwa grupy musi byæ okreœlona.");
		this.name = name;
	}
	
	public GroupType getType() {
		return type;
	}
	
	public void setType(GroupType type) throws LaptopException {
		if (type == null) {
			throw new LaptopException("Typ kolekcji musi byæ okreœlny.");
		}
		if (this.type == type)
			return;
		Collection<Laptopy> oldCollection = collection;
		collection = type.createCollection();
		this.type = type;
		for (Laptopy laptop : oldCollection)
			collection.add(laptop);
	}
	
	public void setType(String type_name) throws LaptopException {
		for(GroupType type : GroupType.values()){
			if (type.toString().equals(type_name)) {
				setType(type);
				return;
			}
		}
		throw new LaptopException("Nie ma takiego typu kolekcji.");
	}
	
	public boolean add(Laptopy e) {
		return collection.add(e);
	}

	
	public Iterator<Laptopy> iterator(){
		return collection.iterator();
	}

	public int size() {
		return collection.size();
	}
	
	public void sortMark() throws LaptopException {
		if (type==GroupType.HASH_SET|| type==GroupType.TREE_SET ){
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Laptopy>)collection);
	}
	
	public void sortPamiecRAM() throws LaptopException {
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Laptopy>) collection, new Comparator<Laptopy>() {

			@Override
			public int compare(Laptopy l1, Laptopy l2) {
				if (l1.getPamiecRAM() < l2.getPamiecRAM())
					return -1;
				if (l1.getPamiecRAM() > l2.getPamiecRAM())
					return 1;
				return 0;
			}
		});
	}
	public void sortMatryca() throws LaptopException {
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Laptopy>) collection, new Comparator<Laptopy>() {

			@Override
			public int compare(Laptopy l1, Laptopy l2) {
				return Float.toString(l1.getMatryca()).compareTo(Float.toString(l2.getMatryca()));
			}
		});
	}
	public void sortHDD() throws LaptopException {
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Laptopy>) collection, new Comparator<Laptopy>() {

			@Override
			public int compare(Laptopy l1, Laptopy l2) {
				if (l1.getHdd() < l2.getHdd())
					return -1;
				if (l1.getHdd() > l2.getHdd())
					return 1;
				return 0;
			}
		});
	}
	public void sortCzestotliwoscTaktowania() throws LaptopException {
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}
		Collections.sort((List<Laptopy>) collection, new Comparator<Laptopy>() {

			@Override
			public int compare(Laptopy l1, Laptopy l2) {
				return Float.toString(l1.getCzestotliwoscTaktowania()).compareTo(Float.toString(l2.getCzestotliwoscTaktowania()));
			}
		});
	}
	public void sortKrajPochodzenia() throws LaptopException {
		if (type == GroupType.HASH_SET || type == GroupType.TREE_SET) {
			throw new LaptopException("Kolekcje typu SET nie mog¹ byæ sortowane.");
		}

		Collections.sort((List<Laptopy>) collection, new Comparator<Laptopy>() {

			@Override
			public int compare(Laptopy l1, Laptopy l2) {
				return l1.getKrajProdukcji().toString().compareTo(l2.getKrajProdukcji().toString());
			}

		});
	}
	
	
	@Override
	public String toString() {
		return name + "  [" + type + "]";
	}
	
	
	public static void printToFile(PrintWriter writer, GroupOfLaptop group) {
		writer.println(group.getName());
		writer.println(group.getType());
		for (Laptopy laptop : group.collection)
			Laptopy.printToFile(writer, laptop);
	}


	public static void printToFile(String file_name, GroupOfLaptop group) throws LaptopException {
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, group);
		} catch (FileNotFoundException e){
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		}
	}


	public static GroupOfLaptop readFromFile(BufferedReader reader) throws LaptopException{
		try {
			String group_name = reader.readLine();
			String type_name = reader.readLine();
			GroupOfLaptop groupOfPeople = new GroupOfLaptop(type_name, group_name);

			Laptopy person;
			while((person = Laptopy.readFromFile(reader)) != null)
				groupOfPeople.collection.add(person);
			return groupOfPeople;
		} catch(IOException e){
			throw new LaptopException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}
	
	public static GroupOfLaptop readFromFile(String file_name) throws LaptopException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return GroupOfLaptop.readFromFile(reader);
		} catch (FileNotFoundException e){
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		} catch(IOException e){
			throw new LaptopException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}
	
	public static GroupOfLaptop createGroupUnion(GroupOfLaptop g1,GroupOfLaptop g2) throws LaptopException {
		String name = "(" + g1.name + " OR " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfLaptop group = new GroupOfLaptop(type, name);
		group.collection.addAll(g1.collection);
		group.collection.addAll(g2.collection);
		return group;
	}
	
	public static GroupOfLaptop createGroupIntersection(GroupOfLaptop g1,GroupOfLaptop g2) throws LaptopException {
		String name = "(" + g1.name + " AND " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfLaptop group = new GroupOfLaptop(type, name);

		  for(Laptopy laptop1 : g1.collection)
			{
				for(Laptopy laptop2 : g2.collection)
				{
					if(laptop2.compareTo(laptop1) == 0) 
					{
						group.add(laptop1);
					}	
				}
			}
		 
		
		return group;

	}
	public static GroupOfLaptop createGroupDifference(GroupOfLaptop g1,GroupOfLaptop g2) throws LaptopException {
		String name = "(" + g1.name + " SUB " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}
		GroupOfLaptop group = new GroupOfLaptop(type, name);
		group.collection.addAll(g1.collection);
		Laptopy laptop1;
		
		Iterator<Laptopy> iterator = group.collection.iterator();
		
			for(Laptopy laptop2 : g2.collection)
			{
				while(iterator.hasNext())
				{
					laptop1 = iterator.next();
					if(laptop2.compareTo(laptop1) == 0) 
					{
						iterator.remove();
					}	
				}
				
			}	
		
		return group;
	}


	public static GroupOfLaptop createGroupSymmetricDiff(GroupOfLaptop g1,GroupOfLaptop g2) throws LaptopException {
		String name = "(" + g1.name + " XOR " + g2.name +")";
		GroupType type;
		if (g2.collection instanceof Set && !(g1.collection instanceof Set) ){
			type = g2.type;
		} else {
			type = g1.type;
		}

		GroupOfLaptop group = new GroupOfLaptop(type, name);
		group.collection.addAll(g1.collection);
		group.collection.addAll(g2.collection);
		Laptopy laptop1;
		Iterator<Laptopy> iterator = group.collection.iterator();
		
		for(Laptopy laptop2 : g2.collection)
		{
			while(iterator.hasNext())
			{
				laptop1 = iterator.next();
				if(laptop2.compareTo(laptop1) == 0) 
				{
					iterator.remove();
				}	
			}
			
		}	
		return group;
	}
	
}
