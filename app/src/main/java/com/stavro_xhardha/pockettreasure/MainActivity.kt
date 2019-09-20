package com.stavro_xhardha.pockettreasure

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.stavro_xhardha.PocketTreasureApplication
import com.stavro_xhardha.pockettreasure.brain.REQUEST_CHECK_LOCATION_SETTINGS
import com.stavro_xhardha.pockettreasure.brain.REQUEST_LOCATION_PERMISSION
import com.stavro_xhardha.pockettreasure.ui.SharedViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AppBarConfiguration.OnNavigateUpListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initMainViewModel()

        if (savedInstanceState != null) {
            checkToolbar()
            checkNavView()
        } else {
            mainActivityViewModel.checkSavedTheme()
        }

        ivDarkMode.setOnClickListener {
            mainActivityViewModel.changeCurrentTheme()
        }

        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return
        val navController = host.navController

        setupNavigation(navController)

        setupActionBar(navController, appBarConfiguration)

        setupNavControllerListener(navController)

        sharedViewModel.onToolbarTitleRemoveRequested.observe(this, Observer {
            toolbar.title = ""
        })
    }

    private fun initMainViewModel() {
        val rocket = PocketTreasureApplication.getPocketTreasureComponent().getSharedPreferences
        mainActivityViewModel = ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T =
                MainActivityViewModel(rocket) as T
        }).get(MainActivityViewModel::class.java)
    }

    private fun checkToolbar() {
        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            toolbar.context.setTheme(R.style.ThemeOverlay_MaterialComponents_Dark)
        } else {
            toolbar.context.setTheme(R.style.ThemeOverlay_MaterialComponents_Light)
        }
    }

    private fun checkNavView() {
        val colorListItems = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.md_black_1000),
                ContextCompat.getColor(this, R.color.colorAccentDark)
            )
        )

        val colorListText = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.md_black_1000),
                ContextCompat.getColor(this, R.color.md_white_1000)
            )
        )

        if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) {
            nav_view.itemTextColor = colorListText
            nav_view.itemIconTintList = colorListItems
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CHECK_LOCATION_SETTINGS -> {
                when (resultCode) {
                    RESULT_OK -> {
                        sharedViewModel.onGpsOpened()
                    }
                    RESULT_CANCELED -> {
                        if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.settingsFragment) {
                            locationErrorToast()
                        } else {
                            locationErrorToast()
                            finish()
                        }
                    }
                }
            }
        }
    }

    private fun locationErrorToast() {
        Toast.makeText(this, R.string.values_cannot_be_updated, Toast.LENGTH_LONG).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_LOCATION_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sharedViewModel.onLocationPermissionGranted()
                } else {
                    if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.settingsFragment) {
                        locationErrorToast()
                    } else {
                        locationErrorToast()
                        finish()
                    }
                }
                return
            }
        }
    }

    private fun setupNavControllerListener(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.setupFragment) {
                toolbar.visibility = View.GONE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else if (destination.id == R.id.fullImageFragment || destination.id == R.id.ayaFragment) {
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            } else {
                toolbar.visibility = View.VISIBLE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                if (destination.id == R.id.homeFragment) {
                    mainActivityViewModel.checkConfigurationState()
                    val setupVisibilityObserver: LiveData<Boolean> =
                        mainActivityViewModel.launchSetupVisibility
                    setupVisibilityObserver.observe(this, Observer {
                        if (it) {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.setupFragment)
                        }
                    })
                }
            }
        }
    }

    private fun setupActionBar(
        navController: NavController,
        appBarConfig: AppBarConfiguration
    ) {
        setupActionBarWithNavController(navController, appBarConfig)
    }

    private fun setupNavigation(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.nav_view)
        sideNavView?.setupWithNavController(navController)

        val drawerLayout: DrawerLayout? = findViewById(R.id.drawer_layout)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.namesFragment,
                R.id.quranFragment,
                R.id.tasbeehFragment,
                R.id.galleryFragment,
                R.id.settingsFragment,
                R.id.setupFragment,
                R.id.compassFragment
            ),
            drawerLayout
        )
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val retValue = super.onCreateOptionsMenu(menu)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        if (navigationView == null) {
            menuInflater.inflate(R.menu.activity_main_drawer, menu)
            return true
        }
        return retValue
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                if (findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.fullImageFragment
                    || findNavController(R.id.nav_host_fragment).currentDestination?.id == R.id.ayaFragment
                ) {
                    onBackPressed()
                } else {
                    drawer_layout.openDrawer(GravityCompat.START)
                }
            }
        }
        return item.onNavDestinationSelected(findNavController(R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}