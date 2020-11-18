
public class Sim2_HalfAdder {
	
	// inputs
	public RussWire a, b;
	// outputs
	public RussWire sum, carry;
	// gates
	public Sim2_XOR xor;
	public AND and;
	
	public Sim2_HalfAdder() {
		a = new RussWire();
		b = new RussWire();
		sum = new RussWire();
		carry = new RussWire();
		and = new AND();
		xor = new Sim2_XOR();
	}
	
	public void execute() {
		// Getting the OR of a and b.
		xor.a.set(a.get());
		xor.b.set(b.get());
		xor.execute();
		// Setting sum result.
		sum.set(xor.out.get());
		// Getting the AND of a and b.
		and.a.set(a.get());
		and.b.set(b.get());
		and.execute();
		// Setting carryout.
		carry.set(and.out.get());
	}

}
