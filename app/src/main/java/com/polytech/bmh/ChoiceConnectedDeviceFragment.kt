package com.polytech.bmh

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.polytech.bmh.data.model.ConnectedDeviceProperties
import com.polytech.bmh.data.model.ConnectedDevicePropertiesSubset
import com.polytech.bmh.databinding.FragmentChoiceConnectedDeviceBinding
import com.polytech.bmh.viewmodel.ChoiceConnectedDeviceViewModel
import com.polytech.bmh.viewmodelfactory.ChoiceConnectedDeviceViewModelFactory

class ChoiceConnectedDeviceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentChoiceConnectedDeviceBinding
    private lateinit var viewModel: ChoiceConnectedDeviceViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_choice_connected_device,
            container,
            false
        )

        val viewModelFactory = ChoiceConnectedDeviceViewModelFactory()
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(ChoiceConnectedDeviceViewModel::class.java)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.getConnectedDevices()

        viewModel.response.observe(viewLifecycleOwner, Observer {

            val result = it ?: return@Observer

            Toast.makeText(
                this.context,
                "${result}",
                Toast.LENGTH_LONG
            ).show()
        })

        viewModel.connectedDevice.observe(viewLifecycleOwner, Observer {
            val result = it ?: return@Observer

            val listConnectedDeviceProperty: List<ConnectedDeviceProperties> = result
            val listConnectedDevicePropertySubset : MutableList<String> = mutableListOf()

            for (connectedDevice in listConnectedDeviceProperty) {
                val connectedDevicePropertySubset : ConnectedDevicePropertiesSubset = ConnectedDevicePropertiesSubset(connectedDevice._id, connectedDevice.name)
                val connectedDevicePropertySubsetString = "Objet n°" + connectedDevicePropertySubset._id + " : " + connectedDevicePropertySubset.name
                listConnectedDevicePropertySubset.add(connectedDevicePropertySubsetString)
            }

            var arrayAdapter = ArrayAdapter(this.requireContext(), android.R.layout.simple_spinner_item, listConnectedDevicePropertySubset)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

            binding.spinnerConnectedDeviceChoice?.adapter = arrayAdapter
            binding.spinnerConnectedDeviceChoice?.onItemSelectedListener = this

        })

        binding.imageBackArrow.setOnClickListener {
            this.findNavController().navigate(R.id.action_choiceConnectedDeviceFragment_to_loginFragment)
        }

        binding.buttonAddConnectedDevice.setOnClickListener {
            this.findNavController().navigate(R.id.action_choiceConnectedDeviceFragment_to_addConnectedDeviceFragment)
        }

        binding.buttonValidateConnectedDevice.setOnClickListener {
            this.findNavController().navigate(R.id.action_choiceConnectedDeviceFragment_to_selectColorFragment)
        }

        return binding.root
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        var items: String = parent?.getItemAtPosition(position) as String
        Toast.makeText(this.context, "$items", Toast.LENGTH_LONG).show()
        binding.textView7.text = items
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        Toast.makeText(this.context, "Nothing select", Toast.LENGTH_LONG).show()
    }
}




      /*  val btAdd = findViewById<FloatingActionButton>(R.id.buttonAddConnectedDevice)

        btAdd.setOnClickListener {
            val AddConnectedDeviceActivityIntent = Intent(this, AddConnectedDeviceActivity::class.java)
            startActivity(AddConnectedDeviceActivityIntent)
        }

        val btValidateConnectedDevice = findViewById<Button>(R.id.buttonValidateConnectedDevice)

        btValidateConnectedDevice.setOnClickListener {
            val SelectColorActivityIntent = Intent(this, SelectColorActivity::class.java)
            startActivity(SelectColorActivityIntent)
        }
    }

        internal fun getCurrentData() {

            val service = RetrofitInstance.getRetrofitInstance().create(ConnectedDevicesProperties::class.java)

            val connectedDevicesRequest = service.getConnectedDevices()

            println(connectedDevicesRequest)

            connectedDevicesRequest.enqueue(object : Callback<List<ConnectedDeviceProperty>>,
                AdapterView.OnItemSelectedListener {
                override fun onResponse(
                    call: Call<List<ConnectedDeviceProperty>>,
                    response: Response<List<ConnectedDeviceProperty>>
                ) {
                    val listConnectedDeviceProperty = response.body()
                    if (listConnectedDeviceProperty != null) {

                        val listConnectedDevicePropertySubset : MutableList<String> = mutableListOf()

                        for (c in listConnectedDeviceProperty) {
                            val connectedDevicePropertySubset : ConnectedDevicePropertySubset = ConnectedDevicePropertySubset(c._id, c.name)
                            val connectedDevicePropertySubsetString = "Objet n°" + connectedDevicePropertySubset._id + " : " + connectedDevicePropertySubset.name
                            //listConnectedDevicePropertySubset.add(connectedDevicePropertySubset)
                            listConnectedDevicePropertySubset.add(connectedDevicePropertySubsetString)

                            //weatherData!!.text = "Objet n°" + c._id + " : " + c.name + "\n"

                        }

                        //var connectedDeviceDifferentValues = arrayListOf<List<ConnectedDevicePropertySubset>(listConnectedDevicePropertySubset)

                        var arrayAdapter = ArrayAdapter(this@ChoiceConnectedDeviceFragment, android.R.layout.simple_spinner_item, listConnectedDevicePropertySubset)
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                        val connectedDevicesSpinner = findViewById<Spinner>(R.id.spinnerConnectedDeviceChoice)

                        connectedDevicesSpinner?.adapter = arrayAdapter
                        connectedDevicesSpinner?.onItemSelectedListener = this

                    }
                }

                override fun onFailure(call: Call<List<ConnectedDeviceProperty>>, t: Throwable) {
                    //weatherData!!.text = t.message
                    error("KO")
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    //var items: ConnectedDevicePropertySubset = parent?.getItemAtPosition(position) as ConnectedDevicePropertySubset
                    var items: String = parent?.getItemAtPosition(position) as String
                    Toast.makeText(applicationContext, "$items", Toast.LENGTH_LONG).show()
                    choiceConnectedDevice!!.text = items
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    Toast.makeText(applicationContext, "Nothing select", Toast.LENGTH_LONG).show()

                }
            })
        }*/



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



    //}

 /*   private var weatherData: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_choice_connected_device)
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

