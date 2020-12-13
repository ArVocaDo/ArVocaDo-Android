package com.example.arvocado_android.ui.sound

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class PapagoTextTranslate {
    fun getTranslation(clientId : String, clientSecret : String ,word: String?, source: String?, target: String?): String {
        try {
            val wordSource: String
            val wordTarget: String
            val text = URLEncoder.encode(word, "UTF-8") //word
            wordSource = URLEncoder.encode(source, "UTF-8")
            wordTarget = URLEncoder.encode(target, "UTF-8")
            val apiURL = "https://openapi.naver.com/v1/papago/n2mt"
            val url = URL(apiURL)
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "POST"
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)
            // post request
            val postParams = "source=$wordSource&target=$wordTarget&text=$text"
            con.doOutput = true
            val wr = DataOutputStream(con.outputStream)
            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode = con.responseCode
            val br: BufferedReader
            br = if (responseCode == 200) { // 정상 호출
                BufferedReader(InputStreamReader(con.inputStream))
            } else {  // 에러 발생
                BufferedReader(InputStreamReader(con.errorStream))
            }
            var inputLine: String?
            val response = StringBuffer()
            while (br.readLine().also { inputLine = it } != null) {
                response.append(inputLine)
            }
            br.close()
            var s = response.toString()
            s = s.split("\"".toRegex()).toTypedArray()[27]
            return s
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return "error"
    }
}