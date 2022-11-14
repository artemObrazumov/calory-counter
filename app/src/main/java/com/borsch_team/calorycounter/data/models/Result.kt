package com.borsch_team.calorycounter.data.models

data class Result(
    val resultCode: Int,
    val message: String = ""
) {
    companion object {
        const val RESULT_SUCCESS = 0

    }
}