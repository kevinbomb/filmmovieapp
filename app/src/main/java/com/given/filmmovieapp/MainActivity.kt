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
import android.widget.LinearLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.given.filmmovieapp.api.UserApi
import com.given.filmmovieapp.databinding.ActivityMainBinding
import com.given.filmmovieapp.databinding.ActivityRegisterBinding
import com.given.filmmovieapp.models.Upcoming
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class MainActivity : AppCompatActivity() {
//    private lateinit var inputUsername: TextInputLayout
//    private lateinit var inputPassword: TextInputLayout
//
//    private lateinit var btnLogin: Button
//    private lateinit var btnRegister: Button
//    private lateinit var mainLayout: ConstraintLayout

    val dbU by lazy { UserDB(this) }
    private lateinit var binding: ActivityMainBinding

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        mainLayout = findViewById(R.id.mainLayout)
//
//        inputUsername = findViewById(R.id.inputLayoutUsername)
//        inputPassword = findViewById(R.id.inputLayoutPassword)
//        btnLogin = findViewById(R.id.btnLogin)
//        btnRegister=findViewById(R.id.btnRegister)!!

        getBundle()


        binding?.btnLogin?.setOnClickListener (View.OnClickListener {
            var cekLogin = false

            val username: String = binding?.inputLayoutUsername?.getEditText()?.getText().toString()
            val password: String = binding?.inputLayoutPassword?.getEditText()?.getText().toString()

            if(username.isEmpty()){
                binding?.inputLayoutUsername?.setError("Username Tidak Boleh Kosong")
                cekLogin = false
            }

            if(password.isEmpty()){
                binding?.inputLayoutPassword?.setError("Password Tidak Boleh Kosong")
                cekLogin = false
            }

            allUser(username,password)

        })

        binding?.btnRegister?.setOnClickListener{

            val moveRegister=Intent(this,RegisterActivity::class.java)
            startActivity(moveRegister)
        }
    }

    fun getBundle(){

        sharedPreferencesRegister = getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesRegister!!.contains(usernameK)){
            binding?.inputLayoutUsername?.getEditText()?.setText(sharedPreferencesRegister!!.getString(usernameK, ""))
        }
        if (sharedPreferencesRegister!!.contains(passK)){
            binding?.inputLayoutPassword?.getEditText()?.setText(sharedPreferencesRegister!!.getString(passK, ""))
        }
    }

    private fun allUser(username:String, password:String){
        val stringRequest : StringRequest = object:
            StringRequest(Method.GET, UserApi.GET_ALL_URL, Response.Listener { response ->

                val gson = Gson()
                val jsonObject = JSONObject(response)
                var user : Array<User> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<User>::class.java)

                for (u in user) {
                    if (u.username == username && u.password == password) {
                        // berhasil login
                        val intent = Intent(this@MainActivity, HomeActivity::class.java)
                        intent.putExtra("usernameLogin", username)
                        intent.putExtra("idLogin", u.id)


                        val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                        editor.putString(usernameK, username)
                        editor.putString(passK, password)
                        editor.putInt("id", u.id)
                        editor.apply()

                        startActivity(intent)
                        return@Listener
                    }
                }

                // gagal login
                if(!user.isEmpty())
                    Toast.makeText(this@MainActivity, "Data berhasil diambil", Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(this@MainActivity, "Data Kosong!", Toast.LENGTH_SHORT).show()

            }, Response.ErrorListener { error ->
                try {
                    val responseBody =
                        String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(this@MainActivity, errors.getString("message"), Toast.LENGTH_SHORT).show()
                } catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

        }
        queue!!.add(stringRequest)
    }



}