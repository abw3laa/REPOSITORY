package com.abw3laa.callrecorder.call

import android.content.Context
import android.content.pm.PackageManager
import android.provider.ContactsContract
import androidx.core.content.ContextCompat

class CallerResolver(private val context: Context) {
    fun resolve(number: String?): CallerIdentity {
        if (number.isNullOrBlank()) return CallerIdentity(null, null)
        if (
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return CallerIdentity(null, number)
        }

        val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
            .appendPath(number)
            .build()

        return runCatching {
            context.contentResolver.query(
                uri,
                arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)
                if (cursor.moveToFirst() && nameIndex >= 0) {
                    CallerIdentity(cursor.getString(nameIndex), number)
                } else {
                    CallerIdentity(null, number)
                }
            } ?: CallerIdentity(null, number)
        }.getOrDefault(CallerIdentity(null, number))
    }
}
