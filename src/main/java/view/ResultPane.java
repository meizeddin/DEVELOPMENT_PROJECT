package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * This class is to show an overview of a student details and their booked course and corresponding modules
 * class extends GridPane class from JavaFx
 * @author meize
 */
public class ResultPane extends GridPane {

	private final Button btnSave;
	private final TextArea txtFunction, txtResult, txtTruthTable;
	private final Label labelFunc, labelTruth, labelResult;


	/**
	 * a constructor method to initiate the overview pane
	 */
	public ResultPane() {

		//styling
		this.setVgap(10);
		this.setHgap(5);
		this.setPadding(new Insets(10, 10, 10, 10));
		this.setPrefSize(700, 700);

		//initialize buttons
		btnSave = new Button("Save");
		btnSave.setPrefSize(100, 50);
		btnSave.setPadding(new Insets(10, 10, 10, 10));
		btnSave.setAlignment(Pos.CENTER);
		btnSave.setMinSize(200, 75);
		
		btnSave.setStyle(WelcomingPane.idle);
		btnSave.setOnMouseEntered(e -> btnSave.setStyle(WelcomingPane.hover));
		btnSave.setOnMouseExited(e -> btnSave.setStyle(WelcomingPane.idle));

		//setup label
		labelFunc = new Label();
		labelFunc.setText("Function");
		labelFunc.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Georgia;"
				);
		//setup text area
		txtFunction = new TextArea();
		txtFunction.setEditable(false);
		txtFunction.setPadding(new Insets(10, 10, 10, 10));
		txtFunction.setMaxSize(700, 250);
		txtFunction.setPrefSize(500, 250);
		txtFunction.setStyle("-fx-control-inner-background: #8897b3;"
				+ "-fx-background-color: linear-gradient(to right, #212838, #c2e3fc);"
				+ "-fx-text-fill: black; -fx-font-size: 30px;"
				+ "-fx-font-alignment: center;"
				);
		//txtFunction

		//setup label
		labelResult = new Label();
		labelResult.setText("Result");
		labelResult.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Georgia;"
				);
		txtResult = new TextArea();
		txtResult.setEditable(false);
		txtResult.autosize();
		txtResult.setPadding(new Insets(10, 10, 10, 10));
		txtResult.setStyle("-fx-control-inner-background: #8897b3;"
				+ "-fx-background-color: linear-gradient(to right, #212838, #c2e3fc);"
				+ "-fx-text-fill: black; -fx-font-size: 30px;"
				);

		//setup label
		labelTruth = new Label();
		labelTruth.setText("Truth Table");
		labelTruth.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Georgia;"
				);
		txtTruthTable = new TextArea();
		txtTruthTable.setEditable(false);
		txtTruthTable.autosize();
		txtTruthTable.setPadding(new Insets(10, 10, 10, 10));
		txtTruthTable.setStyle("-fx-control-inner-background: #8897b3;"
				+ "-fx-background-color: linear-gradient(to right, #c2e3fc, #212838);"
				+ "-fx-text-fill: black; -fx-font-size: 30px;"
				);

		//setup HBoxes
		HBox hboxButton = new HBox();
		hboxButton.getChildren().add(btnSave);
		hboxButton.setAlignment(Pos.CENTER);
		
		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(labelTruth, txtTruthTable);
		vbox1.setAlignment(Pos.CENTER);
		vbox1.setSpacing(20);
		
		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(labelResult, txtResult);
		vbox2.setAlignment(Pos.CENTER);
		vbox2.setSpacing(20);
		
		HBox hbox = new HBox();
		hbox.getChildren().add(vbox1);
		hbox.getChildren().add(vbox2);
		hbox.setSpacing(20);
		
		VBox vbox = new VBox();
		vbox.getChildren().addAll(labelFunc, txtFunction, hbox, hboxButton);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);

		//add controls and labels to container
		this.add(vbox, 0, 0);

	}

	//methods to retrieve overview data
	/**
	 * @param str: accepts a string of student personal details and populate the corresponding textField in the view
	 */
	public void populateFunction(String str) {
		txtFunction.setText(str);
	}
	/**
	 * @param str: accepts a string of student selected modules and populate the corresponding textField in the view
	 */
	public void populateResult(String str) {
		txtResult.setText(str);
	}
	/**
	 * @param str: accepts a string of student reserved modules and populate the corresponding textField in the view
	 */
	public void populateTruthTable(String str) {
		txtTruthTable.setText(str);
	}

	/**
	 * @returns the button (Save Overview) to be accessed by the controller
	 */
	public Button getBtnSaveOverview() {
		return btnSave;
	}
	/**
	 * @returns student personal details of type TextArea
	 */
	public TextArea getTxtFunction() {
		return txtFunction;
	}
	/**
	 * @returns student selected modules of type TextArea
	 */
	public TextArea getTxtResult() {
		return txtResult;
	}
	/**
	 * @return student reserved modules of type TextArea
	 */
	public TextArea getTxtTruthTable() {
		return txtTruthTable;
	}

	/**
	 * method to attach the buttons event handlers
	 * @param handler: accepts an external handler set by the controller
	 */
	public void btnSaveHandler(EventHandler<ActionEvent> handler) {
		btnSave.setOnAction(handler);
	}
}
