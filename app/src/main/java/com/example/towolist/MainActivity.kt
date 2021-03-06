package com.example.towolist

import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ArrayRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.towolist.databinding.ActivityMainBinding
import com.example.towolist.ui.IMainActivityFragment
import com.mancj.materialsearchbar.MaterialSearchBar

class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {

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
        
        initializeBottomNavigationListener(navController)

        binding.gridButton.isEnabled = false
        layoutButtonListener(binding.gridButton, binding.listButton)
        layoutButtonListener(binding.listButton, binding.gridButton)

        setupSpinner()
        binding.searchBar.setOnSearchActionListener(this)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun setupSpinner() {
        val spinner = binding.spinner
        setSpinnerOptions(R.array.online_options)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val navHostFragment =
                    supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
                val fragments =
                    navHostFragment.childFragmentManager.fragments as List<IMainActivityFragment>
                fragments.forEach { fragment ->
                    fragment.updateSpinner()
                }
            }
        }
    }

    fun initFilterBottomFragment(onClick: () -> Unit) {
        binding.filterButton.setOnClickListener {
            onClick()
        }
    }

    private fun initializeBottomNavigationListener(navController: NavController) {
        binding.bottomNavigation.setOnItemSelectedListener {
            if (binding.searchBar.isSearchOpened) {
                binding.searchBar.closeSearch()
            }
            navController.navigate(it.itemId)
            resetSpinnerOption()
            binding.searchBar.setPlaceHolder(it.title)
            true
        }
    }

    private fun layoutButtonListener(clicked: ImageButton, disabled: ImageButton) {
        clicked.setOnClickListener {
            clicked.isEnabled = false
            clicked.imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.secondary)

            disabled.isEnabled = true
            disabled.imageTintList =
                ContextCompat.getColorStateList(applicationContext, R.color.secondaryLight)

            updateCurrentLayout()
        }
    }

    private fun updateCurrentLayout() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragments =
            navHostFragment.childFragmentManager.fragments as List<IMainActivityFragment>
        fragments.forEach { fragment ->
            fragment.updateLayout(binding.gridButton.isEnabled)
        }
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (!enabled) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val fragments =
                navHostFragment.childFragmentManager.fragments as List<IMainActivityFragment>
            fragments.forEach { fragment ->
                fragment.searchClose()
            }
        }
    }

    override fun onSearchConfirmed(text: CharSequence?) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val fragments =
            navHostFragment.childFragmentManager.fragments as List<IMainActivityFragment>
        fragments.forEach { fragment ->
            fragment.search(text, false)
            closeKeyboard(fragment as Fragment)
        }
    }

    private fun closeKeyboard(fragment: Fragment) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow((fragment).requireView().windowToken, 0)
    }

    override fun onButtonClicked(buttonCode: Int) {
    }

    fun isListLayout(): Boolean {
        return binding.gridButton.isEnabled
    }

    fun isFirstSpinnerOption(): Boolean {
        return binding.spinner.selectedItem.toString() == binding.spinner.getItemAtPosition(0)
    }

    fun resetSpinnerOption() {
        binding.spinner.setSelection(0)
    }

    fun setSpinnerOptions(@ArrayRes options: Int) {
        ArrayAdapter.createFromResource(
            this,
            options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // This layout value is standard
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner.adapter = adapter
        }
    }

    fun getSearchBar(): MaterialSearchBar {
        return binding.searchBar
    }
}