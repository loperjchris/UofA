
public class Sim2_AdderX {
	
	public int x;
	// inputs
	public RussWire[] a, b;
	// outputs
	public RussWire[] sum;
	public RussWire carryOut, overflow;
	//gates
	public Sim2_FullAdder[] fullAdd;
	public Sim2_XOR xor;
	
	public Sim2_AdderX(int x) {
		this.x = x;
		a = new RussWire[x];
		b = new RussWire[x];
		sum = new RussWire[x];
		carryOut = new RussWire();
		overflow = new RussWire();
		xor = new Sim2_XOR();
		fullAdd = new Sim2_FullAdder[x];
		for (int i = 0; i < x; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			sum[i] = new RussWire();
			fullAdd[i] = new Sim2_FullAdder();
		}
	}
	
	public void execute() {
		// Evaluates the first full adder and sets its carry in to false.
		fullAdd[0].a.set(a[0].get());
		fullAdd[0].b.set(b[0].get());
		fullAdd[0].carryIn.set(false);
		fullAdd[0].execute();
		sum[0].set(fullAdd[0].sum.get());
		// Iterates through every bit position (except for the first) and finds
		// the sum and carryout for each full adder.
		for (int i = 1; i < x; i++) {
			fullAdd[i].a.set(a[i].get());
			fullAdd[i].b.set(b[i].get());
			fullAdd[i].carryIn.set(fullAdd[i - 1].carryOut.get());
			fullAdd[i].execute();
			sum[i].set(fullAdd[i].sum.get());
		}
		// Getting the carry out of the last full adder.
		carryOut.set(fullAdd[x - 1].carryOut.get());
		// Overflow is the XOR of the previous carryout and the current
		// carryout.
		xor.a.set(fullAdd[x - 2].carryOut.get());
		xor.b.set(carryOut.get());
		xor.execute();
		overflow.set(xor.out.get());
	}

}
