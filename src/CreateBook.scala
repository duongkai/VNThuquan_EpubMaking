import java.io.{FileOutputStream, FileInputStream, File}
import nl.siegmann.epublib.domain.{Metadata, Resource, Book}
import nl.siegmann.epublib.epub.EpubWriter

val RESOURCE_PATH = "Binh Phap Ton Tu"

val resourceDirectory = new File (RESOURCE_PATH)
val fileList = resourceDirectory.listFiles().toList

val myBook = new Book()

val myBookMD = new Metadata()
myBookMD.addTitle (RESOURCE_PATH)

myBook.setMetadata (myBookMD)

fileList.foreach (file => {
    val tmpResource = new Resource (new FileInputStream (file), RESOURCE_PATH + file.toString)
    myBook.addSection (file.toString, tmpResource)
}
)

println (myBook.getTableOfContents.size())

val bookWriter = new EpubWriter()
//bookWriter.setBookProcessor ()
bookWriter.write (myBook, new FileOutputStream ("test.epub"))




