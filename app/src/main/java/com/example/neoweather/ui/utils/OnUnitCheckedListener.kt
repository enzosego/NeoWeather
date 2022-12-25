package com.example.neoweather.ui.utils

import android.view.View
import com.google.android.material.button.MaterialButtonToggleGroup

class OnUnitCheckedListener(
    private val action: (newState: Boolean) -> Unit,
    private val preferenceState: Boolean,
    private var selectedButtonId: Int,
    private val firstOptionId: Int,
    private val secondOptionId: Int
) : MaterialButtonToggleGroup.OnButtonCheckedListener {

    override fun onButtonChecked(
        group: MaterialButtonToggleGroup,
        checkedId: Int,
        isChecked: Boolean
    ) {
        if (isChecked) {
            when(checkedId) {
                secondOptionId ->
                    action(true)
                else ->
                    action(false)
            }
        } else {
            if (group.checkedButtonId == View.NO_ID) {
                selectedButtonId =
                    when(preferenceState) {
                        true -> secondOptionId
                        else -> firstOptionId
                    }
                group.check(selectedButtonId)
            }
        }
    }
}