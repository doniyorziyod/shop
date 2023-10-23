package uz.ictschool.shop.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import retrofit2.Call
import retrofit2.Response
import uz.ictschool.shop.R
import uz.ictschool.shop.adapters.CartAdapter
import uz.ictschool.shop.apis.APIClient
import uz.ictschool.shop.apis.APIService
import uz.ictschool.shop.databinding.FragmentCartBinding
import uz.ictschool.shop.models.CartData
import uz.ictschool.shop.models.Product
import uz.ictschool.shop.models.ProductNumber
import uz.ictschool.shop.models.Profile
import uz.ictschool.shop.services.SharedPreference

private const val ARG_PARAM1 = "product"
class CartFragment : Fragment() {
    var product: Product? = null
    private var _binding: FragmentCartBinding? = null
    val api = APIClient.getInstance().create(APIService::class.java)
    private val binding get() = _binding!!
    private var profile: Profile? = null
    private var didLogin:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
            didLogin = it.getBoolean("didLogin")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCartBinding.inflate(layoutInflater, container, false)
        val shared = SharedPreference.getInstance(requireContext())
        profile = shared.getProfile()
        if (profile == null) {
            findNavController().navigate(R.id.profileFragment)
        }
        binding.cartRc.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.carBack.setOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        setCart()

        return binding.root
    }

    private fun setCart() {
        api.getCartsOfUser(profile!!.id).enqueue(object : retrofit2.Callback<CartData>{
            override fun onResponse(call: Call<CartData>, response: Response<CartData>) {
                if (response.isSuccessful){
                    val products = response.body()!!.carts[0].products.toMutableList()
                    if (product != null){
                        val productX = ProductNumber(0.0, 0, product!!.id, product!!.price, 1, product!!.title, product!!.price * 1)
                        products.add(0, productX)
                    }
                    binding.cartRc.adapter = CartAdapter(products, requireContext())
                }
            }

            override fun onFailure(call: Call<CartData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }
    private fun drawerListener() {
        binding.apply {
            navigationView.setNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_profile -> {
                        findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
                    }
                    R.id.menu_cart -> {
                        findNavController().navigate(R.id.cartFragment)
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
                    }
                }
                true
            }
        }
    }
}