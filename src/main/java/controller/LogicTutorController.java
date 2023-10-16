package controller;
import expressionTree.*;
import expressionTree.rules.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import model.DataBase;
import model.TruthTableHelperFun;
import view.*;
import model.LogicalFormula;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author meize
 * This is a Controller that Accepts input and converts it to commands for the model or the view.
 */
public class LogicTutorController {

	//fields to be used throughout class
	private final LogicTutorRootPane view;
	private final EvaluatorPane ep;
	private final ResultPane rp;
	private final LogicTutorMenuBar ltmb;
	private final LogicalFormula model;
	private final WelcomingPane wp;
	private final ManipulationPane mp;
	private final TestPane testp;
	private final DataBase db;

	/**
	 * a constructor method to initiate the view and model
	 * @param view Accepts a view where the user interaction is happening
	 * @param model Accepts a model to either store the data inputted on the view or-
	 * 				-displays the data exited in the model on the view
	 */
	public LogicTutorController(LogicTutorRootPane view, LogicalFormula model) {
		//initialize view and model fields
		this.view = view;
		this.model = model;

		//initialize view sub-container fields
		ep = view.getLogicTutorPane();
		ltmb = view.getLogicTutorMenuBar();
		rp = view.getResultPane();
		wp = view.getWelcomingPane();
		mp = view.getManipulationPane();
		testp = view.getTestPane();


		//attach event handlers to view using private helper method
		this.attachEventHandlers();

		//starting the database and populating all required fields from the database
		// create the tree view
		db = new DataBase();

		//create a rootItem for the tree
		TreeItem<String> rootItem = new TreeItem<>("Lessons");

		// retrieve the lessons from the database
		ObservableList<String> lessons = db.retrieveLessonsFromDatabase();

		for (String lesson : lessons) {
			TreeItem<String> lessonItem = new TreeItem<>(lesson);

			// retrieve the tests for this lesson from the database
			ObservableList<String> tests = db.retrieveTestsFromDatabaseForLesson(lesson);

			for (String test : tests) {
				TreeItem<String> testItem = new TreeItem<>(test);
				lessonItem.getChildren().add(testItem);
			}
			testp.populateTreeView(rootItem, lessonItem);
		}
	}

