    package ga.brielcastro.test_app_flutter

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.IRegisterCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.builder.ActivatorBuilder
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.api.INeedLoginListener
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {

    private lateinit var channel: MethodChannel
    private lateinit var currentHomeBean: HomeBean;
    private lateinit var token: String;

    var rooms: ArrayList<String> = ArrayList();

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        channel = MethodChannel(
            flutterEngine.dartExecutor.binaryMessenger,
            CHANNEL
        )

        ThingHomeSdk.setDebugMode(true)
        ThingHomeSdk.init(
            application,
            "gf9m4aqr49u9pku889cx",
            "dhdpukgrttxvspdw39qsdkh5wssuct8f")

        channel.setMethodCallHandler { call, result ->
            var argument = call.arguments as Map<String, String>
            var countryCode = argument["country_code"]
            var email = argument["email"]
            var password = argument["password"]
            var code = argument["code"]
            var homeName = argument["home_name"]

            ThingHomeSdk.setOnNeedLoginListener(object : INeedLoginListener {
                override fun onNeedLogin(context: Context?) {
                    result.success("logged")
                }
            })

            // send code
            if (call.method == SEND_CODE) {
                ThingHomeSdk.getUserInstance().getRegisterEmailValidateCode(
                    countryCode,
                    email,
                    object : IResultCallback {
                        override fun onError(code: String?, error: String?) {
                            Toast.makeText(context, "Erro: $code - $error", Toast.LENGTH_LONG)
                                .show()
                        }

                        override fun onSuccess() {
                            Toast.makeText(context, "Codigo enviado", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                )
            }

            // register
            if (call.method == REGISTER) {
                ThingHomeSdk.getUserInstance().registerAccountWithEmail(
                    countryCode,
                    email,
                    password,
                    code,
                    object : IRegisterCallback {
                        override fun onSuccess(user: User?) {
                            Toast.makeText(context, "User: $user", Toast.LENGTH_LONG)
                                .show()
                        }

                        override fun onError(code: String?, error: String?) {
                            Toast.makeText(context, "Erro: $code - $error", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                )
            }

            // login
            if (call.method == AUTHENTICATE) {
                //String countryCode, String email, String passwd, final ILoginCallback callback
                ThingHomeSdk.getUserInstance().loginWithEmail(
                    countryCode,
                    email,
                    password,
                    object : ILoginCallback {
                        override fun onSuccess(user: User?) {
                            user?.let {
                                Toast.makeText(context, "Logged: ${it.username}", Toast.LENGTH_LONG)
                                    .show()
                                result.success(true);
                            }
                        }

                        override fun onError(code: String?, error: String?) {
                            Toast.makeText(context, "code: $code, error: $error", Toast.LENGTH_SHORT)
                                .show()
                            result.success(false);
                        }
                    }
                )
            }

            // create home
            if (call.method == CREATE_HOME) {
                ThingHomeSdk.getHomeManagerInstance().createHome(
                    homeName,
                    0.0,
                    0.0,
                    null,
                    rooms,
                    object : IThingHomeResultCallback {
                        override fun onSuccess(bean: HomeBean?) {
                            bean?.let {
                                currentHomeBean = it
                                Toast.makeText(context, "Home created", Toast.LENGTH_LONG)
                                    .show()
                            }
                        }

                        override fun onError(errorCode: String?, errorMsg: String?) {
                            Toast.makeText(context, "Error home created", Toast.LENGTH_LONG)
                                .show()
                        }
                    }
                )
            }

            // search devices
            if (call.method == SEARCH_DEVICES) {
                ThingHomeSdk.getActivatorInstance().newMultiActivator(
                    ActivatorBuilder()
                        .setSsid("GABRIEL")
                        .setPassword("n4JkVhAcUV")

                )
            }

            // get registration token
            val homeId: Long = currentHomeBean.homeId
            ThingHomeSdk.getActivatorInstance().getActivatorToken(
                homeId,
                object : IThingActivatorGetToken {
                    override fun onSuccess(token: String?) {
                        // TODO
                    }

                    override fun onFailure(errorCode: String?, errorMsg: String?) {
                        // TODO
                    }
                }
            )
        }
    }

    companion object {
        const val CHANNEL = "tuya_integration"
        const val SEND_CODE = "send_code"
        const val REGISTER = "register"
        const val AUTHENTICATE = "authenticate"
        const val SEARCH_DEVICES = "search_devices"
        const val CONNECT_DEVICE = "connect_device"
        const val CREATE_HOME = "create_home"
    }

}
