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

import androidx.room.*
import com.yacgroup.yacguide.database.SqlMacros.Companion.DELETE_ASCENDS
import com.yacgroup.yacguide.database.SqlMacros.Companion.ORDERED_BY_DATE
import com.yacgroup.yacguide.database.SqlMacros.Companion.SELECT_ASCENDS
import com.yacgroup.yacguide.database.SqlMacros.Companion.SELECT_ASCENDS_YEARS
import com.yacgroup.yacguide.database.SqlMacros.Companion.VIA_ASCENDS_ROUTE

@Dao
interface AscendDao {
    @get:Query(SELECT_ASCENDS)
    val all: List<Ascend>

    @Query("$SELECT_ASCENDS WHERE Ascend.year = :year AND Ascend.styleId = :styleId $ORDERED_BY_DATE")
    fun getAll(year: Int, styleId: Int): List<Ascend>

    @Query("$SELECT_ASCENDS WHERE Ascend.year = :year AND Ascend.styleId < :styleIdLimit $ORDERED_BY_DATE")
    fun getAllBelowStyleId(year: Int, styleIdLimit: Int): List<Ascend>

    @Query("$SELECT_ASCENDS WHERE Ascend.routeId = :routeId $ORDERED_BY_DATE")
    fun getAscendsForRoute(routeId: Int): List<Ascend>

    @Query("$SELECT_ASCENDS $VIA_ASCENDS_ROUTE WHERE Route.parentId = :rockId")
    fun getAscendsForRock(rockId: Int): List<Ascend>

    @Query("$SELECT_ASCENDS WHERE Ascend.id = :id")
    fun getAscend(id: Int): Ascend?

    @Query("$SELECT_ASCENDS_YEARS WHERE Ascend.styleId = :styleId")
    fun getYears(styleId: Int): IntArray

    @Query("$SELECT_ASCENDS_YEARS WHERE Ascend.styleId < :styleIdLimit")
    fun getYearsBelowStyleId(styleIdLimit: Int): IntArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ascend: Ascend)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ascends: List<Ascend>)

    @Delete
    fun delete(ascend: Ascend)

    @Query(DELETE_ASCENDS)
    fun deleteAll()
}
