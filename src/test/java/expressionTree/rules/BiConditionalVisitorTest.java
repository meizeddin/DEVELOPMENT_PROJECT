package expressionTree.rules;

import org.junit.jupiter.api.Test;
import expressionTree.*;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BiConditionalVisitorTest {

    @Test
    public void testBiConditionalForward() {
        List<String> tokens = Tokenizer.tokenize("(B<=>C)");
        List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);
        Expression exp = Parser.Evaluator(Objects.requireNonNull(shunting));
        BiConditionalVisitor visitor = new BiConditionalVisitor();

        assertEquals("((B=>C)&(C=>B))",
                exp.accept(visitor).toString(),
                "Testing BiConditional Law"
                        + " From an instance of Equivalence to an instance of And and Imply ");
    }

    @Test
    public void testBiConditionalReverse() {
        List<String> tokens = Tokenizer.tokenize("(B=>C)&(C=>B)");
        List<String> shunting = ShuntingYardAlgorithm.infixToPostfix(tokens);
        Expression exp = Parser.Evaluator(Objects.requireNonNull(shunting));
        BiConditionalVisitor visitor = new BiConditionalVisitor();

        assertEquals("(B<=>C)",
                exp.accept(visitor).toString(),
                "Testing BiConditional Law"
                        + " From instance of And and Imply to an Instance of Equivalence");
    }
}

