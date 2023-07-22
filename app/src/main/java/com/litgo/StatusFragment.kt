package com.litgo

import androidx.fragment.app.Fragment

enum class ReportStatus {
    CLEANED,
    NOT_CLEAN
}

class StatusFragment(status: ReportStatus, description: String, ): Fragment() {

}