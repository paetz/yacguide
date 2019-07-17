package com.example.paetz.yacguide

import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner

import com.example.paetz.yacguide.database.AppDatabase
import com.example.paetz.yacguide.database.Ascend
import com.example.paetz.yacguide.database.Route
import com.example.paetz.yacguide.utils.AscendStyle
import com.example.paetz.yacguide.utils.IntentConstants

import java.util.ArrayList
import java.util.Calendar

class AscendActivity : AppCompatActivity() {

    private var _db: AppDatabase? = null
    private var _ascend: Ascend? = null
    private var _route: Route? = null
    private var _partnerIds: ArrayList<Int>? = null
    private var _resultUpdated: Int = 0
    private var _styleId: Int = 0
    private var _year: Int = 0
    private var _month: Int = 0
    private var _day: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ascend)

        _db = AppDatabase.getAppDatabase(this)
        _resultUpdated = IntentConstants.RESULT_NO_UPDATE
        _ascend = _db!!.ascendDao().getAscend(intent.getIntExtra(IntentConstants.ASCEND_KEY, AppDatabase.INVALID_ID))
        val routeId = intent.getIntExtra(IntentConstants.ROUTE_KEY, AppDatabase.INVALID_ID)
        _route = _db!!.routeDao().getRoute(if (routeId == AppDatabase.INVALID_ID) _ascend!!.routeId else routeId)
        // Beware: _route may still be null (if the route of this ascend has been deleted meanwhile)
        _partnerIds = intent.getIntegerArrayListExtra(IntentConstants.ASCEND_PARTNER_IDS)
        if (_partnerIds == null) {
            _partnerIds = if (_ascend == null) ArrayList() else _ascend!!.partnerIds
        }
        _day = 0
        _month = _day
        _year = _month
        _styleId = _year

        findViewById<View>(R.id.notesEditText).onFocusChangeListener = View.OnFocusChangeListener { v, _ ->
            val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(v.windowToken, 0)
        }

        displayContent()
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            _partnerIds = data!!.getIntegerArrayListExtra(IntentConstants.ASCEND_PARTNER_IDS)
            displayContent()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun enter(v: View) {
        val ascend = Ascend()
        if (_ascend != null) {
            ascend.id = _ascend!!.id
        } else {
            _db!!.routeDao().updateAscendCount(_db!!.routeDao().getAscendCount(_route!!.id) + 1, _route!!.id)
            _db!!.rockDao().updateAscended(true, _db!!.routeDao().getRoute(_route!!.id)!!.parentId)
            _resultUpdated = IntentConstants.RESULT_UPDATED
        }
        ascend.routeId = if (_route == null) _ascend!!.routeId else _route!!.id
        ascend.styleId = _styleId
        ascend.year = _year
        ascend.month = _month
        ascend.day = _day
        ascend.partnerIds = _partnerIds
        ascend.notes = (findViewById<View>(R.id.notesEditText) as EditText).text.toString()
        _db!!.ascendDao().insert(ascend)
        val resultIntent = Intent()
        setResult(_resultUpdated, resultIntent)
        finish()
    }

    @Suppress("UNUSED_PARAMETER")
    fun cancel(v: View) {
        val resultIntent = Intent()
        setResult(IntentConstants.RESULT_NO_UPDATE, resultIntent)
        finish()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("UNUSED_PARAMETER")
    fun enterDate(v: View) {
        val calendar = Calendar.getInstance()
        val yy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)
        val datePicker = DatePickerDialog(this@AscendActivity, DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            _year = year
            _month = monthOfYear + 1
            _day = dayOfMonth
            (findViewById<View>(R.id.dateEditText) as EditText).setText("$_day.$_month.$_year")
        }, yy, mm, dd)
        datePicker.show()
    }

    @Suppress("UNUSED_PARAMETER")
    fun selectPartners(v: View) {
        val partners = findViewById<EditText>(R.id.partnersEditText).text.toString().split(", ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val partnerIds = ArrayList<Int>()
        for (i in partners.indices) {
            partnerIds.add(_db!!.partnerDao().getId(partners[i]))
        }
        val intent = Intent(this@AscendActivity, PartnersActivity::class.java)
        intent.putIntegerArrayListExtra(IntentConstants.ASCEND_PARTNER_IDS, partnerIds)
        startActivityForResult(intent, 0)
    }

    @SuppressLint("SetTextI18n")
    private fun displayContent() {
        title = if (_route != null) _route!!.name + "   " + _route!!.grade else AppDatabase.UNKNOWN_NAME


        val spinner = findViewById<Spinner>(R.id.styleSpinner)
        val adapter = ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, AscendStyle.names)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                _styleId = AscendStyle.fromName(parent.getItemAtPosition(position).toString())!!.id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                _styleId = 0
            }
        }

        if (_ascend != null) {
            _day = _ascend!!.day
            _month = _ascend!!.month
            _year = _ascend!!.year
            findViewById<EditText>(R.id.dateEditText).setText("$_day.$_month.$_year")
            findViewById<EditText>(R.id.notesEditText).setText(_ascend!!.notes)
            spinner.setSelection(adapter.getPosition(AscendStyle.fromId(if (_styleId == 0) _ascend!!.styleId else _styleId)!!.styleName))
        } else {
            spinner.setSelection(if (_styleId != 0) adapter.getPosition(AscendStyle.fromId(_styleId)!!.styleName) else 0)
        }

        val partners = ArrayList<String>()
        for (id in _partnerIds!!) {
            val partner = _db!!.partnerDao().getPartner(id)
            partners.add(partner?.name ?: AppDatabase.UNKNOWN_NAME)
        }
        findViewById<EditText>(R.id.partnersEditText).setText(TextUtils.join(", ", partners))
    }
}