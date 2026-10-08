package com.abw3laa.callrecorder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import android.content.pm.PackageManager
import com.abw3laa.callrecorder.permissions.PermissionPolicy

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var permissionsReady by remember { mutableStateOf(hasRequiredPermissions()) }

            val launcher = rememberLauncherForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) {
                permissionsReady = hasRequiredPermissions()
            }

            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Call Recorder",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = if (permissionsReady) {
                                "الصلاحيات الأساسية متاحة."
                            } else {
                                "قبل اختبار التسجيل، يجب منح صلاحيات حالة المكالمات والميكروفون."
                            }
                        )

                        if (!permissionsReady) {
                            Button(
                                onClick = {
                                    launcher.launch(PermissionPolicy.requiredAtRuntime.toTypedArray())
                                }
                            ) {
                                Text("منح الصلاحيات")
                            }
                        } else {
                            Text("يمكن الانتقال الآن إلى فحص قدرات الجهاز.")
                        }
                    }
                }
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean =
        PermissionPolicy.requiredAtRuntime.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
}
