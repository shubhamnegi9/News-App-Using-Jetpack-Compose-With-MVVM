package com.shubham.newsappwithmvvm

import android.os.Build
import android.util.Log
import com.shubham.newsappwithmvvm.data.models.NewsData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MockData {
    val topNewsList = listOf<NewsData>(
        NewsData(
            1,
            author = "Raja Razek, CNN",
            title = "'Tiger King' Joe Exotic says he has been diagnosed with aggressive form of prostate cancer - CNN",
            description = "Joseph Maldonado, known as Joe Exotic on the 2020 Netflix docuseries \\\"Tiger King: Murder, Mayhem and Madness,\\\" has been diagnosed with an aggressive form of prostate cancer, according to a letter written by Maldonado.",
            publishedAt = "2025-02-21T05:35:21Z"
        ),
        NewsData(
            2,
            R.drawable.namita,
            author = "Namita Singh",
            title = "Cleo Smith news — live: Kidnap suspect 'in hospital again' as 'hard police grind' credited for breakthrough - The Independent",
            description = "Cleo Smith, a four-year-old Australian girl, was abducted on 16 October 2021 from a campsite in the Gascoyne region of Western Australia (WA). She was found alive and well on 3 November, after police raided the home of Terence Darrell Kelly in the nearby town of Carnarvon. Her safe recovery after eighteen days was described as extremely rare, and received widespread news coverage and social media reaction both across Australia and internationally. Kelly was convicted of child abduction and sentenced to 13 years and 6 months in jail. " +
                    "The suspected kidnapper of four-year-old Cleo Smith has been treated in hospital for a second time amid reports he was “attacked” while in custody.",
            publishedAt = "2025-02-20T04:42:40Z"
        ),
        NewsData(
            3,
            R.drawable.reuters,
            author = "Not available",
            title = "'You are not alone': EU Parliament delegation tells Taiwan on first official visit - Reuters",
            description =
            "The European Parliament's first official delegation to Taiwan said on Thursday the diplomatically isolated island is not alone and called for bolder actions to strengthen EU-Taiwan ties as Taipei faces rising pressure from Beijing.",
            publishedAt = "2025-02-01T03:37:00Z"
        ),
        NewsData(
            4,
            R.drawable.michael,
            author = "Mike Florio",
            title = "Aaron Rodgers violated COVID protocol by doing maskless indoor press conferences - NBC Sports",
            description = "Packers quarterback Aaron Rodgers has been conducting in-person press conferences in the Green Bay facility without wearing a mask. Because he was secretly unvaccinated, Rodgers violated the rules.",
            publishedAt = "2025-01-12T03:21:00Z"
        ),
        NewsData(
            5,
            R.drawable.grant,
            author = "Grant Brisbee",
            title = "Buster Posey's career was like no other in Giants history - The Athletic",
            description = "There was a franchise without Buster Posey, and there was one with him, and those two franchises were radically, impossibly different.",
            publishedAt = "2024-10-22T02:12:54Z"
        ),
        NewsData(
            6,
            author = "Michael Schneider",
            title = "‘The Masked Singer’ Reveals Identity of the Beach Ball: Here Are the Stars Under the Mask - Variety",
            description = "SPOILER ALERT: Do not read ahead if you have not watched “The Masked Singer” Season 6, Episode 8, “Giving Thanks,” which aired November 3 on Fox. Honey Boo Boo, we hardly knew you. Reality TV mother and daughter stars June Edith “Mama June” Shannon and Alana …",
            publishedAt = "2024-06-12T02:00:00Z"
        ),
        NewsData(
            7,
            R.drawable.thomas,
            author = "Thomas Barrabi",
            title = "Sen. Murkowski slams Dems over 'show votes' on federal election bills - Fox News",
            description = "Sen. Lisa Murkowski, R-Alaska, slammed Senate Democrats for pursuing “show votes” on federal election bills on Wednesday as Republicans used the filibuster to block consideration the John Lewis Voting Rights Advancement Act.",
            publishedAt = "2023-02-22T01:57:36Z"
        ),
        NewsData(
            8,
            author = "CBSBoston.com Staff",
            title = "Principal Beaten Unconscious At Dorchester School; Classes Canceled Thursday - CBS Boston",
            description = "Principal Patricia Lampron and another employee were assaulted at Henderson Upper Campus during dismissal on Wednesday.",
            publishedAt = "2021-11-04T01:55:00Z"
        )
    )

    fun getNewsDataFromId(newsId: Int?): NewsData {
        // Returns the first NewsData from topNewsList whose id matches with newsId passed
        return topNewsList.first {
            it.id == newsId
        }
    }

    fun Date.getTimeAgo(): String {     // We are calling getTimeAlgo() method on Date type and it returns String
        val calendar = Calendar.getInstance()
        calendar.time = this@getTimeAgo     // This calendar object will take the time from the object on which getTimeAgo is called

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val currentCalendar = Calendar.getInstance()                // Calendar object with the current time
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentDay = currentCalendar.get(Calendar.DAY_OF_MONTH)
        val currentHour = currentCalendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentCalendar.get(Calendar.MINUTE)

        return if(year < currentYear) {
            val interval = currentYear - year
            if(interval == 1) "$interval year ago" else "$interval years ago"
        } else if(month < currentMonth) {
            val interval = currentMonth - month
            if(interval == 1) "$interval month ago" else "$interval months ago"
        } else if(day < currentDay) {
            val interval = currentDay - day
            if(interval == 1) "$interval day ago" else "$interval days ago"
        } else if(hour < currentHour) {
            val interval = currentHour - hour
            if(interval == 1) "$interval month ago" else "$interval months ago"
        } else if(minute < currentMinute) {
            val interval = currentMinute - minute
            if(interval == 1) "$interval minute ago" else "$interval minutes ago"
        } else {
            "a moment ago"
        }
    }

    // Converts String timestamp to Date type
    fun stringToDate(publishedAt: String): Date {
        val date = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
             SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(publishedAt)
        } else {
            java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).parse(publishedAt)
        }
        Log.d("MockData", "stringToDate: $date")
        return date!!
    }
}