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
    val dbUser by lazy { UserDB(requireContext()) }

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


        val userId= requireActivity().intent.getIntExtra("idLogin",0)
        CoroutineScope(Dispatchers.IO).launch{
            println("user id=" + userId)
            val resultCheckUser: List<User> = dbUser.userDao().getUserId(userId)
            println("hasil=" + resultCheckUser)
            binding.tvUsername.setText(resultCheckUser[0].username)
            binding.tvEmail.setText(resultCheckUser[0].email)
            binding.tvPhone.setText(resultCheckUser[0].noTelepon)
            binding.tvDate.setText(resultCheckUser[0].tanggalLahir)

        }

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