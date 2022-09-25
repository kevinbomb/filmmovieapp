package com.given.filmmovieapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.given.filmmovieapp.databinding.ActivityEditUpcomingBinding
import com.given.filmmovieapp.room.upcoming.Constant
import com.given.filmmovieapp.room.upcoming.Upcoming
import com.given.filmmovieapp.room.upcoming.UpcomingDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUpcomingActivity : AppCompatActivity() {
    val db by lazy { UpcomingDB(this) }
    private var num: Int = 0
    private lateinit var binding: ActivityEditUpcomingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_upcoming)
        supportActionBar?.setTitle("Upcoming Movies")

        binding = ActivityEditUpcomingBinding.inflate(layoutInflater)
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
            }
        }

        binding.btnUpdate.setOnClickListener{

            CoroutineScope(Dispatchers.IO).launch {
                db.upcomingDao().updateUpcoming(
                    Upcoming(num, binding.inputJudul.editText?.text.toString(), binding.inputDirektur.editText?.text.toString(),
                        binding.inputTanggal.editText?.text.toString(), binding.inputSinopsis.editText?.text.toString())
                )
                finish()
            }
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