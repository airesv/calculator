/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import ejb.CalcBean;
import javax.ejb.EJB;
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
    boolean calculado;
    boolean temPonto;
    Double aux;
    boolean menos;
    boolean vazio;
    int operador;
    String resultado;
    String ex;
    
    @EJB
    CalcBean cb;

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
        menos=false;
        
    }

    public String getExpressao() {
        
        if(20<expressao.length()){
            
            ex=expressao.substring(0, 20);
        
            return ex;
        }
        else{
            return expressao;
        }
    }

    private boolean isOperador(String st) {
        return st.equals("*") || st.equals("+") || st.equals("/") || st.equals("-") || st.equals(".");
    }

    private boolean isDivision(String st) {
        return st.equals("/");
    }
    
    private boolean isMenos (String st){
        return st.equals("-");
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
                
                //verifica se o inserido é um operador e não deixa.
                if (isOperador(str)) {
                    
                    if(isMenos(str)){
                        expressao = "-";
                        operador++;
                        vazio=false;
                    }
                    else{
                        expressao = "0";

                    }
                }
                
                //Verifica se é um ponto, se for acrescenta à String e muda a 
                //boleana que indica se já foi inserido um ponto.
                if (isDot(str)) {
                    expressao += ".";
                    temPonto=true;
                    vazio=false;
                }
                else{
                     expressao = str;
                     vazio=false; 
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
                    else if(isMenos(str)){
                    
                            if (operador < 2) {
                            expressao += str;
                            operador++;
                            temPonto=false;
                        }
                    }
                }
                
                //Caso não seja operador nem ponto
                else if(!isOperador(str) && !isDot(str)) {
                    
                    expressao += str;
                    operador=0;
                }
            }
        }
    }
    

    public void limpa() {

        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        session.invalidate();
    }

    
    //método chamado pelo botão "="
    public void resultado() {
        
        calculado=true;
        
        if (Double.isInfinite(cb.calcula(expressao))) {

            expressao = "Error! Can´t divide by zero";

        }
        else if(expressao==null || expressao.isEmpty()){
        
            expressao=""+0;
        }
        
        else {
            expressao = "" + cb.calcula(expressao);
        }

    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
  
    public void setExpressao(String expressao) {
        this.expressao = expressao;
    }
}
