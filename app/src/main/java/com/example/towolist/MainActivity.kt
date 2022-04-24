package com.example.towolist

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.towolist.databinding.ActivityMainBinding
import com.example.towolist.ui.IUpdateLayoutFragment
import com.mancj.materialsearchbar.MaterialSearchBar

class MainActivity : AppCompatActivity() {

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
        initializeBottomNavigationListener(navController)

        binding.gridButton.isEnabled = false
        layoutButtonListener(binding.gridButton, binding.listButton)
        layoutButtonListener(binding.listButton, binding.gridButton)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    fun setupSpinner(): Spinner {
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

    private fun initFilterBottomFragment(navController: NavController) {
        binding.filterButton.setOnClickListener {
            navController.navigate(R.id.filterFragment)
        }
    }

    private fun initializeBottomNavigationListener(navController: NavController) {
        binding.bottomNavigation.setOnItemSelectedListener {
            navController.navigate(it.itemId)
            binding.searchBar.setPlaceHolder(it.title)
            true
        }
    }

    private fun layoutButtonListener(clicked: ImageButton, disabled: ImageButton) {
        clicked.setOnClickListener {
            clicked.isEnabled = false
            clicked.imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.secondaryLight)

            disabled.isEnabled = true
            disabled.imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.secondary)

            updateCurrentLayout()
        }
    }

    private fun updateCurrentLayout() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragments =
            navHostFragment.childFragmentManager.fragments as List<IUpdateLayoutFragment>
        fragments.forEach { fragment ->
            fragment.updateLayout(binding.gridButton.isEnabled)
        }
    }

    fun isListLayout(): Boolean {
        return binding.gridButton.isEnabled
    }

    fun isPopularSpinnerOption(): Boolean {
        return binding.spinner.selectedItem.toString() == resources.getStringArray(R.array.spinner_options)[0]
    }

    fun getSearchBar(): MaterialSearchBar {
        return binding.searchBar
    }
}