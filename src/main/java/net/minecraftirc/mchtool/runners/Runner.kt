package net.minecraftirc.mchtool.runners

import tornadofx.urlEncoded
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

interface Runner<out T> {
    fun execute(): T
    fun cleanup()

}

fun paste(content: String): String {
    val postData = "content=${content.urlEncoded}&poster=minecrafthelpbot&syntax=text"
            .toByteArray(Charset.forName("UTF-8"))
    val postDataLength = postData.size

    val pasteConnection = URL("http://paste.ubuntu.com").openConnection() as HttpURLConnection
    pasteConnection.requestMethod = "POST"
    pasteConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
    pasteConnection.setRequestProperty("Charset", "UTF-8")
    pasteConnection.setRequestProperty("Content-Length", Integer.toString(postDataLength))
    pasteConnection.doOutput = true
    pasteConnection.instanceFollowRedirects = false
    pasteConnection.connect()
    pasteConnection.outputStream.write(postData)
    val ret = pasteConnection.getHeaderField("Location")
    pasteConnection.disconnect()
    return ret
}
