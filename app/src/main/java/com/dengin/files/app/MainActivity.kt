package com.dengin.files.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.dengin.files.R
import com.dengin.files.utils.delegate.unsafeLazy

class MainActivity : AppCompatActivity() {

    private val navController by unsafeLazy {
        (supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment)
            .navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
