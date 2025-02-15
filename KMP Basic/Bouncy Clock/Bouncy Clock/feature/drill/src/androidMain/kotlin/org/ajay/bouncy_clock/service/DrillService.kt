package org.ajay.bouncy_clock.service

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.NotificationCompat
import org.ajay.bouncy_clock.domain.ServiceDrillInput
import org.ajay.bouncy_clock.domain.toDrillRecordEntity
import org.ajay.bouncy_clock.ui.drill_detail.DrillState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.ajay.bouncy_clock.DrillRecordDataSource
import org.ajay.bouncy_clock.notification.FlipAppNotificationManager
import org.ajay.bouncy_clock.presentation.Utility.formatTime
import org.ajay.bouncy_clock.presentation.Utility.pad
import org.ajay.bouncy_clock.presentation.Utility.toFormattedClockTimeFromWholeSeconds
import org.ajay.bouncy_clock.presentation.Utility.toTimeInMillis
import org.ajay.bouncy_clock.service.Constants.CANCEL_REQUEST_CODE
import org.ajay.bouncy_clock.service.Constants.DRILL_DETAIL_INPUT_KEY
import org.ajay.bouncy_clock.service.Constants.DRILL_NOTIFICATION_ID
import org.ajay.bouncy_clock.service.Constants.PAUSE_REQUEST_CODE
import org.ajay.bouncy_clock.service.Constants.RESUME_REQUEST_CODE
import org.koin.android.ext.android.inject
import org.koin.core.qualifier.named
import kotlin.getValue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class DrillService : Service() {

    private val notificationManager: NotificationManager by inject()
    private val appNotificationManager: FlipAppNotificationManager by inject(named("DrillNotificationManager"))
    private val notificationBuilder: NotificationCompat.Builder by inject(named("DrillNotificationBuilder"))
    private val recordDataSource: DrillRecordDataSource by inject()

    private val drillBinder = DrillBinder()
    private var countDownTimer: CountDownTimer? = null
    private var remainingTime by mutableStateOf(Duration.ZERO)
    private var mediaPlayer: MediaPlayer? = null // To handle the alert sound

    var currentDrillState by mutableStateOf(DrillState.CANCELLED)
        private set
    var currentServiceDrillInput by mutableStateOf(ServiceDrillInput())
        private set

    private var drillLastStartTime: Long = 0L
    private var activeDrillDuration: Long = 0L

    var startTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time

    val hours: String
        get() = remainingTime.toComponents { hours, _, _, _ -> hours.toInt().pad() }
    val minutes: String
        get() = remainingTime.toComponents { _, minutes, _, _ -> minutes.pad() }
    val seconds: String
        get() = remainingTime.toComponents { _, _, seconds, _ -> seconds.pad() }

    private fun startActiveTracking() {
        if (currentDrillState != DrillState.SHORT_BREAK || currentDrillState != DrillState.SHORT_BREAK_PAUSED) {
            drillLastStartTime = System.currentTimeMillis()
        }
    }

    private fun pauseActiveTracking() {
        if (drillLastStartTime != 0L) {
            activeDrillDuration += System.currentTimeMillis() - drillLastStartTime
            drillLastStartTime = 0L
        }
    }


    override fun onBind(p0: Intent?): IBinder? = drillBinder

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.action?.let {
            when (it) {
                DrillState.DRILL_RUNNING.name -> handleStartCommand(intent)

                DrillState.PAUSED.name -> pauseDrill()

                DrillState.CANCELLED.name -> stopDrill()
            }

        }
        return START_STICKY
    }

    private fun handleStartCommand(intent: Intent) {
        if (currentDrillState != DrillState.DRILL_RUNNING) {
            val drillInput =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    intent.getParcelableExtra(
                        DRILL_DETAIL_INPUT_KEY,
                        ServiceDrillInput::class.java
                    )
                } else {
                    @Suppress("DEPRECATION")
                    intent.getParcelableExtra<ServiceDrillInput>(DRILL_DETAIL_INPUT_KEY)
                } ?: currentServiceDrillInput

            currentServiceDrillInput = drillInput
            if (currentDrillState == DrillState.CANCELLED || currentDrillState == DrillState.COMPLETED) {
                CoroutineScope(Dispatchers.IO).launch {
                    startTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time
                }
            }
            startDrillForegroundService()
            startDrill(currentServiceDrillInput) { hours, minutes, seconds ->
                updateNotification(
                    title = currentServiceDrillInput.title,
                    hours = hours,
                    minutes = minutes,
                    seconds = seconds
                )
            }
        }
    }

    private fun startDrill(
        drillInput: ServiceDrillInput,
        onTick: (String, String, String) -> Unit
    ) {
        countDownTimer?.cancel()
        startActiveTracking()

        val adjustedDuration = when (currentDrillState) {
            DrillState.SHORT_BREAK -> drillInput.breakDuration.toTimeInMillis()
            DrillState.PAUSED, DrillState.SHORT_BREAK_PAUSED -> remainingTime.inWholeMilliseconds
            else -> drillInput.duration.toTimeInMillis()
        }

        countDownTimer = object : CountDownTimer(adjustedDuration, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingTime = millisUntilFinished.milliseconds
                onTick(hours, minutes, seconds)
            }

            override fun onFinish() {
                handleDrillCompletion()
            }

        }.start()
        currentDrillState = if (currentDrillState == DrillState.SHORT_BREAK_PAUSED) {
            DrillState.SHORT_BREAK
        } else {
            DrillState.DRILL_RUNNING
        }

    }


    private fun handleDrillCompletion() {
        when (currentDrillState) {
            DrillState.DRILL_RUNNING -> {
                when {
                    currentServiceDrillInput.currentLap < currentServiceDrillInput.totalLaps && currentServiceDrillInput.breakDuration.toTimeInMillis() == 0L -> {
                        pauseActiveTracking()
                        currentServiceDrillInput =
                            currentServiceDrillInput.copy(currentLap = currentServiceDrillInput.currentLap + 1)
                        playAlertSound(isRepeat = false, soundSpeed = 1f)
                        startDrill(drillInput = currentServiceDrillInput) { hours, minutes, seconds ->
                            updateNotification(
                                title = currentServiceDrillInput.title,
                                hours = hours,
                                minutes = minutes,
                                seconds = seconds
                            )
                        }
                    }

                    currentServiceDrillInput.currentLap < currentServiceDrillInput.totalLaps -> {
                        currentDrillState = DrillState.SHORT_BREAK
                        pauseActiveTracking()
                        playAlertSound()
                        startDrill(drillInput = currentServiceDrillInput) { hours, minutes, seconds ->
                            updateNotification(
                                title = currentServiceDrillInput.title,
                                hours = hours,
                                minutes = minutes,
                                seconds = seconds
                            )
                        }
                        currentDrillState = DrillState.SHORT_BREAK
                    }

                    else -> {
                        countDownTimer?.cancel()
                        remainingTime = Duration.ZERO
                        currentDrillState = DrillState.COMPLETED
                        playAlertSound()
                        startOvertimeCountdown()
                    }
                }

            }

            DrillState.SHORT_BREAK -> {
                currentDrillState = DrillState.DRILL_RUNNING
                currentServiceDrillInput =
                    currentServiceDrillInput.copy(currentLap = currentServiceDrillInput.currentLap + 1)
                playAlertSound(isRepeat = false, soundSpeed = 1f)
                startDrill(drillInput = currentServiceDrillInput) { hours, minutes, seconds ->
                    updateNotification(
                        title = currentServiceDrillInput.title,
                        hours = hours,
                        minutes = minutes,
                        seconds = seconds
                    )
                }
            }

            else -> {
                stopDrill()
            }
        }
    }

    private fun startOvertimeCountdown() {
        countDownTimer = object : CountDownTimer(3600000, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                val elapsedSeconds = (3600000 - millisUntilFinished).milliseconds.inWholeSeconds
                remainingTime = elapsedSeconds.seconds
                updateOverTimeNotification(elapsedSeconds)
            }

            override fun onFinish() {
                stopDrill() //automatically stops
            }
        }.start()
    }

    private fun updateOverTimeNotification(elapsedSeconds: Long) {
        notificationBuilder
            .clearActions()
            .addAction(
                NotificationCompat.Action(
                    0,
                    "Cancel",
                    appNotificationManager.cancelPendingIntent(
                        action = DrillState.CANCELLED.name,
                        cancelRequestCode = CANCEL_REQUEST_CODE
                    )
                )
            )
            .setContentTitle("Timer Finished")
            .setContentText("Overtime: ${elapsedSeconds.seconds}")
        notificationManager.notify(DRILL_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun pauseDrill() {
        pauseActiveTracking()
        countDownTimer?.cancel()
        currentDrillState =
            if (currentDrillState == DrillState.DRILL_RUNNING) DrillState.PAUSED else DrillState.SHORT_BREAK_PAUSED
        updateNotificationButtons(isRunning = false)
    }

    private fun stopDrill(drillState: DrillState = DrillState.CANCELLED) {
        pauseActiveTracking()
        val formattedActiveDuration =
            activeDrillDuration.milliseconds.inWholeSeconds.toFormattedClockTimeFromWholeSeconds()
        val formattedActiveDurationInSeconds =
            activeDrillDuration.milliseconds.inWholeSeconds

        Log.d("formatted active duration", formattedActiveDuration)
        Log.d("formatted active duration in seconds", formattedActiveDurationInSeconds.toString())


        val drillRecord = currentServiceDrillInput.toDrillRecordEntity().copy(
            startTime = startTime,
            endTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).time,
            activeDurationInSeconds = formattedActiveDurationInSeconds
        )
        Log.d("time spent in whole seconds:", formattedActiveDuration)
        CoroutineScope(Dispatchers.IO).launch {
            recordDataSource.insertRecordIfNotExists(drillRecord)
        }
        countDownTimer?.cancel()
        stopAlertSound()
        resetStates(drillState)
        stopDrillForegroundService()
    }

    private fun resetStates(drillState: DrillState = DrillState.CANCELLED) {
        activeDrillDuration = 0L
        drillLastStartTime = 0L
        remainingTime = Duration.ZERO
        currentDrillState = drillState
        currentServiceDrillInput = ServiceDrillInput()
    }

    private fun startDrillForegroundService() {
        updateNotificationButtons(isRunning = true)
        startForeground(DRILL_NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun stopDrillForegroundService() {
        notificationManager.cancel(DRILL_NOTIFICATION_ID)
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
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

    private fun updateNotification(title: String, hours: String, minutes: String, seconds: String) {
        notificationBuilder.setContentTitle(title)
            .setContentText(formatTime(hours, minutes, seconds))
        notificationManager.notify(
            DRILL_NOTIFICATION_ID,
            notificationBuilder.build()
        )
    }

    private fun updateNotificationButtons(isRunning: Boolean) {
        notificationBuilder.clearActions()
        notificationBuilder.addAction(
            if (isRunning) NotificationCompat.Action(
                0,
                "Pause",
                appNotificationManager.pausePendingIntent(
                    action = DrillState.PAUSED.name,
                    pauseRequestCode = PAUSE_REQUEST_CODE
                )
            ) else NotificationCompat.Action(
                0,
                "Resume",
                appNotificationManager.resumePendingIntent(
                    action = DrillState.DRILL_RUNNING.name,
                    resumeRequestCode = RESUME_REQUEST_CODE
                )
            )
        )
        notificationBuilder.addAction(
            NotificationCompat.Action(
                0,
                "Cancel",
                appNotificationManager.cancelPendingIntent(
                    action = DrillState.CANCELLED.name,
                    cancelRequestCode = CANCEL_REQUEST_CODE
                )
            )
        )
        notificationManager.notify(DRILL_NOTIFICATION_ID, notificationBuilder.build())
    }


    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
        remainingTime = Duration.ZERO
    }


    inner class DrillBinder : Binder(), ServiceManager.ServiceBinder<DrillService> {
        override fun getService(): DrillService {
            return this@DrillService
        }
    }
}