package PaymenManager;

import java.time.LocalDate;

public interface PaymentService {
    boolean pay(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price);
    public boolean checkDataFormat(String cvv, LocalDate scadenza, String intestatario, String numeroCarta, int price);

}
