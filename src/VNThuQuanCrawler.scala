import collection.mutable.ListBuffer
import java.io._
import java.lang.StringBuffer
import org.apache.commons.lang.StringEscapeUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

val HTTP_ADDRESS = "http://vnthuquan.net/truyen/";

def getChapterLinks (content: Document): List[List[String]] = {
  val chapterLinks = new ListBuffer[List[String]]
  val linkLines = content.getElementsByClass ("normal8").select ("acronym[title]")
  val it = linkLines.iterator ()
  while (it.hasNext) {
    val tmpElement = it.next ()
    val chapterTitle = tmpElement.attr ("title")
    val hrefLines = tmpElement.select ("a[href]")
    val chapterid = hrefLines.first.text
    val chapterLink = hrefLines.first.attr ("href")
    chapterLinks += List (chapterid, chapterLink, chapterTitle)
  }
  chapterLinks.toList
}

//getChapterLinks (pageContent).foreach (si => println (si(1)))
def getChapterContent (content: Document): List[String] = {
  var chapterContent = new ListBuffer[String]
  val body = content.getElementById ("fontchu")
    //chapterContent += "<?xml version='1.0' encoding='utf-8'?>"
    chapterContent += "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
    chapterContent += "<head>"
    chapterContent += "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"></meta>"
    chapterContent += "</head>"
    chapterContent += "<body>"
    val clean = StringEscapeUtils.unescapeHtml(body.html())
    //println (clean)
    chapterContent += clean
    chapterContent += "</body>"
    chapterContent += "</html>"
    chapterContent.toList
}

/*
def rewriteContent (content: Document): String = {
  val
}
*/

def getDocument (weblink: String): Document = Jsoup.connect (HTTP_ADDRESS + weblink).get()

def mainDriver (firstLink: String, storyName: String) {
  val getAllLinks = getChapterLinks (getDocument (firstLink))
  var id = 0
  getAllLinks.foreach (link => {
    id += 1
    // getting content
    println ("Getting content of the chapter: " + link (0) + "\t" + HTTP_ADDRESS + link (1))
    new File (storyName).mkdir ()
    val tmp1 = getChapterContent (getDocument (link (1)))
    printToFile (new File (storyName + "\\" + id + "." + link (0) + ".html")) (p => {
      tmp1.foreach (p.println (_))
    })
    println ("Chapter is done", link (0))
  })
}

def printToFile (f: File)(op: PrintWriter => Unit) {
  val p = new java.io.PrintWriter (f)
  try {
    op (p)
  } finally {
    p.close ()
  }
}

mainDriver ("truyen.aspx?tid=2qtqv3m3237n1n1ntntn31n343tq83a3q3m3237nvn", "Binh Phap Ton Tu")