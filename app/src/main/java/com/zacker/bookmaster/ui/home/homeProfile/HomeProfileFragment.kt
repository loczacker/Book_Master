package com.zacker.bookmaster.ui.home.homeProfile

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.zacker.bookmaster.R
import com.zacker.bookmaster.databinding.DialogChangeLanguageBinding
import com.zacker.bookmaster.databinding.DialogChoiceLogOutBinding
import com.zacker.bookmaster.databinding.FragmentHomeProfileBinding
import com.zacker.bookmaster.util.Const

class HomeProfileFragment : Fragment() {

    private var _binding: FragmentHomeProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HomeProfileViewModel

    private val imageIds = intArrayOf(
        R.drawable.profile_wall,
        R.drawable.profile_wall_1,
        R.drawable.profile_wall_2,
        R.drawable.profile_wall_3,
        R.drawable.profile_wall_4,
        R.drawable.profile_wall_5
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeProfileBinding.inflate(inflater, container, false)
        val sharedPreferences = requireActivity().getSharedPreferences(Const.KEY_FILE, Context.MODE_PRIVATE)
        val factory = HomeProfileViewModelFactory(sharedPreferences)
        viewModel = ViewModelProvider(requireActivity(), factory)[HomeProfileViewModel::class.java]
        binding.lifecycleOwner = viewLifecycleOwner
        val imgCoverProfile = getRandomCoverImage()
        binding.imgProfile.setImageResource(imgCoverProfile)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObserver()
        setListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setUpObserver() {
        viewModel.user.observe(viewLifecycleOwner) { userModel ->
            userModel?.let {
                binding.tvNameProfile.text = it.name
                binding.tvEmailProfile.text = it.email
                Glide.with(this)
                    .load(it.photoURL)
                    .placeholder(R.drawable.profile)
                    .into(binding.imgProfileCircle)
            }
        }
    }

    private fun getRandomCoverImage(): Int {
        val randomIndex = imageIds.indices.random()
        return imageIds[randomIndex]
    }

    private fun setListener() {
        binding.ibChange.setOnClickListener {
            NavHostFragment.findNavController(this)
                .navigate(R.id.home_to_change_profile, null)
        }
        binding.tvSignOut.setOnClickListener {
            alertDialog()
        }
        binding.imgSignOut.setOnClickListener{
            alertDialog()
        }
        binding.languageCurrent.setOnClickListener {
            changeLanguage()
        }
    }

    private fun alertDialog() {
        val dialogBinding: DialogChoiceLogOutBinding =
            DialogChoiceLogOutBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity()).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }

        dialogBinding.btnOk.setOnClickListener {
            dialog.cancel()
            signOut()
        }
        dialogBinding.btnRefuse.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun signOut() {
        viewModel.signOut()
        NavHostFragment.findNavController(this)
            .navigate(R.id.home_to_login, null)
    }

    private fun changeLanguage() {
        val dialogBinding: DialogChangeLanguageBinding =
            DialogChangeLanguageBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity()).apply {
            setContentView(dialogBinding.root)
            window?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCancelable(false)
        }

        dialogBinding.btnOk.setOnClickListener {
            // Implement logic to change language
        }
        dialogBinding.btnRefuse.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
