
public class Sim3_ALUElement {
	
	// Inputs
	public RussWire[] aluOp;
	public RussWire bInvert, a, b, carryIn, less;
	// Outputs
	public RussWire result, addResult, carryOut;
	// MUXs, Gates, and FullAdder for ALU
	public Sim3_MUX_8by1 mux8, mux2;
	public AND and;
	public OR or;
	public FullAdder adder;
	public XOR xor;
	
	public Sim3_ALUElement() {
		aluOp = new RussWire[3];
		bInvert = new RussWire();
		a  = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		less = new RussWire();
		result = new RussWire();
		addResult = new RussWire();
		carryOut = new RussWire();
		mux8 = new Sim3_MUX_8by1();
		mux2 = new Sim3_MUX_8by1();
		and = new AND();
		or = new OR();
		adder = new FullAdder();
		xor = new XOR();
		
		for (int i = 0; i < 3; i++) {
			aluOp[i] = new RussWire();
		}
	}
	
	// Executing the first pass through the ALU
	public void execute_pass1() {
		// Choosing to invert b or not
		mux2.in[0].set(b.get());
		mux2.in[1].set(!b.get());
		mux2.control[0].set(bInvert.get());
		mux2.execute2Inputs();
		// ANDing a and b
		and.a.set(a.get());
		and.b.set(mux2.out.get());
		and.execute();
		// ORing a and b
		or.a.set(a.get());
		or.b.set(mux2.out.get());
		or.execute();
		// Using full adder to find sum of a and b
		// subtraction is utilized by setting bInvert to 1
		adder.a.set(a.get());
		adder.b.set(mux2.out.get());
		adder.carryIn.set(carryIn.get());
		adder.execute();
		addResult.set(adder.sum.get());
		carryOut.set(adder.carryOut.get());
		// XORing a and b
		xor.a.set(a.get());
		xor.b.set(mux2.out.get());
		xor.execute();
	}
	
	// Executing the second pass through the ALU
	public void execute_pass2() {
		// Once addResult has been set then the 8 input MUX can be executed
		// because less has been set to its proper value
		mux8.in[0].set(and.out.get());
		mux8.in[1].set(or.out.get());
		mux8.in[2].set(adder.sum.get());
		mux8.in[3].set(less.get());
		mux8.in[4].set(xor.out.get());
		// Unused options set to false
		mux8.in[5].set(false);
		mux8.in[6].set(false);
		mux8.in[7].set(false);
		mux8.control[0].set(aluOp[0].get());
		mux8.control[1].set(aluOp[1].get());
		mux8.control[2].set(aluOp[2].get());
		mux8.execute();
		result.set(mux8.out.get());
	}

}
