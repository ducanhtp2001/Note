package com.example.note.activities.home

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.note.R
import com.example.note.Tools.WorkManagerment.MyWork
import com.example.note.activities.login.LoginActivity
import com.example.note.base.BaseActivity
import com.example.note.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>() {

    private lateinit var navigationView: NavigationView
    private lateinit var drawer: DrawerLayout
    private var mAppBarConfiguration: AppBarConfiguration? = null
    private var navController: NavController? = null

    private var curentFragment = DrawMenuType.HOME

    @JvmField
    var idSinhVien: String? = null
    override val viewModel: MainViewModel by viewModels()
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding = { inflater ->
        ActivityMainBinding.inflate(inflater)
    }

    override fun initViews() {
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.action)))

        drawer = binding.drawerLayout
        navigationView = binding.navView

        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_home, R.id.nav_account, R.id.nav_settings, R.id.nav_logout, R.id.nav_trash
        )
            .setOpenableLayout(drawer)
            .build()
    }

    override fun initData() {
        workExecute()
    }

    override fun setupViewEvents() {
        navigationView.setNavigationItemSelectedListener { item ->
            val id = item.itemId
            val type = DrawMenuType.getDrawMenuType(id)

            when(type) {
                DrawMenuType.HOME, DrawMenuType.ACCOUNT, DrawMenuType.TRASH-> {
                    if (curentFragment != type) {
                        curentFragment = type
                        navController!!.navigate(R.id.nav_home)
                    }
                }
                DrawMenuType.LOGOUT -> logoutApp()
            }

//            if (id == R.id.nav_home) {
//                if (curentFragment != FRAGMENT_HOME) {
//                    curentFragment = FRAGMENT_HOME
//                    navController!!.navigate(R.id.nav_home)
//                }
//            } else if (id == R.id.nav_account) {
//                if (curentFragment != FRAGMENT_ACCOUNT) {
//                    curentFragment = FRAGMENT_ACCOUNT
//                    navController!!.navigate(R.id.nav_account)
//                }
//            } else if (id == R.id.nav_trash) {
//                if (curentFragment != FRAGMENT_TRASH) {
//                    curentFragment = FRAGMENT_TRASH
//                    navController!!.navigate(R.id.nav_trash)
//                }
//            } else if (id == R.id.nav_logout) {
//                if (curentFragment != FRAGMENT_LOGOUT) {
//                    logoutApp()
//                }
//            }
//            val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
            drawer.closeDrawer(GravityCompat.START)
            true
        }

        navController = findNavController(this, R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(this, navController!!, mAppBarConfiguration!!)
        setupWithNavController(navigationView, navController!!)
    }


    override fun setupObservers() {


    }

    private fun workExecute() {
        val zoneId = ZoneId.of("Asia/Ho_Chi_Minh")
        val now = ZonedDateTime.now(zoneId)

        val targetHour = 7
        val targetMinute = 0
        val targetSecond = 0

        var nextExecutionTime =
            now.withHour(targetHour).withMinute(targetMinute).withSecond(targetSecond)

        if (now.compareTo(nextExecutionTime) >= 0) {
            nextExecutionTime = nextExecutionTime.plusDays(1)
        }

        // tinh thoi gian can de dat lich
        val duration = Duration.between(now, nextExecutionTime)

        val inputData = Data.Builder()
            .putString("idSinhVien", idSinhVien)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(MyWork::class.java)
            .setInputData(inputData)
            .setInitialDelay(duration.toMillis(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance().enqueue(workRequest)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return navigateUp(navController, mAppBarConfiguration!!) || super.onSupportNavigateUp()
    }

    fun setNightMode(mode: Int) {
        AppCompatDelegate.setDefaultNightMode(mode)
        recreate()
    }

    private fun logoutApp() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
