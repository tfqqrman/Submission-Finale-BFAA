package com.example.submission2bfaa

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.submission2bfaa.setting.SettingActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity(), View.OnClickListener{
    private lateinit var rvUser: RecyclerView
    private var list: ArrayList<User> = arrayListOf()
    private lateinit var bookmarkFav_button: FloatingActionButton
    private lateinit var settingButton : FloatingActionButton


    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvUser = findViewById(R.id.rv_user)
        rvUser.setHasFixedSize(true)
        settingButton = findViewById(R.id.main_setting_btn)
        settingButton.setOnClickListener(this)
        bookmarkFav_button = findViewById(R.id.bookmark_button)
        bookmarkFav_button.setOnClickListener(this)


        cariUsr()
        gUser()
    }
    private fun showRecyclerList(){
        rvUser.layoutManager = LinearLayoutManager(this)
        val listUserAdapter = ListUserAdapter(list)
        rvUser.adapter = listUserAdapter
        listUserAdapter.setOnItemClickCallback(object : ListUserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: User) {
                val moveDetailIntent = Intent(this@MainActivity,IntentDetailActivity::class.java)
                moveDetailIntent.putExtra(IntentDetailActivity.EXTRA_USER,data)
                startActivity(moveDetailIntent)
            }
        })
    }

    override fun onClick(v: View?) {
        when(v){
            settingButton -> startActivity(Intent(this@MainActivity,SettingActivity::class.java))
            bookmarkFav_button -> startActivity(Intent(this@MainActivity,FavoriteUserActivity::class.java))
        }
        //val intentFavorite = Intent(this@MainActivity,FavoriteUserActivity::class.java)
        //startActivity(intentFavorite)
    }

    private fun gUser(){
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                //todo
                progressBar.visibility = View.INVISIBLE
                val result = String(responseBody)
                Log.d(TAG, result)
                try{
                    val jsonArray = JSONArray(result)
                    for(i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usrName =jsonObject.getString("login")
                        userDetail(usrName)
                    }
                }catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                //todo
                progressBar.visibility = View.INVISIBLE
            }
        })
    }
    private fun userDetail(usr_name: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$usr_name"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG,result)
                try{
                    val jsonObject = JSONObject(result)
                    val name = jsonObject.getString("name")
                    val detail = jsonObject.getString("login")
                    val photo = jsonObject.getString("avatar_url")
                    val following = jsonObject.getString("following")
                    val followers = jsonObject.getString("followers")
                    val repository = jsonObject.getString("public_repos")
                    val perusahaan = jsonObject.getString("company")
                    val lokasi = jsonObject.getString("location")
                    val daftarUser = User(name = name,
                            detail = detail,
                            photo = photo,
                            following = following,
                            followers = followers,
                            repository = repository,
                            perusahaan = perusahaan,
                            lokasi = lokasi
                    )
                    list.add(daftarUser)
                } catch(e:Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
                showRecyclerList()
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
            }
        })
    }
    private fun cariUsr(){
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.search)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isEmpty()){
                    return true
                }else{
                    searchView.clearFocus()
                    list.clear()
                    hasil_cariUsr(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }
    private fun hasil_cariUsr(id:String){
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$id"

        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                val result = String(responseBody)
                Log.d(TAG,result)
                try{
                    val jsonArray = JSONObject(result)
                    val item = jsonArray.getJSONArray("items")
                    for (i in 0 until item.length()){
                        val jsonObject = item.getJSONObject(i)
                        val username: String = jsonObject.getString("login")
                        userDetail(username)
                        progressBar.visibility = View.INVISIBLE
                    }
                }catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message,Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable?) {
                progressBar.visibility = View.INVISIBLE
            }

        })
    }


}