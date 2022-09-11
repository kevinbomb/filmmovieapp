package com.given.filmmovieapp.entity

import com.given.filmmovieapp.R
import java.time.LocalDate

class Film(var name: String, var director: String, var release: LocalDate, var img: Int) {

    companion object{
        @JvmField
        var listOfFilm = arrayOf(
            Film("Thor", "Taika Waititi", LocalDate.parse("2018-12-28"), R.drawable.imgthor),
            Film("Batman", "Adam Smith", LocalDate.parse("2020-11-11"), R.drawable.imgbatman),
            Film("Aquaman", "Josh Alexander", LocalDate.parse("2022-12-04"), R.drawable.imgaquaman),
            Film("Sonic", "Kevin Widjaya", LocalDate.parse("2022-04-18"), R.drawable.imgsonic),
            Film("Avatar", "Mike Gorban", LocalDate.parse("2016-09-30"), R.drawable.imgavatar),
            Film("Your Name", "Makoto Shinkai", LocalDate.parse("2016-12-07"), R.drawable.imgyourname),
            Film("Weathering With You", "Makoto Shinkai", LocalDate.parse("2019-08-21"), R.drawable.imgwwy),
            Film("The Garden of Words", "Makoto Shinkai", LocalDate.parse("2013-05-31"), R.drawable.imgtgow),
            Film("My Neighbor Totoro", "Hayao Miyazaki", LocalDate.parse("1988-04-16"), R.drawable.imgtotoro),
            Film("A Silent Voice", "Naoko Yamada", LocalDate.parse("2017-05-03"), R.drawable.imgknk)
        )
    }
}