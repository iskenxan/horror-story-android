package samatov.space.spookies.model

import android.os.Handler
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import samatov.space.spookies.view_model.MyApplication
import samatov.space.spookies.view_model.activities.FeedActivity
import samatov.space.spookies.view_model.activities.ViewProfileActivity
import samatov.space.spookies.view_model.activities.my_profile.MyProfileActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        remoteMessage?: return

        val handler = Handler(baseContext.mainLooper)
        val runnable = Runnable {
            var currentActivity = (application as MyApplication).currentActivity
            currentActivity?.let {
                if (it is FeedActivity) {
                    (currentActivity as FeedActivity).updateNotifications()
                }
                if (it is ViewProfileActivity) {
                    (currentActivity as ViewProfileActivity).updateNotifications()
                }
                if (it is MyProfileActivity) {
                    (currentActivity as MyProfileActivity).updateNotifications()
                }
            }
        }
        handler.post(runnable)
    }

    override fun onDeletedMessages() {
        super.onDeletedMessages()
        Log.i("Test", "Test")
    }


    override fun onNewToken(token: String?) {
        MyPreferenceManager.saveString(baseContext, MyPreferenceManager.NOTIFICATION_TOKEN, token)
    }
}
