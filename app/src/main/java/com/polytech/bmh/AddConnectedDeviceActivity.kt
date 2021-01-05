package com.polytech.bmh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.e
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.gson.Gson
import com.polytech.bmh.data.model.ConnectedDeviceAddBody
import com.polytech.bmh.data.model.SignUpBody
import com.polytech.bmh.service.*
import com.polytech.bmh.ui.login.LoginActivity
import com.squareup.moshi.Json
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject

class AddConnectedDeviceActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_connected_device)

        val connectedDeviceName = findViewById<EditText>(R.id.editTextObjectName)
        val connectedDeviceDescription = findViewById<EditText>(R.id.editTextObjectDescription)
        val connectedDeviceRouter = findViewById<EditText>(R.id.editTextObjectRouter)

        val backArrow = findViewById<ImageView>(R.id.imageViewBackArrow)


        backArrow.setOnClickListener {
            val ChoiceConnectedDeviceActivityIntent = Intent(this, ChoiceConnectedDeviceActivity::class.java)
            startActivity(ChoiceConnectedDeviceActivityIntent)
        }

        val btAddConnectedDevice = findViewById<Button>(R.id.buttonCreateNewConnectedDevice)

        btAddConnectedDevice.setOnClickListener {

            val connectedDeviceNameValue = connectedDeviceName.text.toString()
            val connectedDeviceDescriptionValue = connectedDeviceDescription.text.toString()
            val connectedDeviceRouterValue = connectedDeviceRouter.text.toString()

            if(connectedDeviceNameValue.isEmpty()) {
                connectedDeviceName.error = "Le nom de l'objet connecté est requis !"
                connectedDeviceName.requestFocus()
                val myToast = Toast.makeText(this, "Le nom de l'objet connecté est requis !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            if(connectedDeviceDescriptionValue.isEmpty()) {
                connectedDeviceDescription.error = "La description de l'objet connecté est requise !"
                connectedDeviceDescription.requestFocus()
                val myToast = Toast.makeText(this, "La description de l'objet connecté est requise !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            if(connectedDeviceRouterValue.isEmpty()) {
                connectedDeviceRouter.error = "La valeur du routeur de l'objet connecté est requis !"
                connectedDeviceRouter.requestFocus()
                val myToast = Toast.makeText(this, "La valeur du routeur de l'objet connecté est requis !", Toast.LENGTH_SHORT)
                myToast.show()
                return@setOnClickListener
            }

            addObject(connectedDeviceNameValue, connectedDeviceDescriptionValue, connectedDeviceRouterValue)

        }
    }

    private fun addObject(name: String, description: String, router: String) {
        val service = RetrofitInstance.getRetrofitInstance().create(AddConnectedDevice::class.java)
        val addConnectedDeviceRequest = service.addConnectedDevice(ConnectedDeviceAddBody(name, description, router))

        addConnectedDeviceRequest.enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 201) {
                    Toast.makeText(this@AddConnectedDeviceActivity,
                        "L'object connecté $name a été créé avec succès", Toast.LENGTH_SHORT)
                        .show()

                    val ChoiceConnectedDeviceActivityIntent = Intent(this@AddConnectedDeviceActivity, ChoiceConnectedDeviceActivity::class.java)
                    startActivity(ChoiceConnectedDeviceActivityIntent)

                } else {
                    val errorMessage = Gson().fromJson(response.body()?.string().toString(), MessageError::class.java).message.message
                    Toast.makeText(
                        this@AddConnectedDeviceActivity,
                        "L'objet connecté ne peut pas être créé : $errorMessage",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(
                    this@AddConnectedDeviceActivity,
                    t.message,
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }
}

data class MessageError(
    val message: Message
)

data class Message(
    val message: String
)