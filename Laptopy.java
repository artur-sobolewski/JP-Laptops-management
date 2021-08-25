import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;

/*
 * Program: Aplikacja okienkowa z GUI, która umo¿liwia zarz¹dzanie
 *          grupami obiektów klasy Laptopy.
 * 
 * Plik: Laptopy.java
 * 
 * Autor: Artur Sobolewski 248913
 * Data:  grudzieñ 2019
 */

/*
 * Typ wyliczeniowy LaptopKinds, która reprezentuje ró¿ne marki jakich mog¹ byæ laptopy.
 * Klasa ta mo¿e byæ ³atwo rozszerzona o now¹ markê, przez nowe wywo³anie konstruktora
 * i podanie jej nazwy w jego argumencie.
 */
enum LaptopKinds {
	UNKNOWN("------"), ACER("Acer"), ASUS("Asus"), LENOVO("Lenovo"), DELL("DELL"), MSI("MSI");

	String markName;

	private LaptopKinds(String mark_Name) {
		markName = mark_Name;
	} // Koniec klasy enum LaptopKinds

	@Override
	public String toString() {
		return markName;
	}
}

/*
 * Klasa LaptopException jest klas¹ rozszerzaj¹c¹ klasê Exception, s³u¿y do
 * zwracania informacji o b³êdach, które mog¹ wyst¹piæ podczas wykonywania
 * operacji na obiektach klasy Laptopy.
 */

class LaptopException extends Exception {
	private static final long serialVersionUID = 1L;

	public LaptopException(String message) {
		super(message);
	}
} // Koniec klasy LaptopException

/*
 * Klasa Laptopy reprezenuje laptopy za pomoc¹ 6 atrybutów: pamiêæ RAM, rozmiar
 * matrycy, pamiêæ dysku twardego, czêstotliwoœæ procesora, kraj produkcji i
 * marka. Paramety te sa objête ograniczeniami wypianymi w i setterach.
 * 
 */

public class Laptopy implements Serializable, Comparable<Laptopy>{

	private static final long serialVersionUID = 1L;

	private int pamiecRAM = 0;
	private float matryca = 0;
	private int hdd = 0;
	private float czestotliwoscTaktowania = 0;
	private String krajProdukcji;
	private LaptopKinds laptop;

	// Konstruktor klasy Laptopy
	public Laptopy(int pamiec_RAM, float matryca, int hdd, float czestotliwosc_taktowania, String kraj_Produkcji)
			throws LaptopException {

		setPamiecRAM(pamiec_RAM);
		setMatryca(matryca);
		setHdd(hdd);
		setCzestotliwoscTaktowania(czestotliwosc_taktowania);
		setKrajProdukcji(kraj_Produkcji);
		laptop = LaptopKinds.UNKNOWN;
	}

	@Override
	public int compareTo(Laptopy laptop) {
	    int result = String.CASE_INSENSITIVE_ORDER.compare(""+this.laptop, ""+laptop.laptop);
	     if (result == 0)
	            result = Integer.compare(this.pamiecRAM, laptop.pamiecRAM);
	            if (result == 0)
	                result = Float.compare(this.matryca, laptop.matryca);
	            	if(result == 0)
	            		result = Integer.compare(this.hdd, laptop.hdd);
	            	 	if(result == 0)
	            			result = Float.compare(this.czestotliwoscTaktowania, laptop.czestotliwoscTaktowania);
	            			 if(result == 0)
	            				result = String.CASE_INSENSITIVE_ORDER.compare(""+this.krajProdukcji, ""+laptop.krajProdukcji);
	        
	    return result;
	}
	
