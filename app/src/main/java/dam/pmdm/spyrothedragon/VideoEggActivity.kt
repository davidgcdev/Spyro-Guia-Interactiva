package dam.pmdm.spyrothedragon

import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class VideoEggActivity : AppCompatActivity() {

    private var videoView: VideoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        videoView = VideoView(this)
        videoView!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        setContentView(videoView)

        val videoUri = Uri.parse("android.resource://$packageName/${R.raw.spyro_intro}")
        videoView?.setVideoURI(videoUri)

        videoView?.setOnPreparedListener { mp ->
            mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING)
            mp.start()
        }

        videoView?.setOnCompletionListener {
            finish()
        }

        videoView?.setOnClickListener {
            videoView?.stopPlayback()
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        videoView?.stopPlayback()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView?.stopPlayback()
        videoView = null
    }
}
