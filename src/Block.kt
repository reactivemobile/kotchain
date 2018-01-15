import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter
import kotlin.math.min

/**
 * A class representing a single block in a BlockChain
 *
 * @property timestamp When the block was created
 * @property data Some arbitrary string data to store in the block
 *
 */
class Block(private val timestamp: Long, var data: String) {
    var previousHash = ""
    var hash = ""
    var nonce = -1
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

    /**
     * Get basic block details including truncated versions of the hash and previousHash
     */
    override fun toString(): String {
        return "Block [data=$data hash=${hash.substring(0, 6)}..." +
                " previousHash=${previousHash.substring(0 until min(previousHash.length, 6)) }... " +
                "nonce=$nonce]"
    }
}