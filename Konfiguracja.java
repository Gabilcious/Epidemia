import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.nio.charset.Charset;
import java.io.FileNotFoundException;
import java.lang.NullPointerException;
import java.lang.NumberFormatException;




public class Konfiguracja {

	Integer seed;
	Integer liczbaAgentow;
	Integer liczbaDni;
	Integer srZnajomych;
	Double smiertelnosc;
	Double prawdTowarzyski;
	Double prawdSpotkania;
	Double prawdZarazenia;
	Double prawdWyzdrowienia;
	String plikZRaportem;

	static int pobierzInteger(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		return Konfiguracja.pobierzInteger(
			domyslneWartosci, wartosciSymulacji, klucz,
			Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	static Integer pobierzInteger(
			Properties domyslneWartosci, Properties wartosciSymulacji,
			String klucz, int odWartosci, int doWartosci) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
				domyslneWartosci, wartosciSymulacji, klucz);
		Integer wartosc = null;
		try {
			wartosc = Integer.parseInt(wartosci.getProperty(klucz));
			if (wartosc < odWartosci || wartosc > doWartosci) {
				System.out.println(
					String.format(
						"Zla wartość klucza %s (%d - %d)",
						klucz, odWartosci, doWartosci));
				System.exit(0);
			}
			return wartosc;
		} catch (NumberFormatException ex) {
			System.out.println("Brak klucza " + klucz);
			System.exit(0);
		}
		return wartosc;
	}

	static Double pobierzDouble(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
				domyslneWartosci, wartosciSymulacji, klucz);
		Double wartosc = null;
		try {
			wartosc = Double.parseDouble(wartosci.getProperty(klucz));
			if (wartosc < 0 || wartosc > 1) {
				System.out.println(
					String.format(
						"Zla wartość klucza %s (0 - 1)", klucz));
				System.exit(0);
			}
			return wartosc;
		} catch (NullPointerException ex) {
			System.out.println("Brak klucza " + klucz);
			System.exit(0);
		}
		return wartosc;
	}

	static String pobierzNapis(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
				domyslneWartosci, wartosciSymulacji, klucz);
		String wartosc = wartosci.getProperty(klucz);
		if (wartosc == null) {
			System.out.println("Brak klucza " + klucz);
			System.exit(0);
		}
		if (wartosc.isEmpty()) {
			System.out.println("Pusty napis dla klucza " + klucz);
			System.exit(0);
		}
		return wartosc;
	}
				

	static Properties znajdzKlucz(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		if (wartosciSymulacji.containsKey(klucz)) {
			return wartosciSymulacji;
		}
		return domyslneWartosci;
	}
		
	static Konfiguracja zPlikow(String nazwaPlikuDomyslnego, String nazwaPlikuSymulacji) {
		Properties domyslneWartosci = Konfiguracja.dajWartosci(nazwaPlikuDomyslnego);
		Properties wartosciSymulacji = Konfiguracja.dajWartosci(nazwaPlikuSymulacji);
		Konfiguracja konf = new Konfiguracja();
		konf.seed = Konfiguracja.pobierzInteger(
			domyslneWartosci, wartosciSymulacji, "seed");
		konf.liczbaAgentow = Konfiguracja.pobierzInteger(
			domyslneWartosci, wartosciSymulacji, "liczbaAgentów", 1, 1000000);
		konf.prawdTowarzyski = Konfiguracja.pobierzDouble(
			domyslneWartosci, wartosciSymulacji, "prawdTowarzyski");
		konf.prawdSpotkania = Konfiguracja.pobierzDouble(
			domyslneWartosci, wartosciSymulacji, "prawdSpotkania");
		konf.prawdZarazenia = Konfiguracja.pobierzDouble(
			domyslneWartosci, wartosciSymulacji, "prawdZarażenia");
		konf.prawdWyzdrowienia = Konfiguracja.pobierzDouble(
			domyslneWartosci, wartosciSymulacji, "prawdWyzdrowienia");
		konf.smiertelnosc = Konfiguracja.pobierzDouble(
			domyslneWartosci, wartosciSymulacji, "śmiertelność");
		konf.liczbaDni = Konfiguracja.pobierzInteger(
			domyslneWartosci, wartosciSymulacji, "liczbaDni", 1, 1000);
		konf.srZnajomych = Konfiguracja.pobierzInteger(
			domyslneWartosci, wartosciSymulacji, "liczbaDni", 0,
			konf.liczbaAgentow);
		konf.plikZRaportem = Konfiguracja.pobierzNapis(
			domyslneWartosci, wartosciSymulacji, "plikZRaportem");
		return konf;
	}

	static Properties dajWartosci(String nazwaPliku) {
		InputStream wejscie = null;
		try {
			wejscie = new FileInputStream(nazwaPliku);
		} catch (FileNotFoundException ex) {
			System.out.println("Brak pliku " + nazwaPliku);
			System.exit(0);
		}
		String rozszerzenie = Konfiguracja.dajRozszerzenie(nazwaPliku);
		Properties wartosci = new Properties();

		try {
			if (rozszerzenie.equals("properties")) {
				wartosci.load(new InputStreamReader(wejscie, Charset.forName("UTF-8")));
			} else if (rozszerzenie.equals("xml")) {
				wartosci.loadFromXML(wejscie);
			}
		} catch (IOException ex) {}
		return wartosci;
	}

	static String dajRozszerzenie(String nazwaPliku) {
		String extension = "";
		int i = nazwaPliku.lastIndexOf('.');
		if (i > 0) {
			extension = nazwaPliku.substring(i+1);
		}
		return extension;
	}
}
