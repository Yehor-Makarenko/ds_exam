package part1;

import java.util.ArrayList;
import java.util.List;

public class ServerRmiTask1 {
  private List<Publication> publications;

    public ServerRmiTask1() {
        this.publications = new ArrayList<>();
    }

    public void addPublication(Publication publication) {
        publications.add(publication);
    }

    public List<Publication> searchPublications(Query query) {
        List<Publication> result = new ArrayList<>();
        for (Publication publication : publications) {
            if (publication.search(query)) {
                result.add(publication);
            }
        }
        return result;
    }

    public List<Publication> getPublicationsByRelevance() {
        publications.sort((p1, p2) -> Integer.compare(p2.getRelevance(), p1.getRelevance()));
        return new ArrayList<>(publications);
    }
}

class Query {
    private String title;
    private String author;
    private List<String> keywords;

    public Query(String title, String author, List<String> keywords) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public List<String> getKeywords() {
        return keywords;
    }
}

class Publication {
    private String title;
    private String author;
    private List<String> keywords;
    private int relevance;

    public Publication(String title, String author, List<String> keywords) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.relevance = 0;
    }

    public boolean search(Query query) {
        boolean result = true && (query.getKeywords() == null || !keywords.isEmpty() && keywords.stream().anyMatch(query.getKeywords()::contains));
        result = result && (query.getAuthor() == null || author.toLowerCase().contains(query.getAuthor().toLowerCase()));
        result = result && (query.getTitle() == null || title.toLowerCase().contains(query.getTitle().toLowerCase()));
        if (result) {
            relevance++;
        }
        return result;
    }

    public int getRelevance() {
        return relevance;
    }
}

class Book extends Publication {
    private String publisher;
    private int year;

    public Book(String title, String author, List<String> keywords, String publisher, int year) {
        super(title, author, keywords);
        this.publisher = publisher;
        this.year = year;
    }
}

class Article extends Publication {
    private String journal;
    private int volume;
    private int issue;

    public Article(String title, String author, List<String> keywords, String journal, int volume, int issue) {
        super(title, author, keywords);
        this.journal = journal;
        this.volume = volume;
        this.issue = issue;
    }
}

class Library {
    
}

