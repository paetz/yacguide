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

package com.yacgroup.yacguide.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Sector {

    @PrimaryKey
    var id: Int = 0

    var nr: Float = 0f
    var name: String? = null
    var parentId: Int = 0

    companion object {
        const val SELECT_ALL = "SELECT Sector.* FROM Sector"
        const val DELETE_ALL = "DELETE FROM Sector"
        const val JOIN_ON = "JOIN Sector ON Sector.id ="
        const val FOR_REGION = "WHERE Sector.parentId ="
    }

}
