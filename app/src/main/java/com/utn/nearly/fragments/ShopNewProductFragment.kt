package com.utn.nearly.fragments

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.utn.nearly.databinding.FragmentShopNewProductBinding
import com.utn.nearly.view_models.ShopNewProductViewModel


class ShopNewProductFragment : Fragment() {

    lateinit var binding : FragmentShopNewProductBinding
    var isImagePicked : Boolean = false
    var imageUri : Uri? = null

    companion object {
        fun newInstance() = ShopNewProductFragment()
    }

    private lateinit var viewModel: ShopNewProductViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopNewProductBinding.inflate(inflater,
            container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.newProductProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.newProductProgressBar.visibility = View.GONE
            }
        })

        viewModel.productUploaded.observe(viewLifecycleOwner, Observer { productUploaded ->
            if(productUploaded) {
                binding.root.findNavController().navigateUp()
            }
        })

        binding.btnNewProductAddButton.setOnClickListener {
            //Click on add product
            val name = binding.txtNewProductName.text.toString()
            val description = binding.txtNewProductDescription.text.toString()
            val price = binding.txtNewProductPrice.text.toString()
            val state = binding.swNewProductStateSwitch.isChecked

            if(!isImagePicked) {
                Toast.makeText(requireContext(),"Select image first", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(name == ""){
                Toast.makeText(requireContext(),"Name can't be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(price == ""){
                Toast.makeText(requireContext(),"Price can't be empty", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            viewModel.uploadNewProduct(name, description,price,state, imageUri!!)
        }

        binding.btnNewProductAddFromCamera.setOnClickListener {
            //Agregar foto desde camara
        }

        binding.btnNewProductAddFromFolder.setOnClickListener {
            TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check()

            //Agregar foto desde carpeta


        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopNewProductViewModel::class.java)
        // TODO: Use the ViewModel
    }

    //Handler del result luego de seleccionar la imagen
    private val pickImageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data != null) {
                imageUri = result.data?.data
                binding.imgNewProductImage.setImageURI(imageUri)
                isImagePicked = true
            }else{
                isImagePicked = false
                binding.imgNewProductImage.setImageURI(null)
            }
        }
    }

    var permissionlistener: PermissionListener = object : PermissionListener {
        override fun onPermissionGranted() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.type = "image/*"
            pickImageResult.launch(intent)
            Toast.makeText(requireContext(), "Permission Granted", Toast.LENGTH_SHORT).show()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                requireContext(),
                "Permission Denied\n$deniedPermissions",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}