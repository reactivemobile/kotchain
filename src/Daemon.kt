class Daemon(difficulty: Int) : OperationCallback {
    private val count = 10
    private val nodes: Array<Node> = Array(count) { index -> Node(difficulty, index, this) }

    fun add(data: String) {
        val time = System.currentTimeMillis()
        nodes.forEachIndexed({ index, node -> node.add(index, time, data) })
    }

    fun verify() {
        nodes.forEach { node -> node.verify() }
    }

    fun reset() {
        nodes.forEach { nodes -> nodes.reset() }
    }

    fun updateBlockData(blockInt: Int, newData: String): Boolean {
        var errorOccurred = false
        nodes.forEach { nodes ->
            if (!nodes.updateBlockData(blockInt, newData)) errorOccurred = true
        }
        return errorOccurred;
    }

    fun mineAll() {
        nodes.forEach { node -> node.mineAll() }
    }

    fun printString() {
        nodes.forEach { node -> println(node.toString()) }
    }

    override fun blockMined(nodeIn: Node, block: Block) {
        println("Node ${nodeIn.nodeName} mined first! Cancelling the others")
        nodes.forEach { node -> if (nodeIn != node) node.cancel(block) }
    }
}

interface OperationCallback {
    fun blockMined(nodeIn: Node, block: Block)
}

