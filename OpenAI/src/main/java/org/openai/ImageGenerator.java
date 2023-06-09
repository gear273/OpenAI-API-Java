package org.openai;

import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;

import javax.imageio.ImageIO;
import javax.xml.bind.DatatypeConverter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Scanner;

public class ImageGenerator {
    public static void main(String[] args) throws IOException {
        String token = ApiKey.API_KEY;
        OpenAiService service = new OpenAiService(token, Duration.ofSeconds(60));

        Scanner sc = new Scanner(System.in);
        System.out.print("prompt: ");
        String prompt = sc.nextLine();

        CreateImageRequest request = CreateImageRequest.builder()
                .prompt(prompt)
                .size("256x256")
                .n(3)
                .responseFormat("b64_json")
                .build();

        List<Image> images = service.createImage(request).getData();
        int i = 0;

        for (Image img: images) {
            String data = img.getB64Json();

            byte[] imageBytes = DatatypeConverter.parseBase64Binary(data);
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));

            String fileName = String.join("_", prompt.split(" ")) + "_" + i + ".png";

            File file = new File("src/main/resources/images/" + fileName);
            ImageIO.write(image, "png", file);

            i++;
        }

        System.out.println("Done!");

        service.shutdownExecutor();
    }
}
