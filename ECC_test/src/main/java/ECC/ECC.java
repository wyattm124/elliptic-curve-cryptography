package ecc;

//import Pairs;
import java.lang.Math;
import java.util.Map;
import java.util.HashMap;
//import Pair;

//working over Z/17 officially.
//quad residues = {0, 1, 2, 4, 8, 9, 13, 15, 16}
// 0 ~ 0; 1~1,16; 2 ~6,11; 4~2,15; 8~5,12; 9~3,14; 13~8,9; 15~7,10; 16~4, 13
public class ECC {}
	Pair<Integer, Integer> coeff;
	Pair<Integer, Integer> Generator;
	Pair<Integer, Integer> aG;
	Pair<Integer, Integer> bG;
	Pair<Integer, Integer> pvtkey;
	int myprivates;
	int orderOfGen;
	int fieldsize = 17;
	HashMap<String, Pair<Integer, Integer>> dictionary = new HashMap<String, Pair<Integer, Integer>>();
	HashMap<String, String> yranoitcid = new HashMap<String, String>();

	public ECC(Pair<Integer, Integer> coefficient,Pair<Integer, Integer> G, int o) {
		coeff = coefficient;
		Generator = G;
		myprivates = (int) ((Math.random()*o) + 1);
		aG = TimesN(myprivates, Generator);
		orderOfGen = o;

		dictionary.put("Galois", new Pair(5,1));
		dictionary.put("Wyatt", new Pair(6,3));
		dictionary.put("Shubham", new Pair(10,6));
		dictionary.put("Collusion", new Pair(3,1));
		dictionary.put("Yale", new Pair(9,16));
		dictionary.put("Farmhouse", new Pair(16,13));
		dictionary.put("Asparagus", new Pair(0,6));
		dictionary.put("Osprey", new Pair(13,7));
		dictionary.put("Quaternion", new Pair(7,6));
		dictionary.put("Blueno", new Pair(7,11));
		dictionary.put("Condor", new Pair(13,10));
		dictionary.put("Kirby", new Pair(0,11));
		dictionary.put("Hyrule", new Pair(16,4));
		dictionary.put("Hibiscus", new Pair(9,1));
		dictionary.put("Episcopalian", new Pair(3,16));
		dictionary.put("Pescatarian", new Pair(10,11));
		dictionary.put("Pastels", new Pair(6,14));
		dictionary.put("Rigatoni", new Pair(5,16));
		dictionary.put("devilish", new Pair(0,0));
		yranoitcid.put("( 0 , 0 )", "devilish");
		yranoitcid.put("( 5 , 16 )", "Rigatoni");
		yranoitcid.put("( 6 , 14 )","Pastels");
		yranoitcid.put("( 10 , 11 )","Pescatarian");
		yranoitcid.put("( 3 , 16 )","Episcopalian");
		yranoitcid.put("( 9 , 1 )", "Hibiscus");
		yranoitcid.put("( 16 , 4 )", "Hyrule");
		yranoitcid.put("( 0 , 11 )", "Kirby");
		yranoitcid.put("( 13 , 10 )", "Condor");
		yranoitcid.put("( 7 , 11 )", "Blueno");
		yranoitcid.put("( 7 , 6 )", "Quaternion");
		yranoitcid.put("( 13 , 7 )", "Osprey");
		yranoitcid.put("( 0 , 6 )", "Asparagus");
		yranoitcid.put("( 16 , 13 )", "Farmhouse");
		yranoitcid.put("( 9 , 16 )", "Yale");
		yranoitcid.put("( 3 , 1 )", "Collusion");
		yranoitcid.put("( 10 , 6 )", "Shubham");
		yranoitcid.put("( 6 , 3 )", "Wyatt");
		yranoitcid.put("( 5 , 1 )", "Galois");
	}

	public Pair<Integer, Integer> Inverse(Pair<Integer, Integer> p) {
		p.setValue1(((-1)*(p.getValue1()))%(fieldsize));
		return p;
	}

	public String Encode(String m) {
		if (dictionary.containsKey(m)) {
			Pair<Integer, Integer> medi = Times(dictionary.get(m), this.pvtkey);
			System.out.println("ENC : " + medi); // DEBUG
			String crypt = yranoitcid.get(medi.toString());
			System.out.println("ENC : " + crypt); // TODO : DELETE : only fpr DEBUG
			return crypt;
		} else {
			System.out.println("I don't know what that means");
			return "ERROR!!!";
		}
	}

	public String Decode(String mp) {
		if (dictionary.containsKey(mp)) {
			Pair<Integer, Integer> medi = Times(dictionary.get(mp),
												Inverse(this.pvtkey));
			System.out.println("huh "+mp);
			System.out.println("huh medi "+ medi);
			System.out.println("huh coded point"+ dictionary.get(mp));
			System.out.println("huh pvtkey"+ this.pvtkey);
			System.out.println("huh inverse of pkee"+Inverse(this.pvtkey));
			String ans = yranoitcid.get(medi.toString());
			System.out.println(ans);
			return ans;
		} else {
			System.out.println("huh");
			return "ERROR!!!";
		}
	}
