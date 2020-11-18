
public class Sim2_XOR {
	
	// inputs
	public RussWire a, b;
	// outputs
	public RussWire out;
	// gates
	public AND and1;
	public AND and2;
	public NOT not;
	public OR or;
	
	public Sim2_XOR() {
		a = new RussWire();
		b = new RussWire();
		out = new RussWire();
		and1 = new AND();
		and2 = new AND();
		not = new NOT();
		or = new OR();
	}
	
	public void execute() {
		// Getting the AND of a and b.
		and1.a.set(a.get());
		and1.b.set(b.get());
		and1.execute();
		// Getting the OR of a and b.
		or.a.set(a.get());
		or.b.set(b.get());
		or.execute();
		// Inverting the result of a and b.
		not.in.set(and1.out.get());
		not.execute();
		// ANDing the result of not(ab) and (a or b).
		and2.a.set(not.out.get());
		and2.b.set(or.out.get());
		and2.execute();
		out.set(and2.out.get());
	}

}
