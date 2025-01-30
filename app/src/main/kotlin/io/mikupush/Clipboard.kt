package io.mikupush

import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

fun copyToClipboard(value: String) {
    val clipboard = Toolkit.getDefaultToolkit().systemClipboard
    val selection = StringSelection(value)
    clipboard.setContents(selection, selection)
}