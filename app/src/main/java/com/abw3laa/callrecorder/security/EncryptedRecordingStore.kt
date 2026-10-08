package com.abw3laa.callrecorder.security

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

/**
 * Encrypts recording bytes before they are persisted.
 *
 * File format:
 *   4 bytes  magic "CR01"
 *   1 byte   version
 *   12 bytes AES-GCM nonce
 *   n bytes  ciphertext + authentication tag
 *
 * The AES key never leaves Android Keystore.
 */
class EncryptedRecordingStore(context: Context) {
    private val recordingsDir = File(context.filesDir, "recordings").apply { mkdirs() }

    fun createRecordingFile(): File {
        return File(recordingsDir, "${java.util.UUID.randomUUID()}.cr")
    }

    fun writeEncrypted(source: File, destination: File) {
        require(source.isFile) { "Source recording does not exist" }

        val nonce = ByteArray(GCM_NONCE_SIZE).also(SecureRandom()::nextBytes)
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, getOrCreateKey(), GCMParameterSpec(GCM_TAG_BITS, nonce))

        FileInputStream(source).use { input ->
            FileOutputStream(destination).use { output ->
                output.write(MAGIC)
                output.write(VERSION)
                output.write(nonce)

                val buffer = ByteArray(BUFFER_SIZE)
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    val encrypted = cipher.update(buffer, 0, count)
                    if (encrypted != null) output.write(encrypted)
                }

                val finalBytes = cipher.doFinal()
                output.write(finalBytes)
                output.fd.sync()
            }
        }
    }

    fun decryptTo(source: File, destination: File) {
        FileInputStream(source).use { input ->
            require(input.readNBytes(MAGIC.size).contentEquals(MAGIC)) {
                "Invalid recording header"
            }
            require(input.read() == VERSION) {
                "Unsupported recording version"
            }

            val nonce = input.readNBytes(GCM_NONCE_SIZE)
            require(nonce.size == GCM_NONCE_SIZE) { "Invalid recording nonce" }

            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(
                Cipher.DECRYPT_MODE,
                getOrCreateKey(),
                GCMParameterSpec(GCM_TAG_BITS, nonce)
            )

            FileOutputStream(destination).use { output ->
                val buffer = ByteArray(BUFFER_SIZE)
                while (true) {
                    val count = input.read(buffer)
                    if (count < 0) break
                    val plain = cipher.update(buffer, 0, count)
                    if (plain != null) output.write(plain)
                }
                output.write(cipher.doFinal())
                output.fd.sync()
            }
        }
    }

    private fun getOrCreateKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val generator = KeyGenerator.getInstance(KEY_ALGORITHM, ANDROID_KEYSTORE)
        generator.init(
            android.security.keystore.KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                android.security.keystore.KeyProperties.PURPOSE_ENCRYPT or
                    android.security.keystore.KeyProperties.PURPOSE_DECRYPT
            )
                .setBlockModes(android.security.keystore.KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(android.security.keystore.KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build()
        )
        return generator.generateKey()
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "callrecorder.recordings.v1"
        private const val KEY_ALGORITHM = "AES"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val GCM_TAG_BITS = 128
        private const val GCM_NONCE_SIZE = 12
        private const val BUFFER_SIZE = 64 * 1024
        private const val VERSION = 1
        private val MAGIC = byteArrayOf('C'.code.toByte(), 'R'.code.toByte(), '0'.code.toByte(), '1'.code.toByte())
    }
}
