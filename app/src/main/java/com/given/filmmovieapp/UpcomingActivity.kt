package com.given.filmmovieapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.given.filmmovieapp.adapters.UpcomingAdapter
import com.given.filmmovieapp.api.UpcomingApi
import com.given.filmmovieapp.models.Upcoming
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets

class UpcomingActivity : AppCompatActivity() {
    private var srUpcoming: SwipeRefreshLayout? = null
    private var adapter: UpcomingAdapter? = null
    private var svUpcoming: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null

    companion object{
        const val LAUNCH_ADD_ACTIVITY = 123
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming)

        queue = Volley.newRequestQueue(this )
        layoutLoading = findViewById(R.id.layout_loading)
        srUpcoming = findViewById(R.id.sr_upcoming)
        svUpcoming =findViewById(R.id.sv_upcoming)

        srUpcoming?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allUpcoming() })
        svUpcoming?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })

        val fabAdd = findViewById<FloatingActionButton>(R.id.fab_add)
        fabAdd.setOnClickListener{
            val i = Intent(this@UpcomingActivity, AddEditActivity::class.java)
            startActivityForResult(i, LAUNCH_ADD_ACTIVITY)
        }

        val rvProduk = findViewById<RecyclerView>(R.id.rv_upcoming)
        adapter = UpcomingAdapter(ArrayList(), this)
        rvProduk.layoutManager = LinearLayoutManager(this)
        rvProduk.adapter = adapter
        allUpcoming()
    }

    private fun allUpcoming(){
        srUpcoming!!.isRefreshing  = true
        val stringRequest: StringRequest = object :
            StringRequest(Method.GET, UpcomingApi.GET_ALL_URL, Response.Listener { response ->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                var upcoming : Array<Upcoming> = gson.fromJson(jsonObject.getJSONArray("data").toString(), Array<Upcoming>::class.java)

                adapter!!.setUpcomingList(upcoming)
                adapter!!.filter.filter(svUpcoming!!.query)
                srUpcoming!!.isRefreshing = false

                if(!upcoming.isEmpty())
                   motionToastSuccess()
                else
                    motionToastFailed()
            }, Response.ErrorListener { error ->
                srUpcoming!!.isRefreshing = false
                AlertDialog.Builder(this)
                    .setMessage(error.message)
                    .show()
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpcomingActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this@UpcomingActivity, e.message, Toast.LENGTH_SHORT).show()
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

    fun deleteUpcoming(id: Long){
        setLoading(true)
        val stringRequest: StringRequest = object:
            StringRequest(Method.DELETE, UpcomingApi.DELETE_URL + id, Response.Listener { response ->
                setLoading(false)

                val gson = Gson()
                var upcoming = gson.fromJson(response, Upcoming::class.java)
                if(upcoming != null)
                    Toast.makeText(this@UpcomingActivity, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
                allUpcoming()
            }, Response.ErrorListener { error ->
                setLoading(false)
                try {
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@UpcomingActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: java.lang.Exception){
                    Toast.makeText(this@UpcomingActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(stringRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == LAUNCH_ADD_ACTIVITY && resultCode == RESULT_OK) allUpcoming()
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
            layoutLoading!!.visibility = View.GONE
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

    private fun motionToastFailed(){
        MotionToast.createToast(this,
            "Failed ️",
            "Data gagal tertampil!",
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
    }
}