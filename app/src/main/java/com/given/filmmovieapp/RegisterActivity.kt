package com.given.filmmovieapp

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import android.widget.DatePicker
import com.given.filmmovieapp.databinding.ActivityRegisterBinding
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
//    private lateinit var inputUsername: TextInputLayout
//    private lateinit var inputPassword: TextInputLayout
//    private lateinit var inputEmail:TextInputLayout
//    private lateinit var inputTanggal: TextInputLayout
//    private lateinit var noTelp:TextInputLayout
//    private lateinit var etTanggal:EditText

    var date = Calendar.getInstance()

    val dbU by lazy { UserDB(this) }
    private lateinit var binding: ActivityRegisterBinding

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        //binding view==============
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Register")

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
//        inputUsername=findViewById(R.id.inputLayoutUsername)
//        inputPassword=findViewById(R.id.inputLayoutPassword)
//        inputEmail=findViewById(R.id.inputLayoutEmail)
//        inputTanggal=findViewById(R.id.inputLayoutTanggal)
//        noTelp=findViewById(R.id.inputLayoutTelp)
//        etTanggal=findViewById(R.id.etDate)
//        val btnRegister: Button =findViewById(R.id.btnRegister)

        val datePicker = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }
        binding?.etDate?.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    datePicker,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        binding?.btnRegister?.setOnClickListener(View.OnClickListener {
            var checkLogin=false
            val username:String=binding?.inputLayoutUsername?.getEditText()?.getText().toString()
            val password:String=binding?.inputLayoutPassword?.getEditText()?.getText().toString()
            val email:String=binding?.inputLayoutEmail?.getEditText()?.getText().toString()
            val tgl:String=binding?.inputLayoutTanggal?.getEditText()?.getText().toString()
            val telp:String=binding?.inputLayoutTelp?.getEditText()?.getText().toString()

            if(username.isEmpty()){
                binding?.inputLayoutUsername?.setError("Username must be filled with text")
                checkLogin=false
            }

            if(password.isEmpty()){
                binding?.inputLayoutPassword?.setError("Password must be filled with text")
                checkLogin=false
            }

            if(email.isEmpty()){
                binding?.inputLayoutEmail?.setError("Email must be filled with text")
                checkLogin=false
            }

            if(tgl.isEmpty()){
                binding?.inputLayoutTanggal?.setError("Tanggal must be filled with text")
                checkLogin=false
            }

            if(telp.isEmpty()){
                binding?.inputLayoutTelp?.setError("Telp must be filled with text")
                checkLogin=false
            }

            if(!username.isEmpty() && !password.isEmpty()&& !email.isEmpty() && !telp.isEmpty()&& !tgl.isEmpty()){
                checkLogin=true

                CoroutineScope(Dispatchers.IO).launch {
                    dbU.userDao().addUser(
                        User(0, email, username, password, tgl, telp)
                    )
                    finish()
                }

                //save to shareP
                var strUserName: String = binding.inputLayoutUsername.editText?.text.toString().trim()
                var strPass: String = binding.inputLayoutPassword.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()
            }


            if(!checkLogin)return@OnClickListener
            val moveHome= Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(moveHome)
        })
    }

    private fun updateEditText(){
        var temp : String
        val format = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        temp = simpleDateFormat.format(date.getTime())
        binding?.inputLayoutTanggal?.getEditText()?.setText(temp)
    }
}