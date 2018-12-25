package net.minecraftirc.mchtool

import tornadofx.App
import tornadofx.launch

var pasteFinal: String = ""

class MainApp : App(MainView::class)

fun main(args: Array<String>) {
    launch<MainApp>(args)
}
