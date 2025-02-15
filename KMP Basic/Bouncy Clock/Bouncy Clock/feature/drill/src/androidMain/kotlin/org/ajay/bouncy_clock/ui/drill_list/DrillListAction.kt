package org.ajay.bouncy_clock.ui.drill_list


import org.ajay.bouncy_clock.model.DrillTimerEntity

sealed interface DrillListAction {
    data class OnDrillClick(val drill: DrillTimerEntity): DrillListAction
    data class OnDeleteClick(val drill: DrillTimerEntity): DrillListAction
    data class OnUpdateClick(val drill: DrillTimerEntity): DrillListAction
}