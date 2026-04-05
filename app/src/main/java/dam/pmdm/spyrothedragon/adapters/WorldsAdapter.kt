package dam.pmdm.spyrothedragon.adapters

import android.content.Intent
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dam.pmdm.spyrothedragon.R
import dam.pmdm.spyrothedragon.VideoEggActivity
import dam.pmdm.spyrothedragon.models.World

class WorldsAdapter(
    private val list: List<World>
) : RecyclerView.Adapter<WorldsAdapter.WorldsViewHolder>() {

    private val worldImages = mapOf(
        "sunny_beach" to R.drawable.sunny_beach,
        "midday_gardens" to R.drawable.midday_gardens,
        "autumn_plains" to R.drawable.autumn_plains,
        "glimmer" to R.drawable.glimmer,
        "cloud_spires" to R.drawable.cloud_spires,
        "hurricane_halls" to R.drawable.hurricane_halls,
        "frozen_altars" to R.drawable.frozen_altars,
        "lost_fleet" to R.drawable.lost_fleet,
        "sunset_beach" to R.drawable.sunset_beach
    )

    private var clickedPosition = -1
    private var clickCount = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorldsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cardview, parent, false)
        return WorldsViewHolder(view)
    }

    override fun onBindViewHolder(holder: WorldsViewHolder, position: Int) {
        val world = list[position]
        holder.nameTextView.text = world.name
        val drawableRes = worldImages[world.image] ?: R.drawable.placeholder
        holder.imageImageView.setImageResource(drawableRes)

        holder.itemView.setOnClickListener {
            // Flash visual en cada clic
            val flash = AnimationUtils.loadAnimation(
                holder.itemView.context, R.anim.flash
            )
            holder.imageImageView.startAnimation(flash)

            if (clickedPosition == position) {
                clickCount++
            } else {
                clickedPosition = position
                clickCount = 1
            }

            if (clickCount >= 3) {
                // Easter Egg: 3 clics en el mismo mundo → vídeo
                clickCount = 0
                clickedPosition = -1
                val mp = MediaPlayer.create(holder.itemView.context, R.raw.elec_zap1_1)
                mp?.start()
                mp?.setOnCompletionListener { it.release() }
                holder.itemView.postDelayed({
                    val intent = Intent(holder.itemView.context, VideoEggActivity::class.java)
                    holder.itemView.context.startActivity(intent)
                }, 300)
            } else {
                // Clics 1 y 2: sonido de feedback
                val mp = MediaPlayer.create(holder.itemView.context, R.raw.gem_absorb1)
                mp?.start()
                mp?.setOnCompletionListener { it.release() }
            }
        }
    }

    override fun getItemCount(): Int = list.size

    class WorldsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.name)
        val imageImageView: ImageView = itemView.findViewById(R.id.image)
    }
}