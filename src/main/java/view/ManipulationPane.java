package view;

import expressionTree.Expression;
import expressionTree.rules.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import model.ValidationClass;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

public class ManipulationPane extends ScrollPane {
	private final Button btnSave, btnCalc, btnAdd, btnIntroduce, btnManipulate, btnUpdateCombo;
	private final Button btnAnd, btnOr, btnImplies, btnIfOnlyIf, btnNegation;
	private final ValidationClass txtFormula;
	private final TextArea txtFunction, txtResult;
	private final ComboBox<String> comboBox;


	/**
	 * a constructor method to initiate the overview pane
	 */
	public ManipulationPane() {

		//styling
		this.setPadding(new Insets(20, 10, 20, 10));
		this.setPrefSize(700, 700);
		this.setFitToWidth(true);

		Label lblFormula = new Label("Add An Expression");

		lblFormula.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Harrington;"
		);

		// Create a round button with a tooltip
		Button popupButton = new Button("Click me!");
		popupButton.setStyle("-fx-background-color: #37435c; "
				+ "-fx-text-fill: white; "
				+ "-fx-background-radius: 30;");

		popupButton.setOnMouseEntered(e -> popupButton.setEffect(new DropShadow(10, Color.WHITE)));
		popupButton.setOnMouseExited(e -> popupButton.setEffect(null));

		Tooltip tooltip = new Tooltip("Click me to display a message");
		popupButton.setTooltip(tooltip);

		// Create a popup message
		Tooltip popupMessage = new Tooltip("""
				Only alphabetic characters and the below operators are allowed
							Make sure you close all the brackets
				(T) and (F) are considered true and false rather than variables""");
		popupMessage.setStyle("-fx-font-size: 18px;");
		popupMessage.setAutoHide(true);

