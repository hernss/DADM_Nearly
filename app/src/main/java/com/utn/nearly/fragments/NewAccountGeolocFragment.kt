package com.utn.nearly.fragments

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.utn.nearly.databinding.FragmentNewAccountGeolocBinding
import com.utn.nearly.view_models.NewAccountGeolocViewModel
import android.provider.Settings
import android.util.Log


import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.Observer
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class NewAccountGeolocFragment : Fragment() {
    lateinit var binding : FragmentNewAccountGeolocBinding
    val PERMISSION_ID = 42

    companion object {
        fun newInstance() = NewAccountGeolocFragment()
    }

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: NewAccountGeolocViewModel


    private var mapsSupported: Boolean = true
    private var myPosition = LatLng(-4.0, -5.0)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNewAccountGeolocBinding.inflate(inflater, container, false)
        binding.mapView.onCreate(savedInstanceState)
        setupMap(myPosition)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        return binding.root
    }


    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }


    override fun onStart() {
        super.onStart()

        binding.btnNewAccountFinish.setOnClickListener {
            //Me fijo si ya tengo la localizacion cargada
            if(viewModel.latitud == 0.0){
                //Sino espero
                Toast.makeText(requireContext(), "Wait for geolocalization", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            //Actualizo el usuario y cuando termino salto de pantalla
            viewModel.updateUser()

        }

        viewModel.userUpdated.observe(viewLifecycleOwner, Observer {
            if(it){
                //Salto a la siguiente pantalla
                if(viewModel.userType == "SHOP"){
                    val action = NewAccountGeolocFragmentDirections.actionNewAccountGeolocFragmentToNewAccountShopDetailFragment()
                    binding.root.findNavController().navigate(action)
                }else if (viewModel.userType == "DELIVERY"){
                    val action = NewAccountGeolocFragmentDirections.actionNewAccountGeolocFragmentToDeliveryMainActivity()
                    binding.root.findNavController().navigate(action)
                    activity?.finish()
                }else if (viewModel.userType == "CUSTOMER"){
                    val action = NewAccountGeolocFragmentDirections.actionNewAccountGeolocFragmentToCustomerMainActivity()
                    binding.root.findNavController().navigate(action)
                    activity?.finish()
                }
                stopLocationData()
            }
        })

        viewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if(isLoading){
                //Show Spinner
                binding.geolocProgressBar.visibility = View.VISIBLE
            }else{
                //Hide Spinner
                binding.geolocProgressBar.visibility = View.GONE
            }
        })

        getLastLocation()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NewAccountGeolocViewModel::class.java)
        // TODO: Use the ViewModel

        //Maps initialization
        try {


            MapsInitializer.initialize(requireActivity())
/*

            // Initialize the SDK
            Places.initialize(requireContext(), getString(R.string.places_api_key))

            // Create a new PlacesClient instance
            val placesClient = Places.createClient(requireContext())

            // Initialize the AutocompleteSupportFragment.
            val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

            // Specify the types of place data to return.
            autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

            // Set up a PlaceSelectionListener to handle the response.
            autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
                override fun onPlaceSelected(place: Place) {
                    // TODO: Get info about the selected place.
                    Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng}")
                    setupMap(place.latLng!!)
                }

                override fun onError(status: Status) {
                    // TODO: Handle the error.
                    Log.i(TAG, "An error occurred: $status")
                }
            })
*/
        } catch (e: GooglePlayServicesNotAvailableException) {
            mapsSupported = false
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                Log.d("GEOLOC", "getLastLocation() is enabled")
                mFusedLocationClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    Log.d("GEOLOC", "last location complete")
                    var location: Location? = task.result
                    if (location == null) {
                        Log.d("GEOLOC", "location null")
                        requestNewLocationData()
                    } else {
                        binding.txtLat.text = location.latitude.toString()
                        binding.txtLon.text = location.longitude.toString()
                        viewModel.latitud = location.latitude
                        viewModel.longitud = location.longitude
                        setupMap(location)
                    }
                }


            } else {
                Toast.makeText(requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        Log.d("GEOLOC", "requestNewLocationData()")
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 1
        mLocationRequest.fastestInterval = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private fun stopLocationData() {
        Log.d("GEOLOC", "stopLocationData()")
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        }catch (e:Exception){
            Log.d("GEOLOC", "Error $e")
        }

    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            Log.d ("Test",mLastLocation.latitude.toString())
            Log.d ("Test",mLastLocation.longitude.toString())
            binding.txtLat.text = mLastLocation.latitude.toString()
            binding.txtLon.text = mLastLocation.longitude.toString()
            viewModel.latitud = mLastLocation.latitude
            viewModel.longitud = mLastLocation.longitude
            setupMap(mLastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }else{
                Toast.makeText(requireContext(), "You must activate location for this app.", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setupMap(location : Location) {
        setupMap(LatLng(location.latitude,location.longitude))
    }

    private fun setupMap(latLng: LatLng) {
        if(mapsSupported) {
            binding.mapView.getMapAsync { map: GoogleMap ->
                //map.isMyLocationEnabled = true
                map.mapType = GoogleMap.MAP_TYPE_NORMAL
                map.clear()

                val googlePlex = CameraPosition.builder()
                    .target(latLng)
                    .zoom(15f)
                    .bearing(0f)
                    .tilt(0f)
                    .build()

                map.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 50, null)

                val markerOptions = MarkerOptions()
                    .position(latLng)
                    .title("Your Location")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                val marker = map.addMarker(markerOptions)
                marker?.showInfoWindow()

                binding.mapView.onResume()
            }
        } else {
            binding.mapView.visibility = View.GONE
        }
    }
}