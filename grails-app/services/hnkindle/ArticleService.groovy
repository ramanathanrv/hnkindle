package hnkindle

import grails.util.GrailsUtil
import java.util.regex.*
import org.jsoup.*
import org.jsoup.nodes.*
import org.jsoup.select.*
import java.util.Properties;

import net.sf.jsr107cache.*;
import com.google.appengine.api.memcache.jsr107cache.GCacheFactory;
import com.google.appengine.api.memcache.MemcacheService;
import de.l3s.boilerpipe.extractors.*
import de.l3s.boilerpipe.sax.*


import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataHandler;

class ArticleService {

    def String hackerNewsUrl = "http://news.ycombinator.com/"
    def String sourceDomain = "news.ycombinator.com"

    def importantKeywords = ["twitter","google","facebook","startup","apple","microsoft"]

    static transactional = false

    def Cache cache;

    public ArticleService() {

        CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
        Map props = new HashMap();
        props.put(MemcacheService.SetPolicy.ADD_ONLY_IF_NOT_PRESENT, true);
        props.put(GCacheFactory.EXPIRATION_DELTA, 3600 * 24); //store for a day
        cache = cacheFactory.createCache(props);

    }


    public def extractArticleUsingLibrary(String link) {
        URL url = new URL(link);
        HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance()
        hh.setPreHighlight("")
        hh.setPostHighlight("")
        hh.setOutputHighlightOnly(true)
        String result = hh.process(url, ArticleExtractor.INSTANCE)
        return result            
    }

    public def extractHtml(link) {
        def convUrl = "http://boilerpipe-web.appspot.com/extract?url=${link}&output=htmlFragment"
        URL url = new URL(convUrl);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));
        String html = reader.text
        html = html.replaceAll("’","'")
        return html
    }

    def mailFirstNewArticle() {
        def linkMap = getAllNewArticles()
        def keys = linkMap.keySet() as List
        if(keys.size() > 0) {
            def link = keys[0]
            def title = linkMap[link]
            String html = extractArticleUsingLibrary(link)
            Article.withTransaction {
                Article article = new Article()
                article.link = link
                article.extractedContent = new com.google.appengine.api.datastore.Text(html)
                article.state = "NEW"
                article.save(flush:true)
                article.dateCreated = new Date()
                article.title = title
            }
            mailArticle(title,html)
            //put this in cache
            cache.put(link, 1)
        }
    }

    def mailAllNewArticles() {
        def linkMap = getAllNewArticles()

        linkMap.each {k,v ->
            //extract html
            String html = extractHtml(k)
            mailArticle(v, html)
            Article.withTransaction {
                Article article =  new Article()
                article.link = k
                article.extractedContent = new com.google.appengine.api.datastore.Text(html)
                article.state = "NEW"
                article.save(flush:true)                
            }
            //mail the article
        }
    }

    protected def getAllSubscribers() {
        return Subscription.findAllBySubscribed(true).collect {it.email}
    }

    def mailArticle(def title, def content) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);

        Message msg = new MimeMessage(session);
        msg.setHeader("Content-Type", "text/html; charset=UTF-8");
        msg.setFrom(new InternetAddress("ramanathanrv@gmail.com", "Ramanathan RV"));
        
        msg.addRecipient(Message.RecipientType.TO, new InternetAddress("ramanathanrv@gmail.com","Ram"))
        def subs = getAllSubscribers()
        subs.each {email ->
            msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(email, email))
        }

        msg.setSubject(title)
        String htmlBody = "Convert";        // ...
        byte[] attachmentData;  // ...

        Multipart mp = new MimeMultipart();

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody, "text/html");
        mp.addBodyPart(htmlPart);

        byte[] binaryContent = content.getBytes()
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.setFileName("${title}.html");
        attachment.setContent(new String(content.getBytes("UTF-8"),"UTF-8"), "text/html; charset=UTF-8");
        mp.addBodyPart(attachment);

        msg.setContent(mp);
        Transport.send(msg)
    }


    def getAllNewArticles() {
        def linkMap = fetchAllTopArticles()
        

        def links = linkMap.keySet() as List

        println links
        def r = 0
        links.each {link-> 
            if(cache.get(link) != null) {
                linkMap.remove(link)
                r++
            } 
        }
        println "Removed ${r} links using cache"
        println "Will now try to prune using a database lookup"

        links = linkMap.keySet() as List 

        links.each { o->  
            println "Checking ${o}"
            def article = Article.findByLink(o) 
            if(article != null) {
                cache.put(article.link, 1)
                linkMap.remove(o)
                println "Removed ${o} from Map"
            }
        }
        println "Returning ${linkMap.keySet()} "

        return linkMap
    }

    def fetchAllTopArticles() {

        if(GrailsUtil.isDevelopmentEnv()) {
            hackerNewsUrl = "http://localhost/hnews.html"
        }
//        URL url = new URL(hackerNewsUrl);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
//        String html = reader.text;
        def result = []
        result = getLinksUsingJsoup(hackerNewsUrl)
        return result
    }

    protected def getLinkFromElement(Element e, int index) {
        def obj = new Expando()
        def a = e.getElementsByTag("a")
        if(a.size() > 0) {
            a = a.get(0)
        } else {
            return
        }

        //now get parent element
        def tr = e.parent()
        def scoreRow = tr.nextElementSibling()
        if(!scoreRow || !scoreRow.text())
            return
        
        def scoreText = scoreRow?.text()?:"Nothing here"
        def scoreRegex = /([\d]+) points/
        def commentsRegex = /([\d]+) comments/
        def scoreMatch = scoreText =~ scoreRegex
        def commentsMatch = scoreText =~ commentsRegex
        
        obj.link = a.attr("abs:href")
        obj.text = e.text()

        if(scoreMatch && scoreMatch[0] && scoreMatch[0].size() > 1) {
            obj.score = scoreMatch[0][1].toInteger()                
        }

        if(commentsMatch && commentsMatch[0] && commentsMatch[0].size() > 1) {
            obj.numComments = commentsMatch[0][1].toInteger()
        }

        //println obj
        boolean hasKeyword = importantKeywords.findAll{obj.link.contains(it)} ? true : false

        if(obj.numComments > 50 || obj.score > 100 || (hasKeyword && index < 11)) {
            return obj
        } else {
            return null
        }
        
    }
    def getLinksUsingJsoup(String link) {
        def result = [:]
        Document doc = Jsoup.connect(hackerNewsUrl).get();
        Elements elts = doc.select("td.title")
        def i = 0
        elts.each {e ->
            try {
                def obj = getLinkFromElement(e, ++i)
                if(obj != null) {
                    result[obj.link] = obj.text
                }
            }catch (Exception ex) {
                //ignore the bitch
            }
        }
        return result
    }

}
