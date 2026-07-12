package com.example.csoftproject.viewModel.vm_util_interfaces

import com.example.csoftproject.domain.enums.FilterForTimeFrame
import java.time.LocalDate

interface FiltrationByTimeFrame {
    fun changeFilterByTimeFrame(filterForTimeFrame: FilterForTimeFrame)
    fun setCustomDateRange(start: LocalDate?, end: LocalDate?)
}