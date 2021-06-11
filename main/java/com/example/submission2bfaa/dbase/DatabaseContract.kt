package com.example.submission2bfaa.dbase

import android.app.TaskStackBuilder
import android.net.Uri
import android.provider.BaseColumns
import com.google.android.material.internal.NavigationMenu

internal class DatabaseContract {
    class NoteColumns : BaseColumns{
        companion object{
            const val TABLE_NAME = "githubdata"
            const val USERNAME = "id"
            const val NAME = "name"
            const val PHOTO = "photo"
            const val FOLLOWING = "following"
            const val FOLLOWERS = "followers"
            const val REPOSITORY = "repository"
            const val PERUSAHAAN = "perusahaan"
            const val LOKASI = "lokasi"
            const val FAVORIT = "favorite"
        }
    }
}