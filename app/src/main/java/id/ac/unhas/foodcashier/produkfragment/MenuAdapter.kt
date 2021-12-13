package id.ac.unhas.foodcashier.produkfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unhas.foodcashier.R

class MenuAdapter (private val menuList : ArrayList<Produk>) : RecyclerView.Adapter<MenuAdapter.MyViewHolder>() {

    private lateinit var  mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.produk_item,
            parent,false)
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //val currentitem = userList[position]
        //holder.name.text = currentitem.nama
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int {

        return menuList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView){

        fun bind(produkMenu: Produk){
            val produk : TextView = itemView.findViewById(R.id.nama_produk)
            val harga : TextView = itemView.findViewById(R.id.harga_produk)
            val image: ImageView = itemView.findViewById(R.id.menulistimage)

            produk.text = produkMenu.produk
            harga.text = produkMenu.harga
            Glide.with(itemView.context).load(produkMenu.imageURL).into(image)
        }

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}