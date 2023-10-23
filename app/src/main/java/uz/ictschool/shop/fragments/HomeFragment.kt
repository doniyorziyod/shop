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
import android.widget.SearchView
import android.widget.TextView
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialog
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import uz.ictschool.shop.R
import uz.ictschool.shop.adapters.CategoryAdapter
import uz.ictschool.shop.adapters.DiscountViewPagerAdapter
import uz.ictschool.shop.adapters.ImageViewPagerAdapter
import uz.ictschool.shop.adapters.ProductAdapter
import uz.ictschool.shop.apis.APIClient
import uz.ictschool.shop.apis.APIService
import uz.ictschool.shop.databinding.FragmentHomeBinding
import uz.ictschool.shop.databinding.FragmentProductBinding
import uz.ictschool.shop.models.Product
import uz.ictschool.shop.models.ProductData
import uz.ictschool.shop.models.Profile
import uz.ictschool.shop.services.SharedPreference
import java.util.Locale

private const val ARG_PARAM1 = "product"
class HomeFragment : Fragment() {
    private var profile: Profile? = null
    private var _binding: FragmentHomeBinding? = null
    val api = APIClient.getInstance().create(APIService::class.java)
    private val binding get() = _binding!!
    private lateinit var product: Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getSerializable(ARG_PARAM1) as Product
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        binding.allProducts.layoutManager = GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.categoryRc.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.HORIZONTAL, false)

        allProducts()
        allCategories()
        drawerListener()
        return binding.root
    }

    fun connectAdapterAllProducts(products : List<Product>) {
        binding.viewpager.adapter = DiscountViewPagerAdapter(doArrayForDiscountView(products), requireContext(), object : DiscountViewPagerAdapter.DiscountClicked {
            override fun onClicked(product: Product) {
                var bottomSheet =
                    BottomSheetDialog(binding.root.context, R.style.SheetDialog)
                var bottombiding = FragmentProductBinding.inflate(layoutInflater)
                bottomSheet.behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
                bottombiding.productTitle.text = product.title
                bottombiding.productBrand.text = product.brand
                bottombiding.productDesc.text = product.description
                bottombiding.productRating.text = product.rating.toString()
                bottombiding.productViewpager.adapter = ImageViewPagerAdapter(product.images)
                bottomSheet.setContentView(bottombiding.root)
                bottombiding.productAddToCart.setOnClickListener {
                    val shared = SharedPreference.getInstance(requireContext())
                    profile = shared.getProfile()
                    if (profile == null) {
                        findNavController().navigate(R.id.profileFragment)
                        bottomSheet.cancel()
                    } else {
                        findNavController().navigate(R.id.cartFragment)
                        bottomSheet.cancel()
                    }
                }
                bottomSheet.show()
            }
        })
        binding.allProducts.adapter =
            ProductAdapter(products, requireContext(), object : ProductAdapter.ProductClicked {
                override fun onClicked(product: Product) {
                    var bottomSheet =
                        BottomSheetDialog(binding.root.context, R.style.SheetDialog)
                    var bottombiding = FragmentProductBinding.inflate(layoutInflater)
                    bottomSheet.behavior.peekHeight = ViewGroup.LayoutParams.MATCH_PARENT
                    bottombiding.productTitle.text = product.title
                    bottombiding.productBrand.text = product.brand
                    bottombiding.productDesc.text = product.description
                    bottombiding.productRating.text = product.rating.toString()
                    bottombiding.productViewpager.adapter = ImageViewPagerAdapter(product.images)
                    bottomSheet.setContentView(bottombiding.root)
                    bottombiding.productAddToCart.setOnClickListener {
                        val shared = SharedPreference.getInstance(requireContext())
                        profile = shared.getProfile()
                        if (profile == null) {
                            findNavController().navigate(R.id.profileFragment)
                            bottomSheet.cancel()
                        } else {
                            findNavController().navigate(R.id.cartFragment)
                            bottomSheet.cancel()
                        }
                    }
                    bottomSheet.show()
                }
            })
    }

    fun capitalizeFirstLetter(a : String) : String {
       return a.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
    }

    fun doSearching() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            var lastSearch = ""
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == lastSearch) return false
                api.getProductsBySearch(query!!).enqueue(object : Callback<ProductData> {
                    override fun onResponse(
                        call: Call<ProductData>,
                        response: Response<ProductData>
                    ) {
                        val products = response.body()!!.products
                        connectAdapterAllProducts(products)
                    }

                    override fun onFailure(call: Call<ProductData>, t: Throwable) {
                        Log.d("TAG", "$t")
                    }

                })
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    fun doArrayForDiscountView(products: List<Product>) : List<Product> {
        var yaxshi_products = mutableListOf<Product>()
        for (i in products) {
            if (i.discountPercentage > 0.0) {
                yaxshi_products.add(i)
            }
        }

        return yaxshi_products
    }

    private fun allProducts() {
        api.getAllProducts().enqueue(object : Callback<ProductData> {
            override fun onResponse(call: Call<ProductData>, response: Response<ProductData>) {
                val products = response.body()?.products!!
                connectAdapterAllProducts(products)
                doSearching()
            }

            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                Log.d("TAG", "$t")
            }
        })
    }

    fun allCategories() {
        api.getAllCategories().enqueue(object : Callback<List<String>> {
            override fun onResponse(call: Call<List<String>>, response: Response<List<String>>) {
                val categories = response.body()!!
                binding.categoryRc.adapter = CategoryAdapter(categories, requireContext(), object : CategoryAdapter.CategoryClicked {
                    override fun onClicked(category: String) = if (category == "") {
                        binding.categoryName.text = "All Products"
                        api.getAllProducts().enqueue(object : Callback<ProductData> {
                            override fun onResponse(
                                call: Call<ProductData>,
                                response: Response<ProductData>
                            ) {
                                val products = response.body()!!.products
                                connectAdapterAllProducts(products)
                            }
                            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                Log.d("TAG", "$t")
                            }
                        })
                    } else {
                        var str = capitalizeFirstLetter(category)
                        binding.categoryName.text = str
                        api.getByCategory(category).enqueue(object : Callback<ProductData> {
                            override fun onResponse(
                                call: Call<ProductData>,
                                response: Response<ProductData>
                            ) {
                                val products = response.body()?.products!!
                                connectAdapterAllProducts(products)
                            }
                            override fun onFailure(call: Call<ProductData>, t: Throwable) {
                                Log.d("TAG", "$t")
                            }
                        })
                    }
                })
            }
            override fun onFailure(call: Call<List<String>>, t: Throwable) {
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
                drawer.closeDrawer(GravityCompat.START)
                true
            }
        }
    }
}