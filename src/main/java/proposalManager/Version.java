package proposalManager;

import java.io.File;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

public class Version {

    private int id;
    public LocalDate date;
    private String title;
    private File coverImage;
    private String description;
    private int price;
    private Set<String> genres;
    private File report;
    private File ebookFile;

    public Version(){
    }

    public static Version makeVersion(int id, String title, String description, int price, File report, File ebookFile, File coverImage, LocalDate date, Set<String> genres) {
        Version v = new Version();

        v.id = id;
        v.title = title;
        v.description = description;
        v.price = price;
        v.report = report;
        v.ebookFile = ebookFile;
        v.coverImage = coverImage;
        v.date = date;
        v.genres = genres;

        return v;
    }

    public static Version makeVersion(String title, String description, int price, File report, File ebookFile, File coverImage, LocalDate date, Set<String> genres) {
        Version v = new Version();

        v.title = title;
        v.description = description;
        v.price = price;
        v.report = report;
        v.ebookFile = ebookFile;
        v.coverImage = coverImage;
        v.date = date;
        v.genres = genres;

        return v;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public File getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(File coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public File getReport() {
        return report;
    }

    public void setReport(File report) {
        this.report = report;
    }

    public File getEbookFile() {
        return ebookFile;
    }

    public void setEbookFile(File ebookFile) {
        this.ebookFile = ebookFile;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
