package com.given.filmmovieapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.given.filmmovieapp.api.UserApi
import com.given.filmmovieapp.databinding.FragmentProfileBinding
import com.given.filmmovieapp.models.Upcoming
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.nio.charset.StandardCharsets

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {
//    val dbUser by lazy { UserDB(requireContext()) }
    private var queue: RequestQueue? = null

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
//    lateinit var viewUsername: TextView
//    lateinit var viewEmail: TextView
//    lateinit var viewTelepon: TextView
//    lateinit var viewTanggal: TextView
//
//    lateinit var btnCamera: ImageView
//    lateinit var btnUpdate:Button




    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(getActivity()?.getApplicationContext())
        val aidi:Long = requireActivity().getSharedPreferences("myPref", Context.MODE_PRIVATE).getLong("id", 0)

        val userId= requireActivity().intent.getIntExtra("idLogin",0)
//        CoroutineScope(Dispatchers.IO).launch{
//            println("user id=" + userId)
//            val resultCheckUser: List<User> = dbUser.userDao().getUserId(userId)
//            println("hasil=" + resultCheckUser)
//            binding.tvUsername.setText(resultCheckUser[0].username)
//            binding.tvEmail.setText(resultCheckUser[0].email)
//            binding.tvPhone.setText(resultCheckUser[0].noTelepon)
//            binding.tvDate.setText(resultCheckUser[0].tanggalLahir)
//
//        }

        val stringRequest : StringRequest = object:
            StringRequest(Method.GET, UserApi.GET_BY_ID_URL + aidi, Response.Listener { response ->

                val gson = Gson()
                val jsonObject = JSONObject(response)
                val user = gson.fromJson(jsonObject.getJSONArray("data")[0].toString(), User::class.java)

                binding.tvUsername.text = user.username
                binding.tvEmail.text = user.email
                binding.tvPhone.text = user.noTelepon
                binding.tvDate.text = user.tanggalLahir
//                val tempId = userId
            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        getActivity()?.getApplicationContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(getActivity()?.getApplicationContext(), e.message, Toast.LENGTH_SHORT).show()
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

        val tempId = userId

        binding.buttonEdit.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
                    .putExtra("intent_id", tempId)
                    .putExtra("intent_type", 2)
            )
        }
        binding.buttonTiket.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, CetakTiket::class.java)

            )
        }
        binding.buttonPromo.setOnClickListener {
            startActivity(
                Intent(requireActivity().applicationContext, CekPromo::class.java)

            )
        }
        binding.imageView3.setOnClickListener {
            openCamera()
        }

    }

    fun openCamera(){
        startActivity(
            Intent(requireActivity().applicationContext, CameraActivity::class.java)
        )
    }

}