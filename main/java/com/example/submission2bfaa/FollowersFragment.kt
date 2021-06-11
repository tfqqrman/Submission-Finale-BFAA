package com.example.submission2bfaa

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.MainThread
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowersFragment : Fragment() {
    private lateinit var rvFollowers: RecyclerView
    private var list: ArrayList<User> = arrayListOf()

    companion object{
        private val ARG_USERNAME = "username"
        fun newInstance(username: String?): FollowersFragment{
            val fragment = FollowersFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
        private val TAG = FollowersFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvFollowers = view.findViewById(R.id.rv_followers)
        rvFollowers.setHasFixedSize(true)
        val username = arguments?.getString(ARG_USERNAME)
        gUser(username)
    }
    private fun showRecycleList(){
        rvFollowers.layoutManager = LinearLayoutManager(activity)
        val listUserAdapter = ListUserAdapter(list)
        rvFollowers.adapter = listUserAdapter
    }
    private fun gUser(id:String?){
        val progressBar = view?.findViewById<ProgressBar>(R.id.progressBarFollowers)
        progressBar?.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$id/followers"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url,object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d(TAG,result)
                try{
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()){
                        val jsonObject = jsonArray.getJSONObject(i)
                        val usrName = jsonObject.getString("login")
                        val photo = jsonObject.getString("avatar_url")
                        val daftarUser = User(name = usrName,
                                detail = "",
                                photo = photo,
                                following = "",
                                followers = "",
                                repository = "",
                                perusahaan = "",
                                lokasi = ""
                        )
                        list.add(daftarUser)
                    }
                }catch (e:Exception){
                    Toast.makeText(activity,e.message,Toast.LENGTH_SHORT).show()
                }
                showRecycleList()
                progressBar?.visibility=View.INVISIBLE
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable?) {
                progressBar?.visibility = View.INVISIBLE
            }
        })
    }
}