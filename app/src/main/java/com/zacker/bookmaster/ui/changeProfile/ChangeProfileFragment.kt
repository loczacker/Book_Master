@file:Suppress("DEPRECATION")

package com.zacker.bookmaster.ui.changeProfile

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.FragmentChangeProfileBinding
import com.zacker.bookmaster.model.UsersModel
import com.zacker.bookmaster.ui.home.homeProfile.HomeProfileViewModel
import com.zacker.bookmaster.util.Const

class ChangeProfileFragment : Fragment() {

    private lateinit var binding: FragmentChangeProfileBinding
    private lateinit var viewModel: HomeProfileViewModel
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private var oldPhotoUrl: String? = null

    private val CAMERA_PERMISSION_CODE = 101
    private val GALLERY_PERMISSION_CODE = 102
    private val STORAGE_PERMISSION_CODE = 103

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangeProfileBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(requireActivity())[HomeProfileViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        sharedPreferences = requireContext().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        checkPermission()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)
        firebaseAuth = FirebaseAuth.getInstance()
        loadUserInfo()
        setListeners()
    }

    private fun loadUserInfo() {
        val email = sharedPreferences.getString(Const.KEY_EMAIL_USER, null)
        if (email != null) {
            viewModel.loadUserInfo(email)
            viewModel.resultUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    binding.edtChangeName.setText(user.name)
                    binding.edPhoneChange.setText(user.phone)
                    binding.edAddressChange.setText(user.address)
                    binding.edAboutChange.setText(user.about)
                    oldPhotoUrl = user.photoURL // Lưu trữ URL ảnh cũ
                    if (!user.photoURL.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(user.photoURL)
                            .placeholder(R.drawable.avatar)
                            .into(binding.imgAva)
                    } else {
                        binding.imgAva.setImageResource(R.drawable.profile)
                    }
                } else {
                    Toast.makeText(activity, "Failed to load user info", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(activity, "No email found in shared preferences", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setListeners() {
        binding.imgCamera.setOnClickListener {
            showImageAttachMenu()
        }
        binding.tvDone.setOnClickListener {
            validateData()
        }
        binding.imgBackHomeProfile.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun validateData() {
        val email = sharedPreferences.getString(Const.KEY_EMAIL_USER, null)
        if (email != null) {
            viewModel.loadUserInfo(email)
            viewModel.resultUser.observe(viewLifecycleOwner) { user ->
                if (user != null) {
                    user.name = binding.edtChangeName.text.toString().trim()
                    user.phone = binding.edPhoneChange.text.toString().trim()
                    user.address = binding.edAddressChange.text.toString().trim()
                    user.about = binding.edAboutChange.text.toString().trim()
                    if (user.name!!.isEmpty() || user.phone!!.isEmpty() || user.address!!.isEmpty()) {
                        Toast.makeText(activity, "Please enter all fields", Toast.LENGTH_SHORT).show()
                    } else {
                        if (imageUri == null) {
                            oldPhotoUrl?.let { updateProfile(it) }
                        } else {
                            // Cập nhật ảnh trong ImageView ngay lập tức
                            binding.imgAva.setImageURI(imageUri)
                            // Sau đó, gọi updateProfile để cập nhật ảnh trên máy chủ
                            uploadImage()
                        }
                    }
                } else {
                    Toast.makeText(activity, "Failed to load user info", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private fun checkPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        val galleryPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(requireContext(), cameraPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(cameraPermission),
                CAMERA_PERMISSION_CODE
            )
        }

        if (ContextCompat.checkSelfPermission(requireContext(), galleryPermission)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(galleryPermission),
                GALLERY_PERMISSION_CODE
            )
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageCamera()
                }
            }
            GALLERY_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    picImageGallery()
                }
            }
        }
    }

    private fun updateProfile(uploadedImageUrl: String?) {
        progressDialog.setMessage("Updating profile...")
        val uid = firebaseAuth.uid
        if (uid != null) {
            val updatedUser = UsersModel(
                name = binding.edtChangeName.text.toString().trim(),
                phone = binding.edPhoneChange.text.toString().trim(),
                address = binding.edAddressChange.text.toString().trim(),
                about = binding.edAboutChange.text.toString().trim(),
                photoURL = uploadedImageUrl ?: oldPhotoUrl // Sử dụng URL ảnh cũ nếu không có URL mới
            )
            viewModel.updateUser(uid, updatedUser)

            binding.edtChangeName.setText(updatedUser.name)
            binding.edPhoneChange.setText(updatedUser.phone)
            binding.edAddressChange.setText(updatedUser.address)
            binding.edAboutChange.setText(updatedUser.about)
            if (!updatedUser.photoURL.isNullOrEmpty()) {
                if (isAdded) {
                    Glide.with(this@ChangeProfileFragment)
                        .load(updatedUser.photoURL)
                        .placeholder(R.drawable.profile)
                        .into(binding.imgAva)
                }
            }
            progressDialog.dismiss()
            if (isAdded) {
                Navigation.findNavController(requireView()).navigateUp()
            }
            Toast.makeText(activity, "Update successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "Không thể tải hồ sơ lên", Toast.LENGTH_SHORT).show()
        }
    }



    private fun uploadImage() {
        progressDialog.setMessage("download information")
        progressDialog.show()
        val filePathAndName = "img/${firebaseAuth.uid}"
        val reference = FirebaseStorage.getInstance().getReference(filePathAndName)
        reference.putFile(imageUri!!)
            .addOnSuccessListener { taskSnapshot ->
                reference.downloadUrl.addOnSuccessListener { uri ->
                    val uploadImageUrl = uri.toString()
                    if (isAdded) {
                        updateProfile(uploadImageUrl)
                    }
                }.addOnFailureListener {
                    progressDialog.dismiss()
                    Toast.makeText(activity, "Failed to get image URL", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(activity, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showImageAttachMenu() {
        val popupMenu = PopupMenu(activity, binding.imgCamera)
        popupMenu.menu.add(Menu.NONE, 0, 0, "Camera")
        popupMenu.menu.add(Menu.NONE, 1, 1, "Gallery")
        //handle popup menu item click
        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            if (id == 0) {
                pickImageCamera()
            } else if (id == 1) {
                picImageGallery()
            }
            true
        }
        popupMenu.show()
    }

    private fun picImageGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private fun pickImageCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "Temp_Title")
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Description")
        val resolver = requireActivity().contentResolver
        imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            if (imageUri != null) {
                Glide.with(this@ChangeProfileFragment)
                    .load(imageUri)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgAva)
            } else {
                Toast.makeText(requireContext(), "Unable to load photo.", Toast.LENGTH_SHORT).show()
            }
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
        } else {
            Toast.makeText(requireContext(), "Error when taking photos.", Toast.LENGTH_SHORT).show()
        }
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            imageUri = data!!.data
            if (imageUri != null) {
                Glide.with(this@ChangeProfileFragment)
                    .load(imageUri)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgAva)
            }
        }
    }
}
