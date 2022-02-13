package br.com.yann.rssreader.util;

import java.io.IOException;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.com.yann.rssreader.factory.RequestXmlFromHttpFactory;
import br.com.yann.rssreader.model.Rss;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;

@Stateless
public class RssConvertor {

  private RequestXmlFromHttpFactory factory;
  private JAXBContext context;


   @Inject
   public RssConvertor(){
     this.factory = new RequestXmlFromHttpFactory();
   }

  private String prepareURI(String uri) {
    return uri.replace(" ", "");
  }

  //TODO melhorar isso
  private String treatXml(String treatable) {
    ByteBuffer byteBuffer = StandardCharsets.UTF_8.encode(treatable);
    String utf8EncodedString = StandardCharsets.UTF_8.decode(byteBuffer).toString();

    String treated = utf8EncodedString.replaceAll("\uFEFF", "").replaceAll("\\R+", "").replaceAll("\"xmlns","\" xmlns");

    return treated;

  }

  public Rss getRss(String url)  {
      try {
        context = JAXBContext.newInstance(Rss.class);
        String treatable = prepareURI(url);
        String treated = treatXml(factory.getXml(treatable));
        Rss rss  = (Rss) context.createUnmarshaller().unmarshal(new StringReader(treated));
      return rss;
      } catch (JAXBException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
     return null;
  }

  public void main(String[] args)   {

    String url = "https://www.fashionlady.in/category/beauty-tips/feed";


    Rss rss = getRss(url);
    System.out.println(rss.getLink());
    System.out.println(rss.getTitle());
    System.out.println(rss.getDescription());
  }

}



