package com.example.sosudatech

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_adapter.view.*
import android.R.color
import android.graphics.Color
import androidx.cardview.widget.CardView
import java.util.*
import kotlin.collections.ArrayList


class DetailsAdapter(private val context: Context, list: List<Data> = ArrayList(),onItemClickListener:OnItemClickListener) : RecyclerView.Adapter<DetailsAdapter.ViewHolder>()
{
    private var list: List<Data> = ArrayList()
    var onItemClickListener:OnItemClickListener? = null

    init {

        this.list = list
        this.onItemClickListener = onItemClickListener
    }


    override fun getItemCount(): Int {
        return list.size
    }
    var mColors = arrayOf(
        "#FF0000", "#008000", "#0000FF"
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cv_cardView.setCardBackgroundColor(Color.parseColor(mColors[position % 3]))

        holder.tv_Name.setText(list.get(position).first_name +" "+list.get(position).last_name)
        holder.tv_Email.setText("Email Id: "+list.get(position).email)
        Glide.with(context).load(list.get(position).avatar).into(holder.profile_image)
        holder.cv_cardView.setOnClickListener {
            onItemClickListener!!.onItemClick(list.get(position),holder.cv_cardView.getCardBackgroundColor().defaultColor)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.layout_adapter,
                parent,
                false
            )
        )
    }
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tv_Name:TextView = v.tv_Name
        val tv_Email:TextView = v.tv_Email
        val profile_image:ImageView = v.profile_image
        val cv_cardView:CardView = v.cv_cardView
    }
    interface OnItemClickListener {
        fun onItemClick(data:Data,color:Int)
    }

}