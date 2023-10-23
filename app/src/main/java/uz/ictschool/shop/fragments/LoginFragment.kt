package uz.ictschool.shop.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.ictschool.shop.R
import uz.ictschool.shop.apis.APIClient
import uz.ictschool.shop.apis.APIService
import uz.ictschool.shop.databinding.FragmentLoginBinding
import uz.ictschool.shop.models.Login
import uz.ictschool.shop.models.Product
import uz.ictschool.shop.models.Profile
import uz.ictschool.shop.services.SharedPreference

class LoginFragment : Fragment() {
    private var quantity = 0
    private var product : Product? = null
    private lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.loginBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }

        binding.loginLoginMb.setOnClickListener {
            val username : String = binding.loginUsername.text.toString()
            val password : String = binding.loginPassword.text.toString()
            if (username == "" || password == "") {
                Toast.makeText(requireContext(), "Complete the fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val api = APIClient.getInstance().create(APIService::class.java)
            api.login(Login(username, password)).enqueue(object : Callback<Profile> {
                override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(requireContext(), "Incorrect login or password", Toast.LENGTH_SHORT).show()
                        binding.loginPassword.setText("")
                        return
                    }
                    val shared = SharedPreference.getInstance(requireContext())
                    val user = response.body()!!
                    shared.setProfile(user)
                    if (product == null){
                        requireActivity().onBackPressed()
                    }else{
                        val bundle = Bundle()
                        bundle.putSerializable("product", product)
                        bundle.putInt("quantity", quantity)
                        findNavController().navigate(R.id.action_loginFragment_to_cartFragment, bundle)
                    }
                }

                override fun onFailure(call: Call<Profile>, t: Throwable) {
                    Log.d("TAG", "$t")
                }

            })
        }
        return binding.root
    }
}