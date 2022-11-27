package com.given.filmmovieapp

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
import com.given.filmmovieapp.models.Upcoming
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditActivity : AppCompatActivity() {


    private var etJudul: EditText? = null
    private var etDirektur: EditText? = null
    private var etTanggal: EditText? = null
    private var etSinopsis: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        queue = Volley.newRequestQueue(this)
        etJudul = findViewById(R.id.et_judul)
        etDirektur = findViewById(R.id.et_direktur)
        etTanggal = findViewById(R.id.et_tanggal)
        etSinopsis = findViewById(R.id.et_sinopsis)
        layoutLoading = findViewById(R.id.layout_loading)



        val btnCancel = findViewById<Button>(R.id.btn_cancle)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id == -1L){
            tvTitle.setText("Tambah Upcoming Film")
            btnSave.setOnClickListener { createUpcoming() }
        } else  {
            tvTitle.setText("Edit Upcoming Film")
            getUpcomingByid(id)

            btnSave.setOnClickListener { updateUpcoming(id) }
        }
    }



    private fun getUpcomingByid(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UpcomingApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val upcoming = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), Upcoming::class.java)

                etJudul!!.setText(upcoming.judul)
                etDirektur!!.setText(upcoming.direktur)
                etTanggal!!.setText(upcoming.tanggal)
                etSinopsis!!.setText(upcoming.sinopsis)


                motionToastSuccess()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headres = HashMap<String, String>()
                headres["Accept"] = "application/json"
                return headres
            }
        }
        queue!!.add(stringRequest)
    }

    private fun createUpcoming(){
        setLoading(true)

        val upcoming = Upcoming(
            etJudul!!.text.toString(),
            etDirektur!!.text.toString(),
            etTanggal!!.text.toString(),
            etSinopsis!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object : StringRequest(Method.POST, UpcomingApi.ADD_URL, Response.Listener { response ->
                val gson = Gson()

                val jsonObject = JSONObject(response)

                val upcoming = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), Upcoming::class.java)


//                var upcoming = gson.fromJson(response, Upcoming::class.java)

                if(upcoming != null)
                    Toast.makeText( this@AddEditActivity, "Data berhasil ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error ->

                setLoading(false)
                try {
                    val respondBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(respondBody)
                    Toast.makeText(
                        this@AddEditActivity, errors.getString("messsage"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){e.message
                            Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
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
                    params["judul"] = upcoming.judul
                    params["direktur"] = upcoming.direktur
                    params["tanggal"] = upcoming.tanggal
                    params["sinopsis"] = upcoming.sinopsis
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
    }

    private fun updateUpcoming(id:Long){
        setLoading(true)

        val upcoming = Upcoming(
            etJudul!!.text.toString(),
            etDirektur!!.text.toString(),
            etTanggal!!.text.toString(),
            etSinopsis!!.text.toString(),
        )

        val stringRequest : StringRequest = object :
            StringRequest(Method.PUT, UpcomingApi.UPDATE_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val jsonObject = JSONObject(response)
//                var upcoming : Upcoming = gson.fromJson(jsonObject.getJSONArray("data")[].toString(), Upcoming::class.java)
//              var upcoming = gson.fromJson(response, Upcoming::class.java)

//                if(upcoming != null)
                    Toast.makeText(this@AddEditActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

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
                        this@AddEditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): MutableMap<String, String>? {
                val params = HashMap<String, String>()
                params["judul"] = upcoming.judul
                params["direktur"] = upcoming.direktur
                params["tanggal"] = upcoming.tanggal
                params["sinopsis"] = upcoming.sinopsis
                return params
            }
        }
        queue!!.add(stringRequest)
    }

    private fun setLoading(isLoading: Boolean){
        if(isLoading){
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
        MotionToast.createToast(this,
            "Sukses",
            "Data berhasil tertampil!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

    }

}