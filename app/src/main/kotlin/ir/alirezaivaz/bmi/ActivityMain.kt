package ir.alirezaivaz.bmi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import ir.alirezaivaz.bmi.databinding.ActivityMainBinding

class ActivityMain : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        binding.fab.initGitHubFab()

        binding.resultCard.isVisible = false
        binding.buttonCalculate.setOnClickListener {
            val heightValue = binding.inputHeight.text.toString()
            val weightValue = binding.inputWeight.text.toString()

            if (heightValue.isEmpty()) {
                binding.inputLayoutHeight.isErrorEnabled = true
                binding.inputLayoutHeight.error = getString(R.string.error_enter_height)
            } else {
                binding.inputLayoutHeight.isErrorEnabled = false
            }
            if (weightValue.isEmpty()) {
                binding.inputLayoutWeight.isErrorEnabled = true
                binding.inputLayoutWeight.error = getString(R.string.error_enter_weight)
            } else {
                binding.inputLayoutWeight.isErrorEnabled = false
            }

            if (heightValue.isNotEmpty() && weightValue.isNotEmpty()) {
                val height = binding.inputHeight.text.toString().toDouble() / 100
                val weight = binding.inputWeight.text.toString().toDouble()
                val result = weight / (height * height)
                val bmi = BMI.getFromDouble(result)

                bmi?.let {
                    binding.bmiResult.text = String.format("%.1f", result)
                    binding.bmiResult.enableTextCopy()
                    binding.bmiCategory.setText(it.title)
                    binding.bmiCategory.enableTextCopy()
                    binding.bmiResult.setTextColor(
                        ContextCompat.getColor(
                            this@ActivityMain,
                            it.color
                        )
                    )
                    binding.bmiCategory.setTextColor(
                        ContextCompat.getColor(
                            this@ActivityMain,
                            it.color
                        )
                    )
                    binding.resultCard.isVisible = true
                }
            } else {
                toast(R.string.error_fill_all_fields)
            }

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    putExtra(Intent.EXTRA_TEXT, "${getString(R.string.app_name)}\n${BuildConfig.DOWNLOAD_LINK}")
                }
                startActivity(
                    Intent.createChooser(
                        shareIntent,
                        getString(R.string.action_share_chooser)
                    )
                )
            }
            R.id.action_rate -> {
                try {
                    val intentAction = if (BuildConfig.FLAVOR == "cafebazaar")
                        Intent.ACTION_EDIT
                    else
                        Intent.ACTION_VIEW
                    val intent = Intent(intentAction, Uri.parse(BuildConfig.RATE_INTENT))
                    startActivity(intent)
                } catch (e: Exception) {
                    toast(R.string.error_action_failure)
                }
            }
            R.id.action_apps -> {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.APPS_INTENT))
                    startActivity(intent)
                } catch (e: Exception) {
                    toast(R.string.error_action_failure)
                }
            }
        }
        return true
    }

    private fun ExtendedFloatingActionButton.initGitHubFab() {
        smoothShrink()
        this.setOnClickListener {
            extend()
            smoothShrink()
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this@ActivityMain, R.color.github))
                .build()
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(params)
                .setShowTitle(true)
                .build()
                .launchUrl(this@ActivityMain, Uri.parse(BuildConfig.GITHUB_REPO_URL))
        }
        setOnLongClickListener {
            extend()
            smoothShrink()
            true
        }
    }

    private fun ExtendedFloatingActionButton.smoothShrink() {
        Handler(Looper.getMainLooper()).postDelayed({
            this.shrink()
        }, 2000)
    }

    private fun TextView.enableTextCopy() {
        this.setOnClickListener {
            try {
                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("BMI", this.text)
                clipboardManager.setPrimaryClip(clip)
            } catch (e: Exception) {
                toast(R.string.error_copy_failure)
            }
        }
    }

    private fun Context.toast(@StringRes message: Int, length: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(this, message, length).show()
    }
}