import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static com.codeborne.selenide.Selenide.*;

public class Main {
  private static final String LINK_TEMPLATE = "https://visit-russia.ru/ru/novosti.html?start=";
  private static final int START = 0;
  private static final int STEP = 20;


  public static void main(String[] args) throws InterruptedException, IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

    open(LINK_TEMPLATE + START);
    ElementsCollection articleBlocks = $$("article");

    List<Article> articleList = new ArrayList<>();

//    open("https://visit-russia.ru/ru/novosti.html?start=1340");
//    articleBlocks = $$("article");
//    article = parceArticle(articleBlocks.get(articleBlocks.size()-1));
//    System.out.println(article);

    for (int i = 0; i < articleBlocks.size(); i++) {
      articleList.add(parceArticle(articleBlocks.get(i)));

    }

    Writer writer = new FileWriter("site001.csv");
    StatefulBeanToCsv beanToCsv = new StatefulBeanToCsvBuilder(writer).withSeparator(';').build();

    beanToCsv.write(articleList);
    writer.close();


//    article = parceArticle(articleBlocks.get(7));
//    System.out.println(article);


  }


  private static Article parceArticle(SelenideElement block) throws InterruptedException {
    String temp;
    Article article = new Article();

    String articleClass = "." + block.attr("class");

    article.setLink($(articleClass + " h2 a").attr("href"));
    article.setAuthor(getElement(articleClass + " .field-value"));
    article.setDateTime(getElement(articleClass + " .published").substring(14));
    article.setPicture($(articleClass + " img").attr("src"));
    article.setSynopsis(getElement(articleClass));

    $(articleClass + " h2 a").click();

    article.setTitle(getElement("h1"));

    temp = getElement("figcaption");
    article.setPictAuthor("".equals(temp) ? "" : temp.substring(5));

    ElementsCollection bodyBlocks = $$("article p");

    List<String> bodyList = new ArrayList<>();
    for (SelenideElement bodyBlock : bodyBlocks) {
      temp = bodyBlock.getText().strip();
      if (!temp.isEmpty()) {
        temp = temp.replaceAll("http", " http");
        temp = temp.replaceAll("  ", " ");
        bodyList.add("<p>" + temp + "</p>");
      }
    }

//    int lastIndex = bodyBlocks.size() - 1;
//    while (bodyBlocks.get(lastIndex).getText().isBlank()) {
//      lastIndex--;
//    }
//
//    temp = bodyBlocks.get(lastIndex).lastChild().attr("href");
//    article.setSourceLink(null == temp ? "" : temp);
//    article.setSourceTitle(bodyBlocks.get(lastIndex).lastChild().getOwnText().strip());
//
//    if (!article.getSourceTitle().isEmpty()) bodyList.remove(bodyList.size() - 1);

    article.setBody(String.join("", bodyList).strip());

    back();
    return article;
  }

  private static String getElement(String selector) {
    SelenideElement element = $(selector);
    return element.toString().startsWith("NoSuchElementException") ? "" : element.getOwnText().strip();
  }

}
