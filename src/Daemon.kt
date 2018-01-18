class Daemon(difficulty: Int) {
    private val count = 5;
    private val nodes: Array<Node> = Array(count) { i -> Node(difficulty, "Node $i")}

    fun add(data: String) {
        val time = System.currentTimeMillis()
        nodes.forEach({ node -> node += (Block(time, data)) })
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
}

