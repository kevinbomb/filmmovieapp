package com.given.filmmovieapp.api

class UserApi {
    companion object{
        val BASE_URL="http://192.168.1.26/ci4/ci4/public/"

        val GET_ALL_URL = BASE_URL + "user/"
        val GET_BY_ID_URL = BASE_URL + "user/"
        val ADD_URL = BASE_URL + "user"
        val UPDATE_URL = BASE_URL + "user/"
        val DELETE_URL = BASE_URL + "user/"
    }
}