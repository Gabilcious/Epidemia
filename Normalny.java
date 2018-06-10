import java.util.TreeSet;
import java.util.LinkedList;

public class Normalny extends Agent {
	
	public Normalny(int id) {
		super(id);
	}
	
	public TreeSet<Spotkanie> planujSpotkania(
			int dzien,
			int pozostaleDni,
			double prawdSpotkania) {
		TreeSet<Spotkanie> spotkania = new TreeSet<Spotkanie>(new Cmp());
		double checSpotkania = prawdSpotkania; 
		if (jestChory()) {
			checSpotkania /= 2;
		}
		int liczbaZnajomych = znajomi.size();
		if (liczbaZnajomych == 0) {
			return spotkania;
		}

		while (Generator.getInstance().nextDouble() <= prawdSpotkania) {
			spotkania.add(new Spotkanie(
				this,
				znajomi.get(Generator.getInstance().nextInt(liczbaZnajomych)),
				Generator.getInstance().nextInt(pozostaleDni + 1) + dzien + 1));
		}
		return spotkania;
	}

	public String toString() {
		String res =  new Integer(dajID() + 1).toString();
		if (jestChory()) {
			res += "*";
		}
		res += " zwykły";
		return res;
	}
}
