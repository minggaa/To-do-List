package ch.makery.address

import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.image.Image
import scalafx.scene.Scene
import scalafx.Includes._
import ch.makery.address.view._
import ch.makery.address.model.Tasks
import ch.makery.address.util.Database
import ch.makery.address.util.DateFormat._
import scalafx.stage.{Stage, Modality}
//import scalafx.collections.ObservableBuffer
import scalafxml.core.{NoDependencyResolver, FXMLView, FXMLLoader}
import javafx.{scene => jfxs}


/**
* This part of the code was worked on and referred to online, as well as from Dr. Chin's Lab code.
**/
object Main extends JFXApp {

    //val tasksData = new ObservableBuffer[Tasks]()

    //tasksData += new Tasks("I", "Do work", "21/Feb/2022")
    //tasksData += new Tasks("II", "Do dishes", "30/Jan/2022")

/** For loading in the RootLayout & FXML files. **/
    val rootResource = this.getClass.getResourceAsStream("view/RootLayout.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(rootResource);
    val roots = loader.getRoot[jfxs.layout.BorderPane]

/** Initialling the stage. **/
    stage = new PrimaryStage {
        title = "To-Do's"
        icons += new Image(getClass.getResourceAsStream("images/to-do's-icon.png"))
        scene = new Scene {
            root = roots
            stylesheets += getClass.getResource("style/style.css").toString()
        }
    }

/** Show functions to display app pages. **/
    Database.setupDB()
    Tasks.tasksData ++= Tasks.getAllTasks

    var ctrlOp: Option[TasksPageController#Controller] = None
    var onPage: Boolean = false
    var showOption: Boolean = false

    def showTaskPage() = {
        onPage = true

        val resource = getClass.getResourceAsStream("view/TasksPage.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)

        loader.load(resource);
        val roots = loader.getRoot[jfxs.layout.AnchorPane]
        ctrlOp = Option(loader.getController[TasksPageController#Controller])
        this.roots.setCenter(roots)
    }

    def showInstructionsPage() = {
        onPage = false

        val resource = getClass.getResourceAsStream("view/InstructionsPage.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)

        loader.load(resource);
        val roots = loader.getRoot[jfxs.layout.AnchorPane]
        this.roots.setCenter(roots)
    }

    def showModifyTaskDialog(tasks: Tasks, x: Boolean): Boolean = {
        val resource = getClass.getResourceAsStream("view/ModifyTaskDialog.fxml")
        val loader = new FXMLLoader(null, NoDependencyResolver)
        showOption = x

        loader.load(resource);
        val roots2  = loader.getRoot[jfxs.Parent]
        val control = loader.getController[ModifyTaskDialogController#Controller]

        val dialog = new Stage() {
            initModality(Modality.ApplicationModal)
            initOwner(stage)
            scene = new Scene {
                root = roots2
                stylesheets = List(getClass.getResource("style/style.css").toString)
            }
        }
        control.dialogStage = dialog
        control.task = tasks
        dialog.showAndWait()
        control.okClicked
    }

/** Calling show function to display TaskPage on start. **/
    showTaskPage()
}