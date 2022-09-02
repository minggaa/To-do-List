package ch.makery.address

import ch.makery.address.util.MenuInitialisation._
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.{Button}
import scalafx.scene.layout.{AnchorPane, HBox, VBox}
import scalafx.scene.control.{Label, MenuButton, MenuItem, DatePicker, Button, CheckBox}
import scalafxml.core.macros.sfxml
import scalafx.event.ActionEvent
import scalafx.Includes._


@sfxml
class InstructionsPageController(

    private val instructTaskCheck:      CheckBox,
    private val instructTaskCheckVisual:CheckBox,
    private val instructAEPriorityMenu: MenuButton,
    private val instructDPriorityMenu:  MenuButton,
    private val instructEditButton:     Button,
    private val instructTopBar:         HBox,
    private val instructTaskDetails:    VBox

) {

/** Task Detail availability for the Task Checking **/
    private def instructTaskDetailDisable(x: Boolean) {
        instructTopBar.setDisable(x)
        instructTaskDetails.setDisable(x)
    }

/** Button to let user return back to the Tasks page. **/
    def handleBack() {
        if (Main.onPage) {
            new Alert(Alert.AlertType.Error) {
                initOwner(Main.stage)
                title = "Error!"
                headerText = "Already on Tasks page"
                contentText = "Enter a different page to perform this action."
                }.showAndWait()
        } else {
            Main.showTaskPage()
        }
    }

/** A pop-up dialog to provide user with details on what the instructions page is about. **/
    def handleInstruction() {
        new Alert(Alert.AlertType.Information){
            initOwner(Main.stage)
            title       = "Instruction Information"
            headerText  = "Welcome to To-Do's"
            contentText = "This is an instruction page to provide you with a manual of what to do, and what you can do in the app.\n\nNavigate along the tabs below to view the instructions."
        }.showAndWait()
    }

/** Made to provide a visual representation of how the Task Checking works. **/
    def handleInstructCheck() {
        if (instructTaskCheck.isSelected()) {
            instructTaskCheckVisual.setSelected(true)
            instructTaskDetailDisable(true)
        } else {
            instructTaskDetailDisable(false)
            instructTaskCheckVisual.setSelected(false)
        }
    }
    
/** To initialize the MenuItems for the Prioirity menu as a sample. **/
    LoadMenuItems(instructAEPriorityMenu).loadPriorityMenu
    LoadMenuItems(instructDPriorityMenu).loadPriorityMenu
}