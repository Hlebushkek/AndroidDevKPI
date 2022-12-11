package com.example.lab1

import android.graphics.fonts.SystemFonts

class FontsManager {

    val availableFonts = arrayListOf<String>()
    val availableFontsName = arrayListOf<String>()

    init {
        val fontFolderPath = "/system/fonts/"
        val fontExt = arrayOf(".ttf", ".otf", ".ttc")
        SystemFonts.getAvailableFonts().forEach { font ->
            val fontPath = font.file?.path.toString()
            availableFonts.add(fontPath)

            var fontName = fontPath.removePrefix(fontFolderPath)
            fontExt.forEach { ext ->
                fontName = fontName.removeSuffix(ext)
            }
            availableFontsName.add(fontName)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FontsManager()
    }
}