package id.ac.unhas.foodcashier.menufragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import id.ac.unhas.foodcashier.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMenuBinding.inflate(inflater, container, false)


        binding.cardviewProduk.setOnClickListener {
            val toProduk = MenuFragmentDirections.actionMenuFragmentToProdukFragment()

            findNavController().navigate(toProduk)
        }

        binding.cardviewDatapetugas.setOnClickListener {
            val toDataPetugas = MenuFragmentDirections.actionMenuFragmentToDatapetugasFragment()

            findNavController().navigate(toDataPetugas)
        }

        binding.cardviewKasir.setOnClickListener {
            val toKasir = MenuFragmentDirections.actionMenuFragmentToKasirFragment()

            findNavController().navigate(toKasir)
        }

        binding.menuBackbtn.setOnClickListener {
            val toLogin = MenuFragmentDirections.actionMenuFragmentToViewPagerFragment()

            findNavController().navigate(toLogin)
        }

        return binding.root
    }
}