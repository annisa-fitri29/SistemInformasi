package id.ac.unhas.foodcashier.kasirfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import id.ac.unhas.foodcashier.R
import id.ac.unhas.foodcashier.bottomsheet.BottomSheetFragmentMenu
import id.ac.unhas.foodcashier.databinding.FragmentDatapetugasBinding
import id.ac.unhas.foodcashier.databinding.FragmentKasirBinding
import id.ac.unhas.foodcashier.databinding.FragmentProdukBinding
import id.ac.unhas.foodcashier.databinding.KasirItemBinding
import id.ac.unhas.foodcashier.datapetugasfragment.DataPetugas
import id.ac.unhas.foodcashier.produkfragment.MenuAdapter
import id.ac.unhas.foodcashier.produkfragment.Produk
import id.ac.unhas.foodcashier.produkfragment.TambahProdukFragmentDirections


class KasirFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private var _binding: FragmentKasirBinding? = null
    private val binding get() = _binding!!
    private lateinit var pesananArrayList : ArrayList<Pesanan>
    private lateinit var menuRecyclerview : RecyclerView
    private var total_harga = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentKasirBinding.inflate(inflater, container, false)

        binding.totalHarga.setText("Rp. 0")
        val bottomsheet = BottomSheetFragmentMenu()

        menuRecyclerview = binding.kasirRecyclerview
        menuRecyclerview.layoutManager = GridLayoutManager(requireContext(),2)
        menuRecyclerview.setHasFixedSize(false)

        binding.kasirBackbutton.setOnClickListener {
            val toMenu = KasirFragmentDirections.actionKasirFragmentToMenuFragment()
            findNavController().navigate(toMenu)
        }

        pesananArrayList = arrayListOf<Pesanan>()
        getMenuData()




        return binding.root
    }

    private fun getMenuData() {
        dbref = FirebaseDatabase.getInstance("https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("menu")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val menu = userSnapshot.getValue(Pesanan::class.java)
                        pesananArrayList.add(menu!!)

                    }

                    var adapter = KasirAdapter(pesananArrayList)
                    menuRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object: KasirAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            total_harga += pesananArrayList[position].harga?.toInt()!!
                            binding.totalHarga.setText("Rp. ${total_harga.let { String.format("%,d", it) }}")
                        }

                    })
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}