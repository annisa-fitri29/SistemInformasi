package id.ac.unhas.foodcashier.menufragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.ac.unhas.foodcashier.databinding.FragmentPetugasMenuBinding

class PetugasMenuFragment : Fragment() {

    private var _binding: FragmentPetugasMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPetugasMenuBinding.inflate(inflater, container, false)

        binding.cardviewKasir.setOnClickListener {
            val toKasir = PetugasMenuFragmentDirections.actionPetugasMenuFragmentToKasirFragment()

            findNavController().navigate(toKasir)
        }

        binding.menuBackbtn.setOnClickListener {
            val toLogin = PetugasMenuFragmentDirections.actionPetugasMenuFragmentToViewPagerFragment()

            findNavController().navigate(toLogin)
        }

        return binding.root
    }

}