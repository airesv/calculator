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
 * @author Zueb LDA
 */
@Stateless
public class CalcBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
    String expressao;
    boolean calculado;
    boolean temPonto;
    Double aux;
    
   public CalcBean(){
       
        expressao = "0";
        calculado = false;
        aux = 0.0;
        temPonto = false;
    
    }

    public double calcula(String str) {
        //Verifica se foi realizado o calculo
        //Como o resultado apresenta sempre um double, partimos do principio que 
        //o ponto já existe no número.
        calculado = true;
        temPonto = true;


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

    public String getExpressao() {
        return expressao;
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public boolean isTemPonto() {
        return temPonto;
    }

    public void setTemPonto(boolean temPonto) {
        this.temPonto = temPonto;
    }
   
}
