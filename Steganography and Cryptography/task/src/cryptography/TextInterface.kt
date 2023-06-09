package cryptography

import java.io.File
import javax.imageio.ImageIO


class TextInterface {

    fun start() {

        var input:String? = null;

        while (input != "exit") {

            println(MAIN_MENU)
            input = readln()

            when(input.trim()) {
                "hide" -> hideAction()
                "show" -> showAction()
                "exit" -> println("Bye!")
                else -> println("Wrong task: $input")
            }
        }
    }

    private fun getInput(prompt: String): String {
        println(prompt)
        return readln()
    }

    private fun showAction() {
        val inputFileName: String = getInput(INPUT_IMAGE)

        try {
            val bufferedImage = ImageIO.read(File(inputFileName))
            val key = getInput(PASSWORD)
            val imageDecoder = ImageDecoder(bufferedImage, key)
            val decodedMessage = imageDecoder.decodeToString()
            println(MESSAGE)
            println(decodedMessage)
        } catch (e: Exception) {
            println("Can't read input file!")
        }
    }

    private fun hideAction() {

        val inputFileName: String = getInput(INPUT_IMAGE)
        val outputFileName: String = getInput(OUTPUT_IMAGE)
        val message: String = getInput(MESSAGE_TO_HIDE)
        val key = getInput(PASSWORD)

        try {
            val bufferedImage = ImageIO.read(File(inputFileName))
            val imageEncoder = ImageEncoder(outputFileName, bufferedImage, message, key)
            imageEncoder.encode()
            imageEncoder.save()
            println("Message saved in $outputFileName image.")
        } catch (e: Exception) {
            println("Can't read input file!")
        }

    }




}