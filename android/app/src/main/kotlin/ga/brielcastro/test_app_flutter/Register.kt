package ga.brielcastro.test_app_flutter

import android.content.Context
import android.widget.Toast
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.IRegisterCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.INeedLoginListener
import com.thingclips.smart.sdk.api.IResultCallback
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class Register(
    var channel: MethodChannel
) : FlutterActivity() {

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel.setMethodCallHandler { call, result ->
            var argument = call.arguments as Map<String, String>
            var countryCode = argument["country_code"]
            var email = argument["email"]
            var password = argument["password"]
            var code = argument["code"]

            ThingHomeSdk.setOnNeedLoginListener(object : INeedLoginListener {
                override fun onNeedLogin(context: Context?) {
                    result.success("logged")
                }
            })


        }
    }
}