import com.uonagent.cryptography.extensions.toLong
import com.uonagent.cryptography.hash.SHA1
import com.uonagent.cryptography.message.Message
import com.uonagent.cryptography.signature.certificate.Certificate
import com.uonagent.cryptography.signature.rsa.RSASignaturePrivateKey
import com.uonagent.cryptography.symmetrickey.IDEAKey
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream
import java.time.LocalDate

private const val USER_NAME = "default"
private const val PASSWORD = "definitely_safe"

fun main(args: Array<String>) {
    val centerPrivateKey = RSASignaturePrivateKey()
    val centerPublicKey = centerPrivateKey.publicKey
    val f1 = FileOutputStream(File("/Users/uonagent/Desktop/incert/center_private_key"))
    val f2 = FileOutputStream(File("/Users/uonagent/Desktop/incert/center_public_key"))
    val o1 = ObjectOutputStream(f1)
    val o2 = ObjectOutputStream(f2)
    o1.writeObject(centerPrivateKey)
    o2.writeObject(centerPublicKey)
    o1.close()
    o2.close()
    f1.close()
    f2.close()

    val userPrivateKey = RSASignaturePrivateKey()
    val userPublicKey = userPrivateKey.publicKey

    val passHash = SHA1.calculateHash(PASSWORD)
    val seed = passHash.copyOfRange(0, 8).toLong()
    val ideaKey = IDEAKey(seed)

    val encryptedUserPrivateKey = ideaKey.encrypt(Message(userPrivateKey))

    val today = LocalDate.now()
    val expDate = today.plusYears(1)
    val certParams = hashMapOf<String, Any?>(
            "owner" to USER_NAME,
            "centerName" to "test",
            "fromDate" to today,
            "untilDate" to expDate,
            "ownerPublicKey" to userPublicKey
    )

    val cert = Certificate(certParams, centerPrivateKey)

    val f3 = FileOutputStream(File("/Users/uonagent/Desktop/incert/${USER_NAME}_user_private_key"))
    val f4 = FileOutputStream(File("/Users/uonagent/Desktop/incert/${USER_NAME}_cert"))
    val o3 = ObjectOutputStream(f3)
    val o4 = ObjectOutputStream(f4)
    o3.writeObject(encryptedUserPrivateKey)
    o4.writeObject(cert)
    o3.close()
    o4.close()
    f3.close()
    f4.close()
    println("ok")
}