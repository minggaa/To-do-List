package ch.makery.address

import ch.makery.address.model.Tasks
import ch.makery.address.util.DateFormat._
import java.util.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scalafxml.core.macros.sfxml
import scalafx.scene.image.Image
import scalafx.scene.layout.{AnchorPane, HBox, VBox}
import scalafx.event.ActionEvent
import scalafx.scene.control.Alert.AlertType
import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.scene.control.{TableView, TableColumn, Label, MenuButton, MenuItem, DatePicker, Button, CheckBox, Alert, ScrollPane}
import scalafx.Includes._


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
@sfxml
class TasksPageController(

    private val taskTable:        TableView[Tasks],

    private val priorityColumn:   TableColumn[Tasks, String], 
    private val titleColumn:      TableColumn[Tasks, String],
    private val dueColumn:        TableColumn[Tasks, String],

    private val titleLabel:       Label,
    private val descriptionLabel: Label,
    private val dueDateDisplay:   Label,
    private val priorityMenu:     Label,

    private val taskCheck:        CheckBox,
    private val editButton:       Button,
    private val removeButton:     Button,
    private val popUpLabel:       Label,
    private val changesBar:       HBox,
    private val taskInfo:         VBox

    ) {

/** To initialize the display of contents for TableView. **/
    //taskTable.items = Main.tasksData
    taskTable.items = Tasks.tasksData

/** To initialize column's cell values **/
    priorityColumn.cellValueFactory = {_.value.priority}
    titleColumn.cellValueFactory = {_.value.title}
    dueColumn.cellValueFactory = {_.value.due.asString}

/** To set the visibility of details when selected/unselected. **/
    private def detailsVisibility(x: Boolean) {
        taskCheck.setVisible(x)
        editButton.setVisible(x)
        removeButton.setVisible(x)
        changesBar.setVisible(x)
        taskInfo.setVisible(x)
    }

    private def disableDetails(x: Boolean) {
        changesBar.setDisable(x)
        taskInfo.setDisable(x)
    }

    private def showTasksDetails (task: Option[Tasks]) = {
        taskCheck.setDisable(true)
        task match {
            case Some(task) =>
            /** Display task details if task is selected. **/
            detailsVisibility(true)
            popUpLabel.setVisible(false)

            /** Fill labels with info from the Tasks object. **/
                titleLabel.text         <== task.title
                descriptionLabel.text   <== task.description
                dueDateDisplay.text     <== task.due.asString
                priorityMenu.text       <== task.priority
                taskCheck.selected      <== task.checked
                isChecked()

            case None =>
            /** Display empty pane if no tasks selected. **/
            detailsVisibility(false)
            popUpLabel.setVisible(true)

            /** If no Task is selected, remove all info. **/            
                titleLabel.text.unbind()
                descriptionLabel.text.unbind()
                dueDateDisplay.text.unbind()
                priorityMenu.text.unbind()
                taskCheck.selected.unbind()

                titleLabel.text             = ""
                descriptionLabel.text       = ""
                dueDateDisplay.text         = ""
                priorityMenu.text           = ""
        }
    }
    showTasksDetails(None)

/** Allow the details panel to change based on the cell selected. **/
    taskTable.selectionModel().selectedItem.onChange(
        (_, _, newValue) => showTasksDetails(Option(newValue))
    )

/** Handlers to perfrom different actions/functions. **/
    def handleAddTask(action: ActionEvent) = {
        val tasks = new Tasks("","","")
        val okClicked = Main.showModifyTaskDialog(tasks, false)
        
        if (okClicked) {
            tasks.save()
            Tasks.tasksData += tasks
        }
    }

    def handleModifyTask(action: ActionEvent) = {
        val selectedTask = taskTable.selectionModel().selectedItem.value

        if (selectedTask != null) {
            val okClicked = Main.showModifyTaskDialog(selectedTask, true)
            if (okClicked) {
                selectedTask.save()
                showTasksDetails(Some(selectedTask))
            }
        } else {
            // If nothing selected
            val alert = new Alert(AlertType.Warning){
            initOwner(Main.stage)
            title       = "Error!"
            headerText  = "No task selected."
            contentText = "Please select a task from the table."
            }.showAndWait()
        }
    }

    def handleRemoveTask(action: ActionEvent) = {
        val selectedIndex = taskTable.selectionModel().selectedIndex.value

        if (selectedIndex >= 0) {
            Tasks.tasksData(selectedIndex).delete();
            taskTable.items().remove(selectedIndex);
        } else {
            // If nothing selected
            val alert = new Alert(AlertType.Warning){
            initOwner(Main.stage)
            title       = "Error!"
            headerText  = "No task selected."
            contentText = "Please select a task from the table."
            }.showAndWait()
        }
    }

/** To obtain the state of whether if the Task is checked or not. **/
    def isChecked() {
        if (taskCheck.isSelected()) {
            return disableDetails(true)
        } else {
            return disableDetails(false)
        }
    }

/** Handles the instruction page button. **/
    def handleInstruction(action: ActionEvent) {
        Main.showInstructionsPage()
    }
}