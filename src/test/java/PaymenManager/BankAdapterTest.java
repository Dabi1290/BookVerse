package PaymenManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BankAdapterTest {
    private BankAdapter b;
     @BeforeEach
    void setup(){
        b = new BankAdapter();
    }
    @Test
    void checkDataFormatOk() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertTrue(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatErrorCardNumber() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatErrorIntestatario() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "1234";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatErrorData() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2010,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatErrorCvv() {
        int price=50;
        String cvv="1234";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatErrorPrice() {
        int price=-1;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
}