package ro.aenigma.models

import org.json.JSONObject

class ExportedContactData (
    val guardHostname: String?,
    val guardAddress: String,
    val publicKey: String) {

    override fun toString(): String {
        val data = JSONObject()

        try {
            data.put("guardHostname", guardHostname)
            data.put("publicKey", publicKey)
            data.put("guardAddress", guardAddress)
        } catch (e: Exception) {
            return ""
        }

        return data.toString()
    }
}
