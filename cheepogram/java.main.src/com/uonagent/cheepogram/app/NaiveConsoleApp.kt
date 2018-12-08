/*
package com.uonagent.cheepogram.app

import com.uonagent.cryptography.asymmetriccipher.RSA
import com.uonagent.cryptography.session.Session
import com.uonagent.cryptography.symmetriccipher.IDEACFB
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


private const val PUBLIC_FILE = "/Users/uonagent/Desktop/Cheepogram/public.txt"
private const val ENCRYPTED_DATA_FILE = "/Users/uonagent/Desktop/Cheepogram/data"
private const val SESSION_KEY_FILE = "/Users/uonagent/Desktop/Cheepogram/session.txt"

fun main(args: Array<String>) {
    val app = NaiveConsoleApp()
    app.onStart()
}

class NaiveConsoleApp {

    val id = Random().nextInt()

    private val session = Session(RSA, IDEACFB)
    private lateinit var sessionKey: String

    fun onStart() {
        println("Your ID is $id")
        println("Init new session (1) or connect to existing one (2)?")
        val option = readLine()
        when (option) {
            "1" -> doInit()
            "2" -> doConnect()
            else -> onStart()
        }
        onConnectionDone()
    }

    fun doInit() {
        session.initializeSession()
        sendPublicKey()
        println("Public Key has been published")
        println("Press enter to receive session key")
        readLine()
        receiveSessionKey()
    }

    fun doConnect() {
        val publicKeyString = readFileFromFile(PUBLIC_FILE)
        sessionKey = session.createSessionKey(BigInteger(publicKeyString, 16)).toString(16)
        sendSessionKey()
    }

    fun sendSessionKey() {
        rewriteLineInFile(SESSION_KEY_FILE, sessionKey)
    }

    fun receiveSessionKey() {
        val sessionKeyString = readFileFromFile(SESSION_KEY_FILE)
        session.acceptSessionKey(BigInteger(sessionKeyString, 16))
        createDataFile()
    }

    fun sendPublicKey() {
        rewriteLineInFile(PUBLIC_FILE, session.publicKey.toString(16))
    }

    fun onConnectionDone() {
        commonDialog()
    }

    private fun commonDialog() {
        println("Read (1) or Send (2)?")
        val option = readLine()
        when (option) {
            "1" -> readMessage()
            "2" -> sendMessage()
            else -> onConnectionDone()
        }
    }

    private fun readMessage() {
        val c = Files.readAllBytes(Paths.get(ENCRYPTED_DATA_FILE))
        val message = session.decrypt(c) as String
        println(message)
        commonDialog()
    }

    private fun sendMessage() {
        var m: String? = null
        while (m == null) {
            m = readLine()
        }
        val s = id.toString() + ":\n" + m
        val enc = session.encrypt(s)
        Files.write(Paths.get(ENCRYPTED_DATA_FILE), enc)
        commonDialog()
    }

    private fun createDataFile() {
        rewriteLineInFile(ENCRYPTED_DATA_FILE, "")
    }

    private fun readFileFromFile(path: String): String {
        val b = BufferedReader(FileReader(path))
        return b.readLine()
    }

    private fun rewriteLineInFile(path: String, string: String) {
        val f = File(path)
        val writer = FileWriter(f, false)
        writer.write(string)
        writer.close()
    }
}*/
