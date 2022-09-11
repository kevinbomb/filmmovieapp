package com.given.filmmovieapp

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text
import android.widget.DatePicker
import android.widget.EditText
import java.text.SimpleDateFormat
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail:TextInputLayout
    private lateinit var inputTanggal: TextInputLayout
    private lateinit var noTelp:TextInputLayout

    private lateinit var etTanggal:EditText

    var date = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Register")

        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        inputEmail=findViewById(R.id.inputLayoutEmail)
        inputTanggal=findViewById(R.id.inputLayoutTanggal)
        noTelp=findViewById(R.id.inputLayoutTelp)
        etTanggal=findViewById(R.id.etDate)

        val btnRegister: Button =findViewById(R.id.btnRegister)

        val datePicker = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
                date.set(Calendar.YEAR, year)
                date.set(Calendar.MONTH, monthOfYear)
                date.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateEditText()
            }
        }

        etTanggal.setOnClickListener(object : View.OnClickListener{
            override fun onClick(view: View) {
                DatePickerDialog(this@RegisterActivity,
                    datePicker,
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH),
                    date.get(Calendar.DAY_OF_MONTH)).show()
            }
        })

        btnRegister.setOnClickListener(View.OnClickListener {
            var checkLogin=false
            val username:String=inputUsername.getEditText()?.getText().toString()
            val password:String=inputPassword.getEditText()?.getText().toString()
            val email:String=inputEmail.getEditText()?.getText().toString()
            val tgl:String=inputTanggal.getEditText()?.getText().toString()
            val telp:String=noTelp.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin=false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }

            if(email.isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkLogin=false
            }

            if(tgl.isEmpty()){
                inputTanggal.setError("Tanggal must be filled with text")
                checkLogin=false
            }

            if(telp.isEmpty()){
                noTelp.setError("Telp must be filled with text")
                checkLogin=false
            }

            if(!username.isEmpty() && !password.isEmpty()&& !email.isEmpty() && !telp.isEmpty()&& !tgl.isEmpty()){
                checkLogin=true

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
        inputTanggal.getEditText()?.setText(temp)
    }
}