//for(int i = 0; i < x; i++) {
	//comp = TimesDiff(g, TimeSelf(g)); //should there be if else in case x = 1 or 2 and only TimeSelf is necessary
	public Pair<Integer, Integer> Times(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
		if(a.equals(new Pair(0,0))) {
			return b;
		} else if(b.equals(new Pair(0,0))){
			 return a;
		}
		if(a.equals(b)) {
		return TimeSelf(a);
		} else {
			if(a.getValue0().equals(b.getValue0())) {
				return new Pair<Integer, Integer>(0, 0);
			} else {
			return TimesDiff(a,b);
			}
		}
	}

	public Pair<Integer, Integer> TimesN (int x, Pair<Integer, Integer> g) { //computes g^x
		Pair<Integer, Integer> comp = g;

		for(int i = 1; i<x; i++) {
			comp = Times(comp, g);
		}
		return comp;
	}
	private int Mod(int n) {
		int m = n%(fieldsize);
		if(m<0){m = m + (fieldsize);}
		return m;
	}
	public int myPow (int a, int n) {
		int res = 1;
		for (int i = 0; i < n; i++) {
			res = (res*a) % fieldsize;
		}
		return res;
	}
	private Pair<Integer, Integer>  TimeSelf(Pair<Integer, Integer> q) {
		System.out.println("*** pout TIMESSELF *** ");
		System.out.println("*** pout TIMESSELF IN : " + q);
		int x = q.getValue0();
		int y = q.getValue1();
		int sx = Mod( 3*x*x + this.coeff.getValue0());
		System.out.println("qvt sx is"+ sx);
		int sy = Mod(myPow((2*y),(fieldsize - 2)));
		System.out.println("qvt sy is"+ sy);
		int s = Mod(sx*sy); //HARDCODE FINITE FIELD OPERATIONS???
		System.out.println("qvt s is"+ s);
		int xr = Mod(s*s - 2*x);
		if(xr<0){xr = xr + fieldsize;}
		System.out.println("qvt xr is "+ xr);
		int yr = Mod((-(y + s*(xr - x))));
		System.out.println("qvt yr is "+ yr);
		System.out.println("pout TIMESSELF OUT is : " + new Pair(xr, yr));
		return new Pair(xr, yr);
	}

	private Pair<Integer, Integer> TimesDiff(Pair<Integer, Integer> q,
											Pair<Integer, Integer> p) {
		System.out.println("*** pout TIMES DIFF *** ");
		System.out.println("pout TIMES DIFF IN : " + q);
		System.out.println("pout TIMES DIFF IN : " + p);
		System.out.println(p + " pvt " + q);
		int qx = q.getValue0();
		int qy = q.getValue1();
		int px = p.getValue0();
		int py = p.getValue1();
		int sy = (py - qy);
		System.out.println("pout TIMES DIFF STEP 1 : " + sy);
		int sx = myPow((px - qx),(fieldsize - 2));
		System.out.println("pout TIMES DIFF STEP 2 : " + sx);
		int s = Mod(sx*sy);
		System.out.println("pout TIMES DIFF STEP 3 : " + s);
		int xr = Mod((s*s)-(qx + px));
		int yr = Mod(-(py + s*(xr - px)));
		System.out.println("pout TIMES DIFF OUT : " + (new Pair(xr, yr)).toString());
		return new Pair(xr, yr);
	}


	public void genKey(String pvt) {
		if(dictionary.containsKey(pvt)){
			this.bG = dictionary.get(pvt);
			this.pvtkey = TimesN(myprivates, bG);
			System.out.println("pvtkey " + this.pvtkey);
		}
	}

	public static void main(String [] args) {
		System.out.println("DiD it WORK???");
	}
}
