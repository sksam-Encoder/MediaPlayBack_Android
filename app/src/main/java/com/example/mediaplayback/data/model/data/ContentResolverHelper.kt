package com.example.mediaplayback.data.model.data

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.example.mediaplayback.data.model.Audio
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContentResolverHelper @Inject constructor(@ApplicationContext val context: Context) {
    private var mCursor: Cursor? = null

    private val projection: Array<String> = arrayOf(
        MediaStore.Audio.AudioColumns.DISPLAY_NAME,
        MediaStore.Audio.AudioColumns._ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.DATA,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.AudioColumns.TITLE,
    )

    private var selectClause: String? = "${MediaStore.Audio.AudioColumns.IS_MUSIC} = ?"

    private val selectionArg = arrayOf("1")

    private val sortOrder = "${MediaStore.Audio.AudioColumns.DISPLAY_NAME} ASC"

    fun getAudioData(): List<Audio> = getCursorData()

    private fun getCursorData(): MutableList<Audio> {
        val audiolist = mutableListOf<Audio>()
        mCursor = context.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selectClause,
            selectionArg,
            sortOrder
        )

        mCursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns._ID)
            val colName = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DISPLAY_NAME)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.ARTIST)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.DURATION)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.AudioColumns.TITLE)

            it.apply {
                if (count == 0) {
                    Log.e("cursor", "getcursorData: Cursor iS empty")
                } else {
                    while (it.moveToNext()) {

                        val name = getString(colName)
                        val id = getLong(idColumn)
                        val artist = getString(artistColumn)
                        val data = getString(dataColumn)
                        val duration = getInt(durationColumn)
                        val title = getString(titleColumn)
                        val uri = ContentUris.withAppendedId(
                            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                            id
                        )
                        audiolist += Audio(
                            uri, name, id, artist, data, duration, title
                        )
                    }
                }
            }
        }
        return audiolist
    }


}