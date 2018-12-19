# kotchain

This is a tiny implementation of a [blockchain](https://en.wikipedia.org/wiki/Blockchain) written in Kotlin. It's not representative of a complete system in that it's not distributed. Instead it consists of just a single node, intending to demonstrate the core concept of blockchain technology.

Build the executable jar using `./gradlew jar` and run with `java -jar build/libs/kotchain.jar` 

This creates the node with a blockchain and adds the genesis block. Then you'll see the instructions:
```
Instructions
------------
add <string data>:                         Create a block, mine it and add to the blockchain
verify:                                    Check the integrity of the blockchain
print:                                     Show the contents of the entire blockchain
update: <block number>=<data>:             Update the data in a block
mine-all:                                  Mine all the blocks in the chain
reset:                                     Remove all blocks from the chain
exit:   `                                  Exit the demo
```
Here's a sample interaction with the node:
```
add ABC
Mining block [ABC] 
Done!
add DEF
Mining block [DEF] 
Done!
add GHI
Mining block [GHI] 
Done!
add JKL
Mining block [JKL] 
Done!```

This mined and added 4 blocks to to the blockchain. This can be confirmed by entering `print`:
```
print
******************************
* Index:          0          *
* Data:                      *
* Hash:           0000B44A   *
* Previous hash:             *
* Nonce:          119824     *
******************************
               |
******************************
* Index:          1          *
* Data:           ABC        *
* Hash:           0000C8A4   *
* Previous hash:  0000B44A   *
* Nonce:          70931      *
******************************
               |
******************************
* Index:          2          *
* Data:           DEF        *
* Hash:           0000ED29   *
* Previous hash:  0000C8A4   *
* Nonce:          30616      *
******************************
               |
******************************
* Index:          3          *
* Data:           GHI        *
* Hash:           000056B1   *
* Previous hash:  0000ED29   *
* Nonce:          59710      *
******************************
               |
******************************
* Index:          4          *
* Data:           JKL        *
* Hash:           00007AF6   *
* Previous hash:  000056B1   *
* Nonce:          32516      *
******************************
```
You can see each block has been [mined](https://en.bitcoin.it/wiki/Nonce) (each hash has 4 leading zero characters) and a nonce calculated. Note the `previousHash` field of each block matches the `hash` field of the previous block. This is one of the core concepts of the blockchain. If one block is modified then the hash change is propagated up the chain making the following blocks invalid.

We can validate the integrity of the chain by typing `verify`
```
verify
Block 0 is OK
Block 1 is OK
Block 2 is OK
Block 3 is OK
Block 4 is OK
```
To demonstrate how tampering is clearly detected, use the `update` command to simulate how an attacker might try to change a record.
```
update 2=XYZ
Updated 2 with XYZ
```
Now when we print the chain you can see how the hashes in blocks 4 and above have been changed.

```
print
******************************
* Index:          0          *
* Data:                      *
* Hash:           0000B44A   *
* Previous hash:             *
* Nonce:          119824     *
******************************
               |
******************************
* Index:          1          *
* Data:           ABC        *
* Hash:           0000C8A4   *
* Previous hash:  0000B44A   *
* Nonce:          70931      *
******************************
               |
******************************
* Index:          2          *
* Data:           XYZ        *
* Hash:           3FB70B50   *
* Previous hash:  0000C8A4   *
* Nonce:          30616      *
******************************
               |
******************************
* Index:          3          *
* Data:           GHI        *
* Hash:           DF61DC23   *
* Previous hash:  3FB70B50   *
* Nonce:          59710      *
******************************
               |
******************************
* Index:          4          *
* Data:           JKL        *
* Hash:           08903A32   *
* Previous hash:  DF61DC23   *
* Nonce:          32516      *
******************************
```
Now when we `verify`, the node knows something has been changed.
```
verify
Block 0 is OK
Block 1 is OK
Blockchain was compromised at block 2! Hash mismatch.
Blockchain was compromised at block 3! Hash mismatch.
Blockchain was compromised at block 4! Hash mismatch.
``` 
The only way to correct the hashes is to re-mine the chain again, in real-life the attacker's node would need [more computing power than all the other nodes combined](https://bitcoin.org/en/glossary/51-percent-attack) in order to do this.
But in this simulator we can run `mine-all` to reset the chain.
```
mine-all
Mining block [] 
Done!
Mining block [ABC] 
Done!
Mining block [DEF] 
Done!
Mining block [GHI] 
Done!
Mining block [JKL] 
Done!
```
The first three blocks were aready mined (so their mining time was 0) and the rest were re-mined. Running `verify` we can see that the chain is back to normal again.
```
verify
Block 0 is OK
Block 1 is OK
Block 2 is OK
Block 3 is OK
Block 4 is OK
```

That's it for now, next I hope to make a set of distributed nodes and run the code on them.
