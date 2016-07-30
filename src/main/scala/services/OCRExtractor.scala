package services

import java.awt.Image
import java.awt.image.RenderedImage
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.util.List
import javax.imageio.ImageIO

import entities.DeviceClearnace
import org.bytedeco.javacpp._
import org.bytedeco.javacpp.lept._
import org.bytedeco.javacpp.tesseract._
import org.ghost4j.document.PDFDocument
import org.ghost4j.renderer.SimpleRenderer

import scala.collection.JavaConversions._
import scala.collection.mutable.StringBuilder

/**
  * Created by droidman on 7/29/16.
  */
object OCRExtractor {

  val renderer :SimpleRenderer = new SimpleRenderer()
  renderer.setResolution( 300 )

  def convertFunc (file: (String, org.apache.spark.input.PortableDataStream)) : DeviceClearnace  = {
    /** Render the PDF into a list of images with 300 dpi resolution
      * One image per PDF page, a PDF document may have multiple pages
      */
    val fileName = file._1
    val document: PDFDocument = new PDFDocument()
    document.load(file._2.open)
    val renderer: SimpleRenderer = new SimpleRenderer()
    renderer.setResolution(300)
    val images: List[Image] = renderer.render(document)

    /** Iterate through the image list and extract OCR
      * using Tesseract API.
      */
    var r = new StringBuilder
    images.toList.foreach { x =>
      val imageByteStream = new ByteArrayOutputStream()
      ImageIO.write(
        x.asInstanceOf[RenderedImage], "png", imageByteStream)
      val pix: PIX = pixReadMem(
        ByteBuffer.wrap(imageByteStream.toByteArray()).array(),
        ByteBuffer.wrap(imageByteStream.toByteArray()).capacity()
      )
      val api: TessBaseAPI = new TessBaseAPI()

      /** We assume the documents are in English here, hence \”eng\” */
      api.Init(null, "eng")
      api.SetImage(pix)
      r.append(api.GetUTF8Text().getString())
      imageByteStream.close
      pixDestroy(pix)
      api.End
    }
    DeviceClearnace(fileName, r.toString())
  }
}
