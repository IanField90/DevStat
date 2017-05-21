package uk.co.ianfield.devstat

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import uk.co.ianfield.devstat.model.StatItem
import uk.co.ianfield.devstat.widget.InformationPagerAdapter
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    internal var tabLayout: TabLayout? = null
    internal var viewPager: ViewPager? = null

    @Inject lateinit var helper: StatHelper
    private var hardwareStats: ArrayList<StatItem>? = null
    private var screenStats: ArrayList<StatItem>? = null
    private var softwareStats: ArrayList<StatItem>? = null
    private var featureStats: ArrayList<StatItem>? = null
    private var cryptoStats: ArrayList<StatItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (application as DevStatApplication).component()!!.inject(this)
        viewPager = findViewById(R.id.viewpager) as ViewPager?
        tabLayout = findViewById(R.id.tabs) as TabLayout?

        findViewById(R.id.send_email).setOnClickListener({ emailClick() })

        hardwareStats = helper.hardwareList
        screenStats = helper.screenList
        softwareStats = helper.softwareList
        featureStats = helper.featureList
        cryptoStats = helper.cryptoList

        // This could probably be done better
        val statGroups = ArrayList<ArrayList<StatItem>>()
        statGroups.addAll(
                Arrays.asList<ArrayList<StatItem>>(screenStats, softwareStats, hardwareStats, featureStats, cryptoStats)
        )

        viewPager!!.adapter = InformationPagerAdapter(supportFragmentManager, this,
                intArrayOf(R.string.title_screen_metrics, R.string.title_software, R.string.title_hardware, R.string.title_features, R.string.title_crypto),
                statGroups)

        tabLayout!!.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        when (item.itemId) {
            R.id.action_about -> {
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                return true
            }
            R.id.action_developer -> {
                startActivity(Intent(Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun emailClick() {
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", "", null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.email_subject))

        val stringBuilder = StringBuilder()

        stringBuilder.append(String.format("%s\n", getString(R.string.title_screen_metrics)))
        for (item in screenStats!!) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_software)))
        for (item in softwareStats!!) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_hardware)))
        for (item in hardwareStats!!) {
            stringBuilder.append(item.toString())
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_features)))
        for (item in featureStats!!) {
            stringBuilder.append(item.title)
            stringBuilder.append(":\n")
            stringBuilder.append(item.info)
            stringBuilder.append("\n")
        }

        stringBuilder.append(String.format("\n%s\n", getString(R.string.title_crypto)))
        for (item in cryptoStats!!) {
            stringBuilder.append(item.title)
            stringBuilder.append(":\n")
            stringBuilder.append(item.info)
            stringBuilder.append("\n")
        }

        emailIntent.putExtra(Intent.EXTRA_TEXT, stringBuilder.toString())
        startActivity(Intent.createChooser(emailIntent, getString(R.string.send_email)))
    }

}
