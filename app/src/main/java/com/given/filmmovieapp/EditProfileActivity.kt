package com.given.filmmovieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.given.filmmovieapp.databinding.ActivityEditProfileBinding
import com.given.filmmovieapp.room.user.Constant
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    lateinit var tempUsername: String
    lateinit var tempPassword: String

    var tempId: Int = 0
    var dataUser: Int = 0

    val dbU by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setTitle("Edit Profile")

        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        tempUsername = ""
        tempPassword = ""
        setupView()

        binding.btnSave.setOnClickListener{
            val inputUsername: String = binding?.editUsername?.getEditText()?.getText().toString()
            val inputPassword: String = binding?.editPassword?.getEditText()?.getText().toString()
            val inputEmail: String = binding?.editEmail?.getEditText()?.getText().toString()
            val inputTanggal: String = binding?.editTanggal?.getEditText()?.getText().toString()
            val inputTelepon: String = binding?.editTelp?.getEditText()?.getText().toString()

            if(inputUsername.isEmpty()){
                binding?.editUsername?.setError("Username Kosong")
            }

            if(inputPassword.isEmpty()){
                binding?.editPassword?.setError("Password Kosong")
            }

            if(inputEmail.isEmpty()){
                binding?.editEmail?.setError("Email Kosong")
            }

            if(inputTanggal.isEmpty()){
                binding?.editTanggal?.setError("Tanggal Lahir Tidak Boleh Kosong")
            }

            if(inputTelepon.isEmpty()){
                binding?.editTelp?.setError("Nomor Telepon Kosong")
            }

            if(!inputUsername.isEmpty() && !inputPassword.isEmpty() && !inputEmail.isEmpty() && !inputTelepon.isEmpty()) {
                CoroutineScope(Dispatchers.IO).launch {
                    dbU.userDao().updateUser(
                        User(
                            tempId,
                            binding.editUsername.editText?.text.toString(),
                            binding.editPassword.editText?.text.toString(),
                            binding.editEmail.editText?.text.toString(),
                            binding.editTanggal.editText?.text.toString(),
                            binding.editTelp.editText?.text.toString()
                        )
                    )
                    finish()
                    val intent= Intent(this@EditProfileActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }


    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_UPDATE -> {
                getUser()
            }
        }
    }

    fun getUser(){
        dataUser = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUserId(dataUser)[0]
            binding.editEmail.editText?.setText(user.email)
            binding.editUsername.editText?.setText(user.username)
            binding.editTanggal.editText?.setText(user.tanggalLahir)
            binding.editTelp.editText?.setText(user.noTelepon)
            tempId = user.id
            tempPassword = user.password
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}