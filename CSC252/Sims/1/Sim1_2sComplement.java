/* Simulates a physical device that performs 2's complement on a 32-bit input.
 *
 * Author: Ruben Tequida
 */

public class Sim1_2sComplement
{
	public void execute()
	{
		// Flipping all the bits of a and setting b to 1.
		for (int i = 0; i < 32; i++) {
			not[i].in.set(in[i].get());
			not[i].execute();
			flip.a[i].set(not[i].out.get());
			if (i == 0) {
				flip.b[i].set(true);
			} else {
				flip.b[i].set(false);
			}
		}
		flip.execute();
		for (int i = 0; i < 32; i++) {
			out[i].set(flip.sum[i].get());
		}
	}



	// you shouldn't change these standard variables...
	public RussWire[] in;
	public RussWire[] out;

	private Sim1_ADD flip;
	private Sim1_NOT[] not;

	public Sim1_2sComplement()
	{
		in = new RussWire[32];
		out = new RussWire[32];

		flip = new Sim1_ADD();
		not = new Sim1_NOT[32];

		for (int i = 0; i < 32; i++) {
			in[i] = new RussWire();
			out[i] = new RussWire();
			not[i] = new Sim1_NOT();
		}
	}
}
