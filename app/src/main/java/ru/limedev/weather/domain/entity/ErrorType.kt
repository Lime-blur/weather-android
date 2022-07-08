package ru.limedev.weather.domain.entity

import androidx.annotation.StringRes
import ru.limedev.weather.R

enum class ErrorType(
    @StringRes val resId: Int
) {
    ERROR_0_RESULT_UNSUCCESSFUL(R.string.error_0),
    ERROR_1_UNKNOWN_ERROR(R.string.error_1),
    ERROR_2_TIMEOUT(R.string.error_2),
    ERROR_3_NO_CONNECTION(R.string.error_3),
    ERROR_4_FETCH_DATA(R.string.error_4),
    ERROR_5_ENTITY_FIELD_IS_NULL(R.string.error_5);

    companion object {
        fun getErrorFromExceptionMessage(message: String?): ErrorType {
            message?.let {
                return when {
                    it.contains("timeout") -> ERROR_2_TIMEOUT
                    it.contains("No address associated with hostname") -> ERROR_3_NO_CONNECTION
                    else -> ERROR_4_FETCH_DATA
                }
            }
            return ERROR_4_FETCH_DATA
        }
    }
}