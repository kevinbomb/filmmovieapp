package com.given.filmmovieapp

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setTitle("Cinema AJ21")

        changeFragment(FragmentFilm())

        val bottom: BottomNavigationView = findViewById(R.id.bottomNav)
        bottom.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_film -> changeFragment(FragmentFilm())
                R.id.menu_profil->changeFragment(FragmentProfile())
                R.id.menu_exit->logout()
            }
            true
        }
    }
    private fun logout(){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@HomeActivity)
        builder.setTitle("Log Out")
        builder.setMessage("Are you sure want to exit?")
            .setPositiveButton("Yes"){ dialog, which ->
                finishAndRemoveTask()
            }
            .show()
    }

    //untuk pindah fragment pkai function changeFragment
    //bottom Navigation perlu diberikan color dan item menu.
    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }




}