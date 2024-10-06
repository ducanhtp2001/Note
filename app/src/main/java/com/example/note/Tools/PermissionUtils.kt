package com.example.note.Tools

import com.example.note.R
import com.example.note.Tools.log_helper.LogHelper
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PermissionUtils {
    interface IPermissionsResult {
        fun permissonsPassed()
        fun permissonsNotPassed()
    }

    companion object {
        private var mPermissionsResult: IPermissionsResult? = null


        fun askForPermission(
            activity: Activity, permission: String,
            grantedCallback: ((Int) -> Unit)? = null
        ): Int {
            val result = ContextCompat.checkSelfPermission(activity, permission)
            if (result == PackageManager.PERMISSION_GRANTED) {
                LogHelper.logDebug(this.javaClass, "$permission -> GRANTED")
                grantedCallback?.invoke(result)
            } else {
                LogHelper.logDebug(this.javaClass, "$permission -> NOT GRANTED")
                ActivityCompat.requestPermissions(activity, arrayOf(permission), 100)
            }
            return result
        }

        fun checkPermissionGranted(activity: Activity, permission: String): Boolean {
            val result = ContextCompat.checkSelfPermission(activity, permission)
            return (result == PackageManager.PERMISSION_GRANTED)
        }

        private fun showSystemPermissionsSettingDialog(activity: Activity) {
            MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.common_notice)
                .setMessage(R.string.common_permission_grant)
                .setPositiveButton(R.string.common_ok) { _, _ ->
                    showSystemSetting(activity)
                }
                .setNegativeButton(R.string.common_later) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        fun showSystemSetting(activity: Activity) {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val uri: Uri = Uri.fromParts("package", activity.packageName, null)
            intent.data = uri
            activity.startActivity(intent)
        }

        fun askForNotificationPermission(activity: Activity, showDialogIfDenied: Boolean = false) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val result = askForPermission(activity, Manifest.permission.POST_NOTIFICATIONS)
                if (showDialogIfDenied && result != PackageManager.PERMISSION_GRANTED) {
                    showSystemPermissionsSettingDialog(activity)
                }
            }
        }

        fun checkPermissions(
            context: Activity,
            permissions: Array<String>,
            permissionsResult: IPermissionsResult
        ) {
            this.mPermissionsResult = permissionsResult
            if (Build.VERSION.SDK_INT < 23) {
                permissionsResult.permissonsPassed()
            } else {
                val mPermissionList: MutableList<String> = arrayListOf()
                permissions.forEachIndexed { _, permission ->
                    if (!checkPermissionGranted(context, permission)) {
                        mPermissionList.add(permission)
                    }
                }
                //
                LogHelper.logDebug(this.javaClass, "checkPermissions: ${mPermissionList.size}")
                if (mPermissionList.size > 0) {
                    ActivityCompat.requestPermissions(context, permissions, 100)
                    mPermissionList.forEach {
                        askForPermission(context, it)
                    }
                } else {
                    permissionsResult.permissonsPassed()
                }
            }
        }

    }
}