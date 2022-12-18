package com.given.filmmovieapp

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.textfield.TextInputLayout
import android.widget.DatePicker
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.given.filmmovieapp.api.UpcomingApi
import com.given.filmmovieapp.api.UserApi
import com.given.filmmovieapp.databinding.ActivityRegisterBinding
import com.given.filmmovieapp.models.Upcoming
import com.given.filmmovieapp.models.User
import com.given.filmmovieapp.notification.NotificationReceiver
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
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

//    val dbU by lazy { UserDB(this) }
    private lateinit var binding: ActivityRegisterBinding

    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesRegister: SharedPreferences? = null
    private var queue: RequestQueue? = null

    private val CHANNEL_ID_1 = "channel_notification_01"
    private val notificationId1 = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register)
        supportActionBar?.hide()

        queue = Volley.newRequestQueue(this)
        //binding view==============
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTitle("Register")
        createNotificationChannelRegister()
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
                MotionToast.createColorToast(this@RegisterActivity,
                    "Failed ☹️",
                    "Username tidak boleh kosong!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                checkLogin=false
            }

            else if(password.isEmpty()){
                MotionToast.createColorToast(this@RegisterActivity,
                    "Failed ☹️",
                    "Password tidak boleh kosong!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                checkLogin=false
            }

            else if(email.isEmpty()){
                MotionToast.createColorToast(this@RegisterActivity,
                    "Failed ☹️",
                    "Email tidak boleh kosong!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                checkLogin=false
            }

            else if(telp.isEmpty()){
                MotionToast.createColorToast(this@RegisterActivity,
                    "Failed ☹️",
                    "Nomor Telepon tidak boleh kosong!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                checkLogin=false
            }

            else if(tgl.isEmpty()){
                MotionToast.createColorToast(this@RegisterActivity,
                    "Failed ☹️",
                    "Tanggal tidak boleh kosong!",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                checkLogin=false
            }



            else if(!username.isEmpty() && !password.isEmpty()&& !email.isEmpty() && !telp.isEmpty()&& !tgl.isEmpty()){
                checkLogin=true

                val user = User(
                    username,
                    email,
                    password,
                    tgl,
                    telp,
                )

                val stringRequest: StringRequest =
                    object : StringRequest(Method.POST, UserApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()

                       // val jsonObject = JSONObject(response)

                        //val user = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), User::class.java)

                        val respond = gson.fromJson(response, User::class.java)
//                var upcoming = gson.fromJson(response, Upcoming::class.java)

                        if(user != null)
                            Toast.makeText( this@RegisterActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                    }, Response.ErrorListener { error ->

                        try {
                            val respondBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(respondBody)
                            Toast.makeText(
                                this@RegisterActivity, errors.getString("messsage"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception){e.message
                            Toast.makeText(this@RegisterActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        @Throws(AuthFailureError::class)
                        override fun getHeaders(): Map<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Accept"] = "application/json"
                            return headers
                        }

                        override fun getParams(): MutableMap<String, String>? {
                            val params = HashMap<String, String>()
                            params["email"] = user.email
                            params["username"] = user.username
                            params["password"] = user.password
                            params["tanggalLahir"] = user.tanggalLahir
                            params["noTelepon"] = user.noTelepon
                            return params
                        }
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(upcoming)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
//
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
                    }
                queue!!.add(stringRequest)

                //save to shareP
                var strUserName: String = binding.inputLayoutUsername.editText?.text.toString().trim()
                var strPass: String = binding.inputLayoutPassword.editText?.text.toString().trim()
                val editor: SharedPreferences.Editor = sharedPreferencesRegister!!.edit()
                editor.putString(usernameK, strUserName)
                editor.putString(passK, strPass)
                editor.apply()

                sendNotificationSucessRegister()
            }


            if(!checkLogin)return@OnClickListener
            val moveHome= Intent(this@RegisterActivity,MainActivity::class.java)
            startActivity(moveHome)
        })
    }

    private fun createNotificationChannelRegister(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val title = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, title, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }

            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotificationSucessRegister(){
        val intent: Intent = Intent(this, RegisterActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        val broadcastIntent: Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "Register Done")
        val actionIntent: PendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.ic_baseline_done_24)
            .setContentTitle("Pengguna dengan username " + binding?.inputLayoutUsername?.editText?.text.toString() + " berhasil registrasi!!")
            .setContentText("Silahkan Login ke Aplikasi kami!")
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .setStyle(
                NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.logo)))
            .setColor(Color.RED)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
            .addAction(R.mipmap.ic_launcher, "TOAST", actionIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
    }

    private fun updateEditText(){
        var temp : String
        val format = "dd/MM/yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.US)
        temp = simpleDateFormat.format(date.getTime())
        binding?.inputLayoutTanggal?.getEditText()?.setText(temp)
    }
}