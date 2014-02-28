/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Aires
 */
@ManagedBean(name = "Calculator")
@SessionScoped
public class Calculator {

    String expressao;
    boolean calculado;
    Double aux;
    boolean vazio;
    int operador;

    /**
     * Creates a new instance of Calculator
     */
    public Calculator() {
        expressao = "0";
        calculado = false;
        aux = 0.0;
        vazio = true;
        operador = 0;
    }

    public String getExpressao() {
        return expressao;
    }

    private boolean isOperador(String st) {
        return st.equals("*") || st.equals("+") || st.equals("/") || st.equals("-") || st.equals(".");
    }

    private boolean isDivision(String st) {
        return st.equals("/");
    }

    private boolean isDot(String st) {
        return st.equals(".");
    }

    public void recebe(String str) {

        if (calculado) {

            if (isOperador(str)) {
                expressao += str;
                operador++;
                calculado = false;
            }
        } else {
            if (vazio && isOperador(str)) {
                expressao = "0";
            } else if (vazio && !isOperador(str)) {
                expressao = str;
                vazio = false;
            } else {

                if (isOperador(str) && operador < 1) {
                    expressao += str;
                    operador++;
                } else if (!isOperador(str)) {

                    expressao += str;
                    operador = 0;
                }
            }
        }
    }

    public void limpa() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
        //expressao = "0";
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public void resultado() {
        //expressao += " = " + calcula("1/0");
        String exp = "" + calcula(expressao);

        if (Double.isInfinite(calcula(expressao))) {

            expressao = "Error! Can´t divide by zero";

        } else {
            expressao = "" + calcula(expressao);
        }

    }
    

    private double calcula(String str) {
        //Verifica se foi realizado o calculo
        calculado = true;

        //Cria
        ArrayList<Double> numeros = new ArrayList<>();
        ArrayList<String> operacoes = new ArrayList<>();

        StringTokenizer num = new StringTokenizer(str, "+-*/");
        StringTokenizer op = new StringTokenizer(str, ".0123456789");
        
        int tokenNum=num.countTokens();
        int tokenOp=op.countTokens();

        while (num.hasMoreTokens()) {
            
            numeros.add(Double.valueOf(num.nextToken()));
        }

        while (op.hasMoreTokens()) {
            operacoes.add(op.nextToken());
        }
        
        //Condição que verifica se o 1º token das operações é o sinal negativo,
        //Se for, remove esse sinal e trata-o como equação com negativos, se não for
        //remove o... (ainda falta verificar se o último caracter inserido é uma operação, se assim for tem de se remover)
        if(tokenNum==tokenOp){
        
            if(operacoes.get(0).equals("-")){
            
                numeros.set(0, numeros.get(0)*-1);
                operacoes.remove(0);
            }
        }

        double conta;

        int i = 0;

        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("/")) {

                    conta = (numeros.get(i)) /  (numeros.get(i + 1));
                
                    
                    numeros.set(i + 1, conta);
                    numeros.remove(i);
                    operacoes.remove(i);
                
                
            } else {
                i++;
            }
        }
        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("*")) {

                    conta =  (numeros.get(i)) *  (numeros.get(i + 1));

                
                numeros.set(i + 1, conta );
                numeros.remove(i);
                operacoes.remove(i);
                
            } else {
                i++;
            }
        }

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("-")) {
                
                     conta =  (numeros.get(i)) -  (numeros.get(i + 1));
 
                numeros.set(i + 1, conta );
                numeros.remove(i);
                operacoes.remove(i);
                
            } else {
                i++;
            }
        }

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("+")) {

                    conta =  (numeros.get(i)) +  (numeros.get(i + 1));

                numeros.set(i + 1, conta);
                numeros.remove(i);
                operacoes.remove(i);
               
            } else {
                i++;
            }
        }
        //guarda o valor caso necessite para continuar
        aux =  (numeros.get(0));
        return aux;
    }

}
