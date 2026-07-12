package com.example.csoftproject.data.database.converters

import androidx.room.TypeConverter
import com.example.csoftproject.domain.enums.PaymentMethod

class PaymentMethodConverter {

    @TypeConverter
    fun fromPaymentMethodToString(method: PaymentMethod?): String? {
        return method?.name
    }

    @TypeConverter
    fun fromStringToPaymentMethod(method: String?): PaymentMethod? {
        return method?.let { PaymentMethod.valueOf(it) }
    }
}