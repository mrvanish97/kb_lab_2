package com.uonagent.insignt.app

import com.uonagent.cryptography.extensions.toLong
import com.uonagent.cryptography.hash.SHA1
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.certificate.Certificate
import com.uonagent.cryptography.signature.rsa.RSASignaturePrivateKey
import com.uonagent.cryptography.signature.rsa.RSASignaturePublicKey
import com.uonagent.cryptography.symmetrickey.IDEAKey
import java.io.*
import java.nio.file.Files
import java.time.LocalDate

fun main(args: Array<String>) {
    val app = App()
    app.doStart()
}

class App {
    fun doStart() {
        val s = readLine()
        when (s) {
            "1" -> sign()
            "2" -> verify()
            else -> doStart()
        }
    }

    private fun sign() {
        val user = readLine()!!
        val password = readLine()!!
        val path = readLine()!!

        val signaturePath = "$path.sgn"
        val certificatePath = "$path.crt"

        val passHash = SHA1.calculateHash(password)
        val seed = passHash.copyOfRange(0, 8).toLong()
        val ideaKey = IDEAKey(seed)

        val fisPrivateKey = FileInputStream(File("/Users/uonagent/Desktop/incert/${user}_user_private_key"))
        val oisPrivateKey = ObjectInputStream(fisPrivateKey)

        val encrPrivateKey = oisPrivateKey.readObject() as Message
        val privateKey = ideaKey.decrypt(encrPrivateKey).getObject() as RSASignaturePrivateKey

        val fileBytes = File(path).readBytes()

        val signature = privateKey.sign(Message(fileBytes))

        val fosSignature = FileOutputStream(File(signaturePath))
        val oosSignature = ObjectOutputStream(fosSignature)
        oosSignature.writeObject(signature)

        val defCert = File("/Users/uonagent/Desktop/incert/${user}_cert")
        val newCert = File(certificatePath)
        Files.copy(defCert.toPath(), newCert.toPath())

        println("done")
    }

    private fun verify() {
        val path = readLine()!!
        val signaturePath = "$path.sgn"
        val certificatePath = "$path.crt"

        val fileBytes = File(path).readBytes()

        val fisSignature = FileInputStream(File(signaturePath))
        val fisCertificate = FileInputStream(File(certificatePath))
        val fisPublicKey = FileInputStream(File("/Users/uonagent/Desktop/incert/center_public_key"))
        val oisSignature = ObjectInputStream(fisSignature)
        val oisCertificate = ObjectInputStream(fisCertificate)
        val oisPublicKey = ObjectInputStream(fisPublicKey)
        val signature = oisSignature.readObject() as Message
        val certificate = oisCertificate.readObject() as Certificate
        val publicKey = oisPublicKey.readObject() as RSASignaturePublicKey
        println(certificate)
        if (certificate.fromDate > LocalDate.now() || certificate.untilDate <= LocalDate.now()) {
            println("Certificate is outdated")
            return
        }
        val b = certificate.verify(Message(fileBytes), signature, publicKey)
        if (b) {
            println("Signature is valid")
        } else {
            println("Signature is invalid")
        }
    }
}