		// Show the popup message when the button is clicked
		popupButton.setOnAction(e -> {
			Bounds bounds = popupButton.localToScreen(popupButton.getBoundsInLocal());
			popupMessage.show(popupButton.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
		});

		//error labels
		Label lblFormulaError = new Label("Variables can only be alphabetic, all brackets must be closed,\n"
				+ "\t\tand only the below operators are allowed");

		lblFormulaError.setStyle("-fx-font-size: 25px;"
				+ "-fx-padding: 8px;"
				+ "-fx-background-color: #ff9999;"
				+ "-fx-text-fill: #800000;"
				+ "-fx-border-color: #800000;"
				+ "-fx-border-width: 2px;"
				+ "-fx-alignment: center;"
		);


		//setup text fields
		txtFormula = new ValidationClass(input -> {
			// Match only alphabets, spaces, and a few special characters
			if (!input.matches("[a-zA-Z !&()~|=<>]*")) {
				return false;
			}
			// Check if brackets are balanced
			int count = 0;
			for (char c : input.toCharArray()) {
				if (c == '(') {
					count++;
				} else if (c == ')') {
					count--;
				}
				if (count < 0) {
					return false;
				}
			}
			return count == 0;
		});

		txtFormula.setPadding(new Insets(10, 10, 10, 10));

		txtFormula.setStyle("-fx-background-color: linear-gradient(#686868 0%, #232723 25%, #373837 75%, #757575 100%),"
				+ "linear-gradient(#020b02, #3a3a3a), "
				+ "linear-gradient(#b9b9b9 0%, #c2c2c2 20%, #afafaf 80%, #c8c8c8 100%),"
				+ "linear-gradient(#f5f5f5 0%, #dbdbdb 50%, #cacaca 51%, #d7d7d7 100%);"
				+ "-fx-background-insets: 0,1,4,5,6;"
				+ "-fx-background-radius: 70,69,65,64,63;"
				+ "-fx-padding: 15 30 15 30;"
				+ "-fx-font-size: 30px;"
				+ "-fx-font-weight: bold;"
				+ "-fx-text-fill: #333333;"
				+ "-fx-effect: dropshadow( three-pass-box , rgba(255,255,255,0.2) , 1, 0.0 , 0 , 1);"
		);
		txtFormula.setMaxSize(700, 100);
		txtFormula.setPromptText("Enter a logical expression");



		//initialize the buttons, set styles, and binds them to the validation in the validation helper class
		btnAdd = new Button("Add");
		btnIntroduce = new Button("Introduce");
		btnManipulate = new Button("Manipulate");
		btnAnd = new Button("&");
		btnOr = new Button("|");
		btnImplies = new Button("=>");
		btnIfOnlyIf = new Button("<=>");
		btnNegation = new Button("~");

		btnAdd.setStyle(WelcomingPane.idle);
		btnAdd.setOnMouseEntered(e -> btnAdd.setStyle(WelcomingPane.hover));
		btnAdd.setOnMouseExited(e -> btnAdd.setStyle(WelcomingPane.idle));

		btnIntroduce.setStyle(WelcomingPane.idle);
		btnIntroduce.setOnMouseEntered(e -> btnIntroduce.setStyle(WelcomingPane.hover));
		btnIntroduce.setOnMouseExited(e -> btnIntroduce.setStyle(WelcomingPane.idle));

		btnManipulate.setStyle(WelcomingPane.idle);
		btnManipulate.setOnMouseEntered(e -> btnManipulate.setStyle(WelcomingPane.hover));
		btnManipulate.setOnMouseExited(e -> btnManipulate.setStyle(WelcomingPane.idle));

		btnAnd.setStyle(WelcomingPane.idle);
		btnAnd.setOnMouseEntered(e -> btnAnd.setStyle(WelcomingPane.hover));
		btnAnd.setOnMouseExited(e -> btnAnd.setStyle(WelcomingPane.idle));

		btnOr.setStyle(WelcomingPane.idle);
		btnOr.setOnMouseEntered(e -> btnOr.setStyle(WelcomingPane.hover));
		btnOr.setOnMouseExited(e -> btnOr.setStyle(WelcomingPane.idle));

		btnImplies.setStyle(WelcomingPane.idle);
		btnImplies.setOnMouseEntered(e -> btnImplies.setStyle(WelcomingPane.hover));
		btnImplies.setOnMouseExited(e -> btnImplies.setStyle(WelcomingPane.idle));

		btnIfOnlyIf.setStyle(WelcomingPane.idle);
		btnIfOnlyIf.setOnMouseEntered(e -> btnIfOnlyIf.setStyle(WelcomingPane.hover));
		btnIfOnlyIf.setOnMouseExited(e -> btnIfOnlyIf.setStyle(WelcomingPane.idle));

		btnNegation.setStyle(WelcomingPane.idle);
		btnNegation.setOnMouseEntered(e -> btnNegation.setStyle(WelcomingPane.hover));
		btnNegation.setOnMouseExited(e -> btnNegation.setStyle(WelcomingPane.idle));

		btnAdd.setPadding(new Insets(10, 10, 10, 10));
		btnIntroduce.setPadding(new Insets(10, 10, 10, 10));
		btnManipulate.setPadding(new Insets(10, 10, 10, 10));
		btnAnd.setPadding(new Insets(10, 10, 10, 10));
		btnOr.setPadding(new Insets(10, 10, 10, 10));
		btnImplies.setPadding(new Insets(10, 10, 10, 10));
		btnIfOnlyIf.setPadding(new Insets(10, 10, 10, 10));
		btnNegation.setPadding(new Insets(10, 10, 10, 10));

		btnAdd.setMinSize(50, 50);
		btnIntroduce.setMinSize(50, 50);
		btnManipulate.setMinSize(50, 50);
		btnAnd.setMinSize(35, 35);
		btnOr.setMinSize(35, 35);
		btnImplies.setMinSize(35, 35);
		btnIfOnlyIf.setMinSize(35, 35);
		btnNegation.setMinSize(35, 35);


		btnAdd.disableProperty().bind(txtFormula.isValidProperty.not());
		lblFormulaError.visibleProperty().bind(txtFormula.isValidProperty.not());

		btnIntroduce.disableProperty().bind(txtFormula.isValidProperty.not());
		lblFormulaError.visibleProperty().bind(txtFormula.isValidProperty.not());



		//h Boxes
		HBox hb = new HBox(btnAnd, btnOr, btnImplies, btnIfOnlyIf, btnNegation);
		HBox hb1 = new HBox(btnAdd, btnIntroduce);
		HBox hb2 = new HBox(lblFormula, popupButton);
		hb.setAlignment(Pos.BASELINE_CENTER);
		hb1.setAlignment(Pos.BASELINE_CENTER);
		hb2.setAlignment(Pos.BASELINE_CENTER);
		hb.setPadding(new Insets(5, 5, 5, 5));
		hb1.setPadding(new Insets(5, 5, 5, 5));
		hb2.setPadding(new Insets(5, 5, 5, 5));
		hb.setSpacing(20);
		hb1.setSpacing(20);
		hb2.setSpacing(20);

		//v Box
		VBox vb = new VBox(hb2, txtFormula, lblFormulaError, hb, hb1);
		vb.setAlignment(Pos.CENTER);
		vb.setSpacing(20);
		//initialize buttons
		btnSave = new Button("Save");
		btnSave.setPrefSize(300, 50);
		btnSave.setPadding(new Insets(10, 10, 10, 10));
		btnSave.setAlignment(Pos.CENTER);
		btnSave.setMinSize(300, 75);

		btnSave.setStyle(WelcomingPane.idle);
		btnSave.setOnMouseEntered(e -> btnSave.setStyle(WelcomingPane.hover));
		btnSave.setOnMouseExited(e -> btnSave.setStyle(WelcomingPane.idle));

		btnCalc = new Button("Calculate");
		btnCalc.setPrefSize(300, 50);
		btnCalc.setPadding(new Insets(10, 10, 10, 10));
		btnCalc.setAlignment(Pos.CENTER);
		btnCalc.setMinSize(300, 75);

		btnCalc.setStyle(WelcomingPane.idle);
		btnCalc.setOnMouseEntered(e -> btnCalc.setStyle(WelcomingPane.hover));
		btnCalc.setOnMouseExited(e -> btnCalc.setStyle(WelcomingPane.idle));

		//setup label
		Label labelFunc = new Label();
		labelFunc.setText("Function");
		labelFunc.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Georgia;"
				);

