package com.example.towolist.ui

interface IMainActivityFragment {

    fun updateLayout(isList: Boolean)

    fun updateSpinner()

    fun search(text: CharSequence?, isUpdate: Boolean)

    fun searchClose()
}