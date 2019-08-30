package com.example.project1.ui.main.home


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.databinding.FragmentHomeBinding
import com.example.project1.model.ParkingLot
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.mancj.materialsearchbar.MaterialSearchBar
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener
import io.reactivex.Observable
import kotlinx.coroutines.ExperimentalCoroutinesApi


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
@ExperimentalCoroutinesApi
class HomeFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {

    private lateinit var fragmentHomeBinding: FragmentHomeBinding
    private lateinit var supportMapFragment: SupportMapFragment
    private lateinit var homeViewModel: HomeViewModel
    private var googleMap: GoogleMap? = null
    private lateinit var materialSearchBar: MaterialSearchBar
    private lateinit var homeListAdapter: HomeListAdapter
    private val parkingLotList: MutableList<ParkingLot> = ArrayList()

    private fun onSelectItem(item: ParkingLot) {
        Toast.makeText(activity, item.name, Toast.LENGTH_SHORT).show()
    }

    private lateinit var filteredParkingLotRecyclerView: RecyclerView

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentHomeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return fragmentHomeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        supportMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        homeListAdapter = HomeListAdapter(this::onSelectItem)
        filteredParkingLotRecyclerView = fragmentHomeBinding.recyclerView
        filteredParkingLotRecyclerView.apply {
            filteredParkingLotRecyclerView.visibility = View.GONE
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = homeListAdapter
        }

        materialSearchBar = fragmentHomeBinding.searchBar
        materialSearchBar.apply {
            elevation = 6f
            setPlaceHolder("Search parking")
            setMaxSuggestionCount(0)
        }

        materialSearchBar.setOnSearchActionListener(object : OnSearchActionListener {
            override fun onButtonClicked(buttonCode: Int) {
                when (buttonCode) {
                }
            }

            override fun onSearchStateChanged(enabled: Boolean) {
                when (enabled) {
                    false -> {
                        homeViewModel.filterParkingLots("")
                        filteredParkingLotRecyclerView.visibility = View.GONE
                    }
                    true -> {
                        homeViewModel.filterParkingLots("")
                        filteredParkingLotRecyclerView.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSearchConfirmed(text: CharSequence?) {
            }

        })

        materialSearchBar.addTextChangeListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                /*launch {
                    delay(300)  //debounce timeOut
                    homeViewModel.filterParkingLots(s.toString())
                }*/
                homeViewModel.filterParkingLots(s.toString())
            }

        })

        homeViewModel.getFilteredParkingList().observe(viewLifecycleOwner, Observer { parkingLots ->
            homeListAdapter.updateList(parkingLots)
        })

        homeViewModel.getParkingList().observe(viewLifecycleOwner, Observer { parkingLots ->
            parkingLots.isNotEmpty().run {
                parkingLotList.addAll(parkingLots)
            }
        })
    }

    override fun onMapReady(map: GoogleMap?) {
        googleMap = map
        map?.apply {
            uiSettings.isCompassEnabled = false
            uiSettings.isMapToolbarEnabled = false
            uiSettings.isZoomGesturesEnabled = false
            moveCamera(CameraUpdateFactory.newLatLng(LatLng(14.568294, 120.7404308)))
        }
        map?.setOnMapLoadedCallback(this)
    }


    override fun onMapLoaded() {
        if (parkingLotList.isNotEmpty()) {
            googleMap?.clear()
            val builder: LatLngBounds.Builder = LatLngBounds.builder()
            parkingLotList.forEach { parkingLot ->
                builder.include(parkingLot.latLng)
                googleMap?.addMarker(
                        MarkerOptions()
                                .position(parkingLot.latLng)
                                .draggable(false)
                                .title(parkingLot.name)
                )
            }.run {
                googleMap?.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 240))
            }
        }

    }


}
