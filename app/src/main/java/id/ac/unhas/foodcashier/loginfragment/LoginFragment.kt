package id.ac.unhas.foodcashier.loginfragment

import android.app.ProgressDialog
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import id.ac.unhas.foodcashier.databinding.FragmentLoginBinding
import id.ac.unhas.foodcashier.databinding.FragmentViewPagerBinding
import id.ac.unhas.foodcashier.loginfragment.viewpager.ViewPagerFragmentDirections

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

class LoginFragment : Fragment() {

    private var _binding: FragmentViewPagerBinding? = null
    private var _bindingLogin: FragmentLoginBinding? = null
    private val bindingLogin get()= _bindingLogin!!
    private val binding get() = _binding!!

    private var username = ""
    private var pass = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        _bindingLogin = FragmentLoginBinding.inflate(inflater, container, false)

        progressDialog = ProgressDialog(activity)
        progressDialog.setTitle("Please wait")
        progressDialog.setMessage("Logging in...")
        progressDialog.setCanceledOnTouchOutside(false)

        //val viewPager = activity?.

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        bindingLogin.loginBtn.setOnClickListener {
            //validateData()
            moveToMenu()
        }

        return bindingLogin.root
    }

    private fun validateData(){
        username = bindingLogin.usernameInp.toString().trim()
        pass = bindingLogin.passInp.toString().trim()

        //validate data
        if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
            //invalid email format
            bindingLogin.usernameInp.error = "Invalid username format"
        }
        else if(TextUtils.isEmpty(pass)){
            bindingLogin.passInp.error = "Enter the correct password"
        }
        else{
            //validated, begin login
            firebaseLogin()
        }
    }

    private fun firebaseLogin() {
        //show progress
        progressDialog.show()
        firebaseAuth.signInWithEmailAndPassword(username, pass)
            .addOnSuccessListener {
                //login success
                progressDialog.dismiss()
                val firebaseUser = firebaseAuth.currentUser
                val username = firebaseUser!!.email
                Toast.makeText(requireContext(), "Logged in as $username", Toast.LENGTH_SHORT).show()

                moveToMenu()
            }
            .addOnFailureListener {
                //login failed
                progressDialog.dismiss()
                Toast.makeText(requireContext(), "Login failed due to ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser(){
        //go to menu if user has logged in
        val firebaseUser = firebaseAuth.currentUser
        //user exist
        if (firebaseUser != null){
            moveToMenu()
        }
    }

    private fun moveToMenu(){
        val tomenu = ViewPagerFragmentDirections.actionViewPagerFragmentToMenuFragment()
        findNavController().navigate(tomenu)
    }
}