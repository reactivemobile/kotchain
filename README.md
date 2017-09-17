# kotchain

This is a tiny implementation of a [blockchain](https://en.wikipedia.org/wiki/Blockchain) written in Kotlin. It's not representative of a complete system in that it's not distributed. Instead it consists of just a single node, intending to demonstrate the core concept of blockchain technology.

Running the code in Main.kt creates the node with a blockchain and adds the genesis block. Then you'll see the instructions:
```
Instructions
------------
add <string data>:                         Create a block, mine it and add to the blockchain
verify:                                    Check the integrity of the blockchain
print:                                     Show the contents of the entire blockchain
updateBlockData: <block number>=<data>:    Update the data in a block
mine-all:                                  Mine all the blocks in the chain
reset:                                     Remove all blocks from the chain
exit:   `                                   Exit the demo
```
Here's a sample interaction with the node:
```
add A
Mining Block... Done. Time was 119 nonce is 55369, hash is 0000D385E7F57D031AED5706DDB51A633140DA15ECDF0CCD2918E64946A29144

add B
Mining Block... Done. Time was 27 nonce is 11582, hash is 0000DD24C1D6399542A4D55019A9A2EE32999FA970E5D6F013FD48F9101CD8CB

add C
Mining Block... Done. Time was 133 nonce is 76393, hash is 0000BCC6F460B3F3E3B016C1E09E22759420381F2650E926529FC270750622BF

add D
Mining Block... Done. Time was 0 nonce is 55, hash is 00009E717A1BBE87B0968F8A83A51CE6B4D6707B71C2C5E51F75139089F1C573

add E
Mining Block... Done. Time was 26 nonce is 13758, hash is 0000C447BD38E3707A552CFCFAF3FB360763B6B9D64FA31CDFAB159BEFF9C108

add F
Mining Block... Done. Time was 63 nonce is 41937, hash is 0000EC39059E66543B14CB82708AFB81CF9B7D38D0939A4B0855293E21CEBF43

add G
Mining Block... Done. Time was 46 nonce is 28606, hash is 0000AAFEB6D3CF2A7374EC07F44FE1E3CFA2EF4F922AB6DC2EE35F006853CD45
```

This mined and added 7 blocks to to the blockchain. This can be confirmed by entering `print`:
```
print
0: Block [data=<GENESIS BLOCK> hash=0000B9... previousHash=... nonce=10939]
1: Block [data=A hash=0000D3... previousHash=0000B9... nonce=55369]
2: Block [data=B hash=0000DD... previousHash=0000D3... nonce=11582]
3: Block [data=C hash=0000BC... previousHash=0000DD... nonce=76393]
4: Block [data=D hash=00009E... previousHash=0000BC... nonce=55]
5: Block [data=E hash=0000C4... previousHash=00009E... nonce=13758]
6: Block [data=F hash=0000EC... previousHash=0000C4... nonce=41937]
7: Block [data=G hash=0000AA... previousHash=0000EC... nonce=28606]
```
You can see each block has been [mined](https://en.bitcoin.it/wiki/Nonce) (each hash has 4 leading zero characters) and a nonce calculated. Note the `previousHash` field of each block matches the `hash` field of the previous block. This is one of the core concepts of the blockchain. If one block is modified then the hash change is propogated up the chain making the following blocks invalid.

We can validate the integrity of the chain by typing `verify`
```
verify
Block 0 is OK
Block 1 is OK
Block 2 is OK
Block 3 is OK
Block 4 is OK
Block 5 is OK
Block 6 is OK
Block 7 is OK
Blockchain was verified
```
To demonstrate how tampering is clearly detected, use the `update` command to simulate how an attacker might try to change a record.
```
update 4=X
Updated 4 with X
```
Now when we print the chain you can see how the hashes in blocks 4 and above have been changed.

```
0: Block [data=<GENESIS BLOCK> hash=0000B9... previousHash=... nonce=10939]
1: Block [data=A hash=0000D3... previousHash=0000B9... nonce=55369]
2: Block [data=B hash=0000DD... previousHash=0000D3... nonce=11582]
3: Block [data=C hash=0000BC... previousHash=0000DD... nonce=76393]
4: Block [data=X hash=BC7FAA... previousHash=0000BC... nonce=55]
5: Block [data=E hash=733366... previousHash=BC7FAA... nonce=13758]
6: Block [data=F hash=9C07A0... previousHash=733366... nonce=41937]
7: Block [data=G hash=EF9C1E... previousHash=9C07A0... nonce=28606]
```
Now when we `verify`, the node knows something has been changed.
```
verify
Block 0 is OK
Block 1 is OK
Block 2 is OK
Block 3 is OK
Blockchain was compromised at block 4! Hash mismatch.
Blockchain was compromised at block 5! Hash mismatch.
Blockchain was compromised at block 6! Hash mismatch.
Blockchain was compromised at block 7! Hash mismatch.
``` 
The only way to correct the hashes is to re-mine the chain again, in real-life the attacker's node would need [more computing power than all the other nodes combined](https://bitcoin.org/en/glossary/51-percent-attack) in order to do this.
But in this simulator we can run `mine-all` to reset the chain.
```
mine-all
Mining Block... Done. Time was 0 nonce is 10939, hash is 0000B9C88BAEE3C0AE17E268718D7BBDA6E1AAF0D072AFE2A88BB6CF3E7361C4
Mining Block... Done. Time was 0 nonce is 55369, hash is 0000D385E7F57D031AED5706DDB51A633140DA15ECDF0CCD2918E64946A29144
Mining Block... Done. Time was 0 nonce is 11582, hash is 0000DD24C1D6399542A4D55019A9A2EE32999FA970E5D6F013FD48F9101CD8CB
Mining Block... Done. Time was 1 nonce is 76393, hash is 0000BCC6F460B3F3E3B016C1E09E22759420381F2650E926529FC270750622BF
Mining Block... Done. Time was 180 nonce is 80617, hash is 00008534F171C53BCC72C00BB8AC69622BA7C042B9EA402F9262B207A400A1ED
Mining Block... Done. Time was 77 nonce is 52722, hash is 000081F95476DE94F44BED40931B5CF8ECA7B6720C971F5C8205BFE7C7DB0382
Mining Block... Done. Time was 54 nonce is 70190, hash is 0000265BC2BE3A8D91D379D44ACF8F799B8AC27676558E193D12A0636A3EF418
Mining Block... Done. Time was 36 nonce is 51701, hash is 0000576AD2D45D0C7DEC90E3A1702B5388A70760D042914ED3FE1705E3E743FE
```
The first three blocks were aready mined (so their mining time was 0) and the rest were re-mined. Running `verify` we can see that the chain is back to normal again.
```
verify
Block 0 is OK
Block 1 is OK
Block 2 is OK
Block 3 is OK
Block 4 is OK
Block 5 is OK
Block 6 is OK
Block 7 is OK
Blockchain was verified
```

That's it for now, next I hope to make a set of distributed nodes and run the code on them.
