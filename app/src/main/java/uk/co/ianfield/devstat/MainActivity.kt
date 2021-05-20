package uk.co.ianfield.devstat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import dagger.hilt.android.AndroidEntryPoint
import uk.co.ianfield.devstat.databinding.ActivityMainBinding
import uk.co.ianfield.devstat.model.StatItem
import uk.co.ianfield.devstat.widget.InformationPagerAdapter
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var helper: StatHelper
    private lateinit var hardwareStats: ArrayList<StatItem>
    private lateinit var screenStats: ArrayList<StatItem>
    private lateinit var softwareStats: ArrayList<StatItem>
    private lateinit var featureStats: ArrayList<StatItem>
    private lateinit var cryptoStats: ArrayList<StatItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        (application as DevStatApplication).component()!!.inject(this)
        binding.sendEmail.setOnClickListener { emailClick() }

        hardwareStats = helper.hardwareList
        screenStats = helper.screenList
        softwareStats = helper.softwareList
        featureStats = helper.featureList
        cryptoStats = helper.cryptoList

        // This could probably be done better
        val statGroups = ArrayList<ArrayList<StatItem>>()
        statGroups.addAll(
                listOf(screenStats, softwareStats, hardwareStats, featureStats, cryptoStats)
        )

        binding.viewPager.adapter = InformationPagerAdapter(supportFragmentManager, this,
                intArrayOf(R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware, R.string.title_features, R.string.title_crypto),
                statGroups)

        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_developer -> {
                startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun emailClick() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))

        val stringBuilder = StringBuilder()

        stringBuilder.append(String.format("%s\n", getString(R.string.title_screen_metrics)))
        for (item in screenStats) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_software)))
        for (item in softwareStats) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_hardware)))
        for (item in hardwareStats) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_features)))
        for (item in featureStats) {
            stringBuilder.apply {
                append(item.title)
                append(":\n")
                append(item.info)
                append("\n")
            }

        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_crypto)))
        for (item in cryptoStats) {
            stringBuilder.apply {
                append(item.title)
                append(":\n")
                append(item.info)
                append("\n")
            }
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)))
    }

}
