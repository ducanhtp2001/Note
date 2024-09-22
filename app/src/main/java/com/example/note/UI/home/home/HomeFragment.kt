package com.example.note.UI.home.home

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.ContextMenu
import android.view.ContextMenu.ContextMenuInfo
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.note.Adapter.NoteAdapter
import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.SQLite.Connect
import com.example.note.Tools.SQLite.ConnectSharing
import com.example.note.Tools.SecutityTools.KeyStoreSystem_RSA
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.UI.Calendar.CalendarFragment
import com.example.note.UI.School.SchoolFragment
import com.example.note.UI.home.add_note.AddNoteActivity
import com.example.note.UI.home.edit_note.EditNoteActivity
import com.example.note.UI.settings.SettingsFragment
import com.example.note.base.BaseFragment
import com.example.note.base.provideViewModels
import com.example.note.data.AppState
import com.example.note.data.model.Note
import com.example.note.data.model.SinhVien.Companion.getIdFromMaSinhVien
import com.example.note.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), NoteAdapter.NoteAdapterListener {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentHomeBinding = {
        inflater, container, attachToParent -> FragmentHomeBinding.inflate(inflater, container, attachToParent)
    }

    private val viewModel: HomeFragmentViewModel by provideViewModels()

    private var belong: String = ""

    private val RESULT_CODE_ADDNOTE = 1
    private val RESULT_CODE_EDITNOTE = 2

    private var adapter = NoteAdapter()

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

        binding.listView.setAdapter(adapter)
        adapter.setListener(this)
        registerForContextMenu(binding.listView)
    }

    override fun bindViewEvents() {
        binding.fabAddNote.setOnClickListener {
            val intent = Intent(activity, AddNoteActivity::class.java)
            startActivityForResult(intent, RESULT_CODE_ADDNOTE)
        }

        binding.btnExit.setOnClickListener {
            hideSearch()
        }

        binding.txtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                viewModel.keyWord = s?.toString() ?: ""
            }

        })

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
        viewModel.displayNotes.bindTo {
            LogHelper.logDebug("submit list: $it")
            adapter.submitList(it)
        }

        viewModel.deleteResponse.bindTo { status ->
            val idSinhVien = getIdFromMaSinhVien(idSinhVienstr ?: "")
            val selectedNote = AppState.getInstance().getSelectedNote().guard { return@bindTo }

            val query = "INSERT INTO note VALUES (null, " + idSinhVien +
                    ", '" + KeyStoreSystem_RSA.encryptData(selectedNote.tieuDe) +
                    "', '" + KeyStoreSystem_RSA.encryptData(Note.getNgayStr(selectedNote.ngayTaoDate)) +
                    "', '" + KeyStoreSystem_RSA.encryptData(Note.getNgayStr(selectedNote.ngayCapNhatDate)) +
                    "', '" + KeyStoreSystem_RSA.encryptData(selectedNote.noiDung) +
                    "', '" + KeyStoreSystem_RSA.encryptData(selectedNote.noiDungCua) + "')"

            connect?.nonReturnQuery(query)
            val msg = if (status) getString(R.string.delete_success) else getString(R.string.delete_false)
            toaster.display(msg)
        }
    }

    override fun viewDidLoad() {
        viewModel.getNotes()
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
            viewModel.sortByCreate()
            return true
        } else if (id == R.id.action_sort_by_modify) {
            viewModel.shortByModify()
            return true
        }
        return super.onOptionsItemSelected(item)
    }




    private fun hideSearch() {
        binding.layoutSearch.visibility = View.GONE
    }

    private fun showSearch() {
        binding.layoutSearch.visibility = View.VISIBLE
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
                viewModel.getNotes()
            } else if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val info = menuInfo as AdapterContextMenuInfo?
        adapter.currentList[info!!.position]?.let {
            requireActivity().menuInflater.inflate(R.menu.note_menu, menu)
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as? AdapterContextMenuInfo
        val position = info?.position.guard { return super.onContextItemSelected(item) }
        val selectedNote = adapter.currentList.getOrNull(position)

        if (selectedNote != null) {
            val itemId = item.itemId
            if (itemId == R.id.action_pin) {
                val noteTitle = selectedNote.tieuDe ?: ""
                showPinNotification(noteTitle)
                return true
            } else if (itemId == R.id.action_del) {
                deleteNote(selectedNote)
                return true
            }
        }

        return super.onContextItemSelected(item)
    }

    private fun deleteNote(selectedNote: Note) {
        val idSinhVien = getIdFromMaSinhVien(idSinhVienstr!!)
        AppState.getInstance().setSelectedNote(selectedNote)
        selectedNote.id?.let {
            viewModel.deleteNote(it)
        }
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
