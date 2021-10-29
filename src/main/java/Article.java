import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Article implements Serializable {
  private String link = "";
  private String title = "";
  private String author = "";
  private String dateTime = "";
  private String synopsis = "";
  private String picture = "";
  private String pictAuthor = "";
  private String body = "";
//  private String sourceLink = "";
//  private String sourceTitle = "";

  @Override
  public String toString() {
    return "Article: \n" +
        "link: " + link + '\n' +
        "title: " + title + '\n' +
        "author: " + author + '\n' +
        "time: " + dateTime + '\n' +
        "synopsis: " + synopsis + '\n' +
        "picture: " + picture + '\n' +
        "pictAuthor: " + pictAuthor + '\n' +
        "body: " + body + '\n'
//        + "sourceLink: " + sourceLink + '\n' +
//        "sourceTitle: " + sourceTitle + '\n'
        ;
  }

  public String toCsvLine(){
    return "\""
        + link
        + "\";\"" + title
        + "\";\"" + author
        + "\";\"" + dateTime
        + "\";\"" + synopsis
        + "\";\"" + picture
        + "\";\"" + pictAuthor
        + "\";\"" + body
        + "\"\n"
        ;
  }
}
