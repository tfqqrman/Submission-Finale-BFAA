package com.example.submission2bfaa.dbase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.NAME
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.USERNAME
import java.sql.SQLException

class GitHelper (context: Context){
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object{
        private const val DATABASE_TABLE = TABLE_NAME
        private var INSTANCE: GitHelper? = null
        fun getInstance(context: Context): GitHelper =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: GitHelper(context)
                }
    }

    @Throws(SQLException::class)
    fun open(){
        database = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()
        if(database.isOpen)
            database.close()
    }

    fun queryAll():Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                null,
                null,
                null,
                null,
                "$NAME ASC")
    }

    fun queryByName(id: String): Cursor{
        return database.query(
                DATABASE_TABLE,
                null,
                "$USERNAME = ?",
                arrayOf(id),
                null,
                null,
                null,
                 null)
    }

    fun insert(values : ContentValues?):Long{
        return database.insert(DATABASE_TABLE, null, values)//null nanti diubah
    }

    fun update(id: String, values: ContentValues?): Int{
        return database.update(DATABASE_TABLE, values, "$NAME = ?", arrayOf(id))
    }

    fun deleteById(id:String):Int{
        return database.delete(DATABASE_TABLE, "$USERNAME = '$id'",null)
    }
}
