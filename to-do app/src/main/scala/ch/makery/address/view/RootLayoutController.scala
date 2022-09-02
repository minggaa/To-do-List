package ch.makery.address

import ch.makery.address.model.Tasks
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ButtonType
import scala.util.{ Try, Success, Failure }


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
@sfxml
class RootLayoutController() {
    val error = "Error! Action failed."

    def editAttemptAlert() {
        val editAlert = new Alert(Alert.AlertType.Error){
                initOwner(Main.stage)
                title = "Error!"
                headerText = "Not on Tasks page."
                contentText = "This action can only be performed in the Tasks page\n\nDo you wish to return to the Tasks page?\n"
                buttonTypes = Seq(ButtonType.OK, ButtonType.Cancel)
            }
        
        val returnChoice = editAlert.showAndWait()
            returnChoice match {
                case Some(ButtonType.OK) => Main.showTaskPage()
                case _ => editAlert.close()
            }
    }
    

/** File Section **/
    def handleTasksPage(action: ActionEvent) {
        if (Main.onPage) {
            new Alert(Alert.AlertType.Error) {
                initOwner(Main.stage)
                title = "Error!"
                headerText = "Already on Tasks page"
                contentText = "Enter a different page to perform this action.\n"
                }.showAndWait()
        } else {
            Main.showTaskPage()
        }
    }

    def handleClose() {
        System.exit(0)
    }
    
/** Edit Section **/
    def handleAdd(action: ActionEvent) {
        if (Main.onPage) {
            Main.ctrlOp match {
                case Some(ctrl) => ctrl.handleAddTask(action)
                case None       => throw new Exception(error)
                case _          => editAttemptAlert()
            }
        } else {
            editAttemptAlert()
        }
    }
    
    def handleModify(action: ActionEvent) {
        if (Main.onPage) {
            Main.ctrlOp match {
                case Some(ctrl) => ctrl.handleModifyTask(action)
                case None       => throw new Exception(error)
                case _          => editAttemptAlert()
            }
        } else {
            editAttemptAlert()
        }
    }

    def handleDelete(action: ActionEvent) {
        if (Main.onPage) {
            Main.ctrlOp match {
                case Some(ctrl) => ctrl.handleRemoveTask(action)
                case None       => throw new Exception(error)
                case _          => editAttemptAlert()
            }
        } else {
            editAttemptAlert()
        }
    }

/** Help Section **/
    def handleInstruction(action: ActionEvent) {
        if (Main.onPage) {
            Main.showInstructionsPage()
        } else {
            new Alert(Alert.AlertType.Error){
                initOwner(Main.stage)
                title = "Error!"
                headerText = "Already on Instructions page"
                contentText = "Enter a different page to perform this action.\n"
                }.showAndWait()
        }
    }

    def handleAbout(action: ActionEvent) {
        new Alert(Alert.AlertType.Information){
            initOwner(Main.stage)
            title       = "About"
            headerText  = "To-Do's"
            contentText = "Version: 1.0\n\nA simple and minimal To-Do list for users to keep track of their tasks."
        }.showAndWait()
    }

}