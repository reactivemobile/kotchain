package kotchain

const val CARD_WIDTH = 30
const val HORIZONTAL_LINE_CHAR = "\u2550"
const val VERTICAL_LINE_CHAR = "\u2551"
const val TOP_LEFT = "\u2554"
const val TOP_RIGHT = "\u2557"
const val BOTTOM_LEFT = "\u255A"
const val BOTTOM_RIGHT = "\u255D"

fun BlockChain.getDebugString(): String {
    val builder = StringBuilder()
    val gap = " ".repeat(CARD_WIDTH / 2)
    forEachIndexed { index, block ->
        builder.append(block.getDebugString())
        if (index < size - 1) {
            builder.append("\n$gap|\n$gap\u25BC\n")
        }
    }
    return builder.toString()
}

fun Block.getDebugString(): String {
    val horizontalLine = HORIZONTAL_LINE_CHAR.repeat(CARD_WIDTH)
    val topLine = "$TOP_LEFT$horizontalLine$TOP_RIGHT"
    val bottomLine = "$BOTTOM_LEFT$horizontalLine$BOTTOM_RIGHT"

    val line0 = wrapPrettyPrint("Index:", index.toString())
    val line1 = wrapPrettyPrint("Data:", data)
    val line2 = wrapPrettyPrint("Hash:", hash)
    val line3 = wrapPrettyPrint("Previous hash:", previousHash)
    val line4 = wrapPrettyPrint("Nonce:", nonce.toString())

    return "$topLine\n$line0$line1$line2$line3$line4$bottomLine"
}

private fun wrapPrettyPrint(string: String, value: String): String {
    return "$VERTICAL_LINE_CHAR ${
        (string.padEnd(CARD_WIDTH - 10) + value.take(CARD_WIDTH - 22))
                .padEnd(CARD_WIDTH - 1, ' ')
    }$VERTICAL_LINE_CHAR\n"
}
