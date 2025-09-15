package com.karrar.movieapp.ui.profile.logout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karrar.movieapp.R
import com.karrar.movieapp.databinding.DialogLogoutBinding
import com.karrar.movieapp.ui.profile.ProfileViewModel
import com.karrar.movieapp.utilities.collectLast
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LogoutBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: DialogLogoutBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        binding.btnLogout.setOnClickListener {
            dismiss()
            viewModel.logout()
        }
    }
}
