package com.abw3laa.callrecorder.privileged

import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Per-process authentication for the app <-> recorder-daemon channel.
 *
 * The key is generated for one recorder session and is never persisted.
 * The daemon receives the same key only during its authenticated bootstrap.
 */
class CommandAuthenticator private constructor(
    private val secret: ByteArray
) {
    private val mac = Mac.getInstance(HMAC_ALGORITHM).apply {
        init(SecretKeySpec(secret, HMAC_ALGORITHM))
    }

    fun sign(sequence: Long, command: String, nonce: ByteArray): ByteArray {
        val payload = buildPayload(sequence, command, nonce)
        return mac.doFinal(payload)
    }

    fun verify(
        expected: ByteArray,
        sequence: Long,
        command: String,
        nonce: ByteArray
    ): Boolean {
        val actual = sign(sequence, command, nonce)
        return MessageDigest.isEqual(actual, expected)
    }

    companion object {
        private const val HMAC_ALGORITHM = "HmacSHA256"
        private const val SECRET_SIZE = 32

        fun create(): CommandAuthenticator {
            return CommandAuthenticator(
                ByteArray(SECRET_SIZE).also(SecureRandom()::nextBytes)
            )
        }

        fun fromBootstrapSecret(secret: ByteArray): CommandAuthenticator {
            require(secret.size == SECRET_SIZE) { "Invalid bootstrap secret size" }
            return CommandAuthenticator(secret.copyOf())
        }

        private fun buildPayload(
            sequence: Long,
            command: String,
            nonce: ByteArray
        ): ByteArray {
            val commandBytes = command.toByteArray(Charsets.UTF_8)
            return ByteArray(8 + 4 + commandBytes.size + nonce.size).also { out ->
                var offset = 0
                java.nio.ByteBuffer.wrap(out).apply {
                    putLong(sequence)
                    putInt(commandBytes.size)
                    put(commandBytes)
                    put(nonce)
                }
            }
        }
    }
}
