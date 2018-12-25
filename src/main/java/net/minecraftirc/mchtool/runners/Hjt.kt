package net.minecraftirc.mchtool.runners

import java.io.File
import java.net.URL
import java.nio.charset.Charset

class Hjt : Runner<HjtResult> {
    init {
        getElevateExe()
        getHjtExe()
    }

    override fun execute(): HjtResult {
        val process = ProcessBuilder(
                "Elevate.exe",
                "-wait",
                "hjt.exe",
                "/accepteula /silentautolog /saveLog hjt.log /area+Process /area+Modules /area+Environment /area+Additional /md5"
        ).start().waitFor()

        val contentHJT = File("HiJackThis.log").readText(Charset.forName("UTF-16"))
        return HjtResult(paste(contentHJT))
    }

    override fun cleanup() {
        File("Elevate.exe").delete()
        File("HiJackThis.log").delete()
        File("hjt.exe").delete()
    }

    private fun getElevateExe() {
        val exe = Thread.currentThread().contextClassLoader.getResourceAsStream("Elevate.exe")
        File("Elevate.exe").writeBytes(exe.readBytes())
    }

    private fun getHjtExe() {
        val bytes = URL("https://github.com/dragokas/hijackthis/raw/devel/binary/HiJackThis.exe").readBytes()
        File("hjt.exe").writeBytes(bytes)
    }

}

data class HjtResult(val pasteLink: String)
