package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class ImageDecoder(private val image: BufferedImage) {

    lateinit var messageBytes: ByteArray;

    private fun decode() {

        val bits = mutableListOf<Int>()

        for (i in 0 until image.height) {
            for (j in 0 until image.width) {
                val color = Color(image.getRGB(j, i))
                val blue = color.blue

                val lsbOfBlue = blue and 1

                bits.add(lsbOfBlue)

                val rgb = (color.red shl 16) or (color.green shl 8) or blue
                image.setRGB(j, i, rgb)
            }
        }

        messageBytes = packBitsIntoBytes(bits)

    }

    private fun packBitsIntoBytes(bits: List<Int>): ByteArray {
        val packed = mutableListOf<Byte>()
        for (i in bits.indices step 8) {
            var byte = 0
            for (bitIndex in 0..7) {
                if (i + bitIndex < bits.size) {
                    byte = byte shl 1 or bits[i + bitIndex]
                } else {
                    // Om det är färre än 8 bits kvar, lägger till nollor för att fylla ut.
                    byte = byte shl 1
                }
            }
            packed.add(byte.toByte())
        }
        return packed.toByteArray()
    }

    private fun xorWithKey(message: ByteArray, key: ByteArray): ByteArray {
        val result = ByteArray(message.size)
        for (i in message.indices) {
            result[i] = (message[i].toInt() xor key[i % key.size].toInt()).toByte()
        }
        return result
    }


    fun decodeToString(): String {
        decode()
        val messageWithoutEndIndicator = messageBytes.dropLast(3).toByteArray()
        return messageWithoutEndIndicator.toString(Charsets.UTF_8)
    }


}


