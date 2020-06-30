package com.huawei.accountsmsverification;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnCompleteListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.sms.ReadSmsManager;

import static com.huawei.hms.support.sms.common.ReadSmsConstant.READ_SMS_BROADCAST_ACTION;

public class MainActivity extends AppCompatActivity {

    TextView txt_otp;
    EditText edt_mobile;
    Button generate_Otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_otp = (TextView)findViewById(R.id.textView);
        edt_mobile = (EditText)findViewById(R.id.appCompatEditText);
        generate_Otp = (Button)findViewById(R.id.appCompatButton);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED)
        {

        }
        else
        {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

        generate_Otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                initSmsManager();
                registerSmsBroadcastReceiver();
                sendSms();
                registerOtpBroadcastReceiver();

            }
        });

    }


    private void registerSmsBroadcastReceiver() {

        IntentFilter intentFilter =new IntentFilter(READ_SMS_BROADCAST_ACTION);
        registerReceiver(new MyBroadcastReceiver(), intentFilter);


    }

    private void sendSms()
    {
// 41:10:FC:5D:8F:30:45:37:09:3C:5A:0D:5D:71:C9:AC:B3:F8:FD:79:E2:27:F8:10:D4:2F:1D:E4:12:2A:BA:31

        String  hashValue = new HashManager().getHashValue(getApplicationContext());

        String otp = "999999";


        if (edt_mobile.getText().toString().length()>0) {

            //prefix_flag short message verification code is XXXXXX hash_value
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(
                    edt_mobile.getText().toString(),
                    null,
                    "[#] Your verification code is " + otp + " " + hashValue,
                    null,
                    null
            );
        } else {
            Toast.makeText(this, "Please enter the phone number..", Toast.LENGTH_LONG).show();
        }



//        val otp = Random.nextInt(
//                100000,
//                999999
//        ).toString()
//
//        if (!mainActivityViewModel.mobileNumber.value.isNullOrEmpty()) {
//
//            val smsManager = SmsManager.getDefault()
//            smsManager.sendTextMessage(
//                    mainActivityViewModel.mobileNumber.value,
//                    null,
//                    "[#] Your verification code is $otp $hashValue",
//                    null,
//                    null
//            )
//        } else {
//            Toast.makeText(this, "Please enter the phone number..", Toast.LENGTH_LONG).show()
//        }
    }

    public void initSmsManager()
    {
        Task<Void> task = ReadSmsManager.start(MainActivity.this);
        task.addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                if (task.isSuccessful()) {

                }
            }
        });

    }

    private void registerOtpBroadcastReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("service.to.activity.transfer");
        BroadcastReceiver updateUIReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                try {
                    Bundle bundle = intent.getExtras();
                    if (bundle != null) {

                        txt_otp.setText(intent.getStringExtra("sms"));

                    } else {
                        Toast.makeText(context, "Bundle null", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                }


            }
        };
        registerReceiver(updateUIReceiver, filter);

    }


}