		// Create a round button with a tooltip
		Button manipulateHelpButton = new Button("Click me!");
		manipulateHelpButton.setStyle("-fx-background-color: #37435c; "
				+ "-fx-text-fill: white; "
				+ "-fx-background-radius: 30;");

		manipulateHelpButton.setOnMouseEntered(e -> manipulateHelpButton.setEffect(new DropShadow(10, Color.WHITE)));
		manipulateHelpButton.setOnMouseExited(e -> manipulateHelpButton.setEffect(null));

		Tooltip manipulateTooltip = new Tooltip("Click me to display a message");
		manipulateHelpButton.setTooltip(manipulateTooltip);

		// Create a popup message
		Tooltip manipulatePopupMessage = new Tooltip("""
						Each time you want to manipulate, highlight an expression
										and click on update button.
				If you want to introduce an expression go back up and use the button introduce.""");
		manipulatePopupMessage.setStyle("-fx-font-size: 18px;");
		manipulatePopupMessage.setAutoHide(true);

		// Show the popup message when the button is clicked
		manipulateHelpButton.setOnAction(e -> {
			Bounds bounds = manipulateHelpButton.localToScreen(manipulateHelpButton.getBoundsInLocal());
			manipulatePopupMessage.show(manipulateHelpButton.getScene().getWindow(), bounds.getMinX(), bounds.getMaxY());
		});

		//setup text area
		txtFunction = new TextArea();
		txtFunction.setEditable(false);
		txtFunction.setPadding(new Insets(10, 10, 10, 10));
		txtFunction.setMaxSize(700, 100);
		txtFunction.setPrefSize(700, 100);
		txtFunction.setStyle("-fx-control-inner-background: #8897b3;"
				+ "-fx-background-color: linear-gradient(to right, #212838, #c2e3fc);"
				+ "-fx-text-fill: black; -fx-font-size: 30px;"
				);
		//txtFunction
		btnUpdateCombo = new Button("Update");
		btnUpdateCombo.setPadding(new Insets(10, 10, 10, 10));
		btnUpdateCombo.setAlignment(Pos.CENTER);
		btnUpdateCombo.setMinSize(50, 50);
		btnUpdateCombo.setStyle(WelcomingPane.idle);
		btnUpdateCombo.setOnMouseEntered(e -> btnUpdateCombo.setStyle(WelcomingPane.hover));
		btnUpdateCombo.setOnMouseExited(e -> btnUpdateCombo.setStyle(WelcomingPane.idle));
		comboBox = new ComboBox<>();
		comboBox.getItems().addAll("Select an option..");
		comboBox.setOnAction((event)-> updateModel());
		Label comboLabel = new Label("Select a rule:");
		comboLabel.setStyle("-fx-text-fill: white;");
		VBox comboVBox = new VBox(10, comboLabel, comboBox);
		comboVBox.setPadding(new Insets(10));
		comboBox.setValue(comboBox.getItems().get(0));

