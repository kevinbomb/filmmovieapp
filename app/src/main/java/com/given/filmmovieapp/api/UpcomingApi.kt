package com.given.filmmovieapp.api

class UpcomingApi {
    companion object{
        val BASE_URL="http://192.168.1.6/ci4/ci4/public/"

        val GET_ALL_URL = BASE_URL + "upcoming/"
        val GET_BY_ID_URL = BASE_URL + "upcoming/"
        val ADD_URL = BASE_URL + "upcoming"
        val UPDATE_URL = BASE_URL + "upcoming/"
        val DELETE_URL = BASE_URL + "upcoming/"
    }
}