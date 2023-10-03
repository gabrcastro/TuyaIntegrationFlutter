import 'dart:ffi';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:test_app_flutter/utils/enums.dart';

class RegisterScreen extends StatefulWidget {
  const RegisterScreen({Key? key}) : super(key: key);

  @override
  State<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen> {
  static const channel = MethodChannel(Constants.CHANNEL);

  final TextEditingController _codeController = TextEditingController();

  bool codeSent = false;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.black,
      body: SafeArea(
        child: SingleChildScrollView(
          child: SizedBox(
            height: MediaQuery.of(context).size.height,
            child: Padding(
              padding: const EdgeInsets.only(left: 24, right: 24, bottom: 64),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Visibility(
                    visible: codeSent,
                    child: Padding(
                      padding: const EdgeInsets.only(bottom: 20),
                      child: TextFormField(
                        controller: _codeController,
                        decoration: InputDecoration(
                          border: OutlineInputBorder(
                            borderRadius: BorderRadius.circular(10),
                          ),
                          fillColor: const Color(0xFFDEDEDE),
                          filled: true,
                          hintText: 'Code',
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: MediaQuery.of(context).size.width,
                    height: 50,
                    child: ElevatedButton(
                      onPressed: () {
                        if ( !codeSent ) {
                          _validateTuya();
                        } else {
                          _registerTuya();
                        }

                        setState(() {
                          codeSent = true;
                        });
                      },
                      style: ButtonStyle(
                        backgroundColor: codeSent == false ? const MaterialStatePropertyAll<Color>(
                            Colors.transparent) : const MaterialStatePropertyAll<Color>(Color(0xFF00E1D4)),
                        shape: MaterialStateProperty.all<
                            RoundedRectangleBorder>(
                          RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(10.0),
                            side: const BorderSide(
                              width: 2,
                              color: Color(0xFF00E1D4),
                            ),
                          ),
                        ),
                      ),
                      child: Text(
                        codeSent == false ? 'Vincular minha conta a Tuya'.toUpperCase() : 'Validar'.toUpperCase(),
                        style: TextStyle(
                          color: codeSent == false ? const Color(0xFF00E1D4) : const Color(0xFF000000),
                          fontSize: 18,
                        ),
                      ),
                    ),
                  ),

                ],
              ),
            ),
          ),
        ),
      ),
    );
  }

  Future<void> _validateTuya() async {
    await channel.invokeMethod(
      Methods.SEND_CODE, <String, String>{
      "country_code": "55",
      "email": "gabriel.castro@houseasy.net"
    });
  }

  Future<void> _registerTuya() async {
    await channel.invokeMethod(Methods.REGISTER, <String, String>{
      "country_code": "55",
      "email": "gabriel.castro@houseasy.net",
      "password": "123123123",
      "code": _codeController.text
    });
  }
}
