package hnkindle

import de.l3s.boilerpipe.extractors.*
import de.l3s.boilerpipe.sax.*

class ArticleController {

    def articleService

    def index = {
        def result = articleService.fetchAllTopArticles()
        render result.toString()
    }

    def findByLink = {
        def result = Article.findByLink(params.link)
        render result
    }

    def addToCache = {
        def key = params.key
        def value = params.value
        def cache = articleService.cache
        cache.put(key, value)
        render "Done"
    }

    def getFromCache = {
        def cache = articleService.cache
        def key = params.key
        render cache.get(key)
    }

    def getArticlesToMail = {
        render articleService.getAllNewArticles()
    }



    def sindex = {
        def articles = Article.findAllByLinkInList(["http://socialmediacollective.org/2011/08/04/real-names-policies-are-an-abuse-of-power/",
            "http://fukime.com/",
            "http://ruby.learncodethehardway.org/"])
        render articles.collect{it.link}.join("<br>")
    }

    def newMailFirst = {
        log.info "Beginning mail all new articles action"
        articleService.mailFirstNewArticle()
        render "Done mailing the first article"        
    }
    
    def mailFirst = {
        log.info "Beginning mail all new articles action"
        articleService.mailFirstNewArticle()
        render "Done mailing the first article"
    }

    def mailAllNewArticles = {
    	log.info "Beginning mail all new articles action"
    	articleService.mailAllNewArticles()
    	render "Done"
    }

    def deleteAll = {
    	Article.list().each {
    		it.delete(flush:true)
    	}
    	render "Done"
    }

    def sendSampleMail = {
    	articleService.mailArticle("Title","Content")
    	render "Done"
    }

    def list = {
    	def articleList = Article.list()
    	articleList = articleList.collect { it.link }
    	render articleList.join("\n")
    }

    def sample = {
        def url = "http://collectiveidea.com/blog/archives/2011/08/02/command-line-feedback-from-rvm-and-git/"
        def title = "Command Line Feedback from RVM and Git (collectiveidea.com)"
        String html = articleService.extractHtml(url)
        articleService.mailArticle(title,html)
        render "Done"
    }

    def check = {
        render "If you’re like me, most"
    }

    def extract = {
    	def url = params.url
    	def result = articleService.extractHtml(url)
    	render result
    }

    def extractFromLibrary = {
        def url =  new URL(params.url)

        HTMLHighlighter hh = HTMLHighlighter.newHighlightingInstance()
        hh.setPreHighlight("")
        hh.setPostHighlight("")
        hh.setOutputHighlightOnly(true)
        String result = hh.process(url, ArticleExtractor.INSTANCE)

        //def result = articleService.extractArticleUsingLibrary(url)
        render result
    }

    def getLinks = {

        String hackerNewsUrl = "http://news.ycombinator.com/"
        URL url = new URL(hackerNewsUrl);
        BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        String html = reader.text;
        render articleService.getLinksUsingJsoup(html)

    }
}
