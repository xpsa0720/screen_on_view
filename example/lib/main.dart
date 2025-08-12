import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:screen_on_view/class/alarm_model.dart';
import 'package:screen_on_view/screen_on_view.dart';

/// Main entry point when the app launches normally
void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Specify the entry point to launch when the screen turns on
  final service = ScreenOnView(entryPointName: "main2");

  // Request the necessary permission
  await service.requestPermission();

  // Start the background service
  await service.startService(
    model: AlarmModel(
      title: "Screen_On_View",
      content: "Start foreground service",
    ),
  );

  // Launch the main app
  runApp(
    MaterialApp(
      debugShowCheckedModeBanner: false,
      home: MainApp(service: service),
    ),
  );
}

/// Entry point launched when the screen turns on
@pragma('vm:entry-point')
void main2() {
  runApp(MaterialApp(debugShowCheckedModeBanner: false, home: LockScreen()));
}

/// The main app screen
class MainApp extends StatelessWidget {
  final ScreenOnView service;
  const MainApp({super.key, required this.service});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Screen On View Example')),
      body: Center(
        child: ElevatedButton(
          onPressed: () => service.endService(),
          child: const Text("Stop Service"),
        ),
      ),
    );
  }
}

/// The screen shown when the device screen turns on
class LockScreen extends StatelessWidget {
  const LockScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            const Icon(Icons.wb_sunny, color: Colors.orange, size: 80),
            const SizedBox(height: 20),
            const Text(
              'Screen On Detected!',
              style: TextStyle(fontSize: 24, color: Colors.black),
            ),
            const SizedBox(height: 40),
            ElevatedButton(
              onPressed: () => SystemNavigator.pop(),
              child: const Text('Close'),
            ),
          ],
        ),
      ),
    );
  }
}
