package com.example.guesswhosthat

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

data class UserDetails(val name: String, val socketId: Int, val status: Boolean)

class FriendDetailsAdapter (private val data : Array<UserDetails>,
                            private val clickListener : (UserDetails) -> Unit) :
    RecyclerView.Adapter<FriendDetailsAdapter.UserDetailsViewHolder>() {

    class UserDetailsViewHolder(val item : View) : RecyclerView.ViewHolder(item) {
        val name : TextView = item.findViewById(R.id.user_friendName)
        val socketId : TextView = item.findViewById(R.id.user_socketId)
        val status : TextView = item.findViewById(R.id.user_status)
        val btn_sendInvitation : Button = item.findViewById(R.id.btn_sendInvitation)

        fun bindUserDetails(u : UserDetails) {
            name.text = u.name
            socketId.text = u.socketId.toString()

            if (u.status) {
                status.text = "Online"
                status.setTextColor(Color.parseColor("#80b918"))
            }
            else {
                status.text = "Offline"
                status.setTextColor(Color.parseColor("#ef233c"))
            }

            btn_sendInvitation.setOnClickListener {
                Toast.makeText(item.context, "Invitacion enviada a ${u.name}", Toast.LENGTH_SHORT).show()
                var i : Intent = Intent(MenuActivity.fa, GameActivity::class.java).apply {
                    putExtra(BTN_PRESSED, "1")
                }
                MenuActivity.fa.startActivity(i)
                MenuActivity.fa.finish()
            }
        }
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_user, parent, false) as LinearLayout
        return UserDetailsViewHolder(item)
    }

    @Override
    override fun onBindViewHolder(holder: UserDetailsViewHolder, position: Int) {
        val u = data[position]
        holder.bindUserDetails(u)
        holder.item.setOnClickListener{clickListener(u)}
    }

    @Override
    override fun getItemCount() = data.size
}