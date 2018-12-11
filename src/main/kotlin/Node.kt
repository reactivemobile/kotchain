package kotchain

/**
 * A Node holds a single blockchain and performs operations on it
 */
class Node(difficulty: Int) {

    private val difficultyPrefix = "0".repeat(difficulty)
    private val blockChain: BlockChain = BlockChain()

    /**
     * Add a block. If it's not the first block to be added we update the 'previousHash' field.
     */
    fun add(block: Block) {
        if (blockChain.size > 0) {
            block.previousHash = blockChain.last().hash
        }
        mine(block)
        blockChain.add(block)
    }

    /**
     * Reset the block to its initial state
     */
    fun reset() {
        blockChain.clear()
    }

    /**
     * Update the block at the specific index with the new data.
     * Afterwards we propogate the hash changes up the chain.
     */
    fun updateBlockData(index: Int, newData: String): Boolean {
        if (index in 0..blockChain.size) {
            blockChain[index].data = newData
            updateHashesFromIndex(index)
            return true
        }
        return false
    }

    fun verify() {
        blockChain.verify(difficultyPrefix)
    }

    /**
     * Mine a block. This increments the nonce field until the resultant hash begins with a series of '0'
     * characters. The number if zeros needed is set by the difficulty parameter
     */
    private fun mine(block: Block) {
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
        blockChain.forEachIndexed { index, block ->
            mine(block)
            propagatePreviousHash(index)
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
        for (i in index until blockChain.size) {
            blockChain[i].updateHash()
            propagatePreviousHash(i)
        }
    }

    /**
     * Update the 'previousHash' field of the next block with the 'hash' field of this block
     */
    private fun propagatePreviousHash(index: Int) {
        if (index < blockChain.size - 1) {
            blockChain[index + 1].previousHash = blockChain[index].hash
        }
    }
}

