public class Sim3_MUX_8by1 {
	
	// Inputs
	public RussWire[] control, in;
	// AND gates for MUX
	public RussWire[] andGates;
	// Output
	public RussWire out;
	
	public Sim3_MUX_8by1() {
		control = new RussWire[3];
		in = new RussWire[8];
		andGates = new RussWire[8];
		out = new RussWire();
		
		for (int i = 0; i < 3; i++) {
			control[i] = new RussWire();
		}
		for (int i = 0; i < 8; i++) {
			in[i] = new RussWire();
			andGates[i] = new RussWire();
		}
	}

	public void execute() {
		// Composing all the AND gates that determine which control bit to use
		// input A option 000
		andGates[0].set(in[0].get() & !control[2].get() & !control[1].get() & 
				!control[0].get());
		// input B option 001
		andGates[1].set(in[1].get() & !control[2].get() & !control[1].get() & 
				control[0].get());
		// input C option 010
		andGates[2].set(in[2].get() & !control[2].get() & control[1].get() & 
				!control[0].get());
		// input D option 011
		andGates[3].set(in[3].get() & !control[2].get() & control[1].get() & 
				control[0].get());
		// input E option 100
		andGates[4].set(in[4].get() & control[2].get() & !control[1].get() & 
				!control[0].get());
		// input F option 101
		andGates[5].set(in[5].get() & control[2].get() & !control[1].get() & 
				control[0].get());
		// input G option 110
		andGates[6].set(in[6].get() & control[2].get() & control[1].get() & 
				!control[0].get());
		// input H option 111
		andGates[7].set(in[7].get() & control[2].get() & control[1].get() & 
				control[0].get());
		// out is the OR of all the above AND gates
		out.set(andGates[0].get() | andGates[1].get() | andGates[2].get() | 
				andGates[3].get() | andGates[4].get() | andGates[5].get() | 
				andGates[6].get() | andGates[7].get());
	}
	
	// An additional method to handle a two input MUX
	// Used in Sim3_ALUElement as the bInvert selector
	public void execute2Inputs() {
		andGates[0].set(in[0].get() & !control[0].get());
		andGates[1].set(in[1].get() & control[0].get());
		out.set(andGates[0].get() | andGates[1].get());
	}
}
