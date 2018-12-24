package net.minecraftirc.mchtool

import javafx.scene.Parent
import javafx.scene.control.TextField
import javafx.scene.layout.VBox
import tornadofx.*
import java.net.URL
import com.sun.xml.internal.ws.streaming.XMLStreamWriterUtil.getOutputStream
import com.sun.org.apache.xerces.internal.impl.XMLEntityManager.setInstanceFollowRedirects
import javafx.scene.control.Alert
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import java.io.DataOutputStream
import java.io.File
import java.net.HttpURLConnection
import java.nio.charset.Charset
import kotlin.system.exitProcess


fun main(args: Array<String>) {
    launch<MainApp>(args)
}
var pasteFinal: String = ""
var usernameField: TextField by singleAssign()

class MainView: View("MinecraftHelp tool") {

    override val root = vbox {
        label("Fill your username and click Start scan")
        label("Give your helper the information shown at the end")
        label("")
        hbox {
            label("Username: ")
            usernameField = textfield()
        }
        button("Start scan") {
            action {
                replaceWith(ExecutingView::class)
            }
        }
    }

}

private fun runDiagnostics(): String {
    val exe = Thread.currentThread().getContextClassLoader().getResourceAsStream("Elevate.exe")
    File("Elevate.exe").writeBytes(exe.readBytes())
    val bytes = URL("https://github.com/dragokas/hijackthis/raw/devel/binary/HiJackThis.exe").readBytes()
    File("hjt.exe").writeBytes(bytes)
    ProcessBuilder("dxdiag", "/t", "dxdiag.txt").start().waitFor()
    val process = ProcessBuilder("Elevate.exe", "-wait", "hjt.exe",  "/accepteula /silentautolog /saveLog hjt.log /area+Process /area+Modules /area+Environment /area+Additional /md5").start()
    process.waitFor()
    println(String(process.inputStream.readBytes()))
    val poster = if(usernameField.text.isNullOrBlank()) {
        "MinecrafthelpTool"
    } else {
        usernameField.text
    }
    val contentDxdiag = File("dxdiag.txt").readText(Charset.forName("UTF-8")).urlEncoded
    val contentHJT = File("HiJackThis.log").readText(Charset.forName("UTF-16")).urlEncoded
    val pasteDxdiag = doPost("content=$contentDxdiag&poster=$poster&syntax=text")
    val pasteHjt = doPost("content=$contentHJT&poster=$poster&syntax=text")
    File("Elevate.exe").deleteOnExit()
    File("HiJackThis.log").deleteOnExit()
    File("dxdiag.txt").deleteOnExit()
    File("hjt.exe").deleteOnExit()
    return "Dxdiag: $pasteDxdiag, HJT: $pasteHjt"
}

private fun doPost(urlParameters: String): String {
    val postData = urlParameters.toByteArray(Charset.forName("UTF-8"))
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

class ExecutingView: View() {

    override val root = vbox {
        runAsync {
            runDiagnostics()
        } ui {
            pasteFinal = it
            replaceWith(DoneView::class)
        }
        label("Running HJT + Dxdiag")
        label("Please be patient")
        progressbar()
    }
}

class DoneView: View() {
    override val root = vbox {
        label("Give the following URL to your helper")
        button("Click me to copy URL to clipboard") {
            action {
                Clipboard
                        .getSystemClipboard()
                        .setContent(ClipboardContent().apply { putString(pasteFinal) })
                alert(Alert.AlertType.INFORMATION, "Copied", "Copied to clipboard, paste it in the chat!")
            }
        }
    }
}

class MainApp: App(MainView::class)
