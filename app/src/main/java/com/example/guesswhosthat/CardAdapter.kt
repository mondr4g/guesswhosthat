package com.example.guesswhosthat

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

data class Character(val name : String, val imgId : Int)

class CardAdapter(private val data : Array<Character>,
                 private val clickListener : (Character) -> Unit) :
    RecyclerView.Adapter<CardAdapter.CardsViewHolder>() {

    class CardsViewHolder(val item : View) : RecyclerView.ViewHolder(item) {
        // Character
        val img : ImageView = item.findViewById(R.id.char_img) as ImageView
        val name : TextView = item.findViewById(R.id.char_name) as TextView

        // Card
        val isFront : TextView = item.findViewById(R.id.isFront) as TextView
        val card_front : LinearLayout = item.findViewById(R.id.card_front) as LinearLayout
        val card_back : LinearLayout = item.findViewById(R.id.card_back) as LinearLayout

        val scale : Float = item.context.resources.displayMetrics.density

        // set front animation
        val front_anim : AnimatorSet = AnimatorInflater.loadAnimator(item.context, R.animator.front_animator) as AnimatorSet
        val back_anim : AnimatorSet = AnimatorInflater.loadAnimator(item.context, R.animator.back_animator) as AnimatorSet

        fun bindCard(c : Character) {
            img.setImageResource(c.imgId)
            name.text = c.name

            // camera scale
            card_front.cameraDistance = 8000 * scale
            card_back.cameraDistance = 8000 * scale

            card_front.setOnClickListener {
                if (isFront.text == "0") {
                    front_anim.setTarget(card_front)
                    back_anim.setTarget(card_back)
                    front_anim.start()
                    back_anim.start()
                    isFront.text = "1"
                }
                else {
                    front_anim.setTarget(card_back)
                    back_anim.setTarget(card_front)
                    front_anim.start()
                    back_anim.start()
                    isFront.text = "0"
                }
            }

            card_back.setOnClickListener {
                if (isFront.text == "1") {
                    front_anim.setTarget(card_back)
                    back_anim.setTarget(card_front)
                    front_anim.start()
                    back_anim.start()
                    isFront.text = "0"
                }
                else {
                    front_anim.setTarget(card_front)
                    back_anim.setTarget(card_back)
                    front_anim.start()
                    back_anim.start()
                    isFront.text = "1"
                }
            }
        }
    }

    @Override
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val item = LayoutInflater.from(parent.context)
            .inflate(R.layout.listitem_personaje, parent, false) as ConstraintLayout
        return CardsViewHolder(item)
    }

    @Override
    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        val c = data[position]
        holder.bindCard(c)
        holder.item.setOnClickListener{clickListener(c)}
    }

    @Override
    override fun getItemCount() = data.size
}