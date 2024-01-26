package unitTesting.PaymenManager;

import PaymenManager.BankAdapter;
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
    void checkDataFormat_RP1_FC1_VS1_FI1_VN1() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertTrue(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormatRP1_FC1_VS1_FI1_VN2() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormat_RP1_FC1_VS1_FI2_VN1() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "1234";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormat_RP1_FC1_VS2_FI1_VN1() {
        int price=50;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2010,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormat_RP1_FC2_VS1_FI1_VN1() {
        int price=50;
        String cvv="1234";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
    @Test
    void checkDataFormat_RP2_FC1_VS1_FI1_VN1() {
        int price=-1;
        String cvv="123";
        LocalDate deadline= LocalDate.of(2030,1,1);
        String intestatario= "Pippo";
        String numCard= "1234-1234-1234-1234";
        assertFalse(b.checkDataFormat(cvv,deadline,intestatario,numCard,price));
    }
}