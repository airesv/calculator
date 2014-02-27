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

    /**
     * Creates a new instance of Calculator
     */
    public Calculator() {
        expressao = "";

    }

    public String getExpressao() {
        return expressao;
    }

    public void recebe(String str) {
        
        
        
        expressao += str;
    }

    public void limpa() {
        expressao = "";
    }

    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }

    public void resultado() {
//        if ((expressao != null)) {
//            //invalidate user session
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//            session.invalidate();
//
//            return 
        expressao += " = " + calcula(expressao);
//        } else {
//            return "";
//        }
        //return expressao;

    }

    public void resultado2() {
//        StringWriter writer = new StringWriter();
//        PrintWriter out = new PrintWriter(writer);
//        out.println(expressao);
//        String output = writer.toString();
//
//        expressao += " = " + output;

    }

//    public String getResponse() {
//        if ((expressao != null)) {
//
//            //invalidate user session
//            FacesContext context = FacesContext.getCurrentInstance();
//            HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
//            session.invalidate();
//
//            return "Resultado";
//        } else {
//
//            return "<p>Sorry, </p>";
//        }
//    }
    private double calcula(String str) {
        ArrayList<String> numeros = new ArrayList<>();
        ArrayList<String> operacoes = new ArrayList<>();

        StringTokenizer num = new StringTokenizer(str, "+-*/");
        StringTokenizer op = new StringTokenizer(str, ".0123456789");

        while (num.hasMoreTokens()) {
            numeros.add(num.nextToken());
        }

        while (op.hasMoreTokens()) {
            operacoes.add(op.nextToken());
        }

        double conta;

        int i = 0;

        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("/")) {
                conta = Double.parseDouble(numeros.get(i)) / Double.parseDouble(numeros.get(i + 1));
                numeros.set(i + 1, conta + "");
                numeros.remove(i);
                operacoes.remove(i);
            } else {
                i++;
            }
        }
        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("*")) {
                conta = Double.parseDouble(numeros.get(i)) * Double.parseDouble(numeros.get(i + 1));
                numeros.set(i + 1, conta + "");
                numeros.remove(i);
                operacoes.remove(i);
            } else {
                i++;
            }
        };

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("-")) {

                conta = Double.parseDouble(numeros.get(i)) - Double.parseDouble(numeros.get(i + 1));
                numeros.set(i + 1, conta + "");
                numeros.remove(i);
                operacoes.remove(i);
            } else {
                i++;
            }
        }

        i = 0;
        while (i < operacoes.size()) {
            if (operacoes.get(i).equals("+")) {
                conta = Double.parseDouble(numeros.get(i)) + Double.parseDouble(numeros.get(i + 1));
                numeros.set(i + 1, conta + "");
                numeros.remove(i);
                operacoes.remove(i);
            } else {
                i++;
            }
        }

        return Double.parseDouble(numeros.get(0));
    }

}
