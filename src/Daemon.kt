class Daemon(difficulty: Int) {
    val count = 5;
    private val nodes: ArrayList<Node> = ArrayList(count)

    init {
        for (i in 1..count) {
            nodes.add(Node(difficulty, "Node $i"))
        }
    }

    fun add(data: String) {
        val time = System.currentTimeMillis()
        nodes.forEach({ node -> node.add(Block(time, data)) })
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

