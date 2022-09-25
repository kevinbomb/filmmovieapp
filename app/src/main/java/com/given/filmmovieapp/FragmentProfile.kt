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
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.given.filmmovieapp.databinding.ActivityHomeBinding
import com.given.filmmovieapp.databinding.FragmentProfileBinding
import com.given.filmmovieapp.room.user.User
import com.given.filmmovieapp.room.user.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [FragmentProfile.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentProfile : Fragment() {
    //database
    val dbU by lazy { UserDB(requireActivity()) }

    private var _binding: FragmentProfileBinding? = null

    private val binding get() = _binding!!
    var showUsername: String? = null
    var showPass: String? = null
    var tempID: Int = 0

    lateinit var buttonEdit:Button
    private val myPreference = "myPref"
    private val usernameK = "usernameKey"
    private val passK = "passKey"
    var sharedPreferencesProfile: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        getspData()
        getProfileData(showUsername.toString())



//        binding.buttonEdit.setOnClickListener{
//            intentEdit(tempID, 2)
//        }
    }

    fun getspData(){
        sharedPreferencesProfile = this.getActivity()?.getSharedPreferences(myPreference, Context.MODE_PRIVATE)
        if (sharedPreferencesProfile!!.contains(usernameK)){
            showUsername = sharedPreferencesProfile!!.getString(usernameK, "")
        }
        if (sharedPreferencesProfile!!.contains(passK)){
            showPass = sharedPreferencesProfile!!.getString(passK, "")
        }
    }

    private fun getProfileData(str: String){
        CoroutineScope(Dispatchers.Main).launch {
            val user = dbU.userDao().getUserName(str)[0]
            binding.tvUsername.setText(user.username)
            binding.tvDate.setText((user.tanggalLahir))
            binding.tvPhone.setText(user.noTelepon)
            binding.tvEmail.setText(user.email)

            tempID = user.id
        }
    }



//    fun intentEdit(id_input: Int, intentType: Int){
//        startActivity(
//            Intent(requireActivity().applicationContext, EditProfileActivity::class.java)
//                .putExtra("intent_id", id_input)
//                .putExtra("intent_type", intentType)
//        )
//    }
}