import java.util.LinkedList;
import java.util.TreeSet;
import java.util.Random;

abstract public class Agent {
	private int id;
	private Stan stan;
	protected LinkedList<Agent> znajomi;

	public Agent(int id) {
		this.znajomi = new LinkedList<Agent>();
		this.id = id;
		this.stan = Stan.ZDROWY;
	}
	
	public void obudzSie(Random generator, double prawdWyzdrowienia, double smiertelnosc) {
		if (stan != Stan.CHORY) {
			return;
		}

		if (generator.nextDouble() <= smiertelnosc) {
			zgin();
			return;
		}
		if (generator.nextDouble() <= prawdWyzdrowienia) {
			wyzdrowiej();
		}
	}

	abstract public TreeSet<Spotkanie> planujSpotkania(int dzien, int pozostaleDni, Random generator, double prawdSpotkania);

	public void zachoruj() {
		if (stan == Stan.ZDROWY) {
			stan = Stan.CHORY;
		}
	}

	private void wyzdrowiej() {
		stan = Stan.UODPORNIONY;
	}

	private void zgin() {
		for (Agent agent : znajomi) {
			agent.usunZnajomego(this);
		}
		znajomi.clear();
		stan = Stan.MARTWY;
	}

	public Boolean jestChory() {
		return (stan == Stan.CHORY);
	}

	public Boolean jestZdrowy() {
		return (stan == Stan.ZDROWY);
	}

	public Boolean jestUodporniony() {
		return (stan == Stan.UODPORNIONY);
	}

	public int dajID() {
		return id;
	}

	public LinkedList<Agent> dajZnajomych() {
		return znajomi;
	}

	public void dodajZnajomego(Agent agent) {
		znajomi.add(agent);
	}

	public void usunZnajomego(Agent agent) {
		znajomi.remove(agent);
	}

	public Boolean czyZna(Agent agent) {
		return (agent == this || znajomi.contains(agent));
	}

	public Stan dajStan() {
		return stan;
	}

	abstract public String toString();
}
