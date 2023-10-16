package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A model class that sets and stores logical formula/expression 
 * @author meize
 *
 */
public class LogicalFormula implements Serializable{

	//fields
	@Serial
	private static final long serialVersionUID = 1L;
	private String formula, selectedFormula, result, truthTable;
	private final List<String> rulesList;

	//Constructor method
	/**
	 * A constructor method instantiating an empty formula field 
	 */
	public LogicalFormula() {
		formula = "";
		selectedFormula = "";
		result = "";
		truthTable = "";
		rulesList = new ArrayList<>();
		rulesList.add("Absorption Rule");
		rulesList.add("Associative Rule");
		rulesList.add("Commutative Rule");
		rulesList.add("De Morgan's Rule");
		rulesList.add("Distributive Rule");
		rulesList.add("Double Negation Rule");
		rulesList.add("Idempotence Rule");
		rulesList.add("Switcheroo Rule");
		rulesList.add("Identity Rule");
		rulesList.add("Complement Rule");
		rulesList.add("Elimination of & Right");
		rulesList.add("Elimination of & Left");
		rulesList.add("Elimination of =>");
		rulesList.add("Contrapositive Rule");
		rulesList.add("BiConditional Rule");

	}
	//methods
	/**
	 * @return returns a formula 
	 */
	public List<String> getRulesList(){
		return rulesList;
	}
	public String getFormula() {
		return formula;
	}
	public String getSelectedFormula() {
		return selectedFormula;
	}

	public String getResult() {
		return result;
	}
	public String getTruthTable() {
		return truthTable;
	}
	/**
	 * @param formula accepts a logical expression as a String
	 */
	public void setFormula(String formula) {
		this.formula = formula;
	}
	public void setSelectedFormula(String selectedFormula) {
		this.selectedFormula = selectedFormula;
	}

	public void setResult(String result) {
		this.result = result.toUpperCase();
	}
	public void updateResult(String str) {
		this.result += "\n" + str;
	}

	public void setTruthTable(String truthTable) {
		this.truthTable = truthTable.toUpperCase();
	}
	
	/**
	 * @return returns a String of class LogicalFormula 
	 */
	@Override
	public String toString() {
		return "Logic calculator:[Formula=" + formula + "]";
	}

}