	/**
	 * helper method - used to attach event handlers
	 */
	private void attachEventHandlers() {
		//WelcomePane Handlers
		wp.studyHandler(new moveToStudyPaneHandler());
		wp.evaluatorHandler(new moveToEvaluatePaneHandler());
		wp.manipulatorHandler(new moveToManipulatePaneHandler());
		wp.testHandler(new moveToTestPaneHandler());

		//attach an event handler to create student profile pane
		ep.evaluateLogicHandler(new evalLogicTutorPaneHandler());
		ep.manipulateLogicHandler(new simplificationBtnHandler());
		ep.conjunctionLogicHandler(new conjunctionBtnHandler());
		ep.disjunctionLogicHandler(new disjunctionBtnHandler());
		ep.negationLogicHandler(new negationBtnHandler());
		ep.implicationLogicHandler(new implicationBtnHandler());
		ep.equivalenceLogicHandler(new equivalenceBtnHandler());

		//resultPane Handlers
		rp.btnSaveHandler(new saveResultBtnHandler());

		//Manipulation pane handlers
		mp.btnSaveHandler(new saveManipulationPaneBtnHandler());
		mp.btnCalcHandler(new calcManipulationPaneBtnHandler());
		mp.addButtonLogicHandler(new addManipulationPaneBtnHandler());
		mp.introduceButtonLogicHandler(new introduceManipulationPaneBtnHandler());
		mp.manipulateButtonLogicHandler(new manipulateManipulationPaneBtnHandler());
		mp.conjunctionLogicHandler(new conjunctionManipulationPaneBtnHandler());
		mp.disjunctionLogicHandler(new disjunctionManipulationPaneBtnHandler());
		mp.negationLogicHandler(new negationManipulationPaneBtnHandler());
		mp.implicationLogicHandler(new implicationManipulationPaneBtnHandler());
		mp.equivalenceLogicHandler(new equivalenceManipulationPaneBtnHandler());
		mp.updateBtnLogicHandler(new updateComboManipulationPaneBtnHandler());

		//Test pane handlers
		testp.takeTestHandler(new takeTestHandler());

		//attach an event handler to the menu bar that closes the application
		ltmb.addExitHandler(e -> System.exit(0));

		//attach an event handler to the about menu bar of the application
		ltmb.addAboutHandler(e -> ltmb.aboutInfo());

	}
	//event handlers private class, used for handling actions on the view.
	private class evalLogicTutorPaneHandler implements EventHandler<ActionEvent> {
		/**
		 * Handles the action event when the evaluate button is clicked.
		 * Retrieves data from the view and sets it in the model for calculation.
		 * If the formula is empty, an error is displayed, otherwise, the formula is evaluated.
		 * The resulting truth table and evaluation result are set in the model and displayed in the result pane.
		 *
		 * @param e The action event triggered by clicking the evaluate button.
		 */
		public void handle(ActionEvent e) {
			// Retrieves data from the view
			try {
				ep.removeSpaces(ep.getFormula());
				model.setFormula(ep.getFormula());
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			// Check that input is not empty
			if (model.getFormula().equals("")) {
				// Output error
				alertDialogBuilder("To calculate, you need to enter a formula");
			}else {
				try
				{
					// Clear the parser's list and tokenize the expression
					Parser.list.clear();

					String expression = model.getFormula();
					List<String> tokens = Tokenizer.tokenize(expression);
					List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);
					// Evaluate the expression and generate the truth table and result table
					Expression expr = Parser.Evaluator(Objects.requireNonNull(shunting));
					List<Value> boolTbl = TruthTableHelperFun.booleanTable(Parser.list);
					String truthTbl = TruthTableHelperFun.truthTable(Parser.list);
					String resultTbl = TruthTableHelperFun.resultTable(Parser.list, boolTbl, expr, expression);

					// Set the truth table and result table in the model and display them in the result pane
					model.setTruthTable(truthTbl);
					model.setResult(resultTbl);
					rp.populateFunction(model.getFormula());
					rp.populateResult(model.getResult());
					rp.populateTruthTable(model.getTruthTable());
				}
				catch (EvaluationException Pex)
				{
					// Handle evaluation exception by printing the stack trace
					Pex.printStackTrace();
				}
				// Switch to the result pane
				view.changeTab(3);
			}
		}
	}

	/**
	 * Private class to handle simplification events in the tutor pane.
	 */
	private class simplificationBtnHandler implements EventHandler<ActionEvent> {
		/**
		 * Handles the action event when the simplification button is clicked.
		 * Retrieves data from the view and sets it in the model for simplification.
		 * If the formula is empty, an error is displayed, otherwise, the formula is simplified.
		 * The resulting simplified formula is displayed in the simplification pane.
		 *
		 * @param e The action event triggered by clicking the simplification button.
		 */
		public void handle(ActionEvent e) {
			// Retrieves data from the view
			try {
				ep.removeSpaces(ep.getFormula());
				model.setFormula(ep.getFormula());
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			//Check input not empty
			if (model.getFormula().equals("")) {
				//Output error
				alertDialogBuilder("To calculate, you need to enter a formula");
			}else {
				// Populate the formula and result in the simplification pane
				mp.populateFunction(model.getFormula());
				mp.populateResult(model.getFormula());
				view.changeTab(4);
			}
		}
	}

