package com.Assignment1;

import java.util.ArrayList;

public class Polynomial
{
    private ArrayList<Monomial> monomial;

    public Polynomial()
    {
        this.monomial = new ArrayList<>();
    }

    public Polynomial(ArrayList<Monomial> monomial)
    {
        this.monomial = monomial;
    }

    public ArrayList<Monomial> getMonomial()
    {
        return monomial;
    }

    public void setMonomial(ArrayList<Monomial> monomial)
    {
        this.monomial = monomial;
    }
}
