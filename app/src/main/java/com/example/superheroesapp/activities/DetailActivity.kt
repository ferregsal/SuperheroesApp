package com.example.superheroesapp.activities

import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.superheroesapp.R
import com.example.superheroesapp.data.Superhero
import com.example.superheroesapp.databinding.ActivityDetailBinding
import com.example.superheroesapp.utils.RetrofitProvider
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.panpf.swsv.CircularLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat

class DetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBinding
    lateinit var superhero: Superhero

    companion object {
        const val EXTRA_SUPERHERO_ID = "SUPERHERO_ID"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val superheroId = intent.getIntExtra(EXTRA_SUPERHERO_ID, -1)
        if (superheroId != -1) {
            getSuperheroById(superheroId)
        } else {
            // Manejar el error si el ID no es válido
        }
    }

    private fun getSuperheroById(superheroId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val superheroApiService = RetrofitProvider.getSuperheroApiService()
                val result = superheroApiService.getSuperheroById(superheroId)
                superhero = result
                runOnUiThread {
                    Log.d("DetailActivity", "UI thread")
                    binding.statsButton.setOnClickListener {
                        showAlertDialogWithChart()
                    }
                    binding.nameDetailTextView.text = result.name
                    binding.fullNameTextView.text = "Real Name: \n${result.biography.realName}"
                    binding.alignmentTextView.text = "Alignment: \n${result.biography.alignment}"
                    when (result.biography.alignment) {
                        "good" -> binding.alignmentTextView.setTextColor(Color.BLUE)
                        "bad" -> binding.alignmentTextView.setTextColor(Color.RED)
                    }
                    binding.alterEgosTextView.text = "Alter Egos: \n${result.biography.alterEgos}"
                    binding.aliasesTextView.text = "Aliases: \n${result.biography.aliases.joinToString("\n")}"
                    binding.placeOfBirthTextView.text = "Place of Origin: \n${result.biography.placeOfBirth}"
                    binding.firstAppearanceTextView.text = "First Appearance: \n${result.biography.firstAppearance}"
                    binding.publisherTextView.text = "Publisher: \n${result.biography.publisher}"

                    Picasso.get().load(result.image.url).into(binding.avatarDetailImageView)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("DetailActivity", "Error fetching superhero data", e)
            }
        }
    }

    private fun showAlertDialogWithChart() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Attributes")

        val customLayout: View = layoutInflater.inflate(R.layout.custom_view_layout, null)
        builder.setView(customLayout)

        val radarChart = customLayout.findViewById<RadarChart>(R.id.radarChart)
        val circularLayout1 = customLayout.findViewById<CircularLayout>(R.id.layout_mainActivity_circular1)

        val scores = arrayOf(
            Score(superhero.stats.intelligence.toFloat(), R.drawable.int_icon),
            Score(superhero.stats.strength.toFloat(), R.drawable.strength_icon),
            Score(superhero.stats.speed.toFloat(), R.drawable.speed_icon),
            Score(superhero.stats.durability.toFloat(), R.drawable.durability_icon),
            Score(superhero.stats.power.toFloat(), R.drawable.power_icon),
            Score(superhero.stats.combat.toFloat(), R.drawable.combat_icon)
        )

        setupRadarChart(radarChart, scores)

        builder.setPositiveButton("OK") { dialog: DialogInterface?, which: Int ->
            // Opcional: Realizar acciones adicionales cuando se pulse "OK"
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun setupRadarChart(radarChart: RadarChart, scores: Array<Score>) {
        val entries = scores.map { RadarEntry(it.score) }

        Log.d("DetailActivity", "Radar Entries: ${entries.joinToString { it.value.toString() }}")

        val dataSet = RadarDataSet(entries, "Superhero Stats")
        dataSet.color = Color.BLUE
        dataSet.fillColor = Color.BLUE
        dataSet.setDrawFilled(true)
        dataSet.lineWidth = 2f

        val data = RadarData(dataSet)
        data.setValueTextSize(8f)
        data.setDrawValues(true) // Mostrar los valores en el gráfico

        radarChart.data = data
        radarChart.description.isEnabled = false
        radarChart.legend.isEnabled = false

        val xAxis: XAxis = radarChart.xAxis
        xAxis.textSize = 9f
        xAxis.valueFormatter = IndexAxisValueFormatter(
            arrayOf("Intelligence", "Strength", "Speed", "Durability", "Power", "Combat")
        )
        /*    arrayOf("Intelligence ${R.drawable.int_icon}", "Strength ${R.drawable.strength_icon}", "Speed ${R.drawable.speed_icon}", "Durability ${R.drawable.durability_icon}", "Power ${R.drawable.power_icon}", "Combat ${R.drawable.combat_icon}")
        )*/
        xAxis.setDrawLabels(true) // Asegurarse de que las etiquetas se dibujen

        val yAxis: YAxis = radarChart.yAxis
        yAxis.setLabelCount(6, true)
        yAxis.textSize = 9f
        yAxis.axisMinimum = 0f
        yAxis.axisMaximum = 100f

        yAxis.setDrawLabels(false)

        radarChart.invalidate() // refresca la vista
    }

    private class Score(val score: Float, val iconId: Int)
}