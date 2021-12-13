package id.ac.unhas.foodcashier.produkfragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.ac.unhas.foodcashier.R
import id.ac.unhas.foodcashier.databinding.FragmentProdukBinding
import id.ac.unhas.foodcashier.databinding.FragmentTambahProdukBinding
import id.ac.unhas.foodcashier.tambahpetugasfragment.Petugas
import id.ac.unhas.foodcashier.tambahpetugasfragment.TambahpetugasFragmentDirections
import java.text.DecimalFormat

private lateinit var progressDialog: ProgressDialog


class TambahProdukFragment : Fragment() {

    private var _binding: FragmentTambahProdukBinding? = null
    private val binding get() = _binding!!

    lateinit var database: DatabaseReference
    private var uriprof : Uri? = null
    private lateinit var galIntent: Intent
    private var defaultImageURL = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTambahProdukBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Uploading image...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tambahprodukBackbutton.setOnClickListener {
            moveToData()
        }
        binding.menupict.setOnClickListener {
            openDialog()
        }

        binding.saveBtn.setOnClickListener {

            if(uriprof != null){
                progressDialog.show()
                uploadImageToFirebaseStorage(uriprof)

            }else{
                validationToOpenDatabase()
            }

        }

        return binding.root
    }

    private fun validationToOpenDatabase(){
        if(validateInput(binding.menuInput.text.toString(), binding.menuInput)&&
            validateInput(binding.hargaInput.text.toString(), binding.hargaInput)&&
            validateInput(binding.descInput.text.toString(), binding.descInput)){
            database = FirebaseDatabase.getInstance(
                "https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("menu/")
            inserttoDatabase(defaultImageURL)
        }
    }

    private fun inserttoDatabase(imageURL: String?){
        var produk = binding.menuInput.text.toString()
        var harga = binding.hargaInput.text.toString().replace(".", "")
        var desc = binding.descInput.text.toString()

        database.child(produk).
        setValue(imageURL?.let { ProdukConstructor(produk,harga, desc, it) }).addOnSuccessListener {
            Toast.makeText(requireContext(),
                "inserted to database",
                Toast.LENGTH_SHORT).show()

        }

    }

    private fun validateInput(inputText: String, editText: EditText): Boolean {
        return if (inputText.length <= 1) {
            editText.error = "* Minimum 2 Characters Allowed"
            false
        }else if(inputText.isEmpty()){
            editText.error = "* Wajib diisi"
            false
        } else {
            editText.error = null
            true
        }
    }

    private fun openDialog() {
        val openDialog = AlertDialog.Builder(requireContext())

        openDialog.setTitle("Pilih gambar")
        openDialog.setPositiveButton("Galeri"){
                dialog,_->
            openGallery()
            dialog.dismiss()
        }
        openDialog.setNegativeButton("Batal"){
                dialog,_->
            dialog.dismiss()
        }
        openDialog.create()
        openDialog.show()

    }

    private fun openGallery() {
        galIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        startActivityForResult(
            Intent.createChooser(galIntent,
                "Pilih gambar dari galeri "),2)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK){
        } else if (requestCode == 2){
            if (data != null){
                uriprof = data.data!!
                binding.menupict.setImageURI(uriprof)
            }
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RequestPermissionCode-> if (grantResults.isNotEmpty()
                && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Toast.makeText(requireContext(),
                    "Permission Granted , Now your application can access Camera",
                    Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(requireContext(),
                    "Permission Failed, you can't access the camera",
                    Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(uri: Uri?){

        val filename = binding.menuInput.text.toString()
        val ref = FirebaseStorage.getInstance("gs://sisteminformasi-449ae.appspot.com").getReference("menupict/$filename")

        ref.putFile(uri!!).addOnSuccessListener {

            ref.downloadUrl.addOnSuccessListener {
                Log.d("RegisterActivity", "File Location: $it")
                defaultImageURL = it.toString()
                progressDialog.dismiss()

                validationToOpenDatabase()

                Toast.makeText(requireContext(),
                    "upload foto berhasil",
                    Toast.LENGTH_SHORT).show()
                moveToData()
            }
        }.addOnFailureListener{
            progressDialog.dismiss()
            Toast.makeText(requireContext(),
                "Upload foto GAGAL",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToData(){
        val toproduk = TambahProdukFragmentDirections.actionTambahProdukFragmentToProdukFragment()
        findNavController().navigate(toproduk)
    }

    companion object{
        const val RequestPermissionCode = 111
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}