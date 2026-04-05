package dam.pmdm.spyrothedragon.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import dam.pmdm.spyrothedragon.adapters.CharactersAdapter
import dam.pmdm.spyrothedragon.databinding.FragmentCharactersBinding
import dam.pmdm.spyrothedragon.models.Character
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream

class CharactersFragment : Fragment() {

    private var _binding: FragmentCharactersBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: CharactersAdapter
    private val charactersList = mutableListOf<Character>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharactersBinding.inflate(inflater, container, false)
        binding.recyclerViewCharacters.layoutManager = LinearLayoutManager(requireContext())

        adapter = CharactersAdapter(charactersList) {
            mostrarBrillo()
        }
        binding.recyclerViewCharacters.adapter = adapter
        loadCharacters()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (_binding != null) {
            binding.brilloCetroView.stopBrillo()
        }
        _binding = null
    }

    private fun mostrarBrillo() {
        val view = binding.brilloCetroView
        view.visibility = View.VISIBLE
        view.alpha = 0f
        view.startBrillo()

        ObjectAnimator.ofFloat(view, "alpha", 0f, 1f).apply {
            duration = 500
            start()
        }

        view.postDelayed({
            if (_binding == null) return@postDelayed
            ObjectAnimator.ofFloat(view, "alpha", 1f, 0f).apply {
                duration = 500
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        if (_binding == null) return
                        view.visibility = View.GONE
                        view.stopBrillo()
                    }
                })
                start()
            }
        }, 3000)
    }

    private fun loadCharacters() {
        try {
            val inputStream: InputStream =
                resources.openRawResource(dam.pmdm.spyrothedragon.R.raw.characters)
            val factory = XmlPullParserFactory.newInstance()
            factory.isNamespaceAware = true
            val parser = factory.newPullParser()
            parser.setInput(inputStream, null)
            var eventType = parser.eventType
            var current: Character? = null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> when (parser.name) {
                        "character" -> current = Character()
                        "name" -> current?.name = parser.nextText()
                        "description" -> current?.description = parser.nextText()
                        "image" -> current?.image = parser.nextText()
                    }

                    XmlPullParser.END_TAG -> {
                        if (parser.name == "character" && current != null) {
                            charactersList.add(current)
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