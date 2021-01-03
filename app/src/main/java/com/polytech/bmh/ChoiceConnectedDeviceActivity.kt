package com.polytech.bmh

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Log.ERROR
import android.util.Log.INFO
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.polytech.bmh.service.ConnectedDeviceProperty
import com.polytech.bmh.service.ConnectedDevicesProperties
import com.polytech.bmh.service.RetrofitInstance
import com.polytech.bmh.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ChoiceConnectedDeviceActivity : AppCompatActivity() {

    private val URL = "http://10.0.2.2:8081"


    private var weatherData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_connected_device)
        weatherData = findViewById(R.id.textView7)
        findViewById<View>(R.id.creation_compte4).setOnClickListener { getCurrentData() }


        val backArrow = findViewById<ImageView>(R.id.imageView)

        backArrow.setOnClickListener {
            val LoginActivityIntent = Intent(this, LoginActivity::class.java)
            startActivity(LoginActivityIntent)
        }

        val btAdd = findViewById<FloatingActionButton>(R.id.floatingActionButton3)

        btAdd.setOnClickListener {
            val AddConnectedDeviceActivityIntent = Intent(this, AddConnectedDeviceActivity::class.java)
            startActivity(AddConnectedDeviceActivityIntent)
        }
    }

        internal fun getCurrentData() {

            val service = RetrofitInstance.getRetrofitInstance().create(ConnectedDevicesProperties::class.java)

            val connectedDevicesRequest = service.getConnectedDevices()

            println(connectedDevicesRequest)

            connectedDevicesRequest.enqueue(object : Callback<List<ConnectedDeviceProperty>> {
                override fun onResponse(
                    call: Call<List<ConnectedDeviceProperty>>,
                    response: Response<List<ConnectedDeviceProperty>>
                ) {
                    val allCourse = response.body()
                    if (allCourse != null) {
                        Log.d("bla", "HERE is ALL COURSES FROM HEROKU SERVER:")

                        //info("HERE is ALL COURSES FROM HEROKU SERVER:")
                        for (c in allCourse)
                            Log.d("blo", " one course : ${c.name} : ${c.router} ")

                        //info(" one course : ${c.name} : ${c.router} ")
                    }
                }

                override fun onFailure(call: Call<List<ConnectedDeviceProperty>>, t: Throwable) {
                    weatherData!!.text = t.message
                    //error("KO")
                }
            })
        }

        /*connectedDevicesRequest.enqueue(object : Callback<List<Course>> {
            override fun onResponse(call: Call<List<Course>>, response: Response<List<Course>>) {
                val allCourse = response.body()
                if (allCourse != null) {
                    Log.d("bla", "HERE is ALL COURSES FROM HEROKU SERVER:")
                    for (c in allCourse)
                        Log.d("blo", " one course : ${c.title} : ${c.time} ")
                }
            }
            override fun onFailure(call: Call<List<Course>>, t: Throwable) {
                error("KO")
                Log.d("gk", "klm", t)
            }
        })*/



    }

 /*   private var weatherData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice_connected_device)
        weatherData = findViewById(R.id.textView7)
        findViewById<View>(R.id.creation_compte4).setOnClickListener { getCurrentData() }
    }
    internal fun getCurrentData() {
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(WeatherService::class.java)
        val call = service.getCurrentWeatherData(lat, lon, AppId)
        call.enqueue(object : Callback<WeatherResponse> {
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                println("bonjour")
                if (response.code() == 200) {
                    val weatherResponse = response.body()!!

                    val stringBuilder = "Country: " +
                            weatherResponse.sys!!.country +
                            "\n" +
                            "Temperature: " +
                            weatherResponse.main!!.temp +
                            "\n" +
                            "Temperature(Min): " +
                            weatherResponse.main!!.temp_min +
                            "\n" +
                            "Temperature(Max): " +
                            weatherResponse.main!!.temp_max +
                            "\n" +
                            "Humidity: " +
                            weatherResponse.main!!.humidity +
                            "\n" +
                            "Pressure: " +
                            weatherResponse.main!!.pressure

                    weatherData!!.text = stringBuilder
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                println("bonsoir")
                weatherData!!.text = t.message
            }
        })
    }
    companion object {

        var BaseUrl = "https://api.openweathermap.org/"
        var AppId = "2e65127e909e178d0af311a81f39948c"
        var lat = "35"
        var lon = "139"
    }*/

