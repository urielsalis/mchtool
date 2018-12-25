package net.minecraftirc.mchtool

import net.minecraftirc.mchtool.runners.Dxdiag
import net.minecraftirc.mchtool.runners.Hjt
import tornadofx.View
import tornadofx.label
import tornadofx.progressbar
import tornadofx.vbox

class ExecutingView : View() {

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

private fun runDiagnostics(): String {
    val dxdiag = Dxdiag()
    val hjt = Hjt()
    val dxdiagResult = dxdiag.execute().pasteLink
    val hjtResult = hjt.execute().pasteLink
    dxdiag.cleanup()
    hjt.cleanup()
    return "Dxdiag: $dxdiagResult, HJT: $hjtResult"
}
