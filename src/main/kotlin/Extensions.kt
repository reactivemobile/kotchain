package kotchain

const val cardWidth = 30
const val horizontalLineChar = "\u2550"
const val verticalLineChar = "\u2551"
const val topLeft = "\u2554"
const val topRight = "\u2557"
const val bottomLeft = "\u255A"
const val bottomRight = "\u255D"

fun BlockChain.getDebugString(): String {
    val builder = StringBuilder()
    val gap = " ".repeat(cardWidth / 2)
    forEachIndexed(fun(index: Int, block: Block) {
        builder.append(block.getDebugString())
        if (index < size - 1) {
            builder.append("\n$gap|\n$gap\u25BC\n")
        }
    })
    return builder.toString()
}

fun Block.getDebugString(): String {
    val horizontalLine = horizontalLineChar.repeat(cardWidth)
    val topLine = "$topLeft$horizontalLine$topRight"
    val bottomLine = "$bottomLeft$horizontalLine$bottomRight"

    val line0 = wrapPrettyPrint("Index:", index.toString())
    val line1 = wrapPrettyPrint("Data:", data)
    val line2 = wrapPrettyPrint("Hash:", hash)
    val line3 = wrapPrettyPrint("Previous hash:", previousHash)
    val line4 = wrapPrettyPrint("Nonce:", nonce.toString())

    return "$topLine\n$line0$line1$line2$line3$line4$bottomLine"
}

private fun wrapPrettyPrint(string: String, value: String): String {
    return "$verticalLineChar ${(string.padEnd(cardWidth - 10) + value.take(cardWidth - 22)).padEnd(cardWidth - 1, ' ')}$verticalLineChar\n"
}