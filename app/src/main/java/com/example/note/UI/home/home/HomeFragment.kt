package com.example.note.UI.home.home

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import com.example.note.Adapter.NoteAdapter
import com.example.note.R
import com.example.note.Tools.AnotherTools.guard
import com.example.note.Tools.SQLite.Connect
import com.example.note.Tools.SQLite.ConnectSharing
import com.example.note.Tools.dialogHelper.DialogHelper
import com.example.note.Tools.log_helper.LogHelper
import com.example.note.UI.Calendar.CalendarFragment
import com.example.note.UI.School.school.SchoolFragment
import com.example.note.UI.home.add_note.EditNoteBottomSheetFragment
import com.example.note.UI.settings.SettingsFragment
import com.example.note.base.BaseFragment
import com.example.note.base.provideViewModels
import com.example.note.data.AppState
import com.example.note.data.model.Note
import com.example.note.data.model.SinhVien.Companion.getIdFromMaSinhVien
import com.example.note.databinding.FragmentHomeBinding
import com.example.note.handler.BaseHandler
import com.example.note.handler.BaseHandlerImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(),
    NoteAdapter.NoteAdapterListener,
    EditNoteBottomSheetFragment.EditNoteListener,
    BaseHandler by BaseHandlerImpl() {

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
    }

    override fun bindViewEvents() {

        binding.topAppBar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_search -> {
                    showSearch()
                    true
                }
                R.id.action_sort_by_create -> {
                    viewModel.sortByCreate()
                    true
                }
                R.id.action_sort_by_modify -> {
                    viewModel.shortByModify()
                    true
                }
                else -> false
            }
        }


        binding.fabAddNote.setOnClickListener {
            AppState.getInstance().setSelectedNote(null)
            openEditBottomSheet(requireActivity(), this)
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

        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getNotes()
            binding.swipeRefreshLayout.isRefreshing = false
        }

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            val id = item.itemId
            replaceFragment(NavMenuType.getFragmentInstance(id))
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
                    ", '" + selectedNote.tieuDe +
                    "', '" + selectedNote.ngayTao +
                    "', '" + selectedNote.ngayCapNhat +
                    "', '" + selectedNote.noiDung +
                    "', '" + selectedNote.noiDungCua + "')"

            connect?.nonReturnQuery(query)
            val msg = if (status) getString(R.string.delete_success) else getString(R.string.delete_false)
            toaster.display(msg)
        }
    }

    override fun viewDidLoad() {
        viewModel.getNotes()
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

    private fun deleteNote(selectedNote: Note) {
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

        val channelId = noteTitle
        val channel =
            NotificationChannel(channelId, channelId, NotificationManager.IMPORTANCE_HIGH)
        notificationManager.createNotificationChannel(channel)

        val builder = NotificationCompat.Builder(requireActivity(), noteTitle)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(noteTitle)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)

        notificationManager.notify(0, builder.build())
    }

    override fun onNoteClick(note: Note) {
        AppState.getInstance().setSelectedNote(note)
        openEditBottomSheet(requireActivity(), this)
    }

    override fun onPinClick(note: Note) {
        val noteTitle = note.tieuDe ?: ""
        showPinNotification(noteTitle)
    }

    override fun onDeleteClick(note: Note) {
        DialogHelper.showCustomConfirmDialog(requireContext(),
            getString(R.string.common_confirm),
            getString(R.string.common_confirm_delete),
            getString(R.string.common_ok),
            getString(R.string.common_cancel),
            positiveAction = {
                deleteNote(note)
            }
        )
    }

    override fun onSave(note: Note) {
        LogHelper.logDebug(this.javaClass, "note to save: $note")
        viewModel.editNote(note)
    }
}
