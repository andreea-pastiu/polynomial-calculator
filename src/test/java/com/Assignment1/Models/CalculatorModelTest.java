package com.Assignment1.Models;

import com.Assignment1.DivisionResult;
import com.Assignment1.InvalidPolynomialException;
import com.Assignment1.Monomial;
import com.Assignment1.Polynomial;
import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorModelTest
{
    CalculatorModel model = new CalculatorModel();
    private Boolean comparePolynomials(Polynomial p1, Polynomial p2)
    {
        Boolean result = true;
        p1 = model.sortPolynomial(p1);
        p2 = model.sortPolynomial(p2);
        if(p1.getMonomial().size() != p2.getMonomial().size())
        {
            result = false;
        }
        else
        {
            for (Monomial monomial: p1.getMonomial())
            {
                Monomial existing = p2.getMonomial().stream().filter(m -> m.getPower() == monomial.getPower()).findFirst().orElse(null);
                if(existing == null)
                {
                    result = false;
                    break;
                }
                else
                {
                    if(monomial.getCoefficient() != existing.getCoefficient())
                    {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }
    @Test
    public void test_add_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p1 = new Polynomial();
        p1.getMonomial().add(new Monomial(1, 2));
        p1.getMonomial().add(new Monomial(7, 1));
        Polynomial p2 = new Polynomial();
        p2.getMonomial().add(new Monomial(3, 1));
        p2.getMonomial().add(new Monomial(5, 3));
        p2.getMonomial().add(new Monomial(23, 0));
        Polynomial sum = model.add(p1, p2);
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(5, 3));
        expected.getMonomial().add(new Monomial(1, 2));
        expected.getMonomial().add(new Monomial(10, 1));
        expected.getMonomial().add(new Monomial(23, 0));
        assertTrue(comparePolynomials(sum, expected));
    }
    @Test
    public void test_subtract_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p1 = new Polynomial();
        p1.getMonomial().add(new Monomial(-5, 0));
        p1.getMonomial().add(new Monomial(1, 4));
        p1.getMonomial().add(new Monomial(-2, 1));
        p1.getMonomial().add(new Monomial(3, 3));
        Polynomial p2 = new Polynomial();
        p2.getMonomial().add(new Monomial(4, 4));
        p2.getMonomial().add(new Monomial(2, 2));
        p2.getMonomial().add(new Monomial(-20, 0));
        p2.getMonomial().add(new Monomial(1, 3));
        Polynomial difference = model.subtract(p1, p2);
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-3, 4));
        expected.getMonomial().add(new Monomial(2, 3));
        expected.getMonomial().add(new Monomial(-2, 2));
        expected.getMonomial().add(new Monomial(-2, 1));
        expected.getMonomial().add(new Monomial(15, 0));
        assertTrue(comparePolynomials(difference, expected));
    }
    @Test
    public void test_multiply_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p1 = new Polynomial();
        p1.getMonomial().add(new Monomial(-2, 0));
        p1.getMonomial().add(new Monomial(3, 1));
        p1.getMonomial().add(new Monomial(-7, 3));
        p1.getMonomial().add(new Monomial(456, 2));
        Polynomial p2 = new Polynomial();
        p2.getMonomial().add(new Monomial(4, 3));
        p2.getMonomial().add(new Monomial(-5, 1));
        Polynomial product = model.multiply(p1, p2);
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-28, 6));
        expected.getMonomial().add(new Monomial(1824, 5));
        expected.getMonomial().add(new Monomial(47, 4));
        expected.getMonomial().add(new Monomial(-2288, 3));
        expected.getMonomial().add(new Monomial(-15, 2));
        expected.getMonomial().add(new Monomial(10, 1));
        assertTrue(comparePolynomials(product, expected));
    }
    @Test
    public void test_divide_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p1 = new Polynomial();
        p1.getMonomial().add(new Monomial(-7, 3));
        p1.getMonomial().add(new Monomial(456, 2));
        p1.getMonomial().add(new Monomial(3, 1));
        p1.getMonomial().add(new Monomial(-2, 0));
        Polynomial p2 = new Polynomial();
        p2.getMonomial().add(new Monomial(4, 3));
        p2.getMonomial().add(new Monomial(-5, 1));
        DivisionResult result = model.divide(p1, p2);
        Polynomial expectedQuotient = new Polynomial();
        Polynomial expectedRemainder = new Polynomial();
        expectedQuotient.getMonomial().add(new Monomial(-1.75, 0));
        expectedRemainder.getMonomial().add(new Monomial(456, 2));
        expectedRemainder.getMonomial().add(new Monomial(-5.75, 1));
        expectedRemainder.getMonomial().add(new Monomial(-2, 0));
        assertTrue(comparePolynomials(result.getQuotient(), expectedQuotient));
        assertTrue(comparePolynomials(result.getRemainder(), expectedRemainder));
    }
    @Test
    public void test_derive_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p = new Polynomial();
        p.getMonomial().add(new Monomial(-2, 0));
        p.getMonomial().add(new Monomial(3, 1));
        p.getMonomial().add(new Monomial(-7, 3));
        p.getMonomial().add(new Monomial(456, 2));
        Polynomial derived = model.derive(p);
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-21, 2));
        expected.getMonomial().add(new Monomial(912, 1));
        expected.getMonomial().add(new Monomial(3, 0));
        assertTrue(comparePolynomials(derived, expected));
    }
    @Test
    public void test_integrate_returnsCorrectValue() throws InvalidPolynomialException
    {
        Polynomial p = new Polynomial();
        p.getMonomial().add(new Monomial(-2, 0));
        p.getMonomial().add(new Monomial(3, 1));
        p.getMonomial().add(new Monomial(-7, 3));
        p.getMonomial().add(new Monomial(456, 2));
        Polynomial integrated = model.integrate(p);
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-1.75, 4));
        expected.getMonomial().add(new Monomial(152, 3));
        expected.getMonomial().add(new Monomial(1.5, 2));
        expected.getMonomial().add(new Monomial(-2, 1));
        assertTrue(comparePolynomials(integrated, expected));
    }
    @Test
    public void test_toString_transformsCorrectly()
    {
        Polynomial p = new Polynomial();
        p.getMonomial().add(new Monomial(-2, 0));
        p.getMonomial().add(new Monomial(3, 1));
        p.getMonomial().add(new Monomial(-7, 3));
        p.getMonomial().add(new Monomial(456, 2));
        String transformedString = model.toString(p);
        String expected = "-2+3X-7X^3+456X^2";
        assertEquals(transformedString, expected);
    }
    @Test
    public void test_parsePolynomial_parsesPositiveCoefficients() throws InvalidPolynomialException
    {
        Polynomial p = model.parsePolynomial("5X^2+6X+3");
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(5, 2));
        expected.getMonomial().add(new Monomial(6, 1));
        expected.getMonomial().add(new Monomial(3, 0));
        assertTrue(comparePolynomials(p, expected));
    }
    @Test
    public void test_parsePolynomial_parsesNegativeCoefficients() throws InvalidPolynomialException
    {
        Polynomial p = model.parsePolynomial("-8X^5-10X-7");
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-8, 5));
        expected.getMonomial().add(new Monomial(-10, 1));
        expected.getMonomial().add(new Monomial(-7, 0));
        assertTrue(comparePolynomials(p, expected));
    }
    @Test
    public void test_parsePolynomial_parsesPositiveAndNegativeCoefficients() throws InvalidPolynomialException
    {
        Polynomial p = model.parsePolynomial("-9X^3-6X");
        Polynomial expected = new Polynomial();
        expected.getMonomial().add(new Monomial(-9, 3));
        expected.getMonomial().add(new Monomial(-6, 1));
        assertTrue(comparePolynomials(p, expected));
    }
    @Test (expected = InvalidPolynomialException.class)
    public void test_parsePolynomial_throwsInvalidPolynomialException() throws InvalidPolynomialException
    {
        Polynomial p = model.parsePolynomial("-9XX^3-6x");

    }
}