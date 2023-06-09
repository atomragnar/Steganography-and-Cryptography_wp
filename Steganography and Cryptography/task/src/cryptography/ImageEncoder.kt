package cryptography

import java.awt.image.BufferedImage
import java.io.File
import java.security.SecureRandom
import javax.imageio.ImageIO

class ImageEncoder(private val fileName: String, private val image: BufferedImage, private val message: String,
    private val key: String) {

    fun encode() {
        // konverterar meddelandet till bytes och lägger till "end of message indicator" 0, 0, 3

        val messageBytes = xorEncrypt(message.toByteArray(Charsets.UTF_8), key.toByteArray(Charsets.UTF_8))

        if (messageBytes.size * 8 > image.width * image.height) {
            println("The input image is not large enough to hold this message.")
            return
        }

        // Konvertering av bytes till bits för kunna encoda meddelande i bilden.
        val bits = messageBytes.flatMap { byte ->
            (7 downTo 0).map { i -> (byte.toInt() shr i) and 1 }
        }

        var bitIndex = 0
        for (i in 0 until image.height) {
            for (j in 0 until image.width) {
                if (bitIndex >= bits.size) return // Om det inte finns några fler bits att konvertera

                val rgb = image.getRGB(j, i)
                val red = rgb shr 16 and 0xFF
                val green = rgb shr 8 and 0xFF
                var blue = rgb and 0xFF

                // Här ersätter jag lsb av blå med senaste bits i från meddelandet
                blue = (blue and 0b11111110) or bits[bitIndex]

                image.setRGB(j, i, (red shl 16) or (green shl 8) or blue)

                bitIndex++
            }
        }
    }

    private fun xorEncrypt(message: ByteArray, key: ByteArray): ByteArray {
        val result = ByteArray(message.size + 3)
        for (i in message.indices) {
            result[i] = (message[i].toInt() xor key[i % key.size].toInt()).toByte()
        }
        val endIndicatorBytes = endIndicator.toByteArray(Charsets.UTF_8)
        for (i in endIndicatorBytes.indices) {
            result[message.size + i] = endIndicatorBytes[i]
        }
        return result
    }

    fun generatePassword(length: Int): String {
        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
        val secureRandom = SecureRandom()
        val password = StringBuilder(length)
        for (i in 0 until length) {
            password.append(chars[secureRandom.nextInt(chars.length)])
        }
        return password.toString()
    }

    fun save() {
        ImageIO.write(image, "png", File(this.fileName))
    }

    private fun setBits(pixel:Int):Int {
        return pixel or 1
    }



}


