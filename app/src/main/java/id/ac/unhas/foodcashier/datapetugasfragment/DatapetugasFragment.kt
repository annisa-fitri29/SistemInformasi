package id.ac.unhas.foodcashier.datapetugasfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import id.ac.unhas.foodcashier.databinding.FragmentDatapetugasBinding

class DatapetugasFragment : Fragment() {
    private var _binding: FragmentDatapetugasBinding? = null
    private val binding get() = _binding!!

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<DataPetugas>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentDatapetugasBinding.inflate(inflater, container, false)

        userRecyclerview = binding.petugasRecyclerview
        userRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        userRecyclerview.setHasFixedSize(false)

        userArrayList = arrayListOf<DataPetugas>()
        getUserData()

        binding.floatingDatapetugas.setOnClickListener{
            moveToAddPetugas()
        }

        binding.datapetugasBackbutton.setOnClickListener {
            moveToMenu()
        }

        return binding.root
    }

    private fun getUserData() {
        dbref = FirebaseDatabase.getInstance("https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .getReference("petugas")

        dbref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(DataPetugas::class.java)
                        userArrayList.add(user!!)

                    }
                    var adapter = PetugasAdapter(userArrayList)
                    userRecyclerview.adapter = adapter
                    adapter.setOnItemClickListener(object: PetugasAdapter.onItemClickListener{
                        override fun onItemClick(position: Int) {

                            val openDialog = AlertDialog.Builder(requireContext())

                            openDialog.setTitle("Delete item?")
                            openDialog.setPositiveButton("DELETE"){
                                    dialog,_->
                                userArrayList[position].nama?.let { deleteData(it) }
                                adapter.notifyItemRemoved(position)
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

    private fun moveToAddPetugas(){
        val direction = DatapetugasFragmentDirections.actionDatapetugasFragmentToTambahpetugasFragment()
        findNavController().navigate(direction)
    }

    private fun moveToMenu(){
        val direction = DatapetugasFragmentDirections.actionDatapetugasFragmentToMenuFragment()
        findNavController().navigate(direction)
    }

    private fun openDialog() {
        val openDialog = AlertDialog.Builder(requireContext())

        openDialog.setTitle("Pilih gambar")
        openDialog.setPositiveButton("Galeri"){
                dialog,_->

            dialog.dismiss()
        }
        openDialog.setNegativeButton("Batal"){
                dialog,_->
            dialog.dismiss()
        }
        openDialog.create()
        openDialog.show()

    }
}