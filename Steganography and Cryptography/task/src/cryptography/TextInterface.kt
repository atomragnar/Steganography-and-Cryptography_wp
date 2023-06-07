package cryptography

import java.io.File
import javax.imageio.ImageIO

const val MAIN_MENU = "Task (hide, show, exit):";
const val INPUT_IMAGE = "Input image file:";
const val OUTPUT_IMAGE = "Output image file:";
const val MESSAGE_TO_HIDE = "Message to hide:";
const val MESSAGE = "Message:";



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
            val imageDecoder = ImageDecoder(bufferedImage)
            println("Input Image: $inputFileName")
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

        try {
            val bufferedImage = ImageIO.read(File(inputFileName))
            val imageEncoder = ImageEncoder(outputFileName, bufferedImage, message)
            imageEncoder.encode()
            imageEncoder.save()
            println("Message saved in $outputFileName image.")
        } catch (e: Exception) {
            println("Can't read input file!")
        }

    }




}