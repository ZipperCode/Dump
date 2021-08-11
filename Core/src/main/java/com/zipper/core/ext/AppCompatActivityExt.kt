package com.zipper.core.ext

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

fun AppCompatActivity.hasPermissionCompat(permission: String) =
    checkSelfPermissionCompat(permission) == PackageManager.PERMISSION_GRANTED

fun AppCompatActivity.checkSelfPermissionCompat(permission: String) =
    ActivityCompat.checkSelfPermission(this, permission)

fun AppCompatActivity.shouldShowRequestPermissionRationaleCompat(permission: String) =
    ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

fun AppCompatActivity.requestPermissionsCompat(
    permissionsArray: Array<String>,
    requestCode: Int
) = ActivityCompat.requestPermissions(this, permissionsArray, requestCode)

fun Fragment.hasPermission(permission: String) =
    ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED

fun Fragment.checkSelfPermissionCompat(permission: String) =
    ContextCompat.checkSelfPermission(requireContext(), permission)

fun Fragment.shouldShowRequestPermissionRationale(permission: String): Boolean{
    return if(Build.VERSION.SDK_INT >= 23){
        requireActivity().shouldShowRequestPermissionRationale(permission)
    }else{
        false
    }
}
