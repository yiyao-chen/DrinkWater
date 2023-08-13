package com.example.drinkwater.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.drinkwater.DataStoreProvider
import com.example.drinkwater.R
import com.example.drinkwater.databinding.FragmentHomeBinding
import com.example.drinkwater.ui.dashboard.SharedViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    lateinit var myDataStore: DataStoreProvider
    private lateinit var sharedViewModel: SharedViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    companion object {
//        const val CHANNEL_ID = "MyAppChannel"
//        const val NOTIFICATION_ID = 101
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Fragment之间通过传入同一个Activity来共享ViewModel
        sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textGoal: TextView = binding.textGoal

        // Listen to changes in settings
        sharedViewModel.itemLiveData.observe(requireActivity(), { itemStr ->
            textGoal.text = itemStr
        })


        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myDataStore = DataStoreProvider(requireContext())

        val buttonIncrease: Button = binding.btnIncrease
        val buttonDecrease: Button = binding.btnDecrease
        val buttonAdd: Button = binding.btnAdd

        val textAmount: TextView = binding.textAmount
        val textTotalAmount: TextView = binding.textTotalAmount
        val textDailyGoal: TextView = binding.textGoal
        val imgBottle: ImageView = binding.homeImg
        var amountTobeAdded = 0

        val currentDate = LocalDate.now()
        val currentDateTime = LocalDateTime.now()
        var time = currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))

        System.out.println("time")
        System.out.println(time)
        myDataStore.updateTotalAmount(0)

        if (time.equals("00:00:00")) {
            myDataStore.updateTotalAmount(0)
        }
        textTotalAmount.setText(myDataStore.getTotalAmount().toString())

        buttonIncrease.setOnClickListener {
            System.out.println("clicked button increase")
            val currentAmount = textAmount.text.toString().toInt()
            val newAmount = currentAmount + 50
            textAmount.setText(newAmount.toString())
            amountTobeAdded = newAmount
        }

        buttonDecrease.setOnClickListener {
            System.out.println("clicked button decrease")

            if (!textAmount.text.toString().equals("0")) {
                val currentAmount = textAmount.text.toString().toInt()
                val newAmount = currentAmount - 50
                textAmount.setText(newAmount.toString())
                amountTobeAdded = newAmount

            }
        }

        buttonAdd.setOnClickListener {
            System.out.println("clicked button add")
            //val currentAmount = textTotalAmount.text.toString().toInt()

            val currentAmount = myDataStore.getTotalAmount()
            System.out.println("total amount before: " + currentAmount)

            val newAmount = currentAmount?.plus(amountTobeAdded)

            if (newAmount != null) {
                myDataStore.updateTotalAmount(newAmount)
            }

            textTotalAmount.setText(myDataStore.getTotalAmount().toString())


            // update image

            val currentTotal = myDataStore.getTotalAmount()
            val currentGoal = myDataStore.getDailyGoal()
            val result = currentTotal?.div(currentGoal!!)

            if (result != null) {
                if (result >= 1) {
                    imgBottle.setImageResource(R.drawable.bottle_10)
                } else if (result >= (9/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_9)
                } else if (result >= (8/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_8)
                } else if (result >= (7/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_7)
                } else if (result >= (6/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_6)
                } else if (result >= (5/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_5)
                } else if (result >= (4/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_4)
                } else if (result >= (3/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_3)
                } else if (result >= (2/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_2)
                } else if (result >= (1/10.0)) {
                    imgBottle.setImageResource(R.drawable.bottle_1)
                } else if (result == 0.0) {
                    imgBottle.setImageResource(R.drawable.bottle_0)
                }
            }

        }

        // Schedule periodic work using WorkManager
        val workRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 1, // Repeat every 1 hour
            repeatIntervalTimeUnit = TimeUnit.HOURS
        ).build()

        val workManager = WorkManager.getInstance(requireContext())
        workManager.enqueueUniquePeriodicWork(
            "my_periodic_notification_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private suspend fun saveAmountToPreferencesStore(amount: Int) {
        System.out.println("saveAmountToPreferencesStore")

//        requireContext().dataStore.edit { preferences ->
//            preferences[TOTAL_AMOUNT] = amount
//            System.out.println("preferences[TOTAL_AMOUNT]... " + preferences[TOTAL_AMOUNT])
//        }

        //myDataStore.saveTotalAmount(amount)

        //System.out.println(myDataStore.readTotalAmount() )
    }

//
//    private fun getIntSync(key: String, defValue: Int): Int =
//        readNonNullData(intPreferencesKey(key), defValue)
//


//    private fun showNotification() {
//        val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                CHANNEL_ID,
//                "MyApp Channel",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//        val notification = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
//            .setContentTitle("MyApp Notification")
//            .setContentText("You clicked the button!")
//            .setSmallIcon(R.drawable.ic_baseline_notifications_24)
//            .build()
//
//        notificationManager.notify(NOTIFICATION_ID, notification)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}