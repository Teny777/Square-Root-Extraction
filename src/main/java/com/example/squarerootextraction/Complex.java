package com.example.squarerootextraction;

import java.math.BigDecimal;
import java.math.MathContext;

public class Complex {
    private BigDecimal real;
    private BigDecimal imaginary;

    public Complex(BigDecimal real, BigDecimal imaginary){
        this.real = real;
        this.imaginary = imaginary;

    }

    public BigDecimal getReal(){
        return real;
    }

    public BigDecimal getImaginary(){
        return imaginary;
    }

    public Complex add(Complex number) {
        return new Complex(
                this.getReal().add(number.getReal()),
                this.getImaginary().add(number.getImaginary())
        );
    }

    public Complex subtract(Complex number) {
        return new Complex(
                this.getReal().subtract(number.getReal()),
                this.getImaginary().subtract(number.getImaginary())
        );
    }

    public Complex multiply(Complex number) {
        return new Complex(
                this.getReal().multiply(number.getReal()).subtract(this.getImaginary().multiply(number.getImaginary())),
                this.getImaginary().multiply(number.getReal()).add(this.getReal().multiply(number.getImaginary()))
        );
    }

    public static BigDecimal abs(Complex number) {
        return (number.getReal().multiply(number.getReal()).add(number.getImaginary().multiply(number.getImaginary()))).sqrt(new MathContext(10));
    }

    @Override
    public String toString(){
        if (CheckZero(real) && CheckZero(imaginary)) return "0";
        return String.format("%s%s", CheckZero(real) ? "" : real, CheckZero(imaginary) ? "" : ((imaginary.compareTo(BigDecimal.valueOf(0)) > 0 && !CheckZero(real) ? "+" + imaginary:imaginary) + "i"));
    }


    private boolean CheckZero(BigDecimal value){
        return value.abs().compareTo(BigDecimal.valueOf(Math.pow(0.1,7))) < 0;
    }

}
