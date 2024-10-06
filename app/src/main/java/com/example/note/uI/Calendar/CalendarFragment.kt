package com.example.note.uI.Calendar

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView.OnDateChangeListener
import androidx.activity.result.contract.ActivityResultContracts
import com.example.note.R
import com.example.note.Tools.PermissionUtils
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.base.BaseFragment
import com.example.note.base.provideViewModels
import com.example.note.data.model.Schedule
import com.example.note.databinding.FragmentCalendarBinding
import com.example.note.uI.Calendar.CalendarToolsModel.LichHocStructure
import com.example.note.uI.Calendar.CalendarToolsModel.MonHoc
import com.example.note.uI.Calendar.CalendarToolsModel.MyCell
import com.example.note.uI.Calendar.CalendarToolsModel.TableContent
import com.example.note.uI.Calendar.adapter.ScheduleListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.IOException
import java.io.InputStream
import java.util.Date

@AndroidEntryPoint
class CalendarFragment : BaseFragment<FragmentCalendarBinding>(), ScheduleListAdapter.ScheduleListener {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentCalendarBinding
        get() = { layoutInflater, viewGroup, b ->
            FragmentCalendarBinding.inflate(layoutInflater, viewGroup, b)
        }

    private val viewModel: ScheduleViewModel by provideViewModels()

    private val adapter: ScheduleListAdapter = ScheduleListAdapter()

    override fun initData() {
        super.initData()
        viewModel.getCalendar()
    }

    override fun initView() {
        super.initView()
        binding.calendarListView.adapter = adapter
    }

    override fun bindViewEvents() {
        super.bindViewEvents()

        binding.calendarView.setOnDateChangeListener(OnDateChangeListener { view, year, month, dayOfMonth ->
            val selectedDate = Date(year - 1900, month, dayOfMonth)
            viewModel.getCalendarOnDate(selectedDate)
            binding.calendarListView.setVisibility(View.VISIBLE)
        })

        binding.btnAddCalendar.setOnClickListener(View.OnClickListener {
            pickFileFromGallery()
        })
    }

    override fun bindViewModel() {
        super.bindViewModel()
        viewModel.toastMessage bindTo toaster::display
        viewModel.toastRes bindTo toaster::display

        viewModel.schedule bindTo {
            adapter.submitList(it)
        }
    }

