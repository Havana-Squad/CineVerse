package com.karrar.movieapp.ui.profile.editProfile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.databinding.DialogEditProfileBinding
import com.karrar.movieapp.databinding.DialogLogoutBinding
import com.karrar.movieapp.ui.profile.ProfileViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue


@AndroidEntryPoint
class EditProfileBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: DialogEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnLogout.setOnClickListener {
            dismiss()
            viewModel.goToWebsite()
        }
    }
}
