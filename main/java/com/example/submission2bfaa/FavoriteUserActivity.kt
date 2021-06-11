package com.example.submission2bfaa

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission2bfaa.dbase.GitHelper

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var rvFavUser:RecyclerView
    private var list: ArrayList<User> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_user)

        rvFavUser = findViewById(R.id.rv_favUser)
        rvFavUser.setHasFixedSize(true)
        readDb()
        showRecyclerList()
    }
    private fun showRecyclerList(){
        rvFavUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvFavUser.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object :ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val intentDtl = Intent(this@FavoriteUserActivity,IntentDetailActivity::class.java)
                intentDtl.putExtra(IntentDetailActivity.EXTRA_USER,data)
                startActivity(intentDtl)
            }
        })
    }
    private fun readDb(){
        val gitHelper = GitHelper.getInstance(applicationContext)
        gitHelper.open()
        var cursor = gitHelper.queryAll()
        var a = MappingHelper.mapCursorToArrayList(cursor)
        list.addAll(a)
        gitHelper.close()
    }

}