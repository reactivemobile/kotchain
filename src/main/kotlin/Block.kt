package kotchain

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

/**
 * A class representing a single block in a BlockChain
 */
const val cardWidth = 30
const val horizontalLineChar = "\u2550"
const val verticalLineChar = "\u2551"
const val topLeft = "\u2554"
const val topRight = "\u2557"
const val bottomLeft = "\u255A"
const val bottomRight = "\u255D"

class Block(private val timestamp: Long, var data: String, var index: Int) {
    private val hashUnset = ""
    private val prefixUnset = ""
    private val nonceUnset = -1

    var previousHash = hashUnset
    var hash = hashUnset
    var nonce = nonceUnset
    var difficultyPrefix = prefixUnset

    private val digest = MessageDigest.getInstance("SHA-256")!!

    private fun calculateHash(): String {
        digest.update("$timestamp$data$previousHash$nonce".toByteArray(Charsets.UTF_8))
        val hashBytes = digest.digest()
        return DatatypeConverter.printHexBinary(hashBytes)
    }

    fun updateHash() {
        hash = calculateHash()
    }

    fun isMined(): Boolean {
        return hash.startsWith(difficultyPrefix)
    }

    override fun toString(): String {
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
}