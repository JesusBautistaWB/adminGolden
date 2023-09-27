package com.intabux.gobussines.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.intabux.gobussines.providers.OrdersProvider;

public class CancelReceiver extends BroadcastReceiver {

    private OrdersProvider mOrderProvider;
    @Override
    public void onReceive(Context context, Intent intent) {

        String idOrder = intent.getExtras().getString("idOrder");
        mOrderProvider = new OrdersProvider();
        mOrderProvider.updateStatus(idOrder,0);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);
    }
}
