package com.Assignment1.Models;

import com.Assignment1.DivisionResult;
import com.Assignment1.InvalidPolynomialException;
import com.Assignment1.Monomial;
import com.Assignment1.Polynomial;

import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalculatorModel
{
    private boolean polynomial1;
    private boolean polynomial2;

    public Polynomial add(Polynomial p1, Polynomial p2)
    {
        Polynomial result = new Polynomial();
        for (Monomial monomial : p1.getMonomial())
        {
            Monomial existing = p2.getMonomial().stream().filter(m -> m.getPower() == monomial.getPower()).findFirst().orElse(null);
            if(existing != null)
            {
                double coefficient = monomial.getCoefficient() + existing.getCoefficient();
                Monomial resultMonomial = new Monomial(coefficient, monomial.getPower());
                result.getMonomial().add(resultMonomial);
                p2.getMonomial().remove(existing);
            }
            else
            {
                result.getMonomial().add(monomial);
            }
        }
        for (Monomial monomial : p2.getMonomial())
        {
            result.getMonomial().add(monomial);
        }

        result = sortPolynomial(result);

        return result;
    }

    public Polynomial subtract(Polynomial p1, Polynomial p2)
    {
        Polynomial result = new Polynomial();
        for (Monomial monomial : p1.getMonomial())
        {
            Monomial existing = p2.getMonomial().stream().filter(m -> m.getPower() == monomial.getPower()).findFirst().orElse(null);
            if(existing != null)
            {
                double coefficient = monomial.getCoefficient() - existing.getCoefficient();
                Monomial resultMonomial = new Monomial(coefficient, monomial.getPower());
                if(resultMonomial.getCoefficient() != 0)
                {
                    result.getMonomial().add(resultMonomial);
                }
                p2.getMonomial().remove(existing);
            }
            else
            {
                result.getMonomial().add(monomial);
            }
        }
        for (Monomial monomial : p2.getMonomial())
        {
            monomial.setCoefficient((-1) * monomial.getCoefficient());
            result.getMonomial().add(monomial);
        }

        result = sortPolynomial(result);

        return result;
    }

    public Polynomial multiply(Polynomial p1, Polynomial p2)
    {
        Polynomial result = new Polynomial();
        for(Monomial monomial1 : p1.getMonomial())
        {
            for(Monomial monomial2 : p2.getMonomial())
            {
                Monomial resultMonomial = new Monomial(monomial1.getCoefficient()*monomial2.getCoefficient(), monomial1.getPower()+monomial2.getPower());
                Monomial existing = result.getMonomial().stream().filter(m -> m.getPower() == resultMonomial.getPower()).findFirst().orElse(null);
                if(existing != null)
                {
                    existing.setCoefficient(existing.getCoefficient() + resultMonomial.getCoefficient());
                }
                else
                {
                    result.getMonomial().add(resultMonomial);
                }
            }
        }
        result = sortPolynomial(result);
        return result;
    }

    public DivisionResult divide(Polynomial p1, Polynomial p2)
    {
        Polynomial quotient = new Polynomial();
        if(p1.getMonomial().get(0).getPower() < p2.getMonomial().get(0).getPower())
        {
            Polynomial temp = p1;
            p1 = p2;
            p2 = temp;
        }
        while (p1.getMonomial().get(0).getPower() >= p2.getMonomial().get(0).getPower())
        {
            int power = p1.getMonomial().get(0).getPower() - p2.getMonomial().get(0).getPower();
            double coefficient = p1.getMonomial().get(0).getCoefficient() / p2.getMonomial().get(0).getCoefficient();
            Monomial monomial = new Monomial(coefficient, power);
            Polynomial toMultiply = new Polynomial();
            toMultiply.getMonomial().add(monomial);
            quotient.getMonomial().add(monomial);
            Polynomial multiplied = multiply(toMultiply, p2);
            p1 = subtract(p1, multiplied);
        }
        quotient = sortPolynomial(quotient);
        p1 = sortPolynomial(p1);
        return new DivisionResult(quotient, p1);
    }

    public Polynomial derive(Polynomial p)
    {
        Polynomial result = new Polynomial();
        for(Monomial monomial : p.getMonomial())
        {
            Monomial resultMonomial = new Monomial(monomial.getCoefficient() * monomial.getPower(), monomial.getPower()-1);
            if(resultMonomial.getCoefficient() != 0)
            {
                result.getMonomial().add(resultMonomial);
            }
        }
        result = sortPolynomial(result);
        return result;
    }

    public Polynomial integrate(Polynomial p)
    {
        Polynomial result = new Polynomial();
        for(Monomial monomial : p.getMonomial())
        {
            Monomial resultMonomial;
            if(monomial.getPower() == 0)
            {
                resultMonomial = new Monomial(monomial.getCoefficient(), monomial.getPower()+1);
            }
            else
            {
                resultMonomial = new Monomial(monomial.getCoefficient() / (monomial.getPower()+1), monomial.getPower()+1);
            }
            result.getMonomial().add(resultMonomial);
        }
        result = sortPolynomial(result);
        return result;
    }

    public String toString(Polynomial polynomial)
    {
        String result = "";
        for (Monomial monomial : polynomial.getMonomial())
        {
            double coefficient = monomial.getCoefficient();
            int power = monomial.getPower();
            if(coefficient > 0 && !result.equals(""))
            {
                result += "+";
            }
            if(coefficient != 0)
            {
                if(Math.ceil(coefficient) == coefficient)
                {
                    if(coefficient != 1)
                    {
                        if(coefficient != -1)
                        {
                            result += (int) coefficient;
                        }
                        else
                        {
                            result += "-";
                        }
                    }
                }
                else
                {
                    result += String.format("%.02f", coefficient);
                }
                if(power != 0)
                {
                    result += "X";
                    if(power != 1)
                    {
                        result += "^";
                        result += power;
                    }
                }
            }
        }
        if(result.equals(""))
        {
            return "0";
        }
        else
        {
            return result;
        }
    }

    public Polynomial parsePolynomial(String polynomial) throws InvalidPolynomialException
    {
        if(polynomial == "")
        {
            throw new InvalidPolynomialException("Empty input");
        }
        String pattern = "((\\+|\\-|^)(\\d*))X{0,1}(\\^\\-{0,1}(\\d+))*";
        String matched = "";
        Pattern patternp = Pattern.compile(pattern);
        Matcher matcher = patternp.matcher(polynomial);
        Polynomial result = new Polynomial();
        while(matcher.find())
        {
            matched += matcher.group(0);
            String coefficient = matcher.group(1);
            String power = matcher.group(5);
            int parsedCoefficient = 1;
            if (coefficient != null && !coefficient.equals(""))
            {
                if (!(coefficient.equals("+") || coefficient.equals("-")))
                {
                    parsedCoefficient = Integer.parseInt(coefficient);
                } else
                {
                    if (coefficient.equals("-"))
                    {
                        parsedCoefficient = -1;
                    }
                }
            }
            int parsedPower = 1;
            if (power != null && power != "")
            {
                parsedPower = Integer.parseInt(power);
            } else
            {
                if (!matcher.group(0).contains("X"))
                {
                    parsedPower = 0;
                }
            }
            Monomial monomial = new Monomial(parsedCoefficient, parsedPower);
            if(parsedCoefficient != 0)
            {
                Monomial existing = result.getMonomial().stream().filter(m -> m.getPower() == monomial.getPower()).findFirst().orElse(null);
                if (existing == null)
                {
                    result.getMonomial().add(monomial);
                } else
                {
                    existing.setCoefficient(existing.getCoefficient() + monomial.getCoefficient());
                }
            }
        }
        if(!polynomial.equals(matched))
        {
            throw new InvalidPolynomialException("Polynomial \"" + polynomial + "\" is invalid!");
        }
        result = sortPolynomial(result);
        return result;
    }

    public Polynomial sortPolynomial(Polynomial polynomial)
    {
        Collections.sort(polynomial.getMonomial(), new Comparator<Monomial>(){
            public int compare(Monomial m1, Monomial m2){
                if(m1.getPower() == m2.getPower())
                    return 0;
                return m1.getPower() > m2.getPower() ? -1 : 1;
            }
        });
        return polynomial;
    }

    public boolean isPolynomial1()
    {
        return polynomial1;
    }

    public void setPolynomial1(boolean polynomial1)
    {
        this.polynomial1 = polynomial1;
    }

    public boolean isPolynomial2()
    {
        return polynomial2;
    }

    public void setPolynomial2(boolean polynomial2)
    {
        this.polynomial2 = polynomial2;
    }
}
