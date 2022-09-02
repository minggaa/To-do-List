package ch.makery.address.util

import scalafxml.core.macros.sfxml
import scalafx.scene.control.{MenuButton, MenuItem}
import scalafx.event.ActionEvent
import scalafx.Includes._


object MenuInitialisation {

    implicit class LoadMenuItems(menuButton: MenuButton) {
        
        /** Initiallizing the priority MenuButton Items and Text **/
        def loadPriorityMenu() {
            val priority1 = new MenuItem("I")
            val priority2 = new MenuItem("II")
            val priority3 = new MenuItem("III")
            val priority4 = new MenuItem("IV")
            val priority5 = new MenuItem("none")
            
            menuButton.setText("Priority")
            menuButton.items = List(priority1, priority2, priority3, priority4, priority5)

            priority1.onAction = (event: ActionEvent) => {
                menuButton.setText(priority1.getText())
            }
            priority2.onAction = (event: ActionEvent) => {
                menuButton.setText(priority2.getText())
            }
            priority3.onAction = (event: ActionEvent) => {
                menuButton.setText(priority3.getText())
            }
            priority4.onAction = (event: ActionEvent) => {
                menuButton.setText(priority4.getText())
            }
            priority5.onAction = (event: ActionEvent) => {
                menuButton.setText(priority5.getText())
            }
        }
    }
}