	/**
	 * Private class to handle the conjunction button in the expression pane.
	 * Sets the conjunction symbol in the expression pane.
	 */
	private class conjunctionBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ep.setFormula("&");
		}
	}

	/**
	 * Private class to handle the disjunction button in the expression pane.
	 * Sets the disjunction symbol in the expression pane.
	 */
	private class disjunctionBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ep.setFormula("|");
		}
	}
	/**
	 * Private class to handle the negation button in the expression pane.
	 * Sets the negation symbol in the expression pane.
	 */
	private class negationBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ep.setFormula("~");
		}
	}
	/**
	 * Private class to handle the implication button in the expression pane.
	 * Sets the implication symbol in the expression pane.
	 */
	private class implicationBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ep.setFormula("=>");
		}
	}
	/**
	 * Private class to handle the equivalence button in the expression pane.
	 * Sets the equivalence symbol in the expression pane.
	 */
	private class equivalenceBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			ep.setFormula("<=>");
		}
	}

	/**
	 * Private class to handle moving to the study pane button.
	 * Changes the active tab to the study pane.
	 */
	private class moveToStudyPaneHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			view.changeTab(1);
		}
	}
	/**
	 * Private class to handle moving to the evaluate pane button.
	 * Changes the active tab to the evaluate pane.
	 */
	private class moveToEvaluatePaneHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			view.changeTab(2);
		}
	}
	/**
	 * Private class to handle moving to the manipulate pane button.
	 * Changes the active tab to the manipulate pane.
	 */
	private class moveToManipulatePaneHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			view.changeTab(4);
		}
	}
	/**
	 * Private class to handle moving to the test pane button.
	 * Changes the active tab to the test pane.
	 */
	private class moveToTestPaneHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			view.changeTab(5);
		}
	}

	/**
	 * Handles the save button on the Manipulation pane, writes the contents of the TextArea to a file.
	 */
	private class saveManipulationPaneBtnHandler implements EventHandler<ActionEvent>{

		/**
		 * Handles the action of clicking on the save button.
		 * @param event The event that triggered the action.
		 */
		@Override
		public void handle(ActionEvent event) {
			// Get the contents of the TextArea
			String text = mp.getTxtResult().getText();
			if(!mp.getTxtResult().getText().isEmpty()) {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timeStamp = dateFormat.format(new Date());
				String fileName = "file_" + timeStamp + ".txt";
				// Set the file path
				String filePath = String.format(".//savedManipulations//%s", fileName);

				// Create a File object
				File file = new File(filePath);

				// Write the contents of the TextArea to the file

				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(text);
					fileWriter.close();
					successDialogBuilder("Save Complete");
				} catch (IOException e) {
					e.printStackTrace();
					alertDialogBuilder("Save InComplete");
				}
			}else {
				alertDialogBuilder("There is nothing to save");
			}
		}
	}
	/**
	 * Handles the save button on the Evaluation pane, writes the contents of the TextArea to a file.
	 */
	private class saveResultBtnHandler implements EventHandler<ActionEvent>{
		/**
		 * Handles the action of clicking on the save button.
		 * @param event The event that triggered the action.
		 */
		@Override
		public void handle(ActionEvent event) {
			// Get the contents of the TextArea
			String text = rp.getTxtFunction().getText() +"\n"+ rp.getTxtTruthTable().getText() +"\n"+ rp.getTxtResult().getText();
			if(!rp.getTxtResult().getText().isEmpty()) {
				DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
				String timeStamp = dateFormat.format(new Date());
				String fileName = "file_" + timeStamp + ".txt";
				// Set the file path
				String filePath = String.format(".//savedEvaluations//%s", fileName);
				// Create a File object
				File file = new File(filePath);
				// Write the contents of the TextArea to the file
				try {
					FileWriter fileWriter = new FileWriter(file);
					fileWriter.write(text);
					fileWriter.close();
					successDialogBuilder("Save Complete");
				} catch (IOException e) {
					e.printStackTrace();
					alertDialogBuilder("Save InComplete");
				}
			}else{
				alertDialogBuilder("There is nothing to save");
			}
		}
	}

	/**
	 * Handles the button event for calculating a formula in the Manipulation Pane.
	 */
	private class calcManipulationPaneBtnHandler implements EventHandler<ActionEvent>{
		/**
		 * Calculates a formula and updates the Result and Truth Table tabs accordingly.
		 * If the formula is empty, displays an alert dialog indicating an error.
		 * @param event The button event triggered by the user.
		 */
		@Override
		public void handle(ActionEvent event) {
			try {
				model.setFormula(mp.getFormula());
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			//check input not empty
			if (model.getFormula().equals("")) {
				//output error
				alertDialogBuilder("To calculate, you need to enter a formula");
			}else {
				try
				{
					// Clear the parser list
					Parser.list.clear();
					String expression = model.getFormula();

					// Tokenize the formula and convert to postfix notation
					List<String> tokens = Tokenizer.tokenize(expression);
					List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);

					// Evaluate the expression
					Expression expr = Parser.Evaluator(Objects.requireNonNull(shunting));

					// Generate the truth table and result table
					List<Value> boolTbl = TruthTableHelperFun.booleanTable(Parser.list);
					String truthTbl = TruthTableHelperFun.truthTable(Parser.list);
					String resultTbl = TruthTableHelperFun.resultTable(Parser.list, boolTbl, expr, expression);

					// Update the model with the new truth table and result table
					model.setTruthTable(truthTbl);
					model.setResult(resultTbl);

					// Update the Result and Truth Table tabs
					rp.populateFunction(model.getFormula());
					rp.populateResult(model.getResult());
					rp.populateTruthTable(model.getTruthTable());
				}
				catch (EvaluationException Pex)
				{
					Pex.printStackTrace();
				}
				// Switch to the Result tab
				view.changeTab(3);
			}
		}
	}
	/**
	 * Handles the event when the conjunction button is clicked in the manipulation pane.
	 */
	private class conjunctionManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			mp.setFormula("&");
		}
	}

	/**
	 * Handles the event when the disjunction button is clicked in the manipulation pane.
	 */
	private class disjunctionManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			mp.setFormula("|");
		}
	}
	/**
	 * Handles the event when the negation button is clicked in the manipulation pane.
	 */
	private class negationManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			mp.setFormula("~");
		}
	}
	/**
	 * Handles the event when the implication button is clicked in the manipulation pane.
	 */
	private class implicationManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			mp.setFormula("=>");
		}
	}
	/**
	 * Handles the event when the equivalence button is clicked in the manipulation pane.
	 */
	private class equivalenceManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			mp.setFormula("<=>");
		}
	}

	/**
	 * Handles the event when the add button is clicked in the manipulation pane.
	 */
	private class addManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		public void handle(ActionEvent e) {
			mp.clearResult();
			//retrieves data from the view
			try {
				mp.removeSpaces(mp.getFormula());
				model.setFormula(mp.getFormula());
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			//check input not empty
			if (model.getFormula().equals("")) {
				//output error
				alertDialogBuilder("You need to enter a formula");
			}else {
				mp.populateFunction(model.getFormula());
				mp.populateResult(model.getFormula());
				model.setFormula(model.getFormula());
			}
		}
	}

	/**
	 * EventHandler class that handles the introduceManipulationPaneBtn button click.
	 */
	private class introduceManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		/**
		 * Handles the action event triggered by the introduceManipulationPaneBtn button click.
		 * Retrieves data from the view and sets the formula in the model.
		 * Populates the result using the formula introduced using the Introduction.
		 *
		 * @param e The action event triggered by the button click.
		 */
		public void handle(ActionEvent e) {
			//retrieves data from the view
			try {
				// removes spaces from the formula
				mp.removeSpaces(mp.getFormula());
				// sets the formula in the model
				model.setFormula(mp.getFormula());
			} catch (NullPointerException ex) {
				// prints the stack trace if there's a NullPointerException
				ex.printStackTrace();
			}
			//check input not empty
			if (model.getFormula().equals("")) {
				// outputs an error if the input is empty
				alertDialogBuilder("You need to enter a formula");
			}else if(mp.getFunction().equals("")){
				// outputs an error if there's no formula to introduce to
				alertDialogBuilder("There is no formula to introduce to");
			}else {
				// populates the function with the introduced formula
				mp.populateFunction(model.getFormula());
				// populates the result using the introduced formula
				mp.populateResult("\nUsing Introduction \n" + model.getFormula());
				// sets the formula in the model
				model.setFormula(model.getFormula());
			}
		}
	}

	/**
	 * EventHandler class that handles the updateComboManipulationPaneBtn button click.
	 */
	private class updateComboManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		/**
		 * Handles the action event triggered by the updateComboManipulationPaneBtn button click.
		 * Sets the selected formula in the model and updates the combo box with the appropriate expressions.
		 *
		 * @param e The action event triggered by the button click.
		 *
		 * @throws EmptyStackException if there's an empty stack while evaluating the expression.
		 */
		public void handle(ActionEvent e) throws EmptyStackException {
			// sets the selected formula in the model
			model.setSelectedFormula(mp.getSelectedFormula());
			if (!model.getSelectedFormula().isEmpty()) {
				// gets the list of rules
				List<String> itemList = model.getRulesList();
				// gets the selected formula
				String expression = model.getSelectedFormula();
				// clears the list in the Parser class
				Parser.list.clear();
				// tokenizes the selected formula
				List<String> tokens = Tokenizer.tokenize(expression);
				// converts infix to postfix notation
				List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);
				Expression expr;
				try {
					// evaluates the expression using the Parser class
					expr = Parser.Evaluator(Objects.requireNonNull(shunting));
				} catch (Exception ex){
					if (ex instanceof EmptyStackException){
						// outputs an error if the expression is not appropriate
						alertDialogBuilder("Select an appropriate expression");
					}
					// throws the exception
					throw ex;
				}
				// updates the combo box with the appropriate expressions
				mp.updateComboBox(itemList, expr);
				// expands the combo box automatically after clicking on update showing the relevant rules.
				mp.getCombo().show();
			} else{
				// outputs an error if no expression is highlighted
				alertDialogBuilder("Highlight an expression");
			}
		}
	}


	/**
	 * This class handles the event when the update button is clicked in the ManipulationPane.
	 * It retrieves the selected formula from the ManipulationPane, parses it into a List of tokens,
	 * then converts it into a postfix notation using the Shunting Yard algorithm.
	 * The postfix notation is then passed into a Parser object which evaluates the expression.
	 * Finally, the ComboBox in the ManipulationPane is updated with the available rules for the evaluated expression.
	 */
	private class manipulateManipulationPaneBtnHandler implements EventHandler<ActionEvent> {
		/** It retrieves the selected formula from the ManipulationPane, parses it into a List of tokens,
		 * then converts it into a postfix notation using the Shunting Yard algorithm.
		 * The postfix notation is then passed into a Parser object which builds the expression.
		 * Finally, the ComboBox in the ManipulationPane is updated with the available rules for the evaluated expression.
		 */
		public void handle(ActionEvent e) {
			//retrieves data from the view
			try {
				model.setSelectedFormula(mp.getSelectedFormula());
			} catch (NullPointerException ex) {
				ex.printStackTrace();
			}
			//check input not empty
			if (model.getSelectedFormula().equals("")) {
				//output error
				alertDialogBuilder("Add a formula or highlight an expression");
			}else {
				// clear the list of parsed expressions in the Parser class
				Parser.list.clear();
				String expression = model.getSelectedFormula();
				// tokenize the selected formula
				List<String> tokens = Tokenizer.tokenize(expression);
				// convert the tokens into postfix notation
				List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);
				//evaluates the expression
				Expression expr = Parser.Evaluator(Objects.requireNonNull(shunting));
				Expression result = null;
				model.setResult("");
				//checking available rules
				switch (mp.getSelectedRule()) {
					case "Idempotence Rule" -> {
						IdempotenceVisitor visitor = new IdempotenceVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Idempotence Law");
					}
					case "De Morgan's Rule" -> {
						DeMorganVisitor visitor = new DeMorganVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using De Morgan's Law");
					}
					case "Complement Rule" -> {
						ComplementVisitor visitor = new ComplementVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Complement Rule");
					}
					case "Absorption Rule" -> {
						AbsorptionVisitor visitor = new AbsorptionVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Absorption Law");
					}
					case "Associative Rule" -> {
						AssociativeVisitor visitor = new AssociativeVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Associative Law");
					}
					case "Commutative Rule" -> {
						CommutativeVisitor visitor = new CommutativeVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Commutative Law");
					}
					case "Contrapositive Rule" -> {
						ContrapositiveVisitor visitor = new ContrapositiveVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Contrapositive Law");
					}
					case "Distributive Rule" -> {
						DistributiveVisitor visitor = new DistributiveVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Distributive Law");
					}
					case "Double Negation Rule" -> {
						DoubleNegationVisitor visitor = new DoubleNegationVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Double Negation Law");
					}
					case "Switcheroo Rule" -> {
						SwitcherooVisitor visitor = new SwitcherooVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Switcheroo Law");
					}
					case "Identity Rule" -> {
						IdentityVisitor visitor = new IdentityVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Identity Law");
					}
					case "Elimination of & Right" -> {
						AndEliminationRVisitor visitor = new AndEliminationRVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Elimination of & Right");
					}
					case "Elimination of & Left" -> {
						AndEliminationLVisitor visitor = new AndEliminationLVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Elimination of & Left");
					}
					case "Elimination of =>" -> {
						ImplyEliminationVisitor visitor = new ImplyEliminationVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using Elimination of =>");
					}
					case "BiConditional Rule" -> {
						BiConditionalVisitor visitor = new BiConditionalVisitor();
						result = expr.accept(visitor);
						model.updateResult("Using BiConditional Rule");
					}
					case "Select an option.." -> alertDialogBuilder("Select a rule");
				}
				mp.setFunction(Objects.requireNonNull(result).toString(), mp.getIndexRange().getStart(), mp.getIndexRange().getEnd());
				model.updateResult(mp.getFunction());
				mp.clearFormula();
				mp.populateResult(model.getResult());
				mp.setFormula(mp.getFunction());
				model.setResult("");
			}
		}
	}

	/**
	 * 	Handles the event when the user clicks the "Take Test" button in the TestPane view.
	 */
	private class takeTestHandler implements EventHandler<ActionEvent> {
		/**
		 * Retrieves the selected test from the TestPane view and creates a new TestQuestionsPane
		 * view to display the questions and answers for the selected test.
		 * @param event The ActionEvent triggered by clicking the "Take Test" button.
		 * */
		@Override
		public void handle(ActionEvent event) {
			if(testp.getTreeView().getSelectionModel().getSelectedItem() != null ) {
				String selectedTest = testp.getTreeView().getSelectionModel().getSelectedItem().getValue();
				if(selectedTest.contains("Test")) {
					List<String> questions = db.retrieveQuestionsForTest(selectedTest);
					List<List<String>> answers = new ArrayList<>();
					List<String> correctAnswers= new ArrayList<>();
					for(String question: questions){
						List<String> questionAnswers = db.retrieveAnswersForQuestion(question);
						correctAnswers.add(db.retrieveCorrectAnswersForQuestion(question).get(0));
						answers.add(questionAnswers);
					}
					// create a new TestQuestionsPane to display the questions and answers
					TestQuestionsPane tqp = new TestQuestionsPane(selectedTest, questions, answers, correctAnswers, 2400);
				}else{
					alertDialogBuilder("You selected a lesson. Please select a test");
				}
			}else{
				alertDialogBuilder("You did not select a test");
			}
		}
	}

	/**
	 * Shows an error dialog with the given message.
	 * @param str the message to display in the dialog.
	 */
	private void alertDialogBuilder(String str) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setHeaderText(null);
		alert.setContentText(str);
		alert.showAndWait();
	}
	/**
	 * Shows a success dialog with the given message.
	 * @param str the message to display in the dialog.
	 */
	private void successDialogBuilder(String str) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText(str);
		alert.showAndWait();
	}
}
