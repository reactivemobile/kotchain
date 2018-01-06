import java.util.*

/**
 * A class wrapping a chain of blocks, in this case implemented using a linked list
 */
class BlockChain {

    private val blockChain: LinkedList<Block> = LinkedList()

    fun reset() {
        blockChain.clear()
    }

    fun get(index: Int): Block {
        return blockChain[index]
    }

    fun size(): Int {
        return blockChain.size
    }

    fun add(block: Block) {
        blockChain.add(block)
    }

    fun last(): Block {
        return blockChain.last
    }

    /**
     * Check that the blockchain hasn't been tampered with. We do this by calculating the hash of the block. If it's
     * not in a 'mined' state (with leading '0' string) then something has modified the contents since it was added.
     */
    fun verify(difficultyPrefix: String) {
        blockChain.onEach { block -> block.updateHash() }
        blockChain.forEachIndexed { index, block ->
            if (block.isMined(difficultyPrefix))
                println("Block $index is OK")
            else
                println("Blockchain was compromised at block $index! Hash mismatch.")
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        blockChain.forEachIndexed {index, block -> builder.append(index).append(": ").append(block).append('\n') }
        return builder.toString()
    }
}