		//setup label
		Label labelResult = new Label();
		labelResult.setText("Result");
		labelResult.setStyle("-fx-text-fill: white;"
				+ "-fx-font: 30px Georgia;"
				);
		txtResult = new TextArea();
		txtResult.setEditable(false);
		txtResult.setMaxSize(1000, 500);
		txtResult.setPrefSize(1000, 500);
		txtResult.setPadding(new Insets(10, 10, 10, 10));
		//txtResult.setPrefSize(800, 200);
		txtResult.setStyle("-fx-control-inner-background: #8897b3;"
				+ "-fx-background-color: linear-gradient(to right, #212838, #c2e3fc);"
				+ "-fx-text-fill: black; -fx-font-size: 30px;"
				);

		//setup HBoxes
		HBox hboxButton = new HBox();
		hboxButton.getChildren().addAll(btnSave, btnCalc);
		hboxButton.setAlignment(Pos.CENTER);
		hboxButton.setSpacing(20);

		HBox hboxManipulate = new HBox();
		hboxManipulate.getChildren().addAll(btnUpdateCombo, comboVBox, btnManipulate);
		hboxManipulate.setAlignment(Pos.CENTER);
		hboxManipulate.setSpacing(20);

		VBox vbox1 = new VBox();
		vbox1.getChildren().addAll(labelResult, txtResult);
		vbox1.setAlignment(Pos.CENTER);
		vbox1.setSpacing(20);
		
