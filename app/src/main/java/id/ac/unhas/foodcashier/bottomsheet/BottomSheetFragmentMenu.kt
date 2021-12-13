package id.ac.unhas.foodcashier.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import id.ac.unhas.foodcashier.databinding.BottomsheetMenuBinding

class BottomSheetFragmentMenu: BottomSheetDialogFragment() {

    private var _binding: BottomsheetMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomsheetMenuBinding.inflate(inflater, container, false)

        binding.button.setOnClickListener {
            Toast.makeText(requireContext(),
                "test button",
                Toast.LENGTH_SHORT).show()
        }

        return  binding.root
    }

}