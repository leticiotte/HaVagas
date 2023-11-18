package com.example.havagas.domain.models

class Formation {
    lateinit var formationType: String
    var formationYear: String = ""
    var conclusionYear: String = ""
    var institution: String = ""
    var monographyTitle: String = ""
    var advisor: String = ""

    override fun toString(): String {
        return "- tipo de formação=$formationType, \n" +
                "- ano de formação=$formationYear, \n" +
                "- ano de conclusão=$conclusionYear, \n" +
                "- instituição=$institution, \n" +
                "- título monografia=$monographyTitle, \n" +
                "- orientador=$advisor"
    }
}