package com.example.submission2bfaa

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPagerAdapter(activity: AppCompatActivity):FragmentStateAdapter(activity) {
    var username:String? = null
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when(position){
            0 -> fragment = FollowingFragment.newInstance(username)
            1 -> fragment = FollowersFragment.newInstance(username)
        }
        return fragment as Fragment
    }
}