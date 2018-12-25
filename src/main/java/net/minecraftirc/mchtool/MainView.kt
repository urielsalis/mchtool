package net.minecraftirc.mchtool

import tornadofx.*

class MainView : View("MinecraftHelp tool") {

    override val root = vbox {
        label("Click \"Start scan\" to begin")
        label("This will run some common diagnostic software and generate a report.")
        label("Please give the report to your helper when it finishes")
        label("")
        button("Start scan") {
            action {
                replaceWith(ExecutingView::class)
            }
        }
    }

}