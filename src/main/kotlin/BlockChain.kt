package kotchain

import java.util.*

/**
 * A class wrapping a chain of blocks, in this case implemented using a linked list
 */
class BlockChain : LinkedList<Block>() {

    /**
     * Check that the blockchain hasn't been tampered with. We do this by calculating the hash of the block. If it's
     * not in a 'mined' state (with leading '0' string) then something has modified the contents since it was added.
     */
    fun verify() {
        forEachIndexed { index, block ->
            block.updateHash()
            if (block.isMined()) {
                println("Block $index is OK")
            } else {
                println("Blockchain was compromised at block $index! Hash mismatch.")
            }
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        val gap = " ".repeat(16);
        forEachIndexed(fun(index: Int, block: Block) {
            builder.append(block.getPrettyView(index))
            if (index < size - 1) {
                builder.append("\n").append("$gap|\n").append(gap + "\u25BC\n")
            }
        })
        return builder.toString()
    }
}
