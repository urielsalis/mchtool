package net.minecraftirc.mchtool

import javafx.scene.control.Alert
import javafx.scene.input.Clipboard
import javafx.scene.input.ClipboardContent
import tornadofx.*

class DoneView : View() {
    override val root = vbox {
        label("Give the following URL to your helper")
        button("Click me to copy URL to clipboard") {
            action {
                Clipboard.getSystemClipboard()
                        .setContent(ClipboardContent().apply { putString(pasteFinal) })
                alert(Alert.AlertType.INFORMATION, "Copied", "Copied to clipboard, paste it in the chat!")
            }
        }
    }
}