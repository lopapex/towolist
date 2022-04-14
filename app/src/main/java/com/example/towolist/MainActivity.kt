package com.example.towolist

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.towolist.databinding.ActivityMainBinding
import com.example.towolist.ui.filter.spinner.SpinnerActivity
import com.mancj.materialsearchbar.MaterialSearchBar

class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {

    private lateinit var searchBar: MaterialSearchBar

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        initFilterBottomFragment(navController)

        val spinner = initFilterMovieTypes()
        spinner.onItemSelectedListener = SpinnerActivity()

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        searchBar = findViewById(R.id.search_bar)
        searchBar.setOnSearchActionListener(this)
    }

    private fun initFilterMovieTypes(): Spinner {
        val spinner = binding.spinner
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // This layout value is standard
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }
        return spinner
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        val s = if (enabled) "enabled" else "disabled"
        Toast.makeText(this@MainActivity, "Search $s", Toast.LENGTH_SHORT).show()
    }

    override fun onSearchConfirmed(text: CharSequence) {
        Toast.makeText(this@MainActivity, "Search ", Toast.LENGTH_SHORT).show()
        searchBar.closeSearch()
    }

    override fun onButtonClicked(buttonCode: Int) {
        Toast.makeText(this@MainActivity, "Search ", Toast.LENGTH_SHORT).show()
    }

    private fun initFilterBottomFragment(navController: NavController) {
        binding.filterButton.setOnClickListener {
            navController.navigate(R.id.filterFragment)
        }
    }
}