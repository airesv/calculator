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
    boolean temPonto;
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
        temPonto = false;
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

        //Se estivermos a trabalhar com um resultado anterior
        //só entra se colocarmos um operador
        if (calculado) {

            if (isOperador(str) && !isDot(str)) {
                expressao += str;
                operador++;
                calculado = false;
                temPonto = false;
            }
        } else {
            
            //Se a caixa está vazia (sem resultados)
            if(vazio){
                
                //verifica se o inserido é um operador e não deixa
                if (isOperador(str)) {
                    expressao = "0";
                }
                //Verifica se é um ponto, se for acrescenta à String e muda a boleana que indica se já foi inserido um ponto
                else if (isDot(str)) {
                    expressao += ".";
                    temPonto=true;
                }
            
            }

            //Se não está vazio
            else{
                //Caso é ponto e não tem ponto, acrescenta e troca a boleana
                //caso contrário, não modifica a expressão
                if (isDot(str)) {

                        if (!temPonto) {
                            expressao += str;
                            vazio = false;
                            temPonto = true;
                        } 
                    }
                //Caso é operador e não tem operador, acrescenta, incrementa o contador de operador e troca a boleana
                //caso contrário, não modifica a expressão
                else if (isOperador(str)){
                    if (operador < 1) {
                        expressao += str;
                        operador++;
                        temPonto=false;
                    } 
                }
                
                //Caso não seja operador nem ponto
                else if(!isOperador(str) && !isDot(str)) {

                    expressao += str;
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
        //Como o resultado apresenta sempre um double, partimos do principio que 
        //o ponto já existe no número.
        calculado = true;
        temPonto = true;

        //Cria
        ArrayList<Double> numeros = new ArrayList<>();
        ArrayList<String> operacoes = new ArrayList<>();

        StringTokenizer num = new StringTokenizer(str, "+-*/");
        StringTokenizer op = new StringTokenizer(str, ".0123456789");

        int tokenNum = num.countTokens();
        int tokenOp = op.countTokens();

        while (num.hasMoreTokens()) {

            numeros.add(Double.valueOf(num.nextToken()));
        }

        while (op.hasMoreTokens()) {
            operacoes.add(op.nextToken());
        }

        //Verifica se o numero de tokens de operadores é igual ao de numeros,
        //se for é porque estamos perante uma de duas situações:
        //1 - O 1º numero é negativo se o 1º operador da lista for "-"
        //2 - O ultimo operador foi inserido sem estar seguido de um número.
        if (tokenNum == tokenOp) {

            if (operacoes.get(0).equals("-")) {

                //transforma o 1º numero da lista em negativo e apaga o 1º operador da lista
                numeros.set(0, numeros.get(0) * -1);
                operacoes.remove(0);
            } else {

                //O ultimo operador não foi precedido de numero, portanto deve 
                //ser apagado antes de concluir o cálculo da expressão inserida na máquina de calcular.
                operacoes.remove(tokenOp - 1);
            }
        } //Quando o numero de operadores é maior do que o de números só podemos estar perante uma situação
        //o 1º numero é negativo e a expressão inserida termina com um operador.
        else if (tokenNum < tokenOp) {

            numeros.set(0, numeros.get(0) * -1);
            operacoes.remove(tokenOp - 1);
            operacoes.remove(0);

        }

        double conta;

        int i = 0;

        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("/")) {

                conta = (numeros.get(i)) / (numeros.get(i + 1));

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

                conta = (numeros.get(i)) * (numeros.get(i + 1));

                numeros.set(i + 1, conta);
                numeros.remove(i);
                operacoes.remove(i);

            } else {
                i++;
            }
        }

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("-")) {

                conta = (numeros.get(i)) - (numeros.get(i + 1));

                numeros.set(i + 1, conta);
                numeros.remove(i);
                operacoes.remove(i);

            } else {
                i++;
            }
        }

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("+")) {

                conta = (numeros.get(i)) + (numeros.get(i + 1));

                numeros.set(i + 1, conta);
                numeros.remove(i);
                operacoes.remove(i);

            } else {
                i++;
            }
        }
        //guarda o valor caso necessite para continuar
        aux = (numeros.get(0));
        return aux;
    }

}
