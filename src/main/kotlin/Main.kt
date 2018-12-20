package kotchain

// Difficulty will determine how easily a block can be mined.
// '4' will take a second or two on a 2.8 GHz Intel Core i5
private const val difficulty = 4

private const val command_add = "add "
private const val command_exit = "exit"
private const val command_verify = "verify"
private const val command_print = "print-blocks"
private const val command_update = "update "
private const val command_reset = "reset"
private const val command_mine_all = "mine-all"

private const val equals_sign = "="

private val node = Node(difficulty)

fun main(args: Array<String>) {
    showInstructions()
    addGenesisBlock()
    listenForInput()
}

private fun addGenesisBlock() {
    println("Adding Genesis block... ")
    addBlock("")
}

private fun listenForInput() {
    var input: String?
    do {
        input = readLine()
        input?.let {
            handleInput(input)
        }
    } while (input != command_exit)
}

private fun handleInput(input: String) {
    when {
        input == command_verify -> verify()
        input == command_print -> print()
        input == command_reset -> reset()
        input == command_mine_all -> mineAll()
        input == command_exit -> return
        input.startsWith(command_add) -> addBlock(input)
        input.startsWith(command_update) -> updateBlock(input)
        else -> println("Sorry I didn't understand\n")
    }
}

private fun mineAll() {
    node.mineAll()
}

private fun print() {
    println(node.toString())
}

private fun verify() {
    node.verify()
}

private fun addBlock(input: String) {
    val data = input.substringAfter(command_add)
    node.add(Block(System.currentTimeMillis(), data))
}

private fun reset() {
    node.reset()
    addGenesisBlock()
}

private fun updateBlock(input: String) {
    val blockInt = input.substringAfter(command_update).substringBefore(equals_sign).toInt()
    val newData = input.substringAfter(equals_sign)
    if (node.updateBlockData(blockInt, newData)) {
        println("Updated block #$blockInt with $newData")
    } else {
        println("Error block $blockInt doesn't exist")
    }
}

private fun showInstructions() {
    println("\nInstructions\n------------")
    println("add <string data>:                         Create a block, mine it and add to the blockchain")
    println("verify:                                    Check the integrity of the blockchain")
    println("print-blocks:                              Show the contents of the entire blockchain")
    println("update:  <block number>=<data>:            Update the data in a block")
    println("mine-all:                                  Mine all the blocks in the chain")
    println("reset:                                     Remove all blocks from the chain")
    println("exit:                                      Exit the demo\n")
}

