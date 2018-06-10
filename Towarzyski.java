import java.util.TreeSet;
import java.util.LinkedList;
import java.util.Random;

public class Towarzyski extends Agent {
	
	public Towarzyski(int id) {
		super(id);
	}

	public TreeSet<Spotkanie> planujSpotkania(int dzien, int pozostaleDni, Random generator, double prawdSpotkania) {
		TreeSet<Spotkanie> spotkania = new TreeSet<Spotkanie>(new Cmp());
		LinkedList<Agent> przyjaciele = new LinkedList<Agent>();
		if (jestZdrowy()) {
			for (Agent agent : znajomi) {
				przyjaciele.add(agent);
				przyjaciele.addAll(agent.dajZnajomych());
			}
		}
		int liczbaZnajomych = przyjaciele.size();
		while (liczbaZnajomych > 0 && generator.nextDouble() <= prawdSpotkania) {
			spotkania.add(new Spotkanie(
						this,
						przyjaciele.get(generator.nextInt(liczbaZnajomych)),
						generator.nextInt(pozostaleDni + 1) + dzien + 1));
		}
		return spotkania;
	}

	public String toString() {
		String res =  new Integer(dajID() + 1).toString();
		if (jestChory()) {
			res += "*";
		}
		res += " towarzyski";
		return res;
	}
}