		VBox vbox = new VBox();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_CENTER);
		hbox.setPadding(new Insets(5, 5, 5, 5));
		hbox.setSpacing(20);
		hbox.getChildren().addAll(labelFunc, manipulateHelpButton);
		vbox.getChildren().addAll(hbox, txtFunction, hboxManipulate);
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(20);

		VBox vbox2 = new VBox();
		vbox2.getChildren().addAll(vb, vbox, vbox1, hboxButton);
		vbox2.setAlignment(Pos.CENTER);
		vbox2.setSpacing(20);
		


		//add controls and labels to container
		this.setContent(vbox2);
		// create a new background image
		InputStream input;
		try {
			input = ClassLoader.getSystemResourceAsStream("media/black.png");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		Image image = new Image(Objects.requireNonNull(input));

		// create a new background image view
		BackgroundImage backgroundImageView = new BackgroundImage(image,
				BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
				BackgroundSize.DEFAULT);

		// create a new background
		Background background = new Background(backgroundImageView);

		// create a new StackPane to wrap the content
		StackPane contentPane = new StackPane(this.getContent());

		// set the background of the StackPane
		contentPane.setBackground(background);

		// set the content of the ScrollPane to the StackPane
		this.setContent(contentPane);
		// set the background to the root pane
		this.setBackground(background);
	}

	//methods to retrieve overview data

	public String getFormula(){
		return txtFormula.getText();
	}
	public String getFunction(){
		return txtFunction.getText();
	}
	public String getSelectedFormula(){
		return txtFunction.getSelectedText();
	}
	public IndexRange getIndexRange(){
		return txtFunction.getSelection();
	}

	public void setFormula(String str) {
		int index = txtFormula.getText().length();
		txtFormula.insertText(index, str);
	}
	public void removeSpaces(String str) {
		String purified = str.replaceAll(" ", "");
		txtFormula.clear();
		txtFormula.setText(purified.toUpperCase());
	}

	public void updateComboBox(List<String> itemList, Expression expression) {
		comboBox.getItems().clear();
		comboBox.getItems().add("Select an option..");
		comboBox.setValue(comboBox.getItems().get(0));
		for (String item : itemList) {
			if (compliesWithRule(item, expression)) {
				comboBox.getItems().add(item);
			}
		}
	}

	private boolean compliesWithRule(String item, Expression expr) {
		// Write your propositional logic rule here
		boolean result = false;
		switch (item){
			case "Idempotence Rule" -> {
				IdempotenceVisitor visitor = new IdempotenceVisitor();
				result = visitor.canApply(expr);
			}
			case "De Morgan's Rule" -> {
				DeMorganVisitor visitor = new DeMorganVisitor();
				result = visitor.canApply(expr);
			}
			case "Complement Rule" -> {
				ComplementVisitor visitor = new ComplementVisitor();
				result = visitor.canApply(expr);
			}
			case "Absorption Rule" -> {
				AbsorptionVisitor visitor = new AbsorptionVisitor();
				result = visitor.canApply(expr);
			}
			case "Associative Rule" -> {
				AssociativeVisitor visitor = new AssociativeVisitor();
				result = visitor.canApply(expr);
			}
			case "Commutative Rule" -> {
				CommutativeVisitor visitor = new CommutativeVisitor();
				result = visitor.canApply(expr);
			}
			case "Contrapositive Rule" -> {
				ContrapositiveVisitor visitor = new ContrapositiveVisitor();
				result = visitor.canApply(expr);
			}
			case "Distributive Rule" -> {
				DistributiveVisitor visitor = new DistributiveVisitor();
				result = visitor.canApply(expr);
			}
			case "Double Negation Rule" -> {
				DoubleNegationVisitor visitor = new DoubleNegationVisitor();
				result = visitor.canApply();
			}
			case "Switcheroo Rule" -> {
				SwitcherooVisitor visitor = new SwitcherooVisitor();
				result = visitor.canApply(expr);
			}
			case "Identity Rule" -> {
				IdentityVisitor visitor = new IdentityVisitor();
				result = visitor.canApply(expr);
			}
			case "Elimination of & Right" -> {
				AndEliminationRVisitor visitor = new AndEliminationRVisitor();
				result = visitor.canApply(expr);
			}
			case "Elimination of & Left" -> {
				AndEliminationLVisitor visitor = new AndEliminationLVisitor();
				result = visitor.canApply(expr);
			}
			case "Elimination of =>" -> {
				ImplyEliminationVisitor visitor = new ImplyEliminationVisitor();
				result = visitor.canApply(expr);
			}
			case "BiConditional Rule" -> {
				BiConditionalVisitor visitor = new BiConditionalVisitor();
				result = visitor.canApply(expr);
			}
		}
		return result;
	}

	private void updateModel() {
		// Call the controller to update the model
	}

	public String getSelectedRule(){
		return comboBox.getValue();
	}

	public void clearFormula(){
		txtFormula.clear();
	}

	public void setFunction(String function, int start, int end){
		txtFunction.replaceText(start, end, function);
	}

	public void addButtonLogicHandler(EventHandler<ActionEvent> handler) {
		btnAdd.setOnAction(handler);
	}
	public void introduceButtonLogicHandler(EventHandler<ActionEvent> handler) {
		btnIntroduce.setOnAction(handler);
	}
	public void manipulateButtonLogicHandler(EventHandler<ActionEvent> handler) {
		btnManipulate.setOnAction(handler);
	}
	public void conjunctionLogicHandler(EventHandler<ActionEvent> handler) {
		btnAnd.setOnAction(handler);
	}
	public void disjunctionLogicHandler(EventHandler<ActionEvent> handler) {
		btnOr.setOnAction(handler);
	}
	public void implicationLogicHandler(EventHandler<ActionEvent> handler) {
		btnImplies.setOnAction(handler);
	}
	public void equivalenceLogicHandler(EventHandler<ActionEvent> handler) {
		btnIfOnlyIf.setOnAction(handler);
	}
	public void negationLogicHandler(EventHandler<ActionEvent> handler) {
		btnNegation.setOnAction(handler);
	}
	public void updateBtnLogicHandler(EventHandler<ActionEvent> handler) {
		btnUpdateCombo.setOnAction(handler);
	}

	/**
	 * @param str accepts a string of student personal details and populate the corresponding textField in the view
	 */
	public void populateFunction(String str) {
		txtFunction.setText(str);
	}
	/**
	 * @param str accepts a string of student-selected modules and populate the corresponding textField in the view
	 */
	public void populateResult(String str) {
		txtResult.appendText(str);
	}

	public void clearResult(){
		txtResult.clear();
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

	//See explaination in the controller
	public ComboBox getCombo() {
		return comboBox;
	}

	/**
	 * method to attach the buttons event handlers
	 * @param handler accepts an external handler set by the controller
	 */
	public void btnSaveHandler(EventHandler<ActionEvent> handler) {
		btnSave.setOnAction(handler);
	}
	public void btnCalcHandler(EventHandler<ActionEvent> handler) {
		btnCalc.setOnAction(handler);
	}

}
