package id.ac.unhas.foodcashier.tambahpetugasfragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.ActivityNotFoundException
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
import androidx.core.app.ActivityCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import id.ac.unhas.foodcashier.R
import id.ac.unhas.foodcashier.databinding.FragmentDatapetugasBinding
import id.ac.unhas.foodcashier.databinding.FragmentTambahpetugasBinding
import id.ac.unhas.foodcashier.loginfragment.viewpager.ViewPagerFragmentDirections

private lateinit var progressDialog: ProgressDialog

class TambahpetugasFragment : Fragment() {

    lateinit var database: DatabaseReference
    private var uriprof :Uri? = null
    private lateinit var galIntent:Intent
    private var defaultImageURL = ""

    private var _binding: FragmentTambahpetugasBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTambahpetugasBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Uploading image...")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.tambahpetugasBackbutton.setOnClickListener {
            moveToData()
        }
        binding.profilepict.setOnClickListener {
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
        if(validateInput(binding.nameInput.text.toString(), binding.nameInput)&&
            validateInput(binding.usernameInput.text.toString(), binding.usernameInput)&&
            validateInput(binding.passInput.text.toString(), binding.passInput)&&
            validateInput(binding.konfirmPassInput.text.toString(), binding.konfirmPassInput)&&
            (binding.passInput.text.toString() == binding.konfirmPassInput.text.toString())){
            database = FirebaseDatabase.getInstance(
                "https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("petugas/")
            inserttoDatabase(defaultImageURL)
        }
    }

    private fun inserttoDatabase(imageURL: String?){
        var nama = binding.nameInput.text.toString()
        var username = binding.usernameInput.text.toString()
        var password = binding.passInput.text.toString()

        database.child(nama).
        setValue(imageURL?.let { Petugas(nama,password,username, it) }).addOnSuccessListener {
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
                binding.profilepict.setImageURI(uriprof)
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

        val filename = binding.usernameInput.text.toString()
        val ref = FirebaseStorage.getInstance("gs://sisteminformasi-449ae.appspot.com").getReference("profilepict/$filename")

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
        val todata = TambahpetugasFragmentDirections.actionTambahpetugasFragmentToDatapetugasFragment()
        findNavController().navigate(todata)
    }

    companion object{
        const val RequestPermissionCode = 111
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}