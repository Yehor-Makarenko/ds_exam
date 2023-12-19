package part2;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class ClientRmiTask1 {
  public static void main(String[] args) {
    try {
      RMIServer server = (RMIServer) Naming.lookup("//127.0.0.1/Library");      
      server.addBook("Book", "Author1", List.of("programming", "ruby"), "Publisher", 2021);
      server.addArticle("Article", "Author2", List.of("ruby", "tricks"), "Journal", 10, 3);      
      List<Publication> publications = server.searchPublications(null, null, List.of("ruby"));
      System.out.println(publications.get(0).author);
      System.out.println(publications.get(1).author);
      publications = server.getPublicationsByRelevance();
      System.out.println(publications.get(0).title);
      System.out.println(publications.get(1).title);
      
    } catch (MalformedURLException | RemoteException | NotBoundException e) {      
      e.printStackTrace();
    }
  }
}
