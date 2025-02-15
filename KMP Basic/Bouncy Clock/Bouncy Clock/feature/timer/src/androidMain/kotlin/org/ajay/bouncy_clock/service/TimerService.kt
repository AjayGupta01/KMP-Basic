package org.ajay.bouncy_clock.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Binder
import android.os.CountDownTimer
import android.os.IBinder
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import org.ajay.bouncy_clock.notification.FlipAppNotificationManager
import org.ajay.bouncy_clock.presentation.Utility.formatTime
import org.ajay.bouncy_clock.presentation.Utility.pad
import org.ajay.bouncy_clock.presentation.Utility.toTimeInMillis
import org.ajay.bouncy_clock.service.TimerConstants.CANCEL_REQUEST_CODE
import org.ajay.bouncy_clock.service.TimerConstants.PAUSE_REQUEST_CODE
import org.ajay.bouncy_clock.service.TimerConstants.RESUME_REQUEST_CODE
import org.ajay.bouncy_clock.service.TimerConstants.TIMER_NOTIFICATION_ID
import org.ajay.bouncy_clock.service.TimerConstants.TIME_VALUE
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import kotlin.getValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TimerService : Service() {

    val notificationManager: NotificationManager by inject()
    val appNotificationManager: FlipAppNotificationManager by inject(named("TimerNotificationManager"))
    val notificationBuilder: NotificationCompat.Builder by inject(named("TimerNotificationBuilder"))

    private val timerBinder = TimerBinder()

    private var countDownTimer: CountDownTimer? = null
    private var remainingTime by mutableStateOf(Duration.ZERO)
    var timerState by mutableStateOf(TimerState.CANCELLED)
        private set

    private var mediaPlayer: MediaPlayer? = null // To handle the alert sound
    val hours: String
        get() = remainingTime.toComponents { hours, _, _, _ -> hours.toInt().pad() }
    val minutes: String
        get() = remainingTime.toComponents { _, minutes, _, _ -> minutes.pad() }
    val seconds: String
        get() = remainingTime.toComponents { _, _, seconds, _ -> seconds.pad() }

    override fun onBind(p0: Intent?): IBinder = timerBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            when (it) {
                TimerState.RUNNING.name -> handleStartCommand(intent)

                TimerState.PAUSED.name -> pauseTimer()

                TimerState.CANCELLED.name -> stopTimer()
            }
        }
        return START_STICKY
    }

    private fun handleStartCommand(intent: Intent) {
        if (timerState != TimerState.RUNNING) {
            val duration = intent.getStringExtra(TIME_VALUE)?.toTimeInMillis() ?: 0L
            startForegroundService()
            startTimer(duration) { hours, minutes, seconds ->
                updateNotification(hours, minutes, seconds)
            }
        }
    }

    private fun startTimer(
        durationInMillis: Long,
        onTick: (String, String, String) -> Unit
    ) {
        countDownTimer?.cancel()
        val adjustedDuration =
            if (timerState == TimerState.PAUSED) remainingTime.inWholeMilliseconds else durationInMillis


        countDownTimer = object : CountDownTimer(adjustedDuration, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished.milliseconds
                onTick(hours, minutes, seconds)
            }

            override fun onFinish() {
                handleTimerCompletion()
            }
        }.start()
        timerState = TimerState.RUNNING

    }

    private fun handleTimerCompletion() {
        countDownTimer?.cancel()
        remainingTime = Duration.ZERO
        timerState = TimerState.COMPLETED
        playAlertSound()
        startOvertimeCountdown()
    }

    private fun startOvertimeCountdown() {
        countDownTimer = object : CountDownTimer(3600000, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedSeconds = (3600000 - millisUntilFinished).milliseconds.inWholeSeconds
                remainingTime = elapsedSeconds.seconds
                updateOverTimeNotification(elapsedSeconds)
            }

            override fun onFinish() {
                stopTimer()
            }
        }.start()
    }

    private fun updateNotification(hours: String, minutes: String, seconds: String) {
        notificationBuilder.setContentText(formatTime(hours, minutes, seconds))
        notificationManager.notify(
            TIMER_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun updateOverTimeNotification(elapsedSeconds: Long) {
        notificationBuilder
            .clearActions()
            .addAction(
                NotificationCompat.Action(
                    0,
                    "Cancel",
                    appNotificationManager.cancelPendingIntent(
                        action = TimerState.CANCELLED.name,
                        cancelRequestCode = CANCEL_REQUEST_CODE
                    )
                )
            )
            .setContentTitle("Timer Finished")
            .setContentText("Overtime: ${elapsedSeconds.seconds}")
        notificationManager.notify(TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun playAlertSound(
        isRepeat: Boolean = true,
        soundSpeed: Float = 1.3f,
        useDefaultRingtone: Boolean = true,
        ringtoneResId: Int? = null
    ) {
        val audioUri = if (useDefaultRingtone) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            ringtoneResId?.let {
                Uri.parse("android.resource://${packageName}/$it")
            } ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        }

        mediaPlayer = MediaPlayer.create(this, audioUri).apply {
            isLooping = isRepeat
            playbackParams = playbackParams.setSpeed(soundSpeed)
            start()
        }
    }

    fun stopAlertSound() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.stop()
                it.release()
            }
            mediaPlayer = null
        }
    }


    private fun pauseTimer() {
        if (timerState == TimerState.RUNNING) {
            countDownTimer?.cancel()
            timerState = TimerState.PAUSED
            updateNotificationButtons(isRunning = false)
        }
    }

    private fun stopTimer() {
        countDownTimer?.cancel()
        stopAlertSound()
        remainingTime = Duration.ZERO
        timerState = TimerState.CANCELLED
        stopForegroundService()
    }

    private fun startForegroundService() {
        updateNotificationButtons(isRunning = true)
        startForeground(TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopForegroundService() {
        notificationManager.cancel(TIMER_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    private fun updateNotificationButtons(isRunning: Boolean) {
        notificationBuilder.clearActions()
        notificationBuilder.addAction(
            if (isRunning) NotificationCompat.Action(
                0,
                "Pause",
                appNotificationManager.pausePendingIntent(
                    action = TimerState.PAUSED.name,
                    pauseRequestCode = PAUSE_REQUEST_CODE
                )
            ) else NotificationCompat.Action(
                0,
                "Resume",
                appNotificationManager.resumePendingIntent(
                    action = TimerState.RUNNING.name,
                    resumeRequestCode = RESUME_REQUEST_CODE
                )
            )
        )
        notificationBuilder.addAction(
            NotificationCompat.Action(
                0,
                "Cancel",
                appNotificationManager.cancelPendingIntent(
                    action = TimerState.CANCELLED.name,
                    cancelRequestCode = CANCEL_REQUEST_CODE
                )
            )
        )
        notificationManager.notify(TIMER_NOTIFICATION_ID, notificationBuilder.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        remainingTime = Duration.ZERO
    }

    inner class TimerBinder : Binder(), ServiceManager.ServiceBinder<TimerService> {
        override fun getService(): TimerService {
            return this@TimerService
        }
    }
}