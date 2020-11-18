/* Implementation of a 32-bit adder in C.
 *
 * Author: Ruben Tequida
 */


#include "sim1.h"



void execute_add(Sim1Data *obj)
{
	int abit, bbit;
  /* Using shifting and masking to get the MSB. */
  obj->aNonNeg = ((obj->a >> 31) & 0x1) ^ 1;
  obj->bNonNeg = ((obj->b >> 31) & 0x1) ^ 1;
  obj->carryOut = obj->isSubtraction;
  /* Iterating through each integer bit by bit. */
  for (int i = 0; i < 32; i++) {
    abit = (obj->a >> i) & 0x1;
    /* Flipping b bits if subtracting. */
    if (obj->isSubtraction) {
      bbit = ~(obj->b >> i) & 0x1;
    } else {
      bbit = (obj->b >> i) & 0x1;
    }
    /* Creating the sum interger based on the bit values of a and b. */
    obj->sum |= (obj->carryOut ^ (abit ^ bbit)) << i;
    obj->carryOut = ((abit & bbit) | (obj->carryOut & (abit ^ bbit)));
  }
  /* Checking the sign of the sum. */
  obj->sumNonNeg = ((obj->sum >> 31) & 0x1) ^ 1;
  /* Determining if overflow occured. */
  if (obj->isSubtraction & ((obj->aNonNeg & ~obj->bNonNeg & obj->sumNonNeg)
   | (~obj->aNonNeg & obj->bNonNeg & ~obj->sumNonNeg))) {
      obj->overflow = 1;
  } else if (~obj->isSubtraction &
    ((obj->aNonNeg & obj->bNonNeg & ~obj->sumNonNeg)
    | (~obj->aNonNeg & ~obj->bNonNeg & obj->sumNonNeg))) {
    obj->overflow = 1;
  } else {
    obj->overflow = 0;
  }

}
