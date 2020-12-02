package kotchain

// Difficulty will determine how easily a block can be mined.
// '4' will take a second or two on a 2.8 GHz Intel Core i5
private const val DIFFICULTY = 4

private const val ADD = "add "
private const val EXIT = "exit"
private const val VERIFY = "verify"
private const val PRINT = "print-blocks"
private const val UPDATE = "update "
private const val RESET = "reset"
private const val MINE_ALL = "mine-all"

private const val EQUALS_SIGN = "="

private val logger: Logger = object : Logger {
    override fun print(message: String) {
        kotlin.io.print(message)
    }

    override fun println(message: String) {
        kotlin.io.println(message)
    }
}

private val node = Node(DIFFICULTY, logger)

fun main() {
    showInstructions()
    addGenesisBlock()
    listenForInput()
}

private fun addGenesisBlock() {
    logger.println("Adding Genesis block... ")
    addBlock("")
}

private fun listenForInput() {
    var input: String?
    do {
        input = readLine()
        input?.let {
            handleInput(input)
        }
    } while (input != EXIT)
}

private fun handleInput(input: String) {
    when {
        input == VERIFY -> node.verify()
        input == PRINT -> print()
        input == RESET -> reset()
        input == MINE_ALL -> mineAll()
        input == EXIT -> return
        input.startsWith(ADD) -> addBlock(input)
        input.startsWith(UPDATE) -> updateBlock(input)
        else -> logger.println("Sorry I didn't understand\n")
    }
}

private fun mineAll() {
    node.mineAll()
}

private fun print() {
    println(node.toString())
}

private fun addBlock(input: String) {
    val data = input.substringAfter(ADD)
    node.addBlock(data)
}

private fun reset() {
    node.reset()
    addGenesisBlock()
}

private fun updateBlock(input: String) {
    val blockInt = input.substringAfter(UPDATE).substringBefore(EQUALS_SIGN).toInt()
    val newData = input.substringAfter(EQUALS_SIGN)
    if (node.updateBlockData(blockInt, newData)) {
        logger.println("Updated block #$blockInt with $newData")
    } else {
        logger.println("Error block $blockInt doesn't exist")
    }
}

private fun showInstructions() {
    logger.println("\nInstructions\n------------")
    logger.println("add <string data>:                         Create a block, mine it and add to the blockchain")
    logger.println("verify:                                    Check the integrity of the blockchain")
    logger.println("print-blocks:                              Show the contents of the entire blockchain")
    logger.println("update:  <block number>=<data>:            Update the data in a block")
    logger.println("mine-all:                                  Mine all the blocks in the chain")
    logger.println("reset:                                     Remove all blocks from the chain")
    logger.println("exit:                                      Exit the demo\n")
}
