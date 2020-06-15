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

package com.yacgroup.yacguide

import android.app.Dialog
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import com.yacgroup.yacguide.network.JSONWebParser
import com.yacgroup.yacguide.utils.NetworkUtils

abstract class UpdatableTableActivity : TableActivity(), UpdateListener {

    private var _updateDialog: Dialog? = null
    protected var jsonParser: JSONWebParser? = null

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.sync_delete, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_download -> update()
            R.id.action_delete -> delete()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    // UpdateListener
    override fun onEvent(success: Boolean) {
        _updateDialog?.dismiss()

        if (success) {
            Toast.makeText(this, R.string.objects_refreshed, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, R.string.error_on_refresh, Toast.LENGTH_SHORT).show()
        }
        displayContent()
    }

    private fun update() {
        if (!NetworkUtils.isNetworkAvailable(this)) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show()
            return
        }
        jsonParser?.fetchData()
        showUpdateDialog()
    }

    private fun delete() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog)
        (dialog.findViewById<TextView>(R.id.dialogText)).setText(R.string.dialog_question_delete)
        dialog.findViewById<Button>(R.id.yesButton).setOnClickListener {
            deleteContent()
            dialog.dismiss()
            Toast.makeText(applicationContext, R.string.objects_deleted, Toast.LENGTH_SHORT).show()
            displayContent()
        }
        dialog.findViewById<Button>(R.id.noButton).setOnClickListener { dialog.dismiss() }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun showUpdateDialog() {
        _updateDialog = Dialog(this)
        _updateDialog?.setContentView(R.layout.info_dialog)
        _updateDialog?.setCancelable(false)
        _updateDialog?.setCanceledOnTouchOutside(false)
        _updateDialog?.show()
    }

    protected abstract fun deleteContent()
}
