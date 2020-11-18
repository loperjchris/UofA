# Cryptograms

A cryptogram is a word puzzle based upon a simple substitution cypher. 
The letters of a quotation
are replaced with different letters such that:
1. All occurrences of the same letter are replaced with the same one
2. No replacement letter is ever used twice
3. All letters are uppercase
4. Punctuation is not altered
5. Spaces remain where they originally were
6. No letter stands for itself (We will ignore this one)

So if we had the phrase:

```
HELLO WORLD
```

We might generate a random encryption key that says all H should be 
replaced by T, E by R, L by Z, O
by X, etc., yielding a cryptogram of:

```
TRZZX AXPZE
```

The puzzle is for a user, presented with the encrypted version above, to 
reproduce the original
text by guessing the substitutions that were made. Knowledge of how 
words work in English usually
gives the first attempts. The most common letter in English is 'E', the 
most common three-letter
word is 'THE', the sequence of letters 'QQ' never occurs, but 'OO', 
'LL', or 'SS' might.

You can play some online for yourself to see: 
<https://www.cryptograms.org/play.php>

The purpose of this project is to use HashMaps to create cryptograms, 
and let a human user solve them.

## Example Run

```
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: J
Enter its replacement: T

T            .         T       . -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: R
Enter its replacement: H

T        H   .  H      TH      . -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 
Enter the letter to replace: B
Enter its replacement: E

T        HE  .  H    E THE    E. -       T        
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN 

...

TALK IS CHEAP. SHOW ME THE CODE. - LINUS TORVALDS 
JWSI ZN KRBWP. NRYF LB JRB KYVB. - SZGQN JYTHWSVN

You got it!
```

## Writing the Cryptogram Program
We have provided a file, quotes.txt that contains some 
programming-related quotations. Select a random quote (one line in the 
file) to be the puzzle. Generate a random key for the substitution 
cypher by creating a List of the letters A-Z and using 
Collections.shuffle() to permute them. Create a HashMap that maps 
Characters to Characters and for each letter, associate it with one 
element from the randomly shuffled List.
Encode the chosen quotation by replacing the letters of the quote with 
the shuffled version using your HashMap.
Have an additional HashMap for the user's attempt to solve the puzzle. 
As the user enters their guesses, add the mapping to their HashMap and 
show them their progress by using the HashMap to replace the encrypted 
characters with their guess ones.
A user is always free to change a letter to something else.
The game is over when the user's guesses turn the encrypted quote back 
into the original quotation.

## Requirements

- You need a working implementation of the cryptogram puzzle game that 
uses a HashMap class to 
    1. Store the random encryption key
    2. Encrypt the quotation
    3. Store the player's decryption mappings
    4. Decrypt only those letters you have entered a guess for 
	
- A reasonable design for the Cryptogram game's objects
 
## Submission
 
 As always, the last pushed commit prior to the due date will be graded.
 
 


