package proposalManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class VersionTest {

    private Version v;
    private File report,cover,ebook;
    private LocalDate data;

    @BeforeEach
    void setUp() {
        v = new Version();
        report = Mockito.mock(File.class);
        Mockito.when(report.getAbsolutePath()).thenReturn("pippo.pdf");
        cover = Mockito.mock(File.class);
        Mockito.when(cover.getAbsolutePath()).thenReturn("pippo.png");
        ebook = Mockito.mock(File.class);
        Mockito.when(ebook.getAbsolutePath()).thenReturn("pippo.pdf");
    }
    @Test
    void makeVersionErrorNUmberOfGenres() {
        String title="Titolo";
        String description="Description";
        Set<String> genres= null;
        int price= 50;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionOk() {
        String title="Titolo";
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        Version res;
        try {
            res=Version.makeVersion(title,description,price,report,ebook,cover,data,genres);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(null,res);
        assertEquals(title,res.getTitle());
        assertEquals(genres,res.getGenres());
        assertEquals(genres.size(),res.getGenres().size());
        assertEquals(price,res.getPrice());
        assertEquals(description,res.getDescription());
        assertEquals(data,res.getDate());
    }
    @Test
    void makeVersionOk1() {
        String title="Titolo";
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        genres.add("Fantasy");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        Version res;
        try {
            res=Version.makeVersion(title,description,price,report,ebook,cover,data,genres);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertNotEquals(null,res);
        assertEquals(title,res.getTitle());
        assertEquals(genres,res.getGenres());
        assertEquals(genres.size(),res.getGenres().size());
        assertEquals(price,res.getPrice());
        assertEquals(description,res.getDescription());
        assertEquals(data,res.getDate());



    }
    @Test
    void makeVersionErrorTitle1() {
        String title=null;
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Fantasy");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorTitle2() {
        String title=null;
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        genres.add("Fantasy");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorDescritpion1() {
        String title="Title";
        String description=null;
        Set<String> genres= new HashSet<>();
        genres.add("Fantasy");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorDescritpion2() {
        String title="Title";
        String description=null;
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        genres.add("Fantasy");
        int price= 50;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorPrice1() {
        String title="Title";
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Fantasy");
        int price= -1;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorPrice2() {
        String title="Title";
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        genres.add("Fantasy");
        int price= -1;
        data= LocalDate.of(2030,1,1);
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }
    @Test
    void makeVersionErrorData() {
        String title="Title";
        String description="Description";
        Set<String> genres= new HashSet<>();
        genres.add("Horror");
        genres.add("Fantasy");
        int price= 50;
        data= null;
        assertThrows(Exception.class,()->Version.makeVersion(title,description,price,report,ebook,cover,data,genres) );

    }

    @Test
    void addReportError() {
        report= null;
        assertThrows(Exception.class,()->v.addReport(report));
    }
    @Test
    void addReportOk() {
        try {
            v.addReport(report);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertEquals(report,v.getReport());
    }
}