	@Override
	public String toString() {
		return "Laptopy [pamiecRAM=" + pamiecRAM + ", matryca=" + matryca + ", hdd=" + hdd
				+ ", czestotliwoscTaktowania=" + czestotliwoscTaktowania + ", krajProdukcji=" + krajProdukcji
				+ ", laptop=" + laptop + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(czestotliwoscTaktowania);
		result = prime * result + hdd;
		result = prime * result + ((krajProdukcji == null) ? 0 : krajProdukcji.hashCode());
		result = prime * result + ((laptop == null) ? 0 : laptop.hashCode());
		result = prime * result + Float.floatToIntBits(matryca);
		result = prime * result + pamiecRAM;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Laptopy other = (Laptopy) obj;
		if (Float.floatToIntBits(czestotliwoscTaktowania) != Float.floatToIntBits(other.czestotliwoscTaktowania))
			return false;
		if (hdd != other.hdd)
			return false;
		if (krajProdukcji == null) {
			if (other.krajProdukcji != null)
				return false;
		} else if (!krajProdukcji.equals(other.krajProdukcji))
			return false;
		if (laptop != other.laptop)
			return false;
		if (Float.floatToIntBits(matryca) != Float.floatToIntBits(other.matryca))
			return false;
		if (pamiecRAM != other.pamiecRAM)
			return false;
		return true;
	}

	
	public String getKrajProdukcji() {
		return krajProdukcji;
	}

	public void setKrajProdukcji(String kraj_Produkcji) throws LaptopException {
		if (kraj_Produkcji == null || kraj_Produkcji.equals("")) {
			throw new LaptopException("Pole (kraj Produkcji) musi byæ wype³nione.");
		}
		this.krajProdukcji = kraj_Produkcji;
	}

	public int getPamiecRAM() {
		return pamiecRAM;
	}

	public void setPamiecRAM(int pamiecRAM) throws LaptopException {
		if ((pamiecRAM != 0) && (pamiecRAM < 2 || pamiecRAM > 32))
			throw new LaptopException("Pamiêæ RAM musi byæ z przedzia³u [2 - 32]GB.");
		if (pamiecRAM == 0)
			throw new LaptopException("Pole (pamiêæ RAM) musi byæ wype³nione");
		this.pamiecRAM = pamiecRAM;
	}

	public void setPamiecRAM(String pamiec_ram) throws LaptopException {

		if (pamiec_ram == null || pamiec_ram.equals("")) {
			setPamiecRAM(0);
			return;
		}
		try {
			setPamiecRAM(Integer.parseInt(pamiec_ram));
		} catch (LaptopException e) {
			throw new LaptopException("Pamiêæ RAM musi byæ liczb¹ ca³kowit¹.");
		}
	}

	public float getMatryca() {
		return matryca;
	}

	public void setMatryca(float matryca) throws LaptopException {
		if ((matryca != 0) && (matryca < 10 || matryca > 19))
			throw new LaptopException("Pole (matryca) musi byæ wype³nione wartoœci¹ z przedzia³u [10 - 19]cal.");
		if (matryca == 0)
			throw new LaptopException("Pole (matryca) musi byæ wype³nione");
		this.matryca = matryca;
	}

	public void setMatryca(String matryca) throws LaptopException {

		if (matryca == null || matryca.equals("")) {
			setMatryca(0);
			return;
		}
		try {
			setMatryca(Float.parseFloat(matryca));
		} catch (LaptopException e) {
			throw new LaptopException("Rozmiar matrycy musi byæ liczb¹ rzeczywist¹.");
		}
	}

	public int getHdd() {
		return hdd;
	}

	public void setHdd(int hdd) throws LaptopException {
		if (hdd == 0)
			throw new LaptopException("Pole (HDD) musi byæ wype³nione");
		if ((hdd != 0) && (hdd < 100 || hdd > 3000))
			throw new LaptopException("Pole (HDD) musi byæ wype³nione wartoœci¹ z przedzia³u [100 - 3000]GB.");
		this.hdd = hdd;
	}

	public void setHdd(String hdd) throws LaptopException {

		if (hdd == null || hdd.equals("")) {
			setHdd(0);
			return;
		}
		try {
			setHdd(Integer.parseInt(hdd));
		} catch (LaptopException e) {
			throw new LaptopException("Pamiêæ dysku twardego musi byæ liczb¹ ca³kowit¹.");
		}
	}

	public float getCzestotliwoscTaktowania() {
		return czestotliwoscTaktowania;
	}

