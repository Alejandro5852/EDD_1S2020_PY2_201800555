/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Objetos;

/**
 *
 * @author alejandro
 */
public class Bloque {

    private int INDEX, NONCE;
    private String TIMESTAMP;
    private String DATA;
    private String PREVIOUSHASH;
    private String HASH;

    public Bloque(int INDEX, String TIMESTAMP, String DATA) {
        this.INDEX = INDEX;
        this.TIMESTAMP = TIMESTAMP;
        this.DATA = DATA;
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

    public String getDATA() {
        return DATA;
    }

    public void setDATA(String DATA) {
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

}
