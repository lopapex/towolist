package com.example.towolist

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.towolist.data.MovieItem
import com.example.towolist.databinding.ActivityMainBinding
import com.example.towolist.ui.filter.spinner.SpinnerActivity
import com.example.towolist.ui.list.ListFragmentDirections
import com.example.towolist.ui.list.MovieGridAdapter
import com.example.towolist.ui.list.MovieListAdapter
import com.mancj.materialsearchbar.MaterialSearchBar

class MainActivity : AppCompatActivity(), MaterialSearchBar.OnSearchActionListener {

    private var fragmentListeners : MutableList<Triple<Fragment, RecyclerView, () -> List<MovieItem>>> = ArrayList()

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

        val spinner = initFilterMovieTypes()
        spinner.onItemSelectedListener = SpinnerActivity()

        binding.searchBar.setOnSearchActionListener(this)

        binding.gridButton.isEnabled = false
        layoutButtonListener(binding.gridButton, binding.listButton)
        layoutButtonListener(binding.listButton, binding.gridButton)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_SHORT).show()
        binding.searchBar.closeSearch()
    }

    override fun onButtonClicked(buttonCode: Int) {
        Toast.makeText(this@MainActivity, "Search ", Toast.LENGTH_SHORT).show()
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

            fragmentListeners.forEach {
                updateLayout(it.first, it.second, it.third)
            }
        }
    }

    fun registerLayoutListener(fragment: Fragment, recyclerView: RecyclerView, getItems: () -> List<MovieItem>) {
        fragmentListeners.add(Triple(fragment, recyclerView, getItems))
    }

    fun updateLayout(fragment: Fragment, recyclerView: RecyclerView, getItems: () -> List<MovieItem>) {
        if (!binding.gridButton.isEnabled) {
            val adapter = MovieGridAdapter(onItemClick = {
                fragment.findNavController()
                    .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
            })

            recyclerView.apply {
                layoutManager = GridLayoutManager(context, 3)
            }
            recyclerView.adapter = adapter
            adapter.submitList(getItems())
        } else {
            val adapter = MovieListAdapter(onItemClick = {
                fragment.findNavController()
                    .navigate(ListFragmentDirections.actionListFragmentToDetailMovieFragment(it))
            })

            recyclerView.apply {
                layoutManager = LinearLayoutManager(context)
            }
            recyclerView.adapter = adapter
            adapter.submitList(getItems())
        }
    }
}