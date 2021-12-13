package id.ac.unhas.foodcashier.produkfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import id.ac.unhas.foodcashier.R
import id.ac.unhas.foodcashier.databinding.FragmentLoginBinding
import id.ac.unhas.foodcashier.databinding.FragmentProdukBinding
import id.ac.unhas.foodcashier.datapetugasfragment.DataPetugas
import id.ac.unhas.foodcashier.datapetugasfragment.DatapetugasFragmentDirections
import id.ac.unhas.foodcashier.datapetugasfragment.PetugasAdapter


class ProdukFragment : Fragment() {

    private lateinit var dbref : DatabaseReference
    private var _binding: FragmentProdukBinding? = null
    private val binding get() = _binding!!
    private lateinit var produkArrayList : ArrayList<Produk>
    private lateinit var menuRecyclerview : RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProdukBinding.inflate(inflater, container, false)

        menuRecyclerview = binding.menuRecyclerview
        menuRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        menuRecyclerview.setHasFixedSize(false)

        produkArrayList = arrayListOf<Produk>()
        getMenuData()

        binding.floatingActionButton.setOnClickListener{
            moveToAddProduk()
        }

        binding.produkBackbutton.setOnClickListener{
            moveToMenu()
        }

        return binding.root
    }

    private fun getMenuData() {
        dbref = FirebaseDatabase.getInstance("https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("menu")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val menu = userSnapshot.getValue(Produk::class.java)
                        produkArrayList.add(menu!!)

                    }
                    var adapter = MenuAdapter(produkArrayList)
                    menuRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object: MenuAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {
                            val openDialog = AlertDialog.Builder(requireContext())

                            openDialog.setTitle("Delete item?")
                            openDialog.setPositiveButton("DELETE"){
                                    dialog,_->
                                produkArrayList[position].produk?.let { deleteData(it) }
                                adapter.notifyItemRemoved(position)
                                adapter = MenuAdapter(produkArrayList)
                                dialog.dismiss()
                            }
                            openDialog.setNegativeButton("cancel"){
                                    dialog,_->
                                dialog.dismiss()
                            }
                            openDialog.create()
                            openDialog.show()
                        }

                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun deleteData(nama: String){
        dbref.child(nama).removeValue().addOnSuccessListener {
            Toast.makeText(requireContext(), "Successfully Deleted", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to delete", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToAddProduk(){
        val direction = ProdukFragmentDirections.actionProdukFragmentToTambahProdukFragment()
        findNavController().navigate(direction)
    }

    private fun moveToMenu(){
        val direction = ProdukFragmentDirections.actionProdukFragmentToMenuFragment()
        findNavController().navigate(direction)
    }


}