package frames;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

public class MainComponent extends JComponent {

    private BufferedImage backgroundImage;
    private BufferedImage llamaImage;
    private BufferedImage rainbowImage;
    private BufferedImage chickenSheet;
    private BufferedImage chickenBlack;
    private BufferedImage chickenDark;
    private BufferedImage chickenLight;

    public MainComponent() {
        setOpaque(true);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/resources/background.jpg"));
            llamaImage = ImageIO.read(getClass().getResourceAsStream("/resources/llama.png"));
            rainbowImage = ImageIO.read(getClass().getResourceAsStream("/resources/rainbow.jpg"));
            chickenSheet = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet.png"));
            chickenBlack = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Black.png"));
            chickenDark = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Dark_Brown.png"));
            chickenLight = ImageIO.read(getClass().getResourceAsStream("/resources/Chicken_Sprite_Sheet_Light_Brown.png"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
        }

        if (llamaImage != null) {
            BufferedImage frame = llamaImage.getSubimage(0, 0, 48, 48);
            g2.drawImage(frame, 20, 20, 48, 48, null);
        }

        if (rainbowImage != null) {
            g2.drawImage(rainbowImage, 100, 20, 24, 6, null);
        }

        if (chickenSheet != null) {
            BufferedImage frame = chickenSheet.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 20, 100, 32, 32, null);
        }

        if (chickenBlack != null) {
            BufferedImage frame = chickenBlack.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 100, 100, 32, 32, null);
        }

        if (chickenDark != null) {
            BufferedImage frame = chickenDark.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 180, 100, 32, 32, null);
        }

        if (chickenLight != null) {
            BufferedImage frame = chickenLight.getSubimage(0, 0, 32, 32);
            g2.drawImage(frame, 260, 100, 32, 32, null);
        }
    }
    
}