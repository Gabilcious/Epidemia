import java.util.Scanner;
import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Random;
import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class Symulacja {
	private int
		seed,
		liczbaAgentow,
		liczbaDni,
		srZnajomych;
	private double
		prawdTowarzyski,
		prawdSpotkania,
		prawdZarazenia,
		prawdWyzdrowienia,
		smiertelnosc;
	private Agent[] agenci;
	private Random generator;
	private TreeSet <Spotkanie> spotkania;
	private PrintWriter zapis;

	public Symulacja (
			int seed, int liczbaAgentow, int liczbaDni, int srZnajomych,
			double pTowarzyski, double pSpotkania, double pZarazenia,
			double pWyzdrowienia, double smiertelnosc, String nazwaPliku) throws FileNotFoundException {
		this.agenci = new Agent[liczbaAgentow];
		this.generator = new Random(seed);
		this.spotkania = new TreeSet<Spotkanie>(new Cmp());
		this.seed = seed;
		this.liczbaAgentow = liczbaAgentow;
		this.liczbaDni = liczbaDni;
		this.srZnajomych = srZnajomych;
		this.prawdTowarzyski = pTowarzyski;
		this.prawdSpotkania = pSpotkania;
		this.prawdZarazenia = pZarazenia;
		this.prawdWyzdrowienia = pWyzdrowienia;
		this.smiertelnosc = smiertelnosc;
		try {
			this.zapis = new PrintWriter(nazwaPliku);
		}
		catch (FileNotFoundException ex)
		{

		}

		zapis.println("# twoje wyniki powinny zawierać te komentarze");
		zapis.println("seed=" + this.seed);
		zapis.println("liczbaAgentów=" + this.liczbaAgentow);
		zapis.println("prawdTowarzyski=" + this.prawdTowarzyski);
		zapis.println("prawdSpotkania=" + this.prawdSpotkania);
		zapis.println("prawdZarażenia=" + this.prawdZarazenia);
		zapis.println("prawdWyzdrowienia=" + this.prawdWyzdrowienia);
		zapis.println("śmiertelność=" + this.smiertelnosc);
		zapis.println("liczbaDni=" + this.liczbaDni);
		zapis.println("śrZnajomych=" + this.srZnajomych);
		zapis.println();
	}

	private void przygotujDane() {
		// Losuje towarzyskosc
		for (int i = 0; i < liczbaAgentow; i++) {
			if (generator.nextDouble() <= prawdTowarzyski) {
				agenci[i] = new Towarzyski(i);
			}
			else {
				agenci[i] = new Normalny(i);
			}
		}

		// Losuje graf
		int liczbaKrawedzi = srZnajomych * liczbaAgentow / 2;
		for (int i = 1; i <= liczbaKrawedzi; i++) {
			Agent agentA = agenci[generator.nextInt(liczbaAgentow)];
			Agent agentB = agenci[generator.nextInt(liczbaAgentow)];
			while (agentA.czyZna(agentB)) {
				agentA = agenci[generator.nextInt(liczbaAgentow)];
				agentB = agenci[generator.nextInt(liczbaAgentow)];
			}
			agentA.dodajZnajomego(agentB);
			agentB.dodajZnajomego(agentA);
		}
		
		// Losuj zakazonego
		agenci[generator.nextInt(liczbaAgentow)].zachoruj();

		zapis.println("# agenci jako: id typ lub id* typ dla chorego");
		for (Agent agent : agenci) {
			zapis.println(agent);
		}
		zapis.println();

		zapis.println("# graf");
		for (Agent agent : agenci) {
			zapis.print(agent.dajID() + 1);
			LinkedList<Agent> znajomi = agent.dajZnajomych();
			for (Agent sasiad : znajomi) {
				zapis.print(" " + (sasiad.dajID() + 1));
			}
			zapis.println();
		}
		zapis.println();
	}
	
	private void przeprowadzDzien(int nrDnia) {
		// Sprawdz czy ktos umrze / ozdrowieje
		for (Agent agent : agenci) {
			agent.obudzSie(generator, prawdWyzdrowienia, smiertelnosc);
		}

		// Planuj spotkania
		for (Agent agent : agenci) {
			spotkania.addAll(agent.planujSpotkania(nrDnia, liczbaDni-nrDnia, generator, prawdSpotkania));
		}
		
		// Spotkaj sie
		while (!spotkania.isEmpty() && spotkania.first().dajDzien() == nrDnia) {
			spotkania.pollFirst().przeprowadz();
		}
		
		int zdrowi = 0;
		int chorzy = 0;
		int uodpornieni = 0;
		for (Agent agent : agenci) {
			if (agent.jestZdrowy()) {
				zdrowi++;
			}
			else if (agent.jestChory()) {
				chorzy++;
			}
			else if (agent.jestUodporniony()) {
				uodpornieni++;
			}
		}
		zapis.println(zdrowi + " " + chorzy + " " + uodpornieni);
	}

	public void przeprowadzSymulacje() {
		przygotujDane();
		zapis.println("# liczność w kolejnych dniach");
		for (int dzien = 1; dzien <= liczbaDni; dzien++) {
			przeprowadzDzien(dzien);
		}
		zapis.close();
	}

	public static void main(String[] args) {
		try {
			Symulacja symulacja = new Symulacja(
				110090, // seed
				100,	// liczba agentow
				50,		// liczba dni
				15,		// sr liczba znajomych
				0.05,	// procent towarzyskich
				0.5,	// chec spotkan
				0.1,	// zarazliwosc
				0.01,	// ozdrowienia
				0.01,	// smiertelnosc
				"plik.txt");
			symulacja.przeprowadzSymulacje();
		}
		catch (FileNotFoundException ex) {
		}
	}
}
