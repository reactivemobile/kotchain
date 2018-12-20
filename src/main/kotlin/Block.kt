package kotchain

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

/**
 * A class representing a single block in a BlockChain
 *
 * @property timestamp When the block was created
 * @property data Some arbitrary string data to store in the block
 *
 */
const val cardWidth = 30

class Block(private val timestamp: Long, var data: String) {
    private val hashUnset = ""
    private val prefixUnset = ""
    private val nonceUnset = -1

    var previousHash = hashUnset
    var hash = hashUnset
    var nonce = nonceUnset
    var difficultyPrefix = prefixUnset

    private val digest = MessageDigest.getInstance("SHA-256")!!

    private fun calculateHash(): String {
        val content = "$timestamp$data$previousHash$nonce"
        digest.update(content.toByteArray(Charsets.UTF_8))
        val hashBytes = digest.digest()
        return DatatypeConverter.printHexBinary(hashBytes)
    }

    fun updateHash() {
        hash = calculateHash()
    }

    fun isMined(): Boolean {
        return hash.startsWith(difficultyPrefix)
    }

    fun getPrettyView(index: Int): Any {
        val topLine = '\u2554' + "\u2550".repeat(cardWidth) + '\u2557'
        val bottomLine = '\u255A' + "\u2550".repeat(cardWidth) + '\u255D'
        val line0 = wrapPrettyPrint("Index:", index.toString())
        val line1 = wrapPrettyPrint("Data:", data)
        val line2 = wrapPrettyPrint("Hash:", hash)
        val line3 = wrapPrettyPrint("Previous hash:", previousHash)
        val line4 = wrapPrettyPrint("Nonce:", nonce.toString())

        return "$topLine\n$line0$line1$line2$line3$line4$bottomLine"
    }

    private fun wrapPrettyPrint(string: String, value: String): String {
        return "\u2551 " + (string.padEnd(cardWidth - 10) + value.take(cardWidth - 22)).padEnd(cardWidth - 1, ' ') + "\u2551\n"
    }
}