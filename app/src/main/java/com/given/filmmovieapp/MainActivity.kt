package com.given.filmmovieapp
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername:TextInputLayout
    private lateinit var inputPassword:TextInputLayout
    private lateinit var mainLayout:ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        mainLayout=findViewById(R.id.mainLayout)
        val btnLogin:Button=findViewById(R.id.btnLogin)
        val btnRegister:Button=findViewById(R.id.btnRegister)!!



        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin=false
            val username:String=inputUsername.getEditText()?.getText().toString()
            val password:String=inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username must be filled with text")
                checkLogin=false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password must be filled with text")
                checkLogin=false
            }

            if(username=="admin"&&password=="0561"){
                checkLogin=true
            }else{
                val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
                builder.setTitle("Username atau Password")
                builder.setMessage("Username / Password salah!")
                    .setPositiveButton("oke"){ dialog, which -> }
                    .show()
            }
            if(!checkLogin)return@OnClickListener
            val moveHome=Intent(this@MainActivity,HomeActivity::class.java)
            startActivity(moveHome)
        })

        btnRegister.setOnClickListener{

            val moveRegister=Intent(this,RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }
}