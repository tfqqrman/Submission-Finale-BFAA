package com.example.submission2bfaa.dbase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private const val DATABASE_NAME = "dbgithubbapp"
        private const val DATABASE_VERSION = 6
        private const val SQL_CREATE_TABLE_NOTE ="CREATE TABLE $TABLE_NAME"+
                "(${DatabaseContract.NoteColumns.USERNAME} TEXT PRIMARY KEY NOT NULL,"+
                "${DatabaseContract.NoteColumns.NAME} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.FOLLOWERS} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.FOLLOWING} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.LOKASI} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.PERUSAHAAN} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.PHOTO} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.REPOSITORY} TEXT NOT NULL,"+
                "${DatabaseContract.NoteColumns.FAVORIT} TEXT NOT NULL)"

    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}