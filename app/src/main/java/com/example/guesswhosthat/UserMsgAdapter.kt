package com.example.guesswhosthat

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

data class UserMsg(val msg: String, val userId: Int, val date: String)

class UserMsgAdapter (private val data : Array<UserMsg>,
                      private val clickListener : (UserMsg) -> Unit) :
    RecyclerView.Adapter<UserMsgAdapter.UserMsgViewHolder>() {

    class UserMsgViewHolder(val item : View) : RecyclerView.ViewHolder(item) {
        val msg_layout : LinearLayout = item.findViewById(R.id.msg_layout)
        val msg : TextView = item.findViewById(R.id.msg)
        val date : TextView = item.findViewById(R.id.date)

        fun bindUserMsg(uMsg : UserMsg) {
            msg.text = uMsg.msg
            date.text = uMsg.date

            if (uMsg.userId == 1) {
                msg_layout.gravity = Gravity.END
                msg.backgroundTintList = ColorStateList.valueOf(item.context.resources.getColor(R.color.blue))
            }
            else {
                msg_layout.gravity = Gravity.START
                msg.backgroundTintList = ColorStateList.valueOf(item.context.resources.getColor(R.color.primary))
            }
        }
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMsgViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_usermsg, parent, false) as LinearLayout
        return UserMsgViewHolder(item)
    }

    @Override
    override fun onBindViewHolder(holder: UserMsgViewHolder, position: Int) {
        val u = data[position]
        holder.bindUserMsg(u)
        holder.item.setOnClickListener{clickListener(u)}
    }

    @Override
    override fun getItemCount() = data.size
}