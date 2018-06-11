import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.nio.charset.Charset;
import java.io.FileNotFoundException;

public class Konfiguracja {
	public int
		seed,
		liczbaAgentow,
		liczbaDni,
		srZnajomych;
	public Double
		smiertelnosc,
		prawdTowarzyski,
		prawdSpotkania,
		prawdZarazenia,
		prawdWyzdrowienia;
	public String plikZRaportem;

	private static int pobierzInteger(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		return Konfiguracja.pobierzInteger(
			domyslneWartosci,
			wartosciSymulacji,
			klucz,
			Integer.MIN_VALUE,
			Integer.MAX_VALUE
		);
	}

	private static Integer pobierzInteger(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz,
			int odWartosci,
			int doWartosci) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
			domyslneWartosci,
			wartosciSymulacji,
			klucz
		);
		String surowaWartosc = wartosci.getProperty(klucz);
		Integer wartosc = null;
		try {
			wartosc = Integer.parseInt(surowaWartosc);
			if (wartosc < odWartosci || wartosc > doWartosci) {
				System.out.println(
					String.format(
						"Niedozwolona wartość %d dla klucza %s",
						wartosc,
						klucz
				));
				System.exit(0);
			}
			return wartosc;
		}
		catch (NumberFormatException ex) {
			if (surowaWartosc == null) {
				System.out.println("Brak wartości dla klucza " + klucz);
			}
			else {
				System.out.println(
					String.format(
						"Niedozwolona wartość \"%s\" dla klucza %s",
						surowaWartosc,
						klucz
				));
			}
			System.exit(0);
		}
		return wartosc;
	}

	private static Double pobierzDouble(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
			domyslneWartosci,
			wartosciSymulacji,
			klucz
		);
		String surowaWartosc = wartosci.getProperty(klucz);
		Double wartosc = null;
		try {
			wartosc = Double.parseDouble(surowaWartosc);
			if (wartosc < 0 || wartosc > 1) {
				System.out.println(
					String.format(
						"Niedozwolona wartość %f dla klucza %s",
						wartosc,
						klucz
				));
				System.exit(0);
			}
			return wartosc;
		}
		catch (NumberFormatException | NullPointerException ex) {
			if (surowaWartosc == null) {
				System.out.println("Brak wartości dla klucza " + klucz);
			}
			else {
				System.out.println(
					String.format(
						"Niedozwolona wartość \"%s\" dla klucza %s",
						surowaWartosc,
						klucz
				));
			}
			System.exit(0);
		}
		return wartosc;
	}

	private static String pobierzNapis(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		Properties wartosci = Konfiguracja.znajdzKlucz(
			domyslneWartosci,
			wartosciSymulacji,
			klucz
		);
		String wartosc = wartosci.getProperty(klucz);
		if (wartosc == null || wartosc.isEmpty()) {
			System.out.println("Brak wartości dla klucza " + klucz);
			System.exit(0);
		}
		return wartosc;
	}
				

	private static Properties znajdzKlucz(
			Properties domyslneWartosci,
			Properties wartosciSymulacji,
			String klucz) {
		if (wartosciSymulacji.containsKey(klucz)) {
			return wartosciSymulacji;
		}
		return domyslneWartosci;
	}
		
	public static Konfiguracja zPlikow(
			String nazwaPlikuDomyslnego,
			String nazwaPlikuSymulacji) {
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
			domyslneWartosci, wartosciSymulacji, "śrZnajomych", 0, konf.liczbaAgentow - 1);
		konf.plikZRaportem = Konfiguracja.pobierzNapis(
			domyslneWartosci, wartosciSymulacji, "plikZRaportem");
		return konf;
	}

	private static Properties dajWartosci(String nazwaPliku) {
		InputStream wejscie = null;
		try {
			wejscie = new FileInputStream(nazwaPliku);
		}
		catch (FileNotFoundException ex) {
			System.out.println("Brak pliku " + nazwaPliku);
			System.exit(0);
		}
		String rozszerzenie = Konfiguracja.dajRozszerzenie(nazwaPliku);
		Properties wartosci = new Properties();

		try {
			if (rozszerzenie.equals("properties")) {
				wartosci.load(
					new InputStreamReader(
						wejscie,
						Charset.forName("UTF-8")
				));
			}
			else if (rozszerzenie.equals("xml")) {
				wartosci.loadFromXML(wejscie);
			}
		}
		catch (IOException ex) {
			if (rozszerzenie.equals("properties")) {
				System.out.println("default.properties nie jest tekstowy");
				System.exit(0);
			}
			else if (rozszerzenie.equals("xml")) {
				System.out.println("simulation-conf.xml nie jest XML");
				System.exit(0);
			}
		}
		return wartosci;
	}

	private static String dajRozszerzenie(String nazwaPliku) {
		String extension = "";
		int i = nazwaPliku.lastIndexOf('.');
		if (i > 0) {
			extension = nazwaPliku.substring(i+1);
		}
		return extension;
	}
}
