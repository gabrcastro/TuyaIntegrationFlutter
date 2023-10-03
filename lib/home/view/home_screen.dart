import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:test_app_flutter/utils/enums.dart';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  State<HomeScreen> createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  static const channel = MethodChannel(Constants.CHANNEL);

  @override
  Widget build(BuildContext context) {
    return const Scaffold(
      backgroundColor: Colors.deepOrange,
      body: Center(
        child: Column(
          children: [
            Text(
              "Home Screen"
            ),
          ],
        ),
      ),
    );
  }

  _searchDevices() async {
    bool result = await channel.invokeMethod(Methods.SEARCH_DEVICES, <String, String>{});
    return result;
  }
}
