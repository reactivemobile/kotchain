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
class Block(private val timestamp: Long, var data: String) {
    private val hashUnset = ""
    private val nonceUnset = -1

    var previousHash = hashUnset
    var hash = hashUnset
    var nonce = nonceUnset
    private val digest = MessageDigest.getInstance("SHA-256")!!

    private fun doHash(): String {
        val content = "$timestamp$data$previousHash$nonce"
        digest.update(content.toByteArray(Charsets.UTF_8))
        val hashBytes = digest.digest()
        return DatatypeConverter.printHexBinary(hashBytes)
    }

    fun updateHash() {
        hash = doHash()
    }

    fun isMined(difficultyPrefix: String): Boolean {
        return hash.startsWith(difficultyPrefix)
    }

    fun getPrettyView(index: Int): Any {
        val topBottomLine = "*".repeat(30)
        val line0 = wrapPrettyPrint("Index:", index.toString())
        val line1 = wrapPrettyPrint("Data:", data)
        val line2 = wrapPrettyPrint("Hash:", hash)
        val line3 = wrapPrettyPrint("Previous hash:", previousHash)
        val line4 = wrapPrettyPrint("Nonce:", nonce.toString())

        return "$topBottomLine\n$line0\n$line1\n$line2\n$line3\n$line4\n$topBottomLine"
    }

    private fun wrapPrettyPrint(string: String, value: String): String {
        return "* " + (string.padEnd(16) + value.take(8)).padEnd(27, ' ') + "*"
    }
}