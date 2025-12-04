package uk.co.ianfield.devstat.widget

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import uk.co.ianfield.devstat.R
import uk.co.ianfield.devstat.StatItemAdapter
import uk.co.ianfield.devstat.databinding.FragmentInformationPageBinding
import uk.co.ianfield.devstat.model.StatItem
import java.util.*

class InformationPageFragment : Fragment() {
    private var _binding: FragmentInformationPageBinding? = null
    private val binding get() = _binding!!

    private lateinit var items: ArrayList<StatItem>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInformationPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager

        val adapter = StatItemAdapter(requireContext(), items) { position: Int ->
            val clipboard = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("text label", items[position].toString())
            clipboard.setPrimaryClip(clip)
            Snackbar.make(binding.recyclerView, R.string.copied_to_clipboard, Snackbar.LENGTH_SHORT).show()
        }
        binding.recyclerView.adapter = adapter
    }

    fun setItems(items: ArrayList<StatItem>) {
        this.items = items
    }
}
