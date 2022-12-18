package com.given.filmmovieapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.given.filmmovieapp.api.UpcomingApi
import com.given.filmmovieapp.api.UserApi
import com.given.filmmovieapp.databinding.ActivityEditProfileBinding
import com.given.filmmovieapp.models.Upcoming
import com.given.filmmovieapp.models.User
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_edit.*
import kotlinx.android.synthetic.main.activity_add_edit.et_tanggal
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class EditProfileActivity : AppCompatActivity() {
    var aidi: Long? = null

    private var etEmail: EditText? = null
    private var etUsername: EditText? = null
    private var etPassword: EditText? = null
    private var etTanggal: EditText? = null
    private var etTelp: EditText? = null

    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        aidi = getSharedPreferences("myPref", Context.MODE_PRIVATE).getLong("id", 0)
        supportActionBar?.setTitle("Edit Profile")

        queue = Volley.newRequestQueue(this)
        etEmail = findViewById(R.id.et_email)
        etUsername = findViewById(R.id.et_username)
        etPassword = findViewById(R.id.et_password)
        etTanggal = findViewById(R.id.et_tanggal)
        etTelp = findViewById(R.id.et_telp)
        layoutLoading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancle)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)

            tvTitle.setText("EDIT PROFILE")
            getUpcomingByid(id)

            btnSave.setOnClickListener { updateUpcoming(id) }

    }


    private fun getUpcomingByid(id: Long) {
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(
                Method.GET,
                UserApi.GET_BY_ID_URL + aidi,
                Response.Listener { response ->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    val user = gson.fromJson(
                        jsonObject.getJSONArray("data")[0].toString(),
                        User::class.java
                    )

                    etEmail!!.setText(user.email)
                    etUsername!!.setText(user.username)
                    etPassword!!.setText(user.password)
                    etTanggal!!.setText(user.tanggalLahir)
                    etTelp!!.setText(user.noTelepon)


                    motionToastSuccess()
                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditProfileActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headres = HashMap<String, String>()
                headres["Accept"] = "application/json"
                return headres
            }
        }
        queue!!.add(stringRequest)
    }

    private fun updateUpcoming(id: Long) {
        setLoading(true)

        val user = User(
            etEmail!!.text.toString(),
            etUsername!!.text.toString(),
            etPassword!!.text.toString(),
            etTanggal!!.text.toString(),
            etTelp!!.text.toString(),
        )

        val stringRequest: StringRequest = object :
            StringRequest(Method.PUT, UserApi.UPDATE_URL + aidi, Response.Listener { response ->
//                val gson = Gson()
//                val jsonObject = JSONObject(response)
//                var upcoming : Upcoming = gson.fromJson(jsonObject.getJSONArray("data")[].toString(), Upcoming::class.java)
//              var upcoming = gson.fromJson(response, Upcoming::class.java)

//                if(upcoming != null)
                Toast.makeText(this@EditProfileActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT)
                    .show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)

                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@EditProfileActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(this@EditProfileActivity, e.message, Toast.LENGTH_SHORT).show()
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
                params["tanggal"] = user.tanggalLahir
                params["telp"] = user.noTelepon
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    private fun motionToastSuccess() {
        MotionToast.createToast(
            this,
            "Sukses",
            "Data berhasil tertampil!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular)
        )

    }

}