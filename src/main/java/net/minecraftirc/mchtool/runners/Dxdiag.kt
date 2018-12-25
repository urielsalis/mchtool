package net.minecraftirc.mchtool.runners

import java.io.File
import java.nio.charset.Charset

class Dxdiag : Runner<DxdiagResult> {
    override fun execute(): DxdiagResult {
        ProcessBuilder("dxdiag", "/t", "dxdiag.txt").start().waitFor()
        val contentDxdiag = File("dxdiag.txt").readText(Charset.forName("UTF-8"))
        return DxdiagResult(paste(contentDxdiag))
    }

    override fun cleanup() {
        File("dxdiag.txt").delete()
    }

}

data class DxdiagResult(val pasteLink: String)