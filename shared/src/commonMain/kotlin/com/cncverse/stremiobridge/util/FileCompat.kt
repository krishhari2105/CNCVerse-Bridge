package com.cncverse.stremiobridge.util

import java.io.File
import java.io.IOException
import java.io.InputStream

/**
 * API-21-safe file helpers. `File.toPath()` / `java.nio.file.Files` need Android API 26+,
 * so they must not be used from common code (crashes with NoSuchMethodError on Android 7.1 and below).
 */

/** Moves [src] over [dest], replacing it. Uses rename, falling back to copy + delete. */
fun moveReplacing(src: File, dest: File) {
    if (src.renameTo(dest)) return
    // renameTo can fail on Android if dest exists on some filesystems, or across mount points
    if (dest.exists() && !dest.delete()) throw IOException("Cannot replace ${dest.path}")
    if (src.renameTo(dest)) return
    src.copyTo(dest, overwrite = true)
    src.delete()
}

/** Copies [input] into [dest], replacing any existing file. Closes nothing; caller owns [input]. */
fun copyReplacing(input: InputStream, dest: File) {
    dest.outputStream().use { out -> input.copyTo(out) }
}
