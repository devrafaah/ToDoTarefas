package com.example.todotarefas.util


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.todotarefas.R
import com.example.todotarefas.databinding.BottomSheetBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialog

fun Fragment.initToolbar(toolbar: MaterialToolbar) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""
    (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    toolbar.setNavigationOnClickListener{ activity?.onBackPressedDispatcher?.onBackPressed() }
}

fun Fragment.showButtonSheet(
    message: String,
    onClick: () -> Unit = {}
) {
    val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog )
    val binding: BottomSheetBinding = BottomSheetBinding.inflate(layoutInflater, null, false)

    binding.warningMessage.text = message

    binding.btnGotIt.setOnClickListener {
        onClick()
        bottomSheetDialog.dismiss()

    }

    bottomSheetDialog.setContentView(binding.root)
    bottomSheetDialog.show()
}


