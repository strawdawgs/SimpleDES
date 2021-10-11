# SimpleDES

My version of the Simplified Data Encryption Standard

This program takes an input of 12 bits, as well as a key of 9 bits and the number of rounds of encryption from the user.

It returns the 12 bits in an encrypted format as well as the original 12 bits entered by the user.

Examples: 

Input:
  Plain text - 000000000000
  Key - 000000000
  Number of rounds - 1

Output:
  Cipher text - 101100000000
  Plain text - 000000000000
  
Input:
  Plain text - 111111111111
  Key - 111111111
  Number of rounds - 1
  
Output:
  Cipher text - 010011111111
  Plain text - 111111111111
  
Input:
  Plain text - 001001001110
  Key - 001111001
  Number of rounds - 17
  
Output:
  Cipher text - 111101010011
  Plain text - 001001001110
  

** I didn't put user error checks in this. I figured I could but there's really no point.
