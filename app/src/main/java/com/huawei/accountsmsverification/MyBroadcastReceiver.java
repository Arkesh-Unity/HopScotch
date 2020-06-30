package com.huawei.accountsmsverification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.hms.common.api.CommonStatusCodes;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.sms.common.ReadSmsConstant;

/**
 * Created by guna on 26-06-2020.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Status status = bundle.getParcelable(ReadSmsConstant.EXTRA_STATUS);
            assert status != null;
            if (status.getStatusCode() == CommonStatusCodes.TIMEOUT) {
                // Service has timed out and no SMS message that meets the requirement is read. Service ended.
               // doSomethingWhenTimeOut();
            } else if (status.getStatusCode() == CommonStatusCodes.SUCCESS) {
                if (bundle.containsKey(ReadSmsConstant.EXTRA_SMS_MESSAGE)) {
                    // An SMS message that meets the requirement is read. Service ended.
                    //doSomethingWhenGetMessage(bundle.getString(ReadSmsConstant.EXTRA_SMS_MESSAGE));

                    if (bundle.containsKey(ReadSmsConstant.EXTRA_SMS_MESSAGE)) {

                        // An SMS message that meets the requirement is read. Service ended.
                        //doSomethingWhenGetMessage(bundle.getString(ReadSmsConstant.EXTRA_SMS_MESSAGE))



                            Intent local = new Intent();
                            local.setAction("service.to.activity.transfer");
                            local.putExtra("sms", bundle.getString(ReadSmsConstant.EXTRA_SMS_MESSAGE));
                            context.sendBroadcast(local);
                        }
                    }
                }
            }
        }
    }


