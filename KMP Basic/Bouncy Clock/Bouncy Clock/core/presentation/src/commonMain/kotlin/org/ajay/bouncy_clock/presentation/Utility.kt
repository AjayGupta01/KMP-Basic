package org.ajay.bouncy_clock.presentation

import androidx.compose.ui.graphics.Color
import kotlin.random.Random
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object Utility {
    fun <T> T.pad() = this.toString().padStart(2,'0')

    fun String.toTitleCase() = this.replaceFirstChar {
        it.uppercase()
    }

    fun formatTime(hours: String, minutes: String, seconds: String): String {
        return "$hours:$minutes:$seconds"
    }

/*    fun Context.getAppVersion(): String {
        return try {
            val packageInfo: PackageInfo =
                packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            val versionCode =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    packageInfo.longVersionCode.toString()
                } else {
                    @Suppress("DEPRECATION")
                    packageInfo.versionCode.toString()
                }
            "$versionName ($versionCode)"
        } catch (e: PackageManager.NameNotFoundException) {
            "Unknown Version"
        }
    }*/


 /*   fun getPhoneNameAndModel(): String {
        val manufacturer = Build.MANUFACTURER.toTitleCase()
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            model
        } else {
            "$manufacturer $model"
        }
    }

    fun Context.showToast(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }*/


    /**
    * Converts formatted time to duration.
     * E.g 05:25:36 to 5h 25m 36s
    */
    fun String.toFormattedDuration(): String {
        val timeParts = this.split(':').map { it.toInt() }
        val (hours, minutes, seconds) = when (timeParts.size) {
            2 -> listOf(0, timeParts[0], timeParts[1]) // Default hours to 0 if size is 2
            3 -> timeParts
            else -> throw IllegalArgumentException("Invalid time format")
        }
        return (hours.hours + minutes.minutes + seconds.seconds).toString()
    }

    /**
     * Converts formatted time to wholeMilliSeconds.
     */
    fun String.toTimeInMillis(): Long {
        val timeParts = this.split(':').map { it.toInt() }
        val (hours, minutes, seconds) = when (timeParts.size) {
            2 -> listOf(0, timeParts[0], timeParts[1]) // Default hours to 0 if size is 2
            3 -> timeParts
            else -> throw IllegalArgumentException("Invalid time format")
        }
        return (hours.hours + minutes.minutes + seconds.seconds).inWholeMilliseconds
    }

    /**
     * Generate random dark color
     */
    fun getRandomDarkColor(): Color {
        val base = 0.6f // Upper limit for dark colors
        return Color(
            red = Random.nextFloat() * base,
            green = Random.nextFloat() * base,
            blue = Random.nextFloat() * base,
            alpha = 1f // Fully opaque
        )
    }

    fun Long.toFormattedClockTimeFromWholeSeconds(): String{
        val duration = this.toInt().seconds
        val hours = duration.toComponents { hours, _, _, _ -> hours.toInt().pad() }
        val minutes = duration.toComponents { _, minutes, _, _ -> minutes.pad() }
        val seconds = duration.toComponents { _, _, seconds, _ -> seconds.pad() }
        return formatTime(
            hours = hours,
            minutes = minutes,
            seconds = seconds
        )
    }

}