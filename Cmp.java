import java.util.Comparator;

public class Cmp implements Comparator<Spotkanie>{

	public int compare(Spotkanie s1, Spotkanie s2){
		if (s1.dajDzien() < s2.dajDzien())
			return -1;
		return 1;
	}
}

