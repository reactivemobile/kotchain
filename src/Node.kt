import java.util.*

/**
 * A Node holds a single blockchain and performs operations on it
 */
class Node(difficulty: Int, val nodeName: String) {

    private val difficultyPrefix = "0".repeat(difficulty)
    private val blockChain: BlockChain = BlockChain()

    /**
     * Add a block. If it's not the first block to be added we update the 'previousHash' field.
     */
    operator fun plusAssign(block: Block) {
        if (blockChain.size > 0) {
            block.previousHash = blockChain.last().hash
        }
        mine(block)
        blockChain += block
    }

    /**
     * Reset the block to its initial state
     */
    fun reset() {
        blockChain.clear()
    }

    /**
     * Update the block at the specific index with the new data.
     * Afterwards we propagate the hash changes up the chain.
     */
    fun updateBlockData(index: Int, newData: String): Boolean {
        if (index in 0 until blockChain.size) {
            blockChain[index].data = newData
            updateHashesFromIndex(index)
            return true
        }
        return false
    }

    fun verify() {
        println("Verifying $nodeName")
        blockChain.verify(difficultyPrefix)
        println()
    }

    /**
     * Mine a block. This increments the nonce field until the resultant hash begins with a series of '0'
     * characters. The number if zeros needed is set by the difficulty parameter
     */
    private fun mine(block: Block) {
        object : Thread() {
            override fun run() {
                val startTime = System.currentTimeMillis()
                addRandomDelay()
                println("$nodeName is Mining Block...")
                while (!block.isMined(difficultyPrefix)) {
                    block.nonce++
                    block.updateHash()
                }
                println("$nodeName is done. Time was ${System.currentTimeMillis() - startTime} nonce is ${block.nonce}, hash is ${block.hash}")
            }

            private fun addRandomDelay() {
                object : Thread() {
                    override fun run() {
                        Thread.sleep(Random().nextInt(5000).toLong())
                    }
                }
            }
        }.start()
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
        return nodeName + blockChain
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
        if (index in 0 until blockChain.size - 1)
            propagateHashForward(blockChain[index + 1], blockChain[index])
    }

    private fun propagateHashForward(block: Block, previousBlock: Block) {
        block.previousHash = previousBlock.hash
    }
}

