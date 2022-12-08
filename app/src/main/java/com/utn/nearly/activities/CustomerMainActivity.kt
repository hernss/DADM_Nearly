package com.utn.nearly.activities


import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.utn.nearly.R
import com.utn.nearly.databinding.ActivityCustomerMainBinding


class CustomerMainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    private lateinit var binding : ActivityCustomerMainBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCustomerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar: Toolbar = findViewById<View>(R.id.toolbar2) as Toolbar
        setSupportActionBar(toolbar)

        binding.apply {

            toggle = ActionBarDrawerToggle(this@CustomerMainActivity, drawerLayout,R.string.app_name , R.string.app_name)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.logoutActionBarButton -> {
                        //add the function to perform here
                        Firebase.auth.signOut()
                        //Deberia volver al login.. pero....
                        this@CustomerMainActivity.finish()
                    }

                }
                true
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }

/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logoutActionBarButton -> {
                //add the function to perform here
                Firebase.auth.signOut()
                //Deberia volver al login.. pero....
                this.finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }*/
}