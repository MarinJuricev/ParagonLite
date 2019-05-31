package com.example.domain.usecase.print

import java.io.UnsupportedEncodingException

object ESCPrinterCommand {

    const val CHAR_ENCODING = "UTC-8"

    /**
     * Printer initialization
     * @return
     */
    fun POS_Set_PrtInit(): ByteArray {

        return byteArraysToBytes(arrayOf(Command.ESC_Init))
    }

    /**
     * Print and wrap
     * @return
     */
    fun POS_Set_LF(): ByteArray {

        return byteArraysToBytes(arrayOf(Command.LF))
    }

    /**
     * Print and take paper (0~255)
     * @param feed
     * @return
     */
    fun POS_Set_PrtAndFeedPaper(feed: Int): ByteArray? {
        if ((feed > 255) or (feed < 0))
            return null

        Command.ESC_J[2] = feed.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_J))
    }

    /**
     * Print a self-test page
     * @return
     */
    fun POS_Set_PrtSelfTest(): ByteArray {

        return byteArraysToBytes(arrayOf(Command.US_vt_eot))
    }

    /**
     * Beep command
     * @param m Beep times
     * @param t  Time of each beep
     * @return
     */
    fun POS_Set_Beep(m: Int, t: Int): ByteArray? {

        if ((m < 1 || m > 9) or (t < 1 || t > 9))
            return null

        Command.ESC_B_m_n[2] = m.toByte()
        Command.ESC_B_m_n[3] = t.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_B_m_n))
    }

    /**
     * Cutter instruction( Take paper to the cutter position and cut the paper )
     * @param cut  0~255
     * @return
     */
    fun POS_Set_Cut(cut: Int): ByteArray? {
        if ((cut > 255) or (cut < 0))
            return null

        Command.GS_V_m_n[3] = cut.toByte()
        return byteArraysToBytes(arrayOf(Command.GS_V_m_n))
    }

    /**
     * Cash box instruction
     * @param nMode
     * @param nTime1
     * @param nTime2
     * @return
     */
    fun POS_Set_Cashbox(nMode: Int, nTime1: Int, nTime2: Int): ByteArray? {

        if ((nMode < 0 || nMode > 1) or (nTime1 < 0) or (nTime1 > 255) or (nTime2 < 0) or (nTime2 > 255))
            return null
        Command.ESC_p[2] = nMode.toByte()
        Command.ESC_p[3] = nTime1.toByte()
        Command.ESC_p[4] = nTime2.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_p))
    }

    /**
     * Set absolute print position
     * @param absolute
     * @return
     */
    fun POS_Set_Absolute(absolute: Int): ByteArray? {
        if ((absolute > 65535) or (absolute < 0))
            return null

        Command.ESC_Relative[2] = (absolute % 0x100).toByte()
        Command.ESC_Relative[3] = (absolute / 0x100).toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_Relative))
    }

    /**
     * Set relative print position
     * @param relative
     * @return
     */
    fun POS_Set_Relative(relative: Int): ByteArray? {
        if ((relative < 0) or (relative > 65535))
            return null

        Command.ESC_Absolute[2] = (relative % 0x100).toByte()
        Command.ESC_Absolute[3] = (relative / 0x100).toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_Absolute))
    }

    /**
     * Set the left margin
     * @param left
     * @return
     */
    fun POS_Set_LeftSP(left: Int): ByteArray? {
        if ((left > 255) or (left < 0))
            return null

        Command.GS_LeftSp[2] = (left % 100).toByte()
        Command.GS_LeftSp[3] = (left / 100).toByte()

        return byteArraysToBytes(arrayOf(Command.GS_LeftSp))
    }

    /**
     * Set the alignment mode
     * @param align 0 - left, 1- middle, 2 - right
     * @return
     */
    fun POS_S_Align(align: Int): ByteArray {

        val data = Command.ESC_Align
        data[2] = align.toByte()
        return data
    }

    /**
     * Set the print area width
     * @param width
     * @return
     */
    fun POS_Set_PrintWidth(width: Int): ByteArray? {
        if ((width < 0) or (width > 255))
            return null

        Command.GS_W[2] = (width % 100).toByte()
        Command.GS_W[3] = (width / 100).toByte()

        return byteArraysToBytes(arrayOf(Command.GS_W))
    }

    /**
     * Set default line spacing
     * @return
     */
    fun POS_Set_DefLineSpace(): ByteArray {

        return Command.ESC_Two
    }

    /**
     * Set line spacing
     * @param space
     * @return
     */
    fun POS_Set_LineSpace(space: Int): ByteArray? {
        if ((space < 0) or (space > 255))
            return null

        Command.ESC_Three[2] = space.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_Three))
    }

    /**
     * Select character code page
     * @param page
     * @return
     */
    fun POS_Set_CodePage(page: Int): ByteArray? {
        if (page > 255)
            return null

        Command.ESC_t[2] = page.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_t))
    }

    /**
     * Print text document
     * @param pszString     The string to be printed
     * @param encoding    Print character corresponding code
     * @param codepage      Set up code page(0--255)
     * @param nWidthTimes   Double width(0--4)
     * @param nHeightTimes  Double height(0--4)
     * @param nFontType     Font type(Only for Ascii Code valid)(0,1 48,49)
     */
    fun POS_Print_Text(
        pszString: String?, encoding: String, codepage: Int,
        nWidthTimes: Int, nHeightTimes: Int, nFontType: Int
    ): ByteArray? {

        if (!(codepage in 0..255 && pszString != null && "" != pszString && pszString.isNotEmpty())) {
            return null
        }

        var pbString: ByteArray? = null
        try {
            pbString = pszString.toByteArray()
        } catch (e: UnsupportedEncodingException) {
            return null
        }

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30)
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03)
        Command.GS_ExclamationMark[2] = (intToWidth[nWidthTimes] + intToHeight[nHeightTimes]).toByte()

        Command.ESC_t[2] = codepage.toByte()

        Command.ESC_M[2] = nFontType.toByte()

        return if (codepage == 0) {
            byteArraysToBytes(
                arrayOf(
                    Command.GS_ExclamationMark,
                    Command.ESC_t,
                    Command.FS_and,
                    Command.ESC_M,
                    pbString
                )
            )
        } else {
            byteArraysToBytes(
                arrayOf(
                    Command.GS_ExclamationMark,
                    Command.ESC_t,
                    Command.FS_dot,
                    Command.ESC_M,
                    pbString
                )
            )
        }
    }

    /**
     * Bold instruction( The lowest bit is 1 )
     * @param bold
     * @return
     */
    fun POS_Set_Bold(bold: Int): ByteArray {

        Command.ESC_E[2] = bold.toByte()
        Command.ESC_G[2] = bold.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_E, Command.ESC_G))
    }

    /**
     * Set inverted print mode( Valid when the lowest bit is 1. )
     * @param brace
     * @return
     */
    fun POS_Set_LeftBrace(brace: Int): ByteArray {

        Command.ESC_LeftBrace[2] = brace.toByte()
        return byteArraysToBytes(arrayOf(Command.ESC_LeftBrace))
    }

    /**
     * Set underline
     * @param line
     * @return
     */
    fun POS_Set_UnderLine(line: Int): ByteArray? {

        if (line < 0 || line > 2)
            return null

        Command.ESC_Minus[2] = line.toByte()
        Command.FS_Minus[2] = line.toByte()

        return byteArraysToBytes(arrayOf(Command.ESC_Minus, Command.FS_Minus))
    }

    /**
     * Select font size( Double height )
     * @param size
     * @return
     */
    fun POS_Set_FontSize(size1: Int, size2: Int): ByteArray? {
        if ((size1 < 0) or (size1 > 7) or (size2 < 0) or (size2 > 7))
            return null

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30, 0x40, 0x50, 0x60, 0x70)
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07)
        Command.GS_ExclamationMark[2] = (intToWidth[size1] + intToHeight[size2]).toByte()
        return byteArraysToBytes(arrayOf(Command.GS_ExclamationMark))
    }

    /**
     * Set reverse printing
     * @param inverse
     * @return
     */
    fun POS_Set_Inverse(inverse: Int): ByteArray {

        Command.GS_B[2] = inverse.toByte()

        return byteArraysToBytes(arrayOf(Command.GS_B))
    }

    /**
     * Set rotation 90 degree printing
     * @param rotate
     * @return
     */
    fun POS_Set_Rotate(rotate: Int): ByteArray? {
        if (rotate < 0 || rotate > 1)
            return null
        Command.ESC_V[2] = rotate.toByte()
        return byteArraysToBytes(arrayOf(Command.ESC_V))
    }

    /**
     * Select font font
     * @param font
     * @return
     */
    fun POS_Set_ChoseFont(font: Int): ByteArray? {
        if ((font > 1) or (font < 0))
            return null

        Command.ESC_M[2] = font.toByte()
        return byteArraysToBytes(arrayOf(Command.ESC_M))

    }

    //***********************************The following function is a public function***********************************************************//
    /**
     * QR code print function
     * @param str                     打印二维码数据
     * @param nVersion                      二维码类型
     * @param nErrorCorrectionLevel   纠错级别
     * @param nMagnification          放大倍数
     * @return
     */
    fun getBarCommand(
        str: String, nVersion: Int, nErrorCorrectionLevel: Int,
        nMagnification: Int
    ): ByteArray? {

        if ((nVersion < 0) or (nVersion > 19) or (nErrorCorrectionLevel < 0) or (nErrorCorrectionLevel > 3)
            or (nMagnification < 1) or (nMagnification > 8)
        ) {
            return null
        }

        var bCodeData: ByteArray? = null
        try {
            bCodeData = str.toByteArray(charset("GBK"))

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 7)

        command[0] = 27
        command[1] = 90
        command[2] = nVersion.toByte()
        command[3] = nErrorCorrectionLevel.toByte()
        command[4] = nMagnification.toByte()
        command[5] = (bCodeData.size and 0xff).toByte()
        command[6] = (bCodeData.size and 0xff00 shr 8).toByte()
        System.arraycopy(bCodeData, 0, command, 7, bCodeData.size)

        return command
    }

    /**
     * Print one-dimensional bar
     * @param str                    Print barcode characters
     * @param nType                    Barcode type (65~73)
     * @param nWidthX                Bar code width
     * @param nHeight                Bar code height
     * @param nHriFontType            HRI font
     * @param nHriFontPosition        HRI position
     * @return
     */
    fun getCodeBarCommand(
        str: String, nType: Int, nWidthX: Int, nHeight: Int,
        nHriFontType: Int, nHriFontPosition: Int
    ): ByteArray? {

        if ((nType < 0x41) or (nType > 0x49) or (nWidthX < 2) or (nWidthX > 6)
            or (nHeight < 1) or (nHeight > 255) or (str.length == 0)
        )
            return null

        var bCodeData: ByteArray? = null
        try {
            bCodeData = str.toByteArray(charset("GBK"))

        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(bCodeData.size + 16)

        command[0] = 29
        command[1] = 119
        command[2] = nWidthX.toByte()
        command[3] = 29
        command[4] = 104
        command[5] = nHeight.toByte()
        command[6] = 29
        command[7] = 102
        command[8] = (nHriFontType and 0x01).toByte()
        command[9] = 29
        command[10] = 72
        command[11] = (nHriFontPosition and 0x03).toByte()
        command[12] = 29
        command[13] = 107
        command[14] = nType.toByte()
        command[15] = bCodeData.size.toByte()
        System.arraycopy(bCodeData, 0, command, 16, bCodeData.size)


        return command
    }

    /**
     * Set the print mode and prints the string
     * (Select font (font:A font:B), Bold, Font height and width ( 4 times the height and width ))
     * @param str           Printed string
     * @param bold           Bold
     * @param font           Select font
     * @param widthsize    Double width
     * @param heigthsize   Double height
     * @return
     */
    fun POS_Set_Font_And_Print(str: String, bold: Int, font: Int, widthsize: Int, heigthsize: Int): ByteArray? {

        if (str.isEmpty() || widthsize < 0 || widthsize > 4 || heigthsize < 0 || heigthsize > 4
            || font < 0 || font > 1
        )
            return null

        var strData: ByteArray? = null
        try {
            strData = str.toByteArray()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
            return null
        }

        val command = ByteArray(strData!!.size + 9)

        val intToWidth = byteArrayOf(0x00, 0x10, 0x20, 0x30)//最大四倍宽
        val intToHeight = byteArrayOf(0x00, 0x01, 0x02, 0x03)//最大四倍高

        command[0] = 27
        command[1] = 69
        command[2] = bold.toByte()
        command[3] = 27
        command[4] = 77
        command[5] = font.toByte()
        command[6] = 29
        command[7] = 33
        command[8] = (intToWidth[widthsize] + intToHeight[heigthsize]).toByte()

        System.arraycopy(strData, 0, command, 9, strData.size)
        return command
    }

    fun byteArraysToBytes(data: Array<ByteArray>): ByteArray {
        var length = 0

        for (i in data.indices) {
            length += data[i].size
        }

        val send = ByteArray(length)
        var k = 0

        for (i in data.indices) {
            for (j in 0 until data[i].size) {
                send[k++] = data[i][j]
            }
        }

        return send
    }

}
