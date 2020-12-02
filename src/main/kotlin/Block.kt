package kotchain

import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

/**
 * A class representing a single block in a BlockChain
 */
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
}
