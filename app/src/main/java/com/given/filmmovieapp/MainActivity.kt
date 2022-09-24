package com.given.filmmovieapp
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog
import com.given.filmmovieapp.room.user.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var inputUsername:TextInputLayout
    private lateinit var inputPassword:TextInputLayout
    private lateinit var mainLayout:ConstraintLayout

    lateinit var tempUsername: String
    lateinit var tempPassword: String

    val dbU by lazy { UserDB(this) }

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setTitle("User Login")

        inputUsername=findViewById(R.id.inputLayoutUsername)
        inputPassword=findViewById(R.id.inputLayoutPassword)
        mainLayout=findViewById(R.id.mainLayout)
        val btnLogin:Button=findViewById(R.id.btnLogin)
        val btnRegister:Button=findViewById(R.id.btnRegister)!!

        getBundle()
        tempUsername=""
        tempPassword=""

        btnLogin.setOnClickListener(View.OnClickListener {
            var checkLogin=false
            getTemp(inputUsername.editText?.text.toString())
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

            if(username == tempUsername && password == tempPassword){
                checkLogin=true
                var nowUsermane: String = tempUsername
                var nowPassword: String = tempPassword
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, nowUsermane)
                editor.putString(passK, nowPassword)
                editor.apply()
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

    fun getBundle(){
        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            inputUsername.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            inputPassword.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }

    fun getTemp(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUserName(str)[0]
            tempUsername = user.username
            tempPassword = user.password
            Toast.makeText(applicationContext, user.username, Toast.LENGTH_SHORT).show()
        }}
}