package com.example.note.UI.home.home

import androidx.fragment.app.Fragment
import com.example.note.R
import com.example.note.UI.Calendar.CalendarFragment
import com.example.note.UI.School.SchoolFragment
import com.example.note.UI.settings.SettingsFragment

enum class NavMenuType {
    HOME,
    SCHOOL,
    CALENDAR,
    SETTINGS;

    fun getMenuId(): Int {
        return when (this) {
            HOME -> R.id.home
            SCHOOL -> R.id.school
            CALENDAR -> R.id.calendar
            SETTINGS -> R.id.setting
        }
    }

    companion object {
        fun getNavMenuType(menuId: Int): NavMenuType {
            return when (menuId) {
                R.id.home -> HOME
                R.id.school -> SCHOOL
                R.id.calendar -> CALENDAR
                R.id.setting -> SETTINGS
                else -> HOME
            }
        }

        fun getFragmentInstance(menuId: Int): Fragment {
            return getFragmentInstance(getNavMenuType(menuId))
        }

        fun getFragmentInstance(navMenuType: NavMenuType): Fragment {
            return when (navMenuType) {
                HOME -> HomeFragment()
                SCHOOL -> SchoolFragment()
                CALENDAR -> CalendarFragment()
                SETTINGS -> SettingsFragment()
            }
        }
    }
}