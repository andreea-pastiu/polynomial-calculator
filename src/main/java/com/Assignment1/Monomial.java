package com.Assignment1;

public class Monomial
{
    private double coefficient;
    private int power;

    public Monomial(double coefficient, int power)
    {
        this.coefficient = coefficient;
        this.power = power;
    }

    public double getCoefficient()
    {
        return coefficient;
    }

    public void setCoefficient(double coefficient)
    {
        this.coefficient = coefficient;
    }

    public int getPower()
    {
        return power;
    }

    public void setPower(int power)
    {
        this.power = power;
    }
}
