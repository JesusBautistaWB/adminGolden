package com.intabux.gobussines.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.intabux.gobussines.activities.OrderDetailActivity;
import com.intabux.gobussines.providers.OrdersProvider;

public class AcceptReceiver extends BroadcastReceiver {

    private OrdersProvider mOrderProvider;

    @Override
    public void onReceive(Context context, Intent intent) {

        mOrderProvider = new OrdersProvider();

        String orderString = intent.getExtras().getString("order");
        String idOrder = intent.getExtras().getString("idOrder");

        mOrderProvider.updateStatus(idOrder, 2);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(2);

        Intent intent1 = new Intent(context, OrderDetailActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent1.setAction(Intent.ACTION_RUN);
        intent1.putExtra("id",idOrder);
        intent1.putExtra("userName","");
        intent1.putExtra("order",orderString);

        context.startActivity(intent1);
    }
}
