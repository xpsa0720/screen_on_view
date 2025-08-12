import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:screen_on_view/screen_on_view_method_channel.dart';

import 'class/alarm_model.dart';

abstract class ScreenOnViewPlatform extends PlatformInterface {
  ScreenOnViewPlatform() : super(token: _token);

  static final Object _token = Object();

  static ScreenOnViewPlatform _instance = MethodChannelScreenOnView();

  static ScreenOnViewPlatform get instance => _instance;

  static set instance(ScreenOnViewPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<void> startService({required AlarmModel model}) {
    throw UnimplementedError('startService() has not been implemented.');
  }

  Future<void> endService() {
    throw UnimplementedError('endService() has not been implemented.');
  }
}
