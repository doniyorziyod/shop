package uz.ictschool.shop.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.button.MaterialButton
import com.google.gson.Gson
import uz.ictschool.shop.R
import uz.ictschool.shop.databinding.FragmentProfileBinding
import uz.ictschool.shop.models.Profile
import uz.ictschool.shop.services.SharedPreference

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    var profile : Profile? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        binding.profileBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
        isProfile()

        if (profile == null) setNoAccount()
        else setProfile()
        drawerListener()
        return binding.root
    }

    private fun drawerListener() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_profile -> {
                        findNavController().navigate(R.id.profileFragment)
                    }
                    R.id.menu_info -> {
                        var  alert= Dialog(requireContext())
                        val inflater = LayoutInflater.from(requireContext())
                        var view = inflater.inflate(R.layout.about_dialog,null)
                        alert.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        alert.setContentView(view)
                        alert.show()
                    }
                    R.id.menu_logout -> {
                        val shared = SharedPreference.getInstance(requireContext())
                        shared.logout()
                        setNoAccount()
                    }
                }
                true
            }
        }
    }

    fun isProfile() {
        val shared = SharedPreference.getInstance(requireContext())
        profile = shared.getProfile()
    }

    @SuppressLint("SetTextI18n")
    fun setProfile() {
        binding.profileUsername.text = profile!!.firstName + " " + profile!!.lastName
        binding.profileEmail.text = profile!!.email
        binding.profilePicture.load(profile!!.image)
        setAlert()
    }

    private fun setAlert() {
        binding.profileLogout.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Do you really want to log out?")

            builder.setPositiveButton("Yes") { _, _ ->
                val shared = SharedPreference.getInstance(requireContext())
                shared.logout()
                setNoAccount()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }
    }

    fun setNoAccount(){
        val log: MaterialButton = binding.profileLogout
        binding.profilePicture.setImageResource(R.drawable.profile_icon)
        binding.profileUsername.text = "You Are Not Logged In Yet"
        binding.profileEmail.text = "Please Log In"
        log.setBackgroundColor(resources.getColor(R.color.click_blue))
        log.text = "Log in"
        log.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}