package com.example.drinkwater

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
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


        System.out.println("time" + calendar.timeInMillis)
        setAlarm(calendar.timeInMillis)

        val switches = arrayOf("switch08", "switch10")
        for (i in switches.indices) {
            myDataStore.getSwitchState(switches[i])
            System.out.println("switches[i] :n " + switches[i])
            System.out.println("myDataStore.getSwitchState(switches[i]) : ")
            System.out.println( myDataStore.getSwitchState(switches[i])

            )
            if (myDataStore.getSwitchState(switches[i]) == true) {
                System.out.println(" schedule alarm ")
                val cal: Calendar = Calendar.getInstance()
                cal.set(
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH),
                    15, 30,
                    0
                )
                setNotificationAlarm(cal.timeInMillis)

            }

        }

    }

    private fun setAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ResetAlarm::class.java)
        intent.setAction("com.example.drinkwater.broadcast.reset")
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show()

    }

    private fun setNotificationAlarm(timeInMillis: Long) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, ResetAlarm::class.java)
        intent.setAction("com.example.drinkwater.broadcast.notify")
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
        alarmManager.setRepeating(
            AlarmManager.RTC,
            timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )

        //Toast.makeText(this, "Alarm is set", Toast.LENGTH_LONG).show()

    }


}