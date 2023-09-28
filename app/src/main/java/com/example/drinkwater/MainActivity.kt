package com.example.drinkwater

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.drinkwater.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var myDataStore: DataStoreProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        //setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        myDataStore = DataStoreProvider(this)
        myDataStore.updateDailyGoal(1000.0)


        // Reset total amount to 0 every day at 00:00:00
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            0, 0,
            0
        )

        val intervalTimeInMillis = (myDataStore.getNotificationInterval() ?: 1) * 60*1000

        System.out.println("time::" + calendar.timeInMillis)
        System.out.println("datastore interval::" + myDataStore.getNotificationInterval())
        System.out.println("intervalTimeInMillis::" + intervalTimeInMillis)

        NotificationUtils.setAlarm(this, calendar.timeInMillis)
        NotificationUtils.setNotificationAlarm(this, intervalTimeInMillis.toLong())

    }



}