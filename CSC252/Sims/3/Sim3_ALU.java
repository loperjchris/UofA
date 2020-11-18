
public class Sim3_ALU {

	// Inputs
	public RussWire[] aluOp, a, b;
	public RussWire bNegate;
	public int x;
	// Output
	public RussWire[] result;
	// ALUElements used for complete ALU
	public Sim3_ALUElement[] alu;
	
	public Sim3_ALU(int x) {
		aluOp = new RussWire[3];
		a = new RussWire[x];
		b = new RussWire[x];
		bNegate = new RussWire();
		this.x = x;
		result = new RussWire[x];
		alu = new Sim3_ALUElement[x];
		
		for (int i = 0; i < 3; i++) {
			aluOp[i] = new RussWire();
		}
		for (int i = 0; i < x; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			result[i] = new RussWire();
			alu[i] = new Sim3_ALUElement();
		}
	}
	
	public void execute() {
		// Setting up the first ALU which has its carryIn set to bNegate
		alu[0].aluOp[0].set(aluOp[0].get());
		alu[0].aluOp[1].set(aluOp[1].get());
		alu[0].aluOp[2].set(aluOp[2].get());
		alu[0].bInvert.set(bNegate.get());
		alu[0].a.set(a[0].get());
		alu[0].b.set(b[0].get());
		alu[0].carryIn.set(bNegate.get());
		// Execute pass 1 to determine the result of the adder
		alu[0].execute_pass1();
		// Set up the rest of the ALUs which all behavior the same way
		for (int i = 1; i < x; i++) {
			alu[i].aluOp[0].set(aluOp[0].get());
			alu[i].aluOp[1].set(aluOp[1].get());
			alu[i].aluOp[2].set(aluOp[2].get());
			alu[i].bInvert.set(bNegate.get());
			alu[i].a.set(a[i].get());
			alu[i].b.set(b[i].get());
			alu[i].carryIn.set(alu[i - 1].carryOut.get());
			alu[i].less.set(false);
			alu[i].execute_pass1();
		}
		// Setting the less value of the first ALU to the adder result of the
		// last ALU.
		alu[0].less.set(alu[x - 1].addResult.get());
		// Setting the 8 input MUX result for each ALU
		for (int i = 0; i < x; i++) {
			alu[i].execute_pass2();
			result[i].set(alu[i].result.get());
		}
	}
	
}
