package id.ac.unhas.foodcashier.loginfragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.*
import id.ac.unhas.foodcashier.databinding.FragmentLoginAdminBinding
import id.ac.unhas.foodcashier.loginfragment.viewpager.ViewPagerFragmentDirections


class LoginAdminFragment : Fragment() {

    lateinit var database: DatabaseReference
    lateinit var sharedpref : SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    private var _bindingLoginAdmin: FragmentLoginAdminBinding? = null
    private val bindingLoginAdmin get()= _bindingLoginAdmin!!

    private var username: String? = ""
    private var pass: String? = ""
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _bindingLoginAdmin = FragmentLoginAdminBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        bindingLoginAdmin.loginBtn.setOnClickListener {
            validateData()
        }

        return bindingLoginAdmin.root
    }


    private fun validateData(){
        username = bindingLoginAdmin.usernameInp.text.toString().trim()
        pass = bindingLoginAdmin.passInp.text.toString().trim()

        //validate data
        if (!(username!!.matches(emailPattern.toRegex()))){
            //invalid email format
            bindingLoginAdmin.usernameInp.error = "Invalid username format"
        }
        else if(TextUtils.isEmpty(pass)){
            bindingLoginAdmin.passInp.error = "Enter the correct password"
        }
        else{
            //validated, begin login
            progressDialog.show()
            getDataFromFirebase()
        }
    }

    private fun getDataFromFirebase(){
        database = FirebaseDatabase.getInstance(
            "https://sisteminformasi-449ae-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("admin/")

        var username_data = ""
        var password_data = ""

        database.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("cancel", p0.toString())
            }
            override fun onDataChange(p0: DataSnapshot) {
                for (i in p0.children){
                    username_data = i.child("username").value.toString()
                    password_data = i.child("password").value.toString()

                    if(pass == password_data && username == username_data){
                        progressDialog.dismiss()
                        moveToMenu()

                    }else{
                        Log.e("wrong data", p0.toString())
                        progressDialog.dismiss()
                        //validateDatabase(bindingLoginAdmin.passInp)
                    }
                }
            }
        })
    }

    private fun loadlogin() {
        sharedpref = SharedPreferences(requireContext())
        var savedPass = sharedpref.getStringPass("STRING_KEY")
        var savedUsername = sharedpref.getStringUserame("STRING_KEY")
        if (savedPass != null || savedUsername != null) {
            pass = savedPass
            username = savedUsername

            validateData()
        }
    }

    private fun savelogin(){
        sharedpref = SharedPreferences(requireContext())
        sharedpref.putPass("STRING_KEY", pass!!)
        sharedpref.putUsername("STRING_KEY", username!!)
    }

    private fun moveToMenu(){
        val tomenu = ViewPagerFragmentDirections.actionViewPagerFragmentToMenuFragment()
        findNavController().navigate(tomenu)
    }

    private fun validateDatabase(editText: EditText):Boolean{
        editText.error = "* Password atau username tidak ditemukan"
        return false
    }
}