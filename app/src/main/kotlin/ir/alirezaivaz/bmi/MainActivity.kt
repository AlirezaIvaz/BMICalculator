package ir.alirezaivaz.bmi

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

class MainActivity : AppCompatActivity() {
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
                            this@MainActivity,
                            it.color
                        )
                    )
                    binding.bmiCategory.setTextColor(
                        ContextCompat.getColor(
                            this@MainActivity,
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

    private fun ExtendedFloatingActionButton.initGitHubFab() {
        smoothShrink()
        this.setOnClickListener {
            extend()
            smoothShrink()
            val params = CustomTabColorSchemeParams.Builder()
                .setToolbarColor(ContextCompat.getColor(this@MainActivity, R.color.github))
                .build()
            CustomTabsIntent.Builder()
                .setDefaultColorSchemeParams(params)
                .setShowTitle(true)
                .build()
                .launchUrl(
                    this@MainActivity,
                    Uri.parse("https://github.com/AlirezaIvaz/BMICalculator")
                )
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