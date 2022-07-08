package ru.limedev.weather.presentation.custom

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import ru.limedev.weather.R
import ru.limedev.weather.domain.entity.CityType

class WeatherSpinnerAdapter(
    context: Context,
    resource: Int
) : ArrayAdapter<CityType>(context, resource) {

    fun submitArray(array: Array<CityType>) {
        val cityList = array.toList()
        addAll(cityList)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = getItem(position) ?: return view
        val textView = view.findViewById<TextView>(R.id.tv_spinner_text)
        textView.text = context.getString(item.cityName)
        return view
    }
}