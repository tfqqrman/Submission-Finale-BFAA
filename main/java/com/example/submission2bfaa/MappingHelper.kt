package com.example.submission2bfaa

import android.database.Cursor
import com.example.submission2bfaa.dbase.DatabaseContract

object MappingHelper {
    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<User>{
        val favList = ArrayList<User>()
        notesCursor?.apply{
            while (moveToNext()){
                val userName = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.NAME))
                val followers = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FOLLOWERS))
                val following = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.FOLLOWING))
                val lokasi = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.LOKASI))
                val perusahaan = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.PERUSAHAAN))
                val foto = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.PHOTO))
                val repo = getString(getColumnIndexOrThrow(DatabaseContract.NoteColumns.REPOSITORY))
                val favorit = getColumnIndexOrThrow(DatabaseContract.NoteColumns.FAVORIT)
                favList.add(User(name,userName,foto,following,followers,repo,perusahaan,lokasi))
            }
        }
        return favList
    }
}