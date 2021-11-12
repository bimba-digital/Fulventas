package com.mishasho.tienda.moda.PushNotification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mishasho.tienda.moda.R;
import com.mishasho.tienda.moda.main.MainActivity;
import com.mishasho.tienda.moda.model.PushNotification;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        PushNotification pushNotification = new Gson().fromJson(remoteMessage.getData().get("message"), PushNotification.class);
        showNotofication(pushNotification);

    }

    public void showNotofication(PushNotification pushNotification) {

        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(pushNotification.getTitle())
                .setContentText(pushNotification.getDesc())
                .setSmallIcon(android.R.color.transparent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setSound(alarmSound)
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());

    }
}
