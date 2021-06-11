package com.example.submission2bfaa

import android.annotation.SuppressLint
import android.app.TaskStackBuilder
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.SyncStateContract.Helpers.insert
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.core.database.getBlobOrNull
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.submission2bfaa.dbase.DatabaseContract
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.FAVORIT
//import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.CONTENT_URI
//import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.CONTENT_URI
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.FOLLOWERS
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.FOLLOWING
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.LOKASI
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.NAME
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.PERUSAHAAN
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.PHOTO
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.REPOSITORY
import com.example.submission2bfaa.dbase.DatabaseContract.NoteColumns.Companion.TABLE_NAME
import com.example.submission2bfaa.dbase.DatabaseHelper
import com.example.submission2bfaa.dbase.GitHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.loopj.android.http.AsyncHttpClient.log
import java.lang.Exception
import kotlin.properties.Delegates

class IntentDetailActivity : AppCompatActivity() {
    private lateinit var favBtn: FloatingActionButton
    private lateinit var gitHelper: GitHelper
    private lateinit var avatar: String
    private var favorite by Delegates.notNull<Boolean>()


    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
                R.string.tab_text_1,
                R.string.tab_text_2
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val progressBar = findViewById<ProgressBar>(R.id.progressBarDetail)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_detail)

        val tvNamaUser: TextView = findViewById(R.id.tv_namaUser)
        val tvUserDesc: TextView = findViewById(R.id.tv_bahasaUser)
        val ivUserPhoto: ImageView = findViewById(R.id.iv_userPhoto)
        val tvUserFollowers: TextView = findViewById(R.id.tv_jmlFollower)
        val tvUserFollowing: TextView = findViewById(R.id.tv_jmlFollowing)
        val tvUserRepo: TextView = findViewById(R.id.tv_repo)
        val tvUserCompany: TextView = findViewById(R.id.tv_perusahaanUser)
        val tvUserLocation: TextView = findViewById(R.id.tv_lokasiUser)
        favBtn = findViewById(R.id.fav_button)

        val user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        val gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()

        tvNamaUser.text = user.name
        tvUserDesc.text = user.detail
        Glide.with(this)
                .load(user.photo)
                .apply(RequestOptions())
                .into(ivUserPhoto)
        avatar = user.photo.toString()
        tvUserFollowers.text = user.followers
        tvUserFollowing.text = user.following
        tvUserRepo.text = user.repository
        tvUserCompany.text = user.perusahaan
        tvUserLocation.text = user.lokasi
        favorite = user.isFav


        val sectionPagerAdapter = SectionPagerAdapter(this)
        sectionPagerAdapter.username = user.detail
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        var a = cekFav(tvUserDesc.text.toString().trim())
        Log.d("nilai a", "onCreate:$a ")
        var statusFavorite = a
        setStatusFavorite(statusFavorite)
        favBtn.setOnClickListener {

            statusFavorite = !statusFavorite
            Log.d("fav", "onCreate: $statusFavorite ")

            val values = ContentValues()
            val namaUser = tvNamaUser.text.toString().trim()
            val userNameId = tvUserDesc.text.toString().trim()
            val userPhoto = avatar.trim()
            val userFollowers = tvUserFollowers.text.toString().trim()
            val userFollowing = tvUserFollowing.text.toString().trim()
            val userRepo = tvUserRepo.text.toString().trim()
            val userCompany = tvUserCompany.text.toString().trim()
            val userLocation = tvUserLocation.text.toString().trim()
            val userFav = statusFavorite.toString().trim()

            values.put(DatabaseContract.NoteColumns.USERNAME, userNameId)
            values.put(DatabaseContract.NoteColumns.NAME, namaUser)
            values.put(DatabaseContract.NoteColumns.PHOTO, userPhoto)
            values.put(DatabaseContract.NoteColumns.FOLLOWERS, userFollowers)
            values.put(DatabaseContract.NoteColumns.FOLLOWING, userFollowing)
            values.put(DatabaseContract.NoteColumns.REPOSITORY, userRepo)
            values.put(DatabaseContract.NoteColumns.PERUSAHAAN, userCompany)
            values.put(DatabaseContract.NoteColumns.LOKASI, userLocation)
            values.put(DatabaseContract.NoteColumns.FAVORIT, userFav)
            //cekFav(userNameId)

            if(statusFavorite){
                gitHelper.insert(values)
                Log.d("hahaha1", "onCreate: ${user.isFav}")
            }else{
                gitHelper.deleteById(userNameId)
                Log.d("hahaha2", "onCreate: ${user.isFav}")
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            favBtn.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            favBtn.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun cekFav(userName:String):Boolean{
        val gitHelper = GitHelper.getInstance(applicationContext)
        var nilai = false
        var hasil = "hasil"
        gitHelper.open()
        var c = gitHelper.queryByName(userName)
        c.moveToFirst()
        try {
            hasil = c.getString(c.getColumnIndexOrThrow(DatabaseContract.NoteColumns.USERNAME))
        }catch (e:Exception){
            hasil = "hasil"
        }
        Log.d("hasil", "cekFav:$nilai")
        if(hasil=="hasil"){
            nilai = false
        }else{
            nilai = true
        }
        Log.d("nilai", "cekFav: $nilai")
        return nilai
    }
}


