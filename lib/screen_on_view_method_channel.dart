import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:screen_on_view/class/alarm_model.dart';
import 'package:screen_on_view/screen_on_view_platform_interface.dart';

class MethodChannelScreenOnView extends ScreenOnViewPlatform {
  @visibleForTesting
  final methodChannel = const MethodChannel('screen_on_flutter');

  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }

  Future<String> startService_methodChannel({
    required String entryPointName,
    required AlarmModel model,
  }) async {
    final result = await methodChannel.invokeMethod<String>(
      "startService",
      <String, dynamic>{
        "entryPointName": entryPointName,
        "AlarmModel": model.toJson(),
      },
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }

  Future<String> endService_methodChannel(String entryPointName) async {
    final result = await methodChannel.invokeMethod<String>(
      "endService",
      <String, dynamic>{},
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }

  Future<String> requestPermission_methodChannel(String entryPointName) async {
    final result = await methodChannel.invokeMethod<String>(
      "requestPermission",
      <String, dynamic>{},
    );
    if (result == null) return "return null wrong!!";
    return result.toString();
  }
}
