package dev.atick.berger.ui.dashboard.state

import com.github.mikephil.charting.data.LineDataSet
import dev.atick.core.ui.base.BaseUiState
import dev.atick.core.ui.utils.UiText

data class DashboardUiState(
    override val loading: Boolean = false,
    override val toastMessage: UiText? = null,
    val batchNumber: BatchNumber = BatchNumber.BATCH_1,
    val powerConsumption: LineDataSet = LineDataSet(emptyList(), "Power"),
    val inProgress: Boolean = false
) : BaseUiState()
