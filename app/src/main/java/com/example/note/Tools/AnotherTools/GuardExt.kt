package com.example.note.Tools.AnotherTools

inline fun <T> T?.guard(closure: () -> Nothing): T {
    if (this == null) closure()
    return this
}