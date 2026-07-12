package com.paridhi.onemoment.backup

import android.content.Context
import android.net.Uri
import android.util.Base64
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.paridhi.onemoment.data.local.MemoryEntity
import java.io.InputStream
import java.io.OutputStream
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

data class BackupPayload(
    val version: Int,
    val date: Long,
    val entryCount: Int,
    val salt: String, // Base64
    val iv: String,   // Base64
    val data: String  // Base64 encrypted JSON
)

class BackupManager(private val context: Context) {
    private val gson = Gson()
    private val GCM_IV_LENGTH = 12
    private val GCM_TAG_LENGTH = 128
    private val SALT_LENGTH = 16
    private val ITERATIONS = 10000
    private val KEY_LENGTH = 256

    fun createBackup(uri: Uri, memories: List<MemoryEntity>, password: CharArray): Result<Unit> {
        return try {
            val random = SecureRandom()
            val salt = ByteArray(SALT_LENGTH)
            random.nextBytes(salt)
            val iv = ByteArray(GCM_IV_LENGTH)
            random.nextBytes(iv)

            val secretKey = deriveKey(password, salt)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, spec)

            val memoriesJson = gson.toJson(memories)
            val encryptedData = cipher.doFinal(memoriesJson.toByteArray(Charsets.UTF_8))

            val payload = BackupPayload(
                version = 1,
                date = System.currentTimeMillis(),
                entryCount = memories.size,
                salt = Base64.encodeToString(salt, Base64.NO_WRAP),
                iv = Base64.encodeToString(iv, Base64.NO_WRAP),
                data = Base64.encodeToString(encryptedData, Base64.NO_WRAP)
            )

            val payloadJson = gson.toJson(payload)

            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(payloadJson.toByteArray(Charsets.UTF_8))
            } ?: return Result.failure(Exception("Failed to open output stream"))

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun readBackupMetadata(uri: Uri): Result<BackupPayload> {
        return try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val jsonStr = inputStream.bufferedReader().readText()
                val payload = gson.fromJson(jsonStr, BackupPayload::class.java)
                Result.success(payload)
            } ?: Result.failure(Exception("Failed to open input stream"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun restoreBackup(uri: Uri, password: CharArray): Result<List<MemoryEntity>> {
        return try {
            val payload = readBackupMetadata(uri).getOrThrow()

            val salt = Base64.decode(payload.salt, Base64.NO_WRAP)
            val iv = Base64.decode(payload.iv, Base64.NO_WRAP)
            val encryptedData = Base64.decode(payload.data, Base64.NO_WRAP)

            val secretKey = deriveKey(password, salt)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val spec = GCMParameterSpec(GCM_TAG_LENGTH, iv)
            cipher.init(Cipher.DECRYPT_MODE, secretKey, spec)

            val decryptedData = cipher.doFinal(encryptedData)
            val decryptedJson = String(decryptedData, Charsets.UTF_8)

            val type = object : TypeToken<List<MemoryEntity>>() {}.type
            val memories: List<MemoryEntity> = gson.fromJson(decryptedJson, type)

            // strip IDs
            val memoriesWithoutIds = memories.map { it.copy(id = 0) }

            Result.success(memoriesWithoutIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun deriveKey(password: CharArray, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH)
        val tmp = factory.generateSecret(spec)
        return SecretKeySpec(tmp.encoded, "AES")
    }
}
