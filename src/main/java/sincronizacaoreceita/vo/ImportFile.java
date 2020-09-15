/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sincronizacaoreceita.vo;

import java.io.Serializable;

/**
 * Representação do CSV
 * @author Jefferson Rodrigues
 */
public class ImportFile implements Serializable{
    
    private String agencia;
    private String conta;
    private Double saldo;
    private String status;
    private Boolean retorno;

    public void addLine(String[] line){
        agencia = line[0];
        conta=line[1].replace("-", "");
        saldo=Double.parseDouble(line[2].replace(",", "."));
        status=line[3];
    }
    public String getAgencia() {
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getRetorno() {
        return retorno;
    }

    public void setRetorno(Boolean retorno) {
        this.retorno = retorno;
    }
    
    /**
     * Cabeçario do CSV processado pelo sistema
     * @return 
     */
    public static String[] header(){
        return new String[]{"agencia","conta","saldo","status","retorno_receita"};
    }
    
}
