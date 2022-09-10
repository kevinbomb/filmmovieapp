package com.given.filmmovieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import org.w3c.dom.Text

class RegisterActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout
    private lateinit var inputEmail:TextInputLayout
    private lateinit var inputTanggal:TextInputLayout
    private lateinit var noTelp:TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setTitle("Register")

        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        inputEmail=findViewById(R.id.inputLayoutEmail)
        inputTanggal=findViewById(R.id.inputLayoutTanggal)
        noTelp=findViewById(R.id.inputLayoutTelp)
        val btnRegister: Button =findViewById(R.id.btnRegister)

        var usernameLogin:String
        var passwordLogin:String

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
            }else if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }else if(email.isEmpty()){
                inputEmail.setError("Email must be filled with text")
                checkLogin=false
            }else if(tgl.isEmpty()){
                inputTanggal.setError("Tanggal must be filled with text")
                checkLogin=false
            }else if(telp.isEmpty()){
                noTelp.setError("Telp must be filled with text")
                checkLogin=false
            }else{
                checkLogin=true
                usernameLogin=username
                passwordLogin=password
            }


            if(!checkLogin)return@OnClickListener
            val moveHome= Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(moveHome)
        })
    }
}