package com.example.demoappmusic.Model

class SongModel {
    var title: String = ""
    var file: Int = 0

    constructor()

    constructor(title: String, file: Int) {
        this.title = title
        this.file = file
    }
}

