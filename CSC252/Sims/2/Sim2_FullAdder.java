
public class Sim2_FullAdder {
	
	// inputs
	public RussWire a, b, carryIn;
	// outputs
	public RussWire sum, carryOut;
	// gates
	public Sim2_HalfAdder ha1, ha2;
	public OR or;
	
	public Sim2_FullAdder() {
		a = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		sum = new RussWire();
		carryOut = new RussWire();
		ha1 = new Sim2_HalfAdder();
		ha2 = new Sim2_HalfAdder();
		or = new OR();
		
	}
	
	public void execute() {
		// Getting the results of half adder with a and b inputs.
		ha1.a.set(a.get());
		ha1.b.set(b.get());
		ha1.execute();
		// Getting the results of half adder with ha1 and cIn inputs.
		ha2.a.set(ha1.sum.get());
		ha2.b.set(carryIn.get());
		ha2.execute();
		// Getting the OR of ha1 and ha2.
		or.a.set(ha2.carry.get());
		or.b.set(ha1.carry.get());
		or.execute();
		// Setting the sum equal to ha2's sum.
		sum.set(ha2.sum.get());
		// Setting the carryout to or's output.
		carryOut.set(or.out.get());
	}

}
