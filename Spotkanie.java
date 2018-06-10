public class Spotkanie {
	private Agent agentA, agentB;
	private int dzien;

	public Spotkanie(Agent agentA, Agent agentB, int dzien) {
		this.agentA = agentA;
		this.agentB = agentB;
		this.dzien = dzien;
	}

	public void przeprowadz() {
		if (agentA.jestChory() || agentB.jestChory()) {
			agentA.zachoruj();
			agentB.zachoruj();
		}
	}

	public int dajDzien() {
		return dzien;
	}
}

