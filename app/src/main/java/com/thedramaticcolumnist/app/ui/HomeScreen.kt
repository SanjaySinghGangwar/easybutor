package com.thedramaticcolumnist.app.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.thedramaticcolumnist.app.Database.mDatabase.mDatabase
import com.thedramaticcolumnist.app.R
import com.thedramaticcolumnist.app.Utils.mUtils.mToast
import com.thedramaticcolumnist.app.databinding.HomeScreenBinding

class HomeScreen : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bind: HomeScreenBinding
    private lateinit var navController: NavController
    private lateinit var firebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bind = HomeScreenBinding.inflate(layoutInflater)
        setContentView(bind.root)

        initAllComponents()

        setSupportActionBar(bind.appBarHomeScreen.toolbar)

        bind.appBarHomeScreen.fab.setOnClickListener { view ->
            Snackbar.make(view, "We can add item here", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = bind.drawerLayout
        val navView: NavigationView = bind.navView
        navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_home,
            R.id.nav_order,
            R.id.nav_wishlist,
            R.id.nav_category,
            R.id.nav_products,
            R.id.nav_distributor,
            R.id.nav_account,
            R.id.nav_customer_service), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        setUpProfileData()

    }

    private fun initAllComponents() {
        firebaseDatabase =
            mDatabase.child(getString(R.string.app_name)).child(
                FirebaseAuth.getInstance().currentUser?.uid.toString())
    }

    private fun setUpProfileData() {
        val header = bind.navView.getHeaderView(0);
        header.findViewById<TextView>(R.id.email).text =
            FirebaseAuth.getInstance().currentUser?.email

        firebaseDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild("name")) {
                    header.findViewById<TextView>(R.id.name).text =
                        snapshot.child("name").value.toString()
                }
                if (snapshot.hasChild("profile_image")) {
                    Glide.with(this@HomeScreen)
                        .load(snapshot.child("profile_image").value.toString())
                        .placeholder(R.drawable.ic_person)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.drawable.ic_error)
                        .into(header.findViewById(R.id.imageView))
                }

            }

            override fun onCancelled(error: DatabaseError) {
                mToast(this@HomeScreen, error.message)
            }
        })


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_screen, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cart -> {
                try{
                    navController.navigate(R.id.cart)
                }catch (e:Exception){
                    navController.popBackStack()
                    navController.navigate(R.id.card)
                }



            }
            R.id.search -> {
                try{
                    navController.navigate(R.id.search2)
                }catch (e:Exception){
                    navController.popBackStack()
                    navController.navigate(R.id.search2)
                }

            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}