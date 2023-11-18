package com.example.havagas.domain.models

class Form {
    lateinit var name: String
    lateinit var email: String
    var receiveEmails: Boolean = false
    lateinit var phone: String
    lateinit var cellphone: String
    var hasCellphone: Boolean = false
    lateinit var gender: String
    lateinit var birthDate: String
    var formation: Formation = Formation()
    lateinit var interestJobs: String

    override fun toString(): String {
        return "nome=$name, \n" +
                "email=$email, \n" +
                "receber emails=$receiveEmails, \n" +
                "telefone=$phone, \n" +
                "celular=$cellphone, \n" +
                "possui celular=$hasCellphone, \n" +
                "sexo=$gender, \n" +
                "data de nascimento=$birthDate, \n" +
                "formação=\n$formation, \n" +
                "vagas de interesse=$interestJobs"
    }
}