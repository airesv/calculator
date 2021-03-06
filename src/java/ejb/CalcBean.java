/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.ejb.Stateless;

/**
 *
 * @author Orlando Neves
 * @author Vitor Aires
 */
@Stateless
public class CalcBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    String expressao;
    boolean calculado;
    boolean temPonto;
    Double aux;

    public CalcBean() {

        expressao = "0";
        calculado = false;
        aux = 0.0;
        temPonto = false;

    }

    
    private String simplifica(String txt) {
        //tranforma a string num arraylist de char
        ArrayList<Character> simbolo = new ArrayList();
        for (int i = 0; i < txt.length(); i++) {
            simbolo.add(txt.charAt(i));
        }

        boolean mudou;
        int i = 0;

        while (i < simbolo.size() - 1) {

            mudou = false;

            if (simbolo.get(i) == '+' && simbolo.get(i + 1) == '-') {
                simbolo.set(i, '-');
                simbolo.remove(i + 1);
                mudou = true;
            }
            if (simbolo.get(i) == '+' && simbolo.get(i + 1) == '+') {
                simbolo.set(i, '+');
                simbolo.remove(i + 1);
                mudou = true;
            }

            if (simbolo.get(i) == '-' && simbolo.get(i + 1) == '-') {
                simbolo.set(i, '+');
                simbolo.remove(i + 1);
                mudou = true;
            }
            if (simbolo.get(i) == '-' && simbolo.get(i + 1) == '+') {
                simbolo.set(i, '-');
                simbolo.remove(i + 1);
                mudou = true;
            }

            if (mudou == false) {
                i++;
            }

        }

        txt = "";
        for (Character character : simbolo) {
            txt += character;
        }
        return txt;
    }

    /**
     * 
     * Realiza a calculo da expressão inserida pelo utiulizador
     * 
     * @param str
     * @return 
     */
    public double calcula(String str) {
        //Verifica se foi realizado o calculo
        //Como o resultado apresenta sempre um double, partimos do principio que 
        //o ponto já existe no número.
        calculado = true;
        temPonto = true;
        String expDada = simplifica(str);

        
        ArrayList<Double> numeros = new ArrayList<>();
        ArrayList<String> operacoes = new ArrayList<>();

        StringTokenizer num = new StringTokenizer(expDada, "+-*/");
        StringTokenizer op = new StringTokenizer(expDada, ".0123456789");

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
        
        
        //simplifica a expressão no caso de aparecer a 
        //multiplicação de numeros de sinais contrário

        int i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("*-")) {
                numeros.set(i + 1, numeros.get(i + 1) * -1);
                operacoes.set(i, "*");

            }
            if (operacoes.get(i).equals("/-")) {
                numeros.set(i + 1, numeros.get(i + 1) * -1);
                operacoes.set(i, "/");

            }
            i++;
        }

        double conta;

        i = 0;

        //realiza as operações por ordem de prioridade
        //Em cada calculo realizado com sucesso, os arraylist dos numeros e operadores vão diminuindo
        //No caso do arraylist dos numeros, cada calculo envolve dois numero, após do alculo o primeiro
        //numero recebe o calculo efetuado, o osegundo é apagado.
        //No cado do arraylist de operadores, em cada calculo realizado a operação é eliminda.
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
        //guarda o valor caso necessite para continuar a operação
        aux = (numeros.get(0));
        return aux;
    }

    /**
     * Devolve  a expressã0
     * @return 
     */
    public String getExpressao() {
        return expressao;
    }

    /**
     * Atualiza a expressao
     * @param expressao a escrever na Calculadora
     */
    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }
/**
 * Verifica se o numero escrito tem  ponto.
 * @return True, se o caracter é ponto, false caso contrário
 */
    public boolean isTemPonto() {
        return temPonto;
    }

    /**
     * Informa se o numero escrito tem ponto
     * @param temPonto True, se o caracter é ponto, false caso contrário
     */
    public void setTemPonto(boolean temPonto) {
        this.temPonto = temPonto;
    }

}
