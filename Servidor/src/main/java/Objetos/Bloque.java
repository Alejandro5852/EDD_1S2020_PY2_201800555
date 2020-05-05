/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import com.google.gson.JsonObject;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alejandro
 */
public class Bloque {

    private int INDEX, NONCE;
    private String TIMESTAMP;
    private SimpleMenteEnlazada DATA;
    private String PREVIOUSHASH;
    private String HASH;

    public Bloque(int INDEX, SimpleMenteEnlazada DATA, String PREVIOUSHASH) throws NoSuchAlgorithmException {
        this.INDEX = INDEX;
        this.DATA = DATA;
        this.PREVIOUSHASH = PREVIOUSHASH;
        llenarElRestoDeCampos();
    }

    public int getINDEX() {
        return INDEX;
    }

    public void setINDEX(int INDEX) {
        this.INDEX = INDEX;
    }

    public int getNONCE() {
        return NONCE;
    }

    public void setNONCE(int NONCE) {
        this.NONCE = NONCE;
    }

    public String getTIMESTAMP() {
        return TIMESTAMP;
    }

    public void setTIMESTAMP(String TIMESTAMP) {
        this.TIMESTAMP = TIMESTAMP;
    }

    public SimpleMenteEnlazada getDATA() {
        return DATA;
    }

    public void setDATA(SimpleMenteEnlazada DATA) {
        this.DATA = DATA;
    }

    public String getPREVIOUSHASH() {
        return PREVIOUSHASH;
    }

    public void setPREVIOUSHASH(String PREVIOUSHASH) {
        this.PREVIOUSHASH = PREVIOUSHASH;
    }

    public String getHASH() {
        return HASH;
    }

    public void setHASH(String HASH) {
        this.HASH = HASH;
    }

    public String encriptar(String concatenacion) throws NoSuchAlgorithmException {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        byte[] hash = md.digest(concatenacion.getBytes());
        StringBuffer sb = new StringBuffer();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public String getData() {
        String objetoJSON = "\t\"DATA\":[\n\t\t{\n";
        for (int i = 0; i < this.DATA.Tamaño(); i++) {
            Operacion op = (Operacion) this.DATA.at(i);
            objetoJSON += op.paraJSON();
            if (i < (this.DATA.Tamaño() - 1)) {
                objetoJSON+=",\n";
            }else{
                objetoJSON+="\n";
            }
        }
        objetoJSON += "\t\t}\n\t],\n";
        return objetoJSON;
    }
    public String getJson(){
        String JSON = "{\n";
        JSON+="\t\"INDEX\":"+getINDEX()+",\n";
        JSON+="\t\"TIMESTAMP\":\""+getTIMESTAMP()+"\",\n";
        JSON+="\t\"NONCE\":"+getNONCE()+",\n";
        JSON+=getData();
        JSON+="\t\"PREVIOUSHASH\":\""+getPREVIOUSHASH()+"\",\n";
        JSON+="\t\"HASH\":\""+getHASH()+"\"\n}";
        return JSON;
    }
    public String hash(String entrada) throws NoSuchAlgorithmException {
        String encriptado = "";
        int nonce = 0;
        encriptado = encriptar(entrada + nonce);
        while (true) {
            if (encriptado.charAt(0) == '0' && encriptado.charAt(1) == '0' && encriptado.charAt(2) == '0' && encriptado.charAt(3) == '0') {
                break;
            } else {
                nonce++;
                System.out.println("Vuelvo a encriptar: " + nonce);
                encriptado = encriptar(encriptado + nonce);
            }
        }
        this.setNONCE(nonce);
        return encriptado;
    }

    public void llenarElRestoDeCampos() throws NoSuchAlgorithmException {
        int nonce = 0;
        Calendar fecha = Calendar.getInstance();
        String hora = new SimpleDateFormat("dd-MM-yyyy::HH:mm:ss").format(fecha.getTime());
        String encriptado = encriptar(INDEX + hora + PREVIOUSHASH + getData() + nonce);
        while (true) {
            if (encriptado.charAt(0) == '0' && encriptado.charAt(1) == '0' && encriptado.charAt(2) == '0' && encriptado.charAt(3) == '0') {
                break;
            } else {
                nonce++;
                hora = new SimpleDateFormat("dd-MM-yyyy::HH:mm:ss").format(fecha.getTime());
                System.out.println("Vuelvo a encriptar: " + nonce);
                encriptado = encriptar(INDEX + hora + PREVIOUSHASH + getData() + nonce);
            }
        }
        this.setHASH(encriptado);
        this.setNONCE(nonce);
        this.setTIMESTAMP(hora);
    }
}