	public void setCzestotliwoscTaktowania(float czestotliwoscTaktowania) throws LaptopException {
		if (czestotliwoscTaktowania == 0)
			throw new LaptopException("Pole (czêstotliwoœæ taktowania) musi byæ wype³nione.");
		if ((czestotliwoscTaktowania != 0) && (czestotliwoscTaktowania < 2 || czestotliwoscTaktowania > 6))
			throw new LaptopException(
					"Pole (czêstotliwoœæ taktowania) musi byæ wype³nione wartoœci¹ z przedzia³u [2.0 - 6.0]GHz.");
		this.czestotliwoscTaktowania = czestotliwoscTaktowania;
	}

	public void setCzestotliwoscTaktowania(String czestotliwosc_taktowania) throws LaptopException {

		if (czestotliwosc_taktowania == null || czestotliwosc_taktowania.equals("")) {
			setCzestotliwoscTaktowania(0);
			return;
		}
		try {
			setCzestotliwoscTaktowania((Float.parseFloat(czestotliwosc_taktowania)));
		} catch (LaptopException e) {
			throw new LaptopException("Czêstotliwoœæ procesora musi byæ liczb¹ rzeczywist¹.");
		}
	}

	public LaptopKinds getLaptopKind() {
		return laptop;
	}

	public void setLaptopKind(LaptopKinds laptop) {
		this.laptop = laptop;
	}

	public void setLaptopKind(String laptop) throws LaptopException {
		if (laptop == null || laptop.equals("")) {
			this.laptop = LaptopKinds.UNKNOWN;
			return;
		}
		for (LaptopKinds marka : LaptopKinds.values()) {
			if (marka.markName.equals(laptop)) {
				this.laptop = marka;
				return;
			}
		}
		throw new LaptopException("Nie ma takiej marki.");
	}

	public static void printToFile(PrintWriter writer, Laptopy laptop) {
		writer.println(laptop.pamiecRAM + "#" + laptop.matryca + "#" + laptop.hdd + "#" + laptop.czestotliwoscTaktowania
				+ "#" + laptop.krajProdukcji + "#" + laptop.laptop);
	}

	// metoda s³u¿¹ca do zapisu obiektu w pliku tekstowym
	public static void printToFile(String file_name, Laptopy laptop) throws LaptopException {
		try (PrintWriter writer = new PrintWriter(file_name)) {
			printToFile(writer, laptop);
		} catch (FileNotFoundException e) {
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		}
	}

	// metoda s³u¿¹ca do wczytania obiektu z pliku tekstowego
	public static Laptopy readFromFile(BufferedReader reader) throws LaptopException {
		try {
			String line = reader.readLine();
			String[] txt = line.split("#");
			int ram = Integer.parseInt(txt[0]);
			float matryca = Float.parseFloat(txt[1]);
			int hdd = Integer.parseInt(txt[2]);
			float czestotliwosc = Float.parseFloat(txt[3]);
			Laptopy laptop = new Laptopy(ram, matryca, hdd, czestotliwosc, txt[4]);
			laptop.setLaptopKind(txt[5]);
			return laptop;
		} catch (IOException e) {
			throw new LaptopException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}

	// Metoda s³u¿¹ca do wczytania pliku tekstowego po jego nazwie.
	// Korzysta ona z metody readFromFile(BufferedReader reader).
	public static Laptopy readFromFile(String file_name) throws LaptopException {
		try (BufferedReader reader = new BufferedReader(new FileReader(new File(file_name)))) {
			return Laptopy.readFromFile(reader);
		} catch (FileNotFoundException e) {
			throw new LaptopException("Nie odnaleziono pliku " + file_name);
		} catch (IOException e) {
			throw new LaptopException("Wyst¹pi³ b³¹d podczas odczytu danych z pliku.");
		}
	}

	// metoda s³u¿¹ca do zapisu pliku binarnie
	public static void printToFileBin(String file_name, Laptopy laptop) throws LaptopException {

		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(file_name))) {
			output.writeObject(laptop);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// metoda s³u¿¹ca do wczytania pliku binarnego
	public static Laptopy readFromFileBin(String file_name) throws LaptopException {

		try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(file_name))) {
			Laptopy readLaptop = (Laptopy) input.readObject();
			return readLaptop;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
