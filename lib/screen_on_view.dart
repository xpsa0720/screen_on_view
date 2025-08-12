import 'package:screen_on_view/screen_on_view_method_channel.dart';
import 'package:screen_on_view/screen_on_view_platform_interface.dart';

import 'class/alarm_model.dart';
import 'class/status_class.dart';

class ScreenOnView {
  MethodChannelScreenOnView methodChannel;

  /// Defines the entry point name.
  ///
  /// The `main.dart` file must contain a function with the same name as [entryPointName],
  /// and it must be annotated with `@pragma('vm:entry-point')`.
  final String entryPointName;

  /// Indicates whether the service is currently running.
  bool isRunning = false;
  ScreenOnView({
    required this.entryPointName,
    MethodChannelScreenOnView? MethodChannel,
  }) : methodChannel = MethodChannel ?? MethodChannelScreenOnView();

  Future<String?> getPlatformVersion() {
    return ScreenOnViewPlatform.instance.getPlatformVersion();
  }

  /// Requests the necessary permissions.
  ///
  /// This method requests both the notification permission
  /// and the "draw over other apps" permission.
  Future<StatusBase> requestPermission() async {
    final result = await methodChannel.requestPermission_methodChannel(
      entryPointName,
    );
    return StatusBase.fromMap(result.toString());
  }

  /// Starts the service.
  ///
  /// You must call [requestPermission] before invoking this method.
  Future<StatusBase> startService({required AlarmModel model}) async {
    if (isRunning) {
      return Error(errorMessage: "Service already running.");
    }
    final result = await methodChannel.startService_methodChannel(
      entryPointName: entryPointName,
      model: model,
    );
    isRunning = true;
    return StatusBase.fromMap(result.toString());
  }

  /// Stops the service.
  Future<StatusBase> endService() async {
    if (!isRunning) {
      return Error(errorMessage: "Service already Stop.");
    }
    final result = await methodChannel.endService_methodChannel(entryPointName);
    return StatusBase.fromMap(result.toString());
  }
}
