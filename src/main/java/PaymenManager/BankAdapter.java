package PaymenManager;

import java.time.LocalDate;

public class BankAdapter implements PaymentService{

    private final Bank bank;

    public BankAdapter(Bank bank){
        this.bank = bank;
    }

    @Override
    public boolean pay(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price) {
        return bank.pay(cvv, scadenza, intestatario, numeroCarta, price);
    }

    @Override
    public boolean checkDataFormat(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price) {
        return bank.checkDataFormat(cvv, scadenza, intestatario, numeroCarta, price);
    }
}
