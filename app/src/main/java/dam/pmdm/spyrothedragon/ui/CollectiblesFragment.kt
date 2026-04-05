package dam.pmdm.spyrothedragon.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dam.pmdm.spyrothedragon.adapters.CollectiblesAdapter
import dam.pmdm.spyrothedragon.databinding.FragmentCollectiblesBinding
import dam.pmdm.spyrothedragon.models.Collectible
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class CollectiblesFragment : Fragment() {

    private var _binding: FragmentCollectiblesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CollectiblesAdapter
    private val collectiblesList = mutableListOf<Collectible>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectiblesBinding.inflate(inflater, container, false)
        binding.recyclerViewCollectibles.layoutManager = LinearLayoutManager(requireContext())
        adapter = CollectiblesAdapter(collectiblesList)
        binding.recyclerViewCollectibles.adapter = adapter
        loadCollectibles()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadCollectibles() {
        try {
            val inputStream: InputStream =
                resources.openRawResource(dam.pmdm.spyrothedragon.R.raw.collectibles)
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var current: Collectible? = null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> when (parser.name) {
                        "collectible" -> current = Collectible()
                        "name" -> current?.name = parser.nextText()
                        "description" -> current?.description = parser.nextText()
                        "image" -> current?.image = parser.nextText()
                    }

                    XmlPullParser.END_TAG -> {
                        if (parser.name == "collectible" && current != null) {
                            collectiblesList.add(current)
                        }
                    }
                }
                eventType = parser.next()
            }
            adapter.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}