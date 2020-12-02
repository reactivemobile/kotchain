package kotchain

/**
 * A Node holds a single blockchain and performs operations on it
 */
class Node(difficulty: Int, private val logger: Logger) {

    private val difficultyPrefix = "0".repeat(difficulty)
    private val blockChain: BlockChain = BlockChain(logger)
    private val size = { blockChain.size }

    /**
     * Add a block. If it's not the first block to be added we update the 'previousHash' field.
     */
    fun addBlock(data: String) {
        val block = Block(System.currentTimeMillis(), data, size())
        if (size() > 0) {
            block.previousHash = blockChain.last().hash
        }
        block.difficultyPrefix = difficultyPrefix
        mine(block)
        blockChain.add(block)
    }

    /**
     * Reset the block to its initial state
     */
    fun reset() {
        blockChain.clear()
        logger.print("Node reset... ")
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
        blockChain.verify()
    }

    /**
     * Mine a block. This increments the nonce field until the resultant hash begins with a series of '0'
     * characters. The number if zeros needed is set by the difficulty parameter
     */
    private fun mine(block: Block) {
        logger.println("Mining block [${block.data}] ")
        while (!block.isMined()) {
            block.nonce++
            block.updateHash()
            logger.print("...\r")
            logger.print("   \r")
        }
        logger.println("Done!")
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
        return blockChain.getDebugString()
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
        if (index < size() - 1) {
            blockChain[index + 1].previousHash = blockChain[index].hash
        }
    }
}
