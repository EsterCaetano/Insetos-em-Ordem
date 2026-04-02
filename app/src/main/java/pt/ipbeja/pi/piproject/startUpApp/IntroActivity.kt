package pt.ipbeja.pi.piproject.startUpApp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import pt.ipbeja.pi.piproject.R

/**
 * Activity for the introductory onboarding screens.
 */
class IntroActivity : AppCompatActivity() {

    private lateinit var screenPager: ViewPager
    private lateinit var introViewPagerAdapter: IntroViewPagerAdapter
    private lateinit var tabIndicator: TabLayout
    private lateinit var btnNext: Button
    private lateinit var btnGetStarted: Button
    private lateinit var btnAnim: Animation
    private lateinit var tvSkip: TextView
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Make the activity full screen
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Check if it's the first time the app is started to show privacy policy
        val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
        if (prefs.getBoolean("firstStart", true)) {
            showStartDialog()
        }

        // Check if intro has been seen before
        if (restorePrefData()) {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.activity_intro)

        // Initialize views
        btnNext = findViewById(R.id.btn_next)
        btnGetStarted = findViewById(R.id.btn_get_started)
        tabIndicator = findViewById(R.id.tab_indicator)
        btnAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.button_animation)
        tvSkip = findViewById(R.id.tv_skip)

        // Create list of screens
        val mList = listOf(
            ScreenItem(
                "Sobre a aplicação / About app",
                """
                (PT) O objectivo deste aplicação é dar a conhecer a diversidade de insetos através da experiência de identificação.
                (EN) The purpose of this application is to make known the diversity of insects through the identification experience.
                """.trimIndent(),
                R.drawable.intro_2
            ),
            ScreenItem(
                "Sobre a aplicação / About app",
                """
                (PT) Para iniciar a experiência de identificação tem que ter a certeza absoluta que está perante um inseto.
                (EN) To start the identification experiment you have to be absolutely certain that you are facing an insect.
                """.trimIndent(),
                R.drawable.intro_2
            ),
            ScreenItem(
                "Sobre a aplicação / About app",
                """
                (PT) Na página de Créditos encontra-se a informação completa sobre o funcionamento da aplicação.
                (EN) On the Credits page you will find the complete information on the operation of the application.
                """.trimIndent(),
                R.drawable.intro_2
            )
        )

        // Setup ViewPager
        screenPager = findViewById(R.id.screen_viewpager)
        introViewPagerAdapter = IntroViewPagerAdapter(this, mList)
        screenPager.adapter = introViewPagerAdapter

        // Setup TabLayout with ViewPager
        tabIndicator.setupWithViewPager(screenPager)

        // Next button click listener
        btnNext.setOnClickListener {
            position = screenPager.currentItem
            if (position < mList.size) {
                position++
                screenPager.currentItem = position
            }
            if (position == mList.size - 1) {
                loadLastScreen()
            }
        }

        // TabLayout change listener
        tabIndicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.position == mList.size - 1) {
                    loadLastScreen()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        // Get Started button click listener
        btnGetStarted.setOnClickListener {
            startActivity(Intent(applicationContext, MainActivity::class.java))
            savePrefsData()
            finish()
        }

        // Skip button click listener
        tvSkip.setOnClickListener {
            screenPager.currentItem = mList.size
        }
    }

    private fun showStartDialog() {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.intro_2)
            .setTitle(R.string.Privacy_Policy)
            .setMessage(R.string.Privacy_Policy_INFO_MAIN)
            .setPositiveButton(R.string.agree) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()

        getSharedPreferences("prefs", MODE_PRIVATE).edit().putBoolean("firstStart", false).apply()
    }

    private fun restorePrefData(): Boolean {
        return applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
            .getBoolean("isIntroOpened", false)
    }

    private fun savePrefsData() {
        applicationContext.getSharedPreferences("myPrefs", MODE_PRIVATE)
            .edit()
            .putBoolean("isIntroOpened", true)
            .apply()
    }

    private fun loadLastScreen() {
        btnNext.visibility = View.INVISIBLE
        btnGetStarted.visibility = View.VISIBLE
        tvSkip.visibility = View.INVISIBLE
        tabIndicator.visibility = View.VISIBLE
        btnGetStarted.startAnimation(btnAnim)
    }
}