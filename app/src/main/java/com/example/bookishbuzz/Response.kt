package com.example.bookishbuzz

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Response(

	@field:SerializedName("copyright")
	val copyright: String? = null,

	@field:SerializedName("last_modified")
	val lastModified: String? = null,

	@field:SerializedName("results")
	val results: Results? = null,

	@field:SerializedName("num_results")
	val numResults: Int? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class BuyLinksItem(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Serializable

data class IsbnsItem(

	@field:SerializedName("isbn13")
	val isbn13: String? = null,

	@field:SerializedName("isbn10")
	val isbn10: String? = null
)

data class Results(

	@field:SerializedName("next_published_date")
	val nextPublishedDate: String? = null,

	@field:SerializedName("bestsellers_date")
	val bestsellersDate: String? = null,

	@field:SerializedName("books")
	val books: List<BooksItem?>? = null,

	@field:SerializedName("corrections")
	val corrections: List<Any?>? = null,

	@field:SerializedName("published_date_description")
	val publishedDateDescription: String? = null,

	@field:SerializedName("normal_list_ends_at")
	val normalListEndsAt: Int? = null,

	@field:SerializedName("list_name")
	val listName: String? = null,

	@field:SerializedName("list_name_encoded")
	val listNameEncoded: String? = null,

	@field:SerializedName("previous_published_date")
	val previousPublishedDate: String? = null,

	@field:SerializedName("display_name")
	val displayName: String? = null,

	@field:SerializedName("published_date")
	val publishedDate: String? = null,

	@field:SerializedName("updated")
	val updated: String? = null
)

data class BooksItem(

	@field:SerializedName("isbns")
	val isbns: List<IsbnsItem?>? = null,

	@field:SerializedName("contributor_note")
	val contributorNote: String? = null,

	@field:SerializedName("asterisk")
	val asterisk: Int? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("primary_isbn10")
	val primaryIsbn10: String? = null,

	@field:SerializedName("primary_isbn13")
	val primaryIsbn13: String? = null,

	@field:SerializedName("title")
	val title: String? = null,

	@field:SerializedName("weeks_on_list")
	val weeksOnList: Int? = null,

	@field:SerializedName("article_chapter_link")
	val articleChapterLink: String? = null,

	@field:SerializedName("book_image_width")
	val bookImageWidth: Int? = null,

	@field:SerializedName("contributor")
	val contributor: String? = null,

	@field:SerializedName("amazon_product_url")
	val amazonProductUrl: String? = null,

	@field:SerializedName("price")
	val price: String? = null,

	@field:SerializedName("book_uri")
	val bookUri: String? = null,

	@field:SerializedName("rank")
	val rank: Int? = null,

	@field:SerializedName("dagger")
	val dagger: Int? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("age_group")
	val ageGroup: String? = null,

	@field:SerializedName("buy_links")
	val buyLinks: List<BuyLinksItem?>? = null,

	@field:SerializedName("sunday_review_link")
	val sundayReviewLink: String? = null,

	@field:SerializedName("book_review_link")
	val bookReviewLink: String? = null,

	@field:SerializedName("book_image_height")
	val bookImageHeight: Int? = null,

	@field:SerializedName("book_image")
	val bookImage: String? = null,

	@field:SerializedName("publisher")
	val publisher: String? = null,

	@field:SerializedName("rank_last_week")
	val rankLastWeek: Int? = null,

	@field:SerializedName("first_chapter_link")
	val firstChapterLink: String? = null
)