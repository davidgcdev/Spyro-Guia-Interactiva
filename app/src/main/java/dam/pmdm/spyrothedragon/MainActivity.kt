package dam.pmdm.spyrothedragon

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaPlayer
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dam.pmdm.spyrothedragon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var navController: NavController? = null

    private lateinit var guideOverlay: View
    private lateinit var btnSkip: Button
    private lateinit var btnNext: Button
    private lateinit var guideIndicator: ImageView
    private lateinit var guideBubble: TextView
    private lateinit var welcomeLayout: LinearLayout
    private lateinit var finalLayout: LinearLayout
    private lateinit var btnStart: Button
    private lateinit var btnFinish: Button
    private lateinit var bottomNav: BottomNavigationView

    private var currentStep = 0
    private val TOTAL_STEPS = 5
    private lateinit var prefs: SharedPreferences
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
        setupGuideViews()
        prefs = getSharedPreferences("spyro_prefs", Context.MODE_PRIVATE)
        if (!prefs.getBoolean("guia_completada", false)) {
            showGuide()
        }
    }

    override fun onResume() {
        super.onResume()
        val currentDest = navController?.currentDestination?.id
        when (currentDest) {
            R.id.navigation_characters -> bottomNav.selectedItemId = R.id.nav_characters
            R.id.navigation_worlds -> bottomNav.selectedItemId = R.id.nav_worlds
            R.id.navigation_collectibles -> bottomNav.selectedItemId = R.id.nav_collectibles
        }
    }

    private fun setupNavigation() {
        val navHostFragment: Fragment? =
            supportFragmentManager.findFragmentById(R.id.navHostFragment)
        navHostFragment?.let {
            navController = NavHostFragment.findNavController(it)
            NavigationUI.setupWithNavController(binding.navView, navController!!)
            NavigationUI.setupActionBarWithNavController(this, navController!!)
        }
        binding.navView.setOnItemSelectedListener { menuItem ->
            selectedBottomMenu(menuItem)
        }
        navController?.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_characters,
                R.id.navigation_worlds,
                R.id.navigation_collectibles ->
                    supportActionBar?.setDisplayHomeAsUpEnabled(false)

                else ->
                    supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    private fun setupGuideViews() {
        guideOverlay = layoutInflater.inflate(R.layout.guide_overlay, null)
        val rootView = findViewById<FrameLayout>(android.R.id.content)
        rootView.addView(guideOverlay)

        btnSkip = guideOverlay.findViewById(R.id.btn_skip_guide)
        btnNext = guideOverlay.findViewById(R.id.btn_next_guide)
        guideIndicator = guideOverlay.findViewById(R.id.guide_indicator)
        guideBubble = guideOverlay.findViewById(R.id.guide_bubble)
        welcomeLayout = guideOverlay.findViewById(R.id.welcome_layout)
        finalLayout = guideOverlay.findViewById(R.id.final_layout)
        btnStart = guideOverlay.findViewById(R.id.btn_start_guide)
        btnFinish = guideOverlay.findViewById(R.id.btn_finish_guide)
        bottomNav = findViewById(R.id.navView)

        btnSkip.setOnClickListener {
            playSound(R.raw.whoosh1)
            finishGuide()
        }
        btnNext.setOnClickListener {
            playSound(R.raw.whoosh1)
            nextStep()
        }
        btnStart.setOnClickListener {
            playSound(R.raw.gem_absorb1)
            welcomeLayout.visibility = View.GONE
            currentStep = 1
            showStep(1)
        }
        btnFinish.setOnClickListener {
            playSound(R.raw.elec_zap1_1)
            finishGuide()
        }

        guideOverlay.setOnClickListener { }
        guideOverlay.visibility = View.GONE
    }

    private fun showGuide() {
        guideOverlay.visibility = View.VISIBLE
        currentStep = 0
        showStep(0)
        bottomNav.isEnabled = false
    }

    private fun showStep(step: Int) {
        welcomeLayout.visibility = View.GONE
        guideIndicator.visibility = View.GONE
        guideBubble.visibility = View.GONE
        finalLayout.visibility = View.GONE

        when (step) {
            0 -> {
                welcomeLayout.visibility = View.VISIBLE
                btnNext.visibility = View.GONE
            }

            1 -> {
                navController?.navigate(R.id.navigation_characters)
                showTabGuide(0, getString(R.string.guide_bubble_characters))
                btnNext.visibility = View.VISIBLE
                btnNext.text = getString(R.string.guide_next)
            }

            2 -> {
                navController?.navigate(R.id.navigation_worlds)
                showTabGuide(1, getString(R.string.guide_bubble_worlds))
                btnNext.visibility = View.VISIBLE
                btnNext.text = getString(R.string.guide_next)
            }

            3 -> {
                navController?.navigate(R.id.navigation_collectibles)
                showTabGuide(2, getString(R.string.guide_bubble_collectibles))
                btnNext.visibility = View.VISIBLE
                btnNext.text = getString(R.string.guide_next)
            }

            4 -> {
                showInfoGuide()
                btnNext.visibility = View.VISIBLE
                btnNext.text = getString(R.string.guide_finish)
            }
        }
    }

    private fun showTabGuide(tabIndex: Int, message: String) {
        val screenWidth = resources.displayMetrics.widthPixels
        val tabWidth = screenWidth / 3
        val tabCenterX = (tabIndex * tabWidth) + (tabWidth / 2)

        guideBubble.alpha = 0f
        guideBubble.text = message
        guideBubble.visibility = View.VISIBLE
        guideIndicator.visibility = View.VISIBLE

        guideIndicator.post {
            val indicatorHalf = guideIndicator.width / 2f
            guideIndicator.translationX = tabCenterX - indicatorHalf

            val scaleX = ObjectAnimator.ofFloat(guideIndicator, "scaleX", 1f, 1.3f, 1f).apply {
                repeatCount = 2
                duration = 300
            }
            val scaleY = ObjectAnimator.ofFloat(guideIndicator, "scaleY", 1f, 1.3f, 1f).apply {
                repeatCount = 2
                duration = 300
            }
            val fadeIn = ObjectAnimator.ofFloat(guideBubble, "alpha", 0f, 1f).apply {
                duration = 600
            }
            AnimatorSet().apply {
                playTogether(scaleX, scaleY, fadeIn)
                start()
            }
        }
    }

    private fun showInfoGuide() {
        val screenWidth = resources.displayMetrics.widthPixels

        guideBubble.alpha = 0f
        guideBubble.text = getString(R.string.guide_bubble_info)
        guideBubble.visibility = View.VISIBLE
        guideIndicator.visibility = View.VISIBLE

        guideIndicator.post {
            val indicatorSize = guideIndicator.width.toFloat()
            guideIndicator.translationX = screenWidth - indicatorSize - 8f
            guideIndicator.translationY = -(guideIndicator.bottom.toFloat()) + indicatorSize

            val scaleX = ObjectAnimator.ofFloat(guideIndicator, "scaleX", 1f, 1.3f, 1f).apply {
                repeatCount = 2
                duration = 300
            }
            val scaleY = ObjectAnimator.ofFloat(guideIndicator, "scaleY", 1f, 1.3f, 1f).apply {
                repeatCount = 2
                duration = 300
            }
            val fadeIn = ObjectAnimator.ofFloat(guideBubble, "alpha", 0f, 1f).apply {
                duration = 600
            }
            AnimatorSet().apply {
                playTogether(scaleX, scaleY, fadeIn)
                start()
            }
        }
    }

    private fun nextStep() {
        if (currentStep < TOTAL_STEPS - 1) {
            currentStep++
            showStep(currentStep)
        } else {
            showFinalStep()
        }
    }

    private fun showFinalStep() {
        guideIndicator.visibility = View.GONE
        guideBubble.visibility = View.GONE
        finalLayout.visibility = View.VISIBLE
        btnNext.visibility = View.GONE
    }

    private fun finishGuide() {
        prefs.edit().putBoolean("guia_completada", true).apply()
        guideOverlay.visibility = View.GONE
        bottomNav.isEnabled = true

        val currentDest = navController?.currentDestination?.id
        when (currentDest) {
            R.id.navigation_characters -> bottomNav.selectedItemId = R.id.nav_characters
            R.id.navigation_worlds -> bottomNav.selectedItemId = R.id.nav_worlds
            R.id.navigation_collectibles -> bottomNav.selectedItemId = R.id.nav_collectibles
        }

        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun playSound(resId: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            mediaPlayer = MediaPlayer.create(applicationContext, resId)
            mediaPlayer?.setOnCompletionListener { it.release() }
            mediaPlayer?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun selectedBottomMenu(menuItem: MenuItem): Boolean {
        playSound(R.raw.whoosh1)
        when (menuItem.itemId) {
            R.id.nav_characters ->
                navController?.navigate(R.id.navigation_characters)

            R.id.nav_worlds ->
                navController?.navigate(R.id.navigation_worlds)

            else ->
                navController?.navigate(R.id.navigation_collectibles)
        }
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_info) {
            showInfoDialog()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun showInfoDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.title_about)
            .setMessage(R.string.text_about)
            .setPositiveButton(R.string.accept, null)
            .show()
    }
}
