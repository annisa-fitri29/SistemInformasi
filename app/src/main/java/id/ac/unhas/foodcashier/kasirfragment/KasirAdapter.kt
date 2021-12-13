package id.ac.unhas.foodcashier.kasirfragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.ac.unhas.foodcashier.R
import id.ac.unhas.foodcashier.produkfragment.Produk
import kotlinx.android.synthetic.main.kasir_item.view.*

class KasirAdapter (private val menuList : ArrayList<Pesanan>) : RecyclerView.Adapter<KasirAdapter.MyViewHolder>() {

    private lateinit var  mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.kasir_item,
            parent,false)
        return MyViewHolder(itemView, mListener)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int {

        return menuList.size
    }

    class MyViewHolder(itemView : View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView){

        private val addbtn: CardView = itemView.findViewById(R.id.add_itemkasir)
        fun bind(pesanan: Pesanan){
            val namamakanan : TextView = itemView.findViewById(R.id.nama_makanan)
            val harga : TextView = itemView.findViewById(R.id.harga_kasirtxt)
            val image: ImageView = itemView.findViewById(R.id.menu_image)
            namamakanan.text = pesanan.produk
            harga.text = "Rp. ${pesanan.harga?.let { String.format("%,d", it.toInt()) }}"
            Glide.with(itemView.context).load(pesanan.imageURL).into(image)
        }

        init {
            addbtn.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

}