    private fun pickFileFromGallery() {

        PermissionUtils.askForPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) {
            chooseExcelFile()
        }
    }

    private fun chooseExcelFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("application/vnd.ms-excel")
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        resultLauncher.launch(intent)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent: Intent? = result.data
            val fileUri = intent?.data
            fileUri?.let { uri ->
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val inputStream = requireActivity().contentResolver.openInputStream(
                            uri
                        )

                        val filePath = getRealPathFromUri(uri) // Lấy đường dẫn từ Uri
                        val workbook = getWorkbook(inputStream, filePath)

                        // Lấy sheet cần đọc
                        val sheet = workbook.getSheetAt(0)

                        // Duyệt qua từng dòng và từng ô để lấy dữ liệu
                        val rows: MutableList<Row> = ArrayList()
                        val rowIterator: Iterator<Row> = sheet.iterator()

                        while (rowIterator.hasNext()) {
                            val row = rowIterator.next()
                            rows.add(row)
                        }

                        val numRows = rows.size
                        val maxNumCols = 10

                        val dataArray = Array(numRows) { arrayOfNulls<String>(maxNumCols) }

                        for (i in 0 until numRows) {
                            val row = rows[i]
                            for (j in 0 until maxNumCols) {
                                val cell = row.getCell(j)
                                dataArray[i][j] = if ((cell != null)) cell.toString() else ""
                            }
                        }

                        //                    for (int i = 0; i < dataArray.length; i++) {
//                        for (int j = 0; j < dataArray[0].length; j++) {
//                            String cellInfo = "Cell at Row " + (i + 1) + " and Column " + (j + 1) + " Value: " + dataArray[i][j];
//                            Log.d("ExcelReader", cellInfo);
//                        }
//                    }
                        val maSinhVien = findMSV(dataArray)
                        Log.d("MSV", maSinhVien!!.content)

                        //                    printData(dataArray);
                        var tableContent = TableContent()
                        tableContent = findTableContent(dataArray)

                        //                    Log.d("content", "start: " + dataArray[tableContentStart][3] + tableContentStart
//                        + " end: " + dataArray[tableContentEnd][3] + tableContentEnd);
                        val monHocs = getMonHocFromFile(dataArray, tableContent)

                        if (monHocs.isNotEmpty()) {
//                            callApiUpdateCalendar(
//                                monHocs, getIdFromMaSinhVien(
//                                    maSinhVien.content
//                                )
//                            )

                            viewModel.submitSchedule(monHocs)
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            toaster.display(R.string.common_err)
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == RESULT_CODE_ADDNOTE) {
//            if (resultCode == Activity.RESULT_OK) {
//            } else if (resultCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(activity, "Cancel", Toast.LENGTH_SHORT).show()
//            }
//        }
//
//        if (requestCode == PICK_EXCEL_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                val uri = data.data
//
//            }
//        }
//    }

//    private fun callApiUpdateCalendar(monHocs: List<MonHoc>, msv: Int) {
//        val dangKyHocJson = DangKyHocJson(msv, monHocs)
//        val gson = Gson()
//        val json = gson.toJson(dangKyHocJson)
//
//        //        Log.d("json", json);
//        val client = OkHttpClient()
//        val mediaType: MediaType = parse.parse("application/json; charset=utf-8")
//
//        Log.e("TAG", "saveNote: $json")
//        val request: Request = Builder()
//            .url("https://ttcs-test.000webhostapp.com/androidApi/updateCalendar.php")
//            .post(RequestBody.create(mediaType, json))
//            .build()
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//            }
//
//            @Throws(IOException::class)
//            override fun onResponse(call: Call, response: Response) {
//                val json = response.body()!!.string()
//
//                if (activity != null) {
//                    activity!!.runOnUiThread {
//                        val gson = Gson()
//                        val jsonElement = gson.fromJson(json, JsonElement::class.java)
//                        val jsonObject = jsonElement.asJsonObject
//                        var message: String? = ""
//                        var status = false
//                        if (jsonObject.has("message")) {
//                            message = jsonObject["message"].asString
//                        }
//                        if (jsonObject.has("status")) {
//                            status = jsonObject["status"].asBoolean
//                        }
//                        if (status) {
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
//                        } else {
//                            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            }
//        })
//    }

    private fun getMonHocFromFile(
        dataArray: Array<Array<String?>>,
        tableContent: TableContent
    ): List<MonHoc> {
        val monHocs: MutableList<MonHoc> = ArrayList()

        for (i in tableContent.start..tableContent.end) {
            val tenMonHoc = dataArray[i][tableContent.colTenMonHoc]
            val soTin = MonHoc.getSoTinFromString(dataArray[i][tableContent.colSoTin])
            val lopTinChi = MonHoc.getLopTinChiFromString(dataArray[i][tableContent.colLopTinChi])
            val lichHoc = dataArray[i][tableContent.colLichHoc]

            val lichHocStructure = LichHocStructure(lichHoc)
            val monHoc = MonHoc(tenMonHoc, soTin, lopTinChi, lichHocStructure.ngayHocs)
            monHocs.add(monHoc)
        }

        return monHocs
    }

    private fun findTableContent(dataArray: Array<Array<String?>>): TableContent {
        val tableContent = TableContent()
        val numRows = dataArray.size
        val numCols = dataArray[0].size
        for (i in 0 until numRows) {
            if (dataArray[i][0] == "STT") {
                tableContent.start = i + 1

                for (j in 0 until numCols) {
                    if (dataArray[i][j] == "Tên học phần") {
                        tableContent.colTenMonHoc = j
                        continue
                    }
                    if (dataArray[i][j] == "Số TC") {
                        tableContent.colSoTin = j
                        continue
                    }
                    if (dataArray[i][j] == "Lớp học phần") {
                        tableContent.colLopTinChi = j
                        continue
                    }
                    if (dataArray[i][j] == "Thời gian địa điểm") {
                        tableContent.colLichHoc = j
                        break
                    }
                }
            }
            if (dataArray[i][0] == "Tổng cộng:") {
                tableContent.end = i - 1
                break
            }
        }

        return tableContent
    }

    private fun findMSV(dataArray: Array<Array<String?>>): MyCell? {
        val numRows = dataArray.size
        val numCols = dataArray[0].size
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                if (dataArray[i][j] == "Mã số :") {
                    return MyCell(i, j + 1, dataArray[i][j + 1])
                }
            }
        }
        return null
    }

    private fun printData(dataArray: Array<Array<String>>) {
        val numRows = dataArray.size
        val numCols = dataArray[0].size
        for (i in 0 until numRows) {
            for (j in 0 until numCols) {
                Log.d("table", "row: " + i + ", col: " + j + ", content: " + dataArray[i][j])
            }
        }
    }

    @Throws(IOException::class)
    private fun getWorkbook(inputStream: InputStream?, filePath: String?): Workbook {
        val workbook = if (filePath != null && filePath.endsWith(".xls")) {
            // Xử lý định dạng Excel cũ (.xls) bằng HSSF
            HSSFWorkbook(inputStream)
        } else if (filePath != null && filePath.endsWith(".xlsx")) {
            // Xử lý định dạng Excel mới (.xlsx) bằng XSSF
            XSSFWorkbook(inputStream)
        } else {
            // Xử lý các định dạng khác nếu cần
            throw UnsupportedOperationException("Unsupported Excel format")
        }

        return workbook
    }

    private fun getRealPathFromUri(uri: Uri?): String? {
        var result: String? = null
        val projection = arrayOf(OpenableColumns.DISPLAY_NAME)
        var cursor: Cursor? = null

        try {
            cursor = requireActivity().contentResolver.query(uri!!, projection, null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                result = cursor.getString(columnIndex)
            }
        } finally {
            cursor?.close()
        }

        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    companion object {
        private const val PICK_EXCEL_FILE_REQUEST = 2
    }

    override fun onScheduleClick(schedule: Schedule) {

    }
}