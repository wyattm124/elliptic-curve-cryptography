package ecc;

public class Pair<K, V> {

    private K value0;
    private V value1;

    public static <K, V> Pair<K, V> createPair(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }

    public Pair(K element0, V element1) {
        this.value0 = element0;
        this.value1 = element1;
    }
    public boolean equals(Pair<K, V> P){
      return ((this.getValue0()).equals(P.getValue0()) &&
              (this.getValue1()).equals(P.getValue1()));
    }

    public K getValue0() {return this.value0;}

    public V getValue1() {return this.value1;}

	public void setValue0(K item){this.value0 = item;}

	public void setValue1(V item){this.value1 = item;}

	public String toString() {
		return "( "+ value0.toString() + " , " + value1.toString() + " )";
	}
}
