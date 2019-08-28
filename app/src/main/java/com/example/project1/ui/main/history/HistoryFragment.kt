package com.example.project1.ui.main.history


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.example.project1.databinding.FragmentHistoryBinding
import com.example.project1.enums.DateTimeFormat
import com.example.project1.utils.toReadableString
import kotlinx.android.synthetic.main.fragment_history.*
import java.util.*

class HistoryFragment : Fragment() {

    private lateinit var fragmentHistoryBinding: FragmentHistoryBinding
    private val historyListAdapter: HistoryListAdapter = HistoryListAdapter()
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        fragmentHistoryBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        return fragmentHistoryBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentHistoryBinding.recyclerView.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = historyListAdapter
        }

        //initialize date picker
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            fragmentHistoryBinding.toolbar.title = calendar.time.toReadableString(DateTimeFormat.DATE_LONG)
            historyViewModel.filterHistory(calendar.time)
            toolbar.menu.findItem(R.id.action_clear).isVisible = true
            toolbar.menu.findItem(R.id.action_filter).isVisible = false
        }
        datePickerDialog = DatePickerDialog(
                activity!!,
                dateSetListener,
                calendar.get(Calendar.YEAR), // Initial year selection
                calendar.get(Calendar.MONTH), // Initial month selection
                calendar.get(Calendar.DAY_OF_MONTH) // Inital day selection
        )

        historyViewModel = ViewModelProviders.of(this)
                .get(HistoryViewModel::class.java)

        historyViewModel.getHistoryList().observe(this, Observer { parkingHistoryList ->
            when (parkingHistoryList.isEmpty()) {
                true -> {
                    fragmentHistoryBinding.noResult.root.visibility = View.VISIBLE
                    fragmentHistoryBinding.recyclerView.visibility = View.GONE
                }
                false ->  {
                    fragmentHistoryBinding.noResult.root.visibility = View.GONE
                    fragmentHistoryBinding.recyclerView.visibility = View.VISIBLE
                }
            }
            historyListAdapter.updateList(parkingHistoryList)
        })

        fragmentHistoryBinding.toolbar.apply {
            inflateMenu(R.menu.history_menu)
            title = "Transactions"
        }

        fragmentHistoryBinding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_clear -> {
                    fragmentHistoryBinding.toolbar.title = "Transactions"
                    fragmentHistoryBinding.toolbar.subtitle = ""
                    historyViewModel.loadParkingData()
                    toolbar.menu.findItem(R.id.action_filter).isVisible = true
                    toolbar.menu.findItem(R.id.action_clear).isVisible = false
                    true
                }
                R.id.action_filter -> {
                    datePickerDialog.show()
                    true
                }
                else -> {
                    super.onOptionsItemSelected(it)
                }

            }
        }

    }

}
