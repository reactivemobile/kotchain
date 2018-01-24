import java.util.*

/**
 * A Node holds a single blockchain and performs operations on it
 */
class Node(difficulty: Int, val nodeName: Int, val callback: OperationCallback) {

    private val difficultyPrefix = "0".repeat(difficulty)
    private val blockChain: BlockChain = BlockChain()

    private var thread: Thread? = null

    /**
     * Add a block. If it's not the first block to be added we update the 'previousHash' field.
     */
    fun add(index: Int, timestamp: Long, data: String) {
        val block = Block(index, timestamp, data, difficultyPrefix)
        mine(block)
    }

    fun cancel(block: Block) {
        thread?.stop();
        blockChain += block
    }

    /**
     * Reset the block to its initial state
     */
    fun reset() {
        blockChain.clear()
    }

    fun notifyBlockMined(block: Block) {
        callback.blockMined(this, block)
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
        blockChain.verify()
        println()
    }

    /**
     * Mine a block
     */
    private fun mine(block: Block) {
        thread = object : Thread() {
            override fun run() {
                val startTime = System.currentTimeMillis()
                addDelay(nodeName)
                println("$nodeName is Mining Block...")
                if (blockChain.size > 0) {
                    block.previousHash = blockChain.last().hash
                }
                block.mine()
                blockChain += block
                notifyBlockMined(block);
                println("$nodeName is done. Time was ${System.currentTimeMillis() - startTime} Block: $block")
            }

            private fun addDelay(duration: Int) {
                object : Thread() {
                    override fun run() {
                        sleep(((duration + 1) * 1000L))
                    }
                }
            }
        }

        (thread as Thread).start()
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
        return nodeName.toString() + " " + blockChain
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

