package com.app.smspermission


import android.Manifest
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private val SMS_PERMISSION_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.read_sms_button)
            .setOnClickListener {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_SMS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.READ_SMS),
                        SMS_PERMISSION_CODE
                    )
                } else {
                    // Permission is already granted, you can read SMS here
                    readSMS()
                }
            }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            SMS_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show()
                    readSMS()
                } else {
                    Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun readSMS() {
        // SMS Envoyés
        val smsUri: Uri = Uri.parse("content://sms/sent")
        //SMS Reçus
        //val smsUri: Uri = Uri.parse("content://sms/inbox")
        val cursor: Cursor? = contentResolver.query(smsUri, null, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getString(cursor.getColumnIndexOrThrow("_id"))
                    val address = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                    val body = cursor.getString(cursor.getColumnIndexOrThrow("body"))

                    Toast.makeText(this, "SMS from $address: $body", Toast.LENGTH_SHORT).show()
                    findViewById<TextView>(R.id.read_sms_view).setText("Num : $address, SMS : $body")

                } while (cursor.moveToNext())
            } else {
                // Cursor is empty
                Toast.makeText(this, "No SMS messages found", Toast.LENGTH_SHORT).show()
            }
            cursor.close()
        } else {
            // Cursor is null
            Toast.makeText(this, "Failed to retrieve SMS messages", Toast.LENGTH_SHORT).show()
        }
    }


}
