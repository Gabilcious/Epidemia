public class Spotkanie {
	private Agent agentA, agentB;
	private int dzien;

	public Spotkanie(Agent agentA, Agent agentB, int dzien) {
		this.agentA = agentA;
		this.agentB = agentB;
		this.dzien = dzien;
	}

	public void przeprowadz(double prawdZarazenia) {
		if ((agentA.jestChory() || agentB.jestChory()) &&
				Generator.getInstance().nextDouble() <= prawdZarazenia) {
			agentA.zachoruj();
			agentB.zachoruj();
		}
	}

	public int dajDzien() {
		return dzien;
	}
}

