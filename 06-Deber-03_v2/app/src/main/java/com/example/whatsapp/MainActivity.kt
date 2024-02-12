package com.example.whatsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private lateinit var mToolbar: Toolbar
    private lateinit var myViewPager : ViewPager
    private lateinit var myTabLayout : TabLayout
    private lateinit var myTabsAccessorAdapter: TabsAccessorAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mToolbar = findViewById(R.id.main_page_toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.title = "WhatsApp"

        myViewPager = findViewById(R.id.main_tabs_pager)
        myTabsAccessorAdapter = TabsAccessorAdapter(supportFragmentManager)
        myViewPager.setAdapter(myTabsAccessorAdapter)

        myTabLayout = findViewById(R.id.main_tabs)
        myTabLayout.setupWithViewPager(myViewPager)



    }
}