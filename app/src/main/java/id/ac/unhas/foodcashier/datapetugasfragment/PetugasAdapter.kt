package id.ac.unhas.foodcashier.datapetugasfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unhas.foodcashier.R

class PetugasAdapter (private val userList : ArrayList<DataPetugas>) : RecyclerView.Adapter<PetugasAdapter.MyViewHolder>() {

    private lateinit var  mListener: onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.datapetugas_item,
            parent,false)
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //val currentitem = userList[position]
        //holder.name.text = currentitem.nama
        holder.bind(userList[position])
    }

    override fun getItemCount(): Int {

        return userList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView){

        fun bind(dataPetugas: DataPetugas){
            val name : TextView = itemView.findViewById(R.id.nama_petugas)
            val username : TextView = itemView.findViewById(R.id.username_petugas)
            val image: ImageView = itemView.findViewById(R.id.icon_profil)

            name.text = dataPetugas.nama
            username.text = dataPetugas.username
            Glide.with(itemView.context).load(dataPetugas.imageURL).into(image)
        }
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

    }



}