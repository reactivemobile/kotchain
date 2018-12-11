package kotchain

import java.util.*

// Difficulty will determine how easily a block can be mined.
// '4' will take a second or two on a 2.8 GHz Intel Core i5
const val difficulty = 4

val node = Node(difficulty)

fun main(args: Array<String>) {
    addGenesisBlock()
    showInstructions()
    listenForInput()
}

private fun addGenesisBlock() {
    print("Adding Genesis block... ")
    addBlock("<GENESIS BLOCK>")
}

private fun showInstructions() {
    println("\nInstructions\n------------")
    println("add <string data>:                         Create a block, mine it and add to the blockchain")
    println("verify:                                    Check the integrity of the blockchain")
    println("print:                                     Show the contents of the entire blockchain")
    println("update:  <block number>=<data>:            Update the data in a block")
    println("mine-all:                                  Mine all the blocks in the chain")
    println("reset:                                     Remove all blocks from the chain")
    println("exit:                                      Exit the demo")
}

fun addBlock(data: String) {
    node.add(Block(System.currentTimeMillis(), data))
}


fun listenForInput() {
    val inputScanner = Scanner(System.`in`)
    while (true) {
        if (inputScanner.hasNextLine()) {
            val input: String = inputScanner.nextLine()
            if (input.isNotEmpty()) {
                when {
                    input.startsWith("add ", true) -> addBlock(input.substring(4))
                    input.equals("verify", true) -> node.verify()
                    input.equals("print", true) -> println(node.toString())
                    input.startsWith("update", true) -> updateBlock(input)
                    input.equals("reset", true) -> resetNode()
                    input.equals("mine-all", true) -> node.mineAll()
                    input.equals("exit", true) -> return
                    else -> showUnknownCommand("B:$input")
                }
                listenForInput()
            } else {
                showUnknownCommand("A:$input")
                listenForInput()
            }
        }
    }
}

private fun showUnknownCommand(command: String) {
    println("Sorry I didn't understand [$command]\n\n")
    showInstructions()
}

private fun resetNode() {
    node.reset()
    print("Node reset... ")
    addGenesisBlock()
}

private fun updateBlock(input: String) {
    if (!input.contains("=") || input.endsWith("=")) {
        println("Please use the format: Update <block number>=<new data>")
    } else {
        val equalIndex = input.indexOf("=")
        val blockNumber = input.substring(7, equalIndex)
        val blockInt = blockNumber.toInt()
        val newData = input.substring(equalIndex + 1)
        if (node.updateBlockData(blockInt, newData)) {
            println("Updated $blockInt with $newData")
        } else {
            println("Error block $blockInt doesn't exist")
        }
    }
}

