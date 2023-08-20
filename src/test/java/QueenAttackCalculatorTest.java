import com.polydus.exercism.queenattack.QueenAttackCalculator;
import org.junit.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class QueenAttackCalculatorTest {

    @Test
    public void testCreateQueenWithAValidPosition() {
        new QueenAttackCalculator.Queen(2, 2);
    }

    //@Ignore("Remove to run test")
    @Test
    public void testCreateQueenMustHavePositiveRow() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator.Queen(-2, 2))
                .withMessage("QueenAttackCalculator.Queen position must have positive row.");
    }

    //@Ignore("Remove to run test")
    @Test
    public void testCreateQueenMustHaveRowOnBoard() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator.Queen(8, 4))
                .withMessage("QueenAttackCalculator.Queen position must have row <= 7.");
    }

    //@Ignore("Remove to run test")
    @Test
    public void testCreateQueenMustHavePositiveColumn() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator.Queen(2, -2))
                .withMessage("QueenAttackCalculator.Queen position must have positive column.");
    }

    //@Ignore("Remove to run test")
    @Test
    public void testCreateQueenMustHaveColumnOnBoard() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator.Queen(4, 8))
                .withMessage("QueenAttackCalculator.Queen position must have column <= 7.");
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCannotAttack() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 4), new QueenAttackCalculator.Queen(6, 6));

        assertFalse(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnTheSameRow() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 4), new QueenAttackCalculator.Queen(2, 6));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnTheSameColumn() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(4, 5), new QueenAttackCalculator.Queen(2, 5));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnFirstDiagonal() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 2), new QueenAttackCalculator.Queen(0, 4));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnSecondDiagonal() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 2), new QueenAttackCalculator.Queen(3, 1));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnThirdDiagonal() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 2), new QueenAttackCalculator.Queen(1, 1));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensCanAttackOnFourthDiagonal() {
        QueenAttackCalculator calculator
                = new QueenAttackCalculator(new QueenAttackCalculator.Queen(1, 7), new QueenAttackCalculator.Queen(0, 6));

        assertTrue(calculator.canQueensAttackOneAnother());
    }

    //@Ignore("Remove to run test")
    @Test
    public void testNullPositionsNotAllowed() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator(null, new QueenAttackCalculator.Queen(0, 7)))
                .withMessage("You must supply valid positions for both Queens.");
    }

    //@Ignore("Remove to run test")
    @Test
    public void testQueensMustNotOccupyTheSameSquare() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new QueenAttackCalculator(new QueenAttackCalculator.Queen(2, 2), new QueenAttackCalculator.Queen(2, 2)))
                .withMessage("Queens cannot occupy the same position.");
    }

}
