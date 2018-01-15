import java.util.*

/**
 * A class wrapping a chain of blocks, in this case implemented using a linked list
 */
class BlockChain : LinkedList<Block>() {

    /**
     * Check that the blockchain hasn't been tampered with. We do this by calculating the hash of the block. If it's
     * not in a 'mined' state (with leading '0' string) then something has modified the contents since it was added.
     */
    fun verify(difficultyPrefix: String) {
        forEachIndexed { index, block ->
            block.updateHash()
            if (block.isMined(difficultyPrefix))
                println("   Block $index is OK")
            else
                println("   Blockchain was compromised at block $index! Hash mismatch.")
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        forEachIndexed { index, block -> builder.append(index).append(": ").append(block).append('\n') }
        return builder.toString()
    }
}
