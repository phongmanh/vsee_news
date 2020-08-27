package com.manhnguyen.codebase.system

import android.annotation.SuppressLint
import android.app.*
import android.app.Notification.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.manhnguyen.codebase.ApplicationController
import com.manhnguyen.codebase.R
import com.manhnguyen.codebase.common.SchedulerProvider
import com.manhnguyen.codebase.presentation.login.LoginActivity
import io.reactivex.Flowable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmBuilder @Inject constructor() : ViewModel() {

    private val startNextAlarm = MutableLiveData<Boolean>()
    private val isStartChecking = MutableLiveData<Boolean>()
    private val isOffTime = MutableLiveData<Boolean>()
    private val reloadUserOffTime = MutableLiveData<Boolean>()
    private val nextDayEventChecking = MutableLiveData<Boolean>()

    private var startOrFinishSubscriber: Subscription? = null
    private var alertSubscriber: Subscription? = null
    private var alertNextDaySubscriber: Subscription? = null

    private val CHANNEL_ID = "before_channel"
    private val NOTIFICATION_ID = 77


    @Inject
    lateinit var provider: SchedulerProvider

    fun getStartNextAlarmAlertState(): LiveData<Boolean> {
        return startNextAlarm
    }

    fun getUserOffTimeChecking(): LiveData<Boolean> {
        return isStartChecking
    }


    fun isUserOffTime(): LiveData<Boolean> {
        return isOffTime
    }

    fun setIsUserOffTime(value: Boolean) {
        isOffTime.postValue(value)
    }

    fun setIsUserOffTimeChecking(value: Boolean) {
        isStartChecking.postValue(true)
    }

    fun setStartNextAlarmState(value: Boolean) {
        startNextAlarm.postValue(value)
    }

    fun getReloadUserOffTimeStatus(): LiveData<Boolean> {
        return reloadUserOffTime
    }

    fun setReloadUserOffTimeStatus() {
        reloadUserOffTime.postValue(true)
    }

    fun getNextDayEventChecking(): LiveData<Boolean> {
        return nextDayEventChecking
    }

    @SuppressLint("CheckResult")
    fun createAlarm(context: Context, delay: Long, dateStart: String, finishing: Boolean) {
        try {

            Flowable.timer(delay, TimeUnit.SECONDS)
                .observeOn(provider.mainThread)
                .subscribeOn(provider.runOnUiThread)
                .subscribeWith(object : Subscriber<Long> {
                    override fun onComplete() {
                        setStartNextAlarmState(true)
                    }

                    override fun onSubscribe(s: Subscription?) {
                        alertSubscriber = s
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(l: Long) {
                        beforeOffTime(
                            context,
                            dateStart,
                            finishing
                        )

                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })

            cancelNextDayAlarm()

        } catch (it: Exception) {
            it.printStackTrace()
        }
    }


    @SuppressLint("CheckResult")
    fun createAlarmOffTime(startOff: Boolean, delay: Long) {
        try {

            Flowable.timer(delay, TimeUnit.SECONDS)
                .observeOn(provider.mainThread)
                .subscribeOn(provider.runOnUiThread)
                .subscribeWith(object : Subscriber<Long> {
                    override fun onComplete() {
                        setIsUserOffTimeChecking(true)
                    }

                    override fun onSubscribe(s: Subscription?) {
                        startOrFinishSubscriber = s
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(t: Long?) {

                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })


        } catch (it: Exception) {
            it.printStackTrace()
        }
    }

    @SuppressLint("CheckResult")
    fun createAlarmForNextDay(delay: Long) {
        try {
            alertNextDaySubscriber?.cancel()

            Flowable.timer(delay, TimeUnit.MINUTES)
                .observeOn(provider.mainThread)
                .subscribeOn(provider.runOnUiThread)
                .subscribeWith(object : Subscriber<Long> {
                    override fun onComplete() {
                        nextDayEventChecking.postValue(true)
                    }

                    override fun onSubscribe(s: Subscription?) {
                        alertNextDaySubscriber = s
                        s?.request(Long.MAX_VALUE)
                    }

                    override fun onNext(t: Long?) {

                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }
                })


        } catch (it: Exception) {
            it.printStackTrace()
        }
    }

    fun cancelAlertAlarm() {
        try {
            alertSubscriber?.cancel()

        } catch (it: Exception) {
            it.printStackTrace()
        }
    }

    fun cancelExeAlarm() {
        try {
            startOrFinishSubscriber?.cancel()

        } catch (it: Exception) {
            it.printStackTrace()
        }
    }

    fun cancelNextDayAlarm() {
        alertNextDaySubscriber?.cancel()
    }

    private fun turnOnScreen() {
        try {
            val pm = ApplicationController.appComponent.getContext()
                .getSystemService(Context.POWER_SERVICE) as PowerManager
            val isScreenOn = pm.isInteractive
            if (!isScreenOn) {
                val wl = pm.newWakeLock(
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.ON_AFTER_RELEASE,
                    "MyLock:"
                );
                wl.acquire(10000);
                val wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock:");
                wl_cpu.acquire(10000);
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun beforeOffTime(context: Context, time: String, finishing: Boolean) {
        try {

            val mNotificationManager =
                context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            val notification: Notification
            val content: String = if (!finishing)
                "If you are outside site map, your tracking time will be off in the next 30 minutes at ${time}. " +
                        "If you want to keep tracking, please update your tracking off time"
            else
                "If you are outside site map, your tracking time will start in the next 30 minutes at ${time}. " +
                        "If you want to stop tracking, please update your tracking off time"


            val intentActivity = Intent(context, LoginActivity::class.java)
            intentActivity.flags =
                Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT

            val pIntent = PendingIntent.getActivity(
                context,
                System.currentTimeMillis().toInt(),
                intentActivity,
                0
            )

            if (Build.VERSION.SDK_INT >= 26) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Off Time Notify",
                    NotificationManager.IMPORTANCE_HIGH
                )
                channel.setShowBadge(false)
                channel.lockscreenVisibility = VISIBILITY_PUBLIC
                channel.enableVibration(true)
                mNotificationManager.createNotificationChannel(channel)
                notification = NotificationCompat.Builder(context, CHANNEL_ID)
                    .setContentTitle(if (finishing) "Tracking time start" else "Tracking time off")
                    .setSmallIcon(R.drawable.logo)
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(content)
                    )
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE) //Important for heads-up notification
                    .setAutoCancel(true)
                    .setContentIntent(pIntent)
                    .setContentText(content)
                    .build()

            } else {
                notification = NotificationCompat.Builder(context)
                    .setContentTitle("Notify")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setChannelId(CHANNEL_ID)
                    /*.setContentText(content)*/
                    .setAutoCancel(true)
                    .setContentIntent(pIntent)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setDefaults(DEFAULT_SOUND or DEFAULT_VIBRATE) //Important for heads-up notification
                    .setStyle(
                        NotificationCompat.BigTextStyle()
                            .bigText(if (finishing) "Finishing Off Time" else "Starting Off Time")
                    )
                    .build()
            }
            mNotificationManager.notify(NOTIFICATION_ID, notification)
           /* turnOnScreen()*/
            /*if (mainViewModel.isApplicationInForeground.get()) {
                *//*Auto close if app in foreground*//*
                val handler = Handler()
                handler.postDelayed(Runnable {
                    mNotificationManager.cancel(NOTIFICATION_ID)
                }, 5000)
            }*/
        } catch (it: Exception) {
            it.printStackTrace()
        }
    }

}



