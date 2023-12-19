package part2;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class ServerRmiTask1 {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            RMIServerImpl server = new RMIServerImpl();
            registry.rebind("Library", server);
        } catch (RemoteException e) {      
            e.printStackTrace();
        }
    }
}

interface RMIServer extends Remote {
    public void addBook(String title, String author, List<String> keywords, String publisher, int year) throws RemoteException;
    public void addArticle(String title, String author, List<String> keywords, String journal, int volume, int issue) throws RemoteException;
    public List<Publication> searchPublications(String title, String author, List<String> keywords) throws RemoteException;
    public List<Publication> getPublicationsByRelevance() throws RemoteException;
}

class RMIServerImpl extends UnicastRemoteObject implements RMIServer {
    private List<Publication> publications;
    
    public RMIServerImpl() throws RemoteException {
        this.publications = new ArrayList<>();
    }        

    public void addBook(String title, String author, List<String> keywords, String publisher, int year) {
        publications.add(new Book(title, author, keywords, publisher, year));
    }

    public void addArticle(String title, String author, List<String> keywords, String journal, int volume, int issue) {
        publications.add(new Article(title, author, keywords, journal, volume, issue));
    }

    public List<Publication> searchPublications(String title, String author, List<String> keywords) {
        PublicationQuery query = new PublicationQuery(title, author, keywords);
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

class Publication {
    public String title;
    public String author;
    public List<String> keywords;
    private int relevance;

    public Publication(String title, String author, List<String> keywords) {
        this.title = title;
        this.author = author;
        this.keywords = keywords;
        this.relevance = 0;
    }

    public boolean search(PublicationQuery query) {
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

class PublicationQuery {
    private String title;
    private String author;
    private List<String> keywords;

    public PublicationQuery(String title, String author, List<String> keywords) {
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

class Book extends Publication implements Serializable {
    public String publisher;
    public int year;

    public Book(String title, String author, List<String> keywords, String publisher, int year) {
        super(title, author, keywords);
        this.publisher = publisher;
        this.year = year;
    }
}

class Article extends Publication implements Serializable {
    public String journal;
    public int volume;
    public int issue;

    public Article(String title, String author, List<String> keywords, String journal, int volume, int issue) {
        super(title, author, keywords);
        this.journal = journal;
        this.volume = volume;
        this.issue = issue;
    }
}