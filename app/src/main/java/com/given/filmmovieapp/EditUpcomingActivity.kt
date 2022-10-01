package com.given.filmmovieapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.given.filmmovieapp.databinding.ActivityEditUpcomingBinding
import com.given.filmmovieapp.notification.NotificationReceiver
import com.given.filmmovieapp.room.upcoming.Constant
import com.given.filmmovieapp.room.upcoming.Upcoming
import com.given.filmmovieapp.room.upcoming.UpcomingDB
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUpcomingActivity : AppCompatActivity() {
    val db by lazy { UpcomingDB(this) }
    private var num: Int = 0
    private lateinit var binding: ActivityEditUpcomingBinding
    private lateinit var inputTgl: TextInputLayout

    private var CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId2 = 102



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_upcoming)
        supportActionBar?.setTitle("Upcoming Movies")

        binding = ActivityEditUpcomingBinding.inflate(layoutInflater)
        inputTgl=binding.inputTanggal
        val view = binding.root
        setContentView(view)
        setupView()
        setupListener()
    }

    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when(intentType){
            Constant.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                binding.btnUpdate.visibility = View.GONE
                binding.btnSave.visibility = View.GONE
                getUpcoming()
            }
            Constant.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                getUpcoming()
            }
        }
    }

    fun setupListener(){
        binding.btnSave.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch {
                db.upcomingDao().addUpcoming(
                    Upcoming(0, binding.inputJudul.editText?.text.toString(), binding.inputDirektur.editText?.text.toString(),
                        binding.inputTanggal.editText?.text.toString(), binding.inputSinopsis.editText?.text.toString())
                )
                finish()
                sendNotificationSuccessEdit()
            }
        }

        binding.btnUpdate.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch {
                db.upcomingDao().updateUpcoming(
                    Upcoming(num, binding.inputJudul.editText?.text.toString(), binding.inputDirektur.editText?.text.toString(),
                        binding.inputTanggal.editText?.text.toString(), binding.inputSinopsis.editText?.text.toString())
                )
                finish()
                sendNotificationSuccessEdit()
            }
        }

    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotificationSuccessEdit(){
        val intent : Intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", binding.inputJudul.editText?.text.toString()+" segera tayang")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val icon: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_movie_creation_24)
            .setContentTitle("Update Film Terbaru")
            .setContentText("Ayo di expand biar ga penasaran!!")
            .setLargeIcon(icon)
            .setStyle(
                NotificationCompat.BigTextStyle()
                .bigText(getString(R.string.updateReminder))
                .setBigContentTitle(binding.inputJudul.editText?.text.toString()+ " : " + binding.inputTanggal.editText?.text.toString())
                .setSummaryText("Film baru " + binding.inputDirektur.editText?.text.toString()))
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId2, builder.build())
        }
    }

    fun getUpcoming(){
        num = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch {
            val upcoming = db.upcomingDao().getUserId(num)[0]
            binding.inputJudul.getEditText()?.setText(upcoming.judul)
            binding.inputDirektur.getEditText()?.setText(upcoming.direktur)
            binding.inputTanggal.getEditText()?.setText(upcoming.tanggal)
            binding.inputSinopsis.getEditText()?.setText(upcoming.sinopsis)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}