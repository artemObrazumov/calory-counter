package com.borsch_team.calorycounter.ui.gallery

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.borsch_team.calorycounter.ui.productEditor.ProductEditorActivity
import com.borsch_team.calorycounter.databinding.FragmentGalleryBinding
import com.borsch_team.calorycounter.ui.adapters.CustomProductViewAdapter

class AddProductFragment : Fragment() {
    private lateinit var binding: FragmentGalleryBinding
    private lateinit var addProductViewModel: AddProductViewModel
    private lateinit var adapter: CustomProductViewAdapter

    private lateinit var addProductLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        addProductViewModel =
            ViewModelProvider(this)[AddProductViewModel::class.java]
        setupAdapter()
        binding.addMeal.setOnClickListener {
            addProductLauncher.launch(Intent(context, ProductEditorActivity::class.java))
        }
        addProductViewModel.customProductsList.observe(viewLifecycleOwner) { products ->
            adapter.updateProductsList(products)
        }
        addProductViewModel.getCustomProducts()

        addProductLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                res ->
            if (res.resultCode == Activity.RESULT_OK) {
                addProductViewModel.getCustomProducts()
            }
        }

        return binding.root
    }

    private fun setupAdapter() {
        adapter = CustomProductViewAdapter(listOf()) { pos ->
            addProductViewModel.removeProduct(adapter.getProductID(pos))
            adapter.removeItem(pos)
        }
        binding.productsList.layoutManager = LinearLayoutManager(requireContext())
        binding.productsList.adapter = adapter
    }
}
