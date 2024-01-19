package PaymenManager;

import java.time.LocalDate;
import java.util.Random;

public class Bank {

    public Bank(){}
    public boolean pay(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price){

        Random rand = new Random();
        int n = 1;
        int numeroGenerato = rand.nextInt((10 - 1) + 1) + 1;

        if(numeroGenerato == n)
            return false;
        else
            return true;
    }

    public boolean checkDataFormat(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price){

        boolean check = true;

        //Verifica che il prezzo sia valido
        if(price <= 0)
            check = false;

        // Verifica che il CVV sia un numero a tre cifre
        if(cvv == null || !cvv.matches("\\d{3}"))
            check = false;

        // Verifica che la data di scadenza sia nel futuro
        if(scadenza == null || !scadenza.isAfter(LocalDate.now()))
            check = false;

        // Verifica che l'intestatario contenga solo caratteri validi (alfanumerici)
        if(intestatario == null || !intestatario.matches("[a-zA-Z\\s]+"))
            check = false;

        // Verifica che il numero della carta contenga solo cifre e che abbia il formato corretto
        if(numeroCarta == null || !numeroCarta.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"))
            check = false;

        return check;

    }



}
