package com.utn.nearly.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.utn.nearly.R



class ShopMainActivity : AppCompatActivity() {

    private lateinit var bottomNavView : BottomNavigationView
    private lateinit var navHostFragment : NavHostFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_main)


        val toolbar: Toolbar = findViewById<View>(R.id.shopToolbar) as Toolbar
        setSupportActionBar(toolbar)


        navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        bottomNavView = findViewById(R.id.shopBottomBar)
        NavigationUI.setupWithNavController(bottomNavView, navHostFragment.navController)


    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.side_bar_menu, menu)
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
    }
}

