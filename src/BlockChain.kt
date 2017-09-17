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

    override fun toString(): String {
        val builder: StringBuilder = StringBuilder()
        for (i in 0..blockChain.size - 1) {
            builder.append(i).append(": ").append(blockChain[i]).append('\n')
        }
        return builder.toString()
    }
}
