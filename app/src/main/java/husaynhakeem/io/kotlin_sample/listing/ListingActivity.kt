package husaynhakeem.io.kotlin_sample.listing

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import husaynhakeem.io.kotlin_sample.R
import husaynhakeem.io.kotlin_sample.data.Person
import husaynhakeem.io.kotlin_sample.databinding.ActivityMainBinding

class ListingActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ListingViewModel
    lateinit var bar: Snackbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(ListingViewModel::class.java)
        binding.viewModel = viewModel
        binding.executePendingBindings()

        binding.rvPeople.layoutManager = LinearLayoutManager(this)
        binding.rvPeople.setHasFixedSize(true)
        val personAdapter = PersonAdapter(ArrayList<Person>())
        binding.rvPeople.adapter = personAdapter

        viewModel.people.observe(this, Observer<List<Person>> {
            if (it == null || it.isEmpty())
                onLoadingDataError()
            else {
                personAdapter.replaceData(it)
            }
        })

        viewModel.start()
    }

    fun onLoadingDataError() {
        bar = Snackbar.make(rootView(), getString(R.string.error_loading_people), Snackbar.LENGTH_INDEFINITE);
        bar.setAction(getString(R.string.retry), { view -> viewModel.start() })
        bar.show();
    }

    private fun rootView(): View = findViewById(android.R.id.content)

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onCleared()
    }
}