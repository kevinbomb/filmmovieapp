package com.given.filmmovieapp
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var inputUsername: TextInputLayout
    private lateinit var inputPassword: TextInputLayout

    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var mainLayout: ConstraintLayout

    val dbU by lazy { UserDB(this) }

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        mainLayout = findViewById(R.id.mainLayout)

        inputUsername = findViewById(R.id.inputLayoutUsername)
        inputPassword = findViewById(R.id.inputLayoutPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister=findViewById(R.id.btnRegister)!!

        getBundle()


        btnLogin.setOnClickListener (View.OnClickListener {
            var cekLogin = false

            val username: String = inputUsername.getEditText()?.getText().toString()
            val password: String = inputPassword.getEditText()?.getText().toString()

            if(username.isEmpty()){
                inputUsername.setError("Username Tidak Boleh Kosong")
                cekLogin = false
            }

            if(password.isEmpty()){
                inputPassword.setError("Password Tidak Boleh Kosong")
                cekLogin = false
            }

            CoroutineScope(Dispatchers.IO).launch {
                var resultCheckUser: List<User> = dbU.userDao().checkUser(username,password)
                println("hasil: " + resultCheckUser)

                if(resultCheckUser.isNullOrEmpty()){
                    Snackbar.make(mainLayout,"Username atau Password Salah!", Snackbar.LENGTH_LONG).show()
                    return@launch
                }

                if(resultCheckUser[0].username.equals(username) && resultCheckUser[0].password.equals(password)){
                    cekLogin=true

                    val intent=Intent(this@MainActivity, HomeActivity::class.java)
                    intent.putExtra("usernameLogin",username)
                    intent.putExtra("idLogin",resultCheckUser[0].id)


                    val editor: SharedPreferences.Editor= sharedPreferencesRegister!!.edit()
                    editor.putString(usernameK,username)
                    editor.putString(passK,password)
                    editor.apply()

                    startActivity(intent)
                }
            }


        })

        btnRegister.setOnClickListener{

            val moveRegister=Intent(this,RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }

    fun getBundle(){

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            inputUsername.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            inputPassword.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }



}