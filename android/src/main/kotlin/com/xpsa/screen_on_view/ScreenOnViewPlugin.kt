package com.xpsa.screen_on_view
import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.FlutterInjector
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor.DartEntrypoint
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import kotlin.jvm.java

class ScreenOnViewPlugin : FlutterPlugin, MethodCallHandler, ActivityAware {
    private lateinit var context: Context
    private lateinit var channel: MethodChannel
    private var activity: Activity? = null

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "screen_on_view")
        channel.setMethodCallHandler(this)

        context = flutterPluginBinding.applicationContext
    }


    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> handleGetPlatformVersion(result)
            "requestPermission" -> getPermission(result)
            "startService" -> startScreenService(
                result,
                call.argument<String>("entryPointName"),
                AlarmModel.fromMap(call.argument<Map<*,*>>("AlarmModel"))
            )

            "endService" -> endScreenService(result)
            else -> result.notImplemented()
        }
    }

    private fun endScreenService(result: MethodChannel.Result) {
        try {
            val service = Intent(context, ScreenService::class.java)
            context.stopService(service)
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }

    }

    private fun startScreenService(
        result: MethodChannel.Result,
        entryPointName: String?,
        alarmModel: AlarmModel
    ) {
        try {
            println("엔트리 포인트 이름: ${entryPointName.toString()}")
            println("alarmModel: ${alarmModel.toString()}")
            createFlutterEngine(entryPointName)
            val service = Intent(context, ScreenService::class.java).apply{
                putExtra("entryPointName", entryPointName)
                putExtra("alarmModel", alarmModel)
            }
            context.startService(service)
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }

    }

    private fun getPermission(result: MethodChannel.Result) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(context)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:${context.packageName}")
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val nm =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val alreadyGranted =
                    nm.areNotificationsEnabled() ||
                            ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED

                if (!alreadyGranted) {
                    activity?.let {
                        ActivityCompat.requestPermissions(
                            it,
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            1001
                        )
                    } ?: run {
                        val i = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                        context.startActivity(i)
                    }
                }
            }
            result.success("success")
        } catch (e: Exception) {
            result.success(e.toString())
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    private fun handleGetPlatformVersion(result: MethodChannel.Result) {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    fun createFlutterEngine(name: String?): FlutterEngine {
        val LockScreen_flutterEngine = FlutterEngine(context)
        val flutterLoader = FlutterInjector.instance().flutterLoader()
        flutterLoader.startInitialization(context)
        flutterLoader.ensureInitializationComplete(context, null)
        if (!flutterLoader.initialized()) {
            throw AssertionError(
                "DartEntrypoints can only be created once a FlutterEngine is created."
            );
        }
        LockScreen_flutterEngine.dartExecutor.executeDartEntrypoint(
            DartEntrypoint(
                flutterLoader.findAppBundlePath(),
                name ?: "main2"
            )
        )
        FlutterEngineCache.getInstance().put("LockScreenEngine_id", LockScreen_flutterEngine)
        return LockScreen_flutterEngine
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
    }

    override fun onDetachedFromActivity() {
        activity = null
    }

    override fun onDetachedFromActivityForConfigChanges() {
        activity = null
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        activity = binding.activity
    }
}