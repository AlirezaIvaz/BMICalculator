package ir.alirezaivaz.bmi

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

enum class BMI(
    val range: ClosedRange<Double>,
    @StringRes
    val title: Int,
    @ColorRes
    val color: Int
) {
    VerySeverelyUnderweight(Double.MIN_VALUE..15.9, R.string.result_very_severely_underweight, R.color.blue),
    SeverelyUnderweight(16.0..16.9, R.string.result_severely_underweight, R.color.blue),
    Underweight(17.0..18.4, R.string.result_underweight, R.color.blue),
    Normal(18.5..24.9, R.string.result_normal, R.color.green),
    Overweight(25.0..29.9, R.string.result_overweight, R.color.orange),
    ObeseI(30.0..34.9, R.string.result_obese_1, R.color.orange),
    ObeseII(35.0..39.9, R.string.result_obese_2, R.color.orange),
    ObeseIII(40.0..Double.MAX_VALUE, R.string.result_obese_3, R.color.red);
    companion object {
        fun getFromDouble(range: Double): BMI? {
            return BMI.values().firstOrNull { it.range.contains(range) }
        }
    }
}