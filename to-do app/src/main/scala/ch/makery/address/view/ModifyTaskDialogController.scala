package ch.makery.address

import ch.makery.address.model.Tasks
import ch.makery.address.util.DateFormat._
import ch.makery.address.util.MenuInitialisation._
import scalafxml.core.macros.sfxml
import scalafx.scene.layout.HBox
import scalafx.scene.control.{TextField, TextArea, Alert, TableColumn, Label, MenuButton, MenuItem, DatePicker, CheckBox}
import scalafx.event.ActionEvent
import scalafx.stage.Stage
import scalafx.Includes._


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
@sfxml
class ModifyTaskDialogController (

    private val titleField:             TextField,
    private val descriptionTextArea:    TextArea,
    private val dueDatePicker:          DatePicker,
    private val priorityMenu:           MenuButton,
    private val taskCheck:              CheckBox,
    private val completeTaskOption:     HBox,
    private val wordCount:              Label

    ) {
    
    private var _task:       Tasks   = null
    var         dialogStage: Stage   = null
    var         okClicked            = false

    def task = _task
    def task_= (x: Tasks) {
        _task = x
        
        taskCheck.selected          = _task.checked.value
        titleField.text             = _task.title.value
        descriptionTextArea.text    = _task.description.value
        dueDatePicker.value         = _task.due.value
        priorityMenu.text           = _task.priority.value

    /** Prompt text on what to input into the field. **/
        titleField.setPromptText("Enter task title")
        descriptionTextArea.setPromptText("Enter task description of less than 254 characters (optional)")
        dueDatePicker.setPromptText("Due Date (MM/DD/YYYY)")

    /** To display either priority label or priority set by user. **/
        if (priorityMenu.getText != "") {
            priorityMenu.setText(_task.priority.value)
        } else {
            priorityMenu.setText("Priority")
        }
        
    }

/** To ensure user inputs are valid before saving information. **/
    def isInputValid() : Boolean = {
        var date = ToDateFormatter(dueDatePicker.getValue).asString
        var errorMessage = ""

        if (nullChecking(titleField.text.value))
            errorMessage += "Invalid task title!\n\n"
        if (exceedLimit(titleField.text.value))
            errorMessage += "Characters entered in title field exceeded 254 characters!\n\n"
        if (nullChecking(descriptionTextArea.text.value))
            setTaskDescription(true)    // since description is optional, a text informing user is set.
        if (exceedLimit(descriptionTextArea.text.value))
            errorMessage += "Characters entered in description box exceeded 254 characters!\n\n"
        if ((nullChecking(priorityMenu.text.value)) || (priorityMenu.text.value == "Priority"))
            errorMessage += "Invalid task priority!\n\n"
        if (nullChecking(date))
            errorMessage += "Invalid task due date!\n\n"
        

        if (errorMessage.length() == 0) {
            return true;
        } else {
        /** Displaying the error message. **/
            val alert = new Alert(Alert.AlertType.Error){
            initOwner(dialogStage)
            title = "Invalid Inputs"
            headerText = "Please correct the invalid inputs."
            contentText = errorMessage
            }.showAndWait()

            return false;
        }
    }

/** To ensure characters entered into the fields do not exceed 254 characters. **/
    def exceedLimit(x: String) = x.length > 254

/** To ensure characters entered into the fields are not empty. **/
    def nullChecking(x: String) = x == null || x.length == 0

/** Auto sets an informing message into the field if user leaves it empty. **/
    def setTaskDescription(x: Boolean) {
        if (nullChecking(descriptionTextArea.text.value))
        descriptionTextArea.setText("Edit to set task description (optional)")
    }
    
/** Character count to inform user of their text count. **/
    wordCount.text <== descriptionTextArea.length.asString("Character count: %d")

/** Provides visual warning on word count if user exceeds 254 characters in TextArea. **/
    def charLimitChecking() {
        if (descriptionTextArea.length.value < 255) {
            wordCount.setStyle("-fx-text-fill: #E8E8E8")
        } else {
            wordCount.setStyle("-fx-text-fill: red")
        }
    }
    charLimitChecking()

    def handleDone(action: ActionEvent) {
        if (isInputValid()) {
            _task.checked       <== taskCheck.selected  //is false by default
            _task.title         <== titleField.text
            _task.description   <== descriptionTextArea.text
            _task.due           <== dueDatePicker.value
            _task.priority      <== priorityMenu.text

            okClicked = true;
            dialogStage.close();
        }
    }

    def handleCancel(action: ActionEvent) {
        dialogStage.close();
    }

/** Since Add Task and Modify Task shares the same dialog and controller, this function determines where the Task Check option should be displayed. **/
    def viewCompleteTaskOption(x: Boolean) {
        completeTaskOption.setVisible(x)
    }
    viewCompleteTaskOption(Main.showOption)

    //Loads the Priority Menu items
    LoadMenuItems(priorityMenu).loadPriorityMenu

}