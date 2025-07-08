package com.example.newapplication.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.newapplication.R
import org.w3c.dom.Text

class DetailFragment : Fragment() {

    // SafeArgs로 전달받은 값 받기
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_detail.xml inflate
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        setHasOptionsMenu(true)
        // TextView에 값 설정
        val textView: TextView = view.findViewById(R.id.textLocationName)
        textView.text = args.locationName
        view.findViewById<TextView>(R.id.textCountry).text=args.countryName
        view.findViewById<TextView>(R.id.textDescription).text=args.locationInfoDetail
        view.findViewById<TextView>(R.id.textAuthor).text=args.user
        view.findViewById<RatingBar>(R.id.ratingBar).rating=args.starbar.toInt().toFloat()
        Glide.with(requireContext())
            .load(args.url)
            .into(view.findViewById<ImageView>(R.id.imageLocation))
        return view

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}