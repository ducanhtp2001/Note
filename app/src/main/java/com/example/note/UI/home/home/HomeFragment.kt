package com.example.note.UI.home.home

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.note.Adapter.NoteAdapter
import com.example.note.ApiService.ApiClient
import com.example.note.R
import com.example.note.Tools.SQLite.Connect
import com.example.note.Tools.SQLite.ConnectSharing
import com.example.note.Tools.SecutityTools.KeyStoreSystem_RSA
import com.example.note.UI.Calendar.CalendarFragment
import com.example.note.UI.School.SchoolFragment
import com.example.note.UI.home.add_note.AddNoteActivity
import com.example.note.UI.home.edit_note.EditNoteActivity
import com.example.note.UI.settings.SettingsFragment
import com.example.note.base.BaseFragment
import com.example.note.data.AppState
import com.example.note.data.model.Note
import com.example.note.data.model.ResponseNote
import com.example.note.data.model.SinhVien.Companion.getIdFromMaSinhVien
import com.example.note.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder.build
import okhttp3.Request.Builder.post
import okhttp3.Request.Builder.url
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class HomeFragment : BaseFragment<FragmentHomeBinding>(), NoteAdapter.NoteAdapterListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = {
        inflater, container, attachToParent -> FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    private var belong: String = ""

    private val RESULT_CODE_ADDNOTE = 1
    private val RESULT_CODE_EDITNOTE = 2

    private var notes: MutableList<Note> = mutableListOf()
    private var notesToSearch: MutableList<Note> = mutableListOf()

    private var noteAdapter = NoteAdapter()

    private var idSinhVienstr: String? = null

    private var connect: Connect? = null

    override fun initData() {
        belong = resources.getString(R.string.note_belong)
        idSinhVienstr = AppState.getInstance().getIdSinhVien()

        initConnectSharing()
    }

    override fun initView() {
        setHasOptionsMenu(true)
        hideSearch()

        binding.listView.setAdapter(noteAdapter)
        registerForContextMenu(binding.listView)


    }

    override fun bindViewEvents() {
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            startActivityForResult(intent, RESULT_CODE_ADDNOTE)
        }

        binding.btnExit.setOnClickListener {
            callApi()
            hideSearch()
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            replaceFragment(NavMenuType.getFragmentInstance(id))
//            if (id == R.id.calendar) {
//                replaceFragment(CalendarFragment())
//            } else if (id == R.id.home) {
//                if (parentFragment !is HomeFragment) {
//                    val navController =
//                        findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
//                    navController.navigate(R.id.nav_home)
//                }
            true
        }
    }

    override fun bindViewModel() {

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_search) {
            showSearch()
            return true
        } else if (id == R.id.action_sort_by_create) {
            sortByCreate()
            return true
        } else if (id == R.id.action_sort_by_modify) {
            shortByModify()
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    private fun shortByModify() {
        Toast.makeText(activity, "Sort by modify time", Toast.LENGTH_SHORT).show()
        notes.sortWith{ note1, note2 ->
            java.lang.Long.compare(
                note1.ngayCapNhat.time,
                note2.ngayCapNhat.time
            )
        }
        noteAdapter?.notifyDataSetChanged()
    }

    private fun sortByCreate() {
        Toast.makeText(activity, "Sort by create time", Toast.LENGTH_SHORT).show()
        notes.sortWith{ note1, note2 ->
            java.lang.Long.compare(
                note1.ngayTao.time,
                note2.ngayTao.time
            )
        }
        noteAdapter?.notifyDataSetChanged()
    }

    private fun hideSearch() {
        binding.layoutSearch.visibility = View.GONE
    }

    private fun showSearch() {
        binding.layoutSearch.visibility = View.VISIBLE
    }

    private fun callApi() {
//        listView.setAdapter(noteAdapter);
        val idSinhVien = getIdFromMaSinhVien(idSinhVienstr!!)
        val idMap: MutableMap<String, Int> = HashMap()
        idMap["id"] = idSinhVien
        ApiClient.getApiService().getNoteById(idMap).enqueue(object : Callback<ResponseNote> {
            override fun onResponse(call: Call<ResponseNote>, response: Response<ResponseNote>) {
                Log.e("TAG", "onResponse: " + response.body().toString())
                val res = response.body()
                val status = res!!.isStatus
                notes!!.clear()
                if (res.notes != null) {
                    notes!!.addAll(res.notes)
                }
                noteAdapter!!.notifyDataSetChanged()
            }

            override fun onFailure(call: Call<ResponseNote>, t: Throwable) {
                Toast.makeText(activity, "Load err", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        val destinationId = getDestinationId(fragment)
        if (destinationId != 0) {
            val navController =
                findNavController(requireActivity(), R.id.nav_host_fragment_content_main)
            navController.navigate(destinationId)
        }
    }

    private fun getDestinationId(fragment: Fragment): Int {
        var id = 0
        if (fragment is CalendarFragment) {
            id = R.id.calendar
        } else if (fragment is HomeFragment) {
            id = R.id.home
        } else if (fragment is SchoolFragment) {
            id = R.id.school
        } else if (fragment is SettingsFragment) {
            id = R.id.setting
        }
        return id
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RESULT_CODE_ADDNOTE) {
            if (resultCode == Activity.RESULT_OK) {
                callApi()
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val info = menuInfo as AdapterContextMenuInfo?
        noteAdapter.currentList[info!!.position]?.let {
            requireActivity().menuInflater.inflate(R.menu.note_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterContextMenuInfo
        val position = info?.position
        val selectedNote = noteAdapter.currentList.getOrNull(position)

        if (selectedNote != null) {
            val itemId = item.itemId
            if (itemId == R.id.action_pin) {
                val noteTitle = selectedNote.tieuDe
                showPinNotification(noteTitle)
                return true
            } else if (itemId == R.id.action_del) {
                callDeleteApi(selectedNote)
                return true
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun callDeleteApi(selectedNote: Note) {
        val idSinhVien = getIdFromMaSinhVien(idSinhVienstr!!)
        val id = selectedNote.id

        val query = "INSERT INTO note VALUES (null, " + idSinhVien +
                ", '" + KeyStoreSystem_RSA.encryptData(selectedNote.tieuDe) +
                "', '" + KeyStoreSystem_RSA.encryptData(Note.getNgayStr(selectedNote.ngayTao)) +
                "', '" + KeyStoreSystem_RSA.encryptData(Note.getNgayStr(selectedNote.ngayCapNhat)) +
                "', '" + KeyStoreSystem_RSA.encryptData(selectedNote.noiDung) +
                "', '" + KeyStoreSystem_RSA.encryptData(selectedNote.noiDungCua) + "')"

        val client = OkHttpClient()
        val mediaType: MediaType = parse.parse("application/json; charset=utf-8")
        val jsonObject = JsonObject()
        jsonObject.addProperty("idSinhVien", idSinhVien)
        jsonObject.addProperty("id", id)

        val json = jsonObject.toString()
        Log.e("TAG", "saveNote: $json")
        val request: Request = Builder()
            .url("https://ttcs-test.000webhostapp.com/androidApi/deleteNote.php")
            .post(RequestBody.create(mediaType, json))
            .build()
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Toast.makeText(activity, "False", Toast.LENGTH_SHORT).show()
            }

            @Throws(IOException::class)
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                val json = response.body()!!.string()

                activity!!.runOnUiThread {
                    val gson = Gson()
                    val jsonElement = gson.fromJson(json, JsonElement::class.java)
                    val jsonObject = jsonElement.asJsonObject
                    val status = jsonObject["status"].asBoolean
                    val message = jsonObject["message"].asString
                    if (status) {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                        connect!!.nonReturnQuery(query)
                        callApi()
                    } else {
                        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun initConnectSharing() {
        connect = ConnectSharing.getConnectSharing(activity)

        // cau lenh reset sqlite
        connect?.nonReturnQuery(
            "CREATE TABLE IF NOT EXISTS note (" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    idsinhvien INT," +
                    "    tieude TEXT," +
                    "    ngayTao DATE," +
                    "    ngayCapNhat DATE," +
                    "    noidung TEXT," +
                    "    noidungcua TEXT" +
                    ")"
        )
    }

    private fun showPinNotification(noteTitle: String) {
        val notificationManager =
            requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the notification channel (required for Android Oreo and above)
            val channelId = noteTitle // Use note_title as the channel ID
            val channel =
                NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(requireActivity(), noteTitle)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(noteTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

        notificationManager.notify(0, builder.build())
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(activity, EditNoteActivity::class.java)
        startActivityForResult(intent, RESULT_CODE_EDITNOTE)
    }
}
