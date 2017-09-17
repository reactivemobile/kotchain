/**
 * A Node holds a single blockchain and performs operations on it
 */
class Node(difficulty: Int) {

    val difficultyPrefix = "0".repeat(difficulty)
    val blockChain: BlockChain = BlockChain();

    /**
     * Add a block. If it's not the first block to be added we update the 'previousHash' field.
     */
    fun add(block: Block) {
        if (blockChain.size() > 0) {
            block.previousHash = blockChain.last().hash
        }
        mine(block)
        blockChain.add(block)
    }

    /**
     * Reset the block to its initial state
     */
    fun reset() {
        blockChain.reset()
    }

    /**
     * Update the block at the specific index with the new data.
     * Afterwards we propogate the hash changes up the chain.
     */
    fun updateBlockData(index: Int, newData: String): Boolean {
        if (index > 0 && index < blockChain.size()) {
            blockChain.get(index).data = newData
            updateHashesFromIndex(index)
            return true;
        }
        return false;
    }

    /**
     * Check that the blockchain hasn't been tampered with. We do this by calculating the hash of the block. If it's
     * not in a 'mined' state (with leading '0' string) then something has modified the contents since it was added.
     */
    fun verify() {
        var isOK = true
        for (i in 0..blockChain.size() - 1) {
            val currentBlock = blockChain.get(i)
            currentBlock.updateHash()

            if (!currentBlock.isMined(difficultyPrefix)) {
                println("Blockchain was compromised at block $i! Hash mismatch.")
                isOK = false
            } else {
                println("Block $i is OK")
            }
        }
        if (isOK) {
            println("Blockchain was verified")
        }
    }

    /**
     * Mine a block. This increments the nonce field until the resultant hash begins with a series of '0'
     * characters. The number if zeros needed is set by the difficulty parameter
     */
    fun mine(block: Block) {
        val startTime = System.currentTimeMillis()
        print("Mining Block...")
        while (!block.isMined(difficultyPrefix)) {
            block.nonce++
            block.updateHash()
        }
        println(" Done. Time was ${System.currentTimeMillis() - startTime} nonce is ${block.nonce}, hash is ${block.hash}")
    }

    /**
     * Mine all the blocks in the chain
     */
    fun mineAll() {
        for (i in 0..blockChain.size() - 1) {
            mine(blockChain.get(i))
            propogatePreviousHash(i)
        }
    }

    /**
     * Get the contents of the blockchain as a String
     */
    override fun toString(): String {
        return blockChain.toString()
    }

    /**
     * Iterate through the chain from the start index updating the hashes and previous hashes of the blocks
     */
    private fun updateHashesFromIndex(index: Int) {
        for (i in index..blockChain.size() - 1) {
            blockChain.get(i).updateHash()
            propogatePreviousHash(i)
        }
    }

    /**
     * Update the 'previousHash' field of the next block with the 'hash' field of this block
     */
    private fun propogatePreviousHash(index: Int) {
        if (index < blockChain.size() - 1) {
            blockChain.get(index + 1).previousHash = blockChain.get(index).hash
        }
    }
}

