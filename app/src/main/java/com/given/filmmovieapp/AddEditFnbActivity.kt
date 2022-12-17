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
import com.given.filmmovieapp.api.FnbApi
import com.given.filmmovieapp.models.Fnb
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_edit_fnb.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class AddEditFnbActivity : AppCompatActivity() {
    private var etNamaFnb: EditText? = null
    private var etJenisFnb: EditText? = null
    private var etJumlahFnb: EditText? = null
    private var etHargaFnb: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_fnb)

        queue = Volley.newRequestQueue(this)
        etNamaFnb = findViewById(R.id.et_namafnb)
        etJenisFnb = findViewById(R.id.et_jenisfnb)
        etJumlahFnb = findViewById(R.id.et_jumlahfnb)
        etHargaFnb = findViewById(R.id.et_hargafnb)
        layoutLoading = findViewById(R.id.layout_loading)



        val btnCancel = findViewById<Button>(R.id.btn_cancle)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id", -1)
        if(id == -1L){
            tvTitle.setText("Tambah FNB Bioskop")
            btnSave.setOnClickListener {
                if(et_namafnb!!.text.toString().isEmpty()){
                    MotionToast.createColorToast(this@AddEditFnbActivity,
                        "Failed ☹️",
                        "Nama FNB tidak boleh kosong!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }else if (et_jenisfnb!!.text.toString().isEmpty()){
                    MotionToast.createColorToast(this@AddEditFnbActivity,
                        "Failed ☹️",
                        "Jenis FNB tidak boleh kosong!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }else if(et_jumlahfnb!!.text.toString().isEmpty()){
                    MotionToast.createColorToast(this@AddEditFnbActivity,
                        "Failed ☹️",
                        "Jumlah FNB boleh kosong!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }else if(et_hargafnb!!.text.toString().isEmpty()){
                    MotionToast.createColorToast(this@AddEditFnbActivity,
                        "Failed ☹️",
                        "Harga FNB boleh kosong!",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
                }else {
                    createFnb()
                    val moveFnb= Intent(this,FnbActivity::class.java)
                    startActivity(moveFnb)
                }
            }
        } else  {
            tvTitle.setText("Edit FNB Bioskop")
            getFnbByid(id)

            btnSave.setOnClickListener { updateFnb(id) }
        }
    }



    private fun getFnbByid(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, FnbApi.GET_BY_ID_URL + id, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val fnb = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), Fnb::class.java)

                etNamaFnb!!.setText(fnb.namafnb)
                etJenisFnb!!.setText(fnb.jenisfnb)
                etJumlahFnb!!.setText(fnb.jumlahfnb)
                etHargaFnb!!.setText(fnb.hargafnb)


                motionToastSuccess()
                setLoading(false)
            }, Response.ErrorListener { error ->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@AddEditFnbActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@AddEditFnbActivity, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createFnb(){
        setLoading(true)


        val fnb = Fnb(
            etNamaFnb!!.text.toString(),
            etJenisFnb!!.text.toString(),
            etJumlahFnb!!.text.toString(),
            etHargaFnb!!.text.toString(),
        )

        val stringRequest: StringRequest =
            object :
                StringRequest(Method.POST, FnbApi.ADD_URL, Response.Listener { response ->
                    val gson = Gson()

                    // val jsonObject = JSONObject(response)

                    // val respond = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), Fnb::class.java)
                    val respond = gson.fromJson(response, Fnb::class.java)

//                var fnb = gson.fromJson(response, Fnb::class.java)

                    if (fnb != null)
                        Toast.makeText(
                            this@AddEditFnbActivity,
                            "Data berhasil ditambahkan",
                            Toast.LENGTH_SHORT
                        ).show()

                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                }, Response.ErrorListener { error ->

                    setLoading(false)
                    try {
                        val respondBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(respondBody)
                        Toast.makeText(
                            this@AddEditFnbActivity, errors.getString("messsage"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        e.message
                        Toast.makeText(this@AddEditFnbActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
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
                    params["namafnb"] = fnb.namafnb
                    params["jenisfnb"] = fnb.jenisfnb
                    params["jumlahfnb"] = fnb.jumlahfnb
                    params["hargafnb"] = fnb.hargafnb
                    return params
                }
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(fnb)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
//
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
            }
        queue!!.add(stringRequest)


    }

    private fun updateFnb(id:Long){
        setLoading(true)

        val fnb = Fnb(
            etNamaFnb!!.text.toString(),
            etJenisFnb!!.text.toString(),
            etJumlahFnb!!.text.toString(),
            etHargaFnb!!.text.toString(),
        )

        val stringRequest : StringRequest = object :
            StringRequest(Method.PUT, FnbApi.UPDATE_URL + id, Response.Listener { response ->
//                val gson = Gson()
//                val jsonObject = JSONObject(response)
//                var fnb : Fnb = gson.fromJson(jsonObject.getJSONArray("data")[].toString(), Fnb::class.java)
//              var fnb = gson.fromJson(response, Fnb::class.java)

//                if(fnb != null)
                Toast.makeText(this@AddEditFnbActivity, "Data berhasil diupdate", Toast.LENGTH_SHORT).show()

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
                        this@AddEditFnbActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e:Exception){
                    Toast.makeText(this@AddEditFnbActivity, e.message, Toast.LENGTH_SHORT).show()
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
                params["namafnb"] = fnb.namafnb
                params["jenisfnb"] = fnb.jenisfnb
                params["jumlahfnb"] = fnb.jumlahfnb
                params["hargafnb"] = fnb.hargafnb
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