package com.example.conversion

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import android.util.Base64
import java.io.InputStream

object ImageConversion {

//    fun uriToBase64(uri: Uri, context: Context): String? {
//        return try {
//            val inputStream = context.contentResolver.openInputStream(uri)
//            val bytes = getBytes(inputStream)
//            Base64.encodeToString(bytes, Base64.DEFAULT)
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//            null
//        }
//    }

//    @Throws(IOException::class)
//    private fun getBytes(inputStream: InputStream?): ByteArray {
//        var bytesResult: ByteArray
//        val byteBuffer = ByteArrayOutputStream()
//        val bufferSize = 1024
//        val buffer = ByteArray(bufferSize)
//
//        var len: Int
//        while (inputStream?.read(buffer).also { len = it ?: -1 } != -1) {
//            byteBuffer.write(buffer, 0, len)
//        }
//        bytesResult = byteBuffer.toByteArray()
//
//        return bytesResult
//    }
}
