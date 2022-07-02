package ru.limedev.weather.domain.entity

import androidx.annotation.StringRes
import ru.limedev.weather.R

enum class CityType(
    val lat: String,
    val lon: String,
    @StringRes val cityName: Int
) {
    MINSK(
        "53.8836745",
        "27.01909",
        R.string.city_minsk
    ),
    GOMEL(
        "52.4248758",
        "30.6638297",
        R.string.city_gomel
    ),
    VITEBSK(
        "55.1938373",
        "29.9078183",
        R.string.city_vitebsk
    ),
    MOGILEV(
        "53.8816358",
        "30.2182224",
        R.string.city_mogilev
    ),
    GRODNO(
        "53.6839678",
        "23.5637731",
        R.string.city_grodno
    ),
    BREST(
        "52.0878618",
        "23.4158077",
        R.string.city_brest
    );
}