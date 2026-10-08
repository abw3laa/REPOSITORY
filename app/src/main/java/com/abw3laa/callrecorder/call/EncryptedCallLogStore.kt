package com.abw3laa.callrecorder.call

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.security.KeyStore
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class EncryptedCallLogStore(context: Context) {
    private val file = File(context.filesDir, "call-history.dat")

    @Synchronized
    fun append(record: CallRecord) {
        val records = read().toMutableList()
        records.add(record)
        write(records.takeLast(MAX_RECORDS))
    }

    @Synchronized
    fun read(): List<CallRecord> {
        if (!file.exists()) return emptyList()
        return runCatching {
            val raw = decrypt(file.readBytes())
            val array = JSONArray(raw.toString(Charsets.UTF_8))
            buildList(array.length()) {
                for (i in 0 until array.length()) {
                    val o = array.getJSONObject(i)
                    add(
                        CallRecord(
                            id = o.getString("id"),
                            startedAtMs = o.getLong("startedAtMs"),
                            endedAtMs = o.getLong("endedAtMs"),
                            direction = CallDirection.valueOf(o.getString("direction")),
                            callerName = o.optString("callerName").takeIf { it.isNotBlank() },
                            callerNumber = o.optString("callerNumber").takeIf { it.isNotBlank() }
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    private fun write(records: List<CallRecord>) {
        val array = JSONArray()
        records.forEach { record ->
            array.put(
                JSONObject()
                    .put("id", record.id)
                    .put("startedAtMs", record.startedAtMs)
                    .put("endedAtMs", record.endedAtMs)
                    .put("direction", record.direction.name)
                    .put("callerName", record.callerName ?: "")
                    .put("callerNumber", record.callerNumber ?: "")
            )
        }

        val encrypted = encrypt(array.toString().toByteArray(Charsets.UTF_8))
        val temp = File(file.parentFile, file.name + ".tmp")
        temp.writeBytes(encrypted)
        if (!temp.renameTo(file)) {
            temp.delete()
            error("Unable to atomically replace call history")
        }
    }

    private fun encrypt(plain: ByteArray): ByteArray {
        val nonce = ByteArray(12).also(SecureRandom()::nextBytes)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key(), GCMParameterSpec(128, nonce))
        return nonce + cipher.doFinal(plain)
    }

    private fun decrypt(payload: ByteArray): ByteArray {
        require(payload.size > 12) { "Invalid call history payload" }
        val nonce = payload.copyOfRange(0, 12)
        val ciphertext = payload.copyOfRange(12, payload.size)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key(), GCMParameterSpec(128, nonce))
        return cipher.doFinal(ciphertext)
    }

    private fun key(): SecretKey {
        val keyStore = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (keyStore.getKey(KEY_ALIAS, null) as? SecretKey)?.let { return it }

        val generator = KeyGenerator.getInstance("AES", "AndroidKeyStore")
        generator.init(256)
        return generator.generateKey()
    }

    companion object {
        private const val KEY_ALIAS = "call_history_aes_gcm_v1"
        private const val MAX_RECORDS = 500
    }
}
