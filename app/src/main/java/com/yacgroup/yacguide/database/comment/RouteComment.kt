/*
 * Copyright (C) 2019 Fabian Kantereit
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.yacgroup.yacguide.database.comment

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class RouteComment {

    @PrimaryKey
    var id: Int = 0

    var qualityId: Int = 0
    var securityId: Int = 0
    var wetnessId: Int = 0
    var gradeId: Int = 0
    var text: String? = null
    var routeId: Int = 0

    companion object {

        // This needs to be in sync with sandsteinklettern.de!
        val QUALITY_MAP: Map<Int, String> = object : HashMap<Int, String>() {
            init {
                put(1, "sehr lohnend")
                put(2, "lohnend")
                put(3, "ok")
                put(4, "Geschmackssache")
                put(5, "Müll")
            }
        }

        // This needs to be in sync with sandsteinklettern.de!
        val GRADE_MAP: Map<Int, String> = object : HashMap<Int, String>() {
            init {
                put(1, "I")
                put(2, "II")
                put(3, "III")
                put(4, "IV")
                put(5, "V")
                put(6, "VI")
                put(7, "VIIa")
                put(8, "VIIb")
                put(9, "VIIc")
                put(10, "VIIIa")
                put(11, "VIIIb")
                put(12, "VIIIc")
                put(13, "IXa")
                put(14, "IXb")
                put(15, "IXc")
                put(16, "Xa")
                put(17, "Xb")
                put(18, "Xc")
                put(19, "XIa")
                put(20, "XIb")
                put(21, "XIc")
                put(22, "XIIa")
                put(23, "XIIb")
                put(24, "XIIc")
            }
        }

        // This needs to be in sync with sandsteinklettern.de!
        val SECURITY_MAP: Map<Int, String> = object : HashMap<Int, String>() {
            init {
                put(1, "übersichert")
                put(2, "gut")
                put(3, "ausreichend")
                put(4, "kompliziert")
                put(5, "ungenügend")
                put(6, "kamikaze")
            }
        }

        // This needs to be in sync with sandsteinklettern.de!
        val WETNESS_MAP: Map<Int, String> = object : HashMap<Int, String>() {
            init {
                put(1, "Regenweg")
                put(2, "schnellabtrocknend")
                put(3, "normal abtrocknend")
                put(4, "oft feucht")
                put(5, "immer nass")
            }
        }
    }
}
