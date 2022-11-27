package com.given.filmmovieapp

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import com.given.filmmovieapp.databinding.ActivityCetakTiketBinding
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class CetakTiket : AppCompatActivity() {
    private var binding: ActivityCetakTiketBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCetakTiketBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)
        binding!!.buttonSave.setOnClickListener {
            val title = binding!!.editTextJudul.text.toString()
            val date = binding!!.editTextTanggal.text.toString()
            val time = binding!!.editTextJam.text.toString()
            val row = binding!!.editTextRow.text.toString()
            val seat = binding!!.editTextSeat.text.toString()

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (title.isEmpty() && date.isEmpty() && time.isEmpty() && row.isEmpty() && seat.isEmpty()) {
                        Toast.makeText(
                            applicationContext,
                            "Semuanya Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        createPdf(title, date, time, row, seat)
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

        }

    }
@SuppressLint("ObsoleteSdkInt")
@RequiresApi(api = Build.VERSION_CODES.O)
@Throws(
    FileNotFoundException::class
)
    private fun createPdf(title: String, date: String, time: String, row: String, seat: String) {
        val pdfPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
        val file= File(pdfPath,"tiket.pdf")
        FileOutputStream(file)

    val writer = PdfWriter(file)
    val pdfDocument = PdfDocument(writer)
    val document = Document(pdfDocument)
    pdfDocument.defaultPageSize = PageSize.A4
    document.setMargins(5f, 5f, 5f, 5f)
    @SuppressLint("UseCompatLoadingForDrawables") val d = getDrawable(R.drawable.logo)

    val bitmap = (d as BitmapDrawable?)!!.bitmap
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100,stream)
    val bitmapData = stream.toByteArray()
    val imageData = ImageDataFactory.create(bitmapData)
    val image = Image(imageData)
    val namapengguna = Paragraph("Tiket").setBold().setFontSize(24f)
        .setTextAlignment(TextAlignment.CENTER)
    val group = Paragraph(
        """
                        Berikut Adalah
                        Hasil Cetak Tiket
                        """.trimIndent()).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

    val width = floatArrayOf(100f, 100f)
    val table = Table(width)
    table.setHorizontalAlignment(HorizontalAlignment.CENTER)
    table.addCell(Cell().add(Paragraph("Title")))
    table.addCell(Cell().add(Paragraph(title)))
    table.addCell(Cell().add(Paragraph("Date")))
    table.addCell(Cell().add(Paragraph(date)))
    table.addCell(Cell().add(Paragraph("Time")))
    table.addCell(Cell().add(Paragraph(time)))
    table.addCell(Cell().add(Paragraph("Row")))
    table.addCell(Cell().add(Paragraph(row)))
    table.addCell(Cell().add(Paragraph("Seat")))
    table.addCell(Cell().add(Paragraph(seat)))
    val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    table.addCell(Cell().add(Paragraph("Tanggal Cetak Tiket")))
    table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
    table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
    table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

    val barcodeQRCode = BarcodeQRCode(
        """
                                        $title
                                        $date
                                        $time
                                        $row
                                        $seat
                                        ${LocalDate.now().format(dateTimeFormatter)}
                                        ${LocalTime.now().format(timeFormatter)}
                                        """.trimIndent())
    val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
    val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(HorizontalAlignment.CENTER)

    document.add(image)
    document.add(namapengguna)
    document.add(group)
    document.add(table)
    document.add(qrCodeImage)

    document.close()
    motionToast()
}
    private fun motionToast() {
//
        MotionToast.createToast(this,
            "Sukses 😍",
            "Ticket Created!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))

    }
}