package com.example.domain.usecase.print

object Command {

    private const val ESC: Byte = 0x1B
    private const val FS: Byte = 0x1C
    private const val GS: Byte = 0x1D
    private const val US: Byte = 0x1F
    private const val DLE: Byte = 0x10
    private const val DC4: Byte = 0x14
    private const val DC1: Byte = 0x11
    private const val SP: Byte = 0x20
    private const val NL: Byte = 0x0A
    private const val FF: Byte = 0x0C

    val PIECE = 0xFF.toByte()
    val NUL = 0x00.toByte()

    // Printer initialization
    var ESC_Init = byteArrayOf(ESC, '@'.toByte())

    /**
     * Print order
     */
    // Print and wrap
    var LF = byteArrayOf(NL)

    // Print and take paper
    var ESC_J = byteArrayOf(ESC, 'J'.toByte(), 0x00)
    var ESC_d = byteArrayOf(ESC, 'd'.toByte(), 0x00)

    // Print a self-test page
    var US_vt_eot = byteArrayOf(US, DC1, 0x04)

    // Beep command
    var ESC_B_m_n = byteArrayOf(ESC, 'B'.toByte(), 0x00, 0x00)

    // Cutter instruction
    var GS_V_n = byteArrayOf(GS, 'V'.toByte(), 0x00)
    var GS_V_m_n = byteArrayOf(GS, 'V'.toByte(), 'B'.toByte(), 0x00)
    var GS_i = byteArrayOf(ESC, 'i'.toByte())
    var GS_m = byteArrayOf(ESC, 'm'.toByte())

    /**
     * Character setting command
     */
    // Set the character right spacing
    var ESC_SP = byteArrayOf(ESC, SP, 0x00)

    // Set character print font format
    var ESC_ExclamationMark = byteArrayOf(ESC, '!'.toByte(), 0x00)

    // Set the font height and width
    var GS_ExclamationMark = byteArrayOf(GS, '!'.toByte(), 0x00)

    // Set reverse printing
    var GS_B = byteArrayOf(GS, 'B'.toByte(), 0x00)

    // Cancel / select 90 degree rotation print
    var ESC_V = byteArrayOf(ESC, 'V'.toByte(), 0x00)

    // Select font font (mainly ASCII code)
    var ESC_M = byteArrayOf(ESC, 'M'.toByte(), 0x00)

    // Select / cancel bold instructions
    var ESC_G = byteArrayOf(ESC, 'G'.toByte(), 0x00)
    var ESC_E = byteArrayOf(ESC, 'E'.toByte(), 0x00)

    // Select / cancel inverted print mode
    var ESC_LeftBrace = byteArrayOf(ESC, '{'.toByte(), 0x00)

    // Set the underline height (character)
    var ESC_Minus = byteArrayOf(ESC, 45, 0x00)

    // Character mode
    var FS_dot = byteArrayOf(FS, 46)

    // Chinese character mode
    var FS_and = byteArrayOf(FS, '&'.toByte())

    // Set Chinese character print mode
    var FS_ExclamationMark = byteArrayOf(FS, '!'.toByte(), 0x00)

    // Set the underline height (Chinese characters)
    var FS_Minus = byteArrayOf(FS, 45, 0x00)

    // Set the left and right spacing of Chinese characters
    var FS_S = byteArrayOf(FS, 'S'.toByte(), 0x00, 0x00)

    // Select character code page
    var ESC_t = byteArrayOf(ESC, 't'.toByte(), 0x00)

    /**
     * Formatting instruction
     */
    // Set default line spacing
    var ESC_Two = byteArrayOf(ESC, 50)

    // Set line spacing
    var ESC_Three = byteArrayOf(ESC, 51, 0x00)

    //Set the alignment mode
    var ESC_Align = byteArrayOf(ESC, 'a'.toByte(), 0x00)

    //Set the left margin
    var GS_LeftSp = byteArrayOf(GS, 'L'.toByte(), 0x00, 0x00)

    // Set absolute print position
    // Set the current position to the beginning of the line (nL + nH x 256)
    // This command is ignored if the set position is outside the specified print area
    var ESC_Relative = byteArrayOf(ESC, '$'.toByte(), 0x00, 0x00)

    // Set relative print position
    var ESC_Absolute = byteArrayOf(ESC, 92, 0x00, 0x00)

    // Set the print area width
    var GS_W = byteArrayOf(GS, 'W'.toByte(), 0x00, 0x00)

    /**
     * Status instruction
     */
    // Real-time status transfer instruction
    var DLE_eot = byteArrayOf(DLE, 0x04, 0x00)

    // Real-time cash box instruction
    var DLE_DC4 = byteArrayOf(DLE, DC4, 0x00, 0x00, 0x00)

    // Standard cash box instruction
    var ESC_p = byteArrayOf(ESC, 'F'.toByte(), 0x00, 0x00, 0x00)

    /**
     * Bar code setting instruction
     */
    // Select HRI printing method
    var GS_H = byteArrayOf(GS, 'H'.toByte(), 0x00)

    // Set the bar code height
    var GS_h = byteArrayOf(GS, 'h'.toByte(), 0xa2.toByte())

    // Set the barcode width
    var GS_w = byteArrayOf(GS, 'w'.toByte(), 0x00)

    // Set HRI character font font
    var GS_f = byteArrayOf(GS, 'f'.toByte(), 0x00)

    // Bar code left offset instruction
    var GS_x = byteArrayOf(GS, 'x'.toByte(), 0x00)

    // Print barcode command
    var GS_k = byteArrayOf(GS, 'k'.toByte(), 'A'.toByte(), FF)

    // QR code related instructions
    var GS_k_m_v_r_nL_nH = byteArrayOf(ESC, 'Z'.toByte(), 0x03, 0x03, 0x08, 0x00, 0x00)

}
