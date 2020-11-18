/* Simulates a physical device that performs (signed) subtraction on
 * a 32-bit input.
 *
 * Author: Ruben Tequida
 */

public class Sim1_SUB
{
	public void execute()
	{
		// Getting the twos complement of b.
		for (int i = 0; i < 32; i++) {
			twoComp.in[i].set(b[i].get());
		}
		twoComp.execute();
		for (int i = 0; i < 32; i++) {
			add.a[i].set(a[i].get());
			add.b[i].set(twoComp.out[i].get());
		}
		add.execute();
		for (int i = 0; i < 32; i++) {
			sum[i].set(add.sum[i].get());
		}
	}



	// --------------------
	// Don't change the following standard variables...
	// --------------------

	// inputs
	public RussWire[] a,b;

	// output
	public RussWire[] sum;

	private Sim1_ADD add;
	private Sim1_2sComplement twoComp;



	public Sim1_SUB()
	{
		a = new RussWire[32];
		b = new RussWire[32];
		sum = new RussWire[32];

		add = new Sim1_ADD();
		twoComp = new Sim1_2sComplement();

		for (int i = 0; i < 32; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			sum[i] = new RussWire();
		}
	}
}
